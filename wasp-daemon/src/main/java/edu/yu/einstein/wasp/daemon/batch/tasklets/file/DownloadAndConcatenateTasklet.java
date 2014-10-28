/**
 * 
 */
package edu.yu.einstein.wasp.daemon.batch.tasklets.file;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;


/**
 * This tasklet should be passed a delimited list of URLs to download files via curl and optionally pipe through zcat or bzcat (determined
 * by file extension).  It does not work with tar or zip archives.  The code that chooses to launch a flow using this tasklet should be
 * synchronized and blocking in case the file is to be downloaded to a common area.  Optional delimited list of MD5sums.
 * 
 * @author calder
 *
 */
public class DownloadAndConcatenateTasklet extends AbstractRemoteFileTasklet {
	
	@Autowired
	private GridHostResolver hostResolver;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String hostname;
	private String directory;
	private List<String> fileUrls;
	private String fileName;
	private List<String> checksums = new ArrayList<String>();
	
	public DownloadAndConcatenateTasklet(String hostname, String directory, String fileUrls, String fileName) {
		this.hostname = hostname;
		this.directory = directory;
		this.fileUrls = Arrays.asList(fileUrls.split(FILE_DELIMITER));
		this.fileName = fileName;
		
	}


	@Override
	public GridResult doExecute(ChunkContext context) throws Exception {
		
		GridWorkService host = hostResolver.getGridWorkService(hostname);
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();
		
		c.setWorkingDirectory(directory);
		c.setResultsDirectory(directory);
		WorkUnit w = new WorkUnit(c);
		w.setCommand("if [ -e " + fileName + "_" + FILE_TRANSFER_COMPLETE_SEMAPHORE + " ]; then exit 0; fi");
		w.addCommand("if [ -e " + fileName + "_" + FILE_TRANSFER_BEGUN_SEMAPHORE + " ]; then exit 1; fi");
		w.addCommand("date > " + fileName + "_" + FILE_TRANSFER_BEGUN_SEMAPHORE);
		w.addCommand("rm -f " + fileName);
		w.setSecureResults(true);
		int n = 0;
		for (String u : fileUrls) {
			URL url = new URL(u);
			String decompress = "";
			String filename = FilenameUtils.getName(url.getFile());
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
			w.addCommand("curl " + url.toString() + " -o " + filename);
			if (checksums.size() > 0) {
				w.addCommand("md=`md5sum " + filename + " | sed 's/ .*//g'`; if [[ \"${md}\" != \"" + checksums.get(n++) + "\" ]]; then echo \"MD5 DOES NOT MATCH\" > /dev/stderr; exit 2; fi" );
			}
			w.addCommand(decompress + filename + " >> " + fileName);
			w.addCommand("rm -f " + filename);
		}
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
	public List<String> getChecksums() {
		return checksums;
	}


	/**
	 * @param checksums the checksums to set
	 */
	public void setChecksums(String checksums) {
		if (checksums == null)
			this.checksums = new ArrayList<String>();
		this.checksums = Arrays.asList(checksums.split(FILE_DELIMITER));
	}


	@Override
	public void doCleanupBeforeRestart(ChunkContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}
	

}
