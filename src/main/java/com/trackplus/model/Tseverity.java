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
 * The persistent class for the TSEVERITY database table.
 * 
 */
@Entity
@Table(name="TSEVERITY")
@TableGenerator(name="TSEVERITY_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_SEVERITY, allocationSize = 10)
public class Tseverity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TSEVERITY_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int pkey;

	@Column(length=255)
	private String cssstyle;

	@Column(length=1)
	private String iconchanged;

	@Column(name="`LABEL`", length=25)
	private String label;

	private int sortorder;

	@Column(length=255)
	private String symbol;

	@Column(length=36)
	private String tpuuid;

	private int wlevel;

	//bi-directional many-to-one association to Tpseverity
	@OneToMany(mappedBy="tseverity")
	private List<Tpseverity> tpseverities;

	//bi-directional many-to-one association to Tblob
	@ManyToOne
	@JoinColumn(name="ICONKEY")
	private Tblob tblob;

	//bi-directional many-to-one association to Tworkitem
	@OneToMany(mappedBy="tseverity")
	private List<Tworkitem> tworkitems;

	public Tseverity() {
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

	public String getCssstyle() {
		return this.cssstyle;
	}

	public void setCssstyle(String cssstyle) {
		this.cssstyle = cssstyle;
	}

	public String getIconchanged() {
		return this.iconchanged;
	}

	public void setIconchanged(String iconchanged) {
		this.iconchanged = iconchanged;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getSortorder() {
		return this.sortorder;
	}

	public void setSortorder(int sortorder) {
		this.sortorder = sortorder;
	}

	public String getSymbol() {
		return this.symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public int getWlevel() {
		return this.wlevel;
	}

	public void setWlevel(int wlevel) {
		this.wlevel = wlevel;
	}

	public List<Tpseverity> getTpseverities() {
		return this.tpseverities;
	}

	public void setTpseverities(List<Tpseverity> tpseverities) {
		this.tpseverities = tpseverities;
	}

	public Tpseverity addTpseverity(Tpseverity tpseverity) {
		getTpseverities().add(tpseverity);
		tpseverity.setTseverity(this);

		return tpseverity;
	}

	public Tpseverity removeTpseverity(Tpseverity tpseverity) {
		getTpseverities().remove(tpseverity);
		tpseverity.setTseverity(null);

		return tpseverity;
	}

	public Tblob getTblob() {
		return this.tblob;
	}

	public void setTblob(Tblob tblob) {
		this.tblob = tblob;
	}

	public List<Tworkitem> getTworkitems() {
		return this.tworkitems;
	}

	public void setTworkitems(List<Tworkitem> tworkitems) {
		this.tworkitems = tworkitems;
	}

	public Tworkitem addTworkitem(Tworkitem tworkitem) {
		getTworkitems().add(tworkitem);
		tworkitem.setTseverity(this);

		return tworkitem;
	}

	public Tworkitem removeTworkitem(Tworkitem tworkitem) {
		getTworkitems().remove(tworkitem);
		tworkitem.setTseverity(null);

		return tworkitem;
	}

}
