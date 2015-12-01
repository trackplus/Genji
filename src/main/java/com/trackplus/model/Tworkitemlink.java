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
import java.util.Date;


/**
 * The persistent class for the TWORKITEMLINK database table.
 * 
 */
@Entity
@Table(name="TWORKITEMLINK")
@TableGenerator(name="TWORKITEMLINK_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_WORKITEMLINK, allocationSize = 10)
public class Tworkitemlink extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TWORKITEMLINK_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Temporal(TemporalType.TIMESTAMP)
	private Date datevalue;

	@Lob
	private String description;

	@Column(length=255)
	private String externallink;

	private int integervalue1;

	private int integervalue2;

	private int integervalue3;

	@Column(nullable=false)
	private Timestamp lastedit;

	private int linkdirection;

	@Column(length=1)
	private String linkiscrossproject;

	private double linklag;

	private int linklagformat;

	@Column(length=255)
	private String stringvalue1;

	@Column(length=255)
	private String stringvalue2;

	@Column(length=255)
	private String stringvalue3;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tworkitem
	@ManyToOne
	@JoinColumn(name="LINKPRED", nullable=false)
	private Tworkitem tworkitem1;

	//bi-directional many-to-one association to Tworkitem
	@ManyToOne
	@JoinColumn(name="LINKSUCC")
	private Tworkitem tworkitem2;

	//bi-directional many-to-one association to Tlinktype
	@ManyToOne
	@JoinColumn(name="LINKTYPE", nullable=false)
	private Tlinktype tlinktype;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="CHANGEDBY")
	private Tperson tperson;

	public Tworkitemlink() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public Date getDatevalue() {
		return this.datevalue;
	}

	public void setDatevalue(Date datevalue) {
		this.datevalue = datevalue;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExternallink() {
		return this.externallink;
	}

	public void setExternallink(String externallink) {
		this.externallink = externallink;
	}

	public int getIntegervalue1() {
		return this.integervalue1;
	}

	public void setIntegervalue1(int integervalue1) {
		this.integervalue1 = integervalue1;
	}

	public int getIntegervalue2() {
		return this.integervalue2;
	}

	public void setIntegervalue2(int integervalue2) {
		this.integervalue2 = integervalue2;
	}

	public int getIntegervalue3() {
		return this.integervalue3;
	}

	public void setIntegervalue3(int integervalue3) {
		this.integervalue3 = integervalue3;
	}

	public Timestamp getLastedit() {
		return this.lastedit;
	}

	public void setLastedit(Timestamp lastedit) {
		this.lastedit = lastedit;
	}

	public int getLinkdirection() {
		return this.linkdirection;
	}

	public void setLinkdirection(int linkdirection) {
		this.linkdirection = linkdirection;
	}

	public String getLinkiscrossproject() {
		return this.linkiscrossproject;
	}

	public void setLinkiscrossproject(String linkiscrossproject) {
		this.linkiscrossproject = linkiscrossproject;
	}

	public double getLinklag() {
		return this.linklag;
	}

	public void setLinklag(double linklag) {
		this.linklag = linklag;
	}

	public int getLinklagformat() {
		return this.linklagformat;
	}

	public void setLinklagformat(int linklagformat) {
		this.linklagformat = linklagformat;
	}

	public String getStringvalue1() {
		return this.stringvalue1;
	}

	public void setStringvalue1(String stringvalue1) {
		this.stringvalue1 = stringvalue1;
	}

	public String getStringvalue2() {
		return this.stringvalue2;
	}

	public void setStringvalue2(String stringvalue2) {
		this.stringvalue2 = stringvalue2;
	}

	public String getStringvalue3() {
		return this.stringvalue3;
	}

	public void setStringvalue3(String stringvalue3) {
		this.stringvalue3 = stringvalue3;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tworkitem getTworkitem1() {
		return this.tworkitem1;
	}

	public void setTworkitem1(Tworkitem tworkitem1) {
		this.tworkitem1 = tworkitem1;
	}

	public Tworkitem getTworkitem2() {
		return this.tworkitem2;
	}

	public void setTworkitem2(Tworkitem tworkitem2) {
		this.tworkitem2 = tworkitem2;
	}

	public Tlinktype getTlinktype() {
		return this.tlinktype;
	}

	public void setTlinktype(Tlinktype tlinktype) {
		this.tlinktype = tlinktype;
	}

	public Tperson getTperson() {
		return this.tperson;
	}

	public void setTperson(Tperson tperson) {
		this.tperson = tperson;
	}

}
