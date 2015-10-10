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

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class ImageServerAction extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(ImageServerAction.class);
	private Map<String, Object> session;
	private String imageProviderKey = null;
	private String imageProviderParams = null;
	private String imageFormat = null;

	/**
	 * Write the image in the format given to the output stream
	 */
	@Override

	public String execute() {
		System.out.println("IMAGE ACTION !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

		ImageProvider imageProvider = (ImageProvider)session.get(imageProviderKey);
		if (imageProvider == null) {
			LOGGER.error("Image provider is Null for: "+imageProviderKey);
			return null;
		}

		Map configParametersMap = (Map)session.get(imageProviderParams);
		LOGGER.debug("imageProviderParams Name :"+imageProviderParams);
		LOGGER.debug("imageProviderParams Value :"+configParametersMap);


		if (imageFormat == null || imageFormat.length() < 1) {
			imageFormat = "png";
		}

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("image/" + imageFormat);
		try {
			ServletOutputStream out = response.getOutputStream();
			imageProvider.writeImage(out, configParametersMap, imageFormat);
		} catch (IOException e) {
			LOGGER.error("Error on write Image:"+e.getMessage());
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return null;
	}

	/**
	 * Write the image in the format given to the output stream
	 */
	public String loadJSON() {
        System.out.println("LOAD JSON!");

       /* HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json");
        try {
            PrintWriter out = response.getWriter();

            StringBuilder sb=new StringBuilder();
            JSONUtility.appendStringValue(sb, "name", "mONE");
            JSONUtility.appendStringValue(sb, "data1", "10");
            JSONUtility.appendStringValue(sb, "data2", "150");
            JSONUtility.appendStringValue(sb, "data3", "101");
            JSONUtility.appendStringValue(sb, "data4", "151");
            JSONUtility.appendStringValue(sb, "data5", "123");
            System.out.println("JSONUTILITY " + sb.toString());


            out.write("{'items':[{'name': 'm one', 'data1': 10, 'data2': 12, 'data3': 14, 'data4': 8, 'data5': 13}, {'name': 'm two', 'data1': 100, 'data2': 120, 'data3': 140, 'data4': 80, 'data5': 13}]}");
            out.flush();
            out.close();
        } catch (Exception ex) {
            LOGGER.error("Error" + ex.getMessage());
        }
          


        ImageProvider imageProvider = (ImageProvider)session.get(imageProviderKey);


		if (imageProvider == null) {
			LOGGER.error("Image provider is Null for: "+imageProviderKey);
			return null;
		}

		Map configParametersMap = (Map)session.get(imageProviderParams);
		LOGGER.debug("imageProviderParams Name :"+imageProviderParams);
		LOGGER.debug("imageProviderParams Value :"+configParametersMap);


		if (imageFormat == null || imageFormat.length() < 1) {
			imageFormat = "png";
		}

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/" + imageFormat);
        //response.setContentType("application/json");
		try {
			ServletOutputStream out = response.getOutputStream();

			imageProvider.writeImage(out, configParametersMap, imageFormat);
		} catch (IOException e) {
			LOGGER.error("Error on write Image:"+e.getMessage(), e);
			if(LOGGER.isDebugEnabled()){
				LOGGER.error(org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
			}
		}*/      
		return null;
	}

	/**
	 * @param imageProviderKey
	 */
	public void setImageProviderKey(String imageProviderKey) {
		this.imageProviderKey = imageProviderKey;
	}

	public void setImageProviderParams(String imageProviderParams) {
		this.imageProviderParams = imageProviderParams;
	}

	/**
	 * @param imageFormat
	 */
	public void setImageFormat(String imageFormat) {
		this.imageFormat = imageFormat;
	}

	public void setSession(Map session) {
		this.session = session;
	}

}

