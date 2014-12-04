/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package ___package___.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.yu.einstein.wasp.controller.WaspController;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import ___package___.service.___PluginIName___Service;


@Controller
@RequestMapping("/___pluginIName___")
public class ___PluginIName___Controller extends WaspController {
	
	@Autowired
	private MessageServiceWebapp messageService;
	
	@Autowired
	private ___PluginIName___Service ___pluginIName___Service;
	
	@RequestMapping(value="/displayDescription", method=RequestMethod.GET)
	public String displayDescription(ModelMap m){
		logger.debug("service said: " + ___pluginIName___Service.performAction());
		return "___pluginIName___/description";
	}

}
