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
 * The persistent class for the TLINKTYPE database table.
 * 
 */
@Entity
@Table(name="TLINKTYPE")
@TableGenerator(name="TLINKTYPE_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_LINKTYPE, allocationSize = 10)
public class Tlinktype extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TLINKTYPE_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=255)
	private String lefttorightall;

	@Column(length=255)
	private String lefttorightfirst;

	@Column(length=255)
	private String lefttorightlevel;

	private int linkdirection;

	@Column(length=255)
	private String linktypeplugin;

	@Lob
	private String moreprops;

	@Column(nullable=false, length=255)
	private String name;

	@Column(length=255)
	private String reversename;

	@Column(length=255)
	private String righttoleftall;

	@Column(length=255)
	private String righttoleftfirst;

	@Column(length=255)
	private String righttoleftlevel;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tblob
	@ManyToOne
	@JoinColumn(name="OUTWARDICONKEY")
	private Tblob tblob1;

	//bi-directional many-to-one association to Tblob
	@ManyToOne
	@JoinColumn(name="INWARDICONKEY")
	private Tblob tblob2;

	//bi-directional many-to-one association to Tworkitemlink
	@OneToMany(mappedBy="tlinktype")
	private List<Tworkitemlink> tworkitemlinks;

	public Tlinktype() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getLefttorightall() {
		return this.lefttorightall;
	}

	public void setLefttorightall(String lefttorightall) {
		this.lefttorightall = lefttorightall;
	}

	public String getLefttorightfirst() {
		return this.lefttorightfirst;
	}

	public void setLefttorightfirst(String lefttorightfirst) {
		this.lefttorightfirst = lefttorightfirst;
	}

	public String getLefttorightlevel() {
		return this.lefttorightlevel;
	}

	public void setLefttorightlevel(String lefttorightlevel) {
		this.lefttorightlevel = lefttorightlevel;
	}

	public int getLinkdirection() {
		return this.linkdirection;
	}

	public void setLinkdirection(int linkdirection) {
		this.linkdirection = linkdirection;
	}

	public String getLinktypeplugin() {
		return this.linktypeplugin;
	}

	public void setLinktypeplugin(String linktypeplugin) {
		this.linktypeplugin = linktypeplugin;
	}

	public String getMoreprops() {
		return this.moreprops;
	}

	public void setMoreprops(String moreprops) {
		this.moreprops = moreprops;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReversename() {
		return this.reversename;
	}

	public void setReversename(String reversename) {
		this.reversename = reversename;
	}

	public String getRighttoleftall() {
		return this.righttoleftall;
	}

	public void setRighttoleftall(String righttoleftall) {
		this.righttoleftall = righttoleftall;
	}

	public String getRighttoleftfirst() {
		return this.righttoleftfirst;
	}

	public void setRighttoleftfirst(String righttoleftfirst) {
		this.righttoleftfirst = righttoleftfirst;
	}

	public String getRighttoleftlevel() {
		return this.righttoleftlevel;
	}

	public void setRighttoleftlevel(String righttoleftlevel) {
		this.righttoleftlevel = righttoleftlevel;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tblob getTblob1() {
		return this.tblob1;
	}

	public void setTblob1(Tblob tblob1) {
		this.tblob1 = tblob1;
	}

	public Tblob getTblob2() {
		return this.tblob2;
	}

	public void setTblob2(Tblob tblob2) {
		this.tblob2 = tblob2;
	}

	public List<Tworkitemlink> getTworkitemlinks() {
		return this.tworkitemlinks;
	}

	public void setTworkitemlinks(List<Tworkitemlink> tworkitemlinks) {
		this.tworkitemlinks = tworkitemlinks;
	}

	public Tworkitemlink addTworkitemlink(Tworkitemlink tworkitemlink) {
		getTworkitemlinks().add(tworkitemlink);
		tworkitemlink.setTlinktype(this);

		return tworkitemlink;
	}

	public Tworkitemlink removeTworkitemlink(Tworkitemlink tworkitemlink) {
		getTworkitemlinks().remove(tworkitemlink);
		tworkitemlink.setTlinktype(null);

		return tworkitemlink;
	}

}
