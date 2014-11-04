/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.picard.service.impl;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileGroupMeta;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.plugin.fileformat.service.BamService;
import edu.yu.einstein.wasp.plugin.picard.service.PicardService;
import edu.yu.einstein.wasp.plugin.picard.webpanels.PicardWebPanels;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

@Service
@Transactional("entityManager")
public class PicardServiceImpl extends WaspServiceImpl implements PicardService {
	

	@Autowired
	SampleService sampleService;
	@Autowired
	FileService fileService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String performAction() {
		// do something
		return "done";
	}

	
	public boolean alignmentMetricsExist(FileGroup fileGroup){
		JSONObject jsonObj = getAlignmentMetricsMetaAsJSON(fileGroup);
		if(jsonObj != null){
			return true;
		}
		return false;
	}
	private String getAlignmentMetric(FileGroup fileGroup, String jsonKey){
		String value = "";
		JSONObject json = getAlignmentMetricsMetaAsJSON(fileGroup);
		if(json.has(jsonKey)){
			value = json.getString(jsonKey);
		}
		return value;
	}
	private JSONObject getAlignmentMetricsMetaAsJSON(FileGroup fileGroup){
		
		JSONObject jsonObj = null;
		String meta = "";
		for(FileGroupMeta fgm : fileGroup.getFileGroupMeta()){
			if(fgm.getK().equalsIgnoreCase(BamService.BAMFILE_ALIGNMENT_METRICS_META_KEY)){
				meta = fgm.getV();
			}
		}
		if(!meta.isEmpty()){
			jsonObj = new JSONObject(meta);
		}
		return jsonObj;
	}
	
	public String getUnpairedMappedReads(FileGroup fileGroup){		
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_UNPAIRED_READS);
	}
	public String getPairedMappedReads(FileGroup fileGroup){		
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_PAIRED_READS);
	}
	public String getUnmappedReads(FileGroup fileGroup){		
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_UNMAPPED_READS);
	}
	public String getUnpairedMappedReadDuplicates(FileGroup fileGroup){		
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_UNPAIRED_READ_DUPLICATES);
	}
	public String getPairedMappedReadDuplicates(FileGroup fileGroup){		
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_PAIRED_READ_DUPLICATES);
	}
	public String getPairedMappedReadOpticalDuplicates(FileGroup fileGroup){		
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_PAIRED_READ_OPTICAL_DUPLICATES);
	}
	
	public String getFractionMapped(FileGroup fileGroup){		
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_FRACTION_MAPPED);
	}
	public String getMappedReads(FileGroup fileGroup){		
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_MAPPED_READS);
	}
	public String getTotalReads(FileGroup fileGroup){		
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_TOTAL_READS);
	}	
	public String getFractionMappedAsCalculation(FileGroup fileGroup){		
		String fractionMapped = this.getFractionMapped(fileGroup);
		String mappedReads = this.getMappedReads(fileGroup);
		String totalReads = this.getTotalReads(fileGroup);		
		return mappedReads + " / " + totalReads + " = " + fractionMapped;
	}
	
	public String getFractionDuplicated(FileGroup fileGroup){		
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_FRACTION_DUPLICATED);//fraction of mapped reads
	}
	public String getDuplicateReads(FileGroup fileGroup){		
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_DUPLICATE_READS);
	}
	public String getFractionDuplicatedAsCalculation(FileGroup fileGroup){	
		String fractionDuplicated = getFractionDuplicated(fileGroup);
		String duplicateReads = getDuplicateReads(fileGroup);
		String mappedReads = this.getMappedReads(fileGroup);		
		return duplicateReads + " / " + mappedReads + " = " + fractionDuplicated;	}
	public String getUniqueReads(FileGroup fileGroup){
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_READS_FROM_ALL);
	}
	
	public String getUniqueNonRedundantReads(FileGroup fileGroup){
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_NONREDUNDANT_READS_FROM_ALL);
	}
	
	public String getNonRedundantReadFraction(FileGroup fileGroup){
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_FRACTION_UNIQUE_NONREDUNDANT_FROM_ALL);
	}
	
	
	public PanelTab getAlignmentMetricsForDisplay(FileGroup fileGroup){
		
		List<FileHandle> fileHandleList = new ArrayList<FileHandle>(fileGroup.getFileHandles());
		String bamFileName = fileHandleList.get(0).getFileName();
		
		Map<String,Map<String,String>> metrics = new LinkedHashMap<String,Map<String,String>>();
		
		Map<String,String> mapped = new LinkedHashMap<String, String>();
		mapped.put("Total Reads", formatWithCommas(getTotalReads(fileGroup)));
		mapped.put("Unmapped Reads", formatWithCommas(getUnmappedReads(fileGroup)));		
		mapped.put("Mapped Reads", formatWithCommas(getMappedReads(fileGroup)));		
		mapped.put("Fraction Mapped", getFractionMapped(fileGroup));
		metrics.put("Alignment Stats:", mapped);
		
		Map<String,String> duplicate = new LinkedHashMap<String, String>();		
		duplicate.put("Mapped Reads", formatWithCommas(getMappedReads(fileGroup)));
		duplicate.put("Duplicate Mapped Reads", formatWithCommas(getDuplicateReads(fileGroup)));
		duplicate.put("Fraction Duplicated", getFractionDuplicated(fileGroup));
		metrics.put("Duplicate Stats:", duplicate);
		
		if(getUniqueReads(fileGroup)!=null && !getUniqueReads(fileGroup).isEmpty()){
			Map<String,String> nrf = new LinkedHashMap<String, String>();
			nrf.put("NRF From All Uniquely-Mapped Reads", getNonRedundantReadFraction(fileGroup)  + " = " + formatWithCommas(getUniqueNonRedundantReads(fileGroup)) + " / " + formatWithCommas(getUniqueReads(fileGroup)));
			metrics.put("Non-Redundant Read Fraction From Uniquely-Mapped Reads (NRF):", nrf);
		}
		
		return PicardWebPanels.getAlignmentMetrics(metrics, bamFileName);
	}
	private String formatWithCommas(String unformatedIntegerAsString){	
		//getNumberInstance() will return a general-purpose number format for the current default locale, so for USA, 1000 is converted to 1,000   
		String s = NumberFormat.getIntegerInstance().format(Long.valueOf(unformatedIntegerAsString));
		return s;
		
	}
}
