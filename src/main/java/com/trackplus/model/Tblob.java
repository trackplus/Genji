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

import java.util.List;


/**
 * The persistent class for the TBLOB database table.
 * 
 */
@Entity
@Table(name="TBLOB")
@TableGenerator(name="TBLOB_GEN", table="ID_TABLE",
pkColumnName="ID_TABLE_ID",
pkColumnValue=BaseEntity.TABLE_BLOB, allocationSize = 10)

public class Tblob extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TBLOB_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Lob
	private byte[] blobvalue;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Taction
	@OneToMany(mappedBy="tblob")
	private List<Taction> tactions;

	//bi-directional many-to-one association to Tcategory
	@OneToMany(mappedBy="tblob")
	private List<Tcategory> tcategories;

	//bi-directional many-to-one association to Tlinktype
	@OneToMany(mappedBy="tblob1")
	private List<Tlinktype> tlinktypes1;

	//bi-directional many-to-one association to Tlinktype
	@OneToMany(mappedBy="tblob2")
	private List<Tlinktype> tlinktypes2;

	//bi-directional many-to-one association to Toption
	@OneToMany(mappedBy="tblob")
	private List<Toption> toptions;

	//bi-directional many-to-one association to Tperson
	@OneToMany(mappedBy="tblob")
	private List<Tperson> tpersons;

	//bi-directional many-to-one association to Tpriority
	@OneToMany(mappedBy="tblob")
	private List<Tpriority> tpriorities;

	//bi-directional many-to-one association to Tseverity
	@OneToMany(mappedBy="tblob")
	private List<Tseverity> tseverities;

	//bi-directional many-to-one association to Tstate
	@OneToMany(mappedBy="tblob")
	private List<Tstate> tstates;

	//bi-directional many-to-one association to Tsystemstate
	@OneToMany(mappedBy="tblob")
	private List<Tsystemstate> tsystemstates;

	public Tblob() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public byte[] getBlobvalue() {
		return this.blobvalue;
	}

	public void setBlobvalue(byte[] blobvalue) {
		this.blobvalue = blobvalue;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public List<Taction> getTactions() {
		return this.tactions;
	}

	public void setTactions(List<Taction> tactions) {
		this.tactions = tactions;
	}

	public Taction addTaction(Taction taction) {
		getTactions().add(taction);
		taction.setTblob(this);

		return taction;
	}

	public Taction removeTaction(Taction taction) {
		getTactions().remove(taction);
		taction.setTblob(null);

		return taction;
	}

	public List<Tcategory> getTcategories() {
		return this.tcategories;
	}

	public void setTcategories(List<Tcategory> tcategories) {
		this.tcategories = tcategories;
	}

	public Tcategory addTcategory(Tcategory tcategory) {
		getTcategories().add(tcategory);
		tcategory.setTblob(this);

		return tcategory;
	}

	public Tcategory removeTcategory(Tcategory tcategory) {
		getTcategories().remove(tcategory);
		tcategory.setTblob(null);

		return tcategory;
	}

	public List<Tlinktype> getTlinktypes1() {
		return this.tlinktypes1;
	}

	public void setTlinktypes1(List<Tlinktype> tlinktypes1) {
		this.tlinktypes1 = tlinktypes1;
	}

	public Tlinktype addTlinktypes1(Tlinktype tlinktypes1) {
		getTlinktypes1().add(tlinktypes1);
		tlinktypes1.setTblob1(this);

		return tlinktypes1;
	}

	public Tlinktype removeTlinktypes1(Tlinktype tlinktypes1) {
		getTlinktypes1().remove(tlinktypes1);
		tlinktypes1.setTblob1(null);

		return tlinktypes1;
	}

	public List<Tlinktype> getTlinktypes2() {
		return this.tlinktypes2;
	}

	public void setTlinktypes2(List<Tlinktype> tlinktypes2) {
		this.tlinktypes2 = tlinktypes2;
	}

	public Tlinktype addTlinktypes2(Tlinktype tlinktypes2) {
		getTlinktypes2().add(tlinktypes2);
		tlinktypes2.setTblob2(this);

		return tlinktypes2;
	}

	public Tlinktype removeTlinktypes2(Tlinktype tlinktypes2) {
		getTlinktypes2().remove(tlinktypes2);
		tlinktypes2.setTblob2(null);

		return tlinktypes2;
	}

	public List<Toption> getToptions() {
		return this.toptions;
	}

	public void setToptions(List<Toption> toptions) {
		this.toptions = toptions;
	}

	public Toption addToption(Toption toption) {
		getToptions().add(toption);
		toption.setTblob(this);

		return toption;
	}

	public Toption removeToption(Toption toption) {
		getToptions().remove(toption);
		toption.setTblob(null);

		return toption;
	}

	public List<Tperson> getTpersons() {
		return this.tpersons;
	}

	public void setTpersons(List<Tperson> tpersons) {
		this.tpersons = tpersons;
	}

	public Tperson addTperson(Tperson tperson) {
		getTpersons().add(tperson);
		tperson.setTblob(this);

		return tperson;
	}

	public Tperson removeTperson(Tperson tperson) {
		getTpersons().remove(tperson);
		tperson.setTblob(null);

		return tperson;
	}

	public List<Tpriority> getTpriorities() {
		return this.tpriorities;
	}

	public void setTpriorities(List<Tpriority> tpriorities) {
		this.tpriorities = tpriorities;
	}

	public Tpriority addTpriority(Tpriority tpriority) {
		getTpriorities().add(tpriority);
		tpriority.setTblob(this);

		return tpriority;
	}

	public Tpriority removeTpriority(Tpriority tpriority) {
		getTpriorities().remove(tpriority);
		tpriority.setTblob(null);

		return tpriority;
	}

	public List<Tseverity> getTseverities() {
		return this.tseverities;
	}

	public void setTseverities(List<Tseverity> tseverities) {
		this.tseverities = tseverities;
	}

	public Tseverity addTseverity(Tseverity tseverity) {
		getTseverities().add(tseverity);
		tseverity.setTblob(this);

		return tseverity;
	}

	public Tseverity removeTseverity(Tseverity tseverity) {
		getTseverities().remove(tseverity);
		tseverity.setTblob(null);

		return tseverity;
	}

	public List<Tstate> getTstates() {
		return this.tstates;
	}

	public void setTstates(List<Tstate> tstates) {
		this.tstates = tstates;
	}

	public Tstate addTstate(Tstate tstate) {
		getTstates().add(tstate);
		tstate.setTblob(this);

		return tstate;
	}

	public Tstate removeTstate(Tstate tstate) {
		getTstates().remove(tstate);
		tstate.setTblob(null);

		return tstate;
	}

	public List<Tsystemstate> getTsystemstates() {
		return this.tsystemstates;
	}

	public void setTsystemstates(List<Tsystemstate> tsystemstates) {
		this.tsystemstates = tsystemstates;
	}

	public Tsystemstate addTsystemstate(Tsystemstate tsystemstate) {
		getTsystemstates().add(tsystemstate);
		tsystemstate.setTblob(this);

		return tsystemstate;
	}

	public Tsystemstate removeTsystemstate(Tsystemstate tsystemstate) {
		getTsystemstates().remove(tsystemstate);
		tsystemstate.setTblob(null);

		return tsystemstate;
	}

}
