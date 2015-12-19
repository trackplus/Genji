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


package com.aurel.track.util;

import java.io.StringWriter;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aurel.track.Constants;
import com.aurel.track.admin.server.siteConfig.SiteConfigBL;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.prop.ApplicationBean;


/**
 * This class contains various support methods.
 * @author Joerg Friedrich <joerg.friedrich@computer.org>
 * @version $Revision: 1663 $ $Date: 2015-10-26 00:49:08 +0100 (Mo, 26 Okt 2015) $
 */
public class Support {

	private static final Logger LOGGER = LogManager.getLogger(Support.class);
	private String delimiters = ",; ";
	private final static int RADIX = 16;

	/**
     *
	 * Retrieves a string array of words from a string. The words are
	 * are separated by delimiters. This does not make use of the
	 * <code>BreakIterator</code> class since in this class the
	 * delimiters can not be set explicitly (as far as I know).
     * @param parseString the sentence to be parsed
     * @return a list with all words found
	 */
	public String[] getWords(String parseString) {
		if (parseString == null || parseString.length() < 1) {
			return null;
		}
		ArrayList<String> wordList = new ArrayList<String>(8);
		String ds = parseString;
		String word = null;
		int st = 0;
		int end = 0;
		while (ds.length() > 0) {
			for (st = 0; st < ds.length(); ++st) {
				if (!isDelimiter(ds.charAt(st))) {
					break;
				}
			}
			end = st;
			for (end = st; end < ds.length(); ++end) {
				if (isDelimiter(ds.charAt(end))) {
					break;
				}
			}
			if (end > st) {
				word = ds.substring(st,end);
				wordList.add(word);
			}

			if (end+1 <= ds.length()) {
				ds = ds.substring(end+1, ds.length());
			}
			else {
				break;
			}
		}
		String[] wordArray = new String[wordList.size()];
		for (int i = 0; i < wordList.size(); ++i) {
			wordArray[i] = (String) wordList.get(i);
		}
		return wordArray;
	}

	/**
	 * Sets the delimiters that are being used by <code>getWords()</code>.
	 * The delimiters are concatenated in a string. The default is ";, ".
     * @param delimiters the delimiters
	 */
	public void setDelimiters(String delimiters) {
		this.delimiters = delimiters;
	}

	protected boolean isDelimiter(char search) {
		boolean result = false;
		String ws = this.delimiters;
		int wsLength = ws.length();
		for (int j = 0; j < wsLength; ++j) {
			if (ws.charAt(j) == search) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * Quotes a string with single quotes. This is useful for quoting strings
	 * in SQL queries. Null values are printed as <code>null</code> which
	 * should work with SQL as well. Single quotes in the input string are
	 * also quoted such that they are preserved across an SQL query.
     * @param value the value string to be quoted
	 * @return the quoted string.
	 */
    public String quoteString(String value) {
		if (value == null) {
			return "null";
		}
		if (value.length() == 0) {
			return "''";
		}
		if ("null".equals(value.toLowerCase())) {
			return "null";
		}
        StringBuilder work = new StringBuilder();
		work.append(value);
        for (int i = value.length()-1; i >=0; i--) {
            if (value.charAt(i) == "'".charAt(0)){
            work.insert(i,"'".charAt(0));
            }
        }
        return "'" + work + "'";
    }

    /**
     * Print the string as the next value on the line.	The value
     * will be quoted if needed.  If value is null, an empty value is printed.
     *
     * @param matcherValue value to be output.
     *
     */
    private boolean alwaysQuote = true;
    private boolean newLine = true;
    private char quoteChar = '"';
    private char delimiterChar = ';';

    public String csvWrite(String value) {


        StringBuilder csvString = new StringBuilder();
        if (value == null) value = "";
        boolean quote = false;
        if (alwaysQuote){
            quote = true;
        } else if (value.length() > 0){
            for (int i=0; i<value.length(); i++){
                char c = value.charAt(i);
                if (c==quoteChar || c==delimiterChar || c=='\n' || c=='\r'){
                    quote = true;
                }
            }
        } else if (newLine) {
            // always quote an empty token that is the first
            // on the line, as it may be the only thing on the
            // line.
            quote = true;
        }
        if (newLine){
            newLine = false;
        } else {
            csvString.append(delimiterChar);
        }
        if (quote){
            csvString.append(escapeAndQuote(value));
        } else {
            csvString.append(value);
        }
        return csvString.toString();
    }

    public void setCsvDelimiterChar(Character theDelimiter) {
        this.delimiterChar = theDelimiter.charValue();
    }

    /**
     * Enclose the value in quotes and escape the quote
     * and comma characters that are inside.
     *
     * @param value needs to be escaped and quoted.
     *
     * @return the value, escaped and quoted.
     * @since Track 3.0
     */
    private String escapeAndQuote(String value){
        String s = this.replace(value, String.valueOf(quoteChar),
                                       String.valueOf(quoteChar)
                                       + String.valueOf(quoteChar));
        return (new StringBuilder(2 + s.length())).append(quoteChar).append(s).append(quoteChar).toString();
    }

    /**
     * Replace occurrences of a substring.
     *
     * @param s String to be modified.
     * @param find String to find.
     * @param replace String to replace.
     * @return a string with all the occurrences of the string to find replaced.
     * @throws NullPointerException if s is null.
     *
     */
    private String replace(String s, String find, String replace){
        int findLength;
        // the next statement has the side effect of throwing a null pointer
        // exception if s is null.
        int stringLength = s.length();
        if (find == null || (findLength = find.length()) == 0){
            // If there is nothing to find, we won't try and find it.
            return s;
        }
        if (replace == null){
            // a null string and an empty string are the same
            // for replacement purposes.
            replace = "";
        }
        int replaceLength = replace.length();

        // We need to figure out how long our resulting string will be.
        // This is required because without it, the possible resizing
        // and copying of memory structures could lead to an unacceptable runtime.
        // In the worst case it would have to be resized n times with each
        // resize having a O(n) copy leading to an O(n^2) algorithm.
        // This Algorithm has been borrowed from Steve Ostermillers
        // CVSExport class.

        int length;
        if (findLength == replaceLength){
            // special case in which we don't need to count the replacements
            // because the count falls out of the length formula.
            length = stringLength;
        } else {
            int count;
            int start;
            int end;

            // Scan s and count the number of times we find our target.
            count = 0;
            start = 0;
            while((end = s.indexOf(find, start)) != -1) {
                count++;
                start = end + findLength;
            }
            if (count == 0){
                return s;
            }
            length = stringLength - (count * (findLength - replaceLength));
        }

        int start = 0;
        int end = s.indexOf(find, start);
        if (end == -1){
            return s;
        }

        StringBuilder sb = new StringBuilder(length);

        // Scan s and do the replacements
        while (end != -1) {
            sb.append(s.substring(start, end));
            sb.append(replace);
            start = end + findLength;
            end = s.indexOf(find, start);
        }
        end = stringLength;
        sb.append(s.substring(start, end));

        return sb.toString();
    }


	/**
	 * Encrypts an input password into a new string using the SHA
	 * algorithm. The encrypted password is returned as string
	 * in hexadecimal notation with hyphens in between (e.g.
	 * '-45-58-8050-1e5a36-1954-27-2459-6dd104c-68-24521d')
     * @param password the password to be encrypted
	 * @return the encrypted password.
	 */
    public String encryptPassword(String password) {
        // encrypts a password using the SHA algorithm
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA");
            sha.update(password.getBytes("ISO-8859-1"));
            byte[] hash = sha.digest();
            StringBuilder newPassword = new StringBuilder();
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
            // just ignore
        }
		return password;
    }

	/**
	 * Returns a string with the current date and time in ISO format
	 * <code>yyyy-MM-dd H:mm:ss.S</code>. Can usually directly be used
	 * to write a Timestamp in an SQL query.
	 * @return the current date and time in ISO format.
	 */
    public String dateTimeWriter() {
        Locale currentLocale = new Locale("de", "DE");
        java.util.Date today = new java.util.Date();
		SimpleDateFormat dbFormatter = new SimpleDateFormat(
							"yyyy-MM-dd H:mm:ss.S",
										   currentLocale);
        return dbFormatter.format(today);
    }

    public void setURIs(HttpServletRequest request) {
    	String serverURL = ApplicationBean.getInstance().getSiteBean().getServerURL();

		String baseURI = request.getRequestURI();
		int extension = baseURI.lastIndexOf("/");
		// This is automatically overwritten by StartServlet
		// if there is an entry "serverURL" in web.xml
		if (serverURL == null || serverURL.length() < 1) {
			ApplicationBean.getInstance().getSiteBean().setServerURL(
									request.getScheme() + "://"
								   + request.getServerName() + ":"
								   + request.getServerPort());
		}
		if (extension != -1) {
			Constants.BaseURL = serverURL
                           + baseURI.substring(0,extension);
			Constants.setHyperlink(Constants.BaseURL);
		}

		// So that JSP can access it...
		HttpSession session = request.getSession();
		session.setAttribute("BASEURL",Constants.BaseURL);

		//persist the last serverURL
		persistLastServerURL(serverURL,Constants.BaseURL);
	}

    public static String getLastBaseURL(){
    	TSiteBean site = SiteConfigBL.loadTSite();
    	if(site!=null){
    		String serverURL=site.getPreferenceProperty("lastServerURL");
    		String baseURL=site.getPreferenceProperty("lastBaseURL");
    		ApplicationBean.getInstance().getSiteBean().setServerURL(serverURL);
    		Constants.BaseURL=baseURL;
    		Constants.setHyperlink(baseURL);
    		return baseURL;
    	}
    	return null;
    }

    public static void loadLastURIs() {
		LOGGER.info("Last base URL: " + getLastBaseURL());
    	getLastBaseURL();
    }


    private void persistLastServerURL(String serverURL,String baseURL){
    	Map<Integer, Object> siteBeanValues = new HashMap<Integer, Object>();
	    siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.LASTSERVERURL, serverURL);
	    siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.LASTBASEURL, baseURL);
	    DAOFactory.getFactory().getSiteDAO().loadAndSaveSynchronized(siteBeanValues);
    }

}
