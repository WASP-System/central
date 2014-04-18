package edu.yu.einstein.wasp.load.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.dao.FileTypeDao;
import edu.yu.einstein.wasp.load.service.FileTypeLoadService;
import edu.yu.einstein.wasp.model.FileType;

/**
 * 
 * @author asmclellan
 *
 */
@Service
@Transactional("entityManager")
public class FileTypeLoadServiceImpl extends WaspLoadServiceImpl implements FileTypeLoadService {
	
	  @Autowired
	  private FileTypeDao fileTypeDao;
	  
	  @Override
	  public FileType update(String iname, String name, String description, String extensions, int isActive){
		  Assert.assertParameterNotNull(iname, "iname cannot be null");
		  Assert.assertParameterNotNull(name, "name cannot be null");
		  Assert.assertParameterNotNull(description, "description cannot be null");
		  FileType fileType = fileTypeDao.getFileTypeByIName(iname); 
		  // inserts or updates fileType
		  if (fileType.getId() == null) {
			  fileType.setIName(iname);
			  fileType.setName(name);
			  fileType.setDescription(description);
			  fileType.setExtensions(extensions);
			  fileType.setIsActive(isActive);
			  fileType = fileTypeDao.save(fileType);
		  } else {
			  if (fileType.getName() == null || !fileType.getName().equals(name))
				  fileType.setName(name);
			  if (fileType.getDescription() == null || !fileType.getDescription().equals(description))
				  fileType.setDescription(description);
			  if (fileType.getExtensions() == null || !fileType.getExtensions().equals(extensions))
				  fileType.setExtensions(extensions);
			  if (fileType.getIsActive().intValue() != isActive)
				  fileType.setIsActive(isActive);
		  }
		  return fileType;
	  }
}
