/*
 * XMLShellDescriptor.java
 * 
 * Copyright 2005-2007 Stefan Reichert.
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
import java.util.List;
import java.util.Properties;

import net.sf.wickedshell.ShellID;
import net.sf.wickedshell.domain.DomainID;
import net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor;
import net.sf.wickedshell.facade.descriptor.command.ICommandProvider;
import net.sf.wickedshell.facade.descriptor.executableFile.IExecutableFile;
import net.sf.wickedshell.facade.descriptor.external.IExternalShellInvoker;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.eclipse.swt.SWT;

/**
 * Please enter the purpose of this class.
 * 
 * @author Stefan Reichert
 */
public class XMLShellDescriptor implements IShellDescriptor {

	/** The adapted <code>IXMLShellDescriptor</code>. */
	private IXMLShellDescriptor adaptedDescriptor;

	/**
	 * Constructor for <code>XMLShellDescriptor</code>.
	 * 
	 * @param adaptedDescriptor
	 *            the adapted <code>IXMLShellDescriptor</code>
	 */
	public XMLShellDescriptor(IXMLShellDescriptor adaptedDescriptor) {
		super();
		this.adaptedDescriptor = adaptedDescriptor;
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getBinariesDirectory()
	 */
	public String getBinariesDirectory() {
		return adaptedDescriptor.getBinariesDirectory();
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getCharacterEncoding()
	 */
	public String getCharacterEncoding() {
		return adaptedDescriptor.getCharacterEncoding();
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getCommandDelimiter()
	 */
	public String getCommandDelimiter() {
		return adaptedDescriptor.getCommandDelimiter();
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getCommandProvider()
	 */
	public ICommandProvider getCommandProvider() {
		return null;
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getEnviromentalValues()
	 */
	public Properties getEnviromentalValues() {
		return new Properties();
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getExecutable()
	 */
	public String getExecutable() {
		return adaptedDescriptor.getExecutable();
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getExecutableFiles(boolean)
	 */
	@SuppressWarnings("unchecked")
	public IExecutableFile[] getExecutableFiles(boolean batchFileOnly) {
		List executableFiles = new ArrayList();
		CollectionUtils.collect(Arrays.asList(adaptedDescriptor.getExecutableBatchFiles()), new Transformer() {
			/**
			 * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
			 */
			public Object transform(Object object) {
				String executableFile = (String) object;
				return IExecutableFile.Factory.newInstance("Excutable Batch File", executableFile, true);
			}
		}, executableFiles);
		if(!batchFileOnly) {
			CollectionUtils.collect(Arrays.asList(adaptedDescriptor.getExecutableFiles()), new Transformer() {
				/**
				 * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
				 */
				public Object transform(Object object) {
					String executableFile = (String) object;
					return IExecutableFile.Factory.newInstance("Excutable File", executableFile, false);
				}
			}, executableFiles);
		}
		return (IExecutableFile[]) executableFiles.toArray(new IExecutableFile[0]);
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getExternalShellInvoker()
	 */
	public IExternalShellInvoker getExternalShellInvoker() {
		return null;
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getId()
	 */
	public String getId() {
		return adaptedDescriptor.getId();
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getLineFeedString()
	 */
	public String getLineFeedString() {
		StringBuffer buffer = new StringBuffer();
		if(adaptedDescriptor.getLineFeedString().equals(DomainID.CR_LF_VALUE)) {
			buffer.append(SWT.CR);
		}
		buffer.append(SWT.LF);
		return buffer.toString();
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getName()
	 */
	public String getName() {
		return adaptedDescriptor.getName();
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getOperatingSystems()
	 */
	public String[] getOperatingSystems() {
		return adaptedDescriptor.getOperatingSystems();
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getPathDelimiter()
	 */
	public String getPathDelimiter() {
		return adaptedDescriptor.getPathDelimiter();
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getPathSeparator()
	 */
	public char getPathSeparator() {
		if(adaptedDescriptor.getPathSeparator().equals(DomainID.SLASH_VALUE)) {
			return '/';
		}
		return '\\';
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#getSystemPathSeparator()
	 */
	public char getSystemPathSeparator() {
		return adaptedDescriptor.getSystemPathSeparator();
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#hasCustomRoot()
	 */
	public boolean hasCustomRoot() {
		return adaptedDescriptor.getCustomRootDirectory() != null && !adaptedDescriptor.getCustomRootDirectory().equals("");
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#isAllowedForBatchList(java.io.File)
	 */
	public boolean isAllowedForBatchList(final File file) {
		return CollectionUtils.exists(Arrays.asList(getExecutableFiles(true)), new Predicate() {
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
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#isCurrentOSSupported()
	 */
	public boolean isCurrentOSSupported() {
		final String operatingSystem = System.getProperty(ShellID.OS_KEY);
		return CollectionUtils.exists(Arrays.asList(getOperatingSystems()), new Predicate() {
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
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#isExecutable(java.io.File)
	 */
	public boolean isExecutable(final File file) {
		return CollectionUtils.exists(Arrays.asList(getExecutableFiles(false)), new Predicate() {
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
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#isExecutedCommandProvided()
	 */
	public boolean isExecutedCommandProvided() {
		return adaptedDescriptor.isExecutedCommandProvided();
	}

	/**
	 * @see net.sf.wickedshell.facade.descriptor.IShellDescriptor#isUILineFeedProvided()
	 */
	public boolean isUILineFeedProvided() {
		return adaptedDescriptor.isUiLineFeedProvided();
	}

	/**
	 * @return the adapted <code>IXMLShellDescriptor</code>
	 */
	public IXMLShellDescriptor getAdaptedDescriptor() {
		return adaptedDescriptor;
	}

}
