/**
 * 
 */
package edu.yu.einstein.wasp.plugin.babraham.batch.tasklet;

import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.plugin.babraham.batch.service.BabrahamBatchService;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQScreen;
import edu.yu.einstein.wasp.service.FileService;

/**
 * @author calder / asmclellan
 *
 */
public class FastQScreenTasklet extends WaspRemotingTasklet {

	@Autowired
	private FileService fileService;
	
	@Autowired
	private BabrahamBatchService babrahamService;
	
	@Autowired
	private FastQScreen fastqscreen;
	
	@Autowired
	private GridHostResolver hostResolver;

	private Integer fileGroupId;
	
	public static final String FASTQSCREEN_PLOT_META_KEY = "fastqscreen_plot";

	/**
	 * 
	 */
	public FastQScreenTasklet() {
		// proxy
	}

	public FastQScreenTasklet(String fileGroupId) {
		Assert.assertParameterNotNull(fileGroupId);
		this.fileGroupId = Integer.valueOf(fileGroupId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional("entityManager")
	public GridResult doExecute(ChunkContext context) throws Exception {
		// get work unit
		WorkUnit w = fastqscreen.getFastQScreen(fileGroupId);
		
		// execute it
		return  hostResolver.execute(w);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional("entityManager")
	public void doPreFinish(ChunkContext context) throws Exception {
		// the work unit is complete, parse output
		GridResult result = getGridResult(context);
		// parse and save output
		babrahamService.saveJsonForParsedSoftwareOutput(fastqscreen.parseOutput(result.getResultsDirectory()), FASTQSCREEN_PLOT_META_KEY, fastqscreen, fileGroupId);
	}

	@Override
	public void doCleanupBeforeRestart(ChunkContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
