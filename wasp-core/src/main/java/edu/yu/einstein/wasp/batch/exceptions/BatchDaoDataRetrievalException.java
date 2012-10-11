package edu.yu.einstein.wasp.batch.exceptions;

import java.sql.SQLException;

/**
 * @author andymac
 *
 */
public class BatchDaoDataRetrievalException extends SQLException {

	/**
	 * 
	 */
	public BatchDaoDataRetrievalException() {
		
	}

	/**
	 * @param arg0
	 */
	public BatchDaoDataRetrievalException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public BatchDaoDataRetrievalException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public BatchDaoDataRetrievalException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
