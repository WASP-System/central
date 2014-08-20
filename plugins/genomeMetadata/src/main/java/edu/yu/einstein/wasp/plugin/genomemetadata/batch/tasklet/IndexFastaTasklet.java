/**
 * 
 */
package edu.yu.einstein.wasp.plugin.genomemetadata.batch.tasklet;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * @author calder
 *
 */
public class IndexFastaTasklet extends WaspRemotingTasklet {
	
	@Autowired
	private GridHostResolver hostResolver;
	
	@Autowired
	@Qualifier("picard")
	private SoftwarePackage picard;
	
	@Autowired
	@Qualifier("samtools")
	private SoftwarePackage samtools;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String hostname;
	private String directory;
	private String buildName;
	
	public IndexFastaTasklet(String hostname, String directory, String buildName) {
		this.hostname = hostname;
		this.directory = directory;
		this.buildName = buildName;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void doExecute(ChunkContext context) throws Exception {
		
		GridWorkService host = hostResolver.getGridWorkService(hostname);
		
		WorkUnit w = new WorkUnit();
		
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(picard);
		sd.add(samtools);
		
		w.setWorkingDirectory(directory);
		w.setResultsDirectory(directory);
		w.setSoftwareDependencies(sd);
		w.setProcessMode(ProcessMode.SINGLE);
		w.setMemoryRequirements(4);
		
		w.setCommand("shopt -s nullglob");
		w.addCommand("for x in *.fa *.fasta; do\n");
		w.addCommand("y=${x%.fa*}; if [[ \"${x}\" != \"${y}.fa\" ]]; then ln -s ${x} ${y}.fa; fi");
		w.addCommand("if [ ! -e ${y}.fa.fai ]; then samtools faidx ${y}.fa; fi");
		w.addCommand("if [ ! -e ${y}.dict ]; then java -jar -Xmx4g $PICARD_ROOT/CreateSequenceDictionary.jar R=${y}.fa O=${y}.dict GENOME_ASSEMBLY=\"" + buildName + "\"; fi");
		w.addCommand("done");
		w.addCommand("shopt -u nullglob");
		
		w.setSecureResults(true);
		
		GridResult r = host.execute(w);
		
		saveGridResult(context, r);

	}

}
