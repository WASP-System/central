package edu.yu.einstein.wasp.plugin.mps.genomebrowser;

import java.util.ArrayList;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;

import edu.yu.einstein.wasp.grid.file.FileUrlResolver;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.SampleService;

public class EnsemblGenomeBrowserPlugin extends AbstractGenomeBrowserPlugin {

	private static final long serialVersionUID = -721419692150965282L;
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
	
	public EnsemblGenomeBrowserPlugin(String iName, Properties waspSiteProperties, MessageChannel channel) {
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

	public boolean isDisplayable(Integer fileGroupId){
		FileGroup fg = fileService.getFileGroupById(fileGroupId);
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
	public String getIcon(){
		String icon = "ensembl";
		return icon;
	}
	@Override
	public String getLink(Integer fileGroupId) {
		// TODO Auto-generated method stub
		
		if(!isDisplayable(fileGroupId)){
			return null;
		}
		
		FileGroup fg = fileService.getFileGroupById(fileGroupId);		
		FileHandle fh = new ArrayList<FileHandle>(fg.getFileHandles()).get(0);//OK?????				
		String resolvedURL = "";
		try{
			resolvedURL = fileUrlResolver.getURL(fh).toString();
		}catch(Exception e){
			logger.debug("UNABLE TO RESOLVE URL for filehandle in ensemblgenomebrowserplugin: " + fh.getFileName()); 
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
		
		if(genomeName.startsWith("GRC")){//assume compatible with ucsc	
			//for testing only: "http://useast.ensembl.org/Homo_sapiens/Location/View?r=1:1-620074;contigviewbottom=url:http://wasp.einstein.yu.edu/results/rob/20140710_IP_Wildtype_flag_TARGET_GATA3_CONTROL_Wildtype_inp_summits2.bed";
			String link = "http://useast.ensembl.org/"+ organismNameWithoutSpaces +"/Location/View?r=1:1-620000;contigviewbottom=url:" + resolvedURL;
			if(fg.getFileType().getId().intValue()==bedGraphFileType.getId().intValue()){
				return link + "=tiling;format=bedGraph";
			}
			else{
				return link;
			}
		}
		else{
			return null;
		}
	}


}
