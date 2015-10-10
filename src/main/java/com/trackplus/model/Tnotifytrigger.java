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

package com.trackplus.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the TNOTIFYTRIGGER database table.
 * 
 */
@Entity
@Table(name="TNOTIFYTRIGGER")
@TableGenerator(name="TNOTIFYTRIGGER_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_NOTIFYTRIGGER, allocationSize = 10)
public class Tnotifytrigger extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TNOTIFYTRIGGER_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=1)
	private String issystem;

	@Column(name="`LABEL`", length=255)
	private String label;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tnotifyfield
	@OneToMany(mappedBy="tnotifytrigger")
	private List<Tnotifyfield> tnotifyfields;

	//bi-directional many-to-one association to Tnotifysetting
	@OneToMany(mappedBy="tnotifytrigger")
	private List<Tnotifysetting> tnotifysettings;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="PERSON")
	private Tperson tperson;

	public Tnotifytrigger() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getIssystem() {
		return this.issystem;
	}

	public void setIssystem(String issystem) {
		this.issystem = issystem;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public List<Tnotifyfield> getTnotifyfields() {
		return this.tnotifyfields;
	}

	public void setTnotifyfields(List<Tnotifyfield> tnotifyfields) {
		this.tnotifyfields = tnotifyfields;
	}

	public Tnotifyfield addTnotifyfield(Tnotifyfield tnotifyfield) {
		getTnotifyfields().add(tnotifyfield);
		tnotifyfield.setTnotifytrigger(this);

		return tnotifyfield;
	}

	public Tnotifyfield removeTnotifyfield(Tnotifyfield tnotifyfield) {
		getTnotifyfields().remove(tnotifyfield);
		tnotifyfield.setTnotifytrigger(null);

		return tnotifyfield;
	}

	public List<Tnotifysetting> getTnotifysettings() {
		return this.tnotifysettings;
	}

	public void setTnotifysettings(List<Tnotifysetting> tnotifysettings) {
		this.tnotifysettings = tnotifysettings;
	}

	public Tnotifysetting addTnotifysetting(Tnotifysetting tnotifysetting) {
		getTnotifysettings().add(tnotifysetting);
		tnotifysetting.setTnotifytrigger(this);

		return tnotifysetting;
	}

	public Tnotifysetting removeTnotifysetting(Tnotifysetting tnotifysetting) {
		getTnotifysettings().remove(tnotifysetting);
		tnotifysetting.setTnotifytrigger(null);

		return tnotifysetting;
	}

	public Tperson getTperson() {
		return this.tperson;
	}

	public void setTperson(Tperson tperson) {
		this.tperson = tperson;
	}

}
