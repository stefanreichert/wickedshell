/*
 * WickedShellTestSuite.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sf.wickedshell.test.domain.BatchFileDescriptorDaoTest;
import net.sf.wickedshell.test.domain.CommandDescriptorDaoTest;
import net.sf.wickedshell.test.domain.CompletionDaoTest;
import net.sf.wickedshell.test.domain.ShellDescriptorPropertiesDaoTest;
import net.sf.wickedshell.test.domain.ShellStyleDaoTest;

public class WickedShellTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("TestSuite for net.sf.wickedshell");
		// $JUnit-BEGIN$
		suite.addTest(new ShellStyleDaoTest());
		suite.addTest(new CompletionDaoTest());
		suite.addTest(new ShellDescriptorPropertiesDaoTest());
		suite.addTest(new CommandDescriptorDaoTest());
		suite.addTest(new BatchFileDescriptorDaoTest());

		// $JUnit-END$
		return suite;
	}

}
