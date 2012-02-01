package edu.yu.einstein.wasp.batch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.model.File;
import edu.yu.einstein.wasp.model.JobFile;
import edu.yu.einstein.wasp.model.SampleFile;
import edu.yu.einstein.wasp.model.RunFile;
import edu.yu.einstein.wasp.model.State;

import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobFileService;
import edu.yu.einstein.wasp.service.SampleFileService;
import edu.yu.einstein.wasp.service.RunFileService;
import edu.yu.einstein.wasp.service.StateService;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

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
	JobFileService jobFileService;

	@Autowired
	SampleFileService sampleFileService;

	@Autowired
	RunFileService runFileService;

	@Autowired
	StateService stateService;

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
		fileService.save(file);

		// registers files w/ respective entities
		for (int i = 0; i < matchList.length; i++) {
			String matchKey = matchList[i];

			if ("jobId".equals(matchKey)) {
				Integer jobId = Integer.parseInt(m.group(i+1));
				JobFile newJobFile = new JobFile();
				newJobFile.setJobId(jobId); 
				newJobFile.setFileId(file.getFileId()); 
				jobFileService.save(newJobFile);

				continue;
			}
			if ("sampleId".equals(matchKey)) {
				Integer sampleId = Integer.parseInt(m.group(i+1));
				SampleFile newSampleFile = new SampleFile();
				newSampleFile.setSampleId(sampleId); 
				newSampleFile.setFileId(file.getFileId()); 
				sampleFileService.save(newSampleFile);
				continue;
			}
			if ("runId".equals(matchKey)) {
				Integer runId = Integer.parseInt(m.group(i+1));
				RunFile newRunFile = new RunFile();
				newRunFile.setRunId(runId); 
				newRunFile.setFileId(file.getFileId()); 
				runFileService.save(newRunFile);
				continue;
			}
		}

		return file;
	}



}
