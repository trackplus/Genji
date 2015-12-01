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

package com.aurel.track.admin.customize.treeConfig.screen.importScreen.parser;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.aurel.track.admin.customize.treeConfig.screen.importScreen.IExchangeFieldNames;
import com.aurel.track.admin.customize.treeConfig.screen.importScreen.ITableTypes;
import com.aurel.track.beans.TPersonBean;


public class PersonParser extends ParserFactory //implements IParser  
{
	private static final Logger LOGGER = LogManager.getLogger(PersonParser.class);
	private boolean isPerson = false;
	private boolean isSub = false;
    private boolean isFirstName = false;
    private boolean isLastName = false;
    private boolean isLoginName = false;
    private boolean isEmail = false;
    private boolean isPasswd = false;
    private boolean isSalt = false;
    private boolean isForgotPasswordKey = false;
    private boolean isPhone = false;
    private boolean isDepartmentID = false;
    private boolean isValidUntil = false;
    private boolean isPreferences = false;
    private boolean isLastEdit = false;
    private boolean isCreated = false;
    private boolean isDeleted = false;
    private boolean isTokenPasswd = false;
    private boolean isTokenExpDate = false;
    private boolean isEmailFrequency = false;
    private boolean isEmailLead = false;
    private boolean isEmailLastReminded = false;
    private boolean isEmailRemindMe = false;
    private boolean isPrefEmailType = false;
    private boolean isPrefLocale = false;
    private boolean isMyDefaultReport = false;
    private boolean isNoEmailPlease = false;
    private boolean isRemindMeAsOriginator = false;
    private boolean isRemindMeAsManager = false;
    private boolean isRemindMeAsResponsible = false;
    private boolean isEmailRemindPriorityLevel = false;
    private boolean isEmailRemindSeverityLevel = false;
    private boolean isHoursPerWorkDay = false;
    private boolean isHourlyWage = false;
    private boolean isExtraHourWage = false;
    private boolean isEmployeeID = false;
    private boolean isIsgroup = false;
    private boolean isUserLevel = false;
    private boolean isMaxAssignedItems = false;
    private boolean isMessengerURL = false;
    private boolean isCallUrl = false;
    private boolean isSymbol = false;
    private boolean isIconKey = false;
	
	
    
	private TPersonBean tmpPersonBean;
	private List<TPersonBean> personBeans;
	private StringBuffer buffer ;			
	
	
	
	
	public List<TPersonBean> parse()
	{	
		personBeans = new ArrayList<TPersonBean>();
		File file = getFile();
		 try 
		 {
			 SAXParserFactory parserFact = SAXParserFactory.newInstance();
			 SAXParser parser = parserFact.newSAXParser();
			 parser.parse(file, this);
		 } 
		 catch(SAXException e) {
			 LOGGER.error("Parsing Sax Exception:" + e.getMessage());
		 }
		 catch(ParserConfigurationException e) {
			 LOGGER.error("Parse Configuration Exception:" + e.getMessage());
		 }
		 catch (IOException e) {
			 LOGGER.error("IO exception:" + e.getMessage());
		 }
		 catch (Exception e)
		 {
			 LOGGER.error("Error parsing file " +file.getName() + " :" + e.getMessage());
		 }
		 
		 return personBeans;
	}
	  


	 @Override
	public void startElement(String uri, String localName, 
				 String element_name, Attributes attributes) throws SAXException
	 {
		 buffer = new StringBuffer(); 
		 if (element_name.equals(IExchangeFieldNames.ITEM)){ 
			 if (ITableTypes.PERSON.equals(attributes.getValue("type"))) {
				 tmpPersonBean = new TPersonBean();
				 tmpPersonBean.setObjectID(Integer.parseInt(attributes.getValue(IExchangeFieldNames.OBJECT_ID)));
				 tmpPersonBean.setUuid(attributes.getValue(IExchangeFieldNames.TPUUID));
				 isPerson = true;
				 isSub = false;
			 }
			 else {
				 isSub = true;
			 }
		 }
		 if (isPerson && !isSub)
		 {
			 if (element_name.equals(IExchangeFieldNames.FIRSTNAME)){
				 isFirstName = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.LASTNAME)){
				 isLastName = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.LOGINNAME)){
				 isLoginName = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.EMAIL)){
				 isEmail = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PASSWD)){
				 isPasswd = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.SALT)){
				 isSalt = true;
			 }
			 if (element_name.equals(IExchangeFieldNames.FORGOTPASSWORDKEY)){
				 isForgotPasswordKey = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PHONE)){
				 isPhone = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DEPARTMENTID)){
				 isDepartmentID = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.VALIDUNTIL)){
				 isValidUntil = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PREFERENCES)){
				 isPreferences = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.LASTEDIT)){
				 isLastEdit = true;
			 }
			 if (element_name.equals(IExchangeFieldNames.CREATED)){
				 isCreated = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DELETED)){
				 isDeleted = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.TOKENPASSWD)){
				 isTokenPasswd = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.TOKENEXPDATE)){
				 isTokenExpDate = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.EMAILFREQUENCY)){
				 isEmailFrequency = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.EMAILLEAD)){
				 isEmailLead = true;
			 }
			 if (element_name.equals(IExchangeFieldNames.EMAILLASTREMINDED)){
				 isEmailLastReminded = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.EMAILREMINDME)){
				 isEmailRemindMe = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PREFEMAILTYPE)){
				 isPrefEmailType = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PREFLOCALE)){
				 isPrefLocale = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MYDEFAULTREPORT)){
				 isMyDefaultReport = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.NOEMAILPLEASE)){
				 isNoEmailPlease = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.REMINDMEASORIGINATOR)){
				 isRemindMeAsOriginator = true;
			 }
			 if (element_name.equals(IExchangeFieldNames.REMINDMEASMANAGER)){
				 isRemindMeAsManager = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.REMINDMEASRESPONSIBLE)){
				 isRemindMeAsResponsible = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.EMAILREMINDPRIORITYLEVEL)){
				 isEmailRemindPriorityLevel = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.EMAILREMINDSEVERITYLEVEL)){
				 isEmailRemindSeverityLevel = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.HOURSPERWORKDAY)){
				 isHoursPerWorkDay = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.HOURLYWAGE)){
				 isHourlyWage = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.EXTRAHOURWAGE)){
				 isExtraHourWage = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.EMPLOYEEID)){
				 isEmployeeID = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.ISGROUP)){
				 isIsgroup = true;
			 }
			 if (element_name.equals(IExchangeFieldNames.USERLEVEL)){
				 isUserLevel = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MAXASSIGNEDITEMS)){
				 isMaxAssignedItems = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MESSENGERURL)){
				 isMessengerURL = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.CALLURL)){
				 isCallUrl = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.SYMBOL)){
				 isSymbol = true;
			 }
			 else if (element_name.equals(IExchangeFieldNames.ICONKEY)){
				 isIconKey = true;
			 }

		 }
	 }

	 @Override
	public void endElement(String uri, String localName, 
			 String element_name) throws SAXException
	 {
		 if (element_name.equals(IExchangeFieldNames.ITEM) && isPerson){
			 personBeans.add(tmpPersonBean);
			 isPerson = false;
		 }
		 if (!isSub && isPerson)
		 {
			 if (element_name.equals(IExchangeFieldNames.FIRSTNAME)){
				 tmpPersonBean.setFirstName(buffer.toString());
				 isFirstName = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.LASTNAME)){
				 tmpPersonBean.setLastName(buffer.toString());
				 isLastName = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.LOGINNAME)){
				 tmpPersonBean.setLoginName(buffer.toString());
				 isLoginName = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.EMAIL)){
				 tmpPersonBean.setEmail(buffer.toString());
				 isEmail = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PASSWD)){
				 tmpPersonBean.setPasswd(buffer.toString());
				 isPasswd = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.SALT)){
				 tmpPersonBean.setSalt(buffer.toString());
				 isSalt = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.FORGOTPASSWORDKEY)){
				 tmpPersonBean.setForgotPasswordKey(buffer.toString());
				 isForgotPasswordKey = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PHONE)){
				 tmpPersonBean.setPhone(buffer.toString());
				 isPhone = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DEPARTMENTID)){
				 tmpPersonBean.setDepartmentID(Integer.parseInt(buffer.toString()));
				 isDepartmentID = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.VALIDUNTIL)){
				 try
				 {
					 tmpPersonBean.setValidUntil(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(buffer.toString()));
				 }
				 catch(ParseException e)
				 {
					 LOGGER.debug("Error="+ e.getMessage(), e);
				 }
				 isValidUntil = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PREFERENCES)){
				 tmpPersonBean.setPreferences(buffer.toString());
				 isPreferences = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.LASTEDIT)){
				 try
				 {
					 tmpPersonBean.setLastEdit(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(buffer.toString()));
				 }
				 catch(ParseException e)
				 {
					 LOGGER.debug("Error="+ e.getMessage(), e);
				 }
				 isLastEdit = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.CREATED)){
				 try
				 {
					 tmpPersonBean.setCreated(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(buffer.toString()));
				 }
				 catch(ParseException e)
				 {
					 LOGGER.debug("Error="+ e.getMessage(), e);
				 }
				 isCreated = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.DELETED)){
				 tmpPersonBean.setDeleted(buffer.toString());
				 isDeleted = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.TOKENPASSWD)){
				 tmpPersonBean.setTokenPasswd(buffer.toString());
				 isTokenPasswd = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.TOKENEXPDATE)){
				 try
				 {
					 tmpPersonBean.setTokenExpDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(buffer.toString()));
				 }
				 catch(ParseException e)
				 {
					 LOGGER.debug("Error="+ e.getMessage(), e);
				 }
				 isTokenExpDate = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.EMAILFREQUENCY)){
				 tmpPersonBean.setEmailFrequency(Integer.parseInt(buffer.toString()));
				 isEmailFrequency = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.EMAILLEAD)){
				 tmpPersonBean.setEmailLead(Integer.parseInt(buffer.toString()));
				 isEmailLead = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.EMAILLASTREMINDED)){
				 try
				 {
					 tmpPersonBean.setEmailLastReminded(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(buffer.toString()));
				 }
				 catch(ParseException e)
				 {
					 LOGGER.debug("Error="+ e.getMessage(), e);
				 }
				 isEmailLastReminded = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.EMAILREMINDME)){
				 tmpPersonBean.setEmailRemindMe(buffer.toString());
				 isEmailRemindMe = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PREFEMAILTYPE)){
				 tmpPersonBean.setPrefEmailType(buffer.toString());
				 isPrefEmailType = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.PREFLOCALE)){
				 tmpPersonBean.setPrefLocale(buffer.toString());
				 isPrefLocale = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MYDEFAULTREPORT)){
				 tmpPersonBean.setMyDefaultReport(Integer.parseInt(buffer.toString()));
				 isMyDefaultReport = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.NOEMAILPLEASE)){
				 tmpPersonBean.setNoEmailPlease(Integer.parseInt(buffer.toString()));
				 isNoEmailPlease = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.REMINDMEASORIGINATOR)){
				 tmpPersonBean.setRemindMeAsOriginator(buffer.toString());
				 isRemindMeAsOriginator = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.REMINDMEASMANAGER)){
				 tmpPersonBean.setRemindMeAsManager(buffer.toString());
				 isRemindMeAsManager = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.REMINDMEASRESPONSIBLE)){
				 tmpPersonBean.setRemindMeAsResponsible(buffer.toString());
				 isRemindMeAsResponsible = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.EMAILREMINDPRIORITYLEVEL)){
				 tmpPersonBean.setEmailRemindPriorityLevel(Integer.parseInt(buffer.toString()));
				 isEmailRemindPriorityLevel = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.EMAILREMINDSEVERITYLEVEL)){
				 tmpPersonBean.setEmailRemindSeverityLevel(Integer.parseInt(buffer.toString()));
				 isEmailRemindSeverityLevel = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.HOURSPERWORKDAY)){
				 tmpPersonBean.setHoursPerWorkDay(Double.parseDouble(buffer.toString()));
				 isHoursPerWorkDay = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.HOURLYWAGE)){
				 tmpPersonBean.setHourlyWage(Double.parseDouble(buffer.toString()));
				 isHourlyWage = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.EXTRAHOURWAGE)){
				 tmpPersonBean.setExtraHourWage(Double.parseDouble(buffer.toString()));
				 isExtraHourWage = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.EMPLOYEEID)){
				 tmpPersonBean.setEmployeeID(buffer.toString());
				 isEmployeeID = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.ISGROUP)){
				 tmpPersonBean.setIsgroup(buffer.toString());
				 isIsgroup = false;
			 }
			 if (element_name.equals(IExchangeFieldNames.USERLEVEL)){
				 tmpPersonBean.setUserLevel(Integer.parseInt(buffer.toString()));
				 isUserLevel = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MAXASSIGNEDITEMS)){
				 tmpPersonBean.setMaxAssignedItems(Integer.parseInt(buffer.toString()));
				 isMaxAssignedItems = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.MESSENGERURL)){
				 tmpPersonBean.setMessengerURL(buffer.toString());
				 isMessengerURL = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.CALLURL)){
				 tmpPersonBean.setCALLURL(buffer.toString());
				 isCallUrl = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.SYMBOL)){
				 tmpPersonBean.setSymbol(buffer.toString());
				 isSymbol = false;
			 }
			 else if (element_name.equals(IExchangeFieldNames.ICONKEY)){
				 tmpPersonBean.setIconKey(Integer.parseInt(buffer.toString()));
				 isIconKey = false;
			 }
			 
		 }
	 }


	 
	@Override
	public void characters(char[] ch, int start, int len) throws SAXException{
		String str = new String (ch, start, len);
		if (isFirstName){
			buffer.append(str);
		}
		if (isLastName){
			buffer.append(str);
		}
		if (isLoginName){
			buffer.append(str);
		}
		if (isEmail){
			buffer.append(str);
		}
		if (isPasswd){
			buffer.append(str);
		}
		if (isSalt){
			buffer.append(str);
		}
		if (isForgotPasswordKey){
			buffer.append(str);
		}
		if (isPhone){
			buffer.append(str);
		}
		if (isDepartmentID){
			buffer.append(str);
		}
		if (isValidUntil){
			buffer.append(str);
		}
		if (isPreferences){
			buffer.append(str);
		}
		if (isLastEdit){
			buffer.append(str);
		}
		if (isCreated){
			buffer.append(str);
		}
		if (isDeleted){
			buffer.append(str);
		}
		if (isTokenPasswd){
			buffer.append(str);
		}
		if (isTokenExpDate){
			buffer.append(str);
		}
		if (isEmailFrequency){
			buffer.append(str);
		}
		if (isEmailLead){
			buffer.append(str);
		}
		if (isEmailLastReminded){
			buffer.append(str);
		}
		if (isEmailRemindMe){
			buffer.append(str);
		}
		if (isPrefEmailType){
			buffer.append(str);
		}
		if (isPrefLocale){
			buffer.append(str);
		}
		if (isMyDefaultReport){
			buffer.append(str);
		}
		if (isNoEmailPlease){
			buffer.append(str);
		}
		if (isRemindMeAsOriginator){
			buffer.append(str);
		}
		if (isRemindMeAsManager){
			buffer.append(str);
		}
		if (isRemindMeAsResponsible){
			buffer.append(str);
		}
		if (isEmailRemindPriorityLevel){
			buffer.append(str);
		}
		if (isEmailRemindSeverityLevel){
			buffer.append(str);
		}
		if (isHoursPerWorkDay){
			buffer.append(str);
		}
		if (isHourlyWage){
			buffer.append(str);
		}
		if (isExtraHourWage){
			buffer.append(str);
		}
		if (isEmployeeID){
			buffer.append(str);
		}
		if (isIsgroup){
			buffer.append(str);
		}
		if (isUserLevel){
			buffer.append(str);
		}
		if (isMaxAssignedItems){
			buffer.append(str);
		}
		if (isMessengerURL){
			buffer.append(str);
		}
		if (isCallUrl){
			buffer.append(str);
		}
		if (isSymbol){
			buffer.append(str);
		}
		if (isIconKey){
			buffer.append(str);
		}

	}
}



