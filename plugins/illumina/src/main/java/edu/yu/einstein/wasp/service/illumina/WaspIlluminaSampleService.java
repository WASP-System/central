package edu.yu.einstein.wasp.service.illumina;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.StatusMetaMessagingException;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.service.SampleService;

public interface WaspIlluminaSampleService extends SampleService {

	/**
	 * returns true if cell has passed focus QC, otherwise throws false, or a MetadataException if the value is not set
	 * @param cell
	 * @return
	 * @throws SampleTypeException, MetadataException
	 */
	public boolean isCellPassedFocusQc(Sample cell) throws SampleTypeException, MetadataException;

	/**
	 * sets focus QC pass (true) or fail (false) value
	 * @param cell
	 * @return
	 * @throws SampleTypeException, MetadataException
	 */
	public void setCellPassedFocusQc(Sample cell, boolean success) throws SampleTypeException, MetadataException;
	
	/**
	 * get comment for focus QC or null if none present
	 * @param cell
	 * @return
	 * @throws SampleTypeException
	 */
	public String getCellPassedFocusQcComment(Sample cell) throws SampleTypeException;

	/**
	 * Set comment for focus QC 
	 * @param cell
	 * @param comment
	 * @throws SampleTypeException
	 */
	public void setCellPassedFocusQcComment(Sample cell, String comment) throws SampleTypeException, StatusMetaMessagingException;

	/**
	 * returns true if cell has passed intensity QC, otherwise throws false, or a MetadataException if the value is not set
	 * @param cell
	 * @return
	 * @throws SampleTypeException, MetadataException
	 */
	public boolean isCellPassedIntensityQc(Sample cell) throws SampleTypeException, MetadataException;

	/**
	 * sets intensity QC pass (true) or fail (false) value
	 * @param cell
	 * @return
	 * @throws SampleTypeException, MetadataException
	 */
	public void setCellPassedIntensityQc(Sample cell, boolean success) throws SampleTypeException, MetadataException;
	
	/**
	 * get comment for intensity QC or null if none present
	 * @param cell
	 * @return
	 * @throws SampleTypeException
	 */
	public String getCellPassedIntensityQcComment(Sample cell) throws SampleTypeException;

	/**
	 * Set comment for intensity QC 
	 * @param cell
	 * @param comment
	 * @throws SampleTypeException
	 */
	public void setCellPassedIntensityQcComment(Sample cell, String comment) throws SampleTypeException, StatusMetaMessagingException;

	/**
	 * returns true if cell has passed num bases QS > 30 QC, otherwise throws false, or a MetadataException if the value is not set
	 * @param cell
	 * @return
	 * @throws SampleTypeException, MetadataException
	 */
	public boolean isCellPassedNumGt30Qc(Sample cell) throws SampleTypeException, MetadataException;

	/**
	 * sets num bases QS > 30 QC pass (true) or fail (false) value
	 * @param cell
	 * @return
	 * @throws SampleTypeException, MetadataException
	 */
	public void setCellPassedNumGt30Qc(Sample cell, boolean success) throws SampleTypeException, MetadataException;
	
	/**
	 * get comment for num bases QS > 30 QC or null if none present
	 * @param cell
	 * @return
	 * @throws SampleTypeException
	 */
	public String getCellPassedNumGt30QcComment(Sample cell) throws SampleTypeException;

	/**
	 * Set comment for num bases QS > 30 QC 
	 * @param cell
	 * @param comment
	 * @throws SampleTypeException
	 */
	public void setCellPassedNumGt30QcComment(Sample cell, String comment) throws SampleTypeException, StatusMetaMessagingException;

	/**
	 * returns true if cell has passed cluster density QC, otherwise throws false, or a MetadataException if the value is not set
	 * @param cell
	 * @return
	 * @throws SampleTypeException, MetadataException
	 */
	public boolean isCellPassedClusterDensityQc(Sample cell) throws SampleTypeException, MetadataException;
	
	/**
	 * sets cluster density QC pass (true) or fail (false) value
	 * @param cell
	 * @return
	 * @throws SampleTypeException, MetadataException
	 */
	public void setCellPassedClusterDensityQc(Sample cell, boolean success) throws SampleTypeException, MetadataException;

	/**
	 * get comment for cluster density QC or null if none present
	 * @param cell
	 * @return
	 * @throws SampleTypeException
	 */
	public String getCellPassedClusterDensityQcComment(Sample cell) throws SampleTypeException;

	/**
	 * Set comment for cluster density QC 
	 * @param cell
	 * @param comment
	 * @throws SampleTypeException
	 */
	public void setCellPassedClusterDensityQcComment(Sample cell, String comment) throws SampleTypeException, StatusMetaMessagingException;

}
