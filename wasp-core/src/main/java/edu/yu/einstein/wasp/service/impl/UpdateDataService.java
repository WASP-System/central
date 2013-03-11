package edu.yu.einstein.wasp.service.impl;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.WaspModel;

@Service
@Transactional("entityManager")
public class UpdateDataService {
	
	private Logger logger = LoggerFactory.getLogger(UpdateDataService.class);

	@Autowired
    private ApplicationContext applicationContext;

	public void updateTestData(){
		Map<String, WaspDao> daoBeans = applicationContext.getBeansOfType(WaspDao.class);
		for (WaspDao dao : daoBeans.values()){
			for (WaspModel model : (List<WaspModel>) dao.findAll()){
				try {
					Method getter = null;
					Method setter = null;
					if (model.getClass().getName().endsWith("Meta")){
						getter = model.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("getUUID");
						setter = model.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("setUUID", UUID.class);
					} else {
						getter = model.getClass().getSuperclass().getSuperclass().getDeclaredMethod("getUUID");
						setter = model.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setUUID", UUID.class);
					}
					setter.setAccessible(true);
					getter.setAccessible(true);
					UUID uuid = (UUID) getter.invoke(model);
					if (uuid == null){
						logger.debug("setting UUID for instance of class " + model.getClass().getName());
						setter.invoke(model, UUID.randomUUID());
						dao.merge(model);
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(); 
				}
			}
		}
	}
}