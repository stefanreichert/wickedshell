package net.sf.wickedshell;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.sf.wickedshell.ui.shell.ShellErrorLog;
import net.sf.wickedshell.ui.shell.ShellView;
import net.sf.wickedshell.util.ResourceManager;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class ShellPlugin extends AbstractUIPlugin {

	public static final String ID = "net.sf.wickedshell";

	/** A <code>List</code> of active <code>ShellView</code>. */
	private List<ShellView> shellViewRegistry;

	// The shared instance.
	private static ShellPlugin plugin;

	// Resource bundle.
	private ResourceBundle resourceBundle;

	// The ShellErrorLog
	private ShellErrorLog errorLog;

	/**
	 * The constructor.
	 */
	public ShellPlugin() {
		super();
		plugin = this;
		errorLog = new ShellErrorLog();
		shellViewRegistry = new ArrayList<ShellView>();
	}

	/**
	 * Returns the <code>ErrorLog</code> to be used to log errors.
	 */
	public ShellErrorLog getErrorLog() {
		return errorLog;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
		resourceBundle = null;
		ResourceManager.dispose();
		shellViewRegistry.clear();
	}

	/**
	 * Returns the shared instance.
	 */
	public static ShellPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = ShellPlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		}
		catch (MissingResourceException exception) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		try {
			if(resourceBundle == null)
				resourceBundle = ResourceBundle.getBundle("net.sf.wickedshell.ShellPluginResources");
		}
		catch (MissingResourceException x) {
			resourceBundle = null;
		}
		return resourceBundle;
	}

	/**
	 * Registers a new <code>ShellView</code>.
	 * 
	 * @param shellView
	 *            The <code>ShellView</code> to register
	 */
	public void registerShellView(ShellView shellView) {
		shellViewRegistry.add(shellView);
	}

	/**
	 * Unregisters a new <code>ShellView</code>.
	 * 
	 * @param shellView
	 *            The <code>ShellView</code> to unregister
	 */
	public boolean unregisterShellView(ShellView shellView) {
		return shellViewRegistry.remove(shellView);
	}

	/**
	 * Returns an array of the <code>ShellView</code>s registered.
	 * 
	 * @return an array of the <code>ShellView</code>s registered
	 */
	public ShellView[] getRegisteredShellViews() {
		return shellViewRegistry.toArray(new ShellView[shellViewRegistry.size()]);
	}
}
