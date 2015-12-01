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

import java.sql.Timestamp;
import java.util.Date;


/**
 * The persistent class for the TATTRIBUTEVALUE database table.
 * 
 */
@Entity
@Table(name="TATTRIBUTEVALUE")
@TableGenerator(name="TATTRIBUTEVALUE_GEN", table="ID_TABLE",
pkColumnName="ID_TABLE_ID",
pkColumnValue=BaseEntity.TABLE_ATTRIBUTEVALUE, allocationSize = 10)

public class Tattributevalue extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TATTRIBUTEVALUE_GEN", strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=1)
	private String charactervalue;

	private int customoptionid;

	@Temporal(TemporalType.TIMESTAMP)
	private Date datevalue;

	private double doublevalue;

	private int integervalue;

	@Column(nullable=false)
	private Timestamp lastedit;

	@Lob
	private String longtextvalue;

	private int parametercode;

	private int systemoptionid;

	private int systemoptiontype;

	@Column(length=255)
	private String textvalue;

	@Column(length=36)
	private String tpuuid;

	private int validvalue;

	//bi-directional many-to-one association to Tfield
	@ManyToOne
	@JoinColumn(name="FIELDKEY", nullable=false)
	private Tfield tfield;

	//bi-directional many-to-one association to Tworkitem
	@ManyToOne
	@JoinColumn(name="WORKITEM", nullable=false)
	private Tworkitem tworkitem;

	public Tattributevalue() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getCharactervalue() {
		return this.charactervalue;
	}

	public void setCharactervalue(String charactervalue) {
		this.charactervalue = charactervalue;
	}

	public int getCustomoptionid() {
		return this.customoptionid;
	}

	public void setCustomoptionid(int customoptionid) {
		this.customoptionid = customoptionid;
	}

	public Date getDatevalue() {
		return this.datevalue;
	}

	public void setDatevalue(Date datevalue) {
		this.datevalue = datevalue;
	}

	public double getDoublevalue() {
		return this.doublevalue;
	}

	public void setDoublevalue(double doublevalue) {
		this.doublevalue = doublevalue;
	}

	public int getIntegervalue() {
		return this.integervalue;
	}

	public void setIntegervalue(int integervalue) {
		this.integervalue = integervalue;
	}

	public Timestamp getLastedit() {
		return this.lastedit;
	}

	public void setLastedit(Timestamp lastedit) {
		this.lastedit = lastedit;
	}

	public String getLongtextvalue() {
		return this.longtextvalue;
	}

	public void setLongtextvalue(String longtextvalue) {
		this.longtextvalue = longtextvalue;
	}

	public int getParametercode() {
		return this.parametercode;
	}

	public void setParametercode(int parametercode) {
		this.parametercode = parametercode;
	}

	public int getSystemoptionid() {
		return this.systemoptionid;
	}

	public void setSystemoptionid(int systemoptionid) {
		this.systemoptionid = systemoptionid;
	}

	public int getSystemoptiontype() {
		return this.systemoptiontype;
	}

	public void setSystemoptiontype(int systemoptiontype) {
		this.systemoptiontype = systemoptiontype;
	}

	public String getTextvalue() {
		return this.textvalue;
	}

	public void setTextvalue(String textvalue) {
		this.textvalue = textvalue;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public int getValidvalue() {
		return this.validvalue;
	}

	public void setValidvalue(int validvalue) {
		this.validvalue = validvalue;
	}

	public Tfield getTfield() {
		return this.tfield;
	}

	public void setTfield(Tfield tfield) {
		this.tfield = tfield;
	}

	public Tworkitem getTworkitem() {
		return this.tworkitem;
	}

	public void setTworkitem(Tworkitem tworkitem) {
		this.tworkitem = tworkitem;
	}

}
