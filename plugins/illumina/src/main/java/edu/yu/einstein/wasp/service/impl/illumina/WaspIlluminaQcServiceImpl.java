package edu.yu.einstein.wasp.service.impl.illumina;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.illumina.WaspIlluminaQcService;
import edu.yu.einstein.wasp.service.impl.SampleServiceImpl.CellSuccessMeta;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.util.MetaHelper;
import edu.yu.einstein.wasp.util.illumina.IlluminaQcContext;

@Service
@Transactional("entityManager")
public class WaspIlluminaQcServiceImpl extends WaspServiceImpl implements WaspIlluminaQcService{
	
	public static class CellSuccessQcMetaKey extends CellSuccessMeta{
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
			if (key.equals(RUN_SUCCESS)) return true;
			return false;
		}
		
	}
	
	private static class CellSuccessQcCommentMeta {
		public static final String GROUP = "RunQcComments";
		public static final String FOCUS = "Focus QC Comment";
		public static final String INTENSITY = "Intensity QC Comment";
		public static final String NUMGT30 = "Num Bases QS > 30 Comment";
		public static final String CLUSTER_DENSITY = "Cluster Density QC Comment";
		public static final String LANE = "Overal Lane QC Comment";
	}
	
	@Autowired
	SampleService sampleService;
	
	@Autowired
	RunService runService;
	
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
		success = (String) MetaHelper.getMetaValue(CellSuccessQcMetaKey.AREA, metaKey, sampleMetaList);
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
		sampleMeta.setK(CellSuccessQcMetaKey.AREA + "." + metaKey);
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
		List<MetaMessage> existingMessages = metaMessageService.read(CellSuccessQcCommentMeta.GROUP, metaKey, cellId, SampleMeta.class, sampleMetaDao);
		if (existingMessages.isEmpty()){
			metaMessageService.saveToGroup(CellSuccessQcCommentMeta.GROUP, metaKey, comment, cellId, SampleMeta.class, sampleMetaDao);
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
		List<MetaMessage> messages =  metaMessageService.read(CellSuccessQcCommentMeta.GROUP, metaKey, cell.getSampleId(), SampleMeta.class, sampleMetaDao);
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
	 * {@inheritDoc}
	 */
	@Override
	public IlluminaQcContext getQc(Sample cell, String metaKey) throws SampleTypeException, StatusMetaMessagingException{
		Assert.assertParameterNotNull(cell, "a cell must be provided");
		IlluminaQcContext qcContext = new IlluminaQcContext();
		qcContext.setCell(cell);
		try {
			qcContext.setPassedQc(isCellPassedQc(cell, metaKey));
		} catch (MetadataException e) {
			return null;
		}
		String comment = getCellQcComment(cell, metaKey);
		if (comment == null)
			comment = "";
		qcContext.setComment(comment);
		return qcContext;
	}
	
	/**
	 * get name for QC comments based on metaKey
	 * @param metaKey
	 * @return
	 * @throws StatusMetaMessagingException
	 */
	private String getQcCommentName(String metaKey) throws StatusMetaMessagingException{
		if (metaKey.equals(CellSuccessQcMetaKey.FOCUS))
			return CellSuccessQcCommentMeta.FOCUS;
		if (metaKey.equals(CellSuccessQcMetaKey.INTENSITY))
			return CellSuccessQcCommentMeta.INTENSITY;
		if (metaKey.equals(CellSuccessQcMetaKey.NUMGT30))
			return CellSuccessQcCommentMeta.NUMGT30;
		if (metaKey.equals(CellSuccessQcMetaKey.CLUSTER_DENSITY))
			return CellSuccessQcCommentMeta.CLUSTER_DENSITY;
		if (metaKey.equals(CellSuccessQcMetaKey.RUN_SUCCESS))
			return CellSuccessQcCommentMeta.LANE;
		throw new StatusMetaMessagingException("Unable to determine key for setting status message");
	}

}
