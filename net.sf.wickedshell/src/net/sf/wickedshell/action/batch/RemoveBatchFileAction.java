/*
 * RemoveBatchFileAction.java
 * 
 * Copyright 2003-2004 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.action.batch;

import net.sf.wickedshell.domain.batch.IBatchFileDescriptor;
import net.sf.wickedshell.ui.batch.BatchView;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * <code>Action</code> that removes the currently selected entry from the list of batch files.
 * 
 * @author Stefan Reichert
 * @since 26.05.2005
 */
public class RemoveBatchFileAction implements IViewActionDelegate {

	/** The <code>IViewPart</code>. */
	private IViewPart view;
	/** The selected <code>BatchFileDescriptor</code> to be removed. */
	private IBatchFileDescriptor batchFileDescriptor;

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
		if (batchFileDescriptor != null) {
			BatchView batchView = (BatchView) view;
			batchView.getBatchManager().removeBatchFileDescriptor(batchFileDescriptor);
		}
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection.isEmpty()) {
			batchFileDescriptor = null;
		}
		else {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Object selectedElement = structuredSelection.getFirstElement();
			if (selectedElement instanceof IBatchFileDescriptor) {
				batchFileDescriptor = (IBatchFileDescriptor) selectedElement;
			}
		}
	}

}
