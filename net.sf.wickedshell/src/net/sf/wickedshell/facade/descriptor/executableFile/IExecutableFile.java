/*
 * IExecutableFile.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.facade.descriptor.executableFile;

import java.io.Serializable;

/**
 * @author Stefan Reichert
 * @since 13.08.2006
 */
public interface IExecutableFile extends Serializable {

	/**
	 * Gets the description of these type of files.
	 */
	String getDescription();

	/**
	 * Gets the extension of these executable files.
	 */
	String getExtension();

	/**
	 * Defines whether these type of files are text based and should be listable in the Batch Files -
	 * View.
	 */
	boolean isBatchFile();

	/**
	 * The factory for creating new <code>IExecutableFile</code>s using an internal
	 * implementation.
	 * 
	 * @author Stefan Reichert
	 * @since 19.08.2006
	 */
	class Factory {

		/**
		 * Constructor for Completion.
		 */
		public static IExecutableFile newInstance(
				String description,
				String extension,
				boolean isBatchFile) {
			return new ExecutableFile(description, extension, isBatchFile);
		}
	}

	public final class ExecutableFile implements IExecutableFile {
		/** The serial version UID. */
		private static final long serialVersionUID = -5678622454150858672L;

		/** The extension of these executable files. */
		private final String extension;
		/** The description of these type of files. */
		private final String description;
		/**
		 * Defines whether these type of files are text based and should be listable in the Batch
		 * Files - View.
		 */
		private final boolean isBatchFile;

		/**
		 * Constructor for IExecutableFile.
		 * 
		 * @param description
		 *        The extension of these executable files
		 * @param extension
		 *        The description of these type of files
		 * @param isBatchFile
		 *        Defines whether these type of files are text based and should be listable in the
		 *        Batch Files - View
		 */
		private ExecutableFile(String description, String extension, boolean isBatchFile) {
			super();
			this.description = description;
			this.extension = extension;
			this.isBatchFile = isBatchFile;
		}

		/**
		 * @see net.sf.wickedshell.facade.descriptor.executableFile.IExecutableFile#getDescription()
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * @see net.sf.wickedshell.facade.descriptor.executableFile.IExecutableFile#getExtension()
		 */
		public String getExtension() {
			return extension;
		}

		/**
		 * @see net.sf.wickedshell.facade.descriptor.executableFile.IExecutableFile#isBatchFile()
		 */
		public boolean isBatchFile() {
			return isBatchFile;
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object object) {
			if (object instanceof IExecutableFile) {
				IExecutableFile executableFile = (IExecutableFile) object;
				return executableFile.getExtension().equals(extension);
			}
			return false;
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() {
			return extension.hashCode();
		}
	}
}
