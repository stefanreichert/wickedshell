/*
 * ShellID.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell;

/**
 * @author Stefan Reichert
 * @since 07.05.2006
 */
public class ShellID {

	/** The current version of Wicked Shell. */
	public static final String VERSION = "2.0.7";

	/** PreferenceStore key. */
	public static final String OPEN_SHELL_ERROR_DIALOG_STRING_KEY = "open.shellerrordialog";

	/** PreferenceStore key. */
	public static final String OPEN_COMMAND_HISTORY_DIALOG_STRING_KEY = "open.commandhistorydialog";

	/** PreferenceStore key. */
	public static final String SHOW_CUSTOM_COMMANDS_STRING_KEY = "show.static.completions";

	/** PreferenceStore key. */
	public static final String SHOW_ACTIVE_FOLDER_COMMANDS_STRING_KEY = "show.activefolder.completions";

	/** PreferenceStore key. */
	public static final String SHOW_CASCADING_COMPLETIONS_STRING_KEY = "show.cascading.completions";

	/** PreferenceStore key. */
	public static final String SHOW_SYSTEM_PATH_COMMANDS_STRING_KEY = "show.systempath.completions";

	/** PreferenceStore key. */
	public static final String SHOW_SYSTEM_ENVIROMENTAL_VALUES_STRING_KEY = "show.systemenviromentalvalues.completions";

	/** PreferenceStore key. */
	public static final String SHELL_DESCRIPTOR_ID_STRING_KEY = "shell.descriptor";

	/** PreferenceStore key. */
	public static final String USE_WORKSPACE_AS_STARTUP_DIRECTORY_STRING_KEY = "use.worspace.as.startup.directory";

	/** PreferenceStore key. */
	public static final String CUSTOM_STARTUP_DIRECTORY_STRING_KEY = "custom.startup.directory";

	/** PreferenceStore key. */
	public static final String SHELL_STYLE_STRING_KEY = "shell.style";

	/** PreferenceStore key. */
	public static final String CONFIRM_DETACHED_SHELL_CLOSING_STRING_KEY = "confirm.detached.shell.closing";

	/** Constant of the systempath key. */
	public static final String SYSTEM_PATH_KEY = "java.library.path";

	/** Constant of the systempath key. */
	public static final String OS_KEY = "os.name";

	/** OS constant. */
	public static final String WINDOWS_95_NAME = "Windows 95";

	/** OS constant. */
	public static final String WINDOWS_98_NAME = "Windows 98";

	/** OS constant. */
	public static final String WINDOWS_NT_NAME = "Windows NT";

	/** OS constant. */
	public static final String WINDOWS_2000_NAME = "Windows 2000";

	/** OS constant. */
	public static final String WINDOWS_2003_NAME = "Windows 2003";

	/** OS constant. */
	public static final String WINDOWS_2003_SERVER_NAME = "Windows Server 2003";

	/** OS constant. */
	public static final String WINDOWS_XP_NAME = "Windows XP";

	/** OS constant. */
	public static final String WINDOWS_VISTA_BETA_NAME = "Windows NT (unknown)";

	/** OS constant. */
	public static final String WINDOWS_VISTA_NAME = "Windows Vista";

	/** OS constant. */
	public static final String WINDOWS_7_NAME = "Windows 7";

	/** OS constant. */
	public static final String LINUX_NAME = "Linux";

	/** OS constant. */
	public static final String MAC_OS_X = "MAC OS X";

	/** The array of all supported OSs. */
	public static final String[] SUPPORTED_OS = new String[] { WINDOWS_95_NAME, WINDOWS_98_NAME, WINDOWS_NT_NAME, WINDOWS_2000_NAME, WINDOWS_2003_NAME,
			WINDOWS_2003_SERVER_NAME, WINDOWS_XP_NAME, WINDOWS_VISTA_BETA_NAME, WINDOWS_VISTA_NAME, WINDOWS_7_NAME, LINUX_NAME, MAC_OS_X };

	/** The name of the shellErrorReader. */
	public static final String SHELL_ERROR_READER_NAME = "ShellErrorReader";

	/** The name of the shellReader. */
	public static final String SHELL_READER_NAME = "ShellReader";

	/** The id of the viewer in the <code>BatchView</code>. */
	public static final String BATCH_FILE_DESCRIPTOR_VIEWER_ID = "net.sf.wickedshell.ui.batch.BatchView.viewer";

	/** Batch View menu entry. */
	public static final String BATCH_VIEW_MENU_GROUP_ADD = "add";

	/** Batch View menu entry. */
	public static final String BATCH_VIEW_MENU_GROUP_LISTENTRY = "listentry";

	/** Batch View menu entry. */
	public static final String BATCH_VIEW_MENU_GROUP_GENERAL = "genaral";

	/** Shell View menu entry. */
	public static final String SHELL_VIEW_MENU_GROUP_MANAGE_INTERNAL = "manage_internal";

	/** Shell View menu entry. */
	public static final String SHELL_VIEW_MENU_GROUP_MANAGE_EXTERNAL = "manage_external";

	/** Shell View menu entry. */
	public static final String SHELL_VIEW_MENU_GROUP_GENERAL = "general";

	/** Shell View menu entry. */
	public static final String SHELL_VIEW_MENU_GROUP_EXPORT = "export";

	/** Shell View menu entry. */
	public static final String SHELL_VIEW_SUBMENU_ACTIVE_SHELL = "activeShell";

	/** Shell View menu entry. */
	public static final String SHELL_VIEW_SUBMENU_GROUP_AVAILABLE_SHELLS = "availableShells";

	/** View ID. */
	public static final String SHELL_VIEW_ID = "net.sf.wickedshell.ui.shell.ShellView";

	/** View ID. */
	public static final String BATCH_VIEW_ID = "net.sf.wickedshell.ui.batch.BatchView";

	/** Extension Point ID. */
	public static final String EXTENSION_POINT_ID = "net.sf.wickedshell.shell";

	/** Extension Point ID. */
	public static final String EXTENSION_ATTRIBUTE_DESCRIPTOR = "descriptor";

	/** The name of the <code>File</code> containing the list of batchfiles. */
	public static final String BATCH_FILES_FILE = "batchfiles.xml";

	/** The name of the <code>File</code> containing the list of batchfiles. */
	public static final String STATIC_COMPLETIONS_FILE = "staticCompletions.xml";

	/** The memento key for the shell name. */
	public static final String MEMENTO_SHELL_VIEW_NAME = "shellViewName";

	/** The memento key for the shell descriptor name. */
	public static final String MEMENTO_SHELL_VIEW_DESCRIPTOR_NAME = "shellViewDescriptorName";

	/** File-extension for shell files. */
	public static final String SHELL_FILE_EXTENSION = ".sh";

	/** File-extension for cmd files. */
	public static final String CMD_FILE_EXTENSION = ".cmd";

	/** File-extension for executable files. */
	public static final String EXECUTABLE_FILE_EXTENSION = ".exe";

	/** File-extension for batch files. */
	public static final String BATCH_FILE_EXTENSION = ".bat";

	/** File-extension for command files. */
	public static final String COMMAND_FILE_EXTENSION = ".com";

	/**
	 * Constructor for ShellID.
	 */
	private ShellID() {
		// Avoid instantiation
		super();
	}
}
