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
import edu.yu.einstein.wasp.model.SubtypeSample;
import edu.yu.einstein.wasp.model.SubtypeSampleMeta;
import edu.yu.einstein.wasp.model.Workflowsubtypesample;
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
		queryMap.put("typeSample.iName", "platformunit");
//		queryMap.put("typeSample.typeSampleId", 5);
		return sampleDao.findByMap(queryMap);
	}
	
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<SubtypeSample> getSubtypeSamplesForWorkflowByLoggedInUserRoles(Integer workflowId){
		  return getSubtypeSamplesForWorkflowByRole(workflowId, authenticationService.getRoles(), null);
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<SubtypeSample> getSubtypeSamplesForWorkflowByLoggedInUserRoles(Integer workflowId, String typeSampleIName){
		  return getSubtypeSamplesForWorkflowByRole(workflowId, authenticationService.getRoles(), typeSampleIName);
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<SubtypeSample> getSubtypeSamplesForWorkflowByRole(Integer workflowId, String[] roles){
		  return getSubtypeSamplesForWorkflowByRole(workflowId, roles, null);
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<SubtypeSample> getSubtypeSamplesForWorkflowByRole(Integer workflowId, String[] roles, String typeSampleIName){
		  List<SubtypeSample> subtypeSamples = new ArrayList<SubtypeSample>();
		  for (Workflowsubtypesample wfsts: workflowDao.getWorkflowByWorkflowId(workflowId).getWorkflowsubtypesample() ){
			  SubtypeSample sts = wfsts.getSubtypeSample();
			  if (typeSampleIName == null || typeSampleIName.equals(sts.getTypeSample().getIName())){
				  String[] includedRoles = new String[]{};
				  String[] excludedRoles = new String[]{};
				  MetaHelper metahelper = new MetaHelper("subtypeSample", SubtypeSampleMeta.class, Locale.US);
				  metahelper.setArea(sts.getIName());
				  try{
					  includedRoles = metahelper.getMetaValueByName("includeRoles",sts.getSubtypeSampleMeta()).split(",");
				  } catch(MetadataException e){
					  // "includeRoles" meta not present
				  }
				  try{
					  excludedRoles = metahelper.getMetaValueByName("excludeRoles",sts.getSubtypeSampleMeta()).split(",");
				  } catch(MetadataException e){
					  // "excludeRoles" meta not present
				  }
				  if (authenticationService.hasRoleInRoleArray(includedRoles, roles) && !authenticationService.hasRoleInRoleArray(excludedRoles, roles)){
					  subtypeSamples.add(sts);
				  }
			  }
		  }
		  return subtypeSamples;
	  }
}
