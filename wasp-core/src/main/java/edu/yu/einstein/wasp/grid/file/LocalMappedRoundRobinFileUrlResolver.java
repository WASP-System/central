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
import java.util.List;
import java.util.Set;
import java.util.Map;
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
		return getURL(file, "");
	}
	
	public URL getURL(FileHandle file, String subPath) throws GridUnresolvableHostException {
		if (! file.getFileURI().toString().startsWith("file://")) {
			logger.warn("unable to obtain file URL for file " + file.getId());
			throw new GridUnresolvableHostException();
		}
		
		//currently only handles file:// URLs.
		Matcher hostm = Pattern.compile("^file://(.*?)/").matcher(file.getFileURI().toString());
		
		if (! hostm.find()) {
			logger.warn("unable to parse file URL: " + file.getFileURI().toString());
			throw new GridUnresolvableHostException();
		}
		
		String host = hostm.group(1);
		
		if (!hostMap.containsKey(host)) {
			logger.warn("Host: " + host + " has not been mapped");
			throw new GridUnresolvableHostException();
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
		
		String file_uri = destination + subPath + "/" + file.getUUID().toString();
		try {
			URI uri = new URI(file_uri);
			retval = uri.normalize().toURL();
		} catch (URISyntaxException e) {
			logger.warn("unable to coerce " + file_uri + " to URI");
			throw new GridUnresolvableHostException();
		} catch (MalformedURLException e) {
			logger.warn("unable to coerce " + file_uri + " to URL");
			throw new GridUnresolvableHostException();
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
		return null;
	}

}
