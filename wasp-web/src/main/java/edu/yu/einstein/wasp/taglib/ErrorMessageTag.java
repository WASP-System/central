package edu.yu.einstein.wasp.taglib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
 * Displays localized messages using list of keys stored in session under name "_feedback"
 * 
 * @Author asmclellan
 */
public class ErrorMessageTag extends BodyTagSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6565159633261751602L;

	Logger log = LoggerFactory.getLogger(ErrorMessageTag.class);
	
	public static final String FEEDBACK_SESSION_ATTRIBUTE_NAME="_feedbackError";
	
	@SuppressWarnings("unchecked")
	public static final void addMessage(HttpSession session,String key) {
		
		List<String> messageKeys=(List<String>)session.getAttribute(FEEDBACK_SESSION_ATTRIBUTE_NAME);		
		
		if (messageKeys==null) {
			messageKeys=new ArrayList<String>();
		} 
		 messageKeys.add(key);		
		 
		 session.setAttribute(FEEDBACK_SESSION_ATTRIBUTE_NAME,messageKeys);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int doEndTag() throws javax.servlet.jsp.JspException {
	
		HttpSession session=((HttpServletRequest)this.pageContext.getRequest()).getSession();
		
		if (session==null) return Tag.SKIP_PAGE;
		
		List<String> messageKeys=(List<String>)session.getAttribute(FEEDBACK_SESSION_ATTRIBUTE_NAME);
		
		
		Locale locale=(Locale)session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		
		if (locale==null) locale=Locale.US;
		
	
		
		StringBuilder buf=new StringBuilder("");
		buf.append("<div id='waspErrorMessage' class='waspErrorMessage'>");
		if (messageKeys!=null && !messageKeys.isEmpty()){
			buf.append("<ul>\n");
			for(String key:messageKeys) {
				try {
					String message="<li>"+DBResourceBundle.MESSAGE_SOURCE.getMessage(key, null,locale) + "</li>\n";
					buf.append(message);
				} catch (Throwable e) {
					log.error("Cant get message by key "+key,e);
				}
			}
			buf.append("</ul>\n");
		}
		buf.append("</div>\n");
		
		session.removeAttribute(FEEDBACK_SESSION_ATTRIBUTE_NAME);
		
		try {
			this.pageContext.getOut().print(buf.toString());
		} catch (IOException e) {
			throw new JspTagException(e.getMessage());
		}
		
		
		return Tag.EVAL_PAGE;
	}
	
}
