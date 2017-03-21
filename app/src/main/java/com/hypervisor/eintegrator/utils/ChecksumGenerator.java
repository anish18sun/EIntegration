package com.hypervisor.eintegrator.utils;// class to generate the checksum for input request

import java.util.Formatter;
import java.security.MessageDigest;

public class ChecksumGenerator {
	
	public static String checksum(final String toBeEncryptString){
		if (toBeEncryptString == null) {
			throw new IllegalArgumentException("To be encrypt string must not be null");
		}
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(toBeEncryptString.getBytes());
			byte byteData[] = md.digest();
			return byteArray2Hex(byteData);
		} catch (Exception ex) {
			System.out.println("Exception : " + ex);
		}
		
		return "String could not be conveted to checksum.";
	}
	
	public static String byteArray2Hex(byte[] bytes) {
		
		Formatter formatter = null;
		
		try {
			formatter = new Formatter();
			byte[] arrayOfByte = bytes;
			int j = bytes.length;
			for (int i = 0; i < j; i++) {
				byte b = arrayOfByte[i];
				formatter.format("%02x", new Object[] { Byte.valueOf(b) });
			}
			return formatter.toString();
		}
		catch(Exception e) { System.out.println("Exception :" + e); }
		
		finally {
			formatter.close();
		}
		
		return "Byte array could not be converted to hexadecimal";
	}

	public static void main(String args[]) {

		String inputString = "HACKATHON2017|125685|100.00|#2&[W<nJ*K\"xO_z";
		String checksumString = checksum(inputString);
		System.out.println("Generated String : " + checksumString);
	}
}
