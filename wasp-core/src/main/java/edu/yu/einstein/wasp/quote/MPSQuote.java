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

	private List<SubmittedSample> submittedSamples;
	private List<SequenceRun> sequenceRuns;
	private List<AdditionalCost> additionalCosts;
	private List<Discount> discounts;
	private List<Comment> comments;
	
	public MPSQuote(List<SubmittedSample> submittedSamples, List<SequenceRun> sequenceRuns, 
			List<AdditionalCost> additionalCosts, List<Discount> discounts, List<Comment> comments){
		this.submittedSamples = submittedSamples;
		this.sequenceRuns = sequenceRuns;
		this.additionalCosts = additionalCosts;
		this.discounts = discounts;
		this.comments = comments;
	}
	public MPSQuote(){
		this.submittedSamples = new ArrayList();
		this.sequenceRuns = new ArrayList();
		this.additionalCosts = new ArrayList();
		this.discounts = new ArrayList();
		this.comments = new ArrayList();
	}
	public void setSubmittedSamples(List<SubmittedSample> submittedSamples){
		this.submittedSamples = submittedSamples;
	}	
	public void setSequenceRuns(List<SequenceRun> sequenceRuns){
		this.sequenceRuns = sequenceRuns;
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
	public List<SubmittedSample> getSubmittedSamples(){return this.submittedSamples;}
	public List<SequenceRun> getSequenceRuns(){return this.sequenceRuns;}
	public List<AdditionalCost>  getAdditionalCosts(){return this.additionalCosts;}
	public List<Discount> getDiscounts(){return this.discounts;}
	public List<Comment> getComments(){return this.comments;}
} 
