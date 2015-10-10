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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Transaction;

import com.aurel.track.beans.TScreenBean;
import com.aurel.track.beans.TScreenFieldBean;
import com.aurel.track.beans.TScreenPanelBean;
import com.aurel.track.beans.TScreenTabBean;
import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.beans.screen.ITab;
import com.aurel.track.dao.ScreenDAO;
import com.aurel.track.util.GeneralUtils;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TScreenPeer
	extends com.aurel.track.persist.BaseTScreenPeer implements ScreenDAO{

	private static final long serialVersionUID = -5588221992868145110L;
	
	private static final Logger LOGGER = LogManager.getLogger(TScreenPeer.class);

	private static Class[] replaceScreenPeerClasses = {
		TScreenConfigPeer.class
	};
	
	private static String[] replaceScreenFields = {
		TScreenConfigPeer.SCREEN
	};

	private static Class[] setToNullPeerClasses = {    	
    	TWorkflowActivityPeer.class         
    };
    
    private static String[] setToNullFields = {    	
    	TWorkflowActivityPeer.SCREEN
    };
    
	/**
	 * Loads the screen by primary key
	 * @param objectID
	 * @return
	 */
	public TScreenBean loadByPrimaryKey(Integer objectID)  {
		TScreen tobject = null;
		try {
			tobject = retrieveByPK(objectID);
		} catch(Exception e){
			LOGGER.warn("Loading of a screen by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} 
		if (tobject!=null){
			return tobject.getBean();
		}
		return null;
	}
	
	/**
	 * Loads the screen by primary key, fails silent if none is there
	 * @param objectID
	 * @return
	 */
	public TScreenBean tryToLoadByPrimaryKey(Integer objectID)  {
		TScreen tobject = null;
		try{
			tobject = retrieveByPK(objectID);
		}
		catch(Exception e){
			LOGGER.debug("Loading the screen by primary key " + objectID + " failed: " + e.getMessage(), e);
		} 
		if (tobject!=null){
			return tobject.getBean();
		}
		return null;
	}
	
	/**
	 * Load the screenBeans by primary keys
	 * @param screenIDs
	 * @return
	 */
	public List<TScreenBean> loadByKeys(List<Integer> screenIDs) {
		List<TScreenBean> screenBeans = new LinkedList<TScreenBean>();
		if (screenIDs==null || screenIDs.isEmpty()) {
        	return screenBeans;
		}
		List<int[]> screenIDChunksList = GeneralUtils.getListOfChunks(screenIDs);
		if (screenIDChunksList==null) {
			return screenBeans;
		}
		Iterator<int[]> iterator = screenIDChunksList.iterator();
		while (iterator.hasNext()) {
			int[] screenIDChunk = iterator.next();
			Criteria criteria = new Criteria();					
			criteria.addIn(OBJECTID, screenIDChunk);			
			try {
				screenBeans.addAll(convertTorqueListToBeanList(doSelect(criteria)));
			} catch(Exception e) {
	        	LOGGER.error("Loading the screens by screenIDs " + screenIDs.size() + " failed with " + e.getMessage(), e);
	        }			
		}
		return screenBeans;
	}
	
	public TScreenBean loadFullByPrimaryKey(Integer objectID)  {
		TScreenBean screenBean=null;
		Connection con = null;
		try{
			con = Transaction.begin(DATABASE_NAME);
			TScreen tscreen=retrieveByPK(objectID, con);
			screenBean=tscreen.getBean();

			List<TScreenTabBean> tabs=TScreenTabPeer.loadByScreen(objectID,con);
			List<TScreenPanelBean> panels=TScreenPanelPeer.loadByScreenID(objectID, con);
			List<TScreenFieldBean> fields=TScreenFieldPeer.loadByScreenID(objectID,con);
			Transaction.commit(con);

			Map<Integer,List<TScreenFieldBean>> fieldsMap=new HashMap<Integer, List<TScreenFieldBean>>();
			TScreenFieldBean fieldBean;
			List<TScreenFieldBean> myFields;
			for(int i=0;i<fields.size();i++){
				fieldBean=fields.get(i);
				myFields=fieldsMap.get(fieldBean.getParent());
				if(myFields==null){
					myFields=new ArrayList<TScreenFieldBean>();
				}
				myFields.add(fieldBean);
				fieldsMap.put(fieldBean.getParent(),myFields);
			}

			TScreenPanelBean panelBean;
			Map<Integer,List<TScreenPanelBean>> panelMap=new HashMap<Integer, List<TScreenPanelBean>>();
			List<TScreenPanelBean> myPanels;
			for(int i=0;i<panels.size();i++){
				panelBean=panels.get(i);
				myFields=fieldsMap.get(panelBean.getObjectID());
				panelBean.setFields((List)myFields);
				myPanels=panelMap.get(panelBean.getParent());
				if(myPanels==null){
					myPanels=new ArrayList<TScreenPanelBean>();
				}
				myPanels.add(panelBean);
				panelMap.put(panelBean.getParent(),myPanels);
			}

			TScreenTabBean tabBean;
			for(int i=0;i<tabs.size();i++){
				tabBean=tabs.get(i);
				myPanels=panelMap.get(tabBean.getObjectID());
				tabBean.setPanels((List)myPanels);
			}
			screenBean.setTabs((List)tabs);

		} catch (TorqueException e) {
			Transaction.safeRollback(con);
			LOGGER.error("Loding full a screen for key " + objectID + " failed with: " + e);
		}
		return screenBean;
	}
	public TScreenBean loadFullByPrimaryKey1(Integer objectID)  {
		System.out.println("loadFullByPrimaryKey:"+objectID);
		Date d1=new Date();
		TScreenBean screenBean=null;
		Connection con = null;
		try{
			con = Transaction.begin(DATABASE_NAME);
			TScreen tscreen=retrieveByPK(objectID, con);
			screenBean=tscreen.getBean();
			List<ITab> tabs=loadFullChildren(objectID, con);
			screenBean.setTabs(tabs);
			Transaction.commit(con);
		} catch (TorqueException e) {
			Transaction.safeRollback(con);
			LOGGER.error("Loding full a screen for key " + objectID + " failed with: " + e);
		}
		Date d4=new Date();
		System.out.println("loadFullByPrimaryKey ALL="+(d4.getTime()-d1.getTime()));
		return screenBean;
	}
	public static List<ITab> loadFullChildren(Integer objectID,Connection con) throws TorqueException {
		LOGGER.debug("Getting children for screen:"+objectID+"...");
		List<ITab> reasult=new ArrayList<ITab>();
		Criteria critTabs = new Criteria();
		critTabs.add(BaseTScreenTabPeer.PARENT,objectID);
		List tabs=BaseTScreenTabPeer.doSelect(critTabs,con);
		for (Iterator iterTabs = tabs.iterator(); iterTabs.hasNext();) {
			TScreenTab tab=(TScreenTab) iterTabs.next();
			TScreenTabBean tabBean=tab.getBean();
			List<IPanel> panels=TScreenTabPeer.loadFullChildren(tab.getObjectID(), con);
			tabBean.setPanels(panels);
			reasult.add(tabBean);
		}
		return reasult;
	}

	/**
	 * Loads all screens from TScreen table 
	 * @return
	 */
	public List<TScreenBean> loadAll() {
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all screens failed with " + e.getMessage(), e);
			return null;
		}
	}
	/**
	 * Load all screeen oreder by given column ascending or not
	 * @param sortKey
	 * @param ascending
	 * @return
	 */
	public List<TScreenBean> loadAllOrdered(String sortKey,boolean ascending){		
		Criteria crit = new Criteria();
		if(ascending){
			crit.addAscendingOrderByColumn(sortKey);
		}else{
			crit.addDescendingOrderByColumn(sortKey);
		}
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all screens failed with " + e.getMessage(), e);
			return null;
		}		
	}

	/**
	 * Saves a screen in the TScreen table
	 * @param bean
	 * @return
	 */
	public Integer save(TScreenBean bean){
		try {
			TScreen tobject = BaseTScreen.createTScreen(bean);
			if(tobject.getLabel()==null){
				tobject.setLabel(tobject.getName());
			}
			tobject.save();
			return tobject.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a screen failed with " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Deletes a screen by primary key
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	public void delete(Integer objectID) {
		Connection con = null;
		try{
			con = Transaction.begin(DATABASE_NAME);
			deleteChildren(objectID, con);
			doDelete(SimpleKey.keyFor(objectID),con);
			Transaction.commit(con);
		} catch (TorqueException e) {
			Transaction.safeRollback(con);
			LOGGER.error("Deleting a screen with key " + objectID + " failed with: " + e);
		}
	}
	
	/**
	 * Deletes the TScreen satisfying a certain criteria 
	 * together with the dependent database entries 
	 * @param crit
	 * @throws TorqueException 
	 */
	public static void doDelete(Criteria crit) throws TorqueException {
		List<TScreen> list = null;
		try {
			list = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Getting the list of TClasses to be deleted failed with " + e.getMessage(), e);
		}			
        if (list == null || list.size() < 1) {
            return;
        }
        for (TScreen tScreen : list) {
			LOGGER.warn("Deleting the screen  " + tScreen.getName());
			ReflectionHelper.setToNull(setToNullPeerClasses, setToNullFields, tScreen.getObjectID());
			//BaseTScreenPeer.doDelete(crit);
			new TScreenPeer().delete(tScreen.getObjectID());
		}
	}   
	
	/**
	 * delete all children and then delete the object
	 * @param objectID
	 * @param con
	 * @throws TorqueException
	 */
	private static void deleteChildren(Integer objectID,Connection con) throws TorqueException {
		//delete also all children
		LOGGER.debug("Deleting children for screen:"+objectID+"...");
		Criteria critTabs = new Criteria();
		critTabs.add(BaseTScreenTabPeer.PARENT,objectID);
		List tabs=BaseTScreenTabPeer.doSelect(critTabs,con);
		for (Iterator iterTabs = tabs.iterator(); iterTabs.hasNext();) {
			TScreenTab tab=(TScreenTab) iterTabs.next();
			//delete the children for the tab
			TScreenTabPeer.deleteChildren(tab.getObjectID(), con);
			//delete the tab
			BaseTScreenTabPeer.doDelete(SimpleKey.keyFor(tab.getObjectID()),con);
		}
	}

	/**
	 * Verify is a screen can be delete 
	 * @param objectID
	 */
	public boolean isDeletable(Integer objectID) {
		return !ReflectionHelper.hasDependentData(replaceScreenPeerClasses, replaceScreenFields, objectID);
	}
	
	/**
	 * Replaces the dependences with a new screenID
	 * @param oldScreenID
	 * @param newScreenID
	 */
	public void replaceScreen(Integer oldScreenID, Integer newScreenID) {
		ReflectionHelper.replace(replaceScreenPeerClasses, replaceScreenFields, oldScreenID, newScreenID);
	}
	
	private List<TScreenBean> convertTorqueListToBeanList(List<TScreen> torqueList) {
		List<TScreenBean> beanList = new LinkedList<TScreenBean>();
		List notDeletableScreens=getNotDeletableScreenIds();
		List owners=TPersonPeer.loadScreenOwners();
		if (torqueList!=null){
			Iterator itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				TScreenBean screenBean=((TScreen)itrTorqueList.next()).getBean();
				screenBean.setDeletable(isScreenDeletable(screenBean.getObjectID(),notDeletableScreens));
				screenBean.setOwnerName(findOwnerName(screenBean.getOwner(), owners));
				beanList.add(screenBean);
			}
		}
		return beanList;
	}
	private String findOwnerName(Integer personID,List owners){
		for (int i = 0; i < owners.size(); i++) {
			TPerson person=(TPerson) owners.get(i);
			if(person.getObjectID().equals(personID)){
				return person.getBean().getFullName();
			}
		}
		return null;
	}

	private List getNotDeletableScreenIds(){
		List result=new ArrayList();
		Criteria crit = new Criteria();
		crit.addJoin(BaseTScreenPeer.OBJECTID, BaseTScreenConfigPeer.SCREEN);
		try {
			List screens=doSelect(crit);
			if(screens!=null){
				for (int i = 0; i < screens.size(); i++) {
					TScreen screen=(TScreen) screens.get(i);
					result.add(screen.getObjectID());
				}
			}
		} catch (TorqueException e) {
			LOGGER.error("Get notDeletable screens failed with :"
					+ e.getMessage(), e);
		}	
		return result;
	}
	private boolean isScreenDeletable(Integer screenID,List notDeletableScreens){
		if(notDeletableScreens==null||notDeletableScreens.isEmpty()){
			return true;
		}
		return !(notDeletableScreens.contains(screenID));
	}
}
