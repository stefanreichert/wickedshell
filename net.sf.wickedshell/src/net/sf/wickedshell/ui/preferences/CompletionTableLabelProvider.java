/*
 * CompletionTableLabelProvider.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.ui.preferences;

import net.sf.wickedshell.ShellPlugin;
import net.sf.wickedshell.domain.completion.ICompletion;
import net.sf.wickedshell.util.ResourceManager;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author Stefan Reichert
 * @since 24.03.2006
 */
public class CompletionTableLabelProvider extends LabelProvider implements ITableLabelProvider {

	/** Column index of the completion. */
	private static final int COMPLETION_COLUMN_INDEX = 0;

	/** Column index of the completion description. */
	private static final int LABEL_COLUMN_INDEX = 1;

	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object,
	 *      int)
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		if(columnIndex == COMPLETION_COLUMN_INDEX) {
			ICompletion completion = (ICompletion) element;
			if(completion.getImagePath() != null && completion.getImagePath().length() > 1) {
				return ResourceManager.getPluginImage(ShellPlugin.getDefault(), completion.getImagePath());
			}
			return ResourceManager.getPluginImage(ShellPlugin.getDefault(), "img/staticCompletion.gif");
		}
		return null;
	}

	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object,
	 *      int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		ICompletion completion = (ICompletion) element;
		switch (columnIndex) {
			case COMPLETION_COLUMN_INDEX:
				return completion.getContent();
			case LABEL_COLUMN_INDEX:
				return completion.getLabel();
			default:
				return "invalid column index";
		}
	}

}
