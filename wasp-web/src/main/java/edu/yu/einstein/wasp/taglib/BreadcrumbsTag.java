package edu.yu.einstein.wasp.taglib;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import edu.yu.einstein.wasp.resourcebundle.DBResourceBundle;

/*
 * Converts Apache Tiles mapping to page title string and updates breadcrumb trail
 * Expects "pageTitle.definitionName.label" property in uifield database table.
 * @Author ASMclellan
 */
public class BreadcrumbsTag extends BodyTagSupport {
	
	private static final Logger log=LoggerFactory.getLogger(BreadcrumbsTag.class);
	
	@Override
	public int doStartTag() throws javax.servlet.jsp.JspException {
	
		return Tag.EVAL_PAGE;
	}
	
	
	@Override
	public int doEndTag() throws javax.servlet.jsp.JspException {
		HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
		HttpSession session = request.getSession();
		
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
		
		String breadcrumbs = (String) session.getAttribute("breadcrumbs");
		if (breadcrumbs == null)
			breadcrumbs = "";
		try {
			this.pageContext.getOut().print( breadcrumbs );
		} catch (IOException e) {
			throw new JspTagException(e.getMessage());
		}
		String newBreadcrubs = "";
		if (request.getAttribute("forcePageTitle") == null){
			// forcePageTitle is only used with job submission and this has its own trail
			String[] breadcrumbList = breadcrumbs.split(" &gt;&gt; ");
			boolean isFirst = true;
			for (String breadcrumb : breadcrumbList){
				if (breadcrumb.isEmpty())
					continue;
				if (breadcrumbList.length >=5 && isFirst){
					isFirst = false;
					continue; // don't include the first item in the new list if >=4 elements
				}
				newBreadcrubs += breadcrumb + " &gt;&gt; ";
			}
			
			String viewName = (String) request.getAttribute("waspViewName");
			String waspViewUrl = (String) request.getAttribute("waspViewUrl");
			if (viewName == null || waspViewUrl == null) 
				throw new JspTagException("Cannot get tiles view name or url");
			Locale locale = (Locale) session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
			String code = "pageTitle." + viewName + ".label";
			String title = DBResourceBundle.MESSAGE_SOURCE.getMessage(code, null, locale);
			newBreadcrubs += "<a href='" + waspViewUrl + "'>" + title + "</a>";
			session.setAttribute("breadcrumbs", newBreadcrubs);
		} else {
			// keep the same
			newBreadcrubs = breadcrumbs;
		}
		
		
		return EVAL_BODY_INCLUDE;
	}
	
}
