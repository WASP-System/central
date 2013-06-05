package edu.yu.einstein.wasp.web;

/**
 * 
 * @author asmclellan
 *
 */
public class Tooltip {

	public static String getTooltipHtmlString(String tooltipText){
		return "&nbsp;<img src='/wasp/images/qmark.png' height='15px' border='0' title='" + tooltipText + "' class='tooltip'>&nbsp;";
	}
	
	public static String getCommentHtmlString(String commentText){
		return "&nbsp;<img src='/wasp/images/comment-icon.png' height='20px'  border='0' title='" + commentText + "' class='tooltip'>&nbsp;";
	}
	
	public static String getWarningHtmlString(String commentText){
		return "&nbsp;<img src='/wasp/images/warning.png' height='20px'  border='0' title='" + commentText + "' class='tooltip'>&nbsp;";
	}

}
