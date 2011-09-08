package edu.yu.einstein.wasp.taglib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaAttribute.Area;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.User;

/*
 * Builds jqGrid-compatible JavaScript object to describe a field
 * 
 * @Author Sasha Levchuk
 */
public class JQFieldTag extends BodyTagSupport {
	
	public static final String AREA_ATTR="_area";
	
	Logger log=Logger.getLogger(JQFieldTag.class);
	
	private String name;
	
	private String object;
	

	private Type type;
	
	private Object items;
	
	private String itemLabel;
	
	private String itemValue;
	
	public void setItems(Object items) {
		this.items = items;
	}


	public void setItemLabel(String itemLabel) {
		this.itemLabel = itemLabel;
	}


	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}


	public void release() {
		name=null;
		object=null;
		items=null;
		type=null;
		itemLabel=null;
		itemValue=null;		
	}
	
	public static enum Type {
		text,
		select,
		checkbox,
		password,
		hidden,
		file
	}
	
	

	public void setType(String type) {
		this.type = Type.valueOf(type);
	}
	

	public void setObject(String object) {
		this.object = object;
	}


	public void setName(String name) {
		this.name = name;
	}

	private String getValue(String key, ResourceBundle bundle, String defaultValue) {
		
		if (!bundle.containsKey(key)) {
			bundle=ResourceBundle.getBundle("messages",Locale.ENGLISH);
			if (!bundle.containsKey(key)) return defaultValue;
			return (String)bundle.getObject(key);
		}
		
		return (String)bundle.getObject(key);
	}

	public int doStartTag() throws javax.servlet.jsp.JspException {
		
		if (object==null) {
			String objectStr=(String)((HttpServletRequest)this.pageContext.getRequest()).getAttribute(AREA_ATTR);
			if (objectStr==null) throw new JspException("'object' tag parameter or "+AREA_ATTR+" request attribute are required");
			object=objectStr;
		}

		HttpSession session=((HttpServletRequest)this.pageContext.getRequest()).getSession();
		
		Locale locale=(Locale)session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		
		if (locale==null) locale=Locale.ENGLISH;
		
		ResourceBundle bundle=ResourceBundle.getBundle("messages",locale);
		
		String jsName=object+"_"+name;
		
		try {
		
			Class clazz=null;
			
			String area=object;
			
			if (area.equals("user")) clazz=User.class;
			else if(area.equals("lab")) clazz=Lab.class;
			else if(area.equals("sampleDraft")) clazz=SampleDraft.class;
			else if(area.equals("sample")) clazz=Sample.class;
			else if(area.equals("platformunit")) clazz=Sample.class;
			else throw new JspTagException("unknown area "+object+"");
		
			boolean required=clazz.getDeclaredField(name).getAnnotation(org.hibernate.validator.constraints.NotEmpty.class)==null?false:true;
			
				
			String error="error:'"+getValue(area+"."+name+".error",bundle,"")+"',\n";
								
			String label=getValue(area+"."+name+".label",bundle,name);
				
			String suffix=getValue(area+"."+name+".suffix",bundle,"");
		
			String editrules="{}";
			String formoptions="{}";
			
			if (required) {
				editrules="{custom:true,custom_func:_validate_required}";
				formoptions="{elmsuffix:'"+suffix+"<font color=red>*</font>'}";
			} else {
				formoptions="{elmsuffix:'"+suffix+"'}";
			}
			
			
		
		String buf="var "+jsName+"={\n"+
		 "name:'"+name+"',\n"+
		 "label:'"+label+"',\n"+
	     "required:"+required+",\n"+
	     error+
	     "jq:{\n"+
		 		"	name:'"+name+"', \n"+
				"	width:80, \n"+
				"	align:'center',\n"+
				"	sortable:false,\n"+				
				"	sorttype:'text',\n"+		
				"	editable:true,\n"+
				"   editrules:"+editrules+",\n"+
				"   formoptions:"+formoptions+",\n"+
				"	editoptions:{size:20}\n"+
				"}\n\n"+
		 "};\n";
	
		if (type==Type.select) {
			
			if (this.items==null) {
				throw new JspTagException("'items' parameter is required when type is 'select'");
			}
		
			if (items instanceof Collection && (this.itemValue==null || this.itemLabel==null)) {
				throw new JspTagException("'items','itemValue' and 'itemLabel' parameters are required when type is 'select' and 'items' is a collection");
			}
		
			buf=buf+
			jsName+".jq['edittype']='select';\n"+
			jsName+".jq['editoptions']={value:{}};\n"+
			jsName+".jq['search']=false;\n"+
			jsName+".jq['editoptions']['value']="+getOptions()+";\n";
			
		} else if (type==Type.password) { 
			buf=buf+
			jsName+".jq['edittype']='password';\n"+
			jsName+".jq['hidden']=true;\n"+
			jsName+".jq['search']=false;\n"+			
			jsName+".jq['editrules']['edithidden']=true;\n";
		} else if (type==Type.hidden) { 
			buf=buf+
			jsName+".jq['edittype']='hidden';\n"+
			jsName+".jq['hidden']=true;\n"+
			jsName+".jq['search']=false;\n"+
			jsName+".jq['editrules']['edithidden']=false;\n";
		} else if (type==Type.file) { 
			buf=buf+
			jsName+".jq['edittype']='file';\n"+
			jsName+".jq['hidden']=false;\n"+
			jsName+".jq['search']=false;\n"+
			jsName+".jq['editoptions']['alt']='Select Sample File to upload';\n"+
			jsName+".jq['editoptions']['lang']='"+locale.getLanguage()+"';\n"+
			jsName+".jq['editrules']['edithidden']=true;\n";		
			 
		}

		buf=buf+
		"\ncolNames.push("+jsName+".label);\n"+
		"colModel.push("+jsName+".jq);\n"+
		"colErrors.push("+jsName+".error);\n";
		
		this.pageContext.getOut().print(buf);
	
		} catch (Throwable e) {
			log.error("Cant build JS props",e);
			throw new JspTagException(e.getMessage(),e);
		}
		
		
		return BodyTagSupport.EVAL_PAGE;
	}
	
	public int doEndTag() throws javax.servlet.jsp.JspException {
		//append body of the tag
		String body="";
		
		BodyContent bodyTag=getBodyContent();
		
		if (bodyTag!=null) {
			body=bodyTag.getString();
		}
		
		body=body.replaceAll("#field",object+"_"+name);
		
		try {
			this.pageContext.getOut().print(body);
	
		} catch (Throwable e) {
			log.error("Cant build JS props",e);
			throw new JspTagException(e.getMessage());
		}
		
		return EVAL_PAGE;
	}
	private String getOptions() throws JspException {
		Map map = new TreeMap();
		
		try {
			
			if (this.items instanceof Map) {
				map = (Map) items;
			} else if (this.items instanceof Iterable) {
				map = new TreeMap();
				
				for (Object bean : (Iterable) this.items) {
					map.put(
							BeanUtils.getSimpleProperty(bean, this.itemValue),
							BeanUtils.getSimpleProperty(bean, this.itemLabel)
					);
				}

			} else {
				throw new JspException("Type ["
						+ this.items.getClass().getName()
						+ "] is not valid for option items");
			}

			return new ObjectMapper().writeValueAsString(map);
			
		} catch (Throwable e) {
			log.error("Cant convert " + items, e);
			throw new JspException("Cant convert " + items, e);
		}
		
	}

	   	private String renderFromArray() throws JspException {
	   		return doRenderFromCollection(CollectionUtils.arrayToList(this.items));
	   	}
	
	   	private String renderFromMap() throws JspException {
	   	
	  		Map optionMap = (Map) this.items;
	  		
	  		this.itemValue="key";
	  		this.itemValue="value";
	  		
	  		List<Map.Entry> result=new ArrayList<Map.Entry>();
	  		
	   		for (Iterator iterator = optionMap.entrySet().iterator(); iterator.hasNext();) {
	   			Map.Entry entry = (Map.Entry) iterator.next();
	   			result.add(entry);
	   		}
	   		
	   		return doRenderFromCollection(result);
	   	}
	   
	
	   	private String renderFromCollection() throws JspException {
	   		return doRenderFromCollection((Collection) this.items);
	   	}
	
	   	private String doRenderFromCollection(Collection optionCollection) throws JspException {
	   		try {
	   				   				   			
	   		ObjectMapper mapper = new ObjectMapper();
	   		String json=mapper.writeValueAsString(this.items);
	   			   		
	   		
	   		return " "+object+"_"+name+".jq['editoptions']['value']="+json+";";
	   		
			/*
			String jsName= "_"+name+"_list";
			StringBuffer buf=new StringBuffer();
			
			buf.append("var="+jsName+"="+json+";\n");
			
			
			buf.append(
			"var _tmpArr=[];"+
			"for(lKey in "+jsName+")\n"+
			"  var opt="+jsName+"[lKey];\n"+
			" _tmpArr.push({opt['+"+this.itemValue+"+']:opt['+"+this.itemLabel+"+']}\n"+
			"}\n"
			);
			
			buf.append( " "+name+".jq['editoptions']['value']=_tmpArr;");
			
	   		
			return buf.toString();*/
	   		
	   		} catch (Throwable e) {
	   			log.error("Cant convert "+items,e);
	   			throw new JspException("Cant convert "+items,e);
	   		}
	   	}
	
	   	

}


