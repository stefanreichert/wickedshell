/*
 * IShellListener.java
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
 * @author Stefan Reichert
 * @since 18.10.2006
 */
public interface IShellListener {

	/**
	 * Handles the execution of a command within a shell.
	 * 
	 * @param command
	 *        The command executed in the underlaying shell
	 */
	void handleCommandExecution(String command);

	/**
	 * Handles the activation of a shell.
	 */
	void handleActivation();

	/**
	 * Handles the restart of a shell.
	 */
	void handleRestart();

	/**
	 * Handles the deactivation of a shell.
	 */
	void handleDeactivation();
}
