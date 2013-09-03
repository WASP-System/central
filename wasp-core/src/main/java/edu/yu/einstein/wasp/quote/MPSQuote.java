package edu.yu.einstein.wasp.quote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
	public Integer getJobId(){return this.jobId;}
	public Integer getNumberOfLibrariesExpectedToBeConstructed(){return this.numberOfLibrariesExpectedToBeConstructed;}
	public Integer getNumberOfLanesRequested(){return this.numberOfLanesRequested;}
	public String getLocalCurrencyIcon(){return this.localCurrencyIcon;}
	public List<LibraryCost> getLibraryCosts(){return this.libraryCosts;}
	public List<SequencingCost> getSequencingCosts(){return this.sequencingCosts;}
	public List<AdditionalCost>  getAdditionalCosts(){return this.additionalCosts;}
	public List<Discount> getDiscounts(){return this.discounts;}
	public List<Comment> getComments(){return this.comments;}
} 
