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

public class AttachBLException extends  Exception{
	private static final long serialVersionUID = -3889793250788679585L;
	
	private String localizedKey;
	private String[] localizedParameteres;
	
	public AttachBLException() {
		super();
	}
	public AttachBLException(String message, Exception cause) {
		super(message, cause);
	}
	public AttachBLException(String message) {
		super(message);
	}
	public AttachBLException(Exception cause) {
		super(cause);
	}
	public String getLocalizedKey() {
		return localizedKey;
	}
	public void setLocalizedKey(String localizedKey) {
		this.localizedKey = localizedKey;
	}
	public String[] getLocalizedParameteres() {
		return localizedParameteres;
	}
	public void setLocalizedParameteres(String[] localizedParameteres) {
		this.localizedParameteres = localizedParameteres;
	}
	
}
