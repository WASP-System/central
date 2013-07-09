/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package ___package___.___pluginname___.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.yu.einstein.wasp.controller.WaspController;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import ___package___.___pluginname___.service.___Pluginname___Service;


@Controller
@RequestMapping("/___pluginname___")
public class ___Pluginname___Controller extends WaspController {
	
	@Autowired
	private MessageServiceWebapp messageService;
	
	@Autowired
	private ___Pluginname___Service ___pluginname___Service;
	
	@RequestMapping(value="/description", method=RequestMethod.GET)
	public String displayDescription(ModelMap m){
		logger.debug("service said: " + ___pluginname___Service.performAction());
		return "___pluginname___/description";
	}

}
