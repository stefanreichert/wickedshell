/*
 * AddStaticCompletionDialog.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.ui.preferences;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class StaticCompletionDialog extends TitleAreaDialog {

	private String comment;

	private String completion;

	private Text textComment;

	private Text textCompletion;

	public StaticCompletionDialog(Shell parentShell) {
		this(parentShell, "", "");
	}

	public StaticCompletionDialog(Shell parentShell, String comment, String completion) {
		super(parentShell);
		this.comment = comment;
		this.completion = completion;
		setHelpAvailable(false);
	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		final GridLayout gridLayoutContainer = new GridLayout();
		gridLayoutContainer.numColumns = 2;
		container.setLayout(gridLayoutContainer);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		final Label labelCompletion = new Label(container, SWT.NONE);
		labelCompletion.setText("Completion:");

		textCompletion = new Text(container, SWT.BORDER);
		final GridData gridDataTextCompletion = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridDataTextCompletion.horizontalIndent = 10;
		textCompletion.setLayoutData(gridDataTextCompletion);

		final Label labelComment = new Label(container, SWT.NONE);

		textComment = new Text(container, SWT.BORDER);
		final GridData gridDataTextComment = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridDataTextComment.horizontalIndent = 10;
		textComment.setLayoutData(gridDataTextComment);
		if(comment != null && !comment.equals("") && completion != null && !completion.equals("")) {
			labelComment.setText("Description:");
			textCompletion.setText(completion);
			textComment.setText(comment);
			setTitle("Edit a static completion");
		}
		else {
			labelComment.setText("Comment:");
			setTitle("Add a static completion");
		}
		setMessage("Please enter the completion string and a description.");
		//
		return area;
	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * @see org.eclipse.jface.window.Window#getInitialSize()
	 */
	protected Point getInitialSize() {
		return new Point(300, 200);
	}

	/**
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Static Completion");
	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed() {
		if(textCompletion.getText().length() > 0 && textComment.getText().length() > 0) {
			completion = textCompletion.getText();
			comment = textComment.getText();
			super.okPressed();
		}
		else {
			setErrorMessage("Both completion and comment are mandatory and must not be empty.");
		}
	}

	/**
	 * @return Returns the comment.
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @return Returns the completion.
	 */
	public String getCompletion() {
		return completion;
	}

}
