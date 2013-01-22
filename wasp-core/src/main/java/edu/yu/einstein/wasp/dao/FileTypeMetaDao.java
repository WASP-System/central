package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.FileTypeMeta;

public interface FileTypeMetaDao {

	public FileTypeMeta getFileTypeMetaByFileTypeMetaId(int fileTypeMetaId);

	public FileTypeMeta getFileTypeMetaByKFileTypeId(String k, int fileTypeId);

}
