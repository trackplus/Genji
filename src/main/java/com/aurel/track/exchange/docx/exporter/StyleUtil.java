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

package com.aurel.track.exchange.docx.exporter;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.WordprocessingML.StyleDefinitionsPart;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase.OutlineLvl;
import org.docx4j.wml.Style;
import org.docx4j.wml.Styles;

public class StyleUtil {
	private static final Logger LOGGER = LogManager.getLogger(StyleUtil.class);
	
	public static interface STANDARD_STYLE_NAMES {
		
		public static String HEADING_NAME = "heading";
		//for image caption
		public static String CAPTION_NAME = "caption";
		//for inline content
		
		public static String SUBTLE_EMPHASIS = "Subtle Emphasis";
		
	}
	
	public static interface STANDARD_STYLE_KEYS {
		public static Integer NORMAL_KEY = Integer.valueOf(-1);
		public static Integer CAPTION_KEY = Integer.valueOf(-2);
		public static Integer QUOTE_KEY = Integer.valueOf(-3);
		public static Integer SUBTLE_EMPHASIS_KEY = Integer.valueOf(-4);
	}
	
	private static interface STYLE_TYPE {
		static String PARAGRAPH = "paragraph";
		static String CHARACTER = "character";
	}
	
	/**
	 * Gets the styleIDs by outline levels:
	 * Heading 1 is lvl 0
       There are 9 levels, so Heading 9 will be lvl 8
	 * @param mainDocumentPart
	 * @return
	 */
	static Map<Integer, String> getInterestingStyleIDs(MainDocumentPart mainDocumentPart) {
		Map<Integer, String> outlinelevelToStyleName = new HashMap<Integer, String>();
		StyleDefinitionsPart styleDefinitionsPart =
				mainDocumentPart.getStyleDefinitionsPart();
        Styles styles = null;
		try {
			styles = styleDefinitionsPart.getContents();
		} catch (Docx4JException e) {
			LOGGER.error("Getting the styles contents failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
        if (styles!=null) {
        	for (Style style : styles.getStyle()) {
				String sytleID = style.getStyleId();
				Style.Name name = style.getName();
				String styleName = name.getVal();
				if (styleName!=null) {
									PPr pPr = style.getPPr();
									if (pPr!=null) {
										 OutlineLvl outlineLvl = pPr.getOutlineLvl();
										 if (outlineLvl!=null && styleName.startsWith(STANDARD_STYLE_NAMES.HEADING_NAME)) {
											 //sometimes other styles like Title have OutlineLvl
											 BigInteger level = outlineLvl.getVal();
											 outlinelevelToStyleName.put(Integer.valueOf(level.intValue()), sytleID);
											 LOGGER.debug("StyleID for level " + level + " is: " + sytleID);
										 }
									}
				}
			}
        }
        return outlinelevelToStyleName;
	}

	/**
	 * Gets styles
	 * @param mainDocumentPart
	 * @param paragaphStylesSet
	 * @param characterStylesSet
	 * @param outlinelevelToStyleName
	 */
	public static void getStyles(MainDocumentPart mainDocumentPart, Map<String, String> paragaphStylesSet, Map<String, String> characterStylesSet, Map<Integer, String> outlinelevelToStyleName) {
		StyleDefinitionsPart styleDefinitionsPart =
				mainDocumentPart.getStyleDefinitionsPart();
        Styles styles = null;
		try {
			styles = styleDefinitionsPart.getContents();
		} catch (Docx4JException e) {
			LOGGER.error("Getting the styles contents failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
        if (styles!=null) {
        	for (Style style : styles.getStyle()) {
        		String styleID = style.getStyleId();
				String type = style.getType();
				Style.Name name = style.getName();
				String styleName = name.getVal();
				if (styleName!=null) {
					if (STYLE_TYPE.CHARACTER.equals(type)) {
						characterStylesSet.put(styleName, styleID);
						LOGGER.debug("Character styleID " + styleID + " with name " + styleName);
					} else {
						if (STYLE_TYPE.PARAGRAPH.endsWith(type)) {
							paragaphStylesSet.put(styleName, styleID);
							LOGGER.debug("Paragaph styleID " + styleID + " with name " + styleName);
							PPr pPr = style.getPPr();
							if (pPr!=null) {
								 OutlineLvl outlineLvl = pPr.getOutlineLvl();
								 if (outlineLvl!=null && styleName.startsWith(STANDARD_STYLE_NAMES.HEADING_NAME)) {
									 //sometimes other styles like Title have OutlineLvl
									 BigInteger level = outlineLvl.getVal();
									 outlinelevelToStyleName.put(Integer.valueOf(level.intValue()), styleID);
									 LOGGER.debug("Heading StyleID for level " + level + " is: " + styleID);
								 }
							}
						}
					}
					
					
				}
			}	
        }
       
	}
	
	/**
	 * Gets the configured or a default style which is present in the styles map
	 * @param stylesMap
	 * @param configuredStyle
	 * @param styleKey
	 * @param defaultStyle
	 * @return
	 */
	public static String getStyle(Map<String, String> stylesMap, String configuredStyle, String styleKey, String defaultStyleName) {
		if (configuredStyle==null || "".equals(configuredStyle)) {
			LOGGER.info("No " + styleKey + " specified in GeneralSettings.properties");
		} else {
			if (!stylesMap.containsValue(configuredStyle)) {
				LOGGER.info("The " + styleKey + " specified in GeneralSettings.properties is not found as paragraph style in the template");
				if (stylesMap.containsKey(configuredStyle)) {
					//styleID could be language specific while style name not. So if the same Genji instance is used in more locales there could be different 
					//docx templates in different languages, in this case probably is better to specify the style name instead of the style ID
					//if the style name is reused in all templates for the same formatting of the parts
					configuredStyle = stylesMap.get(configuredStyle);
					LOGGER.info("The " + styleKey + " specified in GeneralSettings.properties is found as paragraph name in the template. Return the paragraph style " + configuredStyle);
					return configuredStyle;
				}
			} else {
				return configuredStyle;
			}
		}
		if (stylesMap.containsKey(defaultStyleName)) {
			//as default we always get the style by name (locale independent) and not by styleID
			configuredStyle = stylesMap.get(defaultStyleName);
			LOGGER.info("Fall back to " + configuredStyle);	
			return configuredStyle;
		}
		return null;
	}
	
	
	 
    
    /**
     *  First we create the package, then we alter the style sheet and add some
     *  styled paragraphs. Finally we save the package.
     */
 
    /**
     *  This method alters the default style sheet that is part of each document.
     *
     *  To do this, we first retrieve the style sheet from the package and then
     *  get the Styles object from it. From this object, we get the list of actual
     *  styles and iterate over them.
     *  We check against all styles we want to alter and apply the alterations if
     *  applicable.
     *
     *  @param wordMLPackage
     */
 
    /**
     *  First we create a run properties object as we want to remove nearly all of
     *  the existing styling. Then we change the font and font size and set the
     *  run properties on the given style. As in previous examples, the font size
     *  is defined to be in half-point size.
     */
 
    /**
     *  For this style, we get the existing run properties from the style and
     *  remove the theme font information from them. Then we also remove the bold
     *  styling, change the font size (half-points) and add an underline.
     */
 
 
    /**
     *  Change the font of the given run properties to Arial.
     *
     *  A run font specifies the fonts which shall be used to display the contents
     *  of the run. Of the four possible types of content, we change the styling of
     *  two of them: ASCII and High ANSI.
     *  Finally we add the run font to the run properties.
     *
     *  @param runProperties
     */
 
    /**
     * Change the font size of the given run properties to the given value.
     *
     * @param runProperties
     * @param fontSize  Twice the size needed, as it is specified as half-point value
     */
 
    /**
     * Removes the theme font information from the run properties.
     * If this is not removed then the styles based on the normal style won't
     * inherit the Arial font from the normal style.
     *
     * @param runProperties
     */
 
    /**
     * Removes the Bold styling from the run properties.
     *
     * @param runProperties
     */
 
    /**
     * Adds a single underline to the run properties.
     *
     * @param runProperties
     */
}
