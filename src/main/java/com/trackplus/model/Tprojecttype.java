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
 * The persistent class for the TPROJECTTYPE database table.
 * 
 */
@Entity
@Table(name="TPROJECTTYPE")
@TableGenerator(name="TPROJECTTYPE_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_PROJECTTYPE, allocationSize = 10)
public class Tprojecttype extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TPROJECTTYPE_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int objectid;

	@Column(length=1)
	private String defaultforprivate;

	private double hoursperworkday;

	@Column(name="`LABEL`", length=35)
	private String label;

	@Lob
	private String moreprops;

	private int notifymanagerlevel;

	private int notifyownerlevel;

	@Column(length=36)
	private String tpuuid;


	//bi-directional many-to-one association to Tevent
	@OneToMany(mappedBy="tprojecttype")
	private List<Tevent> tevents;

	//bi-directional many-to-one association to Tfieldconfig
	@OneToMany(mappedBy="tprojecttype")
	private List<Tfieldconfig> tfieldconfigs;

	//bi-directional many-to-one association to Tmailtemplateconfig
	@OneToMany(mappedBy="tprojecttype")
	private List<Tmailtemplateconfig> tmailtemplateconfigs;

	//bi-directional many-to-one association to Tplisttype
	@OneToMany(mappedBy="tprojecttype")
	private List<Tplisttype> tplisttypes;

	//bi-directional many-to-one association to Tppriority
	@OneToMany(mappedBy="tprojecttype")
	private List<Tppriority> tppriorities;

	//bi-directional many-to-one association to Tproject
	@OneToMany(mappedBy="tprojecttype")
	private List<Tproject> tprojects;

	//bi-directional many-to-one association to Tpseverity
	@OneToMany(mappedBy="tprojecttype")
	private List<Tpseverity> tpseverities;

	//bi-directional many-to-one association to Tpstate
	@OneToMany(mappedBy="tprojecttype")
	private List<Tpstate> tpstates;

	//bi-directional many-to-one association to Treportlayout
	@OneToMany(mappedBy="tprojecttype")
	private List<Treportlayout> treportlayouts;

	//bi-directional many-to-one association to Trole
	@OneToMany(mappedBy="tprojecttype")
	private List<Trole> troles;

	//bi-directional many-to-one association to Tscreenconfig
	@OneToMany(mappedBy="tprojecttype")
	private List<Tscreenconfig> tscreenconfigs;

	//bi-directional many-to-one association to Tscript
	@OneToMany(mappedBy="tprojecttype")
	private List<Tscript> tscripts;

	//bi-directional many-to-one association to Tworkflow
	@OneToMany(mappedBy="tprojecttype")
	private List<Tworkflow> tworkflows;

	//bi-directional many-to-one association to Tworkflowconnect
	@OneToMany(mappedBy="tprojecttype")
	private List<Tworkflowconnect> tworkflowconnects;

	public Tprojecttype() {
	}

	public int getObjectid() {
		return this.objectid;
	}

	public void setObjectid(int objectid) {
		this.objectid = objectid;
	}

	public String getDefaultforprivate() {
		return this.defaultforprivate;
	}

	public void setDefaultforprivate(String defaultforprivate) {
		this.defaultforprivate = defaultforprivate;
	}

	public double getHoursperworkday() {
		return this.hoursperworkday;
	}

	public void setHoursperworkday(double hoursperworkday) {
		this.hoursperworkday = hoursperworkday;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getMoreprops() {
		return this.moreprops;
	}

	public void setMoreprops(String moreprops) {
		this.moreprops = moreprops;
	}

	public int getNotifymanagerlevel() {
		return this.notifymanagerlevel;
	}

	public void setNotifymanagerlevel(int notifymanagerlevel) {
		this.notifymanagerlevel = notifymanagerlevel;
	}

	public int getNotifyownerlevel() {
		return this.notifyownerlevel;
	}

	public void setNotifyownerlevel(int notifyownerlevel) {
		this.notifyownerlevel = notifyownerlevel;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}


	public List<Tevent> getTevents() {
		return this.tevents;
	}

	public void setTevents(List<Tevent> tevents) {
		this.tevents = tevents;
	}

	public Tevent addTevent(Tevent tevent) {
		getTevents().add(tevent);
		tevent.setTprojecttype(this);

		return tevent;
	}

	public Tevent removeTevent(Tevent tevent) {
		getTevents().remove(tevent);
		tevent.setTprojecttype(null);

		return tevent;
	}

	public List<Tfieldconfig> getTfieldconfigs() {
		return this.tfieldconfigs;
	}

	public void setTfieldconfigs(List<Tfieldconfig> tfieldconfigs) {
		this.tfieldconfigs = tfieldconfigs;
	}

	public Tfieldconfig addTfieldconfig(Tfieldconfig tfieldconfig) {
		getTfieldconfigs().add(tfieldconfig);
		tfieldconfig.setTprojecttype(this);

		return tfieldconfig;
	}

	public Tfieldconfig removeTfieldconfig(Tfieldconfig tfieldconfig) {
		getTfieldconfigs().remove(tfieldconfig);
		tfieldconfig.setTprojecttype(null);

		return tfieldconfig;
	}

	public List<Tmailtemplateconfig> getTmailtemplateconfigs() {
		return this.tmailtemplateconfigs;
	}

	public void setTmailtemplateconfigs(List<Tmailtemplateconfig> tmailtemplateconfigs) {
		this.tmailtemplateconfigs = tmailtemplateconfigs;
	}

	public Tmailtemplateconfig addTmailtemplateconfig(Tmailtemplateconfig tmailtemplateconfig) {
		getTmailtemplateconfigs().add(tmailtemplateconfig);
		tmailtemplateconfig.setTprojecttype(this);

		return tmailtemplateconfig;
	}

	public Tmailtemplateconfig removeTmailtemplateconfig(Tmailtemplateconfig tmailtemplateconfig) {
		getTmailtemplateconfigs().remove(tmailtemplateconfig);
		tmailtemplateconfig.setTprojecttype(null);

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
		tplisttype.setTprojecttype(this);

		return tplisttype;
	}

	public Tplisttype removeTplisttype(Tplisttype tplisttype) {
		getTplisttypes().remove(tplisttype);
		tplisttype.setTprojecttype(null);

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
		tppriority.setTprojecttype(this);

		return tppriority;
	}

	public Tppriority removeTppriority(Tppriority tppriority) {
		getTppriorities().remove(tppriority);
		tppriority.setTprojecttype(null);

		return tppriority;
	}

	public List<Tproject> getTprojects() {
		return this.tprojects;
	}

	public void setTprojects(List<Tproject> tprojects) {
		this.tprojects = tprojects;
	}

	public Tproject addTproject(Tproject tproject) {
		getTprojects().add(tproject);
		tproject.setTprojecttype(this);

		return tproject;
	}

	public Tproject removeTproject(Tproject tproject) {
		getTprojects().remove(tproject);
		tproject.setTprojecttype(null);

		return tproject;
	}

	public List<Tpseverity> getTpseverities() {
		return this.tpseverities;
	}

	public void setTpseverities(List<Tpseverity> tpseverities) {
		this.tpseverities = tpseverities;
	}

	public Tpseverity addTpseverity(Tpseverity tpseverity) {
		getTpseverities().add(tpseverity);
		tpseverity.setTprojecttype(this);

		return tpseverity;
	}

	public Tpseverity removeTpseverity(Tpseverity tpseverity) {
		getTpseverities().remove(tpseverity);
		tpseverity.setTprojecttype(null);

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
		tpstate.setTprojecttype(this);

		return tpstate;
	}

	public Tpstate removeTpstate(Tpstate tpstate) {
		getTpstates().remove(tpstate);
		tpstate.setTprojecttype(null);

		return tpstate;
	}

	public List<Treportlayout> getTreportlayouts() {
		return this.treportlayouts;
	}

	public void setTreportlayouts(List<Treportlayout> treportlayouts) {
		this.treportlayouts = treportlayouts;
	}

	public Treportlayout addTreportlayout(Treportlayout treportlayout) {
		getTreportlayouts().add(treportlayout);
		treportlayout.setTprojecttype(this);

		return treportlayout;
	}

	public Treportlayout removeTreportlayout(Treportlayout treportlayout) {
		getTreportlayouts().remove(treportlayout);
		treportlayout.setTprojecttype(null);

		return treportlayout;
	}

	public List<Trole> getTroles() {
		return this.troles;
	}

	public void setTroles(List<Trole> troles) {
		this.troles = troles;
	}

	public Trole addTrole(Trole trole) {
		getTroles().add(trole);
		trole.setTprojecttype(this);

		return trole;
	}

	public Trole removeTrole(Trole trole) {
		getTroles().remove(trole);
		trole.setTprojecttype(null);

		return trole;
	}

	public List<Tscreenconfig> getTscreenconfigs() {
		return this.tscreenconfigs;
	}

	public void setTscreenconfigs(List<Tscreenconfig> tscreenconfigs) {
		this.tscreenconfigs = tscreenconfigs;
	}

	public Tscreenconfig addTscreenconfig(Tscreenconfig tscreenconfig) {
		getTscreenconfigs().add(tscreenconfig);
		tscreenconfig.setTprojecttype(this);

		return tscreenconfig;
	}

	public Tscreenconfig removeTscreenconfig(Tscreenconfig tscreenconfig) {
		getTscreenconfigs().remove(tscreenconfig);
		tscreenconfig.setTprojecttype(null);

		return tscreenconfig;
	}

	public List<Tscript> getTscripts() {
		return this.tscripts;
	}

	public void setTscripts(List<Tscript> tscripts) {
		this.tscripts = tscripts;
	}

	public Tscript addTscript(Tscript tscript) {
		getTscripts().add(tscript);
		tscript.setTprojecttype(this);

		return tscript;
	}

	public Tscript removeTscript(Tscript tscript) {
		getTscripts().remove(tscript);
		tscript.setTprojecttype(null);

		return tscript;
	}

	public List<Tworkflow> getTworkflows() {
		return this.tworkflows;
	}

	public void setTworkflows(List<Tworkflow> tworkflows) {
		this.tworkflows = tworkflows;
	}

	public Tworkflow addTworkflow(Tworkflow tworkflow) {
		getTworkflows().add(tworkflow);
		tworkflow.setTprojecttype(this);

		return tworkflow;
	}

	public Tworkflow removeTworkflow(Tworkflow tworkflow) {
		getTworkflows().remove(tworkflow);
		tworkflow.setTprojecttype(null);

		return tworkflow;
	}

	public List<Tworkflowconnect> getTworkflowconnects() {
		return this.tworkflowconnects;
	}

	public void setTworkflowconnects(List<Tworkflowconnect> tworkflowconnects) {
		this.tworkflowconnects = tworkflowconnects;
	}

	public Tworkflowconnect addTworkflowconnect(Tworkflowconnect tworkflowconnect) {
		getTworkflowconnects().add(tworkflowconnect);
		tworkflowconnect.setTprojecttype(this);

		return tworkflowconnect;
	}

	public Tworkflowconnect removeTworkflowconnect(Tworkflowconnect tworkflowconnect) {
		getTworkflowconnects().remove(tworkflowconnect);
		tworkflowconnect.setTprojecttype(null);

		return tworkflowconnect;
	}

}
