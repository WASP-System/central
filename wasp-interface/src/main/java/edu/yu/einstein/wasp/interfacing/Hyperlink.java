package edu.yu.einstein.wasp.interfacing;

/**
 * A target link and associated label
 * @author asmclellan
 *
 */
public class Hyperlink {
	
	protected String label;
	
	protected String targetLink;
	
	public Hyperlink(String label, String targetLink) {
		this.label = label;
		this.targetLink = targetLink;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}

	public String getTargetLink() {
		return targetLink;
	}

	public void setTargetLink(String targetLink) {
		this.targetLink = targetLink;
	}

}
