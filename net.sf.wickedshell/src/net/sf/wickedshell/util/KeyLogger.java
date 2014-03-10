/*
 * KeyLogger.java
 * 
 * Copyright 2005-2007 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.util;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;

/**
 * Please enter the purpose of this class.
 * 
 * @author Stefan Reichert
 */
public class KeyLogger implements KeyListener {

	/** The <code>ShellLogger</code> of the <code>KeyLogger</code>. */
	private static final ShellLogger logger = new ShellLogger(KeyLogger.class);

	/** The <i>source</i> of the keystrokes. */
	private String source;

	/**
	 * Constructor for <code>KeyLogger</code>.
	 * 
	 * @param source
	 *            The <i>source</i> of the keystrokes
	 */
	public KeyLogger(String source) {
		super();
		this.source = source;
	}

	/**
	 * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
	 */
	public void keyPressed(KeyEvent event) {
		logger.debug("Source [" + source + "] registered key [" + event.character + "] (keycode ["
				+ event.keyCode + "]) pressed");

	}

	/**
	 * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
	}

}
