package com.onescience.journal.methods;

import java.util.regex.Pattern;

public class Issn_methods {
	
	public Issn_methods(){
	}
	
	// test if a String corresponds a Issn standard format
	public boolean issnformat(String issn){
		Pattern pattern = Pattern.compile("\\d{4}-\\d{3}(\\d|x|X)");
		return pattern.matcher(issn).matches();
	}
	
	// return the standard format of a Issn String (example: "xxxxxxxx" -> "xxxx-xxxx")
	public String issnreformat(String issn){
		String result = "";
		Pattern p1 = Pattern.compile("\\d{4}-\\d{3}(\\d|x|X)");
		Pattern p2 = Pattern.compile("\\d{7}(\\d|x|X)");
		if (p2.matcher(issn).matches()){
			issn = issn.substring(0, 4) + "-" + issn.substring(4, 8);
		}
		if (p1.matcher(issn).matches()){
			result = issn;
		}
		return result;
	}
	
	// test if a Issn is valid (verify the last number from the first 7 number)
	public boolean issnvalide(String issn){
		boolean result = false;
		if (issnformat(issn)){
			String c7 = issn.substring(0, 4) + issn.substring(5, 8);
			int s = 0;
			for (int i = 1; i<8; i++){
				s = s+ (i+1)* Integer.parseInt(c7.substring(7-i, 8-i));
			}
			int m = 11 - s % 11;
			if (m == 11){
				m = 0;
			}
			String c8;
			if (m == 10) {
				c8 = "x";
			} else{
				c8 = Integer.toString(m);
			}
			result = c8.equals(issn.substring(8, 9).toLowerCase());
		}
		return result;
	}

}
