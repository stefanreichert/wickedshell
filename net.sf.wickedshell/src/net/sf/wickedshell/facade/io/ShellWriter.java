/*
 * ShellWriter.java
 * 
 * Copyright 2003-2004 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.facade.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import net.sf.wickedshell.domain.DomainID;
import net.sf.wickedshell.facade.descriptor.IShellDescriptor;
import net.sf.wickedshell.util.ShellLogger;

/**
 * @author Stefan Reichert
 * @since 13.05.2005
 */
public class ShellWriter {

	/** The <code>Reader</code> of the shell. */
	private final ShellLogger shellLogger = new ShellLogger(ShellWriter.class);

	/** The <code>Reader</code> of the shell. */
	private final OutputStreamWriter shellWriter;

	/** The <code>OutputStream</code> of the shell. */
	private final OutputStream shellOutputStream;

	/** The active <code>IShellDescriptor</code>. */
	private final IShellDescriptor shellDescriptor;

	/**
	 * Constructor for ShellWriter.
	 */
	public ShellWriter(IShellDescriptor shellDescriptor, OutputStream shellOutputStream)
			throws UnsupportedEncodingException {
		super();
		this.shellDescriptor = shellDescriptor;
		this.shellOutputStream = shellOutputStream;
		if(!shellDescriptor.getCharacterEncoding().equals(DomainID.DEFAULT_OS_CHARACTER_ENCODING)) {
			shellWriter = new OutputStreamWriter(shellOutputStream, shellDescriptor
					.getCharacterEncoding());
		}
		else {
			shellWriter = new OutputStreamWriter(shellOutputStream);
		}
		shellLogger.debug("ShellWriter created with encoding [" + shellWriter.getEncoding() + "]");
	}

	/**
	 * Writes a command to the shellProcess.
	 * 
	 * @param command
	 *            The command to write
	 */
	public void writeCommand(String command) {
		try {
			shellWriter.write(command.toCharArray());
			shellWriter.write(shellDescriptor.getLineFeedString());
			shellWriter.flush();
		}
		catch (IOException exception) {
			shellLogger.error(exception.getMessage(), exception);
		}
	}

	/**
	 * Writes a command to the shellProcess.
	 * 
	 * @param command
	 *            The command to write
	 */
	public void writeInterrupt() {
		try {
			char ctrlC = 0x3;
			shellOutputStream.write(getBytes(new char[] { ctrlC }));
			shellOutputStream.write(getBytes(new char[] { '^', 'C' }));
			shellOutputStream.flush();
		}
		catch (IOException exception) {
			shellLogger.error(exception.getMessage(), exception);
		}
	}

	private byte[] getBytes(char[] chars) {
		String encoding = System.getProperty("file.encoding");
		if(!shellDescriptor.getCharacterEncoding().equals(DomainID.DEFAULT_OS_CHARACTER_ENCODING)) {
			encoding = shellDescriptor.getCharacterEncoding();
		}
		Charset charset = Charset.forName(encoding);
		CharBuffer charBuffer = CharBuffer.allocate(chars.length);
		charBuffer.put(chars);
		charBuffer.flip();
		ByteBuffer byteBuffer = charset.encode(charBuffer);
		return byteBuffer.array();
	}

	/**
	 * Deactivates the ShellWriter.
	 */
	public void deactivate() {
		try {
			shellWriter.flush();
			shellWriter.close();
		}
		catch (IOException exception) {
			shellLogger.error(exception.getMessage(), exception);
		}
	}

}
