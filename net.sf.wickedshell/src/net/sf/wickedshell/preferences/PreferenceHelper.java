/*
 * PreferenceHelper.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.preferences;

import net.sf.wickedshell.ShellID;
import net.sf.wickedshell.ShellPlugin;
import net.sf.wickedshell.facade.descriptor.IShellDescriptor;

import org.eclipse.core.runtime.Platform;

/**
 * @author Stefan Reichert
 * @since 07.05.2006
 */
public class PreferenceHelper {

	/**
	 * Gets the <code>IShellDescriptor</code> for the id selected in the preferences.
	 */
	public static final IShellDescriptor getActiveShellDescriptor() {
		String shellDescriptorId = ShellPlugin.getDefault().getPreferenceStore().getString(
				ShellID.SHELL_DESCRIPTOR_ID_STRING_KEY);
		return IShellDescriptor.Manager.getShellDescriptor(shellDescriptorId);
	}

	/**
	 * Gets the startup directory for a shell.
	 */
	public static final String getStartupDirectory() {
		String startupDirectory;
		if (ShellPlugin.getDefault().getPreferenceStore().getBoolean(
				ShellID.USE_WORKSPACE_AS_STARTUP_DIRECTORY_STRING_KEY)) {
			startupDirectory = Platform.getInstanceLocation().getURL().getFile();
		}
		else {
			startupDirectory = ShellPlugin.getDefault().getPreferenceStore().getString(
					ShellID.CUSTOM_STARTUP_DIRECTORY_STRING_KEY);
		}
		return startupDirectory;
	}
}
