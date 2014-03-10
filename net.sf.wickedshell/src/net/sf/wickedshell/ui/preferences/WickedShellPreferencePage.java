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

import java.io.File;

import net.sf.wickedshell.ShellID;
import net.sf.wickedshell.ShellPlugin;
import net.sf.wickedshell.domain.DomainPlugin;
import net.sf.wickedshell.domain.configuration.IShellDescriptorProperties;
import net.sf.wickedshell.facade.descriptor.IShellDescriptor;
import net.sf.wickedshell.preferences.PreferenceHelper;
import net.sf.wickedshell.provider.ShellDescriptorLabelProvider;
import net.sf.wickedshell.util.ShellLogger;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author Stefan Reichert
 * @since 24.03.2006
 */
public class WickedShellPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	/** The <code>Reader</code> of the shell. */
	private final ShellLogger shellLogger = new ShellLogger(WickedShellPreferencePage.class);

	private Button buttonBrowseCustomStartupDirectory;

	private Text textCustomStartupDirectory;

	private Button buttonUseWorkspaceAsStartupDirectory;

	private Button buttonBrowseShellRootDirectory;

	private Text textShellRootDirectory;

	private ComboViewer comboViewerShellDescriptor;

	/**
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	public Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		final GridLayout gridLayoutContainer = new GridLayout();
		gridLayoutContainer.marginWidth = 2;
		gridLayoutContainer.horizontalSpacing = 2;
		gridLayoutContainer.marginHeight = 2;
		container.setLayout(gridLayoutContainer);

		final Group groupShellSettings = new Group(container, SWT.NONE);
		final GridLayout gridLayoutShell = new GridLayout();
		gridLayoutShell.numColumns = 3;
		groupShellSettings.setLayout(gridLayoutShell);
		groupShellSettings.setText("Static Shell settings");
		groupShellSettings.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

		final Label labelShell = new Label(groupShellSettings, SWT.NONE);
		labelShell.setLayoutData(new GridData());
		labelShell.setText("Default Static Shell:");

		comboViewerShellDescriptor = new ComboViewer(groupShellSettings, SWT.READ_ONLY);
		final Combo comboShellDescriptor = comboViewerShellDescriptor.getCombo();
		comboShellDescriptor
				.setToolTipText("Please select the shell to be used. Ensure the selected shell to be installed and the executable is referenced in the path.");
		comboShellDescriptor.select(0);
		comboViewerShellDescriptor.setContentProvider(new ArrayContentProvider());
		comboViewerShellDescriptor.setLabelProvider(new ShellDescriptorLabelProvider());
		comboViewerShellDescriptor.addFilter(new ViewerFilter() {
			/**
			 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer,
			 *      java.lang.Object, java.lang.Object)
			 */
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				IShellDescriptor descriptor = (IShellDescriptor) element;
				return descriptor.isCurrentOSSupported();
			}
		});

		final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1);
		gridData.horizontalIndent = 25;
		comboShellDescriptor.setLayoutData(gridData);

		final Label labelShellRootDirectory = new Label(groupShellSettings, SWT.NONE);
		labelShellRootDirectory.setText("Shell root directory:");

		textShellRootDirectory = new Text(groupShellSettings, SWT.BORDER);
		textShellRootDirectory
				.setToolTipText("If you use an installed shell with a specific root directory, please enter this root directory here.");
		final GridData gridDataTextShellRootDirectory = new GridData(GridData.FILL,
				GridData.CENTER, true, false);
		gridDataTextShellRootDirectory.horizontalIndent = 25;
		textShellRootDirectory.setLayoutData(gridDataTextShellRootDirectory);

		buttonBrowseShellRootDirectory = new Button(groupShellSettings, SWT.NONE);
		buttonBrowseShellRootDirectory.setText("Browse...");
		buttonBrowseShellRootDirectory.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent selectionEvent) {
				DirectoryDialog shellRootDirectorySelectionDialog = new DirectoryDialog(getShell());
				String shellRootDirectory = shellRootDirectorySelectionDialog.open();
				if(shellRootDirectory != null) {
					textShellRootDirectory.setText(shellRootDirectory);
				}
			}
		});

		comboViewerShellDescriptor.addSelectionChangedListener(new ISelectionChangedListener() {
			/**
			 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
			 */
			public void selectionChanged(SelectionChangedEvent event) {
				if(!event.getSelection().isEmpty()) {
					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					IShellDescriptor descriptor = (IShellDescriptor) selection.getFirstElement();
					if(descriptor.hasCustomRoot()) {
						textShellRootDirectory.setEnabled(true);
						buttonBrowseShellRootDirectory.setEnabled(true);
						IShellDescriptorProperties shellDescriptorProperties = DomainPlugin
								.getDefault().getShellDescriptorProperties(descriptor.getId());
						textShellRootDirectory
								.setText(shellDescriptorProperties.getRootDirectory());

					}
					else {
						textShellRootDirectory.setText(new String());
						textShellRootDirectory.setEnabled(false);
						buttonBrowseShellRootDirectory.setEnabled(false);
					}
				}
				else {
					textShellRootDirectory.setText(new String());
					textShellRootDirectory.setEnabled(false);
					buttonBrowseShellRootDirectory.setEnabled(false);
				}
			}
		});

		// Set the viewer's inputs
		try {
			comboViewerShellDescriptor.setInput(IShellDescriptor.Manager
					.getStaticShellDescriptors());
			IShellDescriptor shellDescriptor = PreferenceHelper.getActiveShellDescriptor();
			IShellDescriptorProperties shellDescriptorProperties = DomainPlugin.getDefault()
					.getShellDescriptorProperties(shellDescriptor.getId());
			comboViewerShellDescriptor.setSelection(new StructuredSelection(shellDescriptor));
			textShellRootDirectory.setText(shellDescriptorProperties.getRootDirectory());

		}
		catch (Exception exception) {
			shellLogger.error(exception.getMessage(), exception);
		}

		final Group groupStartupSettings = new Group(container, SWT.NONE);
		groupStartupSettings.setText("Startup settings (if supported by selected Shell)");
		groupStartupSettings.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		final GridLayout gridLayoutGroupStartupSettings = new GridLayout();
		gridLayoutGroupStartupSettings.numColumns = 3;
		groupStartupSettings.setLayout(gridLayoutGroupStartupSettings);

		buttonUseWorkspaceAsStartupDirectory = new Button(groupStartupSettings, SWT.CHECK);
		buttonUseWorkspaceAsStartupDirectory.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 3, 1));
		buttonUseWorkspaceAsStartupDirectory.setText("Use workspace as startup directory");
		buttonUseWorkspaceAsStartupDirectory.setSelection(ShellPlugin.getDefault()
				.getPreferenceStore().getBoolean(
						ShellID.USE_WORKSPACE_AS_STARTUP_DIRECTORY_STRING_KEY));
		buttonUseWorkspaceAsStartupDirectory.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent event) {
				if(buttonUseWorkspaceAsStartupDirectory.getSelection()) {
					textCustomStartupDirectory.setText(new String());
				}
				else {
					textCustomStartupDirectory.setText(ShellPlugin.getDefault()
							.getPreferenceStore().getString(
									ShellID.CUSTOM_STARTUP_DIRECTORY_STRING_KEY));
				}
				textCustomStartupDirectory.setEnabled(!buttonUseWorkspaceAsStartupDirectory
						.getSelection());
				buttonBrowseCustomStartupDirectory.setEnabled(!buttonUseWorkspaceAsStartupDirectory
						.getSelection());
			}
		});

		final Label customStartupDirectoryLabel = new Label(groupStartupSettings, SWT.NONE);
		customStartupDirectoryLabel.setText("Custom startup directory:");

		textCustomStartupDirectory = new Text(groupStartupSettings, SWT.BORDER);
		textCustomStartupDirectory.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		textCustomStartupDirectory.setText(ShellPlugin.getDefault().getPreferenceStore().getString(
				ShellID.CUSTOM_STARTUP_DIRECTORY_STRING_KEY));
		textCustomStartupDirectory.setEnabled(!buttonUseWorkspaceAsStartupDirectory.getSelection());

		buttonBrowseCustomStartupDirectory = new Button(groupStartupSettings, SWT.NONE);
		buttonBrowseCustomStartupDirectory.setText("Browse...");
		buttonBrowseCustomStartupDirectory.setEnabled(!buttonUseWorkspaceAsStartupDirectory
				.getSelection());
		buttonBrowseCustomStartupDirectory.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent selectionEvent) {
				DirectoryDialog shellCustomStartupDirectorySelectionDialog = new DirectoryDialog(
						getShell());
				String customStartupDirectory = shellCustomStartupDirectorySelectionDialog.open();
				if(customStartupDirectory != null) {
					textCustomStartupDirectory.setText(customStartupDirectory);
				}
			}
		});
		// Set the viewer's input
		return container;
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
		boolean useWorkspaceAsStartupDirectory = buttonUseWorkspaceAsStartupDirectory
				.getSelection()
				|| !new File(textCustomStartupDirectory.getText()).exists();
		buttonUseWorkspaceAsStartupDirectory.setSelection(useWorkspaceAsStartupDirectory);
		textCustomStartupDirectory.setEnabled(!buttonUseWorkspaceAsStartupDirectory.getSelection());
		buttonBrowseCustomStartupDirectory.setEnabled(!buttonUseWorkspaceAsStartupDirectory
				.getSelection());
		ShellPlugin.getDefault().getPreferenceStore().setValue(
				ShellID.USE_WORKSPACE_AS_STARTUP_DIRECTORY_STRING_KEY,
				useWorkspaceAsStartupDirectory);
		ShellPlugin.getDefault().getPreferenceStore().setValue(
				ShellID.CUSTOM_STARTUP_DIRECTORY_STRING_KEY, textCustomStartupDirectory.getText());

		IStructuredSelection structuredSelection = (IStructuredSelection) comboViewerShellDescriptor
				.getSelection();
		IShellDescriptor shellDescriptor = (IShellDescriptor) structuredSelection.getFirstElement();
		ShellPlugin.getDefault().getPreferenceStore().setValue(
				ShellID.SHELL_DESCRIPTOR_ID_STRING_KEY, shellDescriptor.getId());
		IShellDescriptorProperties shellDescriptorProperties = DomainPlugin.getDefault()
				.getShellDescriptorProperties(shellDescriptor.getId());
		shellDescriptorProperties.setRootDirectory(textShellRootDirectory.getText());

		return super.performOk();
	}

	/**
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults() {
		ShellPlugin.getDefault().getPreferenceStore().setToDefault(
				ShellID.SHELL_DESCRIPTOR_ID_STRING_KEY);
		ShellPlugin.getDefault().getPreferenceStore().setToDefault(
				ShellID.USE_WORKSPACE_AS_STARTUP_DIRECTORY_STRING_KEY);
		ShellPlugin.getDefault().getPreferenceStore().setToDefault(
				ShellID.CUSTOM_STARTUP_DIRECTORY_STRING_KEY);
		buttonUseWorkspaceAsStartupDirectory.setSelection(ShellPlugin.getDefault()
				.getPreferenceStore().getBoolean(
						ShellID.USE_WORKSPACE_AS_STARTUP_DIRECTORY_STRING_KEY));
		textCustomStartupDirectory.setText(ShellPlugin.getDefault().getPreferenceStore().getString(
				ShellID.CUSTOM_STARTUP_DIRECTORY_STRING_KEY));

		IShellDescriptor shellDescriptor = PreferenceHelper.getActiveShellDescriptor();
		comboViewerShellDescriptor.setSelection(new StructuredSelection(shellDescriptor));

		DomainPlugin.getDefault().resetShellDescriptorProperties();
		IShellDescriptorProperties shellDescriptorProperties = DomainPlugin.getDefault()
				.getShellDescriptorProperties(shellDescriptor.getId());
		textShellRootDirectory.setText(shellDescriptorProperties.getRootDirectory());

		super.performDefaults();
	}
}
