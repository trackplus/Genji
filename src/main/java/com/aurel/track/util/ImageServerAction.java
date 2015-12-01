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

	@Override
	public void setSession(Map session) {
		this.session = session;
	}

}

