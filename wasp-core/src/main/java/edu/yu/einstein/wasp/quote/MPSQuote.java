package edu.yu.einstein.wasp.quote;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

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
	public Integer getTotalFinalCost(){return this.totalFinalCost;}
	
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
