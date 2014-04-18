package com.xm.netkuu.util;

public class Verification {
	private static final String sHostAddressRegex = "^((http://)?||(https://)?)(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
			+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." 
			+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." 
			+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)(/)?$";
	
	public static boolean verifyHostAddress(String hostAddress){
		return hostAddress != null && hostAddress.matches(sHostAddressRegex);
	}
}
