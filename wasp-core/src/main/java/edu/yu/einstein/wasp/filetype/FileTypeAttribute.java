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
		if (obj == null || !this.getClass().isInstance(obj)) return false;
		FileTypeAttribute attr = (FileTypeAttribute) obj;
		return attrName.equals(attr);
	}
	
	@Override
	public int hashCode(){
		return attrName.hashCode();
	}

}
