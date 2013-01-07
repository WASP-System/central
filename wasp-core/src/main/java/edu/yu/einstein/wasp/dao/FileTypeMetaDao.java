package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.model.FileTypeMeta;

public interface FileTypeMetaDao {

	public FileTypeMeta getFileTypeMetaByFileTypeMetaId(int fileTypeMetaId);

	public FileTypeMeta getFileTypeMetaByKFileTypeId(String k, int fileTypeId);

	public void updateByFileTypeId(int fileTypeId, List<FileTypeMeta> metaList);

}
