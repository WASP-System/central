/**
 * 
 */
package edu.yu.einstein.wasp.plugin.babraham.batch.tasklet;

import org.json.JSONException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.plugin.babraham.batch.service.BabrahamBatchService;
import edu.yu.einstein.wasp.plugin.babraham.exception.BabrahamDataParseException;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQC;
import edu.yu.einstein.wasp.service.FileService;

/**
 * @author calder
 * 
 */
public class FastQCTasklet extends WaspRemotingTasklet {

	@Autowired
	private FileService fileService;
	
	@Autowired
	private BabrahamBatchService babrahamService;
	
	@Autowired
	private FastQC fastqc;
	
	@Autowired
	private GridHostResolver hostResolver;

	private Integer fileGroupId;

	/**
	 * 
	 */
	public FastQCTasklet() {
		// proxy
	}

	public FastQCTasklet(String fileGroupId) {
		Assert.assertParameterNotNull(fileGroupId);
		this.fileGroupId = Integer.valueOf(fileGroupId);
		
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	@Transactional("entityManager")
	public void doExecute(ChunkContext context) throws Exception {
		// get work unit
		WorkUnit w = fastqc.getFastQC(fileGroupId);
		
		// execute it
		GridResult result = hostResolver.execute(w);
		
		//place the grid result in the step context
		saveGridResult(context, result);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	@Transactional("entityManager")
	public void doPreFinish(ChunkContext context) throws BabrahamDataParseException, MetadataException, GridException, JSONException {
		// the work unit is complete, parse output
		GridResult result = getGridResult(context);
		// parse and save output
		babrahamService.saveJsonForParsedSoftwareOutput(fastqc.parseOutput(result.getResultsDirectory()), fastqc, fileGroupId);
	}

}
