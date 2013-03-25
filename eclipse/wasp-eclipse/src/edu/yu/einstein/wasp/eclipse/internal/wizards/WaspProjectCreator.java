package edu.yu.einstein.wasp.eclipse.internal.wizards;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.Resource;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.project.IProjectConfigurationManager;
import org.eclipse.m2e.core.project.ProjectImportConfiguration;
import org.osgi.framework.Bundle;

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
			IPath location, boolean web) throws CoreException {
		Assert.isNotNull(name);
		Assert.isTrue(name.trim().length() > 0);

		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IWorkspaceRoot root = workspace.getRoot();

		IProject project = root.getProject(name);

		try {
			// create project and configure maven
			configureMaven(name, namespace, project, location, web);
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

	public static void copyAndRewriteFiles(String name, String namespace, IPath location, IProject project, IPath projectRoot) {
		Bundle bundle = Platform.getBundle("wasp-eclipse");
		Enumeration<URL> files = bundle.findEntries("include", "*", true);
		String ns = namespace.replaceAll("\\.", "/").toLowerCase();
		String cname = name.toLowerCase().substring(0, 1).toUpperCase() + name.toLowerCase().substring(1);
		String lname = name.toLowerCase();

		while (files.hasMoreElements()) {
			URL f = files.nextElement();
			String opath = f.getFile().replaceFirst("/", "");
			if (!opath.endsWith(".java") && !opath.endsWith(".xml") && !opath.endsWith(".properties"))
				continue;
			IPath path = new Path(opath);
			String dpath = opath;
			dpath = dpath.replaceFirst("include/", "");
			dpath = dpath.replaceFirst("src/main/java/", "src/main/java/" + ns + "/" + lname + "/");
			dpath = dpath.replaceFirst("src/test/java/", "src/test/java/" + ns + "/" + lname + "/");
			dpath = dpath.replaceAll("Xxxxx", cname);
			dpath = dpath.replaceAll("xxxxx", lname);

			IPath dest = new Path(dpath);

			IFile file = project.getFile(dest);

			InputStream stream = null;
			try {
				stream = FileLocator.openStream(bundle, path, false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				file.create(stream, true, null);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			rewrite(new File(projectRoot + File.separator + lname + File.separator + dest.toFile().getPath()), lname, namespace);

		}

	}

	private static void rewrite(File file, String name, String pkg) {

		String cname = name.toLowerCase().substring(0, 1).toUpperCase() + name.toLowerCase().substring(1);

		List<String> lines = new ArrayList<String>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line = in.readLine();
			while (line != null) {
				line = line.replaceAll("___pluginname___", name);
				line = line.replaceAll("___Pluginname___", cname);
				line = line.replaceAll("___package___", pkg);
				lines.add(line);
				line = in.readLine();
			}
			in.close();

			PrintWriter out = new PrintWriter(file);
			for (String l : lines)
				out.println(l);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
			IProject project, IPath location, boolean web) throws CoreException {
		
		String propertiesFile = "/META-INF/maven/edu.yu.einstein.wasp/wasp-eclipse/pom.properties";
		Bundle bundle = Platform.getBundle("wasp-eclipse");
		
		//fallback value
		String version = "0.1.0-SNAPSHOT";
		Properties prop = new Properties();
		InputStream in;
		try {
			in = bundle.getEntry(propertiesFile).openStream();
			prop.load(in);
			in.close();
			version = prop.getProperty("version");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("NO VERSION INFORMATION IN BUNDLE, falling back to wasp version: " + version);
			// not in package
		}

		// create the maven pom model
		Model model = new Model();
		model.setModelVersion("4.0.0");
		model.setGroupId(namespace);
		model.setArtifactId(name);
		model.setPackaging("jar");
		model.setVersion(Messages.Project_defaultVersion);

		// configure maven compiler plugin
		// parent
		Parent parent = new Parent();
		parent.setGroupId("edu.yu.einstein.wasp");
		parent.setArtifactId("wasp-plugin");
		parent.setVersion(version);
		model.setParent(parent);

		// properties

		Properties props = new Properties();
		props.put("wasp.version", "${project.parent.version}");
		model.setProperties(props);

		// dependency management

		DependencyManagement dm = new DependencyManagement();
		Dependency bom = new Dependency();
		bom.setGroupId("edu.yu.einstein.wasp");
		bom.setArtifactId("bom");
		bom.setVersion("${wasp.version}");
		bom.setType("pom");
		bom.setScope("import");
		dm.getDependencies().add(bom);
		model.setDependencyManagement(dm);

		// dependencies

		List<Dependency> deps = new ArrayList<Dependency>();
		Dependency wasp = new Dependency();
		wasp.setGroupId("edu.yu.einstein.wasp");
		if (web) {
			wasp.setArtifactId("wasp-web");
		} else {
			wasp.setArtifactId("wasp-core");
		}
		deps.add(wasp);

		Map<String, String> tdep = new HashMap<String, String>();
		tdep.put("testng", "org.testng");
		tdep.put("powermock-module-testng", "org.powermock");
		tdep.put("powermock-api-mockito", "org.powermock");
		tdep.put("mockito-core", "org.mockito");

		for (String k : tdep.keySet()) {
			Dependency dep = new Dependency();
			dep.setGroupId(tdep.get(k));
			dep.setArtifactId(k);
			dep.setScope("test");
			deps.add(dep);
		}
		model.setDependencies(deps);

		// build
		Build build = new Build();

		// plugins
		Plugin plugin = new Plugin();
		plugin.setGroupId("org.apache.maven.plugins");
		plugin.setArtifactId("maven-dependency-plugin");
		build.addPlugin(plugin);
		Plugin plugin2 = new Plugin();
		plugin2.setGroupId("org.apache.maven.plugins");
		plugin2.setArtifactId("maven-enforcer-plugin");
		build.addPlugin(plugin2);

		// resources
		if (web) {
			Resource res = new Resource();
			res.setTargetPath("WEB-INF");
			res.setFiltering(true);
			res.setDirectory("src/main/webapp/WEB-INF");
			build.addResource(res);
		}
		Resource res = new Resource();
		res.setTargetPath(".");
		res.setFiltering(true);
		res.setDirectory("src/main/resources");
		build.addResource(res);

		model.setBuild(build);

		ProjectImportConfiguration config = new ProjectImportConfiguration();

		String ns = namespace.replaceAll("\\.", "/");

		String[] folders = { "src/main/java", "src/main/java/" + ns + "/" + name.toLowerCase() + "/plugin", "src/main/resources/batch",
				"src/main/resources/wasp", "src/main/resources/META-INF/spring", "src/main/resources/META-INF/tiles", "src/main/resources/flows",
				"src/main/resources/i18n/en_US", "src/main/resources/images", "src/main/webapp/WEB-INF/jsp/" + name.toLowerCase(),
				"src/test/java", "src/test/resources", "target/classes", "target/test-classes" };

		IProjectConfigurationManager mavenConfig = MavenPlugin.getProjectConfigurationManager();

		mavenConfig.createSimpleProject(project, location, model, folders, config, new NullProgressMonitor());
		

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

		try {
			IProjectDescription description = project.getDescription();
			String[] natures = description.getNatureIds();
			String[] newNatures = new String[natures.length + 2];
			System.arraycopy(natures, 0, newNatures, 0, natures.length);
			newNatures[natures.length] = "org.eclipse.jdt.core.javanature";
			newNatures[natures.length+1] = "org.springframework.ide.eclipse.core.springnature";
			description.setNatureIds(newNatures);
			project.setDescription(description, null);
		} catch (CoreException e) {
			// Something went wrong
		}
		
		try {
		IJavaProject javaProject = JavaCore.create(project);
		String pre = "/" + project.getName() + "/";
		IPath [] exclusions = { new Path("**") };
		IClasspathAttribute [] pd = { JavaCore.newClasspathAttribute("maven.pomderived", "true") };
		IClasspathAttribute [] opd = { JavaCore.newClasspathAttribute("optional", "true"), JavaCore.newClasspathAttribute("maven.pomderived", "true") };
		IClasspathEntry[] newClasspath = { 
				JavaCore.newSourceEntry(new Path(pre + "src/main/java"), null, null, new Path(pre + "target/classes"), opd),
				JavaCore.newSourceEntry(new Path(pre + "src/main/resources"), null, exclusions, new Path(pre + "target/classes"), pd),
				JavaCore.newSourceEntry(new Path(pre + "src/test/java"), null, null, new Path(pre + "target/test-classes"), opd),
				JavaCore.newSourceEntry(new Path(pre + "src/test/resources"), null, exclusions, new Path(pre + "target/test-classes"), pd),
				JavaCore.newContainerEntry(new Path("org.eclipse.jdt.launching.JRE_CONTAINER"), null, pd, false), 
				JavaCore.newContainerEntry(new Path("org.eclipse.m2e.MAVEN2_CLASSPATH_CONTAINER"), new IAccessRule[0], pd, false)
			};
		javaProject.setRawClasspath(newClasspath, new NullProgressMonitor());
		javaProject.save(new NullProgressMonitor(), false);
		} catch (Exception e) {
			e.printStackTrace();
		}

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
