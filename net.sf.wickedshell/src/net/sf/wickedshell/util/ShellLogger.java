/*
 * ShellLogger.java
 * 
 * Copyright 2005-2007 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.wickedshell.ShellPlugin;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;

/**
 * A simple class for logging.
 * 
 * @author Stefan Reichert
 */
public class ShellLogger {

	/** Log level program argument. */
	private static final String LOG_LEVEL_PROGRAMM_ARGUMENT = "logLevel";

	/** Log-Mode DEBUG. */
	public static final int DEBUG = 0;

	/** Log-Mode DEBUG. */
	public static final int ERROR = 1;

	/** Log-Mode names as <code>Map</code>. */
	private static final Map<Integer, String> logModeNameMap = createLogModeNameMap();

	/**
	 * s * Create the <code>Map</code> of Log-Mode names.
	 */
	@SuppressWarnings("unchecked")
	private static Map<Integer, String> createLogModeNameMap() {
		Map logModeNameMap = new HashMap();
		logModeNameMap.put(new Integer(DEBUG), "DEBUG");
		logModeNameMap.put(new Integer(ERROR), "ERROR");
		return logModeNameMap;
	}

	/**
	 * The <code>Class</code> which is the source of the logs.
	 */
	private Class<?> logSource;

	/**
	 * The log mode.
	 */
	private static final int logMode = Integer.getInteger(ShellLogger.LOG_LEVEL_PROGRAMM_ARGUMENT, ERROR).intValue();

	/**
	 * The Constructor of the <code>ShellLogger</code>.
	 * 
	 * @param logSource
	 *            The <code>Class</code> which is the source of the logs
	 */
	public ShellLogger(Class<?> logSource) {
		super();
		this.logSource = logSource;
	}

	/**
	 * Debugs the given message
	 */
	public void debug(String message) {
		handleMessage(message, DEBUG);
	}

	/**
	 * Debugs the given message
	 */
	public void error(String message, Throwable throwable) {
		error(message, throwable, false);
	}

	/**
	 * Debugs the given message
	 */
	public void error(String message, Throwable throwable, boolean openErrorDialog) {
		handleMessage(message, ERROR, throwable, openErrorDialog);
		ShellPlugin.getDefault().getErrorLog().log(message, throwable);
	}

	/**
	 * Writes the message to the console if applicable.
	 */
	private void handleMessage(String message, int mode) {
		handleMessage(message, mode, null, false);
	}

	/**
	 * Writes the message to the console if applicable.
	 */
	private void handleMessage(String message, int mode, Throwable throwable, boolean openErrorDialog) {
		Assert.isTrue(logModeNameMap.containsKey(new Integer(mode)));
		if(mode >= logMode) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(new SimpleDateFormat("dd. MMM yyyy - HH:mm:ss").format(new Date()));
			buffer.append(" ");
			buffer.append(logModeNameMap.get(new Integer(mode)));
			buffer.append(" [");
			buffer.append(logSource.getName());
			buffer.append("]: ");
			buffer.append(message);
			int status = IStatus.INFO;
			switch (mode) {
				case ERROR:
					status = IStatus.ERROR;
					break;
			}
			IStatus messageStatus = new Status(status, ShellPlugin.ID, IStatus.OK, message, throwable);
			ShellPlugin.getDefault().getLog().log(messageStatus);
			if(openErrorDialog) {
				ErrorDialog.openError(null, "Wicked Shell - Error", message, messageStatus);
			}
		}
	}
}
