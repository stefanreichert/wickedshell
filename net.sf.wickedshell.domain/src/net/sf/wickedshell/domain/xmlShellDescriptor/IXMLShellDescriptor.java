/*
 * IXMLShellDescriptor.java
 * 
 * Copyright 2005-2007 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.domain.xmlShellDescriptor;

import net.sf.wickedshell.domain.DomainID;

/**
 * Interface to keep a custom shell descriptor.
 * 
 * @author Stefan Reichert
 */
public interface IXMLShellDescriptor {

	/**
	 * Returns the path of the directory relative to the custom root directory
	 * where to find the binaries of the described shell, or <code>null</code>
	 * if the shell has no custom root directory.
	 * 
	 * @return the binaries directory or or <code>null</code> if the shell has
	 *         no custom root directory
	 */
	String getBinariesDirectory();

	/**
	 * Returns the desired character encoding (e.g. US-ASCII, UTF8 ord Cp1252)
	 * or <i>default</i> if to use the default platform encoding.
	 * 
	 * @return the desired character encoding
	 */
	String getCharacterEncoding();

	/**
	 * Returns the command delimiter when entering multiple commands.
	 * 
	 * @return the command delimiter when entering multiple commands
	 */
	String getCommandDelimiter();

	/**
	 * Returns the cutom root directory of the shell or <code>null</code> if
	 * it does not exist.
	 * 
	 * @return the cutom root directory of the shell or <code>null</code> if
	 *         it does not exist
	 */
	String getCustomRootDirectory();

	/**
	 * Returns the executable file of the described shell. The
	 * <code>String</code> will be executed via <code>Runtime.exec()</code>.
	 * 
	 * @return the executable file of the described shell
	 */
	String getExecutable();

	/**
	 * Returns the file extensions of executable batch files (e.g. *.bat or
	 * *.cmd for cmd.exe).
	 * 
	 * @return the file extensions of executable batch files
	 */
	String[] getExecutableBatchFiles();

	/**
	 * Returns the file extensions of executable non batch files (e.g. *.exe or
	 * *.com for cmd.exe).
	 * 
	 * @return file extensions of executable non batch files
	 */
	String[] getExecutableFiles();

	/**
	 * Returns whether the shell process's <code>InputStream</code> provides
	 * the command written to it's <code>OutputStream</code>.
	 * 
	 * @return whether the written command is provided by the shell process's
	 *         <code>InputStream</code>
	 */
	boolean isExecutedCommandProvided();

	/**
	 * Returns the unique id of this shell.
	 * 
	 * @return the unique id of this shell
	 */
	String getId();

	/**
	 * The <code>String</code> which has to be written to the the shell
	 * process's <code>OutputStream</code> to indicate a line feed.
	 * 
	 * @return the shell specific <code>String</code>which represents a line
	 *         feed
	 */
	String getLineFeedString();

	/**
	 * Returns the human readable name of this shell.
	 * 
	 * @return the human readable name of this shell
	 */
	String getName();

	/**
	 * Returns the names of supported operating systems. The name of an
	 * operating system is the value returned by
	 * <code>System.getProperty("os.name")</code>.
	 * 
	 * @return the names of supported operating systems
	 */
	String[] getOperatingSystems();

	/**
	 * Returns the <code>String</code> that delimits the prompt returned by
	 * shell process's <code>InputStream</code>.
	 * 
	 * @return the <code>String</code> that delimits the shell's prompt
	 */
	String getPathDelimiter();

	/**
	 * The <code>char</code> that seperates the different parts of the path,
	 * either this returns <code>DomainID.SLASH_VALUE</code> or
	 * <code>DomainID.BACKSLASH_VALUE</code>
	 * 
	 * @return the <code>char</code> that seperates the different parts of the
	 *         path
	 */
	String getPathSeparator();

	/**
	 * Returns the separator of the system's <i>path variable</i> elements.
	 * 
	 * @return the separator of the system's <i>path variable</i> elements
	 */
	char getSystemPathSeparator();

	/**
	 * Returns whether the shell process's <code>InputStream</code> provides a
	 * linefeed after a command was written to it's <code>OutputStream</code>.
	 * 
	 * @return whether a linefeed is provided by the shell process's after a
	 *         command is written <code>InputStream</code>
	 */
	boolean isUiLineFeedProvided();

	/**
	 * Sets the path of the directory relative to the custom root directory
	 * where to find the binaries of the described shell, or <code>null</code>
	 * if the shell has no custom root directory.
	 * 
	 * @param binariesDirectory
	 *            The binaries directory or or <code>null</code> if the shell
	 *            has no custom root directory
	 */
	public void setBinariesDirectory(String binariesDirectory);

	/**
	 * Sets the desired character encoding (e.g. US-ASCII, UTF8 ord Cp1252) or
	 * <i>default</i> if to use the default platform encoding.
	 * 
	 * @param characterEncoding
	 *            The desired character encoding
	 */
	public void setCharacterEncoding(String characterEncoding);

	/**
	 * Sets the command delimiter when entering multiple commands.
	 * 
	 * @param commandDelimiter
	 *            The command delimiter when entering multiple commands
	 */
	public void setCommandDelimiter(String commandDelimiter);

	/**
	 * Sets the cutom root directory of the shell or <code>null</code> if it
	 * does not exist.
	 * 
	 * @param customRootDirectory
	 *            The cutom root directory of the shell or <code>null</code>
	 *            if it does not exist
	 */
	public void setCustomRootDirectory(String customRootDirectory);

	/**
	 * Sets the executable file of the described shell. The <code>String</code>
	 * will be executed via <code>Runtime.exec()</code>.
	 * 
	 * @param executable
	 *            The executable file of the described shell
	 */
	public void setExecutable(String executable);

	/**
	 * Sets the file extensions of executable batch files (e.g. *.exe or *.com
	 * for cmd.exe).
	 * 
	 * @param executableFiles
	 *            The file extensions of executable batch files
	 */
	public void setExecutableBatchFiles(String[] executableBatchFiles);

	/**
	 * Sets the file extensions of executable non batch files (e.g. *.exe or
	 * *.com for cmd.exe).
	 * 
	 * @param executableFiles
	 *            The file extensions of executable non batch files
	 */
	public void setExecutableFiles(String[] executableFiles);

	/**
	 * Sets whether the shell process's <code>InputStream</code> provides the
	 * command written to it's <code>OutputStream</code>.
	 * 
	 * @param executedCommandProvided
	 *            Whether the written command is provided by the shell process's
	 *            <code>InputStream</code>
	 */
	public void setExecutedCommandProvided(boolean executedCommandProvided);

	/**
	 * Sets the <code>String</code> which has to be written to the the shell
	 * process's <code>OutputStream</code> to indicate a line feed.
	 * 
	 * @param lineFeedString
	 *            The shell specific <code>String</code>which represents a
	 *            line feed
	 */
	public void setLineFeedString(String lineFeedString);

	/**
	 * Sets the human readable name of this shell.
	 * 
	 * @param name
	 *            The human readable name of this shell
	 */
	public void setName(String name);

	/**
	 * Sets the names of supported operating systems. The name of an operating
	 * system is the value returned by
	 * <code>System.getProperty("os.name")</code>.
	 * 
	 * @param operatingSystems
	 *            The names of supported operating systems
	 */
	public void setOperatingSystems(String[] operatingSystems);

	/**
	 * Sets the <code>String</code> that delimits the prompt returned by shell
	 * process's <code>InputStream</code>.
	 * 
	 * @param pathDelimiter
	 *            The <code>String</code> that delimits the shell's prompt
	 */
	public void setPathDelimiter(String pathDelimiter);

	/**
	 * Sets the <code>char</code> that seperates the different parts of the
	 * path, either this is <code>DomainID.SLASH_VALUE</code> or
	 * <code>DomainID.BACKSLASH_VALUE</code>
	 * 
	 * @param pathSeparator
	 *            The <code>char</code> that seperates the different parts of
	 *            the path
	 */
	public void setPathSeparator(String pathSeparator);

	/**
	 * Sets the separator of the system's <i>path variable</i> elements.
	 * 
	 * @param systemPathSeparator
	 *            The separator of the system's <i>path variable</i> elements
	 */
	public void setSystemPathSeparator(char systemPathSeparator);

	/**
	 * Sets whether the shell process's <code>InputStream</code> provides a
	 * linefeed after a command was written to it's <code>OutputStream</code>.
	 * 
	 * @param uiLineFeedProvided
	 *            Whether a linefeed is provided by the shell process's after a
	 *            command is written <code>InputStream</code>
	 */
	public void setUiLineFeedProvided(boolean uiLineFeedProvided);

	/**
	 * The factory for creating new <code>IXMLShellDescriptor</code>s using
	 * an internal implementation.
	 * 
	 * @author Stefan Reichert
	 * @since 19.08.2006
	 */
	class Factory {

		/**
		 * Constructor for IXMLShellDescriptor.
		 */
		public static IXMLShellDescriptor newInstance(
				net.sf.wickedshell.domain.xmlShellDescriptor.XmlShellDescriptor staticXMLShellDescriptor) {
			XMLShellDescriptor xmlShellDescriptor = new XMLShellDescriptor(staticXMLShellDescriptor
					.getId());
			// Transfer the data
			xmlShellDescriptor.setName(staticXMLShellDescriptor.getName());
			xmlShellDescriptor
					.setBinariesDirectory(staticXMLShellDescriptor.getBinariesDirectory());
			xmlShellDescriptor
					.setCharacterEncoding(staticXMLShellDescriptor.getCharacterEncoding());
			xmlShellDescriptor.setCommandDelimiter(staticXMLShellDescriptor.getCommandDelimiter());
			xmlShellDescriptor.setCustomRootDirectory(staticXMLShellDescriptor
					.getCustomRootDirectory());
			xmlShellDescriptor.setExecutable(staticXMLShellDescriptor.getExecutable());
			xmlShellDescriptor.setExecutableBatchFiles(staticXMLShellDescriptor
					.getExecutableBatchFilesArray());
			xmlShellDescriptor.setExecutableFiles(staticXMLShellDescriptor
					.getExecutableFilesArray());
			xmlShellDescriptor.setExecutedCommandProvided(Boolean.valueOf(
					staticXMLShellDescriptor.getExecutedCommandProvided()).booleanValue());
			xmlShellDescriptor.setLineFeedString(staticXMLShellDescriptor.getLineFeedString());
			xmlShellDescriptor.setOperatingSystems(staticXMLShellDescriptor
					.getOperatingSystemsArray());
			xmlShellDescriptor.setPathDelimiter(staticXMLShellDescriptor.getPathDelimiter());
			xmlShellDescriptor.setPathSeparator(staticXMLShellDescriptor.getPathSeparator());
			xmlShellDescriptor.setSystemPathSeparator(staticXMLShellDescriptor
					.getSystemPathSeparator().charAt(0));
			xmlShellDescriptor.setUiLineFeedProvided(Boolean.valueOf(
					staticXMLShellDescriptor.getUiLineFeedProvided()).booleanValue());
			return xmlShellDescriptor;
		}

		/**
		 * Constructor for IXMLShellDescriptor.
		 */
		public static IXMLShellDescriptor newInstance(String id) {
			XMLShellDescriptor xmlShellDescriptor = new XMLShellDescriptor(id);
			xmlShellDescriptor.setExecutableBatchFiles(new String[0]);
			xmlShellDescriptor.setExecutableFiles(new String[0]);
			xmlShellDescriptor.setOperatingSystems(new String[0]);
			xmlShellDescriptor.setLineFeedString(DomainID.CR_LF_VALUE);
			xmlShellDescriptor.setPathSeparator(DomainID.BACKSLASH_VALUE);
			xmlShellDescriptor.setCharacterEncoding(DomainID.DEFAULT_OS_CHARACTER_ENCODING);
			return xmlShellDescriptor;
		}
	}

	public class XMLShellDescriptor implements IXMLShellDescriptor {

		private String binariesDirectory;

		private String characterEncoding;

		private String commandDelimiter;

		private String executable;

		private String[] executableFiles;

		private String[] executableBatchFiles;

		private String id;

		private String lineFeedString;

		private String name;

		private String[] operatingSystems;

		private String pathDelimiter;

		private String pathSeparator;

		private char systemPathSeparator;

		private String customRootDirectory;

		private boolean executedCommandProvided;

		private boolean uiLineFeedProvided;

		/**
		 * Constructor for <code>XMLShellDescriptor</code>
		 * 
		 * @param id
		 *            The unique id of this shell
		 */
		public XMLShellDescriptor(String id) {
			super();
			this.id = id;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#getBinariesDirectory()
		 */
		public String getBinariesDirectory() {
			return binariesDirectory;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#getCharacterEncoding()
		 */
		public String getCharacterEncoding() {
			return characterEncoding;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#getCommandDelimiter()
		 */
		public String getCommandDelimiter() {
			return commandDelimiter;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#getCustomRootDirectory()
		 */
		public String getCustomRootDirectory() {
			return customRootDirectory;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#getExecutable()
		 */
		public String getExecutable() {
			return executable;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#getExecutableBatchFiles()
		 */
		public String[] getExecutableBatchFiles() {
			return executableBatchFiles;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#getExecutableFiles()
		 */
		public String[] getExecutableFiles() {
			return executableFiles;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#isExecutedCommandProvided()
		 */
		public boolean isExecutedCommandProvided() {
			return executedCommandProvided;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#getId()
		 */
		public String getId() {
			return id;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#getLineFeedString()
		 */
		public String getLineFeedString() {
			return lineFeedString;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#getName()
		 */
		public String getName() {
			return name;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#getOperatingSystems()
		 */
		public String[] getOperatingSystems() {
			return operatingSystems;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#getPathDelimiter()
		 */
		public String getPathDelimiter() {
			return pathDelimiter;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#getPathSeparator()
		 */
		public String getPathSeparator() {
			return pathSeparator;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#getSystemPathSeparator()
		 */
		public char getSystemPathSeparator() {
			return systemPathSeparator;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#isUiLineFeedProvided()
		 */
		public boolean isUiLineFeedProvided() {
			return uiLineFeedProvided;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#setBinariesDirectory(java.lang.String)
		 */
		public void setBinariesDirectory(String binariesDirectory) {
			this.binariesDirectory = binariesDirectory;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#setCharacterEncoding(java.lang.String)
		 */
		public void setCharacterEncoding(String characterEncoding) {
			this.characterEncoding = characterEncoding;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#setCommandDelimiter(java.lang.String)
		 */
		public void setCommandDelimiter(String commandDelimiter) {
			this.commandDelimiter = commandDelimiter;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#setCustomRootDirectory(java.lang.String)
		 */
		public void setCustomRootDirectory(String customRootDirectory) {
			this.customRootDirectory = customRootDirectory;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#setExecutable(java.lang.String)
		 */
		public void setExecutable(String executable) {
			this.executable = executable;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#setExecutableBatchFiles(java.lang.String[])
		 */
		public void setExecutableBatchFiles(String[] executableBatchFiles) {
			this.executableBatchFiles = executableBatchFiles;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#setExecutableFiles(java.lang.String[])
		 */
		public void setExecutableFiles(String[] executableFiles) {
			this.executableFiles = executableFiles;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#setExecutedCommandProvided(boolean)
		 */
		public void setExecutedCommandProvided(boolean executedCommandProvided) {
			this.executedCommandProvided = executedCommandProvided;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#setLineFeedString(java.lang.String)
		 */
		public void setLineFeedString(String lineFeedString) {
			this.lineFeedString = lineFeedString;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#setName(java.lang.String)
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#setOperatingSystems(java.lang.String[])
		 */
		public void setOperatingSystems(String[] operatingSystems) {
			this.operatingSystems = operatingSystems;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#setPathDelimiter(java.lang.String)
		 */
		public void setPathDelimiter(String pathDelimiter) {
			this.pathDelimiter = pathDelimiter;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#setPathSeparator(String)
		 */
		public void setPathSeparator(String pathSeparator) {
			this.pathSeparator = pathSeparator;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#setSystemPathSeparator(char)
		 */
		public void setSystemPathSeparator(char systemPathSeparator) {
			this.systemPathSeparator = systemPathSeparator;
		}

		/**
		 * @see net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor#setUiLineFeedProvided(boolean)
		 */
		public void setUiLineFeedProvided(boolean uiLineFeedProvided) {
			this.uiLineFeedProvided = uiLineFeedProvided;
		}
	}
}
