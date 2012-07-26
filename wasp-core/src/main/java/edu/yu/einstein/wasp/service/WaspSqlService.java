package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public interface WaspSqlService {

	public void executeNativeSqlUpdateOnList(List<String> updateQuery);
	
}
