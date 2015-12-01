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
 * The persistent class for the TROLE database table.
 * 
 */
@Entity
@Table(name="TROLE")
@TableGenerator(name="TROLE_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_ROLE, allocationSize = 10)
public class Trole extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TROLE_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int pkey;

	private int accesskey;

	@Column(length=30)
	private String extendedaccesskey;

	@Column(name="`LABEL`", nullable=false, length=25)
	private String label;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tacl
	@OneToMany(mappedBy="trole")
	private List<Tacl> tacls;

	//bi-directional many-to-one association to Tconfigoptionsrole
	@OneToMany(mappedBy="trole")
	private List<Tconfigoptionsrole> tconfigoptionsroles;

	//bi-directional many-to-one association to Tprojecttype
	@ManyToOne
	@JoinColumn(name="PROJECTTYPE")
	private Tprojecttype tprojecttype;

	//bi-directional many-to-one association to Trolefield
	@OneToMany(mappedBy="trole")
	private List<Trolefield> trolefields;

	//bi-directional many-to-one association to Trolelisttype
	@OneToMany(mappedBy="trole")
	private List<Trolelisttype> trolelisttypes;

	//bi-directional many-to-one association to Tworkflowguard
	@OneToMany(mappedBy="trole")
	private List<Tworkflowguard> tworkflowguards;

	//bi-directional many-to-one association to Tworkflowrole
	@OneToMany(mappedBy="trole")
	private List<Tworkflowrole> tworkflowroles;

	public Trole() {
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

	public int getAccesskey() {
		return this.accesskey;
	}

	public void setAccesskey(int accesskey) {
		this.accesskey = accesskey;
	}

	public String getExtendedaccesskey() {
		return this.extendedaccesskey;
	}

	public void setExtendedaccesskey(String extendedaccesskey) {
		this.extendedaccesskey = extendedaccesskey;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public List<Tacl> getTacls() {
		return this.tacls;
	}

	public void setTacls(List<Tacl> tacls) {
		this.tacls = tacls;
	}

	public Tacl addTacl(Tacl tacl) {
		getTacls().add(tacl);
		tacl.setTrole(this);

		return tacl;
	}

	public Tacl removeTacl(Tacl tacl) {
		getTacls().remove(tacl);
		tacl.setTrole(null);

		return tacl;
	}

	public List<Tconfigoptionsrole> getTconfigoptionsroles() {
		return this.tconfigoptionsroles;
	}

	public void setTconfigoptionsroles(List<Tconfigoptionsrole> tconfigoptionsroles) {
		this.tconfigoptionsroles = tconfigoptionsroles;
	}

	public Tconfigoptionsrole addTconfigoptionsrole(Tconfigoptionsrole tconfigoptionsrole) {
		getTconfigoptionsroles().add(tconfigoptionsrole);
		tconfigoptionsrole.setTrole(this);

		return tconfigoptionsrole;
	}

	public Tconfigoptionsrole removeTconfigoptionsrole(Tconfigoptionsrole tconfigoptionsrole) {
		getTconfigoptionsroles().remove(tconfigoptionsrole);
		tconfigoptionsrole.setTrole(null);

		return tconfigoptionsrole;
	}

	public Tprojecttype getTprojecttype() {
		return this.tprojecttype;
	}

	public void setTprojecttype(Tprojecttype tprojecttype) {
		this.tprojecttype = tprojecttype;
	}

	public List<Trolefield> getTrolefields() {
		return this.trolefields;
	}

	public void setTrolefields(List<Trolefield> trolefields) {
		this.trolefields = trolefields;
	}

	public Trolefield addTrolefield(Trolefield trolefield) {
		getTrolefields().add(trolefield);
		trolefield.setTrole(this);

		return trolefield;
	}

	public Trolefield removeTrolefield(Trolefield trolefield) {
		getTrolefields().remove(trolefield);
		trolefield.setTrole(null);

		return trolefield;
	}

	public List<Trolelisttype> getTrolelisttypes() {
		return this.trolelisttypes;
	}

	public void setTrolelisttypes(List<Trolelisttype> trolelisttypes) {
		this.trolelisttypes = trolelisttypes;
	}

	public Trolelisttype addTrolelisttype(Trolelisttype trolelisttype) {
		getTrolelisttypes().add(trolelisttype);
		trolelisttype.setTrole(this);

		return trolelisttype;
	}

	public Trolelisttype removeTrolelisttype(Trolelisttype trolelisttype) {
		getTrolelisttypes().remove(trolelisttype);
		trolelisttype.setTrole(null);

		return trolelisttype;
	}

	public List<Tworkflowguard> getTworkflowguards() {
		return this.tworkflowguards;
	}

	public void setTworkflowguards(List<Tworkflowguard> tworkflowguards) {
		this.tworkflowguards = tworkflowguards;
	}

	public Tworkflowguard addTworkflowguard(Tworkflowguard tworkflowguard) {
		getTworkflowguards().add(tworkflowguard);
		tworkflowguard.setTrole(this);

		return tworkflowguard;
	}

	public Tworkflowguard removeTworkflowguard(Tworkflowguard tworkflowguard) {
		getTworkflowguards().remove(tworkflowguard);
		tworkflowguard.setTrole(null);

		return tworkflowguard;
	}

	public List<Tworkflowrole> getTworkflowroles() {
		return this.tworkflowroles;
	}

	public void setTworkflowroles(List<Tworkflowrole> tworkflowroles) {
		this.tworkflowroles = tworkflowroles;
	}

	public Tworkflowrole addTworkflowrole(Tworkflowrole tworkflowrole) {
		getTworkflowroles().add(tworkflowrole);
		tworkflowrole.setTrole(this);

		return tworkflowrole;
	}

	public Tworkflowrole removeTworkflowrole(Tworkflowrole tworkflowrole) {
		getTworkflowroles().remove(tworkflowrole);
		tworkflowrole.setTrole(null);

		return tworkflowrole;
	}

}
