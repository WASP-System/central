/**
 * 
 */
package edu.yu.einstein.wasp.plugin.genomemetadata.batch.tasklet.file;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import edu.yu.einstein.wasp.daemon.batch.tasklets.file.AbstractRemoteFileTasklet;
import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * Downloads a single URL via curl and sorts by the order in a specified genome's fasta index.
 * Comments are preserved at the beginning of the file and if the file contains additional reference 
 * sequences, they are sorted and appended to the end of the sorted file.
 * 
 * Finally, the file is indexed using tabix (part of samtools installation).
 * 
 * This tasklet will sort on three column strategies:
 * BED: chr:1, start:2, end:3
 * GTF: chr:1, start:4, end:5
 * VCF: chr:1, pos:2
 * 
 * @author calder
 * 
 */
public class DownloadAndSortTasklet extends AbstractRemoteFileTasklet {

	@Autowired
	private GridHostResolver hostResolver;
	
	@Autowired
	@Qualifier("samtools")
	private SoftwarePackage samtools;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private String hostname;
	private String remoteBuildPath;
	private String remoteFastaIndexName;
	private URL fileUrl;
	private String fileName;
	private String checksum = null;
	private enum Strategy { BED, GTF, VCF };
	private String strategy;

	public DownloadAndSortTasklet(String hostname, String remoteBuildPath, String remoteFastaIndexName, String fileUrl, String fileName, String strategy) throws MalformedURLException {
		this.hostname = hostname;
		this.remoteBuildPath = remoteBuildPath;
		this.remoteFastaIndexName = remoteFastaIndexName;
		this.fileUrl = new URL(fileUrl);
		this.fileName = fileName;
		this.strategy = strategy;
	}

	@Override
	public GridResult doExecute(ChunkContext context) throws Exception {

		GridWorkService host = hostResolver.getGridWorkService(hostname);
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();
		
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(samtools);

		c.setWorkingDirectory(remoteBuildPath);
		c.setResultsDirectory(remoteBuildPath);
		c.setSoftwareDependencies(sd);
		WorkUnit w = new WorkUnit(c);
		w.setCommand("if [ -e " + fileName + "_" + FILE_TRANSFER_COMPLETE_SEMAPHORE + " ]; then exit 0; fi");
		w.addCommand("if [ -e " + fileName + "_" + FILE_TRANSFER_BEGUN_SEMAPHORE + " ]; then exit 1; fi");
		w.addCommand("date > " + fileName + "_" + FILE_TRANSFER_BEGUN_SEMAPHORE);
		w.addCommand("rm -f " + fileName);
		w.setSecureResults(true);
		int n = 0;
		String decompress = "";
		String filename = FilenameUtils.getName(fileUrl.getFile());
		if (filename.endsWith(".tar.gz") || filename.endsWith(".zip") || filename.endsWith("tar.bz2")) {
			String mess = "This tasklet only works with single files (uncompressed, .gz or .bz2, but not file archives.";
			logger.error(mess);
			throw new WaspRuntimeException(mess);
		}

		if (filename.endsWith(".gz")) {
			decompress = "zcat ";
		} else if (filename.endsWith(".bz2")) {
			decompress = "bzcat ";
		} else {
			decompress = "cat ";
		}
		w.addCommand("curl " + fileUrl.toString() + " -o " + filename);
		if (checksum != null) {
			w.addCommand("md=`md5sum " + filename + " | sed 's/ .*//g'`; if [[ \"${md}\" != \"" + checksum + "\" ]]; then exit 2; fi");
		}
		
		String sortKeys;
		String tabixKeys;
		
		if (strategy.equals(Strategy.BED) || strategy.equals("BED")) {
			logger.trace("strategy BED");
			sortKeys = " -k2,2n -k3,3n ";
			tabixKeys = " -p bed ";
		} else if (strategy.equals(Strategy.GTF) || strategy.equals("GTF")) {
			logger.trace("strategy GTF");
			sortKeys = " -k4,4n -k5,5n ";
			tabixKeys = " -p gff ";
		} else if (strategy.equals(Strategy.VCF) || strategy.equals("VCF")) {
			logger.trace("strategy VCF");
			sortKeys = " -k2,2n";
			tabixKeys = " -p vcf ";
		} else {
			logger.error("bad strategy " + strategy);
			throw new WaspRuntimeException("unknown strategy " + strategy);
		}
		
		w.addCommand("set +o pipefail\n" + decompress + filename + " | awk '/^[^#]/ {exit} {print}' > " + fileName + "\nset -o pipefail"); // get header, assume correct
		w.addCommand("n=0");
		w.addCommand(decompress + filename + " | grep -v \"^#\" > TEMPSORT.0");
		w.addCommand("for x in `cut -f 1 " + remoteFastaIndexName + "`; do"); // for each chromosome name
		w.addCommand(" awk -v cn=\"$x\" '{ if ($1 == cn) { print } else { print > \"/dev/stderr\" } }' TEMPSORT.${n} 2> TEMPSORT.$(( n + 1 )) | sort -T . " + sortKeys + " >> " + fileName );
		w.addCommand(" let n+=1");
		w.addCommand("done");
		w.addCommand("sort -T . TEMPSORT.${n} -k1,1 " + sortKeys + " >> " + fileName);
		w.addCommand("rm -f " + filename + " TEMPSORT.*");
		w.addCommand("bgzip -c " + fileName + " > " + fileName + ".gz");
		w.addCommand("tabix -f " + tabixKeys + " " + fileName + ".gz");
		
		w.addCommand("date > " + fileName + "_" + FILE_TRANSFER_COMPLETE_SEMAPHORE);

		return host.execute(w);
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

	/**
	 * @return the checksums
	 */
	public String getChecksums() {
		return checksum;
	}

	/**
	 * @param checksums
	 *            the checksums to set
	 */
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	@Override
	public void doCleanupBeforeRestart(ChunkContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
