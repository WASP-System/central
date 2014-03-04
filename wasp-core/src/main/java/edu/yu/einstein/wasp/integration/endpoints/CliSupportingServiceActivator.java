package edu.yu.einstein.wasp.integration.endpoints;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.plugin.cli.CliMessagingTask;
import edu.yu.einstein.wasp.plugin.cli.ClientMessageI;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Genome;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Organism;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.SampleService;


public class CliSupportingServiceActivator implements ClientMessageI, CliSupporting{
	
	
	@Autowired
	private GenomeService genomeService;
	
	@Autowired
	private WaspPluginRegistry pluginRegistry;
	
	@Autowired
	private SampleService sampleService;

	public CliSupportingServiceActivator() {
	}

	/**
	 * ServiceActivator called method
	 */
	@Override
	public Message<String> process(Message<?> m) throws RemoteException {
		if (m.getPayload().toString().equals(CliMessagingTask.LIST_PLUGINS)) {
			return listPlugins();
		} else if (m.getPayload().toString().equals(CliMessagingTask.LIST_GENOME_BUILDS)) {
			return listGenomeBuilds();
		} else if (m.getPayload().toString().equals(CliMessagingTask.LIST_SAMPLE_SUBTYPES)) {
			return listSampleSubtypes();
		} else {
			String mstr = "Unknown command: " + m.toString() + "'\n";
			return MessageBuilder.withPayload(mstr).build();
		}
	}

	@Transactional("entityManager")
	@Override
	public Message<String> listPlugins() {
		String reply = "\nRegistered Wasp System plugins:\n"
				+ "-------------------------------\n\n";
		int index = 1;
		List<WaspPlugin> pluginList = new ArrayList<WaspPlugin>(pluginRegistry.getPlugins().values());
		Collections.sort(pluginList);
		for (WaspPlugin plugin : pluginList) {
			reply += index++ + ") " + plugin.getIName();
			if (plugin.getDescription() != null && !plugin.getDescription().isEmpty()) 
				reply += " -> " + plugin.getDescription();
			reply += "\n";
		}
		return MessageBuilder.withPayload(reply).build();
	}

	@Transactional("entityManager")
	@Override
	public Message<String> listGenomeBuilds(){
		String list = "\nList of possible genome builds:\n";
		for (Organism o : genomeService.getOrganisms())
			for (Genome g : o.getGenomes().values())
				for (Build b : g.getBuilds().values())
					list += "    " + genomeService.getDelimitedParameterString(b) + " (" + b.getDescription() + ")\n";
		 return MessageBuilder.withPayload(list).build();
	}
	
	@Transactional("entityManager")
	@Override
	public Message<String> listSampleSubtypes(){
		String list = "\nList of Sample Subtypes:\n";
		for (SampleSubtype sst : sampleService.getSampleSubtypeDao().findAll())
			if (sst.getIsActive() == 1)
				list += "    " + sst.getId() + " (" + sst.getName() + ")\n";
		return MessageBuilder.withPayload(list).build();
	}
	
}
