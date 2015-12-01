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

package com.aurel.track.admin.customize.treeConfig.screen.importScreen;


public interface IExchangeFieldNames {
	//for xml
	public static final String TRACKPLUS_EXCHANGE = "trackplusExchange";
	public static final String EXCHANGE_ZIP_ENTRY = "exchange.xml";
	public static final String ITEM = "item";
	public static final String TPUUID = "uuid";
	public static final String OBJECT_ID = "itemID";
	public static final String ITEM_ATTRIBUTE = "itemAttribute";
	public static final String FIELD_ID = "fieldID";
	public static final String ITEM_TYPE = "type"; //screen or screenTab
	public static final String DEPRECATED = "deprecated";
	public static final String FIELDTYPE = "fieldType";
	public static final String FILTERFIELD = "filterField";
	public static final String NAME = "name";
	public static final String REQUIRED = "required";
	public static final String ISCUSTOM = "isCustom";
	public static final String OWNER = "owner";
	public static final String TOOLTIP = "toolTip";
	public static final String LABEL = "label";
	public static final String HISTORY = "history";
	public static final String ISSUETYPE = "issueType";
	public static final String PROJECTTYPE = "projectType";
	public static final String PROJECT = "project";
	public static final String DESCRIPTION = "description";
	public static final String FIELD = "field"; 
	public static final String CONFIG = "config";
	public static final String INTEGERVALUE = "integerValue";
	public static final String DOUBLEVALUE = "doubleValue";
	public static final String TEXTVALUE = "textValue"; 
	public static final String DATEVALUE = "dateValue";
	public static final String CHARACTERVALUE = "characterValue";
	public static final String PARAMETERCODE = "parameterCode";
	public static final String VALIDVALUE = "validValue"; 
	public static final String TAGLABEL = "tagLabel";
	public static final String PARENTLIST = "parentList";
	public static final String LISTTYPE = "listType";
	public static final String CHILDNUMBER = "childNumber";
	public static final String DELETED = "deleted";
	public static final String REPOSITORYTYPE = "repositoryType";
	public static final String LIST = "list";
	public static final String PARENTOPTION = "parentOption";
	public static final String SORTORDER = "sortOrder";
	public static final String DEFAULT = "default";
	public static final String SYMBOL = "symbol";
	public static final String ICONKEY = "iconKey";
	public static final String ICONCHANGED = "iconChanged";
	public static final String CSSSTYLE = "CSSStyle";
	public static final String MULTIPLE = "multiple";
	public static final String INDEX = "index";
	public static final String COLINDEX = "colIndex";
	public static final String ROWINDEX = "rowIndex";
	public static final String COLSPAN = "colSpan";
	public static final String ROWSPAN = "rowSpan";
	public static final String LABELHALIGN = "labelHAlign";
	public static final String LABELVALIGN = "labelVAlign";
	public static final String VALUEHALIGN = "valueHAlign";
	public static final String VALUEVALIGN = "valueVAlign";
	public static final String ISEMPTY = "isEmpty";
	public static final String PARENT = "parent";
	public static final String ROWSNO = "rowsNo";
	public static final String COLSNO = "colsNo";
	public static final String DEFAULTTEXT = "defaultText";
	public static final String DEFAULTINTEGER = "defaultInteger";
	public static final String DEFAULTDOUBLE = "defaultDouble";
	public static final String DEFAULTDATE = "defaultDate";
	public static final String DEFAULTCHAR = "defaultChar";
	public static final String DEFAULTOPTION = "defaultOption";
	public static final String MINOPTION = "minOption";
	public static final String MAXOPTION = "maxOption";
	public static final String MINTEXTLENGTH = "minTextLength";
	public static final String MAXTEXTLENGTH = "maxTextLength";
	public static final String MINDATE = "minDate";
	public static final String MAXDATE = "maxDate";
	public static final String MININTEGER = "minInteger";
	public static final String MAXINTEGER = "maxInteger";
	public static final String MINDOUBLE = "minDouble";
	public static final String MAXDOUBLE = "maxDouble";
	public static final String MAXDECIMALDIGIT = "maxDecimalDigit";
	public static final String ISDEFAULT = "isDefault";
	public static final String REPORTTYPE = "reportType";
	public static final String EXPORTFORMAT = "exportFormat";
	public static final String PERSON = "person";
	public static final String CATEGORYKEY = "categoryKey";
	public static final String REPOSITORY = "repository";
	public static final String CREATEDBY = "createdBy";
	public static final String PARENTID = "parentID";
	public static final String FIRSTNAME = "firstName";
	public static final String LASTNAME = "lastName";
	public static final String LOGINNAME = "loginName";
	public static final String EMAIL = "email";
	public static final String PASSWD = "passwd";
	public static final String SALT = "salt";
	public static final String FORGOTPASSWORDKEY = "forgotPasswordKey";
	public static final String PHONE = "phone";
	public static final String DEPARTMENTID = "departmentID";
	public static final String VALIDUNTIL = "validUntil";
	public static final String PREFERENCES = "preferences";
	public static final String LASTEDIT = "lastEdit";
	public static final String CREATED = "created";
	public static final String TOKENPASSWD = "tokenPasswd";
	public static final String TOKENEXPDATE = "tokenExpDate";
	public static final String EMAILFREQUENCY = "emailFrequency";
	public static final String EMAILLEAD = "emailLead";
	public static final String EMAILLASTREMINDED = "emailLastReminded";
	public static final String EMAILREMINDME = "emailRemindMe";
	public static final String PREFEMAILTYPE = "prefEmailType";
	public static final String PREFLOCALE = "prefLocale";
	public static final String MYDEFAULTREPORT = "myDefaultReport";
	public static final String NOEMAILPLEASE = "noEmailPlease";
	public static final String REMINDMEASORIGINATOR = "remindMeAsOriginator";
	public static final String REMINDMEASMANAGER = "remindMeAsManager";
	public static final String REMINDMEASRESPONSIBLE = "remindMeAsResponsible";
	public static final String EMAILREMINDPRIORITYLEVEL = "emailRemindPriorityLevel";
	public static final String EMAILREMINDSEVERITYLEVEL = "emailRemindSeverityLevel";
	public static final String HOURSPERWORKDAY = "hoursPerWorkDay";
	public static final String HOURLYWAGE = "hourlyWage";
	public static final String EXTRAHOURWAGE = "extraHourWage";
	public static final String EMPLOYEEID = "employeeID";
	public static final String ISGROUP = "isGroup";
	public static final String USERLEVEL = "userLevel";
	public static final String MAXASSIGNEDITEMS = "maxAssignedItems";
	public static final String MESSENGERURL = "messengerURL";
	public static final String CALLURL = "callURL";

	
	//for LookUpMap
	public static String TFIELD = "field";
	public static String TFIELDCONFIG = "fieldConfig";
	public static String TTEXTBOXSETTINGS = "texBoxSettings";
	public static String TGENERALSETTINGS = "generalSettings";
	
	public static String TSCREEN = "screen";
	public static String TSCREENTAB = "screenTab";
	public static String TSCREENPANEL = "screenPanel";
	public static String TSCREENFIELD = "screenField";
	
	public static String TLIST = "list";
	public static String TOPTION = "option";
	public static String TOPTIONSETTINGS = "optionSettings";
	
	public static String TEXPORTTEMPLATE = "exportTemplate";
	public static String TREPORTCATEGORY = "reportCategory";
	public static String TPERSON = "person";
	
}
