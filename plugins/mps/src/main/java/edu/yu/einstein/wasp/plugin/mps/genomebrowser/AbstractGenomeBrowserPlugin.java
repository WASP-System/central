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

import edu.yu.einstein.wasp.interfacing.plugin.GenomeBrowserProviding;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.SampleService;

public abstract class AbstractGenomeBrowserPlugin extends WaspPlugin implements GenomeBrowserProviding{

	@Autowired
	private FileService fileService;
	@Autowired
	private SampleService sampleService;
	@Autowired
	private GenomeService genomeService;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5690998668723403358L;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public AbstractGenomeBrowserPlugin(String iName, Properties waspSiteProperties, MessageChannel channel) {
		super(iName, waspSiteProperties, channel);
	}
	
	protected Build getBuild(FileGroup fg){

		FileGroup parentFileGroup = fileService.isFileGroupCollection(fg)?fg.getParent():fg;
		
		Set<Build> buildSet = new HashSet<Build>();
		
		List<SampleSource> cellLibraryList = new ArrayList<SampleSource>(parentFileGroup.getSampleSources());
		for(SampleSource cellLibrary : cellLibraryList){//not always singleton; for chipseq, two samples, the ip and control
			Sample library = sampleService.getLibrary(cellLibrary);
			Sample parentSample = library.getParent()==null?library:library.getParent();
			try{
				buildSet.add(genomeService.getBuild(parentSample));
			}catch(Exception e){
				logger.debug("unable to getBuild via genomeService: " + e.getMessage());
				return null;
			}
		}
		if(buildSet.size()==1){
			return new ArrayList<Build>(buildSet).get(0);
		}
		else{
			return null;
		}
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub

	}

}
