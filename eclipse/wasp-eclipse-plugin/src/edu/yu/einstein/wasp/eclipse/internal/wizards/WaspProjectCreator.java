package edu.yu.einstein.wasp.eclipse.internal.wizards;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
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

/**
 * Eclipse Plugin Creator to generate a template WASP System plugin with working example
 * defaults.  Java, Spring and Maven project natures are configured by default.
 * Template files are stored in the "include" folder at the root of this
 * project.  Files that contain the strings "Xxxxx" or "xxxxx" will have the string
 * replaced with the capitalized or uncapitalized form of the users project name.  If the file name
 * is preceeded with the string 
 * 
 * "FORM" - user selected to create submission forms
 * "RES" - user selected software or metedata resource
 * "PIP" - user selected pipelines
 * "VIZ" - user selected visualization
 * 
 * then the text will be removed from the file name and the file will be included.
 * 
 * Additional placeholders for content within the files include "___pluginname___" (uncapitalized 
 * plugin name), "___PLUGINNAME___" (capitalized plugin name), "___Pluginname___" (first letter capitalized plugin name), 
 * and "___package___" (full java package name).  
 * 
 * A special comment using '/////' or '#////' (depending on file type)  at the end of a line preceding the FORM, RES, PIP, 
 * and VIZ strings will include that line based on the user's selection. 
 * 
 * Can also include blocks of code between lines starting with '////>' or '#///>' (preceding the FORM, RES, PIP, 
 * and VIZ strings) and ending with '////<' or '#///<'
 * 
 * @author calder
 *
 */
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
			IPath location, boolean web, boolean resource, boolean pipeline, boolean viz) throws CoreException {
		Assert.isNotNull(name);
		Assert.isTrue(name.trim().length() > 0);

		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IWorkspaceRoot root = workspace.getRoot();

		IProject project = root.getProject(name);

		try {
			// create project and configure maven
			configureMaven(name, namespace, project, location, web, resource, pipeline, viz);
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

	public static void copyAndRewriteFiles(String name, String namespace, String description, IPath location, IProject project, IPath projectRoot,
			boolean web, boolean resource, boolean pipeline, boolean viz) {
		Bundle bundle = Platform.getBundle("wasp-eclipse-plugin");
		Enumeration<URL> files = bundle.findEntries("include", "*", true);
		String ns = namespace.replaceAll("\\.", "/").toLowerCase();
		String cname = name.toLowerCase().substring(0, 1).toUpperCase() + name.toLowerCase().substring(1);
		String lname = name.toLowerCase();

		while (files.hasMoreElements()) {
			URL f = files.nextElement();
			String opath = f.getFile().replaceFirst("/", "");
			
			// must be a file (must contain a period)
			if (!opath.contains("."))
				continue;

			IPath path = new Path(opath);
			String dpath = opath;
			
			// copy optional files
			
			String[] types = { "FORM", "RES", "PIP", "VIZ" };
			List<String> seen = new ArrayList<String>();
			
			for (String type : types) {
				if (opath.contains(type)) {
					seen.add(type);
					dpath = dpath.replaceAll(type, "");
				}
			}
			
			boolean keep = false;
			if (seen.size() == 0)
				keep = true;
			if (web && seen.contains("FORM"))
				keep = true;
			if (resource && seen.contains("RES"))
				keep = true;
			if (pipeline && seen.contains("PIP"))
				keep = true;
			if (viz && seen.contains("VIZ"))
				keep = true;
			
			if (keep == false)
				continue;
			
			dpath = dpath.replaceFirst("include/", "");
			dpath = dpath.replaceFirst("src/main/java/", "src/main/java/" + ns + "/" + lname + "/");
			dpath = dpath.replaceFirst("src/test/java/", "src/test/java/" + ns + "/" + lname + "/");
			dpath = dpath.replaceAll("Xxxxx", cname);
			dpath = dpath.replaceAll("xxxxx", lname);

			IPath dest = new Path(dpath);

			IFile file = project.getFile(dest);
			
			System.out.println("copying " + opath + ":"+ dpath);

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

			rewrite(new File(projectRoot + File.separator + lname + File.separator + dest.toFile().getPath()), lname, namespace, description,
					web, resource, pipeline, viz);

		}

	}

	private static void rewrite(File file, String name, String pkg, String description,
			boolean web, boolean resource, boolean pipeline, boolean viz) {
		
		String cAllName = name.toUpperCase();
		String cName = name.toLowerCase().substring(0, 1).toUpperCase() + name.toLowerCase().substring(1);

		List<String> lines = new ArrayList<String>();

		try {
			boolean inRemovableBlock = false;
			boolean keepBlock = true;
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line = in.readLine();
			while (line != null) {
				
				line = line.replaceAll("___pluginname___", name);
				line = line.replaceAll("___Pluginname___", cName);
				line = line.replaceAll("___PLUGINNAME___", cAllName);
				line = line.replaceAll("___package___", pkg + "." + name.toLowerCase());
				line = line.replaceAll("___description___", description);
				
				// marked for possible removal
				if (line.contains("////") || line.contains("#///") || inRemovableBlock) {
					boolean keep = false;
					if (line.contains("////<") || line.contains("#///<")){
						inRemovableBlock = false;
					} else if (inRemovableBlock){
						keep = keepBlock;
					} else {
						int pos = -1;
						if (line.contains("#///"))
							pos = line.indexOf("#///"); // must check this variant first
						else 
							pos = line.indexOf("////");
						String rem = line.substring(pos+5);
						if (web && rem.contains("FORM"))
							keep = true;
						if (resource && rem.contains("RES"))
							keep = true;
						if (pipeline && rem.contains("PIP"))
							keep = true;
						if (viz && rem.contains("VIZ"))
							keep = true;
						if (line.contains("////>") || line.contains("#///>")){
							inRemovableBlock = true;
							keepBlock = keep;
							keep = false; // don't keep the line with the special comment
						} else {
							line = line.substring(0, pos-1); // trim off the special comment
						}
					}
					if (!keep) {
						line = in.readLine();
						continue;
					}
				}
				
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
			IProject project, IPath location, boolean web, boolean resource, boolean pipeline, boolean viz) throws CoreException {
		
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
		parent.setArtifactId("plugins");
		parent.setVersion(version);
		model.setParent(parent);

		// properties

		Properties props = new Properties();
		props.put("wasp.version", "${project.parent.version}");
		model.setProperties(props);

		// dependency management

		// DependencyManagement dm = new DependencyManagement();
		// Dependency bom = new Dependency();
		// bom.setGroupId("edu.yu.einstein.wasp");
		// bom.setArtifactId("bom");
		// bom.setVersion("${wasp.version}");
		// bom.setType("pom");
		// bom.setScope("import");
		// dm.getDependencies().add(bom);
		// model.setDependencyManagement(dm);

		// dependencies

		List<Dependency> deps = new ArrayList<Dependency>();
		Dependency wasp = new Dependency();
		wasp.setGroupId("edu.yu.einstein.wasp");
		if (web || resource || viz) {
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
		if (web || resource || viz) {
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
		
		// put all folders to be created here...
		
		String javaMainPackage = "src/main/java/" + ns + "/" + name.toLowerCase();
		String javaTestPackage = "src/test/java/" + ns + "/" + name.toLowerCase();
		String javaRes = "src/main/resources";
		Set<String> folderSet = new LinkedHashSet<String>();
		folderSet.add("src/main/java");
		folderSet.add("src/test/java");
		folderSet.add("src/test/resources");
		folderSet.add("target/classes");
		folderSet.add("target/test-classes");
		folderSet.add(javaRes + "/i18n/en_US");
		folderSet.add(javaRes + "/wasp");
		folderSet.add(javaRes + "/META-INF/spring");
		folderSet.add(javaRes + "/images/" + name.toLowerCase());
		folderSet.add(javaMainPackage + "/exception");
		folderSet.add(javaMainPackage + "/service/impl");
		folderSet.add(javaMainPackage + "/plugin");
		if(resource || pipeline){
			folderSet.add(javaMainPackage + "/integration/messages");
			folderSet.add(javaMainPackage + "/integration/endpoints");
			folderSet.add(javaRes + "/flows");
			folderSet.add(javaMainPackage + "/batch/tasklet");
			folderSet.add(javaTestPackage + "/integration/messages/test");
			if (resource){
				folderSet.add(javaMainPackage + "/software");
			}
		}
		
		if (web || viz || resource){
			folderSet.add(javaMainPackage + "/controller");
			folderSet.add(javaRes + "/META-INF/tiles");
			folderSet.add(javaRes + "/css/" + name.toLowerCase());
			folderSet.add(javaRes + "/scripts/" + name.toLowerCase());
			folderSet.add("src/main/webapp/WEB-INF/jsp/" + name.toLowerCase());
		}
		
		IProjectConfigurationManager mavenConfig = MavenPlugin.getProjectConfigurationManager();
		String[] folderArray = new String[folderSet.size()];
		int i = 0;
		for (String folder: folderSet)
			folderArray[i++] = folder;

		mavenConfig.createSimpleProject(project, location, model, folderArray, config, new NullProgressMonitor());
		

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
