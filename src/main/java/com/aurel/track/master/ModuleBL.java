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

package com.aurel.track.master;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.plugin.ModuleDescriptor;
import com.aurel.track.util.StringArrayParameterUtils;

/**
 * An example of HttpClient can be customized to authenticate
 * preemptively using BASIC scheme.
 * <b/>
 * Generally, preemptive authentication can be considered less
 * secure than a response to an authentication challenge
 * and therefore discouraged.
 */
public class ModuleBL {

	public static String RESPONSE_HEADER = "Header";
	public static String RESPONSE_CONTENT_INPUT_STREAM = "ResponseContentInputStream";

	private static final Logger LOGGER = LogManager.getLogger(ModuleBL.class);

    public static Cookie sendPOSTRequest(String urlString)  {
    	Cookie responseCookie = null;
    	try {
	    	HttpClient httpclient = new DefaultHttpClient();//HttpClients.createDefault();
	    	HttpPost httppost = new HttpPost(urlString);
	    	// Request parameters and other properties.
	    	//Execute and get the response.
	    	HttpContext localContext = new BasicHttpContext();
	    	CookieStore cookieStore = new BasicCookieStore();
	    	localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
	    		HttpResponse response = httpclient.execute(httppost, localContext);

	    	if(cookieStore.getCookies().size() > 0) {
	    		List<org.apache.http.cookie.Cookie>cookies = cookieStore.getCookies();
	    		for (org.apache.http.cookie.Cookie cookie : cookies) {
	    			if(cookie.getName().equals("JSESSIONID")) {
	    				responseCookie = new Cookie(cookie.getName(), cookie.getValue());
	    				responseCookie.setPath(cookie.getPath());
	    				responseCookie.setDomain(cookie.getDomain());
	    			}
				}
	    	}
	    	 if( response.getEntity() != null ) {
	             response.getEntity().consumeContent();
	          }

		}catch(Exception ex) {
			LOGGER.debug(ExceptionUtils.getStackTrace(ex));
		}
		return responseCookie;
    }

    public static List<NameValuePair> getJenkinsParamsForLogin(String username, String password) {
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("j_username", username));
    	params.add(new BasicNameValuePair("j_password", password));
    	params.add(new BasicNameValuePair("remember_me", "false"));
    	params.add(new BasicNameValuePair("from", "/jenkins"));
    	String json = "{\"j_username\": \"" + password + "\", \"j_password\": \"" + password + "\", \"remember_me\": false, \"from\": \"/jenkins/\"}";
    	params.add(new BasicNameValuePair("json", json));
    	return params;
    }

    public static String getJenkinsUrl(ModuleDescriptor md, String userName, String password) {
    	StringBuilder jenkinsURL = new StringBuilder(StringArrayParameterUtils.appendSlashToURLString(md.getCleanedUrl()));
    	jenkinsURL.append("j_acegi_security_check");
    	try {
    		jenkinsURL.append("?j_username=" + URLEncoder.encode(userName, "UTF-8") + "&j_password=" + URLEncoder.encode(password, "UTF-8"));
    	}catch(UnsupportedEncodingException ex) {
    	}
    	return jenkinsURL.toString();
    }

    public static List<NameValuePair> getSonarParamsForLogin(String username, String password) {
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("commit", "Log+in"));
    	params.add(new BasicNameValuePair("login", username));
    	params.add(new BasicNameValuePair("password", password));
    	params.add(new BasicNameValuePair("return_to_anchor", ""));
    	return params;
    }

    public static String getSonarURL(ModuleDescriptor md, String userName, String password) {
    	StringBuilder sonarURL = new StringBuilder(StringArrayParameterUtils.appendSlashToURLString(md.getCleanedUrl()));
    	sonarURL.append("sessions/login");
    	try {
    		sonarURL.append("?login=" + URLEncoder.encode(userName, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8"));
    	}catch(UnsupportedEncodingException ex) {
    	}
    	return sonarURL.toString();
    }

    public static void handleJenkinsResponse(Map<String, Object>responseMap, StringBuilder sessionID, StringBuilder errorLocation, StringBuilder path) {
    	Header[] h = (Header[])responseMap.get(RESPONSE_HEADER);
    	if(h != null) {
	    	for(int i = 0; i< h.length; i++) {
	    		if(h[i].getName().contains("Set-Cookie")) {
	    			String value = h[i].getValue();
	    			if(value != null && value.contains("=") && value.contains(";")) {
	    				sessionID.append(value.substring(value.indexOf("=") + 1, value.indexOf(";")));
	    				if(value.contains("Path=")) {
	    					path.append(value.substring(value.indexOf("Path") + 5, value.lastIndexOf(";")));
	    				}
	    			}
	  	    	}
	  	    	if(h[i].getName().contains("Location")) {
	  	    		String value = h[i].getValue();
	  	    		if(value.contains(";")) {
	  	    			value = value.substring(0, value.indexOf(";"));
	  				}
	  	    		errorLocation.append(value);
	  	    	}
	    	}
    	}
    }

    public static void handleSonarResponse(Map<String, Object>responseMap, StringBuilder sessionID, StringBuilder errorLocation, StringBuilder path) {
    	Header[] h = (Header[])responseMap.get(RESPONSE_HEADER);
    	InputStream responseStream = (InputStream)responseMap.get(RESPONSE_CONTENT_INPUT_STREAM);
    	if(h != null && responseStream != null) {
    		String responseString = StringArrayParameterUtils.getStringFromInputStream(responseStream);
    		errorLocation.append(responseString);
	    	for(int i = 0; i< h.length; i++) {
	    		if(h[i].getName().contains("Set-Cookie")) {
	    			String value = h[i].getValue();
	    			if(value != null && value.contains("=") && value.contains(";")) {
	    				sessionID.append(value.substring(value.indexOf("=") + 1, value.indexOf(";")));
	    				if(value.contains("Path=")) {
	    					path.append(value.substring(value.indexOf("Path") + 5, value.lastIndexOf(";")));
	    				}
	    			}
	  	    	}
	    	}
    	}
    }

    public static Cookie cretaeCookie(String cookieValue, String path, String url) {
    	Cookie myCookie = new Cookie("JSESSIONID", cookieValue);
		myCookie.setPath(path);
		URI uri;
		try {
			uri = new URI(url);
			String domain = uri.getHost();
			myCookie.setDomain(domain);
		} catch (URISyntaxException e) {
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return myCookie;
    }
}
