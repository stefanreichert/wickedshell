/*
 * CompletionDaoTest.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.test.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;
import net.sf.wickedshell.domain.completion.CompletionComparator;
import net.sf.wickedshell.domain.completion.CompletionDao;
import net.sf.wickedshell.domain.completion.ICompletion;

/**
 * @author Stefan Reichert
 * @since 15.11.2006
 */
public class CompletionDaoTest extends TestCase {

	/** The <code>CompletionDao</code> to test. */
	private CompletionDao completionDao;

	/**
	 * Constructor for ShellStyleDaoTest.
	 */
	public CompletionDaoTest() {
		completionDao = new CompletionDao();
		setName("Test for CompletionDao");
	}

	/**
	 * @see junit.framework.TestCase#runTest()
	 */
	protected void runTest() throws Throwable {
		testWriteCompletions();
		testReadCompletions();
		testWriteDefaultCompletions();
	}

	/**
	 * Test method for {@link net.sf.wickedshell.domain.completion.CompletionDao#readCompletions()}.
	 */
	@SuppressWarnings("unchecked")
	public void testReadCompletions() throws Throwable {
		List testCompletions = completionDao.readCompletions();
		assertEquals("test completions count", 7, testCompletions.size());
		Collections.sort(testCompletions, new CompletionComparator());
		for (int index = 0; index < testCompletions.size(); index++) {
			ICompletion completion = (ICompletion) testCompletions.get(index);
			assertEquals("test completion content", String.valueOf(index), completion.getContent());
			assertEquals("test completion label", "Test Completion " + index
					+ " - A stupid completion (Test)", completion.getLabel());
			assertEquals("test completion image path", "img/testCompletion.gif", completion.getImagePath());
		}
	}

	/**
	 * Test method for
	 * {@link net.sf.wickedshell.domain.completion.CompletionDao#writeDefaultCompletions()}.
	 */
	public void testWriteDefaultCompletions() throws Throwable {
		completionDao.writeDefaultCompletions();
		List<ICompletion> defaultCompletions = completionDao.readCompletions();
		assertTrue("default completions contains", defaultCompletions.contains(ICompletion.Factory
				.newInstance("cd " + File.separatorChar, "cd " + File.separatorChar
						+ " - Change to root directory (Static)", "img/changeToRootDirectory.gif")));
		assertTrue("default completions contains", defaultCompletions.contains(ICompletion.Factory
				.newInstance("cd ..", "cd .. - Change to parent directory (Static)",
						"img/changeToParentDirectory.gif")));
		assertTrue("default completions contains", defaultCompletions.contains(ICompletion.Factory
				.newInstance("cls", "cls - Clear screen (Static)", "img/clearScreen.gif")));
		assertTrue("default completions contains", defaultCompletions.contains(ICompletion.Factory
				.newInstance("dir", "dir - List directory (Static)", "img/listDirectory.gif")));
		assertTrue("default completions contains", defaultCompletions.contains(ICompletion.Factory
				.newInstance("ipconfig", "ipconfig - Show IP-Configuration (Static)",
						"img/ipconfig.gif")));
		assertTrue("default completions contains",
				defaultCompletions
						.contains(ICompletion.Factory.newInstance("ipconfig /all",
								"ipconfig /all - Show detailed IP-Configuration (Static)",
								"img/ipconfigAll.gif")));
		assertTrue("default completions contains", defaultCompletions.contains(ICompletion.Factory
				.newInstance("mkdir", "mkdir - Create a new directory (Static)",
						"img/createDirectory.gif")));
		assertTrue("default completions contains", defaultCompletions.contains(ICompletion.Factory
				.newInstance("net", "net - Manage local system services (Static)", "img/net.gif")));
		assertTrue("default completions contains", defaultCompletions.contains(ICompletion.Factory
				.newInstance("net start", "net start - Start local system service (Static)",
						"img/net.gif")));
		assertTrue("default completions contains", defaultCompletions
				.contains(ICompletion.Factory.newInstance("net stop",
						"net stop - Stop local system service (Static)", "img/net.gif")));
	}

	/**
	 * Test method for
	 * {@link net.sf.wickedshell.domain.completion.CompletionDao#writeCompletions(java.util.List)}.
	 */
	@SuppressWarnings("unchecked")
	public void testWriteCompletions() throws Throwable {
		List testCompletions = new ArrayList();
		for (int index = 5; index < 12; index++) {
			ICompletion completion = ICompletion.Factory.newInstance(String.valueOf(index % 7),
					"Test Completion " + (index % 7) + " - A stupid completion (Test)",
					"img/testCompletion.gif");
			assertEquals("test completion content", String.valueOf(index % 7), completion.getContent());
			assertEquals("test completion label", "Test Completion " + (index % 7)
					+ " - A stupid completion (Test)", completion.getLabel());
			assertEquals("test completion image path", "img/testCompletion.gif", completion.getImagePath());
			testCompletions.add(completion);
		}
		completionDao.writeCompletions(testCompletions);
	}
}
