package edu.yu.einstein.wasp.integration.endpoints;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.plugin.cli.CliMessagingTask;
import edu.yu.einstein.wasp.plugin.cli.ClientMessageI;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Genome;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Organism;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.WorkflowService;


public class CliSupportingServiceActivator implements ClientMessageI, CliSupporting{
	
	
	@Autowired
	private GenomeService genomeService;
	
	@Autowired
	private WaspPluginRegistry pluginRegistry;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private WorkflowService workflowService;

	public CliSupportingServiceActivator() {
	}

	/**
	 * ServiceActivator called method
	 */
	@Override
	public Message<?> process(Message<?> m) throws RemoteException {
		String payloadStr = m.getPayload().toString();
		if (payloadStr.startsWith("{") && payloadStr.endsWith("}")){ // looks like JSON to me
			return processImportedFileRegistrationData(new JSONObject(payloadStr));
		} else if (payloadStr.equals(CliMessagingTask.LIST_PLUGINS)) {
			return listPlugins();
		} else if (payloadStr.equals(CliMessagingTask.LIST_GENOME_BUILDS)) {
			return listGenomeBuilds();
		} else if (payloadStr.equals(CliMessagingTask.LIST_SAMPLE_SUBTYPES)) {
			return listSampleSubtypes();
		} else if (payloadStr.equals(CliMessagingTask.LIST_CELL_LIBRARIES)) {
			return listCellLibraries();
		} else if (payloadStr.equals(CliMessagingTask.LIST_FILE_TYPES)) {
			return listFileTypes();
		} else if (payloadStr.equals(CliMessagingTask.LIST_ASSAY_WORKFLOWS)) {
			return listAssayWorkflows();
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

	@Transactional("entityManager")
	@Override
	public Message<String> listCellLibraries() {
		Map<String, String> cellLibraries = new HashMap<>();
		for (SampleSource cellLibrary : sampleService.getCellLibraries()){
			Sample library = sampleService.getLibrary(cellLibrary);
			String libraryName = library.getName();
			Sample cell = sampleService.getCell(cellLibrary);
			if (cell != null){
				if (!sampleService.isCell(cell))
					continue;
				try {
					libraryName += " (" + sampleService.getPlatformUnitForCell(cell).getName() + " / " + sampleService.getCellIndex(cell) + ")";
				} catch (SampleTypeException | SampleParentChildException e) {
					// just leave as default empty string
				}
			}
			cellLibraries.put(cellLibrary.getId().toString(), libraryName);
		}
		return MessageBuilder.withPayload(new JSONObject(cellLibraries).toString()).build();
	}
	
	@Transactional("entityManager")
	@Override
	public Message<String> listFileTypes(){
		Map<String, String> fileTypes = new HashMap<>();
		for (FileType ft : fileService.getFileTypes())
			fileTypes.put(ft.getId().toString(), ft.getIName() + " (" + ft.getName() + ")");
		return MessageBuilder.withPayload(new JSONObject(fileTypes).toString()).build();
	}
	
	@Transactional("entityManager")
	@Override
	public Message<String> listAssayWorkflows(){
		Map<String, String> workflows = new HashMap<>();
		for (Workflow w : workflowService.getWorkflows())
			workflows.put(w.getId().toString(), w.getName());
		return MessageBuilder.withPayload(new JSONObject(workflows).toString()).build();
	}
	
	@Transactional("entityManager")
	@Override
	public Message<String> processImportedFileRegistrationData(JSONObject data) throws JSONException, IOException{
		JSONArray lineArray = data.toJSONArray(new JSONArray());
		List<String> headerList = new ArrayList<>();
		for (int i=0; i < lineArray.length(); i++){
			JSONArray lineElementList = lineArray.getJSONArray(i);
			SampleSource currentCellLibrary = null;
			Job currentJob = null;
			Sample currentSample = null;
			FileGroup currentFileGroup = null;
			FileHandle currentFileHandle = null;
			for (int j = 0; j < lineElementList.length(); j++){
				String element = lineElementList.getString(j);
				if (i == 0){ // first line
					headerList.add(element);
					continue;
				}	
				String heading = headerList.get(j);
				try {
					if (heading.equals("cellLibraryId")){
						Integer id = Integer.parseInt(element);
						if (currentCellLibrary == null || !currentCellLibrary.getId().equals(id))
							currentCellLibrary = sampleService.getCellLibraryBySampleSourceId(id);
						if (currentCellLibrary == null)
							throw new IOException("Unable to get cellLibrary with id=" + element);
					} else {
						String model = heading.substring(0, heading.indexOf("."));
						String attributeName = heading.substring(heading.indexOf(".") + 1);
						if (model.equals("Job")){
							if (attributeName.equals("name")){
								if (currentJob == null || !currentJob.getName().equals(element)){
									currentJob = new Job();
									currentJob.setName(element);
									currentJob = jobService.getJobDao().save(currentJob);
								}
							} else if (attributeName.equals("workflowId")){
								Workflow wf = workflowService.getWorkflowDao().findById(Integer.parseInt(element));
								if (wf == null || wf.getId() == null)
									throw new IOException("Unable to get workflow with id=" + element);
								if (currentJob != null && !wf.getId().equals(currentJob.getWorkflowId()) )
									currentJob.setWorkflow(wf);
							}
						} else if (model.equals("JobMeta")){
							JobMeta meta = new JobMeta();
							meta.setK(attributeName);
							meta.setV(element);
							meta.setJob(currentJob);
							jobService.getJobMetaDao().setMeta(meta);
						} else if (model.equals("Sample")){
							if (attributeName.equals("name")){
								if (currentSample == null || !currentSample.getName().equals(element)){
									currentSample = new Sample();
									currentSample.setName(element);
									currentSample = sampleService.getSampleDao().save(currentSample);
									currentCellLibrary = new SampleSource();
									currentCellLibrary.setSourceSample(currentSample);
									currentCellLibrary = sampleService.getSampleSourceDao().save(currentCellLibrary);
									sampleService.setJobForLibraryOnCell(currentCellLibrary, currentJob);
								}
							} else if (attributeName.equals("sampleType")){
								SampleSubtype ss = sampleService.getSampleSubtypeById(Integer.parseInt(element));
								if (ss == null || ss.getId() == null)
									throw new IOException("Unable to get sampleSubtype with id=" + element);
								if (currentSample != null && !ss.getId().equals(currentSample.getSampleSubtypeId()) )
									currentSample.setSampleSubtype(ss);
							}
						} else if (model.equals("SampleMeta")){
							SampleMeta meta = new SampleMeta();
							meta.setK(attributeName);
							meta.setV(element);
							meta.setSample(currentSample);
							sampleService.getSampleMetaDao().setMeta(meta);
						} else if (model.equals("FileGroup")){
							
						} else if (model.equals("FileHandle")){
							
						}
						
					}
				} catch (SampleTypeException | NumberFormatException | MetadataException e) {
					throw new IOException("Unable to parse or persist data value for element=" + element + ": " + e.getCause().toString());
				}
			}
		}
		
	}
	
	
}
