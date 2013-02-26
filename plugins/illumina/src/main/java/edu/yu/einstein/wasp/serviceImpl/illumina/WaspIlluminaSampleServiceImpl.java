package edu.yu.einstein.wasp.serviceImpl.illumina;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.MetaMessage;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.StatusMetaMessagingException;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.service.illumina.WaspIlluminaSampleService;
import edu.yu.einstein.wasp.service.impl.SampleServiceImpl;
import edu.yu.einstein.wasp.util.MetaHelper;
import edu.yu.einstein.wasp.util.illumina.IlluminaQcContext;

@Component
@Transactional
public class WaspIlluminaSampleServiceImpl extends SampleServiceImpl implements WaspIlluminaSampleService{

	public static final String CELL_SUCCESS_META_KEY_FOCUS_QC = "focusQC";
	public static final String CELL_SUCCESS_META_KEY_INTENSITY_QC = "intensityQC";
	public static final String CELL_SUCCESS_META_KEY_NUMGT30_QC = "NumGt30QC";
	public static final String CELL_SUCCESS_META_KEY_CLUSTER_QC = "clusterDensityQC";
	
	private static final String CELL_SUCCESS_META_KEY_QC_COMMENT_GROUP = "RunQcComments";
	private static final String CELL_SUCCESS_META_KEY_QC_COMMENT_FOCUS = "Focus QC Comment";
	private static final String CELL_SUCCESS_META_KEY_QC_COMMENT_INTENSITY = "Intensity QC Comment";
	private static final String CELL_SUCCESS_META_KEY_QC_COMMENT_NUMGT30 = "Num Bases QS > 30 Comment";
	private static final String CELL_SUCCESS_META_KEY_QC_COMMENT_CLUSTER = "Cluster Density QC Comment";
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public boolean isCellPassedQc(Sample cell, String metaKey) throws SampleTypeException, MetadataException{
		if (!isCell(cell))
			throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		String success = null;
		List<SampleMeta> sampleMetaList = cell.getSampleMeta();
		if (sampleMetaList == null)
			sampleMetaList = new ArrayList<SampleMeta>();
		success = (String) MetaHelper.getMetaValue(CELL_SUCCESS_META_AREA, metaKey, sampleMetaList);
		Boolean b = new Boolean(success);
		return b.booleanValue();
	}
	
	/**
	 *  {@inheritDoc}
	 * 
	 */
	@Override
	public void setCellPassedQc(Sample cell, String metaKey, boolean success) throws SampleTypeException, MetadataException {
		if (!isCell(cell))
			throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		Boolean b = new Boolean(success);
		String successString = b.toString();
		SampleMeta sampleMeta = new SampleMeta();
		sampleMeta.setK(CELL_SUCCESS_META_AREA + "." + metaKey);
		sampleMeta.setV(successString);
		sampleMeta.setSampleId(cell.getSampleId());
		sampleMetaDao.setMeta(sampleMeta);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void setCellQcComment(Sample cell, String metaKey, String comment) throws SampleTypeException, StatusMetaMessagingException{
		if (!isCell(cell))
			throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		metaMessageService.saveToGroup(CELL_SUCCESS_META_KEY_QC_COMMENT_GROUP, metaKey, comment, cell.getSampleId(), SampleMeta.class, sampleMetaDao);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCellQcComment(Sample cell, String metaKey) throws SampleTypeException, StatusMetaMessagingException{
		if (!isCell(cell))
			throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		List<MetaMessage> messages =  metaMessageService.read(CELL_SUCCESS_META_KEY_QC_COMMENT_GROUP, metaKey, cell.getSampleId(), SampleMeta.class, sampleMetaDao);
		if (messages.isEmpty())
			throw new StatusMetaMessagingException("No message found for given key: " + metaKey);
		return messages.get(0).getValue(); // should only be one message
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateQc(List<IlluminaQcContext> qcContextList, String metaKey){
		//TODO: functionality here
	}
	
	private String getQcCommentKey(String metaKey) throws StatusMetaMessagingException{
		if (metaKey.equals(CELL_SUCCESS_META_KEY_FOCUS_QC))
			return CELL_SUCCESS_META_KEY_QC_COMMENT_FOCUS;
		if (metaKey.equals(CELL_SUCCESS_META_KEY_INTENSITY_QC))
			return CELL_SUCCESS_META_KEY_QC_COMMENT_INTENSITY;
		if (metaKey.equals(CELL_SUCCESS_META_KEY_NUMGT30_QC))
			return CELL_SUCCESS_META_KEY_QC_COMMENT_NUMGT30;
		if (metaKey.equals(CELL_SUCCESS_META_KEY_CLUSTER_QC))
			return CELL_SUCCESS_META_KEY_QC_COMMENT_CLUSTER;
		throw new StatusMetaMessagingException("Unable to determine key for setting status message");
	}

}
