/**
 *
 * SampleServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleSubtypeMeta;
import edu.yu.einstein.wasp.model.WorkflowSampleSubtype;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.util.MetaHelper;

@Service
public class SampleServiceImpl extends WaspServiceImpl implements SampleService {

	/**
	 * sampleDao;
	 * 
	 */
	private SampleDao	sampleDao;

	/**
	 * setSampleDao(SampleDao sampleDao)
	 * 
	 * @param sampleDao
	 * 
	 */
	@Override
	@Autowired
	public void setSampleDao(SampleDao sampleDao) {
		this.sampleDao = sampleDao;
	}

	/**
	 * getSampleDao();
	 * 
	 * @return sampleDao
	 * 
	 */
	@Override
	public SampleDao getSampleDao() {
		return this.sampleDao;
	}
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private WorkflowDao workflowDao;

	@Override
	public Sample getSampleByName(final String name) {
		return this.getSampleDao().getSampleByName(name);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Sample> findAllPlatformUnits() {
		Map queryMap = new HashMap();
		queryMap.put("sampleType.iName", "platformunit");
//		queryMap.put("sampleType.sampleTypeId", 5);
		return sampleDao.findByMap(queryMap);
	}
	
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<SampleSubtype> getSampleSubtypesForWorkflowByLoggedInUserRoles(Integer workflowId){
		  return getSampleSubtypesForWorkflowByRole(workflowId, authenticationService.getRoles(), null);
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<SampleSubtype> getSampleSubtypesForWorkflowByLoggedInUserRoles(Integer workflowId, String sampleTypeIName){
		  return getSampleSubtypesForWorkflowByRole(workflowId, authenticationService.getRoles(), sampleTypeIName);
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<SampleSubtype> getSampleSubtypesForWorkflowByRole(Integer workflowId, String[] roles){
		  return getSampleSubtypesForWorkflowByRole(workflowId, roles, null);
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<SampleSubtype> getSampleSubtypesForWorkflowByRole(Integer workflowId, String[] roles, String sampleTypeIName){
		  List<SampleSubtype> sampleSubtypes = new ArrayList<SampleSubtype>();
		  for (WorkflowSampleSubtype wfsts: workflowDao.getWorkflowByWorkflowId(workflowId).getWorkflowSampleSubtype() ){
			  SampleSubtype sts = wfsts.getSampleSubtype();
			  if (sampleTypeIName == null || sampleTypeIName.equals(sts.getSampleType().getIName())){
				  String[] includedRoles = new String[]{};
				  String[] excludedRoles = new String[]{};
				  try{
					  includedRoles = MetaHelper.getMetaValue(sts.getIName(), "includeRoles",sts.getSampleSubtypeMeta()).split(",");
				  } catch(MetadataException e){
					  // "includeRoles" meta not present
				  }
				  try{
					  excludedRoles = MetaHelper.getMetaValue(sts.getIName(), "excludeRoles",sts.getSampleSubtypeMeta()).split(",");
				  } catch(MetadataException e){
					  // "excludeRoles" meta not present
				  }
				  if (authenticationService.hasRoleInRoleArray(includedRoles, roles) && !authenticationService.hasRoleInRoleArray(excludedRoles, roles)){
					  sampleSubtypes.add(sts);
				  }
			  }
		  }
		  return sampleSubtypes;
	  }
}
