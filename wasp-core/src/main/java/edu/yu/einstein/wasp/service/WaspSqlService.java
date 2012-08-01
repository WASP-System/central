package edu.yu.einstein.wasp.service;

import java.util.List;


public interface WaspSqlService {

	public void executeNativeSqlUpdateOnList(List<String> updateQuery);
	
}
