package edu.yu.einstein.wasp.service.impl.illumina;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.MetaMessage;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.StatusMetaMessagingException;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.service.MetaMessageService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.illumina.WaspIlluminaQcService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.util.MetaHelper;
import edu.yu.einstein.wasp.util.illumina.IlluminaQcContext;

@Component
@Transactional
public class WaspIlluminaQcServiceImpl extends WaspServiceImpl implements WaspIlluminaQcService{
	
	public static class CellSuccessQcMetaKey{
		private static final String META_AREA = "cell";
		public static final String FOCUS = "focusQC";
		public static final String INTENSITY = "intensityQC";
		public static final String NUMGT30 = "NumGt30QC";
		public static final String CLUSTER_DENSITY = "clusterDensityQC";
		
		public static boolean isMetaKey(String key){
			if (key == null) return false;
			if (key.equals(FOCUS)) return true;
			if (key.equals(INTENSITY)) return true;
			if (key.equals(NUMGT30)) return true;
			if (key.equals(CLUSTER_DENSITY)) return true;
			return false;
		}
	}
	
	private static final String CELL_SUCCESS_META_KEY_QC_COMMENT_GROUP = "RunQcComments";
	private static final String CELL_SUCCESS_META_KEY_QC_COMMENT_FOCUS = "Focus QC Comment";
	private static final String CELL_SUCCESS_META_KEY_QC_COMMENT_INTENSITY = "Intensity QC Comment";
	private static final String CELL_SUCCESS_META_KEY_QC_COMMENT_NUMGT30 = "Num Bases QS > 30 Comment";
	private static final String CELL_SUCCESS_META_KEY_QC_COMMENT_CLUSTER = "Cluster Density QC Comment";
	
	@Autowired
	SampleService sampleService;
	
	@Autowired
	MetaMessageService metaMessageService;
	
	@Autowired
	SampleMetaDao sampleMetaDao;
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public boolean isCellPassedQc(Sample cell, String metaKey) throws SampleTypeException, MetadataException{
		Assert.assertParameterNotNull(cell, "a cell must be provided");
		if (!CellSuccessQcMetaKey.isMetaKey(metaKey))
			throw new MetadataException("invalid meta key provided");
		if (!sampleService.isCell(cell))
			throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		String success = null;
		List<SampleMeta> sampleMetaList = cell.getSampleMeta();
		if (sampleMetaList == null)
			sampleMetaList = new ArrayList<SampleMeta>();
		success = (String) MetaHelper.getMetaValue(CellSuccessQcMetaKey.META_AREA, metaKey, sampleMetaList);
		Boolean b = new Boolean(success);
		return b.booleanValue();
	}
	
	/**
	 *  {@inheritDoc}
	 * 
	 */
	@Override
	public void setCellPassedQc(Sample cell, String metaKey, boolean success) throws SampleTypeException, MetadataException {
		Assert.assertParameterNotNull(cell, "a cell must be provided");
		if (!CellSuccessQcMetaKey.isMetaKey(metaKey))
			throw new MetadataException("invalid meta key provided");
		if (!sampleService.isCell(cell))
			throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		Boolean b = new Boolean(success);
		String successString = b.toString();
		SampleMeta sampleMeta = new SampleMeta();
		sampleMeta.setK(CellSuccessQcMetaKey.META_AREA + "." + metaKey);
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
		Assert.assertParameterNotNull(cell, "a cell must be provided");
		if (!CellSuccessQcMetaKey.isMetaKey(metaKey))
			throw new StatusMetaMessagingException("invalid meta key provided");
		if (!sampleService.isCell(cell))
			throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		Integer cellId = cell.getSampleId();
		List<MetaMessage> existingMessages = metaMessageService.read(CELL_SUCCESS_META_KEY_QC_COMMENT_GROUP, metaKey, cellId, SampleMeta.class, sampleMetaDao);
		if (existingMessages.isEmpty()){
			metaMessageService.saveToGroup(CELL_SUCCESS_META_KEY_QC_COMMENT_GROUP, metaKey, comment, cellId, SampleMeta.class, sampleMetaDao);
		} else {
			metaMessageService.edit(existingMessages.get(0), comment, cellId, SampleMeta.class, sampleMetaDao);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCellQcComment(Sample cell, String metaKey) throws SampleTypeException, StatusMetaMessagingException{
		Assert.assertParameterNotNull(cell, "a cell must be provided");
		if (!CellSuccessQcMetaKey.isMetaKey(metaKey))
			throw new StatusMetaMessagingException("invalid meta key provided");
		if (!sampleService.isCell(cell))
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
		if (!CellSuccessQcMetaKey.isMetaKey(metaKey))
			throw new RuntimeException("invalid metaKey provided");
		try{
			Assert.assertParameterNotNull(qcContextList, "a QC context list must be supplied");
			Assert.assertParameterNotNull(metaKey, "a metakey must be supplied");
			for (IlluminaQcContext qcContext : qcContextList){
				setCellPassedQc(qcContext.getCell(), metaKey, qcContext.isPassedQc());
				setCellQcComment(qcContext.getCell(), metaKey, qcContext.getComment());
			}
		} catch(Exception e){
			throw new RuntimeException(e); // throw runtime exceptions to invoke rollback on transaction
		}
	}
	
	/**
	 * get name for QC comments based on metaKey
	 * @param metaKey
	 * @return
	 * @throws StatusMetaMessagingException
	 */
	private String getQcCommentName(String metaKey) throws StatusMetaMessagingException{
		if (metaKey.equals(CellSuccessQcMetaKey.FOCUS))
			return CELL_SUCCESS_META_KEY_QC_COMMENT_FOCUS;
		if (metaKey.equals(CellSuccessQcMetaKey.INTENSITY))
			return CELL_SUCCESS_META_KEY_QC_COMMENT_INTENSITY;
		if (metaKey.equals(CellSuccessQcMetaKey.NUMGT30))
			return CELL_SUCCESS_META_KEY_QC_COMMENT_NUMGT30;
		if (metaKey.equals(CellSuccessQcMetaKey.CLUSTER_DENSITY))
			return CELL_SUCCESS_META_KEY_QC_COMMENT_CLUSTER;
		throw new StatusMetaMessagingException("Unable to determine key for setting status message");
	}

}
