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

package com.aurel.track.persist;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TScreenFieldBean;
import com.aurel.track.dao.ScreenFieldDAO;
import com.workingdogs.village.Record;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TScreenFieldPeer
extends com.aurel.track.persist.BaseTScreenFieldPeer implements ScreenFieldDAO{

	private static final Logger LOGGER = LogManager.getLogger(TScreenFieldPeer.class);
	public static final long serialVersionUID = 400L;
	
	/**
	 * Loads the screenField by primary key
	 * @param objectID
	 * @return
	 */
	@Override
	public TScreenFieldBean loadByPrimaryKey(Integer objectID)  {    	
		TScreenField tobject = null;
		try {
			tobject = retrieveByPK(objectID);    		
		} catch(Exception e){
			LOGGER.warn("Loading of a screenField by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (tobject!=null){
			return tobject.getBean();
		}
		return null;    	
	}

	/**
	 * Loads all screenFields from TScreenField table 
	 * @return
	 */
	@Override
	public List<TScreenFieldBean> loadAll() {		
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all screenFields failed with " + e.getMessage());
			return null;
		}		
	}

	/**
	 * Saves a screenField in the TScreenField table
	 * @param bean
	 * @return
	 */
	@Override
	public Integer save(TScreenFieldBean bean){
		try {
			TScreenField tobject = BaseTScreenField.createTScreenField(bean);
			tobject.save();
			return tobject.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a screenField failed with " + e.getMessage());
			return null;
		}		
	}

	/**
	 * Deletes a screenField by primary key
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	@Override
	public void delete(Integer objectID) {
		try {
			doDelete(SimpleKey.keyFor(objectID));
		} catch (TorqueException e) {
			LOGGER.error("Deleting a screenField for key " + objectID + " failed with: " + e);
		}  
	}

	/**
	 * Verify is a screenField can be delete 
	 * @param objectID
	 */
	@Override
	public boolean isDeletable(Integer objectID){
		return true;
	}

	public static List<TScreenFieldBean> loadByScreenID(Integer screenID, Connection con) throws TorqueException {
		List torqueList = new ArrayList();
		Criteria criteria = new Criteria();
		criteria.addJoin(BaseTScreenFieldPeer.PARENT,BaseTScreenPanelPeer.OBJECTID);
		criteria.addJoin(BaseTScreenPanelPeer.PARENT,BaseTScreenTabPeer.OBJECTID);
		criteria.add(BaseTScreenTabPeer.PARENT,screenID);
		try {
			torqueList = doSelect(criteria,con);
		} catch (TorqueException e) {
			LOGGER.error("Loading all screenPanels failed with " + e.getMessage());
		}
		return convertTorqueListToBeanList(torqueList);
	}
	
	/**
	 * Loads all fields from parent
	 * @param parentID 
	 * @return 
	 */
	@Override
	public List loadByParent(Integer parentID){
		List torqueList = new ArrayList();
	    Criteria crit = new Criteria();
		crit.add(PARENT,parentID);
		try	{
			torqueList = doSelect(crit);
		}
		catch(TorqueException e){
			LOGGER.error("Loading fiels by Parent failed with " + e.getMessage());
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
		Criteria crit = new Criteria();		
		crit.add(PARENT, objectID);
		try {
			sortOrder = ((Record) doSelectVillageRecords(crit).get(0)).getValue(1).asIntegerObj();
		} catch (Exception e) {
		}
		if (sortOrder!=null){
			sortOrder = new Integer(sortOrder.intValue()+1);
		}else{
			sortOrder=new Integer(1);
		}
		return sortOrder;
	}
	
	/**
	 * Loads  field from parent on given position
	 * @param parentID
	 * @param row
	 * @param col
	 * @return 
	 */
	@Override
	public TScreenFieldBean loadByParentAndIndex(Integer parentID,Integer row,Integer col){
		List result = new ArrayList();
	    Criteria crit = new Criteria();
		crit.add(PARENT,parentID);
		crit.add(ROWINDEX,row);
		crit.add(COLINDEX,col);
		try	{
			result = doSelect(crit);
		}
		catch(Exception e){
			LOGGER.error("Loading field by Parent and index failed with " + e.getMessage());
		}
		if(result==null||result.isEmpty()){
			return null;
		}
		return ((TScreenField)result.get(0)).getBean();
	}
	private static List<TScreenFieldBean> convertTorqueListToBeanList(List<TScreenField> torqueList) {
		List<TScreenFieldBean> beanList = new LinkedList<TScreenFieldBean>();
		if (torqueList!=null) {
			for (TScreenField tScreenField : torqueList) {
				beanList.add(tScreenField.getBean());
			}			
		}
		return beanList;
	}
	
}
