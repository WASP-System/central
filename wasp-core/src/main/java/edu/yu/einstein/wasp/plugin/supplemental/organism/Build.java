/**
 * 
 */
package edu.yu.einstein.wasp.plugin.supplemental.organism;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

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
	private boolean isDefaultBuild;
	private URL site;
	private URL url;
	private Date buildDate;
	private Map<String,String> metadata = new HashMap<String,String>();
	
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
	 * @return the default status
	 */
	public boolean isDefault() {
		return isDefaultBuild;
	}
	/**
	 * @param isDefaultBuild
	 */
	public void setDefault(boolean isDefaultBuild) {
		this.isDefaultBuild = isDefaultBuild;
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

	/**
	 * @return the buildDate
	 */
	public Date getBuildDate() {
		return buildDate;
	}

	/**
	 * @param buildDate the buildDate to set
	 */
	public void setBuildDate(Date buildDate) {
		this.buildDate = buildDate;
	}
	
	/**
	 * 
	 * Set metadata from site-specific genome preferences file
	 * 
	 * overwrites if key already exists.
	 * 
	 * @param key
	 * @param value
	 */
	public void addMetadata(String key, String value) {
	    metadata.put(key, value);
	}
	
	/**
	 * Get Key-Value metadata encoded in site specific genome preferences file.
	 * 
	 * returns null if DNE.
	 * 
	 * @param key
	 * @return
	 */
	public String getMetadata(String key) {
	    return metadata.get(key);
	}
	
	public boolean hasMetadata(String key) {
		return metadata.containsKey(key);
	}
	
	public void setDate(String date) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		this.buildDate = df.parse(date);
	}

	@Override
	public int compareTo(Build b) {
		return new CompareToBuilder()
	       .append(b.buildDate, this.buildDate)
	       .append(this.version, b.version)
	       .toComparison();

	}
	
	public String getGenomeBuildNameString() {
		return getGenome().getName() + "." + getName(); 
	}
	
	/**
	 * Find all metadata key value pairs based on their prefix.  e.g. gtf.file would return the key and value of
	 * gtf.file.ensembl_v75, etc.
	 * 
	 * @param string
	 * @return
	 */
	public List<Pair<String,String>> getMetadataStartingWith(String string) {
		List<Pair<String,String>> retval = new ArrayList<Pair<String,String>>();
		for (String key : metadata.keySet()) {
			if (key.startsWith(string))
				retval.add(new ImmutablePair<String,String>(key, metadata.get(key)));
		}
		return retval;
	}
	
	public Set<String> getMetadataKeySet() {
		return metadata.keySet();
	}

}
