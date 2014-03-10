/*
 * ExtensionShellDescriptor.java
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
import java.util.Arrays;
import java.util.Properties;

import net.sf.wickedshell.ShellID;
import net.sf.wickedshell.domain.DomainID;
import net.sf.wickedshell.facade.descriptor.command.ICommandProvider;
import net.sf.wickedshell.facade.descriptor.environment.IEnvironmentalValueProvider;
import net.sf.wickedshell.facade.descriptor.executableFile.IExecutableFile;
import net.sf.wickedshell.facade.descriptor.external.IExternalShellInvoker;
import net.sf.wickedshell.util.ShellLogger;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.swt.SWT;

/**
 * @author Stefan Reichert
 * @since 13.08.2006
 */
public class ExtensionShellDescriptor implements IShellDescriptor {

	/** The <code>Reader</code> of the shell. */
	private final ShellLogger shellLogger = new ShellLogger(ExtensionShellDescriptor.class);

	private static final String SHELL_NAME_ATTRIBUTE = "name";

	private static final String EXECUTABLE_ATTRIBUTE = "executable";

	private static final String CHARACTER_ENCODING_ATTRIBUTE = "characterEncoding";

	private static final String LINE_FEED_ATTRIBUTE = "lineFeed";

	private static final String PATH_DELIMITER_ATTRIBUTE = "pathDelimiter";

	private static final String PATH_SEPARATOR_ATTRIBUTE = "pathSeparator";

	private static final String SYSTEM_PATH_SEPARATOR_ATTRIBUTE = "systemPathSeparator";

	private static final String HAS_CUSTOM_ROOT_ATTRIBUTE = "hasCustomRoot";

	private static final String BINARIES_DIRECTORY_ATTRIBUTE = "binariesDirectory";

	private static final String COMMAND_DELIMITER_ATTRIBUTE = "commandDelimiter";

	private static final String IS_UI_LINE_FEED_PROVIDED_ATTRIBUTE = "isUILineFeedProvided";

	private static final String SHELL_ID_ATTRIBUTE = "id";

	private static final String EXECUTABLE_FILES_ATTRIBUTE = "executableFile";

	private static final String EXTENSION_ATTRIBUTE = "extension";

	private static final String IS_BATCH_FILE_ATTRIBUTE = "isBatchFile";

	private static final String SUPPORTING_OS_ATTRIBUTE = "operatingSystem";

	private static final String OS_NAME_ATTRIBUTE = "name";

	private static final String DESCRIPTION_ATTRIBUTE = "description";

	private static final String IS_EXECUTED_COMMAND_PROVIDED_ATTIBUTE = "isExecutedCommandProvided";

	private static final String ENVIRONMENTAL_VALUE_PROVIDER_ATTRIBUTE = "environmentalValueProvider";

	private static final String EXTERNAL_SHELL_INVOKER_ATTRIBUTE = "externalShellInvoker";

	private static final String COMMAND_PROVIDER_ATTRIBUTE = "commandProvider";

	private static final char BLANK_SUBSTITUTE = '_';

	private final String shellName;

	private final String shellExecutable;

	private final String characterEncoding;

	private final String lineFeedString;

	private final String pathDelimiter;

	private final String pathSeparator;

	private final String systemPathSeparator;

	private final boolean hasCustomRoot;

	private final String binariesDirectory;

	private final String commandDelimiter;

	private final boolean isUILineFeedProvided;

	private final boolean isExecutedComandProvided;

	private final String id;

	private final IExecutableFile[] executableFiles;

	private final String[] supportingOperatingSystems;

	private IExternalShellInvoker externalShellInvoker;

	private IEnvironmentalValueProvider environmentalValueProvider;

	private ICommandProvider commandProvider;

	/**
	 * Constructor for ExtensionShellDescriptor.
	 * 
	 * @param extension
	 *            The <code>IConfigurationElement</code> representing the
	 *            <code>IShellDescriptor</code>
	 */
	@SuppressWarnings("unchecked")
	public ExtensionShellDescriptor(IConfigurationElement configurationElement) {
		super();
		shellName = configurationElement.getAttribute(SHELL_NAME_ATTRIBUTE);
		shellExecutable = configurationElement.getAttribute(EXECUTABLE_ATTRIBUTE);
		characterEncoding = configurationElement.getAttribute(CHARACTER_ENCODING_ATTRIBUTE);
		String lineFeed = configurationElement.getAttribute(LINE_FEED_ATTRIBUTE);
		StringBuffer buffer = new StringBuffer();
		if(lineFeed.equals(DomainID.CR_LF_VALUE)) {
			buffer.append(SWT.CR);
		}
		buffer.append(SWT.LF);
		lineFeedString = buffer.toString();
		pathDelimiter = configurationElement.getAttribute(PATH_DELIMITER_ATTRIBUTE).replace(BLANK_SUBSTITUTE, ' ');

		String pathSeparatorDefinition = configurationElement.getAttribute(PATH_SEPARATOR_ATTRIBUTE);
		if(pathSeparatorDefinition.equals(DomainID.SLASH_VALUE)) {
			pathSeparator = "/";
		}
		else {
			pathSeparator = "\\";
		}
		systemPathSeparator = configurationElement.getAttribute(SYSTEM_PATH_SEPARATOR_ATTRIBUTE);
		hasCustomRoot = Boolean.valueOf(configurationElement.getAttribute(HAS_CUSTOM_ROOT_ATTRIBUTE)).booleanValue();
		if(hasCustomRoot) {
			binariesDirectory = configurationElement.getAttribute(BINARIES_DIRECTORY_ATTRIBUTE);
		}
		else {
			binariesDirectory = new String();
		}
		commandDelimiter = configurationElement.getAttribute(COMMAND_DELIMITER_ATTRIBUTE);
		isUILineFeedProvided = Boolean.valueOf(configurationElement.getAttribute(IS_UI_LINE_FEED_PROVIDED_ATTRIBUTE)).booleanValue();
		isExecutedComandProvided = Boolean.valueOf(configurationElement.getAttribute(IS_EXECUTED_COMMAND_PROVIDED_ATTIBUTE)).booleanValue();
		id = configurationElement.getAttribute(SHELL_ID_ATTRIBUTE);
		executableFiles = (IExecutableFile[]) CollectionUtils.collect(Arrays.asList(configurationElement.getChildren(EXECUTABLE_FILES_ATTRIBUTE)),
				new Transformer() {
					/**
					 * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
					 */
					public Object transform(Object object) {
						IConfigurationElement executableFileElement = (IConfigurationElement) object;
						String extension = executableFileElement.getAttribute(EXTENSION_ATTRIBUTE);
						String description = executableFileElement.getAttribute(DESCRIPTION_ATTRIBUTE);
						Boolean isBatchFile = Boolean.valueOf(executableFileElement.getAttribute(IS_BATCH_FILE_ATTRIBUTE));
						return IExecutableFile.Factory.newInstance(description, extension, isBatchFile.booleanValue());
					}
				}).toArray(new IExecutableFile[0]);
		supportingOperatingSystems = (String[]) CollectionUtils.collect(Arrays.asList(configurationElement.getChildren(SUPPORTING_OS_ATTRIBUTE)),
				new Transformer() {
					/**
					 * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
					 */
					public Object transform(Object object) {
						IConfigurationElement executableFileElement = (IConfigurationElement) object;
						return executableFileElement.getAttribute(OS_NAME_ATTRIBUTE);
					}
				}).toArray(new String[0]);
		externalShellInvoker = null;
		if(configurationElement.getAttribute(EXTERNAL_SHELL_INVOKER_ATTRIBUTE) != null) {
			try {
				externalShellInvoker = (IExternalShellInvoker) configurationElement.createExecutableExtension(EXTERNAL_SHELL_INVOKER_ATTRIBUTE);
			}
			catch (CoreException exception) {
				shellLogger.error(exception.getMessage(), exception);
			}
		}
		environmentalValueProvider = null;
		if(configurationElement.getAttribute(ENVIRONMENTAL_VALUE_PROVIDER_ATTRIBUTE) != null) {
			try {
				environmentalValueProvider = (IEnvironmentalValueProvider) configurationElement
						.createExecutableExtension(ENVIRONMENTAL_VALUE_PROVIDER_ATTRIBUTE);
			}
			catch (CoreException exception) {
				shellLogger.error(exception.getMessage(), exception);
			}
		}
		commandProvider = null;
		if(configurationElement.getAttribute(COMMAND_PROVIDER_ATTRIBUTE) != null) {
			try {
				commandProvider = (ICommandProvider) configurationElement.createExecutableExtension(COMMAND_PROVIDER_ATTRIBUTE);
			}
			catch (CoreException exception) {
				shellLogger.error(exception.getMessage(), exception);
			}
		}
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getName()
	 */
	public String getName() {
		return shellName;
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getExecutable()
	 */
	public String getExecutable() {
		return shellExecutable;
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getCharacterEncoding()
	 */
	public String getCharacterEncoding() {
		return characterEncoding;
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getLineFeedString()
	 */
	public String getLineFeedString() {
		return lineFeedString;
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getPathDelimiter()
	 */
	public String getPathDelimiter() {
		return pathDelimiter;
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getSystemPathSeparator()
	 */
	public char getSystemPathSeparator() {
		return systemPathSeparator.charAt(0);
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getPathSeparator()
	 */
	public char getPathSeparator() {
		return pathSeparator.charAt(0);
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getCommandDelimiter()
	 */
	public String getCommandDelimiter() {
		return commandDelimiter;
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#hasCustomRoot()
	 */
	public boolean hasCustomRoot() {
		return hasCustomRoot;
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#isExecutable(java.io.File)
	 */
	public boolean isExecutable(final File file) {
		return CollectionUtils.exists(Arrays.asList(executableFiles), new Predicate() {
			/**
			 * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
			 */
			public boolean evaluate(Object object) {
				IExecutableFile executableFile = (IExecutableFile) object;
				return file.getName().endsWith(executableFile.getExtension());
			}
		});
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#isAllowedForBatchList(java.io.File)
	 */
	public boolean isAllowedForBatchList(final File file) {
		return CollectionUtils.exists(Arrays.asList(executableFiles), new Predicate() {
			/**
			 * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
			 */
			public boolean evaluate(Object object) {
				IExecutableFile executableFile = (IExecutableFile) object;
				return executableFile.isBatchFile() && file.getName().endsWith(executableFile.getExtension());
			}
		});
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getEnviromentalValues()
	 */
	public Properties getEnviromentalValues() {
		if(environmentalValueProvider != null) {
			return environmentalValueProvider.getEnviromentalValues();
		}
		return new Properties();
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#isUILineFeedProvided()
	 */
	public boolean isUILineFeedProvided() {
		return isUILineFeedProvided;
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#isExecutedCommandProvided()
	 */
	public boolean isExecutedCommandProvided() {
		return isExecutedComandProvided;
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#isCurrentOSSupported()
	 */
	public boolean isCurrentOSSupported() {
		final String operatingSystem = System.getProperty(ShellID.OS_KEY);
		return CollectionUtils.exists(Arrays.asList(this.supportingOperatingSystems), new Predicate() {
			/**
			 * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
			 */
			public boolean evaluate(Object object) {
				String supportingOperatingSystem = (String) object;
				return supportingOperatingSystem.equalsIgnoreCase(operatingSystem);
			}
		});
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getId()
	 */
	public String getId() {
		return id;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if(object instanceof IShellDescriptor) {
			IShellDescriptor descriptor = (IShellDescriptor) object;
			return descriptor.getName().equals(getName());
		}
		return false;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getName().hashCode();
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getExecutableFiles()
	 */
	@SuppressWarnings("unchecked")
	public IExecutableFile[] getExecutableFiles(final boolean batchFilesOnly) {
		return (IExecutableFile[]) CollectionUtils.select(Arrays.asList(executableFiles), new Predicate() {
			/**
			 * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
			 */
			public boolean evaluate(Object object) {
				IExecutableFile executableFile = (IExecutableFile) object;
				return !batchFilesOnly || executableFile.isBatchFile();
			}
		}).toArray(new IExecutableFile[0]);
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getOperatingSystems()
	 */
	public String[] getOperatingSystems() {
		return supportingOperatingSystems;
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getBinariesDirectory()
	 */
	public String getBinariesDirectory() {
		return binariesDirectory;
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getExternalShellInvoker()
	 */
	public IExternalShellInvoker getExternalShellInvoker() {
		return externalShellInvoker;
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getCommandProvider()
	 */
	public ICommandProvider getCommandProvider() {
		return commandProvider;
	}

}
