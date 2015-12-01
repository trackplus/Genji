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


/**
 * The persistent class for the TISSUEATTRIBUTEVALUE database table.
 * 
 */
@Entity
@Table(name="TISSUEATTRIBUTEVALUE")
@TableGenerator(name="TISSUEATTRIBUTEVALUE_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_ISSUEATTRIBUTEVALUE, allocationSize = 10)
public class Tissueattributevalue extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TISSUEATTRIBUTEVALUE_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=255)
	private String charvalue;

	private int deleted;

	@Column(length=255)
	private String displayvalue;

	@Lob
	private String longtextvalue;

	private int numericvalue;

	@Column(nullable=false)
	private Timestamp timestampvalue;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="PERSON")
	private Tperson tperson;

	//bi-directional many-to-one association to Tworkitem
	@ManyToOne
	@JoinColumn(name="ISSUE", nullable=false)
	private Tworkitem tworkitem;


	public Tissueattributevalue() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getCharvalue() {
		return this.charvalue;
	}

	public void setCharvalue(String charvalue) {
		this.charvalue = charvalue;
	}

	public int getDeleted() {
		return this.deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public String getDisplayvalue() {
		return this.displayvalue;
	}

	public void setDisplayvalue(String displayvalue) {
		this.displayvalue = displayvalue;
	}

	public String getLongtextvalue() {
		return this.longtextvalue;
	}

	public void setLongtextvalue(String longtextvalue) {
		this.longtextvalue = longtextvalue;
	}

	public int getNumericvalue() {
		return this.numericvalue;
	}

	public void setNumericvalue(int numericvalue) {
		this.numericvalue = numericvalue;
	}

	public Timestamp getTimestampvalue() {
		return this.timestampvalue;
	}

	public void setTimestampvalue(Timestamp timestampvalue) {
		this.timestampvalue = timestampvalue;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tperson getTperson() {
		return this.tperson;
	}

	public void setTperson(Tperson tperson) {
		this.tperson = tperson;
	}

	public Tworkitem getTworkitem() {
		return this.tworkitem;
	}

	public void setTworkitem(Tworkitem tworkitem) {
		this.tworkitem = tworkitem;
	}

}
