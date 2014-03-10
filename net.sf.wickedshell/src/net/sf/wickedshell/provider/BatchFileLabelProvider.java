/*
 * BatchFileLabelProvider.java
 * 
 * Copyright 2003-2004 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.provider;

import net.sf.wickedshell.ShellPlugin;
import net.sf.wickedshell.domain.batch.IBatchFileDescriptor;
import net.sf.wickedshell.util.ResourceManager;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author Stefan Reichert
 * @since 23.05.2005
 */
public class BatchFileLabelProvider extends LabelProvider implements ITableLabelProvider {

	private static final int COLUMN_IMAGE = 0;

	private static final int COLUMN_FILE = 1;

	private static final int COLUMN_PARAMETERS = 2;

	/**
	 * Constructor for BatchFileLabelProvider.
	 */
	public BatchFileLabelProvider() {
		super();
	}

	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object,
	 *      int)
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		switch (columnIndex) {
			case COLUMN_IMAGE:
				return ResourceManager.getPluginImage(ShellPlugin.getDefault(), "img/batch.gif");
			default:
				return null;
		}
	}

	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object,
	 *      int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		IBatchFileDescriptor batchFileDescriptor = (IBatchFileDescriptor) element;
		switch (columnIndex) {
			case COLUMN_FILE:
				return batchFileDescriptor.getFilename();
			case COLUMN_PARAMETERS:
				return batchFileDescriptor.getParameters();
			default:
				return new String();
		}
	}
}
