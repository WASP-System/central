/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.babraham.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.MisconfiguredWorkUnitException;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridTransportConnection;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileGroupMeta;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.plugin.babraham.exception.FastQCDataParseException;
import edu.yu.einstein.wasp.plugin.babraham.service.BabrahamService;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQCDataModule;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.util.MetaHelper;
import edu.yu.einstein.wasp.web.panel.Panel;

@Service
@Transactional("entityManager")
public class BabrahamServiceImpl extends WaspServiceImpl implements BabrahamService {
	
	@Autowired
	private GridHostResolver hostResolver;
	
	@Autowired
	private FileService fileService;
	
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
	public Map<String, FastQCDataModule> processFastQCOutput(InputStream inStream) throws FastQCDataParseException{
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
				if (line == null)
					keepReading = false;
				else{
					if (isFirstLine){
						isFirstLine = false;
						if (line.contains("No such file or directory"))
							throw new FastQCDataParseException("Unable to find fastqc_data.txt file");
						if (!line.startsWith("##FastQC"))
							throw new FastQCDataParseException("Unexpected first line. Suspect wrong file or file corrupt");
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
							throw new FastQCDataParseException("Problem parsing line: value must contain 2 elements (name and result). Instead got " 
									+ elements.length + " elements");
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
							throw new FastQCDataParseException("line contains " + elements.length 
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
			throw new FastQCDataParseException("Unable to parse from InputStream");
		}
		return dataModules;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, FastQCDataModule> parseFastQCOutput(GridResult gridResult) throws FastQCDataParseException{
		WorkUnit w = new WorkUnit();
		w.setProcessMode(ProcessMode.SINGLE);
		try {
			GridWorkService workService = hostResolver.getGridWorkService(w);
			GridTransportConnection transportConnection = workService.getTransportConnection();
			String resultsDir = gridResult.getResultsDirectory();
			w.setWorkingDirectory(resultsDir);
			w.addCommand("cat fastqc_data.txt");
			GridResult r = transportConnection.sendExecToRemote(w);
			return processFastQCOutput(r.getStdOutStream());
		} catch (MisconfiguredWorkUnitException e) {
			throw new FastQCDataParseException("Caught MisconfiguredWorkUnitException when trying to parse FastQC output: " + e.getLocalizedMessage());
		} 
		catch (GridException e) {
			throw new FastQCDataParseException("Caught GridException when trying to parse FastQC output: " + e.getLocalizedMessage());
		} 
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveJsonForParsedSoftwareOutput(Map<String, JSONObject> JsonByKey, Software software, FileGroup fileGroup) throws MetadataException{
		MetaHelper fgMetahelper = new MetaHelper(software.getIName(), FileGroupMeta.class);
		for (String metaKeyName : JsonByKey.keySet())
			fgMetahelper.setMetaValueByName(metaKeyName, JsonByKey.get(metaKeyName).toString());
		fileService.saveFileGroupMeta((List<FileGroupMeta>) fgMetahelper.getMetaList(), fileGroup);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public  JSONObject getJsonForParsedSoftwareOutputByKey(String key, Software software, FileGroup fileGroup) throws JSONException, MetadataException{
		MetaHelper fgMetahelper = new MetaHelper(software.getIName(), FileGroupMeta.class);
		List<FileGroupMeta> fileGroupMeta = fileGroup.getFileGroupMeta();
		if (fileGroupMeta == null)
			fileGroupMeta = new ArrayList<FileGroupMeta>();
		fgMetahelper.setMetaList(fileGroupMeta);
		return new JSONObject(fgMetahelper.getMetaValueByName(key));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Panel> getFastQCDataToDisplay(FileGroup filegroup){
		Set<Panel> panels = new LinkedHashSet<Panel>();
		// TODO: code here
		return panels;
	}

}
