/**
 * (c) 2012 Albert Einstein College of Medicine
 */
package edu.yu.einstein.wasp.eclipse.internal.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

import edu.yu.einstein.wasp.eclipse.internal.Messages;

/**
 * @author calder
 * 
 *         Project wizard for creating a plug-in to the WASP system
 * 
 *         The wizard should allow the user to create a new eclipse project,
 *         choose which types of preconfigured example files should be included
 *         (interface plugin, resource plugin and workflow plugin), and will
 *         create the directory structure, and configure Maven and Spring.
 * 
 */
public class WaspPluginProjectWizard extends Wizard implements INewWizard {

	protected WaspProjectConfigurationPage configPage;

	protected String projectName;
	protected String projectNamespace;

	public WaspPluginProjectWizard() {
		super();
		// setWindowTitle(Messages...);
		// setDefaultPageImageDescriptor(...);
		setNeedsProgressMonitor(true);
	}

	@Override
	public void init(IWorkbench arg0, IStructuredSelection arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addPages() {

		configPage = new WaspProjectConfigurationPage(
				Messages.WizardConfigurationPage_pageName,
				Messages.WizardConfigurationPage_pageDescription);
		addPage(configPage);

	}

	@Override
	public boolean performFinish() {
		WaspProjectConfigurationPage configPage = (WaspProjectConfigurationPage) getPage(Messages.WizardConfigurationPage_pageName);

		IWorkspace workspace = ResourcesPlugin.getWorkspace();

		final IPath location = configPage.isDefaultWorkspace() ? null
				: configPage.getLocation();
		final IWorkspaceRoot root = workspace.getRoot();

		IProject project = root.getProject(configPage.getProjectName());

		IPath projectRoot = configPage.getLocation();

		boolean exists = projectRoot.append(configPage.getProjectName()).toFile().exists();
		boolean pomExists = projectRoot.append(configPage.getProjectName()).append("pom.xml").toFile().exists();

		if (exists || pomExists) {
			MessageDialog.openError(getShell(),
					NLS.bind(Messages.WizardFailed, projectName),
					Messages.WizardFailed_projectOrPomFileAlreadyExists);
			return false;
		}

		IWorkbench workbench = PlatformUI.getWorkbench();
		IProgressService progress = workbench.getProgressService();

		try {
			progress.runInUI(progress, new IRunnableWithProgress() {
				public void run(IProgressMonitor pm) {
					try {
						WaspProjectConfigurationPage configPage = (WaspProjectConfigurationPage) getPage(Messages.WizardConfigurationPage_pageName);
						IPath projectRoot = configPage.getLocation();
						IPath loc = projectRoot.addTrailingSeparator().append(
								configPage.getProjectName());
						IProject project = root.getProject(configPage
								.getProjectName());
						WaspProjectCreator.createProject(
								configPage.getProjectName(),
								configPage.getProjectNamespace(), loc);
						if (configPage.createForms())
							WaspProjectCreator.populateFlow(
									configPage.getProjectName(),
									configPage.getProjectNamespace(), loc, project);
						if (configPage.createResource())
							WaspProjectCreator.populateResource(
									configPage.getProjectName(),
									configPage.getProjectNamespace(), loc);
						if (configPage.createPipeline())
							WaspProjectCreator.populatePipeline(
									configPage.getProjectName(),
									configPage.getProjectNamespace(), loc);
					} catch (Exception e) {
						MessageDialog.openError(getShell(),
								NLS.bind(Messages.WizardFailed, projectName),
								Messages.WizardFailed_errorCreatingProject);
						e.printStackTrace();
					}
				}
			}, ResourcesPlugin.getWorkspace().getRoot());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}

}
