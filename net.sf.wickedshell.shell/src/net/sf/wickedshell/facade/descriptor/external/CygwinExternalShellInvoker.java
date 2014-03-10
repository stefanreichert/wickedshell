/*
 * CygwinExternalShellInvoker.java
 * 
 * Copyright 2005-2007 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.facade.descriptor.external;

import java.io.IOException;

import net.sf.wickedshell.domain.DomainPlugin;
import net.sf.wickedshell.domain.configuration.IShellDescriptorProperties;
import net.sf.wickedshell.facade.descriptor.IShellDescriptor;
import net.sf.wickedshell.facade.descriptor.external.IExternalShellInvoker;
import net.sf.wickedshell.util.ShellLogger;

/**
 * @author Stefan Reichert
 */
public class CygwinExternalShellInvoker implements IExternalShellInvoker {

	/** The <code>Reader</code> of the shell. */
	private final ShellLogger shellLogger = new ShellLogger(
			CygwinExternalShellInvoker.class);

	/**
	 * @see net.sf.wickedshell.facade.descriptor.external.IExternalShellInvoker#execute(net.sf.wickedshell.facade.descriptor.IShellDescriptor,
	 *      java.lang.String, java.lang.String)
	 */
	public void execute(IShellDescriptor descriptor, String command, String path) {
		IShellDescriptorProperties shellDescriptorProperties = DomainPlugin
				.getDefault().getShellDescriptorProperties(descriptor.getId());
		StringBuffer executionBuffer = new StringBuffer("cmd.exe /C start /D");
		executionBuffer.append(shellDescriptorProperties.getRootDirectory());
		executionBuffer.append(" cmd.exe /C \"");
		executionBuffer.append(shellDescriptorProperties.getRootDirectory());
		executionBuffer.append("\\");
		executionBuffer.append(descriptor.getBinariesDirectory());
		executionBuffer.append("\\");
		executionBuffer.append(descriptor.getExecutable());
		executionBuffer.append("\"");
		try {
			Runtime.getRuntime().exec(executionBuffer.toString());
		}
		catch (IOException exception) {
			shellLogger.error(exception.getMessage(), exception);
		}
	}

}
