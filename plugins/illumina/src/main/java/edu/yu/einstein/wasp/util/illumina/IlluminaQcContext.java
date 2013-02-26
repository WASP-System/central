package edu.yu.einstein.wasp.util.illumina;

import edu.yu.einstein.wasp.model.Sample;

/**
 * Container for Illumina run QC items
 * @author asmclellan
 *
 */
public class IlluminaQcContext {

	private Sample cell;
	
	private boolean passed = false;
	
	private String comment = "";

	public Sample getCell() {
		return cell;
	}

	public void setCell(Sample cell) {
		this.cell = cell;
	}

	public boolean isPassed() {
		return passed;
	}

	public void setPassed(boolean passed) {
		this.passed = passed;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public IlluminaQcContext(){}

	public IlluminaQcContext(Sample cell, boolean passed, String comment) {
		super();
		this.cell = cell;
		this.passed = passed;
		this.comment = comment;
	}

}
