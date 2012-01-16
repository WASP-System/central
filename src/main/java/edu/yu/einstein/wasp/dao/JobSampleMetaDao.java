
/**
 *
 * JobSampleMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSampleMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.model.JobSampleMeta;


public interface JobSampleMetaDao extends WaspDao<JobSampleMeta> {

  public JobSampleMeta getJobSampleMetaByJobSampleMetaId (final int jobSampleMetaId);

  public JobSampleMeta getJobSampleMetaByKJobsampleId (final String k, final int jobsampleId);


  public void updateByJobsampleId (final String area, final int jobsampleId, final List<JobSampleMeta> metaList);

  public void updateByJobsampleId (final int jobsampleId, final List<JobSampleMeta> metaList);




}

