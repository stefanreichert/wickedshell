/*
 * ShellErrorDisplay.java
 * 
 * Copyright 2003-2004 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.ui.shell;

import net.sf.wickedshell.ShellID;
import net.sf.wickedshell.ShellPlugin;
import net.sf.wickedshell.listener.IShellOutputListener;
import net.sf.wickedshell.util.ResourceManager;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

public class ShellErrorDisplay extends Dialog implements IShellOutputListener {

	private Text display;

	/**
	 * Constructor for ShellErrorDisplay.
	 * 
	 * @param parentShell
	 */
	public ShellErrorDisplay(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 1;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 1;
		gridLayout.marginHeight = 1;
		container.setLayout(gridLayout);

		display = new Text(container, SWT.V_SCROLL | SWT.MULTI | SWT.READ_ONLY
				| SWT.H_SCROLL);
		display.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true,
				true));
		display.setBackground(ResourceManager.getColor(SWT.COLOR_WHITE));
		display.setForeground(ResourceManager.getColor(SWT.COLOR_DARK_RED));
		display.append(ShellPlugin.getDefault().getErrorLog().getLog());
		display.setTopIndex(display.getLineCount());

		final Label separator = new Label(container, SWT.HORIZONTAL
				| SWT.SEPARATOR);
		separator.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,
				true, false));
		//
		return container;
	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
	}

	/**
	 * @see org.eclipse.jface.window.Window#getInitialSize()
	 */
	protected Point getInitialSize() {
		return new Point(600, 400);
	}

	/**
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setImage(ResourceManager.getPluginImage(ShellPlugin
				.getDefault(), "img/shellErrorDisplay.gif"));
		newShell.setText("Shell Error Display");
	}

	public void handleShellErrorOutput(String errorOut) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				if(ShellPlugin.getDefault().getPreferenceStore().getBoolean(
						ShellID.OPEN_SHELL_ERROR_DIALOG_STRING_KEY)) {
					open();
				}
			}
		});
	}

	public void handleShellOutput(String output) {
	}
}
