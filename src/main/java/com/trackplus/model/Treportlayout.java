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


/**
 * The persistent class for the TREPORTLAYOUT database table.
 * 
 */
@Entity
@Table(name="TREPORTLAYOUT")
@TableGenerator(name="TREPORTLAYOUT_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_REPORTLAYOUT, allocationSize = 10)
public class Treportlayout extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TREPORTLAYOUT_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=1)
	private String expanding;

	private int fieldtype;

	@Column(nullable=false)
	private int fposition;

	@Column(length=1)
	private String fsortdir;

	private int fsortorder;

	@Column(nullable=false)
	private int fwidth;

	private int layoutkey;

	private int queryid;

	private int querytype;

	@Column(nullable=false)
	private int reportfield;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tprojecttype
	@ManyToOne
	@JoinColumn(name="PROJECTTYPE")
	private Tprojecttype tprojecttype;

	//bi-directional many-to-one association to Tproject
	@ManyToOne
	@JoinColumn(name="PROJECT")
	private Tproject tproject;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="PERSON")
	private Tperson tperson;

	public Treportlayout() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getExpanding() {
		return this.expanding;
	}

	public void setExpanding(String expanding) {
		this.expanding = expanding;
	}

	public int getFieldtype() {
		return this.fieldtype;
	}

	public void setFieldtype(int fieldtype) {
		this.fieldtype = fieldtype;
	}

	public int getFposition() {
		return this.fposition;
	}

	public void setFposition(int fposition) {
		this.fposition = fposition;
	}

	public String getFsortdir() {
		return this.fsortdir;
	}

	public void setFsortdir(String fsortdir) {
		this.fsortdir = fsortdir;
	}

	public int getFsortorder() {
		return this.fsortorder;
	}

	public void setFsortorder(int fsortorder) {
		this.fsortorder = fsortorder;
	}

	public int getFwidth() {
		return this.fwidth;
	}

	public void setFwidth(int fwidth) {
		this.fwidth = fwidth;
	}

	public int getLayoutkey() {
		return this.layoutkey;
	}

	public void setLayoutkey(int layoutkey) {
		this.layoutkey = layoutkey;
	}

	public int getQueryid() {
		return this.queryid;
	}

	public void setQueryid(int queryid) {
		this.queryid = queryid;
	}

	public int getQuerytype() {
		return this.querytype;
	}

	public void setQuerytype(int querytype) {
		this.querytype = querytype;
	}

	public int getReportfield() {
		return this.reportfield;
	}

	public void setReportfield(int reportfield) {
		this.reportfield = reportfield;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tprojecttype getTprojecttype() {
		return this.tprojecttype;
	}

	public void setTprojecttype(Tprojecttype tprojecttype) {
		this.tprojecttype = tprojecttype;
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

}
