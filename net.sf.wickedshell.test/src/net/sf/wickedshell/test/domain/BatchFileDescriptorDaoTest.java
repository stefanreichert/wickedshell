/*
 * BatchFileDescriptorDaoTest.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.test.domain;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import net.sf.wickedshell.domain.batch.BatchFileDescriptorDao;
import net.sf.wickedshell.domain.batch.IBatchFileDescriptor;

public class BatchFileDescriptorDaoTest extends TestCase {

	/** The <code>BatchFileDescriptorDao</code> to test. */
	private BatchFileDescriptorDao batchFileDescriptorDao;

	public BatchFileDescriptorDaoTest() {
		batchFileDescriptorDao = new BatchFileDescriptorDao();
		setName("Test for BatchFileDescriptorDao");
	}

	/**
	 * @see junit.framework.TestCase#runTest()
	 */
	protected void runTest() throws Throwable {
		testWriteBatchFileDescriptors();
		testReadBatchFileDescriptors();
	}

	/**
	 * Test method for
	 * {@link net.sf.wickedshell.domain.batch.BatchFileDescriptorDao#readBatchFileDescriptors()}.
	 */
	public void testReadBatchFileDescriptors() throws Throwable {
		List<IBatchFileDescriptor> testBatchFileDescriptorList = batchFileDescriptorDao.readBatchFileDescriptors();
		assertEquals("test batch file decriptors length", 10, testBatchFileDescriptorList.size());
		for (int index = 0; index < testBatchFileDescriptorList.size(); index++) {
			IBatchFileDescriptor batchFileDescriptor = testBatchFileDescriptorList.get(index);
			assertEquals("test batch file descriptor filename", "test" + index, batchFileDescriptor.getFilename());
			assertEquals("test batch file descriptor parameters", "-index " + index, batchFileDescriptor.getParameters());

			IBatchFileDescriptor batchFileDescriptorDuplicate = IBatchFileDescriptor.Factory.newInstance("test" + index);
			batchFileDescriptorDuplicate.setParameters("-index " + index);

			assertTrue("test batch file decriptors contains", testBatchFileDescriptorList.contains(batchFileDescriptorDuplicate));
		}
	}

	/**
	 * Test method for
	 * {@link net.sf.wickedshell.domain.batch.BatchFileDescriptorDao#writeBatchFileDescriptors()}.
	 */
	@SuppressWarnings("unchecked")
	public void testWriteBatchFileDescriptors() throws Throwable {
		List testBatchFileDescriptorList = new ArrayList();
		for (int index = 0; index < 10; index++) {
			IBatchFileDescriptor batchFileDescriptor = IBatchFileDescriptor.Factory.newInstance("test" + index);
			batchFileDescriptor.setParameters("-index " + index);
			assertEquals("test batch file descriptor filename", "test" + index, batchFileDescriptor.getFilename());
			assertEquals("test batch file descriptor parameters", "-index " + index, batchFileDescriptor.getParameters());
			testBatchFileDescriptorList.add(batchFileDescriptor);
		}
		batchFileDescriptorDao.writeBatchFileDescriptors(testBatchFileDescriptorList);
	}

}
