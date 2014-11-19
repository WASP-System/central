package edu.yu.einstein.wasp.plugin.babraham.batch.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.MisconfiguredWorkUnitException;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridTransportConnection;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ProcessMode;
import edu.yu.einstein.wasp.plugin.babraham.batch.service.BabrahamBatchService;
import edu.yu.einstein.wasp.plugin.babraham.exception.BabrahamDataParseException;
import edu.yu.einstein.wasp.plugin.babraham.service.impl.AbstractBabrahamServiceImpl;
import edu.yu.einstein.wasp.plugin.babraham.software.BabrahamDataModule;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQC;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQCDataModule;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQScreen;
import edu.yu.einstein.wasp.service.FileService;

@Service
@Transactional("entityManager")
public class BabrahamBatchServiceImpl extends AbstractBabrahamServiceImpl implements BabrahamBatchService{
	
	@Autowired
	private GridHostResolver hostResolver;
	
	@Autowired
	private FileService fileService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, FastQCDataModule> processFastQCOutput(InputStream inStream) throws BabrahamDataParseException{
		Map<String, FastQCDataModule> dataModules = new HashMap<String, FastQCDataModule>();
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(inStream)); 
			boolean keepReading = true;
			boolean processingModule = false;
			FastQCDataModule currentModule = null;
			boolean isFirstLine = true;
			while (keepReading){
				String line = null;
				line = br.readLine();
				//logger.debug("processing line: " + line);
				if (line == null){
					keepReading = false;
					if (isFirstLine)
						throw new BabrahamDataParseException("Unable to extract data from " + FastQC.OUTPUT_DATA_FILE_TO_EXTRACT + ". File doesn't exist!"); 
				}
				else{
					if (isFirstLine){
						isFirstLine = false;
						if (line.startsWith("caution: filename not matched"))
							throw new BabrahamDataParseException("Unable to extract " + FastQC.OUTPUT_DATA_FILE_TO_EXTRACT + " file from " + 
									FastQC.OUTPUT_ZIP_FILE_NAME + " archive");
						if (!line.startsWith("##FastQC"))
							throw new BabrahamDataParseException("Unexpected first line. Suspect wrong file or file corrupt");
						continue;
					}
					if (line.startsWith(">>")){
						if (processingModule){
							processingModule = false;
							continue;
						}
						// we're looking at the first line of new module
						processingModule = true;
						String[] elements = line.substring(2).split("\t");
						if (elements.length != 2)
							throw new BabrahamDataParseException("Problem parsing line: value must contain 2 elements (name and result). Instead got " 
									+ elements.length + " elements");
						String name = elements[0];
						String result = elements[1];
						String iname = FastQCDataModule.getModuleINameFromName(name);
						if (iname == null)
							throw new BabrahamDataParseException("Unable to obtain a valid fastqc module iname for name '" + name + "'");
						dataModules.put(iname, new FastQCDataModule());
						currentModule = dataModules.get(iname);
						currentModule.setName(name);
						currentModule.setResult(result);
					} else if (line.startsWith("#")){
						// could be a key value pair or a header
						String[] elements = line.substring(1).split("\t");
						if (elements.length == 2){
							try{
								Double.parseDouble(elements[1]); // test value type
								// no exception thrown so reckoning we're looking at key value pair (not a header)
								currentModule.setKeyValueData(elements[0], elements[1]);
								continue;
							} catch (NumberFormatException e){
								// not a numeric value so reckoning this is a header and not a key value pair
								// don't do anything else here
							}
			
						}
						List<String> attributes = new ArrayList<String>();
						for (String attrib : line.substring(1).split("\t")) // remove preceeding # and split on tabs
							attributes.add(attrib);
						currentModule.setAttributes(attributes);
					} else {
						// must be data points
						List<String> row = new ArrayList<String>();
						String[] elements = line.split("\t");
						// check number of data values matches the number of data attributes
						if (elements.length != currentModule.getAttributes().size())
							throw new BabrahamDataParseException("line contains " + elements.length 
									+ " tab-delimited elements which does not match expected number (" + currentModule.getAttributes().size() + ")");
						for (String element: elements)
							row.add(element);
						currentModule.getDataPoints().add(row);
					}
				}
			}
			br.close();
		} catch (IOException e){
			logger.warn(e.getLocalizedMessage());
			throw new BabrahamDataParseException("Unable to parse from InputStream");
		}
		return dataModules;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, FastQCDataModule> parseFastQCOutput(String resultsDir) throws BabrahamDataParseException{
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();
		c.setProcessMode(ProcessMode.SINGLE);
		try {
			GridWorkService workService = hostResolver.getGridWorkService(c);
			GridTransportConnection transportConnection = workService.getTransportConnection();
			resultsDir += "/" + FastQC.OUTPUT_FOLDER;
			c.setWorkingDirectory(resultsDir);
			WorkUnit w = new WorkUnit(c);
			w.addCommand("unzip -p " + FastQC.OUTPUT_ZIP_FILE_NAME + " " + FastQC.OUTPUT_DATA_FILE_TO_EXTRACT);
			GridResult r = transportConnection.sendExecToRemote(w);
			return processFastQCOutput(r.getStdOutStream());
		} catch (MisconfiguredWorkUnitException e) {
			throw new BabrahamDataParseException("Caught MisconfiguredWorkUnitException when trying to parse FastQC output: " + e.getLocalizedMessage());
		} 
		catch (GridException e) {
			throw new BabrahamDataParseException("Caught GridException when trying to parse FastQC output: " + e.getLocalizedMessage());
		} 
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BabrahamDataModule processFastQScreenOutput(InputStream inStream) throws BabrahamDataParseException{
		BabrahamDataModule dataModule = new BabrahamDataModule();
		dataModule.setName("FastQ Screen");
		dataModule.setIName(FastQScreen.FASTQ_SCREEN_AREA);
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(inStream)); 
			boolean keepReading = true;
			int lineNumber = 0;
			while (keepReading){
				lineNumber++;
				String line = null;
				line = br.readLine();
				//logger.debug("processing line: " + line);
				if (line == null)
					keepReading = false;
				else{
					if (lineNumber == 1){
						if (line.contains("No such file or directory"))
							throw new BabrahamDataParseException("Unable to find *_screen.txt file");
						if (!line.startsWith("#Fastq_screen"))
							throw new BabrahamDataParseException("Unexpected first line. Suspect wrong file or file corrupt");
						continue;
					} else if (lineNumber == 2){
						// header
						List<String> attributes = new ArrayList<String>();
						for (String attrib : line.split("\t")) // split on tabs
							attributes.add(attrib.replaceAll("_", " "));
						dataModule.setAttributes(attributes);
					} else {
						// must be data points
						List<String> row = new ArrayList<String>();
						String[] elements = line.split("\t");
						// check number of data values matches the number of data attributes
						if (elements.length != dataModule.getAttributes().size())
							logger.debug("line contains " + elements.length 
									+ " tab-delimited elements which does not match expected number (" + dataModule.getAttributes().size() + ")");
						else {
							for (String element: elements)
								row.add(element);
							dataModule.getDataPoints().add(row);
						}
					}
				}
			}
			br.close();
		} catch (IOException e){
			logger.warn(e.getLocalizedMessage());
			throw new BabrahamDataParseException("Unable to parse from InputStream");
		}
		return dataModule;
	}
	
	/**
	 * {@inheritDoc}
	 * @return 
	 */
	@Override
	public BabrahamDataModule parseFastQScreenOutput(String resultsDir) throws BabrahamDataParseException{
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();
		c.setProcessMode(ProcessMode.SINGLE);
		try {
			GridWorkService workService = hostResolver.getGridWorkService(c);
			GridTransportConnection transportConnection = workService.getTransportConnection();
			resultsDir +=  "/" + FastQScreen.OUTPUT_FOLDER;
			c.setWorkingDirectory(resultsDir);
			WorkUnit w = new WorkUnit(c);
			w.addCommand("cat *_screen.txt");
			GridResult r = transportConnection.sendExecToRemote(w);
			return processFastQScreenOutput(r.getStdOutStream());
		} catch (MisconfiguredWorkUnitException e) {
			throw new BabrahamDataParseException("Caught MisconfiguredWorkUnitException when trying to parse FastQScreen output: " + e.getLocalizedMessage());
		} 
		catch (GridException e) {
			throw new BabrahamDataParseException("Caught GridException when trying to parse FastQScreen output: " + e.getLocalizedMessage());
		} 
	}

}
