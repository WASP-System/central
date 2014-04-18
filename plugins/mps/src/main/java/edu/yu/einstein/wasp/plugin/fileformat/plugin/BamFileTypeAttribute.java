package edu.yu.einstein.wasp.plugin.fileformat.plugin;

import edu.yu.einstein.wasp.filetype.FileTypeAttribute;

public class BamFileTypeAttribute extends FileTypeAttribute {
	
	public static final BamFileTypeAttribute SORTED = new BamFileTypeAttribute("sorted");
	public static final BamFileTypeAttribute DEDUP = new BamFileTypeAttribute("dedup");

	public BamFileTypeAttribute(String attrName) {
		super(attrName);
	}

}
