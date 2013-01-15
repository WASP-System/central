
/**
 *
 * FileServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the FileService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.dao.FileDao;
import edu.yu.einstein.wasp.dao.FileTypeDao;
import edu.yu.einstein.wasp.dao.JobDraftFileDao;
import edu.yu.einstein.wasp.dao.SampleFileDao;
import edu.yu.einstein.wasp.exception.FileUploadException;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.model.File;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftFile;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleFile;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.SampleService;

@Service
@Transactional("entityManager")
public class FileServiceImpl extends WaspServiceImpl implements FileService {


	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private JobDraftFileDao jobDraftFileDao;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private SampleFileDao sampleFileDao;
	
	@Autowired
	private FileTypeDao fileTypeDao;
	
	@Autowired
	private GridHostResolver hostResolver;
	
	private static final Logger logger = LoggerFactory.getLogger(WaspServiceImpl.class);
	
	/**
	 * fileDao;
	 *
	 */
	private FileDao fileDao;

	/**
	 * setFileDao(FileDao fileDao)
	 *
	 * @param fileDao
	 *
	 */
	@Override
	@Autowired
	public void setFileDao(FileDao fileDao) {
		this.fileDao = fileDao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileDao getFileDao() {
		return this.fileDao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getFileByFileId (final int fileId) {
		return this.getFileDao().getFileByFileId(fileId);
	}
  
 
	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getFileByFilelocation (final String filelocation) {
		return this.getFileDao().getFileByFilelocation(filelocation);
	}
	
	/**
	 * TODO: 
	 * {@inheritDoc}
	 */
	@Override 
	public File processUploadedFile(MultipartFile mpFile, String destPath, String description) throws FileUploadException{
		String noSpacesFileName = mpFile.getOriginalFilename().replaceAll("\\s+", "_");
		//String absolutePath = destPath+"/"+mpFile.getOriginalFilename();
		String absolutePath = destPath+"/"+noSpacesFileName;
		java.io.File pathFile = new java.io.File(destPath);
		if (!pathFile.exists()){
			try{
				pathFile.mkdir();
			} catch(Exception e){
				throw new FileUploadException("File upload failure trying to create '"+destPath+"': "+e.getMessage());
			}
		}
						
		String md5Hash = "";
		try {
			md5Hash = DigestUtils.md5Hex(mpFile.getInputStream());
		} catch (IOException e) {
			//logger.warn("Cannot generate MD5 Hash for '"+mpFile.getOriginalFilename()+"': "+ e.getMessage());
			logger.warn("Cannot generate MD5 Hash for '"+noSpacesFileName+"': "+ e.getMessage());
		}
		//String fileName = mpFile.getOriginalFilename();
		String fileName = mpFile.getOriginalFilename().replaceAll("\\s+", "_");
		Integer fileSizeK = (int)((mpFile.getSize()/1024) + 0.5);
		logger.debug("Uploading file '"+fileName+"' to '"+absolutePath+"' (size="+fileSizeK+"Kb, md5Hash="+md5Hash+")");
		java.io.File newFile = new java.io.File(absolutePath);
		try{
			mpFile.transferTo(newFile);
		} catch(Exception e){
			throw new FileUploadException("File upload failure trying to save '"+absolutePath+"': "+e.getMessage());
		}
		File file = new File();
		file.setDescription(description);
		//file.setFileURI(absolutePath);
		file.setIsActive(1);
		file.setMd5hash(md5Hash);
		file.setSizek(fileSizeK);		
		return fileDao.save(file);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public JobDraftFile linkFileWithJobDraft(File file, JobDraft jobDraft){
		JobDraftFile jobDraftFile = new JobDraftFile();
		jobDraftFile.setFile(file);
		jobDraftFile.setJobDraft(jobDraft);
		return jobDraftFileDao.save(jobDraftFile);
	}
	
	@Override
	public Set<FileType> getFileTypes() {
		return fileTypeDao.getFileTypes();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<File> getFilesByType(FileType fileType){
		Assert.assertParameterNotNull(fileType, "must provide a fileType");
		Assert.assertParameterNotNull(fileType.getFileTypeId(), "fileType has no valid fileTypeId");
		Map<String, Integer> m = new HashMap<String, Integer>();
		m.put("fileTypeId", fileType.getFileTypeId());
		return fileDao.findByMap(m);
	}
	
	/**
	 * {@inheritDoc}
	 * @throws SampleTypeException 
	 */
	@Override
	public List<File> getFilesForLibrary(Sample library) throws SampleTypeException{
		Assert.assertParameterNotNull(library, "must provide a library");
		if (!sampleService.isLibrary(library))
			throw new SampleTypeException("sample is not of type library");
		Map<String, Integer> m = new HashMap<String, Integer>();
		m.put("sampleId", library.getSampleId());
		List<File> files = new ArrayList<File>();
		for (SampleFile sf: sampleFileDao.findByMap(m))
			files.add(sf.getFile());
		return files;
	}
	
	/**
	 * {@inheritDoc}
	 * @throws SampleTypeException 
	 */
	@Override
	public List<File> getFilesForLibraryByType(Sample library, FileType fileType) throws SampleTypeException{
		Assert.assertParameterNotNull(fileType, "must provide a fileType");
		Assert.assertParameterNotNull(fileType.getFileTypeId(), "fileType has no valid fileTypeId");
		Map<FileType, List<File>> filesByType = getFilesForLibraryMappedToFileType(library);
		if (!filesByType.containsKey(fileType))
			return new ArrayList<File>();
		return filesByType.get(fileType);
	}
	
	/**
	 * {@inheritDoc}
	 * @throws SampleTypeException 
	 */
	@Override
	public Map<FileType, List<File>> getFilesForLibraryMappedToFileType(Sample library) throws SampleTypeException{
		Assert.assertParameterNotNull(library, "must provide a library");
		if (!sampleService.isLibrary(library))
			throw new SampleTypeException("sample is not of type library");
		Map<String, Integer> m = new HashMap<String, Integer>();
		m.put("sampleId", library.getSampleId());
		Map<FileType, List<File>> filesByType = new HashMap<FileType, List<File>>();
		for (SampleFile sf: sampleFileDao.findByMap(m)){
			File f = sf.getFile();
			FileType ft = f.getFileType();
			if (!filesByType.containsKey(ft))
				filesByType.put(ft, new ArrayList<File>());
			filesByType.get(ft).add(f);
		}
		return filesByType;
	}

	@Override
	public void addFile(File file) {
		fileDao.save(file);
	}

	@Override
	public void setSampleFile(File file, Sample sample) {
		Sample s = sampleService.getSampleById(sample.getSampleId());
		Map<String, Integer> m = new HashMap<String, Integer>();
		m.put("sampleId", sample.getSampleId());
		m.put("fileId", file.getFileId());
		List<SampleFile> sf = sampleFileDao.findByMap(m);
		if (sf.size() == 0) {
			if (s.getSampleFile() == null) {
				s.setSampleFile(new ArrayList<SampleFile>());
			}
			SampleFile sfi = new SampleFile();
			sfi.setFile(file);
			sfi.setSample(s);
			s.getSampleFile().add(sfi);
		}
	}

	@Override
	public FileType getFileType(String iname) {
		return fileTypeDao.getFileTypeByIName(iname);
	}

	/**
	 * Take a new (isActive == 0) entity managed file and register it.
	 * @throws FileNotFoundException 
	 * @throws GridUnresolvableHostException 
	 */
	@Override
	public void registerFile(File file) throws FileNotFoundException, GridException {
		URI uri = file.getFileURI();
		if (uri == null)
			throw new FileNotFoundException("File URI was null");
		
		// TODO: implement grid resolution of URNs
		if (uri.getScheme() != "file") {
			String message = "unable to locate " + uri.toString() + ", unimplemented scheme: " + uri.getScheme();
			logger.warn(message);
			throw new FileNotFoundException(message);
		}
		
		setMD5(file);
		file.setIsActive(1);
		file.setIsArchived(0);
		
	}

	private void setMD5(File file) throws GridException, FileNotFoundException {
		
		URL url;
		try {
			url = file.getFileURI().toURL();
		} catch (MalformedURLException e) {
			String message = "malformed url " + file.getFileURI().toString();
			logger.warn(message);
			throw new FileNotFoundException(message);
		}
		
		GridWorkService gws = hostResolver.getGridWorkService(url.getHost());
		
		String path = url.getPath();
		
		try {
			if (!gws.getGridFileService().exists(path)) {
				String message = "unable to locate" + url.toString() + ", file does not exist";
				logger.warn(message);
				throw new FileNotFoundException(message);
			}
		} catch (IOException e) {
			String message = e.getLocalizedMessage();
			logger.error(message);
			throw new GridAccessException(message);
		}
		
		WorkUnit w = new WorkUnit();
		w.addRequiredFile(file);
		w.setCommand("md5sum ${WASPFILE[0]} | awk '{print $1}'");
		GridResult r = gws.execute(w);
		
		ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
		while(!gws.isFinished(r)) {
			ScheduledFuture<?> foo = ex.schedule(new Runnable() {
				@Override
				public void run() {
					
				}}, 10, TimeUnit.SECONDS);
			while (!foo.isDone()) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
		ex.shutdownNow();
		
		try {
			InputStream is = gws.readResultStdOut(r);
			StringWriter sw = new StringWriter();
			IOUtils.copy(is, sw);
			String md5 = sw.toString();
			file.setMd5hash(md5);
		} catch (IOException e) {
			String message = "Unable to obtain stdout: " + e.getLocalizedMessage();
			logger.warn(message);
			throw new GridExecutionException(message);
		}
	}
	
	
}

