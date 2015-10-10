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

package com.aurel.track.persist;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Transaction;

import com.aurel.track.beans.TScreenPanelBean;
import com.aurel.track.beans.TScreenTabBean;
import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.dao.ScreenTabDAO;
import com.workingdogs.village.Record;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TScreenTabPeer
extends com.aurel.track.persist.BaseTScreenTabPeer implements ScreenTabDAO{

	private static final Logger LOGGER = LogManager.getLogger(TScreenTabPeer.class);
	public static final long serialVersionUID = 400L;
	
	/**
	 * Loads the screenTab by primary key
	 * @param objectID
	 * @return
	 */
	public TScreenTabBean loadByPrimaryKey(Integer objectID)  {
		TScreenTab tobject = null;
		try {
			tobject = retrieveByPK(objectID);    		
		} catch(Exception e){
			LOGGER.warn("Loading of a screenTab by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} 
		TScreenTabBean result=null;
		if (tobject!=null){
			result= tobject.getBean();
		}
		return result;    	
	}
	public TScreenTabBean loadFullByPrimaryKey(Integer objectID)  {
		TScreenTabBean tabBean=null;
		Connection con = null;
		try{
			con = Transaction.begin(DATABASE_NAME);
			TScreenTab tab=retrieveByPK(objectID, con);
			tabBean=tab.getBean();
			List<IPanel> panels=loadFullChildren(objectID, con);
			tabBean.setPanels(panels);
			Transaction.commit(con);
		} catch (TorqueException e) {
			Transaction.safeRollback(con);
			LOGGER.error("Loding full a screen for key " + objectID + " failed with: " + e);
		}  
		return tabBean;
	}
	public static List<TScreenTabBean> loadByScreen(Integer objectID,Connection con) throws TorqueException {
		Criteria criteria = new Criteria();
		criteria.add(BaseTScreenTabPeer.PARENT,objectID);
		criteria.addAscendingOrderByColumn(BaseTScreenTabPeer.SORTORDER);
		List torqueList = null;
		try {
			torqueList = doSelect(criteria);
		} catch (TorqueException e) {
			LOGGER.error("loadByScreen failed with " + e.getMessage(), e);
		}
		return convertTorqueListToBeanList(torqueList);
	}
	public static List<IPanel> loadFullChildren(Integer objectID,Connection con) throws TorqueException {
		//LOGGER.debug("Getting children for tab:"+objectID+"...");
		Criteria critChild = new Criteria();
		critChild.add(BaseTScreenPanelPeer.PARENT,objectID);
		critChild.addAscendingOrderByColumn(BaseTScreenPanelPeer.SORTORDER);
		List<IPanel> result=new ArrayList<IPanel>();
		List panels=BaseTScreenPanelPeer.doSelect(critChild,con);
		for (Iterator it = panels.iterator(); it.hasNext();) {
			TScreenPanelBean panelBean=((TScreenPanel) it.next()).getBean();
			panelBean.setFields(TScreenPanelPeer.loadFullChildren(panelBean.getObjectID(), con));
			result.add(panelBean);
			
		}
		return result;
	}

	/**
	 * Loads all screenTabs from TScreenTab table 
	 * @return
	 */
	public List<TScreenTabBean> loadAll() {		
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all screenTabs failed with " + e.getMessage(), e);
			return null;
		}		
	}

	/**
	 * Saves a screenTab in the TScreenTab table
	 * @param bean
	 * @return
	 */
	public Integer save(TScreenTabBean bean){
		try {
			TScreenTab tobject = BaseTScreenTab.createTScreenTab(bean);
			if(tobject.getIndex()==null){
				tobject.setIndex(getNextSortOrder(tobject.getParent()));
			}
			if(tobject.getLabel()==null){
				tobject.setLabel("Tab"+tobject.getIndex());
			}
			if(tobject.getName()==null){
				tobject.setName("Tab"+tobject.getIndex());
			}
			if(tobject.getDescription()==null){
				tobject.setDescription("Tab"+tobject.getIndex()+" description");
			}
			tobject.save();
			return tobject.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a screenTab failed with " + e.getMessage(), e);
			return null;
		}		
	}

	/**
	 * Deletes a screenTab by primary key
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	public void delete(Integer objectID) {
		Connection con = null;
		try{
			con = Transaction.begin(DATABASE_NAME);
			deleteChildren(objectID, con);
			//reordering tabs
			TScreenTab tab=retrieveByPK(objectID,con);
			Criteria crit=new Criteria();
			crit.add(PARENT,tab.getParent());
			crit.add(SORTORDER,tab.getIndex(),Criteria.GREATER_THAN);
			crit.addAscendingOrderByColumn(SORTORDER);
			List siblingTabs=doSelect(crit,con);
			for(int i=0;i<siblingTabs.size();i++){
				TScreenTab s=(TScreenTab)siblingTabs.get(i);
				s.setIndex(new Integer(s.getIndex().intValue()-1));
				s.save(con);
			}
			//finaly delete the object 
			doDelete(SimpleKey.keyFor(objectID),con);
			Transaction.commit(con);
		} catch (TorqueException e) {
			Transaction.safeRollback(con);
			LOGGER.error("Deleting a screenPanel for key " + objectID + " failed with: " + e);
		}  
	}
	/**
	 * delete all children and then delete the object
	 * @param objectID
	 * @param con
	 * @throws TorqueException
	 */
	public static void deleteChildren(Integer objectID,Connection con) throws TorqueException {
		LOGGER.debug("Deleting children for tab:"+objectID+"...");
		//remove all panels
		Criteria critChild = new Criteria();
		critChild.add(BaseTScreenPanelPeer.PARENT,objectID);
		List panels=BaseTScreenPanelPeer.doSelect(critChild,con);
		for (Iterator iter = panels.iterator(); iter.hasNext();) {
			TScreenPanel panel=(TScreenPanel) iter.next();
			//delete children of panel
			TScreenPanelPeer.deleteChildren(panel.getObjectID(), con);
			//delete the panel
			BaseTScreenPanelPeer.doDelete(SimpleKey.keyFor(panel.getObjectID()),con);
		}
		LOGGER.debug("Children deleted for tab:"+objectID+"!");
	}


	/**
	 * Verify is a screenTab can be delete 
	 * @param objectID
	 */
	public boolean isDeletable(Integer objectID){
		return true;
	}
	
	/**
	 * Loads all tabs from parent 
	 * @return 
	 */
	public List<TScreenTabBean> loadByParent(Integer parentID){		
	    Criteria crit = new Criteria();
		crit.add(PARENT,parentID);
		crit.addAscendingOrderByColumn(SORTORDER);
		try	{
			return convertTorqueListToBeanList(doSelect(crit));
		} catch(TorqueException e){
			LOGGER.error("Loading tabs by Parent failed with " + e.getMessage(), e);
			return null;
		}		
	}
	/**
	 * Gets the last used sort order 
	 * @param objectID
	 * @return
	 */
	public static Integer getNextSortOrder(Integer objectID){
		Integer sortOrder = null;
		String max = "max(" + SORTORDER + ")";
		Criteria crit = new Criteria();		
		crit.add(PARENT, objectID);
		crit.addSelectColumn(max);		
		try {
			sortOrder = ((Record) doSelectVillageRecords(crit).get(0)).getValue(1).asIntegerObj();
		} catch (Exception e) {
			//LOGGER.error("Getting the next sortorder for the list " + listID + " failed with: " + e);		
		}
		if (sortOrder!=null){
			sortOrder = new Integer(sortOrder.intValue()+1);
		}else{
			sortOrder=new Integer(0);
		}
		LOGGER.debug("Next tab sort order="+sortOrder);
		return sortOrder;
	}

	private static List<TScreenTabBean> convertTorqueListToBeanList(List<TScreenTab> torqueList) {
		List<TScreenTabBean> beanList = new LinkedList<TScreenTabBean>();
		if (torqueList!=null){
			for (TScreenTab tScreenTab : torqueList) {
				beanList.add(tScreenTab.getBean());
			}			
		}
		return beanList;
	}
}
