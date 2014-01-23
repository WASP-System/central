package edu.yu.einstein.wasp.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/batchJobStatusViewer")
@Controller
public class BatchJobStatusViewer extends WaspController {

	@RequestMapping(value="/list.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('ft')")
	public String getBatchJobStatusViewerView(ModelMap m){
		return "batchJobStatusViewer/list";
	}
	
	@RequestMapping(value="/getDetailsJson", method = RequestMethod.GET)
	public @ResponseBody String getDetailsJson(HttpServletResponse response) {
		return "{\"text\":\".\",\"task\":\"\",\"duration\":\"\",\"user\":\"\",\"iconCls\":\"\",\"expanded\":true,\"leaf\":false,\"children\":[{\"task\":\"Project:Shopping\",\"duration\":13.25,\"user\":\"TommyMaintz\",\"iconCls\":\"task-folder\",\"expanded\":true,\"leaf\":false,\"children\":[{\"task\":\"Housewares\",\"duration\":1.25,\"user\":\"TommyMaintz\",\"iconCls\":\"task-folder\",\"text\":\"\",\"children\":[{\"task\":\"Kitchensupplies\",\"duration\":0.25,\"user\":\"TommyMaintz\",\"leaf\":true,\"expanded\":false,\"iconCls\":\"task\",\"children\":[]},{\"task\":\"Groceries\",\"duration\":.4,\"user\":\"TommyMaintz\",\"leaf\":true,\"iconCls\":\"task\",\"done\":true},{\"task\":\"Cleaningsupplies\",\"duration\":.4,\"user\":\"TommyMaintz\",\"leaf\":true,\"iconCls\":\"task\"},{\"task\":\"Officesupplies\",\"duration\":.2,\"user\":\"TommyMaintz\",\"leaf\":true,\"iconCls\":\"task\"}]},{\"task\":\"Remodeling\",\"duration\":12,\"user\":\"TommyMaintz\",\"iconCls\":\"task-folder\",\"expanded\":true,\"children\":[{\"task\":\"Retilekitchen\",\"duration\":6.5,\"user\":\"TommyMaintz\",\"leaf\":true,\"iconCls\":\"task\"},{\"task\":\"Paintbedroom\",\"duration\":2.75,\"user\":\"TommyMaintz\",\"iconCls\":\"task-folder\",\"children\":[{\"task\":\"Ceiling\",\"duration\":1.25,\"user\":\"TommyMaintz\",\"iconCls\":\"task\",\"leaf\":true},{\"task\":\"Walls\",\"duration\":1.5,\"user\":\"TommyMaintz\",\"iconCls\":\"task\",\"leaf\":true}]},{\"task\":\"Decoratelivingroom\",\"duration\":2.75,\"user\":\"TommyMaintz\",\"leaf\":true,\"iconCls\":\"task\",\"done\":true},{\"task\":\"Fixlights\",\"duration\":.75,\"user\":\"TommyMaintz\",\"leaf\":true,\"iconCls\":\"task\",\"done\":true},{\"task\":\"Reattachscreendoor\",\"duration\":2,\"user\":\"TommyMaintz\",\"leaf\":true,\"iconCls\":\"task\"}]}]},{\"task\":\"Project:Testing\",\"duration\":2,\"user\":\"CoreTeam\",\"iconCls\":\"task-folder\",\"children\":[{\"task\":\"MacOSX\",\"duration\":0.75,\"user\":\"TommyMaintz\",\"iconCls\":\"task-folder\",\"children\":[{\"task\":\"FireFox\",\"duration\":0.25,\"user\":\"TommyMaintz\",\"iconCls\":\"task\",\"leaf\":true},{\"task\":\"Safari\",\"duration\":0.25,\"user\":\"TommyMaintz\",\"iconCls\":\"task\",\"leaf\":true},{\"task\":\"Chrome\",\"duration\":0.25,\"user\":\"TommyMaintz\",\"iconCls\":\"task\",\"leaf\":true}]},{\"task\":\"Windows\",\"duration\":3.75,\"user\":\"DarrellMeyer\",\"iconCls\":\"task-folder\",\"children\":[{\"task\":\"FireFox\",\"duration\":0.25,\"user\":\"DarrellMeyer\",\"iconCls\":\"task\",\"leaf\":true},{\"task\":\"Safari\",\"duration\":0.25,\"user\":\"DarrellMeyer\",\"iconCls\":\"task\",\"leaf\":true},{\"task\":\"Chrome\",\"duration\":0.25,\"user\":\"DarrellMeyer\",\"iconCls\":\"task\",\"leaf\":true},{\"task\":\"InternetExplorer\",\"duration\":3,\"user\":\"DarrellMeyer\",\"iconCls\":\"task\",\"leaf\":true}]},{\"task\":\"Linux\",\"duration\":0.5,\"user\":\"AaronConran\",\"iconCls\":\"task-folder\",\"children\":[{\"task\":\"FireFox\",\"duration\":0.25,\"user\":\"AaronConran\",\"iconCls\":\"task\",\"leaf\":true},{\"task\":\"Chrome\",\"duration\":0.25,\"user\":\"AaronConran\",\"iconCls\":\"task\",\"leaf\":true}]}]}]}";
	}

}
