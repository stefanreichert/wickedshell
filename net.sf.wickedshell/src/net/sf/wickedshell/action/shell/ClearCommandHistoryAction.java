/*
 * ClearCommandHistoryAction.java
 * 
 * Copyright 2003-2004 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.action.shell;

import net.sf.wickedshell.facade.IShellFacade;
import net.sf.wickedshell.ui.shell.ShellView;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * @author Stefan Reichert
 * @since 03.06.2005
 */
public class ClearCommandHistoryAction implements IViewActionDelegate {

	private ShellView view;

	/**
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 */
	public void init(IViewPart view) {
		this.view = (ShellView) view;
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {

	}

	public void run(IAction action) {
		IShellFacade shellFacade = view.getShellViewer().getShellFacade();
		shellFacade.getCommandHistory().clear(shellFacade.getShellDescriptor().getId());
	}

}
