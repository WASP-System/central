
/**
 *
 * AccountsServiceImpl.java 
 * @author RDubin
 *  
 * the AccountsServiceImpl object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

//import edu.yu.einstein.wasp.controller.util.SampleAndSampleDraftMetaHelper;
import edu.yu.einstein.wasp.dao.AcctGrantDao;
import edu.yu.einstein.wasp.dao.AcctGrantjobDao;
import edu.yu.einstein.wasp.dao.AcctGrantjobDraftDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.AcctGrant;
import edu.yu.einstein.wasp.model.AcctGrantjob;
import edu.yu.einstein.wasp.model.AcctGrantjobDraft;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserMeta;
import edu.yu.einstein.wasp.quote.AdditionalCost;
import edu.yu.einstein.wasp.quote.Comment;
import edu.yu.einstein.wasp.quote.Discount;
import edu.yu.einstein.wasp.quote.LibraryCost;
import edu.yu.einstein.wasp.quote.MPSQuote;
import edu.yu.einstein.wasp.quote.SequencingCost;
import edu.yu.einstein.wasp.service.AccountsService;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.util.MetaHelper;

@Service
@Transactional("entityManager")
public class AccountsServiceImpl extends WaspServiceImpl implements AccountsService{

	public static final Font BIG_BOLD =  new Font(FontFamily.TIMES_ROMAN, 13, Font.BOLD );
	public static final Font NORMAL =  new Font(FontFamily.TIMES_ROMAN, 11 );
	public static final Font NORMAL_BOLD =  new Font(FontFamily.TIMES_ROMAN, 11, Font.BOLD );
	public static final Font TINY_BOLD =  new Font(FontFamily.TIMES_ROMAN, 8, Font.BOLD );
	public static final Font TINY_NORMAL =  new Font(FontFamily.TIMES_ROMAN, 8 );
	public static final Font SMALL_BOLD =  new Font(FontFamily.TIMES_ROMAN, 9, Font.BOLD );
	public static final Font SMALL_NORMAL =  new Font(FontFamily.TIMES_ROMAN, 9 );

	@Autowired
	private JobService jobService;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private AcctGrantDao acctGrantDao;
	
	@Autowired
	private AcctGrantjobDao acctGrantjobDao;
	
	@Autowired
	private AcctGrantjobDraftDao acctGrantjobDraftDao;
	
	@Autowired
	private AdaptorService adaptorService;

	public void buildQuoteAsPDF(MPSQuote mpsQuote, Job job, OutputStream outputStream)throws DocumentException, MetadataException{
		
		Document document = new Document();
 	    PdfWriter.getInstance(document, outputStream).setInitialLeading(10);
 	    document.open();	 	    
 	    List<String> justUnderLetterheadLineList = new ArrayList<String>();
 	    justUnderLetterheadLineList.add("Shahina Maqbool PhD (ESF Director), Albert Einstein College of Medicine, 1301 Morris Park Ave (Price 159F)");
 	    justUnderLetterheadLineList.add("Email:shahina.maqbool@einstein.yu.edu Phone:718-678-1163");
 	    String imageLocation = "/Users/robertdubin/Documents/images/Einstein_Logo.png";
 	    String title = "Epigenomics Shared Facility";
 	    addLetterhead(document, imageLocation, title, justUnderLetterheadLineList);
 	    Date now = new Date();
 	    DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
 	    addNoteLine(document, "", dateFormat.format(now));
 	    addressTheLetterToSubmitterAndPI(document, job);
 	    addNoteLine(document, "Re.: ", "Estimated costs for Job ID " + mpsQuote.getJobId());
 	    document.add(new LineSeparator());
 	    Paragraph jobDetailsParagraph = startJobDetailsParagraphAndAddCommonJobDetails(job);//start new paragraph containing common job details (put the paragraph is NOT added to the document in this method, thus permitting more to be added to it)
 	    jobDetailsParagraph = addMPSDetailsToJobDetailsParagraph(job, jobDetailsParagraph);//add msp-specific info to the jobDetails paragraph
 	    document.add(jobDetailsParagraph);//add the paragraph to the document
 	    
 	    Integer libraryConstructionTotalCost = addSubmittedSamplesMultiplexRequestAndLibraryCostsAsTable(document, mpsQuote);
 	    Integer sequenceRunsTotalCost = addSequenceRunsAndCostAsTable(document, mpsQuote);
 	    Integer additionalTotalCost = addAdditionalCostsAsTable(document, mpsQuote);
 	    
 	    addCommentsAsTable(document, mpsQuote);
 	   
 	    List<String> costReasonList = new ArrayList<String>();
 	    Map<String, Integer> costReasonPriceMap = new HashMap<String, Integer>();
 	    costReasonList.add("Total Library Costs");
 	    costReasonPriceMap.put("Total Library Costs",libraryConstructionTotalCost);
 	    costReasonList.add("Total Sequencing Costs");
 	    costReasonPriceMap.put("Total Sequencing Costs",sequenceRunsTotalCost);
 	    if(additionalTotalCost>0){
 	    	costReasonList.add("Total Additional Costs");
 	    	costReasonPriceMap.put("Total Additional Costs",additionalTotalCost);
 	    } 
 	    Integer totalFinalCost = addCostSummaryTable(document, mpsQuote, costReasonList, costReasonPriceMap);
 	    mpsQuote.setTotalFinalCost(totalFinalCost);
 	    
 	    document.close();		
	}

	private void addLetterhead(Document document, String imageLocation, String title, List<String> justUnderLetterheadLineList) throws DocumentException{
		
		 try{
	 	    	Image image = Image.getInstance(imageLocation);
	 	    	if(image != null){
	 	    		image.setAlignment(Image.MIDDLE);
	 	    		image.scaleToFit(1000, 50);//72 is about 1 inch in height
	 	    		document.add(image);
	 	    	}
	 	    }catch(Exception e){}
	 	    
	 	    Paragraph letterTitle = new Paragraph();
	 	    letterTitle.add(new Chunk(title, BIG_BOLD));
	 	    document.add(letterTitle);	 	    
	 	      	      
	 	    LineSeparator line = new LineSeparator(); 
	 	    line.setOffset(new Float(-5.0));
	 	    document.add(line);

	 	    if(!justUnderLetterheadLineList.isEmpty()){
	 	    	Paragraph justUnderLetterhead = new Paragraph(); 
	 	    	justUnderLetterhead.setSpacingBefore(4);
	 	    	justUnderLetterhead.setSpacingAfter(15);
	 	    	justUnderLetterhead.setLeading(10);
	 	    	for(String text : justUnderLetterheadLineList){
	 	    		Chunk textUnderTheLetterheadLine = new Chunk(text, TINY_BOLD);
	 	    		justUnderLetterhead.add(textUnderTheLetterheadLine);
	 	    		justUnderLetterhead.add(Chunk.NEWLINE);
	 	    	}
	 	    	justUnderLetterhead.setAlignment(Element.ALIGN_CENTER);
	 	    	document.add(justUnderLetterhead);
	 	    }
	}
	
	private void addressTheLetterToSubmitterAndPI(Document document, Job job) throws DocumentException, MetadataException{
		User submitter = job.getUser();
		List<UserMeta> userMetaList = submitter.getUserMeta();
		String submitterTitle = MetaHelper.getMetaValue("user", "title", userMetaList);
		String submitterInstitution = MetaHelper.getMetaValue("user", "institution", userMetaList);
		String submitterBuildingRoom = MetaHelper.getMetaValue("user", "building_room", userMetaList);
		String submitterAddress = MetaHelper.getMetaValue("user", "address", userMetaList);
		String submitterCity = MetaHelper.getMetaValue("user", "city", userMetaList);
		String submitterState = MetaHelper.getMetaValue("user", "state", userMetaList);
		String submitterCountry = MetaHelper.getMetaValue("user", "country", userMetaList);
		String submitterZip = MetaHelper.getMetaValue("user", "zip", userMetaList);
		String submitterPhone = MetaHelper.getMetaValue("user", "phone", userMetaList);		
		
		Lab lab = job.getLab();
		String labDepartment = lab.getDepartment().getName();//Genetics, Internal, External, Cell Biology (External means not Einstein, and used for pricing)
		String pricingSchedule = "Internal";
		if(labDepartment.equalsIgnoreCase("external")){
			pricingSchedule = "External";
		}
		
		User pI = lab.getUser();
		if(submitter.getId().intValue()!=pI.getId().intValue()){
			List<UserMeta> pIMetaList = pI.getUserMeta();
			String pITitle = MetaHelper.getMetaValue("user", "title", pIMetaList);
			String pIInstitution = MetaHelper.getMetaValue("user", "institution", pIMetaList);
			String pIBuildingRoom = MetaHelper.getMetaValue("user", "building_room", pIMetaList);
			String pIAddress = MetaHelper.getMetaValue("user", "address", pIMetaList);
			String pICity = MetaHelper.getMetaValue("user", "city", pIMetaList);
			String pIState = MetaHelper.getMetaValue("user", "state", pIMetaList);
			String pICountry = MetaHelper.getMetaValue("user", "country", pIMetaList);
			String pIZip = MetaHelper.getMetaValue("user", "zip", pIMetaList);
			String pIPhone = MetaHelper.getMetaValue("user", "phone", pIMetaList);
			
	 	    PdfPTable toTheAttentionOftable = new PdfPTable(2);
	 	    toTheAttentionOftable.getDefaultCell().setBorder(0);
	 	    toTheAttentionOftable.setHorizontalAlignment(Element.ALIGN_LEFT);
	 	    toTheAttentionOftable.addCell(new Phrase("Submitter:", NORMAL_BOLD));
	 	    toTheAttentionOftable.addCell(new Phrase("Lab PI:", NORMAL_BOLD));
	 	  	toTheAttentionOftable.addCell(new Phrase(submitterTitle + " " + submitter.getNameFstLst(), NORMAL));
	 	  	toTheAttentionOftable.addCell(new Phrase(pITitle + " " + pI.getNameFstLst(), NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(submitterInstitution, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(pIInstitution, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(submitterBuildingRoom, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(pIBuildingRoom, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(submitterAddress, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(pIAddress, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(submitterCity + ", " + submitterState + " " + submitterCountry, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(pICity + ", " + pIState + " " + pICountry, NORMAL));
	 	    toTheAttentionOftable.addCell(new Phrase(submitter.getEmail(), NORMAL));
	 	    toTheAttentionOftable.addCell(new Phrase(pI.getEmail(), NORMAL));	 	    
	 	    toTheAttentionOftable.addCell(new Phrase(submitterPhone, NORMAL));
	 	    toTheAttentionOftable.addCell(new Phrase(pIPhone, NORMAL));	 	   
	 	    document.add(toTheAttentionOftable);
		}
		else{//submitter is the lab PI
	 	    PdfPTable toTheAttentionOftable = new PdfPTable(1);
	 	    toTheAttentionOftable.getDefaultCell().setBorder(0);
	 	    toTheAttentionOftable.setHorizontalAlignment(Element.ALIGN_LEFT);
	 	    toTheAttentionOftable.addCell(new Phrase("Submitter/Lab PI:", NORMAL_BOLD));
	 	  	toTheAttentionOftable.addCell(new Phrase(submitterTitle + " " + submitter.getNameFstLst(), NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(submitterInstitution, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(submitterBuildingRoom, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(submitterAddress, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(submitterCity + ", " + submitterState + " " + submitterCountry, NORMAL));
	 	    toTheAttentionOftable.addCell(new Phrase(submitter.getEmail(), NORMAL));
	 	    toTheAttentionOftable.addCell(new Phrase(submitterPhone, NORMAL));
	 	    document.add(toTheAttentionOftable);				
		}
	}
	
	private void addNoteLine(Document document, String reason, String theReason) throws DocumentException{
	    Paragraph reasonForDocument = new Paragraph();
	    reasonForDocument.setSpacingBefore(15);
	    reasonForDocument.setSpacingAfter(15);
	    reasonForDocument.add(new Chunk(reason, NORMAL_BOLD));
	    reasonForDocument.add(new Phrase(theReason, NORMAL));
	    document.add(reasonForDocument);
	}
	private void addNoteLineTinyFont(Document document, String reason, String theReason) throws DocumentException{
	    Paragraph reasonForDocument = new Paragraph();
	    reasonForDocument.setSpacingBefore(15);
	    reasonForDocument.setSpacingAfter(15);
	    reasonForDocument.add(new Chunk(reason, TINY_BOLD));
	    reasonForDocument.add(new Phrase(theReason, TINY_NORMAL));
	    document.add(reasonForDocument);
	}
	
	private Paragraph startJobDetailsParagraphAndAddCommonJobDetails(Job job){
	    Paragraph commonJobDetailsParagraph = new Paragraph();
	    commonJobDetailsParagraph.setSpacingBefore(15);
	    commonJobDetailsParagraph.setSpacingAfter(5);
	   	commonJobDetailsParagraph.add(new Chunk("Job Details:", NORMAL_BOLD));commonJobDetailsParagraph.add(Chunk.NEWLINE);
	  	commonJobDetailsParagraph.setLeading(15);
		commonJobDetailsParagraph.add(new Phrase("Job ID: " + job.getId(), NORMAL));commonJobDetailsParagraph.add(Chunk.NEWLINE);
		commonJobDetailsParagraph.add(new Phrase("Job Name: " + job.getName(), NORMAL));commonJobDetailsParagraph.add(Chunk.NEWLINE);	 	 	
	    SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy"); //("yyyy/MM/dd");
	    commonJobDetailsParagraph.add(new Phrase("Submitted: " + formatter.format(job.getCreated()), NORMAL));commonJobDetailsParagraph.add(Chunk.NEWLINE);
	 	//if(job is completed????){
	 	//	commonJobDetails.add(new Phrase("Completed: " + formatter.format(get the date, ask andy how), NORMAL));commonJobDetailsParagraph.add(Chunk.NEWLINE);
	 	//}
	    commonJobDetailsParagraph.add(new Phrase("Assay: " + job.getWorkflow().getName(), NORMAL));commonJobDetailsParagraph.add(Chunk.NEWLINE);
	    String grantDetails = "N/A";
	    AcctGrant grant = getGrantForJob(job);
	    if (grant != null){
		    grantDetails =  grant.getCode();
		    if (grant.getName() != null && !grant.getName().isEmpty())
		    	grantDetails += " (" + grant.getName() + ")";
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    grantDetails += ", expires " + dateFormat.format(grant.getExpirationdt());
	    }
	    commonJobDetailsParagraph.add(new Phrase("Grant Details: " + grantDetails, NORMAL));commonJobDetailsParagraph.add(Chunk.NEWLINE);
	    return commonJobDetailsParagraph;
	}
	
	private Paragraph addMPSDetailsToJobDetailsParagraph(Job job, Paragraph jobDetailsParagraph){
		
		List<JobResourcecategory> jobResourcecategoryList = job.getJobResourcecategory();
		StringBuilder jobMachineListSB = new StringBuilder();
		int count = 0;
	 	for(JobResourcecategory jrc: jobResourcecategoryList){
	 		if(count==0){
	 			jobMachineListSB.append(jrc.getResourceCategory().getName());
	 		}else{ jobMachineListSB.append(", ").append(jrc.getResourceCategory().getName()); }
	 		count++;	 	 		
	 	}
		String jobMachineList = new String(jobMachineListSB);
		List<JobMeta> jobMetaList = job.getJobMeta();
		String readLength = null;
		String readType = null;
		String runType = null;
		for(JobMeta jm : jobMetaList){
			if(jm.getK().toLowerCase().indexOf("readlength")>-1){
				readLength = jm.getV();
			}
			else if(jm.getK().toLowerCase().indexOf("readtype")>-1){
				readType = jm.getV();
			}
			else if(jm.getK().toLowerCase().indexOf("runtype")>-1){
				runType = jm.getV();
			}
		}
		
		int numberOfLanesRequested = job.getJobCellSelection().size();
		String platform = jobResourcecategoryList.size()==1?"Platform: ":"Platforms: ";
		jobDetailsParagraph.add(new Phrase(platform + jobMachineList, NORMAL));jobDetailsParagraph.add(Chunk.NEWLINE);
		if(runType!=null){
	 		jobDetailsParagraph.add(new Phrase("Run Type Requested: " + runType, NORMAL));jobDetailsParagraph.add(Chunk.NEWLINE);
	 	}
	 	if(readType!=null){
	 		jobDetailsParagraph.add(new Phrase("Read Type Requested: " + readType, NORMAL));jobDetailsParagraph.add(Chunk.NEWLINE);
	 	}
	 	if(readLength!=null){
	 		jobDetailsParagraph.add(new Phrase("Read Length Requested: " + readLength, NORMAL));jobDetailsParagraph.add(Chunk.NEWLINE);
	 	}
	 	if(numberOfLanesRequested>0){
	 		jobDetailsParagraph.add(new Phrase("Lanes Requested: " + numberOfLanesRequested, NORMAL));jobDetailsParagraph.add(Chunk.NEWLINE);
	 	}
	 	
	 	//10-17-14 next line gets total samples (submitted macro, submitted library, and facility libraries; we really want number of submitted samples)
	 	//INCORRECT NUMBER jobDetailsParagraph.add(new Phrase("Samples: " + job.getSample().size(), NORMAL));jobDetailsParagraph.add(Chunk.NEWLINE);
	 	List<Sample> allJobSamples = job.getSample();//userSubmitted Macro, userSubmitted Library, facilityGenerated Library
		List<Sample> submittedMacromoleculeList = new ArrayList<Sample>();
		List<Sample> submittedLibraryList = new ArrayList<Sample>();
		List<Sample> facilityLibraryList = new ArrayList<Sample>();
		sampleService.enumerateSamplesForMPS(allJobSamples, submittedMacromoleculeList, submittedLibraryList, facilityLibraryList);
		List<Sample> submittedObjectList = new ArrayList<Sample>();//could have gotten this from submittedObjectList = jobService.getSubmittedSamples(job);
		submittedObjectList.addAll(submittedMacromoleculeList);
		submittedObjectList.addAll(submittedLibraryList);	 	
	 	jobDetailsParagraph.add(new Phrase("Samples Submitted: " + submittedObjectList.size(), NORMAL));jobDetailsParagraph.add(Chunk.NEWLINE);
	 	
	 	/* Simply let the user select any appropriate discounts
	 	Lab lab = job.getLab();
		String labDepartment = lab.getDepartment().getName();//Genetics, Internal, External, Cell Biology (External means not Einstein, and used for pricing)
		String pricingSchedule = "Internal";
		if(labDepartment.equalsIgnoreCase("external")){
			pricingSchedule = "External";
		} 	 	
	 	jobDetailsParagraph.add(new Phrase("Pricing Schedule: " + pricingSchedule, NORMAL));jobDetailsParagraph.add(Chunk.NEWLINE);
	 	*/
		
		return jobDetailsParagraph;
	}
	
	private Integer addSubmittedSamplesMultiplexRequestAndLibraryCostsAsTable(Document document, MPSQuote mpsQuote) throws DocumentException{
		
	 	Paragraph sampleLibraryTitle = new Paragraph();
	 	sampleLibraryTitle.setSpacingBefore(5);
	 	sampleLibraryTitle.setSpacingAfter(5);
	 	sampleLibraryTitle.add(new Chunk("Submitted Samples, Lane/Multiplex Request & Library Construction Costs:", NORMAL_BOLD));
	 	document.add(sampleLibraryTitle);
	 	
	 	PdfPTable sampleLibraryTable = new PdfPTable(7);
	 	sampleLibraryTable.setHorizontalAlignment(Element.ALIGN_LEFT);
	 	sampleLibraryTable.setWidths(new float[]{0.3f, 1.7f, 0.7f, 0.8f, 0.8f, 0.8f, 1.0f});
		PdfPCell cellNo = new PdfPCell(new Phrase("No.", NORMAL_BOLD));
		cellNo.setHorizontalAlignment(Element.ALIGN_CENTER);
		sampleLibraryTable.addCell(cellNo);
		PdfPCell cellSample = new PdfPCell(new Phrase("Sample", NORMAL_BOLD));
		cellSample.setHorizontalAlignment(Element.ALIGN_CENTER);
		sampleLibraryTable.addCell(cellSample);
		PdfPCell cellMaterial = new PdfPCell(new Phrase("Material", NORMAL_BOLD));
		cellMaterial.setHorizontalAlignment(Element.ALIGN_CENTER);
		sampleLibraryTable.addCell(cellMaterial);
		PdfPCell runOnLane = new PdfPCell(new Phrase("Run On Lane(s)", NORMAL_BOLD));
		runOnLane.setHorizontalAlignment(Element.ALIGN_CENTER);
		sampleLibraryTable.addCell(runOnLane);
		PdfPCell libCostCell = new PdfPCell(new Phrase("Library Cost", NORMAL_BOLD));
		libCostCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		sampleLibraryTable.addCell(libCostCell);
		PdfPCell libAnalysisCostCell = new PdfPCell(new Phrase("Analysis Cost", NORMAL_BOLD));
		libAnalysisCostCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		sampleLibraryTable.addCell(libAnalysisCostCell);
		PdfPCell libTotalCostCell = new PdfPCell(new Phrase("Library Total", NORMAL_BOLD));
		libTotalCostCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		sampleLibraryTable.addCell(libTotalCostCell);

		int sampleCounter = 1;
		int cumulativeCostForAllLibraries = 0;
		Map<Sample,String> coverageMap = jobService.getCoverageMap(jobService.getJobByJobId(mpsQuote.getJobId()));//a user-submitted request: which samples are to be run on which lanes 
		String currencyIcon = mpsQuote.getLocalCurrencyIcon();

		for(LibraryCost libraryCost : mpsQuote.getLibraryCosts()){
			int totalLibCost = 0;
			sampleLibraryTable.addCell(new Phrase(""+sampleCounter, NORMAL));
			sampleLibraryTable.addCell(new Phrase(libraryCost.getSampleName(), NORMAL));
			sampleLibraryTable.addCell(new Phrase(libraryCost.getMaterial(), NORMAL));

			Sample sample = sampleService.getSampleById(libraryCost.getSampleId());
			String coverageString = coverageMap.get(sample);//for example, a coverage string for this sample might look like 00101 which would mean run this sample on lanes 3 and 5
			StringBuilder runOnWhichLanesSB = new StringBuilder();
			char testChar = '1';//means run this sample on the lane (the lane number is set by i+1)
			for(int i = 0; i < coverageString.length(); i++){
				if(coverageString.charAt(i) == testChar){//run on lane i+1
					if(runOnWhichLanesSB.length()>0){
						runOnWhichLanesSB.append(", " + (i+1));
					}
					else{
						runOnWhichLanesSB.append(i+1);
					}
				}
			}
			String runOnWhichLanes = new String(runOnWhichLanesSB);
			sampleLibraryTable.addCell(new Phrase(runOnWhichLanes, NORMAL));

			PdfPCell cost = null;
			if(libraryCost.getReasonForNoLibraryCost().isEmpty()){
				Integer libCost = new Integer(libraryCost.getLibraryCost().intValue());//convert the Float to Integer
				cumulativeCostForAllLibraries += libCost.intValue();
				totalLibCost += libCost.intValue();
				cost = new PdfPCell(new Phrase(currencyIcon+" "+libCost.toString(), NORMAL));
			}
			else{
				cost = new PdfPCell(new Phrase(libraryCost.getReasonForNoLibraryCost(), NORMAL)); 				
			}
			cost.setHorizontalAlignment(Element.ALIGN_RIGHT);
			sampleLibraryTable.addCell(cost);
			
			Integer libAnalysisCost = new Integer(libraryCost.getAnalysisCost().intValue());//convert the Float to Integer
			cumulativeCostForAllLibraries += libAnalysisCost.intValue();
			totalLibCost += libAnalysisCost.intValue();
			PdfPCell analysisCost = new PdfPCell(new Phrase(currencyIcon+" "+libAnalysisCost.toString(), NORMAL));
			analysisCost.setHorizontalAlignment(Element.ALIGN_RIGHT);
			sampleLibraryTable.addCell(analysisCost);
			
			PdfPCell libTotal = new PdfPCell(new Phrase(currencyIcon+" "+totalLibCost, NORMAL_BOLD));
			libTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
			sampleLibraryTable.addCell(libTotal);
			sampleCounter++;
		}
		
		for(int i = 0; i < 6; i++){//6 empty cells with no border
			PdfPCell cell = new PdfPCell(new Phrase(""));
			cell.setBorder(Rectangle.NO_BORDER);
			sampleLibraryTable.addCell(cell);
		}
		PdfPCell cumulativeCost = new PdfPCell(new Phrase("Total: " + currencyIcon+" "+cumulativeCostForAllLibraries, NORMAL_BOLD));
		cumulativeCost.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cumulativeCost.setBorderWidth(2f);
		cumulativeCost.setBorderColor(BaseColor.BLACK);
		sampleLibraryTable.addCell(cumulativeCost);
			
		document.add(sampleLibraryTable);
		return new Integer (cumulativeCostForAllLibraries);
	}
	
	private Integer addSequenceRunsAndCostAsTable(Document document, MPSQuote mpsQuote) throws DocumentException{
		
		int cumulativeCostForAllSequenceRuns = 0;
		
		List<SequencingCost> sequencingCosts = mpsQuote.getSequencingCosts();
		
	 	Paragraph sequenceRunTitle = new Paragraph();
	 	sequenceRunTitle.setSpacingBefore(5);
	 	sequenceRunTitle.setSpacingAfter(5);
	 	if(sequencingCosts.size()==0){
		 	sequenceRunTitle.add(new Chunk("Sequence Runs: No Sequence Runs To Be Performed", NORMAL_BOLD));
		 	document.add(sequenceRunTitle);
		 	return cumulativeCostForAllSequenceRuns;
	 	}
	 	sequenceRunTitle.add(new Chunk("Sequence Runs And Costs:", NORMAL_BOLD));
	 	document.add(sequenceRunTitle);

	 	PdfPTable runTable = new PdfPTable(7);
	 	runTable.setHorizontalAlignment(Element.ALIGN_LEFT);
	 	runTable.setWidths(new float[]{0.3f, 1.1f, 0.4f, 0.5f, 0.4f, 0.6f, 0.9f});
		PdfPCell cellRunNo = new PdfPCell(new Phrase("No.", NORMAL_BOLD));
		cellRunNo.setHorizontalAlignment(Element.ALIGN_CENTER);
		runTable.addCell(cellRunNo);
		PdfPCell cellMachine = new PdfPCell(new Phrase("Machine", NORMAL_BOLD));
		cellMachine.setHorizontalAlignment(Element.ALIGN_CENTER);
		runTable.addCell(cellMachine);
		PdfPCell cellReadLength = new PdfPCell(new Phrase("Length", NORMAL_BOLD));
		cellReadLength.setHorizontalAlignment(Element.ALIGN_CENTER);
		runTable.addCell(cellReadLength);
		PdfPCell cellReadType = new PdfPCell(new Phrase("Type", NORMAL_BOLD));
		cellReadType.setHorizontalAlignment(Element.ALIGN_CENTER);
		runTable.addCell(cellReadType);
		PdfPCell cellNumLanes = new PdfPCell(new Phrase("Lanes", NORMAL_BOLD));
		cellNumLanes.setHorizontalAlignment(Element.ALIGN_CENTER);
		runTable.addCell(cellNumLanes);
		PdfPCell cellPricePerLane = new PdfPCell(new Phrase("Cost/Lane", NORMAL_BOLD));
		cellPricePerLane.setHorizontalAlignment(Element.ALIGN_CENTER);
		runTable.addCell(cellPricePerLane);
		PdfPCell totalPerRun = new PdfPCell(new Phrase("Cost/Run", NORMAL_BOLD));
		totalPerRun.setHorizontalAlignment(Element.ALIGN_CENTER);
		runTable.addCell(totalPerRun);

		int runCounter = 1;
		String currencyIcon = mpsQuote.getLocalCurrencyIcon();

		for(SequencingCost sequencingCost : sequencingCosts){
			runTable.addCell(new Phrase(""+runCounter, NORMAL));
			runTable.addCell(new Phrase(sequencingCost.getResourceCategory().getName() + " (run-type:" + sequencingCost.getRunType() + ")", NORMAL));//run-type is printed on next line
			runTable.addCell(new Phrase(sequencingCost.getReadLength().toString(), NORMAL));
			runTable.addCell(new Phrase(sequencingCost.getReadType(), NORMAL));
			Integer numLanes = sequencingCost.getNumberOfLanes();
			runTable.addCell(new Phrase(numLanes.toString(), NORMAL));
			Integer pricePerLane = new Integer(sequencingCost.getCostPerLane().intValue());
			runTable.addCell(new Phrase(currencyIcon + " " + pricePerLane.toString(), NORMAL));
			Integer totalCostPerSequenceRun = numLanes * pricePerLane;
			PdfPCell totalCostPerSequenceRunCell = new PdfPCell(new Phrase(currencyIcon + " " + totalCostPerSequenceRun.toString(), NORMAL));
			totalCostPerSequenceRunCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			runTable.addCell(totalCostPerSequenceRunCell);
				
			cumulativeCostForAllSequenceRuns += totalCostPerSequenceRun.intValue();
			runCounter++;
		}
		for(int i = 0; i < 6; i++){//6 empty cells with no border
			PdfPCell cell = new PdfPCell(new Phrase(""));
			cell.setBorder(Rectangle.NO_BORDER);
			runTable.addCell(cell);
		}
		PdfPCell totalRunCost = new PdfPCell(new Phrase("Total: " + currencyIcon+" "+cumulativeCostForAllSequenceRuns, NORMAL_BOLD));
		totalRunCost.setHorizontalAlignment(Element.ALIGN_RIGHT);
		totalRunCost.setBorderWidth(2f);
		totalRunCost.setBorderColor(BaseColor.BLACK);
		runTable.addCell(totalRunCost);

		document.add(runTable);		
		return new Integer(cumulativeCostForAllSequenceRuns);
	}
	
	private Integer addAdditionalCostsAsTable(Document document, MPSQuote mpsQuote) throws DocumentException{

		int cumulativeAdditionalCost = 0;

		List<AdditionalCost>  additionalCosts = mpsQuote.getAdditionalCosts();
		
		if(additionalCosts.size()==0){
			return cumulativeAdditionalCost;
		}
		
		Paragraph additionalCostTitle = new Paragraph();
		additionalCostTitle.setSpacingBefore(5);
		additionalCostTitle.setSpacingAfter(5);
		additionalCostTitle.add(new Chunk("Additional Costs:", NORMAL_BOLD));
		document.add(additionalCostTitle);
		
	 	PdfPTable additionalCostTable = new PdfPTable(5);
	 	additionalCostTable.setHorizontalAlignment(Element.ALIGN_LEFT);
	 	additionalCostTable.setWidths(new float[]{0.2f, 1.4f, 0.3f, 0.5f, 0.9f});
		PdfPCell celladditionalCostNo = new PdfPCell(new Phrase("No.", NORMAL_BOLD));
		celladditionalCostNo.setHorizontalAlignment(Element.ALIGN_CENTER);
		additionalCostTable.addCell(celladditionalCostNo);
		PdfPCell cellReason = new PdfPCell(new Phrase("Reason", NORMAL_BOLD));
		cellReason.setHorizontalAlignment(Element.ALIGN_CENTER);
		additionalCostTable.addCell(cellReason);
		PdfPCell cellUnits = new PdfPCell(new Phrase("Units", NORMAL_BOLD));
		cellUnits.setHorizontalAlignment(Element.ALIGN_CENTER);
		additionalCostTable.addCell(cellUnits);
		PdfPCell cellCostPerUnit = new PdfPCell(new Phrase("Cost/Unit", NORMAL_BOLD));
		cellCostPerUnit.setHorizontalAlignment(Element.ALIGN_CENTER);
		additionalCostTable.addCell(cellCostPerUnit);
		PdfPCell cellTotalCost = new PdfPCell(new Phrase("Additional Cost", NORMAL_BOLD));
		cellTotalCost.setHorizontalAlignment(Element.ALIGN_CENTER);
		additionalCostTable.addCell(cellTotalCost);
		
		int additionalCostCounter = 1;
		String currencyIcon = mpsQuote.getLocalCurrencyIcon();

		for(AdditionalCost additionalCost : additionalCosts){
			additionalCostTable.addCell(new Phrase(""+additionalCostCounter, NORMAL));
			additionalCostTable.addCell(new Phrase(additionalCost.getReason(), NORMAL));
			Integer units = additionalCost.getNumberOfUnits();
			additionalCostTable.addCell(new Phrase(units.toString(), NORMAL));
			Integer pricePerUnit = new Integer(additionalCost.getCostPerUnit().intValue());
			additionalCostTable.addCell(new Phrase(currencyIcon + " " + pricePerUnit.toString(), NORMAL));
			Integer totalCostPerAdditionalCost = units * pricePerUnit;
			PdfPCell totalCostPerAdditionalCostCell = new PdfPCell(new Phrase(currencyIcon + " " + totalCostPerAdditionalCost.toString(), NORMAL));
			totalCostPerAdditionalCostCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			additionalCostTable.addCell(totalCostPerAdditionalCostCell);
				
			cumulativeAdditionalCost += totalCostPerAdditionalCost.intValue();
			additionalCostCounter++;
		}
		for(int i = 0; i < 4; i++){//4 empty cells with no border
			PdfPCell cell = new PdfPCell(new Phrase(""));
			cell.setBorder(Rectangle.NO_BORDER);
			additionalCostTable.addCell(cell);
		}
		PdfPCell totalAdditionalCost = new PdfPCell(new Phrase("Total: " + currencyIcon+" "+cumulativeAdditionalCost, NORMAL_BOLD));
		totalAdditionalCost.setHorizontalAlignment(Element.ALIGN_RIGHT);
		totalAdditionalCost.setBorderWidth(2f);
		totalAdditionalCost.setBorderColor(BaseColor.BLACK);
		additionalCostTable.addCell(totalAdditionalCost);
		
		document.add(additionalCostTable);
		return new Integer(cumulativeAdditionalCost);
	}
	
	
	private void addCommentsAsTable(Document document, MPSQuote mpsQuote) throws DocumentException{

		List<Comment> comments = mpsQuote.getComments();
		if(comments.size()==0){
			return;
		}
		
		Paragraph commentsTitle = new Paragraph();
		commentsTitle.setSpacingBefore(5);
		commentsTitle.setSpacingAfter(5);		
		commentsTitle.add(new Chunk("Comments:", NORMAL_BOLD));
		document.add(commentsTitle);
		
		PdfPTable commentsTable = new PdfPTable(2);
		commentsTable.setHorizontalAlignment(Element.ALIGN_LEFT);
		commentsTable.setWidths(new float[]{0.3f, 5f});
		PdfPCell commentsNo = new PdfPCell(new Phrase("No.", NORMAL_BOLD));
		commentsNo.setHorizontalAlignment(Element.ALIGN_CENTER);
		commentsTable.addCell(commentsNo);
		PdfPCell commentHeader = new PdfPCell(new Phrase("Comments", NORMAL_BOLD));
		commentHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
		commentsTable.addCell(commentHeader);
		
		int commentsCounter = 1;
		for(Comment comment : comments){
			PdfPCell numberCell = new PdfPCell(new Phrase(""+commentsCounter, NORMAL));
			numberCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	 		commentsTable.addCell(numberCell);
	 		
			commentsTable.addCell(new Phrase(comment.getComment(), NORMAL));
			commentsCounter++;			
		}
		
		document.add(commentsTable);		
		
	}	
	
	private Integer addCostSummaryTable(Document document, MPSQuote mpsQuote, List<String>costReasonList, Map<String, Integer>costReasonPriceMap) throws DocumentException{
	
		String currencyIcon = mpsQuote.getLocalCurrencyIcon();

		List<Discount> discounts = mpsQuote.getDiscounts();
		
		int totalFinalCost = 0;
		int totalCosts = 0;
		int totalDiscounts = 0;
		DecimalFormat twoDFormat = new DecimalFormat("#.##");
		
		Paragraph anticipatedCosts = new Paragraph();
		anticipatedCosts.setSpacingBefore(15);
		anticipatedCosts.setSpacingAfter(5);
		anticipatedCosts.add(new Chunk("Cost Summary:", NORMAL_BOLD));
	 	document.add(anticipatedCosts);
		
	    PdfPTable costTable = new PdfPTable(2);
	    costTable.getDefaultCell().setBorder(0);
	    costTable.setHorizontalAlignment(Element.ALIGN_LEFT);
	    costTable.setWidthPercentage(60);
	   
	    for(String costReason : costReasonList){
	    	
	    	costTable.addCell(new Phrase(costReason, NORMAL_BOLD));
	    	
	    	int thisCost = costReasonPriceMap.get(costReason).intValue();
	    	totalCosts += thisCost;
		    PdfPCell secondCell = new PdfPCell(new Phrase(currencyIcon + " " + thisCost, NORMAL_BOLD));
		    secondCell.setBorder(0);
		    secondCell.setHorizontalAlignment(Element.ALIGN_RIGHT);	 		
	 	    costTable.addCell(secondCell); 	 
	    }

	    if(discounts.size()>0){
		    costTable.addCell(new Phrase("Subtotal", NORMAL_BOLD));
	 	   
	 	    PdfPCell subtotalCell = new PdfPCell(new Phrase(currencyIcon + " " + totalCosts, NORMAL_BOLD));
	 	    subtotalCell.setBorder(0);
	 	    subtotalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);	 		
	 	    costTable.addCell(subtotalCell);
	 	  
	 	    //blank line
	 	    costTable.addCell(new Phrase(" ", NORMAL_BOLD));	 	   
	 	    PdfPCell blankCell = new PdfPCell(new Phrase(" ", NORMAL_BOLD));
	 	    blankCell.setBorder(0);
	 	    blankCell.setHorizontalAlignment(Element.ALIGN_RIGHT);	 		
	 	    costTable.addCell(blankCell);
	 	    
	 	    //next add the discounts and get it's discountTotal
	 	    for(Discount discount : discounts){
	 	    	
	 	    	if(discount.getType().equals("%")){
	 	    		Double percentOff = Double.valueOf(twoDFormat.format(discount.getValue()));//it's stored as float with two fractional decimals ###.##
	 	    		costTable.addCell(new Phrase(discount.getReason() + " (" + percentOff.doubleValue() + discount.getType()+")", NORMAL_BOLD));
	 	    		Double thisDiscount = Double.valueOf(twoDFormat.format(totalCosts * percentOff / 100));
	 	    		totalDiscounts += thisDiscount.intValue();
	 	    		PdfPCell secondCell = new PdfPCell(new Phrase("("+currencyIcon + " " + thisDiscount.intValue()+")", NORMAL_BOLD));
		 		    secondCell.setBorder(0);
		 		    secondCell.setHorizontalAlignment(Element.ALIGN_RIGHT);	 		
		 	 	    costTable.addCell(secondCell); 
	 	    	}
	 	    	else if(discount.getType().equals(currencyIcon)){
	 	    		int thisDiscount = discount.getValue().intValue();//it's stored as float with two fractional decimals ###.##
	 	    		costTable.addCell(new Phrase(discount.getReason(), NORMAL_BOLD));
	 	    		totalDiscounts += thisDiscount;
	 	    		PdfPCell secondCell = new PdfPCell(new Phrase("("+currencyIcon + " " + thisDiscount+")", NORMAL_BOLD));
	 	    		secondCell.setBorder(0);
	 	    		secondCell.setHorizontalAlignment(Element.ALIGN_RIGHT);	 		
	 	    		costTable.addCell(secondCell); 	
	 	    	}
	 	    } 	 
	    }
	    //blank line
	    costTable.addCell(new Phrase(" ", NORMAL_BOLD));	 	   
	    PdfPCell blankCell = new PdfPCell(new Phrase(" ", NORMAL_BOLD));
	    blankCell.setBorder(0);
	    blankCell.setHorizontalAlignment(Element.ALIGN_RIGHT);	 		
	    costTable.addCell(blankCell);
	    
	   	totalFinalCost = totalCosts - totalDiscounts;
	   	if(totalFinalCost < 0){
	   		totalFinalCost = 0;
	   	}
	   	
	   	PdfPCell totalFinalCostWordsCell = new PdfPCell(new Phrase("Total Cost", NORMAL_BOLD));
	   	totalFinalCostWordsCell.setBorderWidth(2f);
	   	totalFinalCostWordsCell.setBorderColorBottom(BaseColor.BLACK);
	   	totalFinalCostWordsCell.setBorderColorLeft(BaseColor.BLACK);
	   	totalFinalCostWordsCell.setBorderColorTop(BaseColor.BLACK);
	   	totalFinalCostWordsCell.setBorderColorRight(BaseColor.WHITE);
	   	totalFinalCostWordsCell.setHorizontalAlignment(Element.ALIGN_LEFT);	 		
	    costTable.addCell(totalFinalCostWordsCell);
	    
	    PdfPCell totalFinalCostCell = new PdfPCell(new Phrase(currencyIcon + " " + totalFinalCost, NORMAL_BOLD));
	    totalFinalCostCell.setBorderWidth(2f);
	    totalFinalCostWordsCell.setBorderColorBottom(BaseColor.BLACK);
	    totalFinalCostWordsCell.setBorderColorRight(BaseColor.BLACK);
	    totalFinalCostWordsCell.setBorderColorLeft(BaseColor.WHITE);
	    totalFinalCostWordsCell.setBorderColorTop(BaseColor.BLACK);
	    totalFinalCostCell.setHorizontalAlignment(Element.ALIGN_RIGHT);	 		
	    costTable.addCell(totalFinalCostCell);	 	    
	 	    
	 	document.add(costTable);		
		
		return new Integer(totalFinalCost);
	}

	@Override
	public AcctGrant saveGrant(AcctGrant grant) {
		return acctGrantDao.save(grant);
	}

	@Override
	public List<AcctGrant> getGrantsForLab(Lab lab) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("labId", lab.getId());
		List<AcctGrant> grants = acctGrantDao.findByMap(m);
		if (grants != null)
			return grants;
		return new ArrayList<AcctGrant>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<AcctGrant> getNonExpiredGrantsForLab(Lab lab) {
		ArrayList<AcctGrant> currentGrants = new ArrayList<AcctGrant>();
		for (AcctGrant grant : getGrantsForLab(lab)){
			if (grant.getExpirationdt().before(new Date()))
				grant.setIsActive(0);
			else
				currentGrants.add(grant);
		}
		return currentGrants;
	}

	@Override
	public AcctGrant getGrantForJob(Job job) {
		List<AcctGrantjob> agjs = acctGrantjobDao.getAcctGrantjobByJobId(job.getId());
		if (agjs.isEmpty())
			return null;
		AcctGrantjob agj = agjs.get(0);
		if (agj == null)
			return null;
		return agj.getAcctGrant();
	}

	@Override
	public AcctGrantjob saveJobGrant(Job job, AcctGrant grant) {
		AcctGrantjob acj = new AcctGrantjob();
		List<AcctGrantjob> acjs = acctGrantjobDao.getAcctGrantjobByJobId(job.getId());
		if (!acjs.isEmpty())
			acj = acjs.get(0);
		acj.setGrantId(grant.getId());
		acj.setJobId(job.getId());
		return acctGrantjobDao.save(acj);
	}

	@Override
	public AcctGrant getAcctGrantById(Integer id) {
		return acctGrantDao.getById(id);
	}

	@Override
	public AcctGrantjob getJobGrant(Job job, AcctGrant grant) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("id", job.getId()); // jobid is primary key in this table (why ????) hence just use id
		m.put("grantId", grant.getId());
		List<AcctGrantjob> jobgrants = acctGrantjobDao.findByMap(m);
		if (jobgrants != null && !jobgrants.isEmpty())
			return jobgrants.get(0);
		return null;
	}

	@Override
	public AcctGrant getGrantForJobDraft(JobDraft jobDraft) {
		List<AcctGrantjobDraft> agjds = acctGrantjobDraftDao.getAcctGrantjobDraftByJobDraftId(jobDraft.getId());
		if (agjds.isEmpty())
			return null;
		AcctGrantjobDraft agjd = agjds.get(0);
		if (agjd == null)
			return null;
		return agjd.getAcctGrant();
	}

	@Override
	public AcctGrantjobDraft saveJobDraftGrant(JobDraft jobDraft, AcctGrant grant) {
		AcctGrantjobDraft acjd = new AcctGrantjobDraft();
		List<AcctGrantjobDraft> acjds = acctGrantjobDraftDao.getAcctGrantjobDraftByJobDraftId(jobDraft.getId());
		if (!acjds.isEmpty())
			acjd = acjds.get(0);
		acjd.setGrantId(grant.getId());
		acjd.setJobDraftId(jobDraft.getId());
		return acctGrantjobDraftDao.save(acjd);
	}

	@Override
	public AcctGrantjobDraft getJobDraftGrant(JobDraft jobDraft, AcctGrant grant) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("id", jobDraft.getId()); // jobDraft is primary key in this table (why ????) hence just use id
		m.put("grantId", grant.getId());
		List<AcctGrantjobDraft> jobDraftgrants = acctGrantjobDraftDao.findByMap(m);
		if (jobDraftgrants != null && !jobDraftgrants.isEmpty())
			return jobDraftgrants.get(0);
		return null;
	}
	
	
	public void buildJobSampleReviewPDF(Job job, OutputStream outputStream)throws DocumentException, MetadataException{
		
		Document document = new Document();
 	    PdfWriter.getInstance(document, outputStream).setInitialLeading(10);
 	    document.open();	 	    
 	    List<String> justUnderLetterheadLineList = new ArrayList<String>();
 	    justUnderLetterheadLineList.add("Shahina Maqbool PhD (ESF Director), Albert Einstein College of Medicine, 1301 Morris Park Ave (Price 159F)");
 	    justUnderLetterheadLineList.add("Email:shahina.maqbool@einstein.yu.edu Phone:718-678-1163");
 	    String imageLocation = "/Users/robertdubin/Documents/images/Einstein_Logo.png";
 	    String title = "Epigenomics Shared Facility";
 	    addLetterhead(document, imageLocation, title, justUnderLetterheadLineList);
 	    Date now = new Date();
 	    DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
 	    addNoteLine(document, "Date Document Created: ", dateFormat.format(now));
 	    addressTheLetterToSubmitterAndPI(document, job);
 	    addNoteLine(document, "Re.: ", "Job & Sample Summary For Job ID " + job.getId());
 	    document.add(new LineSeparator());
 	    Paragraph jobDetailsParagraph = startJobDetailsParagraphAndAddCommonJobDetails(job);//start new paragraph containing common job details (put the paragraph is NOT added to the document in this method, thus permitting more to be added to it)
 	    jobDetailsParagraph = addMPSDetailsToJobDetailsParagraph(job, jobDetailsParagraph);//add msp-specific info to the jobDetails paragraph
 	    document.add(jobDetailsParagraph);//add the paragraph to the document
 	 	    
 	    document.newPage();
 	    addSubmittedSamplesQuickViewDetailsAsTable(document, job);
 	    document.newPage();
 	    addLanesRequestedAsTable(document, job);
 	    document.close();		
	}
	
	private void addSubmittedSamplesQuickViewDetailsAsTable(Document document, Job job ) throws DocumentException{
		
		List<Sample> allJobSamples = job.getSample();//userSubmitted Macro, userSubmitted Library, facilityGenerated Library
		List<Sample> submittedMacromoleculeList = new ArrayList<Sample>();
		List<Sample> submittedLibraryList = new ArrayList<Sample>();
		List<Sample> facilityLibraryList = new ArrayList<Sample>();
		sampleService.enumerateSamplesForMPS(allJobSamples, submittedMacromoleculeList, submittedLibraryList, facilityLibraryList);
		List<Sample> submittedObjectList = new ArrayList<Sample>();//could have gotten this from submittedObjectList = jobService.getSubmittedSamples(job);
		submittedObjectList.addAll(submittedMacromoleculeList);
		submittedObjectList.addAll(submittedLibraryList);
		
		Paragraph title = new Paragraph();
		title.setSpacingBefore(5);
		title.setSpacingAfter(5);	
		if(submittedObjectList.size()==0){//rather unlikely
			title.add(new Chunk("Job J"+job.getId()+": No Macromolecules Or Libraries Submitted", NORMAL_BOLD));
			document.add(title);
			return;
		}
		else{
			title.add(new Chunk("Job J"+job.getId()+": Submitted Macromolecules & Libraries - Quick View", NORMAL_BOLD));
			document.add(title);
		}
		
		PdfPTable submittedSamplesQuickViewTable = new PdfPTable(10);
		submittedSamplesQuickViewTable.setHorizontalAlignment(Element.ALIGN_LEFT);
		submittedSamplesQuickViewTable.setWidths(new float[]{1.1f, 0.4f, 0.6f, 0.6f, 0.5f, 0.5f, 0.5f, 0.3f, 0.5f, 1.1f});
		submittedSamplesQuickViewTable.setWidthPercentage(100f);
		
		PdfPCell sampleNameHeader = new PdfPCell(new Phrase("Sample\n(Internal ID)", TINY_BOLD));
		sampleNameHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
		sampleNameHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
		submittedSamplesQuickViewTable.addCell(sampleNameHeader);
		PdfPCell typeHeader = new PdfPCell(new Phrase("Type", TINY_BOLD));
		typeHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
		typeHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
		submittedSamplesQuickViewTable.addCell(typeHeader);
		PdfPCell speciesHeader = new PdfPCell(new Phrase("Species", TINY_BOLD));
		speciesHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
		speciesHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
		submittedSamplesQuickViewTable.addCell(speciesHeader);
		PdfPCell arrivedHeader = new PdfPCell(new Phrase("Arrived?", TINY_BOLD));
		arrivedHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
		arrivedHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
		submittedSamplesQuickViewTable.addCell(arrivedHeader);
		PdfPCell qcHeader = new PdfPCell(new Phrase("QC", TINY_BOLD));
		qcHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
		qcHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
		submittedSamplesQuickViewTable.addCell(qcHeader);		
		PdfPCell concentrationHeader = new PdfPCell(new Phrase("Conc.\n(ng/mcl)", TINY_BOLD));
		concentrationHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
		concentrationHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
		submittedSamplesQuickViewTable.addCell(concentrationHeader);		
		PdfPCell quantificationMethodHeader = new PdfPCell(new Phrase("Quant.\nMethod", TINY_BOLD));
		quantificationMethodHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
		quantificationMethodHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
		submittedSamplesQuickViewTable.addCell(quantificationMethodHeader);		
		PdfPCell volumeHeader = new PdfPCell(new Phrase("Vol.\n(mcl)", TINY_BOLD));
		volumeHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
		volumeHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
		submittedSamplesQuickViewTable.addCell(volumeHeader);		
		PdfPCell bufferHeader = new PdfPCell(new Phrase("Buffer", TINY_BOLD));
		bufferHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
		bufferHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
		submittedSamplesQuickViewTable.addCell(bufferHeader);
		PdfPCell adaptorHeader = new PdfPCell(new Phrase("Adaptor", TINY_BOLD));
		adaptorHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
		adaptorHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
		submittedSamplesQuickViewTable.addCell(adaptorHeader);
		
		for(Sample submittedObject : submittedObjectList){
			PdfPCell sampleName = new PdfPCell(new Phrase(submittedObject.getName()+"\n(ID: "+ submittedObject.getId().toString()+")", TINY_BOLD));
			sampleName.setHorizontalAlignment(Element.ALIGN_CENTER);
			submittedSamplesQuickViewTable.addCell(sampleName);
			String typeString = submittedObject.getSampleType().getName();
			typeString = typeString.replaceFirst(" ", "\n");
			PdfPCell type = new PdfPCell(new Phrase(typeString, TINY_BOLD));
			type.setHorizontalAlignment(Element.ALIGN_CENTER);
			submittedSamplesQuickViewTable.addCell(type);			
			String speciesString = sampleService.getNameOfOrganism(submittedObject, "???");
			speciesString = speciesString.replaceAll(" ", "\n");
			PdfPCell species = new PdfPCell(new Phrase(speciesString, TINY_BOLD));
			species.setHorizontalAlignment(Element.ALIGN_CENTER);
			submittedSamplesQuickViewTable.addCell(species);
			String arrivalStatusString = sampleService.convertSampleReceivedStatusForWeb(sampleService.getReceiveSampleStatus(submittedObject));
			arrivalStatusString = arrivalStatusString.replaceAll(" ", "\n");
			PdfPCell arrived = new PdfPCell(new Phrase(arrivalStatusString, TINY_BOLD));
			arrived.setHorizontalAlignment(Element.ALIGN_CENTER);
			submittedSamplesQuickViewTable.addCell(arrived);
			String qcStatusString = "N/A";
			if(arrivalStatusString.equalsIgnoreCase("received")){
				qcStatusString = sampleService.convertSampleQCStatusForWeb(sampleService.getSampleQCStatus(submittedObject));
			}
			PdfPCell qc = new PdfPCell(new Phrase(qcStatusString, TINY_BOLD));
			qc.setHorizontalAlignment(Element.ALIGN_CENTER);
			submittedSamplesQuickViewTable.addCell(qc);
			
			List<SampleMeta> smList = submittedObject.getSampleMeta();
			//String pITitle = MetaHelper.getMetaValue("user", "title", pIMetaList);//apparently cannot use, as with samples, the area changes
			PdfPCell concentration = new PdfPCell(new Phrase(getSampleMeta(smList, "concentration"), TINY_BOLD));
			concentration.setHorizontalAlignment(Element.ALIGN_CENTER);
			submittedSamplesQuickViewTable.addCell(concentration);
			PdfPCell quantMethod = new PdfPCell(new Phrase(getSampleMeta(smList, "quantificationmethod"), TINY_BOLD));
			quantMethod.setHorizontalAlignment(Element.ALIGN_CENTER);
			submittedSamplesQuickViewTable.addCell(quantMethod);
			PdfPCell volume = new PdfPCell(new Phrase(getSampleMeta(smList, "volume"), TINY_BOLD));
			volume.setHorizontalAlignment(Element.ALIGN_CENTER);
			submittedSamplesQuickViewTable.addCell(volume);
			PdfPCell buffer = new PdfPCell(new Phrase(getSampleMeta(smList, "buffer"), TINY_BOLD));
			buffer.setHorizontalAlignment(Element.ALIGN_CENTER);
			submittedSamplesQuickViewTable.addCell(buffer);
			if(submittedObject.getSampleType().getIName().toLowerCase().contains("library")){
				String adaptorSetNameString = "";
				String adaptorString = "";
				try{ 
				  Adaptor adaptor = adaptorService.getAdaptor(submittedObject);
				  adaptorSetNameString = adaptor.getAdaptorset().getName();
				  //Index <c:out value="${adaptor.getBarcodenumber()}" /> [<c:out value="${adaptor.getBarcodesequence()}" />]
				  adaptorString = "Index " + adaptor.getBarcodenumber() + "\n[" + adaptor.getBarcodesequence() + "]"; 
				}catch(Exception e){ logger.debug("unable to access adaptor in addSubmittedSamplesQuickViewDetailsAsTable"); }		  
				
				PdfPCell adaptor = new PdfPCell(new Phrase(adaptorSetNameString+"\n"+adaptorString, TINY_BOLD));
				adaptor.setHorizontalAlignment(Element.ALIGN_CENTER);
				submittedSamplesQuickViewTable.addCell(adaptor);
			}
			else{
				PdfPCell adaptor = new PdfPCell(new Phrase("N/A", TINY_BOLD));
				adaptor.setHorizontalAlignment(Element.ALIGN_CENTER);
				submittedSamplesQuickViewTable.addCell(adaptor);
			}
		}
		
		document.add(submittedSamplesQuickViewTable);		
		
	}
	
	private void addLanesRequestedAsTable(Document document, Job job ) throws DocumentException{
		
		Map<Sample, String> cellsRequestedMap = jobService.getCoverageMap(job);
		int totalNumberCellsRequested = job.getJobCellSelection()==null?0:job.getJobCellSelection().size();

		Paragraph title = new Paragraph();
		title.setSpacingBefore(5);
		title.setSpacingAfter(5);	
		if(totalNumberCellsRequested==0){
			title.add(new Chunk("Job J"+job.getId()+": No Sequencing Lanes Requested", NORMAL_BOLD));//rather unlikely
			document.add(title);
			return;
		}
		else if(totalNumberCellsRequested==1){
			title.add(new Chunk("Job J"+job.getId()+": " + totalNumberCellsRequested + " Sequencing Lane Requested", NORMAL_BOLD));
			document.add(title);
		}
		else{
			title.add(new Chunk("Job J"+job.getId()+": " + totalNumberCellsRequested + " Sequencing Lanes Requested", NORMAL_BOLD));
			document.add(title);
		}
		
		List<Sample> allJobSamples = job.getSample();//userSubmitted Macro, userSubmitted Library, facilityGenerated Library
		List<Sample> submittedMacromoleculeList = new ArrayList<Sample>();
		List<Sample> submittedLibraryList = new ArrayList<Sample>();
		List<Sample> facilityLibraryList = new ArrayList<Sample>();
		sampleService.enumerateSamplesForMPS(allJobSamples, submittedMacromoleculeList, submittedLibraryList, facilityLibraryList);
		List<Sample> submittedObjectList = new ArrayList<Sample>();//could have gotten this from submittedObjectList = jobService.getSubmittedSamples(job);
		submittedObjectList.addAll(submittedMacromoleculeList);
		submittedObjectList.addAll(submittedLibraryList);
		
		PdfPTable cellsRequestedTable = new PdfPTable(totalNumberCellsRequested + 1);
		cellsRequestedTable.setHorizontalAlignment(Element.ALIGN_LEFT);
		float[] floatArray = new float[totalNumberCellsRequested + 1];
		floatArray[0] = 1.1f;//for sample name
		for(int i = 1; i <= totalNumberCellsRequested; i++){
			floatArray[i] = 0.2f;
		}
		//cellsRequestedTable.setWidths(new float[]{1.1f, 0.4f, 0.6f, 0.6f, 0.5f, 0.5f, 0.5f, 0.3f, 0.5f, 1.1f});
		cellsRequestedTable.setWidths(floatArray);
		cellsRequestedTable.setWidthPercentage(100f);
		PdfPCell sampleNameHeader = new PdfPCell(new Phrase("Sample\n(Internal ID)", TINY_BOLD));
		sampleNameHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
		sampleNameHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cellsRequestedTable.addCell(sampleNameHeader);
		for(int i = 1; i <= totalNumberCellsRequested; i++){
			PdfPCell laneCellHeader = new PdfPCell(new Phrase("L"+i, TINY_BOLD));
			laneCellHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
			laneCellHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
			cellsRequestedTable.addCell(laneCellHeader);
		}
		for(Sample submittedObject : submittedObjectList){
			
			PdfPCell sampleName = new PdfPCell(new Phrase(submittedObject.getName()+"\n(ID: "+ submittedObject.getId().toString()+")", TINY_BOLD));
			sampleName.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellsRequestedTable.addCell(sampleName);
			
			String yesNoString = "";
			if(cellsRequestedMap.containsKey(submittedObject)){
				yesNoString = cellsRequestedMap.get(submittedObject);
			}
			if(yesNoString.isEmpty()){//rather unlikely
				for(int i = 0; i < totalNumberCellsRequested; i++){				
					PdfPCell laneCell = new PdfPCell(new Phrase("", TINY_BOLD));
					laneCell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cellsRequestedTable.addCell(laneCell);
				}
			}
			else{
				for(int i = 0; i < totalNumberCellsRequested; i++){
					String code = yesNoString.substring(i, i+1);//string of zeros and ones
					PdfPCell laneCell;
					if(code.equalsIgnoreCase("1")){						
						laneCell = new PdfPCell(new Phrase("x", TINY_BOLD));
					}
					else{
						laneCell = new PdfPCell(new Phrase("", TINY_BOLD));
					}
					laneCell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cellsRequestedTable.addCell(laneCell);
				}
			}
		}	
		document.add(cellsRequestedTable);	
	}
	
	private String getSampleMeta(List<SampleMeta> smList, String keyToSearch){
		String val = "";
		for(SampleMeta sm : smList){
			if(sm.getK().toLowerCase().contains(keyToSearch)){
				val = sm.getV();
				break;
			}
		}
		return val;
	}
}
