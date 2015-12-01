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

package com.trackplus.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TNOTIFYFIELD database table.
 * 
 */
@Entity
@Table(name="TNOTIFYFIELD")
@TableGenerator(name="TNOTIFYFIELD_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_NOTIFYFIELD, allocationSize = 10)
public class Tnotifyfield extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TNOTIFYFIELD_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(nullable=false)
	private int actiontype;

	@Column(length=1)
	private String consultant;

	private int field;

	private int fieldtype;

	@Column(length=1)
	private String informant;

	@Column(length=1)
	private String manager;

	@Column(length=1)
	private String observer;

	@Column(length=1)
	private String originator;

	@Column(length=1)
	private String responsible;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tnotifytrigger
	@ManyToOne
	@JoinColumn(name="NOTIFYTRIGGER", nullable=false)
	private Tnotifytrigger tnotifytrigger;

	public Tnotifyfield() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public int getActiontype() {
		return this.actiontype;
	}

	public void setActiontype(int actiontype) {
		this.actiontype = actiontype;
	}

	public String getConsultant() {
		return this.consultant;
	}

	public void setConsultant(String consultant) {
		this.consultant = consultant;
	}

	public int getField() {
		return this.field;
	}

	public void setField(int field) {
		this.field = field;
	}

	public int getFieldtype() {
		return this.fieldtype;
	}

	public void setFieldtype(int fieldtype) {
		this.fieldtype = fieldtype;
	}

	public String getInformant() {
		return this.informant;
	}

	public void setInformant(String informant) {
		this.informant = informant;
	}

	public String getManager() {
		return this.manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getObserver() {
		return this.observer;
	}

	public void setObserver(String observer) {
		this.observer = observer;
	}

	public String getOriginator() {
		return this.originator;
	}

	public void setOriginator(String originator) {
		this.originator = originator;
	}

	public String getResponsible() {
		return this.responsible;
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tnotifytrigger getTnotifytrigger() {
		return this.tnotifytrigger;
	}

	public void setTnotifytrigger(Tnotifytrigger tnotifytrigger) {
		this.tnotifytrigger = tnotifytrigger;
	}

}
