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
package net.sf.wickedshell.domain.command;

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
import org.apache.xml.utils.XMLChar;
import org.apache.xmlbeans.XmlException;

/**
 * @author Stefan Reichert
 * @since 22.10.2006
 */
public class CommandDescriptorDao {

	/**
	 * Returns the <code>List</code> of <code>CommandDescriptor</code>
	 * stored as command history in the <i>StateLocation</i>. First checks
	 * whether the corresponding XML-File exists. If not an empty one is
	 * created.
	 * 
	 * @return the <code>List</code> of <code>CommandDescriptor</code>
	 * @throws IOException
	 *             if creating/parsing the file fails
	 */
	@SuppressWarnings("unchecked")
	public List<ICommandDescriptor> readCommandHistory() throws IOException, XmlException {
		CommandHistoryListDocument commandHistoryListDocument;
		String stateLocation = DomainPlugin.getDefault().getStateLocation().toOSString();
		File commandFileDescriptorListDocumentFile = new File(stateLocation, DomainID.COMMAND_HISTORY_FILE);
		if(!commandFileDescriptorListDocumentFile.exists()) {
			commandHistoryListDocument = CommandHistoryListDocument.Factory.newInstance();
			commandHistoryListDocument.addNewCommandHistoryList();
			commandHistoryListDocument.save(commandFileDescriptorListDocumentFile);
		}
		else {
			commandHistoryListDocument = CommandHistoryListDocument.Factory.parse(commandFileDescriptorListDocumentFile);
		}
		return (List<ICommandDescriptor>) CollectionUtils.collect(
				Arrays.asList(commandHistoryListDocument.getCommandHistoryList().getCommandDescriptorArray()), new Transformer() {
					/**
					 * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
					 */
					public Object transform(Object object) {
						return ICommandDescriptor.Factory.newInstance((CommandDescriptor) object);
					}
				}, new ArrayList());
	}

	/**
	 * Persist the <code>List</code> of <code>ICommandDescriptor</code>.
	 * 
	 * @param commandDescriptors
	 *            the <code>List</code> of <code>ICommandDescriptor</code>
	 */
	public void writeCommandHistory(List<ICommandDescriptor> commandDescriptors) throws IOException {
		String stateLocation = DomainPlugin.getDefault().getStateLocation().toOSString();
		File commandHistoryListDocumentFile = new File(stateLocation, DomainID.COMMAND_HISTORY_FILE);
		if(!commandHistoryListDocumentFile.exists()) {
			commandHistoryListDocumentFile.delete();
		}
		final CommandHistoryListDocument commandHistoryListDocument = CommandHistoryListDocument.Factory.newInstance();
		commandHistoryListDocument.addNewCommandHistoryList();
		CollectionUtils.forAllDo(commandDescriptors, new Closure() {
			/**
			 * @see org.apache.commons.collections.Closure#execute(java.lang.Object)
			 */
			public void execute(Object object) {
				ICommandDescriptor commandFileDescriptor = (ICommandDescriptor) object;
				CommandHistoryList commandHistoryList = commandHistoryListDocument.getCommandHistoryList();
				CommandDescriptor staticCommandDescriptor = commandHistoryList.addNewCommandDescriptor();
				// The command is stored in XML, so we need to remove ALL
				// invalid XML characters
				char[] commandCharacters = commandFileDescriptor.getCommand().toCharArray();
				StringBuffer validXMLCharacters = new StringBuffer();
				for (int charIndex = 0; charIndex < commandCharacters.length; charIndex++) {
					if(XMLChar.isValid(commandCharacters[charIndex])) {
						validXMLCharacters.append(commandCharacters[charIndex]);
					}
				}
				staticCommandDescriptor.setCommand(validXMLCharacters.toString());
				staticCommandDescriptor.setShellDescriptorId(commandFileDescriptor.getShellDescriptorId());
			}
		});
		commandHistoryListDocument.save(commandHistoryListDocumentFile);
	}
}
