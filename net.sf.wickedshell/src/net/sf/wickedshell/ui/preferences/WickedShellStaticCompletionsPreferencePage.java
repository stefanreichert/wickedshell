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
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.sf.wickedshell.ShellPlugin;
import net.sf.wickedshell.action.shell.ExportShellOutputAction;
import net.sf.wickedshell.domain.DomainPlugin;
import net.sf.wickedshell.domain.completion.ICompletion;
import net.sf.wickedshell.domain.completion.ICompletion.Completion;
import net.sf.wickedshell.util.MessageDialogHandler;
import net.sf.wickedshell.util.ResourceManager;
import net.sf.wickedshell.util.ShellLogger;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.dialogs.ListSelectionDialog;

/**
 * @author Stefan Reichert
 * @since 24.03.2006
 */
public class WickedShellStaticCompletionsPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	/** The <code>Reader</code> of the shell. */
	private final ShellLogger shellLogger = new ShellLogger(ExportShellOutputAction.class);

	/** The <code>TableViewer</code> showing the static completions. */
	private TableViewer tableViewerStaticCompletions;

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

		final Group groupStaticCompletions = new Group(container, SWT.NONE);
		groupStaticCompletions.setText("Static completions");
		groupStaticCompletions.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		final GridLayout gridLayoutGroupStaticCompletions = new GridLayout();
		gridLayoutGroupStaticCompletions.numColumns = 2;
		groupStaticCompletions.setLayout(gridLayoutGroupStaticCompletions);

		tableViewerStaticCompletions = new TableViewer(groupStaticCompletions, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		tableViewerStaticCompletions.setContentProvider(new ArrayContentProvider());
		tableViewerStaticCompletions.setLabelProvider(new CompletionTableLabelProvider());
		tableViewerStaticCompletions.setSorter(new ViewerSorter() {
			/**
			 * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer,
			 *      java.lang.Object, java.lang.Object)
			 */
			public int compare(Viewer viewer, Object objectOne, Object objectTwo) {
				ICompletion completionOne = (ICompletion) objectOne;
				ICompletion completionTwo = (ICompletion) objectTwo;
				return super.compare(viewer, completionOne.getContent(), completionTwo.getContent());
			}
		});

		final Table tableStaticCompletions = tableViewerStaticCompletions.getTable();
		tableStaticCompletions.setLinesVisible(true);
		tableStaticCompletions.setHeaderVisible(true);
		final GridData gridDataTableStaticCompletions = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 6);
		gridDataTableStaticCompletions.heightHint = 250;
		tableStaticCompletions.setLayoutData(gridDataTableStaticCompletions);

		final TableColumn completionTableColumn = new TableColumn(tableStaticCompletions, SWT.NONE);
		completionTableColumn.setWidth(150);
		completionTableColumn.setText("Completion");

		final TableColumn descriptionTableColumn = new TableColumn(tableStaticCompletions, SWT.NONE);
		descriptionTableColumn.setWidth(250);
		descriptionTableColumn.setText("Description");

		final Button buttonAdd = new Button(groupStaticCompletions, SWT.NONE);
		final GridData gridDataButtonAdd = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridDataButtonAdd.horizontalIndent = 10;
		buttonAdd.setLayoutData(gridDataButtonAdd);
		buttonAdd.setText("Add completion");
		buttonAdd.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@SuppressWarnings("unchecked")
			public void widgetSelected(SelectionEvent event) {
				StaticCompletionDialog dialog = new StaticCompletionDialog(getShell());
				if(dialog.open() == IDialogConstants.OK_ID) {
					StringBuffer descriptionBuffer = new StringBuffer();
					descriptionBuffer.append(dialog.getCompletion());
					descriptionBuffer.append(" - ");
					descriptionBuffer.append(dialog.getComment());
					descriptionBuffer.append(" (Static)");

					List staticCompletions = DomainPlugin.getDefault().getStaticCompletions();
					staticCompletions.add(ICompletion.Factory.newInstance(dialog.getCompletion(), descriptionBuffer.toString(), new String()));
					tableViewerStaticCompletions.setInput(staticCompletions);
				}
			};
		});

		final Button buttonEdit = new Button(groupStaticCompletions, SWT.NONE);
		final GridData gridDataButtonEdit = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridDataButtonEdit.horizontalIndent = 10;
		buttonEdit.setLayoutData(gridDataButtonAdd);
		buttonEdit.setText("Edit completion");
		buttonEdit.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@SuppressWarnings("unchecked")
			public void widgetSelected(SelectionEvent event) {
				if(!tableViewerStaticCompletions.getSelection().isEmpty()) {
					IStructuredSelection selection = (IStructuredSelection) tableViewerStaticCompletions.getSelection();
					Completion completion = (Completion) selection.getFirstElement();
					StaticCompletionDialog dialog = new StaticCompletionDialog(getShell(), completion.getLabel(), completion.getContent());
					if(dialog.open() == IDialogConstants.OK_ID) {
						List staticCompletions = DomainPlugin.getDefault().getStaticCompletions();
						staticCompletions.remove(completion);
						staticCompletions.add(ICompletion.Factory.newInstance(dialog.getCompletion(), dialog.getComment(), new String()));
						tableViewerStaticCompletions.setInput(staticCompletions);
					}

				}
			};
		});

		final Button buttonRemove = new Button(groupStaticCompletions, SWT.NONE);
		final GridData gridDataButtonRemove = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridDataButtonRemove.horizontalIndent = 10;
		buttonRemove.setLayoutData(gridDataButtonRemove);
		buttonRemove.setText("Remove completion");
		buttonRemove.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@SuppressWarnings("unchecked")
			public void widgetSelected(SelectionEvent event) {
				if(!tableViewerStaticCompletions.getSelection().isEmpty()) {
					IStructuredSelection selection = (IStructuredSelection) tableViewerStaticCompletions.getSelection();
					List staticCompletions = DomainPlugin.getDefault().getStaticCompletions();
					staticCompletions.removeAll(selection.toList());
					tableViewerStaticCompletions.setInput(staticCompletions);
				}
			};
		});
		// Set the viewer's input
		tableViewerStaticCompletions.setInput(DomainPlugin.getDefault().getStaticCompletions());

		final Button buttonExport = new Button(groupStaticCompletions, SWT.NONE);
		final GridData gridDataButtonExport = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gridDataButtonExport.verticalIndent = 10;
		gridDataButtonExport.horizontalIndent = 10;
		buttonExport.setLayoutData(gridDataButtonExport);
		buttonExport.setText("Export completions");
		buttonExport.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent event) {
				FileDialog fileDialog = new FileDialog(getShell());
				fileDialog.setText("Export Static Completions");
				fileDialog.setFilterExtensions(new String[] { "*.completions" });
				fileDialog.setFilterNames(new String[] { "Completion files (*.completions)" });
				fileDialog.setFileName("static.completions");
				String staticCompletionsFileName = fileDialog.open();
				if(staticCompletionsFileName != null) {
					File staticCompletionsFile = new File(staticCompletionsFileName);
					if(staticCompletionsFile.exists()) {
						if(MessageDialogHandler.openConfirmationMessage(getShell(), "The file \"" + staticCompletionsFileName
								+ "\" already exists and will be overwritten. Do you want to proceed ?")) {
							staticCompletionsFile.delete();
						}
						else {
							return;
						}
					}
					try {
						staticCompletionsFile.createNewFile();
						DomainPlugin.getDefault().writeCompletionsToFile(DomainPlugin.getDefault().getStaticCompletions(), staticCompletionsFile);

					}
					catch (IOException exception) {
						shellLogger.error(exception.getMessage(), exception);
					}
				}
			}
		});

		final Button buttonImport = new Button(groupStaticCompletions, SWT.NONE);
		final GridData gridDataButtonImport = new GridData(GridData.FILL, GridData.CENTER, false, false);
		gridDataButtonImport.horizontalIndent = 10;
		buttonImport.setLayoutData(gridDataButtonImport);
		buttonImport.setText("Import completions");
		buttonImport.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@SuppressWarnings("unchecked")
			public void widgetSelected(SelectionEvent event) {
				FileDialog fileDialog = new FileDialog(getShell());
				fileDialog.setText("Import Static Completions");
				fileDialog.setFilterExtensions(new String[] { "*.completions" });
				fileDialog.setFilterNames(new String[] { "Completion files (*.completions)" });
				fileDialog.setFileName("static.completions");
				String staticCompletionsFileName = fileDialog.open();
				if(staticCompletionsFileName != null) {
					File staticCompletionsFile = new File(staticCompletionsFileName);
					try {
						List completions = DomainPlugin.getDefault().readCompletionsFromFile(staticCompletionsFile);
						List staticCompletions = DomainPlugin.getDefault().getStaticCompletions();
						if(completions.removeAll(staticCompletions)) {
							if(completions.isEmpty()) {
								MessageDialogHandler.openWarningMessage(getShell(), "All entries found are already listed and will be ignored!");
								return;
							}
							MessageDialogHandler.openWarningMessage(getShell(), "Duplicate entries have been found and will be ignored!");
						}
						ListSelectionDialog listSelectionDialog = new ListSelectionDialog(getShell(), completions, new ArrayContentProvider(),
								new LabelProvider() {

									/**
									 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
									 */
									public String getText(Object element) {
										if(element instanceof ICompletion) {
											ICompletion completion = (ICompletion) element;
											return completion.getLabel();
										}
										return super.getText(element);
									}

									/**
									 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
									 */
									public Image getImage(Object element) {
										if(element instanceof ICompletion) {
											ICompletion completion = (ICompletion) element;
											if(completion.getImagePath() != null && completion.getImagePath().length() > 1) {
												return ResourceManager.getPluginImage(ShellPlugin.getDefault(), completion.getImagePath());
											}
											return ResourceManager.getPluginImage(ShellPlugin.getDefault(), "img/staticCompletion.gif");
										}
										return super.getImage(element);
									}
								}, "Please choose from the completions found.");
						listSelectionDialog.setTitle("Import Static Completions");
						listSelectionDialog.setHelpAvailable(false);
						if(listSelectionDialog.open() == Dialog.OK) {
							staticCompletions.addAll(Arrays.asList(listSelectionDialog.getResult()));
							tableViewerStaticCompletions.setInput(staticCompletions);
						}

					}
					catch (IOException exception) {
						shellLogger.error(exception.getMessage(), exception);
					}
				}
			}
		});
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
		return super.performOk();
	}

	/**
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults() {
		DomainPlugin.getDefault().resetCompletions();
		tableViewerStaticCompletions.setInput(DomainPlugin.getDefault().getStaticCompletions());

		super.performDefaults();
	}
}
