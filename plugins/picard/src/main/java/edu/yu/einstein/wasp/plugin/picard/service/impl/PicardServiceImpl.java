/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.picard.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridTransportConnection;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.SampleSourceMeta;
import edu.yu.einstein.wasp.plugin.picard.service.PicardService;

import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;

@Service
@Transactional("entityManager")
public class PicardServiceImpl extends WaspServiceImpl implements PicardService {
	
	private static final String BAMFILE_ALIGNMENT_METRICS_META_AREA = "bamFile";
	private static final String BAMFILE_ALIGNMENT_METRICS_META_SPECIFIER = "alignmentMetrics";
	private static final String BAMFILE_ALIGNMENT_METRICS_META_KEY = BAMFILE_ALIGNMENT_METRICS_META_AREA + "." + BAMFILE_ALIGNMENT_METRICS_META_SPECIFIER;

	@Autowired
	SampleService sampleService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String performAction() {
		// do something
		return "done";
	}

	public void setAlignmentMetrics(SampleSource cellLib, JSONObject json)throws MetadataException{
		sampleService.setLibraryOnCellMeta(cellLib, BAMFILE_ALIGNMENT_METRICS_META_AREA, BAMFILE_ALIGNMENT_METRICS_META_SPECIFIER, json.toString());
	}
	private String getAlignmentMetric(SampleSource cellLib, String jsonKey){
		String value = "";
		JSONObject json = getAlignmentMetricsMetaAsJSON(cellLib);
		if(json.has(jsonKey)){
			value = json.getString(jsonKey);
		}
		return value;
	}
	private JSONObject getAlignmentMetricsMetaAsJSON(SampleSource cellLib){
		
		JSONObject jsonObj = null;
		String meta = "";
		for(SampleSourceMeta ssm : cellLib.getSampleSourceMeta()){
			if(ssm.getK().equalsIgnoreCase(BAMFILE_ALIGNMENT_METRICS_META_KEY)){
				meta = ssm.getV();
			}
		}
		if(!meta.isEmpty()){
			jsonObj = new JSONObject(meta);
		}
		return jsonObj;
	}
	
	public String getUnpairedMappedReads(SampleSource cellLib){		
		return this.getAlignmentMetric(cellLib, BAMFILE_ALIGNMENT_METRIC_UNPAIRED_READS);
	}
	public String getPairedMappedReads(SampleSource cellLib){		
		return this.getAlignmentMetric(cellLib, BAMFILE_ALIGNMENT_METRIC_PAIRED_READS);
	}
	public String getUnmappedReads(SampleSource cellLib){		
		return this.getAlignmentMetric(cellLib, BAMFILE_ALIGNMENT_METRIC_UNMAPPED_READS);
	}
	public String getUnpairedMappedReadDuplicates(SampleSource cellLib){		
		return this.getAlignmentMetric(cellLib, BAMFILE_ALIGNMENT_METRIC_UNPAIRED_READ_DUPLICATES);
	}
	public String getPairedMappedReadDuplicates(SampleSource cellLib){		
		return this.getAlignmentMetric(cellLib, BAMFILE_ALIGNMENT_METRIC_PAIRED_READ_DUPLICATES);
	}
	public String getPairedMappedReadOpticalDuplicates(SampleSource cellLib){		
		return this.getAlignmentMetric(cellLib, BAMFILE_ALIGNMENT_METRIC_PAIRED_READ_OPTICAL_DUPLICATES);
	}
	
	public String getFractionMapped(SampleSource cellLib){		
		return this.getAlignmentMetric(cellLib, BAMFILE_ALIGNMENT_METRIC_FRACTION_MAPPED);
	}
	public String getMappedReads(SampleSource cellLib){		
		return this.getAlignmentMetric(cellLib, BAMFILE_ALIGNMENT_METRIC_MAPPED_READS);
	}
	public String getTotalReads(SampleSource cellLib){		
		return this.getAlignmentMetric(cellLib, BAMFILE_ALIGNMENT_METRIC_TOTAL_READS);
	}	
	public String getFractionMappedAsCalculation(SampleSource cellLib){		
		String fractionMapped = this.getFractionMapped(cellLib);
		String mappedReads = this.getMappedReads(cellLib);
		String totalReads = this.getTotalReads(cellLib);		
		return mappedReads + " / " + totalReads + " = " + fractionMapped;
	}
	
	public String getFractionDuplicated(SampleSource cellLib){		
		return this.getAlignmentMetric(cellLib, BAMFILE_ALIGNMENT_METRIC_FRACTION_DUPLICATED);//fraction of mapped reads
	}
	public String getDuplicateReads(SampleSource cellLib){		
		return this.getAlignmentMetric(cellLib, BAMFILE_ALIGNMENT_METRIC_DUPLICATE_READS);
	}
	public String getFractionDuplicatedAsCalculation(SampleSource cellLib){	
		String fractionDuplicated = getFractionDuplicated(cellLib);
		String duplicateReads = getDuplicateReads(cellLib);
		String mappedReads = this.getMappedReads(cellLib);		
		return duplicateReads + " / " + mappedReads + " = " + fractionDuplicated;	}
	
	public String getFractionUniqueNonRedundant(SampleSource cellLib){	
		return this.getAlignmentMetric(cellLib, BAMFILE_ALIGNMENT_METRIC_FRACTION_UNIQUE_NONREDUNDANT);
	}
	public String getUniqueReads(SampleSource cellLib){	
		return this.getAlignmentMetric(cellLib, BAMFILE_ALIGNMENT_METRIC_UNIQUE_READS);
	}
	public String getUniqueNonRedundantReads(SampleSource cellLib){	
		return this.getAlignmentMetric(cellLib, BAMFILE_ALIGNMENT_METRIC_UNIQUE_NONREDUNDANT_READS);
	}
	public String getFractionUniqueNonRedundantAsCalculation(SampleSource cellLib){	
		String fractionUniqueNonRedundant = getFractionUniqueNonRedundant(cellLib);
		String uniqueReads = getUniqueReads(cellLib);
		String uniqueNonRedundantReads = getUniqueNonRedundantReads(cellLib);		
		return uniqueNonRedundantReads + " / " + uniqueReads + " = " + fractionUniqueNonRedundant;
	}
}
