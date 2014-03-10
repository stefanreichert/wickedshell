/*
 * ICommandDescriptor.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.domain.command;

/**
 * Interface to keep an executed cammand for the command history.
 * 
 * @author Stefan Reichert
 * @since 10.08.2006
 */
public interface ICommandDescriptor {

	/**
	 * Gets the executed command.
	 */
	String getCommand();

	/**
	 * Gets the id of the <code>IShellDescriptor</code> this command was executed in.
	 */
	String getShellDescriptorId();

	/**
	 * The factory for creating new <code>ICompletions</code> using an internal implementation.
	 * 
	 * @author Stefan Reichert
	 * @since 19.08.2006
	 */
	class Factory {

		/**
		 * Constructor for CommandDescriptor.
		 */
		public static ICommandDescriptor newInstance(String command, String shellDescriptorId) {
			return new CommandDescriptor(command, shellDescriptorId);
		}

		/**
		 * Constructor for CommandDescriptor.
		 */
		public static ICommandDescriptor newInstance(
				net.sf.wickedshell.domain.command.CommandDescriptor commandDescriptor) {
			return new CommandDescriptor(commandDescriptor.getCommand(), commandDescriptor
					.getShellDescriptorId());
		}
	}

	/**
	 * Internal implementation for <code>ICommandDescriptor</code>.
	 * 
	 * @author Stefan Reichert
	 * @since 19.08.2006
	 */
	public final class CommandDescriptor implements ICommandDescriptor {

		/** The command. */
		private final String command;

		/** The id of the <code>IShellDescriptor</code> this command was executed in. */
		private final String shellDescriptorId;

		/**
		 * Constructor for ICommandDescriptor.
		 * 
		 * @param command
		 *        The command
		 * @param shellDescriptorId
		 *        The id of the <code>IShellDescriptor</code> this command was executed in
		 */
		private CommandDescriptor(String command, String shellDescriptorId) {
			super();
			this.command = command;
			this.shellDescriptorId = shellDescriptorId;
		}

		/**
		 * @see net.sf.wickedshell.domain.command.ICommandDescriptor#getCommand()
		 */
		public String getCommand() {
			return command;
		}

		/**
		 * @see net.sf.wickedshell.domain.command.ICommandDescriptor#getShellDescriptorId()
		 */
		public String getShellDescriptorId() {
			return shellDescriptorId;
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object object) {
			if (object instanceof CommandDescriptor) {
				CommandDescriptor commandDescriptor = (CommandDescriptor) object;
				return command.equals(commandDescriptor.getCommand());
			}
			return false;
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() {
			return command.hashCode();
		}
	}
}
