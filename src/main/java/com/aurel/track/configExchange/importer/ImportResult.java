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

package com.aurel.track.configExchange.importer;

import com.aurel.track.beans.ISerializableLabelBean;

/**
 * a result from import a entity
 * @author Adrian Bojani
 */
public class ImportResult {
	//success constants >0
	public static final int SUCCESS_NEW_ENTITY=1;
	public static final int SUCCESS_OVERRIDE=2;
	public static final int SUCCESS_TAKE_EXISTING=3;

	//error constants <0
	public static final int ERROR_NO_IMPORTER_FOUND=-1;
	public static final int ERROR_DB_SAVE=-2;
	public static final int ERROR=-3;

	private ISerializableLabelBean bean;
	private int code;
	private String entityType;
	private Integer entityID;
	private Integer newObjectID;
	public  ImportResult(){
	}

	public ImportResult(ISerializableLabelBean bean, String entityType, Integer entityID, int code, Integer newObjectID) {
		this.bean = bean;
		this.entityType = entityType;
		this.entityID = entityID;
		this.code = code;
		this.newObjectID = newObjectID;
	}

	public ISerializableLabelBean getBean() {
		return bean;
	}

	public void setBean(ISerializableLabelBean bean) {
		this.bean = bean;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	public boolean isSuccess(){
		return this.code>0&&this.bean!=null;
	}
	public boolean isError(){
		return this.code<0;
	}
	public String getErrorMessage(){
		switch (code){
			case ERROR_DB_SAVE:return "Error saving in DB";
			case ERROR_NO_IMPORTER_FOUND:return "No entity importer found";
			case ERROR:return "Error!";
		}
		return null;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public Integer getEntityID() {
		return entityID;
	}

	public void setEntityID(Integer entityID) {
		this.entityID = entityID;
	}

	public Integer getNewObjectID() {
		return newObjectID;
	}

	public void setNewObjectID(Integer newObjectID) {
		this.newObjectID = newObjectID;
	}

}
