package edu.yu.einstein.wasp.controller;
/**
 * Controller to manage UIField table
 * 
 * @Author: Sasha Levchuk
 */
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.yu.einstein.wasp.dao.UiFieldDao;
import edu.yu.einstein.wasp.model.JQuerySearch;
import edu.yu.einstein.wasp.model.UiField;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.service.impl.WaspMessageSourceImpl;
import edu.yu.einstein.wasp.taglib.JQFieldTag;

@Controller
@Transactional
@RequestMapping("/uiField")
public class UiFieldController extends WaspController {

	Logger log=LoggerFactory.getLogger(UiFieldController.class);
	
	@Autowired
	private UiFieldDao uiFieldDao;

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private MessageService messageService;
	
	//init read-only static structures used ti build dropdown lists
	private static final Map<String, String> MY_LOCALES=new LinkedHashMap<String, String>();
	static { 
		 MY_LOCALES.put("","--select--");
		 MY_LOCALES.putAll(LOCALES);
	 }
	
	  private static final Map<String, String> ATTR_NAMES=new LinkedHashMap<String, String>();
	  static { 

	  ATTR_NAMES.put("","-- select --");	  
	  ATTR_NAMES.put("label","Field Label");
	  ATTR_NAMES.put("constraint","Validation Constraint");
	  ATTR_NAMES.put("error","Validation Constraint Message");	  	  
	  ATTR_NAMES.put("control","Dropdown Options");
	  ATTR_NAMES.put("suffix","Suffix");
	  ATTR_NAMES.put("metaposition","Position");
	  ATTR_NAMES.put("data","Area-specific data");
	  ATTR_NAMES.put("type","Data Type (INTEGER, NUMBER, STRING)");
	  ATTR_NAMES.put("range","Data Range (e.g. 10 or 0:10)");
	  }
	  
	  //init read-only static structures used ti build dropdown lists
	  protected static final Map<String, String> AREA_NAMES=new LinkedHashMap<String, String>();
	  static { 
		  
		  AREA_NAMES.put("","--select--");
	  }

	  /**
	   * prepares data to display JQGrid
	   * @param m
	   * @param response
	   * @return
	   */
	@RequestMapping("/list")
	@PreAuthorize("hasRole('su')")
	public String list(ModelMap m,HttpServletResponse response) {
	
		Map<String, String> areas=new TreeMap<String, String>();
		
		areas.putAll(AREA_NAMES);
		
		for(String area:uiFieldDao.getUniqueAreas()) {
			areas.put(area,area);
		}
		
		response.setContentType("text/html;charset=UTF-8");
		
		m.addAttribute(JQFieldTag.AREA_ATTR, "uiField");		
		m.addAttribute("attrNames", ATTR_NAMES);
		m.addAttribute("areaNames", areas);
		m.addAttribute("locales", MY_LOCALES);
		
		return "uifield/list";
	}

	/**
	 * Returns JSON-formatted list of data rows
	 * 
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/listJSON", method=RequestMethod.GET)	
	public String getListJSON(HttpServletResponse response) {
	
		
		//result
		Map <String, Object> jqgrid = new HashMap<String, Object>();
		
		List<UiField> uiFieldList;
		  
		if (request.getParameter("filters")!=null) {  
			try {
				
				JQuerySearch params= new ObjectMapper().readValue(request.getParameter("filters"),JQuerySearch.class);
				
				Map<String, String> m = new HashMap<String, String>();
				for(JQuerySearch.Rules rule:params.rules) {
					m.put(rule.field, rule.data);
				}
								
				uiFieldList = this.uiFieldDao.findByMap(m);
				
			} catch (Throwable e) {
				log.error("cant read search params "+request.getParameter("filters"),e);
				uiFieldList=uiFieldDao.findAll();
			}
		} else {
			uiFieldList=uiFieldDao.findAll();
		}
		
		
		try {
			//init JQGrid standard structure
			jqgrid.put("page","1");
			jqgrid.put("records",uiFieldList.size()+"");
			jqgrid.put("total",uiFieldList.size()+"");
			
			
			//init selcted row if any
			Map<String, String> userData=new HashMap<String, String>();
			userData.put("page","1");
			userData.put("selId",StringUtils.isEmpty(request.getParameter("selId"))?"":request.getParameter("selId"));
			jqgrid.put("userdata",userData);
			
			List<Map> rows = new ArrayList<Map>();
			
			
			for (UiField field:uiFieldList) {
				
				Map cell = new HashMap();
				cell.put("id", field.getUiFieldId());
				
				List<String> cellList=new ArrayList<String>(Arrays.asList(new String[] {
						MY_LOCALES.get(field.getLocale()),
						field.getArea(),
						field.getName(),						
						ATTR_NAMES.get(field.getAttrName()),
						field.getAttrValue()						
				}));
				 
				 cell.put("cell", cellList);
				 
				 rows.add(cell);
			 }

			 
			 jqgrid.put("rows",rows);
			 
			 return outputJSON(jqgrid, response); 	
			 
		 } catch (Throwable e) {
			 throw new IllegalStateException("Can't marshall to JSON "+uiFieldList,e);
		 }
	
	}
	
	/**
	 * deletes single row from UIField table
	 * @param uiFieldId
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)	
	public String deleteSampleDraftJSON(@RequestParam("id") Integer uiFieldId,HttpServletResponse response) {
		
		this.uiFieldDao.remove(uiFieldDao.findById(uiFieldId));
		
		try {
			response.getWriter().println(messageService.getMessage("uiField.removed.data"));
			return null;
		} catch (Throwable e) {
			throw new IllegalStateException("Cant output success message ",e);
		}
	}


	/**
	 * updates single row from UIField table
	 * 
	 * @param uiFieldId
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/detail_rw/updateJSON.do", method = RequestMethod.POST)
	public String updateDetailJSON(@RequestParam("id") Integer uiFieldId, UiField uiFieldForm, ModelMap m, HttpServletResponse response) {
		try {
			if (uiFieldId == null || uiFieldId == 0 ) {

				if (uiFieldDao.exists(uiFieldForm.getLocale(), uiFieldForm.getArea(), uiFieldForm.getName(), uiFieldForm.getAttrName())) {
					response.getWriter().println(messageService.getMessage("uiField.not_unique.error"));
					return null;
				}

				uiFieldForm.setAttrValue(escapeSingleQuote(uiFieldForm.getAttrValue()));
				
				UiField uiFieldDb = this.uiFieldDao.save(uiFieldForm);

				uiFieldId = uiFieldDb.getUiFieldId();
			} else {
				UiField uiFieldDb = this.uiFieldDao.getById(uiFieldId);
				uiFieldDb.setArea(uiFieldForm.getArea());
				uiFieldDb.setAttrName(uiFieldForm.getAttrName());
				uiFieldDb.setAttrValue(escapeSingleQuote(uiFieldForm.getAttrValue()));
				uiFieldDb.setLocale(uiFieldForm.getLocale());
				uiFieldDb.setName(uiFieldForm.getName());
				this.uiFieldDao.merge(uiFieldDb);
			}

			String newKey = uiFieldForm.getArea() + "." + uiFieldForm.getName() + "." + uiFieldForm.getAttrName();

			String lang = uiFieldForm.getLocale().substring(0, 2);
			String cntry = uiFieldForm.getLocale().substring(3);

			Locale locale = new Locale(lang, cntry);

			((WaspMessageSourceImpl) messageSource).addMessage(newKey, locale, escapeSingleQuote(uiFieldForm.getAttrValue()));

			response.getWriter().println(messageService.getMessage("uiField." + (uiFieldId == null ? "added" : "updated") + ".data"));
			return null;
		} catch (Throwable e) {
			throw new IllegalStateException("Cant output success message ", e);
		}

	}
	
	//single quotes are not allowed in attr values because they might break JavaScript code in other pages.
	private String escapeSingleQuote(String in) {
		if (in==null) return in;
		
		String result=in.replaceAll("'", "&#39;");
		
		return result;
	}
	//START: utility functions to dump strings to files 
	public static void appendToFile(final InputStream in, final File f)
			throws IOException {
		OutputStream os = outStream(f);
		IOUtils.copy(in, os);
		os.flush();
	}

	public static void appendToFile(final String in, final File f)
			throws IOException {
		appendToFile(IOUtils.toInputStream(in), f);
	}

	private static OutputStream outStream(final File f) throws IOException {
		return new BufferedOutputStream(new FileOutputStream(f, true));
	}
	//END: utility functions to dump strings to files 
	
	/**
	 * dumps content of UIField db table as SQL "insert" statements to a file.
	 */
	@RequestMapping(value = "/dump", method = RequestMethod.GET)	
	public String dumpUiFieldTable(HttpServletResponse response) {
		
		
		try {
			String sql=this.uiFieldDao.dumpUiFieldTable();
			String mimeType = "application/octet-stream";
			response.setContentType(mimeType);
			response.setContentLength( sql.getBytes("UTF-8").length);
			response.setHeader( "Content-Disposition", "attachment; filename=\"uifield.update.sql\"" );
			PrintWriter writer=response.getWriter();			
			writer.print(sql);
			writer.flush();
			writer.close();
			return null;
		} catch (Throwable e) {
			throw new IllegalStateException("Cant output success message ",e);
		}
	}
    
}


