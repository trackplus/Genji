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


/**
 * The persistent class for the TPSTATE database table.
 * 
 */
@Entity
@Table(name="TPSTATE")
@TableGenerator(name="TPSTATE_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_PSTATE, allocationSize = 10)
public class Tpstate extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TPSTATE_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tstate
	@ManyToOne
	@JoinColumn(name="STATE")
	private Tstate tstate;

	//bi-directional many-to-one association to Tprojecttype
	@ManyToOne
	@JoinColumn(name="PROJECTTYPE")
	private Tprojecttype tprojecttype;

	//bi-directional many-to-one association to Tcategory
	@ManyToOne
	@JoinColumn(name="LISTTYPE")
	private Tcategory tcategory;

	public Tpstate() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tstate getTstate() {
		return this.tstate;
	}

	public void setTstate(Tstate tstate) {
		this.tstate = tstate;
	}

	public Tprojecttype getTprojecttype() {
		return this.tprojecttype;
	}

	public void setTprojecttype(Tprojecttype tprojecttype) {
		this.tprojecttype = tprojecttype;
	}

	public Tcategory getTcategory() {
		return this.tcategory;
	}

	public void setTcategory(Tcategory tcategory) {
		this.tcategory = tcategory;
	}

}
