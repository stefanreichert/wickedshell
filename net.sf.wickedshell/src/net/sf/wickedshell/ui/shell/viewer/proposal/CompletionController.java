/*
 * CompletionController.java
 * 
 * Copyright 2003-2004 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.ui.shell.viewer.proposal;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import net.sf.wickedshell.ShellID;
import net.sf.wickedshell.domain.DomainPlugin;
import net.sf.wickedshell.domain.completion.CompletionComparator;
import net.sf.wickedshell.domain.completion.ICompletion;
import net.sf.wickedshell.facade.descriptor.IShellDescriptor;
import net.sf.wickedshell.util.ShellLogger;
import net.sf.wickedshell.util.filter.ExecutableFileFilter;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

/**
 * @author Stefan Reichert
 * @since 15.05.2005
 */
public class CompletionController {

	/** The <code>Reader</code> of the shell. */
	private static final ShellLogger shellLogger = new ShellLogger(CompletionController.class);

	/**
	 * Reads the systems's classpath and identifies all possible executable was
	 * can currently be executed.
	 * 
	 * @return the <code>List</code> of <code>Completions</code> referring
	 *         to the system path
	 */
	@SuppressWarnings("unchecked")
	public static final List getSystemPathCompletions(IShellDescriptor descriptor) {
		Set systemPathCompletionSet = new HashSet();
		String systemPath = System.getProperty(ShellID.SYSTEM_PATH_KEY);
		String[] systemPathEntries = systemPath.split(String.valueOf(descriptor.getSystemPathSeparator()));
		File systemPathEntry;
		for (int systemPathEntryIndex = 0; systemPathEntryIndex < systemPathEntries.length; systemPathEntryIndex++) {
			systemPathEntry = new File(systemPathEntries[systemPathEntryIndex]);
			String[] completions = systemPathEntry.list(new ExecutableFileFilter(descriptor, false));
			if(completions != null) {
				for (int index = 0; index < completions.length; index++) {
					String completion = completions[index];
					String description = completions[index] + " - Execute <" + completions[index] + "> (System path)";
					String imagePath = "img/systemPathExecutable.gif";
					systemPathCompletionSet.add(ICompletion.Factory.newInstance(completion, description, imagePath));
				}
			}
		}
		List systemPathCompletions = new ArrayList(systemPathCompletionSet);
		Collections.sort(systemPathCompletions, new CompletionComparator());
		return systemPathCompletions;
	}

	/**
	 * Creates the path as <code>File</code> considering the custom root of
	 * the given <code>IShellDescriptor</code>, if exists.
	 */
	private static final File getPath(IShellDescriptor descriptor, String pathString) {
		StringBuffer fullPathStringBuffer = new StringBuffer();
		if(descriptor.hasCustomRoot()) {
			fullPathStringBuffer.append(DomainPlugin.getDefault().getShellDescriptorProperties(descriptor.getId()).getRootDirectory());
		}
		fullPathStringBuffer.append(pathString.replace(descriptor.getPathSeparator(), File.separatorChar));
		String fullPathString = fullPathStringBuffer.toString();
		// if(fullPathString.startsWith("D:\\Programme\\cygwin\\cygdrive\\d")){
		// int realPathOffset = "D:\\Programme\\cygwin\\cygdrive\\d".length();
		// fullPathString = "D:\\" + fullPathString.substring(realPathOffset);
		// }
		return new File(fullPathString);
	}

	/**
	 * Reads the current classpath and identifies all possible executables and
	 * directories was can currently be executed (accessed).
	 * 
	 * @return the <code>List</code> of <code>Completions</code> referring
	 *         to the system path
	 */
	@SuppressWarnings("unchecked")
	public static final List getCurrentPathCompletions(final IShellDescriptor descriptor, String currentPathString) {
		Set currentPathCompletionSet = new HashSet();
		File currentPath = getPath(descriptor, currentPathString);
		if(currentPath != null) {
			File[] completions = currentPath.listFiles(new ExecutableFileFilter(descriptor, true));
			if(completions != null) {
				CollectionUtils.collect(Arrays.asList(completions), new Transformer() {
					/**
					 * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
					 */
					public Object transform(Object object) {
						File completionFile = (File) object;
						String completionFileName = completionFile.getName();

						String completion;
						String description;
						String imagePath;
						if(completionFile.isDirectory()) {
							// completion = "cd " + new
							// CmdShellPathManager().preparePath(completionFileName)
							// + descriptor.getPathSeparator();
							completion = "cd " + completionFileName + descriptor.getPathSeparator();
							description = "cd " + completionFileName + " - Change to directory <" + completionFileName + "> (Current path)";
							imagePath = "img/changeDirectory.gif";
						}
						else {
							completion = completionFileName;
							description = completionFileName + " - Execute <" + completionFileName + "> (Current path)";
							imagePath = "img/executable.gif";
						}
						return ICompletion.Factory.newInstance(completion, description, imagePath);
					}
				}, currentPathCompletionSet);
			}
		}
		List currentPathCompletions = new ArrayList(currentPathCompletionSet);
		Collections.sort(currentPathCompletions, new CompletionComparator());
		return currentPathCompletions;
	}

	/**
	 * Identifies all possible cascading completion for the given prefix and the
	 * given path.
	 * 
	 * @return the <code>List</code> of <code>Completions</code> referring
	 *         to the given bprefix and the given path
	 */
	@SuppressWarnings("unchecked")
	public static List getCascadingCompletions(IShellDescriptor descriptor, String currentPathString, String command, String pathPrefix) {
		Set cascadingCompletionSet = new HashSet();
		int lastSeparatorIndex = pathPrefix.lastIndexOf(descriptor.getPathSeparator());
		if(lastSeparatorIndex != -1) {
			// Get the current path
			File currentPath = getPath(descriptor, currentPathString);
			// Identify relative cascading completions
			StringBuffer relativeCascadedPathStringBuffer = new StringBuffer();
			relativeCascadedPathStringBuffer.append(currentPath.getAbsolutePath());
			if(!descriptor.hasCustomRoot()) {
				relativeCascadedPathStringBuffer.append(File.separator);
			}
			relativeCascadedPathStringBuffer.append(pathPrefix.substring(0, lastSeparatorIndex).replace(descriptor.getPathSeparator(), File.separatorChar));
			File relativeCascadedPath = getPath(descriptor, relativeCascadedPathStringBuffer.toString());
			shellLogger.debug("Searching relative cascading completions with prefix [" + pathPrefix.substring(lastSeparatorIndex + 1) + "] in path ["
					+ relativeCascadedPath + "]");
			// Inspect the relatively cascaded folder
			cascadingCompletionSet.addAll(computeCascadeCompletions(descriptor, command, pathPrefix, relativeCascadedPath));

			// Identify absolute cascading completions if no others were found
			if(cascadingCompletionSet.isEmpty()) {
				StringBuffer absoluteCascadeStringBuffer = new StringBuffer();
				if(descriptor.hasCustomRoot()) {
					absoluteCascadeStringBuffer.append(DomainPlugin.getDefault().getShellDescriptorProperties(descriptor.getId()).getRootDirectory());
				}
				absoluteCascadeStringBuffer.append(pathPrefix.substring(0, lastSeparatorIndex).replace(descriptor.getPathSeparator(), File.separatorChar));
				if(!descriptor.hasCustomRoot()) {
					absoluteCascadeStringBuffer.append(File.separator);
				}
				File absoluteCascadePath = getPath(descriptor, absoluteCascadeStringBuffer.toString());
				shellLogger.debug("Searching absolute cascading completions with prefix [" + pathPrefix.substring(lastSeparatorIndex + 1) + "] in path ["
						+ absoluteCascadePath + "]");
				// Inspect the absolute cascaded folder
				cascadingCompletionSet.addAll(computeCascadeCompletions(descriptor, command, pathPrefix, absoluteCascadePath));
			}
		}
		List cascadingCompletions = new ArrayList(cascadingCompletionSet);
		Collections.sort(cascadingCompletions, new CompletionComparator());
		return cascadingCompletions;
	}

	/**
	 * Identifies all possible enviromental value completion for the given
	 * prefix.
	 * 
	 * @return the <code>List</code> of <code>Completions</code> referring
	 *         to the given prefix
	 */
	@SuppressWarnings("unchecked")
	public static List getEnviromentalValueCompletions(IShellDescriptor descriptor, final String environmentalValuePrefix, final String command) {
		List currentEnviromentalValueCompletions = new ArrayList();
		CollectionUtils.select(descriptor.getEnviromentalValues().entrySet(), new Predicate() {
			public boolean evaluate(Object object) {
				Entry entry = (Entry) object;
				String enviromentalValue = (String) entry.getKey();
				return enviromentalValue.toLowerCase().startsWith(environmentalValuePrefix.toLowerCase());
			}
		}, currentEnviromentalValueCompletions);
		CollectionUtils.transform(currentEnviromentalValueCompletions, new Transformer() {
			public Object transform(Object object) {
				Entry entry = (Entry) object;
				String completion = command + (String) entry.getKey();
				String description = (String) entry.getKey() + " <" + (String) entry.getValue() + "> - Environmental value (Cascading completion)";
				String imagePath = "img/environmentalValue.gif";
				return ICompletion.Factory.newInstance(completion, description, imagePath);
			}
		});

		Collections.sort(currentEnviromentalValueCompletions, new CompletionComparator());
		return currentEnviromentalValueCompletions;
	}

	/**
	 * Computes cascading completions based on the path prefix. This prefix may
	 * either be relative or absolut
	 * 
	 * @param descriptor
	 *            The underlaying <code>IShellDescriptor</code>
	 * @param command
	 *            The command which predesesses the prefix
	 * @param pathPrefix
	 *            The prefix of the path to compute the completions for
	 * @param cascadedPath
	 *            The path to search with the prefix
	 * @return a <code>Collection</code> of <code>ICompletion</code>
	 */
	private static Collection<ICompletion> computeCascadeCompletions(final IShellDescriptor descriptor, final String command, final String pathPrefix,
			final File cascadedPath) {
		Set<ICompletion> cascadingCompletionSet = new HashSet<ICompletion>();
		if(cascadedPath != null && cascadedPath.exists()) {
			File[] completions = cascadedPath.listFiles();
			if(completions != null) {
				shellLogger.debug("Inspecting [" + completions.length + "] entries in path [" + cascadedPath + "]");
				CollectionUtils.collect(Arrays.asList(completions), new Transformer() {
					/**
					 * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
					 */
					public Object transform(Object object) {
						File completionFile = (File) object;
						StringBuffer cascadedPathCompletionBuffer = new StringBuffer();
						cascadedPathCompletionBuffer.append(pathPrefix.substring(0, pathPrefix.lastIndexOf(descriptor.getPathSeparator())));
						cascadedPathCompletionBuffer.append(descriptor.getPathSeparator());
						cascadedPathCompletionBuffer.append(completionFile.getName());

						String completion;
						String description;
						String imagePath;
						if(completionFile.isDirectory()) {
							completion = command + " " + cascadedPathCompletionBuffer + descriptor.getPathSeparator();
							description = cascadedPathCompletionBuffer.toString() + " - Folder (Cascading completion)";
							imagePath = "img/cascadingFolder.gif";
						}
						else {
							completion = command + " " + cascadedPathCompletionBuffer;
							description = cascadedPathCompletionBuffer.toString() + " - File (Cascading completion)";
							imagePath = "img/cascadingFile.gif";
						}
						return ICompletion.Factory.newInstance(completion, description, imagePath);
					}
				}, cascadingCompletionSet);
			}
		}
		shellLogger.debug("Found [" + cascadingCompletionSet.size() + "] entries in path [" + cascadedPath + "]");
		return cascadingCompletionSet;
	}

}
