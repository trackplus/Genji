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

package com.aurel.track.macro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Business logic for macros
 */
public class MacroBL {

	private static final Logger LOGGER = LogManager.getLogger(MacroBL.class);

	public static String START = "[";
	public static String END = "]";
	public static String END2 = "/]";
	public static String START2 = "[/";

	public static List<MacroDef> parseMacros(String s) {
		return parseMacros(s, null);
	}

	public static List<MacroDef> parseMacros(String s, String macroName) {
		List<MacroDef> result = new ArrayList<MacroDef>();
		int index = 0;
		int endIndex = -1;
		String tagName;
		int tagNameIndex = -1;
		String params;
		MacroManager macroManager = MacroManager.getInstance();
		IMacro macro;
		while ((index = s.indexOf(START, index)) != -1) {
			tagNameIndex = s.indexOf(" ", index);
			endIndex = s.indexOf(END2, index);
			if (endIndex != -1 && endIndex > index) {
				if (tagNameIndex == -1 || tagNameIndex >= endIndex) {
					tagNameIndex = endIndex;
					params = null;
				} else {
					params = s.substring(tagNameIndex, endIndex).trim();
				}
				tagName = s.substring(index + 1, tagNameIndex);
				if (macroName != null) {
					if (!tagName.equals(macroName)) {
						continue;
					}
				} else {
					macro = macroManager.getMacro(tagName);
					if (macro == null) {
						// macro not know by the system
						index++;
						continue;
					}
				}
				endIndex = endIndex + END2.length();
				MacroDef macroDef = new MacroDef();
				macroDef.setStartIndex(index);
				macroDef.setEndIndex(endIndex);
				macroDef.setTagName(tagName);
				if (params != null && params.length() > 0) {
					macroDef.setSimpleParameters(params.split(" "));
				}
				result.add(macroDef);
				index = endIndex;
			} else {
				if (tagNameIndex > index) {
					index = tagNameIndex + 1;
				} else {
					index++;
				}
			}
		}
		return result;
	}

	public static String replaceMacros(List<MacroDef> macros, String s, MacroContext macroContext) {
		return replaceMacros(macros, s, macroContext, null);
	}

	public static String replaceMacros(List<MacroDef> macros, String s, MacroContext macroContext, IMacro macro) {
		StringBuilder sb = new StringBuilder(s);
		int dif = 0;
		String tagName;
		MacroManager macroManager = MacroManager.getInstance();
		String tmpl;
		IMacro curretnMacro;
		for (MacroDef macroDef : macros) {
			dif = sb.length() - s.length();
			tagName = macroDef.getTagName();
			if (macro != null) {
				curretnMacro = macro;
			} else {
				curretnMacro = macroManager.getMacro(tagName);
			}
			if (curretnMacro != null) {
				tmpl = curretnMacro.format(macroDef, macroContext);
				sb.replace(macroDef.getStartIndex() + dif, macroDef.getEndIndex() + dif, tmpl);
			} else {
				tmpl = "";
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		String s1 = "ssss";
		String s2 = "The system will point out to you any conflicts between the top-down values and the bottom-up values, but it will not prevent them. Only if you have permissions to read both, top-down and bottom-up values, will you get information on such conflicts"
				+ "aaaa [issue 12 /] aaa [dummy/] ss [issue 7/]  dasda [eee/] Taking Genji for a test drive\n"
				+ "You have Genji installed and you know how to get to the login page. You have either gotten a login name from your Genji administrator, or you yourself have the administrator password and can log in under the \"admin\" user name. Now you can take Genji for a short test drive."
				+ "[issue 222/] addasa" + "Installing Genji\n"
				+ "Unless a server is provided to you, you need to install Genji on your own personal computer or on another computer like a server. Use the Windows installer for an easy, hassle free installation.";
		Date d1 = new Date();
		List<MacroDef> macros1 = parseMacros(s1);
		List<MacroDef> macros2 = parseMacros(s2);
		printMacros(macros2);
		MacroContext macroContext = new MacroContext();
		Date d2 = new Date();
		LOGGER.debug("d2-d1=" + (d2.getTime() - d1.getTime()));

		macros1 = parseMacros(s1);
		macros2 = parseMacros(s2);
		macroContext = new MacroContext();
		LOGGER.debug("s2=" + s2);
	}

	public static void printMacros(List<MacroDef> macros) {
		if (macros == null || macros.isEmpty()) {
			System.out.println("No macros");
		} else {
			for (int i = 0; i < macros.size(); i++) {
				MacroDef macroDef = macros.get(i);
				System.out.println(i + ": '" + macroDef.getTagName() + "' from:" + macroDef.getStartIndex() + " to:" + macroDef.getEndIndex());
				if (macroDef.getSimpleParameters() != null) {
					String[] params = macroDef.getSimpleParameters();
					System.out.print("   params:[");
					for (int j = 0; j < params.length; j++) {
						System.out.print(params[j] + " ");
					}
					System.out.println("]");
				}
			}
		}
	}
}
