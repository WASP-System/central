package edu.yu.einstein.wasp.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import edu.yu.einstein.wasp.model.MetaAttribute;

/*
 * Initializes request attributes selectItems,itemValue and itemLabel request attributes 
 * based on MetaAttribute.Control object
 * 
 * Used to build dropdown lists off meta field values.
 * 
 * @Author Sasha Levchuk
 */
public class MetaSelectTag extends BodyTagSupport {
	
	Logger log=Logger.getLogger(MetaSelectTag.class);
	
	private MetaAttribute.Control control;
	

	public void setControl(MetaAttribute.Control control) {
		this.control = control;
	}


	@Override
	public int doStartTag() throws javax.servlet.jsp.JspException {

		if (control==null) return Tag.EVAL_PAGE;
		
		if (control.getItems()==null) {
			
			this.pageContext.getRequest().setAttribute("selectItems",control.getOptions());
			this.pageContext.getRequest().setAttribute("itemValue","value");
			this.pageContext.getRequest().setAttribute("itemLabel","label");
			return Tag.EVAL_PAGE;
		}
		
		this.pageContext.getRequest().setAttribute("itemValue",control.getItemValue());
		this.pageContext.getRequest().setAttribute("itemLabel",control.getItemLabel());
		
		
		if (control.getItems().indexOf('.')==-1) {
			this.pageContext.getRequest().setAttribute("selectItems",this.pageContext.getRequest().getAttribute(control.getItems()));
			
			return Tag.EVAL_PAGE;
		}
		
		 String str=control.getItems();
		 if (str.indexOf(".")==-1 || str.indexOf("(")==-1 || str.indexOf(")")==-1) {
			 throw new JspException(control.getItems()+" should match ${beanName} or ${beanName.method(param)} pattern");
		 }
		 
		 String[] pair=StringUtils.tokenizeToStringArray(str, ".", true, true);
		 
		 String beanName=pair[0];
		 String [] method=pair[1].split("[\\(\\)]");
		
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
			 this.pageContext.getRequest().setAttribute("selectItems", result);
		 } catch (Throwable e) {
			 throw new IllegalStateException("Can't invoke method to get dropdown options beanName: "+beanName+",methodName: "+methodName+", argument:"+(args.length==0?"none":args[0]),e);
		 }
		 
		
		return Tag.EVAL_PAGE;
	}
	
}
