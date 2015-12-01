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
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TRELEASE database table.
 * 
 */
@Entity
@Table(name="TRELEASE")
@TableGenerator(name="TRELEASE_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_RELEASE, allocationSize = 10)
public class Trelease extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TRELEASE_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int pkey;

	@Column(length=255)
	private String description;

	@Temporal(TemporalType.TIMESTAMP)
	private Date duedate;

	@Column(name="`LABEL`", length=25)
	private String label;

	@Lob
	private String moreprops;

	private int sortorder;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tproject
	@ManyToOne
	@JoinColumn(name="PROJKEY", nullable=false)
	private Tproject tproject;

	//bi-directional many-to-one association to Tsystemstate
	@ManyToOne
	@JoinColumn(name="STATUS")
	private Tsystemstate tsystemstate;

	//bi-directional many-to-one association to Trelease
	@ManyToOne
	@JoinColumn(name="PARENT")
	private Trelease trelease;

	//bi-directional many-to-one association to Trelease
	@OneToMany(mappedBy="trelease")
	private List<Trelease> treleases;

	//bi-directional many-to-one association to Tworkitem
	@OneToMany(mappedBy="trelease1")
	private List<Tworkitem> tworkitems1;

	//bi-directional many-to-one association to Tworkitem
	@OneToMany(mappedBy="trelease2")
	private List<Tworkitem> tworkitems2;

	public Trelease() {
	}

	@Override
	public int getObjectid() {
		return this.pkey;
	}
	
	@Override
	public void setObjectid(int key) {
		this.pkey = key;
	}

	public int getPkey() {
		return this.pkey;
	}

	public void setPkey(int pkey) {
		this.pkey = pkey;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDuedate() {
		return this.duedate;
	}

	public void setDuedate(Date duedate) {
		this.duedate = duedate;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getMoreprops() {
		return this.moreprops;
	}

	public void setMoreprops(String moreprops) {
		this.moreprops = moreprops;
	}

	public int getSortorder() {
		return this.sortorder;
	}

	public void setSortorder(int sortorder) {
		this.sortorder = sortorder;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tproject getTproject() {
		return this.tproject;
	}

	public void setTproject(Tproject tproject) {
		this.tproject = tproject;
	}

	public Tsystemstate getTsystemstate() {
		return this.tsystemstate;
	}

	public void setTsystemstate(Tsystemstate tsystemstate) {
		this.tsystemstate = tsystemstate;
	}

	public Trelease getTrelease() {
		return this.trelease;
	}

	public void setTrelease(Trelease trelease) {
		this.trelease = trelease;
	}

	public List<Trelease> getTreleases() {
		return this.treleases;
	}

	public void setTreleases(List<Trelease> treleases) {
		this.treleases = treleases;
	}

	public Trelease addTreleas(Trelease treleas) {
		getTreleases().add(treleas);
		treleas.setTrelease(this);

		return treleas;
	}

	public Trelease removeTreleas(Trelease treleas) {
		getTreleases().remove(treleas);
		treleas.setTrelease(null);

		return treleas;
	}

	public List<Tworkitem> getTworkitems1() {
		return this.tworkitems1;
	}

	public void setTworkitems1(List<Tworkitem> tworkitems1) {
		this.tworkitems1 = tworkitems1;
	}

	public Tworkitem addTworkitems1(Tworkitem tworkitems1) {
		getTworkitems1().add(tworkitems1);
		tworkitems1.setTrelease1(this);

		return tworkitems1;
	}

	public Tworkitem removeTworkitems1(Tworkitem tworkitems1) {
		getTworkitems1().remove(tworkitems1);
		tworkitems1.setTrelease1(null);

		return tworkitems1;
	}

	public List<Tworkitem> getTworkitems2() {
		return this.tworkitems2;
	}

	public void setTworkitems2(List<Tworkitem> tworkitems2) {
		this.tworkitems2 = tworkitems2;
	}

	public Tworkitem addTworkitems2(Tworkitem tworkitems2) {
		getTworkitems2().add(tworkitems2);
		tworkitems2.setTrelease2(this);

		return tworkitems2;
	}

	public Tworkitem removeTworkitems2(Tworkitem tworkitems2) {
		getTworkitems2().remove(tworkitems2);
		tworkitems2.setTrelease2(null);

		return tworkitems2;
	}

}
