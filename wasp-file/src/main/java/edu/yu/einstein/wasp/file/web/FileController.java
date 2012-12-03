/**
 * 
 */
package edu.yu.einstein.wasp.file.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.yu.einstein.wasp.service.FileService;

/**
 * @author calder
 * 
 */
@Controller
public class FileController {
	
	@Autowired
	private FileService fileService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(method = RequestMethod.GET, value = "/file/{id}")
	public void getFile(@PathVariable("id") String id, HttpServletResponse response) {
		try {
			edu.yu.einstein.wasp.model.File wf = fileService.getFileByFileId(i);
			String content = new MediaType("application", "octet-stream").toString();
			if (wf.getContentType() != null && wf.getContentType() != "")
				content = wf.getContentType();
			response.setContentType(content);
			String filename = "";
			response.setHeader("Content-Disposition", "attachment;filename=" + filename);
			
			int i = new Integer(id).intValue();
			
			
			
			File f = new File("~/" + wf) ;
			
			InputStream is = new FileInputStream(f);
			// copy it to response's OutputStream
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
			is.close();
		} catch (IOException ex) {
			logger.warn("Error writing file " + id + "to output stream.");
			throw new RuntimeException("IOError writing file to output stream");
		}

	}
}
