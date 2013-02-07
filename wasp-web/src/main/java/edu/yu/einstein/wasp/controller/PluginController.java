package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.plugin.WebInterfacing;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.web.Hyperlink;

@Controller
@RequestMapping("/plugin")
public class PluginController extends WaspController {
	
	@Autowired
	private WaspPluginRegistry pluginRegistry;
	
	private MessageServiceWebapp messageService;

	@Autowired
	public void setMessageService(MessageServiceWebapp messageService) {
		this.messageService = messageService;
	}

	public PluginController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public String getPluginList(ModelMap m){
		List<Hyperlink> registeredPluginDescriptions = new ArrayList<Hyperlink>();
		for (WebInterfacing webPlugin : pluginRegistry.getPlugins(WebInterfacing.class)){
			Hyperlink hyperlink = webPlugin.getDescriptionPageHyperlink();
			hyperlink.setMessageService(messageService);
			registeredPluginDescriptions.add(hyperlink);
		}
		m.addAttribute("pluginDescriptionHyperlinks", registeredPluginDescriptions);
		return "plugin/list";
	}

}
