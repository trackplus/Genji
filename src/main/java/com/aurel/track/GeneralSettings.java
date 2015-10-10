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

package com.aurel.track;

import javax.servlet.ServletException;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.dbase.HandleHome;
import com.aurel.track.prop.ApplicationBean;

public class GeneralSettings {
	private static final Logger LOGGER = LogManager.getLogger(GeneralSettings.class);
	private static PropertiesConfiguration configMap = null;
	private static String ON = "on";
	
	public static interface GENERAL_CONFIG {
		public static String LIMIT_SMTP_CONNECTIONS = "limitSMTPconnections";
		public static String MAX_ITEMS = "maxItems";
		public static String HISTORY_AND_EMAIL_DELAY = "historyAndEmailDelay";
		public static String EXPORT_DOCX_STYLE_IMAGE_CAPTION = "export.docx.style.imageCaptionStyle";
		public static String EXPORT_DOCX_HIGHLIGHT_INLINE_CONTENT = "export.docx.style.highlightInlineContent";
		public static String EXPORT_DOCX_STYLE_INLINE_CONTENT = "export.docx.style.inlineContentStyle";
		public static String EXPORT_DOCX_REMOVE_HTML_HEADERS = "export.docx.removeHTMLHeaders";	
	}
	/**
	 * Loads the filter names from property file
	 * @return
	 */
	public static void loadGeneralConfigs() {
		try {
			configMap = HandleHome.getProperties(HandleHome.GENERAL_SETTINGS_FILE, ApplicationBean.getApplicationBean().getServletContext());
		} catch (ServletException e) {
			LOGGER.error("ServletException by getting the " + HandleHome.GENERAL_SETTINGS_FILE + " from war " + e.getMessage(), e);
		}
	}

	/**
	 * Whether to limit the SMTP connections 
	 * @return
	 */
	public static boolean getLimitSMTPConnections() {
		if (configMap!=null) {
			String limitSMTPconnections = configMap.getString(GENERAL_CONFIG.LIMIT_SMTP_CONNECTIONS);
			if (limitSMTPconnections!=null && ON.equals(limitSMTPconnections)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the maximal number of items loaded from the database for performance reasons
	 * @return
	 */
	public static int getMaxItems() {
		int maxItems = 10000;
		if (configMap!=null) {
			String maxItemsStr = configMap.getString(GENERAL_CONFIG.MAX_ITEMS);
			if (maxItemsStr!=null) {
				try {
					maxItems = Integer.valueOf(maxItemsStr);
				} catch (NumberFormatException e) {
					LOGGER.warn("Getting " + GENERAL_CONFIG.MAX_ITEMS + " as int from " +  maxItemsStr  + " failed with " + e.getMessage(), e);
				}
				
			}
		}
		return maxItems;
	}
	
	/**
	 * Gets the maximal number of items loaded from the database for performance reasons
	 * @return
	 */
	public static int getHistoryAndEmailDelay() {
		int historyAndEmailDelay = 5;
		if (configMap!=null) {
			String historyAndEmailDelayStr = configMap.getString(GENERAL_CONFIG.HISTORY_AND_EMAIL_DELAY);
			if (historyAndEmailDelayStr!=null) {
				try {
					historyAndEmailDelay = Integer.valueOf(historyAndEmailDelayStr);
				} catch (NumberFormatException e) {
					LOGGER.warn("Getting " + GENERAL_CONFIG.HISTORY_AND_EMAIL_DELAY + " as int from " +  historyAndEmailDelayStr  + " failed with " + e.getMessage(), e);
				}
				
			}
		}
		return historyAndEmailDelay;
	}
	
	/**
	 * The style for image caption in docx export
	 * @return
	 */
	public static String getExportDocxImageCaptionStyle() {
		if (configMap!=null) {
			return  configMap.getString(GENERAL_CONFIG.EXPORT_DOCX_STYLE_IMAGE_CAPTION);
		}
		return null;
	}
	
	/**
	 * The style for highlighting the inline content in docx export
	 * @return
	 */
	public static boolean isHighlightInlineContent() {
		if (configMap!=null) {
			String highlightInlineContent = configMap.getString(GENERAL_CONFIG.EXPORT_DOCX_HIGHLIGHT_INLINE_CONTENT);
			if (highlightInlineContent!=null) {
				if (ON.equals(highlightInlineContent)) {
					return true;
				} else {
					try {
						return Boolean.valueOf(highlightInlineContent);
					} catch(Exception e) {
						return false;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * The style for highlighting the inline content in docx export
	 * @return
	 */
	public static String getExportDocxInlineContentStyle() {
		if (configMap!=null) {
			return  configMap.getString(GENERAL_CONFIG.EXPORT_DOCX_STYLE_INLINE_CONTENT);
		}
		return null;
	}
	
	/**
	 * The style for highlighting the inline content in docx export
	 * @return
	 */
	public static boolean isRemoveHTMLHeaders() {
		if (configMap!=null) {
			String removeHTMLHeaders = configMap.getString(GENERAL_CONFIG.EXPORT_DOCX_REMOVE_HTML_HEADERS);
			if (removeHTMLHeaders!=null) {
				if (ON.equals(removeHTMLHeaders)) {
					return true;
				} else {
					try {
						return Boolean.valueOf(removeHTMLHeaders);
					} catch(Exception e) {
						return false;
					}
				}
			}
		}
		return false;
	}
}
