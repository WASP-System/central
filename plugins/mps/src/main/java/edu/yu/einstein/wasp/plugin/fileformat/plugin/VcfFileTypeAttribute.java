package edu.yu.einstein.wasp.plugin.fileformat.plugin;

import edu.yu.einstein.wasp.filetype.FileTypeAttribute;

public class VcfFileTypeAttribute extends FileTypeAttribute {
	
	public static final VcfFileTypeAttribute SORTED = new VcfFileTypeAttribute("sorted");
	public static final VcfFileTypeAttribute ANNOTATED = new VcfFileTypeAttribute("annotated");
	public static final VcfFileTypeAttribute GVCF = new VcfFileTypeAttribute("gvcf");

	public VcfFileTypeAttribute(String attrName) {
		super(attrName);
	}

}
