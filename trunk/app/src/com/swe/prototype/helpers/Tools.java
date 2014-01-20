package com.swe.prototype.helpers;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dany Brossel
 *
 *	Hier kommen alle methoden  rein, die man an mehreren stellen zur prüfung und konvertierung brauchen kann.
 */
public class Tools {

	/* Die Methode sollte auf jeden fall mal richtig getestet werden */
	public static boolean isValidIP(String ip) {
		try {
			Pattern VALID_IPV4_PATTERN = Pattern
					.compile(
							"(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])",
							Pattern.CASE_INSENSITIVE);
			Matcher m1 = VALID_IPV4_PATTERN.matcher(ip);
			if (m1.matches()) {
				return true;
			}

		} catch (Exception e) {

		}

		return false;
	}
	
	public static boolean isValidHost(String newIP) {
		// TODO Auto-generated method stub
		return true;
	}

	public static boolean isValidPort(String port) {
		try {
			int x = Integer.parseInt(port);
			if (x >= 0 && x <= 65535) {
				return true;
			}
		} catch (Exception e) {
		}

		return false;
	}

	/**
	 * @param date
	 *            format: 2014-06-32
	 * @return format: 32 July 2014
	 */
	public static String convertDate(String date) {
		String res = "";
		try {
			if (date.charAt(8) == '0') {
				res += date.charAt(9);
			} else {
				res += date.charAt(8) + "" + date.charAt(9);
			}
			if (date.charAt(8) != '1') {
				switch (date.charAt(9)) {
				case '1':
					res += "st";
					break;
				case '2':
					res += "nd";
					break;
				case '3':
					res += "rd";
					break;
				default:
					res += "th";
				}
			} else {
				res += "th";
			}
			res += " ";
			// month
			if (date.charAt(5) == '1') {
				switch (date.charAt(6)) {
				case '0':
					res += "October";
					break;
				case '1':
					res += "November";
					break;

				case '2':
					res += "December";
				}
			} else {
				switch (date.charAt(6)) {
				case '1':
					res += "January";
					break;
				case '2':
					res += "February";
					break;
				case '3':
					res += "March";
					break;
				case '4':
					res += "April";
					break;
				case '5':
					res += "May";
					break;
				case '6':
					res += "June";
					break;
				case '7':
					res += "July";
					break;
				case '8':
					res += "August";
					break;
				case '9':
					res += "September";

				}

			}
			// year
			res += " " + date.charAt(0);
			res += date.charAt(1);
			res += date.charAt(2);
			res += date.charAt(3);
		} catch (Exception e) {
			return date;
		}
		return res;
	}

	public static String concertSimpleDateFormatToNormal(String cdate) {
		//getestet, sollte klappen
		String res = "";
		//format cdate: dd/mm/yyyy
		res+=cdate.substring(6, 10)+"-";
		res+=""+cdate.charAt(3)+""+cdate.charAt(4)+"-";
		res+=""+cdate.charAt(0)+""+cdate.charAt(1);
		
		return res;
	}



	

}
