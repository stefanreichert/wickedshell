/*
 * ShellViewUtil.java
 * 
 * Copyright 2005-2007 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.wickedshell.ShellID;
import net.sf.wickedshell.ShellPlugin;
import net.sf.wickedshell.facade.descriptor.IShellDescriptor;
import net.sf.wickedshell.preferences.PreferenceHelper;
import net.sf.wickedshell.ui.shell.ShellView;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListDialog;

/**
 * Provides a method to select from all currently open <code>ShellViews</code>.
 * 
 * @author Stefan Reichert
 */
public class ShellViewUtil {

	public interface IShellViewFilter {
		boolean isApplicable(ShellView shellView);
	}

	public static final ShellView selectTargetFromAvailable(String action) {
		return selectTargetFromAvailable(action, null, false);
	}

	public static final ShellView selectTargetFromAvailable(String action, boolean provideNewShell) {
		return selectTargetFromAvailable(action, null, provideNewShell);
	}

	@SuppressWarnings("unchecked")
	public static final ShellView selectTargetFromAvailable(String action, final IShellViewFilter filter, boolean provideNewShell) {
		ShellView targetShellView = null;
		List applicableRegisteredShellViews = new ArrayList(Arrays.asList(ShellPlugin.getDefault().getRegisteredShellViews()));
		if(filter != null) {
			CollectionUtils.filter(applicableRegisteredShellViews, new Predicate() {
				/**
				 * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
				 */
				public boolean evaluate(Object object) {
					ShellView shellView = (ShellView) object;
					return filter.isApplicable(shellView);
				}
			});
		}
		if(provideNewShell) {
			applicableRegisteredShellViews.add(PreferenceHelper.getActiveShellDescriptor());
		}
		if(applicableRegisteredShellViews.size() == 1) {
			targetShellView = (ShellView) applicableRegisteredShellViews.get(0);
		}
		else if(applicableRegisteredShellViews.size() != 0) {
			ListDialog listDialog = new ListDialog(Display.getDefault().getActiveShell());
			listDialog.setContentProvider(new ArrayContentProvider());
			listDialog.setLabelProvider(new LabelProvider() {
				/**
				 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
				 */
				public String getText(Object element) {
					StringBuffer buffer = new StringBuffer();
					if(element instanceof ShellView) {
						ShellView shellView = (ShellView) element;
						buffer.append(shellView.getPartName());
						buffer.append(" [");
						buffer.append(shellView.getContentDescription());
						buffer.append("]");
					}
					else {
						IShellDescriptor shellDescriptor = (IShellDescriptor) element;
						buffer.append("New Shell [");
						buffer.append(shellDescriptor.getName());
						buffer.append("]");
					}
					return buffer.toString();
				}
			});
			listDialog.setTitle("Wicked Shell - " + action);
			listDialog.setHelpAvailable(false);
			listDialog.setMessage("Please choose target from the available Shells.");
			listDialog.setInput(applicableRegisteredShellViews);
			listDialog.open();
			Object[] result = listDialog.getResult();
			if(result != null) {
				if(listDialog.getResult()[0] instanceof ShellView) {
					targetShellView = (ShellView) listDialog.getResult()[0];
				}
				else {
					try {
						targetShellView = (ShellView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ShellID.SHELL_VIEW_ID,
								String.valueOf(System.currentTimeMillis()), IWorkbenchPage.VIEW_ACTIVATE);
					}
					catch (PartInitException exception) {
						exception.printStackTrace();
					}
				}
			}
		}
		return targetShellView;
	}

	public static final boolean isTargetAvailable() {
		return ShellPlugin.getDefault().getRegisteredShellViews().length > 0;
	}

	public static final boolean isTargetForExecutableAvailable(File file) {
		Assert.isTrue(file.exists());
		ShellView[] shellViews = ShellPlugin.getDefault().getRegisteredShellViews();
		int index = 0;
		boolean targetAvailable = false;
		while (!targetAvailable && index < shellViews.length) {
			targetAvailable = shellViews[index].getShellViewer().getShellFacade().getShellDescriptor().isExecutable(file);
			index++;
		}
		return targetAvailable;
	}

	@SuppressWarnings("unchecked")
	public static final List getTargetableExecutableFiles() {
		ShellView[] shellViews = ShellPlugin.getDefault().getRegisteredShellViews();
		Set executableFiles = new HashSet();
		for (int index = 0; index < shellViews.length; index++) {
			executableFiles.addAll(Arrays.asList(shellViews[index].getShellViewer().getShellFacade().getShellDescriptor().getExecutableFiles(true)));
		}
		return new ArrayList(executableFiles);
	}
}
