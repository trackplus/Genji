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

package com.aurel.track.plugin;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import com.opensymphony.xwork2.ActionSupport;

/**
 * An action used to open an image from classpath
 */
public class ImageAction extends ActionSupport {

	private static final long serialVersionUID = 340L;
	private static final String NO_IMAGE="plugins/dashboard/images/noImage.gif";
    private String imageName;
    @Override
	public String execute(){
       HttpServletResponse  response=org.apache.struts2.ServletActionContext.getResponse();
       response.setContentType("image/gif");
       OutputStream oStream = null;
        try {
            oStream = response.getOutputStream();
        } catch (IOException e) {
           return null;
        }
       InputStream in=null;
       if(imageName!=null){
           in=ImageAction.class.getClassLoader().getResourceAsStream(imageName);
       }
       if(in==null){
           in=ImageAction.class.getClassLoader().getResourceAsStream(NO_IMAGE);
       }
       BufferedInputStream instream=new BufferedInputStream(in);
       byte[] buffer = new byte[1024];
	   try {
			int size = -1;
			while (-1 != (size = instream.read(buffer, 0, 1024))){
				oStream.write(buffer, 0, size);
			}
	   } catch (Exception t) {
	   } finally {
		   try {
			   instream.close();
		   }
		   catch (Exception t) {
			   // just ignore
		   }
	   }
	   return  null;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
