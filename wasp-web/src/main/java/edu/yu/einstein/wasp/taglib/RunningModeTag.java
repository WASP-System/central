package edu.yu.einstein.wasp.taglib;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author asmclellan
 *
 */
public class RunningModeTag extends BodyTagSupport {

	private static final long serialVersionUID = -906485942409348166L;
	
	Logger log = LoggerFactory.getLogger(RunningModeTag.class);
	
	@Override
	public int doStartTag() throws javax.servlet.jsp.JspException {
		Boolean isInDemoMode = (Boolean) pageContext.getSession().getAttribute("isInDemoMode");
		if (isInDemoMode == null || !isInDemoMode)
			return Tag.SKIP_BODY;
		else
			return Tag.EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws javax.servlet.jsp.JspException {
		return Tag.EVAL_PAGE;
	}

}
