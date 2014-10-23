/**
 *
 * PDFService.java 
 * @author RDubin (10-17-14)
 *  
 * the PDFService interface
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.io.OutputStream;
import com.itextpdf.text.DocumentException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.quote.MPSQuote;

public interface PDFService extends WaspService {
	public void buildQuoteAsPDF(MPSQuote mpsQuote, Job job, OutputStream outputStream)throws DocumentException, MetadataException;	
	public void buildJobSampleReviewPDF(Job job, OutputStream outputStream)throws DocumentException, MetadataException;
}
