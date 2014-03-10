package net.sf.wickedshell.facade;

public class ShellDeactivationException extends Exception {
	/** The serial version UID. */
	private static final long serialVersionUID = 3510427036948192061L;

	/**
	 * @param message
	 *            The message
	 */
	public ShellDeactivationException(String message) {
		super(message);
	}

	/**
	 * @param message
	 *            The message
	 * @param throwable
	 *            The nested <code>Throwable</code>
	 */
	public ShellDeactivationException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
