/*
 * DomainID.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.domain;

/**
 * @author Stefan Reichert
 * @since 22.10.2006
 */
public class DomainID {

	/** The name of the <code>File</code> containing the list of batchfiles. */
	public static final String BATCH_FILES_FILE = "batchFiles.xml";

	/** The name of the <code>File</code> containing the list of batchfiles. */
	public static final String STATIC_COMPLETIONS_FILE = "staticCompletions.xml";

	/** The name of the <code>File</code> containing the command history. */
	public static final String COMMAND_HISTORY_FILE = "commandHistory.xml";

	/** The name of the <code>File</code> containing the shell style. */
	public static final String SHELL_STYLE_FILE = "shellStyle.xml";

	/**
	 * The name of the <code>File</code> containing the shell descriptor
	 * configuration.
	 */
	public static final String SHELL_DESCRIPTOR_CONFIGURATION_FILE = "shellDescriptorConfiguration.xml";

	/** The name of the <code>File</code> containing the xml shell descriptors. */
	public static final String XML_SHELL_DESCRIPTORS_FILE = "xmlShellDesciptors.xml";

	/** Descriptor id for Bash (Linux) Shell. */
	public static final String BASH_SHELL_DESCRIPTOR_ID = "net.sf.wickedshell.shell.bash";

	/** Descriptor id for Cmd.exe Shell. */
	public static final String CMD_SHELL_DESCRIPTOR_ID = "net.sf.wickedshell.shell.cmd";

	/** Descriptor id for Command.com Shell. */
	public static final String COMMAND_SHELL_DESCRIPTOR_ID = "net.sf.wickedshell.shell.command";

	/** Descriptor id for Cygwin Bash (Windows) Shell. */
	public static final String CYGWIN_BASH_SHELL_DESCRIPTOR_ID = "net.sf.wickedshell.shell.cygwin";

	/** Descriptor id for MSys SH (Windows) Shell. */
	public static final String MSYS_SH_SHELL_DESCRIPTOR_ID = "net.sf.wickedshell.shell.msys";

	/** Value for the slash <i>path delimter</i>. */
	public static final String SLASH_VALUE = "SLASH";

	/** Value for the backslash <i>path delimter</i>. */
	public static final String BACKSLASH_VALUE = "BACKSLASH";

	/** Value for the cariage return and linefeed <i>line feed string</i>. */
	public static final String CR_LF_VALUE = "CR/LF";

	/** Value for the linefeed only <i>line feed string</i>. */
	public static final String LF_ONLY_VALUE = "LF_ONLY";

	/** Character encoding US ASCII. */
	public static final String ENCODING_US_ASCII = "US-ASCII";

	/** Character encoding ISO-8859-1. */
	public static final String ENCODING_ISO_8859_1 = "ISO-8859-1";

	/** Character encoding UTF 8. */
	public static final String ENCODING_UTF_8 = "UTF-8";

	/** Character encoding UTF 16 - Little Endian. */
	public static final String ENCODING_UTF_16_LE = "UTF-16LE";

	/** Character encoding UTF 16 - Big Endian. */
	public static final String ENCODING_UTF_16_BE = "UTF-16BE";

	/** Character encoding Windows 1252. */
	public static final String ENCODING_CP_1252 = "CP1252";

	/** Character encoding Windows 437. */
	public static final String ENCODING_CP_437 = "CP437";

	/** Default OS character encoding. */
	public static final String DEFAULT_OS_CHARACTER_ENCODING = "Default";

	/**
	 * Constructor for ShellID.
	 */
	private DomainID() {
		// Avoid instantiation
		super();
	}
}
