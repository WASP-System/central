package edu.yu.einstein.wasp.service;

import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.dao.WaspDao;


public interface WaspService<E> {

 public WaspDao getWaspDao();

 public void setEntityManager(javax.persistence.EntityManager entityManager);
 public void setEntityManagerFactory(javax.persistence.EntityManagerFactory entityManagerFactory);

 public void setJpaTemplate(org.springframework.orm.jpa.JpaTemplate jpaTemplate);



 public void persist(E entity);

 public E save(E entity);

 public void remove(E entity);

 public E merge(E entity);

 public void refresh(E entity);

 public E findById(int id);
 public E getById(int id);
 public E flush(E entity);
 public List findAll();
 public Integer removeAll();

 public List findByMap(Map m);


} 
