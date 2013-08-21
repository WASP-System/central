package edu.yu.einstein.wasp.controller;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import edu.yu.einstein.wasp.MetaMessage;
import edu.yu.einstein.wasp.charts.highchartsjs.HighChartsJsBase;
import edu.yu.einstein.wasp.grid.file.FileUrlResolver;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.resourcebundle.DBResourceBundle;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.FilterService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.viewpanel.FileDataTabViewing;
import edu.yu.einstein.wasp.viewpanel.FileDataTabViewing.Status;
import edu.yu.einstein.wasp.viewpanel.Panel;
import edu.yu.einstein.wasp.viewpanel.PanelTab;
import edu.yu.einstein.wasp.viewpanel.WebContent;
import edu.yu.einstein.wasp.viewpanel.WebPanel;

@Controller
@Transactional
@RequestMapping("/jobresults")
public class ResultViewController extends WaspController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JobService	jobService;

	@Autowired
	private FilterService	filterService;

	@Autowired
	private AuthenticationService	authenticationService;

	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private FileUrlResolver fileUrlResolver;

	@Autowired
	private FileService fileService;


	//get locale-specific message
	protected String getMessage(String key, String defaultMessage) {
		String r=getMessage(key);
		
		if (defaultMessage!=null && r!=null && r.equals(key)) return defaultMessage; 
		
		return r;
	}
	
	protected String getMessage(String key) {
		HttpSession session = this.request.getSession();
		
		Locale locale = (Locale)session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		
		return DBResourceBundle.MESSAGE_SOURCE.getMessage(key, null, locale);
	}


	@RequestMapping(value = "/treeview/{type}/{id}", method = RequestMethod.GET)
	public String treeView(@PathVariable("type") String type, @PathVariable("id") Integer id, ModelMap m) {
		
		if(type.equalsIgnoreCase("job")) {
			Job job = this.jobService.getJobDao().getById(id.intValue());
			
			m.addAttribute("myid", id.intValue());
			m.addAttribute("type", type);
			m.addAttribute("workflow", job.getWorkflow().getIName());
			m.addAttribute("wf_name", job.getWorkflow().getName());
		}
		
		return "jobresults/treeview"; 	
	}
	

	// get the JSON data to construct the tree 
	@RequestMapping(value="/getTreeJson", method = RequestMethod.GET)
	public @ResponseBody String getTreeJson(@RequestParam("node") String nodeJSON, HttpServletResponse response) {
		
		try {
/*			Map <String, Object> jsTree = null;
			if(type.equalsIgnoreCase("job")) {
				jsTree = this.jobService.getJobSampleD3Tree(id);
			} else if(type.equalsIgnoreCase("sample")) {
				;
			}
*/			
			JSONObject node = new JSONObject(nodeJSON);
			Integer id = node.getInt("myid");
			String type = node.getString("type");
			Integer pid = node.getInt("pid");
			Integer jid = node.getInt("jid");
			
			return outputJSON(jobService.getTreeViewBranch(id, pid, type, jid), response); 	
		} 
		catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON for " + nodeJSON, e);
		}	
	}

	@RequestMapping(value="/getDetailsJson", method = RequestMethod.GET)
	public @ResponseBody String getDetailsJson(@RequestParam("node") String nodeJSON, HttpServletResponse response) {
		
		HashMap<String, Object> jsDetailsTabs = new HashMap<String, Object>();

		LinkedHashMap<String, Object> jsDetails = new LinkedHashMap<String, Object>();
		
		try {
			JSONObject node = new JSONObject(nodeJSON);
			Integer id = node.getInt("myid");
			String type = node.getString("type");
			Integer pid = node.getInt("pid");
			Integer jid = node.getInt("jid");
			
			if(type.startsWith("job")) {
				Integer jobId = id;
				Job job = this.jobService.getJobDao().getById(jobId);
				if(job==null || job.getId()==null){
					  waspErrorMessage("listJobSamples.jobNotFound.label");
					  return null;
				}
				
				jsDetails.put(getMessage("job.name.label"), job.getName());
				
				// add job extra detail info
				HashMap<String, String> extraJobDetails = jobService.getExtraJobDetails(job);
				for (String lblEJD : extraJobDetails.keySet()) {
					try {
						String msg = getMessage(lblEJD);
						if (!msg.equals(lblEJD))
							jsDetails.put(msg, extraJobDetails.get(lblEJD));
					}
					catch (NoSuchMessageException e) {
						;
					}
				}
			
				// add job meta info
				List<JobMeta> metaList = job.getJobMeta();
				for (JobMeta mt : metaList) {
					String mKey = mt.getK();
					try {
						String msg = getMessage(mKey+".label");
						if (!msg.equals(mKey+".label"))
							jsDetails.put(msg, mt.getV());
					}
					catch (NoSuchMessageException e) {
						;
					}
				}
				
				// add job status message info
				List<MetaMessage> msgList = jobService.getUserSubmittedJobComment(jobId);
				for (MetaMessage msg : msgList) {
					jsDetails.put(msg.getName(), msg.getValue());
				}
				msgList = jobService.getAllFacilityJobComments(jobId);
				for (MetaMessage msg : msgList) {
					jsDetails.put(msg.getName(), msg.getValue());
				}
				
				//jsDetailsTabs.put("job details", jsDetails);

			} else if(type.startsWith("sample") || type.startsWith("library") || type.startsWith("cell") || type.startsWith("pu")) {
				Integer sampleId = id;
				Sample sample = this.sampleService.getSampleById(sampleId);
				if(sample==null || sample.getId()==null){
					  waspErrorMessage("sampleDetail.sampleNotFound.error");
					  return null;
				}
				
				jsDetails.put(getMessage("sample.name.label"), sample.getName());
				
				// add sample meta info
				List<SampleMeta> metaList = sample.getSampleMeta();
				for (SampleMeta mt : metaList) {
					String mKey = mt.getK();
					try {
						String msg = getMessage(mKey+".label");
						if (!msg.equals(mKey+".label"))
							jsDetails.put(msg, mt.getV());
					}
					catch (NoSuchMessageException e) {
						;
					}
				}
				
				// add sample status message info
				List<MetaMessage> msgList = sampleService.getSampleQCComments(sampleId);
				for (MetaMessage msg : msgList) {
					jsDetails.put(msg.getName(), msg.getValue());
				}

			} else if(type.startsWith("filetype-")) {
				FileType ft = fileService.getFileType(id);
				Set<FileGroup> fgSet = new HashSet<FileGroup>();
				if (node.has("libid")) {
					Sample library = sampleService.getSampleById(node.getInt("libid"));
					if (node.has("cellid")) {
						Sample cell = sampleService.getSampleById(node.getInt("cellid"));
						fgSet.addAll(fileService.getFilesForCellLibraryByType(cell, library, ft));
					} else {
						fgSet.addAll(fileService.getFilesForLibraryByType(library, ft));
					}
				}

				//TODO: only use one element in the filegroup set, need to confirm this
				//jsDetails.putAll(fileService.getPanelTabSetByFileType(fgSet.iterator().next()));
			} else if(type.startsWith("filegroup")) {
				FileGroup fg = fileService.getFileGroupById(id);
				List<FileDataTabViewing> plugins = fileService.getTabViewProvidingPluginsByFileGroup(fg);
				String[][] statusArray = new String[plugins.size()][3];
				Map<String, PanelTab> pluginPanelTabs = new LinkedHashMap<>();
				int i = 0;
				for (FileDataTabViewing plugin: plugins){
					Status status = plugin.getStatus(fg);
					PanelTab panelTab = plugin.getViewPanelTab(fg);
					statusArray[i][0] = panelTab.getName();
				    statusArray[i][1] = panelTab.getDescription();
				    statusArray[i++][2] = status.toString();
				    if (status.equals(Status.COMPLETED))
				    	pluginPanelTabs.put(plugin.getPluginName(), panelTab);
				}
				jsDetailsTabs.put("statuslist", statusArray);
				jsDetailsTabs.put("paneltablist",pluginPanelTabs);
			}
				
			//jsDetailsTabs = testTabPanelMockup(jsDetailsTabs, jsDetails);
			
			return outputJSON(jsDetailsTabs, response);
		} 
		catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON for " + nodeJSON, e);
		}	
	}
}

