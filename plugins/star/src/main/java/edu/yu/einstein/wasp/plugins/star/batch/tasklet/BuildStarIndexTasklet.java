/**
 * 
 */
package edu.yu.einstein.wasp.plugins.star.batch.tasklet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.plugin.genomemetadata.service.GenomeMetadataService;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.plugins.star.StarGenomeIndexConfiguration;
import edu.yu.einstein.wasp.plugins.star.software.Star;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * @author calder
 *
 */
public class BuildStarIndexTasklet extends WaspRemotingTasklet {

	@Autowired
	private GridHostResolver hostResolver;
	
	@Autowired
	private GenomeService genomeService;
	
	@Autowired
	private GenomeMetadataService genomeMetadataService;
	
	@Autowired
	private Star star;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String hostname;
	private StarGenomeIndexConfiguration config;
	
	public BuildStarIndexTasklet(String hostname, 
			String config) throws JsonParseException, JsonMappingException, IOException {
		
		ObjectMapper mapper = new ObjectMapper();
		
		this.hostname = hostname;
		this.config = mapper.readValue(config, StarGenomeIndexConfiguration.class);
	}

	@Override
	public void doExecute(ChunkContext context) throws Exception {
		
		Build build = genomeService.getBuild(config.getOrganism(), config.getGenome(), config.getBuild());

		GridWorkService host = hostResolver.getGridWorkService(hostname);
		
		logger.debug("setting up to build STAR index for " + build.getGenomeBuildNameString() + ":" + config.generateUniqueKey() + " on " + hostname);

		WorkUnit w = new WorkUnit();
		
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(star);

		w.setWorkingDirectory(config.getDirectory());
		w.setResultsDirectory(config.getDirectory());
		w.setSoftwareDependencies(sd);
		w.setMemoryRequirements(40);
		w.setProcessMode(ProcessMode.MAX);
		w.setCommand("if [ -e " + GenomeService.INDEX_CREATION_COMPLETED + " ]; then echo \"already done\"; exit 0; fi");
		w.addCommand("if [ -e " + GenomeService.INDEX_CREATION_STARTED + ".tmp ]; then echo \"started but not completed\"; exit 1; fi");
		w.addCommand("if [ -e " + GenomeService.INDEX_CREATION_FAILED + " ]; then echo \"previously failed\"; exit 2; fi");
		
		w.setSecureResults(true);
		
		w.addCommand("touch " + GenomeService.INDEX_CREATION_STARTED + ".tmp");
		w.addCommand(star.getStarGenomeBuildString(host, config));
		w.addCommand("rm -f " + GenomeService.INDEX_CREATION_STARTED + ".tmp");
		
		GridResult r = host.execute(w);

		saveGridResult(context, r);
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		super.beforeStep(stepExecution);
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		return super.afterStep(stepExecution);
	}

}
