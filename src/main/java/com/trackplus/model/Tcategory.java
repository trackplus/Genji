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

import java.util.List;


/**
 * The persistent class for the TCATEGORY database table.
 * 
 */
@Entity
@Table(name="TCATEGORY")
@TableGenerator(name="TCATEGORY_GEN", table="ID_TABLE",
pkColumnName="ID_TABLE_ID",
pkColumnValue=BaseEntity.TABLE_CATEGORY, allocationSize = 10)

public class Tcategory extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TCATEGORY_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int pkey;

	@Column(length=255)
	private String cssstyle;

	@Column(length=1)
	private String iconchanged;

	@Column(name="`LABEL`", nullable=false, length=25)
	private String label;

	private int sortorder;

	@Column(length=255)
	private String symbol;

	@Column(length=36)
	private String tpuuid;

	private int typeflag;

	//bi-directional many-to-one association to Tblob
	@ManyToOne
	@JoinColumn(name="ICONKEY")
	private Tblob tblob;

	//bi-directional many-to-one association to Tchildissuetype
	@OneToMany(mappedBy="tcategory1")
	private List<Tchildissuetype> tchildissuetypes1;

	//bi-directional many-to-one association to Tchildissuetype
	@OneToMany(mappedBy="tcategory2")
	private List<Tchildissuetype> tchildissuetypes2;


	//bi-directional many-to-one association to Tfieldconfig
	@OneToMany(mappedBy="tcategory")
	private List<Tfieldconfig> tfieldconfigs;

	//bi-directional many-to-one association to Tinitstate
	@OneToMany(mappedBy="tcategory")
	private List<Tinitstate> tinitstates;

	//bi-directional many-to-one association to Tmailtemplateconfig
	@OneToMany(mappedBy="tcategory")
	private List<Tmailtemplateconfig> tmailtemplateconfigs;

	//bi-directional many-to-one association to Tplisttype
	@OneToMany(mappedBy="tcategory")
	private List<Tplisttype> tplisttypes;

	//bi-directional many-to-one association to Tppriority
	@OneToMany(mappedBy="tcategory")
	private List<Tppriority> tppriorities;

	//bi-directional many-to-one association to Tpseverity
	@OneToMany(mappedBy="tcategory")
	private List<Tpseverity> tpseverities;

	//bi-directional many-to-one association to Tpstate
	@OneToMany(mappedBy="tcategory")
	private List<Tpstate> tpstates;

	//bi-directional many-to-one association to Trolelisttype
	@OneToMany(mappedBy="tcategory")
	private List<Trolelisttype> trolelisttypes;

	//bi-directional many-to-one association to Tscreenconfig
	@OneToMany(mappedBy="tcategory")
	private List<Tscreenconfig> tscreenconfigs;

	//bi-directional many-to-one association to Tworkflowcategory
	@OneToMany(mappedBy="tcategory")
	private List<Tworkflowcategory> tworkflowcategories;

	//bi-directional many-to-one association to Tworkflowconnect
	@OneToMany(mappedBy="tcategory")
	private List<Tworkflowconnect> tworkflowconnects;

	//bi-directional many-to-one association to Tworkitem
	@OneToMany(mappedBy="tcategory")
	private List<Tworkitem> tworkitems;

	public Tcategory() {
	}

	public int getObjectid() {
		return this.pkey;
	}
	
	public void setObjectid(int key) {
		this.pkey = key;
	}

	public int getPkey() {
		return this.pkey;
	}

	public void setPkey(int pkey) {
		this.pkey = pkey;
	}

	public String getCssstyle() {
		return this.cssstyle;
	}

	public void setCssstyle(String cssstyle) {
		this.cssstyle = cssstyle;
	}

	public String getIconchanged() {
		return this.iconchanged;
	}

	public void setIconchanged(String iconchanged) {
		this.iconchanged = iconchanged;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getSortorder() {
		return this.sortorder;
	}

	public void setSortorder(int sortorder) {
		this.sortorder = sortorder;
	}

	public String getSymbol() {
		return this.symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public int getTypeflag() {
		return this.typeflag;
	}

	public void setTypeflag(int typeflag) {
		this.typeflag = typeflag;
	}

	public Tblob getTblob() {
		return this.tblob;
	}

	public void setTblob(Tblob tblob) {
		this.tblob = tblob;
	}

	public List<Tchildissuetype> getTchildissuetypes1() {
		return this.tchildissuetypes1;
	}

	public void setTchildissuetypes1(List<Tchildissuetype> tchildissuetypes1) {
		this.tchildissuetypes1 = tchildissuetypes1;
	}

	public Tchildissuetype addTchildissuetypes1(Tchildissuetype tchildissuetypes1) {
		getTchildissuetypes1().add(tchildissuetypes1);
		tchildissuetypes1.setTcategory1(this);

		return tchildissuetypes1;
	}

	public Tchildissuetype removeTchildissuetypes1(Tchildissuetype tchildissuetypes1) {
		getTchildissuetypes1().remove(tchildissuetypes1);
		tchildissuetypes1.setTcategory1(null);

		return tchildissuetypes1;
	}

	public List<Tchildissuetype> getTchildissuetypes2() {
		return this.tchildissuetypes2;
	}

	public void setTchildissuetypes2(List<Tchildissuetype> tchildissuetypes2) {
		this.tchildissuetypes2 = tchildissuetypes2;
	}

	public Tchildissuetype addTchildissuetypes2(Tchildissuetype tchildissuetypes2) {
		getTchildissuetypes2().add(tchildissuetypes2);
		tchildissuetypes2.setTcategory2(this);

		return tchildissuetypes2;
	}

	public Tchildissuetype removeTchildissuetypes2(Tchildissuetype tchildissuetypes2) {
		getTchildissuetypes2().remove(tchildissuetypes2);
		tchildissuetypes2.setTcategory2(null);

		return tchildissuetypes2;
	}


	public List<Tfieldconfig> getTfieldconfigs() {
		return this.tfieldconfigs;
	}

	public void setTfieldconfigs(List<Tfieldconfig> tfieldconfigs) {
		this.tfieldconfigs = tfieldconfigs;
	}

	public Tfieldconfig addTfieldconfig(Tfieldconfig tfieldconfig) {
		getTfieldconfigs().add(tfieldconfig);
		tfieldconfig.setTcategory(this);

		return tfieldconfig;
	}

	public Tfieldconfig removeTfieldconfig(Tfieldconfig tfieldconfig) {
		getTfieldconfigs().remove(tfieldconfig);
		tfieldconfig.setTcategory(null);

		return tfieldconfig;
	}

	public List<Tinitstate> getTinitstates() {
		return this.tinitstates;
	}

	public void setTinitstates(List<Tinitstate> tinitstates) {
		this.tinitstates = tinitstates;
	}

	public Tinitstate addTinitstate(Tinitstate tinitstate) {
		getTinitstates().add(tinitstate);
		tinitstate.setTcategory(this);

		return tinitstate;
	}

	public Tinitstate removeTinitstate(Tinitstate tinitstate) {
		getTinitstates().remove(tinitstate);
		tinitstate.setTcategory(null);

		return tinitstate;
	}

	public List<Tmailtemplateconfig> getTmailtemplateconfigs() {
		return this.tmailtemplateconfigs;
	}

	public void setTmailtemplateconfigs(List<Tmailtemplateconfig> tmailtemplateconfigs) {
		this.tmailtemplateconfigs = tmailtemplateconfigs;
	}

	public Tmailtemplateconfig addTmailtemplateconfig(Tmailtemplateconfig tmailtemplateconfig) {
		getTmailtemplateconfigs().add(tmailtemplateconfig);
		tmailtemplateconfig.setTcategory(this);

		return tmailtemplateconfig;
	}

	public Tmailtemplateconfig removeTmailtemplateconfig(Tmailtemplateconfig tmailtemplateconfig) {
		getTmailtemplateconfigs().remove(tmailtemplateconfig);
		tmailtemplateconfig.setTcategory(null);

		return tmailtemplateconfig;
	}

	public List<Tplisttype> getTplisttypes() {
		return this.tplisttypes;
	}

	public void setTplisttypes(List<Tplisttype> tplisttypes) {
		this.tplisttypes = tplisttypes;
	}

	public Tplisttype addTplisttype(Tplisttype tplisttype) {
		getTplisttypes().add(tplisttype);
		tplisttype.setTcategory(this);

		return tplisttype;
	}

	public Tplisttype removeTplisttype(Tplisttype tplisttype) {
		getTplisttypes().remove(tplisttype);
		tplisttype.setTcategory(null);

		return tplisttype;
	}

	public List<Tppriority> getTppriorities() {
		return this.tppriorities;
	}

	public void setTppriorities(List<Tppriority> tppriorities) {
		this.tppriorities = tppriorities;
	}

	public Tppriority addTppriority(Tppriority tppriority) {
		getTppriorities().add(tppriority);
		tppriority.setTcategory(this);

		return tppriority;
	}

	public Tppriority removeTppriority(Tppriority tppriority) {
		getTppriorities().remove(tppriority);
		tppriority.setTcategory(null);

		return tppriority;
	}

	public List<Tpseverity> getTpseverities() {
		return this.tpseverities;
	}

	public void setTpseverities(List<Tpseverity> tpseverities) {
		this.tpseverities = tpseverities;
	}

	public Tpseverity addTpseverity(Tpseverity tpseverity) {
		getTpseverities().add(tpseverity);
		tpseverity.setTcategory(this);

		return tpseverity;
	}

	public Tpseverity removeTpseverity(Tpseverity tpseverity) {
		getTpseverities().remove(tpseverity);
		tpseverity.setTcategory(null);

		return tpseverity;
	}

	public List<Tpstate> getTpstates() {
		return this.tpstates;
	}

	public void setTpstates(List<Tpstate> tpstates) {
		this.tpstates = tpstates;
	}

	public Tpstate addTpstate(Tpstate tpstate) {
		getTpstates().add(tpstate);
		tpstate.setTcategory(this);

		return tpstate;
	}

	public Tpstate removeTpstate(Tpstate tpstate) {
		getTpstates().remove(tpstate);
		tpstate.setTcategory(null);

		return tpstate;
	}

	public List<Trolelisttype> getTrolelisttypes() {
		return this.trolelisttypes;
	}

	public void setTrolelisttypes(List<Trolelisttype> trolelisttypes) {
		this.trolelisttypes = trolelisttypes;
	}

	public Trolelisttype addTrolelisttype(Trolelisttype trolelisttype) {
		getTrolelisttypes().add(trolelisttype);
		trolelisttype.setTcategory(this);

		return trolelisttype;
	}

	public Trolelisttype removeTrolelisttype(Trolelisttype trolelisttype) {
		getTrolelisttypes().remove(trolelisttype);
		trolelisttype.setTcategory(null);

		return trolelisttype;
	}

	public List<Tscreenconfig> getTscreenconfigs() {
		return this.tscreenconfigs;
	}

	public void setTscreenconfigs(List<Tscreenconfig> tscreenconfigs) {
		this.tscreenconfigs = tscreenconfigs;
	}

	public Tscreenconfig addTscreenconfig(Tscreenconfig tscreenconfig) {
		getTscreenconfigs().add(tscreenconfig);
		tscreenconfig.setTcategory(this);

		return tscreenconfig;
	}

	public Tscreenconfig removeTscreenconfig(Tscreenconfig tscreenconfig) {
		getTscreenconfigs().remove(tscreenconfig);
		tscreenconfig.setTcategory(null);

		return tscreenconfig;
	}

	public List<Tworkflowcategory> getTworkflowcategories() {
		return this.tworkflowcategories;
	}

	public void setTworkflowcategories(List<Tworkflowcategory> tworkflowcategories) {
		this.tworkflowcategories = tworkflowcategories;
	}

	public Tworkflowcategory addTworkflowcategory(Tworkflowcategory tworkflowcategory) {
		getTworkflowcategories().add(tworkflowcategory);
		tworkflowcategory.setTcategory(this);

		return tworkflowcategory;
	}

	public Tworkflowcategory removeTworkflowcategory(Tworkflowcategory tworkflowcategory) {
		getTworkflowcategories().remove(tworkflowcategory);
		tworkflowcategory.setTcategory(null);

		return tworkflowcategory;
	}

	public List<Tworkflowconnect> getTworkflowconnects() {
		return this.tworkflowconnects;
	}

	public void setTworkflowconnects(List<Tworkflowconnect> tworkflowconnects) {
		this.tworkflowconnects = tworkflowconnects;
	}

	public Tworkflowconnect addTworkflowconnect(Tworkflowconnect tworkflowconnect) {
		getTworkflowconnects().add(tworkflowconnect);
		tworkflowconnect.setTcategory(this);

		return tworkflowconnect;
	}

	public Tworkflowconnect removeTworkflowconnect(Tworkflowconnect tworkflowconnect) {
		getTworkflowconnects().remove(tworkflowconnect);
		tworkflowconnect.setTcategory(null);

		return tworkflowconnect;
	}

	public List<Tworkitem> getTworkitems() {
		return this.tworkitems;
	}

	public void setTworkitems(List<Tworkitem> tworkitems) {
		this.tworkitems = tworkitems;
	}

	public Tworkitem addTworkitem(Tworkitem tworkitem) {
		getTworkitems().add(tworkitem);
		tworkitem.setTcategory(this);

		return tworkitem;
	}

	public Tworkitem removeTworkitem(Tworkitem tworkitem) {
		getTworkitems().remove(tworkitem);
		tworkitem.setTcategory(null);

		return tworkitem;
	}

}
