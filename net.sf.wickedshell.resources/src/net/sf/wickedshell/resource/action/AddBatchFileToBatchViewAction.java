/*
 * AddBatchFileToBatchViewAction.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.resource.action;

import net.sf.wickedshell.ShellID;
import net.sf.wickedshell.domain.batch.IBatchFileDescriptor;
import net.sf.wickedshell.facade.descriptor.IShellDescriptor;
import net.sf.wickedshell.preferences.PreferenceHelper;
import net.sf.wickedshell.ui.batch.BatchView;
import net.sf.wickedshell.util.ShellLogger;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * <code>Action</code> that allows adding files that match the active shell
 * specific criteria to the list of batch file.
 * 
 * @author Stefan Reichert
 * @since 07.04.2006
 */
public class AddBatchFileToBatchViewAction implements IObjectActionDelegate {

	/** The <code>Reader</code> of the shell. */
	private final ShellLogger shellLogger = new ShellLogger(
			AddBatchFileToBatchViewAction.class);

	/**
	 * The currently selected <code>IFolder</code>.
	 */
	private IFile currentFile;

	/**
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction,
	 *      org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		try {
			BatchView batchView = (BatchView) PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage().showView(
							ShellID.BATCH_VIEW_ID);
			batchView.getBatchManager().addBatchFileDescriptor(
					IBatchFileDescriptor.Factory.newInstance(currentFile
							.getLocation().toOSString()));
		}
		catch (PartInitException exception) {
			shellLogger.error(exception.getMessage(), exception);
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
			IShellDescriptor descriptor = PreferenceHelper
					.getActiveShellDescriptor();

			boolean validForBatch = descriptor
					.isAllowedForBatchList(currentFile.getRawLocation()
							.toFile());
			action.setEnabled(validFile && validForBatch);
		}
	}
}
