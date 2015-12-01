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
 * The persistent class for the TCHILDISSUETYPE database table.
 * 
 */
@Entity
@Table(name="TCHILDISSUETYPE")
@TableGenerator(name="TCHILDISSUETYPE_GEN", table="ID_TABLE",
pkColumnName="ID_TABLE_ID",
pkColumnValue=BaseEntity.TABLE_CHILDISSUETYPE, allocationSize = 10)

public class Tchildissuetype extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TCHILDISSUETYPE_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tcategory
	@ManyToOne
	@JoinColumn(name="ISSUETYPEPARENT", nullable=false)
	private Tcategory tcategory1;

	//bi-directional many-to-one association to Tcategory
	@ManyToOne
	@JoinColumn(name="ISSUETYPECHILD", nullable=false)
	private Tcategory tcategory2;

	public Tchildissuetype() {
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

	public Tcategory getTcategory1() {
		return this.tcategory1;
	}

	public void setTcategory1(Tcategory tcategory1) {
		this.tcategory1 = tcategory1;
	}

	public Tcategory getTcategory2() {
		return this.tcategory2;
	}

	public void setTcategory2(Tcategory tcategory2) {
		this.tcategory2 = tcategory2;
	}

}
