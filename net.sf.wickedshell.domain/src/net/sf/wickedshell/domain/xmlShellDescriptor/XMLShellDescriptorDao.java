/*
 * ShellStyleDao.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.domain.xmlShellDescriptor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.wickedshell.domain.DomainID;
import net.sf.wickedshell.domain.DomainPlugin;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.xmlbeans.XmlException;

/**
 * @author Stefan Reichert
 * @since 22.10.2006
 */
public class XMLShellDescriptorDao {

	/**
	 * Returns the <code>List</code> of <code>IXMLShellDescriptors</code>
	 * stored as command history in the <i>StateLocation</i>. First checks
	 * whether the corresponding XML-File exists. If not an empty one is
	 * created.
	 * 
	 * @return the <code>List</code> of <code>IXMLShellDescriptors</code>
	 * @throws IOException
	 *             if creating/parsing the file fails
	 */
	@SuppressWarnings("unchecked")
	public List<IXMLShellDescriptor> readXMLShellDescriptors() throws IOException, XmlException {
		XmlShellDescriptorListDocument xmlShellDescriptorListDocument;
		String stateLocation = DomainPlugin.getDefault().getStateLocation().toOSString();
		File xmlShellDescriptorListDocumentFile = new File(stateLocation, DomainID.XML_SHELL_DESCRIPTORS_FILE);
		if(!xmlShellDescriptorListDocumentFile.exists()) {
			xmlShellDescriptorListDocument = XmlShellDescriptorListDocument.Factory.newInstance();
			xmlShellDescriptorListDocument.addNewXmlShellDescriptorList();
			xmlShellDescriptorListDocument.save(xmlShellDescriptorListDocumentFile);
		}
		else {
			xmlShellDescriptorListDocument = XmlShellDescriptorListDocument.Factory.parse(xmlShellDescriptorListDocumentFile);
		}
		return (List) CollectionUtils.collect(Arrays.asList(xmlShellDescriptorListDocument.getXmlShellDescriptorList().getXmlShellDescriptorArray()),
				new Transformer() {
					/**
					 * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
					 */
					public Object transform(Object object) {
						return IXMLShellDescriptor.Factory.newInstance((XmlShellDescriptor) object);
					}
				}, new ArrayList());
	}

	/**
	 * Persist the <code>List</code> of <code>IXMLShellDescriptor</code>s.
	 * 
	 * @param xmlShellDescriptors
	 *            the <code>List</code> of <code>IXMLShellDescriptor</code>s
	 */
	public void writeXMLShellDescriptors(List<IXMLShellDescriptor> xmlShellDescriptors) throws IOException {
		String stateLocation = DomainPlugin.getDefault().getStateLocation().toOSString();
		File xmlShellDescriptorListDocumentFile = new File(stateLocation, DomainID.XML_SHELL_DESCRIPTORS_FILE);
		if(!xmlShellDescriptorListDocumentFile.exists()) {
			xmlShellDescriptorListDocumentFile.delete();
		}
		final XmlShellDescriptorListDocument xmlShellDescriptorListDocument = XmlShellDescriptorListDocument.Factory.newInstance();
		xmlShellDescriptorListDocument.addNewXmlShellDescriptorList();
		CollectionUtils.forAllDo(xmlShellDescriptors, new Closure() {
			/**
			 * @see org.apache.commons.collections.Closure#execute(java.lang.Object)
			 */
			public void execute(Object object) {
				IXMLShellDescriptor xmlShellDescriptor = (IXMLShellDescriptor) object;
				XmlShellDescriptorList xmlShellDescriptorList = xmlShellDescriptorListDocument.getXmlShellDescriptorList();
				XmlShellDescriptor staticXMLShellDescriptor = xmlShellDescriptorList.addNewXmlShellDescriptor();
				// Transfer the data
				staticXMLShellDescriptor.setId(xmlShellDescriptor.getId());
				staticXMLShellDescriptor.setName(xmlShellDescriptor.getName());
				staticXMLShellDescriptor.setBinariesDirectory(xmlShellDescriptor.getBinariesDirectory());
				staticXMLShellDescriptor.setCharacterEncoding(xmlShellDescriptor.getCharacterEncoding());
				staticXMLShellDescriptor.setCommandDelimiter(xmlShellDescriptor.getCommandDelimiter());
				staticXMLShellDescriptor.setCustomRootDirectory(xmlShellDescriptor.getCustomRootDirectory());
				staticXMLShellDescriptor.setExecutable(xmlShellDescriptor.getExecutable());
				staticXMLShellDescriptor.setExecutableBatchFilesArray(xmlShellDescriptor.getExecutableBatchFiles());
				staticXMLShellDescriptor.setExecutableFilesArray(xmlShellDescriptor.getExecutableFiles());
				staticXMLShellDescriptor.setExecutedCommandProvided(String.valueOf(xmlShellDescriptor.isExecutedCommandProvided()));
				staticXMLShellDescriptor.setLineFeedString(xmlShellDescriptor.getLineFeedString());
				staticXMLShellDescriptor.setOperatingSystemsArray(xmlShellDescriptor.getOperatingSystems());
				staticXMLShellDescriptor.setPathDelimiter(xmlShellDescriptor.getPathDelimiter());
				staticXMLShellDescriptor.setPathSeparator(xmlShellDescriptor.getPathSeparator());
				staticXMLShellDescriptor.setSystemPathSeparator(String.valueOf(xmlShellDescriptor.getSystemPathSeparator()));
				staticXMLShellDescriptor.setUiLineFeedProvided(String.valueOf(xmlShellDescriptor.isUiLineFeedProvided()));
			}
		});
		xmlShellDescriptorListDocument.save(xmlShellDescriptorListDocumentFile);
	}
}
