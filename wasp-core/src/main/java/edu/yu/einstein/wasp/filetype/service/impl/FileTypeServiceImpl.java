/**
 * 
 */
package edu.yu.einstein.wasp.filetype.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.dao.FileGroupDao;
import edu.yu.einstein.wasp.dao.FileGroupMetaDao;
import edu.yu.einstein.wasp.dao.FileHandleDao;
import edu.yu.einstein.wasp.dao.FileHandleMetaDao;
import edu.yu.einstein.wasp.dao.FileTypeDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.filetype.FileTypeAttribute;
import edu.yu.einstein.wasp.filetype.service.FileTypeService;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileGroupMeta;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileHandleMeta;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.util.MetaHelper;

/**
 * @author calder
 *
 */
@Service
@Transactional("entityManager")
public class FileTypeServiceImpl extends WaspServiceImpl implements FileTypeService {
	
	@Autowired
	private FileGroupMetaDao fileGroupMetaDao;
	
	@Autowired
	private FileHandleDao fileHandleDao;
	
	@Autowired
	private FileGroupDao fileGroupDao;
	
	@Autowired
	private FileTypeDao fileTypeDao;
	
	private FileHandleMetaDao fileMetaDao;
	
	private static final String FILEGROUP_ATTRIBUTE_DELIMITER = "::";

	@Autowired
	public void setFileMetaDao(FileHandleMetaDao fileMetaDao) {
		this.fileMetaDao = fileMetaDao;
	}
	
	@Override
	public FileTypeDao getFileTypeDao() {
		return fileTypeDao;
	}

	@Override
	public boolean isSingleFile(FileHandle file) {
		String single = getMeta(file, FILETYPE_AREA, FILETYPE_IS_SINGLE_META_KEY);
		Boolean b = new Boolean(single);
		return b.booleanValue();
	}
	
	public void setSingleFile(FileHandle file, boolean single) throws MetadataException {
		Boolean b = new Boolean(single);
		setMeta(file, FILETYPE_AREA, FILETYPE_IS_SINGLE_META_KEY, b.toString());
	}

	@Override
	public Integer getFileNumber(FileHandle file) {
		String num = getMeta(file, FILETYPE_AREA, FILETYPE_FILE_NUMBER_META_KEY);
		return new Integer(num);
	}
	
	public void setFileNumber(FileHandle file, Integer number) throws MetadataException {
		setMeta(file, FILETYPE_AREA, FILETYPE_FILE_NUMBER_META_KEY, number.toString());
	}
	
	protected String getMeta(FileHandle f, String area, String k) {
		Assert.assertParameterNotNull(f, "file cannot be null");
		String v = null;
		List<FileHandleMeta> fileMetaList = fileMetaDao.getMeta(f.getId());
		if (fileMetaList == null)
			fileMetaList = new ArrayList<FileHandleMeta>();
		fileMetaList.size();
		try{
			v = (String) MetaHelper.getMetaValue(area, k, fileMetaList);
		} catch(MetadataException e) {
			logger.debug("unable to get a meta value with key=" + area + "." + k + " for FileHandle with id=" + f.getId());
		}
		logger.debug("returning meta value " + area + "." + k + "=" + v + " for FileHandle with id=" + f.getId());
		return v;
	}
	
	protected String getMeta(FileGroup f, String area, String k) {
		Assert.assertParameterNotNull(f, "file group cannot be null");
		String v = null;
		List<FileGroupMeta> fileGroupMetaList = fileGroupMetaDao.getMeta(f.getId());
		if (fileGroupMetaList == null)
			fileGroupMetaList = new ArrayList<FileGroupMeta>();
		fileGroupMetaList.size();
		try{
			v = (String) MetaHelper.getMetaValue(area, k, fileGroupMetaList);
		} catch(MetadataException e) {
			logger.debug("unable to get a meta value with key=" + area + "." + k + " for FileGroup with id=" + f.getId());
		}
		logger.debug("returning meta value " + area + "." + k + "=" + v + " for FileGroup with id=" + f.getId());
		return v;
	}
	
	protected Map<String,String> getAllMetaByArea(FileHandle f, String area) {
	    Assert.assertParameterNotNull(f, "file cannot be null");
	    Assert.assertParameterNotNull(area, "area cannot be null");
            f = fileHandleDao.merge(f);
            List<FileHandleMeta> fileMetaList = f.getFileHandleMeta();
            if (fileMetaList == null)
                fileMetaList = new ArrayList<FileHandleMeta>();
            Map<String,String> result = MetaHelper.getKeyValueMap(area, fileMetaList);
            return result;
	}
	
	protected Map<String,String> getAllMetaByArea(FileGroup f, String area) {
            Assert.assertParameterNotNull(f, "file group cannot be null");
            Assert.assertParameterNotNull(area, "area cannot be null");
            f = fileGroupDao.merge(f);
            List<FileGroupMeta> fileMetaList = f.getFileGroupMeta();
            if (fileMetaList == null)
                fileMetaList = new ArrayList<FileGroupMeta>();
            Map<String,String> result = MetaHelper.getKeyValueMap(area, fileMetaList);
            return result;
        }

	protected void setMeta(FileHandle file, String area, String metaKey, String metaValue) throws MetadataException{
		Assert.assertParameterNotNull(file, "file cannot be null");
		Assert.assertParameterNotNull(metaKey, "metaKey cannot be null");
		Assert.assertParameterNotNull(metaValue, "metaValue cannot be null");
		file = fileHandleDao.merge(file);
		FileHandleMeta fileMeta = new FileHandleMeta();
		fileMeta.setFileHandleId(file.getId());
		fileMeta.setK(area + "." + metaKey);
		fileMeta.setV(metaValue);
		logger.debug("setting meta  " + fileMeta + " for FileHandle with id=" + file.getId());
		fileMetaDao.setMeta(fileMeta);
	}
	
	protected void setMeta(FileGroup fileGroup, String area, String metaKey, String metaValue) throws MetadataException{
		Assert.assertParameterNotNull(fileGroup, "file cannot be null");
		Assert.assertParameterNotNull(metaKey, "metaKey cannot be null");
		Assert.assertParameterNotNull(metaValue, "metaValue cannot be null");
		fileGroup = fileGroupDao.merge(fileGroup);
		FileGroupMeta fileMeta = new FileGroupMeta();
		fileMeta.setFileGroupId(fileGroup.getId());
		fileMeta.setK(area + "." + metaKey);
		fileMeta.setV(metaValue);
		logger.debug("setting meta  " + fileMeta + " for FileGroup with id=" + fileGroup.getId());
		fileGroupMetaDao.setMeta(fileMeta);
	}
	
	private String setToString(Set<? extends FileTypeAttribute> s) {
	    String result = "";
	    if (s.size() > 0) {
		result = StringUtils.join(s, FILEGROUP_ATTRIBUTE_DELIMITER);
	    }
	    return result;
	}
	
	private Set<FileTypeAttribute> stringToSet(String s) {
	    Set<FileTypeAttribute> attributes = new HashSet<>();
	    if (s != null) {
	    	for (String attrStr : s.split(FILEGROUP_ATTRIBUTE_DELIMITER))
				attributes.add(new FileTypeAttribute(attrStr));
	    }
	    return attributes;
	}

	@Override
	public void addAttribute(FileGroup fg, FileTypeAttribute attribute) {
		logger.debug("adding attribute=" + attribute + " to FileGroup id=" + fg.getId());
	    String atts = getMeta(fg, FILETYPE_AREA, FILEGROUP_ATTRIBUTE_META_KEY);
	    logger.debug("current attributes=" + atts);
	    Set<FileTypeAttribute> attributes = stringToSet(atts);
	    if (!attributes.contains(attribute)) {
	    	logger.debug("current attributes (atts) does not contain " + attribute);
			attributes.add(attribute);
			try {
				logger.debug("setting meta " + FILETYPE_AREA + "." + FILEGROUP_ATTRIBUTE_META_KEY + "=" + setToString(attributes));
			    setMeta(fg, FILETYPE_AREA, FILEGROUP_ATTRIBUTE_META_KEY, setToString(attributes));
			} catch (MetadataException e) {
			    logger.error("unable to set metadata: " + e.getMessage());
			}
	    } else {
	    	logger.debug("current attributes (atts) contains " + attribute);
	    }
	    
	}

	@Override
	public void removeAttribute(FileGroup fg, FileTypeAttribute attribute) {
	    String atts = getMeta(fg, FILETYPE_AREA, FILEGROUP_ATTRIBUTE_META_KEY);
	    Set<FileTypeAttribute> attributes = stringToSet(atts);
	    if (attributes.contains(attribute)) {
			attributes.remove(attribute);
			try {
			    setMeta(fg, FILETYPE_AREA, FILEGROUP_ATTRIBUTE_META_KEY, setToString(attributes));
			} catch (MetadataException e) {
			    logger.error("unable to set metadata: " + e.getMessage());
			}
	    }
	}

	@Override
	public void setAttributes(FileGroup fg, Set<? extends FileTypeAttribute> attributes) {
	    try {
	    	setMeta(fg, FILETYPE_AREA, FILEGROUP_ATTRIBUTE_META_KEY, setToString(attributes));
	    } catch (MetadataException e) {
	    	logger.error("unable to set metadata: " + e.getMessage());
	    }
	    
	}

	@Override
	public Set<FileTypeAttribute> getAttributes(FileGroup fg) {
	    String atts = getMeta(fg, FILETYPE_AREA, FILEGROUP_ATTRIBUTE_META_KEY);
	    Set<FileTypeAttribute> attributes = stringToSet(atts);
	    return attributes;
	}

	@Override
	public boolean hasAttributes(FileGroup fg, Set<? extends FileTypeAttribute> attributes) {
	    String atts = getMeta(fg, FILETYPE_AREA, FILEGROUP_ATTRIBUTE_META_KEY);
	    boolean attributesMatch = stringToSet(atts).containsAll(attributes);
	    logger.debug("hasAttributes=" + attributesMatch);
	    if (attributesMatch) 
	    	return true;
	    return false;
	}
	
	@Override
	public boolean hasAttribute(FileGroup fg, FileTypeAttribute attribute) {
		Set<FileTypeAttribute> atts = new HashSet<>();
		atts.add(attribute);
	    return hasAttributes(fg, atts);
	}
	
	@Override
	public boolean hasOnlyAttribute(FileGroup fg, FileTypeAttribute attribute) {
		Set<FileTypeAttribute> atts = new HashSet<>();
		atts.add(attribute);
		return hasOnlyAttributes(fg, atts);
	}

	@Override
	public boolean hasOnlyAttributes(FileGroup fg, Set<? extends FileTypeAttribute> attributes) {
	    String atts = getMeta(fg, FILETYPE_AREA, FILEGROUP_ATTRIBUTE_META_KEY);
	    Set<FileTypeAttribute> fgatts = stringToSet(atts);
	    boolean attributesMatchExactly = fgatts.containsAll(attributes);
	    boolean collectionSizesMatchExactly = fgatts.size() == attributes.size();
	    logger.debug("attributesMatchExactly=" + attributesMatchExactly);
	    logger.debug("collectionSizesMatchExactly=" + collectionSizesMatchExactly);
	    if (attributesMatchExactly && collectionSizesMatchExactly) 
	    	return true;
	    return false;
	}

    @Override
    public void copyMetaByArea(FileGroup origin, FileGroup target, String area) throws MetadataException {
        Map<String,String> meta = getAllMetaByArea(origin, area);
        for (String k : meta.keySet()) {
            logger.trace("Copying " + target + ":" + area + ":" + k + ":" + meta.get(k));
            setMeta(target, area, k, meta.get(k));
        }
        
    }

    @Override
    public void copyMetaByArea(FileHandle origin, FileHandle target, String area) throws MetadataException {
        Map<String,String> meta = getAllMetaByArea(origin, area);
        for (String k : meta.keySet()) {
            logger.trace("Copying " + target + ":" + area + ":" + k + ":" + meta.get(k));
            setMeta(target, area, k, meta.get(k));
        }
        
    }
}
