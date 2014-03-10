/*
 * BatchView.java
 * 
 * Copyright 2003-2004 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.ui.batch;

import net.sf.wickedshell.ShellID;
import net.sf.wickedshell.provider.BatchFileLabelProvider;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;

public class BatchView extends ViewPart {

	/** The <code>BatchManager</code>. */
	private BatchManager batchManager;

	private TableViewer tableViewerBatchFiles;

	private Text textBatchFile;

	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout());

		final SashForm sashForm = new SashForm(container, SWT.HORIZONTAL);

		tableViewerBatchFiles = new TableViewer(sashForm, SWT.FULL_SELECTION | SWT.BORDER);
		tableViewerBatchFiles.setContentProvider(new ArrayContentProvider());
		Table tableBatchFiles = tableViewerBatchFiles.getTable();
		tableBatchFiles.setLinesVisible(true);
		tableBatchFiles.setHeaderVisible(true);
		tableBatchFiles.setToolTipText("All currently listed batches");

		final TableColumn tableColumnIcon = new TableColumn(tableBatchFiles, SWT.NONE);
		tableColumnIcon.setResizable(false);
		tableColumnIcon.setWidth(25);

		final TableColumn tableColumnFile = new TableColumn(tableBatchFiles, SWT.NONE);
		tableColumnFile.setText("Batch File");
		tableColumnFile.setWidth(250);

		final TableColumn tableColumnParameters = new TableColumn(tableBatchFiles, SWT.NONE);
		tableColumnParameters.setText("Parameters");
		tableColumnParameters.setWidth(150);

		tableViewerBatchFiles.setContentProvider(new ArrayContentProvider());
		tableViewerBatchFiles.setLabelProvider(new BatchFileLabelProvider());

		textBatchFile = new Text(sashForm, SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.BORDER);
		textBatchFile.setEditable(false);

		sashForm.setWeights(new int[] { 2, 3 });

		getViewSite().setSelectionProvider(tableViewerBatchFiles);
		initializeBatchManager();
		initializeToolBar();
		initializeMenu();
	}

	/**
	 * Initializes the <code>BatchManager</code>.
	 */
	private void initializeBatchManager() {
		batchManager = new BatchManager(tableViewerBatchFiles, textBatchFile);
		batchManager.activate();
		tableViewerBatchFiles.getTable().addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent event) {
				batchManager.deactivate();
			}
		});
	}

	private void initializeToolBar() {
		IToolBarManager manager = getViewSite().getActionBars().getToolBarManager();
		manager.add(new Separator(ShellID.BATCH_VIEW_MENU_GROUP_ADD));
		manager.add(new Separator(ShellID.BATCH_VIEW_MENU_GROUP_LISTENTRY));
		manager.add(new Separator(ShellID.BATCH_VIEW_MENU_GROUP_GENERAL));
		getViewSite().getActionBars().updateActionBars();
	}

	private void initializeMenu() {
		MenuManager manager = new MenuManager();
		manager.add(new Separator(ShellID.BATCH_VIEW_MENU_GROUP_ADD));
		manager.add(new Separator(ShellID.BATCH_VIEW_MENU_GROUP_LISTENTRY));
		manager.add(new Separator(ShellID.BATCH_VIEW_MENU_GROUP_GENERAL));
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		getSite().registerContextMenu(ShellID.BATCH_FILE_DESCRIPTOR_VIEWER_ID, manager,
				tableViewerBatchFiles);
		// Create the menu
		Menu menu = manager.createContextMenu(tableViewerBatchFiles.getTable());
		tableViewerBatchFiles.getTable().setMenu(menu);
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus() {
		tableViewerBatchFiles.getTable().forceFocus();
	}

	/**
	 * @return Returns the batchManager.
	 */
	public BatchManager getBatchManager() {
		return batchManager;
	}

}
