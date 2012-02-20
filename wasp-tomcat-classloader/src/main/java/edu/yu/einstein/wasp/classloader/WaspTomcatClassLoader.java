package edu.yu.einstein.wasp.classloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.loader.WebappClassLoader;

import org.apache.tools.ant.DirectoryScanner;

/**
 * WaspTomcatClassLoader
 *
 * Scans a BASEDIR directory for jars and adds into the
 * webappclassloader
 *
 * BASEDIR is set to ${catalina.base}/waspPlugins
 *
 */

public class WaspTomcatClassLoader extends WebappClassLoader {
	
	final String BASEDIR = "waspPlugins";

	public WaspTomcatClassLoader(ClassLoader parent) {
		super(parent);
	}

	public void start() throws LifecycleException {
		System.out.println("WaspClassLoader.start()");
		addCustomRepositoryOrLocation();
		super.start();
	}

	private void addCustomRepositoryOrLocation() {

		// finds all jars in within the directory tree
		DirectoryScanner scanner = new DirectoryScanner();
		scanner.setIncludes(new String[]{"**/*.jar"});
		scanner.setBasedir(BASEDIR);
		scanner.setCaseSensitive(false);
		scanner.scan();
		String[] stringURLs = scanner.getIncludedFiles();


		URL[] urls = new URL[stringURLs.length];

		for (int n = 0; n < stringURLs.length; n++) {

			try {
				urls[n] = new File(BASEDIR + "/" + stringURLs[n]).toURL();
				System.out.println("WaspClassLoader.adding plugin url " + urls[n]);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

			// this will tell wasp to look for the jars at the new locations also.

			addURL(urls[n]);
		}

	}


}
