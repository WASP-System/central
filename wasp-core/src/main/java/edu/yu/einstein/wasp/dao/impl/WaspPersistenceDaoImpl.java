package edu.yu.einstein.wasp.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import edu.yu.einstein.wasp.dao.WaspPersistenceDao;

@Repository
public class WaspPersistenceDaoImpl implements WaspPersistenceDao{

	
	protected EntityManager	entityManager;
	
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	@PersistenceContext
	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}
