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

package com.aurel.track.admin.customize.notify.trigger;

public class NotifyTriggerFieldTO {
	private String id;
	private Integer objectID;
	private String actionTypeLabel;
	private String fieldTypeLabel;
	private String fieldLabel;	
	private boolean originator;
	private boolean manager;
	private boolean responsible;
	private boolean consulted;
	private boolean informed;
	private boolean observer;
		
	
	public NotifyTriggerFieldTO(String id, Integer objectID,
			boolean originator,  boolean manager, boolean responsible,
			boolean consulted, boolean informed, boolean observer) {
		super();
		this.id = id;
		this.objectID = objectID;
		this.originator = originator;
		this.manager = manager;
		this.responsible = responsible;
		this.consulted = consulted;
		this.informed = informed;
		this.observer = observer;
	}


	public NotifyTriggerFieldTO(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}
	

	public Integer getObjectID() {
		return objectID;
	}


	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}


	public String getActionTypeLabel() {
		return actionTypeLabel;
	}


	public void setActionTypeLabel(String actionTypeLabel) {
		this.actionTypeLabel = actionTypeLabel;
	}


	public String getFieldTypeLabel() {
		return fieldTypeLabel;
	}


	public void setFieldTypeLabel(String fieldTypeLabel) {
		this.fieldTypeLabel = fieldTypeLabel;
	}


	public String getFieldLabel() {
		return fieldLabel;
	}


	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}


	public boolean isOriginator() {
		return originator;
	}


	public void setOriginator(boolean originator) {
		this.originator = originator;
	}


	public boolean isManager() {
		return manager;
	}


	public void setManager(boolean manager) {
		this.manager = manager;
	}


	public boolean isResponsible() {
		return responsible;
	}


	public void setResponsible(boolean responsible) {
		this.responsible = responsible;
	}


	public boolean isConsulted() {
		return consulted;
	}


	public void setConsulted(boolean consulted) {
		this.consulted = consulted;
	}


	public boolean isInformed() {
		return informed;
	}


	public void setInformed(boolean informed) {
		this.informed = informed;
	}


	public boolean isObserver() {
		return observer;
	}


	public void setObserver(boolean observer) {
		this.observer = observer;
	}
}
