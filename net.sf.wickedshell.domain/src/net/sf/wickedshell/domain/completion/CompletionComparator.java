/*
 * CompletionComparator.java
 * 
 * Copyright 2003-2004 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.domain.completion;

import java.util.Comparator;

/**
 * @author Stefan Reichert
 * @since 14.05.2005
 */
public class CompletionComparator implements Comparator<ICompletion> {

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(ICompletion completionOne, ICompletion completionTwo) {
		return completionOne.getContent().compareTo(completionTwo.getContent());
	}
}
