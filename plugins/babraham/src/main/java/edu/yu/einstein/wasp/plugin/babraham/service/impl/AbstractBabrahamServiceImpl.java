/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.babraham.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileGroupMeta;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.plugin.babraham.service.BabrahamService;
import edu.yu.einstein.wasp.plugin.fileformat.plugin.FastqComparator;
import edu.yu.einstein.wasp.plugin.fileformat.service.FastqService;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.util.MetaHelper;

@Transactional("entityManager")
public abstract class AbstractBabrahamServiceImpl extends WaspServiceImpl implements BabrahamService {
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private RunService runService;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private FastqService fastqService;
	
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
	public void saveJsonForParsedSoftwareOutput(Map<String, JSONObject> JsonByKey, Software software, Integer fileGroupId) throws MetadataException{
		MetaHelper fgMetahelper = new MetaHelper(software.getIName(), FileGroupMeta.class);
		for (String metaKeyName : JsonByKey.keySet())
			fgMetahelper.setMetaValueByName(metaKeyName, JsonByKey.get(metaKeyName).toString());
		fileService.saveFileGroupMeta((List<FileGroupMeta>) fgMetahelper.getMetaList(), fileService.getFileGroupById(fileGroupId));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveJsonForParsedSoftwareOutput(JSONObject json, String key, Software software, Integer fileGroupId) throws MetadataException{
		MetaHelper fgMetahelper = new MetaHelper(software.getIName(), FileGroupMeta.class);
		fgMetahelper.setMetaValueByName(key, json.toString());
		fileService.saveFileGroupMeta((List<FileGroupMeta>) fgMetahelper.getMetaList(), fileService.getFileGroupById(fileGroupId));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public  JSONObject getJsonForParsedSoftwareOutputByKey(String key, Software software, Integer fileGroupId) {
		MetaHelper fgMetahelper = new MetaHelper(software.getIName(), FileGroupMeta.class);
		List<FileGroupMeta> fileGroupMeta = fileService.getFileGroupById(fileGroupId).getFileGroupMeta();
		if (fileGroupMeta == null)
			fileGroupMeta = new ArrayList<FileGroupMeta>();
		fgMetahelper.setMetaList(fileGroupMeta);
		try{
			String jsonText = fgMetahelper.getMetaValueByName(key);
			return new JSONObject(jsonText);
		} catch (MetadataException e1) {
			logger.debug(e1.getLocalizedMessage()); // only debug level here as we might expect this if no data for some reason
			return null;
		} catch (JSONException e2){
			logger.warn(e2.getLocalizedMessage());
			return null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<FileHandle> getUpToFiveRandomForwardReadFiles(FileGroup fileGroup){
		
		List<FileHandle> allFastqFiles= new ArrayList<FileHandle>(fileGroup.getFileHandles());
		
		List<FileHandle> forwardReadFastqFiles = new ArrayList<FileHandle>();
		for(FileHandle fh : allFastqFiles){
			Integer fastqReadSegmentNumber = fastqService.getFastqReadSegmentNumber(fh);
			if ( fastqReadSegmentNumber == null){
				logger.info("Unable to get Fastq Read Segment Number. Going to assume it is 1");
				forwardReadFastqFiles.add(fh);
			} else if (fastqReadSegmentNumber == 1 ){ //forward read
				forwardReadFastqFiles.add(fh);
			}
		}
		if(forwardReadFastqFiles.size()<=5){
			return forwardReadFastqFiles;			
		}
		
		List<FileHandle> fiveRandomForwardReadFastqFiles = new ArrayList<FileHandle>();
		Random randomNumberGenerator = new Random(System.currentTimeMillis());
		while(fiveRandomForwardReadFastqFiles.size()<5){
			int randomInt = randomNumberGenerator.nextInt(forwardReadFastqFiles.size());
			if(fiveRandomForwardReadFastqFiles.contains(forwardReadFastqFiles.get(randomInt))){
				continue;
			}
			else{
				fiveRandomForwardReadFastqFiles.add(forwardReadFastqFiles.get(randomInt));
			}
		}		
		Collections.sort(fiveRandomForwardReadFastqFiles, new FastqComparator(fastqService));//this comparator appears to order files like: a read (R1_001.fq), followed immediately by its mate (R2_001.fq),  if it was a paired end read. This is exactly what fastq_screen requires.
		return fiveRandomForwardReadFastqFiles;		
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Run getRunForFileGroup(FileGroup fileGroup){
		fileGroup = fileService.getFileGroupById(fileGroup.getId()); // ensure attached
		Set<SampleSource> ss = fileGroup.getSampleSources();
		if (ss.isEmpty())
			return null;
		SampleSource cellLibrary = ss.iterator().next();
		try {
			return runService.getRunsForPlatformUnit(sampleService.getPlatformUnitForCell(sampleService.getCell(cellLibrary))).get(0);
		} catch (SampleTypeException | SampleParentChildException e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
