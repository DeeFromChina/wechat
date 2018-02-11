package com.dee.util;

import java.security.MessageDigest;

import sun.misc.BASE64Encoder;

public class Encryption {
	
	public static String encryption(String password) throws Exception {
		try{
			   MessageDigest sha = MessageDigest.getInstance("SHA");
			   BASE64Encoder encoder = new BASE64Encoder();
			   return encoder.encode(sha.digest(password.getBytes()));
		   }catch(Exception e){
			   e.printStackTrace();
			   return null;
		   }
	}
}
