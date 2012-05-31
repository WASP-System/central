/**
 *
 * WaspModel.java
 * @author echeng
 *
 * this is the base class,
 * every class in the system should extend this.
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;

import edu.yu.einstein.wasp.exception.ModelCopyException;

public abstract class WaspModel implements Serializable {

  /**
   * generic logger included with every class.
   */
  protected static final Logger logger = Logger.getLogger(WaspModel.class);
  
  
  /**
   * Returns a DEEP copy of the supplied model object. Use example: Sample mySampleCopy = Sample.getDeepCopy(mySample);
   * A deep copy creates a new instance of the same class type as the provided object and sets all attributes of the new object to also equal NEW objects 
   * whose data is the same as that for the equivalent attribute within the object being copied.
   * NOTE: even if the model object is lazy loaded, this method will ensure all fields in the copy are fully populated
   * @param waspModelObj
   * @return
   * @throws ModelCopyException 
   */
  public static <T extends WaspModel> T getDeepCopy(T waspModelObj) throws ModelCopyException{
	  return (T) getDeepCopy(waspModelObj, waspModelObj.getClass());
  }

  /**
   * Internal static method for performing a deep copy
   * @param waspModelObj
   * @param clazz
   * @return
   * @throws ModelCopyException 
   */
  private static <T extends WaspModel> Object getDeepCopy(Object waspModelObj, Class<T> clazz) throws ModelCopyException{
	  Object clonedObj = null;
	  try {
		  clonedObj = clazz.newInstance();
	  } catch (InstantiationException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
	  } catch (IllegalAccessException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
	  }
	  List<Field> allClassFields= new ArrayList<Field>();
	  Class<?> clazzToReap = clazz;
	  do{
		  for (Field f: clazzToReap.getDeclaredFields())
			  allClassFields.add(f);
		  clazzToReap = clazzToReap.getSuperclass();
	  } while(clazzToReap != null);
	  for (Field field : allClassFields){
		  String getterMethodName = "get"+WordUtils.capitalize(field.getName());
		  String setterMethodName = "set"+WordUtils.capitalize(field.getName());
		 // if (!field.isAnnotationPresent(javax.persistence.Id.class)){
			  Class<?> fieldType = field.getType();
			  Class<?> fieldGenericType = null;
			  Type genericType = field.getGenericType();
			  if (genericType instanceof ParameterizedType){
				  ParameterizedType pType = (ParameterizedType) genericType; // cast
				  fieldGenericType = (Class<?>) pType.getActualTypeArguments()[0]; // e.g. for List<String>, fieldGenericType should be java.lang.String
			  }
			  
			  // get from source object
			  Object objFromGet = null; 
			  try {
				  objFromGet = clazz.getMethod(getterMethodName).invoke(waspModelObj);
			  } catch (NoSuchMethodException e) {
				  // ignore
				  continue;
			  } catch (Exception e) {
				  logger.debug("DeepCopy failed to work with method '"+getterMethodName+"'");
				  continue;
			  } 
			  if (objFromGet == null)
				  continue;
			  // set to destination object
			  Object objToSet = null;
			  if (java.util.List.class.equals(fieldType)){
				  // is a List
				  objToSet = new ArrayList<Class<?>>();
				  try{
					  for (Object o : (List<?>) objFromGet){
						  if (WaspModel.class.isInstance(o)){
							  ((List<Object>)objToSet).add(WaspModel.getDeepCopy(o, (Class<? extends WaspModel>) fieldGenericType));
						  } else if (fieldGenericType.toString().contains("java.lang")){ 
							  // java.lang package wrapper classes are all immutable
							  ((List<Object>)objToSet).add(o); 
						  } else if (java.util.Date.class.equals(fieldGenericType)){
							  objToSet = new Date();
							  ((Date) objToSet).setTime( ((Date) o).getTime() );
						  } else {
							  throw new ModelCopyException("Deep copy cannot render an arrayList of type '" + fieldGenericType.getName() + "' to parameratized type WaspModel or a java.lang class");
						  }
					  }
				  } catch (ClassCastException e){
					  logger.debug("Deep copy cannot cast object returned by "+getterMethodName+" to ArrayList");
				  }
			  } else {
				  if (WaspModel.class.isInstance(objFromGet)){
					  objToSet = WaspModel.getDeepCopy(objFromGet, (Class<? extends WaspModel>) fieldType);
				  } else if (fieldType.toString().contains("java.lang")){ 
					  // java.lang package wrapper classes are all immutable
					  objToSet = objFromGet;
				  } else if (java.util.Date.class.equals(fieldType)){
					  objToSet = new Date();
					  ((Date) objToSet).setTime( ((Date) objFromGet).getTime() );
				  } else {
					  throw new ModelCopyException("Deep copy cannot render a field of type '" + fieldType.getName() + "'  to type WaspModel or a java.lang class");
				  }
			  }
			  if (objToSet == null)
				  continue;
			  try {
				  clazz.getMethod(setterMethodName, field.getType()).invoke(clonedObj, objToSet);
			  } catch (Exception e) {
				  logger.debug("DeepCopy failed to work with method '"+setterMethodName+"'");
				  continue;
			  } 
		 // }
	  }
	  return clonedObj;
  }
  
  /**
   * Returns a SHALLOW copy of the supplied model object. Use example: Sample mySampleCopy = Sample.getShallowCopy(mySample);
   * A shallow copy creates a new instance of the same class type as the provided object and sets all attributes of the new object to equal the exact SAME objects 
   * referenced within the object being copied.
   * NOTE: even if the model object is lazy loaded, this method will ensure all fields in the copy are fully populated
   * @param waspModelObj
   * @return
   */
  public static <T extends WaspModel> T getShallowCopy(T waspModelObj){
	  Object clonedObj = null;
	  Class<T> clazz = (Class<T>) waspModelObj.getClass();
	  try {
		  clonedObj = clazz.newInstance();
	  } catch (InstantiationException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
	  } catch (IllegalAccessException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
	  }
	  List<Field> allClassFields= new ArrayList<Field>();
	  Class<?> clazzToReap = clazz;
	  do{
		  for (Field f: clazzToReap.getDeclaredFields())
			  allClassFields.add(f);
		  clazzToReap = clazzToReap.getSuperclass();
	  } while(clazzToReap != null);
	  for (Field field : allClassFields){
		  String getterMethodName = "get"+WordUtils.capitalize(field.getName());
		  String setterMethodName = "set"+WordUtils.capitalize(field.getName());
		  
		  // get from source object
		  Object objFromGet = null; 
		  try {
			  objFromGet = clazz.getMethod(getterMethodName).invoke(waspModelObj);
		  } catch (NoSuchMethodException e) {
			  // ignore
			  continue;
		  } catch (Exception e) {
			  logger.debug("ShallowCopy failed to work with method '"+getterMethodName+"'");
			  continue;
		  } 
		  if (objFromGet == null)
			  continue;
		  // set to destination object
		  Object objToSet = objFromGet;
		  try {
			  clazz.getMethod(setterMethodName, field.getType()).invoke(clonedObj, objToSet);
		  } catch (Exception e) {
			  logger.debug("ShallowCopy failed to work with method '"+setterMethodName+"'");
			  continue;
		  }
	  }
	  return (T) clonedObj;
  }

  
}
