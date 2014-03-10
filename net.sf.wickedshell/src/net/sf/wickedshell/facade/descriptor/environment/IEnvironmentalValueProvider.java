/*
 * IEnvironmentalValueProvider.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.facade.descriptor.environment;

import java.util.Properties;

/**
 * @author Stefan Reichert
 * @since 12.08.2006
 */
public interface IEnvironmentalValueProvider {

	/**
	 * Returns the shell's enviromental values.
	 * 
	 * @return the shell's enviromental values
	 */
	Properties getEnviromentalValues();
}
