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

import java.util.List;


/**
 * The persistent class for the TDASHBOARDTAB database table.
 * 
 */
@Entity
@Table(name="TDASHBOARDTAB")
@TableGenerator(name="TDASHBOARDTAB_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_DASHBOARDTAB, allocationSize = 10)

public class Tdashboardtab extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TDASHBOARDTAB_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Lob
	private String description;

	@Column(name="`LABEL`", length=255)
	private String label;

	@Column(nullable=false, length=255)
	private String name;

	private int sortorder;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tdashboardpanel
	@OneToMany(mappedBy="tdashboardtab")
	private List<Tdashboardpanel> tdashboardpanels;

	//bi-directional many-to-one association to Tdashboardscreen
	@ManyToOne
	@JoinColumn(name="PARENT", nullable=false)
	private Tdashboardscreen tdashboardscreen;

	public Tdashboardtab() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
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

	public List<Tdashboardpanel> getTdashboardpanels() {
		return this.tdashboardpanels;
	}

	public void setTdashboardpanels(List<Tdashboardpanel> tdashboardpanels) {
		this.tdashboardpanels = tdashboardpanels;
	}

	public Tdashboardpanel addTdashboardpanel(Tdashboardpanel tdashboardpanel) {
		getTdashboardpanels().add(tdashboardpanel);
		tdashboardpanel.setTdashboardtab(this);

		return tdashboardpanel;
	}

	public Tdashboardpanel removeTdashboardpanel(Tdashboardpanel tdashboardpanel) {
		getTdashboardpanels().remove(tdashboardpanel);
		tdashboardpanel.setTdashboardtab(null);

		return tdashboardpanel;
	}

	public Tdashboardscreen getTdashboardscreen() {
		return this.tdashboardscreen;
	}

	public void setTdashboardscreen(Tdashboardscreen tdashboardscreen) {
		this.tdashboardscreen = tdashboardscreen;
	}

}
