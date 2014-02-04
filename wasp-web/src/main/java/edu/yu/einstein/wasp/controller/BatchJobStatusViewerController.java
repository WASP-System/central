package edu.yu.einstein.wasp.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.yu.einstein.wasp.controller.util.ExtTreeGridResponse;
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
			@RequestParam(value="limit", required=false) Long limit,
			@RequestParam(value="page", required=false) Long page,
			@RequestParam(value="start", required=false) Long start,
			@RequestParam(value="sort", required=false) String sort,
			HttpServletResponse response) throws WaspException, JsonMappingException, IOException {
		logger.debug("Getting model data for node=" + node + ", limit=" + limit + ", page=" + page + ", start=" + start + ", sort=" + sort);
		if (sort == null)
			return outputJSON(statusViewerService.getPagedModelList(node, start, limit), response);
		JSONObject sortInfo = new JSONObject(sort.replace("[", "").replace("]", ""));
		return outputJSON(statusViewerService.getPagedModelList(node, sortInfo.getString("property"), sortInfo.getString("direction"), start, limit), response);
	}
	
	private  String outputJSON(ExtTreeGridResponse extTreeGridResponse, HttpServletResponse response) throws JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String json=mapper.writeValueAsString(extTreeGridResponse);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().print(json);
		return null;	
	}

}
