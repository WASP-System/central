package edu.yu.einstein.wasp.taglib;

import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

/*
 * Converts java collection to string containing JSON array.
 * 
 * @Author Sasha Levchuk
 */
public class JSONTag extends BodyTagSupport {
	
	Logger log=Logger.getLogger(JSONTag.class);
	
	private Object object;
	


	public void setObject(Object object) {
		this.object = object;
	}



	public int doStartTag() throws javax.servlet.jsp.JspException {

		ObjectMapper mapper = new ObjectMapper();
		
		 try {
			 String json=mapper.writeValueAsString(object);
			 this.pageContext.getOut().print(json);
		 } catch (Throwable e) {
			 throw new IllegalStateException("Can't marshall to JSON "+object,e);
		 }
		 
		
		return BodyTagSupport.EVAL_PAGE;
	}
	
}
