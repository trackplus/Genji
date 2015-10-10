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

package com.aurel.track.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.admin.customize.localize.LocalizeBL;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeJSON;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.resources.ResourceBundleManager;
import com.aurel.track.util.numberFormatter.DoubleWithDecimalDigitsNumberFormatUtil;

/**
 * Utility class for handling the available locales
 * @author Tamas Ruff
 *
 */
public class LocaleHandler {

	private static final Logger LOGGER = LogManager.getLogger(LocaleHandler.class); 
	//map with all locales supported by the JRE
	private static SortedMap<String, Locale> javaLocaleMap = new TreeMap<String,Locale>();
	//list with all locales supported by the JRE for importing new locales
	private static List<LabelValueBean> javaLanguages = new ArrayList<LabelValueBean>();
	private static HashMap <String,Locale> localeMap = new HashMap <String,Locale>();
	private static HashMap <String,Locale> helpMap = new HashMap <String,Locale>();
	private static HashMap <String,Locale> extjsMap = new HashMap <String,Locale>();
	private static List<LabelValueBean> availableLocales = new ArrayList<LabelValueBean>();
	private static List<LabelValueBean> possibleLocales = new ArrayList<LabelValueBean>();
	private static List<Locale> propertiesLocales = new ArrayList<Locale>();


	/**
	 * This routine scans for available locales on the Java system, then
	 * tries to match these with the available localization files for
	 * the Genji application, based on the ApplicationResources files.
	 * Only these locales are being offered to the user.
	 * <br>
	 * If there is no support within Java, but the locale is available 
	 * as a resource file, still add this locale.
	 * 
	 * @return an ArrayList with all available locales.
	 */
	public static void getLocales() {
		LOGGER.debug("Loading locales...");
		helpMap.clear();	
		localeMap.clear();
		javaLanguages.clear();
		possibleLocales.clear();
		availableLocales.clear();

		/**
		 * Get the locales available on Java 
		 */
		Locale[] javaLocalesArr = Locale.getAvailableLocales(); // all on this system
		LOGGER.debug("Number of locales available in JVM " + javaLocalesArr.length);
		javaLocaleMap = new TreeMap<String, Locale>();
		
		for (Locale locale : javaLocalesArr) {
			// language + "_" + country
			javaLocaleMap.put(locale.toString(), locale);	
		}
		
		/**
		 * Get the locales available in database
		 */
		Set<String> existingDatabaseLocales = LocalizeBL.getExistingLocales();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Number of locales available in DB localized resources " + existingDatabaseLocales.size() + ": " + existingDatabaseLocales);
		}
		
		// Always add Farsi
		javaLocaleMap.put("fa", new Locale("fa"));
		possibleLocales.add(new LabelValueBean("Browser",""));

		for (String javaLocaleString : javaLocaleMap.keySet()) {
			Locale javaLocale = javaLocaleMap.get(javaLocaleString);
			javaLanguages.add(new LabelValueBean(javaLocale.getDisplayName(javaLocale), javaLocaleString));
			
			// Match available locales in the JRE with resource
			// files provided for the Genji system. Only those locales
			// are being offered.

			//it is enough to have only the corresponding language files
			//in order to include also the country specific locales
			//(the country specific files do not have to be present)
			//because we need the formatting features linked with the countries

			for (String databaseLocaleString : existingDatabaseLocales) {
				if (javaLocaleString.startsWith(databaseLocaleString)) { // must be supported by Java system
					if (!localeMap.containsKey(javaLocaleString)) {
						possibleLocales.add(new LabelValueBean(javaLocale.getDisplayName(javaLocale), javaLocaleString));
						LOGGER.debug("Add possible user locale " + javaLocale);
						if (javaLocaleString.equals(databaseLocaleString)) {
							localeMap.put(javaLocaleString, javaLocale);
							availableLocales.add(new LabelValueBean(javaLocale.getDisplayName(javaLocale), javaLocaleString));
							LOGGER.debug("Add possible localisation language " + javaLocale);
						}
					}
					InputStream in = null;
					try {
						if (javaLocaleString.equals(databaseLocaleString)) {
							URL helpURL = ApplicationBean.getInstance().getServletContext()
									.getResource("/help/"+databaseLocaleString+"/WebHelp/index.html");
							if (helpURL != null) {
								in = helpURL.openStream();  // Will give an exception if not found
								BufferedReader inr = new BufferedReader(new InputStreamReader(in));
								String fl = inr.readLine();
								if (fl != null) helpMap.put(javaLocale.getLanguage(), javaLocale);
								LOGGER.info("Adding locale " + databaseLocaleString + " to help system");
								inr.close();
								in.close();
							}
						}
					} catch (Exception e) {
						// no luck here, but that is okay
					}
				}
			}
		}
		createExtjsMap(); // create the extJS locale map on the fly
	}
	
	public static void addLanguage(String localeStr) {
		if (localeStr!=null) {
			for (String name : javaLocaleMap.keySet()) {
				Locale loc = javaLocaleMap.get(name);
				if (name.startsWith(localeStr)) {
					String javaLocaleStr = loc.toString();
					if (!localeMap.containsKey(javaLocaleStr)) {
						localeMap.put(loc.toString(), loc);
						LabelValueBean lvBean = new LabelValueBean(loc.getDisplayName(loc), loc.toString());
						possibleLocales.add(lvBean);
						if (name.equals(localeStr)) {
							availableLocales.add(new LabelValueBean(loc.getDisplayName(loc), loc.toString()));
						}
					}
				}
			}
			Collections.sort(possibleLocales);
			Collections.sort(availableLocales);
		}
	}



	/**
	 * @return Returns the available locales in string format, including country code, like en_US.
	 * There is also a "Browser" entry in this list, in case the user wants to control the
	 * preferred language settings via the browser.
	 */
	public static List<LabelValueBean> getPossibleLocales() {
		return possibleLocales;
	}

	/**
	 * @return Returns the available locales in string format, including country code only when there is a . 
	 */
	public static List<LabelValueBean> getAvailableLocales() {
		return availableLocales;
	}

	/**
	 * returns the available languages in java 
	 * @return
	 */
	public static List<LabelValueBean> getJavaLanguages() {
		return javaLanguages;
	}

	
	
	public static List<Locale> getPropertiesLocales() {

		if (propertiesLocales==null) {
			propertiesLocales = new LinkedList<Locale>();
		}
		if (propertiesLocales.isEmpty()) {

			/**
			 * Get the locales available on java 
			 */
			Locale[] javaLocalesArr = Locale.getAvailableLocales(); // all on this system
			LOGGER.debug("Number of locales available in JVM " + javaLocalesArr.length);
			SortedMap<String, Locale> javaLocaleMap = new TreeMap<String, Locale>();
			for (Locale locale : javaLocalesArr) {
				javaLocaleMap.put(locale.toString(), locale);
			}
			//List<Locale> localeList = new LinkedList<Locale>();
			for (String name : javaLocaleMap.keySet()) {
				Locale loc = javaLocaleMap.get(name);
				// Match available locales in the JRE with resource
				// files provided for the Genji system. Only those locales
				// are being offered.
				ResourceBundle propertyBundles = ResourceBundleManager.getInstance().getResourceBundle(
						ResourceBundleManager.APPLICATION_RESOURCES_STRUTS2, loc);
				Locale thepRealLoc = propertyBundles.getLocale();
				//it is enough to have only the corresponding language files
				//in order to include also the country specific locales
				//(the country specific files does not have to be present)
				//because we need the formatting features linked with the countries
				if (EqualUtils.equal(thepRealLoc.getLanguage(), loc.getLanguage()) && 
						EqualUtils.equal(thepRealLoc.getCountry(), loc.getCountry())) {
					propertiesLocales.add(loc);
					LOGGER.debug("Property file found for " + loc);
				}
			}
			LOGGER.debug("Total number of locale property files found " + propertiesLocales.size());
		}
		return propertiesLocales;
	}



	/**
	 * Checks if the locale preferred by the user can actually be provided.
	 * If not, the next best match is taken, for example by removing the variant.
	 * In the worst case, the system default locale is taken, if it exists, or
	 * the standard locale (English).
	 * @param locale desired by the user
	 * @return a best match existing locale
	 * 
	 */
	public static Locale getExistingLocale(Locale locale) {
		if (locale == null) {
			locale = Locale.getDefault();
		}
		Vector<Locale> lv = new Vector<Locale>();
		lv.add(locale);
		return getExistingLocale(lv.elements());
	}


	/**
	 * Retrieve the preferred existing locale for this user. 
	 * @param locales - the preferred locales as sent by the browser
	 * @return the first preferred existing locale out of this list, or the system default
	 * if none exists
	 */
	public static Locale getExistingLocale(Enumeration<Locale> locales) {
		List<Locale> localesList = new LinkedList<Locale>();
		for (Enumeration<Locale> e = locales; e.hasMoreElements(); ) {
			Locale locale = e.nextElement();
			localesList.add(locale);
		}
		for (Locale locale : localesList) {
			if (localeMap.get(locale.toString()) != null) {
				LOGGER.debug("Total match for " + locale.toString());
				return locale;
			}
		}
		// We were not successful. Now let's try without variant.
		for (Locale locale : localesList) {
			if (localeMap.get(locale.getLanguage()+"_"+locale.getCountry()) != null) {
				LOGGER.debug("Language and country match for " + locale.toString());
				return locale;
			}
		} 
		// We were not successful. Now let's try just the language.
		for (Locale locale : localesList) {
			if (localeMap.get(locale.getLanguage()) != null) {
				LOGGER.debug("Language match for " + locale.getLanguage());
				return locale;
			}
		}
		return Locale.getDefault();
	}

	/**
	 * Checks if the locale for the online help preferred by the user can actually be provided.
	 * If not found, the standard online help locale (English) is provided.
	 * <br>
	 * @param locale online help locale desired by the user
	 * @return a best match existing online help locale
	 * 
	 */
	public static String getExistingOnlineHelpLanguageCode(Locale locale) {
		if (locale == null) {
			return "";
		}
		Locale loc = helpMap.get(locale.getLanguage());
		if (loc != null) {
			return loc.getLanguage();
		}
		else {
			return "";
		}
	}

	/**
	 * Add existing locales here, so we can later check if a user requests a locale
	 * if he can be served. The existing locales are determined during system startup
	 * by looking at some specific resource files.
	 * <br>
	 * @param locale
	 */
	/*private static void addLocale(Locale locale) {
		localeMap.put(locale.toString(), locale);
	}*/

	/**
	 * 
	 * @param loc the locale in toString format (e.g. de_DE_BA)
	 * @return a locale object for this locale
	 */
	public static Locale getLocaleFromString(String loc) {
		return new Locale(getLanguage(loc), getCountry(loc), getVariant(loc));
	}



	private static String getLanguage(String loc) {
		String[] token = loc.split("_");
		return token[0];
	}

	private static String getCountry(String loc) {
		String[] token = loc.split("_");
		if (token.length > 1) { 
			return token[1];
		}
		else {
			return "";
		}
	}

	private static String getVariant(String loc) {
		String[] token = loc.split("_");
		if (token.length > 2) { 
			return token[2];
		}
		else {
			return "";
		}		
	}

	/**
	 * Return a matching locale suffix for the ExtJS language files.
	 * @param locale
	 * @return
	 */
	public static String getExistingExtJSLocale(Locale locale) {
		String language = locale.getLanguage();
		String country = locale.getCountry();
		String lpc = language;
		if (country != null) {
			lpc = language + "_" + country;
		}
		if (extjsMap.get(lpc) != null) {
			return lpc;
		}
		if (extjsMap.get(language) != null) {
			return language;
		} else {
			return "en";
		}
	}

	public static void exportLocaleToSession(Map<String, Object> sessionMap , Locale locale) {
		// For I18N support of Struts1 framework.
		//httpSession.setAttribute(Globals.LOCALE_KEY, locale);
		// For I18N interceptor of Struts2 framework.
		// See http://struts.apache.org/2.x/docs/i18n-interceptor.html
		sessionMap.put(Constants.LOCALE_KEY, locale);
		sessionMap.put("localizationJSON", LocalizeJSON.encodeLocalization(locale));
		TPersonBean personBean=(TPersonBean)sessionMap.get(Constants.USER_KEY);
		if(personBean!=null){
			List<TListTypeBean> issueTypes= IssueTypeBL.loadAllByPerson(personBean.getObjectID(),locale);
			sessionMap.put("issueTypesJSON",JSONUtility.encodeIssueTypes(issueTypes));
		}

		String helpLocale = LocaleHandler.getExistingOnlineHelpLanguageCode(locale);
		sessionMap.put("HELPLOCALE", helpLocale);
		String extJSLocale = LocaleHandler.getExistingExtJSLocale(locale);
		sessionMap.put("EXTJSLOCALE", extJSLocale);
		//format for Ext.form.field.Date
		sessionMap.put("EXTJSDATEFORMAT", DateTimeUtils.getInstance().getExtJSDateFormat(locale));
		//submitFormat for Ext.form.field.Date to be interpretable by struts after submit
		sessionMap.put("EXTJSSUBMITDATEFORMAT", DateTimeUtils.getInstance().getExtJsTwoDigitsYearDateFormat(locale));
		//httpSession.setAttribute("EXTJSDATETIMEFMT", DateTimeUtils.getInstance().getExtJsDateTimeFormat(locale));
		sessionMap.put("EXTJSDATETIMEFORMAT", DateTimeUtils.getInstance().getExtJsLongDateTimeFormat(locale));
		sessionMap.put("EXTJSTIMEFORMAT", DateTimeUtils.getInstance().getExtJSTimeFormat(locale));
		sessionMap.put("EXTJSTIMEFORMATNOSECONDS", DateTimeUtils.getInstance().getExtJSTimeFormatNoSeconds(locale));

		char decimalSeparator = '.';
		NumberFormat numberFormat = DoubleWithDecimalDigitsNumberFormatUtil.getInstance(2).getNumberFormatInstance(locale);
		if (numberFormat instanceof DecimalFormat) {
			DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) numberFormat).getDecimalFormatSymbols();
			decimalSeparator = decimalFormatSymbols.getDecimalSeparator();
		}
		sessionMap.put("EXTJSDECIMALSEPARATOR", String.valueOf(decimalSeparator));
		String dir = LocalizeUtil.getLocalizedTextFromApplicationResources("common.dir", locale);
		String alignR="right";
		String alignL="left";
		if ("rtl".equals(dir)) {
			alignR="left";
			alignL="right";
		}
		if (dir == null){
			dir = "ltr";
		}
		sessionMap.put("alignR", alignR);
		sessionMap.put("alignL", alignL);
		sessionMap.put("dir", dir);
	}

	/*
	 * This is not cool, but it is not worthwhile to scan the file system for the language files.
	 */
	private static void createExtjsMap() {
		String[] locs = {"af","bg", "ca", "cs", "da", "de", "el_GR", "en_GB", "en", "es", "fa", "fi", "fr_CA",
				"fr", "gr", "he", "hr", "hu", "id", "it", "ja", "ko", "lt", "lv", "mk", "nl", "no_NB",
				"no_NN", "pl", "pt_BR", "pt_PT", "pt", "ro", "ru", "sk", "sl", "sr_RS", "sr", "sv_SE",
				"th", "tr", "ukr", "vn", "zh_CN", "zh_TW"};
		extjsMap.clear();
		for (int i = 0; i < locs.length; ++i) {
			extjsMap.put(locs[i], getLocaleFromString(locs[i]));
		}
	}

}
