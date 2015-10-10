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


/**
 * The persistent class for the TSUMMARYMAIL database table.
 * 
 */
@Entity
@Table(name="TSUMMARYMAIL")
@TableGenerator(name="TSUMMARYMAIL_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_SUMMARYMAIL, allocationSize = 10)
public class Tsummarymail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TSUMMARYMAIL_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=255)
	private String fromaddress;

	@Column(nullable=false)
	private Timestamp lastedit;

	@Column(length=255)
	private String mailsubject;

	@Column(length=36)
	private String tpuuid;

	@Column(length=255)
	private String workitemlink;

	//bi-directional many-to-one association to Tworkitem
	@ManyToOne
	@JoinColumn(name="WORKITEM", nullable=false)
	private Tworkitem tworkitem;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="PERSONFROM", nullable=false)
	private Tperson tperson1;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="PERSONTO", nullable=false)
	private Tperson tperson2;

	public Tsummarymail() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getFromaddress() {
		return this.fromaddress;
	}

	public void setFromaddress(String fromaddress) {
		this.fromaddress = fromaddress;
	}

	public Timestamp getLastedit() {
		return this.lastedit;
	}

	public void setLastedit(Timestamp lastedit) {
		this.lastedit = lastedit;
	}

	public String getMailsubject() {
		return this.mailsubject;
	}

	public void setMailsubject(String mailsubject) {
		this.mailsubject = mailsubject;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public String getWorkitemlink() {
		return this.workitemlink;
	}

	public void setWorkitemlink(String workitemlink) {
		this.workitemlink = workitemlink;
	}

	public Tworkitem getTworkitem() {
		return this.tworkitem;
	}

	public void setTworkitem(Tworkitem tworkitem) {
		this.tworkitem = tworkitem;
	}

	public Tperson getTperson1() {
		return this.tperson1;
	}

	public void setTperson1(Tperson tperson1) {
		this.tperson1 = tperson1;
	}

	public Tperson getTperson2() {
		return this.tperson2;
	}

	public void setTperson2(Tperson tperson2) {
		this.tperson2 = tperson2;
	}

}
