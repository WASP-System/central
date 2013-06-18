package edu.yu.einstein.wasp.charts;


/**
 * 
 * @author asmclellan
 *
 */
public class WaspChart2D extends WaspChart {
	
	protected String xAxisLabel;
	
	protected String yAxisLabel;
	
	
	public String getxAxisLabel() {
		return xAxisLabel;
	}

	public void setxAxisLabel(String xAxisLabel) {
		this.xAxisLabel = xAxisLabel;
	}

	public String getyAxisLabel() {
		return yAxisLabel;
	}

	public void setyAxisLabel(String yAxisLabel) {
		this.yAxisLabel = yAxisLabel;
	}

	public WaspChart2D() {
		super();
	}

}
