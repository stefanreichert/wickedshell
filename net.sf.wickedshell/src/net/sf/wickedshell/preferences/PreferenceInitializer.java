/*
 * PreferenceInitializer.java
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
import net.sf.wickedshell.facade.descriptor.IShellDescriptor.Manager;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;

/**
 * @author Stefan Reichert
 * @since 21.10.2006
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/**
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		initializeDefaultValue(ShellID.OPEN_SHELL_ERROR_DIALOG_STRING_KEY,
				false);
		initializeDefaultValue(ShellID.OPEN_COMMAND_HISTORY_DIALOG_STRING_KEY,
				true);
		initializeDefaultValue(ShellID.SHOW_CUSTOM_COMMANDS_STRING_KEY, true);
		initializeDefaultValue(ShellID.SHOW_ACTIVE_FOLDER_COMMANDS_STRING_KEY,
				true);
		initializeDefaultValue(ShellID.SHOW_CASCADING_COMPLETIONS_STRING_KEY,
				true);
		initializeDefaultValue(ShellID.SHOW_SYSTEM_PATH_COMMANDS_STRING_KEY,
				false);
		initializeDefaultValue(
				ShellID.SHOW_SYSTEM_ENVIROMENTAL_VALUES_STRING_KEY, true);
		initializeDefaultValue(ShellID.SHELL_DESCRIPTOR_ID_STRING_KEY, Manager
				.getDefaultShellDescriptorId());
		initializeDefaultValue(
				ShellID.USE_WORKSPACE_AS_STARTUP_DIRECTORY_STRING_KEY, true);
		initializeDefaultValue(ShellID.CUSTOM_STARTUP_DIRECTORY_STRING_KEY,
				new String());
		initializeDefaultValue(ShellID.CONFIRM_DETACHED_SHELL_CLOSING_STRING_KEY,
				false);
	}

	/**
	 * Initializes a <i>prefenrence</i> with the given <code>boolean</code>
	 * as default value. If the <i>preference</i> is not yet set, it is set to
	 * the given default value.
	 * 
	 * @param preferenceStoreKey
	 *            The key of the <i>preference</i> to set
	 * @param preferencesStoreValue
	 *            The default value of the <i>preference</i> to set
	 */
	private void initializeDefaultValue(String preferenceStoreKey,
			boolean preferencesStoreValue) {
		ShellPlugin.getDefault().getPreferenceStore().setDefault(
				preferenceStoreKey, preferencesStoreValue);
		if(ShellPlugin.getDefault().getPreferenceStore().getString(
				preferenceStoreKey).equals(new String())) {
			ShellPlugin.getDefault().getPreferenceStore().setToDefault(
					preferenceStoreKey);
		}
	}

	/**
	 * Initializes a <i>prefenrence</i> with the given <code>boolean</code>
	 * as default value. If the <i>preference</i> is not yet set, it is set to
	 * the given default value.
	 * 
	 * @param preferenceStoreKey
	 *            The key of the <i>preference</i> to set
	 * @param preferencesStoreValue
	 *            The default value of the <i>preference</i> to set
	 */
	private void initializeDefaultValue(String preferenceStoreKey,
			String preferencesStoreValue) {
		ShellPlugin.getDefault().getPreferenceStore().setDefault(
				preferenceStoreKey, preferencesStoreValue);
		if(ShellPlugin.getDefault().getPreferenceStore().getString(
				preferenceStoreKey).equals(new String())) {
			ShellPlugin.getDefault().getPreferenceStore().setToDefault(
					preferenceStoreKey);
		}
	}

}
