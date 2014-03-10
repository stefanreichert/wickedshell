/*
 * ShellAdapter.java
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
 * Default implementation of <code>IShellListener</code>.
 * 
 * @author Stefan Reichert
 * @since 26.10.2006
 */
public class ShellAdapter implements IShellListener {

	/**
	 * @see net.sf.wickedshell.listener.IShellListener#handleActivation()
	 */
	public void handleActivation() {
		// Subclasses may overwrite
	}

	/**
	 * @see net.sf.wickedshell.listener.IShellListener#handleCommandExecution(java.lang.String)
	 */
	public void handleCommandExecution(String command) {
		// Subclasses may overwrite
	}

	/**
	 * @see net.sf.wickedshell.listener.IShellListener#handleDeactivation()
	 */
	public void handleDeactivation() {
		// Subclasses may overwrite
	}

	/**
	 * @see net.sf.wickedshell.listener.IShellListener#handleRestart()
	 */
	public void handleRestart() {
		// Subclasses may overwrite
	}

}
