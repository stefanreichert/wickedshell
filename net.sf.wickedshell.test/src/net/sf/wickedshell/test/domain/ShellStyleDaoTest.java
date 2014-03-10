/*
 * ShellStyleDaoTest.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.test.domain;

import junit.framework.TestCase;
import net.sf.wickedshell.domain.style.IColorDescriptor;
import net.sf.wickedshell.domain.style.IFontDescriptor;
import net.sf.wickedshell.domain.style.ShellStyleDao;

/**
 * @author Stefan Reichert
 * @since 15.11.2006
 */
public class ShellStyleDaoTest extends TestCase {

	/** The <code>ShellStyleDao</code> to test. */
	private ShellStyleDao shellStyleDao;

	/**
	 * Constructor for ShellStyleDaoTest.
	 */
	public ShellStyleDaoTest() {
		shellStyleDao = new ShellStyleDao();
		setName("Test for ShellStyleDao");
	}

	/**
	 * @see junit.framework.TestCase#runTest()
	 */
	protected void runTest() throws Throwable {
		testWriteShellStyle();
		testReadForegroundColor();
		testReadBackgroundColor();
		testReadFontDescriptor();
		testWriteDefaultShellStyle();
	}

	/**
	 * Test method for {@link net.sf.wickedshell.domain.shellStyle.ShellStyleDao#writeShellStyle()}.
	 */
	public void testWriteShellStyle() throws Throwable {
		IColorDescriptor foregroundColorDescriptor = IColorDescriptor.Factory
				.newInstance(10, 60, 110);
		assertEquals("test oreground red", 10, foregroundColorDescriptor.getRed());
		assertEquals("test foreground green", 60, foregroundColorDescriptor.getGreen());
		assertEquals("test foreground blue", 110, foregroundColorDescriptor.getBlue());

		IColorDescriptor backgroundColorDescriptor = IColorDescriptor.Factory
				.newInstance(110, 60, 10);
		assertEquals("test background red", 110, backgroundColorDescriptor.getRed());
		assertEquals("test background green", 60, backgroundColorDescriptor.getGreen());
		assertEquals("test background blue", 10, backgroundColorDescriptor.getBlue());

		IFontDescriptor fontDescriptor = IFontDescriptor.Factory.newInstance("Courier", 14, 2);
		assertEquals("test font name", "Courier", fontDescriptor.getName());
		assertEquals("test font height", 14, fontDescriptor.getHeight());
		assertEquals("test font style", 2, fontDescriptor.getStyle());

		shellStyleDao.writeShellStyle(fontDescriptor, backgroundColorDescriptor,
				foregroundColorDescriptor);
	}

	/**
	 * Test method for
	 * {@link net.sf.wickedshell.domain.shellStyle.ShellStyleDao#readForegroundColor()}.
	 */
	public void testReadForegroundColor() throws Throwable {
		IColorDescriptor foregroundColorDescriptor = shellStyleDao.readForegroundColor();
		assertEquals("test foreground red", 10, foregroundColorDescriptor.getRed());
		assertEquals("test foreground green", 60, foregroundColorDescriptor.getGreen());
		assertEquals("test foreground blue", 110, foregroundColorDescriptor.getBlue());
	}

	/**
	 * Test method for
	 * {@link net.sf.wickedshell.domain.shellStyle.ShellStyleDao#readBackgroundColor()}.
	 */
	public void testReadBackgroundColor() throws Throwable {
		IColorDescriptor backgroundColorDescriptor = shellStyleDao.readBackgroundColor();
		assertEquals("test background red", 110, backgroundColorDescriptor.getRed());
		assertEquals("test background green", 60, backgroundColorDescriptor.getGreen());
		assertEquals("test background blue", 10, backgroundColorDescriptor.getBlue());
	}

	/**
	 * Test method for
	 * {@link net.sf.wickedshell.domain.shellStyle.ShellStyleDao#readFontDescriptor()}.
	 */
	public void testReadFontDescriptor() throws Throwable {
		IFontDescriptor fontDescriptor = shellStyleDao.readFontDescriptor();
		assertEquals("test font name", "Courier", fontDescriptor.getName());
		assertEquals("test font height", 14, fontDescriptor.getHeight());
		assertEquals("test font style", 2, fontDescriptor.getStyle());
	}

	/**
	 * Test method for
	 * {@link net.sf.wickedshell.domain.shellStyle.ShellStyleDao#writeDefaultShellStyle()}.
	 */
	public void testWriteDefaultShellStyle() throws Throwable {
		shellStyleDao.writeDefaultShellStyle();

		IColorDescriptor foregroundColorDescriptor = shellStyleDao.readForegroundColor();
		assertEquals("default foreground red", 0, foregroundColorDescriptor.getRed());
		assertEquals("default foreground green", 0, foregroundColorDescriptor.getGreen());
		assertEquals("default foreground blue", 128, foregroundColorDescriptor.getBlue());

		IColorDescriptor backgroundColorDescriptor = shellStyleDao.readBackgroundColor();
		assertEquals("default background red", 255, backgroundColorDescriptor.getRed());
		assertEquals("default background green", 255, backgroundColorDescriptor.getGreen());
		assertEquals("default background blue", 255, backgroundColorDescriptor.getBlue());

		IFontDescriptor fontDescriptor = shellStyleDao.readFontDescriptor();
		assertEquals("default font name", "Courier New", fontDescriptor.getName());
		assertEquals("default font height", 10, fontDescriptor.getHeight());
		assertEquals("default font style", 0, fontDescriptor.getStyle());
	}

}
