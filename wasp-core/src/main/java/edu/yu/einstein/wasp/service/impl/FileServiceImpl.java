/**
 * 
 * FileServiceImpl.java
 * 
 * @author echeng (table2type.pl)
 * 
 *         the FileService Implmentation
 * 
 * 
 **/

package edu.yu.einstein.wasp.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.dao.FileGroupDao;
import edu.yu.einstein.wasp.dao.FileHandleDao;
import edu.yu.einstein.wasp.dao.FileTypeDao;
import edu.yu.einstein.wasp.dao.JobDraftFileDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleFileDao;
import edu.yu.einstein.wasp.exception.FileUploadException;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
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
	private SampleDao sampleDao;

	@Autowired
	private FileTypeDao fileTypeDao;

	@Autowired
	private FileGroupDao fileGroupDao;

	@Autowired
	private GridHostResolver hostResolver;

	@Value("${wasp.temporary.dir}")
	protected String tempDir;

	@Value("${wasp.primaryfilehost}")
	protected String fileHost;

	private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	/**
	 * fileDao;
	 * 
	 */
	private FileHandleDao fileHandleDao;

	/**
	 * setFileDao(FileDao fileDao)
	 * 
	 * @param fileDao
	 * 
	 */
	@Override
	@Autowired
	public void setFileHandleDao(FileHandleDao fileHandleDao) {
		this.fileHandleDao = fileHandleDao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileHandleDao getFileHandleDao() {
		return this.fileHandleDao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileHandle getFileHandleById(final int id) {
		return this.getFileHandleDao().getFileHandleById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileGroup getFileGroupById(final int id) {
		return fileGroupDao.getFileGroupById(id);
	}

	/**
	 * Upload submitted file to a temporary location on the remote host.
	 * {@inheritDoc}
	 */
	@Override
	public FileGroup processUploadedFile(MultipartFile mpFile, JobDraft jobDraft, String description) {
		String noSpacesFileName = mpFile.getOriginalFilename().replaceAll("\\s+", "_");

		File temporaryDirectory = new File(tempDir);

		File localFile;
		try {
			localFile = File.createTempFile("wasp.", ".tmp", temporaryDirectory);
		} catch (IOException e) {
			String mess = "Unable to create local temporary file: " + e.getLocalizedMessage();
			logger.warn(mess);
			e.printStackTrace();
			throw new FileUploadException(mess);
		}

		if (!temporaryDirectory.exists()) {
			try {
				temporaryDirectory.mkdir();
			} catch (Exception e) {
				throw new FileUploadException("FileHandle upload failure trying to create '" + tempDir + "': " + e.getMessage());
			}
		}

		FileGroup retGroup = new FileGroup();
		FileHandle file = new FileHandle();
		file = fileHandleDao.save(file);
		retGroup.addFileHandle(file);
		retGroup = fileGroupDao.save(retGroup);

		if (fileHost == null) {
			String mess = "Primary file host has not been configured!  Please set \"wasp.primaryfilehost\" in wasp-config.";
			logger.warn(mess);
			throw new FileUploadException(mess);
		}

		GridWorkService gws;
		GridFileService gfs;
		try {
			gws = hostResolver.getGridWorkService(fileHost);
			gfs = gws.getGridFileService();
		} catch (GridUnresolvableHostException e) {
			String mess = "Unable to resolve remote host ";
			logger.warn(mess);
			e.printStackTrace();
			throw new FileUploadException(mess);
		}

		String draftDir = gws.getTransportConnection().getConfiguredSetting("draft.dir");

		if (draftDir == null) {
			String mess = "Attempted to configure for file copy to " + fileHost + ", but hostname.settings.draft.dir has not been set.";
			logger.warn(mess);
			throw new FileUploadException(mess);
		}

		String remoteDir = draftDir + "/" + "jobDraft/";
		String remoteFile;
		try {
			gfs.mkdir(remoteDir);
			remoteFile = remoteDir + "/" + noSpacesFileName;

			if (gfs.exists(remoteFile)) {
				noSpacesFileName = retGroup.getId() + "__" + noSpacesFileName;
				remoteFile = remoteDir + "/" + noSpacesFileName;
			}
		} catch (IOException e) {
			String mess = "problem creating resources on remote host " + gws.getTransportConnection().getHostName();
			logger.warn(mess);
			e.printStackTrace();
			throw new FileUploadException(mess);
		}

		file.setFileName(noSpacesFileName);

		try {
			OutputStream tmpFile = new FileOutputStream(localFile);

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = mpFile.getInputStream().read(bytes)) != -1) {
				tmpFile.write(bytes, 0, read);
			}

			mpFile.getInputStream().close();
			tmpFile.flush();
			tmpFile.close();
		} catch (IOException e) {
			String mess = "Unable to generate local temporary file with contents of multipart file";
			logger.warn(mess);
			e.printStackTrace();
			localFile.delete();
			throw new FileUploadException(mess);
		}

		retGroup.setDescription(description);

		// TODO: Determine file type and set on the group.

		file.setFileURI(gfs.remoteFileRepresentationToLocalURI(remoteFile));

		try {
			gfs.put(localFile, remoteFile);
			register(retGroup);
		} catch (GridException e) {
			String mess = "Problem accessing remote resources " + e.getLocalizedMessage();
			logger.warn(mess);
			e.printStackTrace();
			throw new FileUploadException(mess);
		} catch (IOException e) {
			String mess = "Problem putting remote file " + e.getLocalizedMessage();
			logger.warn(mess);
			e.printStackTrace();
			throw new FileUploadException(mess);
		} finally {
			localFile.delete();
		}

		fileHandleDao.save(file);
		return fileGroupDao.save(retGroup);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JobDraftFile linkFileGroupWithJobDraft(FileGroup filegroup, JobDraft jobDraft) {
		JobDraftFile jobDraftFile = new JobDraftFile();
		jobDraftFile.setFileGroup(filegroup);
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
	public List<FileGroup> getFilesByType(FileType fileType) {
		Assert.assertParameterNotNull(fileType, "must provide a fileType");
		Assert.assertParameterNotNull(fileType.getId(), "fileType has no valid fileTypeId");
		Map<String, Integer> m = new HashMap<String, Integer>();
		m.put("fileTypeId", fileType.getId());
		return fileGroupDao.findByMap(m);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public List<FileGroup> getFilesForLibrary(Sample library) throws SampleTypeException {
		Assert.assertParameterNotNull(library, "must provide a library");
		if (!sampleService.isLibrary(library))
			throw new SampleTypeException("sample is not of type library");
		Map<String, Integer> m = new HashMap<String, Integer>();
		m.put("sampleId", library.getId());
		List<FileGroup> files = new ArrayList<FileGroup>();
		for (SampleFile sf : sampleFileDao.findByMap(m))
			files.add(sf.getFileGroup());
		return files;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public List<FileGroup> getFilesForLibraryByType(Sample library, FileType fileType) throws SampleTypeException {
		Assert.assertParameterNotNull(fileType, "must provide a fileType");
		Assert.assertParameterNotNull(fileType.getFileTypeId(), "fileType has no valid fileTypeId");
		Map<FileType, List<FileGroup>> filesByType = getFilesForLibraryMappedToFileType(library);
		if (!filesByType.containsKey(fileType))
			return new ArrayList<FileGroup>();
		return filesByType.get(fileType);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public Map<FileType, List<FileGroup>> getFilesForLibraryMappedToFileType(Sample library) throws SampleTypeException {
		Assert.assertParameterNotNull(library, "must provide a library");
		if (!sampleService.isLibrary(library))
			throw new SampleTypeException("sample is not of type library");
		Map<String, Integer> m = new HashMap<String, Integer>();
		m.put("sampleId", library.getSampleId());
		Map<FileType, List<FileGroup>> filesByType = new HashMap<FileType, List<FileGroup>>();
		for (SampleFile sf : sampleFileDao.findByMap(m)) {
			FileGroup g = sf.getFileGroup();
			FileType ft = g.getFileType();
			if (!filesByType.containsKey(ft))
				filesByType.put(ft, new ArrayList<FileGroup>());
			filesByType.get(ft).add(g);
		}
		return filesByType;
	}

	@Override
	public void addFile(FileHandle file) {
		fileHandleDao.save(file);
	}

	@Override
	public void addFileGroup(FileGroup group) {
		fileGroupDao.save(group);
	}

	@Override
	public void setSampleFile(FileGroup group, Sample sample) {
		Sample s = sampleService.getSampleById(sample.getId());
		Map<String, Integer> m = new HashMap<String, Integer>();
		m.put("sampleId", sample.getId());
		m.put("fileGroupId", group.getId());
		List<SampleFile> sf = sampleFileDao.findByMap(m);
		if (sf.size() == 0) {
			if (s.getSampleFile() == null) {
				s.setSampleFile(new HashSet<SampleFile>());
			}
			SampleFile sfi = new SampleFile();
			sfi.setFileGroup(group);
			sfi.setSample(s);
			sampleFileDao.persist(sfi);
			s.getSampleFile().add(sfi);
			sampleDao.save(s);
		}
	}

	@Override
	public FileType getFileType(String iname) {
		return fileTypeDao.getFileTypeByIName(iname);
	}

	/**
	 * Take a new (isActive == 0) entity managed file and register it.
	 * 
	 * @throws FileNotFoundException
	 * @throws GridUnresolvableHostException
	 */
	@Override
	public void register(FileHandle file) throws FileNotFoundException, GridException {
		file = fileHandleDao.merge(file);
		
		validateFile(file);

		logger.debug("attempting to register " + file.getFileURI().toString());
		setMD5(file);
		fileHandleDao.save(file);
	}

	@Override
	public void register(FileGroup group) throws FileNotFoundException, GridException {
		group = fileGroupDao.merge(group);
		for (FileHandle f : group.getFileHandles()) {
			validateFile(f);
		}
		logger.debug("attempting to register FileGroup: " + group.getId());
		
		setMD5(group);
		
		group.setIsActive(1);
		group.setIsArchived(0);
		fileGroupDao.save(group);
	}
	
	private void validateFile(FileHandle file) throws FileNotFoundException {
		
		URI uri = file.getFileURI();
		if (uri == null)
			throw new FileNotFoundException("FileHandle URI was null");

		// TODO: implement grid resolution of URNs
		if (!uri.getScheme().equals("file")) {
			String message = "unable to locate " + uri.toString() + ", unimplemented scheme: " + uri.getScheme();
			logger.warn(message);
			throw new FileNotFoundException(message);
		}
		
	}

	private void setMD5(FileGroup fileGroup) throws GridException, FileNotFoundException {

		if (fileGroup.getFileHandles().isEmpty())
			throw new FileNotFoundException("No file handles in file group: " + fileGroup.getId());

		String host = fileGroup.getFileHandles().iterator().next().getFileURI().getHost();

		GridWorkService gws = hostResolver.getGridWorkService(host);

		WorkUnit w = new WorkUnit();
		w.setRegistering(true);
		w.setResultsDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);

		int fileNum = 0;

		for (FileHandle f : fileGroup.getFileHandles()) {
			String fileHost = f.getFileURI().getHost().toString();
			if (!fileHost.equals(host))
				throw new GridAccessException("files must all reside on the same host for calculating MD5 by file group");
			try {
				//TODO: URN resolution
				URL url = f.getFileURI().toURL();
			} catch (MalformedURLException e) {
				String message = "malformed url " + f.getFileURI().toString();
				logger.warn(message);
				throw new FileNotFoundException(message);
			}
			w.addRequiredFile(f);
			w.addCommand("md5sum ${WASPFILE[" + fileNum + "]} | awk '{print $1 \",\" $2}'");
			fileNum++;
		}
		
		GridResult r = gws.execute(w);
		
		// don't sleep on the main thread
		ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
		while (!gws.isFinished(r)) {
			ScheduledFuture<?> md5t = ex.schedule(new Runnable() {
				@Override
				public void run() {

				}
			}, 10, TimeUnit.SECONDS);
			while (!md5t.isDone()) {
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
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			Map<String,String> md5s = new TreeMap<String,String>();
			while (br.ready()) {
				String line = br.readLine();
				String[] md5 = line.split(",");
				md5s.put(StringUtils.chomp(md5[1]), md5[0]);
			}
			
			for (FileHandle f : fileGroup.getFileHandles()) {
				String key = null;
				for (String k : md5s.keySet()) {
					if (k.endsWith(f.getFileURI().getPath().toString())) {
						f.setMd5hash(md5s.get(k));
						fileHandleDao.save(f);
					}
				}
				if (f.getMd5hash() == null) 
					throw new FileNotFoundException("Unable to find MD5 value for: " + f.getFileURI().toString());
				logger.debug(f.getFileURI().toString() + "file registered with MD5: " + f.getMd5hash());
			}
			
		} catch (IOException e) {
			String message = "Unable to obtain stdout: " + e.getLocalizedMessage();
			logger.warn(message);
			throw new GridExecutionException(message);
		}
		
		
	}

	
	/**
	 * Set the MD5 of a FileHandle
	 * this is not properly threaded for doing a large number of files, use setMD5(FileGroup)
	 * 
	 * @param file
	 * @throws GridException
	 * @throws FileNotFoundException
	 */
	private void setMD5(FileHandle file) throws GridException, FileNotFoundException {
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

		logger.debug("checking MD5 of " + file.getFileURI().toString());

		WorkUnit w = new WorkUnit();
		w.setRegistering(true);
		w.setResultsDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		w.addRequiredFile(file);
		w.setCommand("md5sum ${WASPFILE[0]} | awk '{print $1}'");
		GridResult r = gws.execute(w);

		ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
		while (!gws.isFinished(r)) {
			ScheduledFuture<?> md5t = ex.schedule(new Runnable() {
				@Override
				public void run() {

				}
			}, 10, TimeUnit.SECONDS);
			while (!md5t.isDone()) {
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
			file.setMd5hash(StringUtils.chomp(md5));
			logger.debug("file registered with MD5: " + file.getMd5hash());
		} catch (IOException e) {
			String message = "Unable to obtain stdout: " + e.getLocalizedMessage();
			logger.warn(message);
			throw new GridExecutionException(message);
		}
	}

	@Override
	public FileGroup promoteJobDraftFileGroupToJob(Job job, FileGroup filegroup) throws GridUnresolvableHostException, IOException {
		for (FileHandle fh : filegroup.getFileHandles()) {
			URI uri = fh.getFileURI();
			String host = uri.getHost();

			if (!host.equals(fileHost)) {
				String mess = "Job Draft File is located on " + host + ", not the primary file host " + fileHost;
				logger.warn(mess);
				throw new GridUnresolvableHostException(mess);
			}

			GridWorkService gws = hostResolver.getGridWorkService(host);
			GridFileService gfs = hostResolver.getGridWorkService(uri.getHost()).getGridFileService();

			String resultsDir = gws.getTransportConnection().getConfiguredSetting("results.dir");

			String path = uri.getPath();

			String basename = path.substring(path.lastIndexOf('/') + 1);

			String resultPath = resultsDir + "/" + job.getId() + "/submitted/";

			gfs.mkdir(resultPath);
			String resultFile = resultPath + basename;
			gfs.copy(path, resultFile);

			URI newUri = gfs.remoteFileRepresentationToLocalURI(resultFile);

			fh.setFileURI(newUri);
			fileHandleDao.save(fh);

		}
		return fileGroupDao.findById(filegroup.getId());
	}

}
