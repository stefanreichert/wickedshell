/*
 * IShellFacade.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.facade;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.sf.wickedshell.ShellID;
import net.sf.wickedshell.domain.DomainPlugin;
import net.sf.wickedshell.domain.configuration.IShellDescriptorProperties;
import net.sf.wickedshell.facade.descriptor.IShellDescriptor;
import net.sf.wickedshell.facade.history.ICommandHistory;
import net.sf.wickedshell.facade.io.ShellReader;
import net.sf.wickedshell.facade.io.ShellWriter;
import net.sf.wickedshell.listener.IShellListener;
import net.sf.wickedshell.listener.IShellOutputListener;
import net.sf.wickedshell.preferences.PreferenceHelper;
import net.sf.wickedshell.util.ShellLogger;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;

/**
 * The <code>IShellFacade</code> is a facade for a selected shell which is
 * configured by an <code>IShellDescriptor</code>.
 * 
 * @author Stefan Reichert
 * @since 17.10.2006
 */
public interface IShellFacade {

	/**
	 * Activates this <code>IShellFacade</code> by starting the underlaying
	 * shell and activate the facade.
	 */
	void activate() throws ShellActivationException;

	/**
	 * Activates this <code>IShellFacade</code> by starting the underlaying
	 * shell and activate the facade with the given
	 * <code>IShellDescriptor</code>.
	 * 
	 * @param shellDescriptor
	 *            The <code>IShellDescriptor</code> to use
	 */
	void activate(IShellDescriptor shellDescriptor) throws ShellActivationException;

	/**
	 * Returns whether the underlaying shell is active.
	 * 
	 * @return whether the shell is active
	 */
	boolean isActive();

	/**
	 * Deactivates this <code>IShellFacade</code> by terminating the
	 * underlaying shell and deactivate the facade.
	 */
	void deactivate() throws ShellDeactivationException;

	/**
	 * Restart the underlaying shell.
	 */
	void restart() throws ShellDeactivationException, ShellActivationException;

	/**
	 * Restart the underlaying shell with the given
	 * <code>IShellDescriptor</code>.
	 * 
	 * @param shellDescriptor
	 *            The <code>IShellDescriptor</code> to use
	 */
	void restart(IShellDescriptor shellDescriptor) throws ShellDeactivationException, ShellActivationException;

	/**
	 * Execute the given command.
	 * 
	 * @param command
	 *            The command to execute
	 */
	void executeCommand(String command);

	/**
	 * Execute an interrupt.
	 */
	void executeInterrupt();

	/**
	 * Returns the current path of the shell.
	 * 
	 * @return the current path of the shell.
	 */
	String getCurrentPath();

	/**
	 * Returns the <code>IShellDescriptor</code> which configures this facade.
	 * 
	 * @return the configuring <code>IShellDescriptor</code>
	 */
	IShellDescriptor getShellDescriptor();

	/**
	 * Returns the history of executed commands.
	 * 
	 * @return the <code>ICommandHistory</code> of this facade
	 */
	ICommandHistory getCommandHistory();

	/**
	 * Adds an <code>IShellOutputListener</code> to the facade. The listener
	 * will be notified on shell output.
	 * 
	 * @param shellOutputListener
	 *            The <code>IShellOutputListener</code> to add
	 */
	void addShellOutputListener(IShellOutputListener shellOutputListener);

	/**
	 * Removes an <code>IShellOutputListener</code> to the facade.
	 * 
	 * @param shellOutputListener
	 *            The <code>IShellOutputListener</code> to remove
	 */
	void removeShellOutputListener(IShellOutputListener shellOutputListener);

	/**
	 * Adds an <code>IShellListener</code> to the facade. The listener will be
	 * notified on shell Command.
	 * 
	 * @param shellListener
	 *            The <code>IShellListener</code> to add
	 */
	void addShellListener(IShellListener shellListener);

	/**
	 * Removes an <code>IShellListener</code> to the facade.
	 * 
	 * @param shellListener
	 *            The <code>IShellListener</code> to remove
	 */
	void removeShellListener(IShellListener shellListener);

	/**
	 * Internal implementation of the <code>IShellFacade</code>.
	 * 
	 * @author Stefan Reichert
	 * @since 18.10.2006
	 */
	class Factory {

		/**
		 * Creates an instance of <code>IShellFacade</code>
		 */
		public static IShellFacade createInstance() {
			return new ShellFacade();
		}
	}

	/**
	 * Internal implementation of the <code>IShellFacade</code>.
	 * 
	 * @author Stefan Reichert
	 * @since 18.10.2006
	 */
	class ShellFacade implements IShellFacade {

		/** The <code>Reader</code> of the shell. */
		private final ShellLogger shellLogger = new ShellLogger(ShellFacade.class);

		/** The <code>ICommandHistory</code>. */
		private ICommandHistory commandHistory;

		/** The active <code>IShellDescriptor</code>. */
		private IShellDescriptor shellDescriptor;

		/** Flag whether this facade is active. */
		private boolean active;

		/** The <code>Process</code> of the shell. */
		private Process shellProcess;

		/** The <code>ShellReader</code> for input. */
		private ShellReader shellReader;

		/** The <code>ShellReader</code> for errors. */
		private ShellReader shellErrorReader;

		/** The <code>ShellWriter</code> of the shell. */
		private ShellWriter shellWriter;

		/** The <code>ShellListener</code> of the shell. */
		private IShellOutputListener shellOutputListener;

		/**
		 * The <code>List</code> of <code>IShellListener</code>s of the
		 * shell.
		 */
		private List<IShellListener> shellListeners;

		/**
		 * The <code>List</code> of <code>IShellOutputListeners</code>s of
		 * the shell.
		 */
		private List<IShellOutputListener> shellOutputListeners;

		/** The last line of the shell. */
		private String lastLine;

		/**
		 * Constructor for ShellFacade.
		 */
		private ShellFacade() {
			super();
			// private constructor to avoid instantiation
			commandHistory = ICommandHistory.Factory.createInstance();
			shellListeners = new ArrayList<IShellListener>();
			shellOutputListeners = new ArrayList<IShellOutputListener>();
			lastLine = new String();
			shellOutputListener = new IShellOutputListener() {
				/**
				 * @see net.sf.wickedshell.listener.IShellOutputListener#handleShellOutput(java.lang.String)
				 */
				public void handleShellOutput(final String output) {
					try {
						StringBuffer buffer = new StringBuffer(lastLine);
						buffer.append(output);
						lastLine = buffer.toString();
						BufferedReader bufferedReader = new BufferedReader(new StringReader(buffer.toString()));
						String nextLine = bufferedReader.readLine();
						while (nextLine != null) {
							lastLine = nextLine;
							nextLine = bufferedReader.readLine();
						}
						bufferedReader.close();
						// Delegate to other listeners
						CollectionUtils.forAllDo(shellOutputListeners, new Closure() {
							/**
							 * @see org.apache.commons.collections.Closure#execute(java.lang.Object)
							 */
							public void execute(Object object) {
								IShellOutputListener shellOutputListener = (IShellOutputListener) object;
								shellOutputListener.handleShellOutput(output);
							}
						});
					}
					catch (IOException exception) {
						shellLogger.error(exception.getMessage(), exception);
					}
				}

				/**
				 * @see net.sf.wickedshell.listener.ShellOutputAdapter#handleShellErrorOutput(java.lang.String)
				 */
				public void handleShellErrorOutput(final String errorOutput) {
					try {
						StringBuffer buffer = new StringBuffer(lastLine);
						buffer.append(errorOutput);
						lastLine = buffer.toString();
						BufferedReader bufferedReader = new BufferedReader(new StringReader(buffer.toString()));
						String nextLine = bufferedReader.readLine();
						while (nextLine != null) {
							lastLine = nextLine;
							nextLine = bufferedReader.readLine();
						}
						bufferedReader.close();
						// Delegate to other listeners
						CollectionUtils.forAllDo(shellOutputListeners, new Closure() {
							/**
							 * @see org.apache.commons.collections.Closure#execute(java.lang.Object)
							 */
							public void execute(Object object) {
								IShellOutputListener shellOutputListener = (IShellOutputListener) object;
								shellOutputListener.handleShellErrorOutput(errorOutput);
							}
						});
					}
					catch (IOException exception) {
						shellLogger.error(exception.getMessage(), exception);
					}
					;
				}
			};
		}

		/**
		 * @see net.sf.wickedshell.facade.IShellFacade#activate()
		 */
		public void activate() throws ShellActivationException {
			activate(PreferenceHelper.getActiveShellDescriptor());
		}

		/**
		 * @see net.sf.wickedshell.facade.IShellFacade#activate(net.sf.wickedshell.facade.descriptor.IShellDescriptor)
		 */
		public void activate(IShellDescriptor shellDescriptor) throws ShellActivationException {
			if(shellDescriptor == null) {
				throw new ShellActivationException(new IllegalArgumentException("Wicked Shell needs a Shell for activation which may not be null."));
			}
			if(!active) {
				this.shellDescriptor = shellDescriptor;
				shellLogger.debug("Activating shell [" + shellDescriptor.getName() + "]");
				StringBuffer executableBuffer = new StringBuffer();
				if(shellDescriptor.hasCustomRoot()) {
					IShellDescriptorProperties shellDescriptorProperties = DomainPlugin.getDefault().getShellDescriptorProperties(shellDescriptor.getId());
					if(shellDescriptorProperties.getRootDirectory() == null || shellDescriptorProperties.getRootDirectory().equals("")) {
						throw new ShellActivationException(new IllegalStateException("Shell " + shellDescriptor.getName()
								+ " has a custom root. Please define this root at the preferences."));
					}
					executableBuffer.append(shellDescriptorProperties.getRootDirectory());
					executableBuffer.append(File.separator);
					executableBuffer.append(shellDescriptor.getBinariesDirectory());
					executableBuffer.append(File.separator);
				}
				executableBuffer.append(shellDescriptor.getExecutable());
				try {
					shellProcess = Runtime.getRuntime().exec(executableBuffer.toString(), null, new File(PreferenceHelper.getStartupDirectory()));
				}
				catch (IOException exception) {
					throw new ShellActivationException(exception);
				}
				Runtime.getRuntime().traceMethodCalls(true);
				Runtime.getRuntime().traceInstructions(true);
				// If process is created manager is active
				active = true;
				// Create ShellReaders
				shellLogger.debug("Creating ShellReader for output");
				try {
					shellReader = new ShellReader(ShellID.SHELL_READER_NAME, shellDescriptor, shellProcess.getInputStream());
				}
				catch (UnsupportedEncodingException exception) {
					throw new ShellActivationException(exception);
				}
				shellLogger.debug("Creating ShellReader for error output");
				try {
					shellErrorReader = new ShellReader(ShellID.SHELL_ERROR_READER_NAME, shellDescriptor, shellProcess.getErrorStream(), true);
				}
				catch (UnsupportedEncodingException exception) {
					throw new ShellActivationException(exception);
				}
				// Add the Listener
				shellReader.addShellOutputListener(shellOutputListener);
				shellErrorReader.addShellOutputListener(shellOutputListener);
				// Activate the ShellReaders
				shellReader.activate();
				shellErrorReader.activate();
				// Activate the shellWriter
				shellLogger.debug("Creating ShellWriter for input");
				try {
					shellWriter = new ShellWriter(shellDescriptor, shellProcess.getOutputStream());
				}
				catch (UnsupportedEncodingException exception) {
					throw new ShellActivationException(exception);
				}
				// Notify listeners
				CollectionUtils.forAllDo(shellListeners, new Closure() {
					/**
					 * @see org.apache.commons.collections.Closure#execute(java.lang.Object)
					 */
					public void execute(Object object) {
						IShellListener commandListener = (IShellListener) object;
						commandListener.handleActivation();
					}
				});
			}
		}

		/**
		 * @see net.sf.wickedshell.facade.IShellFacade#deactivate()
		 */
		public void deactivate() throws ShellDeactivationException {
			if(active) {
				shellLogger.debug("Deactivating shell [" + shellDescriptor.getName() + "]");
				shellWriter.deactivate();
				shellReader.deactivate();
				shellErrorReader.deactivate();
				shellProcess.destroy();
				active = false;
				shellDescriptor = null;
				lastLine = new String();
				// Notify listeners
				CollectionUtils.forAllDo(shellListeners, new Closure() {
					/**
					 * @see org.apache.commons.collections.Closure#execute(java.lang.Object)
					 */
					public void execute(Object object) {
						IShellListener commandListener = (IShellListener) object;
						commandListener.handleDeactivation();
					}
				});
			}
		}

		/**
		 * @see net.sf.wickedshell.facade.IShellFacade#executeCommand(java.lang.String)
		 */
		public void executeCommand(final String command) {
			if(active) {
				shellLogger.debug("Executing command [" + command + "]");
				if(shellDescriptor.isExecutedCommandProvided()) {
					shellLogger.debug("Command is also provided by shell; Reset current command in Viewer");
					setCurrentCommand(new String());
				}
				else {
					shellLogger.debug("Command is not provided by shell; Set command [" + command + "] in Viewer");
					setCurrentCommand(command);
				}
				if(command.trim().length() > 0) {
					// Don't add empty commands to the history
					commandHistory.addCommand(command, shellDescriptor.getId());
				}
				// Notify listeners
				CollectionUtils.forAllDo(shellListeners, new Closure() {
					/**
					 * @see org.apache.commons.collections.Closure#execute(java.lang.Object)
					 */
					public void execute(Object object) {
						IShellListener commandListener = (IShellListener) object;
						commandListener.handleCommandExecution(command);
					}
				});
				// Catch static commands
				if("exit".equals(command.toLowerCase())) {
					try {
						deactivate();
					}
					catch (ShellDeactivationException exception) {
						shellLogger.error(exception.getMessage(), exception);
					}
				}
				else if(command.endsWith("!") && shellDescriptor.getExternalShellInvoker() != null) {
					shellDescriptor.getExternalShellInvoker().execute(shellDescriptor, command.substring(0, command.length() - 1), getCurrentPath());
					// Notify listeners
					CollectionUtils.forAllDo(shellListeners, new Closure() {
						/**
						 * @see org.apache.commons.collections.Closure#execute(java.lang.Object)
						 */
						public void execute(Object object) {
							IShellListener commandListener = (IShellListener) object;
							commandListener.handleCommandExecution(command);
						}
					});
				}
				else if(!"cls".equals(command.toLowerCase())) {
					shellWriter.writeCommand(command);
				}
			}
		}

		/**
		 * @see net.sf.wickedshell.facade.IShellFacade#executeInterrupt()
		 */
		public void executeInterrupt() {
			shellLogger.debug("Executing interrupt");
			shellProcess.destroy();
			shellWriter.writeInterrupt();
		}

		/**
		 * Returns the current path of the shell.
		 * 
		 * @return the current path of the shell.
		 */
		public String getCurrentPath() {
			int pathEnd = lastLine.indexOf(shellDescriptor.getPathDelimiter());
			if(pathEnd == -1) {
				// There are occasions where no delimiter is available.
				// In that case, the last line is considered as path.
				return lastLine;
			}
			return lastLine.substring(0, pathEnd);
		}

		/**
		 * @see net.sf.wickedshell.facade.IShellFacade#getCommandHistory()
		 */
		public ICommandHistory getCommandHistory() {
			return commandHistory;
		}

		/**
		 * @see net.sf.wickedshell.facade.IShellFacade#getShellDescriptor()
		 */
		public IShellDescriptor getShellDescriptor() {
			return shellDescriptor;
		}

		/**
		 * @see net.sf.wickedshell.facade.IShellFacade#isActive()
		 */
		public boolean isActive() {
			return false;
		}

		/**
		 * @see net.sf.wickedshell.facade.IShellFacade#restart()
		 */
		public void restart() throws ShellDeactivationException, ShellActivationException {
			restart(getShellDescriptor());
		}

		/**
		 * @see net.sf.wickedshell.facade.IShellFacade#restart(net.sf.wickedshell.facade.descriptor.IShellDescriptor)
		 */
		public void restart(IShellDescriptor shellDescriptor) throws ShellDeactivationException, ShellActivationException {
			deactivate();
			// Notify listeners
			CollectionUtils.forAllDo(shellListeners, new Closure() {
				/**
				 * @see org.apache.commons.collections.Closure#execute(java.lang.Object)
				 */
				public void execute(Object object) {
					IShellListener commandListener = (IShellListener) object;
					commandListener.handleRestart();
				}
			});
			if(shellDescriptor != null) {
				activate(shellDescriptor);
			}
			else {
				activate();
			}
		}

		/**
		 * @see net.sf.wickedshell.facade.IShellFacade#addShellOutputListener(net.sf.wickedshell.listener.IShellOutputListener)
		 */
		public void addShellOutputListener(IShellOutputListener shellOutputListener) {
			shellOutputListeners.add(shellOutputListener);
		}

		/**
		 * @see net.sf.wickedshell.facade.IShellFacade#removeShellOutputListener(net.sf.wickedshell.listener.IShellOutputListener)
		 */
		public void removeShellOutputListener(IShellOutputListener shellOutputListener) {
			shellOutputListeners.remove(shellOutputListener);
		}

		/**
		 * Set the current command of the shell.
		 * 
		 * @param command
		 *            The current command to set
		 */
		private void setCurrentCommand(String command) {
			StringBuffer lastLine = new StringBuffer();
			lastLine.append(getCurrentPath());
			lastLine.append(shellDescriptor.getPathDelimiter());
			lastLine.append(command);
		}

		/**
		 * @see net.sf.wickedshell.facade.IShellFacade#addShellListener(net.sf.wickedshell.listener.IShellListener)
		 */
		public void addShellListener(IShellListener shellListener) {
			shellListeners.add(shellListener);
		}

		/**
		 * @see net.sf.wickedshell.facade.IShellFacade#removeShellListener(net.sf.wickedshell.listener.IShellListener)
		 */
		public void removeShellListener(IShellListener shellListener) {
			shellListeners.remove(shellListener);
		}

	}
}
