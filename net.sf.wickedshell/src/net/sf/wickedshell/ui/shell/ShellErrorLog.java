/*
 * ShellErrorLog.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.ui.shell;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.wickedshell.listener.IShellOutputListener;

import org.eclipse.swt.widgets.Text;

/**
 * @author Stefan Reichert
 * @since 15.11.2006
 */
public class ShellErrorLog implements IShellOutputListener {

	/** The log containing all errors. */
	private StringBuffer errorLog;

	/**
	 * Constructor for ShellErrorLog.
	 */
	public ShellErrorLog() {
		super();
		errorLog = new StringBuffer();
	}

	/**
	 * @see net.sf.wickedshell.listener.IShellOutputListener#handleShellErrorOutput(java.lang.String)
	 */
	public void handleShellErrorOutput(String errorOutput) {
		log(errorOutput);
	}

	/**
	 * @see net.sf.wickedshell.listener.IShellOutputListener#handleShellOutput(java.lang.String)
	 */
	public void handleShellOutput(String output) {
	}

	/**
	 * Logs the given entry.
	 * 
	 * @param errorLogEntry
	 *            The entry to log as <code>Exception</code>
	 */
	public void log(String message, Throwable errorLogEntry) {
		errorLog.append(Text.DELIMITER);
		errorLog.append(new SimpleDateFormat("dd MMM yyyy HH:mm:ss")
				.format(new Date()));
		errorLog.append(" # [");
		errorLog.append(errorLogEntry.getClass().getName());
		errorLog.append(": ");
		errorLog.append(message);
		errorLog.append(" - ");
		errorLog.append(errorLogEntry.getMessage());
		errorLog.append("]");
		errorLog.append(Text.DELIMITER);
		StringWriter stringWriter = new StringWriter();
		errorLogEntry.printStackTrace(new PrintWriter(stringWriter));
		errorLog.append(stringWriter.getBuffer());
	}

	/**
	 * Logs the given entry.
	 * 
	 * @param errorLogEntry
	 *            The entry to log as <code>String</code>
	 */
	private void log(String errorLogEntry) {
		errorLog.append(Text.DELIMITER);
		errorLog.append(new SimpleDateFormat("dd MMM yyyy HH:mm:ss")
				.format(new Date()));
		errorLog.append(" # ");
		errorLog.append(errorLogEntry);
	}

	/**
	 * Return the log as <code>String</code>.
	 * 
	 * @return the log
	 */
	public String getLog() {
		return errorLog.toString();
	}

}
