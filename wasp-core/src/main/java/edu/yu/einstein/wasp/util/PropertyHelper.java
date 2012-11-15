/**
 * 
 */
package edu.yu.einstein.wasp.util;

/**
 * @author calder
 *
 */
public class PropertyHelper {
	
	public static boolean isSet(String property) {
		if (property == null) return false;
		property = property.replace("^ +", "");
		property = property.replace("^ +$", "");
		if (property.length() == 0) return false;
		if (property.startsWith("${")) return false;
		return true;
	}

}
