/**
 * 
 */
package edu.yu.einstein.wasp.grid.file;

import java.net.MalformedURLException;
import java.net.URL;

import edu.yu.einstein.wasp.exception.LoginNameException;
import edu.yu.einstein.wasp.model.File;
import edu.yu.einstein.wasp.model.User;

/**
 * @author calder
 *
 */
public class DummyFileUrlResolver implements FileUrlResolver {
	
	private String url;
	
	public DummyFileUrlResolver(String url) {
		setUrl(url);
	}

	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.grid.file.FileUrlResolver#getURL(edu.yu.einstein.wasp.model.File, edu.yu.einstein.wasp.model.User)
	 */
	@Override
	public URL getURL(File file, User user) throws SecurityException, LoginNameException {
		try {
			return new URL(getUrl());
		} catch (MalformedURLException e) {
			return null;
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
