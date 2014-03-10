/*
 * IShellPrinter.java
 * 
 * Copyright 2003-2004 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.listener;

/**
 * @author Stefan Reichert
 * @since 13.05.2005
 */
public interface IShellOutputListener {

	/**
	 * Handles output of a shell.
	 * 
	 * @param output
	 *        The output of the underlaying shell
	 */
	void handleShellOutput(String output);

	/**
	 * Handles error output of a shell.
	 * 
	 * @param errorOutput
	 *        The error output of the underlaying shell
	 */
	void handleShellErrorOutput(String errorOutput);
}
