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

package com.aurel.track.attachment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TAccessControlListBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.prop.ApplicationBean;

/**
 * <p>Application logic for managing a file share via Trackplus. The file share is located at
 * a configurable root directory (to be set as part of the server configuration).</p>
 * <p>There is a subdirectory beneath this root directory for each project, and a public
 * space that can be accessed by anyone. The project file share spaces can be read by 
 * those that have read permission in that project. Files can be uploaded by those that have
 * write permissions in that project.</p>
 *
 */
public class FileShareBL {
	
	/**
	 * 
	 * @param person - the person trying to access the file share
	 * @param dir - directory relative to the root of the file share
	 * @return
	 */
	
	
    public static String dirsToJson(HashMap<String, TProjectBean> projects, File file) {
        File[] files = file.listFiles();
        StringBuffer sb = null;
        if (files != null && files.length > 0) {
            sb = new StringBuffer();
            sb.append("[\n");
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                if (f.isDirectory() && projects.containsKey(f.getName())) {
                    sb.append("\t{id: '").append(f.getAbsolutePath());
                    sb.append("', text: '").append(f.getName());
                    sb.append("'}");
                }
                if (i < files.length - 1) {
                    sb.append(",\n");
                }
            }
            sb.append("\n]");
            return sb.toString();
        } else {
            return "[]";
        }
    }
}
