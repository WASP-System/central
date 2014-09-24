package edu.yu.einstein.wasp.plugin.mps.genomebrowser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;

import edu.yu.einstein.wasp.grid.file.FileUrlResolver;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.viewpanel.Action;

public class UcscGenomeBrowserPlugin extends AbstractGenomeBrowserPlugin  {

	private static final long serialVersionUID = 3520106925573483866L;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private FileService fileService;
	@Autowired
	private SampleService sampleService;
	@Autowired
	private GenomeService genomeService;
	@Autowired
	private FileUrlResolver fileUrlResolver;
	
	@Autowired
	private FileType bedFileType;
	@Autowired
	private FileType bedGraphFileType;
	@Autowired
	private FileType bigBedFileType;
	@Autowired
	private FileType wigFileType;
	@Autowired
	private FileType bigWigFileType;
	@Autowired
	private FileType vcfFileType;
	@Autowired
	private FileType bamFileType;	
	
	public UcscGenomeBrowserPlugin(String iName, Properties waspSiteProperties, MessageChannel channel) {
		super(iName, waspSiteProperties, channel);
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub

	}

	public Action getAction(FileGroup fileGroup){
		Action action = null;		
		String link = getLink(fileGroup);
		if(link!=null && !link.isEmpty()){
			action = new Action();
			action.setIconClassName(getIcon());
			action.setCallbackContent(link);
		}		
		return action;
	}
	
	private boolean isDisplayable(FileGroup fg){
		if(fg.getId()==null){
			return false;
		}
		if(fileService.isFileGroupCollection(fg)){
			return false;
		}
		int id = fg.getFileType().getId().intValue();
		if(		id == bedFileType.getId().intValue() || 
				id == bedGraphFileType.getId().intValue()  ||
				id == bigBedFileType.getId().intValue()  ||
				id == wigFileType.getId().intValue()  ||
				id == bigWigFileType.getId().intValue()  ||
				id == vcfFileType.getId().intValue()  ||
				id == bamFileType.getId().intValue() ){
			return true;
		}
		return false;
	}
	private String getIcon(){
		return "icon-gb-ucsc";
	}
	
	private String getLink(FileGroup fg) {
		
		if(!isDisplayable(fg)){
			return null;
		}
		
		FileHandle fh = new ArrayList<FileHandle>(fg.getFileHandles()).get(0);//OK?????				
		String resolvedURL = "";
		try{
			resolvedURL = fileUrlResolver.getURL(fh).toString();
		}catch(Exception e){
			logger.debug("UNABLE TO RESOLVE URL for filehandle in ucscgenomebrowserplugin: " + fh.getFileName()); 
			return null;
		}		
		
		Build build = getBuild(fg);
		if(build==null){
			return null;
		}
		//if(genomeName.startsWith("GRC")){//bam file created using GRC genome, so display in Ensembl genome browser
		String genomeName = build.getGenome().getName();//such as GRCm38 or hg19
		String organismName = build.getGenome().getOrganism().getName();//such as   Mus musculus
		String organismNameWithoutSpaces = organismName.replaceAll("\\s+", "_");
		
		if(genomeName.isEmpty() || organismName.isEmpty() || organismNameWithoutSpaces.isEmpty()){
			return null;
		}
		
		if(!genomeName.startsWith("GRC")){//assume compatible with ucsc										
			String lastCharacter = genomeName.substring(genomeName.length() - 1);//checking for something like hg19
			try{
				Integer i = Integer.valueOf(lastCharacter);
			}
			catch(Exception e){
				logger.debug("expected last character of genomeName (" + genomeName + ") to be a digit, but it was not: " + e.getMessage());
				return null;
			}
			return "http://genome.ucsc.edu/cgi-bin/hgTracks?db="+ genomeName +"&hgt.customText=" + resolvedURL;				
		}
		else{
			return null;
		}
	}

	
}
