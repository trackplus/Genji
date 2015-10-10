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
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TWORKITEM database table.
 * 
 */
@Entity
@Table(name="TWORKITEM")
@TableGenerator(name="TWORKITEM_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_WORKITEM, allocationSize = 10)
public class Tworkitem extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TWORKITEM_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int workitemkey;

	private int accesslevel;

	@Temporal(TemporalType.TIMESTAMP)
	private Date actualenddate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date actualstartdate;

	private int archivelevel;

	@Column(length=25)
	private String build;

	@Column(nullable=false)
	private Timestamp created;

	@Temporal(TemporalType.TIMESTAMP)
	private Date enddate;

	private int escalationlevel;

	private int idnumber;

	@Column(nullable=false)
	private Timestamp lastedit;

	@Lob
	private String packagedescription;

	@Column(nullable=false, length=80)
	private String packagesynopsys;

	@Column(length=255)
	private String pspcode;

	@Column(length=20)
	private String reference;

	@Temporal(TemporalType.TIMESTAMP)
	private Date reminderdate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startdate;

	@Column(length=60)
	private String submitteremail;

	private int superiorworkitem;

	private int taskconstraint;

	@Temporal(TemporalType.TIMESTAMP)
	private Date taskconstraintdate;

	@Column(length=1)
	private String taskismilestone;

	@Column(length=1)
	private String taskissubproject;

	@Column(length=1)
	private String taskissummary;

	@Temporal(TemporalType.TIMESTAMP)
	private Date topdownenddate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date topdownstartdate;

	@Column(length=36)
	private String tpuuid;

	private int wbsonlevel;

	@Column(length=14)
	private String wlevel;

	//bi-directional many-to-one association to Tactualestimatedbudget
	@OneToMany(mappedBy="tworkitem")
	private List<Tactualestimatedbudget> tactualestimatedbudgets;

	//bi-directional many-to-one association to Tattachment
	@OneToMany(mappedBy="tworkitem")
	private List<Tattachment> tattachments;

	//bi-directional many-to-one association to Tattributevalue
	@OneToMany(mappedBy="tworkitem")
	private List<Tattributevalue> tattributevalues;

	//bi-directional many-to-one association to Tbaseline
	@OneToMany(mappedBy="tworkitem")
	private List<Tbaseline> tbaselines;

	//bi-directional many-to-one association to Tbudget
	@OneToMany(mappedBy="tworkitem")
	private List<Tbudget> tbudgets;

	//bi-directional many-to-one association to Tcomputedvalue
	@OneToMany(mappedBy="tworkitem")
	private List<Tcomputedvalue> tcomputedvalues;

	//bi-directional many-to-one association to Tcost
	@OneToMany(mappedBy="tworkitem")
	private List<Tcost> tcosts;

	//bi-directional many-to-one association to Thistorytransaction
	@OneToMany(mappedBy="tworkitem")
	private List<Thistorytransaction> thistorytransactions;

	//bi-directional many-to-one association to Tissueattributevalue
	@OneToMany(mappedBy="tworkitem")
	private List<Tissueattributevalue> tissueattributevalues;

	//bi-directional many-to-one association to Tlastvisiteditem
	@OneToMany(mappedBy="tworkitem")
	private List<Tlastvisiteditem> tlastvisiteditems;

	//bi-directional many-to-one association to Tmsprojecttask
	@OneToMany(mappedBy="tworkitem")
	private List<Tmsprojecttask> tmsprojecttasks;

	//bi-directional many-to-one association to Tnotify
	@OneToMany(mappedBy="tworkitem")
	private List<Tnotify> tnotifies;

	//bi-directional many-to-one association to Tpersonbasket
	@OneToMany(mappedBy="tworkitem")
	private List<Tpersonbasket> tpersonbaskets;

	//bi-directional many-to-one association to Treadissue
	@OneToMany(mappedBy="tworkitem")
	private List<Treadissue> treadissues;

	//bi-directional many-to-one association to Tstatechange
	@OneToMany(mappedBy="tworkitem")
	private List<Tstatechange> tstatechanges;

	//bi-directional many-to-one association to Tsummarymail
	@OneToMany(mappedBy="tworkitem")
	private List<Tsummarymail> tsummarymails;

	//bi-directional many-to-one association to Ttrail
	@OneToMany(mappedBy="tworkitem")
	private List<Ttrail> ttrails;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="OWNER", nullable=false)
	private Tperson tperson1;

	//bi-directional many-to-one association to Trelease
	@ManyToOne
	@JoinColumn(name="RELNOTICEDKEY")
	private Trelease trelease1;

	//bi-directional many-to-one association to Trelease
	@ManyToOne
	@JoinColumn(name="RELSCHEDULEDKEY")
	private Trelease trelease2;

	//bi-directional many-to-one association to Tstate
	@ManyToOne
	@JoinColumn(name="STATE")
	private Tstate tstate;

	//bi-directional many-to-one association to Tproject
	@ManyToOne
	@JoinColumn(name="PROJECTKEY")
	private Tproject tproject;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="CHANGEDBY", nullable=false)
	private Tperson tperson2;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="ORIGINATOR")
	private Tperson tperson3;

	//bi-directional many-to-one association to Tperson
	@ManyToOne
	@JoinColumn(name="RESPONSIBLE")
	private Tperson tperson4;

	//bi-directional many-to-one association to Tprojcat
	@ManyToOne
	@JoinColumn(name="PROJCATKEY")
	private Tprojcat tprojcat;

	//bi-directional many-to-one association to Tcategory
	@ManyToOne
	@JoinColumn(name="CATEGORYKEY", nullable=false)
	private Tcategory tcategory;

	//bi-directional many-to-one association to Tclass
	@ManyToOne
	@JoinColumn(name="CLASSKEY")
	private Tclass tclass;

	//bi-directional many-to-one association to Tpriority
	@ManyToOne
	@JoinColumn(name="PRIORITYKEY", nullable=false)
	private Tpriority tpriority;

	//bi-directional many-to-one association to Tseverity
	@ManyToOne
	@JoinColumn(name="SEVERITYKEY")
	private Tseverity tseverity;

	//bi-directional many-to-one association to Tworkitemlink
	@OneToMany(mappedBy="tworkitem1")
	private List<Tworkitemlink> tworkitemlinks1;

	//bi-directional many-to-one association to Tworkitemlink
	@OneToMany(mappedBy="tworkitem2")
	private List<Tworkitemlink> tworkitemlinks2;

	//bi-directional one-to-one association to Tworkitemlock
	@OneToOne(mappedBy="tworkitem")
	private Tworkitemlock tworkitemlock;

	public Tworkitem() {
	}


	public int getObjectid() {
		return this.workitemkey;
	}
	
	public void setObjectid(int key) {
		this.workitemkey = key;
	}
	
	public int getWorkitemkey() {
		return this.workitemkey;
	}

	public void setWorkitemkey(int workitemkey) {
		this.workitemkey = workitemkey;
	}

	public int getAccesslevel() {
		return this.accesslevel;
	}

	public void setAccesslevel(int accesslevel) {
		this.accesslevel = accesslevel;
	}

	public Date getActualenddate() {
		return this.actualenddate;
	}

	public void setActualenddate(Date actualenddate) {
		this.actualenddate = actualenddate;
	}

	public Date getActualstartdate() {
		return this.actualstartdate;
	}

	public void setActualstartdate(Date actualstartdate) {
		this.actualstartdate = actualstartdate;
	}

	public int getArchivelevel() {
		return this.archivelevel;
	}

	public void setArchivelevel(int archivelevel) {
		this.archivelevel = archivelevel;
	}

	public String getBuild() {
		return this.build;
	}

	public void setBuild(String build) {
		this.build = build;
	}

	public Timestamp getCreated() {
		return this.created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Date getEnddate() {
		return this.enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public int getEscalationlevel() {
		return this.escalationlevel;
	}

	public void setEscalationlevel(int escalationlevel) {
		this.escalationlevel = escalationlevel;
	}

	public int getIdnumber() {
		return this.idnumber;
	}

	public void setIdnumber(int idnumber) {
		this.idnumber = idnumber;
	}

	public Timestamp getLastedit() {
		return this.lastedit;
	}

	public void setLastedit(Timestamp lastedit) {
		this.lastedit = lastedit;
	}

	public String getPackagedescription() {
		return this.packagedescription;
	}

	public void setPackagedescription(String packagedescription) {
		this.packagedescription = packagedescription;
	}

	public String getPackagesynopsys() {
		return this.packagesynopsys;
	}

	public void setPackagesynopsys(String packagesynopsys) {
		this.packagesynopsys = packagesynopsys;
	}

	public String getPspcode() {
		return this.pspcode;
	}

	public void setPspcode(String pspcode) {
		this.pspcode = pspcode;
	}

	public String getReference() {
		return this.reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Date getReminderdate() {
		return this.reminderdate;
	}

	public void setReminderdate(Date reminderdate) {
		this.reminderdate = reminderdate;
	}

	public Date getStartdate() {
		return this.startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public String getSubmitteremail() {
		return this.submitteremail;
	}

	public void setSubmitteremail(String submitteremail) {
		this.submitteremail = submitteremail;
	}

	public int getSuperiorworkitem() {
		return this.superiorworkitem;
	}

	public void setSuperiorworkitem(int superiorworkitem) {
		this.superiorworkitem = superiorworkitem;
	}

	public int getTaskconstraint() {
		return this.taskconstraint;
	}

	public void setTaskconstraint(int taskconstraint) {
		this.taskconstraint = taskconstraint;
	}

	public Date getTaskconstraintdate() {
		return this.taskconstraintdate;
	}

	public void setTaskconstraintdate(Date taskconstraintdate) {
		this.taskconstraintdate = taskconstraintdate;
	}

	public String getTaskismilestone() {
		return this.taskismilestone;
	}

	public void setTaskismilestone(String taskismilestone) {
		this.taskismilestone = taskismilestone;
	}

	public String getTaskissubproject() {
		return this.taskissubproject;
	}

	public void setTaskissubproject(String taskissubproject) {
		this.taskissubproject = taskissubproject;
	}

	public String getTaskissummary() {
		return this.taskissummary;
	}

	public void setTaskissummary(String taskissummary) {
		this.taskissummary = taskissummary;
	}

	public Date getTopdownenddate() {
		return this.topdownenddate;
	}

	public void setTopdownenddate(Date topdownenddate) {
		this.topdownenddate = topdownenddate;
	}

	public Date getTopdownstartdate() {
		return this.topdownstartdate;
	}

	public void setTopdownstartdate(Date topdownstartdate) {
		this.topdownstartdate = topdownstartdate;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public int getWbsonlevel() {
		return this.wbsonlevel;
	}

	public void setWbsonlevel(int wbsonlevel) {
		this.wbsonlevel = wbsonlevel;
	}

	public String getWlevel() {
		return this.wlevel;
	}

	public void setWlevel(String wlevel) {
		this.wlevel = wlevel;
	}

	public List<Tactualestimatedbudget> getTactualestimatedbudgets() {
		return this.tactualestimatedbudgets;
	}

	public void setTactualestimatedbudgets(List<Tactualestimatedbudget> tactualestimatedbudgets) {
		this.tactualestimatedbudgets = tactualestimatedbudgets;
	}

	public Tactualestimatedbudget addTactualestimatedbudget(Tactualestimatedbudget tactualestimatedbudget) {
		getTactualestimatedbudgets().add(tactualestimatedbudget);
		tactualestimatedbudget.setTworkitem(this);

		return tactualestimatedbudget;
	}

	public Tactualestimatedbudget removeTactualestimatedbudget(Tactualestimatedbudget tactualestimatedbudget) {
		getTactualestimatedbudgets().remove(tactualestimatedbudget);
		tactualestimatedbudget.setTworkitem(null);

		return tactualestimatedbudget;
	}

	public List<Tattachment> getTattachments() {
		return this.tattachments;
	}

	public void setTattachments(List<Tattachment> tattachments) {
		this.tattachments = tattachments;
	}

	public Tattachment addTattachment(Tattachment tattachment) {
		getTattachments().add(tattachment);
		tattachment.setTworkitem(this);

		return tattachment;
	}

	public Tattachment removeTattachment(Tattachment tattachment) {
		getTattachments().remove(tattachment);
		tattachment.setTworkitem(null);

		return tattachment;
	}

	public List<Tattributevalue> getTattributevalues() {
		return this.tattributevalues;
	}

	public void setTattributevalues(List<Tattributevalue> tattributevalues) {
		this.tattributevalues = tattributevalues;
	}

	public Tattributevalue addTattributevalue(Tattributevalue tattributevalue) {
		getTattributevalues().add(tattributevalue);
		tattributevalue.setTworkitem(this);

		return tattributevalue;
	}

	public Tattributevalue removeTattributevalue(Tattributevalue tattributevalue) {
		getTattributevalues().remove(tattributevalue);
		tattributevalue.setTworkitem(null);

		return tattributevalue;
	}

	public List<Tbaseline> getTbaselines() {
		return this.tbaselines;
	}

	public void setTbaselines(List<Tbaseline> tbaselines) {
		this.tbaselines = tbaselines;
	}

	public Tbaseline addTbaseline(Tbaseline tbaseline) {
		getTbaselines().add(tbaseline);
		tbaseline.setTworkitem(this);

		return tbaseline;
	}

	public Tbaseline removeTbaseline(Tbaseline tbaseline) {
		getTbaselines().remove(tbaseline);
		tbaseline.setTworkitem(null);

		return tbaseline;
	}

	public List<Tbudget> getTbudgets() {
		return this.tbudgets;
	}

	public void setTbudgets(List<Tbudget> tbudgets) {
		this.tbudgets = tbudgets;
	}

	public Tbudget addTbudget(Tbudget tbudget) {
		getTbudgets().add(tbudget);
		tbudget.setTworkitem(this);

		return tbudget;
	}

	public Tbudget removeTbudget(Tbudget tbudget) {
		getTbudgets().remove(tbudget);
		tbudget.setTworkitem(null);

		return tbudget;
	}

	public List<Tcomputedvalue> getTcomputedvalues() {
		return this.tcomputedvalues;
	}

	public void setTcomputedvalues(List<Tcomputedvalue> tcomputedvalues) {
		this.tcomputedvalues = tcomputedvalues;
	}

	public Tcomputedvalue addTcomputedvalue(Tcomputedvalue tcomputedvalue) {
		getTcomputedvalues().add(tcomputedvalue);
		tcomputedvalue.setTworkitem(this);

		return tcomputedvalue;
	}

	public Tcomputedvalue removeTcomputedvalue(Tcomputedvalue tcomputedvalue) {
		getTcomputedvalues().remove(tcomputedvalue);
		tcomputedvalue.setTworkitem(null);

		return tcomputedvalue;
	}

	public List<Tcost> getTcosts() {
		return this.tcosts;
	}

	public void setTcosts(List<Tcost> tcosts) {
		this.tcosts = tcosts;
	}

	public Tcost addTcost(Tcost tcost) {
		getTcosts().add(tcost);
		tcost.setTworkitem(this);

		return tcost;
	}

	public Tcost removeTcost(Tcost tcost) {
		getTcosts().remove(tcost);
		tcost.setTworkitem(null);

		return tcost;
	}

	public List<Thistorytransaction> getThistorytransactions() {
		return this.thistorytransactions;
	}

	public void setThistorytransactions(List<Thistorytransaction> thistorytransactions) {
		this.thistorytransactions = thistorytransactions;
	}

	public Thistorytransaction addThistorytransaction(Thistorytransaction thistorytransaction) {
		getThistorytransactions().add(thistorytransaction);
		thistorytransaction.setTworkitem(this);

		return thistorytransaction;
	}

	public Thistorytransaction removeThistorytransaction(Thistorytransaction thistorytransaction) {
		getThistorytransactions().remove(thistorytransaction);
		thistorytransaction.setTworkitem(null);

		return thistorytransaction;
	}

	public List<Tissueattributevalue> getTissueattributevalues() {
		return this.tissueattributevalues;
	}

	public void setTissueattributevalues(List<Tissueattributevalue> tissueattributevalues) {
		this.tissueattributevalues = tissueattributevalues;
	}

	public Tissueattributevalue addTissueattributevalue(Tissueattributevalue tissueattributevalue) {
		getTissueattributevalues().add(tissueattributevalue);
		tissueattributevalue.setTworkitem(this);

		return tissueattributevalue;
	}

	public Tissueattributevalue removeTissueattributevalue(Tissueattributevalue tissueattributevalue) {
		getTissueattributevalues().remove(tissueattributevalue);
		tissueattributevalue.setTworkitem(null);

		return tissueattributevalue;
	}

	public List<Tlastvisiteditem> getTlastvisiteditems() {
		return this.tlastvisiteditems;
	}

	public void setTlastvisiteditems(List<Tlastvisiteditem> tlastvisiteditems) {
		this.tlastvisiteditems = tlastvisiteditems;
	}

	public Tlastvisiteditem addTlastvisiteditem(Tlastvisiteditem tlastvisiteditem) {
		getTlastvisiteditems().add(tlastvisiteditem);
		tlastvisiteditem.setTworkitem(this);

		return tlastvisiteditem;
	}

	public Tlastvisiteditem removeTlastvisiteditem(Tlastvisiteditem tlastvisiteditem) {
		getTlastvisiteditems().remove(tlastvisiteditem);
		tlastvisiteditem.setTworkitem(null);

		return tlastvisiteditem;
	}

	public List<Tmsprojecttask> getTmsprojecttasks() {
		return this.tmsprojecttasks;
	}

	public void setTmsprojecttasks(List<Tmsprojecttask> tmsprojecttasks) {
		this.tmsprojecttasks = tmsprojecttasks;
	}

	public Tmsprojecttask addTmsprojecttask(Tmsprojecttask tmsprojecttask) {
		getTmsprojecttasks().add(tmsprojecttask);
		tmsprojecttask.setTworkitem(this);

		return tmsprojecttask;
	}

	public Tmsprojecttask removeTmsprojecttask(Tmsprojecttask tmsprojecttask) {
		getTmsprojecttasks().remove(tmsprojecttask);
		tmsprojecttask.setTworkitem(null);

		return tmsprojecttask;
	}

	public List<Tnotify> getTnotifies() {
		return this.tnotifies;
	}

	public void setTnotifies(List<Tnotify> tnotifies) {
		this.tnotifies = tnotifies;
	}

	public Tnotify addTnotify(Tnotify tnotify) {
		getTnotifies().add(tnotify);
		tnotify.setTworkitem(this);

		return tnotify;
	}

	public Tnotify removeTnotify(Tnotify tnotify) {
		getTnotifies().remove(tnotify);
		tnotify.setTworkitem(null);

		return tnotify;
	}

	public List<Tpersonbasket> getTpersonbaskets() {
		return this.tpersonbaskets;
	}

	public void setTpersonbaskets(List<Tpersonbasket> tpersonbaskets) {
		this.tpersonbaskets = tpersonbaskets;
	}

	public Tpersonbasket addTpersonbasket(Tpersonbasket tpersonbasket) {
		getTpersonbaskets().add(tpersonbasket);
		tpersonbasket.setTworkitem(this);

		return tpersonbasket;
	}

	public Tpersonbasket removeTpersonbasket(Tpersonbasket tpersonbasket) {
		getTpersonbaskets().remove(tpersonbasket);
		tpersonbasket.setTworkitem(null);

		return tpersonbasket;
	}

	public List<Treadissue> getTreadissues() {
		return this.treadissues;
	}

	public void setTreadissues(List<Treadissue> treadissues) {
		this.treadissues = treadissues;
	}

	public Treadissue addTreadissue(Treadissue treadissue) {
		getTreadissues().add(treadissue);
		treadissue.setTworkitem(this);

		return treadissue;
	}

	public Treadissue removeTreadissue(Treadissue treadissue) {
		getTreadissues().remove(treadissue);
		treadissue.setTworkitem(null);

		return treadissue;
	}

	public List<Tstatechange> getTstatechanges() {
		return this.tstatechanges;
	}

	public void setTstatechanges(List<Tstatechange> tstatechanges) {
		this.tstatechanges = tstatechanges;
	}

	public Tstatechange addTstatechange(Tstatechange tstatechange) {
		getTstatechanges().add(tstatechange);
		tstatechange.setTworkitem(this);

		return tstatechange;
	}

	public Tstatechange removeTstatechange(Tstatechange tstatechange) {
		getTstatechanges().remove(tstatechange);
		tstatechange.setTworkitem(null);

		return tstatechange;
	}

	public List<Tsummarymail> getTsummarymails() {
		return this.tsummarymails;
	}

	public void setTsummarymails(List<Tsummarymail> tsummarymails) {
		this.tsummarymails = tsummarymails;
	}

	public Tsummarymail addTsummarymail(Tsummarymail tsummarymail) {
		getTsummarymails().add(tsummarymail);
		tsummarymail.setTworkitem(this);

		return tsummarymail;
	}

	public Tsummarymail removeTsummarymail(Tsummarymail tsummarymail) {
		getTsummarymails().remove(tsummarymail);
		tsummarymail.setTworkitem(null);

		return tsummarymail;
	}

	public List<Ttrail> getTtrails() {
		return this.ttrails;
	}

	public void setTtrails(List<Ttrail> ttrails) {
		this.ttrails = ttrails;
	}

	public Ttrail addTtrail(Ttrail ttrail) {
		getTtrails().add(ttrail);
		ttrail.setTworkitem(this);

		return ttrail;
	}

	public Ttrail removeTtrail(Ttrail ttrail) {
		getTtrails().remove(ttrail);
		ttrail.setTworkitem(null);

		return ttrail;
	}

	public Tperson getTperson1() {
		return this.tperson1;
	}

	public void setTperson1(Tperson tperson1) {
		this.tperson1 = tperson1;
	}

	public Trelease getTrelease1() {
		return this.trelease1;
	}

	public void setTrelease1(Trelease trelease1) {
		this.trelease1 = trelease1;
	}

	public Trelease getTrelease2() {
		return this.trelease2;
	}

	public void setTrelease2(Trelease trelease2) {
		this.trelease2 = trelease2;
	}

	public Tstate getTstate() {
		return this.tstate;
	}

	public void setTstate(Tstate tstate) {
		this.tstate = tstate;
	}

	public Tproject getTproject() {
		return this.tproject;
	}

	public void setTproject(Tproject tproject) {
		this.tproject = tproject;
	}

	public Tperson getTperson2() {
		return this.tperson2;
	}

	public void setTperson2(Tperson tperson2) {
		this.tperson2 = tperson2;
	}

	public Tperson getTperson3() {
		return this.tperson3;
	}

	public void setTperson3(Tperson tperson3) {
		this.tperson3 = tperson3;
	}

	public Tperson getTperson4() {
		return this.tperson4;
	}

	public void setTperson4(Tperson tperson4) {
		this.tperson4 = tperson4;
	}

	public Tprojcat getTprojcat() {
		return this.tprojcat;
	}

	public void setTprojcat(Tprojcat tprojcat) {
		this.tprojcat = tprojcat;
	}

	public Tcategory getTcategory() {
		return this.tcategory;
	}

	public void setTcategory(Tcategory tcategory) {
		this.tcategory = tcategory;
	}

	public Tclass getTclass() {
		return this.tclass;
	}

	public void setTclass(Tclass tclass) {
		this.tclass = tclass;
	}

	public Tpriority getTpriority() {
		return this.tpriority;
	}

	public void setTpriority(Tpriority tpriority) {
		this.tpriority = tpriority;
	}

	public Tseverity getTseverity() {
		return this.tseverity;
	}

	public void setTseverity(Tseverity tseverity) {
		this.tseverity = tseverity;
	}

	public List<Tworkitemlink> getTworkitemlinks1() {
		return this.tworkitemlinks1;
	}

	public void setTworkitemlinks1(List<Tworkitemlink> tworkitemlinks1) {
		this.tworkitemlinks1 = tworkitemlinks1;
	}

	public Tworkitemlink addTworkitemlinks1(Tworkitemlink tworkitemlinks1) {
		getTworkitemlinks1().add(tworkitemlinks1);
		tworkitemlinks1.setTworkitem1(this);

		return tworkitemlinks1;
	}

	public Tworkitemlink removeTworkitemlinks1(Tworkitemlink tworkitemlinks1) {
		getTworkitemlinks1().remove(tworkitemlinks1);
		tworkitemlinks1.setTworkitem1(null);

		return tworkitemlinks1;
	}

	public List<Tworkitemlink> getTworkitemlinks2() {
		return this.tworkitemlinks2;
	}

	public void setTworkitemlinks2(List<Tworkitemlink> tworkitemlinks2) {
		this.tworkitemlinks2 = tworkitemlinks2;
	}

	public Tworkitemlink addTworkitemlinks2(Tworkitemlink tworkitemlinks2) {
		getTworkitemlinks2().add(tworkitemlinks2);
		tworkitemlinks2.setTworkitem2(this);

		return tworkitemlinks2;
	}

	public Tworkitemlink removeTworkitemlinks2(Tworkitemlink tworkitemlinks2) {
		getTworkitemlinks2().remove(tworkitemlinks2);
		tworkitemlinks2.setTworkitem2(null);

		return tworkitemlinks2;
	}

	public Tworkitemlock getTworkitemlock() {
		return this.tworkitemlock;
	}

	public void setTworkitemlock(Tworkitemlock tworkitemlock) {
		this.tworkitemlock = tworkitemlock;
	}

}
