package edu.yu.einstein.wasp.integration.endpoints;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
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
	public Message<?> process(Message<?> m) throws RemoteException {
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
		Map<String, String> plugins = new HashMap<>();
		List<WaspPlugin> pluginList = new ArrayList<WaspPlugin>(pluginRegistry.getPlugins().values());
		for (WaspPlugin plugin : pluginList) 
			plugins.put(plugin.getIName(), plugin.getDescription());
		return MessageBuilder.withPayload(new JSONObject(plugins).toString()).build();
	}

	@Transactional("entityManager")
	@Override
	public Message<String> listGenomeBuilds(){
		Map<String, String> builds = new HashMap<>();
		for (Organism o : genomeService.getOrganisms())
			for (Genome g : o.getGenomes().values())
				for (Build b : g.getBuilds().values())
					builds.put(genomeService.getDelimitedParameterString(b), b.getDescription());
		return MessageBuilder.withPayload(new JSONObject(builds).toString()).build();
	}
	
	@Transactional("entityManager")
	@Override
	public Message<String> listSampleSubtypes(){
		Map<String, String> sampleSubtypes = new HashMap<>();
		for (SampleSubtype sst : sampleService.getSampleSubtypeDao().findAll())
			if (sst.getIsActive() == 1)
				sampleSubtypes.put(sst.getId().toString(), sst.getName());
		return MessageBuilder.withPayload(new JSONObject(sampleSubtypes).toString()).build();
	}
	
}
