package com.hypervisor.eintegrator.utils;


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


public class Encryptor {
	
	private static SecretKeySpec secretKey;
	private static IvParameterSpec ivParameterSpec;
	
	public static String encrypt(String encryptionSTR) {
		
		String appKey = "E-m!tr@2016";
		byte[] key;
		MessageDigest sha = null;
		
		String encode = "";		
		
		try {
			if (encryptionSTR != null) {
				
				key = appKey.getBytes("UTF-8");
				sha = MessageDigest.getInstance("SHA-256");
				key = sha.digest(key);
				key = Arrays.copyOf(key, 16);
				
				secretKey = new SecretKeySpec(key, "AES");
				ivParameterSpec = new IvParameterSpec(key);
				
				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
				final String encryptedString = Base64.encode(cipher.doFinal(encryptionSTR.getBytes("UTF-8")));
				encode = encryptedString;
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
		return encode;
	}

}
