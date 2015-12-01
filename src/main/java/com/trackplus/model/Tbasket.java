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
 * The persistent class for the TBASKET database table.
 * 
 */
@Entity
@Table(name="TBASKET")
@TableGenerator(name="TBASKET_GEN", table="ID_TABLE",
pkColumnName="ID_TABLE_ID",
pkColumnValue=BaseEntity.TABLE_BASKET, allocationSize = 10)

public class Tbasket extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TBASKET_GEN", strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=1)
	private String divisible;

	@Column(name="`LABEL`", nullable=false, length=255)
	private String label;

	private int parentbasket;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="PERSON")
	private Tperson tperson;

	//bi-directional many-to-one association to Tpersonbasket
	@OneToMany(mappedBy="tbasket")
	private List<Tpersonbasket> tpersonbaskets;

	public Tbasket() {
	}

	@Override
	public int getObjectid() {
		return this.objectid;
	}

	@Override
	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getDivisible() {
		return this.divisible;
	}

	public void setDivisible(String divisible) {
		this.divisible = divisible;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getParentbasket() {
		return this.parentbasket;
	}

	public void setParentbasket(int parentbasket) {
		this.parentbasket = parentbasket;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public Tperson getTperson() {
		return this.tperson;
	}

	public void setTperson(Tperson tperson) {
		this.tperson = tperson;
	}

	public List<Tpersonbasket> getTpersonbaskets() {
		return this.tpersonbaskets;
	}

	public void setTpersonbaskets(List<Tpersonbasket> tpersonbaskets) {
		this.tpersonbaskets = tpersonbaskets;
	}

	public Tpersonbasket addTpersonbasket(Tpersonbasket tpersonbasket) {
		getTpersonbaskets().add(tpersonbasket);
		tpersonbasket.setTbasket(this);

		return tpersonbasket;
	}

	public Tpersonbasket removeTpersonbasket(Tpersonbasket tpersonbasket) {
		getTpersonbaskets().remove(tpersonbasket);
		tpersonbasket.setTbasket(null);

		return tpersonbasket;
	}

}
