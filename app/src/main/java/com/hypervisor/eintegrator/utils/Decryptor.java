package com.hypervisor.eintegrator.utils;// class to check the decryption method for the input data

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class Decryptor {
	
	private static SecretKeySpec secretKey;
	private static IvParameterSpec ivParameterSpec;
	
	public static String decrypt(String decryptionSTR) {
		// TODO Auto-generated method stub
		
		String appKey = "E-m!tr@2016";
		byte[] key;
		MessageDigest sha = null;
		
		try {
			if (decryptionSTR != null) {
				
				key = appKey.getBytes("UTF-8");
				sha = MessageDigest.getInstance("SHA-256");
				key = sha.digest(key);
				key = Arrays.copyOf(key, 16);
				
				secretKey = new SecretKeySpec(key, "AES");
				ivParameterSpec = new IvParameterSpec(key);
				
				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
				byte[] original = cipher.doFinal(Base64.decode(decryptionSTR));
				return new String(original);
			}
		} catch (NoSuchPaddingException npe) {
			npe.printStackTrace();
		} catch (NoSuchAlgorithmException nae) {
			nae.printStackTrace();
		} catch (InvalidKeyException ike) {
			ike.printStackTrace();
		} catch (BadPaddingException bpe) {
			bpe.printStackTrace();
		} catch (IllegalBlockSizeException ibe) {
			ibe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String parseXml(String inputXml) {
		
		if(inputXml.contains("text-success"))
			return "success";
		else if(inputXml.contains("text-failure"))
			return "failure";
		else if(inputXml.contains("text-cancel"))
			return "cancel";
		else return "";
	}
}
