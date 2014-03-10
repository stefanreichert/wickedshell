/*
 * ChangeToFolderInShellAction.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.jdt.action;

import java.io.File;

import net.sf.wickedshell.ShellID;
import net.sf.wickedshell.facade.descriptor.IShellDescriptor;
import net.sf.wickedshell.facade.descriptor.command.ICommandProvider;
import net.sf.wickedshell.ui.shell.ShellView;
import net.sf.wickedshell.util.MessageDialogHandler;
import net.sf.wickedshell.util.ShellLogger;
import net.sf.wickedshell.util.ShellViewUtil;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;

/**
 * @author Stefan Reichert
 * @since 07.04.2006
 */
public class ChangeToFolderInShellAction implements IObjectActionDelegate {

	/** The currently selected <code>IResource</code>. */
	private IResource currentResource;

	/** The <code>IWorkbenchPart</code>. */
	private IWorkbenchPart targetPart;

	/** This actions <code>ShellLogger</code>. */
	private static final ShellLogger shellLogger = new ShellLogger(ChangeToFolderInShellAction.class);

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
		ShellView shellView = null;
		if(!ShellViewUtil.isTargetAvailable()) {
			// No ShellView available
			if(MessageDialogHandler.openConfirmationMessage(targetPart.getSite().getShell(), "There is no Shell available, do you want to open a new one?")) {
				try {
					shellView = (ShellView) targetPart.getSite().getPage().showView(ShellID.SHELL_VIEW_ID);
				}
				catch (PartInitException exception) {
					shellLogger.error(exception.getMessage(), exception);
				}
			}
		}
		else {
			shellView = ShellViewUtil.selectTargetFromAvailable("Change To Folder", true);
		}
		if(shellView != null) {
			IShellDescriptor shellDescriptor = shellView.getShellViewer().getShellFacade().getShellDescriptor();
			ICommandProvider commandProvider = shellDescriptor.getCommandProvider();
			File file;
			if(currentResource.getLocation().toFile().isDirectory()) {
				file = new File(currentResource.getLocation().toOSString());
			}
			else {
				file = new File(currentResource.getLocation().toOSString()).getParentFile();
			}
			String changeDirectoryCommand = commandProvider.getChangeDirectoryCommand(shellDescriptor, file);
			shellView.getShellViewer().getShellFacade().executeCommand(changeDirectoryCommand);
			shellView.setFocus();
		}
		else {
			MessageDialogHandler.openInformationMessage(targetPart.getSite().getShell(),
					"No Shell was selected. Please choose one to change to the selected folder.");
		}
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	@SuppressWarnings("null")
	public void selectionChanged(IAction action, ISelection selection) {
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		if(!structuredSelection.isEmpty()) {
			Object selectedElement = structuredSelection.getFirstElement();
			if(selectedElement instanceof IJavaElement) {
				IJavaElement currentJavaElement = (IJavaElement) selectedElement;
				if(currentJavaElement != null) {
					currentResource = currentJavaElement.getResource();
				}
				else {
					currentResource = null;
				}
				action.setEnabled(currentResource != null && currentResource.exists());
			}
			else {
				action.setEnabled(false);
			}
		}
	}
}
