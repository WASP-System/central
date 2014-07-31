/**
 * 
 */
package edu.yu.einstein.wasp.plugin.picard.metrics;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.SgeWorkService;

/**
 * Generic parser for Picard Metrics files to JSON
 * 
 * @author calder
 *
 */
public class PicardMetricsParser {
	
	private static final String PICARD_METRICS_CLASS = "picardMetricsClass";
	private static final String PICARD_METRICS_COLNAMES = "picardMetricsColnames";
	private static final String PICARD_METRICS_CONTENT = "picardMetricsContent";
	private static final String PICARD_METRICS_COMMENT = "picardMetricsComment";
	
	@Autowired
	private GridHostResolver hostResolver;
	
	private GridResult gridResult;
	
	private String pathToFile;
	
	private File file;
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public PicardMetricsParser(GridHostResolver hostResolver, GridResult gridResult, String fileName) {
		this.gridResult = gridResult;
		this.file = null;
		this.pathToFile = fileName;
		this.hostResolver = hostResolver;
	}
	
	public JSONObject parseResult() {
		JSONObject result = new JSONObject();
		JSONArray content = new JSONArray();
		String stringContent;
		String comment = "";
		List<String> header = new ArrayList<String>();
		boolean data = false;
		try {
			stringContent = getContents();
		} catch (GridUnresolvableHostException | IOException e) {
			String mess = "unable to access metrics file: " + e.getLocalizedMessage();
			logger.error(mess);
			throw new WaspRuntimeException(mess);
		}
		Scanner scanner = new Scanner(stringContent);
		while (scanner.hasNextLine()) {
		  String line = scanner.nextLine();
		  if (line.startsWith("# ")) {
			  comment += line + " ";
			  continue;
		  }
		  if (line.startsWith("##")) {
			  if (!line.startsWith("## METRICS CLASS")) continue;
			  result.put(PICARD_METRICS_CLASS, StringUtils.chomp(line.split("\t")[1]));
			  continue;
		  }
		  if (StringUtils.chomp(line).length() == 0) {
			  continue;
		  }
		  if (data == false) {
			  header = Arrays.asList(StringUtils.chomp(line).split("\t"));
			  result.put(PICARD_METRICS_COLNAMES, header);
			  data = true;
			  continue;
		  }
		  List<String> theData = Arrays.asList(StringUtils.chomp(line).split("\t"));
		  content.put(theData);
		  
		}
		scanner.close();
		
		if (!comment.equals("")) {
			result.put(PICARD_METRICS_COMMENT, comment);
		}
		result.put(PICARD_METRICS_CONTENT, content);
		return result;
	}
	
	private String getContents() throws GridUnresolvableHostException, IOException {
		String result;
		if (gridResult != null) {
			GridWorkService gws = hostResolver.getGridWorkService(gridResult);
			result = gws.getUnregisteredFileContents(gridResult, pathToFile, SgeWorkService.NO_FILE_SIZE_LIMIT);
		} else {
			file = new File(pathToFile);
			FileInputStream afis = new FileInputStream(file);
			result = IOUtils.toString(afis, "UTF-8");
		}
		return result;
	}
	
	public PicardMetricsParser(String pathToFile) {
		this.gridResult = null;
		this.file = null;
		this.pathToFile = pathToFile;
	}

}
