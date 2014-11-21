package edu.yu.einstein.wasp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.impl.WaspPersistenceDaoImpl;
import edu.yu.einstein.wasp.service.WaspSqlService;

@Service
@Transactional("entityManager")
public class WaspSqlServiceImpl implements WaspSqlService{

	@Autowired
	WaspPersistenceDaoImpl waspPersistenceDaoImpl;
	
	@Override
	public void executeQueryUpdateOnList(List<String> updateQuery){
		for (String query: updateQuery)
			waspPersistenceDaoImpl.getEntityManager().createQuery(query).executeUpdate();
	}
	
}
