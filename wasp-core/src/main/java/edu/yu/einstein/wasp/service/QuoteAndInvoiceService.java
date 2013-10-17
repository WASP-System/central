/**
 *
 * QuoteAndInvoiceService.java 
 * @author RDubin (09-18-13)
 *  
 * the LabService interface
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.io.OutputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;

import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.quote.MPSQuote;

@Service
public interface QuoteAndInvoiceService extends WaspService{

	public void buildQuoteAsPDF(MPSQuote mpsQuote, Job job, OutputStream outputStream)throws DocumentException, MetadataException;	

}
