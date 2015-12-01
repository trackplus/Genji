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
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


/**
 * The persistent class for the TACCOUNT database table.
 * 
 */
@Entity
@Table(name="TACCOUNT")
@TableGenerator(name="TACCOUNT_GEN", table="ID_TABLE",
pkColumnName="ID_TABLE_ID",
pkColumnValue=BaseEntity.TABLE_ACCOUNT, allocationSize = 1)
public class Taccount extends BaseEntity implements Serializable {
    
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator="TACCOUNT_GEN", strategy = TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=80)
	private String accountname;

	@Column(nullable=false, length=30)
	private String accountnumber;

	@Column(length=255)
	private String description;

	@Lob
	private String moreprops;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tsystemstate
	@ManyToOne
	@JoinColumn(name="STATUS")
	private Tsystemstate tsystemstate;

	//bi-directional many-to-one association to Tcostcenter
	@ManyToOne
	@JoinColumn(name="COSTCENTER")
	private Tcostcenter tcostcenter;

	//bi-directional many-to-one association to Tcost
	@OneToMany(mappedBy="taccount")
	private List<Tcost> tcosts;

	//bi-directional many-to-one association to Tprojectaccount
	@OneToMany(mappedBy="taccount")
	private List<Tprojectaccount> tprojectaccounts;

	public Taccount() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getAccountname() {
		return this.accountname;
	}

	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}

	public String getAccountnumber() {
		return this.accountnumber;
	}

	public void setAccountnumber(String accountnumber) {
		this.accountnumber = accountnumber;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Tsystemstate getTsystemstate() {
		return this.tsystemstate;
	}

	public void setTsystemstate(Tsystemstate tsystemstate) {
		this.tsystemstate = tsystemstate;
	}

	public Tcostcenter getTcostcenter() {
		return this.tcostcenter;
	}

	public void setTcostcenter(Tcostcenter tcostcenter) {
		this.tcostcenter = tcostcenter;
	}

	public List<Tcost> getTcosts() {
		return this.tcosts;
	}

	public void setTcosts(List<Tcost> tcosts) {
		this.tcosts = tcosts;
	}

	public Tcost addTcost(Tcost tcost) {
		getTcosts().add(tcost);
		tcost.setTaccount(this);

		return tcost;
	}

	public Tcost removeTcost(Tcost tcost) {
		getTcosts().remove(tcost);
		tcost.setTaccount(null);

		return tcost;
	}

	public List<Tprojectaccount> getTprojectaccounts() {
		return this.tprojectaccounts;
	}

	public void setTprojectaccounts(List<Tprojectaccount> tprojectaccounts) {
		this.tprojectaccounts = tprojectaccounts;
	}

	public Tprojectaccount addTprojectaccount(Tprojectaccount tprojectaccount) {
		getTprojectaccounts().add(tprojectaccount);
		tprojectaccount.setTaccount(this);

		return tprojectaccount;
	}

	public Tprojectaccount removeTprojectaccount(Tprojectaccount tprojectaccount) {
		getTprojectaccounts().remove(tprojectaccount);
		tprojectaccount.setTaccount(null);

		return tprojectaccount;
	}

}
