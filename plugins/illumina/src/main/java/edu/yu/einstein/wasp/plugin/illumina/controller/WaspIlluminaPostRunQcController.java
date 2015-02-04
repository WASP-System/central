package edu.yu.einstein.wasp.plugin.illumina.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.yu.einstein.wasp.controller.WaspController;
import edu.yu.einstein.wasp.exception.FormParameterException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.interfacing.Hyperlink;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.plugin.illumina.service.WaspIlluminaQcService;
import edu.yu.einstein.wasp.plugin.illumina.service.impl.WaspIlluminaQcServiceImpl.CellSuccessQcMetaKey;
import edu.yu.einstein.wasp.plugin.illumina.util.IlluminaQcContext;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;

@Controller
@RequestMapping("/waspIlluminaPlatform/postRunQC")
public class WaspIlluminaPostRunQcController extends WaspController{
	
	Logger logger = LoggerFactory.getLogger(WaspIlluminaPostRunQcController.class);

	@Autowired
	RunService runService;
	
	@Autowired
	SampleService sampleService;
	
	@Autowired
	WaspIlluminaQcService illuminaQcService;
	
	@Autowired
	FileService fileService;
	
	@Autowired
	MessageService messageService;
	
	private int getCycleCount(Run run) throws SampleTypeException{
		int cycleCount = 0;
		for (FileGroup fileGroup : fileService.getFilesForPlatformUnitByType(run.getPlatformUnit(), fileService.getFileType("waspIlluminaPlatformQcMetrics")) ){
			for (FileHandle fh : fileGroup.getFileHandles()){
				String name = fh.getFileName();
				if (name.startsWith("Intensity") && name.endsWith("a.png"))
					cycleCount++;
			}
		}
		return cycleCount;
	}
	
	private Map<String, FileHandle> getFileHandlesByName(Run run, String subFolder) throws SampleTypeException{
		Map<String, FileHandle> fileHandlesByName = new HashMap<String, FileHandle>();
		for (FileGroup fileGroup : run.getPlatformUnit().getFileGroups()){
			if (fileGroup.getFileType().equals(fileService.getFileType("waspIlluminaPlatformQcMetrics"))){
				for (FileHandle fh : fileGroup.getFileHandles()){
					String name = fh.getFileName();
					if (subFolder.isEmpty() && !name.contains("/") && name.endsWith(".png"))
						fileHandlesByName.put(fh.getFileName(), fh);
					else if (name.startsWith(subFolder) && name.endsWith(".png"))
						fileHandlesByName.put(name, fh);
				}
			}
		}
		return fileHandlesByName;
	}
	
	private String getSubfolderByMetaKey(String metaKey){
		if (metaKey.equals(CellSuccessQcMetaKey.INTENSITY))
			return "Intensity";
		if (metaKey.equals(CellSuccessQcMetaKey.FOCUS))
			return "FWHM";
		if (metaKey.equals(CellSuccessQcMetaKey.NUMGT30))
			return "NumGT30";
		return "";
			
	}
	
	private void setCoreDataModelParameters(Run run, String metaKey, ModelMap m) throws SampleTypeException{
		List<Integer> cellIndexList = new ArrayList<Integer>();
		Map<Integer, IlluminaQcContext> indexedQcData = new HashMap<Integer, IlluminaQcContext>();
		
		try {
			Map<Integer, Sample> cells = sampleService.getIndexedCellsOnPlatformUnit(run.getPlatformUnit());
			cellIndexList.addAll(cells.keySet());
			for (Integer index: cellIndexList){
				Sample cell = cells.get(index);
				IlluminaQcContext qcContext = null;
				try{
					qcContext = illuminaQcService.getQc(cell, metaKey);
				} catch(Exception e){ } // do nothing, leave set to null
				if (qcContext == null)
					qcContext = new IlluminaQcContext(cell, null, null);
				indexedQcData.put(index, qcContext);
			}
		} catch (SampleTypeException e) {
			logger.warn(e.getLocalizedMessage());
		}
		if (!metaKey.equals(CellSuccessQcMetaKey.RUN_SUCCESS) && !metaKey.equals(CellSuccessQcMetaKey.CLUSTER_DENSITY)){
			String subFolder =  getSubfolderByMetaKey(metaKey);
			m.addAttribute("fileHandlesByName", getFileHandlesByName(run, subFolder));
			m.addAttribute("chartSubFolder", subFolder);
		}
		m.addAttribute("numCycles", getCycleCount(run));
		m.addAttribute("cellIndexList", cellIndexList);
		m.addAttribute("runName", run.getName());
		m.addAttribute("existingQcValuesIndexed",indexedQcData);
	}
	
	private void processPostRequest(Run run, String metaKey) throws SampleTypeException, FormParameterException{
		Sample pu = run.getPlatformUnit();
		List<IlluminaQcContext> qcContextList = new ArrayList<IlluminaQcContext>();
		Map<Integer, Sample> cells = sampleService.getIndexedCellsOnPlatformUnit(pu);
		for(Integer cellIndex : cells.keySet()){
			IlluminaQcContext qcContext = new IlluminaQcContext();
			qcContext.setCell(cells.get(cellIndex));
			int passed = 0;
			try{
				passed = Integer.parseInt(request.getParameter("radioL" + cellIndex));
			} catch(NumberFormatException e){
				throw new FormParameterException("Could not parse form parameter radioL" + cellIndex);
			}
			qcContext.setPassedQc((passed == 1) ? true:false);
			String comment = request.getParameter("commentsL" + cellIndex);
			if (comment == null){
				throw new FormParameterException("Could not parse form parameter commentsL" + cellIndex);
			}
			qcContext.setComment(comment);
			qcContextList.add(qcContext);
		}
		illuminaQcService.updateQc(qcContextList, metaKey);
	}
	
	@RequestMapping(value="/run/{runId}/displayFocusQualityCharts", method=RequestMethod.GET)
	public String displayFocusQualityCharts(@PathVariable("runId") Integer runId, ModelMap m){
		Run run = runService.getRunById(runId);
		if (run.getId() == null){
			logger.warn("No run found with id of " + runId + " in model");
			waspErrorMessage("run.invalid_id.error");
			return "redirect:/dashboard.do";
		}
		try{
			setCoreDataModelParameters(run, CellSuccessQcMetaKey.FOCUS, m);
		} catch(SampleTypeException e){
			logger.warn(e.getLocalizedMessage());
			waspErrorMessage("waspIlluminaPlugin.notPu.error");
			return "redirect:/dashboard.do";
		}
		return "waspIlluminaPlatform/postrunqc/displayfocusqualitycharts";
	}
	
	@RequestMapping(value="/run/{runId}/displayFocusQualityCharts", method=RequestMethod.POST)
	public String processFocusQualityCharts(@PathVariable("runId") Integer runId, ModelMap m){
		Run run = runService.getRunById(runId);
		if (run.getId() == null){
			logger.warn("No run found with id of " + runId + " in model");
			waspErrorMessage("run.invalid_id.error");
			return "redirect:/dashboard.do";
		}
		try {
			processPostRequest(run, CellSuccessQcMetaKey.FOCUS);
		} catch (SampleTypeException e1) {
			logger.warn(e1.getLocalizedMessage());
			waspErrorMessage("waspIlluminaPlugin.notPu.error");
			return "redirect:/dashboard.do";
		} catch (FormParameterException e1) {
			logger.warn(e1.getLocalizedMessage());
			waspErrorMessage("waspIlluminaPlugin.formParameter.error");
			return "redirect:/dashboard.do";
		} catch (Exception e){
			waspErrorMessage("waspIlluminaPlugin.update.error");
			return "redirect:/dashboard.do";
		}
		return "redirect:/waspIlluminaPlatform/postRunQC/run/" + runId + "/displayIntensityCharts.do";
	}
	
	@RequestMapping(value="/run/{runId}/displayIntensityCharts", method=RequestMethod.GET)
	public String displayIntensityCharts(@PathVariable("runId") Integer runId, ModelMap m){
		Run run = runService.getRunById(runId);
		if (run.getId() == null){
			logger.warn("No run found with id of " + runId + " in model");
			waspErrorMessage("run.invalid_id.error");
			return "redirect:/dashboard.do";
		}
		try{
			setCoreDataModelParameters(run, CellSuccessQcMetaKey.INTENSITY, m);
		} catch (SampleTypeException e1) {
			logger.warn(e1.getLocalizedMessage());
			waspErrorMessage("waspIlluminaPlugin.notPu.error");
			return "redirect:/dashboard.do";
		}
		return "waspIlluminaPlatform/postrunqc/displayinstensitycharts";
	}
	
	@RequestMapping(value="/run/{runId}/displayIntensityCharts", method=RequestMethod.POST)
	public String processIntensityCharts(@PathVariable("runId") Integer runId, ModelMap m){
		Run run = runService.getRunById(runId);
		if (run.getId() == null){
			logger.warn("No run found with id of " + runId + " in model");
			waspErrorMessage("run.invalid_id.error");
			return "redirect:/dashboard.do";
		}
		try {
			processPostRequest(run, CellSuccessQcMetaKey.INTENSITY);
		} catch (FormParameterException e1) {
			logger.warn(e1.getLocalizedMessage());
			waspErrorMessage("waspIlluminaPlugin.formParameter.error");
			return "redirect:/dashboard.do";
		} catch (Exception e){
			waspErrorMessage("waspIlluminaPlugin.update.error");
			return "redirect:/dashboard.do";
		}
		return "redirect:/waspIlluminaPlatform/postRunQC/run/" + runId + "/displayNumGT30Charts.do";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/run/{runId}/displayNumGT30Charts", method=RequestMethod.GET)
	public String displayNumGT30Charts(@PathVariable("runId") Integer runId, ModelMap m){
		Run run = runService.getRunById(runId);
		if (run.getId() == null){
			logger.warn("No run found with id of " + runId + " in model");
			waspErrorMessage("run.invalid_id.error");
			return "redirect:/dashboard.do";
		}
		try{
			setCoreDataModelParameters(run, CellSuccessQcMetaKey.NUMGT30, m);
		} catch (SampleTypeException e1) {
			logger.warn(e1.getLocalizedMessage());
			waspErrorMessage("waspIlluminaPlugin.notPu.error");
			return "redirect:/dashboard.do";
		}
		m.addAttribute("qscoreSubFolder", "ByCycle");
		try {
			((Map<String, FileHandle>) m.get("fileHandlesByName")).putAll(getFileHandlesByName(run, "ByCycle"));
		} catch (SampleTypeException e) {
			logger.warn(e.getLocalizedMessage());
			waspErrorMessage("waspIlluminaPlugin.notPu.error");
			return "redirect:/dashboard.do";
		}
		return "waspIlluminaPlatform/postrunqc/displaynumgt30charts";
	}
	
	
	@RequestMapping(value="/run/{runId}/displayNumGT30Charts", method=RequestMethod.POST)
	public String processNumGT30Charts(@PathVariable("runId") Integer runId, ModelMap m){
		Run run = runService.getRunById(runId);
		if (run.getId() == null){
			logger.warn("No run found with id of " + runId + " in model");
			waspErrorMessage("run.invalid_id.error");
			return "redirect:/dashboard.do";
		}
		try {
			processPostRequest(run, CellSuccessQcMetaKey.NUMGT30);
		} catch (SampleTypeException e1) {
			logger.warn(e1.getLocalizedMessage());
			waspErrorMessage("waspIlluminaPlugin.notPu.error");
			return "redirect:/dashboard.do";
		} catch (FormParameterException e1) {
			logger.warn(e1.getLocalizedMessage());
			waspErrorMessage("waspIlluminaPlugin.formParameter.error");
			return "redirect:/dashboard.do";
		} catch (Exception e){
			waspErrorMessage("waspIlluminaPlugin.update.error");
			return "redirect:/dashboard.do";
		}
		return "redirect:/waspIlluminaPlatform/postRunQC/run/" + runId + "/displayClusterDensityChart.do";
	}
	
	@RequestMapping(value="/run/{runId}/displayClusterDensityChart", method=RequestMethod.GET)
	public String displayClusterDensityChart(@PathVariable("runId") Integer runId, ModelMap m){
		Run run = runService.getRunById(runId);
		if (run.getId() == null){
			logger.warn("No run found with id of " + runId + " in model");
			waspErrorMessage("run.invalid_id.error");
			return "redirect:/dashboard.do";
		}
		try{
			setCoreDataModelParameters(run, CellSuccessQcMetaKey.CLUSTER_DENSITY, m);
			Map<String, FileHandle> fileHandlesByName = getFileHandlesByName(run, "");
			m.addAttribute("clusterDensityChartFileHandle", fileHandlesByName.get("NumClusters_By_Lane.png"));
		} catch (SampleTypeException e1) {
			logger.warn(e1.getLocalizedMessage());
			waspErrorMessage("waspIlluminaPlugin.notPu.error");
			return "redirect:/dashboard.do";
		}
		return "waspIlluminaPlatform/postrunqc/displayclusterdensitychart";
	}
	
	@RequestMapping(value="/run/{runId}/displayClusterDensityChart", method=RequestMethod.POST)
	public String processClusterDensityChart(@PathVariable("runId") Integer runId, ModelMap m){
		Run run = runService.getRunById(runId);
		if (run.getId() == null){
			logger.warn("No run found with id of " + runId + " in model");
			waspErrorMessage("run.invalid_id.error");
			return "redirect:/dashboard.do";
		}
		try {
			processPostRequest(run, CellSuccessQcMetaKey.CLUSTER_DENSITY);
		} catch (SampleTypeException e1) {
			logger.warn(e1.getLocalizedMessage());
			waspErrorMessage("waspIlluminaPlugin.notPu.error");
			return "redirect:/dashboard.do";
		} catch (FormParameterException e1) {
			logger.warn(e1.getLocalizedMessage());
			waspErrorMessage("waspIlluminaPlugin.formParameter.error");
			return "redirect:/dashboard.do";
		} catch (Exception e){
			waspErrorMessage("waspIlluminaPlugin.update.error");
			return "redirect:/dashboard.do";
		}
		return "redirect:/waspIlluminaPlatform/postRunQC/run/" + runId + "/updateQualityReport.do";
	}
	
	@RequestMapping(value="/run/{runId}/updateQualityReport", method=RequestMethod.GET)
	public String updateQualityReport(@PathVariable("runId") Integer runId, ModelMap m){
		Map<String, Map<Integer, IlluminaQcContext>> qcDataMap = new LinkedHashMap<String, Map<Integer,IlluminaQcContext>>();
		Run run = runService.getRunById(runId);
		if (run.getId() == null){
			logger.warn("No run found with id of " + runId + " in model");
			waspErrorMessage("run.invalid_id.error");
			return "redirect:/dashboard.do";
		}
		try{
			setCoreDataModelParameters(run, CellSuccessQcMetaKey.RUN_SUCCESS, m);
		} catch (SampleTypeException e1) {
			logger.warn(e1.getLocalizedMessage());
			waspErrorMessage("waspIlluminaPlugin.notPu.error");
			return "redirect:/dashboard.do";
		}
		Sample pu = run.getPlatformUnit();
		Map<String, String> qcHeadingsByMetaKey = new LinkedHashMap<String, String>();
		qcHeadingsByMetaKey.put(CellSuccessQcMetaKey.FOCUS, messageService.getMessage("waspIlluminaPlugin.updateQualityReport_focus.label"));
		qcHeadingsByMetaKey.put(CellSuccessQcMetaKey.INTENSITY, messageService.getMessage("waspIlluminaPlugin.updateQualityReport_intensity.label"));
		qcHeadingsByMetaKey.put(CellSuccessQcMetaKey.NUMGT30, messageService.getMessage("waspIlluminaPlugin.updateQualityReport_baseQc.label"));
		qcHeadingsByMetaKey.put(CellSuccessQcMetaKey.CLUSTER_DENSITY, messageService.getMessage("waspIlluminaPlugin.updateQualityReport_clusterDensity.label"));
		
		try{
			Map<Integer, Sample> cells = sampleService.getIndexedCellsOnPlatformUnit(pu);
			for(String metaKey: qcHeadingsByMetaKey.keySet()){
				Map<Integer, IlluminaQcContext> cellQcResults = new LinkedHashMap<Integer, IlluminaQcContext>();
				for(Integer cellIndex : cells.keySet()){
					IlluminaQcContext qcContext = illuminaQcService.getQc(cells.get(cellIndex), metaKey);
					if (qcContext == null)
						throw new WaspException("Data missing for cell index " + cellIndex);
					cellQcResults.put(cellIndex, qcContext);
				}
				qcDataMap.put(metaKey, cellQcResults);
			}
		} catch (Exception e) {
			logger.warn(e.getLocalizedMessage());
			waspErrorMessage("waspIlluminaPlugin.qcRetrieval.error");
		}
		m.addAttribute("qcHeadingsByMetaKey", qcHeadingsByMetaKey);
		m.addAttribute("qcDataMap", qcDataMap);
		return "waspIlluminaPlatform/postrunqc/updatequalityreport";
	}
	
	@RequestMapping(value="/run/{runId}/updateQualityReport", method=RequestMethod.POST)
	public String processQualityReport(@PathVariable("runId") Integer runId, ModelMap m){
		Run run = runService.getRunById(runId);
		if (run.getId() == null){
			logger.warn("No run found with id of " + runId + " in model");
			waspErrorMessage("run.invalid_id.error");
			return "redirect:/dashboard.do";
		}
		try {
			processPostRequest(run, CellSuccessQcMetaKey.RUN_SUCCESS);
		} catch (SampleTypeException e1) {
			logger.warn(e1.getLocalizedMessage());
			waspErrorMessage("waspIlluminaPlugin.notPu.error");
			return "redirect:/dashboard.do";
		} catch (FormParameterException e1) {
			logger.warn(e1.getLocalizedMessage());
			waspErrorMessage("waspIlluminaPlugin.formParameter.error");
			return "redirect:/dashboard.do";
		} catch (Exception e){
			logger.warn(e.getLocalizedMessage());
			waspErrorMessage("waspIlluminaPlugin.update.error");
			return "redirect:/dashboard.do";
		}
		try {
			// send message to daemon to indicate run qc completed
			runService.updateRunQcStatusSetComplete(run);
		} catch (WaspMessageBuildingException e) {
			logger.warn(e.getLocalizedMessage());
			if (!isInDemoMode)
				waspErrorMessage("waspIlluminaPlugin.updateQcfailed.label");
			else
				waspErrorMessage("waspIlluminaPlugin.warnNoRunStart.label");
			return "redirect:/dashboard.do";
		}
		waspMessage("waspIlluminaPlugin.updateQcsuccess.label");
		return "redirect:/dashboard.do";
	}
	
	@RequestMapping(value="/list.do", method=RequestMethod.GET)
	public String listRunsRequiringQc(ModelMap m){
		List<Hyperlink> hyperlinks = new ArrayList<Hyperlink>();
		for (Run run: runService.getRunsAwaitingQc())
			hyperlinks.add(new Hyperlink(run.getName(), "/waspIlluminaPlatform/postRunQC/run/" + run.getId() + "/displayFocusQualityCharts.do"));
		m.addAttribute("taskHyperlinks", hyperlinks);
		m.addAttribute("isTasks", (hyperlinks.isEmpty()) ? false : true);
		return "waspIlluminaPlatform/postrunqc/list";
	}


}
