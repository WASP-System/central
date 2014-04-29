package edu.yu.einstein.wasp.gatk.fileformat;

import edu.yu.einstein.wasp.plugin.fileformat.plugin.BamFileTypeAttribute;

public class GatkBamFileTypeAttribute extends BamFileTypeAttribute {
	
	public static final GatkBamFileTypeAttribute REALN_AROUND_INDELS = new GatkBamFileTypeAttribute("realignAroundIndels");
	public static final GatkBamFileTypeAttribute RECAL_QC_SCORES = new GatkBamFileTypeAttribute("recalibratedQcScores");

	public GatkBamFileTypeAttribute(String attrName) {
		super(attrName);
	}

}
