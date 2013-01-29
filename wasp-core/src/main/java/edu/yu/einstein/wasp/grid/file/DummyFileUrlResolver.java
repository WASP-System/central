/**
 * 
 */
package edu.yu.einstein.wasp.grid.file;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.model.File;

/**
 * @author calder
 *
 */
public class DummyFileUrlResolver implements FileUrlResolver {
	
	private String url = "";
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public DummyFileUrlResolver(String url) {
		setUrl(url);
	}

	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.grid.file.FileUrlResolver#getURL(edu.yu.einstein.wasp.model.File, edu.yu.einstein.wasp.model.User)
	 */
	@Override
	public URL getURL(File file) throws GridUnresolvableHostException {
		try {
			return new URL(getUrl());
		} catch (MalformedURLException e) {
			logger.warn("bad URL: " + url);
			throw new GridUnresolvableHostException();
		}
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

}
