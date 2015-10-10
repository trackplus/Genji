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
 * The persistent class for the TDASHBOARDFIELD database table.
 * 
 */
@Entity
@Table(name="TDASHBOARDFIELD")
@TableGenerator(name="TDASHBOARDFIELD_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_DASHBOARDFIELD, allocationSize = 10)


public class Tdashboardfield extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TDASHBOARDFIELD_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	private int colindex;

	private int colspan;

	@Column(nullable=false, length=255)
	private String dashboardid;

	@Lob
	private String description;

	@Column(nullable=false, length=255)
	private String name;

	private int rowindex;

	private int rowspan;

	private int sortorder;

	@Column(length=255)
	private String thedescription;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tdashboardpanel
	@ManyToOne
	@JoinColumn(name="PARENT", nullable=false)
	private Tdashboardpanel tdashboardpanel;

	//bi-directional many-to-one association to Tdashboardparameter
	@OneToMany(mappedBy="tdashboardfield")
	private List<Tdashboardparameter> tdashboardparameters;

	public Tdashboardfield() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public int getColindex() {
		return this.colindex;
	}

	public void setColindex(int colindex) {
		this.colindex = colindex;
	}

	public int getColspan() {
		return this.colspan;
	}

	public void setColspan(int colspan) {
		this.colspan = colspan;
	}

	public String getDashboardid() {
		return this.dashboardid;
	}

	public void setDashboardid(String dashboardid) {
		this.dashboardid = dashboardid;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRowindex() {
		return this.rowindex;
	}

	public void setRowindex(int rowindex) {
		this.rowindex = rowindex;
	}

	public int getRowspan() {
		return this.rowspan;
	}

	public void setRowspan(int rowspan) {
		this.rowspan = rowspan;
	}

	public int getSortorder() {
		return this.sortorder;
	}

	public void setSortorder(int sortorder) {
		this.sortorder = sortorder;
	}

	public String getThedescription() {
		return this.thedescription;
	}

	public void setThedescription(String thedescription) {
		this.thedescription = thedescription;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tdashboardpanel getTdashboardpanel() {
		return this.tdashboardpanel;
	}

	public void setTdashboardpanel(Tdashboardpanel tdashboardpanel) {
		this.tdashboardpanel = tdashboardpanel;
	}

	public List<Tdashboardparameter> getTdashboardparameters() {
		return this.tdashboardparameters;
	}

	public void setTdashboardparameters(List<Tdashboardparameter> tdashboardparameters) {
		this.tdashboardparameters = tdashboardparameters;
	}

	public Tdashboardparameter addTdashboardparameter(Tdashboardparameter tdashboardparameter) {
		getTdashboardparameters().add(tdashboardparameter);
		tdashboardparameter.setTdashboardfield(this);

		return tdashboardparameter;
	}

	public Tdashboardparameter removeTdashboardparameter(Tdashboardparameter tdashboardparameter) {
		getTdashboardparameters().remove(tdashboardparameter);
		tdashboardparameter.setTdashboardfield(null);

		return tdashboardparameter;
	}

}
