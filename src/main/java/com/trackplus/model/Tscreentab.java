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
 * The persistent class for the TSCREENTAB database table.
 * 
 */
@Entity
@Table(name="TSCREENTAB")
@TableGenerator(name="TSCREENTAB_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_SCREENTAB, allocationSize = 10)
public class Tscreentab extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TSCREENTAB_GEN",strategy=GenerationType.TABLE)
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

	//bi-directional many-to-one association to Tscreenpanel
	@OneToMany(mappedBy="tscreentab")
	private List<Tscreenpanel> tscreenpanels;

	//bi-directional many-to-one association to Tscreen
	@ManyToOne
	@JoinColumn(name="PARENT", nullable=false)
	private Tscreen tscreen;

	public Tscreentab() {
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

	public List<Tscreenpanel> getTscreenpanels() {
		return this.tscreenpanels;
	}

	public void setTscreenpanels(List<Tscreenpanel> tscreenpanels) {
		this.tscreenpanels = tscreenpanels;
	}

	public Tscreenpanel addTscreenpanel(Tscreenpanel tscreenpanel) {
		getTscreenpanels().add(tscreenpanel);
		tscreenpanel.setTscreentab(this);

		return tscreenpanel;
	}

	public Tscreenpanel removeTscreenpanel(Tscreenpanel tscreenpanel) {
		getTscreenpanels().remove(tscreenpanel);
		tscreenpanel.setTscreentab(null);

		return tscreenpanel;
	}

	public Tscreen getTscreen() {
		return this.tscreen;
	}

	public void setTscreen(Tscreen tscreen) {
		this.tscreen = tscreen;
	}

}
