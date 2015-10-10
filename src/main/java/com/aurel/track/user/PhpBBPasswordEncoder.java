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


package com.aurel.track.user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generates and verifies password hashes.
 */
public class PhpBBPasswordEncoder {

	private static final Logger LOGGER = Logger.getLogger("Access");

	/**
	 * Non-standard compliant Base64 character mapping.
	 *
	 * Phpass's Base64 character mapping table deviates from RFC 2045:
	 * ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/
	 */
	private static final char[] BASE64_CHAR_MAPPING = {
		'.', '/',
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

	private static final int HASH_ITERATIONS = 15;

	private static final int HASH_SIZE_MD5 = 34;
	private static final int HASH_SIZE_SHA = 55;

	private MessageDigest messageDigestMD5;
	private MessageDigest messageDigestSHA;
	private SecureRandom secureRandom;

	public PhpBBPasswordEncoder() {
		secureRandom = new SecureRandom();
		try {
			messageDigestMD5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			LOGGER.log(Level.SEVERE, "Could not create an MD5 digest.");
		}
		try {
			messageDigestSHA = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			LOGGER.log(Level.SEVERE, "Could not create an SHA-512 digest.");
		}
	}


	/**
	 *  This tries three different password encryption algorithms: SHA, 
	 *  SHA hex coded with hyphens, and MD5.
	 *  @param encPassword - the encrypted password
	 *  @param rawPass - the raw, clear text password
	 *  @param salt - a user specific salt
	 *  @return true if the encrypted raw password matches the encrypted password
	 */
	public boolean isPasswordValid(String encPassword, String rawPass, Object salt) {
		// The first 12 digits of the hash is used to modify the encryption.
		String setting = encPassword.substring(0, 12);
		if (encPassword.equals(encryptMD5(rawPass, setting))) {
			return true;
		} else {
			return encPassword.equals(encryptSHA(rawPass, setting));
		}
	}

	/**
	 * The first 3 characters of the setting is ignored; it is used to describe
	 * the hashing algorithm, but we're always using SHA-512
	 *
	 * @return Encrypted hash of the password using the given settings
	 */
	private String encryptMD5(String password, String setting) {
		String salt = setting.substring(4, 12);
		// The index in our mapping table of the 4th character of the setting is
		// used to determine the number of times we apply the SHA512 hashing
		int log2Iterations = new String(BASE64_CHAR_MAPPING).indexOf(setting.substring(3, 4));
		// We apply the SHA-512 hashing log2Iterations^2 times
		int iterations = (int) Math.pow(2, log2Iterations);

		// Initially hash the password with the salt
		byte[] hash = messageDigestMD5.digest((salt + password).getBytes());
		// Running the hash lots of times causes the hash function to be slow and
		// expensive, defending against brute-force attacks
		for (int i = 0; i < iterations; i++) {
			// At each iteration, re-salt using the password
			// This reduces the risk of collisions
			hash = messageDigestMD5.digest(concatArrays(hash, password.getBytes()));
		}

		// The final hash is the SHA'ed hash appended at the end of the setting
		String encodedHash = setting + encodeBase64(hash);
		// I have no idea why we have to truncate the hash at HASH_SIZE characters;
		// in fact that sounds incredibly wrong in every way possible, but that's
		// what PHPass does so we have to mimic the behavior
		String truncatedEncodedHash = encodedHash.substring(0, HASH_SIZE_MD5);
		return truncatedEncodedHash;
	}

	/**
	 * The first 3 characters of the setting is ignored; it is used to describe
	 * the hashing algorithm, but we're always using SHA-512
	 *
	 * @return Encrypted hash of the password using the given settings
	 */
	private String encryptSHA(String password, String setting) {
		String salt = setting.substring(4, 12);
		// The index in our mapping table of the 4th character of the setting is
		// used to determine the number of times we apply the SHA512 hashing
		int log2Iterations = new String(BASE64_CHAR_MAPPING).indexOf(setting.substring(3, 4));
		// We apply the SHA-512 hashing log2Iterations^2 times
		int iterations = (int) Math.pow(2, log2Iterations);

		// Initially hash the password with the salt
		byte[] hash = messageDigestSHA.digest((salt + password).getBytes());
		// Running the hash lots of times causes the hash function to be slow and
		// expensive, defending against brute-force attacks
		for (int i = 0; i < iterations; i++) {
			// At each iteration, re-salt using the password
			// This reduces the risk of collisions
			hash = messageDigestSHA.digest(concatArrays(hash, password.getBytes()));
		}

		// The final hash is the SHA'ed hash appended at the end of the setting
		String encodedHash = setting + encodeBase64(hash);
		// I have no idea why we have to truncate the hash at HASH_SIZE characters;
		// in fact that sounds incredibly wrong in every way possible, but that's
		// what PHPass does so we have to mimic the behavior
		String truncatedEncodedHash = encodedHash.substring(0, HASH_SIZE_SHA);
		return truncatedEncodedHash;
	}

	/**
	 * @return A randomly salted hash for the given password.
	 */
	public String encryptPassword(String password) {
		return encryptSHA(password, generateSetting());
	}

	/**
	 * Generates a random Base64 salt prefixed with settings for the hash.
	 */
	private String generateSetting() {
		String algorithm = "$H$"; // Always use SHA512
		String iterations = Character.toString(BASE64_CHAR_MAPPING[HASH_ITERATIONS]);
		String salt = generateRandomSalt(8);

		return algorithm + iterations + salt;
	}

	/**
	 * Generate a random salt using the Base64 alphabet of the given number of
	 * characters
	 */
	private String generateRandomSalt(int characters) {
		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < characters; i++) {
			stringBuilder.append(BASE64_CHAR_MAPPING[secureRandom.nextInt(64)]);
		}

		return stringBuilder.toString();
	}

	/**
	 * @return The second array appended at the end of the first array.
	 */
	private byte[] concatArrays(byte[] first, byte[] second) {
		byte[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	/**
	 * We have to use our own encode Base64 function here the one used by PHPass
	 * does not follow RFC 2045, and hence no other libraries out there actually
	 * supports it.
	 *
	 * If the end contains fewer than 24 input bits, do NOT pad, stop producing
	 * output bits.
	 *
	 * @return Base64 encoding of the given digest
	 */
	private String encodeBase64(byte[] input) {
		StringBuilder builder = new StringBuilder();
		int i = 0;

		// Normally, Base64 encoding looks at 24-bit chunks of the input data, but
		// because we're not doing zero-padding at the end, we must look at each
		// byte one at a time to make sure we don't get IndexOutOfBoundsExceptions
		// Base64 has 64 (2^6) characters in its dictionary, and is represented by
		// 6-bit chunks
		// It is acceptable to evaluate a 6-bit chunk even if we only have partial
		// information about it (i.e. there are 16 bits of input data, so we can
		// only get a 4 bit chunk for the final character) because the bitwise shift
		// will automatically pad zeros for evaluation
		do {
			// Build the first 8-bit chunk
			int inputGroup = unsignedByteToSignedInt(input[i++]);

			// Append the alphabet mapping for the first 6-bit chunk
			builder.append(BASE64_CHAR_MAPPING[inputGroup & 0x3f]);

			// Build the second 8-bit chunk
			if (i < input.length) {
				inputGroup += unsignedByteToSignedInt(input[i]) << 8;
			}

			// Append the alphabet mapping for the second 6-bit chunk
			// chunk with zeros to do the mapping
			builder.append(BASE64_CHAR_MAPPING[(inputGroup >> 6) & 0x3f]);

			// If we didn't push the second 8-bit chunk, stop evaluating because we're
			// out of inputs
			if (i++ >= input.length) {
				break;
			}

			// Build the third 8-bit chunk of the inputGroup
			if (i < input.length) {
				inputGroup += unsignedByteToSignedInt(input[i]) << 16;
			}

			// Append the alphabet mapping for the third 6-bit chunk
			builder.append(BASE64_CHAR_MAPPING[(inputGroup >> 12) & 0x3f]);

			// If we didn't push the third 8-bit chunk, stop evaluating because we're
			// out of inputs
			if (i++ >= input.length) {
				break;
			}

			// Append the alphabet mapping for the fourth 6-bit chunk
			builder.append(BASE64_CHAR_MAPPING[(inputGroup >> 18) & 0x3f]);
		} while (i < input.length);

		return builder.toString();
	}

	/**
	 * Because Java stores bytes as signed a signed value (-128, 127), we need to
	 * do a conversion to get the (0, 255) unsigned byte value that the algorithm
	 * expects
	 */
	private int unsignedByteToSignedInt(int value) {
		return value & 0xFF;
	}
}
