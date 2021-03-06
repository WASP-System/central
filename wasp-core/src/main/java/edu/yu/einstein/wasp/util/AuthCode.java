package edu.yu.einstein.wasp.util;

import java.util.Random;

/**
 * 
 * @author asmclellan
 *
 */
public class AuthCode {

	public static String create(int length){
		if (length < 5 || length > 50){
			length = 20; //default 
		}
		String authcode = new String();
		Random random = new Random();
		for (int i=0; i < length; i++){
			int ascii = 0;
				switch(random.nextInt(3)){
		  		case 0:
		  			ascii = 48 + random.nextInt(10); // 0-9
		  		break;
		  		case 1:
		  			ascii = 65 + random.nextInt(26); // A-Z 
		  		break;
		  		case 2:
		  			ascii = 97 + random.nextInt(26); // a-z
		  		break;	
			}
				authcode = authcode.concat(String.valueOf( (char)ascii ));  
		}
		return authcode;
	}
  
}

