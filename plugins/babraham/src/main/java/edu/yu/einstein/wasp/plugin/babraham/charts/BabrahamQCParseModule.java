package edu.yu.einstein.wasp.plugin.babraham.charts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import edu.yu.einstein.wasp.charts.DataSeries;
import edu.yu.einstein.wasp.charts.WaspBoxPlot;
import edu.yu.einstein.wasp.charts.WaspChart;
import edu.yu.einstein.wasp.charts.WaspChart2D;
import edu.yu.einstein.wasp.plugin.babraham.exception.BabrahamDataParseException;
import edu.yu.einstein.wasp.plugin.babraham.software.BabrahamDataModule;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQC;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQC.PlotType;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQCDataModule;
import edu.yu.einstein.wasp.service.MessageService;

/**
 * 
 * @author asmclellan
 *
 */
public class BabrahamQCParseModule {

	private static List<String> getQCResultsDataRow(final FastQCDataModule module, String comment){
		List<String> row = new ArrayList<>();
		row.add(module.getName());
		row.add(module.getResult());
		row.add(comment);
		return row;
	}
	
	public static JSONObject getParsedQCResults(final Map<String, FastQCDataModule> moduleMap, MessageService messageService) throws BabrahamDataParseException, JSONException{
		WaspChart chart = new WaspChart();
		chart.setTitle("fastqc.summary_title.label");
		DataSeries ds = new DataSeries();
		ds.addColLabel("fastqc.summary_col1.label");
		ds.addColLabel("fastqc.summary_col2.label");
		ds.addColLabel("fastqc.summary_col3.label");
		
		FastQCDataModule module = moduleMap.get(PlotType.PER_BASE_QUALITY);
		String comment = "";
		if (module.getResult().equals(FastQC.QC_ANALYSIS_RESULT_WARN))
			comment = "fastqc.perBaseQuality_warn.label";
		else if (module.getResult().equals(FastQC.QC_ANALYSIS_RESULT_FAIL))
			comment = "fastqc.perBaseQuality_fail.label";
		ds.addRow(getQCResultsDataRow(module, comment));
		
		module = moduleMap.get(PlotType.DUPLICATION_LEVELS);
		comment = "";
		if (module.getResult().equals(FastQC.QC_ANALYSIS_RESULT_WARN))
			comment = "fastqc.duplicationLevels_warn.label";
		else if (module.getResult().equals(FastQC.QC_ANALYSIS_RESULT_FAIL))
			comment = "fastqc.duplicationLevels_fail.label";
		ds.addRow(getQCResultsDataRow(module, comment));
		
		module = moduleMap.get(PlotType.PER_SEQUENCE_QUALITY);
		comment = "";
		if (module.getResult().equals(FastQC.QC_ANALYSIS_RESULT_WARN))
			comment = "fastqc.perSequenceQuality_warn.label";
		else if (module.getResult().equals(FastQC.QC_ANALYSIS_RESULT_FAIL))
			comment = "fastqc.perSequenceQuality_fail.label";
		ds.addRow(getQCResultsDataRow(module, comment));
		
		module = moduleMap.get(PlotType.PER_BASE_SEQUENCE_CONTENT);
		comment = "";
		if (module.getResult().equals(FastQC.QC_ANALYSIS_RESULT_WARN))
			comment = "fastqc.perSequenceContent_warn.label";
		else if (module.getResult().equals(FastQC.QC_ANALYSIS_RESULT_FAIL))
			comment = "fastqc.perSequenceContent_fail.label";
		ds.addRow(getQCResultsDataRow(module, comment));
		
		module = moduleMap.get(PlotType.PER_BASE_GC_CONTENT);
		comment = "";
		if (module.getResult().equals(FastQC.QC_ANALYSIS_RESULT_WARN))
			comment = "fastqc.perBaseGcContent_warn.label";
		else if (module.getResult().equals(FastQC.QC_ANALYSIS_RESULT_FAIL))
			comment = "fastqc.perBaseGcContent_fail.label";
		ds.addRow(getQCResultsDataRow(module, comment));
		
		module = moduleMap.get(PlotType.PER_SEQUENCE_GC_CONTENT);
		comment = "";
		if (module.getResult().equals(FastQC.QC_ANALYSIS_RESULT_WARN))
			comment = "fastqc.perSequenceGcContent_warn.label";
		else if (module.getResult().equals(FastQC.QC_ANALYSIS_RESULT_FAIL))
			comment = "fastqc.perSequenceGcContent_fail.label";
		ds.addRow(getQCResultsDataRow(module, comment));
		
		module = moduleMap.get(PlotType.PER_BASE_N_CONTENT);
		comment = "";
		if (module.getResult().equals(FastQC.QC_ANALYSIS_RESULT_WARN))
			comment = "fastqc.perBaseNContent_warn.label";
		else if (module.getResult().equals(FastQC.QC_ANALYSIS_RESULT_FAIL))
			comment = "fastqc.perBaseNContent_fail.label";
		ds.addRow(getQCResultsDataRow(module, comment));
		
		module = moduleMap.get(PlotType.OVERREPRESENTED_SEQUENCES);
		comment = "";
		if (module.getResult().equals(FastQC.QC_ANALYSIS_RESULT_WARN))
			comment = "fastqc.overrepresentedSeq_warn.label";
		else if (module.getResult().equals(FastQC.QC_ANALYSIS_RESULT_FAIL))
			comment = "fastqc.overrepresentedSeq_fail.label";
		ds.addRow(getQCResultsDataRow(module, comment));
		
		module = moduleMap.get(PlotType.KMER_PROFILES);
		comment = "";
		// no comments for this
		ds.addRow(getQCResultsDataRow(module, comment));
		
		module = moduleMap.get(PlotType.SEQUENCE_LENGTH_DISTRIBUTION);
		comment = "";
		if (module.getResult().equals(FastQC.QC_ANALYSIS_RESULT_WARN))
			comment = "fastqc.sequenceLengthDist_warn.label";
		else if (module.getResult().equals(FastQC.QC_ANALYSIS_RESULT_FAIL))
			comment = "fastqc.sequenceLengthDist_fail.label.";
		ds.addRow(getQCResultsDataRow(module, comment));
		chart.addDataSeries(ds);
		return chart.getAsJSON();
	}
	
	
	public static JSONObject getParsedBasicStatistics(final Map<String, FastQCDataModule> moduleMap, MessageService messageService) throws BabrahamDataParseException, JSONException{
		FastQCDataModule bs = moduleMap.get(PlotType.BASIC_STATISTICS);
		WaspChart chart = new WaspChart();
		chart.setTitle("fastqc.basicStats_plot_title.label");
		chart.addProperty(FastQC.QC_ANALYSIS_RESULT_KEY, bs.getResult());
		DataSeries ds = new DataSeries();
		List<String> colList = new ArrayList<>();
		colList.add("fastqc.basicStats_plot_col1.label");
		colList.add("fastqc.basicStats_plot_col2.label");
		colList.add("fastqc.basicStats_plot_col3.label");
		colList.add("fastqc.basicStats_plot_col4.label");
		colList.add("fastqc.basicStats_plot_col5.label");
		colList.add("fastqc.basicStats_plot_col6.label");
		ds.setDataFromString(bs.getDataPoints());
		ds.setColLabels(colList);
		chart.addDataSeries(ds);
		return chart.getAsJSON();
	}
	
	
	public static JSONObject getOverrepresentedSequences(final Map<String, FastQCDataModule> moduleMap, MessageService messageService) throws BabrahamDataParseException, JSONException{
		FastQCDataModule os = moduleMap.get(PlotType.OVERREPRESENTED_SEQUENCES);
		WaspChart chart = new WaspChart();
		chart.setTitle("fastqc.overrepresentedSeq_plot_title.label");
		chart.addProperty(FastQC.QC_ANALYSIS_RESULT_KEY, os.getResult());
		chart.setDescription("fastqc.overrepresentedSeq_plot_description.label");
		DataSeries ds = new DataSeries();
		ds.setColLabels(os.getAttributes());
		ds.setDataFromString(os.getDataPoints());
		chart.addDataSeries(ds);
		return chart.getAsJSON();
	}
	
	
	public static JSONObject getParsedPerBaseQualityData(final Map<String, FastQCDataModule> moduleMap, MessageService messageService) throws BabrahamDataParseException, JSONException{
		FastQCDataModule perBaseQual = moduleMap.get(PlotType.PER_BASE_QUALITY);
		WaspBoxPlot boxPlot = new WaspBoxPlot();
		boxPlot.setTitle("fastqc.perBaseQuality_plot_title.label");
		boxPlot.setxAxisLabel("fastqc.perBaseQuality_plot_xAxis.label");
		boxPlot.setyAxisLabel("fastqc.perBaseQuality_plot_yAxis.label");
		boxPlot.setDescription("fastqc.perBaseQuality_plot_description.label");
		boxPlot.addProperty(FastQC.QC_ANALYSIS_RESULT_KEY, perBaseQual.getResult());
		for (List<String> row : perBaseQual.getDataPoints()){
			try{
				boxPlot.addBoxAndWhiskers(
						row.get(0), // Base
						Double.valueOf(row.get(5)), // 10th Percentile
						Double.valueOf(row.get(3)), // Lower Quartile
						Double.valueOf(row.get(2)), // Median
						Double.valueOf(row.get(4)), // Upper Quartile
						Double.valueOf(row.get(6)) // 90th Percentile
					);
				boxPlot.addRunningMeanValue(row.get(0), Double.valueOf(row.get(1)));
			} catch (NumberFormatException e){
				throw new BabrahamDataParseException("Caught NumberFormatException attempting to convert string values to Double");
			} catch (NullPointerException e){
				throw new BabrahamDataParseException("Caught NullPointerException attempting to convert string values to Double");
			}
		}
		return boxPlot.getAsJSON();
	}
	
	
	public static JSONObject getSequenceDuplicationLevels(final Map<String, FastQCDataModule> moduleMap, MessageService messageService) throws BabrahamDataParseException, JSONException{
		FastQCDataModule dl = moduleMap.get(PlotType.DUPLICATION_LEVELS);
		WaspChart2D chart = new WaspChart2D();
		chart.setxAxisLabel("fastqc.duplicationLevels_xAxis.label");
		chart.setyAxisLabel("fastqc.duplicationLevels_yAxis.label");
		chart.addProperty(FastQC.QC_ANALYSIS_RESULT_KEY, dl.getResult());
		chart.setDescription("fastqc.duplicationLevels_description.label");
		int decimalPlaces = 1;
		BigDecimal bd = new BigDecimal(dl.getKeyValueData().get("Total Duplicate Percentage"));
		String roundedPercentage = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP).toPlainString();
		// for title render the property key here as cannot do the calculation otherwise. 
		chart.setTitle(messageService.getMessage("fastqc.duplicationLevels_title.label") + " >= " + roundedPercentage); 
		DataSeries ds = new DataSeries();
		ds.setName("fastqc.duplicationLevels_dsName.label");
		ds.setColLabels(dl.getAttributes());
		try{
			for (List<String> dataRow : dl.getDataPoints()){
				ds.addRowWithSingleColumn(dataRow.get(0), Double.valueOf(dataRow.get(1)));
			}
		} catch (NumberFormatException e){
			throw new BabrahamDataParseException("Caught NumberFormatException attempting to convert string values to Double");
		} catch (NullPointerException e){
			throw new BabrahamDataParseException("Caught NullPointerException attempting to convert string values to Double");
		}
		chart.addDataSeries(ds);
		return chart.getAsJSON();
	}
	
	
	public static JSONObject getPerSequenceQualityScores(final Map<String, FastQCDataModule> moduleMap, MessageService messageService) throws BabrahamDataParseException, JSONException{
		FastQCDataModule psq = moduleMap.get(PlotType.PER_SEQUENCE_QUALITY);
		WaspChart2D chart = new WaspChart2D();
		chart.setxAxisLabel("fastqc.perSequenceQuality_xAxis.label");
		chart.setyAxisLabel("fastqc.perSequenceQuality_yAxis.label");
		chart.addProperty(FastQC.QC_ANALYSIS_RESULT_KEY, psq.getResult());
		chart.setTitle("fastqc.perSequenceQuality_title.label");
		chart.setDescription("fastqc.perSequenceQuality_description.label");
		DataSeries ds = new DataSeries();
		ds.setName("fastqc.perSequenceQuality_dsName.label");
		ds.setColLabels(psq.getAttributes());
		try{
			for (List<String> dataRow : psq.getDataPoints()){
				ds.addRowWithSingleColumn(dataRow.get(0), Double.valueOf(dataRow.get(1)));
			}
		} catch (NumberFormatException e){
			throw new BabrahamDataParseException("Caught NumberFormatException attempting to convert string values to Double");
		} catch (NullPointerException e){
			throw new BabrahamDataParseException("Caught NullPointerException attempting to convert string values to Double");
		}
		chart.addDataSeries(ds);
		return chart.getAsJSON();
	}
	
	
	public static JSONObject getPerBaseSequenceContent(final Map<String, FastQCDataModule> moduleMap, MessageService messageService) throws BabrahamDataParseException, JSONException{
		FastQCDataModule pbsq = moduleMap.get(PlotType.PER_BASE_SEQUENCE_CONTENT);
		WaspChart2D chart = new WaspChart2D();
		chart.setxAxisLabel("fastqc.perSequenceContent_xAxis.label");
		chart.setyAxisLabel("fastqc.perSequenceContent_yAxis.label");
		chart.addProperty(FastQC.QC_ANALYSIS_RESULT_KEY, pbsq.getResult());
		chart.setTitle("fastqc.perSequenceContent_title.label");
		chart.setDescription("fastqc.perSequenceContent_description.label");
		DataSeries dsA = new DataSeries("fastqc.perSequenceContent_ds1Name.label");
		DataSeries dsC = new DataSeries("fastqc.perSequenceContent_ds2Name.label");
		DataSeries dsT = new DataSeries("fastqc.perSequenceContent_ds3Name.label");
		DataSeries dsG = new DataSeries("fastqc.perSequenceContent_ds4Name.label");
		dsA.setColLabels(pbsq.getAttributes());
		dsC.setColLabels(pbsq.getAttributes());
		dsT.setColLabels(pbsq.getAttributes());
		dsG.setColLabels(pbsq.getAttributes());
		try{
			for (List<String> dataRow : pbsq.getDataPoints()){
				dsG.addRowWithSingleColumn(dataRow.get(0), Double.valueOf(dataRow.get(1)));
				dsA.addRowWithSingleColumn(dataRow.get(0), Double.valueOf(dataRow.get(2)));
				dsT.addRowWithSingleColumn(dataRow.get(0), Double.valueOf(dataRow.get(3)));
				dsC.addRowWithSingleColumn(dataRow.get(0), Double.valueOf(dataRow.get(4)));
			}
		} catch (NumberFormatException e){
			throw new BabrahamDataParseException("Caught NumberFormatException attempting to convert string values to Double");
		} catch (NullPointerException e){
			throw new BabrahamDataParseException("Caught NullPointerException attempting to convert string values to Double");
		}
		chart.addDataSeries(dsA);
		chart.addDataSeries(dsC);
		chart.addDataSeries(dsT);
		chart.addDataSeries(dsG);
		return chart.getAsJSON();
	}
	
	
	public static JSONObject getPerBaseGcContent(final Map<String, FastQCDataModule> moduleMap, MessageService messageService) throws BabrahamDataParseException, JSONException{
		FastQCDataModule pbgc = moduleMap.get(PlotType.PER_BASE_GC_CONTENT);
		WaspChart2D chart = new WaspChart2D();
		chart.setxAxisLabel("fastqc.perBaseGcContent_xAxis.label");
		chart.setyAxisLabel("fastqc.perBaseGcContent_yAxis.label");
		chart.addProperty(FastQC.QC_ANALYSIS_RESULT_KEY, pbgc.getResult());
		chart.setTitle("fastqc.perBaseGcContent_title.label");
		chart.setDescription("fastqc.perBaseGcContent_description.label");
		DataSeries ds = new DataSeries();
		ds.setName("fastqc.perBaseGcContent_dsName.label");
		ds.setColLabels(pbgc.getAttributes());
		try{
			for (List<String> dataRow : pbgc.getDataPoints()){
				ds.addRowWithSingleColumn(dataRow.get(0), Double.valueOf(dataRow.get(1)));
			}
		} catch (NumberFormatException e){
			throw new BabrahamDataParseException("Caught NumberFormatException attempting to convert string values to Double");
		} catch (NullPointerException e){
			throw new BabrahamDataParseException("Caught NullPointerException attempting to convert string values to Double");
		}
		chart.addDataSeries(ds);
		return chart.getAsJSON();
	}
	
	
	public static JSONObject getPerSequenceGcContent(final Map<String, FastQCDataModule> moduleMap, MessageService messageService) throws BabrahamDataParseException, JSONException{
		FastQCDataModule psgc = moduleMap.get(PlotType.PER_SEQUENCE_GC_CONTENT);
		WaspChart2D chart = new WaspChart2D();
		chart.setxAxisLabel("fastqc.perSequenceGcContent_xAxis.label");
		chart.setyAxisLabel("fastqc.perSequenceGcContent_yAxis.label");
		chart.addProperty(FastQC.QC_ANALYSIS_RESULT_KEY, psgc.getResult());
		chart.setTitle("fastqc.perSequenceGcContent_title.label");
		chart.setDescription("fastqc.perSequenceGcContent_description.label");
		DataSeries dsActual = new DataSeries();
		DataSeries dsTheory = new DataSeries();
		dsActual.setName("fastqc.perSequenceGcContent_ds1Name.label");
		dsTheory.setName("fastqc.perSequenceGcContent_ds2Name.label");
		dsActual.setColLabels(psgc.getAttributes());
		dsTheory.setColLabels(psgc.getAttributes());
		List<Double> data = new ArrayList<Double>();
		List<Double> xValues = new ArrayList<Double>();
		try{
			for (List<String> dataRow : psgc.getDataPoints()){
				dsActual.addRowWithSingleColumn(dataRow.get(0), Double.valueOf(dataRow.get(1)));
				xValues.add(Double.valueOf(dataRow.get(0)));
				data.add(Double.valueOf(dataRow.get(1)));
			}
		} catch (NumberFormatException e){
			throw new BabrahamDataParseException("Caught NumberFormatException attempting to convert string values to Double");
		} catch (NullPointerException e){
			throw new BabrahamDataParseException("Caught NullPointerException attempting to convert string values to Double");
		}
		chart.addDataSeries(dsActual);
		
		// get modeled distribution
		List<Double> modelOfData = getTheoreticalDistribution(data);
		for (int i=0; i < xValues.size(); i++)
			dsTheory.addRowWithSingleColumn(xValues.get(i).toString(), modelOfData.get(i));
		chart.addDataSeries(dsTheory);
		
		return chart.getAsJSON();
	}
	
	
	public static JSONObject getPerBaseNContent(final Map<String, FastQCDataModule> moduleMap, MessageService messageService) throws BabrahamDataParseException, JSONException{
		FastQCDataModule pbnc = moduleMap.get(PlotType.PER_BASE_N_CONTENT);
		WaspChart2D chart = new WaspChart2D();
		chart.setxAxisLabel("fastqc.perBaseNContent_xAxis.label");
		chart.setyAxisLabel("fastqc.perBaseNContent_yAxis.label");
		chart.addProperty(FastQC.QC_ANALYSIS_RESULT_KEY, pbnc.getResult());
		chart.setDescription("fastqc.perBaseNContent_description.label");
		chart.setTitle("fastqc.perBaseNContent_title.label");
		DataSeries ds = new DataSeries();
		ds.setName("fastqc.perBaseNContent_dsName.label");
		ds.setColLabels(pbnc.getAttributes());
		try{
			for (List<String> dataRow : pbnc.getDataPoints()){
				ds.addRowWithSingleColumn(dataRow.get(0), Double.valueOf(dataRow.get(1)));
			}
		} catch (NumberFormatException e){
			throw new BabrahamDataParseException("Caught NumberFormatException attempting to convert string values to Double");
		} catch (NullPointerException e){
			throw new BabrahamDataParseException("Caught NullPointerException attempting to convert string values to Double");
		}
		chart.addDataSeries(ds);
		return chart.getAsJSON();
	}
	
	
	public static JSONObject getOverrepresentedKmers(final Map<String, FastQCDataModule> moduleMap, MessageService messageService) throws BabrahamDataParseException, JSONException{
		FastQCDataModule ok = moduleMap.get(PlotType.KMER_PROFILES);
		WaspChart chart = new WaspChart();
		chart.setTitle("fastqc.kmer_title.label");
		chart.addProperty(FastQC.QC_ANALYSIS_RESULT_KEY, ok.getResult());
		chart.setDescription("fastqc.kmer_description.label");
		DataSeries ds = new DataSeries();
		List<String> colList = new ArrayList<>();
		colList.add("fastqc.kmer_plot_col1.label");
		colList.add("fastqc.kmer_plot_col2.label");
		colList.add("fastqc.kmer_plot_col3.label");
		colList.add("fastqc.kmer_plot_col4.label");
		colList.add("fastqc.kmer_plot_col5.label");
		ds.setColLabels(colList);
		ds.setDataFromString(ok.getDataPoints());
		chart.addDataSeries(ds);
		return chart.getAsJSON();
	}
	
	
	public static JSONObject getSequenceLengthDist(final Map<String, FastQCDataModule> moduleMap, MessageService messageService) throws BabrahamDataParseException, JSONException{
		FastQCDataModule pbgc = moduleMap.get(PlotType.SEQUENCE_LENGTH_DISTRIBUTION);
		WaspChart2D chart = new WaspChart2D();
		chart.setxAxisLabel("fastqc.sequenceLengthDist_xAxis.label");
		chart.setyAxisLabel("fastqc.sequenceLengthDist_yAxis.label");
		chart.addProperty(FastQC.QC_ANALYSIS_RESULT_KEY, pbgc.getResult());
		chart.setTitle("fastqc.sequenceLengthDist_title.label");
		chart.setDescription("fastqc.sequenceLengthDist_description.label");
		DataSeries ds = new DataSeries();
		ds.setName("fastqc.sequenceLengthDist_dsName.label");
		ds.setColLabels(pbgc.getAttributes());
		try{
			Map<String, Double> lengthCounts = new LinkedHashMap<String, Double>();
			if (pbgc.getDataPoints().size() == 1){
				int lastIndex = pbgc.getDataPoints().size() - 1;
				Integer maxLength =  Integer.valueOf( pbgc.getDataPoints().get(lastIndex).get(0) ); // last row in list
				for (Integer i=1; i <= maxLength; i++)
					lengthCounts.put(i.toString(), 0.0d);
			}
			for (List<String> dataRow : pbgc.getDataPoints())
				lengthCounts.put(dataRow.get(0), Double.valueOf(dataRow.get(1)));
			for (String length : lengthCounts.keySet()){
				ds.addRowWithSingleColumn(length, lengthCounts.get(length));
			}
		} catch (NumberFormatException e){
			throw new BabrahamDataParseException("Caught NumberFormatException attempting to get numeric value of string values");
		} catch (NullPointerException e){
			throw new BabrahamDataParseException("Caught NullPointerException attempting to get numeric value of string values");
		}
		chart.addDataSeries(ds);
		return chart.getAsJSON();
	}
	
	public static JSONObject getParsedFastqScreenStatistics(final BabrahamDataModule module, MessageService messageService) throws JSONException, BabrahamDataParseException {
		WaspChart2D chart = new WaspChart2D();
		chart.setTitle("fastqscreen.title.label");
		chart.setxAxisLabel("fastqscreen.xAxis.label");
		chart.setyAxisLabel("fastqscreen.yAxis.label");
		chart.setDescription("fastqscreen.description.label");
		DataSeries ds1 = new DataSeries("fastqscreen.ds1Name.label");
		DataSeries ds2 = new DataSeries("fastqscreen.ds2Name.label");
		DataSeries ds3 = new DataSeries("fastqscreen.ds3Name.label");
		DataSeries ds4 = new DataSeries("fastqscreen.ds4Name.label");
		try{
			for (List<String> row : module.getDataPoints()){
				ds1.addRowWithSingleColumn(row.get(0), Double.parseDouble(row.get(2)));
				ds2.addRowWithSingleColumn(row.get(0), Double.parseDouble(row.get(3)));
				ds3.addRowWithSingleColumn(row.get(0), Double.parseDouble(row.get(4)));
				ds4.addRowWithSingleColumn(row.get(0), Double.parseDouble(row.get(5)));
			}
		} catch (NumberFormatException e){
			throw new BabrahamDataParseException("Caught NumberFormatException attempting to get numeric value of string values");
		} catch (NullPointerException e){
			throw new BabrahamDataParseException("Caught NullPointerException attempting to get numeric value of string values");
		}
		
		chart.addDataSeries(ds1);
		chart.addDataSeries(ds2);
		chart.addDataSeries(ds3);
		chart.addDataSeries(ds4);
		return chart.getAsJSON();
	}
	
	public static JSONObject getTrimGaloreChart(List<String> trimStats, int numberOfClusters) throws IOException {
		
		final class TrimStruct {
			public Integer pos;
			public Integer obs;
			public Double expect;
			public Integer err;
			
			TrimStruct(String[] init) {
				pos = Integer.parseInt(init[0]);
				obs = Integer.parseInt(init[1]);
				expect = Double.valueOf(init[2]);
				err = Integer.parseInt(init[3]);
			}
		}
		
		WaspChart2D chart = new WaspChart2D();
		chart.setTitle("trimgalore.title.label");
		chart.setxAxisLabel("trimgalore.xAxis.label");
		chart.setyAxisLabel("trimgalore.yAxis.label");
		chart.setDescription("trimgalore.description.label");
		List<Map<Integer,TrimStruct>> tsl = new ArrayList<Map<Integer,TrimStruct>>();
		
		for (String sdat : trimStats) { // for each read segment
			BufferedReader br = new BufferedReader(new StringReader(sdat));
			
			Map<Integer,TrimStruct> obs = new HashMap<Integer,TrimStruct>();
			
			String line;
			while ((line=br.readLine()) != null) {
				String[] e = line.split("\t");
				obs.put(Integer.parseInt(e[0]), new TrimStruct(e));
			}
			tsl.add(obs);
		}
		int max = 0;
		// find the longest length read
		for(Map<Integer,TrimStruct> m : tsl) {
			int nm = Collections.max(m.keySet());
			if (nm > max)
				max = nm;
		}
		//loop down to 1
		int i = 1;
		for(Map<Integer,TrimStruct> m : tsl) {

			DataSeries dso = new DataSeries("trimgalore.ds" + i + "Name.label");
			DataSeries dse = new DataSeries("trimgalore.eds" + i + "Name.label");
			double cumObsPct = 0;
			double cumExpectPct = 0;
			for (int n = max; n>0; n--) {
				TrimStruct ts = m.get(n);
				if (ts == null)
					continue;
				
				cumObsPct += (double) ts.obs / numberOfClusters;
				cumExpectPct += (double) ts.expect / numberOfClusters;
				
				dso.addRowWithSingleColumn(ts.pos.toString(), cumObsPct);
				dse.addRowWithSingleColumn(ts.pos.toString(), cumExpectPct);
			}
			chart.addDataSeries(dso);
			if (i==1)
				chart.addDataSeries(dse);
			i++;
		}
		return chart.getAsJSON();
	}
	
	/*
	 * The following method for getting a theoretical modal distribution is modified from part of the FastQC source code (Version 0.10.1)
	 * (derived from uk.ac.babraham.FastQC.Modules.PerSequenceGCContent and uk.ac.babraham.FastQC.Statistics.NormalDistribution). 
	 * It should be functionally identical to the original.
	 * 
	 * Modified from original by A.S.McLellan 07/05/2013.
	 * 
	 * The original source code is covered by the following copyright and license information:
	 * 
	 * 
	 * Copyright 2010-12 Simon Andrews
	 *
	 *    FastQC is free software; you can redistribute it and/or modify
	 *    it under the terms of the GNU General Public License as published by
	 *    the Free Software Foundation; either version 3 of the License, or
	 *    (at your option) any later version.
	 *
	 *    FastQC is distributed in the hope that it will be useful,
	 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
	 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	 *    GNU General Public License for more details.
	 *
	 *    You should have received a copy of the GNU General Public License
	 *    along with FastQC; if not, write to the Free Software
	 *    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
	 */
	private static List<Double> getTheoreticalDistribution(List<Double> gcDistribution){
		List<Double> theoreticalDistribution = new ArrayList<Double>();
		int xCategories[] = new int[gcDistribution.size()];
		double totalCount = 0;
		
		
		// We use the mode to calculate the theoretical distribution
		// so that we cope better with skewed distributions.
		int firstMode = 0;
		double modeCount = 0;
		
		for (int i=0;i<gcDistribution.size();i++) {
			xCategories[i] = i;
			totalCount += gcDistribution.get(i);
			
			if (gcDistribution.get(i) > modeCount) {
				modeCount = gcDistribution.get(i);
				firstMode = i;
			}
		}
				
		// The mode might not be a very good measure of the centre
		// of the distribution either due to duplicated vales or
		// several very similar values next to each other.  We therefore
		// average over adjacent points which stay above 95% of the modal
		// value

		double mode = 0;
		int modeDuplicates = 0;
		
		boolean fellOffTop = true;

		for (int i=firstMode;i<gcDistribution.size();i++) {
			if (gcDistribution.get(i) > gcDistribution.get(firstMode) - (gcDistribution.get(firstMode)/10)) {
				mode += i;
				modeDuplicates++;
			}
			else {
				fellOffTop = false;
				break;
			}
		}

		boolean fellOffBottom = true;
		
		for (int i=firstMode-1;i>=0;i--) {
			if (gcDistribution.get(i) > gcDistribution.get(firstMode) - (gcDistribution.get(firstMode)/10)) {
				mode += i;
				modeDuplicates++;
			}
			else {
				fellOffBottom = false;
				break;
			}
		}

		if (fellOffBottom || fellOffTop) {
			// If the distribution is so skewed that 95% of the mode
			// is off the 0-100% scale then we keep the mode as the 
			// centre of the model
			mode = firstMode;
		}
		else {
			mode /= modeDuplicates;
		}
		
		
		// We can now work out a theoretical distribution
		double stdev = 0;
		
		for (int i=0;i<gcDistribution.size();i++) {
			stdev += Math.pow((i-mode),2) * gcDistribution.get(i);
		}
		
		stdev /= totalCount-1;
		
		stdev = Math.sqrt(stdev);
		
		@SuppressWarnings("unused")
		double deviationPercent = 0;
		
		for (int i=0; i < gcDistribution.size(); i++) {
			double lhs = 1d/(Math.sqrt(2*Math.PI*stdev*stdev));
			double rhs = Math.pow(Math.E, 0 - (Math.pow((i+1)-mode,2)/(2*stdev*stdev)));
			double probability = lhs*rhs; // z-score for value i
			theoreticalDistribution.add(probability*totalCount);
			deviationPercent += Math.abs(theoreticalDistribution.get(i)-gcDistribution.get(i));
		}
		
		deviationPercent /= totalCount;
		deviationPercent *= 100;
		
		//logger.trace("Percentage deviation from normality is "+deviationPercent);
		
		return theoreticalDistribution;
	}
	
}
