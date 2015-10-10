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
 * The persistent class for the TSCREENPANEL database table.
 * 
 */
@Entity
@Table(name="TSCREENPANEL")
@TableGenerator(name="TSCREENPANEL_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_SCREENPANEL, allocationSize = 10)
public class Tscreenpanel extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TSCREENPANEL_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(nullable=false)
	private int colsno;

	@Lob
	private String description;

	@Column(name="`LABEL`", length=255)
	private String label;

	@Column(nullable=false, length=255)
	private String name;

	@Column(nullable=false)
	private int rowsno;

	private int sortorder;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tscreenfield
	@OneToMany(mappedBy="tscreenpanel")
	private List<Tscreenfield> tscreenfields;

	//bi-directional many-to-one association to Tscreentab
	@ManyToOne
	@JoinColumn(name="PARENT", nullable=false)
	private Tscreentab tscreentab;

	public Tscreenpanel() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public int getColsno() {
		return this.colsno;
	}

	public void setColsno(int colsno) {
		this.colsno = colsno;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRowsno() {
		return this.rowsno;
	}

	public void setRowsno(int rowsno) {
		this.rowsno = rowsno;
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

	public List<Tscreenfield> getTscreenfields() {
		return this.tscreenfields;
	}

	public void setTscreenfields(List<Tscreenfield> tscreenfields) {
		this.tscreenfields = tscreenfields;
	}

	public Tscreenfield addTscreenfield(Tscreenfield tscreenfield) {
		getTscreenfields().add(tscreenfield);
		tscreenfield.setTscreenpanel(this);

		return tscreenfield;
	}

	public Tscreenfield removeTscreenfield(Tscreenfield tscreenfield) {
		getTscreenfields().remove(tscreenfield);
		tscreenfield.setTscreenpanel(null);

		return tscreenfield;
	}

	public Tscreentab getTscreentab() {
		return this.tscreentab;
	}

	public void setTscreentab(Tscreentab tscreentab) {
		this.tscreentab = tscreentab;
	}

}
