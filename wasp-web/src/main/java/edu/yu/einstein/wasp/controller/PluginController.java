package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.yu.einstein.wasp.interfacing.plugin.WebInterfacing;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.web.WebHyperlink;

@Controller
@RequestMapping("/plugin")
public class PluginController extends WaspController {
	
	@Autowired
	private WaspPluginRegistry pluginRegistry;
	
	private MessageService messageService;

	@Autowired
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	public PluginController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public String getPluginList(ModelMap m){
		List<WebHyperlink> registeredDescriptions = new ArrayList<WebHyperlink>();
		for (WebInterfacing webPlugin : pluginRegistry.getPlugins(WebInterfacing.class))
			registeredDescriptions.add(new WebHyperlink(webPlugin.getDescriptionPageHyperlink(), messageService));
		Collections.sort(registeredDescriptions);
		m.addAttribute("descriptionHyperlinks", registeredDescriptions);
		return "plugin/list";
	}

}
