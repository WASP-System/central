package edu.yu.einstein.wasp.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.springframework.batch.core.ExitStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.yu.einstein.wasp.controller.util.BatchJobTreeModel;
import edu.yu.einstein.wasp.controller.util.ExtTreeGridResponse;
import edu.yu.einstein.wasp.controller.util.ExtTreeModel;
import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.service.BatchJobStatusViewerService;

@RequestMapping("/batchJobStatusViewer")
@Controller
public class BatchJobStatusViewerController extends WaspController {
	
	@Autowired
	BatchJobStatusViewerService statusViewerService;
	
	@RequestMapping(value="/list.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('ft')")
	public String getBatchJobStatusViewerView(ModelMap m){
		return "batchJobStatusViewer/list";
	}
	
	@RequestMapping(value="/getDetailsJson", method = RequestMethod.GET)
	public String getNodeJson(@RequestParam(value="node", required=true) String node, 
			@RequestParam(value="displayParam", required=true) String displayParam,
			@RequestParam(value="limit", required=false) Long limit,
			@RequestParam(value="page", required=false) Long page,
			@RequestParam(value="start", required=false) Long start,
			@RequestParam(value="sort", required=false) String sort,
			HttpServletResponse response) throws WaspException, JsonMappingException, IOException {
		logger.debug("Getting model data for node=" + node + ", limit=" + limit + ", page=" + page + ", start=" + start + ", sort=" + sort);
		ExtTreeGridResponse extTreeGridResponse;
		if (sort == null)
			extTreeGridResponse = statusViewerService.getPagedModelList(node, displayParam, start, limit);
		else {
			JSONObject sortInfo = new JSONObject(sort.replace("[", "").replace("]", ""));
			extTreeGridResponse = statusViewerService.getPagedModelList(node, displayParam, sortInfo.getString("property"), sortInfo.getString("direction"), start, limit);
		}
		return outputJSON(replaceExitCodesWithIcons(extTreeGridResponse), response);
	}
	
	private ExtTreeGridResponse replaceExitCodesWithIcons(ExtTreeGridResponse extTreeGridResponse){
		for (ExtTreeModel treeModel: extTreeGridResponse.getModelList()){
			BatchJobTreeModel batchJobTreeModel = (BatchJobTreeModel) treeModel;
			String exitCode = batchJobTreeModel.getExitCode();
			if (exitCode.equals(ExitStatus.EXECUTING.getExitCode()))
				batchJobTreeModel.setExitCode("<img src='" + getServletPath() + "/images/gears_green_30x30.png' height='15'/>");
			else if (exitCode.equals(ExitStatus.UNKNOWN.getExitCode()))
				batchJobTreeModel.setExitCode("<img src='" + getServletPath() + "/images/gears_green_30x30.png' height='15' />");
			else if (exitCode.equals(ExitStatus.HIBERNATING.getExitCode()))
				batchJobTreeModel.setExitCode("<img src='" + getServletPath() + "/images/gears_green_zzz_30x30.png' height='15' />");
			else if (exitCode.equals(ExitStatus.COMPLETED.getExitCode()))
				batchJobTreeModel.setExitCode("<img src='" + getServletPath() + "/images/pass.png' height='15' />");
			else if (exitCode.equals(ExitStatus.TERMINATED.getExitCode()))
				batchJobTreeModel.setExitCode("<img src='" + getServletPath() + "/images/stop_yellow_25x25.png' height='15' />");
			else if (exitCode.equals(ExitStatus.FAILED.getExitCode()))
				batchJobTreeModel.setExitCode("<img src='" + getServletPath() + "/images/fail.png' height='15' />");
		}
		return extTreeGridResponse;
	}
	
	private  String outputJSON(ExtTreeGridResponse extTreeGridResponse, HttpServletResponse response) throws JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String json=mapper.writeValueAsString(extTreeGridResponse);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().print(json);
		return null;	
	}

}
