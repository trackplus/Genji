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

package com.aurel.track.prop;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.aurel.track.util.LocaleHandler;

/**
 * The class <code>LoginBLTest</code> contains tests for the class <code>{@link LoginBL}</code>.

 *
 * @generatedBy CodePro at 05.01.15 23:20
 * @author friedj
 * @version $Revision: 1.0 $
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(LocaleHandler.class)
public class LoginBLTest{
	
	final static HttpServletRequest mockrequest  = mock(HttpServletRequest.class); 
	HashMap<String, Object> hashmap = new HashMap<String,Object>();
	

	/**
	 * Run the LoginBL() constructor test.
	 *
	 * @generatedBy CodePro at 05.01.15 23:20
	 */
	@Test
	@Ignore("private Constructor")
	public void testCostructor()throws Exception {
		/* 
		LoginBL result = new LoginBL();
		assertNotNull(result);
		add additional test code here
		*/
	}

	/**
	 * Run the String decrypt(int,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.01.15 23:20
	 */
	@Test
	public void testDecrypt_1()
		throws Exception {
		int key = 1;
		String ciphertext = "";

		String result = LoginBL.decrypt(key, ciphertext);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String decrypt(int,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 05.01.15 23:20
	 */
	@Test
	public void testDecrypt_2()
		throws Exception {
		int key = 1;
		String ciphertext = "";

		String result = LoginBL.decrypt(key, ciphertext);

		// add additional test code here
		assertEquals("", result);
	}
	
	@Test()
	public void testSetEnvironment(){
		LoginBL.setEnvironment("username", "userPwd", "nonce", mockrequest, hashmap, true, false, false);
		assertTrue(true);	//just a Smoke-Test!
	}

	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 * @generatedBy CodePro at 05.01.15 23:20
	 */
	@Before
	public void setUp() throws Exception {
		// add additional set up code here
		
		PowerMockito.mockStatic(LocaleHandler.class);
		Locale locale = Locale.getDefault();
		//PowerMockito.when(LocaleHandler.getExistingLocale(any(Enumeration.class))).thenReturn(locale);
		
	}
}
