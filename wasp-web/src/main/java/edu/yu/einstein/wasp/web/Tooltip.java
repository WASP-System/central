package edu.yu.einstein.wasp.web;

/**
 * 
 * @author asmclellan
 *
 */
public class Tooltip {

	public static String getTooltipHtmlString(String tooltipText, String servletName){
		return "&nbsp;<img src='/" + servletName  + "/images/qmark.png' height='15px' border='0' title='" + tooltipText + "' class='tooltip'>&nbsp;";
	}
	
	public static String getCommentHtmlString(String commentText, String servletName){
		return "&nbsp;<img src='/" + servletName  + "/images/comment-icon.png' height='20px'  border='0' title='" + commentText + "' class='tooltip'>&nbsp;";
	}
	
	public static String getWarningHtmlString(String commentText, String servletName){
		if (commentText != null && !commentText.isEmpty())
			return "&nbsp;<img src='/" + servletName  + "/images/warningAndComment.png' height='20px'  border='0' title='" + commentText + "' class='tooltip'>&nbsp;";
		else
			return "&nbsp;<img src='/" + servletName  + "/images/warning.png' height='20px'  border='0' >&nbsp;";
	}
	
	public static String getSuccessHtmlString(String commentText, String servletName){
		if (commentText != null && !commentText.isEmpty())
			return "&nbsp;<img src='/" + servletName  + "/images/passAndComment.png' height='20px'  border='0' title='" + commentText + "' class='tooltip'>&nbsp;";
		else
			return "&nbsp;<img src='/" + servletName  + "/images/pass.png' height='20px'  border='0' >&nbsp;";
	}
	
	public static String getFailureHtmlString(String commentText, String servletName){
		if (commentText != null && !commentText.isEmpty())
			return "&nbsp;<img src='/" + servletName  + "/images/failAndComment.png' height='20px'  border='0' title='" + commentText + "' class='tooltip'>&nbsp;";
		else
			return "&nbsp;<img src='/" + servletName  + "/images/fail.png' height='20px'  border='0' >&nbsp;";
	}

}
