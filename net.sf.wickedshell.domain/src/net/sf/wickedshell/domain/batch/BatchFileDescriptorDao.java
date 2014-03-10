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
package net.sf.wickedshell.domain.batch;

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
public class BatchFileDescriptorDao {

	/**
	 * Returns the <code>List</code> of <code>IBatchFileDescriptor</code> stored in the
	 * <i>StateLocation</i>. First checks whether the corresponding XML-File exists. If not an empty
	 * one is created.
	 * 
	 * @return the <code>List</code> of <code>IBatchFileDescriptor</code>
	 * @throws IOException
	 *         if creating/parsing the file fails
	 */
	@SuppressWarnings("unchecked")
	public List<IBatchFileDescriptor> readBatchFileDescriptors() throws IOException, XmlException {
		BatchFileDescriptorListDocument batchFileDescriptorListDocument;
		String stateLocation = DomainPlugin.getDefault().getStateLocation().toOSString();
		File batchFileDescriptorListDocumentFile = new File(stateLocation, DomainID.BATCH_FILES_FILE);
		if (!batchFileDescriptorListDocumentFile.exists()) {
			batchFileDescriptorListDocument = BatchFileDescriptorListDocument.Factory.newInstance();
			batchFileDescriptorListDocument.addNewBatchFileDescriptorList();
			batchFileDescriptorListDocument.save(batchFileDescriptorListDocumentFile);
		}
		else {
			batchFileDescriptorListDocument = BatchFileDescriptorListDocument.Factory
					.parse(batchFileDescriptorListDocumentFile);
		}
		return (List<IBatchFileDescriptor>) CollectionUtils.collect(Arrays.asList(batchFileDescriptorListDocument
				.getBatchFileDescriptorList().getBatchFileDescriptorArray()), new Transformer() {
			/**
			 * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
			 */
			public Object transform(Object object) {
				return IBatchFileDescriptor.Factory.newInstance((BatchFileDescriptor) object);
			}
		}, new ArrayList());
	}

	/**
	 * Persist the <code>List</code> of <code>IBatchFileDescriptor</code>.
	 * 
	 * @param completions
	 *        the <code>List</code> of <code>IBatchFileDescriptor</code>
	 */
	public void writeBatchFileDescriptors(List<IBatchFileDescriptor> batchFileDescriptors) throws IOException {
		String stateLocation = DomainPlugin.getDefault().getStateLocation().toOSString();
		File batchFileDescriptorListDocumentFile = new File(stateLocation, DomainID.BATCH_FILES_FILE);
		if (!batchFileDescriptorListDocumentFile.exists()) {
			batchFileDescriptorListDocumentFile.delete();
		}
		final BatchFileDescriptorListDocument batchFileDescriptorListDocument = BatchFileDescriptorListDocument.Factory
				.newInstance();
		batchFileDescriptorListDocument.addNewBatchFileDescriptorList();
		CollectionUtils.forAllDo(batchFileDescriptors, new Closure() {
			/**
			 * @see org.apache.commons.collections.Closure#execute(java.lang.Object)
			 */
			public void execute(Object object) {
				IBatchFileDescriptor batchFileDescriptor = (IBatchFileDescriptor) object;
				BatchFileDescriptorList batchFileDescriptorList = batchFileDescriptorListDocument
						.getBatchFileDescriptorList();
				BatchFileDescriptor staticBatchFileDescriptor = batchFileDescriptorList
						.addNewBatchFileDescriptor();
				staticBatchFileDescriptor.setFilename(batchFileDescriptor.getFilename());
				staticBatchFileDescriptor.setParameters(batchFileDescriptor.getParameters());
			}
		});
		batchFileDescriptorListDocument.save(batchFileDescriptorListDocumentFile);
	}
}
