package edu.yu.einstein.wasp.model;

import java.io.Serializable;
import java.util.List;

/*
 * Holds jquery advanced search parameters
 * @Author Sasha Levchuk
 */
public final class JQuerySearch implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7669968072116619631L;

	public String groupOp;
	
	public List<Rules> rules;
	
	public static class Rules {
		public String field;
		public String op;
		public String data;
	}
	
}
