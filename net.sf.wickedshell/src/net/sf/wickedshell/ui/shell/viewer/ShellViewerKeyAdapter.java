/*
 * ShellViewerKeyAdapter.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.ui.shell.viewer;

import net.sf.wickedshell.facade.history.ICommandHistory;
import net.sf.wickedshell.ui.shell.viewer.adapter.ICommandHistoryHandler;
import net.sf.wickedshell.util.ShellLogger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Stefan Reichert
 * @since 18.10.2006
 */
public class ShellViewerKeyAdapter implements Listener {

	/** The <code>Reader</code> of the shell. */
	private final ShellLogger shellLogger = new ShellLogger(ShellViewerKeyAdapter.class);

	/** The history index if no selection ocurred yet. */
	public static final int DEFAULT_SELECTION = -1;

	/** The adapted <code>IShellViewer</code>. */
	private IShellViewer shellViewer;

	/** The index of the history that was requested the latest. */
	private int historyIndex;

	/**
	 * The <code>ICommandHistoryHandler</code> of the adapted
	 * <code>IShellViewer</code> or <code>null</code> if none available.
	 */
	private ICommandHistoryHandler commandHistoryHandler;

	/**
	 * Adapts the given <code>IShellViewer</code>.
	 */
	public static final void adapt(IShellViewer shellViewer) {
		shellViewer.getShellDisplayControl().addListener(SWT.KeyDown, new ShellViewerKeyAdapter(shellViewer));
	}

	/**
	 * Constructor for ShellViewerKeyAdapter.
	 */
	private ShellViewerKeyAdapter(IShellViewer shellViewer) {
		super();
		// private constructor to avoid instantiation
		this.shellViewer = shellViewer;
		commandHistoryHandler = (ICommandHistoryHandler) shellViewer.getAdapter(ICommandHistoryHandler.class);
	}

	public void handleEvent(Event event) {
		if(event.doit) {
			if(event.character == SWT.CR || event.character == SWT.LF) {
				handleExecute(event);
			}
			else if(event.keyCode == SWT.ARROW_UP) {
				handleCommandHistoryUp(event);
			}
			else if(event.keyCode == SWT.ARROW_DOWN) {
				handleCommandHistoryDown(event);
			}
			else if(event.keyCode == SWT.PAGE_UP || event.keyCode == SWT.PAGE_UP) {
				// forbidden keys (vertical navigation)
				event.doit = false;
			}
			else if(event.stateMask == SWT.CTRL && event.keyCode == 'c') {
				// Copy is allowed
			}
			else {
				// everything else only within the range of the current command
				if(shellViewer.isCurrentCaretPositionValid()) {
					// We need special handling of backward-navigation (LEFT,
					// HOME and BACKSPACE)
					if(event.keyCode == SWT.ARROW_LEFT) {
						handleMoveLeft(event);
					}
					else if(event.keyCode == SWT.HOME) {
						handleMoveHome(event);
					}
					else if(event.character == SWT.BS) {
						handleBackspace(event);
					}
					else if(event.keyCode == SWT.TAB) {
						// forbidden key --> TAB is CA activation
						event.doit = false;
					}
				}
				else if(event.keyCode != SWT.CTRL && shellViewer.isPathDelimiterAvailableInLastLine()) {
					handleCharacterWithMissingPathDelimiter(event);
				}
				else if(event.keyCode != SWT.CTRL) {
					// everything else out the range of the current command is
					// forbidden so move the cursor to after the last character
					// and print the character
					shellViewer.appendToCurrentCommand(String.valueOf(event.character));
					shellViewer.setCurrentCaretPosition(shellViewer.getContentLength());
					event.doit = false;
				}
				else {
					// Everything else is forbidden
					event.doit = false;
				}
			}
		}
	}

	/**
	 * Handles a <code>KeyEvent</code> with a new character when the
	 * <code>IShellViewer</code> missses the defined <i>path delimiter</i> of
	 * the <code>IShellDescriptor</code>.
	 * 
	 * @param event
	 *            The <code>KeyEvent</code> to handle
	 */
	private void handleCharacterWithMissingPathDelimiter(Event event) {
		shellLogger.debug("No path delimiter found in last line; Appending path delimiter ["
				+ shellViewer.getShellFacade().getShellDescriptor().getPathDelimiter() + "]");
		shellViewer.appendToCurrentCommand(shellViewer.getShellFacade().getShellDescriptor().getPathDelimiter());
		shellViewer.appendToCurrentCommand(String.valueOf(event.character));
		shellViewer.setCurrentCaretPosition(shellViewer.getContentLength());
		event.doit = false;
	}

	/**
	 * Handles a backspace <code>KeyEvent</code>.
	 * 
	 * @param event
	 *            The <code>KeyEvent</code> to handle
	 */
	private void handleBackspace(Event event) {
		// BACKSPACE only back to the path delimiter
		if(shellViewer.getCurrentCaretPosition() == shellViewer.getCurrentCommandOffset()) {
			event.doit = shellViewer.getCurrentTextSelectionCount() > 0;
		}
	}

	/**
	 * Handles a move home <code>KeyEvent</code>. This is a little tricky as
	 * only the last line starting behind the defined <i>path delimiter</i> of
	 * the <code>IShellDescriptor</code> is editable.
	 * 
	 * @param event
	 *            The <code>KeyEvent</code> to handle
	 */
	private void handleMoveHome(Event event) {
		// Handling of HOME (keystroke is within the range of
		// the current command)
		if(event.stateMask == SWT.SHIFT) {
			// Jump left with SHIFT-HOME or only back to the
			// path delimiter
			int xSelection = shellViewer.getCurrentTextSelection().x;
			shellViewer.setCurrentTextSelection(new Point(shellViewer.getCurrentCommandOffset(), xSelection));
			event.doit = false;
		}
		else if(event.stateMask != SWT.NONE) {
			// forbidden combination of keys (navigation)
			event.doit = false;
		}
		else {
			// Jump left with HOME or only back to the path
			// delimiter
			shellViewer.setCurrentCaretPosition(shellViewer.getCurrentCommandOffset());
			event.doit = false;
		}
	}

	/**
	 * Handles a move left <code>KeyEvent</code>. This is a little tricky as
	 * only the last line starting behind the defined <i>path delimiter</i> of
	 * the <code>IShellDescriptor</code> is editable.
	 * 
	 * @param event
	 *            The <code>KeyEvent</code> to handle
	 */
	private void handleMoveLeft(Event event) {
		// Handling of LEFT (keystroke is within the the range
		// of the current command)
		if(event.stateMask == SWT.NONE && shellViewer.getCurrentCaretPosition() == shellViewer.getCurrentCommandOffset()) {
			// LEFT only back to the path delimiter
			shellViewer.setCurrentCaretPosition(shellViewer.getCurrentCommandOffset());
			event.doit = false;
		}
		else if(event.stateMask == SWT.SHIFT && shellViewer.getCurrentCaretPosition() == shellViewer.getCurrentCommandOffset()) {
			event.doit = false;
		}
		else if(event.stateMask == SWT.CTRL) {
			// Jump left with CTRL-LEFT only back to the path
			// delimiter
			shellViewer.setCurrentCaretPosition(shellViewer.getCurrentCommandOffset());
			event.doit = false;
		}
		else if(event.stateMask == (SWT.CTRL | SWT.SHIFT)) {
			// Jump left with CTRL-SHIFT-LEFT only back to the
			// path delimiter
			int ySelection = shellViewer.getCurrentTextSelection().y;
			shellViewer.setCurrentTextSelection(new Point(shellViewer.getCurrentCommandOffset(), ySelection));
			event.doit = false;
		}
		else if(event.stateMask != SWT.NONE && event.stateMask != SWT.SHIFT) {
			// forbidden combination of keys
			event.doit = false;
		}
	}

	/**
	 * Handles a down <code>KeyEvent</code> for the command history. If
	 * available, the previous entry from the command history should be
	 * displayed.
	 * 
	 * @param event
	 *            The <code>KeyEvent</code> to handle
	 */
	private void handleCommandHistoryDown(Event event) {
		// array up/down within the range of the current command means
		// history access
		if(shellViewer.isCurrentCaretPositionValid()) {
			ICommandHistory commandHistory = shellViewer.getShellFacade().getCommandHistory();
			if(historyIndex >= commandHistory.getSize(shellViewer.getShellFacade().getShellDescriptor().getId())) {
				historyIndex = DEFAULT_SELECTION;
			}
			if(historyIndex >= 1) {
				historyIndex--;
				String command = commandHistory.getCommand(historyIndex, shellViewer.getShellFacade().getShellDescriptor().getId());
				shellViewer.setCurrentCommand(command);
				if(commandHistoryHandler != null && historyIndex != DEFAULT_SELECTION) {
					commandHistoryHandler.handleCommandRequest(commandHistory, historyIndex);
				}
				shellLogger.debug("Accessing history at index [" + historyIndex + "]; Setting command ["
						+ shellViewer.getShellFacade().getCommandHistory().getCommand(historyIndex, shellViewer.getShellFacade().getShellDescriptor().getId())
						+ "] ");
			}
			else {
				historyIndex = DEFAULT_SELECTION;
				shellViewer.setCurrentCommand(new String());
			}
		}

		event.doit = false;
	}

	/**
	 * Handles an up <code>KeyEvent</code> for the command history. If
	 * available, the next entry from the command history should be displayed.
	 * 
	 * @param event
	 *            The <code>KeyEvent</code> to handle
	 */
	private void handleCommandHistoryUp(Event event) {
		// array up/down within the range of the current command means
		// history access
		if(shellViewer.isCurrentCaretPositionValid()) {
			ICommandHistory commandHistory = shellViewer.getShellFacade().getCommandHistory();
			if(historyIndex >= commandHistory.getSize(shellViewer.getShellFacade().getShellDescriptor().getId())) {
				historyIndex = DEFAULT_SELECTION;
			}
			if((historyIndex + 1) < commandHistory.getSize(shellViewer.getShellFacade().getShellDescriptor().getId())) {
				historyIndex++;
				String command = commandHistory.getCommand(historyIndex, shellViewer.getShellFacade().getShellDescriptor().getId());
				shellViewer.setCurrentCommand(command);
				if(commandHistoryHandler != null) {
					commandHistoryHandler.handleCommandRequest(commandHistory, historyIndex);
				}
				shellLogger.debug("Accessing history at index [" + historyIndex + "]; Setting command ["
						+ shellViewer.getShellFacade().getCommandHistory().getCommand(historyIndex, shellViewer.getShellFacade().getShellDescriptor().getId())
						+ "] ");
			}
		}
		event.doit = false;
	}

	/**
	 * Handles an execution <code>KeyEvent</code>. The current command is
	 * transferred to the shell process.
	 * 
	 * @param event
	 *            The <code>KeyEvent</code> to handle
	 */
	private void handleExecute(Event event) {
		if(shellViewer.isCurrentCaretPositionValid()) {
			// carriage return or newline means enter was pressed --> if
			// within the h range of the
			// current command execute current command
			shellLogger.debug("Executing command [" + shellViewer.getCurrentCommand() + "] from ShellViewer");
			shellViewer.getShellFacade().executeCommand(shellViewer.getCurrentCommand());
		}
		historyIndex = DEFAULT_SELECTION;
		event.doit = false;
	}
}
