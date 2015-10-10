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


/**
 * The persistent class for the TSCREENCONFIG database table.
 * 
 */
@Entity
@Table(name="TSCREENCONFIG")
@TableGenerator(name="TSCREENCONFIG_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_SCREENCONFIG, allocationSize = 10)
public class Tscreenconfig extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TSCREENCONFIG_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Lob
	private String description;

	@Column(length=255)
	private String name;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tscreen
	@ManyToOne
	@JoinColumn(name="SCREEN")
	private Tscreen tscreen;

	//bi-directional many-to-one association to Tcategory
	@ManyToOne
	@JoinColumn(name="ISSUETYPE")
	private Tcategory tcategory;

	//bi-directional many-to-one association to Tprojecttype
	@ManyToOne
	@JoinColumn(name="PROJECTTYPE")
	private Tprojecttype tprojecttype;

	//bi-directional many-to-one association to Tproject
	@ManyToOne
	@JoinColumn(name="PROJECT")
	private Tproject tproject;

	//bi-directional many-to-one association to Taction
	@ManyToOne
	@JoinColumn(name="ACTIONKEY", nullable=false)
	private Taction taction;

	public Tscreenconfig() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
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

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tscreen getTscreen() {
		return this.tscreen;
	}

	public void setTscreen(Tscreen tscreen) {
		this.tscreen = tscreen;
	}

	public Tcategory getTcategory() {
		return this.tcategory;
	}

	public void setTcategory(Tcategory tcategory) {
		this.tcategory = tcategory;
	}

	public Tprojecttype getTprojecttype() {
		return this.tprojecttype;
	}

	public void setTprojecttype(Tprojecttype tprojecttype) {
		this.tprojecttype = tprojecttype;
	}

	public Tproject getTproject() {
		return this.tproject;
	}

	public void setTproject(Tproject tproject) {
		this.tproject = tproject;
	}

	public Taction getTaction() {
		return this.taction;
	}

	public void setTaction(Taction taction) {
		this.taction = taction;
	}

}
