
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

import edu.yu.einstein.wasp.model.JobSampleMeta;


public interface JobSampleMetaDao extends WaspMetaDao<JobSampleMeta> {

  public JobSampleMeta getJobSampleMetaByJobSampleMetaId (final int jobSampleMetaId);

  public JobSampleMeta getJobSampleMetaByKJobsampleId (final String k, final int jobsampleId);





}

