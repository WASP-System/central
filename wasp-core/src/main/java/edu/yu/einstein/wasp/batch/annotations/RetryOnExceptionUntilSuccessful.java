/**
 * 
 */
package edu.yu.einstein.wasp.batch.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to provide a custom backoff policy for tasklets.
 * 
 * @author calder
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface RetryOnExceptionUntilSuccessful {
	
//	
//	/**
//	 * The types of exceptions that are handled by this anntotation
//	 */
//	Class<? extends Exception>[] exceptions();
//	
//	
//	/**
//	 * Initial delay, default 5s.
//	 */
//	int initialDelay() default 5000;
//	
//	/**
//	 * Max Delay, default 1 hour
//	 */
//	int maxDelay() default 3600000;
//	
//	
//	/**
//	 * multiplier, default 2.
//	 */
//	double multiplier() default 2;

}
