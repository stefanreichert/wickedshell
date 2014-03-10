/*
 * IShellDescriptorProperties.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.domain.configuration;


public interface IShellDescriptorProperties {

	/**
	 * Gets the id of the <code>IShellDescriptor</code> this command was executed in.
	 */
	String getShellDescriptorId();

	/**
	 * Sets the root directory.
	 */
	void setRootDirectory(String rootDirectory);

	/**
	 * Sets whether descriptor is default for OS.
	 */
	void setOSDefault(boolean isOSDefault);

	/**
	 * Gets the root directory.
	 */
	String getRootDirectory();

	/**
	 * Gets the executed command.
	 */
	boolean isOSDefault();

	/**
	 * The factory for creating new <code>ICompletions</code> using an internal implementation.
	 * 
	 * @author Stefan Reichert
	 * @since 19.08.2006
	 */
	class Factory {

		/**
		 * Constructor for ShellDescriptorProperties.
		 */
		public static IShellDescriptorProperties newInstance(
				String shellDescriptorId,
				String rootDirectory,
				boolean isOSDefault) {
			return new ShellDescriptorProperties(shellDescriptorId, rootDirectory, isOSDefault);
		}

		/**
		 * Constructor for ShellDescriptorProperties.
		 */
		public static IShellDescriptorProperties newInstance(
				net.sf.wickedshell.domain.configuration.ShellDescriptorProperties descriptorProperties) {
			return new ShellDescriptorProperties(descriptorProperties.getShellDescriptorId(),
					descriptorProperties.getRootDirectory(), Boolean.valueOf(
							descriptorProperties.getIsOSDefault()).booleanValue());
		}
	}

	/**
	 * Internal implementation for <code>IShellDescriptorProperties</code>.
	 * 
	 * @author Stefan Reichert
	 * @since 19.08.2006
	 */
	public final class ShellDescriptorProperties implements IShellDescriptorProperties {

		/** The id of the <code>IShellDescriptor</code> this command was executed in. */
		private final String shellDescriptorId;

		/** The root directory. */
		private String rootDirectory;

		/** Flag whether descriptor is default for the OS. */
		private boolean isOSDefault;

		/**
		 * Constructor for IShellDescriptorProperties.
		 * 
		 * @param command
		 *        The command
		 * @param shellDescriptorId
		 *        The id of the <code>IShellDescriptor</code> this command was executed in
		 */
		private ShellDescriptorProperties(
				String shellDescriptorId,
				String rootDirectory,
				boolean isOSDefault) {
			super();
			this.shellDescriptorId = shellDescriptorId;
			this.rootDirectory = rootDirectory;
			this.isOSDefault = isOSDefault;
		}

		/**
		 * @see net.sf.wickedshell.domain.configuration.IShellDescriptorProperties#getRootDirectory()
		 */
		public String getRootDirectory() {
			return rootDirectory;
		}

		/**
		 * @see net.sf.wickedshell.domain.configuration.IShellDescriptorProperties#getShellDescriptorId()
		 */
		public String getShellDescriptorId() {
			return shellDescriptorId;
		}

		/**
		 * @see net.sf.wickedshell.domain.configuration.IShellDescriptorProperties#isOSDefault()
		 */
		public boolean isOSDefault() {
			return isOSDefault;
		}

		/**
		 * @see net.sf.wickedshell.domain.configuration.IShellDescriptorProperties#setOSDefault(boolean)
		 */
		public void setOSDefault(boolean isOSDefault) {
			this.isOSDefault = isOSDefault;
		}

		/**
		 * @see net.sf.wickedshell.domain.configuration.IShellDescriptorProperties#setRootDirectory(java.lang.String)
		 */
		public void setRootDirectory(String rootDirectory) {
			this.rootDirectory = rootDirectory;
		}
		
		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object object) {
			if (object instanceof ShellDescriptorProperties) {
				ShellDescriptorProperties shellDescriptorProperties = (ShellDescriptorProperties) object;
				return this.shellDescriptorId.equals(shellDescriptorProperties.getShellDescriptorId());
			}
			return false;
		}
		
		/**
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() {
			return shellDescriptorId.hashCode();
		}
	}
}
