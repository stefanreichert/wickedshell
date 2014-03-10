package net.sf.wickedshell.domain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.wickedshell.domain.batch.BatchFileDescriptorDao;
import net.sf.wickedshell.domain.batch.IBatchFileDescriptor;
import net.sf.wickedshell.domain.command.CommandDescriptorDao;
import net.sf.wickedshell.domain.command.ICommandDescriptor;
import net.sf.wickedshell.domain.completion.CompletionDao;
import net.sf.wickedshell.domain.completion.ICompletion;
import net.sf.wickedshell.domain.configuration.IShellDescriptorProperties;
import net.sf.wickedshell.domain.configuration.ShellDescriptorPropertiesDao;
import net.sf.wickedshell.domain.style.IColorDescriptor;
import net.sf.wickedshell.domain.style.IFontDescriptor;
import net.sf.wickedshell.domain.style.ShellStyleDao;
import net.sf.wickedshell.domain.xmlShellDescriptor.IXMLShellDescriptor;
import net.sf.wickedshell.domain.xmlShellDescriptor.XMLShellDescriptorDao;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.xmlbeans.XmlException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class DomainPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "net.sf.wickedshell.domain";

	// The shared instance
	private static DomainPlugin plugin;

	// The document containing the batch files
	private List<IBatchFileDescriptor> batchFileDescriptors;

	// The list of static completions
	private List<ICompletion> staticCompletions;

	// The command history
	private List<ICommandDescriptor> commandHistory;

	// The xml shell descriptors
	private List<IXMLShellDescriptor> xmlShellDescriptors;

	// The font of the shell
	private IFontDescriptor fontDescriptor;

	// The foreground color
	private IColorDescriptor foregroundColor;

	// The background color
	private IColorDescriptor backgroundColor;

	// The properties for the shell descriptors
	private List<IShellDescriptorProperties> shellDescriptorProperties;

	/**
	 * The constructor
	 */
	public DomainPlugin() {
		plugin = this;
	}

	/**
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		// Trach errors on startup
		MultiStatus startupErrors = new MultiStatus(DomainPlugin.PLUGIN_ID, IStatus.ERROR, "The stored Wicked Shell Configuration files were invalid.", null);
		// Load stored batchfiles
		try {
			batchFileDescriptors = new BatchFileDescriptorDao().readBatchFileDescriptors();
		}
		catch (Throwable throwable) {
			startupErrors.add(new Status(IStatus.ERROR, DomainPlugin.PLUGIN_ID, IStatus.ERROR,
					"Loading the stored list of 'Batch Files' failed - It was automatically reset.", throwable));
			batchFileDescriptors = new ArrayList<IBatchFileDescriptor>();
			new BatchFileDescriptorDao().writeBatchFileDescriptors(batchFileDescriptors);

		}
		try {
			staticCompletions = new CompletionDao().readCompletions();
		}
		catch (Throwable throwable) {
			startupErrors.add(new Status(IStatus.ERROR, DomainPlugin.PLUGIN_ID, IStatus.ERROR,
					"Loading the stored list of 'Static Completions' failed - It was automatically reset.", throwable));
			resetCompletions();
		}
		try {
			commandHistory = new CommandDescriptorDao().readCommandHistory();
		}
		catch (Throwable throwable) {
			startupErrors.add(new Status(IStatus.ERROR, DomainPlugin.PLUGIN_ID, IStatus.ERROR,
					"Loading the stored 'Command History' failed - It was automatically reset.", throwable));
			commandHistory = new ArrayList<ICommandDescriptor>();
			new CommandDescriptorDao().writeCommandHistory(commandHistory);
		}
		try {
			ShellStyleDao shellStyleDao = new ShellStyleDao();
			fontDescriptor = shellStyleDao.readFontDescriptor();
			backgroundColor = shellStyleDao.readBackgroundColor();
			foregroundColor = shellStyleDao.readForegroundColor();
		}
		catch (Throwable throwable) {
			startupErrors.add(new Status(IStatus.ERROR, DomainPlugin.PLUGIN_ID, IStatus.ERROR,
					"Loading the stored 'Shell Style' failed - It was automatically reset.", throwable));
			resetShellStyle();
		}
		try {
			shellDescriptorProperties = new ShellDescriptorPropertiesDao().readShellDescriptorProperties();
		}
		catch (Throwable throwable) {
			startupErrors.add(new Status(IStatus.ERROR, DomainPlugin.PLUGIN_ID, IStatus.ERROR,
					"Loading the stored list of 'Shell Descriptor Properties' failed - It was automatically reset.", throwable));
			resetShellStyle();
		}
		try {
			xmlShellDescriptors = new XMLShellDescriptorDao().readXMLShellDescriptors();
		}
		catch (Throwable throwable) {
			startupErrors.add(new Status(IStatus.ERROR, DomainPlugin.PLUGIN_ID, IStatus.ERROR,
					"Loading the stored list of 'Custom Shell Descriptors' failed - It was automatically reset.", throwable));
			xmlShellDescriptors = new ArrayList<IXMLShellDescriptor>();
			new XMLShellDescriptorDao().writeXMLShellDescriptors(xmlShellDescriptors);
		}
		if(!startupErrors.isOK()) {
			ErrorDialog errorDialog = new ErrorDialog(getWorkbench().getActiveWorkbenchWindow().getShell(), "Wicked Shell Error",
					"Loading of the Wicked Shell Configuration failed and was reset.", startupErrors, IStatus.ERROR);
			errorDialog.open();
		}
	}

	/**
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		new BatchFileDescriptorDao().writeBatchFileDescriptors(batchFileDescriptors);
		new CompletionDao().writeCompletions(staticCompletions);
		new CommandDescriptorDao().writeCommandHistory(commandHistory);
		new ShellStyleDao().writeShellStyle(fontDescriptor, backgroundColor, foregroundColor);
		new ShellDescriptorPropertiesDao().writeShellDescriptorProperties(shellDescriptorProperties);
		new XMLShellDescriptorDao().writeXMLShellDescriptors(xmlShellDescriptors);
		plugin = null;
		super.stop(context);
	}

	/**
	 * Writes the <code>List</code> of completions to the given
	 * <code>File</code>.
	 * 
	 * @param completions
	 *            The <code>List</code> of completions to store
	 * @param staticCompletionsFile
	 *            The <code>File</code> to write the completions to
	 * @throws IOException
	 *             if writing the <code>File</code> fails
	 */
	public void writeCompletionsToFile(List<ICompletion> completions, File staticCompletionsFile) throws IOException {
		new CompletionDao().writeCompletions(completions, staticCompletionsFile);
	}

	/**
	 * Reads the <code>List</code> of completions from the given
	 * <code>File</code>.
	 * 
	 * @param staticCompletionsFile
	 *            The <code>File</code> to write the completions to
	 * @return The <code>List</code> of completions to store
	 * @throws IOException
	 *             if reading the <code>File</code> fails
	 */
	public List<ICompletion> readCompletionsFromFile(File staticCompletionsFile) throws IOException {
		try {
			return new CompletionDao().readCompletions(staticCompletionsFile);
		}
		catch (XmlException exception) {
			throw new IOException(exception.getMessage());
		}
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static DomainPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path.
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin("net.sf.wickedshell", path);
	}

	public List<ICompletion> getStaticCompletions() {
		return staticCompletions;
	}

	public List<IBatchFileDescriptor> getBatchFileDescriptors() {
		return batchFileDescriptors;
	}

	public List<ICommandDescriptor> getCommandHistory() {
		return commandHistory;
	}

	public List<IXMLShellDescriptor> getXMLShellDescriptors() {
		return xmlShellDescriptors;
	}

	public IColorDescriptor getBackgroundColor() {
		return backgroundColor;
	}

	public IFontDescriptor getFontDescriptor() {
		return fontDescriptor;
	}

	public IColorDescriptor getForegroundColor() {
		return foregroundColor;
	}

	public void setBackgroundColor(IColorDescriptor backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public void setFontDescriptor(IFontDescriptor fontDescriptor) {
		this.fontDescriptor = fontDescriptor;
	}

	public void setForegroundColor(IColorDescriptor foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	public IShellDescriptorProperties getShellDescriptorProperties(final String shellDescriptorId) {
		IShellDescriptorProperties shellDescriptorProperties = (IShellDescriptorProperties) CollectionUtils.find(this.shellDescriptorProperties,
				new Predicate() {
					/**
					 * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
					 */
					public boolean evaluate(Object object) {
						IShellDescriptorProperties shellDescriptorProperties = (IShellDescriptorProperties) object;
						return shellDescriptorProperties.getShellDescriptorId().equals(shellDescriptorId);
					}
				});
		if(shellDescriptorProperties == null) {
			shellDescriptorProperties = IShellDescriptorProperties.Factory.newInstance(shellDescriptorId, new String(), false);
			this.shellDescriptorProperties.add(shellDescriptorProperties);
		}
		return shellDescriptorProperties;
	}

	/**
	 * Resets the <code>StaticCompletionList</code> of static
	 * <code>Completions</code> defined by the guy who wrote this plugin :)
	 */
	public void resetCompletions() {
		try {
			CompletionDao completionDao = new CompletionDao();
			completionDao.writeDefaultCompletions();
			staticCompletions = completionDao.readCompletions();
		}
		catch (IOException exception) {
			exception.printStackTrace();
		}
		catch (XmlException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Resets the shell's style defined by the guy who wrote this plugin :)
	 */
	public void resetShellStyle() {
		try {
			ShellStyleDao shellStyleDao = new ShellStyleDao();
			shellStyleDao.writeDefaultShellStyle();
			fontDescriptor = shellStyleDao.readFontDescriptor();
			backgroundColor = shellStyleDao.readBackgroundColor();
			foregroundColor = shellStyleDao.readForegroundColor();
		}
		catch (IOException exception) {
			exception.printStackTrace();
		}
		catch (XmlException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Resets the <code>StaticCompletionList</code> of static
	 * <code>Completions</code> defined by the guy who wrote this plugin :)
	 */
	public void resetShellDescriptorProperties() {
		try {
			ShellDescriptorPropertiesDao shellDescriptorPropertiesDao = new ShellDescriptorPropertiesDao();
			shellDescriptorPropertiesDao.writeDefaultShellDescriptorProperties();
			shellDescriptorProperties = shellDescriptorPropertiesDao.readShellDescriptorProperties();
		}
		catch (IOException exception) {
			exception.printStackTrace();
		}
		catch (XmlException exception) {
			exception.printStackTrace();
		}
	}
}
