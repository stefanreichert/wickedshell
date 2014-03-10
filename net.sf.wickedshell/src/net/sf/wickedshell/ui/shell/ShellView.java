/*
 * ShellView.java
 * 
 * Copyright 2003-2004 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.ui.shell;

import java.util.Collection;
import java.util.Iterator;

import net.sf.wickedshell.ShellID;
import net.sf.wickedshell.ShellPlugin;
import net.sf.wickedshell.action.shell.OpenShellErrorDisplayAction;
import net.sf.wickedshell.facade.IShellFacade;
import net.sf.wickedshell.facade.ShellActivationException;
import net.sf.wickedshell.facade.ShellDeactivationException;
import net.sf.wickedshell.facade.descriptor.IShellDescriptor;
import net.sf.wickedshell.listener.ShellAdapter;
import net.sf.wickedshell.ui.shell.viewer.IShellViewer;
import net.sf.wickedshell.ui.shell.viewer.ShellViewerKeyAdapter;
import net.sf.wickedshell.ui.shell.viewer.text.TextShellViewer;
import net.sf.wickedshell.util.MessageDialogHandler;
import net.sf.wickedshell.util.ResourceManager;
import net.sf.wickedshell.util.ShellLogger;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

public class ShellView extends ViewPart {

	/** The <code>Reader</code> of the shell. */
	private final ShellLogger shellLogger = new ShellLogger(ShellView.class);

	/** The <code>IShellFacade</code>. */
	private IShellFacade shellFacade;

	/** The <code>IShellViewer</code>. */
	private IShellViewer shellViewer;

	/** The <code>ShellErrorDisplay</code>. */
	private ShellErrorDisplay errorDisplay;

	private IShellDescriptor shellDescriptor;

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout());

		 shellViewer = new TextShellViewer(container, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL);
// shellViewer = new TextViewerShellViewer(container, SWT.V_SCROLL | SWT.MULTI |
// SWT.BORDER | SWT.H_SCROLL);

		errorDisplay = new ShellErrorDisplay(getSite().getShell());

		// Avoid instant closing of the view within a detached window
		shellViewer.getShellDisplayControl().addListener(SWT.Traverse, new Listener() {

			/**
			 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
			 */
			public void handleEvent(Event event) {
				if(event.doit && event.detail == SWT.TRAVERSE_ESCAPE && isDetached()
						&& ShellPlugin.getDefault().getPreferenceStore().getBoolean(ShellID.CONFIRM_DETACHED_SHELL_CLOSING_STRING_KEY)) {
					event.doit = MessageDialogHandler.openConfirmationMessage(getSite().getShell(),
							"You've just pressed escape which will close this detached window. Do you want to proceed?");
				}
			}

		});

		initShellFacade();
		initToolBar();
		initMenuBar();
		ShellPlugin.getDefault().registerShellView(this);
	}

	/**
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	public void dispose() {
		// handle deactivation
		try {
			ShellPlugin.getDefault().unregisterShellView(ShellView.this);
			shellFacade.deactivate();
		}
		catch (ShellDeactivationException exception) {
			shellLogger.error(exception.getMessage(), exception);
		}
		super.dispose();
	}

	/**
	 * @return whether this <code>ShellView</code> is detached or not.
	 */
	private boolean isDetached() {
		return getSite().getShell().getParent() != null;
	}

	/**
	 * Initializes the <code>IShellFacade</code> used by this
	 * <code>ShellView</code>.
	 */
	private void initShellFacade() {
		try {
			// Create the shell facade
			shellFacade = IShellFacade.Factory.createInstance();
			shellViewer.initialize(shellFacade);
			ShellViewerKeyAdapter.adapt(shellViewer);
			// Register the global errorLog to enable logging
			shellFacade.addShellOutputListener(ShellPlugin.getDefault().getErrorLog());
			shellFacade.addShellOutputListener(errorDisplay);

			shellFacade.addShellListener(new ShellAdapter() {

				/**
				 * @see net.sf.wickedshell.listener.ShellAdapter#handleActivation()
				 */
				public void handleActivation() {
					ShellView.this.setContentDescription(createContentDescription());
				}
			});
			try {
				// Activate shell facade
				if(shellDescriptor != null) {
					// If the view is restored, use the previous shellDescriptor
					shellFacade.activate(shellDescriptor);
				}
				else {
					// Otherwise use the one defined in the preferences
					shellFacade.activate();
				}
			}
			catch (ShellActivationException shellActivationException) {
				shellLogger.error("Associated Shell could not be activated.", shellActivationException, true);
				activateDefaultShell();
			}
		}
		catch (Exception exception) {
			StringBuffer errorBuffer = new StringBuffer();
			errorBuffer.append("Exception occured: ");
			errorBuffer.append(exception.getClass().getName());
			errorBuffer.append(" --- ");
			errorBuffer.append(exception.getMessage());
			errorBuffer.append(Text.DELIMITER);
			errorBuffer.append("Please refer to error log for further details. ");
			errorBuffer.append("The setup of Wicked Shell failed.");
			Text shellDisplayControl = (Text) shellViewer.getShellDisplayControl();
			shellDisplayControl.append(errorBuffer.toString());
			shellDisplayControl.setEnabled(false);
			errorDisplay.handleShellOutput(errorBuffer.toString());
			shellLogger.error(exception.getMessage(), exception);
		}
	}

	/**
	 * Activate the default shell which should work finally.
	 */
	private void activateDefaultShell() {
		String defaultOSShellDescriptorId = IShellDescriptor.Manager.getDefaultShellDescriptorId();
		IShellDescriptor defaultOSShellDescriptor = IShellDescriptor.Manager.getShellDescriptor(defaultOSShellDescriptorId);
		shellLogger.debug("Trying to restart default OS Shell <" + defaultOSShellDescriptor.getName() + ">");
		try {
			getShellViewer().getShellFacade().restart(defaultOSShellDescriptor);
		}
		catch (ShellDeactivationException shellDeactivationException) {
			shellLogger.error("Default OS Shell <" + defaultOSShellDescriptor.getName() + "> could not be activated.", shellDeactivationException, true);
			throw new RuntimeException(shellDeactivationException);
		}
		catch (ShellActivationException shellActivationException) {
			shellLogger.error("Default OS Shell <" + defaultOSShellDescriptor.getName() + "> could not be activated.", shellActivationException, true);
			throw new RuntimeException(shellActivationException);
		}
	}

	/**
	 * Creates the content description of this view by inpecting the
	 * <code>IShellFacade</code>.
	 * 
	 * @return the content description
	 */
	private String createContentDescription() {
		StringBuffer contentDescriptionBuffer = new StringBuffer();
		contentDescriptionBuffer.append("Wicked Shell ");
		contentDescriptionBuffer.append(ShellID.VERSION);
		contentDescriptionBuffer.append(" - ");
		if(shellFacade.getShellDescriptor() != null) {
			contentDescriptionBuffer.append(shellFacade.getShellDescriptor().getName());
		}
		else {
			contentDescriptionBuffer.append("No Shell available");
		}
		return contentDescriptionBuffer.toString();
	}

	/**
	 * Initializes the menubar of this <code>ShellView</code>.
	 */
	private void initMenuBar() {
		IMenuManager manager = getViewSite().getActionBars().getMenuManager();
		manager.add(new Separator(ShellID.SHELL_VIEW_MENU_GROUP_MANAGE_INTERNAL));
		final IMenuManager subMenuManager = new MenuManager("Active Shell", ShellID.SHELL_VIEW_SUBMENU_ACTIVE_SHELL);
		manager.add(subMenuManager);
		manager.addMenuListener(new IMenuListener() {
			/**
			 * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
			 */
			@SuppressWarnings("unchecked")
			public void menuAboutToShow(IMenuManager manager) {
				subMenuManager.removeAll();
				subMenuManager.add(new GroupMarker(ShellID.SHELL_VIEW_SUBMENU_GROUP_AVAILABLE_SHELLS));
				Collection<IShellDescriptor> availableOSShellDescriptors = CollectionUtils.select(IShellDescriptor.Manager.getKnownShellDescriptors(), new Predicate() {
					/**
					 * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
					 */
					public boolean evaluate(Object object) {
						IShellDescriptor shellDescriptor = (IShellDescriptor) object;
						return shellDescriptor.isCurrentOSSupported();
					}
				});
				for (Iterator descriptorIterator = availableOSShellDescriptors.iterator(); descriptorIterator.hasNext();) {
					SetShellDescriptorAction setShellDescriptorAction = new SetShellDescriptorAction((IShellDescriptor) descriptorIterator.next());
					setShellDescriptorAction.setChecked(setShellDescriptorAction.getShellDescriptor().getId().equals(
							getShellViewer().getShellFacade().getShellDescriptor().getId()));
					subMenuManager.appendToGroup(ShellID.SHELL_VIEW_SUBMENU_GROUP_AVAILABLE_SHELLS, setShellDescriptorAction);

				}
			}
		});
		manager.add(new RenameShellAction());
		manager.add(new Separator(ShellID.SHELL_VIEW_MENU_GROUP_MANAGE_EXTERNAL));
		manager.add(new Separator(ShellID.SHELL_VIEW_MENU_GROUP_EXPORT));
		manager.add(new Separator(ShellID.SHELL_VIEW_MENU_GROUP_GENERAL));
		manager.add(new OpenShellErrorDisplayAction(errorDisplay));
		getViewSite().getActionBars().updateActionBars();
	}

	/**
	 * Initializes the toolbar of this <code>ShellView</code>.
	 */
	private void initToolBar() {
		IToolBarManager manager = getViewSite().getActionBars().getToolBarManager();
		manager.add(new Separator(ShellID.SHELL_VIEW_MENU_GROUP_MANAGE_INTERNAL));
		manager.add(new Separator(ShellID.SHELL_VIEW_MENU_GROUP_MANAGE_EXTERNAL));
		manager.add(new Separator(ShellID.SHELL_VIEW_MENU_GROUP_GENERAL));
		getViewSite().getActionBars().updateActionBars();
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus() {
		shellViewer.getShellDisplayControl().forceFocus();
	}

	/**
	 * @see org.eclipse.ui.part.ViewPart#init(org.eclipse.ui.IViewSite,
	 *      org.eclipse.ui.IMemento)
	 */
	public void init(IViewSite site, IMemento memento) throws PartInitException {
		if(memento != null && memento.getString(ShellID.MEMENTO_SHELL_VIEW_DESCRIPTOR_NAME) != null) {
			shellLogger.debug("Memento found - Restoring ShellView");
			super.init(site, memento);
			setPartName(memento.getString(ShellID.MEMENTO_SHELL_VIEW_NAME));
			try {
				shellDescriptor = IShellDescriptor.Manager.getShellDescriptor(memento.getString(ShellID.MEMENTO_SHELL_VIEW_DESCRIPTOR_NAME));
			}
			catch (Throwable throwable) {
				shellLogger.error("ShellDescriptor cannot be restored - Using default OS ShellDescriptor", throwable, true);
				shellDescriptor = IShellDescriptor.Manager.getShellDescriptor(IShellDescriptor.Manager.getDefaultShellDescriptorId());
			}
		}
		else {
			shellLogger.debug("No memento found - Creating fresh ShellView");
			super.init(site);
			shellDescriptor = null;
			if(getViewSite().getSecondaryId() != null) {
				StringBuffer partnameBuffer = new StringBuffer();
				partnameBuffer.append(getPartName());
				partnameBuffer.append(" (additional)");
				setPartName(partnameBuffer.toString());
			}
		}
	}

	/**
	 * @see org.eclipse.ui.part.ViewPart#saveState(org.eclipse.ui.IMemento)
	 */
	public void saveState(IMemento memento) {
		super.saveState(memento);
		memento.putString(ShellID.MEMENTO_SHELL_VIEW_NAME, getPartName());
		memento.putString(ShellID.MEMENTO_SHELL_VIEW_DESCRIPTOR_NAME, shellFacade.getShellDescriptor().getId());
	}

	/**
	 * @return the <code>ShellViewer</code> of this <code>ShellView</code>
	 */
	public IShellViewer getShellViewer() {
		return shellViewer;
	}

	/**
	 * Action that allows renaming this <code>ShellView</code>.
	 * 
	 * @author Stefan Reichert
	 */
	private class RenameShellAction extends Action {

		/**
		 * Constrcutor for <code>RenameShellAction</code>
		 */
		public RenameShellAction() {
			super();
			setText("Rename Shell View");
			setToolTipText("Renames this Shell View");
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(ShellPlugin.getDefault(), "img/shellRename.gif"));
		}

		/**
		 * @see org.eclipse.jface.action.Action#run()
		 */
		public void run() {
			InputDialog inputDialog = new InputDialog(getSite().getShell(), "Rename Shell View", "Please enter a new name for this Shell View.", getPartName(),
					null);
			if(inputDialog.open() == Dialog.OK) {
				setPartName(inputDialog.getValue());
				ShellView.this.firePropertyChange(PROP_TITLE);
			}
		}
	};

	/**
	 * Action that allows setting the underlaying <code>IShellDescriptor</code>
	 * of this <code>ShellView</code>.
	 * 
	 * @author Stefan Reichert
	 */
	private class SetShellDescriptorAction extends Action {

		/**
		 * The corresponding <code>IShellDescriptor</code>.
		 */
		private IShellDescriptor shellDescriptor;

		/**
		 * Constrcutor for <code>SetShellDescriptorAction</code>
		 */
		public SetShellDescriptorAction(IShellDescriptor shellDescriptor) {
			super(shellDescriptor.getName(), Action.AS_CHECK_BOX);
			this.shellDescriptor = shellDescriptor;
			setToolTipText("Restarts this Shell View with the " + shellDescriptor.getName() + " Shell");
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(ShellPlugin.getDefault(), "img/shellDescriptor.gif"));
		}

		/**
		 * @see org.eclipse.jface.action.Action#run()
		 */
		public void run() {
			final IShellDescriptor oldShellDescriptor = getShellViewer().getShellFacade().getShellDescriptor();
			try {
				getShellViewer().getShellFacade().restart(shellDescriptor);
			}
			catch (ShellActivationException shellActivationException) {
				shellLogger.error("Shell <" + shellDescriptor.getName() + "> could not be activated.", shellActivationException, true);
				processFallback(oldShellDescriptor);
			}
			catch (ShellDeactivationException shellDeactivationException) {
				shellLogger.error("Shell <" + shellDescriptor.getName() + "> could not be activated.", shellDeactivationException, true);
				processFallback(oldShellDescriptor);
			}
		}

		/**
		 * Processes the fallback if desired shell cannot be started.
		 */
		private void processFallback(IShellDescriptor oldShellDescriptor) {
			shellLogger.debug("Trying to restart former Shell <" + oldShellDescriptor.getName() + ">");
			try {
				getShellViewer().getShellFacade().restart(oldShellDescriptor);
			}
			catch (ShellDeactivationException shellDeactivationException) {
				shellLogger.error("Shell <" + oldShellDescriptor.getName() + "> could not be activated.", shellDeactivationException, true);
				activateDefaultShell();
			}
			catch (ShellActivationException shellActivationException) {
				shellLogger.error("Shell <" + oldShellDescriptor.getName() + "> could not be activated.", shellActivationException, true);
				activateDefaultShell();
			}
		}

		/**
		 * @return The corresponding <code>IShellDescriptor</code>
		 */
		public IShellDescriptor getShellDescriptor() {
			return shellDescriptor;
		}
	};

}
