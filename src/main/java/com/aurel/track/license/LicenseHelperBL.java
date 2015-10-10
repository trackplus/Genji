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

package com.aurel.track.license;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.server.siteConfig.LicenseTO;
import com.aurel.track.admin.server.siteConfig.SiteConfigBL;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.json.ControlError;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.StringArrayParameterUtils;

public class LicenseHelperBL {

	private static final Logger LOGGER = LogManager.getLogger(LicenseHelperBL.class);
	

	public static String sendPOSTReq(List<BasicNameValuePair> params, String licenseProviderAddress) {
		String responseStr = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(licenseProviderAddress);
			// Request parameters and other properties.
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			//Execute and get the response.
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
			    InputStream instream = entity.getContent();
			    responseStr = StringArrayParameterUtils.getStringFromInputStream(instream);
			    try {
			        // do something useful
			    } finally {
			        instream.close();
			    }
			}

		}catch(Exception ex) {
			LOGGER.error(ExceptionUtils.getStackTrace(ex));
		}
	    int idx = responseStr.indexOf("}<!DOCTYPE HTML", 0);
	    if (idx > 0) {
	    	responseStr = responseStr.substring(0, idx+1);
	    }
		return responseStr;
	}

}

