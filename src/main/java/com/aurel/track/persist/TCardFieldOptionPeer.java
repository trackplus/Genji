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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TCardFieldOptionBean;
import com.aurel.track.dao.CardFieldOptionDAO;

/**
 * fields for card
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TCardFieldOptionPeer
    extends com.aurel.track.persist.BaseTCardFieldOptionPeer implements CardFieldOptionDAO{

    	static private Logger LOGGER = LogManager.getLogger(TCardFieldOptionPeer.class);

    	public List<TCardFieldOptionBean> loadByCardGroupingField(Integer cardGroupingFieldID){
    		List<TCardFieldOption> torqueList = null;
    		Criteria crit = new Criteria();
    		crit.add(GROUPINGFIELD, cardGroupingFieldID);
    		crit.addAscendingOrderByColumn(OPTIONPOSITION);
    		try {
    			torqueList = doSelect(crit);
    		} catch (TorqueException e) {
    			LOGGER.error("Loading TCardFieldOptions for cardField key:"+cardGroupingFieldID+" failed with " + e.getMessage(), e);
    		}
    		return convertTorqueListToBeanList(torqueList);
    	}
    	public TCardFieldOptionBean loadByCardGroupingFieldAndOption(Integer cardGroupingFieldID,Integer optionID){
    		List<TCardFieldOption> cardFieldOptions = null;
    		Criteria crit = new Criteria();
    		crit.add(GROUPINGFIELD, cardGroupingFieldID);
    		crit.add(OPTIONID,optionID);
    		try {
    			cardFieldOptions = doSelect(crit);
    		} catch(Exception e) {
    			LOGGER.error("Loading by loadByCardGroupingFieldAndOption cardGroupingFieldID=" +cardGroupingFieldID+" optionID="+optionID + " failed with " + e.getMessage(), e);
    		}
    		if (cardFieldOptions!=null && !cardFieldOptions.isEmpty()) {
    			return cardFieldOptions.get(0).getBean();
    		}else{
    			return null;
    		}
    	}
    	public Integer save(TCardFieldOptionBean cardFieldOptionBean){
    		try {
    			TCardFieldOption cardFieldOption = BaseTCardFieldOption.createTCardFieldOption(cardFieldOptionBean);
    			cardFieldOption.save();
    			return cardFieldOption.getObjectID();
    		} catch (Exception e) {
    			LOGGER.error("Saving of a cardFieldOptionBean failed with " + e.getMessage());
    			LOGGER.debug(ExceptionUtils.getStackTrace(e));
    			return null;
    		}
    	}
    	public void delete(Integer objectID) {
    		try {
    			doDelete(SimpleKey.keyFor(objectID));
    		} catch (TorqueException e) {
    			LOGGER.error("Deleting a TCardFieldOptionsBean for key " + objectID + " failed with: " + e);
    		}
    	}
    	
    	/**
    	 * Deletes an option for the field
    	 * @param fieldID
    	 * @param optionID
    	 */
    	public void deleteOptionForField(Integer fieldID, Integer optionID) {
    		if (fieldID==null) {
    			return;
    		}
    		Criteria criteria = new Criteria();
    		criteria.add(GROUPINGFIELD, fieldID);
    		criteria.add(OPTIONID, optionID);
    		try {
    			doDelete(criteria);
    		} catch (TorqueException e) {
    			LOGGER.error("Deleting the field for triggers failed with " + e.getMessage(), e);
    		}
    	}
    	
    	private List<TCardFieldOptionBean> convertTorqueListToBeanList(List<TCardFieldOption> torqueList) {
    		List<TCardFieldOptionBean> beanList = new ArrayList<TCardFieldOptionBean>();
    		if (torqueList!=null){
    			Iterator<TCardFieldOption> itrTorqueList = torqueList.iterator();
    			while (itrTorqueList.hasNext()){
    				beanList.add(itrTorqueList.next().getBean());
    			}
    		}
    		return beanList;
    	}

}
