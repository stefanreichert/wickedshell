/*
 * ExecutableFileFilter.java
 * 
 * Copyright 2003-2004 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.util.filter;

import java.io.File;
import java.io.FilenameFilter;

import net.sf.wickedshell.facade.descriptor.IShellDescriptor;

/**
 * @author Stefan Reichert
 * @since 14.05.2005
 */
public class ExecutableFileFilter implements FilenameFilter {

	/** Flag whether to accept directories. */
	private boolean acceptDirectories;
	/** The active <code>IShellDescriptor</code>. */
	private IShellDescriptor activeShell;

	/**
	 * Constructor for ExecutableFileFilter.
	 */
	public ExecutableFileFilter(IShellDescriptor activeShell, boolean acceptDirectories) {
		super();
		this.activeShell = activeShell;
		this.acceptDirectories = acceptDirectories;
	}

	/**
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	public boolean accept(File dir, String name) {
		File file = new File(dir.getAbsolutePath() + File.separatorChar + name);
		return (acceptDirectories && file.isDirectory()) || activeShell.isExecutable(file);
	}

}
