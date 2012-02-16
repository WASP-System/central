package edu.yu.einstein.wasp.tiles;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.ServletContextAware;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.context.support.ServletContextResourcePatternResolver;
import org.springframework.web.context.support.ServletContextResource;

import org.springframework.core.io.Resource;

// based from http://stackoverflow.com/questions/5013917/can-i-serve-jsps-from-inside-a-jar-in-lib-or-is-there-a-workaround

/**
 * Allows extraction of contents of a JAR file. All files matching a given Ant path pattern will be extracted into a
 * specified path.
 */
public class WaspJspPluginSupport implements ServletContextAware {

	private String resourceDirectory;
	private String resourceFilePattern;
	private ServletContext servletContext;
	private AntPathMatcher pathMatcher = new AntPathMatcher();

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
	private WaspJspPluginSupport(String resourceDirectory, String resourceFilePattern) {
		this.resourceDirectory = resourceDirectory;
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

		Enumeration<java.net.URL>resources = this.getClass().getClassLoader().getResources(resourceDirectory);
		while (resources.hasMoreElements()) {
			java.net.URL foundMatch = resources.nextElement(); 

			try {
				java.net.JarURLConnection conn = (java.net.JarURLConnection)foundMatch.openConnection();
				JarFile jarFile = conn.getJarFile();
	
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
				throw new FileNotFoundException("Cannot find jar file in libs: " + foundMatch);
			} catch (IOException e) {
				throw new IOException("IOException while moving resources.", e);
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

