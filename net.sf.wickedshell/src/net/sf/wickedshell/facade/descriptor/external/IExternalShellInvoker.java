/*
 * IExternalShellInvoker.java
 * 
 * Copyright 2005-2007 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.facade.descriptor.external;

import net.sf.wickedshell.facade.descriptor.IShellDescriptor;

/**
 * This interface enables the invocation of a command within a given path.
 * 
 * @author Stefan Reichert
 */
public interface IExternalShellInvoker {

	/**
	 * Executes the given command within the given path.
	 * 
	 * @param command
	 *            The command to execute
	 * @param path
	 *            The base path
	 */
	void execute(IShellDescriptor descriptor, String command, String path);

}
