/**
 * 
 */
package edu.yu.einstein.wasp.file.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javassist.util.proxy.ProxyFactory;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Job;
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

	@RequestMapping(method = RequestMethod.GET, value = "/get/{id}")
	public void getFile(@PathVariable("id") String id, HttpServletResponse response) {

		try {
			Integer i;
			try {
				i = new Integer(id);
			} catch (NumberFormatException e) {
				throw new WaspException("unable to search for record " + id);
			}
			FileHandle wf = fileService.getFileHandleById(i);
			
			if (wf == null || !(wf instanceof FileHandle)) {
				logger.debug("not in db");
				throw new WaspException("FileHandle not in database");
			} else {
				logger.debug(wf.toString());
			}
			if (wf.getId() == null) {
				logger.debug("empty object");
				throw new WaspException("FileHandle is not known");
			}

			if (!wf.getFileURI().toString().startsWith("file://")) {
				logger.warn("This implementation only handles file URIs. URI: " + wf.getFileURI());
				throw new WaspException("unable to resolve URI: " + wf.getId());
			}

			Matcher filem = Pattern.compile("^file://(.*?)/(.*)$").matcher(wf.getFileURI().normalize().toString());

			if (!filem.find()) {
				logger.warn("unable to parse URI: " + wf.getFileURI().toString());
				throw new WaspException("unable to parse URI: " + wf.getFileId());
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

			java.io.File download = new java.io.File(prefix + "/" + folder + filename);
			
			if (!download.exists()) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}

			InputStream is = new FileInputStream(download);

			String content = new MediaType("application", "octet-stream").toString();
			response.setContentType(content);

			response.setHeader("Content-Disposition", "attachment;filename=" + filename);
			response.setHeader( "Content-Length", String.valueOf( download.length() ));

			// copy it to response's OutputStream
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
			is.close();
		} catch (IOException ex) {
			logger.warn("Error writing file " + id + " to output stream.");
			ex.printStackTrace();
		} catch (WaspException e) {
			logger.warn("unable to deliver file: " + id);
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
