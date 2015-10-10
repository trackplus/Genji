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
 * The persistent class for the TSYSTEMSTATE database table.
 * 
 */
@Entity
@Table(name="TSYSTEMSTATE")
@TableGenerator(name="TSYSTEMSTATE_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_SYSTEMSTATE, allocationSize = 10)
public class Tsystemstate extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TSYSTEMSTATE_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=255)
	private String cssstyle;

	private int entityflag;

	@Column(name="`LABEL`", length=255)
	private String label;

	private int sortorder;

	private int stateflag;

	@Column(length=255)
	private String symbol;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Taccount
	@OneToMany(mappedBy="tsystemstate")
	private List<Taccount> taccounts;

	//bi-directional many-to-one association to Tproject
	@OneToMany(mappedBy="tsystemstate")
	private List<Tproject> tprojects;

	//bi-directional many-to-one association to Trelease
	@OneToMany(mappedBy="tsystemstate")
	private List<Trelease> treleases;

	//bi-directional many-to-one association to Tblob
	@ManyToOne
	@JoinColumn(name="ICONKEY")
	private Tblob tblob;

	public Tsystemstate() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getCssstyle() {
		return this.cssstyle;
	}

	public void setCssstyle(String cssstyle) {
		this.cssstyle = cssstyle;
	}

	public int getEntityflag() {
		return this.entityflag;
	}

	public void setEntityflag(int entityflag) {
		this.entityflag = entityflag;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getSortorder() {
		return this.sortorder;
	}

	public void setSortorder(int sortorder) {
		this.sortorder = sortorder;
	}

	public int getStateflag() {
		return this.stateflag;
	}

	public void setStateflag(int stateflag) {
		this.stateflag = stateflag;
	}

	public String getSymbol() {
		return this.symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public List<Taccount> getTaccounts() {
		return this.taccounts;
	}

	public void setTaccounts(List<Taccount> taccounts) {
		this.taccounts = taccounts;
	}

	public Taccount addTaccount(Taccount taccount) {
		getTaccounts().add(taccount);
		taccount.setTsystemstate(this);

		return taccount;
	}

	public Taccount removeTaccount(Taccount taccount) {
		getTaccounts().remove(taccount);
		taccount.setTsystemstate(null);

		return taccount;
	}

	public List<Tproject> getTprojects() {
		return this.tprojects;
	}

	public void setTprojects(List<Tproject> tprojects) {
		this.tprojects = tprojects;
	}

	public Tproject addTproject(Tproject tproject) {
		getTprojects().add(tproject);
		tproject.setTsystemstate(this);

		return tproject;
	}

	public Tproject removeTproject(Tproject tproject) {
		getTprojects().remove(tproject);
		tproject.setTsystemstate(null);

		return tproject;
	}

	public List<Trelease> getTreleases() {
		return this.treleases;
	}

	public void setTreleases(List<Trelease> treleases) {
		this.treleases = treleases;
	}

	public Trelease addTreleas(Trelease treleas) {
		getTreleases().add(treleas);
		treleas.setTsystemstate(this);

		return treleas;
	}

	public Trelease removeTreleas(Trelease treleas) {
		getTreleases().remove(treleas);
		treleas.setTsystemstate(null);

		return treleas;
	}

	public Tblob getTblob() {
		return this.tblob;
	}

	public void setTblob(Tblob tblob) {
		this.tblob = tblob;
	}

}
