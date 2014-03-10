package net.sf.wickedshell.ui.preferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import net.sf.wickedshell.ShellID;
import net.sf.wickedshell.ShellPlugin;
import net.sf.wickedshell.domain.DomainID;
import net.sf.wickedshell.domain.DomainPlugin;
import net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor;
import net.sf.wickedshell.facade.descriptor.IShellDescriptor;
import net.sf.wickedshell.util.MessageDialogHandler;
import net.sf.wickedshell.util.ResourceManager;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.dialogs.ListSelectionDialog;

public class WickedShellCustomShellsPreferencesPage extends PreferencePage implements IWorkbenchPreferencePage {

	private Button buttonRemoveSupportedExecutableFile;

	private Button buttonAddExecutableFile;

	private Button buttonBrowseCustomRootDirectory;

	private Button buttonDelete;

	private Button buttonRemoveSupportedOperatingSystems;

	private Button buttonAddSupportedOperatingSystems;

	private List listSupportedOperatingSystems;

	private Button buttonHasCustomRoot;

	private Button buttonIsUILineFeedProvided;

	private Button buttonIsExecutedCommand;

	private Combo comboCharacterEncoding;

	private Combo comboLineFeed;

	private Text textSystempathSeparator;

	private Text textCommandDelimiter;

	private Combo comboPathSeparator;

	private Text textCustomRootDirectory;

	private Text textPathDelimiter;

	private Text textBinariesDirectory;

	private Text textExecutable;

	private Text textName;

	private Text textId;

	private ComboViewer comboViewerShellDescriptor;

	private CheckboxTableViewer tableViewerExecutableFiles;

	/**
	 * Create the preference page
	 */
	public WickedShellCustomShellsPreferencesPage() {
		super();
		setTitle("Custom Shell Descriptors");
	}

	/**
	 * Create contents of the preference page
	 * 
	 * @param parent
	 */
	public Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		container.setLayout(gridLayout);

		final Group groupShellDescriptor = new Group(container, SWT.NONE);
		groupShellDescriptor.setText("Custom Shell settings");
		groupShellDescriptor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		final GridLayout gridLayoutGroupShellDescriptor = new GridLayout();
		gridLayoutGroupShellDescriptor.numColumns = 3;
		groupShellDescriptor.setLayout(gridLayoutGroupShellDescriptor);

		final Label shellDescriptorLabel = new Label(groupShellDescriptor, SWT.NONE);
		shellDescriptorLabel.setText("Custom Shell:");

		comboViewerShellDescriptor = new ComboViewer(groupShellDescriptor, SWT.READ_ONLY);
		final Combo comboShellDescriptor = comboViewerShellDescriptor.getCombo();
		final GridData gridDataComboShellDescriptor = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridDataComboShellDescriptor.horizontalIndent = 25;
		comboShellDescriptor.setLayoutData(gridDataComboShellDescriptor);
		comboViewerShellDescriptor.addSelectionChangedListener(new ISelectionChangedListener() {
			/**
			 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
			 */
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection structuredSelection = (IStructuredSelection) event.getSelection();
				setXMLShellDescriptor((IXMLShellDescriptor) structuredSelection.getFirstElement());
			}
		});
		comboViewerShellDescriptor.setContentProvider(new ArrayContentProvider());
		comboViewerShellDescriptor.setLabelProvider(new LabelProvider() {
			/**
			 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
			 */
			public String getText(Object element) {
				IXMLShellDescriptor shellDescriptor = (IXMLShellDescriptor) element;
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append(shellDescriptor.getId());
				return stringBuffer.toString();
			}

			/**
			 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
			 */
			public Image getImage(Object element) {
				return ResourceManager.getPluginImage(ShellPlugin.getDefault(), "img/shellDescriptor.gif");
			}
		});
		comboViewerShellDescriptor.setInput(DomainPlugin.getDefault().getXMLShellDescriptors());

		final Label labelSeparator_one = new Label(groupShellDescriptor, SWT.SEPARATOR | SWT.HORIZONTAL);
		labelSeparator_one.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));

		final Composite compositeShellDescriptorDetails = new Composite(groupShellDescriptor, SWT.NONE);
		compositeShellDescriptorDetails.setCapture(true);
		final GridData gridDataCompositeShellDescriptorDetails = new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1);
		gridDataCompositeShellDescriptorDetails.verticalIndent = 2;
		compositeShellDescriptorDetails.setLayoutData(gridDataCompositeShellDescriptorDetails);
		final GridLayout gridLayoutCompositeShellDescriptorDetails = new GridLayout();
		gridLayoutCompositeShellDescriptorDetails.numColumns = 4;
		gridLayoutCompositeShellDescriptorDetails.marginWidth = 0;
		gridLayoutCompositeShellDescriptorDetails.marginHeight = 0;
		compositeShellDescriptorDetails.setLayout(gridLayoutCompositeShellDescriptorDetails);

		final Label labelId = new Label(compositeShellDescriptorDetails, SWT.NONE);
		final GridData gridDataLabelId = new GridData();
		gridDataLabelId.horizontalIndent = 5;
		labelId.setLayoutData(gridDataLabelId);
		labelId.setText("ID:");

		textId = new Text(compositeShellDescriptorDetails, SWT.BORDER);
		textId.setEnabled(false);
		final GridData gridDataTextId = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gridDataTextId.horizontalIndent = 5;
		textId.setLayoutData(gridDataTextId);

		final Label labelName = new Label(compositeShellDescriptorDetails, SWT.NONE);
		final GridData gridDataLabelName = new GridData();
		gridDataLabelName.horizontalIndent = 5;
		labelName.setLayoutData(gridDataLabelName);
		labelName.setText("Name:");

		textName = new Text(compositeShellDescriptorDetails, SWT.BORDER);
		textName.setEnabled(false);
		final GridData gridDataTextName = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gridDataTextName.horizontalIndent = 5;
		textName.setLayoutData(gridDataTextName);

		final Label labelExecutable = new Label(compositeShellDescriptorDetails, SWT.NONE);
		final GridData gridDataLabelExecutable = new GridData(SWT.LEFT, SWT.TOP, false, false);
		gridDataLabelExecutable.horizontalIndent = 5;
		labelExecutable.setLayoutData(gridDataLabelExecutable);
		labelExecutable.setText("Executable:");

		textExecutable = new Text(compositeShellDescriptorDetails, SWT.BORDER);
		textExecutable.setEnabled(false);
		final GridData gridDataTextExecutable = new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1);
		gridDataTextExecutable.horizontalIndent = 5;
		textExecutable.setLayoutData(gridDataTextExecutable);

		final Label labelSeparator_two = new Label(compositeShellDescriptorDetails, SWT.SEPARATOR | SWT.HORIZONTAL);
		labelSeparator_two.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));

		buttonHasCustomRoot = new Button(compositeShellDescriptorDetails, SWT.CHECK);
		buttonHasCustomRoot.setEnabled(false);
		final GridData gridDataButtonHasCustomRoot = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gridDataButtonHasCustomRoot.verticalIndent = 5;
		gridDataButtonHasCustomRoot.horizontalIndent = 5;
		buttonHasCustomRoot.setLayoutData(gridDataButtonHasCustomRoot);
		buttonHasCustomRoot.setText("Has custom root directory");
		buttonHasCustomRoot.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent event) {
				textCustomRootDirectory.setEnabled(buttonHasCustomRoot.getSelection());
				textBinariesDirectory.setEnabled(buttonHasCustomRoot.getSelection());
				buttonBrowseCustomRootDirectory.setEnabled(buttonHasCustomRoot.getSelection());
			}
		});

		final Label labelCustomRootDirectory = new Label(compositeShellDescriptorDetails, SWT.NONE);
		final GridData gridDataLabelCustomRootDirectory = new GridData();
		gridDataLabelCustomRootDirectory.horizontalIndent = 5;
		labelCustomRootDirectory.setLayoutData(gridDataLabelCustomRootDirectory);
		labelCustomRootDirectory.setText("Custom root directory:");

		textCustomRootDirectory = new Text(compositeShellDescriptorDetails, SWT.BORDER);
		textCustomRootDirectory.setEnabled(false);
		final GridData gridDataTxtCustomRootDirectory = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gridDataTxtCustomRootDirectory.horizontalIndent = 5;
		textCustomRootDirectory.setLayoutData(gridDataTxtCustomRootDirectory);

		final Label labelBinariesDirectory = new Label(compositeShellDescriptorDetails, SWT.NONE);
		final GridData gridDataLabelBinariesDirectory = new GridData();
		gridDataLabelBinariesDirectory.horizontalIndent = 5;
		labelBinariesDirectory.setLayoutData(gridDataLabelBinariesDirectory);
		labelBinariesDirectory.setText("Binaries directory:");

		textBinariesDirectory = new Text(compositeShellDescriptorDetails, SWT.BORDER);
		textBinariesDirectory.setEnabled(false);
		final GridData gridDataTextBinariesDirectory = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gridDataTextBinariesDirectory.horizontalIndent = 5;
		textBinariesDirectory.setLayoutData(gridDataTextBinariesDirectory);

		buttonBrowseCustomRootDirectory = new Button(compositeShellDescriptorDetails, SWT.NONE);
		buttonBrowseCustomRootDirectory.setEnabled(false);
		final GridData gridDataButtonBrowseCustomRootDirectory = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gridDataButtonBrowseCustomRootDirectory.horizontalIndent = 5;
		buttonBrowseCustomRootDirectory.setLayoutData(gridDataButtonBrowseCustomRootDirectory);
		buttonBrowseCustomRootDirectory.setText("Browse Custom Root...");
		buttonBrowseCustomRootDirectory.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent event) {
				DirectoryDialog shellRootDirectorySelectionDialog = new DirectoryDialog(getShell());
				String shellRootDirectory = shellRootDirectorySelectionDialog.open();
				if(shellRootDirectory != null) {
					textCustomRootDirectory.setText(shellRootDirectory);
				}
			}
		});

		final Label labelSeparator_three = new Label(compositeShellDescriptorDetails, SWT.SEPARATOR | SWT.HORIZONTAL);
		labelSeparator_three.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));

		buttonIsExecutedCommand = new Button(compositeShellDescriptorDetails, SWT.CHECK);
		buttonIsExecutedCommand.setEnabled(false);
		final GridData gridDataButtonIsExecutedCommand = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gridDataButtonIsExecutedCommand.verticalIndent = 5;
		gridDataButtonIsExecutedCommand.horizontalIndent = 5;
		buttonIsExecutedCommand.setLayoutData(gridDataButtonIsExecutedCommand);
		buttonIsExecutedCommand.setText("Executed command provided");

		buttonIsUILineFeedProvided = new Button(compositeShellDescriptorDetails, SWT.CHECK);
		buttonIsUILineFeedProvided.setEnabled(false);
		final GridData gridDataButtonIsUILineFeedProvided = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gridDataButtonIsUILineFeedProvided.verticalIndent = 5;
		gridDataButtonIsUILineFeedProvided.horizontalIndent = 5;
		buttonIsUILineFeedProvided.setLayoutData(gridDataButtonIsUILineFeedProvided);
		buttonIsUILineFeedProvided.setText("UI line feed provided");

		final Label labelPathDelimiter = new Label(compositeShellDescriptorDetails, SWT.NONE);
		final GridData gridDataLabelPathDelimiter = new GridData();
		gridDataLabelPathDelimiter.horizontalIndent = 5;
		labelPathDelimiter.setLayoutData(gridDataLabelPathDelimiter);
		labelPathDelimiter.setText("Path Delimiter:");

		textPathDelimiter = new Text(compositeShellDescriptorDetails, SWT.BORDER);
		textPathDelimiter.setEnabled(false);
		final GridData gridDataTextPathDelimiter = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridDataTextPathDelimiter.horizontalIndent = 5;
		textPathDelimiter.setLayoutData(gridDataTextPathDelimiter);

		final Label labelCommandDelimiter = new Label(compositeShellDescriptorDetails, SWT.NONE);
		final GridData gridDataLabelCommandDelimiter = new GridData();
		gridDataLabelCommandDelimiter.horizontalIndent = 5;
		labelCommandDelimiter.setLayoutData(gridDataLabelCommandDelimiter);
		labelCommandDelimiter.setText("Command Delimiter:");

		textCommandDelimiter = new Text(compositeShellDescriptorDetails, SWT.BORDER);
		textCommandDelimiter.setEnabled(false);
		final GridData gridDataTextCommandDelimiter = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridDataTextCommandDelimiter.horizontalIndent = 5;
		textCommandDelimiter.setLayoutData(gridDataTextCommandDelimiter);

		final Label labelSystempathSeparator = new Label(compositeShellDescriptorDetails, SWT.NONE);
		final GridData gridDataLabelSystempathSeparator = new GridData();
		gridDataLabelSystempathSeparator.horizontalIndent = 5;
		labelSystempathSeparator.setLayoutData(gridDataLabelSystempathSeparator);
		labelSystempathSeparator.setText("Systempath Separator:");

		textSystempathSeparator = new Text(compositeShellDescriptorDetails, SWT.BORDER);
		textSystempathSeparator.setTextLimit(1);
		textSystempathSeparator.setEnabled(false);
		final GridData gridDataTextSystempathSeparator = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridDataTextSystempathSeparator.horizontalIndent = 5;
		textSystempathSeparator.setLayoutData(gridDataTextSystempathSeparator);

		final Label labelPathSeparator = new Label(compositeShellDescriptorDetails, SWT.NONE);
		final GridData gridDataLabelPathSeparator = new GridData();
		gridDataLabelPathSeparator.horizontalIndent = 5;
		labelPathSeparator.setLayoutData(gridDataLabelPathSeparator);
		labelPathSeparator.setText("Path Separator:");

		comboPathSeparator = new Combo(compositeShellDescriptorDetails, SWT.READ_ONLY);
		comboPathSeparator.setEnabled(false);
		final GridData gridDataComboPathSeparator = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridDataComboPathSeparator.horizontalIndent = 5;
		comboPathSeparator.setLayoutData(gridDataComboPathSeparator);
		comboPathSeparator.setItems(new String[] { DomainID.SLASH_VALUE, DomainID.BACKSLASH_VALUE });

		final Label labelLineFeed = new Label(compositeShellDescriptorDetails, SWT.NONE);
		final GridData gridDataLabelLineFeed = new GridData();
		gridDataLabelLineFeed.horizontalIndent = 5;
		labelLineFeed.setLayoutData(gridDataLabelLineFeed);
		labelLineFeed.setText("Line Feed:");

		comboLineFeed = new Combo(compositeShellDescriptorDetails, SWT.READ_ONLY);
		comboLineFeed.setEnabled(false);
		final GridData gridDataComboLineFeed = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridDataComboLineFeed.horizontalIndent = 5;
		comboLineFeed.setLayoutData(gridDataComboLineFeed);
		comboLineFeed.setItems(new String[] { DomainID.CR_LF_VALUE, DomainID.LF_ONLY_VALUE });

		final Label labelCharacterEncoding = new Label(compositeShellDescriptorDetails, SWT.NONE);
		final GridData gridDataLabelCharacterEncoding = new GridData();
		gridDataLabelCharacterEncoding.horizontalIndent = 5;
		labelCharacterEncoding.setLayoutData(gridDataLabelCharacterEncoding);
		labelCharacterEncoding.setText("Character Encoding:");

		comboCharacterEncoding = new Combo(compositeShellDescriptorDetails, SWT.READ_ONLY);
		comboCharacterEncoding.setEnabled(false);
		final GridData gridDataComboCharacterEncoding = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridDataComboCharacterEncoding.horizontalIndent = 5;
		comboCharacterEncoding.setLayoutData(gridDataComboCharacterEncoding);
		comboCharacterEncoding.setItems(new String[] { DomainID.DEFAULT_OS_CHARACTER_ENCODING, DomainID.ENCODING_US_ASCII, DomainID.ENCODING_ISO_8859_1,
				DomainID.ENCODING_UTF_8, DomainID.ENCODING_UTF_16_LE, DomainID.ENCODING_UTF_16_BE, DomainID.ENCODING_CP_1252, DomainID.ENCODING_CP_437 });

		final Label labelSeparator_four = new Label(compositeShellDescriptorDetails, SWT.SEPARATOR | SWT.HORIZONTAL);
		labelSeparator_four.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));

		final Label labelSupportedOperatingSystems = new Label(compositeShellDescriptorDetails, SWT.NONE);
		final GridData gridDataLabelSupportedOperatingSystems = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 2);
		gridDataLabelSupportedOperatingSystems.verticalIndent = 5;
		gridDataLabelSupportedOperatingSystems.horizontalIndent = 5;
		labelSupportedOperatingSystems.setLayoutData(gridDataLabelSupportedOperatingSystems);
		labelSupportedOperatingSystems.setText("Supported OSs:");

		listSupportedOperatingSystems = new List(compositeShellDescriptorDetails, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER);
		listSupportedOperatingSystems.setBackground(ResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		final GridData gridDataListSupportedOperatingSystems = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 2);
		gridDataListSupportedOperatingSystems.verticalIndent = 5;
		gridDataListSupportedOperatingSystems.heightHint = 53;
		gridDataListSupportedOperatingSystems.horizontalIndent = 5;
		listSupportedOperatingSystems.setLayoutData(gridDataListSupportedOperatingSystems);

		buttonAddSupportedOperatingSystems = new Button(compositeShellDescriptorDetails, SWT.NONE);
		buttonAddSupportedOperatingSystems.setEnabled(false);
		final GridData gridDataButtonAddSupportedOperatingSystems = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gridDataButtonAddSupportedOperatingSystems.verticalIndent = 5;
		gridDataButtonAddSupportedOperatingSystems.horizontalIndent = 5;
		buttonAddSupportedOperatingSystems.setLayoutData(gridDataButtonAddSupportedOperatingSystems);
		buttonAddSupportedOperatingSystems.setText("Add supported OSs..");
		buttonAddSupportedOperatingSystems.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@SuppressWarnings("unchecked")
			public void widgetSelected(SelectionEvent event) {
				java.util.List remainingSupportedOperatingSystems = new ArrayList();
				remainingSupportedOperatingSystems.addAll(Arrays.asList(ShellID.SUPPORTED_OS));
				remainingSupportedOperatingSystems.removeAll(Arrays.asList(listSupportedOperatingSystems.getItems()));
				ListSelectionDialog listSelectionDialog = new ListSelectionDialog(getShell(), remainingSupportedOperatingSystems, new ArrayContentProvider(),
						new LabelProvider(), "Please select the supported operation systems to add.");
				listSelectionDialog.setTitle("Add supported Operating Systems");
				if(listSelectionDialog.open() == Dialog.OK) {
					Set supportedOperatingSystems = new TreeSet();
					supportedOperatingSystems.addAll(Arrays.asList(listSupportedOperatingSystems.getItems()));
					supportedOperatingSystems.addAll(Arrays.asList(listSelectionDialog.getResult()));
					listSupportedOperatingSystems.setItems((String[]) supportedOperatingSystems.toArray(new String[0]));
				}
			}
		});

		buttonRemoveSupportedOperatingSystems = new Button(compositeShellDescriptorDetails, SWT.NONE);
		buttonRemoveSupportedOperatingSystems.setEnabled(false);
		final GridData gridDataButtonRemoveSupportedOperatingSystems = new GridData(SWT.FILL, SWT.TOP, false, false);
		gridDataButtonRemoveSupportedOperatingSystems.horizontalIndent = 5;
		buttonRemoveSupportedOperatingSystems.setLayoutData(gridDataButtonRemoveSupportedOperatingSystems);
		buttonRemoveSupportedOperatingSystems.setText("Remove selected OSs");
		buttonRemoveSupportedOperatingSystems.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent event) {
				listSupportedOperatingSystems.remove(listSupportedOperatingSystems.getSelectionIndices());
			}
		});

		final Label labelSeparator_five = new Label(compositeShellDescriptorDetails, SWT.SEPARATOR | SWT.HORIZONTAL);
		labelSeparator_five.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));

		final Label labelExecutableFiles = new Label(compositeShellDescriptorDetails, SWT.NONE);
		final GridData gridDataLabelExecutableFiles = new GridData(SWT.LEFT, SWT.TOP, false, false);
		gridDataLabelExecutableFiles.heightHint = 15;
		gridDataLabelExecutableFiles.verticalIndent = 5;
		gridDataLabelExecutableFiles.horizontalIndent = 5;
		labelExecutableFiles.setLayoutData(gridDataLabelExecutableFiles);
		labelExecutableFiles.setText("Executable Files:");

		tableViewerExecutableFiles = CheckboxTableViewer.newCheckList(compositeShellDescriptorDetails, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER);
		tableViewerExecutableFiles.getTable().setBackground(ResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tableViewerExecutableFiles.setContentProvider(new ArrayContentProvider());
		tableViewerExecutableFiles.setLabelProvider(new LabelProvider());
		final Table tableExecutableFiles = tableViewerExecutableFiles.getTable();
		final GridData gridDataListExecutableFiles = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 3);
		gridDataListExecutableFiles.verticalIndent = 5;
		gridDataListExecutableFiles.horizontalIndent = 5;
		gridDataListExecutableFiles.heightHint = 53;
		tableExecutableFiles.setLayoutData(gridDataListExecutableFiles);
		tableExecutableFiles.setEnabled(true);

		buttonAddExecutableFile = new Button(compositeShellDescriptorDetails, SWT.NONE);
		final GridData gridDataButtonAddSupportedOperatingSystems_1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 2);
		gridDataButtonAddSupportedOperatingSystems_1.verticalIndent = 5;
		gridDataButtonAddSupportedOperatingSystems_1.horizontalIndent = 5;
		buttonAddExecutableFile.setLayoutData(gridDataButtonAddSupportedOperatingSystems_1);
		buttonAddExecutableFile.setEnabled(false);
		buttonAddExecutableFile.setText("Add Executable File..");
		buttonAddExecutableFile.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@SuppressWarnings("unchecked")
			public void widgetSelected(SelectionEvent event) {
				InputDialog inputDialog = new InputDialog(getShell(), "Add new Executable File",
						"Please enter the extension of the new type of Executable File (e.g. .exe)", null, null);
				if(inputDialog.open() == Dialog.OK) {
					java.util.List executableFiles = (java.util.List) tableViewerExecutableFiles.getInput();
					executableFiles.add(inputDialog.getValue());
					tableViewerExecutableFiles.setInput(executableFiles);
				}
			}
		});

		final Label label = new Label(compositeShellDescriptorDetails, SWT.NONE);
		final GridData gridData = new GridData(SWT.LEFT, SWT.TOP, false, false);
		gridData.horizontalIndent = 5;
		label.setFont(ResourceManager.getSizedFont(label.getFont(), 7));
		label.setLayoutData(gridData);
		label.setText("(Please check batch files)");
		new Label(compositeShellDescriptorDetails, SWT.NONE);

		buttonRemoveSupportedExecutableFile = new Button(compositeShellDescriptorDetails, SWT.NONE);
		final GridData gridDataButtonRemoveSupportedOperatingSystems_1 = new GridData(SWT.FILL, SWT.TOP, false, false);
		gridDataButtonRemoveSupportedOperatingSystems_1.horizontalIndent = 5;
		buttonRemoveSupportedExecutableFile.setLayoutData(gridDataButtonRemoveSupportedOperatingSystems_1);
		buttonRemoveSupportedExecutableFile.setEnabled(false);
		buttonRemoveSupportedExecutableFile.setText("Remove selected Files");

		// Set the initial selection
		if(!DomainPlugin.getDefault().getXMLShellDescriptors().isEmpty()) {
			comboViewerShellDescriptor.setSelection(new StructuredSelection(DomainPlugin.getDefault().getXMLShellDescriptors().get(0)));
		}
		return container;
	}

	/**
	 * Initialize the preference page
	 */
	public void init(IWorkbench workbench) {
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void setXMLShellDescriptor(IXMLShellDescriptor xmlShellDescriptor) {
		controlXMLShellDescriptorWidgets(xmlShellDescriptor != null);
		if(xmlShellDescriptor != null) {
			textId.setText(getValidString(xmlShellDescriptor.getId()));
			textName.setText(getValidString(xmlShellDescriptor.getName()));
			textExecutable.setText(getValidString(xmlShellDescriptor.getExecutable()));
			buttonIsExecutedCommand.setSelection(xmlShellDescriptor.isExecutedCommandProvided());
			buttonIsUILineFeedProvided.setSelection(xmlShellDescriptor.isUiLineFeedProvided());
			textPathDelimiter.setText(getValidString(xmlShellDescriptor.getPathDelimiter()));
			textCommandDelimiter.setText(getValidString(xmlShellDescriptor.getCommandDelimiter()));
			textSystempathSeparator.setText(String.valueOf(xmlShellDescriptor.getSystemPathSeparator()));
			comboPathSeparator.select(comboPathSeparator.indexOf(getValidString(xmlShellDescriptor.getPathSeparator())));
			comboLineFeed.select(comboLineFeed.indexOf(getValidString(xmlShellDescriptor.getLineFeedString())));
			comboCharacterEncoding.select(comboCharacterEncoding.indexOf(getValidString(xmlShellDescriptor.getCharacterEncoding())));
			buttonHasCustomRoot.setSelection(!getValidString(xmlShellDescriptor.getCustomRootDirectory()).equals(new String()));
			textCustomRootDirectory.setText(getValidString(xmlShellDescriptor.getCustomRootDirectory()));
			textBinariesDirectory.setText(getValidString(xmlShellDescriptor.getBinariesDirectory()));
			listSupportedOperatingSystems.setItems(getValidStringArray(xmlShellDescriptor.getOperatingSystems()));
			java.util.List executableFiles = new ArrayList();
			executableFiles.addAll(Arrays.asList(getValidStringArray(xmlShellDescriptor.getExecutableFiles())));
			executableFiles.addAll(Arrays.asList(getValidStringArray(xmlShellDescriptor.getExecutableBatchFiles())));
			tableViewerExecutableFiles.setInput(executableFiles);
			tableViewerExecutableFiles.setCheckedElements(getValidStringArray(xmlShellDescriptor.getExecutableBatchFiles()));
		}
		else {
			textId.setText(new String());
			textName.setText(new String());
			textExecutable.setText(new String());
			buttonIsExecutedCommand.setSelection(false);
			buttonIsUILineFeedProvided.setSelection(false);
			textPathDelimiter.setText(new String());
			textCommandDelimiter.setText(new String());
			textSystempathSeparator.setText(new String());
			comboPathSeparator.deselectAll();
			comboLineFeed.deselectAll();
			comboCharacterEncoding.deselectAll();
			buttonHasCustomRoot.setSelection(false);
			textCustomRootDirectory.setText(new String());
			textBinariesDirectory.setText(new String());
			listSupportedOperatingSystems.setItems(new String[0]);
			tableViewerExecutableFiles.setInput(new ArrayList());
		}
	}

	/**
	 * @see org.eclipse.jface.preference.PreferencePage#contributeButtons(org.eclipse.swt.widgets.Composite)
	 */
	protected void contributeButtons(Composite parent) {
		GridLayout gridLayout = (GridLayout) parent.getLayout();
		gridLayout.numColumns = 4;
		final Button buttonCreateNew = new Button(parent, SWT.NONE);
		final GridData gridDataButtonCreateNew = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gridDataButtonCreateNew.widthHint = 80;
		buttonCreateNew.setLayoutData(gridDataButtonCreateNew);
		buttonCreateNew.setText("Create New");
		buttonCreateNew.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent event) {
				InputDialog inputDialog = new InputDialog(getShell(), "Create new Shell Descriptor", "Please enter a unique id for the Shell", null,
						new IInputValidator() {
							/**
							 * @see org.eclipse.jface.dialogs.IInputValidator#isValid(java.lang.String)
							 */
							public String isValid(final String newText) {
								boolean valid = newText != null && !newText.equals("")
										&& !CollectionUtils.exists(IShellDescriptor.Manager.getKnownShellDescriptors(), new Predicate() {
											/**
											 * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
											 */
											public boolean evaluate(Object object) {
												IShellDescriptor shellDescriptor = (IShellDescriptor) object;
												return shellDescriptor.getId().equals(newText);
											}
										});
								if(!valid) {
									return "The ID for the Shell must be not null and unique!";
								}
								return null;
							}
						});
				if(inputDialog.open() == Dialog.OK) {
					IXMLShellDescriptor xmlShellDescriptor = IXMLShellDescriptor.Factory.newInstance(inputDialog.getValue());
					DomainPlugin.getDefault().getXMLShellDescriptors().add(xmlShellDescriptor);
					comboViewerShellDescriptor.setInput(DomainPlugin.getDefault().getXMLShellDescriptors());
					comboViewerShellDescriptor.setSelection(new StructuredSelection(xmlShellDescriptor));
				}
			}
		});
		buttonDelete = new Button(parent, SWT.NONE);
		final GridData gridDataButtonCreateDelete = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gridDataButtonCreateDelete.widthHint = 80;
		buttonDelete.setLayoutData(gridDataButtonCreateDelete);
		buttonDelete.setText("Delete");
		buttonDelete.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent event) {
				IStructuredSelection structuredSelection = (IStructuredSelection) comboViewerShellDescriptor.getSelection();
				if(!structuredSelection.isEmpty()) {
					if(MessageDialogHandler.openConfirmationMessage(getShell(), "Do you really want to delete this shell?")) {
						DomainPlugin.getDefault().getXMLShellDescriptors().remove(structuredSelection.getFirstElement());
						comboViewerShellDescriptor.setInput(DomainPlugin.getDefault().getXMLShellDescriptors());
						ISelection newSelection = StructuredSelection.EMPTY;
						if(!DomainPlugin.getDefault().getXMLShellDescriptors().isEmpty()) {
							newSelection = new StructuredSelection(DomainPlugin.getDefault().getXMLShellDescriptors().get(0));
						}
						comboViewerShellDescriptor.setSelection(newSelection);
					}
				}
			}
		});

	}

	/**
	 * Controls the relevant widgets to be either <i>enabled</i> or <i>disabled</i>
	 * depending on <code>enabled</code>.
	 * 
	 * @param enabled
	 *            The flag whether the widgets are <i>enabled</i> or
	 *            <i>disabled</i>
	 */
	private void controlXMLShellDescriptorWidgets(boolean enabled) {
		textName.setEnabled(enabled);
		textExecutable.setEnabled(enabled);
		buttonIsExecutedCommand.setEnabled(enabled);
		buttonIsUILineFeedProvided.setEnabled(enabled);
		textPathDelimiter.setEnabled(enabled);
		textCommandDelimiter.setEnabled(enabled);
		textSystempathSeparator.setEnabled(enabled);
		comboPathSeparator.setEnabled(enabled);
		comboLineFeed.setEnabled(enabled);
		comboCharacterEncoding.setEnabled(enabled);
		buttonHasCustomRoot.setEnabled(enabled);
		listSupportedOperatingSystems.setEnabled(enabled);
		if(enabled) {
			listSupportedOperatingSystems.setBackground(ResourceManager.getColor(SWT.COLOR_WHITE));
			tableViewerExecutableFiles.getTable().setBackground(ResourceManager.getColor(SWT.COLOR_WHITE));
		}
		else {
			listSupportedOperatingSystems.setBackground(ResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
			tableViewerExecutableFiles.getTable().setBackground(ResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		}
		tableViewerExecutableFiles.getTable().setEnabled(enabled);
		buttonAddExecutableFile.setEnabled(enabled);
		buttonRemoveSupportedExecutableFile.setEnabled(enabled);
		buttonAddSupportedOperatingSystems.setEnabled(enabled);
		buttonRemoveSupportedOperatingSystems.setEnabled(enabled);
	}

	/**
	 * Return the <code>originalString</code> if it is not <code>null</code>,
	 * otherwise an empty <code>String</code> is returned.
	 * 
	 * @param originalString
	 *            The <code>String</code> to check
	 * @return the <code>originalString</code> if it is not <code>null</code>
	 *         or an empty <code>String</code>
	 */
	private String getValidString(String originalString) {
		if(originalString == null) {
			return new String();
		}
		return originalString;
	}

	/**
	 * Return the <code>originalStringArray</code> if it is not
	 * <code>null</code>, otherwise an empty <code>String[]</code> is
	 * returned.
	 * 
	 * @param originalStringArray
	 *            The <code>String[]</code> to check
	 * @return the <code>originalStringArray</code> if it is not
	 *         <code>null</code> or an empty <code>String[]</code>
	 */
	private String[] getValidStringArray(String[] originalStringArray) {
		if(originalStringArray == null) {
			return new String[0];
		}
		return originalStringArray;
	}

	/**
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	@SuppressWarnings("unchecked")
	public boolean performOk() {
		IStructuredSelection structuredSelection = (IStructuredSelection) comboViewerShellDescriptor.getSelection();
		if(!structuredSelection.isEmpty()) {
			IXMLShellDescriptor xmlShellDescriptor = (IXMLShellDescriptor) structuredSelection.getFirstElement();
			xmlShellDescriptor.setName(textName.getText());
			xmlShellDescriptor.setExecutable(textExecutable.getText());
			xmlShellDescriptor.setExecutedCommandProvided(buttonIsExecutedCommand.getSelection());
			xmlShellDescriptor.setUiLineFeedProvided(buttonIsUILineFeedProvided.getSelection());
			xmlShellDescriptor.setPathDelimiter(textPathDelimiter.getText());
			xmlShellDescriptor.setCommandDelimiter(textCommandDelimiter.getText());
			if(textSystempathSeparator.getText().equals("")) {
				xmlShellDescriptor.setSystemPathSeparator(' ');
			}
			else {
				xmlShellDescriptor.setSystemPathSeparator(textSystempathSeparator.getText().charAt(0));
			}
			xmlShellDescriptor.setPathSeparator(comboPathSeparator.getItem(comboPathSeparator.getSelectionIndex()));
			xmlShellDescriptor.setLineFeedString(comboLineFeed.getItem(comboLineFeed.getSelectionIndex()));
			xmlShellDescriptor.setCharacterEncoding(comboCharacterEncoding.getItem(comboCharacterEncoding.getSelectionIndex()));
			xmlShellDescriptor.setOperatingSystems(listSupportedOperatingSystems.getItems());
			if(buttonHasCustomRoot.getSelection()) {
				xmlShellDescriptor.setCustomRootDirectory(textCustomRootDirectory.getText());
				xmlShellDescriptor.setBinariesDirectory(textBinariesDirectory.getText());
			}
			else {
				xmlShellDescriptor.setCustomRootDirectory(new String());
				xmlShellDescriptor.setBinariesDirectory(new String());
			}
			java.util.List executableBatchFiles = Arrays.asList(tableViewerExecutableFiles.getCheckedElements());
			xmlShellDescriptor.setExecutableBatchFiles((String[]) executableBatchFiles.toArray(new String[0]));
			java.util.List allExecutableFiles = (java.util.List) tableViewerExecutableFiles.getInput();
			xmlShellDescriptor.setExecutableFiles((String[]) CollectionUtils.subtract(allExecutableFiles, executableBatchFiles).toArray(new String[0]));
			proceedValidation(xmlShellDescriptor);
		}
		return true;
	}

	/**
	 * Proceeds a validation of the passed <code>IXMLShellDescriptor</code>.
	 * 
	 * @param xmlShellDescriptor
	 *            The <code>IXMLShellDescriptor</code> to validate
	 */
	private void proceedValidation(IXMLShellDescriptor xmlShellDescriptor) {
		MultiStatus multiStatus = new MultiStatus(ShellPlugin.ID, IStatus.OK,
				"Some errors within the parameter definition of the descriptor were detected. Please refer to the list below.", null);
		if(xmlShellDescriptor.getName().length() == 0) {
			multiStatus.add(new Status(IStatus.WARNING, ShellPlugin.ID, IStatus.OK, "The name of the shell must not be empty", null));
		}
		if(xmlShellDescriptor.getExecutable().length() == 0) {
			multiStatus.add(new Status(IStatus.WARNING, ShellPlugin.ID, IStatus.OK, "The executable of the shell must not be empty", null));
		}
		if(xmlShellDescriptor.getPathDelimiter().length() == 0) {
			multiStatus.add(new Status(IStatus.WARNING, ShellPlugin.ID, IStatus.OK, "The path delimiter of the shell must not be empty", null));
		}
		if(xmlShellDescriptor.getCommandDelimiter().length() == 0) {
			multiStatus.add(new Status(IStatus.WARNING, ShellPlugin.ID, IStatus.OK, "The command delimiter of the shell must not be empty", null));
		}
		if(xmlShellDescriptor.getSystemPathSeparator() == ' ') {
			multiStatus.add(new Status(IStatus.WARNING, ShellPlugin.ID, IStatus.OK, "The system path separator of the shell must not be empty", null));
		}
		if(buttonHasCustomRoot.getSelection() && xmlShellDescriptor.getCustomRootDirectory().length() == 0) {
			multiStatus.add(new Status(IStatus.WARNING, ShellPlugin.ID, IStatus.OK, "The custom root of the shell must not be empty", null));
		}
		if(multiStatus.getChildren().length > 0) {
			ErrorDialog errorDialog = new ErrorDialog(getShell(), "Wicked Shell Error",
					"The Shell defined by the currently selected descriptor will cause Wicked Shell to fail on ivocation of the Shell."
							+ "The descriptor was saved but it is advisable to correct the descriptor " + "in order to make the Shell working.", multiStatus,
					IStatus.WARNING);
			errorDialog.open();
		}
	}

	/**
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults() {
		if(!DomainPlugin.getDefault().getXMLShellDescriptors().isEmpty()) {
			comboViewerShellDescriptor.setSelection(comboViewerShellDescriptor.getSelection());
		}
		super.performDefaults();
	}
}
