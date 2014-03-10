/*
 * ShellOutputAdapter.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.listener;

/**
 * Default implementation of <code>IShellOutputListener</code>.
 * 
 * @author Stefan Reichert
 * @since 17.10.2006
 */
public class ShellOutputAdapter implements IShellOutputListener {

	/**
	 * @see net.sf.wickedshell.listener.IShellOutputListener#handleShellErrorOutput(java.lang.String)
	 */
	public void handleShellErrorOutput(String errorOut) {
		// Subclasses may overwrite
	}

	/**
	 * @see net.sf.wickedshell.listener.IShellOutputListener#handleShellOutput(java.lang.String)
	 */
	public void handleShellOutput(String output) {
		// Subclasses may overwrite
	}

}
