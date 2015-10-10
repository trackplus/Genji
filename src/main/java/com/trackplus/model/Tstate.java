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
 * The persistent class for the TSTATE database table.
 * 
 */
@Entity
@Table(name="TSTATE")
@TableGenerator(name="TSTATE_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_STATE, allocationSize = 10)
public class Tstate extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TSTATE_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int pkey;

	@Column(length=255)
	private String cssstyle;

	@Column(length=1)
	private String iconchanged;

	@Column(name="`LABEL`", nullable=false, length=25)
	private String label;

	private int percentcomplete;

	private int sortorder;

	private int stateflag;

	@Column(length=255)
	private String symbol;

	@Column(length=36)
	private String tpuuid;

	//bi-directional many-to-one association to Tescalationstate
	@OneToMany(mappedBy="tstate")
	private List<Tescalationstate> tescalationstates;

	//bi-directional many-to-one association to Tinitstate
	@OneToMany(mappedBy="tstate")
	private List<Tinitstate> tinitstates;

	//bi-directional many-to-one association to Tnotify
	@OneToMany(mappedBy="tstate")
	private List<Tnotify> tnotifies;

	//bi-directional many-to-one association to Tproject
	@OneToMany(mappedBy="tstate")
	private List<Tproject> tprojects;

	//bi-directional many-to-one association to Tpstate
	@OneToMany(mappedBy="tstate")
	private List<Tpstate> tpstates;

	//bi-directional many-to-one association to Tblob
	@ManyToOne
	@JoinColumn(name="ICONKEY")
	private Tblob tblob;

	//bi-directional many-to-one association to Tstatechange
	@OneToMany(mappedBy="tstate")
	private List<Tstatechange> tstatechanges;

	//bi-directional many-to-one association to Tworkflow
	@OneToMany(mappedBy="tstate1")
	private List<Tworkflow> tworkflows1;

	//bi-directional many-to-one association to Tworkflow
	@OneToMany(mappedBy="tstate2")
	private List<Tworkflow> tworkflows2;

	//bi-directional many-to-one association to Tworkflowstation
	@OneToMany(mappedBy="tstate")
	private List<Tworkflowstation> tworkflowstations;

	//bi-directional many-to-one association to Tworkitem
	@OneToMany(mappedBy="tstate")
	private List<Tworkitem> tworkitems;

	public Tstate() {
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

	public int getPercentcomplete() {
		return this.percentcomplete;
	}

	public void setPercentcomplete(int percentcomplete) {
		this.percentcomplete = percentcomplete;
	}

	public int getSortorder() {
		return this.sortorder;
	}

	public void setSortorder(int sortorder) {
		this.sortorder = sortorder;
	}

	public int getStateflag() {
		return this.stateflag;
	}

	public void setStateflag(int stateflag) {
		this.stateflag = stateflag;
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

	public List<Tescalationstate> getTescalationstates() {
		return this.tescalationstates;
	}

	public void setTescalationstates(List<Tescalationstate> tescalationstates) {
		this.tescalationstates = tescalationstates;
	}

	public Tescalationstate addTescalationstate(Tescalationstate tescalationstate) {
		getTescalationstates().add(tescalationstate);
		tescalationstate.setTstate(this);

		return tescalationstate;
	}

	public Tescalationstate removeTescalationstate(Tescalationstate tescalationstate) {
		getTescalationstates().remove(tescalationstate);
		tescalationstate.setTstate(null);

		return tescalationstate;
	}

	public List<Tinitstate> getTinitstates() {
		return this.tinitstates;
	}

	public void setTinitstates(List<Tinitstate> tinitstates) {
		this.tinitstates = tinitstates;
	}

	public Tinitstate addTinitstate(Tinitstate tinitstate) {
		getTinitstates().add(tinitstate);
		tinitstate.setTstate(this);

		return tinitstate;
	}

	public Tinitstate removeTinitstate(Tinitstate tinitstate) {
		getTinitstates().remove(tinitstate);
		tinitstate.setTstate(null);

		return tinitstate;
	}

	public List<Tnotify> getTnotifies() {
		return this.tnotifies;
	}

	public void setTnotifies(List<Tnotify> tnotifies) {
		this.tnotifies = tnotifies;
	}

	public Tnotify addTnotify(Tnotify tnotify) {
		getTnotifies().add(tnotify);
		tnotify.setTstate(this);

		return tnotify;
	}

	public Tnotify removeTnotify(Tnotify tnotify) {
		getTnotifies().remove(tnotify);
		tnotify.setTstate(null);

		return tnotify;
	}

	public List<Tproject> getTprojects() {
		return this.tprojects;
	}

	public void setTprojects(List<Tproject> tprojects) {
		this.tprojects = tprojects;
	}

	public Tproject addTproject(Tproject tproject) {
		getTprojects().add(tproject);
		tproject.setTstate(this);

		return tproject;
	}

	public Tproject removeTproject(Tproject tproject) {
		getTprojects().remove(tproject);
		tproject.setTstate(null);

		return tproject;
	}

	public List<Tpstate> getTpstates() {
		return this.tpstates;
	}

	public void setTpstates(List<Tpstate> tpstates) {
		this.tpstates = tpstates;
	}

	public Tpstate addTpstate(Tpstate tpstate) {
		getTpstates().add(tpstate);
		tpstate.setTstate(this);

		return tpstate;
	}

	public Tpstate removeTpstate(Tpstate tpstate) {
		getTpstates().remove(tpstate);
		tpstate.setTstate(null);

		return tpstate;
	}

	public Tblob getTblob() {
		return this.tblob;
	}

	public void setTblob(Tblob tblob) {
		this.tblob = tblob;
	}

	public List<Tstatechange> getTstatechanges() {
		return this.tstatechanges;
	}

	public void setTstatechanges(List<Tstatechange> tstatechanges) {
		this.tstatechanges = tstatechanges;
	}

	public Tstatechange addTstatechange(Tstatechange tstatechange) {
		getTstatechanges().add(tstatechange);
		tstatechange.setTstate(this);

		return tstatechange;
	}

	public Tstatechange removeTstatechange(Tstatechange tstatechange) {
		getTstatechanges().remove(tstatechange);
		tstatechange.setTstate(null);

		return tstatechange;
	}

	public List<Tworkflow> getTworkflows1() {
		return this.tworkflows1;
	}

	public void setTworkflows1(List<Tworkflow> tworkflows1) {
		this.tworkflows1 = tworkflows1;
	}

	public Tworkflow addTworkflows1(Tworkflow tworkflows1) {
		getTworkflows1().add(tworkflows1);
		tworkflows1.setTstate1(this);

		return tworkflows1;
	}

	public Tworkflow removeTworkflows1(Tworkflow tworkflows1) {
		getTworkflows1().remove(tworkflows1);
		tworkflows1.setTstate1(null);

		return tworkflows1;
	}

	public List<Tworkflow> getTworkflows2() {
		return this.tworkflows2;
	}

	public void setTworkflows2(List<Tworkflow> tworkflows2) {
		this.tworkflows2 = tworkflows2;
	}

	public Tworkflow addTworkflows2(Tworkflow tworkflows2) {
		getTworkflows2().add(tworkflows2);
		tworkflows2.setTstate2(this);

		return tworkflows2;
	}

	public Tworkflow removeTworkflows2(Tworkflow tworkflows2) {
		getTworkflows2().remove(tworkflows2);
		tworkflows2.setTstate2(null);

		return tworkflows2;
	}

	public List<Tworkflowstation> getTworkflowstations() {
		return this.tworkflowstations;
	}

	public void setTworkflowstations(List<Tworkflowstation> tworkflowstations) {
		this.tworkflowstations = tworkflowstations;
	}

	public Tworkflowstation addTworkflowstation(Tworkflowstation tworkflowstation) {
		getTworkflowstations().add(tworkflowstation);
		tworkflowstation.setTstate(this);

		return tworkflowstation;
	}

	public Tworkflowstation removeTworkflowstation(Tworkflowstation tworkflowstation) {
		getTworkflowstations().remove(tworkflowstation);
		tworkflowstation.setTstate(null);

		return tworkflowstation;
	}

	public List<Tworkitem> getTworkitems() {
		return this.tworkitems;
	}

	public void setTworkitems(List<Tworkitem> tworkitems) {
		this.tworkitems = tworkitems;
	}

	public Tworkitem addTworkitem(Tworkitem tworkitem) {
		getTworkitems().add(tworkitem);
		tworkitem.setTstate(this);

		return tworkitem;
	}

	public Tworkitem removeTworkitem(Tworkitem tworkitem) {
		getTworkitems().remove(tworkitem);
		tworkitem.setTstate(null);

		return tworkitem;
	}

}
