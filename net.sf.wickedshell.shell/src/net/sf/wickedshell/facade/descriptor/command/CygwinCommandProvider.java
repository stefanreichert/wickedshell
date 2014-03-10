/*
 * CygwinCommandProvider.java
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
import net.sf.wickedshell.facade.descriptor.command.ICommandProvider;

/**
 * @author Stefan Reichert
 */
public class CygwinCommandProvider implements ICommandProvider {

	/**
	 * @see net.sf.wickedshell.facade.descriptor.command.ICommandProvider#getChangeDirectoryCommand(net.sf.wickedshell.facade.descriptor.IShellDescriptor,
	 *      java.io.File)
	 */
	public String getChangeDirectoryCommand(IShellDescriptor shellDescriptor,
			File targetDirectory) {
		String absolutePathString = targetDirectory.getAbsolutePath();
		StringBuffer commandBuffer = new StringBuffer();
		// Change to the correct directory
		commandBuffer.append("cd ");
		commandBuffer.append(shellDescriptor.getPathSeparator());
		commandBuffer.append("cygdrive");
		commandBuffer.append(shellDescriptor.getPathSeparator());
		commandBuffer.append(absolutePathString.substring(0, 1));
		commandBuffer.append(absolutePathString.substring(2).replace(
				File.separatorChar, shellDescriptor.getPathSeparator()));
		return commandBuffer.toString();
	}

}
