package edu.yu.einstein.wasp.integration.endpoints;

import java.net.URI;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileGroupMeta;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileHandleMeta;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.User;
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
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.service.WorkflowService;

@Transactional("entityManager")
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
	
	@Autowired
	private UserService userService;
	
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
		} else if (payloadStr.equals(CliMessagingTask.LIST_USERS)) {
			return listUsers();
		} else {
			String mstr = "Unknown command: " + m.toString() + "'\n";
			return MessageBuilder.withPayload(mstr).build();
		}
	}

	@Override
	public Message<String> listPlugins() {
		Map<String, String> plugins = new HashMap<>();
		List<WaspPlugin> pluginList = new ArrayList<WaspPlugin>(pluginRegistry.getPlugins().values());
		for (WaspPlugin plugin : pluginList) 
			plugins.put(plugin.getIName(), plugin.getDescription());
		return MessageBuilder.withPayload(new JSONObject(plugins).toString()).build();
	}

	@Override
	public Message<String> listGenomeBuilds(){
		Map<String, String> builds = new HashMap<>();
		for (Organism o : genomeService.getOrganisms())
			for (Genome g : o.getGenomes().values())
				for (Build b : g.getBuilds().values())
					builds.put(genomeService.getDelimitedParameterString(b), b.getDescription());
		return MessageBuilder.withPayload(new JSONObject(builds).toString()).build();
	}
	
	@Override
	public Message<String> listSampleSubtypes(){
		Map<String, String> sampleSubtypes = new HashMap<>();
		for (SampleSubtype sst : sampleService.getSampleSubtypeDao().findAll())
			if (sst.getIsActive() == 1)
				sampleSubtypes.put(sst.getId().toString(), sst.getName());
		return MessageBuilder.withPayload(new JSONObject(sampleSubtypes).toString()).build();
	}

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
	
	@Override
	public Message<String> listFileTypes(){
		Map<String, String> fileTypes = new HashMap<>();
		for (FileType ft : fileService.getFileTypes())
			fileTypes.put(ft.getId().toString(), ft.getIName() + " (" + ft.getName() + ")");
		return MessageBuilder.withPayload(new JSONObject(fileTypes).toString()).build();
	}
	
	@Override
	public Message<String> listAssayWorkflows(){
		Map<String, String> workflows = new HashMap<>();
		for (Workflow w : workflowService.getWorkflows())
			workflows.put(w.getId().toString(), w.getName());
		return MessageBuilder.withPayload(new JSONObject(workflows).toString()).build();
	}
	
	@Override
	public Message<String> listUsers(){
		Map<String, String> users = new HashMap<>();
		for (User u : userService.getUserDao().getActiveUsers()){
			// TODO: only selects first lab below (u.getLab().get(0)) but may be in more than one lab
			String labName = "";
			if (u.getLab().size() > 0)
				labName = u.getLab().get(0).getName();
			users.put(u.getId().toString(), u.getLastName() + ", " + u.getFirstName() + " (" + labName + ")");
		}
		return MessageBuilder.withPayload(new JSONObject(users).toString()).build();
	}
	
	@Override
	public Message<String> processImportedFileRegistrationData(JSONObject data){
		JSONArray lineArray = data.toJSONArray(new JSONArray());
		List<String> headerList = new ArrayList<>();
		SampleSource currentCellLibrary = null;
		Job currentJob = null;
		Sample currentSample = null;
		FileGroup currentFileGroup = null;
		FileHandle currentFileHandle = null;
		for (int i=0; i < lineArray.length(); i++){
			JSONArray lineElementList = lineArray.getJSONArray(i);
			for (int j = 0; j < lineElementList.length(); j++){
				String element = lineElementList.getString(j);
				if (element.isEmpty())
					continue;
				if (i == 0){ // first line
					headerList.add(element);
					continue;
				}	
				String heading = headerList.get(j);
				String model = heading.substring(0, heading.indexOf("."));
				String attributeName = heading.substring(heading.indexOf(".") + 1);
				try{
					handleDataEntry(model, attributeName, element, currentCellLibrary, currentJob, currentSample, currentFileGroup, currentFileHandle);
				} catch (Exception e){
					return MessageBuilder.withPayload("ERROR: unable to parse data: "+ e.getMessage() + "\n").build();
				}
			}
		}
		return MessageBuilder.withPayload("Update successful\n").build();
	}
	
	public void handleDataEntry(String model, String attributeName, String attributeVal, SampleSource currentCellLibrary,
			Job currentJob, Sample currentSample, FileGroup currentFileGroup, FileHandle currentFileHandle){
		// do all the work in this transactional method and throw runtime exception if anything goes wrong
		// to ensure everything is rolled back
		try {
			if (model.equals("cellLibraryId")){
				Integer id = Integer.parseInt(attributeVal);
				if (currentCellLibrary == null || !currentCellLibrary.getId().equals(id))
					currentCellLibrary = sampleService.getCellLibraryBySampleSourceId(id);
				if (currentCellLibrary == null || currentCellLibrary.getId() == null)
					throw new WaspRuntimeException("Unable to get cellLibrary with id=" + attributeVal);
			} else if (model.equals("Job")){
				if (attributeName.equals("name")){
					if (currentJob == null || !currentJob.getName().equals(attributeVal)){
						currentJob = new Job();
						currentJob.setName(attributeVal);
						currentJob = jobService.getJobDao().save(currentJob);
					}
				} else if (attributeName.equals("userId")){
					User u = userService.getUserDao().findById(Integer.parseInt(attributeVal));
					if (u == null || u.getId() == null)
						throw new WaspRuntimeException("Unable to get user with id=" + attributeVal);
					if (currentJob != null && !u.getId().equals(currentJob.getUserId()) ){
						currentJob.setUser(u);
						currentJob.setLab(u.getLab().get(0)); // TODO:: user may be in more than one lab
						currentJob.setIsActive(1);
					}
				} else if (attributeName.equals("workflowId")){
					Workflow wf = workflowService.getWorkflowDao().findById(Integer.parseInt(attributeVal));
					if (wf == null || wf.getId() == null)
						throw new WaspRuntimeException("Unable to get workflow with id=" + attributeVal);
					if (currentJob != null && !wf.getId().equals(currentJob.getWorkflowId()) )
						currentJob.setWorkflow(wf);
				}
			} else if (model.equals("JobMeta")){
				JobMeta meta = new JobMeta();
				meta.setK(attributeName);
				meta.setV(attributeVal);
				meta.setJob(currentJob);
				jobService.getJobMetaDao().setMeta(meta);
			} else if (model.equals("Sample")){
				if (attributeName.equals("name")){
					if (currentSample == null || !currentSample.getName().equals(attributeVal)){
						currentSample = new Sample();
						currentSample.setName(attributeVal);
						currentSample.setSubmitterUserId(currentJob.getUserId());
						currentSample.setSubmitterLabId(currentJob.getLabId());
						currentSample.setSubmitterJobId(currentJob.getId());
						currentSample.setIsActive(1);
						currentSample = sampleService.getSampleDao().save(currentSample);
						currentCellLibrary = new SampleSource();
						currentCellLibrary.setSourceSample(currentSample);
						currentCellLibrary = sampleService.getSampleSourceDao().save(currentCellLibrary);
						sampleService.setJobForLibraryOnCell(currentCellLibrary, currentJob);
						JobSample jobSample = new JobSample();
						jobSample.setJob(currentJob);
						jobSample.setSample(currentSample);
						jobSample = jobService.getJobSampleDao().save(jobSample);
					}
				} else if (attributeName.equals("sampleSubtypeId")){
					SampleSubtype ss = sampleService.getSampleSubtypeById(Integer.parseInt(attributeVal));
					if (ss == null || ss.getId() == null)
						throw new WaspRuntimeException("Unable to get sampleSubtype with id=" + attributeVal);
					if (currentSample != null && !ss.getId().equals(currentSample.getSampleSubtypeId()) ){
						currentSample.setSampleSubtype(ss);
						currentSample.setSampleTypeId(ss.getSampleTypeId());
					}
				}
			} else if (model.equals("SampleMeta")){
				SampleMeta meta = new SampleMeta();
				meta.setK(attributeName);
				meta.setV(attributeVal);
				meta.setSample(currentSample);
				sampleService.getSampleMetaDao().setMeta(meta);
			} else if (model.equals("FileGroup")){
				if (attributeName.equals("description")){
					if (currentFileGroup == null || !currentFileGroup.getDescription().equals(attributeVal)){
						currentFileGroup = new FileGroup();
						currentFileGroup.setDescription(attributeVal);
						currentFileGroup.setIsActive(1);
						currentFileGroup.setIsArchived(0);
						currentFileGroup = fileService.addFileGroup(currentFileGroup);
						fileService.setSampleSourceFile(currentFileGroup, currentCellLibrary);
					}
				} else if (attributeName.equals("fileTypeId")){
					FileType ft = fileService.getFileType(Integer.parseInt(attributeVal));
					if (ft == null || ft.getId() == null)
						throw new WaspRuntimeException("Unable to get fileTypeSubtype with id=" + attributeVal);
					if (currentFileGroup != null && !ft.getId().equals(currentFileGroup.getId()) )
						currentFileGroup.setFileType(ft);
				}
			}
			else if (model.equals("FileGroupMeta")){
				FileGroupMeta meta = new FileGroupMeta();
				meta.setK(attributeName);
				meta.setV(attributeVal);
				meta.setFileGroup(currentFileGroup);
				List<FileGroupMeta> metaList = new ArrayList<>();
				metaList.add(meta);
				fileService.saveFileGroupMeta(metaList, currentFileGroup);
			} else if (model.equals("FileHandle")){
				if (attributeName.equals("name")){
					if (currentFileHandle == null || !currentFileHandle.getFileName().equals(attributeVal)){
						currentFileHandle = new FileHandle();
						currentFileHandle.setFileName(attributeVal);
						currentFileHandle.setFileType(currentFileGroup.getFileType());
						currentFileHandle = fileService.addFile(currentFileHandle);
						currentFileGroup.addFileHandle(currentFileHandle);
					}
				} else if (attributeName.equals("fileURI")){
					currentFileHandle.setFileURI(new URI(attributeVal));
				} else if (attributeName.equals("md5hash")){
					currentFileHandle.setMd5hash(attributeVal);
				}
			} else if (model.equals("FileHandleMeta")){
				FileHandleMeta meta = new FileHandleMeta();
				meta.setK(attributeName);
				meta.setV(attributeVal);
				meta.setFile(currentFileHandle);
				List<FileHandleMeta> metaList = new ArrayList<>();
				metaList.add(meta);
				fileService.saveFileHandleMeta(metaList, currentFileHandle);
			}
			
		} catch (Exception e) {
			throw new WaspRuntimeException("Unable to parse or persist data value for element=" + attributeVal + ": " + e.getCause().toString());
		} 
	}
	
	
}
