/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */


package com.aurel.track.user;

import java.security.MessageDigest;

public class TpPasswordEncoder {

	private final static int RADIX = 16;

	/**
	 *
	 * @param rawPassword - clear text password
	 * @param salt - a user specific salt, up to 80 characters
	 * @return - an SHA-512 encrypted password string
	 */
	public String encodePassword(String rawPassword, Object salt) {
		if (salt != null && salt.getClass() == String.class ) {
			if (((String)salt).length() > 0) {
			rawPassword = rawPassword+salt;
			}
		}
		return encryptPassword(rawPassword);
	}

	/**
	 * This routine is just for LDAP user backward compatibility
	 * with a constant LDAP user password stored in the password field.
	 * @param rawPassword - clear text password
	 * @return - an SHA-1 encrypted password string
	 */
	public String encodePassword1(String rawPassword) {
		return encryptPassword1(rawPassword);
	}

	/**
	 *  This tries three different password encryption algorithms: SHA,
	 *  SHA hex coded with hyphens, and MD5.
	 *  @param encPassword - the encrypted password. The encrypted password
	 *  may be appended by a ":" and a salt string.
	 *  <br>
	 *  Example for Contao CMS:<br>
	 *  "791dfa6ba1a5be364803f8cf709c4745f09e9f67:b263360c7576f25a4ec8617"
	 *  <br>
	 *  In Contao the password hash is the SHA-1(salt+rawPass).
	 *  @param rawPass - the raw, clear text password
	 *  @param salt - a user specific salt, optional
	 *  @return true if the encrypted raw password matches the encrypted password
	 */
	public boolean isPasswordValid(String encPassword, String rawPass, Object salt) {
		String saltPass = rawPass;
		if (salt != null && salt.getClass() == String.class ) {
			if (((String)salt).length() > 0) {
				saltPass = saltPass+salt;
			}
		} else {
			String tokens[] = encPassword.split(":");
			if (tokens.length > 1) {
				saltPass = tokens[1] + rawPass;
				encPassword = tokens[0];
			}
		}
		if (encryptPassword(saltPass).equals(encPassword)) {
			return true;
		}
		else if (encryptPassword1(saltPass).equals(encPassword)) {
			return true;
		}
		else if (encryptPassword2(saltPass).equals(encPassword)) {
			return true;
		}
		else if(encryptPasswordMd5(saltPass).equals(encPassword)) {
			return true;
		}else {
			return false;
		}
	}


	/**
	 * Encrypts an input password into a new string using the SHA-512
	 * algorithm. The encrypted password is returned as string
	 * in hexadecimal notation without hyphens in between (e.g.
	 * '455880501e5a361954272459dd104c6824521d')
	 * @param password the password to be encrypted
	 * @return the encrypted password.
	 */
	private String encryptPassword(String password) {
		// encrypts a password using the SHA-512 algorithm
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-512");
			sha.update(password.getBytes("ISO-8859-1"));
			byte[] hash = sha.digest();

			return encodeBase16(hash);
		}
		catch (Exception e) {
		    System.err.println("TpPasswordEncoder: No such algorithm");
		}
		return password;
	}

	/**
	 * Encrypts an input password into a new string using the SHA
	 * algorithm. The encrypted password is returned as string
	 * in hexadecimal notation without hyphens in between (e.g.
	 * '455880501e5a361954272459dd104c6824521d')
     * Author: Stefan Craatz
	 * @param password the password to be encrypted
	 * @return the encrypted password.
	 */
	public String encryptPassword1(String password) {
		// encrypts a password using the SHA-1 algorithm
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			sha.update(password.getBytes("ISO-8859-1"));
			byte[] hash = sha.digest();

			return encodeBase16(hash);
		}
		catch (Exception e) {
		    System.err.println("TpPasswordEncoder: No such algorithm");
		}
		System.err.println(password);
		return password;
	}

	/**
	 * Encrypts an input password into a new string using the SHA
	 * algorithm. The encrypted password is returned as string
	 * in hexadecimal notation with hyphens in between (e.g.
	 * '-45-58-8050-1e5a36-1954-27-2459-6dd104c-68-24521d')
     * This is for compatibility reasons only. New passwords
     * are encrypted with encryptPassword() method to match
     * Tomcat container managed security and openSSL.
     * @param password the password to be encrypted
	 * @return the encrypted password.
	 */
	private String encryptPassword2(String password) {
        // encrypts a password using the SHA algorithm
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            sha.update(password.getBytes("ISO-8859-1"));
            byte[] hash = sha.digest();
            StringBuffer newPassword = new StringBuffer();
            int hex = 0;
            Byte hexbyte = null;
            for (int i = 0; i < hash.length; i++) {
                hexbyte = new Byte(hash[i]);
                hex = hexbyte.intValue();
                newPassword.append(Integer.toString(hex, RADIX));
            }
			return newPassword.toString();
        }
        catch (Exception e) {
            // do nothing
        }
		return password;
    }

	/**
	 * Encodes an array of bytes to a hex-string.
	 *
	 * @param bytes Array of bytes to be encoded
	 * @return The encoded string.
	 */
	private String encodeBase16(byte[] bytes) {
		StringBuffer sb = new StringBuffer(bytes.length * 2);

		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];

			// top 4 bits
			char c = (char)((b >> 4) & 0xf);
			if(c > 9)
				c = (char)((c - 10) + 'a');
			else
				c = (char)(c + '0');
			sb.append(c);

			// bottom 4 bits
			c = (char)(b & 0xf);
			if (c > 9)
				c = (char)((c - 10) + 'a');
			else
				c = (char)(c + '0');
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * Encrypts an input password into a new string using the MD5
	 * algorithm. The encrypted password is returned as string
	 * in hexadecimal notation with hyphens in between (e.g.
	 * '-45-58-8050-1e5a36-1954-27-2459-6dd104c-68-24521d')
     * This is for compatibility reasons only. New passwords
     * are encrypted with encryptPassword() method to match
     * Tomcat container managed security and openSSL.
     * @param password the password to be encrypted
	 * @return the encrypted password.
	 */
	private String encryptPasswordMd5(String password) {
        // encrypts a password using the SHA algorithm
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(password.getBytes("ISO-8859-1"));
            byte[] hash = md5.digest();
            StringBuffer newPassword = new StringBuffer();
            int hex = 0;
            Byte hexbyte = null;
            for (int i = 0; i < hash.length; i++) {
                hexbyte = new Byte(hash[i]);
                hex = hexbyte.intValue();
                newPassword.append(Integer.toString(hex, RADIX));
            }
			return newPassword.toString();
        }
        catch (Exception e) {
            // do nothing
        }
		return password;
    }

}
