/**
 * 
 */
package edu.yu.einstein.wasp.grid.file;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;

/**
 * Implements a FileUrlResolver where a map of lists can be supplied where the key is  
 * to obtain the file.
 * 
 * for example:
 * 
 * key: host1.example.org values: http://file1.example.org:8080/file/get/, http://file2.example.org:8080/file/get/
 * key: host2.example.org values: http://file1.example.org:8080/file/get/
 * 
 * in this example file requests for host1 would be dispatched round-robin to file1 and file2
 * host2 requests would go to file1.
 * 
 * Currently only handles file:// URL, does not participate in URN resolution.
 * 
 * @author calder
 *
 */
public class LocalMappedRoundRobinFileUrlResolver implements FileUrlResolver {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());


	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.grid.file.FileUrlResolver#getURL(edu.yu.einstein.wasp.model.FileHandle)
	 */
	@Override
	public URL getURL(FileHandle file) throws GridUnresolvableHostException {
		return getURL(file, "file");
	}
	
	public URL getURL(FileHandle file, String subPath) throws GridUnresolvableHostException {
		if (! file.getFileURI().toString().startsWith("file://")) {
			String message = "unable to obtain file URL for file " + file.getId();
			logger.warn(message);
			throw new GridUnresolvableHostException(message);
		}
		
		//currently only handles file:// URLs.
		Matcher hostm = Pattern.compile("^file://(.*?)/").matcher(file.getFileURI().toString());
		
		if (! hostm.find()) {
			logger.warn("unable to parse file URL: " + file.getFileURI().toString());
			throw new GridUnresolvableHostException();
		}
		
		String host = hostm.group(1);
		
		if (!hostMap.containsKey(host)) {
			String message = "Host: " + host + " has not been mapped";
			logger.warn(message);
			throw new GridUnresolvableHostException(message);
		}
		
		List<String> l = hostMap.get(host);
		
		String destination = (String) l.get(0);
		if (l.size() > 1) {
			Collections.rotate(l, 1);
			hostMap.put(host, l);
		}
		
		if (!destination.endsWith("/"))
			destination += "/";
		
		if (!subPath.endsWith("/"))
			subPath += "/";
		
		URL retval;
		
		String file_uri = destination + subPath + file.getUUID().toString() + "/" + file.getFileName();
		try {
			URI uri = new URI(file_uri);
			retval = uri.normalize().toURL();
		} catch (URISyntaxException e) {
			String message = "unable to coerce " + file_uri + " to URI";
			logger.warn(message);
			throw new GridUnresolvableHostException(message);
		} catch (MalformedURLException e) {
			String message = "unable to coerce " + file_uri + " to URL";
			logger.warn(message);
			throw new GridUnresolvableHostException(message);
		} finally {
			
		}
		
		return retval;
	}
	
	private HashMap<String,List<String>> hostMap;
	
	public LocalMappedRoundRobinFileUrlResolver(Map<String,List<String>> hostMap) {
		this.hostMap = new HashMap<String,List<String>>(hostMap);
	}

	@Override
	public URL getURL(FileGroup group) throws GridUnresolvableHostException {
		Set<FileHandle> fileHandles = group.getFileHandles();
		if (fileHandles.size() == 1) {
			return getURL(fileHandles.iterator().next());
		}
		// TODO: Implement concatenation and zipping up of multi-handle file groups
		return getURL(group, "filegroup");
	}

	public URL getURL(FileGroup group, String subPath) throws GridUnresolvableHostException {
		Set<FileHandle> fileHandles = group.getFileHandles();
		if (fileHandles.size() == 0) {
			logger.warn("File group " + group.getDescription() + " is empty!");
			throw new GridUnresolvableHostException();
		}
		else if (fileHandles.size() == 1) {
			return getURL(fileHandles.iterator().next());
		}
		
		// Get the first file in group to decide which host to resolve
		Iterator<FileHandle> it = fileHandles.iterator();
		FileHandle file = it.next();
		
		if (! file.getFileURI().toString().startsWith("file://")) {
			String message = "unable to obtain file URL for file " + file.getId();
			logger.warn("unable to obtain file URL for file " + file.getId());
			throw new GridUnresolvableHostException("unable to obtain file URL for file " + file.getId());
		}
		
		//currently only handles file:// URLs.
		Matcher hostm = Pattern.compile("^file://(.*?)/").matcher(file.getFileURI().toString());
		
		if (! hostm.find()) {
			String message = "unable to parse file URL: " + file.getFileURI().toString();
			logger.warn(message);
			throw new GridUnresolvableHostException(message);
		}
		
		String host = hostm.group(1);
		
		if (!hostMap.containsKey(host)) {
			String message = "Host: " + host + " has not been mapped";
			logger.warn(message);
			throw new GridUnresolvableHostException(message);
		}
		
		// verify the rest files in the group are from same host
		while (it.hasNext()) {
			file = it.next();
			if (! file.getFileURI().toString().startsWith("file://")) {
				String message = "unable to obtain file URL for file " + file.getId();
				logger.warn(message);
				throw new GridUnresolvableHostException(message);
			}
			
			//currently only handles file:// URLs.
			Matcher hostm2 = Pattern.compile("^file://(.*?)/").matcher(file.getFileURI().toString());
			
			if (! hostm2.find()) {
				String message = "unable to parse file URL: " + file.getFileURI().toString();
				logger.warn(message);
				throw new GridUnresolvableHostException(message);
			}
			
			String host2 = hostm2.group(1);
			
			if (!hostMap.containsKey(host2)) {
				String message = "Host: " + host2 + " has not been mapped";
				logger.warn(message);
				throw new GridUnresolvableHostException(message);
			} else if (host2.compareTo(host)!=0) {
				// throw exception if the files in the group not from same host
				String message = "File group contains files from multiple host: '" + host + "', '" + host2 + "' and might be more.";
				logger.warn(message);
				throw new GridUnresolvableHostException(message);
			}
		}
		
		List<String> l = hostMap.get(host);
		
		String destination = (String) l.get(0);
		if (l.size() > 1) {
			Collections.rotate(l, 1);
			hostMap.put(host, l);
		}
		
		if (!destination.endsWith("/"))
			destination += "/";
		
		if (!subPath.endsWith("/"))
			subPath += "/";
		
		URL retval;
		
		String group_uri = destination + subPath + group.getUUID().toString() + "/" + group.getDescription().replaceAll("\\W", "") + ".zip";
		try {
			URI uri = new URI(group_uri);
			retval = uri.normalize().toURL();
		} catch (URISyntaxException e) {
			String message = "unable to coerce " + group_uri + " to URI";
			logger.warn(message);
			throw new GridUnresolvableHostException(message);
		} catch (MalformedURLException e) {
			String message = "unable to coerce " + group_uri + " to URL";
			logger.warn(message);
			throw new GridUnresolvableHostException(message);
		} finally {
			
		}
		
		return retval;
	}
}
