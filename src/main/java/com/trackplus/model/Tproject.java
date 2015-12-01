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
 * The persistent class for the TPROJECT database table.
 * 
 */
@Entity
@Table(name="TPROJECT")
@TableGenerator(name="TPROJECT_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_PROJECT, allocationSize = 10)
public class Tproject extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TPROJECT_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int pkey;

	@Column(length=255)
	private String currencyname;

	@Column(length=255)
	private String currencysymbol;

	@Column(nullable=false, length=1)
	private String deleted;

	@Column(length=255)
	private String description;

	private double hoursperworkday;

	@Column(length=1)
	private String isprivate;

	@Column(name="`LABEL`", length=35)
	private String label;

	private int lastid;

	@Lob
	private String moreprops;

	private int nextitemid;

	@Column(length=50)
	private String prefix;

	@Column(length=50)
	private String taglabel;

	@Column(length=36)
	private String tpuuid;

	@Column(length=255)
	private String versionsystemfield0;

	@Column(length=255)
	private String versionsystemfield1;

	@Column(length=255)
	private String versionsystemfield2;

	@Column(length=255)
	private String versionsystemfield3;

	//bi-directional many-to-one association to Tacl
	@OneToMany(mappedBy="tproject")
	private List<Tacl> tacls;

	//bi-directional many-to-one association to Tclass
	@OneToMany(mappedBy="tproject")
	private List<Tclass> tclasses;

	//bi-directional many-to-one association to Tdashboardscreen
	@OneToMany(mappedBy="tproject")
	private List<Tdashboardscreen> tdashboardscreens;

	//bi-directional many-to-one association to Tevent
	@OneToMany(mappedBy="tproject")
	private List<Tevent> tevents;

	//bi-directional many-to-one association to Texporttemplate
	@OneToMany(mappedBy="tproject")
	private List<Texporttemplate> texporttemplates;

	//bi-directional many-to-one association to Tfieldconfig
	@OneToMany(mappedBy="tproject")
	private List<Tfieldconfig> tfieldconfigs;

	//bi-directional many-to-one association to Tfiltercategory
	@OneToMany(mappedBy="tproject")
	private List<Tfiltercategory> tfiltercategories;

	//bi-directional many-to-one association to Tinitstate
	@OneToMany(mappedBy="tproject")
	private List<Tinitstate> tinitstates;

	//bi-directional many-to-one association to Tlist
	@OneToMany(mappedBy="tproject")
	private List<Tlist> tlists;

	//bi-directional many-to-one association to Tmailtemplateconfig
	@OneToMany(mappedBy="tproject")
	private List<Tmailtemplateconfig> tmailtemplateconfigs;

	//bi-directional many-to-one association to Tnotifysetting
	@OneToMany(mappedBy="tproject")
	private List<Tnotifysetting> tnotifysettings;

	//bi-directional many-to-one association to Torgprojectsla
	@OneToMany(mappedBy="tproject")
	private List<Torgprojectsla> torgprojectslas;

	//bi-directional many-to-one association to Tprojcat
	@OneToMany(mappedBy="tproject")
	private List<Tprojcat> tprojcats;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="DEFOWNER")
	private Tperson tperson1;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="DEFMANAGER")
	private Tperson tperson2;

	//bi-directional many-to-one association to Tstate
	@ManyToOne
	@JoinColumn(name="DEFINITSTATE")
	private Tstate tstate;

	//bi-directional many-to-one association to Tprojecttype
	@ManyToOne
	@JoinColumn(name="PROJECTTYPE")
	private Tprojecttype tprojecttype;

	//bi-directional many-to-one association to Tsystemstate
	@ManyToOne
	@JoinColumn(name="STATUS")
	private Tsystemstate tsystemstate;

	//bi-directional many-to-one association to Tproject
	@ManyToOne
	@JoinColumn(name="PARENT")
	private Tproject tproject;

	//bi-directional many-to-one association to Tproject
	@OneToMany(mappedBy="tproject")
	private List<Tproject> tprojects;

	//bi-directional many-to-one association to Tprojectaccount
	@OneToMany(mappedBy="tproject")
	private List<Tprojectaccount> tprojectaccounts;

	//bi-directional many-to-one association to Tprojectreportrepository
	@OneToMany(mappedBy="tproject")
	private List<Tprojectreportrepository> tprojectreportrepositories;

	//bi-directional many-to-one association to Tqueryrepository
	@OneToMany(mappedBy="tproject")
	private List<Tqueryrepository> tqueryrepositories;

	//bi-directional many-to-one association to Trelease
	@OneToMany(mappedBy="tproject")
	private List<Trelease> treleases;

	//bi-directional many-to-one association to Treportcategory
	@OneToMany(mappedBy="tproject")
	private List<Treportcategory> treportcategories;

	//bi-directional many-to-one association to Treportlayout
	@OneToMany(mappedBy="tproject")
	private List<Treportlayout> treportlayouts;

	//bi-directional many-to-one association to Tscreenconfig
	@OneToMany(mappedBy="tproject")
	private List<Tscreenconfig> tscreenconfigs;

	//bi-directional many-to-one association to Tscript
	@OneToMany(mappedBy="tproject")
	private List<Tscript> tscripts;

	//bi-directional many-to-one association to Tversioncontrolparameter
	@OneToMany(mappedBy="tproject")
	private List<Tversioncontrolparameter> tversioncontrolparameters;

	//bi-directional many-to-one association to Tworkflowconnect
	@OneToMany(mappedBy="tproject")
	private List<Tworkflowconnect> tworkflowconnects;

	//bi-directional many-to-one association to Tworkitem
	@OneToMany(mappedBy="tproject")
	private List<Tworkitem> tworkitems;

	public Tproject() {
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

	public String getCurrencyname() {
		return this.currencyname;
	}

	public void setCurrencyname(String currencyname) {
		this.currencyname = currencyname;
	}

	public String getCurrencysymbol() {
		return this.currencysymbol;
	}

	public void setCurrencysymbol(String currencysymbol) {
		this.currencysymbol = currencysymbol;
	}

	public String getDeleted() {
		return this.deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getHoursperworkday() {
		return this.hoursperworkday;
	}

	public void setHoursperworkday(double hoursperworkday) {
		this.hoursperworkday = hoursperworkday;
	}

	public String getIsprivate() {
		return this.isprivate;
	}

	public void setIsprivate(String isprivate) {
		this.isprivate = isprivate;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getLastid() {
		return this.lastid;
	}

	public void setLastid(int lastid) {
		this.lastid = lastid;
	}

	public String getMoreprops() {
		return this.moreprops;
	}

	public void setMoreprops(String moreprops) {
		this.moreprops = moreprops;
	}

	public int getNextitemid() {
		return this.nextitemid;
	}

	public void setNextitemid(int nextitemid) {
		this.nextitemid = nextitemid;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getTaglabel() {
		return this.taglabel;
	}

	public void setTaglabel(String taglabel) {
		this.taglabel = taglabel;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public String getVersionsystemfield0() {
		return this.versionsystemfield0;
	}

	public void setVersionsystemfield0(String versionsystemfield0) {
		this.versionsystemfield0 = versionsystemfield0;
	}

	public String getVersionsystemfield1() {
		return this.versionsystemfield1;
	}

	public void setVersionsystemfield1(String versionsystemfield1) {
		this.versionsystemfield1 = versionsystemfield1;
	}

	public String getVersionsystemfield2() {
		return this.versionsystemfield2;
	}

	public void setVersionsystemfield2(String versionsystemfield2) {
		this.versionsystemfield2 = versionsystemfield2;
	}

	public String getVersionsystemfield3() {
		return this.versionsystemfield3;
	}

	public void setVersionsystemfield3(String versionsystemfield3) {
		this.versionsystemfield3 = versionsystemfield3;
	}

	public List<Tacl> getTacls() {
		return this.tacls;
	}

	public void setTacls(List<Tacl> tacls) {
		this.tacls = tacls;
	}

	public Tacl addTacl(Tacl tacl) {
		getTacls().add(tacl);
		tacl.setTproject(this);

		return tacl;
	}

	public Tacl removeTacl(Tacl tacl) {
		getTacls().remove(tacl);
		tacl.setTproject(null);

		return tacl;
	}

	public List<Tclass> getTclasses() {
		return this.tclasses;
	}

	public void setTclasses(List<Tclass> tclasses) {
		this.tclasses = tclasses;
	}

	public Tclass addTclass(Tclass tclass) {
		getTclasses().add(tclass);
		tclass.setTproject(this);

		return tclass;
	}

	public Tclass removeTclass(Tclass tclass) {
		getTclasses().remove(tclass);
		tclass.setTproject(null);

		return tclass;
	}

	public List<Tdashboardscreen> getTdashboardscreens() {
		return this.tdashboardscreens;
	}

	public void setTdashboardscreens(List<Tdashboardscreen> tdashboardscreens) {
		this.tdashboardscreens = tdashboardscreens;
	}

	public Tdashboardscreen addTdashboardscreen(Tdashboardscreen tdashboardscreen) {
		getTdashboardscreens().add(tdashboardscreen);
		tdashboardscreen.setTproject(this);

		return tdashboardscreen;
	}

	public Tdashboardscreen removeTdashboardscreen(Tdashboardscreen tdashboardscreen) {
		getTdashboardscreens().remove(tdashboardscreen);
		tdashboardscreen.setTproject(null);

		return tdashboardscreen;
	}

	public List<Tevent> getTevents() {
		return this.tevents;
	}

	public void setTevents(List<Tevent> tevents) {
		this.tevents = tevents;
	}

	public Tevent addTevent(Tevent tevent) {
		getTevents().add(tevent);
		tevent.setTproject(this);

		return tevent;
	}

	public Tevent removeTevent(Tevent tevent) {
		getTevents().remove(tevent);
		tevent.setTproject(null);

		return tevent;
	}

	public List<Texporttemplate> getTexporttemplates() {
		return this.texporttemplates;
	}

	public void setTexporttemplates(List<Texporttemplate> texporttemplates) {
		this.texporttemplates = texporttemplates;
	}

	public Texporttemplate addTexporttemplate(Texporttemplate texporttemplate) {
		getTexporttemplates().add(texporttemplate);
		texporttemplate.setTproject(this);

		return texporttemplate;
	}

	public Texporttemplate removeTexporttemplate(Texporttemplate texporttemplate) {
		getTexporttemplates().remove(texporttemplate);
		texporttemplate.setTproject(null);

		return texporttemplate;
	}

	public List<Tfieldconfig> getTfieldconfigs() {
		return this.tfieldconfigs;
	}

	public void setTfieldconfigs(List<Tfieldconfig> tfieldconfigs) {
		this.tfieldconfigs = tfieldconfigs;
	}

	public Tfieldconfig addTfieldconfig(Tfieldconfig tfieldconfig) {
		getTfieldconfigs().add(tfieldconfig);
		tfieldconfig.setTproject(this);

		return tfieldconfig;
	}

	public Tfieldconfig removeTfieldconfig(Tfieldconfig tfieldconfig) {
		getTfieldconfigs().remove(tfieldconfig);
		tfieldconfig.setTproject(null);

		return tfieldconfig;
	}

	public List<Tfiltercategory> getTfiltercategories() {
		return this.tfiltercategories;
	}

	public void setTfiltercategories(List<Tfiltercategory> tfiltercategories) {
		this.tfiltercategories = tfiltercategories;
	}

	public Tfiltercategory addTfiltercategory(Tfiltercategory tfiltercategory) {
		getTfiltercategories().add(tfiltercategory);
		tfiltercategory.setTproject(this);

		return tfiltercategory;
	}

	public Tfiltercategory removeTfiltercategory(Tfiltercategory tfiltercategory) {
		getTfiltercategories().remove(tfiltercategory);
		tfiltercategory.setTproject(null);

		return tfiltercategory;
	}

	public List<Tinitstate> getTinitstates() {
		return this.tinitstates;
	}

	public void setTinitstates(List<Tinitstate> tinitstates) {
		this.tinitstates = tinitstates;
	}

	public Tinitstate addTinitstate(Tinitstate tinitstate) {
		getTinitstates().add(tinitstate);
		tinitstate.setTproject(this);

		return tinitstate;
	}

	public Tinitstate removeTinitstate(Tinitstate tinitstate) {
		getTinitstates().remove(tinitstate);
		tinitstate.setTproject(null);

		return tinitstate;
	}

	public List<Tlist> getTlists() {
		return this.tlists;
	}

	public void setTlists(List<Tlist> tlists) {
		this.tlists = tlists;
	}

	public Tlist addTlist(Tlist tlist) {
		getTlists().add(tlist);
		tlist.setTproject(this);

		return tlist;
	}

	public Tlist removeTlist(Tlist tlist) {
		getTlists().remove(tlist);
		tlist.setTproject(null);

		return tlist;
	}

	public List<Tmailtemplateconfig> getTmailtemplateconfigs() {
		return this.tmailtemplateconfigs;
	}

	public void setTmailtemplateconfigs(List<Tmailtemplateconfig> tmailtemplateconfigs) {
		this.tmailtemplateconfigs = tmailtemplateconfigs;
	}

	public Tmailtemplateconfig addTmailtemplateconfig(Tmailtemplateconfig tmailtemplateconfig) {
		getTmailtemplateconfigs().add(tmailtemplateconfig);
		tmailtemplateconfig.setTproject(this);

		return tmailtemplateconfig;
	}

	public Tmailtemplateconfig removeTmailtemplateconfig(Tmailtemplateconfig tmailtemplateconfig) {
		getTmailtemplateconfigs().remove(tmailtemplateconfig);
		tmailtemplateconfig.setTproject(null);

		return tmailtemplateconfig;
	}

	public List<Tnotifysetting> getTnotifysettings() {
		return this.tnotifysettings;
	}

	public void setTnotifysettings(List<Tnotifysetting> tnotifysettings) {
		this.tnotifysettings = tnotifysettings;
	}

	public Tnotifysetting addTnotifysetting(Tnotifysetting tnotifysetting) {
		getTnotifysettings().add(tnotifysetting);
		tnotifysetting.setTproject(this);

		return tnotifysetting;
	}

	public Tnotifysetting removeTnotifysetting(Tnotifysetting tnotifysetting) {
		getTnotifysettings().remove(tnotifysetting);
		tnotifysetting.setTproject(null);

		return tnotifysetting;
	}

	public List<Torgprojectsla> getTorgprojectslas() {
		return this.torgprojectslas;
	}

	public void setTorgprojectslas(List<Torgprojectsla> torgprojectslas) {
		this.torgprojectslas = torgprojectslas;
	}

	public Torgprojectsla addTorgprojectsla(Torgprojectsla torgprojectsla) {
		getTorgprojectslas().add(torgprojectsla);
		torgprojectsla.setTproject(this);

		return torgprojectsla;
	}

	public Torgprojectsla removeTorgprojectsla(Torgprojectsla torgprojectsla) {
		getTorgprojectslas().remove(torgprojectsla);
		torgprojectsla.setTproject(null);

		return torgprojectsla;
	}

	public List<Tprojcat> getTprojcats() {
		return this.tprojcats;
	}

	public void setTprojcats(List<Tprojcat> tprojcats) {
		this.tprojcats = tprojcats;
	}

	public Tprojcat addTprojcat(Tprojcat tprojcat) {
		getTprojcats().add(tprojcat);
		tprojcat.setTproject(this);

		return tprojcat;
	}

	public Tprojcat removeTprojcat(Tprojcat tprojcat) {
		getTprojcats().remove(tprojcat);
		tprojcat.setTproject(null);

		return tprojcat;
	}

	public Tperson getTperson1() {
		return this.tperson1;
	}

	public void setTperson1(Tperson tperson1) {
		this.tperson1 = tperson1;
	}

	public Tperson getTperson2() {
		return this.tperson2;
	}

	public void setTperson2(Tperson tperson2) {
		this.tperson2 = tperson2;
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

	public Tsystemstate getTsystemstate() {
		return this.tsystemstate;
	}

	public void setTsystemstate(Tsystemstate tsystemstate) {
		this.tsystemstate = tsystemstate;
	}

	public Tproject getTproject() {
		return this.tproject;
	}

	public void setTproject(Tproject tproject) {
		this.tproject = tproject;
	}

	public List<Tproject> getTprojects() {
		return this.tprojects;
	}

	public void setTprojects(List<Tproject> tprojects) {
		this.tprojects = tprojects;
	}

	public Tproject addTproject(Tproject tproject) {
		getTprojects().add(tproject);
		tproject.setTproject(this);

		return tproject;
	}

	public Tproject removeTproject(Tproject tproject) {
		getTprojects().remove(tproject);
		tproject.setTproject(null);

		return tproject;
	}

	public List<Tprojectaccount> getTprojectaccounts() {
		return this.tprojectaccounts;
	}

	public void setTprojectaccounts(List<Tprojectaccount> tprojectaccounts) {
		this.tprojectaccounts = tprojectaccounts;
	}

	public Tprojectaccount addTprojectaccount(Tprojectaccount tprojectaccount) {
		getTprojectaccounts().add(tprojectaccount);
		tprojectaccount.setTproject(this);

		return tprojectaccount;
	}

	public Tprojectaccount removeTprojectaccount(Tprojectaccount tprojectaccount) {
		getTprojectaccounts().remove(tprojectaccount);
		tprojectaccount.setTproject(null);

		return tprojectaccount;
	}

	public List<Tprojectreportrepository> getTprojectreportrepositories() {
		return this.tprojectreportrepositories;
	}

	public void setTprojectreportrepositories(List<Tprojectreportrepository> tprojectreportrepositories) {
		this.tprojectreportrepositories = tprojectreportrepositories;
	}

	public Tprojectreportrepository addTprojectreportrepository(Tprojectreportrepository tprojectreportrepository) {
		getTprojectreportrepositories().add(tprojectreportrepository);
		tprojectreportrepository.setTproject(this);

		return tprojectreportrepository;
	}

	public Tprojectreportrepository removeTprojectreportrepository(Tprojectreportrepository tprojectreportrepository) {
		getTprojectreportrepositories().remove(tprojectreportrepository);
		tprojectreportrepository.setTproject(null);

		return tprojectreportrepository;
	}

	public List<Tqueryrepository> getTqueryrepositories() {
		return this.tqueryrepositories;
	}

	public void setTqueryrepositories(List<Tqueryrepository> tqueryrepositories) {
		this.tqueryrepositories = tqueryrepositories;
	}

	public Tqueryrepository addTqueryrepository(Tqueryrepository tqueryrepository) {
		getTqueryrepositories().add(tqueryrepository);
		tqueryrepository.setTproject(this);

		return tqueryrepository;
	}

	public Tqueryrepository removeTqueryrepository(Tqueryrepository tqueryrepository) {
		getTqueryrepositories().remove(tqueryrepository);
		tqueryrepository.setTproject(null);

		return tqueryrepository;
	}

	public List<Trelease> getTreleases() {
		return this.treleases;
	}

	public void setTreleases(List<Trelease> treleases) {
		this.treleases = treleases;
	}

	public Trelease addTreleas(Trelease treleas) {
		getTreleases().add(treleas);
		treleas.setTproject(this);

		return treleas;
	}

	public Trelease removeTreleas(Trelease treleas) {
		getTreleases().remove(treleas);
		treleas.setTproject(null);

		return treleas;
	}

	public List<Treportcategory> getTreportcategories() {
		return this.treportcategories;
	}

	public void setTreportcategories(List<Treportcategory> treportcategories) {
		this.treportcategories = treportcategories;
	}

	public Treportcategory addTreportcategory(Treportcategory treportcategory) {
		getTreportcategories().add(treportcategory);
		treportcategory.setTproject(this);

		return treportcategory;
	}

	public Treportcategory removeTreportcategory(Treportcategory treportcategory) {
		getTreportcategories().remove(treportcategory);
		treportcategory.setTproject(null);

		return treportcategory;
	}

	public List<Treportlayout> getTreportlayouts() {
		return this.treportlayouts;
	}

	public void setTreportlayouts(List<Treportlayout> treportlayouts) {
		this.treportlayouts = treportlayouts;
	}

	public Treportlayout addTreportlayout(Treportlayout treportlayout) {
		getTreportlayouts().add(treportlayout);
		treportlayout.setTproject(this);

		return treportlayout;
	}

	public Treportlayout removeTreportlayout(Treportlayout treportlayout) {
		getTreportlayouts().remove(treportlayout);
		treportlayout.setTproject(null);

		return treportlayout;
	}

	public List<Tscreenconfig> getTscreenconfigs() {
		return this.tscreenconfigs;
	}

	public void setTscreenconfigs(List<Tscreenconfig> tscreenconfigs) {
		this.tscreenconfigs = tscreenconfigs;
	}

	public Tscreenconfig addTscreenconfig(Tscreenconfig tscreenconfig) {
		getTscreenconfigs().add(tscreenconfig);
		tscreenconfig.setTproject(this);

		return tscreenconfig;
	}

	public Tscreenconfig removeTscreenconfig(Tscreenconfig tscreenconfig) {
		getTscreenconfigs().remove(tscreenconfig);
		tscreenconfig.setTproject(null);

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
		tscript.setTproject(this);

		return tscript;
	}

	public Tscript removeTscript(Tscript tscript) {
		getTscripts().remove(tscript);
		tscript.setTproject(null);

		return tscript;
	}

	public List<Tversioncontrolparameter> getTversioncontrolparameters() {
		return this.tversioncontrolparameters;
	}

	public void setTversioncontrolparameters(List<Tversioncontrolparameter> tversioncontrolparameters) {
		this.tversioncontrolparameters = tversioncontrolparameters;
	}

	public Tversioncontrolparameter addTversioncontrolparameter(Tversioncontrolparameter tversioncontrolparameter) {
		getTversioncontrolparameters().add(tversioncontrolparameter);
		tversioncontrolparameter.setTproject(this);

		return tversioncontrolparameter;
	}

	public Tversioncontrolparameter removeTversioncontrolparameter(Tversioncontrolparameter tversioncontrolparameter) {
		getTversioncontrolparameters().remove(tversioncontrolparameter);
		tversioncontrolparameter.setTproject(null);

		return tversioncontrolparameter;
	}

	public List<Tworkflowconnect> getTworkflowconnects() {
		return this.tworkflowconnects;
	}

	public void setTworkflowconnects(List<Tworkflowconnect> tworkflowconnects) {
		this.tworkflowconnects = tworkflowconnects;
	}

	public Tworkflowconnect addTworkflowconnect(Tworkflowconnect tworkflowconnect) {
		getTworkflowconnects().add(tworkflowconnect);
		tworkflowconnect.setTproject(this);

		return tworkflowconnect;
	}

	public Tworkflowconnect removeTworkflowconnect(Tworkflowconnect tworkflowconnect) {
		getTworkflowconnects().remove(tworkflowconnect);
		tworkflowconnect.setTproject(null);

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
		tworkitem.setTproject(this);

		return tworkitem;
	}

	public Tworkitem removeTworkitem(Tworkitem tworkitem) {
		getTworkitems().remove(tworkitem);
		tworkitem.setTproject(null);

		return tworkitem;
	}

}
