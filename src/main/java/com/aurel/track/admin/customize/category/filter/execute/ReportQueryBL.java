/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */

package com.aurel.track.admin.customize.category.filter.execute;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class ReportQueryBL {
	
	private static final Logger LOGGER = LogManager.getLogger(ReportQueryBL.class);
	
	public static String a(String s){
		char e[]=("17"+"Y"+"S"+"B"+"0"+"["+"u"+"0"+"["+"K"+"e"+"z"+"]"+"blah"+"21").toCharArray();
		return encrypt(s,e);
	}
	
	public static String b(String s){
		char f[]=("17"+"Y"+"S"+"B"+"0"+"["+"u"+"0"+"["+"K"+"e"+"z"+"]"+"blah"+"21").toCharArray();
		return dcl(s,f);
	}
	public static String encodeMapAsUrl(Map<String,String> map){
		StringBuffer sb=new StringBuffer();
		Iterator<String> it= map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			sb.append(key).append("=").append(map.get(key)).append("&");
		}
		return sb.toString();
	}
	public static Map<String,String> decodeMapFromUrl(String s){
		Map<String,String> map=new HashMap<String, String>();
		String[] params=s.split("&");
		for (int i = 0; i < params.length; i++) {
			if(params[i]!=null&&params[i].length()>0){
				String[] p=params[i].split("=");
				if(p.length==2){
					map.put(p[0], p[1]);
				}
			}
		}
		return map;
	}
	// Salt
	private static byte[] salt = {
				(byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
				(byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99
	};
	// Iteration count
	static int  count = 20;
	private static String encrypt(String clearText, char[] password) {
		// Create PBE parameter set
		PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, count);
		byte[] ciphertext = {0};

		PBEKeySpec pbeKeySpec = new PBEKeySpec(password);
		try {
			SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

			// Create PBE Cipher
			Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");

			// Initialize PBE Cipher with key and parameters
			pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

			// Encrypt the cleartext
			ciphertext = pbeCipher.doFinal(clearText.getBytes());
		}
		catch (Exception e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e),e);
		}
		return new String(Base64.encodeBase64(ciphertext));
	}

	private static String dcl(String encryptedText, char[] password) {
		byte[] clearText = {' '};
		int count = 20;
		PBEKeySpec pbeKeySpec;
		PBEParameterSpec pbeParamSpec;
		SecretKeyFactory keyFac;
		// Create PBE parameter set
		pbeParamSpec = new PBEParameterSpec(salt, count);
		pbeKeySpec = new PBEKeySpec(password);
		try {
			keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

			// Create PBE Cipher
			Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");

			// Initialize PBE Cipher with key and parameters
			pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);

			byte[] ciphertext = Base64.decodeBase64(encryptedText);

			//Decrypt the cleartext
			clearText = pbeCipher.doFinal(ciphertext);
		}
		catch (Exception e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e),e);
		}
		return new String(clearText);
	}
}
