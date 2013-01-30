/**
 * 
 */
package edu.yu.einstein.wasp.plugin.supplemental.organism;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author calder
 *
 */
public class Build implements Comparable<Build> {
	
	private Genome genome;
	private String name;
	private String version;
	private String versionstring;
	private String description;
	private URL site;
	private URL url;
	private Map<String,OrganismMetadataSource> metadata = new HashMap<String,OrganismMetadataSource>();
	
	public Build(String name) {
		this.name = name;
	}
	
	/**
	 * @return the genome
	 */
	public Genome getGenome() {
		return genome;
	}
	/**
	 * @param genome the genome to set
	 */
	public void setGenome(Genome genome) {
		this.genome = genome;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @return the versionstring
	 */
	public String getVersionstring() {
		return versionstring;
	}
	/**
	 * @param versionstring the versionstring to set
	 */
	public void setVersionstring(String versionstring) {
		this.versionstring = versionstring;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the site
	 */
	public URL getSite() {
		return site;
	}
	/**
	 * @param site the site to set
	 */
	public void setSite(String site) {
		try {
			this.site = new URL(site);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the url
	 */
	public URL getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		try {
			this.url = new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int compareTo(Build b) {
		return this.version.compareTo(b.getVersion());
	}

}
