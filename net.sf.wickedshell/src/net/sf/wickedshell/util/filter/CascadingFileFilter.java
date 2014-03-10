/*
 * CascadingFileFilter.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.util.filter;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author Stefan Reichert
 * @since 23.06.2006
 */
public class CascadingFileFilter implements FilenameFilter {

	/** The prefix for the filtered <i>folders</i>. */
	private String prefix;

	/**
	 * Constructor for CascadingFileFilter.
	 */
	public CascadingFileFilter(String prefix) {
		super();
		this.prefix = prefix;
	}

	/**
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	public boolean accept(File dir, String name) {
		File file = new File(dir.getAbsolutePath() + File.separatorChar + name);
		return file.exists() && name.toLowerCase().startsWith(prefix.toLowerCase());
	}

}
