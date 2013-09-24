package edu.yu.einstein.wasp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.WaspMetaDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.service.WaspMetaService;

@Service
@Transactional("entityManager")
public class WaspMetaServiceImpl<E> extends WaspServiceImpl implements WaspMetaService<E> {
	
	protected WaspMetaDao<E> waspMetaDao;
	
	public WaspMetaServiceImpl() {}

	public WaspMetaDao<E> getWaspMetaDao() {
		return waspMetaDao;
	}

	@Autowired
	@Qualifier("waspMetaDaoImpl")
	public void setWaspMetaDao(WaspMetaDao<E> waspMetaDao) {
		this.waspMetaDao = waspMetaDao;
	}

	public List<E> setMeta(final List<E> metaList, final int modelParentId) throws MetadataException{
		return waspMetaDao.setMeta(metaList, modelParentId);
	}

	public E setMeta(final E meta) throws MetadataException{
		return waspMetaDao.setMeta(meta);
	}
	
	public List<E> getMeta(final int modelParentId) {
		return waspMetaDao.getMeta(modelParentId);
	}
	

}
