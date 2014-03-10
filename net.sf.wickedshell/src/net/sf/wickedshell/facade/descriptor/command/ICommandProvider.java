/*
 * ICommandProvider.java
 * 
 * Copyright 2005-2007 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.facade.descriptor.command;

import java.io.File;

import net.sf.wickedshell.facade.descriptor.IShellDescriptor;

/**
 * Provides command for different purposes.
 * 
 * @author Stefan Reichert
 */
public interface ICommandProvider {

	/**
	 * Gets the command for changing to the given directory
	 * 
	 * @param shellDescriptor
	 *            The <code>IShellDescriptor</code> of the underlaying shell
	 * @param targetDirectory
	 *            The target directory
	 * @return the change directory command
	 */
	String getChangeDirectoryCommand(IShellDescriptor shellDescriptor,
			File targetDirectory);

}
