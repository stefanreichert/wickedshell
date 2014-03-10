/*
 * ShellActivationException.java
 * 
 * Copyright 2005-2007 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.facade;

/**
 * Exception is thrown on error on shell activation.
 * 
 * @author Stefan Reichert
 */
public class ShellActivationException extends Exception {

	/** The serial version UID. */
	private static final long serialVersionUID = 3510427036948192061L;

	/**
	 * @param message
	 *            The message
	 */
	public ShellActivationException(Throwable throwable) {
		super(throwable);
	}
}
