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

package com.aurel.track.vc;

public class FileDiff {
	public static final char TYPE_ADDED='A';
	public static final char TYPE_MODIFIED='M';
	public static final char TYPE_DELETED='D';
	public static final char TYPE_REPLACED='R';

	protected String path;
	protected char type;

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public char getType() {
		return type;
	}
	public void setType(char type) {
		this.type = type;
	}
	public String replacePattern(String pattern,String revision){
		String s=pattern;//.replaceAll("\\$\\{repo-name\\}", repoName);
		if(path!=null){
			String encodedPath=path.replaceAll("/", "%2F");
			s=s.replaceAll("\\$\\{path\\}", path);
			s=s.replaceAll("\\$\\{encodedPath\\}", encodedPath);
		}
		s=s.replaceAll("\\$\\{rev\\}", revision);
		int revInt=-1;
		try{
			revInt=Integer.parseInt(revision);
			s=s.replaceAll("\\$\\{rev-1\\}", (revInt-1)+"");
		}catch (Exception e) {
			// ignore
		}
		return s;
	}
}
