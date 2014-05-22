package edu.yu.einstein.wasp.classloader;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.loader.WebappClassLoader;
import org.apache.catalina.startup.Bootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WaspTomcatClassLoader
 *
 * Scans a BASEDIR directory for jars and adds into the
 * webappclassloader.
 * 
 *
 * BASEDIR is set to ${catalina.home}/waspPlugins
 * 
 * this will NOT scan recursively
 *
 */

public class WaspTomcatClassLoader extends WebappClassLoader {
	
	final String BASEDIR = "waspPlugins";
	final String JAREXTENSION = "jar";
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public WaspTomcatClassLoader(ClassLoader parent) {
		super(parent);
	}

	public void start() throws LifecycleException {
		logger.debug("WaspClassLoader.start()");
		addCustomRepositoryOrLocation(Bootstrap.getCatalinaHome() + "/" + BASEDIR);
		super.start();
	}

	private void addCustomRepositoryOrLocation(String directory) throws LifecycleException {

		File dir = new File(directory);

		// filter to only allow for .jar files
		FilenameFilter filter = new FilenameFilter() {  
			public boolean accept(File dir, String name) {  
				return name.endsWith("." + JAREXTENSION);  
			}  
		};  

		String[] children = dir.list(filter);  
		if (children == null) {  
			// Either dir does not exist or is not a directory  

			// throw exception
			logger.error("Wasp Loader Could not find directory " + directory);

			throw new LifecycleException(directory + " not found");
		} 

		for (int n=0; n<children.length; n++) {  
			// Get filename of file or directory  
			String filename = children[n];  

			try {
				URL jarUrl = new File(directory + "/" + filename).toURI().toURL();
				logger.info("** WaspClassLoader.adding plugin url " + jarUrl);
				addURL(jarUrl);

			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		}  

		/** chance there to recurse through directory, but needs to tag seen jars

		 ** BELOW UNTESTED
			FilenameFilter dirFilter = new FileFilter() {  
				public boolean accept(File file) {
					return file.isDirectory(); 
				}  
			};  
			String[] childrenDirs = dir.list(dirFilter);  
			if (childrenDirs != null) {
				for (int n=0; n<childDirs.length; n++) {  
					addCustomRepositoryOrLocation(childDirs[n]);
				}			
			}

		**/
	}


}
