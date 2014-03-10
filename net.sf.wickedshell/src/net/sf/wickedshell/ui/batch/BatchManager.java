/*
 * BatchManager.java
 * 
 * Copyright 2003-2004 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.ui.batch;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import net.sf.wickedshell.ShellID;
import net.sf.wickedshell.ShellPlugin;
import net.sf.wickedshell.domain.DomainPlugin;
import net.sf.wickedshell.domain.batch.IBatchFileDescriptor;
import net.sf.wickedshell.facade.descriptor.IShellDescriptor;
import net.sf.wickedshell.preferences.PreferenceHelper;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Text;

/**
 * @author Stefan Reichert
 * @since 23.05.2005
 */
public class BatchManager implements ISelectionChangedListener, IPropertyChangeListener {

	private TableViewer tableViewerBatchFiles;

	private Text textBatchFile;

	/**
	 * Constructor for BatchManager.
	 */
	public BatchManager(TableViewer tableViewerBatchFiles, Text textBatchFile) {
		super();
		this.tableViewerBatchFiles = tableViewerBatchFiles;
		this.textBatchFile = textBatchFile;
		ShellPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(this);
	}

	/**
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
		if(!tableViewerBatchFiles.getTable().isDisposed()) {
			String property = event.getProperty();
			if(property.equals(ShellID.SHELL_DESCRIPTOR_ID_STRING_KEY)) {
				refreshBatchView();
			}
		}
	}

	public void activate() {
		tableViewerBatchFiles.setInput(DomainPlugin.getDefault().getBatchFileDescriptors());
		tableViewerBatchFiles.addSelectionChangedListener(this);
	}

	public void deactivate() {
		tableViewerBatchFiles.removeSelectionChangedListener(this);
	}

	@SuppressWarnings("unchecked")
	public void addBatchFileDescriptor(IBatchFileDescriptor batchFileDescriptor) {
		IShellDescriptor descriptor = PreferenceHelper.getActiveShellDescriptor();
		File file = new File(batchFileDescriptor.getFilename());
		if(file.isFile() && descriptor.isAllowedForBatchList(file)) {
			List batchFileDescriptors = DomainPlugin.getDefault().getBatchFileDescriptors();
			batchFileDescriptors.add(batchFileDescriptor);
			tableViewerBatchFiles.setInput(batchFileDescriptors);
			tableViewerBatchFiles.setSelection(new StructuredSelection(batchFileDescriptor));
			tableViewerBatchFiles.getTable().forceFocus();
		}
	}

	public void removeBatchFileDescriptor(IBatchFileDescriptor batchFileDescriptor) {
		List<IBatchFileDescriptor> batchFileDescriptors = DomainPlugin.getDefault().getBatchFileDescriptors();
		batchFileDescriptors.remove(batchFileDescriptor);
		tableViewerBatchFiles.setInput(batchFileDescriptors);
		tableViewerBatchFiles.setSelection(StructuredSelection.EMPTY);
		tableViewerBatchFiles.getTable().forceFocus();
	}

	public void removeAllBatchFileDescriptors() {
		List<IBatchFileDescriptor> batchFileDescriptors = DomainPlugin.getDefault().getBatchFileDescriptors();
		batchFileDescriptors.clear();
		tableViewerBatchFiles.setInput(batchFileDescriptors);
		tableViewerBatchFiles.setSelection(StructuredSelection.EMPTY);
		tableViewerBatchFiles.getTable().forceFocus();
	}

	/**
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		StructuredSelection selection = (StructuredSelection) event.getSelection();
		if(!selection.isEmpty()) {
			IBatchFileDescriptor descriptor = (IBatchFileDescriptor) selection.getFirstElement();
			File selectedFile = new File(descriptor.getFilename());
			try {
				char[] fileContentBuffer = new char[256];
				StringBuffer fileContent = new StringBuffer();
				FileReader fileReader = new FileReader(selectedFile);
				while (fileReader.read(fileContentBuffer) != -1) {
					fileContent.append(fileContentBuffer);
				}
				fileReader.close();
				textBatchFile.setText(fileContent.toString());
			}
			catch (IOException exception) {
				textBatchFile.setText("Error while reading selected file (" + exception.getMessage() + ")!");
			}
		}
		else {
			textBatchFile.setText("");
		}
	}

	public void refreshBatchView() {
		tableViewerBatchFiles.refresh();
	}
}