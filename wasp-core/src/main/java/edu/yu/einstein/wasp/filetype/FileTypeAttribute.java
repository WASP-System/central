package edu.yu.einstein.wasp.filetype;

public class FileTypeAttribute {
	
	private final String attrName;

	public FileTypeAttribute(String attrName) {
		this.attrName = attrName;
	}
	
	@Override 
	public String toString(){
		return attrName;
	}
	
	@Override
	public boolean equals(Object obj){
		if (this == obj) return true;
		if (obj == null) return false;
		if (!this.getClass().isInstance(obj) && !obj.getClass().isInstance(this.getClass())) 
			return false; // allow comparison if one class is derived from the other
		FileTypeAttribute attr = (FileTypeAttribute) obj;
		return attrName.equals(attr);
	}
	
	@Override
	public int hashCode(){
		return attrName.hashCode();
	}

}
