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

import java.sql.Timestamp;
import java.util.Date;


/**
 * The persistent class for the TBASELINE database table.
 * 
 */
@Entity
@Table(name="TBASELINE")
@TableGenerator(name="TBASELINE_GEN", table="ID_TABLE",
pkColumnName="ID_TABLE_ID",
pkColumnValue=BaseEntity.TABLE_BASELINE, allocationSize = 10)

public class Tbaseline extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TBASELINE_GEN", strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int blkey;

	@Temporal(TemporalType.TIMESTAMP)
	private Date enddate;

	@Column(nullable=false)
	private Timestamp lastedit;

	@Lob
	private String reasonforchange;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startdate;

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

	public Tbaseline() {
	}

	public int getObjectid() {
		return this.blkey;
	}
	
	public void setObjectid(int key) {
		this.blkey = key;
	}

	public int getBlkey() {
		return this.blkey;
	}

	public void setBlkey(int blkey) {
		this.blkey = blkey;
	}

	public Date getEnddate() {
		return this.enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public Timestamp getLastedit() {
		return this.lastedit;
	}

	public void setLastedit(Timestamp lastedit) {
		this.lastedit = lastedit;
	}

	public String getReasonforchange() {
		return this.reasonforchange;
	}

	public void setReasonforchange(String reasonforchange) {
		this.reasonforchange = reasonforchange;
	}

	public Date getStartdate() {
		return this.startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
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
