package edu.yu.einstein.wasp.charts;

import java.util.Locale;

import edu.yu.einstein.wasp.service.MessageService;


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
	
	public String getLocalizedXAxisLabel(MessageService messageService) {
		return messageService.getMessage(xAxisLabel);
	}
	
	public String getLocalizedXAxisLabel(MessageService messageService, Locale locale) {
		return messageService.getMessage(xAxisLabel, locale);
	}

	public void setxAxisLabel(String xAxisLabel) {
		this.xAxisLabel = xAxisLabel;
	}

	public String getyAxisLabel() {
		return yAxisLabel;
	}
	
	public String getLocalizedYAxisLabel(MessageService messageService) {
		return messageService.getMessage(yAxisLabel);
	}
	
	public String getLocalizedYAxisLabel(MessageService messageService, Locale locale) {
		return messageService.getMessage(yAxisLabel, locale);
	}

	public void setyAxisLabel(String yAxisLabel) {
		this.yAxisLabel = yAxisLabel;
	}

	public WaspChart2D() {
		super();
	}

}
