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

package com.aurel.track.admin.customize.lists;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class CssStyleBean {
	private static final Logger LOGGER = LogManager.getLogger(CssStyleBean.class);
	private static String VALUE_SEPARATOR = "__";
	private static String STYLE_SEPARTOR = "|";
	
	static String BGR_COLOR = "bgrColor";
	static String COLOR = "color";
	static String FONT_WEIGHT = "fontWeight";
	static String FONT_STYLE = "fontStyle";
	static String TEXT_DECORATION = "textDecoration";
	
	private String bgrColor;
	private String color;
	private String textDecoration;
	private String fontWeight;
	private String fontStyle;
	
	public String getBgrColor() {
		return bgrColor;
	}

	public void setBgrColor(String bgrColor) {
		this.bgrColor = bgrColor;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getTextDecoration() {
		return textDecoration;
	}

	public void setTextDecoration(String textDecoration) {
		this.textDecoration = textDecoration;
	}

	public String getFontWeight() {
		return fontWeight;
	}

	public void setFontWeight(String fontWeight) {
		this.fontWeight = fontWeight;
	}

	public String getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
	}

	public String getCssStyle() {
		StringBuilder stringBuilder = new StringBuilder();
		if (this.bgrColor!=null && this.bgrColor.trim().length()>0) {
			stringBuilder.append("background-color:#");
			stringBuilder.append(this.bgrColor);
			stringBuilder.append(";");
		}
		if (this.color!=null && this.color.trim().length()>0) {
			stringBuilder.append("color:#");
			stringBuilder.append(this.color);
			stringBuilder.append(";");
		}
		if (this.fontWeight!=null && this.fontWeight.trim().length()>0) {
			stringBuilder.append("font-weight:");
			stringBuilder.append(this.fontWeight);
			stringBuilder.append(";");
		}
		if (this.fontStyle!=null && this.fontStyle.trim().length()>0) {
			stringBuilder.append("font-style:");
			stringBuilder.append(this.fontStyle);
			stringBuilder.append(";");
		}
		if (this.textDecoration!=null && this.textDecoration.trim().length()>0) {
			stringBuilder.append("text-decoration:");
			stringBuilder.append(this.textDecoration);
			stringBuilder.append(";");
		}
		return stringBuilder.toString();
	}
	
	/**
	 * Prepares a string form the list of GroupField to be saved in the user's profile
	 * @return
	 */
	public String encodeCssStyle() {
		StringBuilder stringBuilder = new StringBuilder();
		addEncodedAttribute(stringBuilder, BGR_COLOR, bgrColor);
		addEncodedAttribute(stringBuilder, COLOR, color);
		addEncodedAttribute(stringBuilder, FONT_WEIGHT, fontWeight);
		addEncodedAttribute(stringBuilder, FONT_STYLE, fontStyle);
		addEncodedAttribute(stringBuilder, TEXT_DECORATION, textDecoration);
		return stringBuilder.toString();
	}
	
	
	private static void addEncodedAttribute(StringBuilder stringBuilder,
			String attributeName, String attributeValue) {
		if (attributeValue!=null && attributeValue.length()>0) {
			if (stringBuilder.length()!=0) {
				stringBuilder.append(STYLE_SEPARTOR);
			}
			stringBuilder.append(attributeName);
			stringBuilder.append(VALUE_SEPARATOR);
			stringBuilder.append(attributeValue);
		}
	}
	
	/**
	 * Parses a saved groupByFieldString and creates a list of GroupFiled elements
	 * @param cssStyleString
	 * @return
	 */
	public static CssStyleBean decodeCssStyle(String cssStyleString) {
		CssStyleBean cssStyleBean = new CssStyleBean();
		if (cssStyleString==null || "".equals(cssStyleString.trim())) {
			return cssStyleBean;
		}
		String[] styleAttributes = cssStyleString.split("\\" + STYLE_SEPARTOR);
		if (styleAttributes!=null && styleAttributes.length>0) {
			for (int i = 0; i < styleAttributes.length; i++) {
				String[] attributeValues = styleAttributes[i].split(VALUE_SEPARATOR);
				if (attributeValues.length!=2) {
					LOGGER.warn("Splitting the string " + styleAttributes[i] + " resulted in an array of length " + attributeValues.length);
					continue;
				}
				String attributeName = attributeValues[0];
				String attributeValue = attributeValues[1];
				if (BGR_COLOR.equals(attributeName)) {
					cssStyleBean.setBgrColor(attributeValue);
				} else {
					if (COLOR.equals(attributeName)) {
						cssStyleBean.setColor(attributeValue);
					} else {
						if (FONT_WEIGHT.equals(attributeName)) {
								cssStyleBean.setFontWeight(attributeValue);
						} else {
							if (FONT_STYLE.equals(attributeName)) {
								cssStyleBean.setFontStyle(attributeValue);
							} else {
								if (TEXT_DECORATION.equals(attributeName)) {
									cssStyleBean.setTextDecoration(attributeValue);
								}
							}
						}
					}
				}
			}
		}
		return cssStyleBean;
	}
}
