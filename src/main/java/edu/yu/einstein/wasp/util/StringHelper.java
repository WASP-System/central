package edu.yu.einstein.wasp.util;

/**
 * 
 * @author nvolnova
 *
 */
public class StringHelper {
		
	
	
	/**
	 * capitalize first letter of each word in name
	 * code derived from http://stackoverflow.com/questions/1149855/how-to-upper-case-every-first-letter-of-word-in-a-string
	 * 
	 * @param sParam
	 * @return
	 */
	public String toCapFirstLetter(String sParam) {
		
		StringBuilder b = new StringBuilder(sParam);
		int i = 0;
		do {
		  b.replace(i, i + 1, b.substring(i,i + 1).toUpperCase());
		  i =  b.indexOf(" ", i) + 1;
		} while (i > 0 && i < b.length());
		
		String modifiedName = new String(b);
		
		return modifiedName;
	}

}
