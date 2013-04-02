package edu.yu.einstein.wasp.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.FileGroupMetaDao;
import edu.yu.einstein.wasp.model.FileGroupMeta;

@Transactional
@Repository
public class FileGroupMetaDaoImpl extends WaspMetaDaoImpl<FileGroupMeta> implements FileGroupMetaDao {
	
	public FileGroupMetaDaoImpl() {
		super();
		this.entityClass = FileGroupMeta.class;
	}


}
