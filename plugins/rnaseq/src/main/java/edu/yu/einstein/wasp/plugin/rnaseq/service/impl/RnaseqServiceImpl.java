/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.rnaseq.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.plugin.rnaseq.service.RnaseqService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.util.MetaHelper;

@Service
@Transactional("entityManager")
public class RnaseqServiceImpl extends WaspServiceImpl implements RnaseqService {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String performAction() {
		// do something
		return "done";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getRNAFraction(Sample sample){
		try{
			return MetaHelper.getMetaValue(RNASEQ_RNA_AREA, FRACTION_META_KEY, sample.getSampleMeta());
		}
		catch(Exception e){return "";}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getRibosomeDepletionMethod(Sample sample){
		
		try{
			if(sample.getSampleType().getIName().toLowerCase().endsWith("library")){
				return MetaHelper.getMetaValue(RNASEQ_LIBRARY_AREA, RIBOSOME_DEPLETION_META_KEY, sample.getSampleMeta());
			}
			else{
				return MetaHelper.getMetaValue(RNASEQ_CDNA_AREA, RIBOSOME_DEPLETION_META_KEY, sample.getSampleMeta());
			}
		}
		catch(Exception e){return "";}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDirectionality(Sample sample){
		try{
			if(sample.getSampleType().getIName().toLowerCase().endsWith("library")){
				return MetaHelper.getMetaValue(RNASEQ_LIBRARY_AREA, DIRECTIONALITY_META_KEY, sample.getSampleMeta());
			}
			else if(sample.getSampleType().getIName().equalsIgnoreCase("cdna")){
				return MetaHelper.getMetaValue(RNASEQ_CDNA_AREA, DIRECTIONALITY_META_KEY, sample.getSampleMeta());
			}
			else if(sample.getSampleType().getIName().equalsIgnoreCase("rna")){
				return MetaHelper.getMetaValue(RNASEQ_RNA_AREA, DIRECTIONALITY_META_KEY, sample.getSampleMeta());
			}
		}
		catch(Exception e){}
		return "";
	}
}
