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

		System.out.println("\n\n\n\n\n\n\n yyy " + filename + "\n");
		System.out.println("\n zzz " + filenameRegex + "\n");

		Pattern p = Pattern.compile(filenameRegex); 
		Matcher m = p.matcher(filename);

		if (! m.matches()) {
		System.out.println("\n doesnt match " + filenameRegex + "\n");
			throw new Exception("doesnt match"); 
		}

		File file = fileService.getMetaInformation(filename);
		fileService.save(file);

		for (int i = 0; i < matchList.length; i++) {
		System.out.println("\n i " + i + "\n");
			String matchKey = matchList[i];

			System.out.println("\n\n\n[" + matchKey + "]\n");
			if ("jobId".equals(matchKey)) {

		System.out.println("\n" + i + "JobId " + m.group(i+1) + "\n");
				Integer jobId = Integer.parseInt(m.group(i+1));
				JobFile newJobFile = new JobFile();
				newJobFile.setJobId(jobId); 
				newJobFile.setFileId(file.getFileId()); 
				jobFileService.save(newJobFile);

				continue;
			}
			if ("sampleId".equals(matchKey)) {
		System.out.println("\n" + i + "sampleId  " + m.group(i+1) + "\n");
				Integer sampleId = Integer.parseInt(m.group(i+1));
				SampleFile newSampleFile = new SampleFile();
				newSampleFile.setSampleId(sampleId); 
				newSampleFile.setFileId(file.getFileId()); 
				sampleFileService.save(newSampleFile);
				continue;
			}
			if ("runId".equals(matchKey)) {
		System.out.println("\n" + i + "runId  " + m.group(i+1) + "\n");
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
