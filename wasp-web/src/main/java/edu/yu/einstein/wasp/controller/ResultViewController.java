package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
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
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.file.FileUrlResolver;
import edu.yu.einstein.wasp.interfacing.Hyperlink;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
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
import edu.yu.einstein.wasp.viewpanel.DataTabViewing.Status;
import edu.yu.einstein.wasp.viewpanel.FileDataTabViewing;
import edu.yu.einstein.wasp.viewpanel.JobDataTabViewing;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

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

	// get the JSON data of the given filegroup id list
	@RequestMapping(value="/getFileGroupsDetailJson", method = RequestMethod.GET)
	public @ResponseBody String getFileGroupListJson(@RequestParam("fglist") String fgListStr, HttpServletResponse response) {
		try {
//			List<Map<String, String>> fgMapList = new ArrayList<Map<String,String>>();
//			Hyperlink hl;
//			for (String s : fgListStr.split(",")) {
//				FileGroup fg = fileService.getFileGroupById(Integer.parseInt(s.trim()));
//				Map<String,String> fgMap = new HashMap<String,String>();
//				fgMap.put("id", fg.getId().toString());
//				fgMap.put("name", fg.getDescription());
//				try {
//					hl = new Hyperlink("Download", fileUrlResolver.getURL(fg).toString());
//					fgMap.put("link", hl.getTargetLink());
//				} catch (GridUnresolvableHostException e) {
//					logger.debug("Cannot resolver host for filegroup id " + s);
//					e.printStackTrace();
//				}
//				
//				fgMapList.add(fgMap);
//			}
//			
//			return outputJSON(fgMapList, response);
			
			Set<FileHandle> fhSet = new HashSet<FileHandle>();
			List<Map<String,String>> fileMapList = new ArrayList<Map<String,String>>();
			Hyperlink hl;
			try {
				for (String s : fgListStr.split(",")) {
					FileGroup fg = fileService.getFileGroupById(Integer.parseInt(s.trim()));

					String fgidStr = fg.getId().toString();
					String fgName = fg.getDescription();
					hl = new Hyperlink("Download", fileUrlResolver.getURL(fg).toString());
					String fgLink = hl.getTargetLink();
					
					fhSet = fg.getFileHandles();
					for (FileHandle fh : fhSet) {
						Map<String,String> fileMap = new HashMap<String,String>();
						
						fileMap.put("fgid", fgidStr);
						fileMap.put("fgname", fgName);
						fileMap.put("fglink", fgLink);
						
						fileMap.put("fid", fh.getId().toString());
						fileMap.put("fname", fh.getFileName());
						fileMap.put("md5", fh.getMd5hash());
						fileMap.put("size", fh.getSizek() != null ? fh.getSizek().toString() : "0");
						//fileMap.put("updated", fh.getUpdated().);
						hl = new Hyperlink("Download", fileUrlResolver.getURL(fh).toString());
						fileMap.put("link", hl.getTargetLink());
						
						fileMapList.add(fileMap);
					}
				}
			} catch (GridUnresolvableHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return outputJSON(fileMapList, response);
		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON for " + fgListStr, e);
		}	
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
				
				JobDataTabViewing plugin = jobService.getTabViewPluginByJob(job);
				logger.debug("***at Q");
				if (plugin!=null) {logger.debug("***R");
					Map<String, PanelTab> pluginPanelTabs = new LinkedHashMap<>();
					Set<PanelTab> panelTabSet = plugin.getViewPanelTabs(job);
					logger.debug("***size of panelTabSet = " + panelTabSet.size());
					Integer tabCount = 0;
					for (PanelTab ptab : panelTabSet) {
				    	if (!ptab.getPanels().isEmpty()){
				    		logger.debug("***at S");
							
					    	String tabId = "tab-" + (tabCount++).toString();
					    	pluginPanelTabs.put(tabId, ptab);
				    	}
					}
	
					//jsDetailsTabs.put("statuslist", statusArray);
					jsDetailsTabs.put("paneltablist",pluginPanelTabs);
				}

			} else if(type.startsWith("sample") || type.startsWith("library") || type.startsWith("cell") || type.startsWith("pu")) {
//				Integer sampleId = id;
//				Sample sample = this.sampleService.getSampleById(sampleId);
//				if(sample==null || sample.getId()==null){
//					  waspErrorMessage("sampleDetail.sampleNotFound.error");
//					  return null;
//				}
//				
//				jsDetails.put(getMessage("sample.name.label"), sample.getName());
//				
//				// add sample meta info
//				List<SampleMeta> metaList = sample.getSampleMeta();
//				for (SampleMeta mt : metaList) {
//					String mKey = mt.getK();
//					try {
//						String msg = getMessage(mKey+".label");
//						if (!msg.equals(mKey+".label"))
//							jsDetails.put(msg, mt.getV());
//					}
//					catch (NoSuchMessageException e) {
//						;
//					}
//				}
//				
//				// add sample status message info
//				List<MetaMessage> msgList = sampleService.getSampleQCComments(sampleId);
//				for (MetaMessage msg : msgList) {
//					jsDetails.put(msg.getName(), msg.getValue());
//				}

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
				
				String fgList = "";
				for (FileGroup fg : fgSet) {
					fgList += "," + fg.getId().toString();
				}
				
				jsDetailsTabs.put("fgliststr", fgList.substring(1));
			} else if(type.startsWith("filegroup")) {
				FileGroup fg = fileService.getFileGroupById(id);
				List<FileDataTabViewing> plugins = fileService.getTabViewProvidingPluginsByFileGroup(fg);
				String[][] statusArray = new String[plugins.size()][4];
				Map<String, PanelTab> pluginPanelTabs = new LinkedHashMap<>();
				if (fg.getId() == null){
					logger.warn("No filegroup found with id = " + id);
				} else {
					int i = 0;
					Integer tabCount = 0;
					for (FileDataTabViewing plugin: plugins){
						Status status = plugin.getStatus(fg);
						statusArray[i][0] = plugin.getName();
					    statusArray[i][1] = plugin.getDescription();
					    statusArray[i][2] = status.toString();
					    if (status.equals(Status.COMPLETED)){
					    	PanelTab panelTab = plugin.getViewPanelTab(fg);
					    	if (!panelTab.getPanels().isEmpty()){
						    	String tabId = "tab-" + (tabCount++).toString();
						    	pluginPanelTabs.put(tabId, panelTab);
						    	statusArray[i][3] = tabId;
					    	}
					    }
					    i++;
					}
				}
				jsDetailsTabs.put("statuslist", statusArray);
				jsDetailsTabs.put("paneltablist",pluginPanelTabs);
			}
			
			return outputJSON(jsDetailsTabs, response);
		} 
		catch (Throwable e) {
			e.printStackTrace();
			throw new IllegalStateException("Can't marshall to JSON for " + nodeJSON, e);
		}	
	}
}

