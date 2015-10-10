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
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Transaction;

import com.aurel.track.beans.TDashboardPanelBean;
import com.aurel.track.beans.TDashboardTabBean;
import com.aurel.track.beans.screen.IField;
import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.dao.DashboardTabDAO;
import com.workingdogs.village.Record;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TDashboardTabPeer
    extends com.aurel.track.persist.BaseTDashboardTabPeer implements DashboardTabDAO {

	private static final Logger LOGGER = LogManager.getLogger(TDashboardTabPeer.class);

	public static final long serialVersionUID = 400L;
	
	/**
	 * Loads the DashboardTab by primary key
	 * @param objectID
	 * @return
	 */
	public TDashboardTabBean loadByPrimaryKey(Integer objectID)  {
		TDashboardTab tobject = null;
		try{
			tobject = retrieveByPK(objectID);
		}
		catch(Exception e){
			LOGGER.warn("Loading of a DashboardTab by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		TDashboardTabBean result=null;
		if (tobject!=null){
			result= tobject.getBean();
		}
		return result;
	}
	public TDashboardTabBean loadFullByPrimaryKey(Integer objectID)  {
		TDashboardTabBean tabBean=null;
		Connection con = null;
		try{
			con = Transaction.begin(DATABASE_NAME);
			TDashboardTab tab=retrieveByPK(objectID, con);
			tabBean=tab.getBean();
			List<IPanel> panels=loadFullChildren(objectID, con);
			tabBean.setPanels(panels);
			Transaction.commit(con);
		} catch (TorqueException e) {
			Transaction.safeRollback(con);
			LOGGER.error("Loding full a tab for key " + objectID + " failed with: " + e);
		}  
		return tabBean;
	}
	public static List<IPanel> loadFullChildren(Integer objectID,Connection con) throws TorqueException {
		//delete also all children
		LOGGER.debug("Getting children for tab:"+objectID+"...");
		Criteria critChild = new Criteria();
		critChild.add(BaseTDashboardPanelPeer.PARENT,objectID);
		critChild.addAscendingOrderByColumn(BaseTDashboardPanelPeer.SORTORDER);
		List<IPanel> result=new ArrayList<IPanel>();
		List panels=BaseTDashboardPanelPeer.doSelect(critChild,con);
		for (Iterator iter = panels.iterator(); iter.hasNext();) {
			TDashboardPanel panel=(TDashboardPanel) iter.next();
			TDashboardPanelBean panelBean=panel.getBean();
			
			List<IField> fields=TDashboardPanelPeer.loadFullChildren(panel.getObjectID(), con);
			panelBean.setFields(fields);
			result.add(panelBean);
			
		}
		return result;
	}

	/**
	 * Loads all DashboardTabs from TDashboardTab table
	 * @return
	 */
	public List loadAll() {
		List torqueList = null;
		Criteria crit = new Criteria();
		try {
			torqueList = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Loading all DashboardTabs failed with " + e.getMessage(), e);
		}
		return convertTorqueListToBeanList(torqueList);
	}

	/**
	 * Saves a DashboardTab in the TDashboardTab table
	 * @param bean
	 * @return
	 */
	public Integer save(TDashboardTabBean bean){
		try {
			TDashboardTab tobject = BaseTDashboardTab.createTDashboardTab(bean);
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
			LOGGER.error("Saving of a DashboardTab failed with " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Deletes a DashboardTab by primary key
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	public void delete(Integer objectID) {
		Connection con = null;
		try{
			con = Transaction.begin(DATABASE_NAME);
			deleteChildren(objectID, con);
			//reordering tabs
			TDashboardTab tab=retrieveByPK(objectID,con);
			Criteria crit=new Criteria();
			crit.add(PARENT,tab.getParent());
			crit.add(SORTORDER,tab.getIndex(),Criteria.GREATER_THAN);
			crit.addAscendingOrderByColumn(SORTORDER);
			List siblingTabs=doSelect(crit,con);
			for(int i=0;i<siblingTabs.size();i++){
				TDashboardTab s=(TDashboardTab)siblingTabs.get(i);
				s.setIndex(new Integer(s.getIndex().intValue()-1));
				s.save(con);
			}
			//finaly delete the object
			doDelete(SimpleKey.keyFor(objectID),con);
			Transaction.commit(con);
		} catch (TorqueException e) {
			Transaction.safeRollback(con);
			LOGGER.error("Deleting a DashboardPanel for key " + objectID + " failed with: " + e);
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
		critChild.add(BaseTDashboardPanelPeer.PARENT,objectID);
		List panels=BaseTDashboardPanelPeer.doSelect(critChild,con);
		for (Iterator iter = panels.iterator(); iter.hasNext();) {
			TDashboardPanel panel=(TDashboardPanel) iter.next();
			//delete children of panel
			TDashboardPanelPeer.deleteChildren(panel.getObjectID(), con);
			//delete the panel
			BaseTDashboardPanelPeer.doDelete(SimpleKey.keyFor(panel.getObjectID()),con);
		}
		LOGGER.debug("Children deleted for tab:"+objectID+"!");
	}


	/**
	 * Verify is a DashboardTab can be delete
	 * @param objectID
	 */
	public boolean isDeletable(Integer objectID){
		return true;
	}

	/**
	 * Loads all tabs from parent
	 * @return
	 */
	public List loadByParent(Integer parentID){
		List torqueList = new ArrayList();
	    Criteria crit = new Criteria();
		crit.add(PARENT,parentID);
		crit.addAscendingOrderByColumn(SORTORDER);
		try	{
			torqueList = doSelect(crit);
		}
		catch(TorqueException e){
			LOGGER.error("Loading tabs by Parent failed with " + e.getMessage(), e);
		}
		return convertTorqueListToBeanList(torqueList);
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

	private List convertTorqueListToBeanList(List torqueList) {
		List beanList = new ArrayList();
		if (torqueList!=null){
			Iterator itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				beanList.add(((TDashboardTab)itrTorqueList.next()).getBean());
			}
		}
		return beanList;
	}
}
