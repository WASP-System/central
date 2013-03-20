package edu.yu.einstein.wasp.taglib;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import edu.yu.einstein.wasp.resourcebundle.DBResourceBundle;

/*
 * Converts Apache Tiles mapping to page title string
 * Expects "pageTitle.definitionName.label" property in uifield database table.
 * @Author Sasha Levchuk
 */
public class PageTitleTag extends BodyTagSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8696522045638180707L;
	
	@Override
	public int doStartTag() throws javax.servlet.jsp.JspException {
		
		HttpSession session=((HttpServletRequest)this.pageContext.getRequest()).getSession();
		
		BodyContent bodyTag=getBodyContent();
		
		if (bodyTag!=null) {
			String body=bodyTag.getString();
			if (!StringUtils.isEmpty(body)) {
				try {										
					this.pageContext.getOut().print(body);
					return EVAL_BODY_INCLUDE;
				} catch (IOException e) {
					throw new JspTagException(e.getMessage());
				}
			}
		} 
		
		String title=(String)((HttpServletRequest)this.pageContext.getRequest()).getAttribute("forcePageTitle");
		if (title == null) {
			String viewName=(String)pageContext.getRequest().getAttribute("waspViewName");
			
			if (viewName==null) throw new JspTagException("Cannot get tiles view name");
			
			Locale locale=(Locale)session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
			
			String code="pageTitle."+viewName+".label";
				
			title=DBResourceBundle.MESSAGE_SOURCE.getMessage(code, null, locale);
		}
		
				
		try {
			this.pageContext.getOut().print( title );
		} catch (IOException e) {
			throw new JspTagException(e.getMessage());
		}
		
		return EVAL_BODY_INCLUDE;
	}
	
}
