/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.helptag.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.controller.util.SampleAndSampleDraftMetaHelper;
import edu.yu.einstein.wasp.dao.FileTypeDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.MetadataTypeException;
import edu.yu.einstein.wasp.helptag.service.HelptagService;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleDraftMeta;
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
				if(sd.getSampleType().getIName().equalsIgnoreCase("library")){
					enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_LIB_AREA, RESTRICTION_ENZYME_META_KEY, sd.getSampleDraftMeta());
				}
				else{//genomic DNA; 1-8-15; dubin
					enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_DNA_AREA, TYPE_OF_HELP_LIBRARY_REQUESTED_META_KEY, sd.getSampleDraftMeta());					
				}
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
				if(sd.getSampleType().getIName().equalsIgnoreCase("library")){
					enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_LIB_AREA, RESTRICTION_ENZYME_META_KEY, sd.getSampleDraftMeta());
				}
				else{//genomic DNA; 12-30-14
					enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_DNA_AREA, TYPE_OF_HELP_LIBRARY_REQUESTED_META_KEY, sd.getSampleDraftMeta());					
				}
				if (enzymeString.equals("HpaII"))
					hpaSampleDrafts.add(sd);
			} catch(MetadataException e) {
				// not found
				logger.debug("Restriction Enzyme Meta is not found for Sample Draft id = " + id);
			}
		}

		return hpaSampleDrafts;
	}
	@Override
	public List<String> getLibrariesToCreateList(List<SampleDraftMeta> sampleDraftMetaList){
		List<String> libraryToCreateList = new ArrayList<String>();
		for(SampleDraftMeta sdm : sampleDraftMetaList){
			if(sdm.getK().endsWith("libraryToCreate")){
				String [] stringArray = StringUtils.split(sdm.getV(),",");
				for(int i = 0; i < stringArray.length; i++){
					libraryToCreateList.add(stringArray[i].trim());
				}
				break;
			}
		}
		return libraryToCreateList;
	}
	@Override
	public List<SampleDraft> createNewHelpDNASampleDrafts(SampleDraft sampleDraft, List<String> librariesToCreateList){
		List<SampleDraft> newSampleDrafts = new ArrayList<SampleDraft>();
		for(String libraryTypeToCreate : librariesToCreateList){
			SampleDraft clone = sampleService.cloneSampleDraft(sampleDraft);
			List<SampleDraftMeta> sampleDraftMetaList = clone.getSampleDraftMeta();
			clone.setName(clone.getName() + "_" + libraryTypeToCreate);	
			SampleDraft cloneDB = sampleService.getSampleDraftDao().save(clone);
			cloneDB.setSampleDraftMeta(sampleDraftMetaList);
			this.resetLibraryToCreateMetaData(cloneDB, libraryTypeToCreate);
			newSampleDrafts.add(cloneDB);
		}		
		return newSampleDrafts;
	}
	private void resetLibraryToCreateMetaData(SampleDraft clone, String libraryTypeToCreate){
		for(SampleDraftMeta sdm : clone.getSampleDraftMeta()){
			if(sdm.getK().endsWith("typeOfHelpLibraryRequested")){
				sdm.setV(libraryTypeToCreate);
			}
			sdm.setSampleDraftId(clone.getId());//must do this for every meta entry
			sampleService.getSampleDraftMetaDao().save(sdm);//then must do this for every meta entry
		}
	}
	public List<SampleDraft> getAllHpaIIAndbetaGTMspISampleDraftsFromJobDraftId(Integer id){
		JobDraft jobDraft = jobDraftService.getJobDraftById(id);
		List<SampleDraft> sampleDrafts = jobDraft.getSampleDraft();
		
		List<SampleDraft> hpaAndbetaGTMspSampleDrafts = new ArrayList<SampleDraft>();
		String enzymeString;
		for (SampleDraft sd : sampleDrafts) {
			try{
				if(sd.getSampleType().getIName().equalsIgnoreCase("library")){
					enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_LIB_AREA, RESTRICTION_ENZYME_META_KEY, sd.getSampleDraftMeta());
				}
				else{//genomic DNA; 1-8-15; dubin
					enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_DNA_AREA, TYPE_OF_HELP_LIBRARY_REQUESTED_META_KEY, sd.getSampleDraftMeta());					
				}
				if (enzymeString.equals("HpaII")||enzymeString.equals("beta-GT-MspI"))
					hpaAndbetaGTMspSampleDrafts.add(sd);
			} catch(MetadataException e) {
				// not found
				logger.debug("Restriction Enzyme Meta (and libraryToCreate meta) is not found for Sample Draft id = " + id);
			}
		}

		return hpaAndbetaGTMspSampleDrafts;
	}

	public List<SampleDraft> getAllbetaGTMspISampleDraftsFromJobDraftId(Integer id){
		JobDraft jobDraft = jobDraftService.getJobDraftById(id);
		List<SampleDraft> sampleDrafts = jobDraft.getSampleDraft();
		
		List<SampleDraft> betaGTMspSampleDrafts = new ArrayList<SampleDraft>();
		String enzymeString;
		for (SampleDraft sd : sampleDrafts) {
			try{
				if(sd.getSampleType().getIName().equalsIgnoreCase("library")){
					enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_LIB_AREA, RESTRICTION_ENZYME_META_KEY, sd.getSampleDraftMeta());
				}
				else{//genomic DNA; 1-8-15; dubin
					enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_DNA_AREA, TYPE_OF_HELP_LIBRARY_REQUESTED_META_KEY, sd.getSampleDraftMeta());					
				}
				if (enzymeString.equals("beta-GT-MspI"))
					betaGTMspSampleDrafts.add(sd);
			} catch(MetadataException e) {
				// not found
				logger.debug("Restriction Enzyme Meta (and libraryToCreate meta) is not found for Sample Draft id = " + id);
			}
		}
		return betaGTMspSampleDrafts;
	}
	public String getGlycosylationStatusBeforeSubmission(Sample sample){
		try{
			return (String) MetaHelper.getMetaValue(HELPTAG_DNA_AREA, GLYCOSYLATED_BEFORE_SUBMISSION_META_KEY, sample.getSampleMeta());					
		} catch(MetadataException e) {}
		try{
			return (String) MetaHelper.getMetaValue(HELPTAG_LIB_AREA, GLYCOSYLATED_BEFORE_SUBMISSION_META_KEY, sample.getSampleMeta());
		}catch(Exception e){}		
		// not found
		return null;
	}
	public String getRestrictionStatusBeforeSubmission(Sample sample){
		try{
			return (String) MetaHelper.getMetaValue(HELPTAG_DNA_AREA, RESTRICTED_BEFORE_SUBMISSION_META_KEY, sample.getSampleMeta());					
		} catch(MetadataException e) {}
		try{
			return (String) MetaHelper.getMetaValue(HELPTAG_LIB_AREA, RESTRICTED_BEFORE_SUBMISSION_META_KEY, sample.getSampleMeta());
		}catch(Exception e){}		
		// not found
		return null;
	}
	public String getTypeOfHelpLibraryRequestedForMacromolecule(Sample sample){
		try{
			return (String) MetaHelper.getMetaValue(HELPTAG_DNA_AREA, TYPE_OF_HELP_LIBRARY_REQUESTED_META_KEY, sample.getSampleMeta());					
		} catch(MetadataException e) {}	
		// not found
		return null;
	}
	public String getTypeOfHelpLibraryForLibrary(Sample sample){
		try{
			return (String) MetaHelper.getMetaValue(HELPTAG_LIB_AREA, RESTRICTION_ENZYME_META_KEY, sample.getSampleMeta());					
		} catch(MetadataException e) {}	
		// not found
		return null;
		
	}
	public boolean isHpaII(Sample s){
		String enzymeString;
		try{
			if(s.getSampleType().getIName().equalsIgnoreCase("library")){
				enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_LIB_AREA, RESTRICTION_ENZYME_META_KEY, s.getSampleMeta());
			}
			else{//genomic DNA; 1-8-15; dubin
				enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_DNA_AREA, TYPE_OF_HELP_LIBRARY_REQUESTED_META_KEY, s.getSampleMeta());					
			}
			if (enzymeString.equals("HpaII")){
				return true;
			}
		} catch(MetadataException e) {
			// not found
			logger.debug("Restriction Enzyme Meta (and libraryToCreate meta) not found for Sample id = " + s.getId());
		}
		return false;
	}
	public boolean isBetaGTMspI(Sample s){
		String enzymeString;
		try{
			if(s.getSampleType().getIName().equalsIgnoreCase("library")){
				enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_LIB_AREA, RESTRICTION_ENZYME_META_KEY, s.getSampleMeta());
			}
			else{//genomic DNA; 1-8-15; dubin
				enzymeString = (String) MetaHelper.getMetaValue(HELPTAG_DNA_AREA, TYPE_OF_HELP_LIBRARY_REQUESTED_META_KEY, s.getSampleMeta());					
			}
			if (enzymeString.equals("HpaII")){
				return true;
			}
		} catch(MetadataException e) {
			// not found
			logger.debug("Restriction Enzyme Meta (and libraryToCreate meta) not found for Sample id = " + s.getId());
		}
		return false;
	}
	
	
	//1-21-15
	@Override
	public List<String> getTypeOfHelpLibrariesRequestedList(List<SampleDraftMeta> sampleDraftMetaList){
		List<String> typeOfHelpLibrariesRequestedList = new ArrayList<String>();
		for(SampleDraftMeta sdm : sampleDraftMetaList){
			if(sdm.getK().endsWith("typeOfHelpLibraryRequested")){
				String [] stringArray = StringUtils.split(sdm.getV(),",");
				for(int i = 0; i < stringArray.length; i++){
					typeOfHelpLibrariesRequestedList.add(stringArray[i].trim());
				}
				break;
			}
		}
		return typeOfHelpLibrariesRequestedList;
	}
	
	
}
