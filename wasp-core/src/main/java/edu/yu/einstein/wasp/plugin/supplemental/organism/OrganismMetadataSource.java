/**
 * 
 */
package edu.yu.einstein.wasp.plugin.supplemental.organism;

import java.util.HashMap;
import java.util.Map;

/**
 * @author calder
 *
 */
public class OrganismMetadataSource extends HashMap<String, Object> {

	/**
	 * 
	 */
	public OrganismMetadataSource() {
		// TODO Auto-generated constructor stub
	}
	
	public void addFiles(String files) {
		this.put("files", files.split(","));
	}
	
	public String[] getFiles() {
		return (String[]) this.get("files");
	}
	
	public void addFilename(String filename) {
		this.put("filename", filename);
	}
	
	public String getFilename() {
		return (String) this.get("filename");
	}
	
	public void addCheksum(String checksum) {
		this.put("checksum", checksum);
	}
	
	public String getChecksum() {
		return (String) this.get("checksum");
	}

}
