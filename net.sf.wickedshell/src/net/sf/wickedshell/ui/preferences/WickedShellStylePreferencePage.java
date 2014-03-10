/*
 * WickedShellPreferencePage.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.ui.preferences;

import net.sf.wickedshell.ShellID;
import net.sf.wickedshell.ShellPlugin;
import net.sf.wickedshell.domain.DomainPlugin;
import net.sf.wickedshell.domain.style.IColorDescriptor;
import net.sf.wickedshell.domain.style.IFontDescriptor;
import net.sf.wickedshell.util.ResourceManager;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author Stefan Reichert
 * @since 24.03.2006
 */
public class WickedShellStylePreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	private Button buttonConfirmClosingOfDetachedShell;
	private Button buttonOpenCommandHistoryDialog;

	private Button buttonShowCascadingCompletions;

	private Button buttonChooseFont;

	private Label labelShellStyle;

	private Button buttonOpenShellErrorDialog;

	private Button buttonSystemPathCompletions;

	private Button buttonShowActiveFolderCompletions;

	private Button buttonShowStaticCompletions;

	private Button buttonSystemEnviromentalValueCompletions;

	private IFontDescriptor shellFontDescriptor;

	private RGB shellForegroundRGB;

	private RGB shellBackgroundRGB;

	/**
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	public Control createContents(final Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		final GridLayout gridLayoutContainer = new GridLayout();
		gridLayoutContainer.marginWidth = 2;
		gridLayoutContainer.horizontalSpacing = 2;
		gridLayoutContainer.marginHeight = 2;
		container.setLayout(gridLayoutContainer);

		final Group groupGeneralSettings = new Group(container, SWT.NONE);
		groupGeneralSettings.setText("General settings");
		groupGeneralSettings.setLayoutData(new GridData(GridData.FILL,
				GridData.CENTER, true, false));
		final GridLayout gridLayout = new GridLayout();
		groupGeneralSettings.setLayout(gridLayout);

		buttonOpenShellErrorDialog = new Button(groupGeneralSettings, SWT.CHECK);
		buttonOpenShellErrorDialog.setLayoutData(new GridData());
		buttonOpenShellErrorDialog.setText("Open dialog on error in Shell");
		buttonOpenShellErrorDialog.setSelection(ShellPlugin.getDefault()
				.getPreferenceStore().getBoolean(
						ShellID.OPEN_SHELL_ERROR_DIALOG_STRING_KEY));

		buttonConfirmClosingOfDetachedShell = new Button(groupGeneralSettings, SWT.CHECK);
		buttonConfirmClosingOfDetachedShell.setText("Confirm closing of a detached Shell");
		buttonConfirmClosingOfDetachedShell.setSelection(ShellPlugin.getDefault()
				.getPreferenceStore().getBoolean(
						ShellID.CONFIRM_DETACHED_SHELL_CLOSING_STRING_KEY));

		buttonOpenCommandHistoryDialog = new Button(groupGeneralSettings,
				SWT.CHECK);
		buttonOpenCommandHistoryDialog.setLayoutData(new GridData());
		buttonOpenCommandHistoryDialog
				.setText("Open dialog for command history overview");
		buttonOpenCommandHistoryDialog.setSelection(ShellPlugin.getDefault()
				.getPreferenceStore().getBoolean(
						ShellID.OPEN_COMMAND_HISTORY_DIALOG_STRING_KEY));

		buttonShowStaticCompletions = new Button(groupGeneralSettings,
				SWT.CHECK);
		buttonShowStaticCompletions.setLayoutData(new GridData());
		buttonShowStaticCompletions
				.setText("Suggest static completions in Shell");
		buttonShowStaticCompletions.setSelection(ShellPlugin.getDefault()
				.getPreferenceStore().getBoolean(
						ShellID.SHOW_CUSTOM_COMMANDS_STRING_KEY));

		buttonShowActiveFolderCompletions = new Button(groupGeneralSettings,
				SWT.CHECK);
		buttonShowActiveFolderCompletions.setLayoutData(new GridData());
		buttonShowActiveFolderCompletions
				.setText("Suggest active folder completions in Shell");
		buttonShowActiveFolderCompletions.setSelection(ShellPlugin.getDefault()
				.getPreferenceStore().getBoolean(
						ShellID.SHOW_ACTIVE_FOLDER_COMMANDS_STRING_KEY));

		buttonShowCascadingCompletions = new Button(groupGeneralSettings,
				SWT.CHECK);
		buttonShowCascadingCompletions.setLayoutData(new GridData());
		buttonShowCascadingCompletions
				.setText("Suggest cascading completions in Shell");
		buttonShowCascadingCompletions.setSelection(ShellPlugin.getDefault()
				.getPreferenceStore().getBoolean(
						ShellID.SHOW_CASCADING_COMPLETIONS_STRING_KEY));

		buttonSystemPathCompletions = new Button(groupGeneralSettings,
				SWT.CHECK);
		buttonSystemPathCompletions.setLayoutData(new GridData());
		buttonSystemPathCompletions
				.setText("Suggest system path completions in Shell");
		buttonSystemPathCompletions.setSelection(ShellPlugin.getDefault()
				.getPreferenceStore().getBoolean(
						ShellID.SHOW_SYSTEM_PATH_COMMANDS_STRING_KEY));

		buttonSystemEnviromentalValueCompletions = new Button(
				groupGeneralSettings, SWT.CHECK);
		buttonSystemEnviromentalValueCompletions.setLayoutData(new GridData());
		buttonSystemEnviromentalValueCompletions
				.setText("Suggest enviromental value completions in Shell");
		buttonSystemEnviromentalValueCompletions.setSelection(ShellPlugin
				.getDefault().getPreferenceStore().getBoolean(
						ShellID.SHOW_SYSTEM_ENVIROMENTAL_VALUES_STRING_KEY));

		final Group groupShellStyleSettings = new Group(container, SWT.NONE);
		groupShellStyleSettings.setLayoutData(new GridData(GridData.FILL,
				GridData.FILL, true, false));
		groupShellStyleSettings.setText("Shell style settings");
		final GridLayout gridLayoutGroupStyleSettings = new GridLayout();
		gridLayoutGroupStyleSettings.numColumns = 3;
		groupShellStyleSettings.setLayout(gridLayoutGroupStyleSettings);

		labelShellStyle = new Label(groupShellStyleSettings, SWT.BORDER);
		final GridData gridDataLabelShellFontName = new GridData(GridData.FILL,
				GridData.CENTER, true, true, 3, 1);
		gridDataLabelShellFontName.heightHint = 40;
		labelShellStyle.setLayoutData(gridDataLabelShellFontName);
		handleFontDescriptorSelection(DomainPlugin.getDefault()
				.getFontDescriptor());

		buttonChooseFont = new Button(groupShellStyleSettings, SWT.NONE);
		buttonChooseFont.setLayoutData(new GridData(GridData.FILL,
				GridData.BEGINNING, true, false));
		buttonChooseFont.setText("Choose Font...");
		buttonChooseFont.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				FontDialog fontDialog = new FontDialog(getShell());
				fontDialog.setFontList(ResourceManager.getFont(
						shellFontDescriptor.getName(),
						shellFontDescriptor.getHeight(),
						shellFontDescriptor.getStyle()).getFontData());
				fontDialog.setText("Select Shell Font");
				fontDialog.open();
				if(fontDialog.getFontList().length != 0) {
					FontData fontData = fontDialog.getFontList()[0];
					// TODO Support array of font data!
					handleFontDescriptorSelection(IFontDescriptor.Factory
							.newInstance(fontData.getName(), fontData
									.getHeight(), fontData.getStyle()));
				}
			}
		});
		IColorDescriptor foregroundColorDescriptor = DomainPlugin.getDefault()
				.getForegroundColor();
		handleForegroundRGBSelection(new RGB(
				foregroundColorDescriptor.getRed(), foregroundColorDescriptor
						.getGreen(), foregroundColorDescriptor.getBlue()));
		final Button buttonShellForegroundColor = new Button(
				groupShellStyleSettings, SWT.NONE);
		buttonShellForegroundColor.setText("Choose Foreground Color...");
		buttonShellForegroundColor.setLayoutData(new GridData(GridData.FILL,
				GridData.BEGINNING, true, false));
		buttonShellForegroundColor.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent event) {
				ColorDialog colorDialog = new ColorDialog(getShell());
				colorDialog.setRGB(shellBackgroundRGB);
				RGB selectedForegroundRGB = colorDialog.open();
				if(selectedForegroundRGB != null) {
					handleForegroundRGBSelection(selectedForegroundRGB);
				}
			}
		});
		IColorDescriptor backgroundColorDescriptor = DomainPlugin.getDefault()
				.getBackgroundColor();
		handleBackgroundRGBSelection(new RGB(
				backgroundColorDescriptor.getRed(), backgroundColorDescriptor
						.getGreen(), backgroundColorDescriptor.getBlue()));

		final Button buttonShellBackgroundColor = new Button(
				groupShellStyleSettings, SWT.NONE);
		buttonShellBackgroundColor.setSelection(true);
		buttonShellBackgroundColor.setText("Choose Background Color...");
		final GridData gridDataButtonShellBackgroundColor = new GridData(
				GridData.FILL, GridData.BEGINNING, true, false);
		buttonShellBackgroundColor
				.setLayoutData(gridDataButtonShellBackgroundColor);
		buttonShellBackgroundColor.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent event) {
				ColorDialog colorDialog = new ColorDialog(getShell());
				colorDialog.setRGB(shellBackgroundRGB);
				RGB selectedBackgroundRGB = colorDialog.open();
				if(selectedBackgroundRGB != null) {
					handleBackgroundRGBSelection(selectedBackgroundRGB);
				}
			}
		});
		// Set the viewer's input
		return container;
	}

	/**
	 * Handles the selection of a <code>Font</code>.
	 * 
	 * @param selectedFont
	 *            The selected <code>Font</code>
	 */
	private void handleFontDescriptorSelection(
			IFontDescriptor selectedFontDescriptor) {
		shellFontDescriptor = selectedFontDescriptor;
		StringBuffer buffer = new StringBuffer();
		buffer.append("Style of Wicked Shell ");
		buffer.append(ShellID.VERSION);
		buffer.append(Text.DELIMITER);
		buffer.append("Chosen Font: ");
		buffer.append(shellFontDescriptor.getName());
		buffer.append(" [");
		buffer.append(shellFontDescriptor.getHeight());
		buffer.append("]");
		labelShellStyle.setText(buffer.toString());
		labelShellStyle.setFont(ResourceManager.getFont(shellFontDescriptor
				.getName(), shellFontDescriptor.getHeight(),
				shellFontDescriptor.getStyle()));
	}

	/**
	 * Handles the selection of a foreground <code>RGB</code>.
	 * 
	 * @param selectedForegroundRGB
	 *            The selected foreground <code>RGB</code>
	 */
	private void handleForegroundRGBSelection(RGB selectedForegroundRGB) {
		shellForegroundRGB = selectedForegroundRGB;
		labelShellStyle.setForeground(ResourceManager
				.getColor(selectedForegroundRGB));
	}

	/**
	 * Handles the selection of a background <code>RGB</code>.
	 * 
	 * @param selectedBackgroundRGB
	 *            The selected background <code>RGB</code>
	 */
	private void handleBackgroundRGBSelection(RGB selectedBackgroundRGB) {
		shellBackgroundRGB = selectedBackgroundRGB;
		labelShellStyle.setBackground(ResourceManager
				.getColor(selectedBackgroundRGB));
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

	/**
	 * @see org.eclipse.jface.preference.PreferencePage#doGetPreferenceStore()
	 */
	protected IPreferenceStore doGetPreferenceStore() {
		return ShellPlugin.getDefault().getPreferenceStore();
	}

	/**
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk() {
		ShellPlugin.getDefault().getPreferenceStore().setValue(
				ShellID.OPEN_SHELL_ERROR_DIALOG_STRING_KEY,
				buttonOpenShellErrorDialog.getSelection());
		ShellPlugin.getDefault().getPreferenceStore().setValue(
				ShellID.CONFIRM_DETACHED_SHELL_CLOSING_STRING_KEY,
				buttonConfirmClosingOfDetachedShell.getSelection());
		ShellPlugin.getDefault().getPreferenceStore().setValue(
				ShellID.OPEN_COMMAND_HISTORY_DIALOG_STRING_KEY,
				buttonOpenCommandHistoryDialog.getSelection());
		ShellPlugin.getDefault().getPreferenceStore().setValue(
				ShellID.SHOW_CUSTOM_COMMANDS_STRING_KEY,
				buttonShowStaticCompletions.getSelection());
		ShellPlugin.getDefault().getPreferenceStore().setValue(
				ShellID.SHOW_ACTIVE_FOLDER_COMMANDS_STRING_KEY,
				buttonShowActiveFolderCompletions.getSelection());
		ShellPlugin.getDefault().getPreferenceStore().setValue(
				ShellID.SHOW_CASCADING_COMPLETIONS_STRING_KEY,
				buttonShowCascadingCompletions.getSelection());
		ShellPlugin.getDefault().getPreferenceStore().setValue(
				ShellID.SHOW_SYSTEM_PATH_COMMANDS_STRING_KEY,
				buttonSystemPathCompletions.getSelection());
		ShellPlugin.getDefault().getPreferenceStore().setValue(
				ShellID.SHOW_SYSTEM_ENVIROMENTAL_VALUES_STRING_KEY,
				buttonSystemEnviromentalValueCompletions.getSelection());

		DomainPlugin.getDefault().setFontDescriptor(shellFontDescriptor);
		DomainPlugin.getDefault().setForegroundColor(
				IColorDescriptor.Factory.newInstance(shellForegroundRGB.red,
						shellForegroundRGB.green, shellForegroundRGB.blue));
		DomainPlugin.getDefault().setBackgroundColor(
				IColorDescriptor.Factory.newInstance(shellBackgroundRGB.red,
						shellBackgroundRGB.green, shellBackgroundRGB.blue));
		ShellPlugin.getDefault().getPreferenceStore().firePropertyChangeEvent(
				ShellID.SHELL_STYLE_STRING_KEY, null, null);
		return super.performOk();
	}

	/**
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults() {
		ShellPlugin.getDefault().getPreferenceStore().setToDefault(
				ShellID.OPEN_SHELL_ERROR_DIALOG_STRING_KEY);
		ShellPlugin.getDefault().getPreferenceStore().setToDefault(
				ShellID.CONFIRM_DETACHED_SHELL_CLOSING_STRING_KEY);
		ShellPlugin.getDefault().getPreferenceStore().setToDefault(
				ShellID.OPEN_COMMAND_HISTORY_DIALOG_STRING_KEY);
		ShellPlugin.getDefault().getPreferenceStore().setToDefault(
				ShellID.SHOW_CUSTOM_COMMANDS_STRING_KEY);
		ShellPlugin.getDefault().getPreferenceStore().setToDefault(
				ShellID.SHOW_ACTIVE_FOLDER_COMMANDS_STRING_KEY);
		ShellPlugin.getDefault().getPreferenceStore().setToDefault(
				ShellID.SHOW_CASCADING_COMPLETIONS_STRING_KEY);
		ShellPlugin.getDefault().getPreferenceStore().setToDefault(
				ShellID.SHOW_SYSTEM_PATH_COMMANDS_STRING_KEY);
		ShellPlugin.getDefault().getPreferenceStore().setToDefault(
				ShellID.SHOW_SYSTEM_ENVIROMENTAL_VALUES_STRING_KEY);
		ShellPlugin.getDefault().getPreferenceStore().setToDefault(
				ShellID.SHELL_DESCRIPTOR_ID_STRING_KEY);
		ShellPlugin.getDefault().getPreferenceStore().setToDefault(
				ShellID.USE_WORKSPACE_AS_STARTUP_DIRECTORY_STRING_KEY);
		ShellPlugin.getDefault().getPreferenceStore().setToDefault(
				ShellID.CUSTOM_STARTUP_DIRECTORY_STRING_KEY);

		buttonOpenShellErrorDialog.setSelection(ShellPlugin.getDefault()
				.getPreferenceStore().getBoolean(
						ShellID.OPEN_SHELL_ERROR_DIALOG_STRING_KEY));
		buttonConfirmClosingOfDetachedShell.setSelection(ShellPlugin.getDefault()
				.getPreferenceStore().getBoolean(
						ShellID.CONFIRM_DETACHED_SHELL_CLOSING_STRING_KEY));
		buttonShowStaticCompletions.setSelection(ShellPlugin.getDefault()
				.getPreferenceStore().getBoolean(
						ShellID.SHOW_CUSTOM_COMMANDS_STRING_KEY));
		buttonShowActiveFolderCompletions.setSelection(ShellPlugin.getDefault()
				.getPreferenceStore().getBoolean(
						ShellID.SHOW_ACTIVE_FOLDER_COMMANDS_STRING_KEY));
		buttonShowCascadingCompletions.setSelection(ShellPlugin.getDefault()
				.getPreferenceStore().getBoolean(
						ShellID.SHOW_CASCADING_COMPLETIONS_STRING_KEY));
		buttonSystemPathCompletions.setSelection(ShellPlugin.getDefault()
				.getPreferenceStore().getBoolean(
						ShellID.SHOW_SYSTEM_PATH_COMMANDS_STRING_KEY));
		buttonSystemEnviromentalValueCompletions.setSelection(ShellPlugin
				.getDefault().getPreferenceStore().getBoolean(
						ShellID.SHOW_SYSTEM_ENVIROMENTAL_VALUES_STRING_KEY));

		DomainPlugin.getDefault().resetShellStyle();
		handleFontDescriptorSelection(DomainPlugin.getDefault()
				.getFontDescriptor());

		IColorDescriptor foregroundColorDescriptor = DomainPlugin.getDefault()
				.getForegroundColor();
		handleForegroundRGBSelection(new RGB(
				foregroundColorDescriptor.getRed(), foregroundColorDescriptor
						.getGreen(), foregroundColorDescriptor.getBlue()));
		IColorDescriptor defaultBackgroundColorDescriptor = DomainPlugin
				.getDefault().getBackgroundColor();
		handleBackgroundRGBSelection(new RGB(defaultBackgroundColorDescriptor
				.getRed(), defaultBackgroundColorDescriptor.getGreen(),
				defaultBackgroundColorDescriptor.getBlue()));

		ShellPlugin.getDefault().getPreferenceStore().firePropertyChangeEvent(
				ShellID.SHELL_STYLE_STRING_KEY, null, null);
		super.performDefaults();
	}
}
