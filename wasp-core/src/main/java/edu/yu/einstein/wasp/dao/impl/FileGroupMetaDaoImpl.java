package edu.yu.einstein.wasp.dao.impl;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import edu.yu.einstein.wasp.dao.FileGroupMetaDao;
import edu.yu.einstein.wasp.exception.ModelDetachException;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileGroupMeta;

public class FileGroupMetaDaoImpl extends WaspDaoImpl<FileGroupMeta> implements FileGroupMetaDao {
	
	public FileGroupMetaDaoImpl() {
		super();
		this.entityClass = FileGroupMeta.class;
	}


}
