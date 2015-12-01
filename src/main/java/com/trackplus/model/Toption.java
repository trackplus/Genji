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
 * The persistent class for the TOPTION database table.
 * 
 */
@Entity
@Table(name="TOPTION")
@TableGenerator(name="TOPTION_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_OPTION, allocationSize = 10)
public class Toption extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TOPTION_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=255)
	private String cssstyle;

	@Column(length=1)
	private String deleted;

	@Column(length=1)
	private String iconchanged;

	@Column(nullable=false, length=1)
	private String isdefault;

	@Column(name="`LABEL`", nullable=false, length=255)
	private String label;

	private int parentoption;

	private int sortorder;

	@Column(length=255)
	private String symbol;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tconfigoptionsrole
	@OneToMany(mappedBy="toption")
	private List<Tconfigoptionsrole> tconfigoptionsroles;

	//bi-directional many-to-one association to Tfieldchange
	@OneToMany(mappedBy="toption1")
	private List<Tfieldchange> tfieldchanges1;

	//bi-directional many-to-one association to Tfieldchange
	@OneToMany(mappedBy="toption2")
	private List<Tfieldchange> tfieldchanges2;

	//bi-directional many-to-one association to Tlist
	@ManyToOne
	@JoinColumn(name="LIST", nullable=false)
	private Tlist tlist;

	//bi-directional many-to-one association to Tblob
	@ManyToOne
	@JoinColumn(name="ICONKEY")
	private Tblob tblob;

	public Toption() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getCssstyle() {
		return this.cssstyle;
	}

	public void setCssstyle(String cssstyle) {
		this.cssstyle = cssstyle;
	}

	public String getDeleted() {
		return this.deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public String getIconchanged() {
		return this.iconchanged;
	}

	public void setIconchanged(String iconchanged) {
		this.iconchanged = iconchanged;
	}

	public String getIsdefault() {
		return this.isdefault;
	}

	public void setIsdefault(String isdefault) {
		this.isdefault = isdefault;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getParentoption() {
		return this.parentoption;
	}

	public void setParentoption(int parentoption) {
		this.parentoption = parentoption;
	}

	public int getSortorder() {
		return this.sortorder;
	}

	public void setSortorder(int sortorder) {
		this.sortorder = sortorder;
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

	public List<Tconfigoptionsrole> getTconfigoptionsroles() {
		return this.tconfigoptionsroles;
	}

	public void setTconfigoptionsroles(List<Tconfigoptionsrole> tconfigoptionsroles) {
		this.tconfigoptionsroles = tconfigoptionsroles;
	}

	public Tconfigoptionsrole addTconfigoptionsrole(Tconfigoptionsrole tconfigoptionsrole) {
		getTconfigoptionsroles().add(tconfigoptionsrole);
		tconfigoptionsrole.setToption(this);

		return tconfigoptionsrole;
	}

	public Tconfigoptionsrole removeTconfigoptionsrole(Tconfigoptionsrole tconfigoptionsrole) {
		getTconfigoptionsroles().remove(tconfigoptionsrole);
		tconfigoptionsrole.setToption(null);

		return tconfigoptionsrole;
	}

	public List<Tfieldchange> getTfieldchanges1() {
		return this.tfieldchanges1;
	}

	public void setTfieldchanges1(List<Tfieldchange> tfieldchanges1) {
		this.tfieldchanges1 = tfieldchanges1;
	}

	public Tfieldchange addTfieldchanges1(Tfieldchange tfieldchanges1) {
		getTfieldchanges1().add(tfieldchanges1);
		tfieldchanges1.setToption1(this);

		return tfieldchanges1;
	}

	public Tfieldchange removeTfieldchanges1(Tfieldchange tfieldchanges1) {
		getTfieldchanges1().remove(tfieldchanges1);
		tfieldchanges1.setToption1(null);

		return tfieldchanges1;
	}

	public List<Tfieldchange> getTfieldchanges2() {
		return this.tfieldchanges2;
	}

	public void setTfieldchanges2(List<Tfieldchange> tfieldchanges2) {
		this.tfieldchanges2 = tfieldchanges2;
	}

	public Tfieldchange addTfieldchanges2(Tfieldchange tfieldchanges2) {
		getTfieldchanges2().add(tfieldchanges2);
		tfieldchanges2.setToption2(this);

		return tfieldchanges2;
	}

	public Tfieldchange removeTfieldchanges2(Tfieldchange tfieldchanges2) {
		getTfieldchanges2().remove(tfieldchanges2);
		tfieldchanges2.setToption2(null);

		return tfieldchanges2;
	}

	public Tlist getTlist() {
		return this.tlist;
	}

	public void setTlist(Tlist tlist) {
		this.tlist = tlist;
	}

	public Tblob getTblob() {
		return this.tblob;
	}

	public void setTblob(Tblob tblob) {
		this.tblob = tblob;
	}

}
