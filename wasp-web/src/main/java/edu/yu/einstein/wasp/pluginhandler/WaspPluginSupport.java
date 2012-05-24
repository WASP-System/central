package edu.yu.einstein.wasp.pluginhandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.ServletContextAware;


// based from http://stackoverflow.com/questions/5013917/can-i-serve-jsps-from-inside-a-jar-in-lib-or-is-there-a-workaround

/**
 * Allows extraction of contents of a JAR file. All files matching a given Ant path pattern will be extracted into a
 * specified path.
 */
public class WaspPluginSupport implements ServletContextAware {

	private List<String> resourceDirectoryList;
	private String resourceFilePattern;
	private ServletContext servletContext;
	private AntPathMatcher pathMatcher = new AntPathMatcher();
	private String resourceUrlFilter;

	/**
	 * Creates a new instance of the JarFileResourcesExtractor
	 * 
	 * @param resourcePathPattern
	 *			The Ant style path pattern (supports wildcards) of the resources files to extract
	 * @param jarFile
	 *			The jar file (located inside WEB-INF/lib) to search for resources
	 * @param destination
	 *			Target folder of the extracted resources. Relative to the context.
	 */
	private WaspPluginSupport(String resourceDirectory, String resourceUrlFilter, String resourceFilePattern) {
		this.resourceDirectoryList = new ArrayList<String>();
		this.resourceDirectoryList.add(resourceDirectory);
		this.resourceUrlFilter = resourceUrlFilter;
		this.resourceFilePattern = resourceFilePattern;
	}
	
	private WaspPluginSupport(List<String> resourceDirectory, String resourceUrlFilter, String resourceFilePattern) {
		this.resourceDirectoryList = resourceDirectory;
		this.resourceUrlFilter = resourceUrlFilter;
		this.resourceFilePattern = resourceFilePattern;
	}

	/** 
	 * Extracts the resource files found in the specified jar file into the destination path
	 * 
	 * @throws IOException
	 *			 If an IO error occurs when reading the jar file
	 * @throws FileNotFoundException
	 *			 If the jar file cannot be found
	 */
	@PostConstruct
	public void extractFiles() throws IOException {
		// get list of applicable jars
		Map<String,List<URL>> filteredResourceUrls = new HashMap<String,List<URL>>();
		System.out.println("WaspPluginSupport: Resource URL filter: "+resourceUrlFilter);
		for (String resourceDirectory: this.resourceDirectoryList){
			Enumeration<URL> resources = this.getClass().getClassLoader().getResources(resourceDirectory);
			while (resources.hasMoreElements()) {
				URL url = resources.nextElement(); 
				if (!url.getPath().contains(resourceUrlFilter))
					break; // we're looking in a jar that is not a waspPlugin jar
				if (!filteredResourceUrls.containsKey(resourceDirectory))
					filteredResourceUrls.put(resourceDirectory, new ArrayList<URL>());
				filteredResourceUrls.get(resourceDirectory).add(url);
			}
		}
		if (filteredResourceUrls.isEmpty()){
			System.out.println("WaspPluginSupport: No WASP plugin jars contained files in any resource directory provided");
			return;
		}
		for (String resourceDirectory: filteredResourceUrls.keySet()){
			for (URL resourceUrl: filteredResourceUrls.get(resourceDirectory)){
				try {
					JarURLConnection conn = (JarURLConnection)resourceUrl.openConnection();
					JarFile jarFile = conn.getJarFile();
					System.out.println("WaspPluginSupport: Extracting resources from "+resourceUrl.getPath());
					Enumeration<JarEntry> entries = jarFile.entries();
					while (entries.hasMoreElements()) {
						JarEntry entry = entries.nextElement();
						if (pathMatcher.match( resourceDirectory + "/" + resourceFilePattern, entry.getName())) {
							String fileName = entry.getName().replaceFirst(".*\\/", "");
							File destinationFolder = new File(servletContext.getRealPath(resourceDirectory));
							InputStream inputStream = jarFile.getInputStream(entry);
							File materializedJsp = new File(destinationFolder, fileName);
							FileOutputStream outputStream = new FileOutputStream(materializedJsp);
							copyAndClose(inputStream, outputStream);
						}
					}
				} catch (MalformedURLException e) {
					throw new FileNotFoundException("Cannot process '"+resourceUrl.getPath()+"': "+e.getMessage());
				} catch (IOException e) {
					throw new IOException("IOException while moving resources: "+e.getMessage());
				}
			}
		}



/*
		try {
			String path = servletContext.getRealPath("/WEB-INF/lib/" + jarFile);
			JarFile jarFile = new JarFile(path);

			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (pathMatcher.match(resourcePathPattern, entry.getName())) {
					String fileName = entry.getName().replaceFirst(".*\\/", "");
					File destinationFolder = new File(servletContext.getRealPath(destination));
					InputStream inputStream = jarFile.getInputStream(entry);
					File materializedJsp = new File(destinationFolder, fileName);
					FileOutputStream outputStream = new FileOutputStream(materializedJsp);
					copyAndClose(inputStream, outputStream);
				}
			}

		}
		catch (MalformedURLException e) {
			throw new FileNotFoundException("Cannot find jar file in libs: " + jarFile);
		}
		catch (IOException e) {
			throw new IOException("IOException while moving resources.", e);
		}
*/
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public static int IO_BUFFER_SIZE = 8192;

	private static void copyAndClose(InputStream in, OutputStream out) throws IOException {
		try {
			byte[] b = new byte[IO_BUFFER_SIZE];
			int read;
			while ((read = in.read(b)) != -1) {
				out.write(b, 0, read);
			}
		} finally {
			in.close();
			out.close();
		}
	}
}

