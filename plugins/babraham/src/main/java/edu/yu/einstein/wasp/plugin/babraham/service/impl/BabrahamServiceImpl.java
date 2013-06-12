/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.babraham.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridTransportConnection;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.plugin.babraham.exception.FastQCDataParseException;
import edu.yu.einstein.wasp.plugin.babraham.service.BabrahamService;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQCDataModule;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;

@Service
@Transactional("entityManager")
public class BabrahamServiceImpl extends WaspServiceImpl implements BabrahamService {
	
	@Autowired
	private GridHostResolver hostResolver;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String performAction() {
		// do something
		return "done";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, FastQCDataModule> parseFastQCOutput(GridResult gridResult) throws GridException, FastQCDataParseException{
		Map<String, FastQCDataModule> dataModules = new HashMap<String, FastQCDataModule>(); 
		WorkUnit w = new WorkUnit();
		w.setProcessMode(ProcessMode.SINGLE);
		GridWorkService workService = hostResolver.getGridWorkService(w);
		GridTransportConnection transportConnection = workService.getTransportConnection();
		String resultsDir = gridResult.getResultsDirectory();
		w.setWorkingDirectory(resultsDir);
		w.addCommand("cat fastqc_data.txt");
		try {
			GridResult r = transportConnection.sendExecToRemote(w);
			BufferedReader br = new BufferedReader(new InputStreamReader(r.getStdOutStream()));
			boolean keepReading = true;
			boolean processingModule = false;
			
			FastQCDataModule currentModule = null;
			boolean isFirstLine = true;
			while (keepReading){
				String line = br.readLine();
				if (line == null)
					keepReading = false;
				else{
					if (isFirstLine){
						isFirstLine = false;
						if (line.contains("No such file or directory"))
							throw new FastQCDataParseException("Unable to find fastqc_data.txt file in " + resultsDir);
					}
					if (line.startsWith(">>")){
						if (processingModule){
							processingModule = false;
							continue;
						}
						// we're looking at the first line of new module
						processingModule = true;
						String[] elements = line.substring(2).split("\n");
						if (elements.length != 2)
							throw new FastQCDataParseException("Problem parsing line: value must contain two elements (name and result)");
						String name = elements[0];
						String result = elements[1];
						String iname = FastQCDataModule.getModuleINameFromName(name);
						if (iname == null)
							throw new FastQCDataParseException("Unable to obtain a valid fastqc module iname for name '" + name + "'");
						dataModules.put(iname, new FastQCDataModule());
						currentModule = dataModules.get(iname);
						currentModule.setName(name);
						currentModule.setResult(result);
					} else if (line.startsWith("#")){
						// could be a key value pair or a header
						String[] elements = line.substring(1).split("\n");
						if (elements.length == 2){
							try{
								Double value = Double.parseDouble(elements[1]);
								// no exception thrown so reckoning we're looking at key value pair (not a header)
								currentModule.setKeyValueData(elements[0], value);
								continue;
							} catch (NumberFormatException e){
								// not a numeric value so reckoning this is a header and not a key value pair
								// don't do anything else here
							}
			
						}
						Set<String> attributes = new LinkedHashSet<String>();
						for (String attrib : line.substring(1).split("\t")) // remove preceeding # and split on tabs
							attributes.add(attrib);
						currentModule.setAttributes(attributes);
					} else {
						// must be data points
						Set<String> attributes = currentModule.getAttributes();
						Set<Map<String, Double>> dataPoints = new LinkedHashSet<Map<String,Double>>();
						String[] elements = line.split("\t");
						// check number of data values matches the number of data attributes
						if (elements.length != attributes.size())
							throw new FastQCDataParseException("line contains " + elements.length + " tab-delimited elements which does not match expected number (" + attributes.size() + ")");
						int i = 0;
						for (String attrib: attributes){
							Map<String, Double> dataPoint = new LinkedHashMap<String, Double>();
							dataPoint.put(attrib, Double.valueOf(elements[i++]));
							dataPoints.add(dataPoint);
						}
						currentModule.setDataPoints(dataPoints);
					}
				}
			}
			br.close();
		} catch (Exception e) {
			throw new GridException("Caught " + e.getClass().getSimpleName() + " when trying to parse FastQC output: " + e.getLocalizedMessage());
		}
		return dataModules;
	}
	

}
