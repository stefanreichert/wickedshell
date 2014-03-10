/*
 * RunBatchActionDelegate.java
 * 
 * Copyright 2003-2004 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.action.batch;

import java.io.File;

import net.sf.wickedshell.ShellID;
import net.sf.wickedshell.domain.batch.IBatchFileDescriptor;
import net.sf.wickedshell.ui.shell.ShellView;
import net.sf.wickedshell.util.MessageDialogHandler;
import net.sf.wickedshell.util.ShellLogger;
import net.sf.wickedshell.util.ShellViewUtil;
import net.sf.wickedshell.util.ShellViewUtil.IShellViewFilter;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;

/**
 * @author Stefan Reichert
 * @since 03.06.2005
 */
public class RunBatchFileAction implements IViewActionDelegate {

	/** The <code>IViewPart</code>. */
	private IViewPart view;

	/** The selected <code>BatchFileDescriptor</code> to be run in the shell. */
	private IBatchFileDescriptor batchFileDescriptor;

	/** This actions <code>ShellLogger</code>. */
	private static final ShellLogger shellLogger = new ShellLogger(RunBatchFileAction.class);

	/**
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 */
	public void init(IViewPart view) {
		this.view = view;
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		if(batchFileDescriptor != null) {
			File selectedFile = new File(batchFileDescriptor.getFilename());
			ShellView shellView = null;
			if(!ShellViewUtil.isTargetAvailable()) {
				// No ShellView available
				if(MessageDialogHandler.openConfirmationMessage(view.getSite().getShell(), "There is no Shell available, do you want to open a new one?")) {
					try {
						shellView = (ShellView) view.getSite().getPage().showView(ShellID.SHELL_VIEW_ID);
					}
					catch (PartInitException exception) {
						shellLogger.error(exception.getMessage(), exception);
					}
				}
			}
			else {
				shellView = ShellViewUtil.selectTargetFromAvailable("Run Batch File", new IShellViewFilter() {
					/**
					 * @see net.sf.wickedshell.util.ShellViewUtil.IShellViewFilter#isApplicable(net.sf.wickedshell.ui.shell.ShellView)
					 */
					public boolean isApplicable(ShellView shellView) {
						return shellView.getShellViewer().getShellFacade().getShellDescriptor().isAllowedForBatchList(
								new File(batchFileDescriptor.getFilename()));
					}
				}, false);
			}
			if(shellView != null) {
				view.getSite().getPage().activate(shellView);
				StringBuffer commandBuffer = new StringBuffer();
				commandBuffer.append(selectedFile.getAbsolutePath());
				if(batchFileDescriptor.getParameters() != null) {
					commandBuffer.append(" ");
					commandBuffer.append(batchFileDescriptor.getParameters());
				}
				shellView.getShellViewer().getShellFacade().executeCommand(commandBuffer.toString());
			}
			else {
				MessageDialogHandler.openInformationMessage(view.getSite().getShell(),
						"No Shell was selected. Please choose one to run the selected Batch File.");
			}
		}
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		if(selection.isEmpty()) {
			batchFileDescriptor = null;
		}
		else {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Object selectedElement = structuredSelection.getFirstElement();
			if(selectedElement instanceof IBatchFileDescriptor) {
				batchFileDescriptor = (IBatchFileDescriptor) selectedElement;
				action.setEnabled(ShellViewUtil.isTargetForExecutableAvailable((new File(batchFileDescriptor.getFilename()))));
			}
		}
	}

}
