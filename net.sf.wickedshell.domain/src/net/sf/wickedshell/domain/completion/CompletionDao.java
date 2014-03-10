/*
 * CompletionDao.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.domain.completion;

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
public class CompletionDao {

	/**
	 * Returns the <code>List</code> of <code>ICompletions</code> stored in
	 * the <i>StateLocation</i>. First checks whether the corresponding
	 * XML-File exists. If not an empty one is created.
	 * 
	 * @return the <code>List</code> of <code>ICompletions</code>
	 * @throws IOException
	 *             if creating/parsing the file fails
	 */
	public List<ICompletion> readCompletions() throws IOException, XmlException {
		String stateLocation = DomainPlugin.getDefault().getStateLocation().toOSString();
		File staticCompletionListDocumentFile = new File(stateLocation, DomainID.STATIC_COMPLETIONS_FILE);
		return readCompletions(staticCompletionListDocumentFile);
	}

	/**
	 * Returns the <code>List</code> of <code>ICompletions</code> stored in
	 * the <i>StateLocation</i>. First checks whether the corresponding
	 * XML-File exists. If not an empty one is created.
	 * 
	 * @return the <code>List</code> of <code>ICompletions</code>
	 * @throws IOException
	 *             if creating/parsing the file fails
	 */
	@SuppressWarnings("unchecked")
	public List<ICompletion> readCompletions(File staticCompletionListDocumentFile) throws IOException, XmlException {
		if(!staticCompletionListDocumentFile.exists()) {
			writeDefaultCompletions();
		}
		StaticCompletionListDocument staticCompletionListDocument = StaticCompletionListDocument.Factory.parse(staticCompletionListDocumentFile);
		return (List) CollectionUtils.collect(Arrays.asList(staticCompletionListDocument.getStaticCompletionList().getStaticCompletionArray()),
				new Transformer() {
					/**
					 * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
					 */
					public Object transform(Object object) {
						return ICompletion.Factory.newInstance((StaticCompletion) object);
					}
				}, new ArrayList());
	}

	/**
	 * Resets the <code>StaticCompletionList</code> of static
	 * <code>Completions</code> defined by the guy who wrote this plugin :)
	 */
	public void writeDefaultCompletions() throws IOException {
		List<ICompletion> staticCompletions = new ArrayList<ICompletion>();
		// create the default static completions
		staticCompletions.add(ICompletion.Factory.newInstance("cd " + File.separatorChar, "cd " + File.separatorChar + " - Change to root directory (Static)",
				"img/changeToRootDirectory.gif"));
		staticCompletions.add(ICompletion.Factory.newInstance("cd ..", "cd .. - Change to parent directory (Static)", "img/changeToParentDirectory.gif"));
		staticCompletions.add(ICompletion.Factory.newInstance("cls", "cls - Clear screen (Static)", new String()));
		staticCompletions.add(ICompletion.Factory.newInstance("dir", "dir - List directory (Static)", new String()));
		staticCompletions.add(ICompletion.Factory.newInstance("ipconfig", "ipconfig - Show IP-Configuration (Static)", new String()));
		staticCompletions.add(ICompletion.Factory.newInstance("ipconfig /all", "ipconfig /all - Show detailed IP-Configuration (Static)", new String()));
		staticCompletions.add(ICompletion.Factory.newInstance("mkdir", "mkdir - Create a new directory (Static)", new String()));
		staticCompletions.add(ICompletion.Factory.newInstance("net", "net - Manage local system services (Static)", new String()));
		staticCompletions.add(ICompletion.Factory.newInstance("net start", "net start - Start local system service (Static)", new String()));
		staticCompletions.add(ICompletion.Factory.newInstance("net stop", "net stop - Stop local system service (Static)", new String()));
		writeCompletions(staticCompletions);
	}

	/**
	 * Persist the <code>List</code> of <code>ICompletions</code>.
	 * 
	 * @param completions
	 *            The <code>List</code> of <code>ICompletions</code>
	 * @param staticCompletionListDocumentFile
	 *            The <code>File</code> to use
	 */
	public void writeCompletions(List<ICompletion> completions, File staticCompletionListDocumentFile) throws IOException {
		if(!staticCompletionListDocumentFile.exists()) {
			staticCompletionListDocumentFile.delete();
		}
		final StaticCompletionListDocument staticCompletionListDocument = StaticCompletionListDocument.Factory.newInstance();
		staticCompletionListDocument.addNewStaticCompletionList();
		CollectionUtils.forAllDo(completions, new Closure() {
			/**
			 * @see org.apache.commons.collections.Closure#execute(java.lang.Object)
			 */
			public void execute(Object object) {
				ICompletion completion = (ICompletion) object;
				StaticCompletionList staticCompletionList = staticCompletionListDocument.getStaticCompletionList();
				StaticCompletion staticCompletion = staticCompletionList.addNewStaticCompletion();
				staticCompletion.setContent(completion.getContent());
				staticCompletion.setLabel(completion.getLabel());
				staticCompletion.setImagePath(completion.getImagePath());
			}
		});
		staticCompletionListDocument.save(staticCompletionListDocumentFile);
	}

	/**
	 * Persist the <code>List</code> of <code>ICompletions</code>.
	 * 
	 * @param completions
	 *            The <code>List</code> of <code>ICompletions</code>
	 */
	public void writeCompletions(List<ICompletion> completions) throws IOException {
		String stateLocation = DomainPlugin.getDefault().getStateLocation().toOSString();
		File staticCompletionListDocumentFile = new File(stateLocation, DomainID.STATIC_COMPLETIONS_FILE);
		writeCompletions(completions, staticCompletionListDocumentFile);
	}
}
