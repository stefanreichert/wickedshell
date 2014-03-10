/*
 * TextShellViewer.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.ui.shell.viewer.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.sf.wickedshell.ShellID;
import net.sf.wickedshell.ShellPlugin;
import net.sf.wickedshell.domain.DomainPlugin;
import net.sf.wickedshell.domain.completion.ICompletion.Completion;
import net.sf.wickedshell.domain.style.IColorDescriptor;
import net.sf.wickedshell.domain.style.IFontDescriptor;
import net.sf.wickedshell.facade.IShellFacade;
import net.sf.wickedshell.facade.history.ICommandHistory;
import net.sf.wickedshell.listener.IShellListener;
import net.sf.wickedshell.ui.shell.viewer.IShellViewer;
import net.sf.wickedshell.ui.shell.viewer.adapter.ICommandHistoryHandler;
import net.sf.wickedshell.ui.shell.viewer.proposal.ShellContentProposalProvider;
import net.sf.wickedshell.util.ResourceManager;
import net.sf.wickedshell.util.ShellLogger;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

/**
 * @author Stefan Reichert
 * @since 18.10.2006
 */
public class TextShellViewer implements IShellViewer, IShellListener {

	/** The <code>Reader</code> of the shell. */
	private final ShellLogger shellLogger = new ShellLogger(TextShellViewer.class);

	/** The <code>Text</code> functioning as display. */
	private Text shellDisplayControl;

	/** The active <code>IShellDescriptor</code>. */
	private IShellFacade shellFacade;

	/** Flag whether scrolllock is enabled. */
	private boolean scrollLock;

	/** The adapter for the proposal. */
	private ContentProposalAdapter adapter;

	/** The last line of the shell. */
	private String lastLine;

	private ICommandHistoryHandler commandHistoryHandler;

	private CommandHistoryDialog commandHistoryDialog;

	/**
	 * Constructor for TextShellViewer.
	 * 
	 * @param shellDisplayControl
	 *            The <code>Text</code> functioning as display
	 */
	public TextShellViewer(Composite parent, int style) {
		this(new Text(parent, style));
	}

	/**
	 * Constructor for TextShellViewer.
	 * 
	 * @param shellDisplayControl
	 *            The <code>Text</code> functioning as display
	 */
	public TextShellViewer(final Text shellDisplayControl) {
		super();
		this.shellDisplayControl = shellDisplayControl;
		refreshFont();
		refreshForeground();
		refreshBackground();
		initFieldAssist();
		this.lastLine = new String();
		this.shellDisplayControl.addModifyListener(new ModifyListener() {
			/**
			 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
			 */
			public void modifyText(ModifyEvent event) {
				int lastLineStart = shellDisplayControl.getText().lastIndexOf(shellDisplayControl.getLineDelimiter());
				if(lastLineStart == -1) {
					lastLine = shellDisplayControl.getText();
				}
				else {
					lastLine = shellDisplayControl.getText().substring(lastLineStart + shellDisplayControl.getLineDelimiter().length());
				}
			}
		});
		ShellPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(new org.eclipse.jface.util.IPropertyChangeListener() {
			/**
			 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
			 */
			public void propertyChange(PropertyChangeEvent event) {
				if(!TextShellViewer.this.shellDisplayControl.isDisposed()) {
					String property = event.getProperty();
					if(property.equals(ShellID.SHELL_STYLE_STRING_KEY)) {
						refreshBackground();
						refreshForeground();
						refreshFont();
					}
				}
			}
		});
		commandHistoryHandler = new ICommandHistoryHandler() {

			/**
			 * @see net.sf.wickedshell.ui.shell.viewer.adapter.ICommandHistoryHandler#handleCommandRequest(net.sf.wickedshell.facade.history.ICommandHistory,
			 *      int)
			 */
			public void handleCommandRequest(ICommandHistory commandHistory, int historyIndex) {
				if(ShellPlugin.getDefault().getPreferenceStore().getBoolean(ShellID.OPEN_COMMAND_HISTORY_DIALOG_STRING_KEY)) {
					if(commandHistoryDialog == null) {
						commandHistoryDialog = new CommandHistoryDialog(commandHistory);
					}
					commandHistoryDialog.updateTableContent(historyIndex);
				}
			};
		};
	}

	/**
	 * @see net.sf.wickedshell.ui.shell.viewer.IShellViewer#appendToCurrentCommand(java.lang.String)
	 */
	public synchronized void appendToCurrentCommand(String text) {
		shellDisplayControl.append(text);
	}

	/**
	 * @see net.sf.wickedshell.ui.shell.viewer.IShellViewer#getCurrentCaretPosition()
	 */
	public int getCurrentCaretPosition() {
		return shellDisplayControl.getCaretPosition();
	}

	/**
	 * @see net.sf.wickedshell.ui.shell.viewer.IShellViewer#getCurrentCommand()
	 */
	public String getCurrentCommand() {
		return shellDisplayControl.getText().substring(getCurrentCommandOffset());
	}

	/**
	 * @see net.sf.wickedshell.ui.shell.viewer.IShellViewer#getCurrentCommandOffset()
	 */
	public int getCurrentCommandOffset() {
		if(lastLine.indexOf(shellFacade.getShellDescriptor().getPathDelimiter()) != -1) {
			int delimiterLength = shellFacade.getShellDescriptor().getPathDelimiter().length();
			return shellDisplayControl.getText().length() - (lastLine.length() - getCurrentPath().length() - delimiterLength);
		}
		return shellDisplayControl.getText().length() + 1;
	}

	/**
	 * @see net.sf.wickedshell.ui.shell.viewer.IShellViewer#getCurrentPath()
	 */
	public String getCurrentPath() {
		int pathEnd = lastLine.indexOf(shellFacade.getShellDescriptor().getPathDelimiter());
		return lastLine.substring(0, pathEnd);
	}

	/**
	 * @see net.sf.wickedshell.ui.shell.viewer.IShellViewer#getCurrentTextSelection()
	 */
	public Point getCurrentTextSelection() {
		return shellDisplayControl.getSelection();
	}

	/**
	 * @see net.sf.wickedshell.ui.shell.viewer.IShellViewer#getCurrentTextSelectionCount()
	 */
	public int getCurrentTextSelectionCount() {
		return shellDisplayControl.getSelectionCount();
	}

	/**
	 * @see net.sf.wickedshell.ui.shell.viewer.IShellViewer#isCurrentCaretPositionValid()
	 */
	public boolean isCurrentCaretPositionValid() {
		return shellDisplayControl.getSelection().x >= getCurrentCommandOffset();
	}

	/**
	 * @see net.sf.wickedshell.ui.shell.viewer.IShellViewer#setCurrentCaretPosition(int)
	 */
	public void setCurrentCaretPosition(int caretPosition) {
		shellDisplayControl.setSelection(caretPosition);
	}

	/**
	 * @see net.sf.wickedshell.ui.shell.viewer.IShellViewer#setCurrentCommand(java.lang.String)
	 */
	public void setCurrentCommand(String command) {
		shellDisplayControl.setSelection(getCurrentCommandOffset(), shellDisplayControl.getText().length());
		shellDisplayControl.insert(command);
	}

	/**
	 * @see net.sf.wickedshell.ui.shell.viewer.IShellViewer#setCurrentTextSelection(org.eclipse.swt.graphics.Point)
	 */
	public void setCurrentTextSelection(Point selection) {
		shellDisplayControl.setSelection(selection);
	}

	/**
	 * @see net.sf.wickedshell.ui.shell.viewer.IShellViewer#clear()
	 */
	public void clear() {
		String currentPath = getCurrentPath();
		shellDisplayControl.setText("");
		shellDisplayControl.append(currentPath);
		shellDisplayControl.append(shellFacade.getShellDescriptor().getPathDelimiter());
	}

	/**
	 * @see net.sf.wickedshell.ui.shell.viewer.IShellViewer#reset()
	 */
	public void reset() {
		shellDisplayControl.setText("");
	}

	/**
	 * @see net.sf.wickedshell.ui.shell.viewer.IShellViewer#getContentLength()
	 */
	public int getContentLength() {
		return shellDisplayControl.getText().length();
	}

	/**
	 * @see net.sf.wickedshell.ui.shell.viewer.IShellViewer#isPathDelimiterAvailableInLastLine()
	 */
	public boolean isPathDelimiterAvailableInLastLine() {
		return lastLine.indexOf(shellFacade.getShellDescriptor().getPathDelimiter()) == -1;
	}

	/**
	 * @see net.sf.wickedshell.ui.shell.viewer.IShellViewer#refreshBackground()
	 */
	public void refreshBackground() {
		IColorDescriptor backgroundColorDescriptor = DomainPlugin.getDefault().getBackgroundColor();
		RGB backgroundRGB = new RGB(backgroundColorDescriptor.getRed(), backgroundColorDescriptor.getGreen(), backgroundColorDescriptor.getBlue());
		shellDisplayControl.setBackground(ResourceManager.getColor(backgroundRGB));
	}

	/**
	 * @see net.sf.wickedshell.ui.shell.viewer.IShellViewer#refreshFont()
	 */
	public void refreshFont() {
		IFontDescriptor fontDescriptor;
		fontDescriptor = DomainPlugin.getDefault().getFontDescriptor();
		shellDisplayControl.setFont(ResourceManager.getFont(fontDescriptor.getName(), fontDescriptor.getHeight(), fontDescriptor.getStyle()));
	}

	/**
	 * @see net.sf.wickedshell.ui.shell.viewer.IShellViewer#refreshForeground()
	 */
	public void refreshForeground() {
		IColorDescriptor foregroundColorDescriptor = DomainPlugin.getDefault().getForegroundColor();
		RGB foregroundRGB = new RGB(foregroundColorDescriptor.getRed(), foregroundColorDescriptor.getGreen(), foregroundColorDescriptor.getBlue());
		shellDisplayControl.setForeground(ResourceManager.getColor(foregroundRGB));
	}

	/**
	 * @see net.sf.wickedshell.ui.shell.viewer.IShellViewer#initialize(net.sf.wickedshell.facade.IShellFacade)
	 */
	public void initialize(IShellFacade shellFacade) {
		shellDisplayControl.setEditable(true);
		this.shellFacade = shellFacade;
		this.shellFacade.addShellOutputListener(this);
		this.shellFacade.addShellListener(this);
	}

	/**
	 * @see net.sf.wickedshell.listener.IShellOutputListener#handleShellErrorOutput(java.lang.String)
	 */
	public void handleShellErrorOutput(final String errorOutput) {
		if(!shellDisplayControl.isDisposed()) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					if(!shellDisplayControl.isDisposed()) {
						int scrollIndex = shellDisplayControl.getTopIndex();
						shellDisplayControl.append(errorOutput);
						if(scrollLock) {
							shellDisplayControl.setTopIndex(scrollIndex);
						}
					}
				}
			});
		}
	}

	/**
	 * @see net.sf.wickedshell.listener.IShellOutputListener#handleShellOutput(java.lang.String)
	 */
	public void handleShellOutput(final String output) {
		if(!shellDisplayControl.isDisposed()) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					if(!shellDisplayControl.isDisposed()) {
						int scrollIndex = shellDisplayControl.getTopIndex();
						shellDisplayControl.append(output);
						if(scrollLock) {
							shellDisplayControl.setTopIndex(scrollIndex);
						}
					}
				}
			});
		}
	}

	/**
	 * @see net.sf.wickedshell.ui.shell.viewer.IShellViewer#getShellDisplayControl()
	 */
	public Control getShellDisplayControl() {
		return shellDisplayControl;
	}

	/**
	 * @see net.sf.wickedshell.ui.shell.viewer.IShellViewer#setScrollLock(boolean)
	 */
	public void setScrollLock(boolean scrollLock) {
		this.scrollLock = scrollLock;
	}

	/**
	 * @see net.sf.wickedshell.ui.shell.viewer.IShellViewer#getShellFacade()
	 */
	public IShellFacade getShellFacade() {
		return shellFacade;
	}

	/**
	 * @see net.sf.wickedshell.listener.IShellListener#handleCommandExecution(java.lang.String)
	 */
	public void handleCommandExecution(String command) {
		shellLogger.debug("Processing command execution of [" + command + "]");
		if("cls".equals(command.toLowerCase())) {
			clear();
		}
		else if("exit".equals(command.toLowerCase())) {
			setCurrentCommand(command);
		}
		else if(command.endsWith("!")) {
			setCurrentCommand(new String());
		}
		else {
			if(shellFacade.getShellDescriptor().isExecutedCommandProvided()) {
				// If the current command is provided by the shell reset the
				// viewer to avoid displaying the command twice
				setCurrentCommand(new String());
			}
			else if(!getShellFacade().getShellDescriptor().isUILineFeedProvided()) {
				// process linedelimiter in the GUI if the shell doesn't
				// provide it and the shell is still active
				shellLogger.debug("UI linefeed is not provided; Appending UI linefeed");
				appendToCurrentCommand(Text.DELIMITER);
			}
		}
	}

	/**
	 * @see net.sf.wickedshell.listener.IShellListener#handleRestart()
	 */
	public void handleRestart() {
		reset();

		refreshFont();
		refreshForeground();
		refreshBackground();
	}

	/**
	 * @see net.sf.wickedshell.listener.IShellListener#handleActivation()
	 */
	public void handleActivation() {
		shellDisplayControl.setEditable(true);
	}

	/**
	 * @see net.sf.wickedshell.listener.IShellListener#handleDeactivation()
	 */
	public void handleDeactivation() {
		if(!shellDisplayControl.isDisposed()) {
			shellDisplayControl.setEditable(false);
			shellDisplayControl.append(Text.DELIMITER);
			shellDisplayControl.append("--- Shell was terminated; ---");
		}
	}

	/**
	 * Initializes the field assist which provides command completion.
	 */
	private void initFieldAssist() {
		KeyStroke keyStroke = KeyStroke.getInstance(SWT.CTRL, ' ');
		adapter = new ContentProposalAdapter(shellDisplayControl, new TextContentAdapter(), new ShellContentProposalProvider(this), keyStroke,
				new char[] { SWT.TAB });
		adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_IGNORE);
		adapter.addContentProposalListener(new IContentProposalListener() {
			/**
			 * @see org.eclipse.jface.fieldassist.IContentProposalListener#proposalAccepted(org.eclipse.jface.fieldassist.IContentProposal)
			 */
			public void proposalAccepted(IContentProposal proposal) {
				setCurrentCommand(proposal.getContent());
			}
		});
		adapter.setLabelProvider(new LabelProvider() {

			/**
			 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
			 */
			public String getText(Object element) {
				Completion completion = (Completion) element;
				return completion.getLabel();
			}

			/**
			 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
			 */
			public Image getImage(Object element) {
				Completion completion = (Completion) element;
				if(completion.getImagePath() != null && completion.getImagePath().length() > 1) {
					return ResourceManager.getPluginImage(ShellPlugin.getDefault(), completion.getImagePath());
				}
				return ResourceManager.getPluginImage(ShellPlugin.getDefault(), "img/staticCompletion.gif");
			}
		});
	}

	/**
	 * @see net.sf.wickedshell.ui.shell.viewer.IShellViewer#getViewerContent()
	 */
	public String getViewerContent() {
		return shellDisplayControl.getText();
	}

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		if(adapter.equals(ICommandHistoryHandler.class)) {
			return commandHistoryHandler;
		}
		return null;
	}

	/**
	 * Represents a <code>PopupDialog</code> displaying relevant history
	 * entries.
	 * 
	 * @author Stefan Reichert
	 */
	private class CommandHistoryDialog extends PopupDialog {
		/** The offset (y axis) for the dialog. */
		private static final int POPUP_OFFSET_X = -15;

		/** The offset (y axis) for the dialog. */
		private static final int POPUP_OFFSET_Y = 18;

		/** The count of entries displayed. */
		private static final int LIST_COUNT = 7;

		/** The period of visibilty of this dialog. */
		private static final int DIALOG_VISIBILITY_PERIOD = 1000;

		/** The base <code>ICommandHistory</code>. */
		private ICommandHistory commandHistory;

		/** The <code>ListViewer</code> displaying the history. */
		private TableViewer commandHistoryTableViewer;

		/** The Timer which controls the visibility of this dialog. */
		private Timer popupTimer;

		/**
		 * Constructor for <code>CommandHistoryDialog</code>.
		 * 
		 * @param commandHistory
		 *            The <code>ICommandHistory</code> to be displayed
		 */
		@SuppressWarnings("deprecation")
		public CommandHistoryDialog(ICommandHistory commandHistory) {
			super(shellDisplayControl.getShell(), SWT.ON_TOP | SWT.BORDER, false, false, false, false, null, " Command history overview ");
			this.commandHistory = commandHistory;
			this.popupTimer = new Timer();
		}

		/**
		 * @see org.eclipse.jface.dialogs.PopupDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
		 */
		protected Control createDialogArea(Composite parent) {
			commandHistoryTableViewer = new TableViewer(parent, SWT.FLAT);
			GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
			gridData.widthHint = computeWidthHint();
			gridData.heightHint = (LIST_COUNT * 15) - 2;
			gridData.verticalIndent = 2;
			commandHistoryTableViewer.getTable().setLayoutData(gridData);
			commandHistoryTableViewer.setContentProvider(new ArrayContentProvider());
			commandHistoryTableViewer.setLabelProvider(new LabelProvider() {
				public Image getImage(Object element) {
					return ResourceManager.getPluginImage(ShellPlugin.getDefault(), "img/historyCommand.gif");
				}
			});
			parent.setEnabled(false);
			return commandHistoryTableViewer.getTable();
		}

		/**
		 * Computes the width hint for the <code>ListViewer</code> using the
		 * width of the widest command which is present in the
		 * <code>CommandHistory</code>.
		 * 
		 * @return the computed width hint
		 */
		private int computeWidthHint() {
			int maxCommandStringWidth = 200;
			GC gc = new GC(commandHistoryTableViewer.getTable());
			for (int index = 0; index < commandHistory.getSize(shellFacade.getShellDescriptor().getId()); index++) {
				String command = commandHistory.getCommand(index, shellFacade.getShellDescriptor().getId());
				FontMetrics metrics = gc.getFontMetrics();
				int commandStringWidth = metrics.getAverageCharWidth() * command.length();
				maxCommandStringWidth = Math.max((commandStringWidth + 50), maxCommandStringWidth);
			}
			return maxCommandStringWidth;
		}

		/**
		 * @see org.eclipse.jface.dialogs.PopupDialog#createContents(org.eclipse.swt.widgets.Composite)
		 */
		protected Control createContents(Composite parent) {
			Control contents = super.createContents(parent);
			changeDefaultColors(parent);
			return contents;
		}

		/**
		 * Set the colors of the popup. The contents have already been created.
		 */
		private void changeDefaultColors(Control control) {
			applyForegroundColor(getShell().getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND), control);
			applyBackgroundColor(getShell().getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND), control);
		}

		/**
		 * @see org.eclipse.jface.dialogs.PopupDialog#adjustBounds()
		 */
		protected void adjustBounds() {
			// Get our control's location in display coordinates.
			Point location = shellDisplayControl.getDisplay().map(shellDisplayControl, null, shellDisplayControl.getCaretLocation());
			int commandLength = 0;
			if(getCurrentCommand() != null) {
				commandLength = new GC(shellDisplayControl).getFontMetrics().getAverageCharWidth() * getCurrentCommand().length();
			}
			int initialX = location.x - commandLength + POPUP_OFFSET_X;
			int initialY = location.y + POPUP_OFFSET_Y;
			// If we are inserting content, use the cursor position to
			// position the control.
			getShell().setBounds(initialX, initialY, getShell().getSize().x, getShell().getSize().y);
		}

		/**
		 * Computes he content of this dialog. The content count is defined by
		 * <code>LIST_COUNT</code> as maximum.
		 * 
		 * @param commandHistory
		 *            The <code>ICommandHistory</code> to use
		 * @param selectedCommandIndex
		 *            The selected index
		 * @return The computed list of commands
		 */
		private List<String> computeTableContent(ICommandHistory commandHistory, int selectedCommandIndex) {
			List<String> popupListContent = new ArrayList<String>();
			// Start index is included
			int startIndex = 0;
			// End index is included
			int endIndex = commandHistory.getSize(shellFacade.getShellDescriptor().getId()) - 1;
			if(endIndex > LIST_COUNT) {
				startIndex = selectedCommandIndex - ((LIST_COUNT - 1) / 2);
				endIndex = selectedCommandIndex + ((LIST_COUNT - 1) / 2);
				while (startIndex < 0) {
					startIndex++;
					endIndex++;
				}
				while (endIndex > (commandHistory.getSize(shellFacade.getShellDescriptor().getId()) - 1)) {
					startIndex--;
					endIndex--;
				}
			}
			int index = endIndex;
			while (index >= startIndex) {
				String command = commandHistory.getCommand(index, shellFacade.getShellDescriptor().getId());
				if(index == selectedCommandIndex) {
					popupListContent.add(" " + command + " ");
				}
				else {
					popupListContent.add(" " + command);
				}
				index--;
			}
			return popupListContent;
		}

		/**
		 * Updates the content of the <code>List</code> displayed within the
		 * <code>commandHistoryDialog</code>
		 * 
		 * @param newSelectedCommandIndex
		 *            The index of the newly selected command from the history
		 */
		public void updateTableContent(int newSelectedCommandIndex) {
			// Open this popup
			open();
			// Compute the content of the table
			List<String> tableContent = computeTableContent(commandHistory, newSelectedCommandIndex);
			// Set the input
			commandHistoryTableViewer.setInput(tableContent);
			// Select the indexed command
			String command = commandHistory.getCommand(newSelectedCommandIndex, shellFacade.getShellDescriptor().getId());
			commandHistoryTableViewer.getTable().select(tableContent.indexOf(" " + command + " "));

			// Cancel the former timer in case the scheduled task is still
			// running
			popupTimer.cancel();
			// Create a new task
			popupTimer = new Timer();
			popupTimer.schedule(new TimerTask() {
				public void run() {
					Display.getDefault().asyncExec(new Runnable() {
						/**
						 * @see java.lang.Runnable#run()
						 */
						public void run() {
							if(commandHistoryTableViewer != null && !commandHistoryTableViewer.getControl().isDisposed()) {
								close();
								commandHistoryDialog = null;
							}
						}
					});
				}
			}, DIALOG_VISIBILITY_PERIOD);
		}
	}
}
