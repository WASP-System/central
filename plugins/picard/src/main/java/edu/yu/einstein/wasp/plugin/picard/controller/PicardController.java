/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.picard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.yu.einstein.wasp.controller.WaspController;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.plugin.picard.service.PicardService;


@Controller
@RequestMapping("/picard")
public class PicardController extends WaspController {
	
	@Autowired
	private MessageServiceWebapp messageService;
	
	@Autowired
	private PicardService picardService;
	
	@RequestMapping(value="/displayDescription", method=RequestMethod.GET)
	public String displayDescription(ModelMap m){
		logger.debug("service said: " + picardService.performAction());
		return "picard/description";
	}

}
