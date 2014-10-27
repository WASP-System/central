/**
 * 
 */
package edu.yu.einstein.wasp.plugin.genomemetadata;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;

/**
 * @author calder
 * 
 */
public class GenomeIndexStatusKey {

	private String nullString = "<<<NO VERSION STRING>>>";

	protected Triple<Build, String, Pair<GenomeIndexType, String>> key;

	public GenomeIndexStatusKey(Build build, GridWorkService workService, GenomeIndexType type, String versionString) {
		key = new ImmutableTriple<Build, String, Pair<GenomeIndexType, String>>(build, workService.getTransportConnection().getHostName(),
				new ImmutablePair<GenomeIndexType, String>(type, versionString));
	}

	@Override
	public int hashCode() {
		String version = nullString;
		if (key.getRight().getRight() != null)
			version = key.getRight().getRight();
		return new HashCodeBuilder()
			.append(key.getLeft().getGenomeBuildNameString())
			.append(key.getMiddle())
			.append(key.getRight().getLeft().toString())
			.append(version)
			.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof GenomeIndexStatusKey) {
			String version = nullString;
			String otherVersion = nullString;
			final GenomeIndexStatusKey other = (GenomeIndexStatusKey) obj;

			if (key.getRight().getRight() != null)
				version = key.getRight().getRight();
			if (other.key.getRight().getRight() != null)
				otherVersion = key.getRight().getRight();

			return new EqualsBuilder()
				.append(key.getLeft().getGenomeBuildNameString(), other.key.getLeft().getGenomeBuildNameString())
				.append(key.getMiddle(), other.key.getMiddle())
				.append(key.getRight().getLeft().toString(), other.key.getRight().getLeft().toString())
				.append(version, otherVersion)
				.isEquals();
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return key.getLeft().getGenomeBuildNameString() + ":" + key.getMiddle() + ":" + key.getRight().getLeft().toString() + ":" + key.getRight().getRight();
	}
	
	public Build getBuild() {
		return key.getLeft();
	}
	
	public String getHostname() {
		return key.getMiddle();
	}
	
	public GenomeIndexType getType() {
		return key.getRight().getLeft();
	}
	
	/**
	 * if version is 1:1 with build, this value will be null.
	 * @return
	 */
	public String getVersion() {
		return key.getRight().getRight();
	}

}
