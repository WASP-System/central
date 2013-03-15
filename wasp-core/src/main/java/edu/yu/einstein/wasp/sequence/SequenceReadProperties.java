package edu.yu.einstein.wasp.sequence;

import java.util.List;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.dao.WaspMetaDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.WaspModel;
import edu.yu.einstein.wasp.util.MetaHelper;

public class SequenceReadProperties {
	
	static class ReadType{
		public static final String SINGLE = "single";
		public static final String PAIRED = "paired";
		public static final String UNDEFINED = "undefined";
		
		public static boolean isReadType(String readType){
			if (readType.equals(READ_TYPE_KEY) || readType.equals(SINGLE) || readType.equals(PAIRED) || readType.equals(UNDEFINED))
				return true;
			return false;
		}
	}
	
	public static final String READ_LENGTH_KEY = "readLength";
	public static final String READ_TYPE_KEY = "readType";
	
	private String readType;
	
	private Integer readLength;

	public Integer getReadLength() {
		return readLength;
	}

	public void setReadLength(Integer readLength) {
		this.readLength = readLength;
	}

	public String getReadType() {
		return readType;
	}

	public void setReadType(String readType){
		Assert.assertTrue(ReadType.isReadType(readType));
		this.readType = readType;
	}
	
	public SequenceReadProperties(){};

	public SequenceReadProperties(String readType, Integer readLength) {
		setReadType(readType);
		setReadLength(readLength);
	}
	
	/**
	 * Get SequenceReadProperties from metadata of supplied modelInstance. If 
	 * @param modelInstance
	 * @param metaClass
	 * @return
	 * @throws MetadataException
	 */
	public static <T extends MetaBase> SequenceReadProperties getSequenceReadProperties(WaspModel modelInstance, String area, Class<T> metaClass) throws MetadataException{
		Assert.assertParameterNotNull(modelInstance, "you must supply a value for modelInstance");
		Assert.assertParameterNotNull(metaClass, "you must supply a value for metaClass");
		Assert.assertTrue(! MetaBase.class.isInstance(modelInstance), "modelInstance cannot be of type MetaBase");
		String type = null;
		Integer length = null;
		MetaHelper metaHelper = new MetaHelper(metaClass);
		if (area != null)
			metaHelper.setArea(area);
		try{
			metaHelper.setMetaList((List<? extends MetaBase>) metaClass.getMethod("get" + metaClass.getSimpleName()).invoke(modelInstance));
			try{
				type = metaHelper.getMetaValueByName(READ_TYPE_KEY);
			} catch (MetadataException e){}
			try{
				length = Integer.parseInt(metaHelper.getMetaValueByName(READ_LENGTH_KEY));
			} catch (MetadataException e){}
		} catch (Exception e){
			throw new MetadataException("Failed to get metadata for modelInstance");
		}
		if (type == null && length == null)
			return null;
		return new SequenceReadProperties(type, length);
	}
	
	public static <T extends MetaBase> SequenceReadProperties getSequenceReadProperties(WaspModel modelInstance, Class<T> metaClass) throws MetadataException{
		return getSequenceReadProperties(modelInstance, null, metaClass);
	}
	
	public static <T extends MetaBase> void setSequenceReadProperties(SequenceReadProperties readProperties, WaspModel modelInstance, String area, WaspMetaDao<T> metaDao, Class<T> metaClass) throws MetadataException{
		Assert.assertParameterNotNull(readProperties, "you must supply a value for readProperties");
		Assert.assertParameterNotNull(modelInstance, "you must supply a value for modelInstance");
		Assert.assertParameterNotNull(metaDao, "you must supply a value for metaDao");
		Assert.assertParameterNotNull(metaClass, "you must supply a value for metaClass");
		Assert.assertTrue(! MetaBase.class.isInstance(modelInstance), "modelInstance cannot be of type MetaBase");
		MetaHelper metaHelper = new MetaHelper(metaClass);
		if (area != null)
			metaHelper.setArea(area);
		if (readProperties.getReadType() != null)
			metaHelper.setMetaValueByName(READ_TYPE_KEY, readProperties.getReadType());
		if (readProperties.getReadLength() != null)
			metaHelper.setMetaValueByName(READ_LENGTH_KEY, readProperties.getReadLength().toString());
		metaDao.setMeta((List<T>) metaHelper.getMetaList(), modelInstance.getId());
	}
	
	public static <T extends MetaBase> void setSequenceReadProperties(SequenceReadProperties readProperties, WaspModel modelInstance, WaspMetaDao<T> metaDao, Class<T> metaClass) throws MetadataException{
		setSequenceReadProperties(readProperties, modelInstance, null, metaDao, metaClass);
	}

}
