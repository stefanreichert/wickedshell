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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import net.sf.wickedshell.facade.IShellFacade;
import net.sf.wickedshell.facade.history.ICommandHistory;
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
public class ExportCommandHistoryAction implements IViewActionDelegate {

	/** The <code>Reader</code> of the shell. */
	private final ShellLogger shellLogger = new ShellLogger(ExportCommandHistoryAction.class);

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
		fileDialog.setText("Export command history");
		fileDialog.setFilterExtensions(new String[] { "*.txt" });
		fileDialog.setFilterNames(new String[] { "Textfiles (*.txt)" });
		fileDialog.setFileName("commandhistory.txt");
		String commandhistoryFileName = fileDialog.open();
		if(commandhistoryFileName != null) {
			File commandhistoryFile = new File(commandhistoryFileName);
			if(commandhistoryFile.exists()) {
				if(MessageDialogHandler
						.openConfirmationMessage(
								viewPart.getSite().getShell(),
								"The file \""
										+ commandhistoryFileName
										+ "\" already exists and will be overwritten. Do you want to proceed ?")) {
					commandhistoryFile.delete();
				}
				else {
					return;
				}
			}
			try {
				commandhistoryFile.createNewFile();
				BufferedWriter fileWriter = new BufferedWriter(new FileWriter(commandhistoryFile));
				ShellView shellView = (ShellView) viewPart;
				IShellFacade shellFacade = shellView.getShellViewer().getShellFacade();
				ICommandHistory commandHistory = shellFacade.getCommandHistory();
				for (int index = 0; index < commandHistory.getSize(shellFacade.getShellDescriptor()
						.getId()); index++) {
					String command = commandHistory.getCommand(index, shellFacade
							.getShellDescriptor().getId());
					fileWriter.write(command);
					fileWriter.newLine();
				}
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
