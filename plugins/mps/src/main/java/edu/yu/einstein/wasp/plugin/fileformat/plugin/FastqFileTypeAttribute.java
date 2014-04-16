package edu.yu.einstein.wasp.plugin.fileformat.plugin;

import edu.yu.einstein.wasp.filetype.FileTypeAttribute;

public class FastqFileTypeAttribute extends FileTypeAttribute {
	
	public static final FastqFileTypeAttribute TRIMMED = new FastqFileTypeAttribute("trimmed");

	public FastqFileTypeAttribute(String attrName) {
		super(attrName);
	}

}
