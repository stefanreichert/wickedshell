/*
 * ICommandHistory.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.facade.history;

import java.util.ArrayList;
import java.util.List;

import net.sf.wickedshell.domain.DomainPlugin;
import net.sf.wickedshell.domain.command.ICommandDescriptor;
import net.sf.wickedshell.util.ShellLogger;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * Represents the history of the commands executed
 * 
 * @author Stefan Reichert
 * @since 17.10.2006
 */
public interface ICommandHistory {

	/**
	 * Adds a command to the history.
	 * 
	 * @param command
	 *            The command which was executed
	 * @param shellId
	 *            The id of the shell which executed the command
	 */
	void addCommand(String command, String shellId);

	/**
	 * Clears the history.
	 * 
	 * @param shellId
	 *            The id of the shell which history should be cleared
	 */
	void clear(String shellId);

	/**
	 * Gets the command at the given index executed in the given shell.
	 * 
	 * @param index
	 *            The index of the executed command
	 * @param shellId
	 *            The id of the shell which executed the command
	 * @return the command at the given index executed in the given shell
	 */
	String getCommand(int index, String shellId);

	/**
	 * Returns the size of the history for the given shell, that means the count
	 * of entries.
	 * 
	 * @param shellId
	 *            The id of the shell of which the size is requested
	 * @return the size of the history as <code>int</code>
	 */
	int getSize(String shellId);

	/**
	 * Internal implementation of the <code>IShellFacade</code>.
	 * 
	 * @author Stefan Reichert
	 * @since 18.10.2006
	 */
	class Factory {

		/**
		 * Gets the <code>ICommandHistory</code>
		 */
		public static ICommandHistory createInstance() {
			return new CommandHistory();
		}
	}

	/**
	 * Internal implementation for <code>ICommandHistory</code>.
	 * 
	 * @author Stefan Reichert
	 * @since 17.10.2006
	 */
	class CommandHistory implements ICommandHistory {

		/** The <code>Reader</code> of the shell. */
		private final ShellLogger shellLogger = new ShellLogger(CommandHistory.class);

		/**
		 * Constructor for ShellFacade.
		 */
		private CommandHistory() {
			super();
			// private constructor to avoid instantiation
		}

		/**
		 * @see net.sf.wickedshell.facade.history.ICommandHistory#addCommand(java.lang.String,
		 *      java.lang.String)
		 */
		@SuppressWarnings("unchecked")
		public void addCommand(String command, String shellId) {
			try {
				List commandHistory = DomainPlugin.getDefault().getCommandHistory();
				ICommandDescriptor newCommandDescriptor = ICommandDescriptor.Factory.newInstance(command, shellId);
				if(commandHistory.isEmpty() || !newCommandDescriptor.equals(commandHistory.get(0))) {
					// Only add the command if it does not equal the last one to
					// avoid repeatings
					commandHistory.add(0, newCommandDescriptor);
				}
			}
			catch (Exception exception) {
				shellLogger.error(exception.getMessage(), exception);
			}
		}

		/**
		 * @see net.sf.wickedshell.facade.history.ICommandHistory#clear()
		 */
		public void clear(final String shellId) {
			try {
				List<ICommandDescriptor> commandHistory = DomainPlugin.getDefault().getCommandHistory();
				CollectionUtils.filter(commandHistory, new Predicate() {
					/**
					 * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
					 */
					public boolean evaluate(Object object) {
						ICommandDescriptor commandDescriptor = (ICommandDescriptor) object;
						return !commandDescriptor.getShellDescriptorId().equals(shellId);
					}
				});
			}
			catch (Exception exception) {
				shellLogger.error(exception.getMessage(), exception);
			}
		}

		/**
		 * @see net.sf.wickedshell.facade.history.ICommandHistory#getCommand(int,
		 *      java.lang.String)
		 */
		public String getCommand(int index, final String shellId) {
			List<ICommandDescriptor> commandHistory = new ArrayList<ICommandDescriptor>();
			try {
				CollectionUtils.select(DomainPlugin.getDefault().getCommandHistory(), new Predicate() {
					/**
					 * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
					 */
					public boolean evaluate(Object object) {
						ICommandDescriptor commandDescriptor = (ICommandDescriptor) object;
						return commandDescriptor.getShellDescriptorId().equals(shellId);
					}
				}, commandHistory);
			}
			catch (Exception exception) {
				shellLogger.error(exception.getMessage(), exception);
			}
			ICommandDescriptor commandDescriptor = commandHistory.get(index);
			return commandDescriptor.getCommand();
		}

		/**
		 * @see net.sf.wickedshell.facade.history.ICommandHistory#getSize(java.lang.String)
		 */
		public int getSize(final String shellId) {
			List<ICommandDescriptor> commandHistory = new ArrayList<ICommandDescriptor>();
			try {
				CollectionUtils.select(DomainPlugin.getDefault().getCommandHistory(), new Predicate() {
					/**
					 * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
					 */
					public boolean evaluate(Object object) {
						ICommandDescriptor commandDescriptor = (ICommandDescriptor) object;
						return commandDescriptor.getShellDescriptorId().equals(shellId);
					}
				}, commandHistory);
			}
			catch (Exception exception) {
				shellLogger.error(exception.getMessage(), exception);
			}
			return commandHistory.size();
		}

	}
}