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

// ******************************************************************************
// This is a Java version of an original PHP routine provided by:
// Copyright 2003-2009 by A J Marston <http://www.tonymarston.net>
// ******************************************************************************

package com.aurel.track.util;

import java.util.ArrayList;

public class SimpleEncryption {

	private String scramble1;     // 1st string of ASCII characters
	private String scramble2;     // 2nd string of ASCII characters

	private ArrayList<String> errors;        // array of error messages
	private double   adj;           // 1st adjustment value (optional)
	private int mod;           // 2nd adjustment value (optional)
	private ArrayList<Double> fudgefactor;

	// ****************************************************************************
	// class constructor
	// ****************************************************************************
	public SimpleEncryption ()
	{
		errors = new ArrayList<String>();

		// Each of these two strings must contain the same characters, but in a different order.
		// Use only printable characters from the ASCII table.
		// Do not use single quote, double quote or backslash as these have special meanings in PHP.
		// Each character can only appear once in each string.
		scramble1 = "! #$%&()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[]^_`abcdefghijklmnopqrstuvwxyz{|}~\r\n";
		scramble2 = "f^jAE]okIOzU[2&q1{3`h5w_794p@6s8?BgP>dFV=m D<TcS%Ze|r:lGK/uCy.Jx)HiQ!#$~(;Lt-R}Ma,NvW+Ynb*0X\r\n";

		if (scramble1.length() != scramble2.length()) {
			System.err.println("** SCRAMBLE1 is not same length as SCRAMBLE2 **");
		} // if

		adj = 1.75;  // this value is added to the rolling fudgefactors
		mod = 3;     // if divisible by this the adjustment is made negative

	} // constructor

	// ****************************************************************************
	public String encrypt (String key, String source) // , $sourcelen = 0)
	// encrypt string into a garbled form
	{
		errors = new ArrayList<String>();

		// convert key into a sequence of numbers
		fudgefactor = convertKey(key);
		if (errors.size() > 0) return "";

		if (source == null || source.length() == 0) {
			errors.add("No value has been supplied for encryption");
			return "";
		} // if

		// pad $source with spaces up to $sourcelen

		StringBuffer target = new StringBuffer();
		double factor2 = 0;

		char theChar;

		for (int i = 0; i < source.length(); ++i) {
			theChar = source.charAt(i);

			// identify its position in $scramble1
			int num1 = scramble1.indexOf(theChar);
			if (num1 < 0) {
				errors.add("Source string contains an invalid character (" + theChar + ")");
				return "Source string contains an invalid character (" + theChar + ")";
			} // if

			// get an adjustment value using $fudgefactor
			double adjIntern = applyFudgeFactor();

			double factor1 = factor2 + adjIntern;                       // accumulate in $factor1
			int num2    = (int) Math.round(factor1) + num1;     // generate offset for $scramble
			
			num2    = this.checkRange(num2);        // check range
			factor2 = factor1 + num2;               // accumulate in $factor2

			char char2 = scramble2.charAt(num2);

			// append to $target string
			target.append(char2);


		} // for

		return target.toString();

	} // encrypt

	
    // ****************************************************************************
    public String decrypt (String key, String source)
    // decrypt string into its original form
    {
        errors = new ArrayList<String>();

        // convert $key into a sequence of numbers
        fudgefactor = convertKey(key);
        if (errors.size() > 0) return "Problem with converting key";

        if (source == null || source.length() == 0) {
            errors.add("No value has been supplied for decryption");
            return "No value has been supplied for decryption";
        } // if

        StringBuffer target = new StringBuffer();
        double factor2 = 0;
        char theChar1;
        char theChar2;

        for (int i = 0; i < source.length(); i++) {
            // extract a (multibyte) character from $source

            	theChar2 = source.charAt(i);


            // identify its position in $scramble2
            int num2 = scramble2.indexOf(theChar2);
            if (num2 < 0) {
                errors.add("Source string contains an invalid character ($char2)");
                return "Source string contains an invalid character (" + theChar2 + ")";
            } // if

            // get an adjustment value using $fudgefactor
            double adjIntern     = applyFudgeFactor();

            double factor1 = factor2 + adjIntern;                 // accumulate in $factor1
            int num1    = num2 - (int)Math.round(factor1);         // generate offset for $scramble1
            num1    = checkRange(num1);       // check range
            factor2 = factor1 + num2;                // accumulate in $factor2

            // extract (multibyte) character from $scramble1
        	theChar1 = scramble1.charAt(num1);

            // append to $target string
            target.append(theChar1);


        } // for

        return target.toString().trim();

    } // decrypt

	// ****************************************************************************
	// private methods
	// ****************************************************************************
	private double applyFudgeFactor ()
	// return an adjustment value based on the contents of $fudgefactor
	// NOTE: $fudgefactor is passed by reference so that it can be modified
	{
		double fudge = fudgefactor.get(0);
		fudgefactor.remove(0);// extract 1st number from array
		fudge = fudge + adj;           // add in adjustment value
		fudgefactor.add(fudge);         // put it back at end of array

		if ((int)fudge % mod == 0) {        // if it is divisible by modifier
			fudge = fudge * -1;        // make it negative
		} // if
		return fudge;

	} // _applyFudgeFactor

	// ****************************************************************************
	private int checkRange (double num)
	// check that $num points to an entry in this.scramble1
	{
		num = Math.round(num);   // round up to nearest whole number

		int limit = scramble1.length();

		while (num >= limit) {
			num = num - limit;   // value too high, so reduce it
		} // while
		while (num < 0) {
			num = num + limit;   // value too low, so increase it
		} // while

		return (int) Math.round(num);

	} // _checkRange

	// ****************************************************************************
	private ArrayList<Double> convertKey (String key)
	// convert $key into an array of numbers
	{
		ArrayList<Double>array = new ArrayList<Double>(key.length()+2);
		if (key == null || key.length() == 0) {
			errors.add("No value has been supplied for the encryption key");
			return array;
		} // if


		array.add(new Double(key.length()));    // first entry in array is length of $key
		char theChar = ' ';
		int tot = 0;
		for (int i = 0; i < key.length(); i++) {
			// extract a (multibyte) character from $key
			theChar = key.charAt(i);

			// identify its position in $scramble1
			int num = scramble1.indexOf(theChar);
			if (num < 0) {
				System.err.println("Key contains an invalid character (" + theChar + ")");
				return array;
			} // if

			array.add(new Double(num));         // store in output array
			tot = tot + num;          // accumulate total for later
		} // for

		array.add(new Double(tot));  // insert total as last entry in array
		return array;

	} // _convertKey

}
