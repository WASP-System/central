package edu.yu.einstein.wasp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.WordUtils;

/**
 * 
 * @author nvolnova
 *
 */
public class StringHelper {
		
	// NOTE: Before adding methods in here check those available in the org.apache.commons.lang.WordUtils 
	//       and org.apache.commons.lang.StringUtils packages
			
	/**
	 * trim whitespace from left and right 
	 * AND
	 * if there are 2 or more adjacent spaces between words, then replace them with a single space
	 * Note: trim() will fail if sParam is empty string, so check before using
	 * @param sParam
	 * @return
	 */
	public static String trimAndRemoveExtraSpacesBetweenWords(String sParam){
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
	public static String removeExtraSpacesAndCapOnlyFirstLetterOfEachWord(String sParam){
		String modifiedString = trimAndRemoveExtraSpacesBetweenWords(sParam);
		return WordUtils.capitalize(modifiedString); // Capitalizes first letter of all the whitespace separated words in a String.
	}

	/**
	 * extract login from formatted string such as 'John Greally (jgreally)' and return login 
	 * in this example, the returned login would be 'jgreally'. If there is no parenthesis
	 * this method assumes the input string represents the login (e.g. 'jgreally')
	 * 
	 * @param formattedNameAndLogin
	 * @return user login name
	 */
	public static String getLoginFromFormattedNameAndLogin(String formattedNameAndLogin){
		Pattern userLoginRegexPattern = Pattern.compile("^.*?\\(?(\\w++)\\)?$");
		Matcher matchLoginRegex = userLoginRegexPattern.matcher(formattedNameAndLogin.trim());
		String userLogin = "";
		if (matchLoginRegex.find())
			userLogin = matchLoginRegex.group(1);
		return userLogin;
	}
}
