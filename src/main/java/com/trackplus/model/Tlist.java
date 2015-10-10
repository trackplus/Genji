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
 * The persistent class for the TLIST database table.
 * 
 */
@Entity
@Table(name="TLIST")
@TableGenerator(name="TLIST_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_LIST, allocationSize = 10)
public class Tlist extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TLIST_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	private int childnumber;

	@Column(length=1)
	private String deleted;

	@Column(length=255)
	private String description;

	private int listtype;

	@Lob
	private String moreprops;

	@Column(nullable=false, length=255)
	private String name;

	private int repositorytype;

	@Column(length=50)
	private String taglabel;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tlist
	@ManyToOne
	@JoinColumn(name="PARENTLIST")
	private Tlist tlist;

	//bi-directional many-to-one association to Tlist
	@OneToMany(mappedBy="tlist")
	private List<Tlist> tlists;

	//bi-directional many-to-one association to Tproject
	@ManyToOne
	@JoinColumn(name="PROJECT")
	private Tproject tproject;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="OWNER")
	private Tperson tperson;

	//bi-directional many-to-one association to Toption
	@OneToMany(mappedBy="tlist")
	private List<Toption> toptions;

	//bi-directional many-to-one association to Toptionsetting
	@OneToMany(mappedBy="tlist")
	private List<Toptionsetting> toptionsettings;

	public Tlist() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public int getChildnumber() {
		return this.childnumber;
	}

	public void setChildnumber(int childnumber) {
		this.childnumber = childnumber;
	}

	public String getDeleted() {
		return this.deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getListtype() {
		return this.listtype;
	}

	public void setListtype(int listtype) {
		this.listtype = listtype;
	}

	public String getMoreprops() {
		return this.moreprops;
	}

	public void setMoreprops(String moreprops) {
		this.moreprops = moreprops;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRepositorytype() {
		return this.repositorytype;
	}

	public void setRepositorytype(int repositorytype) {
		this.repositorytype = repositorytype;
	}

	public String getTaglabel() {
		return this.taglabel;
	}

	public void setTaglabel(String taglabel) {
		this.taglabel = taglabel;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tlist getTlist() {
		return this.tlist;
	}

	public void setTlist(Tlist tlist) {
		this.tlist = tlist;
	}

	public List<Tlist> getTlists() {
		return this.tlists;
	}

	public void setTlists(List<Tlist> tlists) {
		this.tlists = tlists;
	}

	public Tlist addTlist(Tlist tlist) {
		getTlists().add(tlist);
		tlist.setTlist(this);

		return tlist;
	}

	public Tlist removeTlist(Tlist tlist) {
		getTlists().remove(tlist);
		tlist.setTlist(null);

		return tlist;
	}

	public Tproject getTproject() {
		return this.tproject;
	}

	public void setTproject(Tproject tproject) {
		this.tproject = tproject;
	}

	public Tperson getTperson() {
		return this.tperson;
	}

	public void setTperson(Tperson tperson) {
		this.tperson = tperson;
	}

	public List<Toption> getToptions() {
		return this.toptions;
	}

	public void setToptions(List<Toption> toptions) {
		this.toptions = toptions;
	}

	public Toption addToption(Toption toption) {
		getToptions().add(toption);
		toption.setTlist(this);

		return toption;
	}

	public Toption removeToption(Toption toption) {
		getToptions().remove(toption);
		toption.setTlist(null);

		return toption;
	}

	public List<Toptionsetting> getToptionsettings() {
		return this.toptionsettings;
	}

	public void setToptionsettings(List<Toptionsetting> toptionsettings) {
		this.toptionsettings = toptionsettings;
	}

	public Toptionsetting addToptionsetting(Toptionsetting toptionsetting) {
		getToptionsettings().add(toptionsetting);
		toptionsetting.setTlist(this);

		return toptionsetting;
	}

	public Toptionsetting removeToptionsetting(Toptionsetting toptionsetting) {
		getToptionsettings().remove(toptionsetting);
		toptionsetting.setTlist(null);

		return toptionsetting;
	}

}
