/**
 * 
 */
package edu.yu.einstein.wasp.service.filetype.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.dao.FileMetaDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.File;
import edu.yu.einstein.wasp.model.FileMeta;
import edu.yu.einstein.wasp.service.filetype.FileTypeService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.util.MetaHelper;

/**
 * @author calder
 *
 */
public abstract class FileTypeServiceImpl extends WaspServiceImpl implements FileTypeService {
	
	private FileMetaDao fileMetaDao;
	
	@Autowired
	public void setFileMetaDao(FileMetaDao fileMetaDao) {
		this.fileMetaDao = fileMetaDao;
	}

	@Override
	public boolean isSingleFile(File file) {
		String single = getMeta(file, FILETYPE_AREA, FILETYPE_IS_SINGLE_META_KEY);
		Boolean b = new Boolean(single);
		return b.booleanValue();
	}
	
	public void setSingleFile(File file, boolean single) throws MetadataException {
		Boolean b = new Boolean(single);
		setMeta(file, FILETYPE_AREA, FILETYPE_IS_SINGLE_META_KEY, b.toString());
	}

	@Override
	public Integer getFileNumber(File file) {
		String num = getMeta(file, FILETYPE_AREA, FILETYPE_FILE_NUMBER_META_KEY);
		return new Integer(num);
	}
	
	public void setFileNumber(File file, Integer number) throws MetadataException {
		setMeta(file, FILETYPE_AREA, FILETYPE_FILE_NUMBER_META_KEY, number.toString());
	}
	
	protected String getMeta(File f, String area, String k) {
		Assert.assertParameterNotNull(f, "file cannot be null");
		String v = null;
		List<FileMeta> fileMetaList = f.getFileMeta();
		if (fileMetaList == null)
			fileMetaList = new ArrayList<FileMeta>();
		try{
			v = (String) MetaHelper.getMetaValue(area, k, fileMetaList);
		} catch(MetadataException e) {
			// value not found
		}
		return v;
	}

	protected void setMeta(File file, String area, String metaKey, String metaValue) throws MetadataException{
		Assert.assertParameterNotNull(file, "file cannot be null");
		Assert.assertParameterNotNull(metaKey, "metaKey cannot be null");
		Assert.assertParameterNotNull(metaValue, "metaValue cannot be null");
		FileMeta fileMeta = new FileMeta();
		fileMeta.setFileId(file.getFileId());
		fileMeta.setK(area + "." + metaKey);
		fileMeta.setV(metaValue);
		fileMetaDao.setMeta(fileMeta);
	}
	
}
