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



package com.aurel.track.report.execute;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.json.JSONUtility;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;



/**
 * Expands/collapses one reportBean or group or all reportBeans
 * Because now the expand-collapse logic is shifted to the client side,
 * in the server side we only save the modified expand/collapse node(s)
 * into the session but no page refresh takes place. 
 * That's why these action methods are executed through ajax. 
 * The registered expand/collapse nodes will be used by the next
 * page refresh which takes place for example after a sort operation     
 * @author Tamas Ruff
 *
 */
public class ReportExpandAction extends ActionSupport implements Preparable, SessionAware {
			
	private static final long serialVersionUID = -3155944423987319322L;
	public static String OTHER_ITEMS_SET = "otherItemsSet";
	public static String ALL_ITEMS_EXPANDED="allItemsExpanded";
	public static String GROUP_BY ="groupBy";
	public static String OTHER_GROUPS_SET = "otherGroupsSet";
	private Map session;
	private Integer workItemID;
	private String groupID;
	private boolean expanded;
	
	
	/**
     * Prepares the reportBeans
     */
	public void prepare() throws Exception {		

    }
	
	/**
	 * Expands all parent ReportBeans
	 * @return
	 */
	public String expandAll() {
		session.put(ALL_ITEMS_EXPANDED, new Boolean(true));
		session.remove(OTHER_ITEMS_SET);
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}

	/**
	 * Collapses all parent ReportBeans
	 * @return
	 */
	public String collapseAll() {
		session.remove(ALL_ITEMS_EXPANDED);
		session.remove(OTHER_ITEMS_SET);
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}

	/**
	 * Expands or collapses a single ReportBean node
	 * @return
	 */
	public String expandReportBean() {		
		boolean allItemsExpandedInEffect;
		Boolean allItemsExpanded = (Boolean)session.get(ALL_ITEMS_EXPANDED);
		if (allItemsExpanded==null) {
			allItemsExpandedInEffect = false;
		} else {
			allItemsExpandedInEffect = allItemsExpanded.booleanValue();
		}
		if (workItemID != null) {
			//ReportBean reportBean = reportBeans.getItem(workItemID);
			//if (reportBean!=null) {
			Set<Integer> otherItems = (Set<Integer>)session.get(OTHER_ITEMS_SET);
				if (otherItems==null) {
					otherItems=new HashSet<Integer>();
					session.put(OTHER_ITEMS_SET, otherItems);
				}
				//register the expand/collapse for the next recalculation of ReportBeans 				
				//boolean isExpanded = reportBean.isExpanded();
				if (allItemsExpandedInEffect) {
					if (expanded) {					
						otherItems.remove(workItemID);
					} else {						
						otherItems.add(workItemID);
					}
				} else {
					if (expanded) {
						otherItems.add(workItemID);						
					} else {
						otherItems.remove(workItemID);
					}
				}
				//after an reportBean expand/collapse the ReportBeans structure will not be recalculated
				//so the needed modifications on the reportBean and his ancestors should be made here recursively
				//reportBean.setExpanded(!isExpanded);				
			//}
		}
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}	
		
	/**
	 * Expands or collapses a single group node
	 * @return
	 */
	public String expandGroup() {
		if (groupID!=null) {
			Set<String> otherGroups = (Set<String>)session.get(OTHER_GROUPS_SET);
				if (otherGroups==null) {
					otherGroups=new HashSet<String>();
					session.put(OTHER_GROUPS_SET, otherGroups);
				}
				if (otherGroups.contains(groupID)) {
					otherGroups.remove(groupID);
				} else {
					otherGroups.add(groupID);
				}
		}
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}
	public String expandGroupWBS(){
		boolean groupExpanded=true;
		Set<String> otherGroups = (Set<String>)session.get(OTHER_GROUPS_SET+".wbs");
		if (otherGroups==null) {
			otherGroups=new HashSet<String>();
			session.put(OTHER_GROUPS_SET+".wbs", otherGroups);
		}
		//register the expand/collapse for the next recalculation of ReportBeans
		if (groupExpanded) {
			if (expanded) {
				otherGroups.remove(groupID);
			} else {
				otherGroups.add(groupID);
			}
		} else {
			if (expanded) {
				otherGroups.add(groupID);
			} else {
				otherGroups.remove(groupID);
			}
		}
		JSONUtility.encodeJSONSuccess(ServletActionContext.getResponse());
		return null;
	}
	
	
	
	/**
	 * @param session the session to set
	 */
	public void setSession(Map session) {
		this.session = session;
	}


	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}


	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}


	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
}
