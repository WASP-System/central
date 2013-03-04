package edu.yu.einstein.wasp.controller.illumina;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.yu.einstein.wasp.Hyperlink;
import edu.yu.einstein.wasp.controller.WaspController;
import edu.yu.einstein.wasp.exception.FormParameterException;
import edu.yu.einstein.wasp.exception.ModelIdException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.illumina.WaspIlluminaQcService;
import edu.yu.einstein.wasp.service.impl.illumina.WaspIlluminaQcServiceImpl.CellSuccessQcMetaKey;
import edu.yu.einstein.wasp.util.illumina.IlluminaQcContext;
import edu.yu.einstein.wasp.web.WebHyperlink;

@Controller
@RequestMapping("/wasp-illumina/postRunQC")
public class WaspIlluminaPostRunQcController extends WaspController{
	
	Logger logger = LoggerFactory.getLogger(WaspIlluminaPostRunQcController.class);

	@Autowired
	RunService runService;
	
	@Autowired
	SampleService sampleService;
	
	@Autowired
	WaspIlluminaQcService illuminaQcService;
	
	@Autowired
	@Qualifier("messageServiceWebappImpl")
	MessageService messageService;
	
	// *** TODO: remove this section in production code ***
	private static final String TEST_BASE_URL = "http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX"; 
	
	private List<URL> getTestImageFileUrlList(String subFolder, String metaKey){
		List<URL> imageFileUrlList = new ArrayList<URL>();
		String[] bases = {"a", "c", "t", "g"};
		try {
			for (int i=1; i <= 120; i++){
				if (metaKey.equals(CellSuccessQcMetaKey.NUMGT30)){
					imageFileUrlList.add(new URL("http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/" + subFolder + "/Chart_" + i + ".png"));
				} else {
				for (String b : bases)
					imageFileUrlList.add(new URL("http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/" + subFolder + "/Chart_" + i + "_" + b + ".png"));
				}
			}
		} catch (MalformedURLException e) {
			logger.warn("malformed url: " + e.getLocalizedMessage());
		}
		return imageFileUrlList;
	}
	// *** end of remove in production code section ***
	
	private void setImageDataModelParametersForGetRequest(String metaKey, String chartSubFolder, ModelMap m) {
		// TODO: replace imageFileUrlList with proper list from database (filegroups)
		List<URL> imageFileUrlList = getTestImageFileUrlList(chartSubFolder, metaKey);
		
		String runReportBaseImagePath = TEST_BASE_URL; // TODO: replace in production code with method call
		m.addAttribute("runReportBaseImagePath", runReportBaseImagePath);
		m.addAttribute("chartSubFolder", chartSubFolder);
		m.addAttribute("imageFileUrlList", imageFileUrlList);
		m.addAttribute("numCycles", imageFileUrlList.size() / 4);
	}
	
	private void setCoreDataModelParametersForGetRequest(Integer runId, String metaKey, ModelMap m) throws ModelIdException{
		Run run = runService.getRunById(runId);
		if (run.getRunId() == null)
			throw new ModelIdException("No run found with id of " + runId + " in model");
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
		
		m.addAttribute("cellIndexList", cellIndexList);
		m.addAttribute("runName", run.getName());
		m.addAttribute("existingQcValuesIndexed",indexedQcData);
	}
	
	private void processPostRequest(Integer runId, String metaKey) throws SampleTypeException, ModelIdException, FormParameterException{
		Run run = runService.getRunById(runId);
		if (run.getRunId() == null)
			throw new ModelIdException("No run found with id of " + runId + " in model");
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
	
	@RequestMapping(value="/displayFocusQualityCharts/{runId}", method=RequestMethod.GET)
	public String displayFocusQualityCharts(@PathVariable("runId") Integer runId, ModelMap m){
		setImageDataModelParametersForGetRequest(CellSuccessQcMetaKey.FOCUS, "FWHM", m);
		try{
			setCoreDataModelParametersForGetRequest(runId, CellSuccessQcMetaKey.FOCUS, m);
		} catch(ModelIdException e){
			logger.warn(e.getLocalizedMessage());
			waspErrorMessage("run.invalid_id.error");
			return "redirect:/dashboard.do";
		}
		return "wasp-illumina/postrunqc/displayfocusqualitycharts";
	}
	
	@RequestMapping(value="/displayFocusQualityCharts/{runId}", method=RequestMethod.POST)
	public String processFocusQualityCharts(@PathVariable("runId") Integer runId, ModelMap m){
		try {
			processPostRequest(runId, CellSuccessQcMetaKey.FOCUS);
		} catch (SampleTypeException e1) {
			logger.warn(e1.getLocalizedMessage());
			waspErrorMessage("waspIlluminaPlugin.puCells.error");
			return "redirect:/dashboard.do";
		} catch (ModelIdException e1) {
			logger.warn(e1.getLocalizedMessage());
			waspErrorMessage("run.invalid_id.error");
			return "redirect:/dashboard.do";
		} catch (FormParameterException e1) {
			logger.warn(e1.getLocalizedMessage());
			waspErrorMessage("waspIlluminaPlugin.formParameter.error");
			return "redirect:/dashboard.do";
		} catch (Exception e){
			waspErrorMessage("waspIlluminaPlugin.update.error");
			return "redirect:/dashboard.do";
		}
		return "redirect:/wasp-illumina/postRunQC/displayIntensityCharts/" + runId + ".do";
	}
	
	@RequestMapping(value="/displayIntensityCharts/{runId}", method=RequestMethod.GET)
	public String displayIntensityCharts(@PathVariable("runId") Integer runId, ModelMap m){
		setImageDataModelParametersForGetRequest(CellSuccessQcMetaKey.INTENSITY, "Intensity", m);
		try{
			setCoreDataModelParametersForGetRequest(runId, CellSuccessQcMetaKey.INTENSITY, m);
		} catch(ModelIdException e){
			logger.warn(e.getLocalizedMessage());
			waspErrorMessage("run.invalid_id.error");
			return "redirect:/dashboard.do";
		}
		return "wasp-illumina/postrunqc/displayinstensitycharts";
	}
	
	@RequestMapping(value="/displayIntensityCharts/{runId}", method=RequestMethod.POST)
	public String processIntensityCharts(@PathVariable("runId") Integer runId, ModelMap m){
		try {
			processPostRequest(runId, CellSuccessQcMetaKey.INTENSITY);
		} catch (SampleTypeException e1) {
			logger.warn(e1.getLocalizedMessage());
			waspErrorMessage("waspIlluminaPlugin.puCells.error");
			return "redirect:/dashboard.do";
		} catch (ModelIdException e1) {
			logger.warn(e1.getLocalizedMessage());
			waspErrorMessage("run.invalid_id.error");
			return "redirect:/dashboard.do";
		} catch (FormParameterException e1) {
			logger.warn(e1.getLocalizedMessage());
			waspErrorMessage("waspIlluminaPlugin.formParameter.error");
			return "redirect:/dashboard.do";
		} catch (Exception e){
			waspErrorMessage("waspIlluminaPlugin.update.error");
			return "redirect:/dashboard.do";
		}
		return "redirect:/wasp-illumina/postRunQC/displayNumGT30Charts/" + runId + ".do";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/displayNumGT30Charts/{runId}", method=RequestMethod.GET)
	public String displayNumGT30Charts(@PathVariable("runId") Integer runId, ModelMap m){
		setImageDataModelParametersForGetRequest(CellSuccessQcMetaKey.NUMGT30, "NumGT30", m);
		try{
			setCoreDataModelParametersForGetRequest(runId, CellSuccessQcMetaKey.NUMGT30, m);
		} catch(ModelIdException e){
			logger.warn(e.getLocalizedMessage());
			waspErrorMessage("run.invalid_id.error");
			return "redirect:/dashboard.do";
		}
		m.addAttribute("qscoreSubFolder", "ByCycle");
		try {
			for (Integer index : (List<Integer>) m.get("cellIndexList"))
				((List<URL>) m.get("imageFileUrlList"))
					.add(new URL("http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/ByCycle/QScore_L" + index + ".png"));
		} catch (MalformedURLException e) {
			logger.warn(e.getLocalizedMessage());
		}
		return "wasp-illumina/postrunqc/displaynumgt30charts";
	}
	
	
	@RequestMapping(value="/displayNumGT30Charts/{runId}", method=RequestMethod.POST)
	public String processNumGT30Charts(@PathVariable("runId") Integer runId, ModelMap m){
		try {
			processPostRequest(runId, CellSuccessQcMetaKey.NUMGT30);
		} catch (SampleTypeException e1) {
			logger.warn(e1.getLocalizedMessage());
			waspErrorMessage("waspIlluminaPlugin.puCells.error");
			return "redirect:/dashboard.do";
		} catch (ModelIdException e1) {
			logger.warn(e1.getLocalizedMessage());
			waspErrorMessage("run.invalid_id.error");
			return "redirect:/dashboard.do";
		} catch (FormParameterException e1) {
			logger.warn(e1.getLocalizedMessage());
			waspErrorMessage("waspIlluminaPlugin.formParameter.error");
			return "redirect:/dashboard.do";
		} catch (Exception e){
			waspErrorMessage("waspIlluminaPlugin.update.error");
			return "redirect:/dashboard.do";
		}
		return "redirect:/wasp-illumina/postRunQC/displayClusterDensityChart/" + runId + ".do";
	}
	
	@RequestMapping(value="/displayClusterDensityChart/{runId}", method=RequestMethod.GET)
	public String displayClusterDensityChart(@PathVariable("runId") Integer runId, ModelMap m){
		setImageDataModelParametersForGetRequest(CellSuccessQcMetaKey.CLUSTER_DENSITY, "", m);
		try{
			setCoreDataModelParametersForGetRequest(runId, CellSuccessQcMetaKey.CLUSTER_DENSITY, m);
		} catch(ModelIdException e){
			logger.warn(e.getLocalizedMessage());
			waspErrorMessage("run.invalid_id.error");
			return "redirect:/dashboard.do";
		}
		return "wasp-illumina/postrunqc/displayclusterdensitychart";
	}
	
	@RequestMapping(value="/displayClusterDensityChart/{runId}", method=RequestMethod.POST)
	public String processClusterDensityChart(@PathVariable("runId") Integer runId, ModelMap m){
			try {
				processPostRequest(runId, CellSuccessQcMetaKey.CLUSTER_DENSITY);
			} catch (SampleTypeException e1) {
				logger.warn(e1.getLocalizedMessage());
				waspErrorMessage("waspIlluminaPlugin.puCells.error");
				return "redirect:/dashboard.do";
			} catch (ModelIdException e1) {
				logger.warn(e1.getLocalizedMessage());
				waspErrorMessage("run.invalid_id.error");
				return "redirect:/dashboard.do";
			} catch (FormParameterException e1) {
				logger.warn(e1.getLocalizedMessage());
				waspErrorMessage("waspIlluminaPlugin.formParameter.error");
				return "redirect:/dashboard.do";
			} catch (Exception e){
				waspErrorMessage("waspIlluminaPlugin.update.error");
				return "redirect:/dashboard.do";
			}
		return "redirect:/wasp-illumina/postRunQC/updateQualityReport/1.do";
	}
	
	@RequestMapping(value="/updateQualityReport/{runId}", method=RequestMethod.GET)
	public String updateQualityReport(@PathVariable("runId") Integer runId, ModelMap m){
		Map<String, Map<Integer, IlluminaQcContext>> qcDataMap = new HashMap<String, Map<Integer,IlluminaQcContext>>();
		Run run = runService.getRunById(runId);
		if (run.getRunId() == null){
			logger.warn("No run found with id of " + runId + " in model");
			waspErrorMessage("run.invalid_id.error");
			return "redirect:/dashboard.do";
		}
		try{
			setCoreDataModelParametersForGetRequest(runId, CellSuccessQcMetaKey.RUN_SUCCESS, m);
		} catch(ModelIdException e){
			logger.warn(e.getLocalizedMessage());
			waspErrorMessage("run.invalid_id.error");
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
				Map<Integer, IlluminaQcContext> cellQcResults = new HashMap<Integer, IlluminaQcContext>();
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
		return "wasp-illumina/postrunqc/updatequalityreport";
	}
	
	@RequestMapping(value="/updateQualityReport/{runId}", method=RequestMethod.POST)
	public String processQualityReport(@PathVariable("runId") Integer runId, ModelMap m){
		try {
			processPostRequest(runId, CellSuccessQcMetaKey.RUN_SUCCESS);
		} catch (SampleTypeException e1) {
			logger.warn(e1.getLocalizedMessage());
			waspErrorMessage("waspIlluminaPlugin.puCells.error");
			return "redirect:/dashboard.do";
		} catch (ModelIdException e1) {
			logger.warn(e1.getLocalizedMessage());
			waspErrorMessage("run.invalid_id.error");
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
		waspMessage("waspIlluminaPlugin.updateQcsuccess.label");
		return "redirect:/dashboard.do";
	}
	
	@RequestMapping(value="/list.do", method=RequestMethod.GET)
	public String listRunsRequiringQc(ModelMap m){
		List<Hyperlink> hyperlinks = new ArrayList<Hyperlink>();
		for (Run run: runService.getRunsAwaitingQc())
			hyperlinks.add(new Hyperlink(run.getName(), "/wasp-illumina/postRunQC/displayFocusQualityCharts/" + run.getRunId() + ".do"));
		m.addAttribute("taskHyperlinks", hyperlinks);
		m.addAttribute("isTasks", (hyperlinks.isEmpty()) ? false : true);
		return "wasp-illumina/postrunqc/list";
	}


}
