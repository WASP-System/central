package edu.yu.einstein.wasp.eclipse.internal.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.Repository;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.project.IProjectConfigurationManager;
import org.eclipse.m2e.core.project.ProjectImportConfiguration;

import edu.yu.einstein.wasp.eclipse.internal.Messages;

public class WaspProjectCreator {

	/**
	 * Creates a WASP plugin project with some sensible defaults
	 * 
	 * @param name
	 *            name of the project [a-zA-z0-9_]
	 * @param namespace
	 *            java-style namespace for the project
	 * @param location
	 *            IPath to the project location
	 * @return the IProject
	 * @throws CoreException
	 *             Eclipse failed to create the project
	 */
	public static IProject createProject(String name, String namespace,
			IPath location) throws CoreException {
		Assert.isNotNull(name);
		Assert.isTrue(name.trim().length() > 0);

		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IWorkspaceRoot root = workspace.getRoot();

		IProject project = root.getProject(name);

		try {
			// create project and configure maven
			configureMaven(name, namespace, project, location);
			// configure additional project natures (e.g. spring)
			configureNatures(project);
			// add package folders to the project
			addPackages(name, namespace, project);
		} catch (CoreException e1) {
			e1.printStackTrace();
			throw e1;
		}

		return project;

	}

	public static void populateFlow(String name, String namespace,
			IPath location, IProject project) {

		String ns = namespace.replaceAll("\\.", "/");

		IPath path = new Path("src/main/java/" + ns + "/" + name.toLowerCase()
				+ "/WaspFormController.java");
		// project path is relative
		IFile file = project.getFile(path);

		InputStream inputStream = null;
		try {
			String pageSource = Messages.Project_webFormControllerSource;
			pageSource = pageSource.replace("PACKAGEPLACEHOLDER", namespace
					+ "." + name.toLowerCase());
			pageSource = pageSource.replace("CLASSNAMEPLACEHOLDER",
					"WaspFormController");
			inputStream = new ByteArrayInputStream(pageSource.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			file.create(inputStream, false, null);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void populateResource(String name, String namespace,
			IPath location) {

	}

	public static void populatePipeline(String name, String namespace,
			IPath location) {

	}

	/**
	 * Create a new project based on the m2e classes
	 * 
	 * Creates a new java project with sensible defaults.
	 * 
	 * @param name
	 * @param namespace
	 * @param project
	 * @param location
	 * @throws CoreException
	 */
	private static void configureMaven(String name, String namespace,
			IProject project, IPath location) throws CoreException {

		// configure maven compiler plugin
		Plugin compilePlugin = new Plugin();
		compilePlugin.setGroupId("org.apache.maven.plugins");
		compilePlugin.setArtifactId("maven-compiler-plugin");
		compilePlugin.setVersion("2.3.2");

		Xpp3Dom conf = new Xpp3Dom("configuration");
		Xpp3Dom source = new Xpp3Dom("source");
		source.setValue("1.6");
		Xpp3Dom target = new Xpp3Dom("target");
		target.setValue("1.6");
		Xpp3Dom encoding = new Xpp3Dom("encoding");
		encoding.setValue("UTF-8");
		conf.addChild(source);
		conf.addChild(target);
		conf.addChild(encoding);
		compilePlugin.setConfiguration(conf);

		// set up maven build phase with plugins
		Build build = new Build();
		build.addPlugin(compilePlugin);

		// create the maven pom model
		Model model = new Model();
		model.setBuild(build);
		model.setModelVersion("4.0.0");
		model.setGroupId(namespace);
		model.setArtifactId(Messages.Project_prefix + name);
		model.setVersion(Messages.Project_defaultVersion);

		// configure repositories and add to model
		Repository repo = new Repository();
		repo.setUrl(Messages.Project_waspRepositoryURL);
		repo.setName(Messages.Project_waspRepositoryName);
		repo.setId(Messages.Project_waspRepositoryID);
		model.addRepository(repo);

		// configure dependencies (primarily wasp-core) and add to model
		Dependency dep = new Dependency();
		dep.setArtifactId(Messages.Project_waspArtifactID);
		dep.setGroupId(Messages.Project_waspGroupID);
		dep.setVersion(Messages.Project_waspVersion);
		dep.setType(Messages.Project_waspType);
		model.addDependency(dep);

		ProjectImportConfiguration config = new ProjectImportConfiguration();

		String[] folders = { "src/main/java", "src/main/resources/batch",
				"src/main/resources/wasp", "src/test/java",
				"src/test/resources" };

		IProjectConfigurationManager mavenConfig = MavenPlugin
				.getProjectConfigurationManager();

		mavenConfig.createSimpleProject(project, location, model, folders,
				config, new NullProgressMonitor());

	}

	/**
	 * Configures eclipse natures into the project.
	 * 
	 * The project is created as a Maven project, here we configure it to have
	 * the Spring project nature, in addition to the maven and java natures.
	 * 
	 * @param project
	 *            IProject object for the new project
	 * @throws CoreException
	 *             Eclipse failed to add natures
	 */
	private static void configureNatures(IProject project) throws CoreException {

		String[] old = project.getDescription().getNatureIds();
		List<String> natures = new ArrayList<String>(Arrays.asList(old));
		natures.add("org.springframework.ide.eclipse.core.springnature");
		project.getDescription().setNatureIds(
				natures.toArray(new String[natures.size()]));

	}

	/**
	 * Adds the java project hierarchy to the source tree.
	 * 
	 * @param name
	 *            Name of the new project
	 * @param namespace
	 *            Project's namespace
	 * @param project
	 *            project object
	 * @throws CoreException
	 *             Eclipse failed to add projects
	 */
	private static void addPackages(String name, String namespace,
			IProject project) throws CoreException {

		IJavaProject jproject = JavaCore.create(project);
		// create src/main/java/${namespace}
		IFolder src = project.getFolder("src/main/java");
		IPackageFragmentRoot srcRoot = jproject.getPackageFragmentRoot(src);
		srcRoot.createPackageFragment(namespace + "." + name.toLowerCase(),
				true, null);

		// create src/test/java/${namespace}
		src = project.getFolder("src/test/java");
		srcRoot = jproject.getPackageFragmentRoot(src);
		srcRoot.createPackageFragment(namespace + "." + name.toLowerCase(),
				true, null);

	}

}
