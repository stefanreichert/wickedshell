/*
 * MSysShellEnvironmentalValueProvider.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.facade.descriptor.environment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.StringTokenizer;

import net.sf.wickedshell.facade.descriptor.environment.IEnvironmentalValueProvider;

/**
 * @author Stefan Reichert
 * @since 16.08.2006
 */
public class MSysShellEnvironmentalValueProvider implements IEnvironmentalValueProvider {

	/**
	 * @see net.sf.wickedshell.facade.descriptor.environment.IEnvironmentalValueProvider#getEnviromentalValues()
	 */
	public Properties getEnviromentalValues() {
		try {
			StringBuffer commandBuffer = new StringBuffer();
			commandBuffer.append("cmd.exe");
			commandBuffer.append(" /c SET");

			Process enviromentalValuesReaderProcess = Runtime.getRuntime().exec(
					commandBuffer.toString());
			BufferedReader enviromentalValuesReader = new BufferedReader(new InputStreamReader(
					enviromentalValuesReaderProcess.getInputStream()));
			Properties enviromentalValues = new Properties();
			String enviromentalValue;
			while ((enviromentalValue = enviromentalValuesReader.readLine()) != null
					&& enviromentalValue.indexOf("=") != -1 && !enviromentalValue.endsWith(">")) {
				StringTokenizer tokenizer = new StringTokenizer(enviromentalValue, "=");
				enviromentalValues.setProperty(tokenizer.nextToken(), tokenizer.nextToken());
			}
			enviromentalValuesReader.close();
			return enviromentalValues;
		}
		catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

}
