package edu.yu.einstein.wasp.controller.fastq;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.yu.einstein.wasp.controller.WaspController;

@Controller
@RequestMapping("/wasp-fastq")
public class WaspFastqController extends WaspController {

	@RequestMapping(value="/description", method=RequestMethod.GET)
	public String displayDescription(ModelMap m){
		return "fastq/description";
	}
	

}
