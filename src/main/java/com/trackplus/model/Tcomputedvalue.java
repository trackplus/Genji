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
 * The persistent class for the TCOMPUTEDVALUES database table.
 * 
 */
@Entity
@Table(name="TCOMPUTEDVALUES")
@TableGenerator(name="TCOMPUTEDVALUES_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_COMPUTEDVALUES, allocationSize = 10)


public class Tcomputedvalue extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TCOMPUTEDVALUES_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int pkey;

	private double computedvalue;

	@Column(nullable=false)
	private int computedvaluetype;

	@Column(nullable=false)
	private int efforttype;

	private int measurementunit;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tworkitem
	@ManyToOne
	@JoinColumn(name="WORKITEMKEY", nullable=false)
	private Tworkitem tworkitem;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="PERSON")
	private Tperson tperson;

	public Tcomputedvalue() {
	}

	public int getObjectid() {
		return this.pkey;
	}
	
	public void setObjectid(int key) {
		this.pkey = key;
	}

	public int getPkey() {
		return this.pkey;
	}

	public void setPkey(int pkey) {
		this.pkey = pkey;
	}

	public double getComputedvalue() {
		return this.computedvalue;
	}

	public void setComputedvalue(double computedvalue) {
		this.computedvalue = computedvalue;
	}

	public int getComputedvaluetype() {
		return this.computedvaluetype;
	}

	public void setComputedvaluetype(int computedvaluetype) {
		this.computedvaluetype = computedvaluetype;
	}

	public int getEfforttype() {
		return this.efforttype;
	}

	public void setEfforttype(int efforttype) {
		this.efforttype = efforttype;
	}

	public int getMeasurementunit() {
		return this.measurementunit;
	}

	public void setMeasurementunit(int measurementunit) {
		this.measurementunit = measurementunit;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tworkitem getTworkitem() {
		return this.tworkitem;
	}

	public void setTworkitem(Tworkitem tworkitem) {
		this.tworkitem = tworkitem;
	}

	public Tperson getTperson() {
		return this.tperson;
	}

	public void setTperson(Tperson tperson) {
		this.tperson = tperson;
	}

}
