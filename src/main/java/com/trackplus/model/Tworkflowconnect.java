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
 * The persistent class for the TWORKFLOWCONNECT database table.
 * 
 */
@Entity
@Table(name="TWORKFLOWCONNECT")
@TableGenerator(name="TWORKFLOWCONNECT_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_WORKFLOWCONNECT, allocationSize = 10)
public class Tworkflowconnect extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TWORKFLOWCONNECT_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tworkflowdef
	@ManyToOne
	@JoinColumn(name="WORKFLOW", nullable=false)
	private Tworkflowdef tworkflowdef;

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

	public Tworkflowconnect() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tworkflowdef getTworkflowdef() {
		return this.tworkflowdef;
	}

	public void setTworkflowdef(Tworkflowdef tworkflowdef) {
		this.tworkflowdef = tworkflowdef;
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

}
