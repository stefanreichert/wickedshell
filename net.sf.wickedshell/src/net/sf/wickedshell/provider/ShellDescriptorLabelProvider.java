/*
 * ShellDescriptorLabelProvider.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.provider;

import net.sf.wickedshell.facade.descriptor.IShellDescriptor;

import org.eclipse.jface.viewers.LabelProvider;

/**
 * @author Stefan Reichert
 * @since 06.05.2006
 */
public class ShellDescriptorLabelProvider extends LabelProvider {

	/**
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object element) {
		IShellDescriptor descriptor = (IShellDescriptor) element;
		StringBuffer buffer = new StringBuffer();
		buffer.append(descriptor.getName());
		buffer.append(" [");
		buffer.append(descriptor.getExecutable());
		buffer.append("]");
		return buffer.toString();
	}
}
