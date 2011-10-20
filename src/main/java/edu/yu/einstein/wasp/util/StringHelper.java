package edu.yu.einstein.wasp.util;

/**
 * 
 * @author nvolnova
 *
 */
public class StringHelper {
		
	
	
	/**
	 * capitalize first letter of each word in sParam
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
	
	/**
	 * capitalize first letter of each word in sParam; and all other letters will be lower case
	 * code modified from http://stackoverflow.com/questions/1149855/how-to-upper-case-every-first-letter-of-word-in-a-string
	 * 
	 * @param sParam
	 * @return
	 */
	public String toCapOnlyFirstLetterOfEachWord(String sParam) {
		
		String lowercaseString = sParam.toLowerCase();
		StringBuilder b = new StringBuilder(lowercaseString);
		int i = 0;
		do {
		  b.replace(i, i + 1, b.substring(i,i + 1).toUpperCase());
		  i =  b.indexOf(" ", i) + 1;
		} while (i > 0 && i < b.length());
		
		String modifiedName = new String(b);
		
		return modifiedName;
	}
	
	/**
	 * trim whitespace from left and right 
	 * AND
	 * if there are 2 or more adjacent spaces between words, then replace them with a single space
	 * Note: trim() will fail if sParam is empty string, so check before using
	 * @param sParam
	 * @return
	 */
	public String toTrimAndRemoveExtraSpacesBetweenWords(String sParam){
		String modifiedString = sParam.trim();//trim whitespace from left and right
		modifiedString = modifiedString.replaceAll("\\s+", " ");//if there are 2 or more adjacent spaces between words, replace them with a single space
		return modifiedString;
	}
	
	/**
	 * trim whitespace from left and right 
	 * AND
	 * if there are 2 or more adjacent spaces between words, then replace them with a single space
	 * AND
	 * capitalize ONLY the first letter of EACH word (with all other letters set to lower case)
	 * Note: trim() will fail if sParam is empty string, so check before using
	 * @param sParam
	 * @return
	 */
	public String toRemoveExtraSpacesAndCapOnlyFirstLetterOfEachWord(String sParam){
		
		String modifiedString = sParam.trim();//trim whitespace from left and right
		modifiedString = modifiedString.replaceAll("\\s+", " ");//if there are 2 or more adjacent spaces between words, replace them with a single space
		modifiedString = toCapOnlyFirstLetterOfEachWord(modifiedString);
		return modifiedString;
	}

	/**
	 * extract login from formatted string such as John Greally (jgreally) and return login 
	 * in this example, the returned login would be jgreally
	 * 
	 * @param formattedNameAndLogin
	 * @return
	 */
	public String getLoginFromFormattedNameAndLogin(String formattedNameAndLogin){
		
		int startIndex = formattedNameAndLogin.indexOf("(");
		int endIndex = formattedNameAndLogin.indexOf(")");
		return formattedNameAndLogin.substring(startIndex+1, endIndex);		
	}
}
