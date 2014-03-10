/*
 * IFontDescriptor.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.domain.style;

/**
 * @author Stefan Reichert
 * @since 30.05.2006
 */
public interface IFontDescriptor {

	/**
	 * Gets the height of the <code>Font</code>.
	 */
	int getHeight();

	/**
	 * Gets the name of the <code>Font</code>.
	 */
	String getName();

	/**
	 * Gets the style of the <code>Font</code>.
	 */
	int getStyle();

	/**
	 * The factory for creating new <code>BatchFileDescriptor</code> using an internal
	 * implementation.
	 * 
	 * @author Stefan Reichert
	 * @since 19.08.2006
	 */
	class Factory {

		/**
		 * Constructor for IFontDescriptor.
		 */
		public static IFontDescriptor newInstance(String name, int height, int style) {
			return new FontDescriptor(name, height, style);
		}

		/**
		 * Constructor for IFontDescriptor.
		 */
		public static IFontDescriptor newInstance(
				net.sf.wickedshell.domain.style.FontDescriptor fontDescriptor) {
			return new FontDescriptor(fontDescriptor.getName(), Integer.parseInt(fontDescriptor
					.getHeight()), Integer.parseInt(fontDescriptor.getStyle()));
		}
	}

	/**
	 * An internal implementation for <code>IFontDescriptor</code>.
	 * 
	 * @author Stefan Reichert
	 * @since 19.08.2006
	 */
	public class FontDescriptor implements IFontDescriptor {

		/** The name of the <code>Font</code>. */
		private final String name;
		/** The height of the <code>Font</code>. */
		private final int height;
		/** The style of the <code>Font</code>. */
		private final int style;

		/**
		 * Constructor for IFontDescriptor.
		 */
		private FontDescriptor(String name, int height, int style) {
			super();
			this.name = name;
			this.height = height;
			this.style = style;
		}

		public int getHeight() {
			return height;
		}

		public String getName() {
			return name;
		}

		public int getStyle() {
			return style;
		}
	}
}
