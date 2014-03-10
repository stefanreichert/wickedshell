/*
 * MessageDialogHandler.java
 * 
 * Copyright 2005-2007 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.util;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * Handles displaying messages.
 * 
 * @author Stefan Reichert
 */
public class MessageDialogHandler {

	/**
	 * Opens an <code>MessageDialog</code> displaying an information.
	 * 
	 * @param parentShell
	 *            The parent <code>Shell</code>
	 * @param message
	 *            The message
	 */
	public static final void openInformationMessage(Shell parentShell, String message) {
		MessageDialog.openInformation(parentShell, "Wicked Shell Information", message);

	}

	/**
	 * Opens an <code>MessageDialog</code> displaying a warning.
	 * 
	 * @param parentShell
	 *            The parent <code>Shell</code>
	 * @param message
	 *            The message
	 */
	public static final void openWarningMessage(Shell parentShell, String message) {
		MessageDialog.openInformation(parentShell, "Wicked Shell Warning", message);

	}

	/**
	 * Opens an <code>MessageDialog</code> displaying a confirmation.
	 * 
	 * @param parentShell
	 *            The parent <code>Shell</code>
	 * @param message
	 *            The message
	 * @return the result of the <code>MessageDialog</code>
	 */
	public static final boolean openConfirmationMessage(Shell parentShell, String message) {
		return MessageDialog.openConfirm(parentShell, "Wicked Shell Confirmation", message);
	}

	/**
	 * Constrcutor for <code>MessageDialogHandler</code>.
	 */
	private MessageDialogHandler() {
		super();
		// private constructor to avoid instantiation
	}
}
