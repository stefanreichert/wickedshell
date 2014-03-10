/*
 * RunBatchFileInShellAction.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.resource.action;

import net.sf.wickedshell.ui.shell.ShellView;
import net.sf.wickedshell.util.MessageDialogHandler;
import net.sf.wickedshell.util.ShellViewUtil;
import net.sf.wickedshell.util.ShellViewUtil.IShellViewFilter;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class RunBatchFileInShellAction implements IObjectActionDelegate {

	/** The currently selected <code>IFolder</code>. */
	private IFile currentFile;

	/** The <code>IWorkbenchPart</code>. */
	private IWorkbenchPart targetPart;

	/**
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction,
	 *      org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.targetPart = targetPart;
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		ShellView shellView = ShellViewUtil.selectTargetFromAvailable("Run Batch File", new IShellViewFilter() {
			public boolean isApplicable(ShellView shellView) {
				return shellView.getShellViewer().getShellFacade().getShellDescriptor().isExecutable(currentFile.getRawLocation().toFile());
			}
		}, false);
		if(shellView != null) {
			targetPart.getSite().getPage().activate(shellView);
			shellView.getShellViewer().getShellFacade().executeCommand(currentFile.getRawLocation().toOSString());
		}
		else {
			MessageDialogHandler.openInformationMessage(targetPart.getSite().getShell(),
					"The Shell is not open or no Shell selected. Please open or choose one to run the selected Batch File.");
		}
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		if(!structuredSelection.isEmpty()) {
			currentFile = (IFile) structuredSelection.getFirstElement();
			boolean validFile = currentFile.exists();
			action.setEnabled(validFile && ShellViewUtil.isTargetForExecutableAvailable(currentFile.getRawLocation().toFile()));
		}
	}
}
