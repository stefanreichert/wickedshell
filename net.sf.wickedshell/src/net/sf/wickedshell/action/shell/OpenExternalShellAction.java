/*
 * OpenExternalShellAction.java
 * 
 * Copyright 2005-2007 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.action.shell;

import net.sf.wickedshell.facade.IShellFacade;
import net.sf.wickedshell.facade.descriptor.external.IExternalShellInvoker;
import net.sf.wickedshell.ui.shell.ShellView;
import net.sf.wickedshell.util.MessageDialogHandler;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * Opens an external shell within the current path.
 * 
 * @author Stefan Reichert
 */
public class OpenExternalShellAction implements IViewActionDelegate {

	private IViewPart view;

	/**
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 */
	public void init(IViewPart view) {
		this.view = view;
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		ShellView shellView = (ShellView) view;
		IShellFacade shellFacade = shellView.getShellViewer().getShellFacade();
		IExternalShellInvoker externalShellInvoker = shellFacade.getShellDescriptor()
				.getExternalShellInvoker();
		action.setEnabled(externalShellInvoker != null);
	}

	public void run(IAction action) {
		if(view instanceof ShellView) {
			ShellView shellView = (ShellView) view;
			IShellFacade shellFacade = shellView.getShellViewer().getShellFacade();
			IExternalShellInvoker externalShellInvoker = shellFacade.getShellDescriptor()
					.getExternalShellInvoker();
			if(externalShellInvoker != null) {
				externalShellInvoker.execute(shellFacade.getShellDescriptor(), new String(),
						shellFacade.getCurrentPath());
			}
			else {
				MessageDialogHandler.openInformationMessage(view.getSite().getShell(),
						"This Shell does not support opening an external instance.");
			}
		}
	}

}
