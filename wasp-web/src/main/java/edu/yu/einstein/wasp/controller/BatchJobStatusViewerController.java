package edu.yu.einstein.wasp.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
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
import edu.yu.einstein.wasp.controller.util.ExtModel;
import edu.yu.einstein.wasp.controller.util.ExtStepInfoModel;
import edu.yu.einstein.wasp.controller.util.ExtGridResponse;
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
		m.addAttribute("startPage", 1);
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
		ExtGridResponse<ExtTreeModel> extTreeGridResponse;
		if (sort == null)
			extTreeGridResponse = statusViewerService.getPagedModelList(node, displayParam, start, limit);
		else {
			JSONObject sortInfo = new JSONObject(sort.replace("[", "").replace("]", ""));
			extTreeGridResponse = statusViewerService.getPagedModelList(node, displayParam, sortInfo.getString("property"), sortInfo.getString("direction"), start, limit);
		}
		return outputJSON(replaceExitCodesWithIcons(extTreeGridResponse), response);
	}
	
	@RequestMapping(value="/getStepInfoJson", method = RequestMethod.GET)
	public String getStepInfoJson(@RequestParam(value="jobExecutionId", required=true) Long jobExecutionId,
			@RequestParam(value="stepName", required=true) String stepName,
			HttpServletResponse response) throws WaspException, JsonMappingException, IOException {
		ExtStepInfoModel extStepInfoModel = statusViewerService.getExtStepInfoModel(jobExecutionId, stepName);
		ExtGridResponse<ExtStepInfoModel> extTreeGridResponse = new ExtGridResponse<>();
		extTreeGridResponse.addModel(extStepInfoModel);
		extTreeGridResponse.setTotalCount(1L);
		return outputJSON(extTreeGridResponse, response);
	}
	
	private ExtGridResponse<ExtTreeModel> replaceExitCodesWithIcons(ExtGridResponse<ExtTreeModel> extTreeGridResponse) {
		for (ExtTreeModel treeModel: extTreeGridResponse.getModelList()){
			BatchJobTreeModel batchJobTreeModel = (BatchJobTreeModel) treeModel;
			String exitCode = batchJobTreeModel.getExitCode();
			if (exitCode.equals(ExitStatus.EXECUTING.getExitCode()))
				batchJobTreeModel.setExitCode("<img src='" + getServletPath() + "/images/gears_green_30x30.png' alt='running (live thread)' height='15'/>");
			else if (exitCode.equals(ExitStatus.UNKNOWN.getExitCode()))
				batchJobTreeModel.setExitCode("<img src='" + getServletPath() + "/images/gears_green_30x30.png' alt='running (live thread)' height='15' />");
			else if (exitCode.equals(ExitStatus.ERROR.getExitCode()))
				batchJobTreeModel.setExitCode("<img src='" + getServletPath() + "/images/warning.png' alt='Error Condition' height='15' />");
			else if (exitCode.equals(ExitStatus.HIBERNATING.getExitCode())){
				try {
					DateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
					DateTime startDateTime = new DateTime(df.parse(batchJobTreeModel.getStartTime()).getTime());
					if (startDateTime.plusMonths(1).isBeforeNow())
						batchJobTreeModel.setExitCode("<img src='" + getServletPath() + "/images/gears_red_zzz_30x30.png' alt='running (hiberating &gt; 1 month)' height='15' />");
					else if (startDateTime.plusWeeks(1).isBeforeNow())
						batchJobTreeModel.setExitCode("<img src='" + getServletPath() + "/images/gears_orange_zzz_30x30.png' alt='running (hiberating &gt; 1 week)' height='15' />");
					else
						batchJobTreeModel.setExitCode("<img src='" + getServletPath() + "/images/gears_green_zzz_30x30.png' alt='running (hiberating)' height='15' />");
				} catch (ParseException e) {
					logger.warn("unable to parse to DateTime: " + batchJobTreeModel.getStartTime());
					batchJobTreeModel.setExitCode("<img src='" + getServletPath() + "/images/gears_green_zzz_30x30.png' alt='running (hiberating)' height='15' />");
				}
				
			}
			else if (exitCode.equals(ExitStatus.COMPLETED.getExitCode()))
				batchJobTreeModel.setExitCode("<img src='" + getServletPath() + "/images/pass.png' alt='completed' height='15' />");
			else if (exitCode.equals(ExitStatus.TERMINATED.getExitCode()) || exitCode.equals(ExitStatus.STOPPED.getExitCode()))
				batchJobTreeModel.setExitCode("<img src='" + getServletPath() + "/images/stop_yellow_25x25.png' alt='stopped' height='15' />");
			else if (exitCode.equals(ExitStatus.FAILED.getExitCode()))
				batchJobTreeModel.setExitCode("<img src='" + getServletPath() + "/images/fail.png' alt='failed' height='15' />");
		}
		return extTreeGridResponse;
	}
	
	private String outputJSON(ExtGridResponse<? extends ExtModel> extTreeGridResponse, HttpServletResponse response) throws JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String json=mapper.writeValueAsString(extTreeGridResponse);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().print(json);
		return null;	
	}

}
