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
	
	public String getFractionUniqueNonRedundant(FileGroup fileGroup){	
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_FRACTION_UNIQUE_NONREDUNDANT);
	}
	public String getUniqueReads(FileGroup fileGroup){	
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_READS);
	}
	public String getUniqueNonRedundantReads(FileGroup fileGroup){	
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_NONREDUNDANT_READS);
	}
	public String getFractionUniqueNonRedundantAsCalculation(FileGroup fileGroup){	
		String fractionUniqueNonRedundant = getFractionUniqueNonRedundant(fileGroup);
		String uniqueReads = getUniqueReads(fileGroup);
		String uniqueNonRedundantReads = getUniqueNonRedundantReads(fileGroup);		
		return uniqueNonRedundantReads + " / " + uniqueReads + " = " + fractionUniqueNonRedundant;
	}
	
	public String getUniqueReadsFor2M(FileGroup fileGroup){
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_READS_FROM_2M);
	}
	public String getUniqueReadsFor5M(FileGroup fileGroup){
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_READS_FROM_5M);
	}
	public String getUniqueReadsFor10M(FileGroup fileGroup){
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_READS_FROM_10M);
	}
	public String getUniqueReadsFor20M(FileGroup fileGroup){
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_READS_FROM_20M);
	}	
	public String getUniqueReadsForAll(FileGroup fileGroup){
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_READS_FROM_ALL);
	}
	
	public String getUniqueNonRedundantReadsFor2M(FileGroup fileGroup){
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_NONREDUNDANT_READS_FROM_2M);
	}
	public String getUniqueNonRedundantReadsFor5M(FileGroup fileGroup){
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_NONREDUNDANT_READS_FROM_5M);
	}
	public String getUniqueNonRedundantReadsFor10M(FileGroup fileGroup){
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_NONREDUNDANT_READS_FROM_10M);
	}
	public String getUniqueNonRedundantReadsFor20M(FileGroup fileGroup){
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_NONREDUNDANT_READS_FROM_20M);
	}
	public String getUniqueNonRedundantReadsForAll(FileGroup fileGroup){
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_NONREDUNDANT_READS_FROM_ALL);
	}
	
	public String getNonRedundantReadFractionFor2M(FileGroup fileGroup){
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_FRACTION_UNIQUE_NONREDUNDANT_FROM_2M);
	}	
	public String getNonRedundantReadFractionFor5M(FileGroup fileGroup){
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_FRACTION_UNIQUE_NONREDUNDANT_FROM_5M);
	}
	public String getNonRedundantReadFractionFor10M(FileGroup fileGroup){
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_FRACTION_UNIQUE_NONREDUNDANT_FROM_10M);
	}
	public String getNonRedundantReadFractionFor20M(FileGroup fileGroup){
		return this.getAlignmentMetric(fileGroup, BamService.BAMFILE_ALIGNMENT_METRIC_FRACTION_UNIQUE_NONREDUNDANT_FROM_20M);
	}
	public String getNonRedundantReadFractionForAll(FileGroup fileGroup){
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
		/*
		Map<String,String> unique = new LinkedHashMap<String, String>();
		unique.put("Uniquely Mapped Reads", formatWithCommas(getUniqueReads(fileGroup)));
		unique.put("Uniquely Mapped &amp; Nonredundant Reads", formatWithCommas(getUniqueNonRedundantReads(fileGroup)));
		unique.put("Fraction Uniquely Mapped &amp; Nonredundant", getFractionUniqueNonRedundant(fileGroup));
		metrics.put("Uniquely Aligned Stats:", unique);
		 */
		Map<String,String> nrf = new LinkedHashMap<String, String>();
		if("2000000".equals(getUniqueReadsFor2M(fileGroup))){
			nrf.put("NRF From First 2M Uniquely-Mapped Reads", getNonRedundantReadFractionFor2M(fileGroup) + " = " + formatWithCommas(getUniqueNonRedundantReadsFor2M(fileGroup)) + " / " + formatWithCommas(getUniqueReadsFor2M(fileGroup)));
		}
		if("5000000".equals(getUniqueReadsFor5M(fileGroup))){
			nrf.put("NRF From First 5M Uniquely-Mapped Reads", getNonRedundantReadFractionFor5M(fileGroup) + " = " + formatWithCommas(getUniqueNonRedundantReadsFor5M(fileGroup)) + " / " + formatWithCommas(getUniqueReadsFor5M(fileGroup)));
		}
		if("10000000".equals(getUniqueReadsFor10M(fileGroup))){
			nrf.put("NRF From First 10M Uniquely-Mapped Reads", getNonRedundantReadFractionFor10M(fileGroup) + " = " + formatWithCommas(getUniqueNonRedundantReadsFor10M(fileGroup)) + " / " + formatWithCommas(getUniqueReadsFor10M(fileGroup)));
		}
		if("20000000".equals(getUniqueReadsFor20M(fileGroup))){
			nrf.put("NRF From First 20M Uniquely-Mapped Reads", getNonRedundantReadFractionFor20M(fileGroup) + " = " + formatWithCommas(getUniqueNonRedundantReadsFor20M(fileGroup)) + " / " + formatWithCommas(getUniqueReadsFor20M(fileGroup)));
		}
		nrf.put("NRF From All Uniquely-Mapped Reads", getNonRedundantReadFractionForAll(fileGroup)  + " = " + formatWithCommas(getUniqueNonRedundantReadsForAll(fileGroup)) + " / " + formatWithCommas(getUniqueReadsForAll(fileGroup)));
		metrics.put("Non-Redundant Fraction From Uniquely-Mapped Reads (NRF):", nrf);		
		return PicardWebPanels.getAlignmentMetrics(metrics, bamFileName);
	}
	private String formatWithCommas(String unformatedIntegerAsString){	
		//getNumberInstance() will return a general-purpose number format for the current default locale, so for USA, 1000 is converted to 1,000   
		String s = NumberFormat.getIntegerInstance().format(Long.valueOf(unformatedIntegerAsString));
		return s;
		
	}
}
