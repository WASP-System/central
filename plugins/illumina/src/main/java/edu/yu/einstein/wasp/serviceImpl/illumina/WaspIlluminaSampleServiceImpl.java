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

@Component
@Transactional
public class WaspIlluminaSampleServiceImpl extends SampleServiceImpl implements WaspIlluminaSampleService{

	public static final String CELL_SUCCESS_META_KEY_FOCUS_QC = "focusQC";
	public static final String CELL_SUCCESS_META_KEY_INTENSITY_QC = "intensityQC";
	public static final String CELL_SUCCESS_META_KEY_NUMGT30_QC = "NumGt30QC";
	public static final String CELL_SUCCESS_META_KEY_CLUSTER_QC = "clusterDensityQC";
	
	private static final String CELL_SUCCESS_META_KEY_QC_COMMENT_GROUP = "RunQcComments";
	public static final String CELL_SUCCESS_META_KEY_QC_COMMENT_FOCUS = "Focus QC Comment";
	public static final String CELL_SUCCESS_META_KEY_QC_COMMENT_INTENSITY = "Intensity QC Comment";
	public static final String CELL_SUCCESS_META_KEY_QC_COMMENT_NUMGT30 = "Num Bases QS > 30 Comment";
	public static final String CELL_SUCCESS_META_KEY_QC_COMMENT_CLUSTER = "Cluster Density QC Comment";
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public boolean isCellPassedFocusQc(Sample cell) throws SampleTypeException, MetadataException{
		return isPassedQc(cell, CELL_SUCCESS_META_KEY_FOCUS_QC);
	}
	
	/**
	 *  {@inheritDoc}
	 * 
	 */
	@Override
	public void setCellPassedFocusQc(Sample cell, boolean success) throws SampleTypeException, MetadataException {
		setPassedQc(cell, CELL_SUCCESS_META_KEY_FOCUS_QC, success);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void setCellPassedFocusQcComment(Sample cell, String comment) throws SampleTypeException, StatusMetaMessagingException{
		if (!isCell(cell))
			throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		metaMessageService.saveToGroup(CELL_SUCCESS_META_KEY_QC_COMMENT_GROUP, CELL_SUCCESS_META_KEY_QC_COMMENT_FOCUS, comment, cell.getSampleId(), SampleMeta.class, sampleMetaDao);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCellPassedFocusQcComment(Sample cell) throws SampleTypeException{
		if (!isCell(cell))
			throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		List<MetaMessage> messages =  metaMessageService.read(CELL_SUCCESS_META_KEY_QC_COMMENT_GROUP, CELL_SUCCESS_META_KEY_QC_COMMENT_FOCUS, cell.getSampleId(), SampleMeta.class, sampleMetaDao);
		if (messages.isEmpty())
			return null;
		return messages.get(0).getValue(); // should only be one message
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public boolean isCellPassedIntensityQc(Sample cell) throws SampleTypeException, MetadataException{
		return isPassedQc(cell, CELL_SUCCESS_META_KEY_INTENSITY_QC);
	}
	
	/**
	 *  {@inheritDoc}
	 * @throws MetadataException 
	 */
	@Override
	public void setCellPassedIntensityQc(Sample cell, boolean success) throws SampleTypeException, MetadataException {
		setPassedQc(cell, CELL_SUCCESS_META_KEY_INTENSITY_QC, success);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void setCellPassedIntensityQcComment(Sample cell, String comment) throws SampleTypeException, StatusMetaMessagingException{
		if (!isCell(cell))
			throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		metaMessageService.saveToGroup(CELL_SUCCESS_META_KEY_QC_COMMENT_GROUP, CELL_SUCCESS_META_KEY_QC_COMMENT_INTENSITY, comment, cell.getSampleId(), SampleMeta.class, sampleMetaDao);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCellPassedIntensityQcComment(Sample cell) throws SampleTypeException{
		if (!isCell(cell))
			throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		List<MetaMessage> messages =  metaMessageService.read(CELL_SUCCESS_META_KEY_QC_COMMENT_GROUP, CELL_SUCCESS_META_KEY_QC_COMMENT_INTENSITY, cell.getSampleId(), SampleMeta.class, sampleMetaDao);
		if (messages.isEmpty())
			return null;
		return messages.get(0).getValue(); // should only be one message
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public boolean isCellPassedNumGt30Qc(Sample cell) throws SampleTypeException, MetadataException{
		return isPassedQc(cell, CELL_SUCCESS_META_KEY_NUMGT30_QC);
	}
	
	/**
	 *  {@inheritDoc}
	 * @throws MetadataException 
	 */
	@Override
	public void setCellPassedNumGt30Qc(Sample cell, boolean success) throws SampleTypeException, MetadataException {
		setPassedQc(cell, CELL_SUCCESS_META_KEY_NUMGT30_QC, success);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void setCellPassedNumGt30QcComment(Sample cell, String comment) throws SampleTypeException, StatusMetaMessagingException{
		if (!isCell(cell))
			throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		metaMessageService.saveToGroup(CELL_SUCCESS_META_KEY_QC_COMMENT_GROUP, CELL_SUCCESS_META_KEY_QC_COMMENT_NUMGT30, comment, cell.getSampleId(), SampleMeta.class, sampleMetaDao);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCellPassedNumGt30QcComment(Sample cell) throws SampleTypeException{
		if (!isCell(cell))
			throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		List<MetaMessage> messages =  metaMessageService.read(CELL_SUCCESS_META_KEY_QC_COMMENT_GROUP, CELL_SUCCESS_META_KEY_QC_COMMENT_NUMGT30, cell.getSampleId(), SampleMeta.class, sampleMetaDao);
		if (messages.isEmpty())
			return null;
		return messages.get(0).getValue(); // should only be one message
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public boolean isCellPassedClusterDensityQc(Sample cell) throws SampleTypeException, MetadataException{
		return isPassedQc(cell, CELL_SUCCESS_META_KEY_CLUSTER_QC);
	}
	
	/**
	 *  {@inheritDoc}
	 * @throws MetadataException 
	 */
	@Override
	public void setCellPassedClusterDensityQc(Sample cell, boolean success) throws SampleTypeException, MetadataException {
		setPassedQc(cell, CELL_SUCCESS_META_KEY_CLUSTER_QC, success);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void setCellPassedClusterDensityQcComment(Sample cell, String comment) throws SampleTypeException, StatusMetaMessagingException{
		if (!isCell(cell))
			throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		metaMessageService.saveToGroup(CELL_SUCCESS_META_KEY_QC_COMMENT_GROUP, CELL_SUCCESS_META_KEY_QC_COMMENT_CLUSTER, comment, cell.getSampleId(), SampleMeta.class, sampleMetaDao);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCellPassedClusterDensityQcComment(Sample cell) throws SampleTypeException{
		if (!isCell(cell))
			throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		List<MetaMessage> messages =  metaMessageService.read(CELL_SUCCESS_META_KEY_QC_COMMENT_GROUP, CELL_SUCCESS_META_KEY_QC_COMMENT_CLUSTER, cell.getSampleId(), SampleMeta.class, sampleMetaDao);
		if (messages.isEmpty())
			return null;
		return messages.get(0).getValue(); // should only be one message
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update
	
	
	
	private boolean isPassedQc(Sample cell, String metaKey) throws SampleTypeException, MetadataException{
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
	
	private void setPassedQc(Sample cell, String metaKey, boolean success) throws SampleTypeException, MetadataException {
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

}
