
/**
 *
 * QuoteAndInvoiceServiceImpl.java 
 * @author RDubin
 *  
 * the QuoteAndInvoiceService object
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

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserMeta;
import edu.yu.einstein.wasp.quote.AdditionalCost;
import edu.yu.einstein.wasp.quote.Comment;
import edu.yu.einstein.wasp.quote.Discount;
import edu.yu.einstein.wasp.quote.LibraryCost;
import edu.yu.einstein.wasp.quote.MPSQuote;
import edu.yu.einstein.wasp.quote.SequencingCost;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.QuoteAndInvoiceService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.util.MetaHelper;

@Service
@Transactional("entityManager")
public class QuoteAndInvoiceServiceImpl extends WaspServiceImpl implements QuoteAndInvoiceService{

	public static final Font BIG_BOLD =  new Font(FontFamily.TIMES_ROMAN, 13, Font.BOLD );
	public static final Font NORMAL =  new Font(FontFamily.TIMES_ROMAN, 11 );
	public static final Font NORMAL_BOLD =  new Font(FontFamily.TIMES_ROMAN, 11, Font.BOLD );
	public static final Font TINY_BOLD =  new Font(FontFamily.TIMES_ROMAN, 8, Font.BOLD );

	@Autowired
	private JobService jobService;
	@Autowired
	private SampleService sampleService;

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
		for(JobMeta jm : jobMetaList){
			if(jm.getK().toLowerCase().indexOf("readlength")>-1){
				readLength = jm.getV();
			}
			else if(jm.getK().toLowerCase().indexOf("readtype")>-1){
				readType = jm.getV();
			}
		}
		
		int numberOfLanesRequested = job.getJobCellSelection().size();
		String platform = jobResourcecategoryList.size()==1?"Platform: ":"Platforms: ";
		jobDetailsParagraph.add(new Phrase(platform + jobMachineList, NORMAL));jobDetailsParagraph.add(Chunk.NEWLINE);
	 	if(readType!=null){
	 		jobDetailsParagraph.add(new Phrase("Read Type: " + readType, NORMAL));jobDetailsParagraph.add(Chunk.NEWLINE);
	 	}
	 	if(readLength!=null){
	 		jobDetailsParagraph.add(new Phrase("Read Length: " + readLength, NORMAL));jobDetailsParagraph.add(Chunk.NEWLINE);
	 	}
	 	if(numberOfLanesRequested>0){
	 		jobDetailsParagraph.add(new Phrase("Lanes Requested: " + numberOfLanesRequested, NORMAL));jobDetailsParagraph.add(Chunk.NEWLINE);
	 	}
	 	jobDetailsParagraph.add(new Phrase("Samples: " + job.getSample().size(), NORMAL));jobDetailsParagraph.add(Chunk.NEWLINE);
	 	
	 	/* Simply let the user select any appropriate discounts
	 	Lab lab = job.getLab();
		String labDepartment = lab.getDepartment().getName();//Genetics, Internal, External, Cell Biology (External means not Einstein, and used for pricing)
		String pricingSchedule = "Internal";
		if(labDepartment.equalsIgnoreCase("external")){
			pricingSchedule = "External";
		} 	 	
	 	jobDetailsParagraph.add(new Phrase("Pricing Schecule: " + pricingSchedule, NORMAL));jobDetailsParagraph.add(Chunk.NEWLINE);
	 	*/
		
		return jobDetailsParagraph;
	}
	
	private Integer addSubmittedSamplesMultiplexRequestAndLibraryCostsAsTable(Document document, MPSQuote mpsQuote) throws DocumentException{
		
	 	Paragraph sampleLibraryTitle = new Paragraph();
	 	sampleLibraryTitle.setSpacingBefore(5);
	 	sampleLibraryTitle.setSpacingAfter(5);
	 	sampleLibraryTitle.add(new Chunk("Submitted Samples, Lane/Multiplex Request & Library Construction Costs:", NORMAL_BOLD));
	 	document.add(sampleLibraryTitle);
	 	
	 	PdfPTable sampleLibraryTable = new PdfPTable(5);
	 	sampleLibraryTable.setHorizontalAlignment(Element.ALIGN_LEFT);
	 	sampleLibraryTable.setWidths(new float[]{0.3f, 2f, 0.6f, 1f, 1f});
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

		int sampleCounter = 1;
		int cumulativeCostForAllLibraries = 0;
		Map<Sample,String> coverageMap = jobService.getCoverageMap(jobService.getJobByJobId(mpsQuote.getJobId()));//a user-submitted request: which samples are to be run on which lanes 
		String currencyIcon = mpsQuote.getLocalCurrencyIcon();

		for(LibraryCost libraryCost : mpsQuote.getLibraryCosts()){
			
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
				cost = new PdfPCell(new Phrase(currencyIcon+" "+libCost.toString(), NORMAL));
			}
			else{
				cost = new PdfPCell(new Phrase(libraryCost.getReasonForNoLibraryCost(), NORMAL)); 				
			}
			cost.setHorizontalAlignment(Element.ALIGN_RIGHT);
			sampleLibraryTable.addCell(cost);
			sampleCounter++;
		}
		
		for(int i = 0; i < 4; i++){//4 empty cells with no border
			PdfPCell cell = new PdfPCell(new Phrase(""));
			cell.setBorder(Rectangle.NO_BORDER);
			sampleLibraryTable.addCell(cell);
		}
		PdfPCell totalLibCost = new PdfPCell(new Phrase("Total: " + currencyIcon+" "+cumulativeCostForAllLibraries, NORMAL_BOLD));
		totalLibCost.setHorizontalAlignment(Element.ALIGN_RIGHT);
		totalLibCost.setBorderWidth(2f);
		totalLibCost.setBorderColor(BaseColor.BLACK);
		sampleLibraryTable.addCell(totalLibCost);
			
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
	
}
