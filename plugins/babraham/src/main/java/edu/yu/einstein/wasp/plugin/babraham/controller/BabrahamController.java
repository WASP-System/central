/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.babraham.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.yu.einstein.wasp.controller.WaspController;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.plugin.babraham.service.BabrahamService;


@Controller
@RequestMapping("/babraham")
public class BabrahamController extends WaspController {
	
	@Autowired
	private MessageServiceWebapp messageService;
	
	@Autowired
	private BabrahamService babrahamService;
	
	@RequestMapping(value="/fastqc/description", method=RequestMethod.GET)
	public String displayFastQcDescription(ModelMap m){
		logger.debug("service said: " + babrahamService.performAction());
		return "babraham/fastqc/description";
	}
	
	@RequestMapping(value="/fastqscreen/description", method=RequestMethod.GET)
	public String displayFastQScreenDescription(ModelMap m){
		logger.debug("service said: " + babrahamService.performAction());
		return "babraham/fastqscreen/description";
	}

}
