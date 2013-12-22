package com.swe.prototype.globalsettings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {
	
	/*Die Methode sollte auf jeden fall mal richtig getestet werden*/
	public static boolean isValidIP(String ip){
		try{
			Pattern VALID_IPV4_PATTERN = Pattern.compile("(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])", Pattern.CASE_INSENSITIVE);
			Matcher m1 = VALID_IPV4_PATTERN.matcher(ip);
			if (m1.matches()) {
			      return true;
			}
			    
		}
		catch(Exception e){
			
		}
	    
		return false;
	}
	public static boolean isValidPort(String port){
		try{
			int x = Integer.parseInt(port);
			if(x>=0 && x<=65535){
				return true;
			}
		}
		catch(Exception e){
		}
		
		return false;
	}

}
