/*
 * ShellContentProposalProvider.java
 * 
 * Copyright 2005-2006 Stefan Reichert.
 * All Rights Reserved.
 * 
 * This software is the proprietary information of Stefan Reichert.
 * Use is subject to license terms.
 * 
 */
package net.sf.wickedshell.ui.shell.viewer.proposal;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.sf.wickedshell.ShellID;
import net.sf.wickedshell.ShellPlugin;
import net.sf.wickedshell.domain.DomainPlugin;
import net.sf.wickedshell.domain.completion.ICompletion;
import net.sf.wickedshell.ui.shell.viewer.IShellViewer;
import net.sf.wickedshell.util.ShellLogger;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

/**
 * @author Stefan Reichert
 * @since 19.10.2006
 */
public class ShellContentProposalProvider implements IContentProposalProvider {

	/** The <code>Reader</code> of the shell. */
	private final ShellLogger shellLogger = new ShellLogger(ShellContentProposalProvider.class);

	private IShellViewer shellViewer;

	/**
	 * Constructor for ShellContentAssistProcessor.
	 * 
	 * @param shellManager
	 */
	public ShellContentProposalProvider(IShellViewer shellViewer) {
		super();
		this.shellViewer = shellViewer;
	}

	/**
	 * @see org.eclipse.jface.fieldassist.IContentProposalProvider#getProposals(java.lang.String,
	 *      int)
	 */
	@SuppressWarnings("unchecked")
	public IContentProposal[] getProposals(String contents, int position) {
		int commandOffset = contents.length() - position;
		String currentCommand = shellViewer.getCurrentCommand();
		int commandPrefixLength = currentCommand.length() - commandOffset;

		List proposals = new ArrayList();
		if(commandPrefixLength >= 0) {
			String prefix = currentCommand.substring(0, commandPrefixLength);
			shellLogger.debug("Computing command completion with prefix [" + prefix + "]");
			List possibleProposals = getProposalCandidates(prefix);
			for (int index = 0; index < possibleProposals.size(); index++) {
				ICompletion proposalCandidate = (ICompletion) possibleProposals.get(index);
				if(prefix.length() == 0 || proposalCandidate.getContent().toLowerCase().startsWith(prefix.toLowerCase())) {
					proposals.add(proposalCandidate);
				}
			}
		}
		return (IContentProposal[]) proposals.toArray(new IContentProposal[0]);
	}

	/**
	 * Returns a <code>List</code> of possible <code>Completions</code>
	 * including static, systempath and currentpath completions.
	 * 
	 * @param prefix
	 *            The prefix yet entered by the user
	 */
	@SuppressWarnings("unchecked")
	private List getProposalCandidates(String prefix) {
		List proposalCandidates = new ArrayList();
		if(ShellPlugin.getDefault().getPreferenceStore().getBoolean(ShellID.SHOW_CUSTOM_COMMANDS_STRING_KEY)) {
			proposalCandidates.addAll(DomainPlugin.getDefault().getStaticCompletions());
		}
		if(ShellPlugin.getDefault().getPreferenceStore().getBoolean(ShellID.SHOW_ACTIVE_FOLDER_COMMANDS_STRING_KEY)) {
			proposalCandidates.addAll(CompletionController.getCurrentPathCompletions(shellViewer.getShellFacade().getShellDescriptor(), shellViewer
					.getCurrentPath()));
		}
		if(ShellPlugin.getDefault().getPreferenceStore().getBoolean(ShellID.SHOW_CASCADING_COMPLETIONS_STRING_KEY)) {
			if(prefix.indexOf(' ') != -1) {
				StringTokenizer tokenizer = new StringTokenizer(prefix, " ");
				StringBuffer buffer = new StringBuffer();
				while (tokenizer.hasMoreTokens()) {
					buffer.append(tokenizer.nextToken());
					String command = buffer.toString();
					int pathPrefixOffset = command.length() + 1;
					if(pathPrefixOffset > prefix.length()) {
						// Avoid Exception on last token
						pathPrefixOffset = prefix.length();
					}
					String pathPrefix = prefix.substring(pathPrefixOffset, prefix.length());
					shellLogger.debug("Computing cascading completions for command [" + command + "] and cascading path prefix [" + pathPrefix + "] in path ["
							+ shellViewer.getCurrentPath() + "]");
					proposalCandidates.addAll(CompletionController.getCascadingCompletions(shellViewer.getShellFacade().getShellDescriptor(), shellViewer
							.getCurrentPath(), buffer.toString(), pathPrefix));
					buffer.append(' ');
				}
			}
		}
		if(ShellPlugin.getDefault().getPreferenceStore().getBoolean(ShellID.SHOW_SYSTEM_ENVIROMENTAL_VALUES_STRING_KEY)) {
			int indexLastBlank = prefix.lastIndexOf(' ');
			String command = new String();
			String environmentalValuePrefix = prefix;
			if(indexLastBlank != -1) {
				command = prefix.substring(0, indexLastBlank + 1);
				environmentalValuePrefix = prefix.substring(indexLastBlank + 1, prefix.length());
			}
			proposalCandidates.addAll(CompletionController.getEnviromentalValueCompletions(shellViewer.getShellFacade().getShellDescriptor(),
					environmentalValuePrefix, command));
		}
		if(ShellPlugin.getDefault().getPreferenceStore().getBoolean(ShellID.SHOW_SYSTEM_PATH_COMMANDS_STRING_KEY)) {
			proposalCandidates.addAll(CompletionController.getSystemPathCompletions(shellViewer.getShellFacade().getShellDescriptor()));
		}
		return proposalCandidates;
	}
}
