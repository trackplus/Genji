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
 * The persistent class for the TCOSTCENTER database table.
 * 
 */
@Entity
@Table(name="TCOSTCENTER")
@TableGenerator(name="TCOSTCENTER_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_COSTCENTER, allocationSize = 10)

public class Tcostcenter extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TCOSTCENTER_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=80)
	private String costcentername;

	@Column(nullable=false, length=30)
	private String costcenternumber;

	@Lob
	private String moreprops;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Taccount
	@OneToMany(mappedBy="tcostcenter")
	private List<Taccount> taccounts;

	//bi-directional many-to-one association to Tdepartment
	@OneToMany(mappedBy="tcostcenter")
	private List<Tdepartment> tdepartments;

	public Tcostcenter() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getCostcentername() {
		return this.costcentername;
	}

	public void setCostcentername(String costcentername) {
		this.costcentername = costcentername;
	}

	public String getCostcenternumber() {
		return this.costcenternumber;
	}

	public void setCostcenternumber(String costcenternumber) {
		this.costcenternumber = costcenternumber;
	}

	public String getMoreprops() {
		return this.moreprops;
	}

	public void setMoreprops(String moreprops) {
		this.moreprops = moreprops;
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
		taccount.setTcostcenter(this);

		return taccount;
	}

	public Taccount removeTaccount(Taccount taccount) {
		getTaccounts().remove(taccount);
		taccount.setTcostcenter(null);

		return taccount;
	}

	public List<Tdepartment> getTdepartments() {
		return this.tdepartments;
	}

	public void setTdepartments(List<Tdepartment> tdepartments) {
		this.tdepartments = tdepartments;
	}

	public Tdepartment addTdepartment(Tdepartment tdepartment) {
		getTdepartments().add(tdepartment);
		tdepartment.setTcostcenter(this);

		return tdepartment;
	}

	public Tdepartment removeTdepartment(Tdepartment tdepartment) {
		getTdepartments().remove(tdepartment);
		tdepartment.setTcostcenter(null);

		return tdepartment;
	}

}
