package edu.yu.einstein.wasp.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.beanutils.MethodUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/*
 * Converts java collection to string containing JSON array.
 * 
 * @Author Sasha Levchuk
 */
public class JSONTag extends BodyTagSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3228211300174272597L;

	Logger log=LoggerFactory.getLogger(JSONTag.class);
	
	private Object object;
	


	public void setObject(Object object) {
		this.object = object;
	}



	@Override
	public int doEndTag() throws javax.servlet.jsp.JspException {

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES, false);
		
		 try {
			 
			 String json=null;
			 
			 if (object!=null && object instanceof String) {//bean.method(param) syntax
				 String str=(String)object;
				 if (str.indexOf(".")==-1 || str.indexOf("(")==-1 || str.indexOf(")")==-1) {
					 throw new JspException(object+" should match ${beanName} or ${beanName.method(param)} pattern");
				 }
				 
				 String[] pair=StringUtils.tokenizeToStringArray(str, ".", true, true);
				 
				 String beanName=pair[0];
				 String [] method=pair[1].split("[\\(\\)]");
				 //boolean paramPresent=method.length==4;
				 String methodName=method[0];
				 
				 
				 Object bean= this.pageContext.getRequest().getAttribute(beanName);
				
				 
				 if (bean==null) throw new JspException("No bean under name "+beanName+" exist in request attributes");
				 
				 Object[] args;
				 
				 if (method.length==2) {
					 args=new Object[1];
					 args[0]=method[1];
				 } else {
					 args=new Object[0];
				 }
				  
				 try {				 
					 Object result = MethodUtils.invokeExactMethod(bean, methodName, args);
					 json=mapper.writeValueAsString(result);
				 } catch (Throwable e) {
					 throw new IllegalStateException("Can't invoke method to get dropdown options beanName: "+beanName+",methodName: "+methodName+", argument:"+(args.length==0?"none":args[0]),e);
				 }
				 
			 } else {
				 json=mapper.writeValueAsString(object);
			 }
			 
			 
			 this.pageContext.getOut().print(json);
		 } catch (Throwable e) {
			 throw new IllegalStateException("Can't marshall to JSON "+object,e);
		 }
		 
		
		return Tag.EVAL_PAGE;
	}
	
}
