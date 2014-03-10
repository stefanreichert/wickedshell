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
package net.sf.wickedshell.domain.style;

import java.io.File;
import java.io.IOException;

import net.sf.wickedshell.domain.DomainID;
import net.sf.wickedshell.domain.DomainPlugin;

import org.apache.xmlbeans.XmlException;

/**
 * @author Stefan Reichert
 * @since 22.10.2006
 */
public class ShellStyleDao {

	/**
	 * Returns the <code>IFontDescriptor</code> stored as shell font in the <i>StateLocation</i>.
	 * First checks whether the corresponding XML-File exists. If not an empty one is created.
	 * 
	 * @return the <code>IFontDescriptor</code>
	 * @throws IOException
	 *         if creating/parsing the file fails
	 */
	public IFontDescriptor readFontDescriptor() throws IOException, XmlException {
		String stateLocation = DomainPlugin.getDefault().getStateLocation().toOSString();
		File shellStyleDocumentFile = new File(stateLocation, DomainID.SHELL_STYLE_FILE);
		if (!shellStyleDocumentFile.exists()) {
			writeDefaultShellStyle();
		}
		ShellStyleDocument shellStyleDocument = ShellStyleDocument.Factory
				.parse(shellStyleDocumentFile);
		return IFontDescriptor.Factory.newInstance(shellStyleDocument.getShellStyle()
				.getFontDescriptor());
	}

	/**
	 * Returns the <code>RGB</code> stored as background color in the <i>StateLocation</i>. First
	 * checks whether the corresponding XML-File exists. If not an empty one is created.
	 * 
	 * @return the <code>List</code> of <code>CommandDescriptor</code>
	 * @throws IOException
	 *         if creating/parsing the file fails
	 */
	public IColorDescriptor readBackgroundColor() throws IOException, XmlException {
		String stateLocation = DomainPlugin.getDefault().getStateLocation().toOSString();
		File shellStyleDocumentFile = new File(stateLocation, DomainID.SHELL_STYLE_FILE);
		if (!shellStyleDocumentFile.exists()) {
			writeDefaultShellStyle();
		}
		ShellStyleDocument shellStyleDocument = ShellStyleDocument.Factory
				.parse(shellStyleDocumentFile);
		ColorDescriptor backgroundColorDescriptor = shellStyleDocument.getShellStyle()
				.getBackgroundColor().getColorDescriptor();
		return IColorDescriptor.Factory.newInstance(backgroundColorDescriptor);
	}

	/**
	 * Returns the <code>RGB</code> stored as background color in the <i>StateLocation</i>. First
	 * checks whether the corresponding XML-File exists. If not an empty one is created.
	 * 
	 * @return the <code>List</code> of <code>CommandDescriptor</code>
	 * @throws IOException
	 *         if creating/parsing the file fails
	 */
	public IColorDescriptor readForegroundColor() throws IOException, XmlException {
		String stateLocation = DomainPlugin.getDefault().getStateLocation().toOSString();
		File shellStyleDocumentFile = new File(stateLocation, DomainID.SHELL_STYLE_FILE);
		if (!shellStyleDocumentFile.exists()) {
			writeDefaultShellStyle();
		}
		ShellStyleDocument shellStyleDocument = ShellStyleDocument.Factory
				.parse(shellStyleDocumentFile);
		ColorDescriptor foreColorDescriptor = shellStyleDocument.getShellStyle().getForegroundColor()
				.getColorDescriptor();
		return IColorDescriptor.Factory.newInstance(foreColorDescriptor);
	}

	/**
	 * Resets the <code>StaticCompletionList</code> of static <code>Completions</code> defined by
	 * the guy who wrote this plugin :)
	 */
	public void writeDefaultShellStyle() throws IOException {
		writeShellStyle(IFontDescriptor.Factory.newInstance("Courier New", 10, 0),
				IColorDescriptor.Factory.newInstance(255, 255, 255), IColorDescriptor.Factory
						.newInstance(0, 0, 128));
	}

	/**
	 * Persist the <code>List</code> of <code>ICommandDescriptor</code>.
	 * 
	 * @param foregroundColor
	 *        the <code>RGB</code> of the foreground color
	 */
	public void writeShellStyle(
			IFontDescriptor fontDescriptor,
			IColorDescriptor backgroundColor,
			IColorDescriptor foregroundColor) throws IOException {
		String stateLocation = DomainPlugin.getDefault().getStateLocation().toOSString();
		File shellStyleDocumentFile = new File(stateLocation, DomainID.SHELL_STYLE_FILE);
		if (!shellStyleDocumentFile.exists()) {
			shellStyleDocumentFile.delete();
		}
		final ShellStyleDocument shellStyleDocument = ShellStyleDocument.Factory.newInstance();
		ShellStyle shellStyle = shellStyleDocument.addNewShellStyle();
		// Write font
		FontDescriptor staticFontDescriptor = shellStyle.addNewFontDescriptor();
		staticFontDescriptor.setName(fontDescriptor.getName());
		staticFontDescriptor.setHeight(String.valueOf(fontDescriptor.getHeight()));
		staticFontDescriptor.setStyle(String.valueOf(fontDescriptor.getStyle()));
		// Write background color
		ColorDescriptor backgroundColorDescriptor = shellStyle.addNewBackgroundColor()
				.addNewColorDescriptor();
		backgroundColorDescriptor.setRed(String.valueOf(backgroundColor.getRed()));
		backgroundColorDescriptor.setBlue(String.valueOf(backgroundColor.getBlue()));
		backgroundColorDescriptor.setGreen(String.valueOf(backgroundColor.getGreen()));
		// Write foreground color
		ColorDescriptor foregroundColorDescriptor = shellStyle.addNewForegroundColor()
				.addNewColorDescriptor();
		foregroundColorDescriptor.setRed(String.valueOf(foregroundColor.getRed()));
		foregroundColorDescriptor.setBlue(String.valueOf(foregroundColor.getBlue()));
		foregroundColorDescriptor.setGreen(String.valueOf(foregroundColor.getGreen()));
		shellStyleDocument.save(shellStyleDocumentFile);
	}
}
