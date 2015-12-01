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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TCardGroupingFieldBean;
import com.aurel.track.dao.CardGroupingFieldDAO;
import com.aurel.track.fieldType.constants.BooleanFields;

/**
 * grouping field for card
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TCardGroupingFieldPeer
	extends com.aurel.track.persist.BaseTCardGroupingFieldPeer implements CardGroupingFieldDAO{

	static private Logger LOGGER = LogManager.getLogger(TCardGroupingFieldPeer.class);

	private static Class[] deletePeerClasses = {
    	TCardFieldOptionPeer.class,
    	//use the superclass doDelete() method!!!
    	BaseTCardGroupingFieldPeer.class
    };
    
    private static String[] deleteFields = {  
    	TCardFieldOptionPeer.GROUPINGFIELD,
    	BaseTCardGroupingFieldPeer.OBJECTID  	
    };
    
	@Override
	public List<TCardGroupingFieldBean> loadByNavigatorLayout(Integer navigatorLayoutID){
		List<TCardGroupingField> torqueList = null;
		Criteria crit = new Criteria();
		crit.add(NAVIGATORLAYOUT, navigatorLayoutID);
		try {
			torqueList = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Loading Card group field for navigator layout key:"+navigatorLayoutID+" failed with " + e.getMessage());
		}
		return convertTorqueListToBeanList(torqueList);
	}
	@Override
	public List<TCardGroupingFieldBean> loadActiveByNavigatorLayout(Integer navigatorLayoutID){
		List<TCardGroupingField> torqueList = null;
		Criteria crit = new Criteria();
		crit.add(NAVIGATORLAYOUT, navigatorLayoutID);
		crit.add(ISACTIV, BooleanFields.TRUE_VALUE);
		try {
			torqueList = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Loading Card group field for navigator layout key:"+navigatorLayoutID+" failed with " + e.getMessage());
		}
		return convertTorqueListToBeanList(torqueList);
	}
	@Override
	public Integer save(TCardGroupingFieldBean cardGroupingFieldBean){
		try {
			TCardGroupingField cardGroupingField = BaseTCardGroupingField.createTCardGroupingField(cardGroupingFieldBean);
			cardGroupingField.save();
			return cardGroupingField.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a cardGroupingField failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			return null;
		}
	}
	
	/**
	 * Deletes a navigator layout with dependencies
	 * @param objectID
	 * @return
	 */
	public void delete(Integer objectID) {
		ReflectionHelper.delete(deletePeerClasses, deleteFields, objectID);
	}
	
	/**
	 * Deletes the TQueryRepositoryBean satisfying a certain criteria 
	 * together with the dependent database entries 
	 * @param crit
	 */
	public static void doDelete(Criteria crit) {
		List<TCardGroupingField> list = null;
		try {
			list = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Getting the list of TNotifyTriggers to be deleted failed with " + e.getMessage());
		}			
        if (list == null || list.size() < 1) {
            return;
        }
        for (TCardGroupingField cardGroupingField : list) {
        	ReflectionHelper.delete(deletePeerClasses, deleteFields, cardGroupingField.getObjectID());		
		}
	}
	
	
	
	@Override
	public TCardGroupingFieldBean loadByID(Integer cardGroupingFieldID){
		TCardGroupingField tCardGroupingField = null;
		try {
			tCardGroupingField = retrieveByPK(cardGroupingFieldID);
		} catch(Exception e) {
			LOGGER.info("Loading the TCardGroupingFieldBean by primary key " + cardGroupingFieldID + " failed with " + e.getMessage());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		if (tCardGroupingField!=null) {
			return tCardGroupingField.getBean();
		}
		return null;
	}
	@Override
	public TCardGroupingFieldBean loadByGroupField(Integer navigatorID,Integer fieldID){
		List<TCardGroupingField> cardGroupingFields = null;
		Criteria crit = new Criteria();
		crit.add(NAVIGATORLAYOUT, navigatorID);
		crit.add(CARDFIELD,fieldID);
		try {
			cardGroupingFields = doSelect(crit);
		} catch(Exception e) {
			LOGGER.error("Loading by loadByGroupField navigatorID=" +navigatorID+" fieldID="+fieldID + " failed with " + e.getMessage());
		}
		if (cardGroupingFields!=null && !cardGroupingFields.isEmpty()) {
			return cardGroupingFields.get(0).getBean();
		}else{
			return null;
		}
	}

	@Override
	public void deleteByLayout(Integer navigatorLayoutID){
		Criteria crit = new Criteria();
		crit.add(NAVIGATORLAYOUT, navigatorLayoutID);
		try {
			doDelete(crit);
		} catch (Exception e) {
			LOGGER.error("Deleting the groupingField for layoutID "+navigatorLayoutID + " failed with: " + e);
		}
	}


	private List<TCardGroupingFieldBean> convertTorqueListToBeanList(List<TCardGroupingField> torqueList) {
		List<TCardGroupingFieldBean> beanList = new ArrayList<TCardGroupingFieldBean>();
		if (torqueList!=null){
			Iterator<TCardGroupingField> itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				beanList.add(itrTorqueList.next().getBean());
			}
		}
		return beanList;
	}
}
