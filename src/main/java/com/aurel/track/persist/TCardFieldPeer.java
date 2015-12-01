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
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TCardFieldBean;
import com.aurel.track.dao.CardFieldDAO;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TCardFieldPeer extends com.aurel.track.persist.BaseTCardFieldPeer implements CardFieldDAO {
	private static final Logger LOGGER = LogManager.getLogger(TCardFieldPeer.class);

	@Override
	public List<TCardFieldBean> loadByPanel(Integer panelID){
		List torqueList = new ArrayList();
		Criteria crit = new Criteria();
		crit.add(CARDPANEL,panelID);
		try	{
			torqueList = doSelect(crit);
		}
		catch(TorqueException e){
			LOGGER.error("Loading fiels by Parent failed with " + e.getMessage());
		}
		return convertTorqueListToBeanList(torqueList);
	}
	@Override
	public TCardFieldBean loadByPrimaryKey(Integer objectID)  {
		TCardField tobject = null;
		try{
			tobject = retrieveByPK(objectID);
		}
		catch(Exception e){
			LOGGER.info("Loading of a cardField by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (tobject!=null){
			return tobject.getBean();
		}
		return null;
	}
	@Override
	public Integer save(TCardFieldBean cardFieldBean){
		try {
			TCardField tobject = BaseTCardField.createTCardField(cardFieldBean);
			tobject.save();
			return tobject.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of a cardFieldBean failed with " + e.getMessage());
			return null;
		}
	}
	@Override
	public void delete(Integer objectID){
		try {
			doDelete(SimpleKey.keyFor(objectID));
		} catch (TorqueException e) {
			LOGGER.error("Deleting a cardField for key " + objectID + " failed with: " + e);
		}
	}

	@Override
	public List<TCardFieldBean> loadAll() {
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all cardFields failed with " + e.getMessage());
			return null;
		}
	}

	/**
	 * Loads  field from parent on given position
	 * @param parentID
	 * @param row
	 * @param col
	 * @return
	 */
	@Override
	public TCardFieldBean loadByParentAndIndex(Integer parentID,Integer row,Integer col){
		List result = new ArrayList();
		Criteria crit = new Criteria();
		crit.add(CARDPANEL,parentID);
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
		return ((TCardField)result.get(0)).getBean();
	}

	private static List<TCardFieldBean> convertTorqueListToBeanList(List<TCardField> torqueList) {
		List<TCardFieldBean> beanList = new LinkedList<TCardFieldBean>();
		if (torqueList!=null) {
			for (TCardField tCardField : torqueList) {
				beanList.add(tCardField.getBean());
			}
		}
		return beanList;
	}

}
