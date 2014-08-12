/**
 *
 * AdaptorService.java 
 * @author rdubin
 *  
 * the AdaptorService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AdaptorDao;
import edu.yu.einstein.wasp.dao.AdaptorsetDao;
import edu.yu.einstein.wasp.dao.AdaptorsetMetaDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.interfacing.IndexingStrategy;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.Sample;

/**
 * @author calder
 * 
 */
@Service
public interface AdaptorService extends WaspService {

	/**
	 * Accepts and (in-situ) sorts list of Adaptors (of a particular Adaptorset)
	 * by Adaptor barcodenumber
	 * 
	 * @param List
	 *            <Adaptr>
	 * @return void
	 */
	public void sortAdaptorsByBarcodenumber(List<Adaptor> adaptors);

	/**
	 * Returns the adaptor associated with sample metadata "genericLibrary.adaptor"
	 * 
	 * @param adaptorId
	 * @return
	 */
	public Adaptor getAdaptorByAdaptorId(Integer adaptorId);
	
	/**
	 * Given a library, return the adaptor
	 * 
	 * @param library
	 * @return
	 */
	public Adaptor getAdaptor(Sample library) throws WaspException;

	public void setAdaptorDao(AdaptorDao adaptorDao);

	public AdaptorDao getAdaptorDao();
	
	public AdaptorsetMetaDao getAdaptorsetMetaDao();
	
	public void setAdaptorsetMetaDao(AdaptorsetMetaDao adaptorsetMetaDao);
	
	public void setIndexingStrategy(Adaptorset adaptorset, IndexingStrategy indexingStrategy) throws MetadataException;
	
	public IndexingStrategy getIndexingStrategy(Adaptorset adaptorset);
	
	public IndexingStrategy getIndexingStrategy(Integer adaptorsetId);
	
	public AdaptorsetDao getAdaptorsetDao();
	
	public void setAdaptorsetDao(AdaptorsetDao adaptorsetDao);

}
