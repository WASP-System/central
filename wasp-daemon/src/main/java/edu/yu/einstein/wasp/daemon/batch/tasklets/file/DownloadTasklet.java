/**
 * 
 */
package edu.yu.einstein.wasp.daemon.batch.tasklets.file;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;

/**
 * This tasklet downloads a remote file on a remote host and optionally creates a symbolic link to the file (filename).
 * @author calder
 *
 */
public class DownloadTasklet extends AbstractRemoteFileTasklet {
	
	@Autowired
	private GridHostResolver hostResolver;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String hostname;
	private String directory;
	private String fileUrl;
	private String filename = null;
	
	public DownloadTasklet(String hostname, String directory, String fileUrl) {
		this.hostname = hostname;
		this.directory = directory;
		this.fileUrl = fileUrl;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void doExecute(ChunkContext context) throws Exception {
		GridWorkService host = hostResolver.getGridWorkService(hostname);
		URL url = new URL(fileUrl);
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();
		c.setWorkingDirectory(directory);
		WorkUnit w = new WorkUnit(c);
		w.setCommand("if [ -e " + url.getFile() + "_" + FILE_TRANSFER_BEGUN_SEMAPHORE + " ]; then exit 1; fi");
		w.addCommand("date > " + url.getFile() + "_" + FILE_TRANSFER_BEGUN_SEMAPHORE);
		if (filename == null) {
			w.addCommand("rm -f " + url.getFile());
		} else {
			w.addCommand("rm -f " + filename);
		}
		w.setSecureResults(true);
		if (filename == null) {
			w.addCommand("curl -O " + url.toString());
		} else {
			w.addCommand("curl " + url.toString() + " -o " + filename);
		}
		w.addCommand("date > " + url.getFile() + "_" + FILE_TRANSFER_COMPLETE_SEMAPHORE);
		
		GridResult r = host.execute(w);
		
		saveGridResult(context, r);

	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Override
	public void doCleanupBeforeRestart(ChunkContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
