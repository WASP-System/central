package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.exception.MetadataException;

public interface WaspMetaDao<E> extends WaspDao<E>{
	
	/**
	 * set modelParentId (e.g. UserId for WUser model or jobDraftId for jobDraft model) for all provided meta then persist to the database
	 * @param metaList
	 * @param modelParentId
	 * @return
	 * @throws MetadataException
	 */
	public List<E> setMeta(final List<E> metaList, final int modelParentId) throws MetadataException;
	
	/**
	 * persist meta to the database if not already present
	 * @param meta
	 * @return
	 * @throws MetadataException
	 */
	public E setMeta(final E meta) throws MetadataException;
	
	public List<E> getMeta(final int modelParentId);

}
