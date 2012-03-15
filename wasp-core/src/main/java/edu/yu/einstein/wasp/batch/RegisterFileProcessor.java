package edu.yu.einstein.wasp.batch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.dao.JobFileDao;
import edu.yu.einstein.wasp.dao.RunFileDao;
import edu.yu.einstein.wasp.dao.SampleFileDao;
import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.model.File;
import edu.yu.einstein.wasp.model.JobFile;
import edu.yu.einstein.wasp.model.RunFile;
import edu.yu.einstein.wasp.model.SampleFile;
import edu.yu.einstein.wasp.service.FileService;

/**
 * RegisterFileProcessor
 * Processer to listen for existance of file on file system
 * then matches it the approriate entity
 */

@Component
public class RegisterFileProcessor implements ItemProcessor {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	FileService fileService;

	@Autowired
	JobFileDao jobFileDao;

	@Autowired
	SampleFileDao sampleFileDao;

	@Autowired
	RunFileDao runFileDao;

	@Autowired
	StateDao stateDao;

	/**
	 * fileRegex
	 * -  regular expresion for filename
	 *
	 */
	protected String filenameRegex;
	public void setFilenameRegex(String filenameRegex) { this.filenameRegex = filenameRegex;}
	public String getFilenameRegex() { return filenameRegex; }

	/**
	 * matchList
	 * - list of matches
	 *
	 */
	protected String[] matchList;
	public void setMatchList(String[] matchList) { this.matchList = matchList;}
	public String[] getMatchList() { return matchList; }


	@Override
	public Object process(Object filenameObj) throws Exception {
		String filename = (String) filenameObj;

		Pattern p = Pattern.compile(filenameRegex); 
		Matcher m = p.matcher(filename);

		if (! m.matches()) {
			throw new Exception("doesnt match"); 
		}

		File file = fileService.getMetaInformation(filename);
		fileService.getFileDao().save(file);

		// registers files w/ respective entities
		for (int i = 0; i < matchList.length; i++) {
			String matchKey = matchList[i];

			if ("jobId".equals(matchKey)) {
				Integer jobId = Integer.parseInt(m.group(i+1));
				JobFile newJobFile = new JobFile();
				newJobFile.setJobId(jobId); 
				newJobFile.setFileId(file.getFileId()); 
				jobFileDao.save(newJobFile);

				continue;
			}
			if ("sampleId".equals(matchKey)) {
				Integer sampleId = Integer.parseInt(m.group(i+1));
				SampleFile newSampleFile = new SampleFile();
				newSampleFile.setSampleId(sampleId); 
				newSampleFile.setFileId(file.getFileId()); 
				sampleFileDao.save(newSampleFile);
				continue;
			}
			if ("runId".equals(matchKey)) {
				Integer runId = Integer.parseInt(m.group(i+1));
				RunFile newRunFile = new RunFile();
				newRunFile.setRunId(runId); 
				newRunFile.setFileId(file.getFileId()); 
				runFileDao.save(newRunFile);
				continue;
			}
		}

		return file;
	}



}
