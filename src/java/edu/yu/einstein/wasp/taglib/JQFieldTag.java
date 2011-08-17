package edu.yu.einstein.wasp.taglib;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaAttribute.Area;
import edu.yu.einstein.wasp.model.User;

/*
 * Builds jqGrid-compatible JavaScript object to describe a field
 * 
 * @Author Sasha Levchuk
 */
public class JQFieldTag extends BodyTagSupport {
	
	Logger log=Logger.getLogger(JQFieldTag.class);
	
	private String name;
	
	private String object;
		 
	
	public String getName() {
		return name;
	}


	public String getObject() {
		return object;
	}


	public void setObject(String object) {
		this.object = object;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int doStartTag() throws javax.servlet.jsp.JspException {

		HttpSession session=((HttpServletRequest)this.pageContext.getRequest()).getSession();
		
		Locale locale=(Locale)session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		
		if (locale==null) locale=Locale.ENGLISH;
		
		ResourceBundle bundle=ResourceBundle.getBundle("messages",locale);
		
		try {
		
			Class clazz=null;
			
			MetaAttribute.Area area=MetaAttribute.Area.valueOf(object);
			
			if (area==Area.user) clazz=User.class;
			else if(area==Area.lab) clazz=Lab.class;
			else throw new JspTagException("unknown area "+object+" currently support user or lab");
		
		boolean required=clazz.getDeclaredField(name).getAnnotation(org.hibernate.validator.constraints.NotEmpty.class)==null?false:true;
			

		String error="error:'',\n";
		
		try {
			error="error:'"+bundle.getObject(area+"."+name+".error")+"',\n";
		} catch (Throwable e) {
			 
		}
		
		String buf="var "+name+"={\n"+
		 "name:'"+name+"',\n"+
		 "label:'"+bundle.getObject(area+"."+name+".label")+"',\n"+
	     "required:"+required+",\n"+
	     error+
	     "jq:{\n"+
		 		"	name:'"+name+"', \n"+
				"	width:80, \n"+
				"	align:'center',\n"+
				"	sortable:false,\n"+				
				"	sorttype:'text',\n"+		
				"	editable:true,\n"+
				(required?"	editrules:{custom:true,custom_func:_validate_required},formoptions:{elmsuffix:'<font color=red>*</font>'},":"")+"\n"+
				"	editoptions:{size:20}\n"+
				"}\n\n"+
		 "};\n";
	
		buf=buf+
		"colNames.push("+name+".label)\n"+
		"colModel.push("+name+".jq)\n"+
		"colErrors.push("+name+".error)\n";
		
		this.pageContext.getOut().print(buf);
	
		} catch (Throwable e) {
			log.error("Cant build JS props",e);
			throw new JspTagException(e.getMessage());
		}
		
		
		return BodyTagSupport.EVAL_PAGE;
	}
	
}
