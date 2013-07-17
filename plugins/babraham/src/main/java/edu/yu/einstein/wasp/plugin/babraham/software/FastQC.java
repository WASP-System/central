/**
 * 
 */
package edu.yu.einstein.wasp.plugin.babraham.software;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.charts.DataSeries;
import edu.yu.einstein.wasp.charts.WaspBoxPlot;
import edu.yu.einstein.wasp.charts.WaspChart;
import edu.yu.einstein.wasp.charts.WaspChart2D;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.filetype.FastqComparator;
import edu.yu.einstein.wasp.filetype.service.FastqService;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.mps.illumina.IlluminaSequenceRunProcessor;
import edu.yu.einstein.wasp.plugin.babraham.exception.BabrahamDataParseException;
import edu.yu.einstein.wasp.plugin.babraham.service.BabrahamService;
import edu.yu.einstein.wasp.software.SoftwarePackage;



/**
 * @author calder / asmclellan
 *
 */
public class FastQC extends SoftwarePackage{

	
	@Autowired
	private FastqService fastqService;
	
	@Autowired
	private IlluminaSequenceRunProcessor casava;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7075104587205964069L;
	
	public static final String QC_ANALYSIS_RESULT_KEY = "result";
	
	public static final String QC_ANALYSIS_RESULT_PASS = "pass";
	
	public static final String QC_ANALYSIS_RESULT_WARN = "warn";
	
	public static final String QC_ANALYSIS_RESULT_FAIL = "fail";
	
	public static class PlotType{
		// meta keys for charts produced
		public static final String QC_RESULT_SUMMARY = "result_summary";
		public static final String BASIC_STATISTICS = "fastqc_basic_statistics";
		public static final String DUPLICATION_LEVELS = "fastqc_duplication_levels";
		public static final String KMER_PROFILES = "fastqc_kmer_profiles";
		public static final String PER_BASE_GC_CONTENT = "fastqc_per_base_gc_content";
		public static final String PER_BASE_N_CONTENT = "fastqc_per_base_n_content";
		public static final String PER_BASE_QUALITY = "fastqc_per_base_quality";
		public static final String PER_BASE_SEQUENCE_CONTENT = "fastqc_per_base_sequence_content";
		public static final String PER_SEQUENCE_GC_CONTENT = "fastqc_per_sequence_gc_content";
		public static final String PER_SEQUENCE_QUALITY = "fastqc_per_sequence_quality";
		public static final String SEQUENCE_LENGTH_DISTRIBUTION = "fastqc_sequence_length_distribution"; 
		public static final String OVERREPRESENTED_SEQUENCES = "fastqc_overrepresented_sequences";
	}
	
	@Autowired
	BabrahamService babrahamService;
	
	/**
	 * 
	 */
	public FastQC() {
		// TODO Auto-generated constructor stub
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String getSoftwareVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setSoftwareVersion(String softwareVersion) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Takes a FileGroup and returns a configured WorkUnit to run FastQC on the file group.
	 * The output will be placed in folders that are numbered by their read segment number.
	 * e.g. a paired end run will generate two output folders, 1 and 2.
	 * 
	 * @param fileGroup
	 * @return
	 */
	public WorkUnit getFastQC(FileGroup fileGroup) {
		
		WorkUnit w = new WorkUnit();
		
		// require fastqc
		List<SoftwarePackage> software = new ArrayList<SoftwarePackage>();
		software.add(this);
		w.setSoftwareDependencies(software);
		
		// require 1GB memory
		w.setMemoryRequirements(1);
		
		// require a single thread, execution mode PROCESS
		// indicates this is a vanilla execution.
		w.setProcessMode(ProcessMode.SINGLE);
		w.setMode(ExecutionMode.PROCESS);
		
		// set working directory to scratch
		w.setWorkingDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		
		// we aren't actually going to retain any files, so we will set the output
		// directory to the scratch directory.  Also set "secure results" to
		// false to indicate that we don't care about the output.
		w.setResultsDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		w.setSecureResults(false);
		
		// add the files to the work unit
		// files will be represented as bash variables in the work unit and
		// are sorted using the FastqComparator.  For single samples (what we have here)
		// this ensures that they will be in groups of read segments.
		// e.g.
		// s1.R1.001
		// s1.R2.001
		// s1.R1.002
		// s1.R2.002
		List<FileHandle> files = new ArrayList<FileHandle>(fileGroup.getFileHandles());
		Collections.sort(files, new FastqComparator(fastqService));
		w.setRequiredFiles(files);
		
		// set the command
		w.setCommand(getCommand(fileGroup));
		
		return w;
	}
	
	/**
	 * Set the fastqc command.  Choose casava mode if the data was generated by the Illumina plugin.
	 * 
	 * @param fileGroup
	 * @param workUnit
	 * @return
	 */
	private String getCommand(FileGroup fileGroup) {
		
		String command = "";
		int segments = fastqService.getNumberOfReadSegments(fileGroup);
		int files = fileGroup.getFileHandles().size();
		
		// fileList is an array of file name refs
		// 1 for each read segment.
		String[] fileList = new String[segments];
		
		for (int i = 0; i < segments; i++) {
			for (int j = 0; j < (files/segments); j++) {
				int index = ((j-1) * segments) + (i-1);
				if(fileList[i]==null)
					fileList[i] = " ${" + WorkUnit.INPUT_FILE + "[" + index + "]}";
				else
					fileList[i] += " ${" + WorkUnit.INPUT_FILE + "[" + index + "]}";
			}
		}
		
		String opts = "--noextract --nogroup --quiet";
		
		for (int i = 0; i < segments; i++) {
			int dir = i+1;
			command += "mkdir " + dir + "\n";
			// if casava, use casava mode
			if (fileGroup.getSoftwareGeneratedBy().equals(casava)) {
				command += "fastqc --casava " + opts + " --outdir " + i + " " + fileList[i] + "\n";
			} else {
				// otherwise treat like fastq
				String name = i + ".fq";
				command += "zcat " + fileList[i] + " > " + name + " && fastqc " + opts + " --outdir " + i + " " + name + "\n";
			}
		}

		return command;
	}
	
	/**
	 * This method takes a grid result of a successfully run FastQC job, gets the working directory
	 * and uses it to parse the <em>fastqc_data.txt</em> file into a Map which contains
	 * static Strings defining the output keys (see above) and JSONObjects representing the
	 * data.  
	 * @param result
	 * @return
	 * @throws GridException
	 * @throws BabrahamDataParseException
	 * @throws JSONException 
	 */
	public Map<String,JSONObject> parseOutput(GridResult result) throws GridException, BabrahamDataParseException, JSONException {
		Map<String,JSONObject> output = new LinkedHashMap<String, JSONObject>();
		Map<String, FastQCDataModule> mMap = babrahamService.parseFastQCOutput(result);
		output.put(PlotType.QC_RESULT_SUMMARY, getParsedQCResults(mMap));
		output.put(PlotType.BASIC_STATISTICS, getParsedBasicStatistics(mMap));
		output.put(PlotType.PER_BASE_QUALITY, getParsedPerBaseQualityData(mMap));
		output.put(PlotType.PER_SEQUENCE_QUALITY, getPerSequenceQualityScores(mMap));
		output.put(PlotType.PER_BASE_SEQUENCE_CONTENT, getPerBaseSequenceContent(mMap));
		output.put(PlotType.PER_BASE_GC_CONTENT, getPerBaseGcContent(mMap));
		output.put(PlotType.PER_SEQUENCE_GC_CONTENT, getPerSequenceGcContent(mMap));
		output.put(PlotType.PER_BASE_N_CONTENT, getPerBaseNContent(mMap));
		output.put(PlotType.SEQUENCE_LENGTH_DISTRIBUTION, getSequenceLengthDist(mMap));
		output.put(PlotType.DUPLICATION_LEVELS, getSequenceDuplicationLevels(mMap));
		output.put(PlotType.OVERREPRESENTED_SEQUENCES, getOverrepresentedSequences(mMap));
		output.put(PlotType.KMER_PROFILES, getOverrepresentedKmers(mMap));
		return output;
	}
	
	private List<String> getQCResultsDataRow(final FastQCDataModule module, String comment){
		List<String> row = new ArrayList<>();
		row.add(module.getName());
		row.add(module.getResult());
		row.add(comment);
		return row;
	}
	
	private String getCredits(){
		return "<div class='chart_credit'>Data generated by and legend text derived from <a href='http://www.bioinformatics.babraham.ac.uk/projects/fastqc/'>FastQC</a></div>";
	}
	
	private JSONObject getParsedQCResults(final Map<String, FastQCDataModule> moduleMap) throws BabrahamDataParseException, JSONException{
		WaspChart chart = new WaspChart();
		chart.setTitle("FastQC Results Summary");
		DataSeries ds = new DataSeries();
		ds.addColLabel("FastQC Module");
		ds.addColLabel("Result");
		ds.addColLabel("Reason (if " + QC_ANALYSIS_RESULT_WARN + "/" + QC_ANALYSIS_RESULT_FAIL + ")");
		FastQCDataModule module = moduleMap.get(PlotType.PER_BASE_QUALITY);
		String comment = "";
		if (module.getResult().equals(QC_ANALYSIS_RESULT_WARN))
			comment = "The lower quartile for at least one base is less than 10, or if a median value is less than 25.";
		else if (module.getResult().equals(QC_ANALYSIS_RESULT_FAIL))
			comment = "The lower quartile for at least one base is less than 5 or if a median value is less than 20.";
		ds.addRow(getQCResultsDataRow(module, comment));
		
		module = moduleMap.get(PlotType.DUPLICATION_LEVELS);
		comment = "";
		if (module.getResult().equals(QC_ANALYSIS_RESULT_WARN))
			comment = "Non-unique sequences make up more than 20% of the total.";
		else if (module.getResult().equals(QC_ANALYSIS_RESULT_FAIL))
			comment = "Non-unique sequences make up more than 50% of the total.";
		ds.addRow(getQCResultsDataRow(module, comment));
		
		module = moduleMap.get(PlotType.PER_SEQUENCE_QUALITY);
		comment = "";
		if (module.getResult().equals(QC_ANALYSIS_RESULT_WARN))
			comment = "The most frequently observed mean quality is below 27 - this equates to a 0.2% error rate.";
		else if (module.getResult().equals(QC_ANALYSIS_RESULT_FAIL))
			comment = "The most frequently observed mean quality is below 20 - this equates to a 1% error rate.";
		ds.addRow(getQCResultsDataRow(module, comment));
		
		module = moduleMap.get(PlotType.PER_BASE_SEQUENCE_CONTENT);
		comment = "";
		if (module.getResult().equals(QC_ANALYSIS_RESULT_WARN))
			comment = "The difference between A and T, or G and C is greater than 10% in at least one position.";
		else if (module.getResult().equals(QC_ANALYSIS_RESULT_FAIL))
			comment = "The difference between A and T, or G and C is greater than 20% in at least one position.";
		ds.addRow(getQCResultsDataRow(module, comment));
		
		module = moduleMap.get(PlotType.PER_BASE_GC_CONTENT);
		comment = "";
		if (module.getResult().equals(QC_ANALYSIS_RESULT_WARN))
			comment = "The GC content of any base strays more than 5% from the mean GC content.";
		else if (module.getResult().equals(QC_ANALYSIS_RESULT_FAIL))
			comment = "The GC content of any base strays more than 10% from the mean GC content.";
		ds.addRow(getQCResultsDataRow(module, comment));
		
		module = moduleMap.get(PlotType.PER_SEQUENCE_GC_CONTENT);
		comment = "";
		if (module.getResult().equals(QC_ANALYSIS_RESULT_WARN))
			comment = "The sum of the deviations from the normal distribution represents more than 15% of the reads.";
		else if (module.getResult().equals(QC_ANALYSIS_RESULT_FAIL))
			comment = "The sum of the deviations from the normal distribution represents more than 30% of the reads.";
		ds.addRow(getQCResultsDataRow(module, comment));
		
		module = moduleMap.get(PlotType.PER_BASE_N_CONTENT);
		comment = "";
		if (module.getResult().equals(QC_ANALYSIS_RESULT_WARN))
			comment = "At least one position shows an N content of >5%.";
		else if (module.getResult().equals(QC_ANALYSIS_RESULT_FAIL))
			comment = "At least one position shows an N content of >20%.";
		ds.addRow(getQCResultsDataRow(module, comment));
		
		module = moduleMap.get(PlotType.OVERREPRESENTED_SEQUENCES);
		comment = "";
		if (module.getResult().equals(QC_ANALYSIS_RESULT_WARN))
			comment = "At least one sequence is found to represent more than 0.1% of the total.";
		else if (module.getResult().equals(QC_ANALYSIS_RESULT_FAIL))
			comment = "At least one sequence is found to represent more than 1% of the total.";
		ds.addRow(getQCResultsDataRow(module, comment));
		
		module = moduleMap.get(PlotType.KMER_PROFILES);
		comment = "";
		// no comments for this
		ds.addRow(getQCResultsDataRow(module, comment));
		
		module = moduleMap.get(PlotType.SEQUENCE_LENGTH_DISTRIBUTION);
		comment = "";
		if (module.getResult().equals(QC_ANALYSIS_RESULT_WARN))
			comment = "All sequences are not the same length.";
		else if (module.getResult().equals(QC_ANALYSIS_RESULT_FAIL))
			comment = "At least one sequence has zero length.";
		ds.addRow(getQCResultsDataRow(module, comment));
		chart.addDataSeries(ds);
		return chart.getAsJSON();
	}
	
	
	private JSONObject getParsedBasicStatistics(final Map<String, FastQCDataModule> moduleMap) throws BabrahamDataParseException, JSONException{
		FastQCDataModule bs = moduleMap.get(PlotType.BASIC_STATISTICS);
		WaspChart chart = new WaspChart();
		chart.setTitle(bs.getName());
		chart.addProperty(QC_ANALYSIS_RESULT_KEY, bs.getResult());
		chart.setDescription(getCredits());
		DataSeries ds = new DataSeries();
		ds.setColLabels(bs.getAttributes());
		ds.setDataFromString(bs.getDataPoints());
		chart.addDataSeries(ds);
		return chart.getAsJSON();
	}
	
	private JSONObject getOverrepresentedSequences(final Map<String, FastQCDataModule> moduleMap) throws BabrahamDataParseException, JSONException{
		FastQCDataModule os = moduleMap.get(PlotType.OVERREPRESENTED_SEQUENCES);
		WaspChart chart = new WaspChart();
		chart.setTitle(os.getName());
		chart.addProperty(QC_ANALYSIS_RESULT_KEY, os.getResult());
		chart.setDescription("\n" + 
				"A normal high-throughput library will contain a diverse set of sequences, with no individual sequence making up a tiny fraction of the whole. " + 
				"Finding that a single sequence is very overrepresented in the set either means that it is highly biologically significant, or indicates " + 
				"that the library is contaminated, or not as diverse as you expected.\n" +
				"This module lists all of the sequence which make up more than 0.1% of the total. To conserve memory only sequences which appear in the first " + 
				"200,000 sequences are tracked to the end of the file. It is therefore possible that a sequence which is overrepresented but doesn't appear " + 
				"at the start of the file for some reason could be missed by this module.\n" +
				"For each overrepresented sequence the program will look for matches in a database of common contaminants and will report the best hit it " + 
				"finds. Hits must be at least 20bp in length and have no more than 1 mismatch. Finding a hit doesn't necessarily mean that this is the source " + 
				"of the contamination, but may point you in the right direction. It's also worth pointing out that many adapter sequences are very similar to " + 
				"each other so you may get a hit reported which isn't technically correct, but which has very similar sequence to the actual match.\n" +
				"Because the duplication detection requires an exact sequence match over the whole length of the sequence any reads over 75bp in length are " + 
				"truncated to 50bp for the purposes of this analysis. Even so, longer reads are more likely to contain sequencing errors which will " + 
				"artificially increase the observed diversity and will tend to underrepresent highly duplicated sequences." + getCredits()
			);
		DataSeries ds = new DataSeries();
		ds.setColLabels(os.getAttributes());
		ds.setDataFromString(os.getDataPoints());
		chart.addDataSeries(ds);
		return chart.getAsJSON();
	}
	
	private JSONObject getParsedPerBaseQualityData(final Map<String, FastQCDataModule> moduleMap) throws BabrahamDataParseException, JSONException{
		FastQCDataModule perBaseQual = moduleMap.get(PlotType.PER_BASE_QUALITY);
		WaspBoxPlot boxPlot = new WaspBoxPlot();
		boxPlot.setTitle("Quality scores across all bases");
		boxPlot.setxAxisLabel("position in read (bp)");
		boxPlot.setyAxisLabel("Quality Score");
		boxPlot.setDescription("This view shows an overview of the range of quality values across all bases at each position in the FastQ file.\n" + getCredits());
		boxPlot.addProperty(QC_ANALYSIS_RESULT_KEY, perBaseQual.getResult());
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
	
	private JSONObject getSequenceDuplicationLevels(final Map<String, FastQCDataModule> moduleMap) throws BabrahamDataParseException, JSONException{
		FastQCDataModule dl = moduleMap.get(PlotType.DUPLICATION_LEVELS);
		WaspChart2D chart = new WaspChart2D();
		chart.setxAxisLabel("Sequence Duplication Level");
		chart.setyAxisLabel("% Duplicate Relative to Unique");
		chart.addProperty(QC_ANALYSIS_RESULT_KEY, dl.getResult());
		chart.setDescription("In a diverse library most sequences will occur only once in the final set. A low level of duplication may indicate a very high " + 
				"level of coverage of the target sequence, but a high level of duplication is more likely to indicate some kind of enrichment bias (eg PCR " + 
				"over amplification).\n" + 
				"This module counts the degree of duplication for every sequence in the set and creates a plot showing the relative number of sequences with " + 
				"different degrees of duplication.\n" + 
				"Because the duplication detection requires an exact sequence match over the whole length of the sequence any reads over 75bp in length " + 
				"are truncated to 50bp for the purposes of this analysis. Even so, longer reads are more likely to contain sequencing errors which will " + 
				"artificially increase the observed diversity and will tend to underrepresent highly duplicated sequences." + getCredits()
			);
		int decimalPlaces = 1;
		BigDecimal bd = new BigDecimal(dl.getKeyValueData().get("Total Duplicate Percentage"));
		String roundedPercentage = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP).toPlainString();
		chart.setTitle("Sequence Duplication Level >= " + roundedPercentage);
		DataSeries ds = new DataSeries();
		ds.setName("% Duplication");
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
	
	private JSONObject getPerSequenceQualityScores(final Map<String, FastQCDataModule> moduleMap) throws BabrahamDataParseException, JSONException{
		FastQCDataModule psq = moduleMap.get(PlotType.PER_SEQUENCE_QUALITY);
		WaspChart2D chart = new WaspChart2D();
		chart.setxAxisLabel("Quality Score");
		chart.setyAxisLabel("Sequence Count");
		chart.addProperty(QC_ANALYSIS_RESULT_KEY, psq.getResult());
		chart.setTitle("Quality Score Distribution Over all Sequences");
		chart.setDescription("The per sequence quality score report allows you to see if a subset of your sequences have universally low quality values. " + 
				"It is often the case that a subset of sequences will have universally poor quality, often because they are poorly imaged (on the edge of " + 
				"the field of view etc), however these should represent only a small percentage of the total sequences.\n" + 
				"If a significant proportion of the sequences in a run have overall low quality then this could indicate some kind of systematic problem - " + 
				"possibly with just part of the run (for example one end of a flowcell)." + getCredits()
			);
		DataSeries ds = new DataSeries();
		ds.setName("average quality per read");
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
	
	private JSONObject getPerBaseSequenceContent(final Map<String, FastQCDataModule> moduleMap) throws BabrahamDataParseException, JSONException{
		FastQCDataModule pbsq = moduleMap.get(PlotType.PER_BASE_SEQUENCE_CONTENT);
		WaspChart2D chart = new WaspChart2D();
		chart.setxAxisLabel("position in read (bp)");
		chart.setyAxisLabel("proportion of base (%)");
		chart.addProperty(QC_ANALYSIS_RESULT_KEY, pbsq.getResult());
		chart.setTitle("Quality Score Distribution Over all Sequences");
		chart.setDescription("Per Base Sequence Content plots out the proportion of each base position in a file for which each of the four normal DNA " + 
				"bases has been called.\n" + 
				"In a random library you would expect that there would be little to no difference between the different bases of a sequence run, so the " + 
				"lines in this plot should run parallel with each other. The relative amount of each base should reflect the overall amount of these bases " + 
				"in your genome, but in any case they should not be hugely imbalanced from each other.\n" + 
				"If you see strong biases which change in different bases then this usually indicates an overrepresented sequence which is contaminating " + 
				"your library. A bias which is consistent across all bases either indicates that the original library was sequence biased, or that there was " + 
				"a systematic problem during the sequencing of the library." + getCredits()
			);
		DataSeries dsA = new DataSeries("% A");
		DataSeries dsC = new DataSeries("% C");
		DataSeries dsT = new DataSeries("% T");
		DataSeries dsG = new DataSeries("% G");
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
	
	private JSONObject getPerBaseGcContent(final Map<String, FastQCDataModule> moduleMap) throws BabrahamDataParseException, JSONException{
		FastQCDataModule pbgc = moduleMap.get(PlotType.PER_BASE_GC_CONTENT);
		WaspChart2D chart = new WaspChart2D();
		chart.setxAxisLabel("position in read (bp)");
		chart.setyAxisLabel("% GC");
		chart.addProperty(QC_ANALYSIS_RESULT_KEY, pbgc.getResult());
		chart.setTitle("Per Base GC Content");
		chart.setDescription("Per Base GC Content plots out the GC content of each base position in a file.\n" + 
				"In a random library you would expect that there would be little to no difference between the different bases of a sequence run, so the " + 
				"line in this plot should run horizontally across the graph. The overall GC content should reflect the GC content of the underlying genome.\n" + 
				"If you see a GC bias which changes in different bases then this could indicate an overrepresented sequence which is contaminating your " + 
				"library. A bias which is consistent across all bases either indicates that the original library was sequence biased, or that there was a " + 
				"systematic problem during the sequencing of the library." + getCredits() );
		DataSeries ds = new DataSeries();
		ds.setName("% GC");
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
	
	private JSONObject getPerSequenceGcContent(final Map<String, FastQCDataModule> moduleMap) throws BabrahamDataParseException, JSONException{
		FastQCDataModule psgc = moduleMap.get(PlotType.PER_SEQUENCE_GC_CONTENT);
		WaspChart2D chart = new WaspChart2D();
		chart.setxAxisLabel("Mean GC content (%)");
		chart.setyAxisLabel("count per read");
		chart.addProperty(QC_ANALYSIS_RESULT_KEY, psgc.getResult());
		chart.setTitle("Per Sequence GC Content");
		chart.setDescription("This module measures the GC content across the whole length of each sequence in a file and compares it to a modelled normal " + 
				"distribution of GC content.\n" + 
				"In a normal random library you would expect to see a roughly normal distribution of GC content where the central peak corresponds to the " + 
				"overall GC content of the underlying genome. Since we don't know the the GC content of the genome the modal GC content is calculated from " + 
				"the observed data and used to build a reference distribution.\n" + 
				"An unusually shaped distribution could indicate a contaminated library or some other kinds of biased subset. A normal distribution which " + 
				"is shifted indicates some systematic bias which is independent of base position. If there is a systematic bias which creates a shifted " + 
				"normal distribution then this won't be flagged as an error by the module since it doesn't know what your genome's GC content should be." + 
				getCredits() );
		DataSeries dsActual = new DataSeries();
		DataSeries dsTheory = new DataSeries();
		dsActual.setName("GC count per read");
		dsTheory.setName("Theoretical Distribution");
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
	
	private JSONObject getPerBaseNContent(final Map<String, FastQCDataModule> moduleMap) throws BabrahamDataParseException, JSONException{
		FastQCDataModule pbnc = moduleMap.get(PlotType.PER_BASE_N_CONTENT);
		WaspChart2D chart = new WaspChart2D();
		chart.setxAxisLabel("position in read (bp)");
		chart.setyAxisLabel("% N");
		chart.addProperty(QC_ANALYSIS_RESULT_KEY, pbnc.getResult());
		chart.setDescription("If a sequencer is unable to make a base call with sufficient confidence then it will normally substitute an N rather than " + 
				"a conventional base call. This module plots out the percentage of base calls at each position for which an N was called.\n" + 
				"It's not unusual to see a very low proportion of Ns appearing in a sequence, especially nearer the end of a sequence. However, if this " + 
				"proportion rises above a few percent it suggests that the analysis pipeline was unable to interpret the data well enough to make valid " + 
				"base calls." + getCredits() 
				);
		chart.setTitle("N content acrosss all bases");
		DataSeries ds = new DataSeries();
		ds.setName("% N");
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
	
	private JSONObject getOverrepresentedKmers(final Map<String, FastQCDataModule> moduleMap) throws BabrahamDataParseException, JSONException{
		FastQCDataModule ok = moduleMap.get(PlotType.KMER_PROFILES);
		WaspChart chart = new WaspChart();
		chart.setTitle(ok.getName());
		chart.addProperty(QC_ANALYSIS_RESULT_KEY, ok.getResult());
		chart.setDescription("\n" + 
				"The analysis of overrepresented sequences will spot an increase in any exactly duplicated sequences, but there are a different subset of " + 
				"problems where it will not work:\n" +
				"<ul>" +
				"<li>If you have very long sequences with poor sequence quality then random sequencing errors will dramatically reduce the counts for " + 
				"exactly duplicated sequences.</li>" +
				"<li>If you have a partial sequence which is appearing at a variety of places within your sequence then this won't be seen either by the " + 
				"per base content plot or the duplicate sequence analysis.</li>" +
				"</ul>" +
				"This module counts the enrichment of every 5-mer within the sequence library. It calculates an expected level at which this k-mer should " + 
				"have been seen based on the base content of the library as a whole and then uses the actual count to calculate an observed/expected ratio " + 
				"for that k-mer." + getCredits()
				);
		DataSeries ds = new DataSeries();
		ds.setColLabels(ok.getAttributes());
		ds.setDataFromString(ok.getDataPoints());
		chart.addDataSeries(ds);
		return chart.getAsJSON();
	}
	
	private JSONObject getSequenceLengthDist(final Map<String, FastQCDataModule> moduleMap) throws BabrahamDataParseException, JSONException{
		FastQCDataModule pbgc = moduleMap.get(PlotType.SEQUENCE_LENGTH_DISTRIBUTION);
		WaspChart2D chart = new WaspChart2D();
		chart.setxAxisLabel("sequence length (bp)");
		chart.setyAxisLabel("count");
		chart.addProperty(QC_ANALYSIS_RESULT_KEY, pbgc.getResult());
		chart.setTitle("Distribution of sequence lengths over all sequences");
		chart.setDescription("Some high throughput sequencers generate sequence fragments of uniform length, but others can contain reads of wildly varying " +
				"lengths. Even within uniform length libraries some pipelines will trim sequences to remove poor quality base calls from the end.\n" +
				"This module generates a graph showing the distribution of fragment sizes in the file which was analysed.\n" + 
				"In many cases this will produce a simple graph showing a peak only at one size, but for variable length FastQ files this will show the " +
				"lengths. Even within uniform length libraries some pipelines will trim sequences to remove poor quality base calls from the end.\n" +
				"relative amounts of each different size of sequence fragment." + getCredits());
		DataSeries ds = new DataSeries();
		ds.setName("sequence length");
		ds.setColLabels(pbgc.getAttributes());
		try{
			int lastIndex = pbgc.getDataPoints().size() - 1;
			Integer maxLength =  Integer.valueOf( pbgc.getDataPoints().get(lastIndex).get(0) ); // last row in list
			Map<String, Double> lengthCounts = new LinkedHashMap<String, Double>();
			for (Integer i=1; i <= maxLength; i++)
				lengthCounts.put(i.toString(), 0.0d);
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
	private List<Double> getTheoreticalDistribution(List<Double> gcDistribution){
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
		
		logger.trace("Percentage deviation from normality is "+deviationPercent);
		
		return theoreticalDistribution;
	}


}
