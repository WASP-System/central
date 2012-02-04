package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaspMetaServiceImpl<E> extends WaspServiceImpl<E> {

	public List findDistinctMetaOrderBy(final String metaKeyName, final String direction){
		Map metaQueryMap = new HashMap();
		metaQueryMap.put("k", metaKeyName);
	  	List<String> orderByList = new ArrayList();
	  	orderByList.add("v");
		return this.getWaspDao().findByMapDistinctOrderBy(metaQueryMap, orderByList, orderByList, direction); 
	}
	
}
