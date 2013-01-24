package edu.yu.einstein.wasp.dao;

import javax.persistence.EntityManager;

public interface WaspPersistenceDao {

	public EntityManager getEntityManager();

	public void setEntityManager(EntityManager entityManager);
}
