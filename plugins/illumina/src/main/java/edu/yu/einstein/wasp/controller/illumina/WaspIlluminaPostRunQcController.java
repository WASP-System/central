package edu.yu.einstein.wasp.controller.illumina;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.yu.einstein.wasp.controller.WaspController;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.illumina.WaspIlluminaSampleService;
import edu.yu.einstein.wasp.serviceImpl.illumina.WaspIlluminaSampleServiceImpl;
import edu.yu.einstein.wasp.util.illumina.IlluminaQcContext;

@Controller
@RequestMapping("/wasp-illumina/postRunQC")
public class WaspIlluminaPostRunQcController extends WaspController{
	
	Logger logger = LoggerFactory.getLogger(WaspIlluminaPostRunQcController.class);

	@Autowired
	RunService runService;
	
	@Autowired
	WaspIlluminaSampleService sampleService;
	
	private List<URL> getTestImageFileUrlList(){
		// TODO: remove this method in production code
		List<URL> imageFileUrlList = new ArrayList<URL>();
		String[] bases = {"a", "c", "t", "g"};
		for (int i=1; i <= 120; i++)
			for (String b : bases)
				try {
					imageFileUrlList.add(new URL("http://wasp.einstein.yu.edu/results/IlluminaOLBStatsLinks/110720_SN844_0092_B81M6DABXX/FWHM/Chart_" + i + "_" + b + ".png"));
				} catch (MalformedURLException e) {
					logger.warn("malformed url: " + e.getLocalizedMessage());
				}
		return imageFileUrlList;
	}
	
	@RequestMapping(value="/displayFocusQualityCharts/{runId}", method=RequestMethod.GET)
	public String displayFocusQualityCharts(@PathVariable("runId") Integer runId, ModelMap m){
		Run run = runService.getRunById(runId);
		if (run.getRunId() == null){
			waspErrorMessage("run.invalid_id.error");
			return "redirect:/dashboard.do";
		}
		// TODO: replace imageFileUrlList with proper list from database (filegroups)
		List<URL> imageFileUrlList = getTestImageFileUrlList();
		
		List<Integer> cellList = new ArrayList<Integer>();
		
		try {
			for (int i=1; i <= sampleService.getNumberOfIndexedCellsOnPlatformUnit(run.getPlatformUnit()); i++)
				cellList.add(i);
		} catch (SampleTypeException e) {
			logger.warn(e.getLocalizedMessage());
		}
		
		String runReportBaseImagePath = StringUtils.substringBeforeLast(imageFileUrlList.get(0).toString(), "/");
		m.addAttribute("runReportBaseImagePath", runReportBaseImagePath);
		m.addAttribute("imageFileUrlList", imageFileUrlList);
		m.addAttribute("numCycles", imageFileUrlList.size() / 4);
		m.addAttribute("cellList", cellList);
		m.addAttribute("runName", run.getName());
		return "wasp-illumina/postrunqc/displayfocusqualitycharts";
	}
	
	@RequestMapping(value="/displayFocusQualityCharts/{runId}", method=RequestMethod.POST)
	public String processFocusQualityCharts(@PathVariable("runId") Integer runId, ModelMap m){
		Run run = runService.getRunById(runId);
		if (run.getRunId() == null){
			waspErrorMessage("run.invalid_id.error");
			return "redirect:/dashboard.do";
		}
		Sample pu = run.getPlatformUnit();
		List<IlluminaQcContext> qcContextList = new ArrayList<IlluminaQcContext>();
		try {
			Map<Integer, Sample> cells = sampleService.getIndexedCellsOnPlatformUnit(pu);
			for(Integer cellIndex : cells.keySet()){
				IlluminaQcContext qcContext = new IlluminaQcContext();
				qcContext.setCell(cells.get(cellIndex));
				int passed = 0;
				try{
					passed = Integer.parseInt(request.getParameter("radioL" + cellIndex));
				} catch(NumberFormatException e){
					waspErrorMessage("waspIlluminaPlugin.formParameter.error");
					logger.warn("Could not parse form parameter radioL" + cellIndex);
					return "redirect:/dashboard.do";
				}
				qcContext.setPassed((passed == 1) ? true:false);
				String comment = request.getParameter("commentsL");
				if (comment == null){
					waspErrorMessage("waspIlluminaPlugin.formParameter.error");
					logger.warn("Could not parse form parameter commentsL" + cellIndex);
					return "redirect:/dashboard.do";
				}
				qcContext.setComment(comment);
				qcContextList.add(qcContext);
			}
		} catch (SampleTypeException e) {
			logger.warn(e.getLocalizedMessage());
			waspErrorMessage("waspIlluminaPlugin.puCells.error");
			return "redirect:/dashboard.do";
		} 
		try{
			sampleService.updateQc(qcContextList, WaspIlluminaSampleServiceImpl.CELL_SUCCESS_META_KEY_FOCUS_QC);
		} catch (Exception e){
			waspErrorMessage("waspIlluminaPlugin.update.error");
			return "redirect:/dashboard.do";
		}
		return "redirect:/wasp-illumina/postRunQC/displayIntensityCharts.do";
	}
	
	@RequestMapping(value="/displayIntensityCharts", method=RequestMethod.GET)
	public String displayIntensityCharts(ModelMap m){
		// TODO: replace null with filegroup path (IlluminaRunFolder/Data/reports/Intensity)  
		String runReportBaseImagePath = null;
		m.addAttribute("runReportBaseImagePath", runReportBaseImagePath);
		m.addAttribute("imageFileUrlList", null);
		m.addAttribute("numCycles", null);
		m.addAttribute("cellList", null);
		return "wasp-illumina/postrunqc/displayinstensitycharts";
	}
	
	@RequestMapping(value="/displayIntensityCharts", method=RequestMethod.POST)
	public String processIntensityCharts(ModelMap m){
		// TODO: add functionality here 
		return "redirect:/wasp-illumina/postRunQC/displayNumGT30Charts.do";
	}
	
	@RequestMapping(value="/displayNumGT30Charts", method=RequestMethod.GET)
	public String displayNumGT30Charts(ModelMap m){
		// TODO: replace null with filegroup path (IlluminaRunFolder/Data/reports/NumGT30)  
		String runReportBaseImagePath = null;
		m.addAttribute("runReportBaseImagePath", runReportBaseImagePath);
		m.addAttribute("imageFileUrlList", null);
		m.addAttribute("numCycles", null);
		m.addAttribute("cellList", null);
		return "wasp-illumina/postrunqc/displaynumgt30Charts";
	}
	
	@RequestMapping(value="/displayNumGT30Charts", method=RequestMethod.POST)
	public String processNumGT30Charts(ModelMap m){
		// TODO: add functionality here 
		return "redirect:/wasp-illumina/postRunQC/displayClusterDensityCharts.do";
	}
	
	@RequestMapping(value="/displayClusterDensityChart", method=RequestMethod.GET)
	public String displayClusterDensityChart(ModelMap m){
		// TODO: replace null with filegroup path (IlluminaRunFolder/Data/reports)  
		String runReportBaseImagePath = null;
		m.addAttribute("runReportBaseImagePath", runReportBaseImagePath);
		m.addAttribute("imageFileUrlList", null);
		m.addAttribute("numCycles", null);
		m.addAttribute("cellList", null);
		return "wasp-illumina/postrunqc/displayclusterdensitychart";
	}
	
	@RequestMapping(value="/displayClusterDensityChart", method=RequestMethod.POST)
	public String processClusterDensityChart(ModelMap m){
		// TODO: add functionality here 
		return "redirect:/wasp-illumina/postRunQC/updateQualityReport.do";
	}
	
	@RequestMapping(value="/updateQualityReport", method=RequestMethod.GET)
	public String updateQualityReport(ModelMap m){
		// TODO: add functionality here
		return "wasp-illumina/postrunqc/updatequalityreport";
	}
	
	@RequestMapping(value="/updateQualityReport", method=RequestMethod.POST)
	public String processQualityReport(ModelMap m){
		// TODO: add functionality here 
		return "redirect:/wasp-illumina/postRunQC/commitQC.do";
	}
	
	@RequestMapping(value="/commitQC", method=RequestMethod.GET)
	public String commitQC(ModelMap m){
		// TODO: add functionality here
		return "wasp-illumina/postrunqc/commitqc";
	}
	
	@RequestMapping(value="/commitQC", method=RequestMethod.POST)
	public String processQC(ModelMap m){
		// TODO: add functionality here 
		return "wasp-illumina/postrunqc/done";
	}

}
