/**
 * Tasklet to run the Picard tool "ExtractIlluminaBarcodes" 
 */
package edu.yu.einstein.wasp.plugin.picard.tasklet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.w3c.dom.Document;

import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.plugin.illumina.IlluminaIndexingStrategy;
import edu.yu.einstein.wasp.plugin.illumina.service.WaspIlluminaService;
import edu.yu.einstein.wasp.plugin.illumina.software.IlluminaHiseqSequenceRunProcessor;
import edu.yu.einstein.wasp.plugin.picard.software.Picard;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * @author calder
 *
 */
public class ExtractIlluminaBarcodes extends WaspRemotingTasklet {
	
	@Autowired
	private RunService runService;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private GridHostResolver hostResolver;
	
	@Autowired
	private IlluminaHiseqSequenceRunProcessor casava;
	
	@Autowired
	private WaspIlluminaService illuminaService;
	
	@Autowired
	@Qualifier("picard")
	private Picard picard;
	
	private Run run;
	private Sample platformUnit;
	private IlluminaIndexingStrategy strategy;

	/**
	 * @throws WaspException 
	 * 
	 */
	public ExtractIlluminaBarcodes(Integer runId, String strategy) throws WaspException {
		this.run = runService.getRunById(runId);
		this.platformUnit = run.getPlatformUnit();
		this.strategy = new IlluminaIndexingStrategy(strategy);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void doExecute(ChunkContext context) throws Exception {
		
		Map<Integer,Sample> indexedCellMap = sampleService.getIndexedCellsOnPlatformUnit(platformUnit);
		Document runInfo = illuminaService.getIlluminaRunXml(run);
		
		
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(casava);
		sd.add(picard);
		WorkUnit w = new WorkUnit();
		w.setSoftwareDependencies(sd);
		w.setProcessMode(ProcessMode.MAX);
		w.setMode(ExecutionMode.PROCESS);
		
		w.setCommand(picard.getExtractIlluminaBarcodesCommand(run, indexedCellMap));
		
		GridWorkService gws = hostResolver.getGridWorkService(w);
		
		hostResolver.get

	}

}
