package edu.yu.einstein.wasp.batch;

import edu.yu.einstein.wasp.model.*;
import edu.yu.einstein.wasp.service.*;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.support.transaction.FlushFailedException;
import org.springframework.batch.core.StepExecution;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.*;

import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.*;

import util.spring.PostInitialize;

/**
 * DoSendInvoiceStateProcessor
 * sends an invoice to proper party
 *
 * ** **  currently test to echo hello world via System.exec
 *
 * throws an retryable exception unless
 * a sibling state for that job has hit a status of property status 
 * if it does, update that sibling status w/ a new target status
 *
 * this only supports the first task of the job, anticipated
 * rewrite to take a mapping of types, list of statuses, etc.
 * 
 */

@Component
@Transactional
public class DoSendInvoiceTasklet extends org.springframework.batch.core.step.tasklet.SystemCommandTasklet {

	@Autowired
	StateService stateService;

	@Autowired
	StatejobService statejobService;

	protected Integer stateId;
	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}
	public Integer getStateId() {
		return this.stateId;
	}

	protected List<String> params;
	public void setParams(List<String> params) { 
		this.params = params; 
	  setCommand("/tmp/abc.pl WRONG COMMAND");
  } 
	public List<String> getParams() { return this.params;}


  // @ PostInitialize
	// public void postInitialize() {
  @Transactional
	public void beforeStep(StepExecution stepExecution) {
System.out.println("\n\n\n\n\n\n\n\nTTTTTTTTTTTTTTTTTTTTTTTTTTTTT\n["+  stateService+ "]\n\n\n\n\n\n");
		Map m = new HashMap(); 
		State state = stateService.getStateByStateId(this.stateId.intValue());
		m.put("state", state); 

		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setVariable("m", m);

		List<String> parsedParams = new ArrayList<String>();
		ExpressionParser parser = new SpelExpressionParser();
		for (String s: this.params) {
			parsedParams.add((String) parser.parseExpression(s).getValue(context));
		}

		this.setCommand(org.springframework.util.StringUtils.collectionToDelimitedString(parsedParams, " "));
	}


}

