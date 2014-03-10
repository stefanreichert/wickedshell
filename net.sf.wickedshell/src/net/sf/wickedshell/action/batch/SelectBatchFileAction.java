/*
 * SelectBatchFileAction.java
 * 
 * Copyright 2003-2004 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.action.batch;

import java.util.List;

import net.sf.wickedshell.domain.batch.IBatchFileDescriptor;
import net.sf.wickedshell.facade.descriptor.executableFile.IExecutableFile;
import net.sf.wickedshell.ui.batch.BatchView;
import net.sf.wickedshell.util.ShellViewUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * @author Stefan Reichert
 * @since 26.05.2005
 */
public class SelectBatchFileAction implements IViewActionDelegate {

	/** The <code>IViewPart</code>. */
	private IViewPart view;

	/**
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 */
	public void init(IViewPart view) {
		this.view = view;
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	@SuppressWarnings("unchecked")
	public void run(IAction action) {
		FileDialog fileDialog = new FileDialog(view.getSite().getShell(), SWT.OPEN);
		fileDialog.setText("Select existing Batch File for Batch View");

		List executableFiles = ShellViewUtil.getTargetableExecutableFiles();
		String[] batchFileExtensions = (String[]) CollectionUtils.collect(executableFiles, new Transformer() {
			/**
			 * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
			 */
			public Object transform(Object object) {
				IExecutableFile executableFile = (IExecutableFile) object;
				StringBuffer buffer = new StringBuffer();
				buffer.append("*");
				buffer.append(executableFile.getExtension());
				return buffer.toString();
			}
		}).toArray(new String[0]);

		String[] batchFileDescriptions = (String[]) CollectionUtils.collect(executableFiles, new Transformer() {
			public Object transform(Object object) {
				IExecutableFile executableFile = (IExecutableFile) object;
				StringBuffer buffer = new StringBuffer();
				buffer.append(executableFile.getDescription());
				buffer.append(" (*");
				buffer.append(executableFile.getExtension());
				buffer.append(")");
				return buffer.toString();
			}
		}).toArray(new String[0]);

		fileDialog.setFilterExtensions(batchFileExtensions);
		fileDialog.setFilterNames(batchFileDescriptions);

		String selectedBatch = fileDialog.open();
		if(selectedBatch != null) {
			BatchView batchView = (BatchView) view;
			batchView.getBatchManager().addBatchFileDescriptor(IBatchFileDescriptor.Factory.newInstance(selectedBatch));
		}
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}
}