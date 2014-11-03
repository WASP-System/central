/**
 * 
 */
package edu.yu.einstein.wasp.plugins.star;

import java.io.Serializable;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexConfiguration;
import edu.yu.einstein.wasp.plugin.genomemetadata.service.GenomeMetadataService;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.GenomeService;

/**
 * @author calder
 *
 */
public class StarGenomeIndexConfiguration extends GenomeIndexConfiguration<String,String> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6471130654232884188L;
	
	public static final String STAR_GENOME_INDEX_CONFIGURATION_KEY = "starConfig";

	public static final String SJDB_OVERHANG_KEY = "sjdbOverhang";
	public static final String DIRECTORY = "starGenomeDirectory";
	public static final String SECOND_PASS_KEY = "starSecondPass";
	public static final String JUNCTIONS_DATABASE_KEY = "junctionsDatabase";
	
	@Autowired
	private GenomeService genomeService;
	
	@Autowired
	private GenomeMetadataService genomeMetadataService;
	
	public StarGenomeIndexConfiguration() {
		// for json marshalling
	}
	
	/**
	 * @param build Genome build object
	 * @param gtfVersion String name of GTF version
	 * @param sjdbOverhang overhang length
	 * @param isSecond If this is the second call of a two pass alignment, junctions output from the previous alignment will be used to generate the new database, otherwise GTF
	 * @param directory directory where genome will be built
	 * @param pathToJunctions path to GTF file or if isSecond=true, path and unique prefix of files that will be concatenated and sorted to make a second pass index
	 */
	public StarGenomeIndexConfiguration(Build build, String gtfVersion, Integer sjdbOverhang, boolean isSecond) {
		setBuild(build);
		setGtfVersion(gtfVersion);
		setSjdbOverhang(sjdbOverhang);
		setSecond(isSecond);
	}

	public void setGtfVersion(String version) {
		this.put(GenomeMetadataService.GTF_VERSION_KEY, version);
	}
	
	public String getGtfVersion() {
		return this.get(GenomeMetadataService.GTF_VERSION_KEY);
	}
	
	public void setSjdbOverhang(Integer sjdbOverhang) {
		this.put(SJDB_OVERHANG_KEY, sjdbOverhang.toString());
	}
	
	public Integer getSjdbOverhang() {
		return Integer.parseInt(this.get(SJDB_OVERHANG_KEY));
	}
	
	public void setSecond(boolean isSecond) {
		this.put(SECOND_PASS_KEY, String.valueOf(isSecond));
	}
	
	public boolean isSecond() {
		return Boolean.parseBoolean(this.get(SECOND_PASS_KEY));
	}
	
	
	public void setBuild(Build build) {
		this.put(GenomeService.ORGANISM_KEY, build.getGenome().getOrganism().getNcbiID().toString());
		this.put(GenomeService.GENOME_KEY, build.getGenome().getName());
		this.put(GenomeService.BUILD_KEY, build.getName());
	}
	
	public Integer getOrganism() {
		return new Integer(this.get(GenomeService.ORGANISM_KEY));
	}
	
	public String getGenome() {
		return this.get(GenomeService.GENOME_KEY);
	}
	
	public String getBuild() {
		return this.get(GenomeService.BUILD_KEY);
	}
	
	public void setDirectory(String directory) {
		this.put(DIRECTORY, directory);
	}
	
	public String getDirectory() {
		return this.get(DIRECTORY);
	}
	
	public void setPathToJunctions(String pathToJunctions) {
		this.put(JUNCTIONS_DATABASE_KEY, pathToJunctions);
	}
	
	public String getPathToJunctions() {
		return this.get(JUNCTIONS_DATABASE_KEY);
	}
	
	@Override
	public String generateUniqueKey() {
		String params = "";
		TreeSet<String> ss = new TreeSet<String>();
		ss.add(GenomeMetadataService.GTF_VERSION_KEY);
		ss.add(SJDB_OVERHANG_KEY);
		for (String x : ss) {
			if (this.containsKey(x)) {
				params += x + "-" + this.get(x) + "__";
			} else {
				params += x + "-" + "none__";
			}
		}
		params = params.replaceFirst("__$", "");
		//return getDirectory() + ":" + params;
		return params;
	}

}
