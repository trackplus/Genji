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

package com.aurel.track.util.emailHandling;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Utility methods typically called from groovy scripts 
 * @author Tamas
 *
 */
public class EmailCleanUtils {
	private static final Logger LOGGER = LogManager.getLogger(EmailCleanUtils.class);
	
	

	/**
	 * Compile the pattern strings to patterns
	 * @param patternStrings
	 * @return
	 */
	public static List<Pattern> compilePatterns(List<String> patternStrings, int flags) {
		List<Pattern> patterns = new LinkedList<Pattern>();
		for (String patternString : patternStrings) {
		    try {
				Pattern pattern = Pattern.compile(patternString, flags);
				if (pattern!=null) {
					patterns.add(pattern); 
				}
		    } catch (Exception e) {
		    	LOGGER.warn("Compiling the regex string " + patternString + " failed with " + e.getMessage());
		    }
		}
		return patterns;
	}
	
	/**
	 * Compile the pattern strings to patterns
	 * @param patternStrings
	 * @return
	 */
	public static List<Pattern> compilePatterns(List<String> patternStrings) {
		List<Pattern> patterns = new LinkedList<Pattern>();
		for (String patternString : patternStrings) {
		    try {
				Pattern pattern = Pattern.compile(patternString);
				if (pattern!=null) {
					patterns.add(pattern); 
				}
		    } catch (Exception e) {
		    	LOGGER.warn("Compiling the regex string " + patternString + " failed with " + e.getMessage());
		    }
		}
		return patterns;
	}
	
	/**
	 * Whether a subject matches any pattern from the list 
	 * @param subject
	 * @param patterns
	 * @return
	 */
	public static boolean patternFound(String subject, List<Pattern> patterns) {
		if (patterns!=null) {			
			for (Pattern pattern : patterns) {
				try {
					Matcher matcher = pattern.matcher(subject);
					if (matcher.find()) {
						LOGGER.debug("Subject " + subject + " matches reject string " + pattern.toString());
						return true;
					}
				} catch (Exception e) {
					LOGGER.info("Matching the pattern " + pattern.toString() + " on " + subject + " failed with " + e.getMessage(), e);
				}				
			}
		}
		return false;
	}
	
	/**
	 * Remove the attachments matching any pattern
	 * @param attachments
	 * @param rejectAttachmentPatterns
	 */
	public static void removeMatchingAttachments(List<File> attachments, List<Pattern> rejectAttachmentPatterns) {
		if (rejectAttachmentPatterns!=null && attachments!=null) {			
			for (Pattern pattern : rejectAttachmentPatterns) {
				for (Iterator<File> iterator = attachments.iterator(); iterator.hasNext();) {
					File file = iterator.next();
					String name = file.getName();
					try {					
						Matcher matcher = pattern.matcher(name);
						if (matcher.find()) {
							LOGGER.debug("Attached file " + name + " matches reject string " + pattern.toString());
							file.delete();
							iterator.remove();
						}
					} catch (Exception e) {
						LOGGER.info("Matching the pattern " + pattern.toString() + " on file " + name + " failed with " + e.getMessage(), e);
					}
				}							
			}
		}
	}
	
	/**
	 * Remove the shortest possible pattern match
	 * @param contentBuffer
	 * @param cutPatterns
	 * @return
	 */
	public static boolean removeShortest(StringBuilder contentBuffer, List<Pattern> cutPatterns) {			
		int minStart = 0;
		int minEnd = 0;
		int minLength = contentBuffer.length();
		boolean foundAny = false;
		for (Pattern pattern : cutPatterns) {
			try {
				Matcher matcher = pattern.matcher(contentBuffer);
				boolean found = matcher.find();
				if (found) {
					foundAny = true;
					int start = matcher.start();
					int end = matcher.end();
					int length = end-start;
					if (length<=minLength) {
						minLength = length;
						minStart = start;
						minEnd = end;
					}
				}
			} catch (Exception e) {
			}
		}
		if (foundAny) {
			LOGGER.debug("Matching part to remove: " + contentBuffer.substring(minStart, minEnd));
			System.out.println(contentBuffer.substring(minStart, minEnd));
			contentBuffer.replace(minStart, minEnd, "");			
		}
		return foundAny;
	}
}
