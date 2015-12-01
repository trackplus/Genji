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

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.localize.LocalizeBL;
import com.aurel.track.beans.TPersonBean;

/**
 * The class <code>LocaleHandlerTest</code> contains tests for the class <code>{@link LocaleHandler}</code>.
 *
 *  $Id:$
 */
@RunWith (PowerMockRunner.class)
@PrepareForTest(LocalizeBL.class)
public class LocaleHandlerTest {
	
	private Set<String> localeSet;
	
	/**
	 */
	@Test
	public void testGetLocales() 
		throws Exception {
		PowerMockito.mockStatic(LocalizeBL.class);
		PowerMockito.when(LocalizeBL.getExistingLocales()).thenReturn(localeSet);
		LocaleHandler.getLocales();
		List<LabelValueBean> javaLanguages = LocaleHandler.getJavaLanguages();
		assertNotNull(javaLanguages);
	}
	
	/**
	 */
	@Test
	public void testGetPropertiesLocales() 
		throws Exception {
		PowerMockito.mockStatic(LocalizeBL.class);
		PowerMockito.when(LocalizeBL.getExistingLocales()).thenReturn(localeSet);
		LocaleHandler.getPropertiesLocales();
		List<LabelValueBean> javaLanguages = LocaleHandler.getJavaLanguages();
		assertNotNull(javaLanguages);
	}

	/**
	 */
	@Test
	public void testExportLocaleToSession() 
		throws Exception {
		PowerMockito.mockStatic(LocalizeBL.class);
		PowerMockito.when(LocalizeBL.getExistingLocales()).thenReturn(localeSet);
		TPersonBean person = new TPersonBean();
		Map sessionMap = new HashMap<String,Object>();
		// sessionMap.put(Constants.USER_KEY, person);
		LocaleHandler.exportLocaleToSession(sessionMap , new Locale("de"));
		List<LabelValueBean> javaLanguages = LocaleHandler.getJavaLanguages();
		assertNotNull(javaLanguages);
	}
	 

	/**
	 */
	@Test
	public void testGetExistingLocale() 
		throws Exception {
		PowerMockito.mockStatic(LocalizeBL.class);
		PowerMockito.when(LocalizeBL.getExistingLocales()).thenReturn(localeSet);
		Locale loc = LocaleHandler.getExistingLocale(new Locale("de"));
		Locale nloc = null;
		loc = LocaleHandler.getExistingLocale(nloc);
		assertNotNull(loc);
	}
	
	/**
	 */
	@Test
	public void testGetExistingLocales() 
		throws Exception {
		PowerMockito.mockStatic(LocalizeBL.class);
		PowerMockito.when(LocalizeBL.getExistingLocales()).thenReturn(localeSet);
		Vector<Locale> lv = new Vector<Locale>();
		lv.add(new Locale("de"));
		lv.add(new Locale("en_en"));
		lv.add(new Locale("nl"));
		Locale loc = LocaleHandler.getExistingLocale(lv.elements());
		assertNotNull(loc);
	}
	
	/**
	 */
	@Test
	public void testAddLanguage() 
		throws Exception {
		PowerMockito.mockStatic(LocalizeBL.class);
		PowerMockito.when(LocalizeBL.getExistingLocales()).thenReturn(localeSet);
		LocaleHandler.addLanguage("de");
		assertNotNull(1);
	}
	

	
	/**
	 */
	@Test
	public void testGetExistingOnlineHelpLanguageCode() 
		throws Exception {
		PowerMockito.mockStatic(LocalizeBL.class);
		PowerMockito.when(LocalizeBL.getExistingLocales()).thenReturn(localeSet);
		String lc = LocaleHandler.getExistingOnlineHelpLanguageCode(new Locale("de"));
		assertEquals("",lc);
	}
	
	
	
	
	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@Before
	public void setUp()
		throws Exception {
		localeSet = new HashSet<String>(Arrays.asList("de","en","fr","it","es"));
	}

	/**
	 * Perform post-test clean-up.
	 *
	 * @throws Exception
	 *         if the clean-up fails for some reason
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	@After
	public void tearDown()
		throws Exception {
		// Add additional tear down code here
	}

	/**
	 * Launch the test.
	 *
	 * @param args the command line arguments
	 *
	 * @generatedBy CodePro at 19.03.15 01:28
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(LocaleHandlerTest.class);
	}
}
