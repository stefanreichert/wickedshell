/*
 * IShellDescriptor.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.facade.descriptor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import net.sf.wickedshell.ShellID;
import net.sf.wickedshell.domain.DomainID;
import net.sf.wickedshell.domain.DomainPlugin;
import net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor;
import net.sf.wickedshell.facade.descriptor.command.ICommandProvider;
import net.sf.wickedshell.facade.descriptor.executableFile.IExecutableFile;
import net.sf.wickedshell.facade.descriptor.external.IExternalShellInvoker;
import net.sf.wickedshell.util.ShellLogger;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * Interface encapsulationg all values depending on the OS.
 * 
 * @author Stefan Reichert
 * @since 10.04.2006
 */
public interface IShellDescriptor {

	/**
	 * Returns the name of this shell.
	 * 
	 * @return the name of this shell
	 */
	String getName();

	/**
	 * Returns the shell's executable.
	 * 
	 * @return the shell's executable
	 */
	String getExecutable();

	/**
	 * Gets the character encoding which has to be used when communication with
	 * the shell.
	 * 
	 * @return the character encoding (e.g. UTF-8, US-ASCII, etc.)
	 */
	String getCharacterEncoding();

	/**
	 * Gets the <code>String</code> representing the <i>LineFeed</i> for this
	 * <i>shell</i>.<br>
	 * This <i>LineFeed</i> is attached to each executed command written to
	 * this shell's <code>OutputStream</code>.
	 * 
	 * @return the <code>String</code> representing the <i>LineFeed</i> for
	 *         this <i>shell</i>
	 */
	String getLineFeedString();

	/**
	 * Returns the shell's path delimiter. The given <code>String</code> marks
	 * the end of the current path in the shells UI.
	 * 
	 * @return the shell's path delimiter
	 */
	String getPathDelimiter();

	/**
	 * Returns the separator of the systempath. The given <code>String</code>
	 * is used to separate the different entries of the systempath.
	 * 
	 * @return the separator of the systempath
	 */
	char getSystemPathSeparator();

	/**
	 * Returns the separator of a path used by the shell. The given
	 * <code>String</code> separates the different elements that constitute a
	 * path in the shell.
	 * 
	 * @return the separator used in a path by the shell
	 */
	char getPathSeparator();

	/**
	 * Returns the shell's command delimiter.
	 * 
	 * @return the shell's command delimiter
	 */
	String getCommandDelimiter();

	/**
	 * Returns whether the shell uses the base path defined as preference.<br>
	 * If the shell is shipped with the OS, this should return
	 * <code>false</code>. An installed shell like cygwin or msys has a root
	 * directory and this must be entered as preference. The
	 * <code>CompletionController</code> will use the given String as prefix
	 * to identify the completions for the current path.
	 */
	boolean hasCustomRoot();

	/**
	 * Returns the directory where the binaries of the shell are placed. This is
	 * only necessary for shells having a custom root.
	 */
	String getBinariesDirectory();

	/**
	 * States whether given file is executable in the shell.
	 * 
	 * @param file
	 *            the potetial executable file
	 * @return the shell's path delimiter
	 */
	boolean isExecutable(File file);

	/**
	 * States whether given file is allowed to be listed in the Batch list.
	 * 
	 * @param file
	 *            the potetial batch list file
	 * @return the shell's path delimiter
	 */
	boolean isAllowedForBatchList(File file);

	/**
	 * Returns the shell's enviromental values.
	 * 
	 * @return the shell's enviromental values
	 */
	Properties getEnviromentalValues();

	/**
	 * Returns the <code>IExternalShellInvoker</code> of this descriptor.
	 * 
	 * @return the <code>IExternalShellInvoker</code> of this descriptor or
	 *         <code>null</code> if not supported
	 */
	IExternalShellInvoker getExternalShellInvoker();

	/**
	 * States whether a <i>LineFeed</i> has to be added in the UI after sending
	 * a <i>LineFeed</i> via <code>OutputStream</code>. <br>
	 * For example the <code>CmdShellDescriptor</code> transfers the
	 * <i>newline</i> written to the <code>OutputStream</code> directly to
	 * the <code>InputStream</code>.
	 * 
	 * @return whether a <code>SWT.LF</code> or <code>SWT.CR</code> is
	 *         provided by the shell's <code>OutputStream</code>
	 */
	boolean isUILineFeedProvided();

	/**
	 * States whether an executed command has to be removed from the
	 * <code>Text</code> working as UI. <br>
	 * For example the <code>CmdShellDescriptor</code> transfers the executed
	 * commans written to the <code>OutputStream</code> directly to the
	 * <code>InputStream</code>.
	 * 
	 * @return whether a <code>SWT.LF</code> or <code>SWT.CR</code> is
	 *         provided by the shell's <code>OutputStream</code>
	 */
	boolean isExecutedCommandProvided();

	/**
	 * States whether the current os is supported by the shell.
	 * 
	 * @return whether the current os is supported by the shell
	 */
	boolean isCurrentOSSupported();

	/**
	 * Returns a unique id for this <code>IShellDescriptor</code>.
	 * 
	 * @return the unique id
	 */
	String getId();

	/**
	 * Returns the <code>IExecutableFile</code>s.
	 * 
	 * @param batchFileOnly
	 *            Flag whether only Batch Files are returned
	 * @return the <code>IExecutableFile</code>
	 */
	IExecutableFile[] getExecutableFiles(boolean batchFileOnly);

	/**
	 * Returns the supporting <i>operating systems</i>s.
	 * 
	 * @return the supporting <i>operating systems</i>
	 */
	String[] getOperatingSystems();

	/**
	 * Returns the <code>ICommandProvider</code> of the shell.
	 * 
	 * @return the <code>ICommandProvider</code> of the shell
	 */
	ICommandProvider getCommandProvider();

	/**
	 * The manager of the system's shell.
	 */
	class Manager {

		private static List<IShellDescriptor> staticShellDescriptors;

		private static ShellLogger shellLogger = new ShellLogger(Manager.class);

		/**
		 * Returns all static <code>IShellDescriptor</code>s defined by
		 * extension definitions.
		 * 
		 * @return the <code>List</code> of static
		 *         <code>IShellDescriptors</code>
		 */
		@SuppressWarnings("unchecked")
		public static List getStaticShellDescriptors() {
			if(staticShellDescriptors == null) {
				staticShellDescriptors = new ArrayList(Arrays.asList(readShellDescriptors()));
			}
			return staticShellDescriptors;
		}

		/**
		 * Returns all custom <code>IShellDescriptor</code>s defined by an
		 * XML representation.
		 * 
		 * @return the <code>List</code> of custom
		 *         <code>IShellDescriptors</code>
		 */
		@SuppressWarnings("unchecked")
		public static List<IShellDescriptor> getCustomShellDescriptors() {
			try {
				return (List<IShellDescriptor>) CollectionUtils.collect(DomainPlugin.getDefault().getXMLShellDescriptors(), new Transformer() {
					public Object transform(Object object) {
						return new XMLShellDescriptor((IXMLShellDescriptor) object);
					}

				}, new ArrayList<IShellDescriptor>());
			}
			catch (Throwable throwable) {
				shellLogger.error("Loading custom ShellDescriptors failed.", throwable);
				return new ArrayList();
			}
		}

		/**
		 * Returns all known <code>IShellDescriptor</code>s, static and
		 * custom ones.
		 * 
		 * @return the <code>List</code> of all known
		 *         <code>IShellDescriptors</code>
		 */
		@SuppressWarnings("unchecked")
		public static Collection getKnownShellDescriptors() {
			List knownShellDescriptors = new ArrayList();
			knownShellDescriptors.addAll(getStaticShellDescriptors());
			knownShellDescriptors.addAll(getCustomShellDescriptors());
			return knownShellDescriptors;
		}

		/**
		 * Returns all known <code>IShellDescriptor</code>
		 */

		/**
		 * Reads all existing extensions.
		 */
		@SuppressWarnings("unchecked")
		private static final IShellDescriptor[] readShellDescriptors() {
			IExtensionRegistry registry = Platform.getExtensionRegistry();
			IExtensionPoint extensionPoint = registry.getExtensionPoint(ShellID.EXTENSION_POINT_ID);
			final List shellDescriptors = new ArrayList();
			CollectionUtils.forAllDo(Arrays.asList(extensionPoint.getExtensions()), new Closure() {
				/**
				 * @see org.apache.commons.collections.Closure#execute(java.lang.Object)
				 */
				public void execute(Object object) {
					IExtension extension = (IExtension) object;
					shellDescriptors.addAll(CollectionUtils.collect(Arrays.asList(extension.getConfigurationElements()), new Transformer() {
						/**
						 * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
						 */
						public Object transform(Object object) {
							return new ExtensionShellDescriptor((IConfigurationElement) object);
						};
					}));
				}
			});
			return (IShellDescriptor[]) shellDescriptors.toArray(new IShellDescriptor[0]);
		}

		/**
		 * Gets the <code>IShellDescriptor</code> with the given name.
		 * 
		 * @return the <code>IShellDescriptor</code> with the given name
		 */
		public static IShellDescriptor getShellDescriptor(final String shellDescriptorId) {

			IShellDescriptor shellDescriptor = (IShellDescriptor) CollectionUtils.find(getKnownShellDescriptors(), new Predicate() {
				/**
				 * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
				 */
				public boolean evaluate(Object object) {
					IShellDescriptor shellDescriptor = (IShellDescriptor) object;
					return shellDescriptor.getId().equals(shellDescriptorId);
				}
			});
			if(shellDescriptor == null) {
				throw new RuntimeException("No ShellDescriptor found with id <" + shellDescriptorId + ">");
			}
			return shellDescriptor;
		}

		/**
		 * Gets the system's default shell.
		 * 
		 * @return the system's default shell id
		 */
		public static String getDefaultShellDescriptorId() {
			String operatingSystem = System.getProperty(ShellID.OS_KEY);

			if(ShellID.WINDOWS_95_NAME.equalsIgnoreCase(operatingSystem)) {
				return DomainID.COMMAND_SHELL_DESCRIPTOR_ID;
			}
			else if(ShellID.WINDOWS_98_NAME.equalsIgnoreCase(operatingSystem)) {
				return DomainID.COMMAND_SHELL_DESCRIPTOR_ID;
			}
			else if(ShellID.WINDOWS_NT_NAME.equalsIgnoreCase(operatingSystem)) {
				return DomainID.CMD_SHELL_DESCRIPTOR_ID;
			}
			else if(ShellID.WINDOWS_2000_NAME.equalsIgnoreCase(operatingSystem)) {
				return DomainID.CMD_SHELL_DESCRIPTOR_ID;
			}
			else if(ShellID.WINDOWS_2003_NAME.equalsIgnoreCase(operatingSystem)) {
				return DomainID.CMD_SHELL_DESCRIPTOR_ID;
			}
			else if(ShellID.WINDOWS_2003_SERVER_NAME.equalsIgnoreCase(operatingSystem)) {
				return DomainID.CMD_SHELL_DESCRIPTOR_ID;
			}
			else if(ShellID.WINDOWS_XP_NAME.equalsIgnoreCase(operatingSystem)) {
				return DomainID.CMD_SHELL_DESCRIPTOR_ID;
			}
			else if(ShellID.WINDOWS_VISTA_BETA_NAME.equalsIgnoreCase(operatingSystem)) {
				return DomainID.CMD_SHELL_DESCRIPTOR_ID;
			}
			else if(ShellID.WINDOWS_VISTA_NAME.equalsIgnoreCase(operatingSystem)) {
				return DomainID.CMD_SHELL_DESCRIPTOR_ID;
			}
			else if(ShellID.WINDOWS_7_NAME.equalsIgnoreCase(operatingSystem)) {
				return DomainID.CMD_SHELL_DESCRIPTOR_ID;
			}
			else if(ShellID.LINUX_NAME.equalsIgnoreCase(operatingSystem)) {
				return DomainID.BASH_SHELL_DESCRIPTOR_ID;
			}
			else if(ShellID.MAC_OS_X.equalsIgnoreCase(operatingSystem)) {
				return DomainID.BASH_SHELL_DESCRIPTOR_ID;
			}
			else {
				throw new RuntimeException("Operating System <" + operatingSystem + "> currently not supported.");
			}
		}
	}
}
