
/**
 *
 * SubtypeSampleServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SubtypeSampleService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SubtypeSampleDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.model.SubtypeSample;
import edu.yu.einstein.wasp.model.SubtypeSampleMeta;
import edu.yu.einstein.wasp.model.TypeSample;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.Workflowsubtypesample;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.SubtypeSampleService;
import edu.yu.einstein.wasp.service.TypeSampleService;
import edu.yu.einstein.wasp.service.WorkflowService;
import edu.yu.einstein.wasp.util.MetaHelper;

@Service
public class SubtypeSampleServiceImpl extends WaspServiceImpl<SubtypeSample> implements SubtypeSampleService {

	/**
	 * subtypeSampleDao;
	 *
	 */
	private SubtypeSampleDao subtypeSampleDao;
	
	@Autowired
	private WorkflowService workflowService;
	
	@Autowired
	private AuthenticationService authenticationService;
	/**
	 * setSubtypeSampleDao(SubtypeSampleDao subtypeSampleDao)
	 *
	 * @param subtypeSampleDao
	 *
	 */
	@Override
	@Autowired
	public void setSubtypeSampleDao(SubtypeSampleDao subtypeSampleDao) {
		this.subtypeSampleDao = subtypeSampleDao;
		this.setWaspDao(subtypeSampleDao);
	}

	/**
	 * getSubtypeSampleDao();
	 *
	 * @return subtypeSampleDao
	 *
	 */
	@Override
	public SubtypeSampleDao getSubtypeSampleDao() {
		return this.subtypeSampleDao;
	}


  @Override
public SubtypeSample getSubtypeSampleBySubtypeSampleId (final int subtypeSampleId) {
    return this.getSubtypeSampleDao().getSubtypeSampleBySubtypeSampleId(subtypeSampleId);
  }

  @Override
public SubtypeSample getSubtypeSampleByIName (final String iName) {
    return this.getSubtypeSampleDao().getSubtypeSampleByIName(iName);
  }
  
  @Override
  public List<SubtypeSample> getActiveSubtypeSamples(){
	  Map queryMap = new HashMap();
	  queryMap.put("isActive", 1);
	  return this.findByMap(queryMap);
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
	  for (Workflowsubtypesample wfsts: workflowService.getWorkflowByWorkflowId(workflowId).getWorkflowsubtypesample() ){
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

