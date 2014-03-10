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
package net.sf.wickedshell.domain.completion;

import org.eclipse.jface.fieldassist.IContentProposal;

/**
 * Interface for entries in the content assist.
 * 
 * @author Stefan Reichert
 * @since 19.08.2006
 */
public interface ICompletion extends IContentProposal {

	/**
	 * @see org.eclipse.jface.fieldassist.IContentProposal#getContent()
	 */
	public String getContent();

	/**
	 * @see org.eclipse.jface.fieldassist.IContentProposal#getLabel()
	 */
	public String getLabel();

	/**
	 * @return Returns the imagePath.
	 */
	String getImagePath();

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
		public static ICompletion newInstance(String completion, String description, String imagePath) {
			return new Completion(completion, description, imagePath);
		}

		/**
		 * Constructor for Completion.
		 */
		public static ICompletion newInstance(StaticCompletion staticCompletion) {
			return new Completion(staticCompletion.getContent(), staticCompletion.getLabel(),
					staticCompletion.getImagePath());
		}
	}

	/**
	 * Internal implementation for <code>IBatchFileDescriptor</code>.
	 * 
	 * @author Stefan Reichert
	 * @since 19.08.2006
	 */
	public final class Completion implements ICompletion {

		/** The content to be inserted in the shell. */
		private final String content;
		/** The description to be shown in the content assist. */
		private final String label;
		/** The path of the image to be shown in the content assist. */
		private final String imagePath;

		/**
		 * Private constructor for <code>Completion</code> to avoid instantiating.
		 */
		private Completion(String content, String label, String imagePath) {
			super();
			this.content = content;
			this.label = label;
			this.imagePath = imagePath;
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() {
			return content.hashCode();
		}

		/**
		 * @see org.eclipse.jface.fieldassist.IContentProposal#getContent()
		 */
		public String getContent() {
			return content;
		}

		/**
		 * @see org.eclipse.jface.fieldassist.IContentProposal#getCursorPosition()
		 */
		public int getCursorPosition() {
			return content.length();
		}

		/**
		 * @see org.eclipse.jface.fieldassist.IContentProposal#getLabel()
		 */
		public String getLabel() {
			return label;
		}

		/**
		 * @see net.sf.wickedshell.domain.completion.ICompletion#getDescription()
		 */
		public String getDescription() {
			return null;
		}

		/**
		 * @see net.sf.wickedshell.domain.completion.ICompletion#getImagePath()
		 */
		public String getImagePath() {
			return imagePath;
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object object) {
			if (object instanceof Completion) {
				Completion completion = (Completion) object;
				return content.equals(completion.getContent());
			}
			return false;
		}

	}
}
