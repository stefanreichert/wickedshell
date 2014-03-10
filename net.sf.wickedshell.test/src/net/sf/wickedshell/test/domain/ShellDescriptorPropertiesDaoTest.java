/*
 * ShellDescriptorPropertiesDaoTest.java
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
import net.sf.wickedshell.domain.DomainID;
import net.sf.wickedshell.domain.configuration.IShellDescriptorProperties;
import net.sf.wickedshell.domain.configuration.ShellDescriptorPropertiesDao;

/**
 * @author Stefan Reichert
 * @since 16.11.2006
 */
public class ShellDescriptorPropertiesDaoTest extends TestCase {

	/** The <code>ShellDescriptorProperties</code> to test. */
	private ShellDescriptorPropertiesDao shellDescriptorPropertiesDao;

	/**
	 * Constructor for ShellDescriptorPropertiesDaoTest.
	 */
	public ShellDescriptorPropertiesDaoTest() {
		shellDescriptorPropertiesDao = new ShellDescriptorPropertiesDao();
		setName("Test for ShellDescriptorPropertiesDao");
	}

	/**
	 * @see junit.framework.TestCase#runTest()
	 */
	protected void runTest() throws Throwable {
		testWriteShellDescriptorProperties();
		testReadShellDescriptorProperties();
		testWriteDefaultShellDescriptorProperties();
	}

	/**
	 * Test method for
	 * {@link net.sf.wickedshell.domain.configuration.ShellDescriptorPropertiesDao#readShellDescriptorProperties()}.
	 */
	public void testReadShellDescriptorProperties() throws Throwable {
		List<IShellDescriptorProperties> testDescriptorProperties = shellDescriptorPropertiesDao.readShellDescriptorProperties();
		assertEquals("test shell descriptor properties count", 10, testDescriptorProperties.size());
		for (int index = 0; index < testDescriptorProperties.size(); index++) {
			IShellDescriptorProperties descriptorProperties = testDescriptorProperties.get(index);
			assertEquals("descriptor properties id", "net.sf.wickedshell.test.descriptor." + index, descriptorProperties.getShellDescriptorId());
			assertEquals("descriptor properties root", String.valueOf(index), descriptorProperties.getRootDirectory());
			assertEquals("descriptor properties OS default", false, descriptorProperties.isOSDefault());
		}
	}

	/**
	 * Test method for
	 * {@link net.sf.wickedshell.domain.configuration.ShellDescriptorPropertiesDao#writeDefaultShellDescriptorProperties()}.
	 */
	public void testWriteDefaultShellDescriptorProperties() throws Throwable {
		shellDescriptorPropertiesDao.writeDefaultShellDescriptorProperties();
		List<IShellDescriptorProperties> defaultShellDescriptorProperties = shellDescriptorPropertiesDao.readShellDescriptorProperties();
		assertTrue("default shell descriptor properties contains", defaultShellDescriptorProperties.contains(IShellDescriptorProperties.Factory.newInstance(
				DomainID.CMD_SHELL_DESCRIPTOR_ID, new String(), true)));
		assertTrue("default shell descriptor properties contains", defaultShellDescriptorProperties.contains(IShellDescriptorProperties.Factory.newInstance(
				DomainID.COMMAND_SHELL_DESCRIPTOR_ID, new String(), true)));
		assertTrue("default shell descriptor properties contains", defaultShellDescriptorProperties.contains(IShellDescriptorProperties.Factory.newInstance(
				DomainID.BASH_SHELL_DESCRIPTOR_ID, new String(), true)));
		assertTrue("default shell descriptor properties contains", defaultShellDescriptorProperties.contains(IShellDescriptorProperties.Factory.newInstance(
				DomainID.CYGWIN_BASH_SHELL_DESCRIPTOR_ID, new String(), false)));
		assertTrue("default shell descriptor properties contains", defaultShellDescriptorProperties.contains(IShellDescriptorProperties.Factory.newInstance(
				DomainID.MSYS_SH_SHELL_DESCRIPTOR_ID, new String(), false)));
	}

	/**
	 * Test method for
	 * {@link net.sf.wickedshell.domain.configuration.ShellDescriptorPropertiesDao#writeShellDescriptorProperties(java.util.List)}.
	 */
	@SuppressWarnings("unchecked")
	public void testWriteShellDescriptorProperties() throws Throwable {
		List testDescriptorProperties = new ArrayList();
		for (int index = 0; index < 10; index++) {
			IShellDescriptorProperties descriptorProperties = IShellDescriptorProperties.Factory.newInstance("net.sf.wickedshell.test.descriptor." + index,
					String.valueOf(index), false);
			assertEquals("descriptor properties id", "net.sf.wickedshell.test.descriptor." + index, descriptorProperties.getShellDescriptorId());
			assertEquals("descriptor properties root", String.valueOf(index), descriptorProperties.getRootDirectory());
			assertEquals("descriptor properties OS default", false, descriptorProperties.isOSDefault());
			testDescriptorProperties.add(descriptorProperties);
		}
		shellDescriptorPropertiesDao.writeShellDescriptorProperties(testDescriptorProperties);
	}

}
