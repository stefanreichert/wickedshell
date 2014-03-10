/*
 * IShellViewer.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.ui.shell.viewer;

import net.sf.wickedshell.facade.IShellFacade;
import net.sf.wickedshell.listener.IShellOutputListener;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;

/**
 * The <code>IShellViewer</code> represents an <code>IAdaptable</code>
 * Interface for a <i>Viewer</i> which displays a shell <i>Widget</i>.
 * 
 * @author Stefan Reichert
 * @since 18.10.2006
 */
public interface IShellViewer extends IShellOutputListener, IAdaptable {

	/**
	 * Returns the shells display <code>Control</code>.
	 * 
	 * @return the shells display <code>Control</code>
	 */
	Control getShellDisplayControl();

	/**
	 * Returns the <code>IShellFacade</code> which is used to run this
	 * <code>IShellViewer</code>.
	 * 
	 * @return the <code>IShellFacade</code> used
	 */
	IShellFacade getShellFacade();

	/**
	 * Returns whether the current position of the caret lies within the
	 * editable range of the <code>IShellViewer</code>. The editable range
	 * starts in the last line right behind the path delimiter defined in the
	 * <code>IShellDescriptor</code>.
	 * 
	 * @return whether the current position of the caret is valid for editing
	 */
	boolean isCurrentCaretPositionValid();

	/**
	 * Returns the offset of the currently entered command. The offset marks a
	 * position in the last line right behind the the path delimiter defined in
	 * the <code>IShellDescriptor</code>.
	 * 
	 * @return the offset of the currently entered command.
	 */
	int getCurrentCommandOffset();

	/**
	 * Returns the currently entered command. The currently entered command is
	 * the text in the last line starting right behind the path delimiter
	 * defined in the <code>IShellDescriptor</code>.
	 * 
	 * @return the currently entered command
	 */
	String getCurrentCommand();

	/**
	 * Sets the current command of the shell. The current command is replaced a
	 * the givven command is inserted after the current command offset.
	 * 
	 * @param command
	 *            The current command to set
	 */
	void setCurrentCommand(String command);

	/**
	 * Returns the current path of the shell. The current path is the text from
	 * the beginning of the last line right until the path delimiter defined in
	 * the <code>IShellDescriptor</code>.
	 * 
	 * @return the current path of the shell.
	 */
	String getCurrentPath();

	/**
	 * Returns the current position of the caret within the viewer.
	 * 
	 * @return the current position of the caret within the viewer as
	 *         <code>int</code>
	 */
	int getCurrentCaretPosition();

	/**
	 * Sets the current position of the caret within the viewer.
	 * 
	 * @return the current position of the caret within the viewer as
	 *         <code>int</code>
	 */
	void setCurrentCaretPosition(int caretPosition);

	/**
	 * Returns the current selection of text within the viewer.
	 * 
	 * @return the current selection as <code>Point</code> where
	 *         <code>aPoint.x</code> marks the start and <code>aPoint.y</code>
	 *         the end
	 */
	Point getCurrentTextSelection();

	/**
	 * Sets the current selection of text within the viewer.
	 * 
	 * @param selection
	 *            The current selection as <code>Point</code> where
	 *            <code>aPoint.x</code> marks the start and
	 *            <code>aPoint.y</code> the end
	 */
	void setCurrentTextSelection(Point selection);

	/**
	 * Returns the count of the current selection of text within the viewer.
	 * 
	 * @return the count as <code>int</code>
	 */
	int getCurrentTextSelectionCount();

	/**
	 * Appends the given <code>String</code> to the current command.
	 * 
	 * @param text
	 *            the text to append
	 */
	void appendToCurrentCommand(String text);

	/**
	 * Initializes this shell viewer fo the given facade.
	 * 
	 * @param shellFacade
	 *            The shell facade to use for initialization
	 */
	void initialize(IShellFacade shellFacade);

	/**
	 * Clears this shell viewer. Clearing means deleting all shell output and
	 * provide a prompt.
	 */
	void clear();

	/**
	 * Gets the length of the current content of the viewer.
	 * 
	 * @return the length of the conent as <code>int</code>
	 */
	int getContentLength();

	/**
	 * Returns whether the path delimiter defined in the
	 * <code>IShellDescriptor</code> is present in the last line.
	 * 
	 * @return whether the path delimiter is present in the last line
	 */
	public boolean isPathDelimiterAvailableInLastLine();

	/**
	 * Refreshes the background of the viewer with the values from the
	 * preferences.
	 */
	void refreshBackground();

	/**
	 * Refreshes the font of the viewer with the values from the preferences.
	 */
	void refreshFont();

	/**
	 * Refreshes the foreground of the viewer with the values from the
	 * preferences.
	 */
	void refreshForeground();

	/**
	 * En-/Disables the scroll lock.
	 * 
	 * @param scrollLock
	 *            The flag whether scrolllock is en- or disabled
	 */
	public void setScrollLock(boolean scrollLock);

	/**
	 * Gets the content of the viewer, which is the yet captured shell content.
	 */
	public String getViewerContent();
}
