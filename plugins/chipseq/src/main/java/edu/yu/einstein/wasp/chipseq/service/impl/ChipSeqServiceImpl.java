package edu.yu.einstein.wasp.chipseq.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.chipseq.service.ChipSeqService;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.MisconfiguredWorkUnitException;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridTransportConnection;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileGroupMeta;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.util.MetaHelper;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

@Service
@Transactional("entityManager")
public class ChipSeqServiceImpl extends WaspServiceImpl implements ChipSeqService {
	
	@Autowired
	private SampleService sampleService;
	@Autowired
	private JobService jobService;

	/**
	 * {@inheritDoc}
	 */
	@Transactional("entityManager")
	@Override
	public Set<PanelTab> getChipSeqDataToDisplay(Integer jobId) throws PanelException{
		logger.debug("***************starting chipseqService.getChipSeqDataToDisplay(job)");
		Job job = jobService.getJobByJobId(jobId);
		try{
			Sample noControlSample = new Sample();
			noControlSample.setId(0);
			noControlSample.setName("None");
			Set<Sample> testSampleSet = new HashSet<Sample>();
			Map<Sample, List<Sample>> testSampleControlSampleListMap = new HashMap<Sample, List<Sample>>();
			for(Sample sample : job.getSample()){
				for(SampleMeta sm : sample.getSampleMeta()){
					if(sm.getK().startsWith("chipseqAnalysis.controlId::")){//could me zero, one, many
						Sample controlSample = null;
						Integer controlId = Integer.parseInt(sm.getV());
						if(controlId.intValue()==0){
							controlSample = noControlSample;
						}
						else{
							controlSample = sampleService.getSampleById(controlId);
						}
						if(testSampleControlSampleListMap.get(sample)==null){
							List<Sample> controlSampleList = new ArrayList<Sample>();
							controlSampleList.add(controlSample);
							testSampleControlSampleListMap.put(sample, controlSampleList);
							testSampleSet.add(sample);
						}
						else{
							List<Sample> controlSampleList = testSampleControlSampleListMap.get(sample);
							controlSampleList.add(controlSample);
							//testSampleControlSampleListMap.put(sample, controlSampleList);//I doubt this is needed here
						}
					}
				}
			}
			
			Map<Sample, List<String>> sampleRunInfoMap = new HashMap<Sample, List<String>>();
			Map<String, String> sampleIdControlIdCommandLine = new HashMap<String, String>();
			for(Sample sample : testSampleSet){
				for(SampleMeta sm : sample.getSampleMeta()){
					if(sm.getK().startsWith("chipseqAnalysis.testCellLibraryIdList::")){
						if(sampleRunInfoMap.containsKey(sample)){
							continue;
						}
						String cellLibraryIdListAsString = sm.getV();
						List<Integer> cellLibraryIdList = WaspSoftwareJobParameters.getCellLibraryIdList(cellLibraryIdListAsString);
						List<String> runInfo = new ArrayList<String>();
						for(Integer cellLibraryId : cellLibraryIdList){
							SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(cellLibraryId);
							Sample library = sampleService.getLibrary(cellLibrary);
							Sample cell = sampleService.getCell(cellLibrary);
							String lane = "(lane " + sampleService.getCellIndex(cell).toString() + ")";
							Sample platformUnit = sampleService.getPlatformUnitForCell(cell);
							Run run = sampleService.getCurrentRunForPlatformUnit(platformUnit);
							runInfo.add(library.getName() + " [" + run.getName() + " " + lane + "]");
						}
						sampleRunInfoMap.put(sample, runInfo);
					}
					if(sm.getK().startsWith("chipseqAnalysis.controlCellLibraryIdList::")){
						String[] splitK = sm.getK().split("::");
						String controlIdAsString = splitK[1];
						if(controlIdAsString.equalsIgnoreCase("0")){//there was no control, and corresponding sm.getV() is empty
							continue;
						}
						Integer controlId = Integer.parseInt(splitK[1]);
						Sample controlSample = sampleService.getSampleById(controlId);
						if(sampleRunInfoMap.containsKey(controlSample)){
							continue;
						}
						String cellLibraryIdListAsString = sm.getV();
						List<Integer> cellLibraryIdList = WaspSoftwareJobParameters.getCellLibraryIdList(cellLibraryIdListAsString);
						List<String> runInfo = new ArrayList<String>();
						for(Integer cellLibraryId : cellLibraryIdList){
							SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(cellLibraryId);
							Sample library = sampleService.getLibrary(cellLibrary);
							Sample cell = sampleService.getCell(cellLibrary);
							String lane = "(lane " + sampleService.getCellIndex(cell).toString() + ")";
							Sample platformUnit = sampleService.getPlatformUnitForCell(cell);
							Run run = sampleService.getCurrentRunForPlatformUnit(platformUnit);
							runInfo.add(library.getName() + " [" + run.getName() + " " + lane + "]");//if you alter this, then alter line about 24 lines up too 
						}
						sampleRunInfoMap.put(controlSample, runInfo);				
					}
					
					if(sm.getK().startsWith("chipseqAnalysis.commandLineCall::")){
						String[] splitK = sm.getK().split("::");
						String controlIdAsString = splitK[1];
						sampleIdControlIdCommandLine.put(sample.getId().toString() + "::" + controlIdAsString, sm.getV());
					}				
				}
			}
			
			//get the fileGroups for each testSample
			Map<String, List<FileGroup>> sampleIdControlIdFileGroupListMap = new HashMap<String, List<FileGroup>>();
			Set<FileType> fileTypeSet = new HashSet<FileType>();
			List<FileType> fileTypeList = new ArrayList<FileType>();
			
			for(Sample sample : testSampleSet){
				for(FileGroup fg : sample.getFileGroups()){
					for(FileGroupMeta fgm : fg.getFileGroupMeta()){
						if(fgm.getK().equalsIgnoreCase("chipseqAnalysis.controlId")){
							if(sampleIdControlIdFileGroupListMap.containsKey(sample.getId()+"::"+fgm.getV())){
								sampleIdControlIdFileGroupListMap.get(sample.getId().toString()+"::"+fgm.getV()).add(fg);
							}
							else{
								List<FileGroup> fileGroupList = new ArrayList<FileGroup>();
								fileGroupList.add(fg);
								sampleIdControlIdFileGroupListMap.put(sample.getId().toString()+"::"+fgm.getV(), fileGroupList);
							}
							FileHandle fileHandle = new ArrayList<FileHandle>(fg.getFileHandles()).get(0);
							fileTypeSet.add(fg.getFileType());
							break;//from filegroupmeta for loop
						}
					}
				}
			}
			fileTypeList.addAll(fileTypeSet);
			class FileTypeComparator implements Comparator<FileType> {
			    @Override
			    public int compare(FileType arg0, FileType arg1) {
			        return arg0.getIName().compareToIgnoreCase(arg1.getIName());
			    }
			}
			Collections.sort(fileTypeList, new FileTypeComparator());

			
			class FileGroupComparator implements Comparator<FileGroup> {
			    @Override
			    public int compare(FileGroup arg0, FileGroup arg1) {
			        return arg0.getFileType().getIName().compareToIgnoreCase(arg1.getFileType().getIName());
			    }
			}
			
			//check AND order the filegroups by filetype.iname
			for(Sample sample : testSampleControlSampleListMap.keySet()){
				List<Sample> controlSampleList = testSampleControlSampleListMap.get(sample);
				for(Sample controlSample : controlSampleList){
					Assert.assertTrue(sampleIdControlIdFileGroupListMap.containsKey(sample.getId().toString()+"::"+controlSample.getId().toString()));
					Collections.sort(sampleIdControlIdFileGroupListMap.get(sample.getId().toString()+"::"+controlSample.getId().toString()), new FileGroupComparator());
				}
			}
			
			
			Set<PanelTab> panelTabSet = new LinkedHashSet<PanelTab>();
			PanelTab panelTab = new PanelTab();
	
			panelTabSet.add(panelTab);

			logger.debug("***************ending chipseqService.getChipSeqDataToDisplay(job)");

			return panelTabSet;
		}catch(Exception e){logger.debug("***************EXCEPTION IN chipseqService.getChipSeqDataToDisplay(job): "+ e.getStackTrace());throw new PanelException(e.getMessage());}
	}

}
