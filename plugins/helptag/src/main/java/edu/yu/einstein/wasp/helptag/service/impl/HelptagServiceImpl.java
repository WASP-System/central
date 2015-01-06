/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.helptag.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.FileTypeDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.helptag.service.HelptagService;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.service.JobDraftService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.util.MetaHelper;

@Service
@Transactional("entityManager")
public class HelptagServiceImpl extends WaspServiceImpl implements HelptagService {
	
	@Autowired
	private JobDraftService jobDraftService;
	
	@Autowired
	private FileTypeDao fileTypeDao;

	@Autowired
	private SampleService sampleService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String performAction() {
		// do something
		return "done";
	}

	@Override
	public List<SampleDraft> getAllMspISampleDraftsFromJobDraftId(Integer id) {
		JobDraft jobDraft = jobDraftService.getJobDraftById(id);
		List<SampleDraft> sampleDrafts = jobDraft.getSampleDraft();
		
		List<SampleDraft> mspSampleDrafts = new ArrayList<SampleDraft>();
		String enzymeString;
		for (SampleDraft sd : sampleDrafts) {
			try{
				enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_LIB_AREA, RESTRICTION_ENZYME_META_KEY, sd.getSampleDraftMeta());
				if (enzymeString.equals("MspI"))
					mspSampleDrafts.add(sd);
			} catch(MetadataException e) {
				// not found
				logger.debug("Restriction Enzyme Meta is not found for Sample Draft id = " + id);
			}
		}

		return mspSampleDrafts;
	}

	@Override
	public List<SampleDraft> getAllHpaIISampleDraftsFromJobDraftId(Integer id) {
		JobDraft jobDraft = jobDraftService.getJobDraftById(id);
		List<SampleDraft> sampleDrafts = jobDraft.getSampleDraft();
		
		List<SampleDraft> hpaSampleDrafts = new ArrayList<SampleDraft>();
		String enzymeString;
		for (SampleDraft sd : sampleDrafts) {
			try{
				enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_LIB_AREA, RESTRICTION_ENZYME_META_KEY, sd.getSampleDraftMeta());
				if (enzymeString.equals("HpaII"))
					hpaSampleDrafts.add(sd);
			} catch(MetadataException e) {
				// not found
				logger.debug("Restriction Enzyme Meta is not found for Sample Draft id = " + id);
			}
		}

		return hpaSampleDrafts;
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional("entityManager")
	@Override
	public boolean isHpaII(Integer sampleId) {
		Sample s = sampleService.getSampleById(sampleId);
		try {
			String enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_LIB_AREA, RESTRICTION_ENZYME_META_KEY, s.getSampleMeta());
			if (enzymeString.equals("HpaII"))
				return true;
		} catch (MetadataException e) {
			// not found
			logger.debug("Restriction Enzyme Meta is not found for Sample id = " + sampleId);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional("entityManager")
	@Override
	public boolean isMspI(Integer sampleId) {
		Sample s = sampleService.getSampleById(sampleId);
		try {
			String enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_LIB_AREA, RESTRICTION_ENZYME_META_KEY, s.getSampleMeta());
			if (enzymeString.equals("MspI"))
				return true;
		} catch (MetadataException e) {
			// not found
			logger.debug("Restriction Enzyme Meta is not found for Sample id = " + sampleId);
		}
		return false;
	}

}
