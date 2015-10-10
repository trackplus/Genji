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


package com.aurel.track.item.consInf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.json.JSONUtility;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * Action class with "load", "delete", and "me" operations
 * for consultant/informant groups/persons  
 * @author Tamas Ruff
 *
 */
public class ConsInfAction extends ActionSupport implements Preparable,SessionAware {
		
	private static final long serialVersionUID = 8555290272485363249L;
	private Map<String, Object> session;	
	private ConsInfShow consInfShow;
	private boolean create;
	private Integer person;
	private Integer workItemID;
	
	/**
	 * Has only setter to prevent setting the "operation" hidden field 
	 * after executing the business logic. In this mode we avoid reexecuting 
	 * the last business logic operation in case of a submit caused by changig the tab.
	 * The same effect can be achieved if we reset expicitely the "operation" field
	 * after executing the specific operation or if we do not use hidden field at all
	 * but only set the "operation" as request parameter
	 */ 
	private String operation;
	//has only setter
	private String raciRole;
	
	public void prepare() throws Exception {
		person = ((TPersonBean) session.get(Constants.USER_KEY)).getObjectID();
		create=(workItemID==null);
		WorkItemContext ctx=(WorkItemContext)session.get("workItemContext");
		if(ctx!=null){
			consInfShow = ctx.getConsInfShow();
		}else{
			consInfShow = new ConsInfShow();
			ConsInfBL.loadConsInfFromDb(workItemID,person, consInfShow);
		}
	}
	
	/**
	 * Submit after tab change from this tab 
	 * to save the content of the tab into the ConsInfShow
	 * but no other business logic executed
	 */
	public String submit(){
		//nothing
		return "ok";
	}
	
	/**
	 * The main entry point into the action.
	 * It will be branched to the specific operation 
	 * depending on the request parameter called "operation".
	 * This extra "operation" parameter should be used 
	 * (instead of action!method syntax in jsp submit tags)
	 * because the buttons which trigger this operations are on the toolbar 
	 * (outside of the HTML form)
	 * The submit resulting in executing this method will be triggered through AJAX 
	 */
	@Override
	public String execute()	{		
		if ("d".equals(operation)) {
			//delete selected consultant/informant persons/groups
			delete();
		}
		if ("r".equals(operation) || "a".equals(operation)) {
			//add/remove me
			me();
		}
		//by tab change to cons/inf tab or refresh from add consultant/informant popup
		//return load();
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}
	
	/**
	 * Reloads the consInfShow after "add" operation from the popup
	 * @return
	 */
	public String load(){
		if (create) {
			List<TPersonBean> consultantPersons = consInfShow.getRealConsultantPersons();				
			List<TPersonBean> informantPersons = consInfShow.getRealInformantPersons();
			consInfShow.setAmIConsultant(ConsInfBL.foundMeInRole(consultantPersons, person));
			consInfShow.setAmIInformant(ConsInfBL.foundMeInRole(informantPersons, person));			
		} else {
			ConsInfBL.loadConsInfFromDb(workItemID, person, consInfShow);
		}
		return INPUT;	
	}
	
	/**
	 * Deletes the selected consultants/informants 
	 * from the database or from the session
	 * and then refreshes the consInfShow in the session  
	 * @return
	 */
	private String delete() {		
		Integer[] selectedConsultantPersons = consInfShow.getSelectedConsultantPersons();
		Integer[] selectedConsultantGroups = consInfShow.getSelectedConsultantGroups();		
		Integer[] selectedInformantPersons = consInfShow.getSelectedInformantPersons();
		Integer[] selectedInformantGroups = consInfShow.getSelectedInformantGroups();    
    	if ((selectedConsultantPersons==null || selectedConsultantPersons.length ==0) &&
    			(selectedConsultantGroups==null || selectedConsultantGroups.length ==0) &&
    			(selectedInformantPersons==null || selectedInformantPersons.length ==0) &&
    			(selectedInformantGroups==null || selectedInformantGroups.length ==0)) {
    		//addActionError(getText("item.tabs.watchers.err.notSelected"));
    		return null;
    	}
    	if (create) {
	    	//reset me if present and selected
	    	if (ConsInfBL.foundMeAsSelected(selectedConsultantPersons, person)) {    		
	    		consInfShow.setAmIConsultant(false);
	    	}
	    	if (ConsInfBL.foundMeAsSelected(selectedInformantPersons, person)) {
	    		consInfShow.setAmIInformant(false);
	    	}
	    	//remove the selected persons/groups from the session object
	    	ConsInfBL.deleteSelected(consInfShow.getRealConsultantPersons(), selectedConsultantPersons);
	    	ConsInfBL.deleteSelected(consInfShow.getRealConsultantGroups(), selectedConsultantGroups);  
	    	ConsInfBL.deleteSelected(consInfShow.getRealInformantPersons(), selectedInformantPersons);
	    	ConsInfBL.deleteSelected(consInfShow.getRealInformantGroups(), selectedInformantGroups);	    		    	
    	} else {
    		//delete selected consultant persons/groups
    		ConsInfBL.deleteByWorkItemAndPersonsAndRaciRole(workItemID, selectedConsultantPersons, RaciRole.CONSULTANT);
    		ConsInfBL.deleteByWorkItemAndPersonsAndRaciRole(workItemID, selectedConsultantGroups, RaciRole.CONSULTANT);
	    		
	    	//delete selected informant persons/groups
    		ConsInfBL.deleteByWorkItemAndPersonsAndRaciRole(workItemID, selectedInformantPersons, RaciRole.INFORMANT);
    		ConsInfBL.deleteByWorkItemAndPersonsAndRaciRole(workItemID, selectedInformantGroups, RaciRole.INFORMANT);
	    	
	    	//reload the consultants/informants from database
	    	ConsInfBL.loadConsInfFromDb(workItemID, person, consInfShow);	    	
    	}
    	consInfShow.setSelectedConsultantPersons(new Integer[0]);
    	consInfShow.setSelectedConsultantGroups(new Integer[0]);    	
    	consInfShow.setSelectedInformantPersons(new Integer[0]);
    	consInfShow.setSelectedInformantGroups(new Integer[0]);
    	JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
    }
	
	/**
	 * Adds/removes the current user as consultant/informant
	 * to/from the database or to/from the session
	 * and then refreshes the consInfShow in the session
	 * @return
	 */
	private String me() {
    	if (create) {
    		//add consultant/informant
	    	if ("a".equals(operation))
	    	{
	    		if (RaciRole.CONSULTANT.equals(raciRole)){
	    			consInfShow.setAmIConsultant(true);
					List<TPersonBean> realConsultantPersons=consInfShow.getRealConsultantPersons();
					if(realConsultantPersons==null){
						realConsultantPersons=new ArrayList<TPersonBean>();
						consInfShow.setRealConsultantPersons(realConsultantPersons);
					}
	    			ConsInfBL.meAdd(realConsultantPersons, person);
	    		}
	    		if (RaciRole.INFORMANT.equals(raciRole)){
	    			consInfShow.setAmIInformant(true);
					List<TPersonBean> realInformantPersons=consInfShow.getRealInformantPersons();
					if(realInformantPersons==null){
						realInformantPersons=new ArrayList<TPersonBean>();
						consInfShow.setRealInformantGroups(realInformantPersons);
					}
	    			ConsInfBL.meAdd(realInformantPersons, person);
	    		}    		
	    	}
	    	//remove consultant/informant
	    	if ("r".equals(operation)) {
	    		if (RaciRole.CONSULTANT.equals(raciRole)) {
	    			consInfShow.setAmIConsultant(false);    			
	    			ConsInfBL.meRemove(consInfShow.getRealConsultantPersons(), person);
	    		}
	    		if (RaciRole.INFORMANT.equals(raciRole)) {
	    			consInfShow.setAmIInformant(false);    			
	    			ConsInfBL.meRemove(consInfShow.getRealInformantPersons(), person);
	    		}    		    		
	    	}
    	} else {
    		//add consultant/informant    	
	    	if ("a".equals(operation)) {
	    		ConsInfBL.insertByWorkItemAndPersonAndRaciRole(workItemID, person, raciRole);
	    	}
	    	//remove consultant/informant
	    	if ("r".equals(operation)) {    		
	    		ConsInfBL.deleteByWorkItemAndPersonsAndRaciRole(workItemID, new Integer[] {person}, raciRole);
	    	}    	    	
	    	//reload the consultants/informants in editConsInfForm
	    	if (consInfShow==null) {
	    		consInfShow = new ConsInfShow();
	    	}
	    	ConsInfBL.loadConsInfFromDb(workItemID, person, consInfShow);
    	}
    	JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}
	
	public ConsInfShow getConsInfShow() {
		return consInfShow;
	}

	public void setConsInfShow(ConsInfShow consInfShow) {
		this.consInfShow = consInfShow;
	}	

	public void setSession(Map<String, Object> session) {
		this.session=session;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public void setRaciRole(String raciRole) {
		this.raciRole = raciRole;
	}

	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}
}
