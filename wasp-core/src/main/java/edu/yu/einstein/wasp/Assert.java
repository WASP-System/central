package edu.yu.einstein.wasp;

import java.util.Collection;

import edu.yu.einstein.wasp.exception.InvalidParameterException;

public abstract class Assert {

	/**
	 * Asserts that the supplied object is not null. Otherwise throws a InvalidParameterException
	 * which is of type RuntimeException
	 * @param o
	 * @return
	 */
	public static boolean assertParameterNotNull(Object o){
		return assertParameterNotNull(o, null);
	}
	
	
	/**
	 * Asserts that the supplied object is not null. Otherwise throws a InvalidParameterException
	 * which is of type RuntimeException
	 * @param o
	 * @return
	 */
	public static boolean assertParameterNotNull(Object o, String message){
		if (message == null)
			message = "parameter cannot be null";
		if (o == null)
			throw new InvalidParameterException(message);
		return true;
	}
	
	/**
	 * Asserts that the supplied collection is not null and not empty. Otherwise throws a InvalidParameterException
	 * which is of type RuntimeException
	 * @param o
	 * @return
	 */
	public static boolean assertParameterNotNullNotEmpty(Collection<?> collection){
		return assertParameterNotNullNotEmpty(collection, null);
	}
	
	/**
	 * Asserts that the supplied collection is not null and not empty. Otherwise throws a InvalidParameterException
	 * which is of type RuntimeException
	 * @param o
	 * @return
	 */
	public static boolean assertParameterNotNullNotEmpty(Collection<?> collection, String message){
		assertParameterNotNull(collection, message);
		if (collection.isEmpty())
			throw new InvalidParameterException(message);
		return true;
	}
	
	/**
	 * Asserts that the supplied Integer is not null and not 0. Otherwise throws a InvalidParameterException
	 * which is of type RuntimeException
	 * @param o
	 * @return
	 */
	public static boolean assertParameterNotNullNotZero(Integer integer){
		return assertParameterNotNullNotZero(integer, null);
	}
	
	/**
	 * Asserts that the supplied Integer is not null and not 0. Otherwise throws a InvalidParameterException
	 * which is of type RuntimeException
	 * @param o
	 * @return
	 */
	public static boolean assertParameterNotNullNotZero(Integer integer, String message){
		assertParameterNotNull(integer, message);
		if (integer.equals(0))
			throw new InvalidParameterException(message);
		return true;
	}
	
	/**
	 * Asserts that the supplied condition evaluates to true, otherwise throws an InvalidParameterException
	 * @param condition
	 * @param message
	 * @return
	 */
	public static boolean assertTrue(boolean condition, String message){
		if (!condition)
			throw new InvalidParameterException(message);
		return true;
	}
	
	/**
	 * Asserts that the supplied condition evaluates to true, otherwise throws an InvalidParameterException
	 * which is of type RuntimeException
	 * @param condition
	 * @return
	 */
	public static boolean assertTrue(boolean condition){
		return assertTrue(condition, null);
	}
	
}
