package edu.yu.einstein.wasp.controller.chipseq;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.yu.einstein.wasp.controller.WaspController;

@Controller
@RequestMapping("/wasp-chipseq")
public class WaspChipSeqController extends WaspController {

	@RequestMapping(value="/description", method=RequestMethod.GET)
	public String displayDescription(ModelMap m){
		return "waspchipseq/description";
	}
	

}
