package edu.yu.einstein.wasp.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author asmclellan
 *
 */
public class UrlTag extends BodyTagSupport {

	private static final long serialVersionUID = 7897505359306370856L;
	
	Logger log = LoggerFactory.getLogger(UrlTag.class);
	
	String value;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int doEndTag() throws javax.servlet.jsp.JspException {
		StringBuffer buf=new StringBuffer("");
		String servletName = (String) pageContext.getSession().getAttribute("servletName");
		buf.append("/")
			.append(servletName)
			.append("/")
			.append(value);
		
		try {
			this.pageContext.getOut().print(buf.toString());
		} catch (IOException e) {
			throw new JspTagException(e.getMessage());
		}

		return Tag.EVAL_PAGE;
	}

}
