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



public abstract class BaseEntity {

	public static final String TABLE_CLUSTERNODE="91";
	public static final String TABLE_ACCOUNT= 	"24";
	public static final String TABLE_ACL= 	"1";
	public static final String TABLE_ACTION= 	"73";
	public static final String TABLE_ACTUALESTIMATEDBUDGET= 	"49";
	public static final String TABLE_APPLICATIONCONTEXT= 	"89";
	public static final String TABLE_ATTACHMENT= 	"25";
	public static final String TABLE_ATTRIBUTE= 	"43";
	public static final String TABLE_ATTRIBUTECLASS= 	"41";
	public static final String TABLE_ATTRIBUTEOPTION= 	"40";
	public static final String TABLE_ATTRIBUTETYPE= 	"42";
	public static final String TABLE_ATTRIBUTEVALUE= 	"68";
	public static final String TABLE_BASELINE= 	"2";
	public static final String TABLE_BASKET= 	"115";
	public static final String TABLE_BLOB= 	"104";
	public static final String TABLE_BUDGET= 	"48";
	public static final String TABLE_CATEGORY= 	"3";
	public static final String TABLE_CHILDISSUETYPE= 	"113";
	public static final String TABLE_CLASS= 	"4";
	public static final String TABLE_CLOB= 	"77";
	public static final String TABLE_COMPUTEDVALUES= 	"20";
	public static final String TABLE_CONFIGOPTIONSROLE= 	"62";
	public static final String TABLE_COST= 	"26";
	public static final String TABLE_COSTCENTER= 	"51";
	public static final String TABLE_DASHBOARDFIELD= 	"56";
	public static final String TABLE_DASHBOARDPANEL= 	"55";
	public static final String TABLE_DASHBOARDPARAMETER= 	"57";
	public static final String TABLE_DASHBOARDSCREEN= 	"53";
	public static final String TABLE_DASHBOARDTAB= 	"54";
	public static final String TABLE_DEPARTMENT= 	"5";
	public static final String TABLE_DISABLEFIELD= 	"30";
	public static final String TABLE_DOCSTATE= 	"29";
	public static final String TABLE_EFFORTTYPE= 	"27";
	public static final String TABLE_EFFORTUNIT= 	"28";
	public static final String TABLE_EMAILPROCESSED= 	"88";
	public static final String TABLE_ENTITYCHANGES= 	"92";
	public static final String TABLE_ESCALATIONENTRY= 	"127";
	public static final String TABLE_ESCALATIONSTATE= 	"128";
	public static final String TABLE_EVENT= 	"76";
	public static final String TABLE_EXPORTTEMPLATE= 	"87";
	public static final String TABLE_FIELD= 	"59";
	public static final String TABLE_FIELDCHANGE= 	"98";
	public static final String TABLE_FIELDCONFIG= 	"60";
	public static final String TABLE_FILTERCATEGORY= 	"110";
	public static final String TABLE_GENERALSETTINGS= 	"64";
	public static final String TABLE_GLOBALCSSSTYLE= 	"132";
	public static final String TABLE_GROUPMEMBER= 	"47";
	public static final String TABLE_HISTORYTRANSACTION= 	"97";
	public static final String TABLE_INITSTATE= 	"75";
	public static final String TABLE_ISSUEATTRIBUTEVALUE= 	"39";
	public static final String TABLE_LASTEXECUTEDQUERY= 	"131";
	public static final String TABLE_LASTVISITEDITEM= 	"119";
	public static final String TABLE_LINKTYPE= 	"83";
	public static final String TABLE_LIST= 	"65";
	public static final String TABLE_LOCALIZEDRESOURCES= 	"82";
	public static final String TABLE_LOGGEDINUSERS= 	"90";
	public static final String TABLE_LOGGINGLEVEL= 	"85";
	public static final String TABLE_MAILTEMPLATE= 	"117";
	public static final String TABLE_MAILTEMPLATECONFIG= 	"116";
	public static final String TABLE_MAILTEMPLATEDEF= 	"118";
	public static final String TABLE_MENUITEMQUERY= 	"112";
	public static final String TABLE_MOTD= 	"52";
	public static final String TABLE_MSPROJECTEXCHANGE= 	"109";
	public static final String TABLE_MSPROJECTTASK= 	"108";
	public static final String TABLE_NOTIFY= 	"6";
	public static final String TABLE_NOTIFYFIELD= 	"78";
	public static final String TABLE_NOTIFYSETTINGS= 	"80";
	public static final String TABLE_NOTIFYTRIGGER= 	"79";
	public static final String TABLE_OPTION= 	"66";
	public static final String TABLE_OPTIONSETTINGS= 	"67";
	public static final String TABLE_ORGPROJECTSLA= 	"129";
	public static final String TABLE_OUTLINECODE= 	"94";
	public static final String TABLE_OUTLINETEMPLATE= 	"96";
	public static final String TABLE_OUTLINETEMPLATEDEF= 	"95";
	public static final String TABLE_PERSON= 	"7";
	public static final String TABLE_PERSONBASKET= 	"114";
	public static final String TABLE_PLISTTYPE= 	"31";
	public static final String TABLE_PPRIORITY= 	"9";
	public static final String TABLE_PRIORITY= 	"8";
	public static final String TABLE_PRIVATEREPORTREPOSITORY= 	"21";
	public static final String TABLE_PROJCAT= 	"10";
	public static final String TABLE_PROJECT= 	"11";
	public static final String TABLE_PROJECTACCOUNT= 	"46";
	public static final String TABLE_PROJECTREPORTREPOSITORY= 	"23";
	public static final String TABLE_PROJECTTYPE= 	"32";
	public static final String TABLE_PSEVERITY= 	"15";
	public static final String TABLE_PSTATE= 	"33";
	public static final String TABLE_PUBLICREPORTREPOSITORY= 	"22";
	public static final String TABLE_QUERYREPOSITORY= 	"81";
	public static final String TABLE_READISSUE= 	"130";
	public static final String TABLE_RECURRENCEPATTERN= 	"105";
	public static final String TABLE_RELEASE= 	"12";
	public static final String TABLE_REPORTCATEGORY= 	"111";
	public static final String TABLE_REPORTLAYOUT= 	"44";
	public static final String TABLE_REPORTPARAMETER= 	"107";
	public static final String TABLE_REPORTPERSONSETTINGS= 	"106";
	public static final String TABLE_REPOSITORY= 	"102";
	public static final String TABLE_REVISION= 	"100";
	public static final String TABLE_REVISIONWORKITEMS= 	"101";
	public static final String TABLE_ROLE= 	"13";
	public static final String TABLE_ROLEFIELD= 	"61";
	public static final String TABLE_ROLELISTTYPE= 	"38";
	public static final String TABLE_SCHEDULER= 	"45";
	public static final String TABLE_SCREEN= 	"69";
	public static final String TABLE_SCREENCONFIG= 	"74";
	public static final String TABLE_SCREENFIELD= 	"72";
	public static final String TABLE_SCREENPANEL= 	"71";
	public static final String TABLE_SCREENTAB= 	"70";
	public static final String TABLE_SCRIPTS= 	"99";
	public static final String TABLE_SEVERITY= 	"14";
	public static final String TABLE_SITE= 	"34";
	public static final String TABLE_SLA= 	"126";
	public static final String TABLE_STATE= 	"16";
	public static final String TABLE_STATECHANGE= 	"17";
	public static final String TABLE_SUMMARYMAIL= 	"93";
	public static final String TABLE_SYSTEMSTATE= 	"50";
	public static final String TABLE_TEMPLATEPERSON= 	"103";
	public static final String TABLE_TEXTBOXSETTINGS= 	"63";
	public static final String TABLE_TRAIL= 	"18";
	public static final String TABLE_VERSIONCONTROLPARAMETER= 	"58";
	public static final String TABLE_WORKFLOW= 	"35";
	public static final String TABLE_WORKFLOWACTIVITY= 	"123";
	public static final String TABLE_WORKFLOWCATEGORY= 	"37";
	public static final String TABLE_WORKFLOWCONNECT= 	"125";
	public static final String TABLE_WORKFLOWDEF= 	"120";
	public static final String TABLE_WORKFLOWGUARD= 	"124";
	public static final String TABLE_WORKFLOWROLE= 	"36";
	public static final String TABLE_WORKFLOWSTATION= 	"122";
	public static final String TABLE_WORKFLOWTRANSITION= 	"121";
	public static final String TABLE_WORKITEM= 	"19";
	public static final String TABLE_WORKITEMLINK= 	"84";
	public static final String TABLE_WORKITEMLOCK= 	"86";
	public static final String TABLE_USESPACES=	"-1000";	

	
	abstract public int getObjectid();
	abstract public void setObjectid(int id);
	
	public Integer getObjectID() {
		return new Integer(getObjectid());
	}
	
	public void setObjectID(Integer id) {
		if (id != null) {
			setObjectid(id.intValue());
		} else {
			setObjectid(0);
		}
	}
	
	public boolean isNew() {
		if (getObjectID() == 0) {
			return true;
		} else {
			return false;
		}
	}
	
}
