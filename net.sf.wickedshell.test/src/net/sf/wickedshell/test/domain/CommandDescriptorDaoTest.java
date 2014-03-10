/*
 * CommandDescriptorDaoTest.java
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
import net.sf.wickedshell.domain.command.CommandDescriptorDao;
import net.sf.wickedshell.domain.command.ICommandDescriptor;

/**
 * @author Stefan Reichert
 * @since 16.11.2006
 */
public class CommandDescriptorDaoTest extends TestCase {

	/** The <code>CommandDescriptorDao</code> to test. */
	private CommandDescriptorDao commandDescriptorDao;

	/**
	 * Constructor for CommandDescriptorDaoTest.
	 */
	public CommandDescriptorDaoTest() {
		commandDescriptorDao = new CommandDescriptorDao();
		setName("Test for CommandDescriptorDao");
	}

	/**
	 * @see junit.framework.TestCase#runTest()
	 */
	protected void runTest() throws Throwable {
		testWriteCommandHistory();
		testReadCommandHistory();
	}

	/**
	 * Test method for
	 * {@link net.sf.wickedshell.domain.command.CommandDescriptorDao#readCommandHistory()}.
	 */
	public void testReadCommandHistory() throws Throwable {
		List<ICommandDescriptor> testCommandHistory = commandDescriptorDao.readCommandHistory();
		assertEquals("test command history length", 10, testCommandHistory.size());
		for (int index = 0; index < testCommandHistory.size(); index++) {
			ICommandDescriptor commandDescriptor = testCommandHistory.get(index);
			assertEquals("test command descriptor command", "test -index " + index, commandDescriptor
					.getCommand());
			assertEquals("test command descriptor id", "net.sf.wickedshell.test.descriptor." + index,
					commandDescriptor.getShellDescriptorId());

			assertTrue("test command history contains", testCommandHistory
					.contains(ICommandDescriptor.Factory.newInstance("test -index " + index,
							"net.sf.wickedshell.test.descriptor." + index)));
		}
	}

	/**
	 * Test method for
	 * {@link net.sf.wickedshell.domain.command.CommandDescriptorDao#writeCommandHistory(java.util.List)}.
	 */
	@SuppressWarnings("unchecked")
	public void testWriteCommandHistory() throws Throwable {
		List testCommandHistory = new ArrayList();
		for (int index = 0; index < 10; index++) {
			ICommandDescriptor commandDescriptor = ICommandDescriptor.Factory.newInstance(
					"test -index " + index, "net.sf.wickedshell.test.descriptor." + index);
			assertEquals("test command descriptor command", "test -index " + index, commandDescriptor
					.getCommand());
			assertEquals("test command descriptor id", "net.sf.wickedshell.test.descriptor." + index,
					commandDescriptor.getShellDescriptorId());
			testCommandHistory.add(commandDescriptor);
		}
		commandDescriptorDao.writeCommandHistory(testCommandHistory);
	}

}
