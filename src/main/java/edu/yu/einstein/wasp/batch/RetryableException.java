package edu.yu.einstein.wasp.batch; 

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Retryable Exception 
 * - used to suppress error messages; 
 *
 */


public class RetryableException extends Exception {
	public RetryableException() {super();}
	public RetryableException(String s) {super(s);}
	public RetryableException(String s, Throwable t) {super(s, t);}
	public RetryableException(Throwable t) {super(t);}

	public void printStackTrace() {}
	public void printStackTrace(PrintStream s) {}
	public void printStackTrace(PrintWriter w) {}
}
