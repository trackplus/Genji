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
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the THISTORYTRANSACTION database table.
 * 
 */
@Entity
@Table(name="THISTORYTRANSACTION")
@TableGenerator(name="THISTORYTRANSACTION_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_HISTORYTRANSACTION, allocationSize = 10)
public class Thistorytransaction extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="THISTORYTRANSACTION_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(nullable=false)
	private Timestamp lastedit;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tfieldchange
	@OneToMany(mappedBy="thistorytransaction")
	private List<Tfieldchange> tfieldchanges;

	//bi-directional many-to-one association to Tworkitem
	@ManyToOne
	@JoinColumn(name="WORKITEM", nullable=false)
	private Tworkitem tworkitem;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="CHANGEDBY", nullable=false)
	private Tperson tperson;

	public Thistorytransaction() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public Timestamp getLastedit() {
		return this.lastedit;
	}

	public void setLastedit(Timestamp lastedit) {
		this.lastedit = lastedit;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public List<Tfieldchange> getTfieldchanges() {
		return this.tfieldchanges;
	}

	public void setTfieldchanges(List<Tfieldchange> tfieldchanges) {
		this.tfieldchanges = tfieldchanges;
	}

	public Tfieldchange addTfieldchange(Tfieldchange tfieldchange) {
		getTfieldchanges().add(tfieldchange);
		tfieldchange.setThistorytransaction(this);

		return tfieldchange;
	}

	public Tfieldchange removeTfieldchange(Tfieldchange tfieldchange) {
		getTfieldchanges().remove(tfieldchange);
		tfieldchange.setThistorytransaction(null);

		return tfieldchange;
	}

	public Tworkitem getTworkitem() {
		return this.tworkitem;
	}

	public void setTworkitem(Tworkitem tworkitem) {
		this.tworkitem = tworkitem;
	}

	public Tperson getTperson() {
		return this.tperson;
	}

	public void setTperson(Tperson tperson) {
		this.tperson = tperson;
	}

}
