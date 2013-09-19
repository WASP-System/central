package edu.yu.einstein.wasp.service;

import java.util.List;

import edu.yu.einstein.wasp.dao.WaspMetaDao;
import edu.yu.einstein.wasp.exception.MetadataException;

public interface WaspMetaService<E> extends WaspService {
	
	public void setWaspMetaDao(WaspMetaDao<E> waspMetaDao);
	
	public List<E> setMeta(final List<E> metaList, final int modelParentId) throws MetadataException;

	public E setMeta(final E meta) throws MetadataException;
	
	public List<E> getMeta(final int modelParentId);
}
