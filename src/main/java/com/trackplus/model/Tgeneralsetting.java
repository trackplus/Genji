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
import java.util.Date;


/**
 * The persistent class for the TGENERALSETTINGS database table.
 * 
 */
@Entity
@Table(name="TGENERALSETTINGS")
@TableGenerator(name="TGENERALSETTINGS_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_GENERALSETTINGS, allocationSize = 10)
public class Tgeneralsetting extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TGENERALSETTINGS_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=1)
	private String charactervalue;

	@Temporal(TemporalType.TIMESTAMP)
	private Date datevalue;

	private double doublevalue;

	private int integervalue;

	private int parametercode;

	@Column(length=255)
	private String textvalue;

	@Column(length=36)
	private String tpuuid;

	private int validvalue;

	//bi-directional many-to-one association to Tfieldconfig
	@ManyToOne
	@JoinColumn(name="CONFIG", nullable=false)
	private Tfieldconfig tfieldconfig;

	public Tgeneralsetting() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getCharactervalue() {
		return this.charactervalue;
	}

	public void setCharactervalue(String charactervalue) {
		this.charactervalue = charactervalue;
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

	public int getParametercode() {
		return this.parametercode;
	}

	public void setParametercode(int parametercode) {
		this.parametercode = parametercode;
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

	public Tfieldconfig getTfieldconfig() {
		return this.tfieldconfig;
	}

	public void setTfieldconfig(Tfieldconfig tfieldconfig) {
		this.tfieldconfig = tfieldconfig;
	}

}
