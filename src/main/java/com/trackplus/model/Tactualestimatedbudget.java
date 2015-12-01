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

import static javax.persistence.GenerationType.TABLE;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;


/**
 * The persistent class for the TACTUALESTIMATEDBUDGET database table.
 * 
 */
@Entity
@Table(name="TACTUALESTIMATEDBUDGET")
@TableGenerator(name="TACTESTBUDGET_GEN", table="ID_TABLE",
pkColumnName="ID_TABLE_ID",
pkColumnValue=BaseEntity.TABLE_ACTUALESTIMATEDBUDGET, allocationSize = 10)
public class Tactualestimatedbudget extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator="TACTESTBUDGET_GEN", strategy = TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	private int efforttype;

	private double effortvalue;

	private double estimatedcost;

	private double estimatedhours;

	@Column(nullable=false)
	private Timestamp lastedit;

	private int timeunit;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tworkitem
	@ManyToOne
	@JoinColumn(name="WORKITEMKEY", nullable=false)
	private Tworkitem tworkitem;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="CHANGEDBY")
	private Tperson tperson;

	public Tactualestimatedbudget() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public int getEfforttype() {
		return this.efforttype;
	}

	public void setEfforttype(int efforttype) {
		this.efforttype = efforttype;
	}

	public double getEffortvalue() {
		return this.effortvalue;
	}

	public void setEffortvalue(double effortvalue) {
		this.effortvalue = effortvalue;
	}

	public double getEstimatedcost() {
		return this.estimatedcost;
	}

	public void setEstimatedcost(double estimatedcost) {
		this.estimatedcost = estimatedcost;
	}

	public double getEstimatedhours() {
		return this.estimatedhours;
	}

	public void setEstimatedhours(double estimatedhours) {
		this.estimatedhours = estimatedhours;
	}

	public Timestamp getLastedit() {
		return this.lastedit;
	}

	public void setLastedit(Timestamp lastedit) {
		this.lastedit = lastedit;
	}

	public int getTimeunit() {
		return this.timeunit;
	}

	public void setTimeunit(int timeunit) {
		this.timeunit = timeunit;
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
