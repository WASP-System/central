/**
 * 
 */
package edu.yu.einstein.wasp.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.service.RunService;

/**
 * @author calder
 *
 */
@Service
@Transactional
public class RunServiceImpl implements RunService {
	
	private static Log logger = LogFactory.getLog(RunServiceImpl.class);
	
	private RunDao runDao;

	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.service.RunService#setRunDao(edu.yu.einstein.wasp.dao.RunDao)
	 */
	@Override
	@Autowired
	public void setRunDao(RunDao runDao) {
		this.runDao = runDao;

	}

	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.service.RunService#getRunDao()
	 */
	@Override
	public RunDao getRunDao() {
		return this.runDao;
	}

	@Override
	public Run getRunByName(String name) {
		Map<String, String> m = new HashMap<String,String>();
		m.put("name", name);
		logger.debug("looking for run " + name);
		List<Run> l = runDao.findByMap(m);
		
		Run result;
		if (l.size() > 1) {
			logger.error("Run name " + name + " is not unique! Returning only the newest run.");
			result = l.get(0);
			
			for (int x = 1; x <= l.size(); x++) {
				Run r = l.get(x);
				if (r.getRunId() > result.getRunId()) result = r;
			}
		} else {
			result = l.get(0);
		}
		
		return result;
	}

}
