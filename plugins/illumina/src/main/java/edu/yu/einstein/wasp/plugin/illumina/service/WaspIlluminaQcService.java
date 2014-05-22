package edu.yu.einstein.wasp.plugin.illumina.service;

import java.util.List;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.StatusMetaMessagingException;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.plugin.illumina.util.IlluminaQcContext;
import edu.yu.einstein.wasp.service.WaspService;

public interface WaspIlluminaQcService extends WaspService {

	/**
	 * returns true if cell has passed QC for the chosen meta key, otherwise throws false, or a MetadataException if the value is not set
	 * @param cell
	 * @return
	 * @throws SampleTypeException, MetadataException
	 */
	public boolean isCellPassedQc(Sample cell, String metaKey) throws SampleTypeException, MetadataException;

	/**
	 * sets QC pass (true) or fail (false) value for chosen meta key
	 * @param cell
	 * @return
	 * @throws SampleTypeException, MetadataException
	 */
	public void setCellPassedQc(Sample cell, String metaKey, boolean success) throws SampleTypeException, MetadataException;
	
	/**
	 * get comment for QC or null if none present
	 * @param cell
	 * @return
	 * @throws SampleTypeException
	 */
	public String getCellQcComment(Sample cell, String metaKey) throws SampleTypeException, StatusMetaMessagingException;

	/**
	 * Set comment for QC 
	 * @param cell
	 * @param comment
	 * @throws SampleTypeException
	 */
	public void setCellQcComment(Sample cell, String metaKey, String comment) throws SampleTypeException, StatusMetaMessagingException;
	
	/**
	 * update database with provided qc data
	 * @param qcContextList
	 * @param metaKey
	 */
	public void updateQc(List<IlluminaQcContext> qcContextList, String metaKey);

	/**
	 * Get the QC data for a given cell and meta-key. Returns null if QC status boolean not set.
	 * @param cell
	 * @param metaKey
	 * @return
	 * @throws SampleTypeException
	 * @throws StatusMetaMessagingException
	 */
	public IlluminaQcContext getQc(Sample cell, String metaKey)	throws SampleTypeException, StatusMetaMessagingException;

}
