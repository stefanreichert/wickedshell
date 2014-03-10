/*
 * IBatchFileDescriptor.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.domain.batch;

/**
 * Interface for entries in the batch file view.
 * 
 * @author Stefan Reichert
 * @since 19.08.2006
 */
public interface IBatchFileDescriptor {

	/**
	 * Returns the name of the batch file.
	 */
	public String getFilename();

	/**
	 * Returns the parameters of the batch file.
	 */
	public String getParameters();

	/**
	 * Sets the parameters of the batch file.
	 */
	public void setParameters(String parameters);

	/**
	 * The factory for creating new <code>ICompletions</code> using an internal implementation.
	 * 
	 * @author Stefan Reichert
	 * @since 19.08.2006
	 */
	class Factory {

		/**
		 * Constructor for Completion.
		 */
		public static IBatchFileDescriptor newInstance(String filename) {
			return new BatchFileDescriptor(filename, new String());
		}

		/**
		 * Constructor for Completion.
		 */
		public static IBatchFileDescriptor newInstance(
				net.sf.wickedshell.domain.batch.BatchFileDescriptor batchFileDescriptor) {
			return new BatchFileDescriptor(batchFileDescriptor.getFilename(), batchFileDescriptor
					.getParameters());
		}
	}

	/**
	 * @author Stefan Reichert
	 * @since 07.08.2006
	 */
	public final class BatchFileDescriptor implements IBatchFileDescriptor {

		private String filename;

		private String parameters;

		/**
		 * Constructor for BatchFileDescriptor.
		 */
		private BatchFileDescriptor(String filename, String parameters) {
			super();
			this.filename = filename;
			this.parameters = parameters;
		}

		/**
		 * @see net.sf.wickedshell.domain.batch.IBatchFileDescriptor#getFilename()
		 */
		public String getFilename() {
			return filename;
		}

		/**
		 * @see net.sf.wickedshell.domain.batch.IBatchFileDescriptor#getParameters()
		 */
		public String getParameters() {
			return parameters;
		}

		/**
		 * @see net.sf.wickedshell.domain.batch.IBatchFileDescriptor#setParameters(java.lang.String)
		 */
		public void setParameters(String parameters) {
			this.parameters = parameters;
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object object) {
			if (object instanceof BatchFileDescriptor) {
				BatchFileDescriptor batchFileDescriptor = (BatchFileDescriptor) object;
				return filename.equals(batchFileDescriptor.getFilename())
						&& parameters.equals(batchFileDescriptor.getParameters());
			}
			return false;
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() {
			return filename.hashCode();
		}
	}

}
