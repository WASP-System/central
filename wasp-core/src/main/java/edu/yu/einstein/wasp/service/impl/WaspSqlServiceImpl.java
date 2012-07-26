package edu.yu.einstein.wasp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.impl.WaspPersistenceDao;
import edu.yu.einstein.wasp.service.WaspSqlService;

@Service
@Transactional
public class WaspSqlServiceImpl implements WaspSqlService{

	@Autowired
	WaspPersistenceDao waspPersistenceDao;
	
	@Override
	public void executeNativeSqlUpdateOnList(List<String> updateQuery){
		for (String query: updateQuery)
			waspPersistenceDao.getEntityManager().createNativeQuery(query).executeUpdate();
	}
	
}
