package edu.yu.einstein.wasp.controller.illumina;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.yu.einstein.wasp.controller.WaspController;

@Controller
@RequestMapping("/wasp-illumina")
public class illuminaController extends WaspController {

	@RequestMapping(value="/description", method=RequestMethod.GET)
	public String displayDescription(ModelMap m){
		return "/wasp-illumina/description";
	}
	
	@RequestMapping(value="/task/illuminaQC/list", method=RequestMethod.GET)
	public String display(ModelMap m){
		return "/task/illuminaQC/list";
	}
	

}
