package edu.yu.einstein.wasp.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/batchJobStatusViewer")
@Controller
public class BatchJobStatusViewer extends WaspController {

	@RequestMapping(value="/list.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('ft')")
	public String getBatchJobStatusViewerView(ModelMap m){
		return "batchJobStatusViewer/list";
	}

}
