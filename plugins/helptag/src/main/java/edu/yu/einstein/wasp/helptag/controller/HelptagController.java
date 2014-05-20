/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.helptag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.yu.einstein.wasp.controller.WaspController;
import edu.yu.einstein.wasp.helptag.service.HelptagService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;


@Controller
@RequestMapping("/helptag")
public class HelptagController extends WaspController {
	
	@Autowired
	private MessageServiceWebapp messageService;
	
	@Autowired
	private HelptagService helptagService;
	
	@RequestMapping(value="/displayDescription", method=RequestMethod.GET)
	public String displayDescription(ModelMap m){
		logger.debug("service said: " + helptagService.performAction());
		return "helptag/description";
	}

}
