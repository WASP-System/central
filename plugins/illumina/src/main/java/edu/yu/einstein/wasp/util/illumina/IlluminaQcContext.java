package edu.yu.einstein.wasp.util.illumina;

import edu.yu.einstein.wasp.model.Sample;

/**
 * Container for Illumina run QC items
 * @author asmclellan
 *
 */
public class IlluminaQcContext {

	private Sample cell = null;
	
	private Boolean passedQc = null;
	
	private String comment = "";

	public Sample getCell() {
		return cell;
	}

	public void setCell(Sample cell) {
		this.cell = cell;
	}

	public Boolean isPassedQc() {
		return passedQc;
	}

	public void setPassedQc(boolean passedQc) {
		this.passedQc = passedQc;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public IlluminaQcContext(){}

	public IlluminaQcContext(Sample cell, boolean passedQc, String comment) {
		super();
		this.cell = cell;
		this.passedQc = passedQc;
		this.comment = comment;
	}

}
