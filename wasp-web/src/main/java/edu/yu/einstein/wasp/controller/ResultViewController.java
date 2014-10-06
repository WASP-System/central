package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import edu.yu.einstein.wasp.Strategy;
import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.grid.file.FileUrlResolver;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobFile;
import edu.yu.einstein.wasp.model.JobSoftware;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.plugin.mps.genomebrowser.GenomeBrowserProviding;
import edu.yu.einstein.wasp.resourcebundle.DBResourceBundle;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.FilterService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.service.ResultViewService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.viewpanel.Action.CallbackFunctionType;
import edu.yu.einstein.wasp.viewpanel.DataTabViewing.Status;
import edu.yu.einstein.wasp.viewpanel.Action;
import edu.yu.einstein.wasp.viewpanel.FileDataTabViewing;
import edu.yu.einstein.wasp.viewpanel.GridColumn;
import edu.yu.einstein.wasp.viewpanel.GridContent;
import edu.yu.einstein.wasp.viewpanel.GridDataField;
import edu.yu.einstein.wasp.viewpanel.GridPanel;
import edu.yu.einstein.wasp.viewpanel.JobDataTabViewing;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

@Controller
@RequestMapping("/jobresults")
// don't even think of putting @Transactional on here. Use the resultViewService to do transactional work
public class ResultViewController extends WaspController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JobService jobService;

	@Autowired
	private FilterService filterService;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private FileUrlResolver fileUrlResolver;

	@Autowired
	private FileService fileService;
	
	@Autowired
	private WaspPluginRegistry waspPluginRegistry;

	@Autowired
	private MessageServiceWebapp messageService;

	@Autowired
	private ResultViewService resultViewService;


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
	@PreAuthorize("hasRole('lu-*') or hasRole('su') or hasRole('ft-*') or hasRole('fm-*')")
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
	@PreAuthorize("hasRole('lu-*') or hasRole('su') or hasRole('ft-*') or hasRole('fm-*')")
	public @ResponseBody String getFileGroupListJson(@RequestParam("fglist") String fgListStr, HttpServletResponse response) {
		try {
			List<Map<String,String>> fileMapList = new ArrayList<Map<String,String>>();
			try {
				List<Integer> fgIdList = new ArrayList<Integer>();
				for (String s : fgListStr.split(","))
					fgIdList.add(Integer.parseInt(s.trim()));
				fileMapList = resultViewService.getFileDataMapList(fgIdList);
			} catch (Exception e) {
				waspErrorMessage("resultViewer.getFileInfo.error");
				e.printStackTrace();
			}
			return outputJSON(fileMapList, response);
		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON for " + fgListStr, e);
		}	
	}

	// get the JSON data to construct the tree 
	@RequestMapping(value="/getTreeJson", method = RequestMethod.GET)
	@PreAuthorize("hasRole('lu-*') or hasRole('su') or hasRole('ft-*') or hasRole('fm-*')")
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
	@PreAuthorize("hasRole('lu-*') or hasRole('su') or hasRole('ft-*') or hasRole('fm-*')")
	public @ResponseBody String getDetailsJson(@RequestParam("node") String nodeJSON, HttpServletResponse response) {
		
		HashMap<String, Object> jsDetailsTabs = new HashMap<String, Object>();

		logger.trace("nodeJSON=" + nodeJSON);
		try {
			JSONObject node = new JSONObject(nodeJSON);
			Integer id = node.getInt("myid");
			String type = node.getString("type");
			
			if(type.startsWith("job")) {
				Integer jobId = id;
				Job job = this.jobService.getJobDao().getById(jobId);
				if(job==null || job.getId()==null){
					  waspErrorMessage("listJobSamples.jobNotFound.label");
					  return null;
				}
				
				Map<String, PanelTab> pluginPanelTabs = new LinkedHashMap<>();
				JobDataTabViewing plugin = jobService.getTabViewPluginByJob(job);
				Integer tabCountNow = 0;
				// get "job summary" tab and add to panel tab set
				PanelTab summaryPanelTab = this.getSummaryPanelTab(job, plugin==null?null:plugin.getStatus(job));
				pluginPanelTabs.put("tab-" + (tabCountNow++).toString(), summaryPanelTab);
				// get "files by type" tab and add to panel tab set
				//summaryPanelTab = this.getFilesByTypeTab(job);
				//pluginPanelTabs.put("tab-" + (tabCountNow++).toString(), summaryPanelTab);
					
				if (plugin!=null) {
					Set<PanelTab> panelTabSet = plugin.getViewPanelTabs(job);//additional, plugin-specific ordered, panelTabs
					for (PanelTab ptab : panelTabSet) {
				    	if (!ptab.getPanels().isEmpty()){
					    	String tabId = "tab-" + (tabCountNow++).toString();
					    	pluginPanelTabs.put(tabId, ptab);
				    	}
					}
				}

				jsDetailsTabs.put("paneltablist",pluginPanelTabs);

			} else if(type.startsWith("sample") || type.startsWith("library") || type.startsWith("cell") || type.startsWith("pu")) {

			} else if(type.startsWith("filetype-")) {
				Integer fileTypeId = id;
				Integer libraryId = null;
				Integer cellId = null ;
				if (node.has("libid")) 
					libraryId = node.getInt("libid");
				if (node.has("cellid"))
					cellId = node.getInt("cellid");
				GridPanel filePanel = null;
				try{
					filePanel = resultViewService.getFileGridPanel(fileTypeId, libraryId, cellId);
					jsDetailsTabs.put("filepanel", filePanel);
				} catch (Exception e){
					waspErrorMessage("resultViewer.getFilePanel.error");
					e.printStackTrace();
				}
				
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
					    if (status.equals(Status.COMPLETED) || status.equals(Status.NOT_APPLICABLE)){
					    	PanelTab panelTab = null;
					    	try {
					    		panelTab = plugin.getViewPanelTab(fg);
					    	} catch (Exception e) {
					    		logger.warn("Plugin " + plugin.getName() + " threw exception " + e.getLocalizedMessage() + " when attempting to get panel tab.  Skipping.");
					    		e.printStackTrace();
					    	}
					    	if (panelTab!=null && !panelTab.getPanels().isEmpty()){
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
		} catch (Throwable e) {
			e.printStackTrace();
			throw new IllegalStateException("Can't marshall to JSON for " + nodeJSON, e);
		}	
	}
	
	private PanelTab getSummaryPanelTab(Job job, Status jobStatus)throws PanelException{
		try{
			Strategy strategy = jobService.getStrategy(Strategy.StrategyType.LIBRARY_STRATEGY, job);
			List<String> softwareNameList = new ArrayList<String>();
			for(JobSoftware js : job.getJobSoftware()){
				softwareNameList.add(js.getSoftware().getName());
			}
			Collections.sort(softwareNameList);
			PanelTab summaryPanelTab = constructSummaryPanelTab(jobStatus, job, strategy, softwareNameList);
			return summaryPanelTab;			
		}catch(Exception e){
			logger.debug(e.getStackTrace().toString());
			throw new PanelException(e.getMessage());
		}
	}
	
	private PanelTab constructSummaryPanelTab(Status jobStatus, Job job, Strategy strategy,	List<String> softwareNameList) {

		//create the panelTab to house the panel
		PanelTab panelTab = new PanelTab();
		panelTab.setTabTitle(messageService.getMessage("resultViewer.summaryTab.title"));
		panelTab.setNumberOfColumns(1);

		//create the panel
		GridPanel panel = new GridPanel();
		panel.setTitle(messageService.getMessage("resultViewer.summaryTab.title"));
		panel.setDescription(messageService.getMessage("resultViewer.summaryTab.description"));
		panel.setResizable(false);
		panel.setMaxOnLoad(true);	
		panel.setOrder(1);
		panel.setStatusField("Status");
		
		//create content (think of it as the table)
		GridContent content = new GridContent();
		//create the data model 
		content.addDataFields(new GridDataField("Strategy", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("Description", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("Workflow", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("Software", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("Status", "String"));//dataIndex, datatype

		//create columns and associate each column with its displayed header and a data model attribute (dataIndex)
		content.addColumn(new GridColumn(messageService.getMessage("resultViewer.summaryTab.strategyCol.header"), "Strategy", 150, 0));//header,dataIndex	set width=150, flex=0	
		content.addColumn(new GridColumn(messageService.getMessage("resultViewer.summaryTab.descriptionCol.header"), "Description", 1));//header,dataIndex	set flex=1	
		content.addColumn(new GridColumn(messageService.getMessage("resultViewer.summaryTab.workflowCol.header"), "Workflow", 150, 0));//header,dataIndex		
		content.addColumn(new GridColumn(messageService.getMessage("resultViewer.summaryTab.softwareCol.header"), "Software", 200, 0));//header,dataIndex		
		content.addColumn(new GridColumn(messageService.getMessage("resultViewer.summaryTab.statusCol.header"), "Status", 150, 0));//header,dataIndex
		
		//create rows with  information
		List<String> row = new ArrayList<String>();
		row.add(strategy.getDisplayStrategy());
		row.add(strategy.getDescription());
		row.add(job.getWorkflow().getName());
		String softwareNames = "";
		for(String name : softwareNameList){
			if(!softwareNames.isEmpty()){
				softwareNames += "<br />";
			}
			softwareNames += name;
		}
		row.add(softwareNames);
		row.add(jobStatus==null ? "UNKNOWN" : jobStatus.toString());
		content.addDataRow(row);//add row to content
		
		panel.setContent(content);//add content to panel
		panelTab.addPanel(panel);//add panel to panelTab
		return panelTab;
	}
	
	private PanelTab getFilesByTypeTab(Job job) {

		//create the panelTab to house the panel
		PanelTab panelTab = new PanelTab();
		panelTab.setTabTitle(messageService.getMessage("resultViewer.filesByTypeTab.title"));
		panelTab.setNumberOfColumns(1);

		// create the panel
		GridPanel panel = new GridPanel();
		panel.setTitle(messageService.getMessage("resultViewer.filesByTypeTab.title"));
		panel.setDescription(messageService.getMessage("resultViewer.filesByTypeTab.description"));
		panel.setResizable(false);
		panel.setMaxOnLoad(true);
		panel.setOrder(1);
		
		panel.setGrouping(true);
		panel.setGroupField("FileType");
		panel.setAllowSelectDownload(true);
		panel.setAllowGroupDownload(true);
		panel.setSelectDownloadText("Download Selected");

		// create content (think of it as the table)
		GridContent content = new GridContent();

		// create the data model
		content.addDataFields(new GridDataField("FileType", "String"));
		content.addDataFields(new GridDataField("File", "String"));
		content.addDataFields(new GridDataField("Size", "String"));
		content.addDataFields(new GridDataField("MD5", "String"));

		content.addColumn(new GridColumn(messageService.getMessage("resultViewer.filesByTypeTab.fileCol.header"), "File", 1));
		content.addColumn(new GridColumn(messageService.getMessage("resultViewer.filesByTypeTab.sizeCol.header"), "Size", 100, 0));
		content.addColumn(new GridColumn(messageService.getMessage("resultViewer.filesByTypeTab.md5Col.header"), "MD5", 170, 0));
		
		for (JobFile jf : job.getJobFile()) {
			List<String> row = new ArrayList<String>();
			FileGroup fg = jf.getFile();
			for (FileHandle fh : fg.getFileHandles()) {
				//row.add(fh.getFileType()==null?"Unknown File Type":fh.getFileType().getName());
				if (fh.getFileType()==null)
					continue;

				row.add(fh.getFileName());
				row.add(fh.getSizek()==null ? "" : fh.getSizek().toString());
				row.add(fh.getMd5hash());
				content.addDataRow(row);//add the new row to the content	
				
				List<Action> actionList = new ArrayList<Action>();
				// add download action to the list
				String resolvedURL = "";
				try{
					resolvedURL = fileUrlResolver.getURL(fh).toString();
				}catch(Exception e){logger.debug("UNABLE TO RESOLVE URL for file: " + fh.getFileName());}
				actionList.add(new Action("icon-download", "Download", CallbackFunctionType.DOWNLOAD, resolvedURL));
				
				// add generic file viewer action to the list
				actionList.add(new Action("icon-view-file", "View", CallbackFunctionType.OPEN_IN_CSS_WIN, fg.getId().toString()));
				
				List<GenomeBrowserProviding> plugins = new ArrayList<>();				
				plugins.addAll(waspPluginRegistry.getPlugins(GenomeBrowserProviding.class));
				for(GenomeBrowserProviding plugin : plugins){
					Action action = plugin.getAction(fg);
					if(action != null){
						actionList.add(action);
					}
				}
				content.addActions(actionList);				
			}
		}
		panel.setContent(content);// add content to panel

		panelTab.addPanel(panel);//add panel to panelTab
		return panelTab;
	}

}

