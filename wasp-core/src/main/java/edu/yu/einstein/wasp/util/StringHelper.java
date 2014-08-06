package edu.yu.einstein.wasp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
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
	 * @param sParam
	 * @return
	 */
	public static String trimAndRemoveExtraSpacesBetweenWords(String sParam){
		if (sParam == null || sParam.isEmpty()){
			return ""; // trim() will fail if sParam is empty string, so simply return empty string
		}
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
	 * @param sParam
	 * @return
	 */
	public static String removeExtraSpacesAndCapOnlyFirstLetterOfEachWord(String sParam){
		String modifiedString = trimAndRemoveExtraSpacesBetweenWords(sParam);
		return WordUtils.capitalize(modifiedString); // Capitalizes first letter of ALL the whitespace separated words in a String.
	}
	
	/**
	 * trim whitespace from left and right 
	 * AND
	 * if there are 2 or more adjacent spaces between words, then replace them with a single space
	 * AND
	 * capitalize ONLY the first letter of THE FIRST word (with all other letters set to lower case)
	 * @param sParam
	 * @return
	 */
	public static String removeExtraSpacesAndCapFirstLetter(String sParam){
		String modifiedString = trimAndRemoveExtraSpacesBetweenWords(sParam);
		return StringUtils.capitalize(modifiedString); // Capitalizes first letter in a String.
	}

	/**
	 * extract login from formatted string such as 'John Greally (jgreally)' and return login 
	 * in this example, the returned login would be 'jgreally'. If there is no parenthesis
	 * this method assumes the input string represents the login (e.g. 'jgreally')
	 * Note: regular expression was modified 7/9/12 to include names such as lhall-pearson
	 * 
	 * @param formattedNameAndLogin
	 * @return user login name
	 */
	public static String getLoginFromFormattedNameAndLogin(String formattedNameAndLogin){
		if (formattedNameAndLogin == null || formattedNameAndLogin.isEmpty()){
			return ""; // trim() will fail if formattedNameAndLogin is empty string, so simply return empty string
		}
		Pattern userLoginRegexPattern = Pattern.compile("^.*?\\(?([-\\w+]+)\\)?$");
		Matcher matchLoginRegex = userLoginRegexPattern.matcher(formattedNameAndLogin.trim());
		String userLogin = "";
		if (matchLoginRegex.find())
			userLogin = matchLoginRegex.group(1);
		return userLogin;
	}
	
	/**
	 * Returns true if the supplied string is a valid email address format
	 * @param s
	 * @return true/false
	 */
	public static boolean isStringAValidEmailAddress(String s){
		if (s == null || s.isEmpty()) return false;
		return s.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	}
	
	/**
	 * Converts a string to Integer. Non-numeric characters are excluded (example: string "   # J1001 " converted to Integer 1001). 
	 * @param String s
	 * @return Integer
	 */
	public static Integer convertStringToInteger(String s){
		Integer _integer = null;
		if(s != null){
			StringBuilder sb = new StringBuilder();
			for(int i=0; i<s.length(); i++)
			{
				if(Character.isDigit(s.charAt(i))){
					sb.append(s.charAt(i));
				}
			}
			if(sb.length() > 0){
				int id = Integer.parseInt(sb.toString());
				_integer = new Integer(id);
			}
		}
		return _integer;
	}
	
	/**
	 * Converts provided string to camel-case e.g. "Foo bar baz" -> "fooBarBaz"
	 * @param s
	 * @return
	 */
	public static String toCamelCase(String s){
		return WordUtils.uncapitalize(WordUtils.capitalizeFully(s).replaceAll(" ", ""));
	}
	
	public static String deCamelCase(String s){
		Pattern p = Pattern.compile("([a-z])([A-Z])");
		Matcher m = p.matcher(s);
		while (m.find())
			s = s.replaceFirst(m.group(0), m.group(1) + " " + m.group(2).toLowerCase());
		return StringUtils.capitalize(s);
	}
}
