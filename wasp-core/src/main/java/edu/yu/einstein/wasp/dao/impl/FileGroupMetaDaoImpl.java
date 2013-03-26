package edu.yu.einstein.wasp.dao.impl;

import edu.yu.einstein.wasp.dao.FileGroupMetaDao;
import edu.yu.einstein.wasp.model.FileGroupMeta;

public class FileGroupMetaDaoImpl extends WaspMetaDaoImpl<FileGroupMeta> implements FileGroupMetaDao {
	
	public FileGroupMetaDaoImpl() {
		super();
		this.entityClass = FileGroupMeta.class;
	}


}
