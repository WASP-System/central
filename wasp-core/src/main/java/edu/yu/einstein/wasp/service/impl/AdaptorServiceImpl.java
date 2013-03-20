/**
 *
 * AdaptorServiceImpl.java 
 * @author rdubin
 *  
 * the AdaptorService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.AdaptorDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.util.MetaHelper;

@Service
@Transactional("entityManager")
public class AdaptorServiceImpl extends WaspServiceImpl implements
		AdaptorService {
	
	AdaptorDao adaptorDao;

	@Autowired
	@Override
	public void setAdaptorDao(AdaptorDao adaptorDao) {
		this.adaptorDao = adaptorDao;
	}
	
	@Override
	public AdaptorDao getAdaptorDao() {
		return adaptorDao;
	}

	@Autowired
	SampleService sampleService;
	
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sortAdaptorsByBarcodenumber(List<Adaptor> adaptors) {
		class AdaptorBarcodenumberComparator implements Comparator<Adaptor> {
			@Override
			public int compare(Adaptor arg0, Adaptor arg1) {
				return arg0.getBarcodenumber().compareTo(
						arg1.getBarcodenumber());
			}
		}
		Collections.sort(adaptors, new AdaptorBarcodenumberComparator()); // sort by sample's name
	}

	@Override
	public Adaptor getAdaptorByAdaptorId(Integer adaptorId) {
		return adaptorDao.findById(adaptorId);
	}

	@Override
	public Adaptor getAdaptor(Sample library) throws SampleTypeException, MetadataException {
		Sample lib = sampleService.getSampleById(library.getSampleId());
		String adaptorId = MetaHelper.getMetaValue("genericLibrary", "adaptor", lib.getSampleMeta());
		return getAdaptorByAdaptorId(new Integer(adaptorId));
	}

}
