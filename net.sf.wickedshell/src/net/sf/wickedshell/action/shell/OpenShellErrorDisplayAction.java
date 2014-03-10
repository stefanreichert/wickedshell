/*
 * OpenShellErrorDisplayAction.java
 * 
 * Copyright 2003-2004 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.action.shell;

import net.sf.wickedshell.ShellPlugin;
import net.sf.wickedshell.ui.shell.ShellErrorDisplay;
import net.sf.wickedshell.util.ResourceManager;

import org.eclipse.jface.action.Action;

/**
 * @author Stefan Reichert
 * @since 22.12.2005
 */
public class OpenShellErrorDisplayAction extends Action {

	/** The <code>ShellErrorDisplay</code>. */
	private ShellErrorDisplay shellErrorDisplay;

	/**
	 * Constructor for ShowShellAction.
	 */
	public OpenShellErrorDisplayAction(ShellErrorDisplay shellErrorDisplay) {
		super("Open Shell Error Display");
		this.shellErrorDisplay = shellErrorDisplay;
		this.setImageDescriptor(ResourceManager.getPluginImageDescriptor(ShellPlugin.getDefault(),
				"img/shellErrorDisplay.gif"));
		this.setToolTipText("Opens the Shell Error Display");
	}

	/**
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		shellErrorDisplay.open();
	}
}
