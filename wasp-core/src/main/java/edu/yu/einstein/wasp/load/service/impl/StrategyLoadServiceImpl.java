package edu.yu.einstein.wasp.load.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.additionalClasses.Strategy;
import edu.yu.einstein.wasp.dao.MetaDao;
import edu.yu.einstein.wasp.dao.SampleTypeCategoryDao;
import edu.yu.einstein.wasp.load.service.SampleTypeLoadService;
import edu.yu.einstein.wasp.load.service.StrategyLoadService;
import edu.yu.einstein.wasp.load.service.WaspLoadService;
import edu.yu.einstein.wasp.model.Meta;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.SampleTypeCategory;

@Service
@Transactional("entityManager")
public class StrategyLoadServiceImpl extends WaspLoadServiceImpl implements StrategyLoadService {

	@Autowired
	private MetaDao metaDao;

	public Strategy update(String k, String v){
		 
		Assert.assertParameterNotNull(k, "k Cannot be null");
		Assert.assertParameterNotNull(v, "v Cannot be null");		
		
		Meta meta = metaDao.getMetaByK(Strategy.KEY_PREFIX+k);
		meta.setK(Strategy.KEY_PREFIX+k);
		meta.setV(v);
		meta = metaDao.save(meta);
		
		String nameAndDescription = meta.getV();
		String [] array = nameAndDescription.split("::");
		Strategy strategy = new Strategy();
		strategy.setIName(k);
		strategy.setName(array[0]);
		strategy.setDescription(array[1]);
		return strategy;
		/*
			String nameAndDescription = meta.getK();
			String [] array = nameAndDescription.split("::");
			strategy.setName(array[0]);
			strategy.setDescription(array[1]);
		}
		Meta meta = metaDao.setMeta(StrategyLoadService.KEY_PREFIX+k, v);
		//if(meta.getId()==null){
		//	String nameAndDescription = meta.getK();
		//	String [] array = nameAndDescription.split("::");
		//	strategy.setName(array[0]);
		//	strategy.setDescription(array[1]);
		//	return strategy;
		//}
		//else{
			String nameAndDescription = meta.getV();
			String [] array = nameAndDescription.split("::");
			Strategy strategy = new Strategy();
			strategy.setName(array[0]);
			strategy.setDescription(array[1]);
			return strategy;
		//}
		*/
		
		
	}

}
