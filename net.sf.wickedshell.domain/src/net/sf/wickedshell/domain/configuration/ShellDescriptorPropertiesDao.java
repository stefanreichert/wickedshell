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
package net.sf.wickedshell.domain.configuration;

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
public class ShellDescriptorPropertiesDao {

	/**
	 * Returns the <code>List</code> of
	 * <code>IShellDescriptorProperties</code> stored as shell descriptor
	 * configuration in the <i>StateLocation</i>. First checks whether the
	 * corresponding XML-File exists. If not an empty one is created.
	 * 
	 * @return the <code>List</code> of <code>CommandDescriptor</code>
	 * @throws IOException
	 *             if creating/parsing the file fails
	 */
	@SuppressWarnings("unchecked")
	public List<IShellDescriptorProperties> readShellDescriptorProperties() throws IOException, XmlException {
		String stateLocation = DomainPlugin.getDefault().getStateLocation().toOSString();
		File shelldescriptorConfigurationDocumentFile = new File(stateLocation, DomainID.SHELL_DESCRIPTOR_CONFIGURATION_FILE);
		if(!shelldescriptorConfigurationDocumentFile.exists()) {
			writeDefaultShellDescriptorProperties();
		}
		ShellDescriptorConfigurationDocument shellDescriptorConfigurationDocument = ShellDescriptorConfigurationDocument.Factory
				.parse(shelldescriptorConfigurationDocumentFile);
		return (List) CollectionUtils.collect(Arrays.asList(shellDescriptorConfigurationDocument.getShellDescriptorConfiguration()
				.getShellDescriptorPropertiesArray()), new Transformer() {
			/**
			 * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
			 */
			public Object transform(Object object) {
				return IShellDescriptorProperties.Factory.newInstance((ShellDescriptorProperties) object);
			}
		}, new ArrayList());
	}

	/**
	 * Resets the <code>StaticCompletionList</code> of static
	 * <code>Completions</code> defined by the guy who wrote this plugin :)
	 */
	public void writeDefaultShellDescriptorProperties() throws IOException {
		List<IShellDescriptorProperties> staticShellDescriptorProperties = new ArrayList<IShellDescriptorProperties>();
		staticShellDescriptorProperties.add(IShellDescriptorProperties.Factory.newInstance(DomainID.CMD_SHELL_DESCRIPTOR_ID, new String(), true));
		staticShellDescriptorProperties.add(IShellDescriptorProperties.Factory.newInstance(DomainID.COMMAND_SHELL_DESCRIPTOR_ID, new String(), true));
		staticShellDescriptorProperties.add(IShellDescriptorProperties.Factory.newInstance(DomainID.BASH_SHELL_DESCRIPTOR_ID, new String(), true));
		staticShellDescriptorProperties.add(IShellDescriptorProperties.Factory.newInstance(DomainID.CYGWIN_BASH_SHELL_DESCRIPTOR_ID, new String(), false));
		staticShellDescriptorProperties.add(IShellDescriptorProperties.Factory.newInstance(DomainID.MSYS_SH_SHELL_DESCRIPTOR_ID, new String(), false));
		writeShellDescriptorProperties(staticShellDescriptorProperties);
	}

	/**
	 * Persist the <code>List</code> of
	 * <code>IShellDescriptorProperties</code>.
	 * 
	 * @param shellDescriptorProperties
	 *            the <code>List</code> of
	 *            <code>IShellDescriptorProperties</code>
	 */
	public void writeShellDescriptorProperties(List<IShellDescriptorProperties> shellDescriptorProperties) throws IOException {
		String stateLocation = DomainPlugin.getDefault().getStateLocation().toOSString();
		File shelldescriptorConfigurationDocumentFile = new File(stateLocation, DomainID.SHELL_DESCRIPTOR_CONFIGURATION_FILE);
		if(!shelldescriptorConfigurationDocumentFile.exists()) {
			shelldescriptorConfigurationDocumentFile.delete();
		}
		final ShellDescriptorConfigurationDocument shelldescriptorConfigurationDocument = ShellDescriptorConfigurationDocument.Factory.newInstance();
		shelldescriptorConfigurationDocument.addNewShellDescriptorConfiguration();
		CollectionUtils.forAllDo(shellDescriptorProperties, new Closure() {
			/**
			 * @see org.apache.commons.collections.Closure#execute(java.lang.Object)
			 */
			public void execute(Object object) {
				IShellDescriptorProperties shellDescriptorProperties = (IShellDescriptorProperties) object;
				ShellDescriptorConfiguration shelldescriptorConfiguration = shelldescriptorConfigurationDocument.getShellDescriptorConfiguration();
				ShellDescriptorProperties staticShellDescriptorProperties = shelldescriptorConfiguration.addNewShellDescriptorProperties();
				staticShellDescriptorProperties.setShellDescriptorId(shellDescriptorProperties.getShellDescriptorId());
				staticShellDescriptorProperties.setRootDirectory(shellDescriptorProperties.getRootDirectory());
				staticShellDescriptorProperties.setIsOSDefault(String.valueOf(shellDescriptorProperties.isOSDefault()));
			}
		});
		shelldescriptorConfigurationDocument.save(shelldescriptorConfigurationDocumentFile);
	}
}
