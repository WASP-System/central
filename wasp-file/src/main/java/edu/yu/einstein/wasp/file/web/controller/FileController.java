/**
 * 
 */
package edu.yu.einstein.wasp.file.web.controller;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.file.web.service.WebFileService;
import edu.yu.einstein.wasp.service.FileService;

/**
 * @author calder
 * 
 */
@Controller
public class FileController {

	@Autowired
	private WebFileService wfService;
	
	@Autowired
	private FileService fileService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(method = {RequestMethod.GET, RequestMethod.HEAD}, value = "/get/file/{uuid:.+}")
	public void getFile(@PathVariable("uuid") String uuid, HttpServletRequest request, HttpServletResponse response) {
		try {
			String adjExtension = "";
			Matcher adjm = Pattern.compile("^(.+)(\\..+)$").matcher(uuid);
			if (adjm.find()) {
				adjExtension = adjm.group(2);
				uuid = adjm.group(1);
			}
			
			wfService.processFileRequest(uuid, adjExtension, request, response);
			
		} catch (IOException ex) {
			if (ex.toString().contains("ClientAbortException")) {
				logger.warn("Client abort when downloading file " + uuid.toString());
			} else {
				logger.warn("Error writing file " + uuid.toString() + " to output stream.");
				ex.printStackTrace();
			}
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

	@RequestMapping(method = {RequestMethod.GET, RequestMethod.HEAD}, value = "/get/filegroups/{uuids:.+}")
	public void getFileGroup(@PathVariable("uuid") String uuids, HttpServletRequest request, HttpServletResponse response) {
		try {
			wfService.processFileGroupListRequest(uuids, request, response);
			
		} catch (IOException ex) {
			if (ex.toString().contains("ClientAbortException")) {
				logger.warn("Client abort when downloading filegroups(" + uuids + ")");
			} else {
				logger.warn("Error writing filegroups(" + uuids + ") to output stream.");
				ex.printStackTrace();
			}
		} catch (WaspException e) {
			logger.warn("unable to deliver filegroups(" + uuids + ")");
			try {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
