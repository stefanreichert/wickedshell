/*
 * OpenShellActionDelegate.java
 * 
 * Copyright 2003-2004 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.action.batch;

import net.sf.wickedshell.ShellID;
import net.sf.wickedshell.util.ShellLogger;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;

/**
 * @author Stefan Reichert
 * @since 03.06.2005
 */
public class ShowShellAction implements IViewActionDelegate,
		IObjectActionDelegate {

	/** The <code>Reader</code> of the shell. */
	private final ShellLogger shellLogger = new ShellLogger(
			ShowShellAction.class);

	private IWorkbenchPart targetPart;

	/**
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 */
	public void init(IViewPart view) {
		this.targetPart = view;
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.targetPart = targetPart;
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {

	}

	public void run(IAction action) {
		try {
			targetPart.getSite().getPage().showView(ShellID.SHELL_VIEW_ID);
		}
		catch (PartInitException exception) {
			shellLogger.error(exception.getMessage(), exception);
		}
	}

}
