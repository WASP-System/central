package edu.yu.einstein.wasp.filetype;

import java.util.HashSet;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

public class FileTypeAttributeTest {
	
	private class NewFileTypeAttribute extends FileTypeAttribute{
		
		public NewFileTypeAttribute(String code){
			super(code);
		}
		
	}
	
	private FileTypeAttribute fta1 = new FileTypeAttribute("foo");
	private NewFileTypeAttribute fta2 = new NewFileTypeAttribute("foo");
	private FileTypeAttribute fta3 = new FileTypeAttribute("bar");
  
	@Test (groups = "unit-tests")
	public void testEquals() {
		Assert.assertTrue(fta1.equals(fta2));
		Assert.assertTrue(fta2.equals(fta1));
		Assert.assertFalse(fta1.equals(fta3));
		Assert.assertFalse(fta2.equals(fta3));
	}
	
	@Test (groups = "unit-tests")
	public void testHash() {
		Set<FileTypeAttribute> set1 = new HashSet<>();
		set1.add(fta1);
		set1.add(fta2);
		set1.add(fta3);
		Set<FileTypeAttribute> set2 = new HashSet<>();
		set2.add(fta3);
		Assert.assertTrue(set1.size() == 2);
		Assert.assertTrue(set1.contains(fta3));
		Assert.assertTrue(set2.contains(fta3));
		Assert.assertFalse(set2.contains(fta1));
	}
}
