package edu.yu.einstein.wasp.taglib;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.service.FileService;
/**
 * Class for resolving file handle objects or ids to urls
 * @author asmclellan
 *
 */
public class UrlForFile extends BodyTagSupport {
	

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Object fileAccessor;
		
	public void setFileAccessor(Object fileAccessor) {
		this.fileAccessor = fileAccessor;
	}
	
	
	@Override
	public int doEndTag() throws JspException {
		StringBuilder buf = new StringBuilder("");
		URL url = null;
		try{
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
			
			try{
				if (fileAccessor == null)
					throw new JspException("No object (accessor) with which to obtain a file has been provided");
				if (FileHandle.class.isInstance(fileAccessor))
					url = fileUrlResolver.getURL((FileHandle) fileAccessor);
				else if (FileGroup.class.isInstance(fileAccessor))
					url = fileUrlResolver.getURL((FileGroup) fileAccessor);
				else {
					try{
						UUID uuid = UUID.fromString((String) fileAccessor);
						url = fileUrlResolver.getURL(fileService.getFileHandle(uuid));
					} catch (IllegalArgumentException e){
						// not a valid UUID so try an integer
						try{
							Integer fileHandleId = Integer.parseInt( (String) fileAccessor);
							url = fileUrlResolver.getURL(fileService.getFileHandleById(fileHandleId));
						} catch (NumberFormatException e1){
							throw new JspException("No Filehandle has been provided", e1);
						}
					}
				}
			} catch (GridUnresolvableHostException e){
				logger.warn("Unable to resolve grid host", e);
			} catch (FileNotFoundException e1){
				logger.warn("Unable to find file", e1);
			}
		} catch (Exception e){
			logger.warn(e.getLocalizedMessage());
		}
		if (url == null)
			buf.append("#");
		else
			buf.append(url);
		try {
			this.pageContext.getOut().print(buf.toString());
		} catch (IOException e) {
			throw new JspTagException(e.getMessage());
		}
		return Tag.EVAL_PAGE;
	}
}
