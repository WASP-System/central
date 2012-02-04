package edu.yu.einstein.wasp.service;

import java.util.List;


public interface WaspMetaService<E> extends WaspService<E>{

	public List findDistinctMetaOrderBy(final String metaKeyName, final String direction);
} 
