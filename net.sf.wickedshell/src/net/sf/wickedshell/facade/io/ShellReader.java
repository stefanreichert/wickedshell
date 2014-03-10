/*
 * ShellReader.java
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.wickedshell.domain.DomainID;
import net.sf.wickedshell.facade.descriptor.IShellDescriptor;
import net.sf.wickedshell.listener.IShellOutputListener;
import net.sf.wickedshell.util.ShellLogger;

/**
 * A <code>ShellReader</code> is a reader of a shell's
 * <code>InputStraem</code>. It allows registering <code>ShellPrinter</code>
 * that are triggered on input.
 * 
 * @author Stefan Reichert
 * @since 11.05.2005
 */
public class ShellReader implements Runnable {

	/** The <code>Reader</code> of the shell. */
	private final ShellLogger shellLogger = new ShellLogger(ShellReader.class);

	/** The length for the buffer used to read the input of the shell. */
	public static final int BUFFER_LENGTH = 512;

	/** The <code>Reader</code> of the shell. */
	private final InputStreamReader shellReader;

	/** The name of this reader. */
	private final String name;

	/** The <code>List</code> of this <code>ShellReader</code>. */
	private final List<IShellOutputListener> listenerList;

	/** The flag whether the <code>ShellReader</code> is active. */
	private boolean active;

	/** The flag whether the output is error output. */
	private boolean isErrorReader;

	/**
	 * Constructor for ShellReader.
	 * 
	 * @throws UnsupportedEncodingException
	 *             If the given encoding does not exist
	 */
	public ShellReader(String name, IShellDescriptor descriptor, InputStream shellInputStream)
			throws UnsupportedEncodingException {
		this(name, descriptor, shellInputStream, false);
	}

	/**
	 * Constructor for ShellReader.
	 * 
	 * @throws UnsupportedEncodingException
	 *             If the given encoding does not exist
	 */
	public ShellReader(String namePrefix, IShellDescriptor descriptor,
			InputStream shellInputStream, boolean isErrorReader)
			throws UnsupportedEncodingException {
		super();
		active = true;
		if(!descriptor.getCharacterEncoding().equals(DomainID.DEFAULT_OS_CHARACTER_ENCODING)) {
			shellReader = new InputStreamReader(shellInputStream, descriptor.getCharacterEncoding());
		}
		else {
			shellReader = new InputStreamReader(shellInputStream);
		}
		listenerList = new ArrayList<IShellOutputListener>();
		StringBuffer nameBuffer = new StringBuffer(namePrefix);
		nameBuffer.append(" {");
		nameBuffer.append(descriptor.getId());
		nameBuffer.append(" - ");
		nameBuffer.append(new SimpleDateFormat("HH:mm:ss").format(new Date()));
		nameBuffer.append("}");
		this.name = nameBuffer.toString();
		this.isErrorReader = isErrorReader;
		shellLogger.debug("ShellReader created with encoding [" + shellReader.getEncoding() + "]");
	}

	/**
	 * If active, this method activates this reader by starting a new
	 * <code>Thread</code> reading the given shellInputStream.
	 */
	public void activate() {
		if(active) {
			Thread readerThread = new Thread(this, name);
			readerThread.start();
		}
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			char[] input;
			while (active) {
				input = new char[BUFFER_LENGTH];
				int offset = shellReader.read(input);
				if(offset != -1) {
					handleInput(String.valueOf(input).substring(0, offset));
				}
				else {
					// EOF --> terminate
					deactivate();
				}
			}
		}
		catch (IOException exception) {
			shellLogger.error(exception.getMessage(), exception);
		}
	}

	/**
	 * Handles the input which was read from the shell. Notifies all registered
	 * <code>Printers</code>.
	 */
	public void handleInput(String inputString) {
		Object[] listeners = listenerList.toArray();
		for (int index = 0; index < listeners.length; index++) {
			IShellOutputListener outputListener = (IShellOutputListener) listeners[index];
			if(isErrorReader) {
				outputListener.handleShellErrorOutput(inputString);
			}
			else {
				outputListener.handleShellOutput(inputString);
			}
		}
	}

	/**
	 * Adds a <code>IShellOutputListener</code> which is notified on shell
	 * input.
	 */
	public void addShellOutputListener(IShellOutputListener shellOutputListener) {
		listenerList.add(shellOutputListener);
	}

	/**
	 * Removes a <code>IShellOutputListener</code>.
	 */
	public void removeShellOutputListener(IShellOutputListener shellOutputListener) {
		listenerList.remove(shellOutputListener);
	}

	/**
	 * Deactivates the ShellReader.
	 */
	public void deactivate() {
		listenerList.clear();
		active = false;
	}
}
