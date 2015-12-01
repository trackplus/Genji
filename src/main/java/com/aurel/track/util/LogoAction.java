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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.dbase.HandleHome;
import com.opensymphony.xwork2.ActionSupport;

/**
 * This class writes logo images into a given output stream. The images are retrieved 
 * from the TRACKPLUS_HOME directory and buffered. Changing the images requires to
 * restart the application server.
 * 
 */
public class LogoAction extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(LogoAction.class);
	private Map<String, Object> session;
	private String logoType = null;	
	private HashMap<String,LogoBuffer> logoMap = new HashMap<String,LogoBuffer>();

	/**
	 * Write the image in the format given to the output stream
	 */
	@Override
	public String execute() {
		String logo = null;

		HttpServletResponse response = ServletActionContext.getResponse();

		if ("m".equals(logoType)) {
			response.setContentType("image/gif");
			logo = "tracklogo.gif";
		}  else if ("r".equals(logoType)) {
			response.setContentType("image/png");
			logo = "logo-254x105.png";
		}
		else {
			response.setContentType("image/png");
			logo = "trackLogo.png";
			logoType = "x";
		}

		OutputStream oStream = null;
		try {
			oStream = response.getOutputStream();
		} catch (IOException e) {
			return null;
		}

		LogoBuffer lb = logoMap.get(logoType);

		if (lb != null) {;
		try {
			oStream.write(lb.getImage(), 0, lb.getSize());
		} catch (Exception e) {
			LOGGER.error("Problem with logos: " + e.getMessage());
		}			
		return null;
		} 

		String logoDir = HandleHome.getTrackplus_Home()+"/logos";

		InputStream in=null;
		BufferedInputStream instream=null;
		
		final int BUFFERSIZE = 256*1024;
		
		byte[] buffer = new byte[BUFFERSIZE];

		try {
			in= FileUtils.openInputStream(new File(logoDir+"/"+logo));
			instream=new BufferedInputStream(in);
			int size = -1;
			int isize = size;
			while (-1 != (size = instream.read(buffer, 0, BUFFERSIZE))){
				oStream.write(buffer, 0, size);
				if (size > 256*BUFFERSIZE) {
					LOGGER.error("Logos should not be larger than " + BUFFERSIZE/1024 + " kByte!");
					break;
				}
				isize = size;
			}
			byte[] tbuffer = new byte[isize];
			for (int i = 0; i < size; ++i ) {
				tbuffer[i] = buffer[i];
			}
			lb = new LogoBuffer();
			lb.setImage(tbuffer);
			lb.setSize(size);
			logoMap.put(logoType,lb);
			instream.close();
			in.close();
		} catch (Exception e) {
			LOGGER.error("Problem with logos: " + e.getMessage());
		}
		return null;
	}


	/**
	 * @param _logoType
	 */
	public void setLogoType(String _logoType) {
		this.logoType = _logoType;
	}

	public String getLogoType() {
		return this.logoType;
	}

	@Override
	public void setSession(Map<String,Object> session) {
		this.session = session;
	}

	public Map<String,Object> getSession() {
		return this.session;
	}
	
	/**
	 * Internal class to buffer the logos
	 *
	 */
	class LogoBuffer {
		public byte[] getImage() {
			return image;
		}
		public void setImage(byte[] image) {
			this.image = image;
		}
		public int getSize() {
			return size;
		}
		public void setSize(int size) {
			this.size = size;
		}
		byte[] image;
		int size;
	}

}

