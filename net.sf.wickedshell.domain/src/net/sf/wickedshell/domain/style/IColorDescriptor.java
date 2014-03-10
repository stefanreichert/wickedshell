/*
 * IColorDescriptor.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.domain.style;

import java.io.Serializable;

/**
 * @author Stefan Reichert
 * @since 30.05.2006
 */
public interface IColorDescriptor extends Serializable {

	/**
	 * Gets the red value of the <code>Color</code>.
	 */
	int getRed();

	/**
	 * Gets the green value of the <code>Color</code>.
	 */
	int getGreen();

	/**
	 * Gets the blue value of the <code>Color</code>.
	 */
	int getBlue();

	/**
	 * The factory for creating new <code>IColorDescriptor</code> using an internal implementation.
	 * 
	 * @author Stefan Reichert
	 * @since 19.08.2006
	 */
	class Factory {

		/**
		 * Constructor for IColorDescriptor.
		 */
		public static IColorDescriptor newInstance(int red, int green, int blue) {
			return new ColorDescriptor(red, green, blue);
		}

		/**
		 * Constructor for IColorDescriptor.
		 */
		public static IColorDescriptor newInstance(
				net.sf.wickedshell.domain.style.ColorDescriptor colorDescriptor) {
			return new ColorDescriptor(Integer.parseInt(colorDescriptor.getRed()), Integer
					.parseInt(colorDescriptor.getGreen()), Integer.parseInt(colorDescriptor.getBlue()));
		}
	}

	/**
	 * An internal implementation for <code>IColorDescriptor</code>.
	 * 
	 * @author Stefan Reichert
	 * @since 19.08.2006
	 */
	public class ColorDescriptor implements IColorDescriptor {

		/** The seial version UID. */
		private static final long serialVersionUID = 2064008731355656380L;

		/** The name of the <code>Color</code>. */
		private final int red;
		/** The height of the <code>Color</code>. */
		private final int green;
		/** The style of the <code>Color</code>. */
		private final int blue;

		/**
		 * Constructor for ColorDescriptor.
		 */
		private ColorDescriptor(int red, int green, int blue) {
			super();
			this.red = red;
			this.green = green;
			this.blue = blue;
		}

		/**
		 * @see net.sf.wickedshell.domain.style.IColorDescriptor#getRed()
		 */
		public int getRed() {
			return red;
		}

		/**
		 * @see net.sf.wickedshell.domain.style.IColorDescriptor#getGreen()
		 */
		public int getGreen() {
			return green;
		}

		/**
		 * @see net.sf.wickedshell.domain.style.IColorDescriptor#getBlue()
		 */
		public int getBlue() {
			return blue;
		}
	}
}
