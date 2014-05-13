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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.persistence.TypedQuery;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.ConfigurableMimeFileTypeMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.dao.FileGroupDao;
import edu.yu.einstein.wasp.dao.FileGroupMetaDao;
import edu.yu.einstein.wasp.dao.FileHandleDao;
import edu.yu.einstein.wasp.dao.FileHandleMetaDao;
import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.JobDraftDao;
import edu.yu.einstein.wasp.dao.JobDraftFileDao;
import edu.yu.einstein.wasp.dao.JobFileDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.exception.FileDownloadException;
import edu.yu.einstein.wasp.exception.FileUploadException;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.PluginException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.filetype.FileTypeAttribute;
import edu.yu.einstein.wasp.filetype.service.FileTypeService;
import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.interfacing.Hyperlink;
import edu.yu.einstein.wasp.interfacing.plugin.FileTypeViewProviding;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileGroupMeta;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileHandleMeta;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftFile;
import edu.yu.einstein.wasp.model.JobFile;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.viewpanel.FileDataTabViewing;


@Service
@Transactional("entityManager")
public class FileServiceImpl extends WaspServiceImpl implements FileService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private JobDraftFileDao jobDraftFileDao;

	@Autowired
	private JobDraftDao jobDraftDao;

	@Autowired
	private SampleService sampleService;
	
	@Autowired
	protected WaspPluginRegistry pluginRegistry;

	@Autowired
	private SampleDao sampleDao;

	@Autowired
	@Qualifier("fileTypeServiceImpl")
	private FileTypeService fileTypeService;

	@Autowired
	private FileGroupDao fileGroupDao;
	
	@Autowired
	private FileGroupMetaDao fileGroupMetaDao;
	
	@Autowired
	private FileHandleMetaDao fileHandleMetaDao;

	@Autowired
	private GridHostResolver hostResolver;
	
	@Autowired
	private JobFileDao jobFileDao;
	
	@Autowired
	private JobDao jobDao;

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

	@Override
	public FileGroup getFileGroup(UUID uuid) throws FileNotFoundException {
		TypedQuery<FileGroup> fgq = fileGroupDao.getEntityManager()
				.createQuery("select f from FileGroup f where f.uuid = :uuid", FileGroup.class)
				.setParameter("uuid", uuid);
		FileGroup fg = fgq.getSingleResult();
		if (fg == null)
			throw new FileNotFoundException("File group represented by " + uuid.toString() + " was not found.");
		return fg;
	}
	
	/**
	 * this has actually been replaced by this.uploadJobDraftFile();
	 * Upload submitted file to a temporary location on the remote host.
	 * {@inheritDoc}
	 */
	@Override
	public FileGroup processUploadedFile(MultipartFile mpFile, JobDraft jobDraft, String description, Random randomNumberGenerator) throws FileUploadException{
		if (isInDemoMode)
			throw new FileUploadException("Cannot perform this action in demo mode");
		int randomNumber = randomNumberGenerator.nextInt(1000000000) + 100;

		String noSpacesFileName = mpFile.getOriginalFilename().replaceAll("\\s+", "_");
		String taggedNoSpacesFileName = randomNumber + "_" + noSpacesFileName;

		File temporaryDirectory = new File(tempDir);

		if (!temporaryDirectory.exists()) {
			try {
				temporaryDirectory.mkdir();
			} catch (Exception e) {
				throw new FileUploadException("FileHandle upload failure trying to create '" + tempDir + "': " + e.getMessage());
			}
		}
		
		File localFile;
		try {
			localFile = File.createTempFile("wasp.", ".tmp", temporaryDirectory);
		} catch (IOException e) {
			String mess = "Unable to create local temporary file: " + e.getLocalizedMessage();
			logger.warn(mess);
			e.printStackTrace();
			throw new FileUploadException(mess);
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

		String remoteDir = draftDir + "/" + jobDraft.getId() + "/";
		String remoteFile;
		try {
			if(!gfs.exists(remoteDir)){
				gfs.mkdir(remoteDir);
			}
			remoteFile = remoteDir + "/" + taggedNoSpacesFileName;

			if (gfs.exists(remoteFile)) {
				taggedNoSpacesFileName = retGroup.getId() + "__" + taggedNoSpacesFileName;
				remoteFile = remoteDir + "/" + taggedNoSpacesFileName;
			}
		} catch (IOException e) {
			String mess = "problem creating resources on remote host " + gws.getTransportConnection().getHostName();
			logger.warn(mess);
			e.printStackTrace();
			throw new FileUploadException(mess);
		}

		file.setFileName(mpFile.getOriginalFilename());

		try {
			OutputStream tmpFile = new FileOutputStream(localFile);

			int read = 0;
			byte[] bytes = new byte[1024];

			InputStream mpFileInputStream = mpFile.getInputStream();
			while ((read = mpFileInputStream.read(bytes)) != -1) {
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
		// probably not.  Figure out where to put or whether to
		// automatically determine mime type.

		file.setFileURI(gfs.remoteFileRepresentationToLocalURI(remoteFile));

		try {
			gfs.put(localFile, remoteFile);			
			registerWithoutMD5(retGroup);
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
		fileGroupDao.save(retGroup);
		
		return retGroup;
		
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
		return fileTypeService.getFileTypeDao().getFileTypes();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<FileGroup> getFilesByType(FileType fileType) {
		Assert.assertParameterNotNull(fileType, "must provide a fileType");
		Assert.assertParameterNotNull(fileType.getId(), "fileType has no valid fileTypeId");
		Map<String, Integer> m = new HashMap<String, Integer>();
		m.put("fileTypeId", fileType.getId());
		Set<FileGroup> result = new HashSet<FileGroup>();
		result.addAll(fileGroupDao.findByMap(m));
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<FileGroup> getFilesByType(FileType fileType, Set<? extends FileTypeAttribute> attributes, boolean hasOnlyAttributesSpecified) {
		Set<FileGroup> fgsWithAttributes = new LinkedHashSet<>();
		for (FileGroup fg : getFilesByType(fileType))
			if (hasOnlyAttributesSpecified){
				if (fileTypeService.hasOnlyAttributes(fg, attributes))
					fgsWithAttributes.add(fg);
			} else {
				if (fileTypeService.hasAttributes(fg, attributes))
					fgsWithAttributes.add(fg);
			}
		return fgsWithAttributes;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public Set<FileGroup> getFilesForLibrary(Sample library) throws SampleTypeException {
		Assert.assertParameterNotNull(library, "must provide a library");
		if (!sampleService.isLibrary(library))
			throw new SampleTypeException("sample is not of type library");
		Sample lib = sampleService.getSampleById(library.getId());
		return lib.getFileGroups();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public Set<FileGroup> getFilesForLibraryByType(Sample library, FileType fileType, Set<? extends FileTypeAttribute> attributes, boolean hasOnlyAttributesSpecified) throws SampleTypeException {
		Set<FileGroup> fgsWithAttributes = new LinkedHashSet<>();
		for (FileGroup fg : getFilesForLibraryByType(library, fileType))
			if (hasOnlyAttributesSpecified){
				if (fileTypeService.hasOnlyAttributes(fg, attributes))
					fgsWithAttributes.add(fg);
			} else {
				if (fileTypeService.hasAttributes(fg, attributes))
					fgsWithAttributes.add(fg);
			}
		return fgsWithAttributes;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public Set<FileGroup> getFilesForLibraryByType(Sample library, FileType fileType) throws SampleTypeException {
		Assert.assertParameterNotNull(fileType, "must provide a fileType");
		Assert.assertParameterNotNull(fileType.getId(), "fileType has no valid fileTypeId");
		Map<FileType, Set<FileGroup>> filesByType = getFilesForLibraryMappedToFileType(library);
		if (!filesByType.containsKey(fileType))
			return new HashSet<FileGroup>();
		return filesByType.get(fileType);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public Map<FileType, Set<FileGroup>> getFilesForLibraryMappedToFileType(Sample library) throws SampleTypeException {
		Assert.assertParameterNotNull(library, "must provide a library");
		if (!sampleService.isLibrary(library))
			throw new SampleTypeException("sample is not of type library");
		Sample s = sampleDao.findById(library.getId());
		Map<FileType, Set<FileGroup>> filesByType = new HashMap<FileType, Set<FileGroup>>();
		for (FileGroup fg : s.getFileGroups()) {
			FileType ft = fg.getFileType();
			if (!filesByType.containsKey(ft))
				filesByType.put(ft, new LinkedHashSet<FileGroup>());
			filesByType.get(ft).add(fg);
		}
		return filesByType;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public Set<FileGroup> getFilesForCellLibrary(SampleSource cellLibrary) {
		return fileGroupDao.getFilesForCellLibrary(cellLibrary);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public Set<FileGroup> getActiveFilesForCellLibrary(SampleSource cellLibrary) {
		return fileGroupDao.getActiveFilesForCellLibrary(cellLibrary);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public Set<FileGroup> getFilesForSample(Sample sample) {
		return fileGroupDao.getFilesForSample(sample);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	*/
	@Override
	public Set<FileGroup> getActiveFilesForSample(Sample sample) {
		return fileGroupDao.getActiveFilesForSample(sample);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public Set<FileGroup> getFilesForCellLibraryByType(SampleSource cellLibrary, FileType fileType) {
		Assert.assertParameterNotNull(cellLibrary, "must provide a cellLibrary");
		Assert.assertParameterNotNull(fileType, "must provide a fileType");
		Assert.assertParameterNotNull(fileType.getId(), "fileType has no valid fileTypeId");
		return fileGroupDao.getActiveFilesForCellLibraryByType(cellLibrary, fileType);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public Set<FileGroup> getFilesForCellLibraryByType(SampleSource cellLibrary, FileType fileType, Set<? extends FileTypeAttribute> attributes, boolean hasOnlyAttributesSpecified) {
		Set<FileGroup> fgsWithAttributes = new LinkedHashSet<>();
		for (FileGroup fg : getFilesForCellLibraryByType(cellLibrary, fileType)){
			logger.debug("FileGroup id= " + fg.getId() + " is associated with cell library id=" + cellLibrary.getId());
			if (hasOnlyAttributesSpecified){
				if (fileTypeService.hasOnlyAttributes(fg, attributes)){
					logger.debug("FileGroup id= " + fg.getId() + " matches attribute requirements ");
					fgsWithAttributes.add(fg);
				} else {
					logger.debug("FileGroup id= " + fg.getId() + " does not match attribute requirements ");
				}
			} else {
				if (fileTypeService.hasAttributes(fg, attributes)){
					logger.debug("FileGroup id= " + fg.getId() + " matches attribute requirements ");
					fgsWithAttributes.add(fg);
				} else {
					logger.debug("FileGroup id= " + fg.getId() + " does not match attribute requirements ");
				}
			}
		}		
		return fgsWithAttributes;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public Set<FileGroup> getFilesForCellLibraryByType(Sample cell, Sample library, FileType fileType) throws SampleTypeException {
		Assert.assertParameterNotNull(cell, "must provide a cell");
		if (!sampleService.isCell(cell))
			throw new SampleTypeException("sample is not of type cell");
		Assert.assertParameterNotNull(library, "must provide a library");
		if (!sampleService.isLibrary(library))
			throw new SampleTypeException("sample is not of type library");
		return fileGroupDao.getActiveFilesForCellLibraryByType(sampleService.getCellLibrary(cell, library), fileType);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public Set<FileGroup> getFilesForCellLibraryByType(Sample cell, Sample library, FileType fileType, Set<? extends FileTypeAttribute> attributes, boolean hasOnlyAttributesProvided) throws SampleTypeException {
		return getFilesForCellLibraryByType(sampleService.getCellLibrary(cell, library), fileType, attributes, hasOnlyAttributesProvided);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public Map<FileType, Set<FileGroup>> getFilesForCellLibraryMappedToFileType(Sample cell, Sample library) throws SampleTypeException {
		Assert.assertParameterNotNull(cell, "must provide a cell");
		if (!sampleService.isCell(cell))
			throw new SampleTypeException("sample is not of type cell");
		Assert.assertParameterNotNull(library, "must provide a library");
		if (!sampleService.isLibrary(library))
			throw new SampleTypeException("sample is not of type library");
		
		SampleSource ss = sampleService.getCellLibrary(cell, library);
		Map<FileType, Set<FileGroup>> filesByType = new HashMap<FileType, Set<FileGroup>>();
		for (FileGroup fg : ss.getFileGroups()) {
			FileType ft = fg.getFileType();
			if (!filesByType.containsKey(ft))
				filesByType.put(ft, new HashSet<FileGroup>());
			filesByType.get(ft).add(fg);
		}
		return filesByType;
	}

	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public Set<FileGroup> getFilesForPlatformUnit(Sample platformUnit) throws SampleTypeException {
		Assert.assertParameterNotNull(platformUnit, "must provide a platformUnit");
		if (!sampleService.isPlatformUnit(platformUnit))
			throw new SampleTypeException("sample is not of type platformUnit");
		Sample lib = sampleService.getSampleById(platformUnit.getId());
		return lib.getFileGroups();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public Set<FileGroup> getFilesForPlatformUnitByType(Sample platformUnit, FileType fileType) throws SampleTypeException {
		Assert.assertParameterNotNull(platformUnit, "must provide a platformUnit");
		Assert.assertParameterNotNull(fileType, "must provide a fileType");
		Assert.assertParameterNotNull(fileType.getId(), "fileType has no valid fileTypeId");
		Map<FileType, Set<FileGroup>> filesByType = getFilesForPlatformUnitMappedToFileType(platformUnit);
		if (!filesByType.containsKey(fileType))
			return new LinkedHashSet<FileGroup>();
		return filesByType.get(fileType);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public Set<FileGroup> getFilesForPlatformUnitByType(Sample platformUnit, FileType fileType, Set<? extends FileTypeAttribute> attributes, boolean hasOnlyAttributesSpecified) throws SampleTypeException {
		Set<FileGroup> fgsWithAttributes = new LinkedHashSet<>();
		for (FileGroup fg : getFilesForPlatformUnitByType(platformUnit, fileType))
			if (hasOnlyAttributesSpecified){
				if (fileTypeService.hasOnlyAttributes(fg, attributes))
					fgsWithAttributes.add(fg);
			} else {
				if (fileTypeService.hasAttributes(fg, attributes))
					fgsWithAttributes.add(fg);
			}
		return fgsWithAttributes;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public Map<FileType, Set<FileGroup>> getFilesForPlatformUnitMappedToFileType(Sample platformUnit) throws SampleTypeException {
		Assert.assertParameterNotNull(platformUnit, "must provide a platformUnit");
		if (!sampleService.isPlatformUnit(platformUnit))
			throw new SampleTypeException("sample is not of type platformUnit");
		Sample s = sampleDao.findById(platformUnit.getId());
		Map<FileType, Set<FileGroup>> filesByType = new HashMap<FileType, Set<FileGroup>>();
		for (FileGroup fg : s.getFileGroups()) {
			FileType ft = fg.getFileType();
			if (!filesByType.containsKey(ft))
				filesByType.put(ft, new HashSet<FileGroup>());
			filesByType.get(ft).add(fg);
		}
		return filesByType;
	}

	@Override
	public FileHandle addFile(FileHandle file) {
		return fileHandleDao.save(file);
	}

	@Override
	public FileGroup addFileGroup(FileGroup group) {
		return fileGroupDao.save(group);
	}

	@Override
	public void setSampleFile(FileGroup group, Sample sample) {
		Sample s = sampleService.getSampleById(sample.getId());
		s.getFileGroups().add(group);
		sampleDao.save(s);
	}
	
	@Override
	public void setSampleSourceFile(FileGroup group, SampleSource sampleSource) {
		SampleSource s = sampleService.getSampleSourceDao().getById(sampleSource.getId());
		s.getFileGroups().add(group);
		sampleService.getSampleSourceDao().save(s);
	}

	@Override
	public FileType getFileType(String iname) {
		return fileTypeService.getFileTypeDao().getFileTypeByIName(iname);
	}

	@Override
	public FileType getFileType(Integer id) {
		return fileTypeService.getFileTypeDao().getById(id);
	}

	/**
	 * Take a new (isActive == 0) entity managed file and register it.
	 * 
	 * @throws FileNotFoundException
	 * @throws GridUnresolvableHostException
	 */
	@Override
	@Deprecated
	public void register(FileHandle file) throws FileNotFoundException, GridException {
		file = fileHandleDao.merge(file);

		validateFile(file);

		logger.debug("attempting to register " + file.getFileURI().toString());
		setMD5(file);
		fileHandleDao.save(file);
	}

	private enum Md5 { YES, NO };
	
	@Override
	public void register(FileGroup group) throws FileNotFoundException, GridException {
		register(group, Md5.YES);
	}
	
	@Override
	public void registerWithoutMD5(FileGroup group) throws FileNotFoundException, GridException {
		register(group, Md5.NO);
	}
	
	public void register(FileGroup group, Md5 md5) throws FileNotFoundException, GridException {
		group = fileGroupDao.merge(group);
		for (FileHandle f : group.getFileHandles()) {
			validateFile(f);
		}
		logger.debug("attempting to register FileGroup: " + group.getId());

		if (md5.equals(Md5.YES))
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

		// use task array to submit in one batch
		WorkUnit w = new WorkUnit();
		w.setRegistering(true);
		w.setSecureResults(false);
		w.setResultsDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		w.setMode(ExecutionMode.TASK_ARRAY);

		int numFiles = 0;

		Set<FileHandle> fileH = new LinkedHashSet<FileHandle>();
		fileH.addAll(fileGroup.getFileHandles());

		for (FileHandle f : fileH) {
			String fileHost = f.getFileURI().getHost().toString();
			if (!fileHost.equals(host))
				throw new GridAccessException("files must all reside on the same host for calculating MD5 by file group");

			numFiles++;
			w.addRequiredFile(f);

			w.setCommand("md5sum ${WASPFILE[WASP_TASK_ID]} | awk '{print $1}' > $WASP_TASK_OUTPUT");

			try {
				// TODO: URN resolution
				URL url = f.getFileURI().toURL();
			} catch (MalformedURLException e) {
				String message = "malformed url " + f.getFileURI().toString();
				logger.warn(message);
				throw new FileNotFoundException(message);
			}
			logger.debug("added " + f.getFileURI() + " to get MD5");
		}

		w.setNumberOfTasks(numFiles);

		GridWorkService gws = hostResolver.getGridWorkService(host);
		GridResult r = gws.execute(w);

		logger.debug("waiting for file registration");
		ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
		while (!gws.isFinished(r)) {
			ScheduledFuture<?> md5t = ex.schedule(new Runnable() {
				@Override
				public void run() {
				}
			}, 20, TimeUnit.SECONDS);
			while (!md5t.isDone()) {
				// not done
			}
		}
		ex.shutdownNow();
		logger.debug("registered, getting results");
		
		try {
			Map<Integer, String> output = gws.getMappedTaskOutput(r);

			Iterator<FileHandle> fhi = fileH.iterator();
			for (int rec=0; rec < fileH.size(); rec++) {
			    FileHandle f = fhi.next();
			    String md5 = StringUtils.chomp(output.get(rec));
			    if (md5 == null) {
			        logger.error("unable to find MD5 result for " + f.getFileName());
			        continue;
			    }
			    logger.debug("MD5: " + rec + " : " + md5 + ":" + f.getFileName());
			    f.setMd5hash(md5);
			    fileHandleDao.save(f);
			    logger.debug("file registered with MD5: " + f.getMd5hash());
			}
		} catch (IOException e) {
			throw new GridException("unable to read output of task array", e);
		}
		
	}

	/**
	 * 
	 * @param file
	 * @throws GridException
	 * @throws FileNotFoundException
	 */
	@Deprecated
	private MD5Result setMD5(FileHandle file) throws GridException, FileNotFoundException {
		URL url;
		logger.debug("calculating MD5 for: " + file.getFileURI());
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
		logger.debug("returning MD5 result");
		return new MD5Result(file, gws, r);

	}

	@Deprecated
	private class MD5Result {

		FileHandle file;
		GridWorkService gws;
		GridResult r;

		public MD5Result(FileHandle file, GridWorkService gws, GridResult r) {
			this.file = file;
			this.gws = gws;
			this.r = r;
		}

		public boolean isRegistered() throws GridException {
			boolean running = gws.isFinished(r);
			if (running)
				return false;
			try {
				InputStream is = gws.readResultStdOut(r);
				StringWriter sw = new StringWriter();
				IOUtils.copy(is, sw);
				String md5 = sw.toString();
				file.setMd5hash(StringUtils.chomp(md5));
				fileHandleDao.save(file);
				logger.debug("file registered with MD5: " + file.getMd5hash());
			} catch (IOException e) {
				String message = "Unable to obtain stdout: " + e.getLocalizedMessage();
				logger.warn(message);
				throw new GridExecutionException(message);
			}
			return true;
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

		JobFile jf = new JobFile();
		jf.setFileGroup(filegroup);
		jf.setIsActive(1);
		jobFileDao.save(jf);
		
		job.getJobFile().add(jf);
		jobDao.save(job);
		
		return fileGroupDao.findById(filegroup.getId());
	}

	@Override
	public FileHandle getFileHandle(UUID uuid) throws FileNotFoundException {
		TypedQuery<FileHandle> fhq = fileHandleDao.getEntityManager()
				.createQuery("select f from FileHandle f where f.uuid = :uuid", FileHandle.class)
				.setParameter("uuid", uuid);
		FileHandle fh = fhq.getSingleResult();
		if (fh == null)
			throw new FileNotFoundException("File represented by " + uuid.toString() + " was not found.");
		return fh;
	}
	
	@Override
	public Map<String, Hyperlink> getFileDetailsByFileType(FileGroup filegroup) {
		FileType ft = filegroup.getFileType();
		String area = ft.getIName();
		List<FileTypeViewProviding> plugins = pluginRegistry.getPluginsHandlingArea(area, FileTypeViewProviding.class);
		// we expect one (and ONLY one) plugin to handle the area otherwise we do not know which one to show so programming defensively:
		if (plugins.size() == 0)
			throw new PluginException("No plugins found for area=" + area + " with class=SequencingViewProviding");
		if (plugins.size() > 1)
			throw new PluginException("More than one plugin found for area=" + area + " with class=SequencingViewProviding");
		return plugins.get(0).getFileDetails(filegroup.getId());
	}
	
	@Override
	public List<FileDataTabViewing> getTabViewProvidingPluginsByFileGroup(FileGroup fileGroup) {
		String area = fileGroup.getFileType().getIName();
		return pluginRegistry.getPluginsHandlingArea(area, FileDataTabViewing.class);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeFileGroupFromRemoteServerAndMarkDeleted(FileGroup fileGroup) throws Exception{
		Assert.assertTrue(fileGroup.isDeleted() == 0, "Filegroup is already deleted");
		for (FileHandle fh : fileGroup.getFileHandles()){
			if (!fh.isDeleted()){
				String path = fh.getFileURI().getPath();
				GridWorkService gws = hostResolver.getGridWorkService(fileHost);
				GridFileService gfs = gws.getGridFileService();
				if (gfs.exists(path))
					gfs.delete(path);
				fh.setDeleted(true);	
			}
		}
		fileGroup.setDeleted(1);
		fileGroup.setIsActive(0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeFileHandleFromRemoteServerAndMarkDeleted(FileHandle fileHandle) throws Exception{
		// delete remote file
		String path = fileHandle.getFileURI().getPath();
		GridWorkService gws = hostResolver.getGridWorkService(fileHost);
		GridFileService gfs = gws.getGridFileService();
		if (gfs.exists(path))
			gfs.delete(path);
		
		// check if any fileGroups need to be marked deleted (this was the last non-deleted file in the fileGroup)
		for (FileGroup fileGroup : fileHandle.getFileGroup()){
			boolean nonDeletedFhExists = false;
			for (FileHandle fh : fileGroup.getFileHandles())
				if (!fh.isDeleted() && !fh.equals(fileHandle)){
					nonDeletedFhExists = false;
					break;
				}
			if (!nonDeletedFhExists){
				fileGroup.setDeleted(1);
				fileGroup.setIsActive(0);
			}
		}
		
		fileHandle.setDeleted(true);
	}

	@Override
	public void removeUploadedFileFromJobDraft(Integer jobDraftId, Integer fileGroupId, Integer fileHandleId) throws FileNotFoundException{

		JobDraft jobDraft = jobDraftDao.findById(jobDraftId);
		FileHandle fileHandle = fileHandleDao.findById(fileHandleId);
		FileGroup fileGroup = fileGroupDao.findById(fileGroupId);
		JobDraftFile jobDraftFile = null;
		String file_path = null;
		
		if(jobDraft==null || jobDraft.getId()==null){
			throw new FileNotFoundException("JobDraft  " + jobDraftId + " unexpectedly not found");
		}
		else if(fileHandle==null || fileHandle.getId()==null){
			throw new FileNotFoundException("FileHandle  " + fileHandleId + " unexpectedly not found");
		}
		else if(fileGroup==null || fileGroup.getId()==null){
			throw new FileNotFoundException("FileGroup  " + fileGroupId + " unexpectedly not found");
		}
		
		for(JobDraftFile jdf : jobDraft.getJobDraftFile()){
			if(jdf.getFileGroup().getId().intValue() == fileGroupId.intValue()){
				jobDraftFile = jdf;
				break;
			}
		}
		if(jobDraftFile==null){
			throw new FileNotFoundException("No jobDraftFile found in db associated with fileGroupId  " + fileGroupId);
		}
		
		Set<FileHandle> fileHandleList = fileGroup.getFileHandles();
		if(fileHandleList.size()==0){
			throw new FileNotFoundException("List of fileHandles in filegroup " + fileGroupId + " is unexpectedly empty");
		}
		else if(!fileHandleList.contains(fileHandle)){
			throw new FileNotFoundException("List of fileHandles in filegroup " + fileGroupId + " unexpectedly does NOT contain fileHandle " + fileHandleId);
		}

		file_path = fileHandle.getFileURI().getPath();//example of a file_path: /wasp/draft/85/783768138_bioanalyzer_test.txt

		fileHandleList.remove(fileHandle);
		fileGroupDao.save(fileGroup);//this removes the db entry in groupfile, but not db entry in filehandle (which is table file)
		fileHandleDao.remove(fileHandle);//this removes the db entry for the filehandle (in table file)

		if(fileHandleList.size()==0){//will be the case for uploaded files			
			jobDraftFileDao.remove(jobDraftFile);//remove the db jobDraftFile entry
			fileGroupDao.remove(fileGroup);//remove the db fileGroup entry
		}
		
		//delete the file on the remote server. don't really care if this works or not, so do not re-throw any exception
		GridWorkService gws;
		GridFileService gfs;
		try {
			gws = hostResolver.getGridWorkService(fileHost);
			gfs = gws.getGridFileService();
			if(gfs.exists(file_path)){
				gfs.delete(file_path);
			}
		} catch (Exception e) {
			String mess = "Unable to resolve remote host";
			logger.warn(mess);
			e.printStackTrace();
		}	
		
	}

	@Override
	@Transactional
	public void uploadJobDraftFile(MultipartFile mpFile, JobDraft jobDraft, String fileDescription, Random randomNumberGenerator) throws FileUploadException{
		if (isInDemoMode)
			throw new FileUploadException("Cannot perform this action in demo mode");
		try{
			FileGroup fileGroup = this.uploadFile(mpFile.getOriginalFilename(), mpFile.getInputStream(), jobDraft.getId(), fileDescription, randomNumberGenerator, "draft.dir", "");
			this.linkFileGroupWithJobDraft(fileGroup, jobDraft);
		}catch(Exception e){
			throw new FileUploadException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public void uploadJobFile(MultipartFile mpFile, Job job, String fileDescription, Random randomNumberGenerator) throws FileUploadException{
		if (isInDemoMode)
			throw new FileUploadException("Cannot perform this action in demo mode");
		try{
			FileGroup fileGroup = this.uploadFile(mpFile.getOriginalFilename(), mpFile.getInputStream(), job.getId(), fileDescription, randomNumberGenerator, "results.dir", "submitted");
			this.linkFileGroupWithJob(fileGroup, job);//this should really be in the job service, not fileservice
		}catch(Exception e){
			throw new FileUploadException(e.getMessage());
		} 
	}

	@Override
	@Transactional
	public FileGroup uploadFileAndReturnFileGroup(MultipartFile mpFile, Job job, String fileDescription, Random randomNumberGenerator) throws FileUploadException{
		if (isInDemoMode)
			throw new FileUploadException("Cannot perform this action in demo mode");
		try{
			FileGroup fileGroup = this.uploadFile(mpFile.getOriginalFilename(), mpFile.getInputStream(), job.getId(), fileDescription, randomNumberGenerator, "results.dir", "submitted");
			return fileGroup;
		}catch(Exception e){
			throw new FileUploadException(e.getMessage());
		}
	}

	private FileGroup uploadFile(String originalFileName, InputStream inputStream, Integer id, String fileDescription, Random randomNumberGenerator, String targetDir, String additionalSubJobDir) throws FileUploadException{
		
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

		//////////String draftDir = gws.getTransportConnection().getConfiguredSetting("draft.dir");
		String partialDir = gws.getTransportConnection().getConfiguredSetting(targetDir);
		
		if (partialDir == null) {
			String mess = "Attempted to configure for file copy to " + fileHost + ", but hostname.settings." + targetDir + " has not been set.";
			logger.warn(mess);
			throw new FileUploadException(mess);
		}

		//String remoteDir = partialDir + "/" + id + "/";
		String remoteDir;
		if(additionalSubJobDir==null || additionalSubJobDir.isEmpty()){
			remoteDir = partialDir + "/" + id + "/"; 
		}
		else{
			remoteDir = partialDir + "/" + id + "/" + additionalSubJobDir + "/";
		}

		try {
			if(!gfs.exists(remoteDir)){
				gfs.mkdir(remoteDir);
			}
		} catch (IOException e) {
			String mess = "Problem creating resources (remote directory:"+remoteDir+") on remote host " + gws.getTransportConnection().getHostName();
			logger.warn(mess);
			e.printStackTrace();
			throw new FileUploadException(mess);
		}

		File localFile = this.createTempFile();

		try {
			OutputStream tmpFile = new FileOutputStream(localFile);

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				tmpFile.write(bytes, 0, read);
			}

			inputStream.close();
			tmpFile.flush();
			tmpFile.close();
		} catch (IOException e) {
			String mess = "Unable to generate local temporary file with contents of multipart file";
			logger.warn(mess);
			e.printStackTrace();
			localFile.delete();
			throw new FileUploadException(mess);
		}
		
		int randomNumber = randomNumberGenerator.nextInt(1000000000) + 100;
		//String noSpacesFileName = mpFile.getOriginalFilename().replaceAll("\\s+", "_");
		String noSpacesFileName = originalFileName.replaceAll("\\s+", "_");
		String taggedNoSpacesFileName = randomNumber + "_" + noSpacesFileName;

		String remoteFile = remoteDir + "/" + taggedNoSpacesFileName;

		FileHandle file = new FileHandle();
		file.setFileName(taggedNoSpacesFileName);
		file.setFileURI(gfs.remoteFileRepresentationToLocalURI(remoteFile));
		file = fileHandleDao.save(file);
		FileGroup retGroup = new FileGroup();
		retGroup.addFileHandle(file);
		retGroup.setDescription(fileDescription);
		retGroup = fileGroupDao.save(retGroup);	

		// TODO: Determine file type and set on the group.
		// probably not.  Figure out where to put or whether to
		// automatically determine mime type.

		try {
			gfs.put(localFile, remoteFile);			
			registerWithoutMD5(retGroup);
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
		fileGroupDao.save(retGroup);
		
		return retGroup;
	}	
	
	private JobFile linkFileGroupWithJob(FileGroup filegroup, Job job) {
		JobFile jobFile = new JobFile();
		jobFile.setFileGroup(filegroup);
		jobFile.setJob(job);
		return jobFileDao.save(jobFile);
	}

	@Override
	public void copyFileHandleToOutputStream(FileHandle fileHandle, OutputStream os) throws FileUploadException, FileDownloadException, FileNotFoundException, GridException{
		
		if(tempDir == null){
			String mess = "Temporary directory on local host has not been configured!  Please set \"wasp.temporary.dir\" in wasp-config.";
			logger.warn(mess);
			throw new FileDownloadException(mess);
		}

  		URI uri = fileHandle.getFileURI();
  		if(uri==null){
  			String mess = "FileHandle's URI is null for fileHandleId = " + fileHandle.getId();
  			logger.debug(mess);
  			throw new FileDownloadException(mess);
  		}
  		
		GridWorkService gws;
		GridFileService gfs;
		try {
			gws = hostResolver.getGridWorkService(uri.getHost());
			gfs = gws.getGridFileService();
		} catch (GridUnresolvableHostException e) {
			String mess = "Unable to resolve remote host";
			logger.warn(mess);
			e.printStackTrace();
			throw new GridException(mess);
		}

		try{
			if(!gfs.exists(uri.getPath())){
				String mess = "File not found on remote host";
				logger.warn(mess);
				throw new FileNotFoundException(mess);
			}
		}catch(Exception e){
			String mess = "Unable to query remote location regarding existence of file existence on remote host";
			logger.warn(mess);
			throw new FileNotFoundException(mess);
		}
		
		File temporaryDirectory = new File(tempDir);

		if (!temporaryDirectory.exists()) {
			try {
				temporaryDirectory.mkdir();
			} catch (Exception e) {
				String mess = "FileHandle download failure trying to create '" + tempDir + "': " + e.getMessage();
				logger.warn(mess);
				throw new FileUploadException(mess);
			}
		}
		
		File localFile;
		try {
			localFile = File.createTempFile("wasp.", ".tmp", temporaryDirectory);
		} catch (IOException e) {
			String mess = "Unable to create local temporary file: " + e.getLocalizedMessage();
			logger.warn(mess);
			e.printStackTrace();
			throw new FileUploadException(mess);
		}

		try {
			gfs.get(uri.getPath(), localFile);//should maybe check md5??
		}catch (IOException e) {
			String mess = "Unable to copy remote file to local temporary file: " + e.getLocalizedMessage();
			logger.warn(mess);
			localFile.delete();
			throw new FileDownloadException(mess);
		}
  		
  		try {
    	      // get your file as InputStream
    	      InputStream is = new FileInputStream(localFile);
    	      // copy it to response's OutputStream
    	      IOUtils.copy(is, os);
    	      is.close();
    	} catch (Exception ex) {
    	      logger.debug("Error writing file to output stream. FileHandleId = '" + fileHandle.getId() + "'");
    	}finally{
    		localFile.delete();
    	}
	}
	
	@Override
	public void copyFileHandlesInFileGroupToOutputStream(FileGroup fileGroup, OutputStream os) throws FileDownloadException, FileNotFoundException, GridException, FileUploadException{
		
		if(tempDir == null){
			String mess = "Temporary directory on local host has not been configured!  Please set \"wasp.temporary.dir\" in wasp-config.";
			logger.warn(mess);
			throw new FileDownloadException(mess);
		}

		Set<FileHandle> fileHandleSet = fileGroup.getFileHandles();

		if(fileHandleSet.size()==0){
			String mess = "FileGroup with Id of " +fileGroup.getId()+ " contains no file handles.";
			logger.warn(mess);
			throw new FileDownloadException(mess);
		}
		
		List<File> copiedTemporaryFileList = new ArrayList<File>();
		List<FileHandle> fileHandlesOfCopiedFilesList = new ArrayList<FileHandle>();

		GridWorkService gws = null;
		GridFileService gfs = null;
		String currentHost = null;
		File temporaryDirectory = null;

		for(FileHandle fileHandle : fileHandleSet){
			
	  		URI uri = fileHandle.getFileURI();
	  		if(uri==null){
	  			String mess = "FileHandle's URI is null for fileHandleId = " + fileHandle.getId();
	  			logger.debug(mess);
	  			throw new FileDownloadException(mess);
	  		}
	  		
	  		if(gws == null || gfs == null || (gws != null && gfs != null && currentHost != null && !currentHost.equals(uri.getHost()))){
				try {
					gws = hostResolver.getGridWorkService(uri.getHost());
					gfs = gws.getGridFileService();
					currentHost = uri.getHost();
				} catch (GridUnresolvableHostException e) {
					String mess = "Unable to resolve remote host";
					logger.warn(mess);
					e.printStackTrace();
					throw new GridException(mess);
				}
	  		}
	  		
			try{
				if(!gfs.exists(uri.getPath())){
					String mess = "File not found on remote host";
					logger.warn(mess);
					throw new FileNotFoundException(mess);
				}
			}catch(Exception e){
				String mess = "Unable to query remote location regarding existence of file existence on remote host";
				logger.warn(mess);
				throw new FileNotFoundException(mess);
			}
			
			temporaryDirectory = new File(tempDir);
	
			if (!temporaryDirectory.exists()) {
				try {
					temporaryDirectory.mkdir();
				} catch (Exception e) {
					String mess = "FileHandle download failure trying to create '" + tempDir + "': " + e.getMessage();
					logger.warn(mess);
					throw new FileUploadException(mess);
				}
			}
			
			File localFile;
			try {
				localFile = File.createTempFile("wasp.", ".tmp", temporaryDirectory);//guaranteed to have unique name
			} catch (IOException e) {
				String mess = "Unable to create local temporary file: " + e.getLocalizedMessage();
				logger.warn(mess);
				e.printStackTrace();
				for(File f : copiedTemporaryFileList){
	    			f.delete();
	    		}
				throw new FileUploadException(mess);
			}
	
			try {//copy the remote file to tmp location
				gfs.get(uri.getPath(), localFile);//should maybe check md5??
				copiedTemporaryFileList.add(localFile);
				fileHandlesOfCopiedFilesList.add(fileHandle);
			}catch (IOException e) {
				String mess = "Unable to copy remote file to local temporary file: " + e.getLocalizedMessage();
				logger.warn(mess);
				for(File f : copiedTemporaryFileList){
	    			f.delete();
	    		}
				throw new FileDownloadException(mess);
			}
		}//end of the for(FileHandle fh: Set	) end of copying the remote files to tmp location on server and captured those file's fileHandles

		if(copiedTemporaryFileList.size()!=fileHandlesOfCopiedFilesList.size() || copiedTemporaryFileList.size()!=fileHandleSet.size()){
			for(File f : copiedTemporaryFileList){
				f.delete();
			}
			throw new FileDownloadException("file number mismatch");
		}

		if(copiedTemporaryFileList.size()==1){		
			try {
			      // get your file as InputStream
			      InputStream is = new FileInputStream(copiedTemporaryFileList.get(0));
			      // copy it to response's OutputStream
			      IOUtils.copy(is, os);
			      is.close();
			} catch (Exception e) {
				String mess = "Error writing file to output stream. FileHandleId = '" + fileHandlesOfCopiedFilesList.get(0).getId() + "': " + e.getLocalizedMessage();
				logger.warn(mess);
				throw new FileDownloadException(mess);
			}finally{//delete files either way
				for(File f : copiedTemporaryFileList){
					f.delete();
				}
			}
		}
		else{
			//more than one file in FileGroup, so zip them up and pump zipped file through the response.os stream
			//code based on (from web):  http://www.java-examples.com/create-zip-file-multiple-files-using-zipoutputstream-example

			File zipTempFile;
			try {//create the empty zipTempFile
				zipTempFile = File.createTempFile("wasp.", ".tmp", temporaryDirectory);
			} catch (IOException e) {
				String mess = "Unable to create local temporary zip file: " + e.getLocalizedMessage();
				logger.warn(mess);
				e.printStackTrace();
				for(File f : copiedTemporaryFileList){
					f.delete();
				}
				throw new FileUploadException(mess);
			}
			
			 //create object of FileOutputStream
	        FileOutputStream fout = new FileOutputStream(zipTempFile);        
	        //create object of ZipOutputStream from FileOutputStream
	        ZipOutputStream zout = new ZipOutputStream(fout);
			
	        for(int i = 0; i < copiedTemporaryFileList.size(); i++){
	        	
	        	//create object of FileInputStream for source file
	            FileInputStream fin = new FileInputStream(copiedTemporaryFileList.get(i));
	            /*
	             * To begin writing ZipEntry in the zip file, use
	             *
	             * void putNextEntry(ZipEntry entry)
	             * method of ZipOutputStream class.
	             *
	             * This method begins writing a new Zip entry to
	             * the zip file and positions the stream to the start
	             * of the entry data.
	             */
	
	            try{
	            	zout.putNextEntry(new ZipEntry(fileHandlesOfCopiedFilesList.get(i).getFileName()));
	            	IOUtils.copy(fin, zout);
	            	fin.close();
	  		      	zout.closeEntry();
	            }catch(Exception e){
	            	String mess = "Unable to create or copy or close zip entry " + i + ": " + e.getLocalizedMessage();
	    			logger.warn(mess);
	    			e.printStackTrace();
	    			for(File f : copiedTemporaryFileList){
	    				f.delete();
	    			}
	    			zipTempFile.delete();
	    			throw new FileUploadException(mess);
	            }
	        }//end of for(int i = 0; i < copiedTemporaryFileList.size(); i++)
	        
	        try{
	        	zout.close();
	        }catch(Exception e){
	        	String mess = "Unable to close zout: " + e.getLocalizedMessage();
				logger.warn(mess);
				e.printStackTrace();
				zipTempFile.delete();
				throw new FileUploadException(mess);
	        }finally{
	        	for(File f : copiedTemporaryFileList){
					f.delete();//these can now be deleted
				}
	        }
	        
	        
	        try{
	        	FileInputStream zin = new FileInputStream(zipTempFile);
	        	IOUtils.copy(zin, os);
			    zin.close();
	        }catch(Exception e){
	        	String mess = "Error copying zip to os: " + e.getLocalizedMessage();
				logger.warn(mess);
				throw new FileDownloadException(mess);
	        }
	        finally{
	        	//temp files in copiedTemporaryFileList were already deleted in previous finally block
				zipTempFile.delete();
	        }

		}//end of if(copiedTemporaryFileList.size()==1) and else

	}//end of method


	@Override
	public String getMimeType(String fileName){
		ConfigurableMimeFileTypeMap mimeMap = new ConfigurableMimeFileTypeMap();
		String mimeType = mimeMap.getContentType(fileName);
		logger.debug("ContentType of file is: " + mimeType);
		return mimeType;
	}

	/** 
	 * This needs to be improved
	 * TODO: add proper read segment handling
	 * {@inheritDoc}
	 */
	@Override
	public String generateUniqueBaseFileName(SampleSource cellLibrary) {

		final String DELIM = ".";
		final String SEP = "_";

		try {
			Sample cell = sampleService.getCell(cellLibrary);
			String platformUnitName = "unknown";
			String cellIndex = "L" + cellLibrary.getId(); // default to cell library (CL) id
			String barcode = "none";
			String libraryName = WordUtils.capitalizeFully(sampleService.getLibrary(cellLibrary).getName()).replaceAll(" ", ""); //camelcase
			if (cell != null){ // may be null if imported from external run of unknown origin
				platformUnitName = sampleService.getPlatformUnitForCell(cell).getName().replaceAll(" ", "");
				barcode = sampleService.getLibraryAdaptor(sampleService.getLibrary(cellLibrary)).getBarcodesequence().replaceAll(" ", "");
				cellIndex = sampleService.getCellIndex(cell).toString();
			}

			return new StringBuilder()
					.append(libraryName).append(DELIM)
					.append(platformUnitName).append(DELIM)
					.append("C" + cellIndex).append(SEP)
					.append("S" + "A").append(SEP)
					.append("I" + barcode).append(DELIM)
					.toString();
		} catch (Exception e) {
			String mess = "problem creating unique file base name";
			logger.error(mess);
			throw new RuntimeException(mess);
		}
	}
	
	@Override
	public String generateUniqueBaseFileName(Sample sample) {
		Assert.assertTrue(sampleService.isBiomolecule(sample), "sample must be a biomolecule");
		final String DELIM = ".";
		String biomoleculeTypeIdStr = "B"; // for biomolecule
		if (sampleService.isLibrary(sample))
			biomoleculeTypeIdStr = "L"; // for library
		else if (sampleService.isDnaOrRna(sample))
			biomoleculeTypeIdStr = "S"; // for sample
		String libraryName = WordUtils.capitalizeFully(sample.getName()).replaceAll(" ", ""); // camelCase the name
		return libraryName + DELIM + biomoleculeTypeIdStr + sample.getId().toString() + DELIM;
	}
	
	@Override
	public String generateUniqueBaseFileName(Job job) {
		final String DELIM = ".";
		String jobName = WordUtils.capitalizeFully(job.getName()).replaceAll(" ", ""); // camelCase the name
		return jobName + DELIM + "J" + job.getId().toString() + DELIM;
	}
	
	/*
	 * create and return a temporary file
	 * @see edu.yu.einstein.wasp.service.FileService#createTempFile()
	 */
	@Override
	public File createTempFile() throws FileUploadException{
		
		if(tempDir == null){
			String mess = "Temporary directory on local host has not been configured!  Please set \"wasp.temporary.dir\" in wasp-config.";
			logger.warn(mess);
			throw new FileUploadException(mess);
		}
		
		File temporaryDirectory = new File(tempDir);


		if (!temporaryDirectory.exists()) {
			try {
				temporaryDirectory.mkdir();
			} catch (Exception e) {
				String mess = "FileHandle upload failure trying to create '" + tempDir + "': " + e.getMessage();
				logger.warn(mess);
				throw new FileUploadException(mess);
			}
		}
		
		File localFile;
		try {
			localFile = File.createTempFile("wasp.", ".tmp", temporaryDirectory);
		} catch (IOException e) {
			String mess = "Unable to create local temporary file: " + e.getLocalizedMessage();
			logger.warn(mess);
			e.printStackTrace();
			throw new FileUploadException(mess);
		}
		return localFile;
	}

	@Override
	@Transactional
	public FileGroup saveLocalJobFile(Job job, File localFile, String fileName, String fileDescription, Random randomNumberGenerator) throws FileUploadException{
		try{
			FileGroup fileGroup = this.saveLocalFile(job.getId(), localFile, fileName, fileDescription, randomNumberGenerator, "results.dir", "submitted");
			this.linkFileGroupWithJob(fileGroup, job);
			return fileGroup;
		}catch(Exception e){
			throw new FileUploadException(e.getMessage());
		}
	}
	
	public FileGroup saveLocalQuoteOrInvoiceFile(Job job, File localFile, String fileName, String fileDescription, Random randomNumberGenerator) throws FileUploadException{
		try{
			FileGroup fileGroup = this.saveLocalFile(job.getId(), localFile, fileName, fileDescription, randomNumberGenerator, "results.dir", "submitted");
			//DO NOT USE NEXT LINE HERE: the quote or invoice is linked via job's acctQuote or job's acctInvoice   
			//////////this.linkFileGroupWithJob(fileGroup, job);
			return fileGroup;
		}catch(Exception e){
			throw new FileUploadException(e.getMessage());
		}
	}

	private FileGroup saveLocalFile(Integer id, File localFile, String fileName, String fileDescription, Random randomNumberGenerator, String targetDir, String additionalSubJobDir) throws FileUploadException{

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

		//////////String draftDir = gws.getTransportConnection().getConfiguredSetting("draft.dir");
		String partialDir = gws.getTransportConnection().getConfiguredSetting(targetDir);
		
		if (partialDir == null) {
			String mess = "Attempted to configure for file copy to " + fileHost + ", but hostname.settings." + targetDir + " has not been set.";
			logger.warn(mess);
			throw new FileUploadException(mess);
		}

		//String remoteDir = partialDir + "/" + id + "/";
		String remoteDir;
		if(additionalSubJobDir==null || additionalSubJobDir.isEmpty()){
			remoteDir = partialDir + "/" + id + "/"; 
		}
		else{
			remoteDir = partialDir + "/" + id + "/" + additionalSubJobDir + "/";
		}

		try {
			if(!gfs.exists(remoteDir)){
				gfs.mkdir(remoteDir);
			}
		} catch (IOException e) {
			String mess = "Problem creating resources (remote directory:"+remoteDir+") on remote host " + gws.getTransportConnection().getHostName();
			logger.warn(mess);
			e.printStackTrace();
			throw new FileUploadException(mess);
		}
				
		int randomNumber = randomNumberGenerator.nextInt(1000000000) + 100;
		//String noSpacesFileName = mpFile.getOriginalFilename().replaceAll("\\s+", "_");
		String noSpacesFileName = fileName.replaceAll("\\s+", "_");
		String taggedNoSpacesFileName = randomNumber + "_" + noSpacesFileName;

		String remoteFile = remoteDir + "/" + taggedNoSpacesFileName;

		FileHandle file = new FileHandle();
		file.setFileName(taggedNoSpacesFileName);
		file.setFileURI(gfs.remoteFileRepresentationToLocalURI(remoteFile));
		file = fileHandleDao.save(file);
		FileGroup retGroup = new FileGroup();
		retGroup.addFileHandle(file);
		retGroup.setDescription(fileDescription);
		retGroup = fileGroupDao.save(retGroup);	

		// TODO: Determine file type and set on the group.
		// probably not.  Figure out where to put or whether to
		// automatically determine mime type.

		try {
			gfs.put(localFile, remoteFile);			
			registerWithoutMD5(retGroup);
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
		fileGroupDao.save(retGroup);
		
		return retGroup;
		
	}

	@Override
	public List<FileGroupMeta> saveFileGroupMeta(List<FileGroupMeta> metaList, FileGroup filegroup) throws MetadataException{
		Assert.assertParameterNotNull(metaList, "a list of metadata is required");
		Assert.assertParameterNotNull(filegroup, "a filegroup is required");
		Assert.assertParameterNotNull(filegroup.getId(), "filegroup must have an id");
		return fileGroupMetaDao.setMeta(metaList, filegroup.getId());
	}
	
	@Override
	public List<FileHandleMeta> saveFileHandleMeta(List<FileHandleMeta> metaList, FileHandle filehandle) throws MetadataException{
		Assert.assertParameterNotNull(metaList, "a list of metadata is required");
		Assert.assertParameterNotNull(filehandle, "a filehandle is required");
		Assert.assertParameterNotNull(filehandle.getId(), "filehandle must have an id");
		return fileHandleMetaDao.setMeta(metaList, filehandle.getId());
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public Set<FileGroup> getFilesForMacromoleculeOrLibraryByType(Sample sample, FileType fileType) throws SampleTypeException {
		Assert.assertParameterNotNull(sample, "must provide a library");
		Assert.assertParameterNotNull(fileType, "must provide a fileType");
		Assert.assertParameterNotNull(fileType.getId(), "fileType has no valid fileTypeId");
		Map<FileType, Set<FileGroup>> filesByType = getFilesForMacromoleculeOrLibraryMappedToFileType(sample);
		if (!filesByType.containsKey(fileType))
			return new HashSet<FileGroup>();
		return filesByType.get(fileType);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public Set<FileGroup> getFilesForMacromoleculeOrLibraryByType(Sample sample, FileType fileType, Set<? extends FileTypeAttribute> attributes, boolean hasOnlyAttributesSpecified) throws SampleTypeException {
		Set<FileGroup> fgsWithAttributes = new LinkedHashSet<>();
		for (FileGroup fg : getFilesForMacromoleculeOrLibraryByType(sample, fileType))
			if (hasOnlyAttributesSpecified){
				if (fileTypeService.hasOnlyAttributes(fg, attributes))
					fgsWithAttributes.add(fg);
			} else {
				if (fileTypeService.hasAttributes(fg, attributes))
					fgsWithAttributes.add(fg);
			}
		return fgsWithAttributes;
	}
	
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public Map<FileType, Set<FileGroup>> getFilesForMacromoleculeOrLibraryMappedToFileType(Sample sample) throws SampleTypeException {
		
		String sampleTypeIName = sample.getSampleType().getIName();
		
		Map<FileType, Set<FileGroup>> filesByType = new HashMap<FileType, Set<FileGroup>>();
		
		if (sampleTypeIName.equals("library") || sampleTypeIName.equals("dna") || sampleTypeIName.equals("rna")){
			Sample s = sampleDao.findById(sample.getId());
			for (FileGroup fg : s.getFileGroups()) {
				FileType ft = fg.getFileType();
				if (!filesByType.containsKey(ft)){
					filesByType.put(ft, new LinkedHashSet<FileGroup>());
				}
				filesByType.get(ft).add(fg);
			}		
		}
		else{ 
			throw new SampleTypeException("sample is neither macromolecule nor library");
		}
		return filesByType;
		
	}
}

