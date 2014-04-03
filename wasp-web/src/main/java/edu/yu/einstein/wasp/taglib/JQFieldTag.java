package edu.yu.einstein.wasp.taglib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Currency;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.WordUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.resourcebundle.DBResourceBundle;

/**
 * Builds jqGrid-compatible JavaScript object to describe a field
 * 
 * @Author Sasha Levchuk
 */
public class JQFieldTag extends BodyTagSupport {

	//name of the request attribute to fetch area name from
	public static final String AREA_ATTR="_area";
	
	Logger log=LoggerFactory.getLogger(JQFieldTag.class);
	
	//width of a column; not required
	private String columnWidth;
	private Integer columnWidthAsInteger;
	
	//field name
	private String name;
	
	//fields object class to lookup constraints
	private String object;
	
	//fields HTML type 
	private Type type;
	
	//list of options to render html select input element
	private Object items;
	
	//Name of the property mapped to 'value' attribute of the 'option' tag
	private String itemLabel;
	
	//Name of the property mapped to the inner text of the 'option' tag
	private String itemValue;
	
	// Whether to show the field's value as a hyperlink in such format: {baseLinkURL}?{idName}={rowid}
	// By default, baseLinkURL = '/wasp/{area}/list.do' , idName = 'selId'
	private String showLink;
	private String baseLinkURL;
	private String idName;
	private String idCol;

	//Read-only field
	private String readOnly;
	
	//Read-only field
	private String editReadOnly;
	
	//Sortable field
	private String sortable;
	
	//editable hidden field
	private String editHidden;
	
	//hidden field
	private String hidden;
	
	//editable field
	private String editable;
	
	//Searchable field
	private String searchable;
	
	//Default select field
	private String defaultSelect;
  
	private Map metaMessages;
  
	public void setItems(Object items) {
		this.items = items;
	}


	public void setItemLabel(String itemLabel) {
		this.itemLabel = itemLabel;
	}


	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}


	@Override
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
		file,
		link,
		currency
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

	public void setShowLink(String sl) {
		this.showLink = sl;
	}

	public void setBaseLinkURL(String bu) {
		this.baseLinkURL = bu;
	}
	
	public void setIdName(String in) {
		this.idName = in;
	}

	public void setIdCol(String ic) {
		this.idCol = ic;
	}

	public void setReadOnly(String ro) {
		this.readOnly = ro;
	}
	
	public void setSortable(String st) {
		this.sortable = st;
	}
	
	public void setEditHidden(String eh) {
		this.editHidden = eh;
	}

	public void setEditReadOnly(String eh) {
		this.editReadOnly = eh;
	}
	public void setHidden(String hd) {
		this.hidden = hd;
	}

	public void setEditable(String ed) {
		this.editable = ed;
	}
	
	public void setSearchable(String sb) {
		this.searchable = sb;
	}

	public void setDefaultSelect(String ds) {
		this.defaultSelect = ds;
	}
	
	public void setColumnWidth(String w) {
		
		this.columnWidth = w;
	}
	
	//get locale-specific message
	private String getMessage(String key, String defaultMessage) {
		String r=getMessage(key);
		
		if (defaultMessage!=null && r!=null && r.equals(key)) return defaultMessage; 
		
		return r;
	}
	
	private String getMessage(String key) {
		HttpSession session=((HttpServletRequest)this.pageContext.getRequest()).getSession();
		
		Locale locale=(Locale)session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		
		return DBResourceBundle.MESSAGE_SOURCE.getMessage(key, null, locale);
	}
	
	@Override
	public int doStartTag() throws javax.servlet.jsp.JspException {
		if (object==null) {
			String objectStr=(String)((HttpServletRequest)this.pageContext.getRequest()).getAttribute(AREA_ATTR);
			if (objectStr==null) throw new JspException("either 'object' tag parameter or "+AREA_ATTR+" request attribute are required");
			object=objectStr;
		}

		String jsName=object+"_"+name;
		
		try {
			//get class of the "object"
			Class clazz=null;
			
			String area=object;
			
			if(area.equals("platformunit") || area.equals("platformunitInstance") || area.equals("platformunitById")) clazz=Sample.class;			
			//else if(area.equals("fmpayment")) clazz=State.class;
			else {
				try {
					String path="edu.yu.einstein.wasp.model."+WordUtils.capitalize(area);	
					clazz = Class.forName(path);
				 
				} catch (Throwable e) {
					throw new JspTagException("unknown area "+object+"");
				}
				
			}
		
			//check if the field is "required"
			boolean required=false;
			
			try {
				required=clazz.getDeclaredField(name).getAnnotation(org.hibernate.validator.constraints.NotEmpty.class)==null?false:true;
			} catch (NoSuchFieldException e) {
				//let it slide - we'll allow fields that are not in the entity objects
			}
			
			//check if field is an email address
			boolean isEmail=false;
			
			try {
				isEmail=clazz.getDeclaredField(name).getAnnotation(org.hibernate.validator.constraints.Email.class)==null?false:true;
			} catch (NoSuchFieldException e) {
				//let it slide - we'll allow fields that are not in the entity objects
			}
			
			
			//get error message to display if constraint validation fails
			String error="error:'"+getMessage(area+"."+name+".error","")+"',\n";
			
			//get constraint if set
			boolean isRegExp = false;
			String constraintMessage = getMessage(area+"."+name+".constraint","");
			String constraint="constraint:'"+constraintMessage+"',\n";
			if (constraintMessage.equals("NotEmpty") ){
				required = true;
			} else if (constraintMessage.startsWith("RegExp:")){
				isRegExp = true;
			}
			
			//get metaType if set
			String metaType="metaType:'"+getMessage(area+"."+name+".type","STRING")+"',\n";
			
			//get rangeMax if set
			String range="range:'"+getMessage(area+"."+name+".range","")+"',\n";
			

			//get column label
			String label=getMessage(area+"."+name+".label");
			
			// get tooltip
			String tooltip=getMessage(area+"."+name+".tooltip","");
			
			//get suffix to show after field's input
			String suffix=getMessage(area+"."+name+".suffix","");
		
			String editrules="{}";
			String formoptions="{}";
			
			//display red star if the field is required
			if (required) {
				editrules="{custom:true,custom_func:_validate_required}";
				formoptions="{elmsuffix:'"+suffix+"<span class=\"requiredField\">*</span>";
			} else {
				formoptions="{elmsuffix:'"+suffix;
			}
			if (!tooltip.isEmpty())
				formoptions += "<img src='qmark.png' height='13px' width='12px' border='0' title='" + tooltip + "'>";
			formoptions += "'}";
			
			// override validation with email validation if isEmail == true
			if (isEmail) {
				editrules="{custom:true,custom_func:_validate_email}";
			} 
			// override validation with regular expression if 
			if (isRegExp){
				editrules="{custom:true,custom_func:_validate_regexp}";
			}
			
			try{
				columnWidthAsInteger = new Integer(columnWidth);
			}
			catch(Exception e){
					columnWidthAsInteger = new Integer(200);
			}
			
			//init js column definition 
			String buf="var "+jsName+"={\n"+
			 "name:'"+name+"',\n"+
			 "label:'"+label+"',\n"+
		     "required:"+required+",\n"+
		     error + constraint + metaType + range +
		     "jq:{\n"+
			 		"	name:'"+name+"', \n"+
			 		"	index:'"+name+"', \n"+
					//"	width:80, \n"+
					"	width:"+columnWidthAsInteger.toString()+", \n"+
					"	align:'left',\n"+
					"	sortable:false,\n"+	
					"	editHidden:false,\n"+	
					"	hidden:false,\n"+
					"	sorttype:'text',\n"+		
					"	editable:true,\n"+
					"   editrules:"+editrules+",\n"+
					"   formoptions:"+formoptions+",\n"+
					"	editoptions:{size:20}\n"+
					"}\n\n"+
			 "};\n";
		
			//add type-specific parameters to field definition
			if (type==Type.select) {
				
				if (this.items==null) {
					throw new JspTagException("'items' parameter is required when type is 'select'");
				}
			
				if (items instanceof Collection && (this.itemValue==null || this.itemLabel==null)) {
					throw new JspTagException("'items','itemValue' and 'itemLabel' parameters are required when type is 'select' and 'items' is a collection");
				}
				
				String optionStr = getOptions();
				if ("true".equals(this.defaultSelect)) {
					optionStr = "{\"-1\":\"" + getMessage("wasp.default_select.label") + "\"," + optionStr.substring(1);
				}
			
				buf = buf + 
					jsName + ".jq['edittype']='select';\n" +
					jsName + ".jq['editoptions']={value:{}};\n" +
					jsName + ".jq['search']=false;\n" +
					jsName + ".jq['editoptions']['value']=" + optionStr + ";\n";
				
			} else if (type==Type.password) { 
				buf = buf +
					jsName + ".jq['edittype']='password';\n"+
					jsName + ".jq['hidden']=true;\n" +
					jsName + ".jq['search']=false;\n" +			
					jsName + ".jq['editrules']['edithidden']=true;\n";
			} else if (type==Type.hidden) { 
				buf = buf + 
					jsName + ".jq['edittype']='hidden';\n" + 
					jsName + ".jq['hidden']=true;\n" + 
					jsName + ".jq['search']=false;\n" + 
					jsName + ".jq['editrules']['edithidden']=false;\n";
			} else if (type==Type.file) { 
				buf = buf + 
					jsName + ".jq['edittype']='file';\n" + 
					jsName + ".jq['hidden']=false;\n" + 
					jsName + ".jq['search']=false;\n" + 
					jsName + ".jq['editoptions']['alt']='Select Sample File to upload';\n" + 
					jsName + ".jq['editrules']['edithidden']=true;\n";
			} else if (type==Type.checkbox) {
				buf = buf + 
					jsName + ".jq['edittype']='checkbox';\n" + 
					jsName + ".jq['editoptions']={value:'1:0'};\n" + 
					jsName + ".jq['formatter']='checkbox';\n" + 
					jsName + ".jq['formatoptions']={disabled : true};\n" + 
					jsName + ".jq['align']='center';\n" + 
					jsName + ".jq['search']=false;\n";
				
			} else if (type==Type.currency) {
				buf = buf + 
				jsName + ".jq['formatter']='currencyFormatter';\n" + 
				jsName + ".jq['formatoptions']={symbol:'" + Currency.getInstance(Locale.getDefault()).getSymbol() + "'};\n" +
				jsName + ".jq['align']='right';\n" + 
				jsName + ".jq['search']=false;\n";
			}
			
	
			if ("true".equals(this.showLink)) {
				// Set baseLinkURL and idName as default if they are not presented
				String servletName = (String) pageContext.getSession().getAttribute("servletName");
				if (this.baseLinkURL==null || this.baseLinkURL.isEmpty())
					this.baseLinkURL = "/" + servletName + "/" + area + "/list.do";
				if (this.idName==null || this.idName.isEmpty())
					this.idName = "selId";
				if (this.idCol==null || this.idCol.isEmpty())
					this.idCol = "-1";
				
				buf = buf + 
					jsName + ".jq['formatter']='linkFormatter';\n" + 
					jsName + ".jq['formatoptions']={baseLinkUrl:'" + this.baseLinkURL 
												+ "',idName:'" + this.idName 
												+ "',idCol:'" + this.idCol 
												+ "'};\n";
			}
	
//			if ("true".equals(this.readOnly)) {
//				buf = buf + 
//					jsName + ".jq['editoptions']['dataInit'] = function(elm){setTimeout(disableControl(this.id), 200);};\n";
//			}
			if ("true".equals(this.editReadOnly)) {
				buf = buf + 
					jsName + ".jq['editoptions']['readonly']='readonly';\n";
			}
	
	
			if ("true".equals(this.sortable)) {
				buf = buf + 
					jsName + ".jq['sortable']=true;\n";
			}
			
			if ("true".equals(this.editHidden)) {
				buf = buf + 
					jsName + ".jq['editrules']['edithidden']=true;\n";
			}
			
			if ("true".equals(this.hidden)) {
				buf = buf + 
					jsName + ".jq['hidden']=true;\n";
			}
			if ("false".equals(this.editable)) {
				buf = buf + 
					jsName + ".jq['editable']=false;\n";
			}
	
			if ("false".equals(this.searchable)) {
				buf = buf + 
					jsName + ".jq['search']=false;\n";
			}
	
			buf = buf + "\ncolNames.push(" + jsName + ".label);\n" 
				+ "colModel.push(" + jsName + ".jq);\n" 
				+ "colErrors.push(" + jsName + ".error);\n"
				+ "colConstraint.push(" + jsName + ".constraint);\n"
				+ "colMetaType.push(" + jsName + ".metaType);\n"
				+ "colRange.push(" + jsName + ".range);\n";
			
			this.pageContext.getOut().print(buf);
		} catch (Throwable e) {
			log.error("Cant build JS props",e);
			throw new JspTagException(e.getMessage(),e);
		}
		
		return Tag.EVAL_BODY_INCLUDE;
	}
	
	//replace "#field" tokens in tag body with real name 
	@Override
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
	
	//build JSON - formatted string suitable for building JQGrid HTML select dropdowns 
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

			} else if (this.items instanceof String && "empty".equals(this.items)) {
				//empty select lists 
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

		//render HTML select data structure from array
	   	private String renderFromArray() throws JspException {
	   		return doRenderFromCollection(CollectionUtils.arrayToList(this.items));
	   	}
	
	   	
		//render HTML select data structure from array
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
	   
	
		//render HTML select data structure from collection
	   	private String renderFromCollection() throws JspException {
	   		return doRenderFromCollection((Collection) this.items);
	   	}
	
	   //render HTML select data structure from collection
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


