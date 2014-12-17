package edu.yu.einstein.wasp.quote;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;

/**
 * 
 * @author dubin
 *
 */

public class MPSQuote {

	private Integer jobId;
	private Integer numberOfLibrariesExpectedToBeConstructed;
	private Integer numberOfLanesRequested;
	private String localCurrencyIcon;
	private List<LibraryCost> libraryCosts;
	private List<SequencingCost> sequencingCosts;
	private List<AdditionalCost> additionalCosts;
	private List<Discount> discounts;
	private List<Comment> comments;
	private List<String> errors;
	private Integer totalFinalCost;
	//12/16/14
	private Integer totalLibraryConstructionCost;
	private Integer totalSequenceRunCost;
	private Integer totalAdditionalCost;
	private Integer totalComputationalCost;
	private Integer totalDiscountCost;
	
	public MPSQuote(){
		this.jobId = new Integer(0);
		this.numberOfLibrariesExpectedToBeConstructed = new Integer(0);
		this.numberOfLanesRequested = new Integer(0);
		this.localCurrencyIcon = "";
		this.libraryCosts = new ArrayList<LibraryCost>();
		this.sequencingCosts = new ArrayList<SequencingCost>();
		this.additionalCosts = new ArrayList<AdditionalCost>();
		this.discounts = new ArrayList<Discount>();
		this.comments = new ArrayList<Comment>();
		this.errors = new ArrayList<String>();
		this.totalFinalCost = new Integer(0);
		//12/16/14
		this.totalLibraryConstructionCost = new Integer(-1);
		this.totalSequenceRunCost = new Integer(-1);
		this.totalAdditionalCost = new Integer(-1);
		this.totalComputationalCost = new Integer(-1);
		this.totalDiscountCost = new Integer(-1);
	}

	public MPSQuote(Integer jobId){
		this.jobId = jobId;
		this.numberOfLibrariesExpectedToBeConstructed = new Integer(0);
		this.numberOfLanesRequested = new Integer(0);
		this.localCurrencyIcon = "";
		this.libraryCosts = new ArrayList<LibraryCost>();
		this.sequencingCosts = new ArrayList<SequencingCost>();
		this.additionalCosts = new ArrayList<AdditionalCost>();
		this.discounts = new ArrayList<Discount>();
		this.comments = new ArrayList<Comment>();
		this.errors = new ArrayList<String>();
		this.totalFinalCost = new Integer(0);
		//12/16/14
		this.totalLibraryConstructionCost = new Integer(-1);
		this.totalSequenceRunCost = new Integer(-1);
		this.totalAdditionalCost = new Integer(-1);
		this.totalComputationalCost = new Integer(-1);
		this.totalDiscountCost = new Integer(-1);
	}
	
	public void setJobId(Integer jobId){this.jobId = jobId;}
	public void setNumberOfLibrariesExpectedToBeConstructed(Integer numberOfLibrariesExpectedToBeConstructed){
		this.numberOfLibrariesExpectedToBeConstructed = numberOfLibrariesExpectedToBeConstructed;
	}
	public void setNumberOfLanesRequested(Integer numberOfLanesRequested){
		this.numberOfLanesRequested = numberOfLanesRequested;
	}
	public void setLocalCurrencyIcon(String localCurrencyIcon){
		this.localCurrencyIcon = localCurrencyIcon;
	}
	public void setLibraryCosts(List<LibraryCost> libraryCosts){
		this.libraryCosts = libraryCosts;
	}	
	public void setSequencingCosts(List<SequencingCost> sequencingCosts){
		this.sequencingCosts = sequencingCosts;
	}
	public void setAdditionalCosts(List<AdditionalCost> additionalCosts){
		this.additionalCosts = additionalCosts;
	}
	public void setDiscounts(List<Discount> discounts){
		this.discounts = discounts;
	}
	public void setComments(List<Comment> comments){
		this.comments = comments;
	}
	public void setErrors(List<String> errors){
		this.errors = errors;
	}
	public void setTotalFinalCost(Integer totalFinalCost){
		this.totalFinalCost = totalFinalCost;
	}
	//12-16-14
	public void setTotalLibraryConstructionCost(Integer totalLibraryConstructionCost){
		this.totalLibraryConstructionCost = totalLibraryConstructionCost;
	}
	public void setTotalSequenceRunCost(Integer totalSequenceRunCost){
		this.totalSequenceRunCost = totalSequenceRunCost;
	}
	public void setTotalAdditionalCost(Integer totalAdditionalCost){
		this.totalAdditionalCost = totalAdditionalCost;
	}
	public void setTotalComputationalCost(Integer totalComputationalCost){
		this.totalComputationalCost = totalComputationalCost;
	}
	public void setTotalDiscountCost(Integer totalDiscountCost){
		this.totalDiscountCost = totalDiscountCost;
	}
	
	public Integer getJobId(){return this.jobId;}
	public Integer getNumberOfLibrariesExpectedToBeConstructed(){return this.numberOfLibrariesExpectedToBeConstructed;}
	public Integer getNumberOfLanesRequested(){return this.numberOfLanesRequested;}
	public String getLocalCurrencyIcon(){return this.localCurrencyIcon;}
	public List<LibraryCost> getLibraryCosts(){return this.libraryCosts;}
	public List<SequencingCost> getSequencingCosts(){return this.sequencingCosts;}
	public List<AdditionalCost>  getAdditionalCosts(){return this.additionalCosts;}
	public List<Discount> getDiscounts(){return this.discounts;}
	public List<Comment> getComments(){return this.comments;}
	public List<String> getErrors(){return this.errors;}
	public Integer getTotalFinalCost(){
		//just for now, to deal with the old quotes; 12-17-14 since old quotes have different total
		Integer newTotalFinalCost = this.getTotalLibraryConstructionCost() + this.getTotalSequenceRunCost() + this.getTotalAdditionalCost() 
					+ this.getTotalComputationalCost() - this.getTotalDiscountCost();
		this.setTotalFinalCost(newTotalFinalCost);
		return this.totalFinalCost;
	}

	//12-16-14
	public Integer getTotalLibraryConstructionCost(){
		if(this.totalLibraryConstructionCost.intValue() == -1){	//for old quotes that lack this attribute in the stored json, pull this data from libraryCosts list 		
			int cumulativeCostForAllLibraryConstructions = 0;	
			for(LibraryCost libraryCost : this.getLibraryCosts()){
				if(libraryCost.getReasonForNoLibraryCost().isEmpty()){//means there is a numeric value for this entry
					Integer libConstructionCost = new Integer(libraryCost.getLibraryCost().intValue());//convert the Float to Integer
					cumulativeCostForAllLibraryConstructions += libConstructionCost.intValue();
				}
			}
			this.setTotalLibraryConstructionCost(Integer.valueOf(cumulativeCostForAllLibraryConstructions));
		}
		return this.totalLibraryConstructionCost;
	}

	public Integer getTotalSequenceRunCost(){
		if(this.totalSequenceRunCost.intValue() == -1){	//for old quotes that lack this attribute in the stored json, pull this data from sequencingCosts list 		
			int cumulativeCostForAllSequenceRuns = 0;
			for(SequencingCost sequencingCost : this.getSequencingCosts()){
				Integer numLanes = sequencingCost.getNumberOfLanes();
				Integer pricePerLane = new Integer(sequencingCost.getCostPerLane().intValue());	//converting float to int to Integer
				Integer totalCostPerSequenceRun = numLanes * pricePerLane;
				cumulativeCostForAllSequenceRuns += totalCostPerSequenceRun.intValue();
			}
			this.setTotalSequenceRunCost(Integer.valueOf(cumulativeCostForAllSequenceRuns));
		}
		return this.totalSequenceRunCost;
	}

	public Integer getTotalAdditionalCost(){
		
		if(this.totalAdditionalCost.intValue() == -1){	//for old quotes that lack this attribute in the stored json, pull this data from additionalCosts list 		
			int cumulativeAdditionalCost = 0;
			for(AdditionalCost additionalCost : this.getAdditionalCosts()){
				Integer units = additionalCost.getNumberOfUnits();
				Integer pricePerUnit = new Integer(additionalCost.getCostPerUnit().intValue());//convert float to into to Integer
				Integer totalCostPerAdditionalCost = units * pricePerUnit;
				cumulativeAdditionalCost += totalCostPerAdditionalCost.intValue();
			}
			this.setTotalAdditionalCost(Integer.valueOf(cumulativeAdditionalCost));
		}
		return this.totalAdditionalCost;
	}

	public Integer getTotalComputationalCost(){
		
		if(this.totalComputationalCost.intValue() == -1){	//for old quotes that lack this attribute in the stored json, pull this data from libraryCosts list 		
			int cumulativeCostForAllLibraryComputationalAnalyses = 0;	
			for(LibraryCost libraryCost : this.getLibraryCosts()){
				Integer libComputationalAnalysisCost = new Integer(libraryCost.getAnalysisCost().intValue());//convert the Float to Integer
				cumulativeCostForAllLibraryComputationalAnalyses += libComputationalAnalysisCost.intValue();				
			}
			this.setTotalComputationalCost(Integer.valueOf(cumulativeCostForAllLibraryComputationalAnalyses));
		}
		return this.totalComputationalCost;
	}

	public Integer getTotalDiscountCost(){
		if(this.totalDiscountCost.intValue() == -1){	//for old quotes that lack this attribute in the stored json, pull this data from libraryCosts list 		
			int cumulativeTotalDiscounts = 0;
			DecimalFormat twoDFormat = new DecimalFormat("#.##");
			Integer totalSequencingFacilityCost = this.getTotalLibraryConstructionCost()+this.getTotalSequenceRunCost()+this.getTotalAdditionalCost();
			for(Discount discount : this.getDiscounts()){
				if(discount.getType().equals("%")){
	 	    		Double percentOff = Double.valueOf(twoDFormat.format(discount.getValue()));//it's stored as float with two fractional decimals ###.##
	 	    		Double thisDiscount = Double.valueOf(twoDFormat.format(totalSequencingFacilityCost * percentOff / 100));
	 	    		cumulativeTotalDiscounts += thisDiscount.intValue();	 	    		 
	 	    	}
	 	    	else if(discount.getType().equals(this.getLocalCurrencyIcon())){
	 	    		int thisDiscount = discount.getValue().intValue();//it's stored as float with two fractional decimals ###.##
	 	    		cumulativeTotalDiscounts += thisDiscount;	
	 	    	}
			}
			this.setTotalDiscountCost(Integer.valueOf(cumulativeTotalDiscounts));
		}
		return this.totalDiscountCost;
	}

	/**
	* sets parameters based on JSON input
	* @param <T>
	* @param json
	* @return
	* @throws JSONException
	*/
	
	@JsonIgnore
	public static <T> T getMPSQuoteFromJSONObject(JSONObject json, Class<T> clazz) throws JSONException{
		ObjectMapper mapper = new ObjectMapper();
		try{
			return mapper.readValue(json.toString(), clazz);
		} catch(Exception e){
			throw new JSONException("Cannot create object of type " + clazz.getName() + " from json: " + e.getLocalizedMessage());
		}
	}

	@JsonIgnore
	public JSONObject getAsJSON() throws JSONException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			// use jackson object mapper to create json as text then wrap in JSONObject (Jackson understands @JsonIgnore)
			return new JSONObject(mapper.writeValueAsString(this));
		} catch (Exception e) {
			e.printStackTrace();
			throw new JSONException("Cannot convert object to JSON");
			
		}
	}
	
	
	
} 
