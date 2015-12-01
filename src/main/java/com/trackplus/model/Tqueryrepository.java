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
import java.util.List;


/**
 * The persistent class for the TQUERYREPOSITORY database table.
 * 
 */
@Entity
@Table(name="TQUERYREPOSITORY")
@TableGenerator(name="TQUERYREPOSITORY_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_QUERYREPOSITORY, allocationSize = 10)
public class Tqueryrepository extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TQUERYREPOSITORY_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(name="`LABEL`", length=100)
	private String label;

	@Column(length=1)
	private String menuitem;

	@Column(nullable=false)
	private int querytype;

	@Column(nullable=false)
	private int repositorytype;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tnotifysetting
	@OneToMany(mappedBy="tqueryrepository")
	private List<Tnotifysetting> tnotifysettings;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="PERSON")
	private Tperson tperson;

	//bi-directional many-to-one association to Tproject
	@ManyToOne
	@JoinColumn(name="PROJECT")
	private Tproject tproject;

	//bi-directional many-to-one association to Tclob
	@ManyToOne
	@JoinColumn(name="QUERYKEY", nullable=false)
	private Tclob tclob;

	//bi-directional many-to-one association to Tfiltercategory
	@ManyToOne
	@JoinColumn(name="CATEGORYKEY")
	private Tfiltercategory tfiltercategory;

	public Tqueryrepository() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getMenuitem() {
		return this.menuitem;
	}

	public void setMenuitem(String menuitem) {
		this.menuitem = menuitem;
	}

	public int getQuerytype() {
		return this.querytype;
	}

	public void setQuerytype(int querytype) {
		this.querytype = querytype;
	}

	public int getRepositorytype() {
		return this.repositorytype;
	}

	public void setRepositorytype(int repositorytype) {
		this.repositorytype = repositorytype;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public List<Tnotifysetting> getTnotifysettings() {
		return this.tnotifysettings;
	}

	public void setTnotifysettings(List<Tnotifysetting> tnotifysettings) {
		this.tnotifysettings = tnotifysettings;
	}

	public Tnotifysetting addTnotifysetting(Tnotifysetting tnotifysetting) {
		getTnotifysettings().add(tnotifysetting);
		tnotifysetting.setTqueryrepository(this);

		return tnotifysetting;
	}

	public Tnotifysetting removeTnotifysetting(Tnotifysetting tnotifysetting) {
		getTnotifysettings().remove(tnotifysetting);
		tnotifysetting.setTqueryrepository(null);

		return tnotifysetting;
	}

	public Tperson getTperson() {
		return this.tperson;
	}

	public void setTperson(Tperson tperson) {
		this.tperson = tperson;
	}

	public Tproject getTproject() {
		return this.tproject;
	}

	public void setTproject(Tproject tproject) {
		this.tproject = tproject;
	}

	public Tclob getTclob() {
		return this.tclob;
	}

	public void setTclob(Tclob tclob) {
		this.tclob = tclob;
	}

	public Tfiltercategory getTfiltercategory() {
		return this.tfiltercategory;
	}

	public void setTfiltercategory(Tfiltercategory tfiltercategory) {
		this.tfiltercategory = tfiltercategory;
	}

}
