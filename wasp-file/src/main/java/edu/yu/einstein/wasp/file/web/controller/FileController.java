/**
 * 
 */
package edu.yu.einstein.wasp.file.web.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.ConfigurableMimeFileTypeMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobService;

/**
 * @author calder
 * 
 */
@Controller
public class FileController implements InitializingBean {

	@Autowired
	private FileService fileService;
	
	@Autowired
	private JobService jobService;

	@Autowired
	private Properties waspSiteProperties;

	private Map<String, String> hosts = new HashMap<String, String>();

	private Map<String, String> roots = new HashMap<String, String>();

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(method = RequestMethod.GET, value = "/get/file/{uuid}")
	public void getFile(@PathVariable("uuid") String uuid, HttpServletResponse response) {

		try {
			UUID uu;
			try {
				uu = UUID.fromString(uuid);
			} catch (NumberFormatException e) {
				throw new WaspException("unable to search for record " + uuid);
			}
			
			FileHandle wf;
			try {
				 wf = fileService.getFileHandle(uu);
				 logger.debug(wf.toString());
			} catch (FileNotFoundException e1) {
				logger.debug(uuid.toString() + " not in db");
				throw new WaspException("FileHandle not in database");
			}

			if (wf.getId() == null) {
				logger.debug("empty object");
				throw new WaspException("FileHandle is not known");
			}

			// TODO: non file URL and URN resolution
			if (!wf.getFileURI().toString().startsWith("file://")) {
				logger.warn("This implementation only handles file URIs. URI: " + wf.getFileURI());
				throw new WaspException("unable to resolve URI: " + wf.getId());
			}

			Matcher filem = Pattern.compile("^file://(.*?)/(.*)$").matcher(wf.getFileURI().normalize().toString());

			if (!filem.find()) {
				logger.warn("unable to parse URI: " + wf.getFileURI().toString());
				throw new WaspException("unable to parse URI: " + wf.getId());
			}

			String prefix = "";
			String host = filem.group(1);
			String location = filem.group(2);
			String folder;
			String filename;
			if (location.contains("/")) {
				Matcher locm = Pattern.compile("^(.*/)(.*)$").matcher(location);
				if (!locm.find()) {
					logger.warn("unable to parse file location: " + location);
					throw new WaspException("unable to parse location");
				}
				folder = locm.group(1);
				filename = locm.group(2);
			} else {
				folder = "";
				filename = location;
			}

			if (roots.get(hosts.get(host)).equals("true")) {
				prefix = System.getProperty("user.home");
			}
			
			logger.debug(prefix + "/" + folder + filename);
			
			ConfigurableMimeFileTypeMap mimeMap = new ConfigurableMimeFileTypeMap();
			String contentType = mimeMap.getContentType(filename);
			logger.debug("ContentType of file is: " + contentType);
			
			java.io.File download = new java.io.File(prefix + "/" + folder + filename);
			
			if (!download.exists()) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}

			InputStream is = new FileInputStream(download);
			
			response.setContentType(contentType);
			response.setHeader("Content-Disposition", "attachment;filename=" + filename);
			response.setHeader("Content-Length", String.valueOf( download.length() ));
			
			// set 'max-age' to cache files for up to 1h (3600s) since most files shouldn't change on the server anyway. 
			// We use 'must-revalidate' to force browser to always use this rule. 
			response.setHeader("Cache-Control", "max-age=3600, must-revalidate"); 

			// copy it to response's OutputStream
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
			is.close();
		} catch (IOException ex) {
			logger.warn("Error writing file " + uuid.toString() + " to output stream.");
			ex.printStackTrace();
		} catch (WaspException e) {
			logger.warn("unable to deliver file: " + uuid.toString());
			try {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for (Object k : waspSiteProperties.keySet()) {
			String key = (String) k;
			Matcher hostm = Pattern.compile("^(.+?)\\.settings\\.host$").matcher(key);
			if (hostm.find()) {
				String host = hostm.group(1);
				hosts.put((String) waspSiteProperties.get(k), host);
				continue;
			}
			Matcher rootm = Pattern.compile("^(.+?)\\.settings\\.userDirIsRoot$").matcher(key);
			if (rootm.find()) {
				String root = rootm.group(1);
				roots.put(root, (String) waspSiteProperties.get(k));
			}
		}
	}
}
