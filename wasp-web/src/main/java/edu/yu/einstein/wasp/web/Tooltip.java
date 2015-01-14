package edu.yu.einstein.wasp.web;

/**
 * 
 * @author asmclellan
 *
 */
public class Tooltip {

	public static String getTooltipHtmlString(String tooltipText, String servletPath){
		return "&nbsp;<img src='" + servletPath  + "/images/qmark.png' height='15px' border='0' title=\"" + tooltipText + "\" class='tooltip'>&nbsp;";
	}
	
	public static String getCommentHtmlString(String commentText, String servletPath){
		return "&nbsp;<img src='" + servletPath  + "/images/comment-icon.png' height='20px'  border='0' title=\"" + commentText + "\" class='tooltip'>&nbsp;";
	}
	
	public static String getWarningHtmlString(String commentText, String servletPath){
		if (commentText != null && !commentText.isEmpty())
			return "&nbsp;<img src='" + servletPath  + "/images/warningAndComment.png' height='20px'  border='0' title=\"" + commentText + "\" class='tooltip'>&nbsp;";
		else
			return "&nbsp;<img src='" + servletPath  + "/images/warning.png' height='20px'  border='0' >&nbsp;";
	}
	
	public static String getSuccessHtmlString(String commentText, String servletPath){
		if (commentText != null && !commentText.isEmpty())
			return "&nbsp;<img src='" + servletPath  + "/images/passAndComment.png' height='20px'  border='0' title=\"" + commentText + "\" class='tooltip'>&nbsp;";
		else
			return "&nbsp;<img src='" + servletPath  + "/images/pass.png' height='20px'  border='0' >&nbsp;";
	}
	
	public static String getFailureHtmlString(String commentText, String servletPath){
		if (commentText != null && !commentText.isEmpty())
			return "&nbsp;<img src='" + servletPath  + "/images/failAndComment.png' height='20px'  border='0' title=\"" + commentText + "\" class='tooltip'>&nbsp;";
		else
			return "&nbsp;<img src='" + servletPath  + "/images/fail.png' height='20px'  border='0' >&nbsp;";
	}

}
