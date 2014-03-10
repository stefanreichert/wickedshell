/*
 * ICommandHistoryHandler.java
 * 
 * Copyright 2005-2007 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.ui.shell.viewer.adapter;

import net.sf.wickedshell.facade.history.ICommandHistory;

/**
 * Handles events around <code>ICommandHistory</code>.
 * 
 * @author Stefan Reichert
 */
public interface ICommandHistoryHandler {

	/**
	 * Handles the request of a command from the <code>ICommandHistory</code>.
	 * 
	 * @param commandHistory
	 *            The source <code>ICommandHistory</code>
	 * @param historyIndex
	 *            The index of the requested command
	 */
	void handleCommandRequest(ICommandHistory commandHistory, int historyIndex);

}
