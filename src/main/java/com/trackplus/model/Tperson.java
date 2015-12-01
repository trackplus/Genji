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
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TPERSON database table.
 * 
 */
@Entity
@Table(name="TPERSON")
@TableGenerator(name="TPERSON_GEN", table="ID_TABLE",
  pkColumnName="ID_TABLE_ID",
  pkColumnValue=BaseEntity.TABLE_PERSON, allocationSize = 10)
public class Tperson extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="TPERSON_GEN",strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int pkey;

	@Column(length=255)
	private String callurl;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Column(length=1)
	private String deleted;

	@Column(length=60)
	private String email;

	private int emailfrequency;

	@Column(nullable=false)
	private Timestamp emaillastreminded;

	private int emaillead;

	@Column(length=1)
	private String emailremindme;

	private int emailremindplevel;

	private int emailremindslevel;

	@Column(length=30)
	private String employeeid;

	@Column(length=25)
	private String firstname;

	@Column(length=100)
	private String forgotpasswordkey;

	private double hoursperworkday;

	@Column(length=1)
	private String isgroup;

	@Column(nullable=false)
	private Timestamp lastedit;

	@Column(length=25)
	private String lastname;

	@Column(nullable=false, length=60)
	private String loginname;

	private int maxassigneditems;

	@Column(length=255)
	private String messengerurl;

	private int noemailsplease;

	@Column(length=160)
	private String passwd;

	@Column(length=18)
	private String phone;

	@Column(length=15)
	private String prefemailtype;

	@Lob
	private String preferences;

	@Column(length=10)
	private String preflocale;

	@Column(length=1)
	private String remindmeasmanager;

	@Column(length=1)
	private String remindmeasoriginator;

	@Column(length=1)
	private String remindmeasresponsible;

	@Column(length=80)
	private String salt;

	@Column(length=255)
	private String symbol;

	@Column(nullable=false)
	private Timestamp tokenexpdate;

	@Column(length=80)
	private String tokenpasswd;

	@Column(length=36)
	private String tpuuid;

	private int userlevel;

	@Temporal(TemporalType.TIMESTAMP)
	private Date validuntil;

	//bi-directional many-to-one association to Tacl
	@OneToMany(mappedBy="tperson")
	private List<Tacl> tacls;

	//bi-directional many-to-one association to Tactualestimatedbudget
	@OneToMany(mappedBy="tperson")
	private List<Tactualestimatedbudget> tactualestimatedbudgets;

	//bi-directional many-to-one association to Tattachment
	@OneToMany(mappedBy="tperson")
	private List<Tattachment> tattachments;

	//bi-directional many-to-one association to Tbaseline
	@OneToMany(mappedBy="tperson")
	private List<Tbaseline> tbaselines;

	//bi-directional many-to-one association to Tbasket
	@OneToMany(mappedBy="tperson")
	private List<Tbasket> tbaskets;

	//bi-directional many-to-one association to Tbudget
	@OneToMany(mappedBy="tperson")
	private List<Tbudget> tbudgets;

	//bi-directional many-to-one association to Tcomputedvalue
	@OneToMany(mappedBy="tperson")
	private List<Tcomputedvalue> tcomputedvalues;

	//bi-directional many-to-one association to Tcost
	@OneToMany(mappedBy="tperson")
	private List<Tcost> tcosts;

	//bi-directional many-to-one association to Tdashboardscreen
	@OneToMany(mappedBy="tperson")
	private List<Tdashboardscreen> tdashboardscreens;

	//bi-directional many-to-one association to Tescalationentry
	@OneToMany(mappedBy="tperson")
	private List<Tescalationentry> tescalationentries;

	//bi-directional many-to-one association to Texporttemplate
	@OneToMany(mappedBy="tperson")
	private List<Texporttemplate> texporttemplates;

	//bi-directional many-to-one association to Tfield
	@OneToMany(mappedBy="tperson")
	private List<Tfield> tfields;

	//bi-directional many-to-one association to Tfiltercategory
	@OneToMany(mappedBy="tperson")
	private List<Tfiltercategory> tfiltercategories;

	//bi-directional many-to-one association to Tgroupmember
	@OneToMany(mappedBy="tperson1")
	private List<Tgroupmember> tgroupmembers1;

	//bi-directional many-to-one association to Tgroupmember
	@OneToMany(mappedBy="tperson2")
	private List<Tgroupmember> tgroupmembers2;

	//bi-directional many-to-one association to Thistorytransaction
	@OneToMany(mappedBy="tperson")
	private List<Thistorytransaction> thistorytransactions;

	//bi-directional many-to-one association to Tissueattributevalue
	@OneToMany(mappedBy="tperson")
	private List<Tissueattributevalue> tissueattributevalues;

	//bi-directional many-to-one association to Tlastexecutedquery
	@OneToMany(mappedBy="tperson")
	private List<Tlastexecutedquery> tlastexecutedqueries;

	//bi-directional many-to-one association to Tlastvisiteditem
	@OneToMany(mappedBy="tperson")
	private List<Tlastvisiteditem> tlastvisiteditems;

	//bi-directional many-to-one association to Tlist
	@OneToMany(mappedBy="tperson")
	private List<Tlist> tlists;

	//bi-directional many-to-one association to Tloggedinuser
	@OneToMany(mappedBy="tperson")
	private List<Tloggedinuser> tloggedinusers;

	//bi-directional many-to-one association to Tmsprojectexchange
	@OneToMany(mappedBy="tperson")
	private List<Tmsprojectexchange> tmsprojectexchanges;

	//bi-directional many-to-one association to Tnotify
	@OneToMany(mappedBy="tperson")
	private List<Tnotify> tnotifies;

	//bi-directional many-to-one association to Tnotifysetting
	@OneToMany(mappedBy="tperson")
	private List<Tnotifysetting> tnotifysettings;

	//bi-directional many-to-one association to Tnotifytrigger
	@OneToMany(mappedBy="tperson")
	private List<Tnotifytrigger> tnotifytriggers;

	//bi-directional many-to-one association to Tdepartment
	@ManyToOne
	@JoinColumn(name="DEPKEY")
	private Tdepartment tdepartment;

	//bi-directional many-to-one association to Tprivatereportrepository
	@ManyToOne
	@JoinColumn(name="MYDEFAULTREPORT")
	private Tprivatereportrepository tprivatereportrepository;

	//bi-directional many-to-one association to Tblob
	@ManyToOne
	@JoinColumn(name="ICONKEY")
	private Tblob tblob;

	//bi-directional many-to-one association to Tpersonbasket
	@OneToMany(mappedBy="tperson")
	private List<Tpersonbasket> tpersonbaskets;

	//bi-directional many-to-one association to Tprivatereportrepository
	@OneToMany(mappedBy="tperson")
	private List<Tprivatereportrepository> tprivatereportrepositories;

	//bi-directional many-to-one association to Tproject
	@OneToMany(mappedBy="tperson1")
	private List<Tproject> tprojects1;

	//bi-directional many-to-one association to Tproject
	@OneToMany(mappedBy="tperson2")
	private List<Tproject> tprojects2;

	//bi-directional many-to-one association to Tpublicreportrepository
	@OneToMany(mappedBy="tperson")
	private List<Tpublicreportrepository> tpublicreportrepositories;

	//bi-directional many-to-one association to Tqueryrepository
	@OneToMany(mappedBy="tperson")
	private List<Tqueryrepository> tqueryrepositories;

	//bi-directional many-to-one association to Treadissue
	@OneToMany(mappedBy="tperson")
	private List<Treadissue> treadissues;

	//bi-directional many-to-one association to Treportcategory
	@OneToMany(mappedBy="tperson")
	private List<Treportcategory> treportcategories;

	//bi-directional many-to-one association to Treportlayout
	@OneToMany(mappedBy="tperson")
	private List<Treportlayout> treportlayouts;

	//bi-directional many-to-one association to Treportpersonsetting
	@OneToMany(mappedBy="tperson")
	private List<Treportpersonsetting> treportpersonsettings;


	//bi-directional many-to-one association to Tscreen
	@OneToMany(mappedBy="tperson")
	private List<Tscreen> tscreens;

	//bi-directional many-to-one association to Tscript
	@OneToMany(mappedBy="tperson")
	private List<Tscript> tscripts;

	//bi-directional many-to-one association to Tstatechange
	@OneToMany(mappedBy="tperson")
	private List<Tstatechange> tstatechanges;

	//bi-directional many-to-one association to Tsummarymail
	@OneToMany(mappedBy="tperson1")
	private List<Tsummarymail> tsummarymails1;

	//bi-directional many-to-one association to Tsummarymail
	@OneToMany(mappedBy="tperson2")
	private List<Tsummarymail> tsummarymails2;

	//bi-directional many-to-one association to Ttemplateperson
	@OneToMany(mappedBy="tperson")
	private List<Ttemplateperson> ttemplatepersons;

	//bi-directional many-to-one association to Ttrail
	@OneToMany(mappedBy="tperson")
	private List<Ttrail> ttrails;

	//bi-directional many-to-one association to Tworkflow
	@OneToMany(mappedBy="tperson")
	private List<Tworkflow> tworkflows;

	//bi-directional many-to-one association to Tworkflowactivity
	@OneToMany(mappedBy="tperson1")
	private List<Tworkflowactivity> tworkflowactivities1;

	//bi-directional many-to-one association to Tworkflowactivity
	@OneToMany(mappedBy="tperson2")
	private List<Tworkflowactivity> tworkflowactivities2;

	//bi-directional many-to-one association to Tworkflowdef
	@OneToMany(mappedBy="tperson")
	private List<Tworkflowdef> tworkflowdefs;

	//bi-directional many-to-one association to Tworkflowguard
	@OneToMany(mappedBy="tperson")
	private List<Tworkflowguard> tworkflowguards;

	//bi-directional many-to-one association to Tworkitem
	@OneToMany(mappedBy="tperson1")
	private List<Tworkitem> tworkitems1;

	//bi-directional many-to-one association to Tworkitem
	@OneToMany(mappedBy="tperson2")
	private List<Tworkitem> tworkitems2;

	//bi-directional many-to-one association to Tworkitem
	@OneToMany(mappedBy="tperson3")
	private List<Tworkitem> tworkitems3;

	//bi-directional many-to-one association to Tworkitem
	@OneToMany(mappedBy="tperson4")
	private List<Tworkitem> tworkitems4;

	//bi-directional many-to-one association to Tworkitemlink
	@OneToMany(mappedBy="tperson")
	private List<Tworkitemlink> tworkitemlinks;

	//bi-directional many-to-one association to Tworkitemlock
	@OneToMany(mappedBy="tperson")
	private List<Tworkitemlock> tworkitemlocks;

	public Tperson() {
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

	public String getCallurl() {
		return this.callurl;
	}

	public void setCallurl(String callurl) {
		this.callurl = callurl;
	}

	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getDeleted() {
		return this.deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getEmailfrequency() {
		return this.emailfrequency;
	}

	public void setEmailfrequency(int emailfrequency) {
		this.emailfrequency = emailfrequency;
	}

	public Timestamp getEmaillastreminded() {
		return this.emaillastreminded;
	}

	public void setEmaillastreminded(Timestamp emaillastreminded) {
		this.emaillastreminded = emaillastreminded;
	}

	public int getEmaillead() {
		return this.emaillead;
	}

	public void setEmaillead(int emaillead) {
		this.emaillead = emaillead;
	}

	public String getEmailremindme() {
		return this.emailremindme;
	}

	public void setEmailremindme(String emailremindme) {
		this.emailremindme = emailremindme;
	}

	public int getEmailremindplevel() {
		return this.emailremindplevel;
	}

	public void setEmailremindplevel(int emailremindplevel) {
		this.emailremindplevel = emailremindplevel;
	}

	public int getEmailremindslevel() {
		return this.emailremindslevel;
	}

	public void setEmailremindslevel(int emailremindslevel) {
		this.emailremindslevel = emailremindslevel;
	}

	public String getEmployeeid() {
		return this.employeeid;
	}

	public void setEmployeeid(String employeeid) {
		this.employeeid = employeeid;
	}

	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getForgotpasswordkey() {
		return this.forgotpasswordkey;
	}

	public void setForgotpasswordkey(String forgotpasswordkey) {
		this.forgotpasswordkey = forgotpasswordkey;
	}

	public double getHoursperworkday() {
		return this.hoursperworkday;
	}

	public void setHoursperworkday(double hoursperworkday) {
		this.hoursperworkday = hoursperworkday;
	}

	public String getIsgroup() {
		return this.isgroup;
	}

	public void setIsgroup(String isgroup) {
		this.isgroup = isgroup;
	}

	public Timestamp getLastedit() {
		return this.lastedit;
	}

	public void setLastedit(Timestamp lastedit) {
		this.lastedit = lastedit;
	}

	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getLoginname() {
		return this.loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public int getMaxassigneditems() {
		return this.maxassigneditems;
	}

	public void setMaxassigneditems(int maxassigneditems) {
		this.maxassigneditems = maxassigneditems;
	}

	public String getMessengerurl() {
		return this.messengerurl;
	}

	public void setMessengerurl(String messengerurl) {
		this.messengerurl = messengerurl;
	}

	public int getNoemailsplease() {
		return this.noemailsplease;
	}

	public void setNoemailsplease(int noemailsplease) {
		this.noemailsplease = noemailsplease;
	}

	public String getPasswd() {
		return this.passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPrefemailtype() {
		return this.prefemailtype;
	}

	public void setPrefemailtype(String prefemailtype) {
		this.prefemailtype = prefemailtype;
	}

	public String getPreferences() {
		return this.preferences;
	}

	public void setPreferences(String preferences) {
		this.preferences = preferences;
	}

	public String getPreflocale() {
		return this.preflocale;
	}

	public void setPreflocale(String preflocale) {
		this.preflocale = preflocale;
	}

	public String getRemindmeasmanager() {
		return this.remindmeasmanager;
	}

	public void setRemindmeasmanager(String remindmeasmanager) {
		this.remindmeasmanager = remindmeasmanager;
	}

	public String getRemindmeasoriginator() {
		return this.remindmeasoriginator;
	}

	public void setRemindmeasoriginator(String remindmeasoriginator) {
		this.remindmeasoriginator = remindmeasoriginator;
	}

	public String getRemindmeasresponsible() {
		return this.remindmeasresponsible;
	}

	public void setRemindmeasresponsible(String remindmeasresponsible) {
		this.remindmeasresponsible = remindmeasresponsible;
	}

	public String getSalt() {
		return this.salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getSymbol() {
		return this.symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Timestamp getTokenexpdate() {
		return this.tokenexpdate;
	}

	public void setTokenexpdate(Timestamp tokenexpdate) {
		this.tokenexpdate = tokenexpdate;
	}

	public String getTokenpasswd() {
		return this.tokenpasswd;
	}

	public void setTokenpasswd(String tokenpasswd) {
		this.tokenpasswd = tokenpasswd;
	}

	public String getTpuuid() {
		return this.tpuuid;
	}

	public void setTpuuid(String tpuuid) {
		this.tpuuid = tpuuid;
	}

	public int getUserlevel() {
		return this.userlevel;
	}

	public void setUserlevel(int userlevel) {
		this.userlevel = userlevel;
	}

	public Date getValiduntil() {
		return this.validuntil;
	}

	public void setValiduntil(Date validuntil) {
		this.validuntil = validuntil;
	}

	public List<Tacl> getTacls() {
		return this.tacls;
	}

	public void setTacls(List<Tacl> tacls) {
		this.tacls = tacls;
	}

	public Tacl addTacl(Tacl tacl) {
		getTacls().add(tacl);
		tacl.setTperson(this);

		return tacl;
	}

	public Tacl removeTacl(Tacl tacl) {
		getTacls().remove(tacl);
		tacl.setTperson(null);

		return tacl;
	}

	public List<Tactualestimatedbudget> getTactualestimatedbudgets() {
		return this.tactualestimatedbudgets;
	}

	public void setTactualestimatedbudgets(List<Tactualestimatedbudget> tactualestimatedbudgets) {
		this.tactualestimatedbudgets = tactualestimatedbudgets;
	}

	public Tactualestimatedbudget addTactualestimatedbudget(Tactualestimatedbudget tactualestimatedbudget) {
		getTactualestimatedbudgets().add(tactualestimatedbudget);
		tactualestimatedbudget.setTperson(this);

		return tactualestimatedbudget;
	}

	public Tactualestimatedbudget removeTactualestimatedbudget(Tactualestimatedbudget tactualestimatedbudget) {
		getTactualestimatedbudgets().remove(tactualestimatedbudget);
		tactualestimatedbudget.setTperson(null);

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
		tattachment.setTperson(this);

		return tattachment;
	}

	public Tattachment removeTattachment(Tattachment tattachment) {
		getTattachments().remove(tattachment);
		tattachment.setTperson(null);

		return tattachment;
	}

	public List<Tbaseline> getTbaselines() {
		return this.tbaselines;
	}

	public void setTbaselines(List<Tbaseline> tbaselines) {
		this.tbaselines = tbaselines;
	}

	public Tbaseline addTbaseline(Tbaseline tbaseline) {
		getTbaselines().add(tbaseline);
		tbaseline.setTperson(this);

		return tbaseline;
	}

	public Tbaseline removeTbaseline(Tbaseline tbaseline) {
		getTbaselines().remove(tbaseline);
		tbaseline.setTperson(null);

		return tbaseline;
	}

	public List<Tbasket> getTbaskets() {
		return this.tbaskets;
	}

	public void setTbaskets(List<Tbasket> tbaskets) {
		this.tbaskets = tbaskets;
	}

	public Tbasket addTbasket(Tbasket tbasket) {
		getTbaskets().add(tbasket);
		tbasket.setTperson(this);

		return tbasket;
	}

	public Tbasket removeTbasket(Tbasket tbasket) {
		getTbaskets().remove(tbasket);
		tbasket.setTperson(null);

		return tbasket;
	}

	public List<Tbudget> getTbudgets() {
		return this.tbudgets;
	}

	public void setTbudgets(List<Tbudget> tbudgets) {
		this.tbudgets = tbudgets;
	}

	public Tbudget addTbudget(Tbudget tbudget) {
		getTbudgets().add(tbudget);
		tbudget.setTperson(this);

		return tbudget;
	}

	public Tbudget removeTbudget(Tbudget tbudget) {
		getTbudgets().remove(tbudget);
		tbudget.setTperson(null);

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
		tcomputedvalue.setTperson(this);

		return tcomputedvalue;
	}

	public Tcomputedvalue removeTcomputedvalue(Tcomputedvalue tcomputedvalue) {
		getTcomputedvalues().remove(tcomputedvalue);
		tcomputedvalue.setTperson(null);

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
		tcost.setTperson(this);

		return tcost;
	}

	public Tcost removeTcost(Tcost tcost) {
		getTcosts().remove(tcost);
		tcost.setTperson(null);

		return tcost;
	}

	public List<Tdashboardscreen> getTdashboardscreens() {
		return this.tdashboardscreens;
	}

	public void setTdashboardscreens(List<Tdashboardscreen> tdashboardscreens) {
		this.tdashboardscreens = tdashboardscreens;
	}

	public Tdashboardscreen addTdashboardscreen(Tdashboardscreen tdashboardscreen) {
		getTdashboardscreens().add(tdashboardscreen);
		tdashboardscreen.setTperson(this);

		return tdashboardscreen;
	}

	public Tdashboardscreen removeTdashboardscreen(Tdashboardscreen tdashboardscreen) {
		getTdashboardscreens().remove(tdashboardscreen);
		tdashboardscreen.setTperson(null);

		return tdashboardscreen;
	}

	public List<Tescalationentry> getTescalationentries() {
		return this.tescalationentries;
	}

	public void setTescalationentries(List<Tescalationentry> tescalationentries) {
		this.tescalationentries = tescalationentries;
	}

	public Tescalationentry addTescalationentry(Tescalationentry tescalationentry) {
		getTescalationentries().add(tescalationentry);
		tescalationentry.setTperson(this);

		return tescalationentry;
	}

	public Tescalationentry removeTescalationentry(Tescalationentry tescalationentry) {
		getTescalationentries().remove(tescalationentry);
		tescalationentry.setTperson(null);

		return tescalationentry;
	}

	public List<Texporttemplate> getTexporttemplates() {
		return this.texporttemplates;
	}

	public void setTexporttemplates(List<Texporttemplate> texporttemplates) {
		this.texporttemplates = texporttemplates;
	}

	public Texporttemplate addTexporttemplate(Texporttemplate texporttemplate) {
		getTexporttemplates().add(texporttemplate);
		texporttemplate.setTperson(this);

		return texporttemplate;
	}

	public Texporttemplate removeTexporttemplate(Texporttemplate texporttemplate) {
		getTexporttemplates().remove(texporttemplate);
		texporttemplate.setTperson(null);

		return texporttemplate;
	}

	public List<Tfield> getTfields() {
		return this.tfields;
	}

	public void setTfields(List<Tfield> tfields) {
		this.tfields = tfields;
	}

	public Tfield addTfield(Tfield tfield) {
		getTfields().add(tfield);
		tfield.setTperson(this);

		return tfield;
	}

	public Tfield removeTfield(Tfield tfield) {
		getTfields().remove(tfield);
		tfield.setTperson(null);

		return tfield;
	}

	public List<Tfiltercategory> getTfiltercategories() {
		return this.tfiltercategories;
	}

	public void setTfiltercategories(List<Tfiltercategory> tfiltercategories) {
		this.tfiltercategories = tfiltercategories;
	}

	public Tfiltercategory addTfiltercategory(Tfiltercategory tfiltercategory) {
		getTfiltercategories().add(tfiltercategory);
		tfiltercategory.setTperson(this);

		return tfiltercategory;
	}

	public Tfiltercategory removeTfiltercategory(Tfiltercategory tfiltercategory) {
		getTfiltercategories().remove(tfiltercategory);
		tfiltercategory.setTperson(null);

		return tfiltercategory;
	}

	public List<Tgroupmember> getTgroupmembers1() {
		return this.tgroupmembers1;
	}

	public void setTgroupmembers1(List<Tgroupmember> tgroupmembers1) {
		this.tgroupmembers1 = tgroupmembers1;
	}

	public Tgroupmember addTgroupmembers1(Tgroupmember tgroupmembers1) {
		getTgroupmembers1().add(tgroupmembers1);
		tgroupmembers1.setTperson1(this);

		return tgroupmembers1;
	}

	public Tgroupmember removeTgroupmembers1(Tgroupmember tgroupmembers1) {
		getTgroupmembers1().remove(tgroupmembers1);
		tgroupmembers1.setTperson1(null);

		return tgroupmembers1;
	}

	public List<Tgroupmember> getTgroupmembers2() {
		return this.tgroupmembers2;
	}

	public void setTgroupmembers2(List<Tgroupmember> tgroupmembers2) {
		this.tgroupmembers2 = tgroupmembers2;
	}

	public Tgroupmember addTgroupmembers2(Tgroupmember tgroupmembers2) {
		getTgroupmembers2().add(tgroupmembers2);
		tgroupmembers2.setTperson2(this);

		return tgroupmembers2;
	}

	public Tgroupmember removeTgroupmembers2(Tgroupmember tgroupmembers2) {
		getTgroupmembers2().remove(tgroupmembers2);
		tgroupmembers2.setTperson2(null);

		return tgroupmembers2;
	}

	public List<Thistorytransaction> getThistorytransactions() {
		return this.thistorytransactions;
	}

	public void setThistorytransactions(List<Thistorytransaction> thistorytransactions) {
		this.thistorytransactions = thistorytransactions;
	}

	public Thistorytransaction addThistorytransaction(Thistorytransaction thistorytransaction) {
		getThistorytransactions().add(thistorytransaction);
		thistorytransaction.setTperson(this);

		return thistorytransaction;
	}

	public Thistorytransaction removeThistorytransaction(Thistorytransaction thistorytransaction) {
		getThistorytransactions().remove(thistorytransaction);
		thistorytransaction.setTperson(null);

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
		tissueattributevalue.setTperson(this);

		return tissueattributevalue;
	}

	public Tissueattributevalue removeTissueattributevalue(Tissueattributevalue tissueattributevalue) {
		getTissueattributevalues().remove(tissueattributevalue);
		tissueattributevalue.setTperson(null);

		return tissueattributevalue;
	}

	public List<Tlastexecutedquery> getTlastexecutedqueries() {
		return this.tlastexecutedqueries;
	}

	public void setTlastexecutedqueries(List<Tlastexecutedquery> tlastexecutedqueries) {
		this.tlastexecutedqueries = tlastexecutedqueries;
	}

	public Tlastexecutedquery addTlastexecutedquery(Tlastexecutedquery tlastexecutedquery) {
		getTlastexecutedqueries().add(tlastexecutedquery);
		tlastexecutedquery.setTperson(this);

		return tlastexecutedquery;
	}

	public Tlastexecutedquery removeTlastexecutedquery(Tlastexecutedquery tlastexecutedquery) {
		getTlastexecutedqueries().remove(tlastexecutedquery);
		tlastexecutedquery.setTperson(null);

		return tlastexecutedquery;
	}

	public List<Tlastvisiteditem> getTlastvisiteditems() {
		return this.tlastvisiteditems;
	}

	public void setTlastvisiteditems(List<Tlastvisiteditem> tlastvisiteditems) {
		this.tlastvisiteditems = tlastvisiteditems;
	}

	public Tlastvisiteditem addTlastvisiteditem(Tlastvisiteditem tlastvisiteditem) {
		getTlastvisiteditems().add(tlastvisiteditem);
		tlastvisiteditem.setTperson(this);

		return tlastvisiteditem;
	}

	public Tlastvisiteditem removeTlastvisiteditem(Tlastvisiteditem tlastvisiteditem) {
		getTlastvisiteditems().remove(tlastvisiteditem);
		tlastvisiteditem.setTperson(null);

		return tlastvisiteditem;
	}

	public List<Tlist> getTlists() {
		return this.tlists;
	}

	public void setTlists(List<Tlist> tlists) {
		this.tlists = tlists;
	}

	public Tlist addTlist(Tlist tlist) {
		getTlists().add(tlist);
		tlist.setTperson(this);

		return tlist;
	}

	public Tlist removeTlist(Tlist tlist) {
		getTlists().remove(tlist);
		tlist.setTperson(null);

		return tlist;
	}

	public List<Tloggedinuser> getTloggedinusers() {
		return this.tloggedinusers;
	}

	public void setTloggedinusers(List<Tloggedinuser> tloggedinusers) {
		this.tloggedinusers = tloggedinusers;
	}

	public Tloggedinuser addTloggedinuser(Tloggedinuser tloggedinuser) {
		getTloggedinusers().add(tloggedinuser);
		tloggedinuser.setTperson(this);

		return tloggedinuser;
	}

	public Tloggedinuser removeTloggedinuser(Tloggedinuser tloggedinuser) {
		getTloggedinusers().remove(tloggedinuser);
		tloggedinuser.setTperson(null);

		return tloggedinuser;
	}

	public List<Tmsprojectexchange> getTmsprojectexchanges() {
		return this.tmsprojectexchanges;
	}

	public void setTmsprojectexchanges(List<Tmsprojectexchange> tmsprojectexchanges) {
		this.tmsprojectexchanges = tmsprojectexchanges;
	}

	public Tmsprojectexchange addTmsprojectexchange(Tmsprojectexchange tmsprojectexchange) {
		getTmsprojectexchanges().add(tmsprojectexchange);
		tmsprojectexchange.setTperson(this);

		return tmsprojectexchange;
	}

	public Tmsprojectexchange removeTmsprojectexchange(Tmsprojectexchange tmsprojectexchange) {
		getTmsprojectexchanges().remove(tmsprojectexchange);
		tmsprojectexchange.setTperson(null);

		return tmsprojectexchange;
	}

	public List<Tnotify> getTnotifies() {
		return this.tnotifies;
	}

	public void setTnotifies(List<Tnotify> tnotifies) {
		this.tnotifies = tnotifies;
	}

	public Tnotify addTnotify(Tnotify tnotify) {
		getTnotifies().add(tnotify);
		tnotify.setTperson(this);

		return tnotify;
	}

	public Tnotify removeTnotify(Tnotify tnotify) {
		getTnotifies().remove(tnotify);
		tnotify.setTperson(null);

		return tnotify;
	}

	public List<Tnotifysetting> getTnotifysettings() {
		return this.tnotifysettings;
	}

	public void setTnotifysettings(List<Tnotifysetting> tnotifysettings) {
		this.tnotifysettings = tnotifysettings;
	}

	public Tnotifysetting addTnotifysetting(Tnotifysetting tnotifysetting) {
		getTnotifysettings().add(tnotifysetting);
		tnotifysetting.setTperson(this);

		return tnotifysetting;
	}

	public Tnotifysetting removeTnotifysetting(Tnotifysetting tnotifysetting) {
		getTnotifysettings().remove(tnotifysetting);
		tnotifysetting.setTperson(null);

		return tnotifysetting;
	}

	public List<Tnotifytrigger> getTnotifytriggers() {
		return this.tnotifytriggers;
	}

	public void setTnotifytriggers(List<Tnotifytrigger> tnotifytriggers) {
		this.tnotifytriggers = tnotifytriggers;
	}

	public Tnotifytrigger addTnotifytrigger(Tnotifytrigger tnotifytrigger) {
		getTnotifytriggers().add(tnotifytrigger);
		tnotifytrigger.setTperson(this);

		return tnotifytrigger;
	}

	public Tnotifytrigger removeTnotifytrigger(Tnotifytrigger tnotifytrigger) {
		getTnotifytriggers().remove(tnotifytrigger);
		tnotifytrigger.setTperson(null);

		return tnotifytrigger;
	}

	public Tdepartment getTdepartment() {
		return this.tdepartment;
	}

	public void setTdepartment(Tdepartment tdepartment) {
		this.tdepartment = tdepartment;
	}

	public Tprivatereportrepository getTprivatereportrepository() {
		return this.tprivatereportrepository;
	}

	public void setTprivatereportrepository(Tprivatereportrepository tprivatereportrepository) {
		this.tprivatereportrepository = tprivatereportrepository;
	}

	public Tblob getTblob() {
		return this.tblob;
	}

	public void setTblob(Tblob tblob) {
		this.tblob = tblob;
	}

	public List<Tpersonbasket> getTpersonbaskets() {
		return this.tpersonbaskets;
	}

	public void setTpersonbaskets(List<Tpersonbasket> tpersonbaskets) {
		this.tpersonbaskets = tpersonbaskets;
	}

	public Tpersonbasket addTpersonbasket(Tpersonbasket tpersonbasket) {
		getTpersonbaskets().add(tpersonbasket);
		tpersonbasket.setTperson(this);

		return tpersonbasket;
	}

	public Tpersonbasket removeTpersonbasket(Tpersonbasket tpersonbasket) {
		getTpersonbaskets().remove(tpersonbasket);
		tpersonbasket.setTperson(null);

		return tpersonbasket;
	}

	public List<Tprivatereportrepository> getTprivatereportrepositories() {
		return this.tprivatereportrepositories;
	}

	public void setTprivatereportrepositories(List<Tprivatereportrepository> tprivatereportrepositories) {
		this.tprivatereportrepositories = tprivatereportrepositories;
	}

	public Tprivatereportrepository addTprivatereportrepository(Tprivatereportrepository tprivatereportrepository) {
		getTprivatereportrepositories().add(tprivatereportrepository);
		tprivatereportrepository.setTperson(this);

		return tprivatereportrepository;
	}

	public Tprivatereportrepository removeTprivatereportrepository(Tprivatereportrepository tprivatereportrepository) {
		getTprivatereportrepositories().remove(tprivatereportrepository);
		tprivatereportrepository.setTperson(null);

		return tprivatereportrepository;
	}

	public List<Tproject> getTprojects1() {
		return this.tprojects1;
	}

	public void setTprojects1(List<Tproject> tprojects1) {
		this.tprojects1 = tprojects1;
	}

	public Tproject addTprojects1(Tproject tprojects1) {
		getTprojects1().add(tprojects1);
		tprojects1.setTperson1(this);

		return tprojects1;
	}

	public Tproject removeTprojects1(Tproject tprojects1) {
		getTprojects1().remove(tprojects1);
		tprojects1.setTperson1(null);

		return tprojects1;
	}

	public List<Tproject> getTprojects2() {
		return this.tprojects2;
	}

	public void setTprojects2(List<Tproject> tprojects2) {
		this.tprojects2 = tprojects2;
	}

	public Tproject addTprojects2(Tproject tprojects2) {
		getTprojects2().add(tprojects2);
		tprojects2.setTperson2(this);

		return tprojects2;
	}

	public Tproject removeTprojects2(Tproject tprojects2) {
		getTprojects2().remove(tprojects2);
		tprojects2.setTperson2(null);

		return tprojects2;
	}

	public List<Tpublicreportrepository> getTpublicreportrepositories() {
		return this.tpublicreportrepositories;
	}

	public void setTpublicreportrepositories(List<Tpublicreportrepository> tpublicreportrepositories) {
		this.tpublicreportrepositories = tpublicreportrepositories;
	}

	public Tpublicreportrepository addTpublicreportrepository(Tpublicreportrepository tpublicreportrepository) {
		getTpublicreportrepositories().add(tpublicreportrepository);
		tpublicreportrepository.setTperson(this);

		return tpublicreportrepository;
	}

	public Tpublicreportrepository removeTpublicreportrepository(Tpublicreportrepository tpublicreportrepository) {
		getTpublicreportrepositories().remove(tpublicreportrepository);
		tpublicreportrepository.setTperson(null);

		return tpublicreportrepository;
	}

	public List<Tqueryrepository> getTqueryrepositories() {
		return this.tqueryrepositories;
	}

	public void setTqueryrepositories(List<Tqueryrepository> tqueryrepositories) {
		this.tqueryrepositories = tqueryrepositories;
	}

	public Tqueryrepository addTqueryrepository(Tqueryrepository tqueryrepository) {
		getTqueryrepositories().add(tqueryrepository);
		tqueryrepository.setTperson(this);

		return tqueryrepository;
	}

	public Tqueryrepository removeTqueryrepository(Tqueryrepository tqueryrepository) {
		getTqueryrepositories().remove(tqueryrepository);
		tqueryrepository.setTperson(null);

		return tqueryrepository;
	}

	public List<Treadissue> getTreadissues() {
		return this.treadissues;
	}

	public void setTreadissues(List<Treadissue> treadissues) {
		this.treadissues = treadissues;
	}

	public Treadissue addTreadissue(Treadissue treadissue) {
		getTreadissues().add(treadissue);
		treadissue.setTperson(this);

		return treadissue;
	}

	public Treadissue removeTreadissue(Treadissue treadissue) {
		getTreadissues().remove(treadissue);
		treadissue.setTperson(null);

		return treadissue;
	}

	public List<Treportcategory> getTreportcategories() {
		return this.treportcategories;
	}

	public void setTreportcategories(List<Treportcategory> treportcategories) {
		this.treportcategories = treportcategories;
	}

	public Treportcategory addTreportcategory(Treportcategory treportcategory) {
		getTreportcategories().add(treportcategory);
		treportcategory.setTperson(this);

		return treportcategory;
	}

	public Treportcategory removeTreportcategory(Treportcategory treportcategory) {
		getTreportcategories().remove(treportcategory);
		treportcategory.setTperson(null);

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
		treportlayout.setTperson(this);

		return treportlayout;
	}

	public Treportlayout removeTreportlayout(Treportlayout treportlayout) {
		getTreportlayouts().remove(treportlayout);
		treportlayout.setTperson(null);

		return treportlayout;
	}

	public List<Treportpersonsetting> getTreportpersonsettings() {
		return this.treportpersonsettings;
	}

	public void setTreportpersonsettings(List<Treportpersonsetting> treportpersonsettings) {
		this.treportpersonsettings = treportpersonsettings;
	}

	public Treportpersonsetting addTreportpersonsetting(Treportpersonsetting treportpersonsetting) {
		getTreportpersonsettings().add(treportpersonsetting);
		treportpersonsetting.setTperson(this);

		return treportpersonsetting;
	}

	public Treportpersonsetting removeTreportpersonsetting(Treportpersonsetting treportpersonsetting) {
		getTreportpersonsettings().remove(treportpersonsetting);
		treportpersonsetting.setTperson(null);

		return treportpersonsetting;
	}

	public List<Tscreen> getTscreens() {
		return this.tscreens;
	}

	public void setTscreens(List<Tscreen> tscreens) {
		this.tscreens = tscreens;
	}

	public Tscreen addTscreen(Tscreen tscreen) {
		getTscreens().add(tscreen);
		tscreen.setTperson(this);

		return tscreen;
	}

	public Tscreen removeTscreen(Tscreen tscreen) {
		getTscreens().remove(tscreen);
		tscreen.setTperson(null);

		return tscreen;
	}

	public List<Tscript> getTscripts() {
		return this.tscripts;
	}

	public void setTscripts(List<Tscript> tscripts) {
		this.tscripts = tscripts;
	}

	public Tscript addTscript(Tscript tscript) {
		getTscripts().add(tscript);
		tscript.setTperson(this);

		return tscript;
	}

	public Tscript removeTscript(Tscript tscript) {
		getTscripts().remove(tscript);
		tscript.setTperson(null);

		return tscript;
	}

	public List<Tstatechange> getTstatechanges() {
		return this.tstatechanges;
	}

	public void setTstatechanges(List<Tstatechange> tstatechanges) {
		this.tstatechanges = tstatechanges;
	}

	public Tstatechange addTstatechange(Tstatechange tstatechange) {
		getTstatechanges().add(tstatechange);
		tstatechange.setTperson(this);

		return tstatechange;
	}

	public Tstatechange removeTstatechange(Tstatechange tstatechange) {
		getTstatechanges().remove(tstatechange);
		tstatechange.setTperson(null);

		return tstatechange;
	}

	public List<Tsummarymail> getTsummarymails1() {
		return this.tsummarymails1;
	}

	public void setTsummarymails1(List<Tsummarymail> tsummarymails1) {
		this.tsummarymails1 = tsummarymails1;
	}

	public Tsummarymail addTsummarymails1(Tsummarymail tsummarymails1) {
		getTsummarymails1().add(tsummarymails1);
		tsummarymails1.setTperson1(this);

		return tsummarymails1;
	}

	public Tsummarymail removeTsummarymails1(Tsummarymail tsummarymails1) {
		getTsummarymails1().remove(tsummarymails1);
		tsummarymails1.setTperson1(null);

		return tsummarymails1;
	}

	public List<Tsummarymail> getTsummarymails2() {
		return this.tsummarymails2;
	}

	public void setTsummarymails2(List<Tsummarymail> tsummarymails2) {
		this.tsummarymails2 = tsummarymails2;
	}

	public Tsummarymail addTsummarymails2(Tsummarymail tsummarymails2) {
		getTsummarymails2().add(tsummarymails2);
		tsummarymails2.setTperson2(this);

		return tsummarymails2;
	}

	public Tsummarymail removeTsummarymails2(Tsummarymail tsummarymails2) {
		getTsummarymails2().remove(tsummarymails2);
		tsummarymails2.setTperson2(null);

		return tsummarymails2;
	}

	public List<Ttemplateperson> getTtemplatepersons() {
		return this.ttemplatepersons;
	}

	public void setTtemplatepersons(List<Ttemplateperson> ttemplatepersons) {
		this.ttemplatepersons = ttemplatepersons;
	}

	public Ttemplateperson addTtemplateperson(Ttemplateperson ttemplateperson) {
		getTtemplatepersons().add(ttemplateperson);
		ttemplateperson.setTperson(this);

		return ttemplateperson;
	}

	public Ttemplateperson removeTtemplateperson(Ttemplateperson ttemplateperson) {
		getTtemplatepersons().remove(ttemplateperson);
		ttemplateperson.setTperson(null);

		return ttemplateperson;
	}

	public List<Ttrail> getTtrails() {
		return this.ttrails;
	}

	public void setTtrails(List<Ttrail> ttrails) {
		this.ttrails = ttrails;
	}

	public Ttrail addTtrail(Ttrail ttrail) {
		getTtrails().add(ttrail);
		ttrail.setTperson(this);

		return ttrail;
	}

	public Ttrail removeTtrail(Ttrail ttrail) {
		getTtrails().remove(ttrail);
		ttrail.setTperson(null);

		return ttrail;
	}

	public List<Tworkflow> getTworkflows() {
		return this.tworkflows;
	}

	public void setTworkflows(List<Tworkflow> tworkflows) {
		this.tworkflows = tworkflows;
	}

	public Tworkflow addTworkflow(Tworkflow tworkflow) {
		getTworkflows().add(tworkflow);
		tworkflow.setTperson(this);

		return tworkflow;
	}

	public Tworkflow removeTworkflow(Tworkflow tworkflow) {
		getTworkflows().remove(tworkflow);
		tworkflow.setTperson(null);

		return tworkflow;
	}

	public List<Tworkflowactivity> getTworkflowactivities1() {
		return this.tworkflowactivities1;
	}

	public void setTworkflowactivities1(List<Tworkflowactivity> tworkflowactivities1) {
		this.tworkflowactivities1 = tworkflowactivities1;
	}

	public Tworkflowactivity addTworkflowactivities1(Tworkflowactivity tworkflowactivities1) {
		getTworkflowactivities1().add(tworkflowactivities1);
		tworkflowactivities1.setTperson1(this);

		return tworkflowactivities1;
	}

	public Tworkflowactivity removeTworkflowactivities1(Tworkflowactivity tworkflowactivities1) {
		getTworkflowactivities1().remove(tworkflowactivities1);
		tworkflowactivities1.setTperson1(null);

		return tworkflowactivities1;
	}

	public List<Tworkflowactivity> getTworkflowactivities2() {
		return this.tworkflowactivities2;
	}

	public void setTworkflowactivities2(List<Tworkflowactivity> tworkflowactivities2) {
		this.tworkflowactivities2 = tworkflowactivities2;
	}

	public Tworkflowactivity addTworkflowactivities2(Tworkflowactivity tworkflowactivities2) {
		getTworkflowactivities2().add(tworkflowactivities2);
		tworkflowactivities2.setTperson2(this);

		return tworkflowactivities2;
	}

	public Tworkflowactivity removeTworkflowactivities2(Tworkflowactivity tworkflowactivities2) {
		getTworkflowactivities2().remove(tworkflowactivities2);
		tworkflowactivities2.setTperson2(null);

		return tworkflowactivities2;
	}

	public List<Tworkflowdef> getTworkflowdefs() {
		return this.tworkflowdefs;
	}

	public void setTworkflowdefs(List<Tworkflowdef> tworkflowdefs) {
		this.tworkflowdefs = tworkflowdefs;
	}

	public Tworkflowdef addTworkflowdef(Tworkflowdef tworkflowdef) {
		getTworkflowdefs().add(tworkflowdef);
		tworkflowdef.setTperson(this);

		return tworkflowdef;
	}

	public Tworkflowdef removeTworkflowdef(Tworkflowdef tworkflowdef) {
		getTworkflowdefs().remove(tworkflowdef);
		tworkflowdef.setTperson(null);

		return tworkflowdef;
	}

	public List<Tworkflowguard> getTworkflowguards() {
		return this.tworkflowguards;
	}

	public void setTworkflowguards(List<Tworkflowguard> tworkflowguards) {
		this.tworkflowguards = tworkflowguards;
	}

	public Tworkflowguard addTworkflowguard(Tworkflowguard tworkflowguard) {
		getTworkflowguards().add(tworkflowguard);
		tworkflowguard.setTperson(this);

		return tworkflowguard;
	}

	public Tworkflowguard removeTworkflowguard(Tworkflowguard tworkflowguard) {
		getTworkflowguards().remove(tworkflowguard);
		tworkflowguard.setTperson(null);

		return tworkflowguard;
	}

	public List<Tworkitem> getTworkitems1() {
		return this.tworkitems1;
	}

	public void setTworkitems1(List<Tworkitem> tworkitems1) {
		this.tworkitems1 = tworkitems1;
	}

	public Tworkitem addTworkitems1(Tworkitem tworkitems1) {
		getTworkitems1().add(tworkitems1);
		tworkitems1.setTperson1(this);

		return tworkitems1;
	}

	public Tworkitem removeTworkitems1(Tworkitem tworkitems1) {
		getTworkitems1().remove(tworkitems1);
		tworkitems1.setTperson1(null);

		return tworkitems1;
	}

	public List<Tworkitem> getTworkitems2() {
		return this.tworkitems2;
	}

	public void setTworkitems2(List<Tworkitem> tworkitems2) {
		this.tworkitems2 = tworkitems2;
	}

	public Tworkitem addTworkitems2(Tworkitem tworkitems2) {
		getTworkitems2().add(tworkitems2);
		tworkitems2.setTperson2(this);

		return tworkitems2;
	}

	public Tworkitem removeTworkitems2(Tworkitem tworkitems2) {
		getTworkitems2().remove(tworkitems2);
		tworkitems2.setTperson2(null);

		return tworkitems2;
	}

	public List<Tworkitem> getTworkitems3() {
		return this.tworkitems3;
	}

	public void setTworkitems3(List<Tworkitem> tworkitems3) {
		this.tworkitems3 = tworkitems3;
	}

	public Tworkitem addTworkitems3(Tworkitem tworkitems3) {
		getTworkitems3().add(tworkitems3);
		tworkitems3.setTperson3(this);

		return tworkitems3;
	}

	public Tworkitem removeTworkitems3(Tworkitem tworkitems3) {
		getTworkitems3().remove(tworkitems3);
		tworkitems3.setTperson3(null);

		return tworkitems3;
	}

	public List<Tworkitem> getTworkitems4() {
		return this.tworkitems4;
	}

	public void setTworkitems4(List<Tworkitem> tworkitems4) {
		this.tworkitems4 = tworkitems4;
	}

	public Tworkitem addTworkitems4(Tworkitem tworkitems4) {
		getTworkitems4().add(tworkitems4);
		tworkitems4.setTperson4(this);

		return tworkitems4;
	}

	public Tworkitem removeTworkitems4(Tworkitem tworkitems4) {
		getTworkitems4().remove(tworkitems4);
		tworkitems4.setTperson4(null);

		return tworkitems4;
	}

	public List<Tworkitemlink> getTworkitemlinks() {
		return this.tworkitemlinks;
	}

	public void setTworkitemlinks(List<Tworkitemlink> tworkitemlinks) {
		this.tworkitemlinks = tworkitemlinks;
	}

	public Tworkitemlink addTworkitemlink(Tworkitemlink tworkitemlink) {
		getTworkitemlinks().add(tworkitemlink);
		tworkitemlink.setTperson(this);

		return tworkitemlink;
	}

	public Tworkitemlink removeTworkitemlink(Tworkitemlink tworkitemlink) {
		getTworkitemlinks().remove(tworkitemlink);
		tworkitemlink.setTperson(null);

		return tworkitemlink;
	}

	public List<Tworkitemlock> getTworkitemlocks() {
		return this.tworkitemlocks;
	}

	public void setTworkitemlocks(List<Tworkitemlock> tworkitemlocks) {
		this.tworkitemlocks = tworkitemlocks;
	}

	public Tworkitemlock addTworkitemlock(Tworkitemlock tworkitemlock) {
		getTworkitemlocks().add(tworkitemlock);
		tworkitemlock.setTperson(this);

		return tworkitemlock;
	}

	public Tworkitemlock removeTworkitemlock(Tworkitemlock tworkitemlock) {
		getTworkitemlocks().remove(tworkitemlock);
		tworkitemlock.setTperson(null);

		return tworkitemlock;
	}

}
