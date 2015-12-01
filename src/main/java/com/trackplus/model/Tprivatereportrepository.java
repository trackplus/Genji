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
 * The persistent class for the TPRIVATEREPORTREPOSITORY database table.
 * 
 */
@Entity
@Table(name="TPRIVATEREPORTREPOSITORY")
@TableGenerator(name="TPRIVATEREPORTREPOSITORY_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_PRIVATEREPORTREPOSITORY, allocationSize = 10)
public class Tprivatereportrepository extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TPRIVATEREPORTREPOSITORY_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int pkey;

	@Column(length=1)
	private String menuitem;

	@Column(nullable=false, length=100)
	private String name;

	@Lob
	@Column(nullable=false)
	private String query;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tperson
	@OneToMany(mappedBy="tprivatereportrepository")
	private List<Tperson> tpersons;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="OWNER", nullable=false)
	private Tperson tperson;

	public Tprivatereportrepository() {
	}

	@Override
	public int getObjectid() {
		return this.pkey;
	}
	
	@Override
	public void setObjectid(int key) {
		this.pkey = key;
	}

	public int getPkey() {
		return this.pkey;
	}

	public void setPkey(int pkey) {
		this.pkey = pkey;
	}

	public String getMenuitem() {
		return this.menuitem;
	}

	public void setMenuitem(String menuitem) {
		this.menuitem = menuitem;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuery() {
		return this.query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public List<Tperson> getTpersons() {
		return this.tpersons;
	}

	public void setTpersons(List<Tperson> tpersons) {
		this.tpersons = tpersons;
	}

	public Tperson addTperson(Tperson tperson) {
		getTpersons().add(tperson);
		tperson.setTprivatereportrepository(this);

		return tperson;
	}

	public Tperson removeTperson(Tperson tperson) {
		getTpersons().remove(tperson);
		tperson.setTprivatereportrepository(null);

		return tperson;
	}

	public Tperson getTperson() {
		return this.tperson;
	}

	public void setTperson(Tperson tperson) {
		this.tperson = tperson;
	}

}
