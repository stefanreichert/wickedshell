/*
 * CmommandExternalShellInvoker.java
 * 
 * Copyright 2005-2007 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.facade.descriptor.external;

import java.io.File;
import java.io.IOException;

import net.sf.wickedshell.facade.descriptor.IShellDescriptor;
import net.sf.wickedshell.util.ShellLogger;

/**
 * Please enter the purpose of this class.
 * 
 * @author Stefan Reichert
 */
public class CommandExternalShellInvoker implements IExternalShellInvoker {

	/** The <code>Reader</code> of the shell. */
	private final ShellLogger shellLogger = new ShellLogger(
			CommandExternalShellInvoker.class);

	/**
	 * @see net.sf.wickedshell.facade.descriptor.external.IExternalShellInvoker#execute(net.sf.wickedshell.facade.descriptor.IShellDescriptor,
	 *      java.lang.String, java.lang.String)
	 */
	public void execute(IShellDescriptor descriptor, String command, String path) {
		try {
			Runtime.getRuntime().exec(
					"command.com /C start command.com /K " + command, null,
					new File(path));
		}
		catch (IOException exception) {
			shellLogger.error(exception.getMessage(), exception);
		}
	}
}
