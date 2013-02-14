package edu.yu.einstein.wasp.taglib;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import edu.yu.einstein.wasp.resourcebundle.DBResourceBundle;

/**
 * Displays tooltip icon and provides tooltip
 * @author asmclellan
 *
 */
public class Tooltip extends BodyTagSupport {
	
	
	private static final long serialVersionUID = -7313415827087355691L;

	Logger log = LoggerFactory.getLogger(Tooltip.class);
	
	private String tooltipText;
	
	public void setKey(String key){
		HttpSession session=((HttpServletRequest) this.pageContext.getRequest()).getSession();
		if (session == null)
			return;
		Locale locale=(Locale) session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		if (locale==null) 
			locale=Locale.US;
		this.tooltipText = DBResourceBundle.MESSAGE_SOURCE.getMessage(key, null,locale);
	}
	
	public void setValue(String value){
		this.tooltipText = value;
	}
	
		
	@Override
	public int doStartTag() throws javax.servlet.jsp.JspException {
		if (tooltipText == null) 
			return Tag.SKIP_BODY;
		
		StringBuffer buf=new StringBuffer("");
		buf.append("<img src='/wasp/images/qmark.png' height='13px' width='12px' border='0' title='" + tooltipText + "' id='tooltip'>");
		
		try {
			this.pageContext.getOut().print(buf.toString());
		} catch (IOException e) {
			throw new JspTagException(e.getMessage());
		}

		return Tag.EVAL_PAGE;
	}
	
}
