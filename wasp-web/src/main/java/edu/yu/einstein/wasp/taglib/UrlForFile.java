package edu.yu.einstein.wasp.taglib;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.file.FileUrlResolver;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.service.FileService;

public class UrlForFile extends BodyTagSupport {
	

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Integer fileHandleId;
	
	private FileHandle fileHandle;
	
	public void setFileHandle(FileHandle fileHandle) {
		this.fileHandle = fileHandle;
	}
	
	public void setFileHandleId(Integer fileHandleId) {
		this.fileHandleId = fileHandleId;
	}
	
	
	@Override
	public int doEndTag() throws JspException {
				
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(this.pageContext.getServletContext());
		
		Map<String, FileUrlResolver> fileUrlResolversByName = context.getBeansOfType(FileUrlResolver.class);
		List<FileUrlResolver> fileUrlResolvers = new ArrayList<FileUrlResolver>();
		fileUrlResolvers.addAll(fileUrlResolversByName.values());
		if (fileUrlResolvers.size() < 1) 
			throw new JspException("no Beans found of type FileUrlResolver in pageContext");
		if (fileUrlResolvers.size() > 1) 
			throw new JspException("More than one Bean found of type FileUrlResolver in pageContext");
		FileUrlResolver fileUrlResolver = fileUrlResolvers.get(0);
		
		Map<String, FileService> fileServicesByName = context.getBeansOfType(FileService.class);
		List<FileService> fileServices = new ArrayList<FileService>();
		fileServices.addAll(fileServicesByName.values());
		if (fileServices.size() < 1) 
			throw new JspException("no Beans found of type FileService in pageContext");
		if (fileServices.size() > 1) 
			throw new JspException("More than one Bean found of type FileService in pageContext");
		FileService fileService = fileServices.get(0);
		
		URL url = null;
		try{
			if (fileHandle != null)
				url = fileUrlResolver.getURL((FileHandle) fileHandle);
			else if (fileHandleId != null)
				url = fileUrlResolver.getURL(fileService.getFileHandleById(fileHandleId));
			else
				throw new JspException("No Filehandle has been provided");
		} catch (GridUnresolvableHostException e){
			throw new JspException("Unable to resolve grid host", e);
		}
		if (url == null)
			return Tag.SKIP_PAGE;
		
		StringBuffer buf = new StringBuffer("");
		buf.append(url);
		
		try {
			this.pageContext.getOut().print(buf.toString());
		} catch (IOException e) {
			throw new JspTagException(e.getMessage());
		}

		return Tag.EVAL_PAGE;
	}

}
