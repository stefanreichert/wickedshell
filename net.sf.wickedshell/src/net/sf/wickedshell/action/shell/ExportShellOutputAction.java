/*
 * ExportShellOutputAction.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.action.shell;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import net.sf.wickedshell.ui.shell.ShellView;
import net.sf.wickedshell.util.MessageDialogHandler;
import net.sf.wickedshell.util.ShellLogger;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * @author Stefan Reichert
 * @since 24.10.2006
 */
public class ExportShellOutputAction implements IViewActionDelegate {

	/** The <code>Reader</code> of the shell. */
	private final ShellLogger shellLogger = new ShellLogger(ExportShellOutputAction.class);

	/**
	 * The target <code>IViewPart</code>
	 */
	IViewPart viewPart;

	/**
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 */
	public void init(IViewPart view) {
		this.viewPart = view;
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		FileDialog fileDialog = new FileDialog(viewPart.getSite().getShell());
		fileDialog.setText("Export Shell Output");
		fileDialog.setFilterExtensions(new String[] { "*.txt" });
		fileDialog.setFilterNames(new String[] { "Textfiles (*.txt)" });
		fileDialog.setFileName("shelloutput.txt");
		String shelloutputFileName = fileDialog.open();
		if(shelloutputFileName != null) {
			File shelloutputFile = new File(shelloutputFileName);
			if(shelloutputFile.exists()) {
				if(MessageDialogHandler
						.openConfirmationMessage(
								viewPart.getSite().getShell(),
								"The file \""
										+ shelloutputFileName
										+ "\" already exists and will be overwritten. Do you want to proceed ?")) {
					shelloutputFile.delete();
				}
				else {
					return;
				}
			}
			try {
				shelloutputFile.createNewFile();
				FileWriter fileWriter = new FileWriter(shelloutputFile);
				ShellView shellView = (ShellView) viewPart;
				fileWriter.write(shellView.getShellViewer().getViewerContent());
				fileWriter.flush();
				fileWriter.close();
			}
			catch (IOException exception) {
				shellLogger.error(exception.getMessage(), exception);
			}
		}
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
