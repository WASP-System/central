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
	
	@Autowired
	private FileMetaDao fileMetaDao;

	@Override
	public boolean isSingleFile(File file) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Integer getFileNumber(File file) {
		// TODO Auto-generated method stub
		return null;
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

	protected void setMeta(File file, String area, String metaKey, String metaValue){
		Assert.assertParameterNotNull(file, "file cannot be null");
		Assert.assertParameterNotNull(metaKey, "metaKey cannot be null");
		Assert.assertParameterNotNull(metaValue, "metaValue cannot be null");
		List<FileMeta> fileMetaList = file.getFileMeta();
		if (fileMetaList == null)
			fileMetaList = new ArrayList<FileMeta>();
		FileMeta fileMeta = null;
		try{
			fileMeta = MetaHelper.getMetaObjectFromList(area, metaKey, fileMetaList);
			
			if (fileMeta.getV().equals(metaValue)){ // no change in value
				return;
			}
		} catch(MetadataException e) {
			// doesn't exist so create
			fileMeta = new FileMeta();
			fileMeta.setK(area + "." + metaKey);
		}
		fileMeta.setV(metaValue);
		fileMetaDao.updateByFileId(file.getFileId(), fileMeta);
	
	}
	
}
