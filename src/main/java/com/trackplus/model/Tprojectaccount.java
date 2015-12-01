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
 * The persistent class for the TPROJECTACCOUNT database table.
 * 
 */
@Entity
@Table(name="TPROJECTACCOUNT")
@TableGenerator(name="TPROJECTACCOUNT_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
pkColumnValue=BaseEntity.TABLE_PROJECTACCOUNT, allocationSize = 10)
public class Tprojectaccount extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TprojectaccountPK id;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tproject
	@ManyToOne
	@JoinColumn(name="PROJECT", nullable=false, insertable=false, updatable=false)
	private Tproject tproject;

	//bi-directional many-to-one association to Taccount
	@ManyToOne
	@JoinColumn(name="ACCOUNT", nullable=false, insertable=false, updatable=false)
	private Taccount taccount;

	public Tprojectaccount() {
	}


	@Override
	public int getObjectid() {
		return 0;
	}
	
	@Override
	public void setObjectid(int key) {
		
	}
	
	public TprojectaccountPK getId() {
		return this.id;
	}

	public void setId(TprojectaccountPK id) {
		this.id = id;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tproject getTproject() {
		return this.tproject;
	}

	public void setTproject(Tproject tproject) {
		this.tproject = tproject;
	}

	public Taccount getTaccount() {
		return this.taccount;
	}

	public void setTaccount(Taccount taccount) {
		this.taccount = taccount;
	}

}
