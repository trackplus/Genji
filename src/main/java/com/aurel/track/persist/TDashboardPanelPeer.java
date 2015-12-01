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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Transaction;

import com.aurel.track.beans.TDashboardFieldBean;
import com.aurel.track.beans.TDashboardPanelBean;
import com.aurel.track.beans.screen.IField;
import com.aurel.track.dao.DashboardPanelDAO;
import com.aurel.track.itemNavigator.ItemNavigatorBL.QUERY_TYPE;
import com.aurel.track.itemNavigator.lastExecuted.LastExecutedBL;
import com.workingdogs.village.Record;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TDashboardPanelPeer
    extends com.aurel.track.persist.BaseTDashboardPanelPeer implements DashboardPanelDAO {

	private static final Logger LOGGER = LogManager.getLogger(TDashboardPanelPeer.class);
	public static final long serialVersionUID = 400L;
	
	/**
	 * Loads the DashboardPanel by primary key
	 * @param objectID
	 * @return
	 */
	@Override
	public TDashboardPanelBean loadByPrimaryKey(Integer objectID)  {
		TDashboardPanel tobject = null;
		try{
			tobject = retrieveByPK(objectID);
		}
		catch(Exception e){
			LOGGER.warn("Loading of a DashboardPanel by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (tobject!=null){
			return tobject.getBean();
		}
		return null;
	}
	@Override
	public TDashboardPanelBean loadFullByPrimaryKey(Integer objectID){
		TDashboardPanelBean panelBean=null;
		Connection con = null;
		try{
			con = Transaction.begin(DATABASE_NAME);
			TDashboardPanel tpanel=retrieveByPK(objectID, con);
			panelBean=tpanel.getBean();
			List<IField> fields=loadFullChildren(objectID, con);
			panelBean.setFields(fields);
			Transaction.commit(con);
		} catch (TorqueException e) {
			Transaction.safeRollback(con);
			LOGGER.error("Loding full a panel for key " + objectID + " failed with: " + e);
		}
		return panelBean;
	}
	public static List<IField> loadFullChildren(Integer objectID, Connection con) throws TorqueException {
		LOGGER.debug("Load children for panel:"+objectID);
		Criteria critChild = new Criteria();
		critChild.add(BaseTDashboardFieldPeer.PARENT,objectID);
		List<IField> result=new ArrayList<IField>();
		List torqueList=BaseTDashboardFieldPeer.doSelect(critChild,con);
		if (torqueList!=null){
			Iterator itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				TDashboardField dashboardField=(TDashboardField)itrTorqueList.next();
				TDashboardFieldBean fieldBean=dashboardField.getBean();
				updateFieldParameters(fieldBean,con);
				result.add(fieldBean);
			}
		}
		return result;
	}
	public static void updateFieldParameters(TDashboardFieldBean field,Connection con){
		List params=TDashboardParameterPeer.getByDashboardField(field.getObjectID(),con);
		Map mapParams=new HashMap();
		if(params!=null){
			for (Iterator it = params.iterator(); it.hasNext();) {
				TDashboardParameter  param= (TDashboardParameter) it.next();
				if(param.getParamValue()!=null){
					mapParams.put(param.getName(),param.getParamValue());
				}
			}
		}
		field.setParametres(mapParams);
	}
	/**
	 * Loads all DashboardPanels from TDashboardPanel table
	 * @return
	 */
	@Override
	public List loadAll() {
		List torqueList = null;
		Criteria crit = new Criteria();
		try {
			torqueList = doSelect(crit);
		} catch (TorqueException e) {
			LOGGER.error("Loading all DashboardPanels failed with " + e.getMessage());
		}
		return convertTorqueListToBeanList(torqueList);
	}

	/**
	 * Saves a DashboardPanel in the TDashboardPanel table
	 * @param bean
	 * @return
	 */
	@Override
	public Integer save(TDashboardPanelBean bean){
		Connection con = null;
		try{
			con = Transaction.begin(DATABASE_NAME);
			TDashboardPanel tobject = BaseTDashboardPanel.createTDashboardPanel(bean);
			if(tobject.getIndex()==null){
				tobject.setIndex(getNextSortOrder(tobject.getParent(),con));
			}
			if(tobject.getLabel()==null){
				tobject.setLabel("Panel"+tobject.getIndex());
			}
			if(tobject.getName()==null){
				tobject.setName("Panel"+tobject.getIndex());
			}
			if(tobject.getDescription()==null){
				tobject.setDescription(tobject.getLabel()+" description");
			}
			tobject.save(con);
			Transaction.commit(con);
			return tobject.getObjectID();
		} catch (Exception e) {
			Transaction.safeRollback(con);
			LOGGER.error("Saving of a DashboardPanel failed with " + e.getMessage());
			return null;
		}
	}

	/**
	 * Deletes a DashboardPanel by primary key
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	@Override
	public void delete(Integer objectID) {
		Connection con = null;
		try{
			con = Transaction.begin(DATABASE_NAME);
			deleteChildren(objectID, con);
			//reordering borders
			TDashboardPanel panel=retrieveByPK(objectID,con);
			Criteria crit=new Criteria();
			crit.add(PARENT,panel.getParent());
			crit.add(SORTORDER,panel.getIndex(),Criteria.GREATER_THAN);
			crit.addAscendingOrderByColumn(SORTORDER);
			List siblingPanels=doSelect(crit,con);
			for(int i=0;i<siblingPanels.size();i++){
				TDashboardPanel pan=(TDashboardPanel)siblingPanels.get(i);
				pan.setIndex(new Integer(pan.getIndex().intValue()-1));
				pan.save(con);
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
		//delete also all children
		LOGGER.debug("Delete children for panel:"+objectID);
		Criteria critChild = new Criteria();
		critChild.add(BaseTDashboardFieldPeer.PARENT,objectID);
		List fields=BaseTDashboardFieldPeer.doSelect(critChild,con);
	    for (Iterator iterator = fields.iterator(); iterator.hasNext();) {
            TDashboardField o =  (TDashboardField)iterator.next();
			Integer dashboardFieldID=o.getObjectID();
			//delete the dashboard field params
			Criteria critparams = new Criteria();
		    critparams.add(BaseTDashboardParameterPeer.DASHBOARDFIELD,dashboardFieldID);
            BaseTDashboardParameterPeer.doDelete(critparams,con);

			//delete the last executed query associated with this dashboard
			LastExecutedBL.deleteByFilterIDAndFilterType(dashboardFieldID, QUERY_TYPE.DASHBOARD, con);
        }
        BaseTDashboardFieldPeer.doDelete(critChild,con);
    }

	/**
	 * Verify is a DashboardPanel can be delete
	 * @param objectID
	 */
	@Override
	public boolean isDeletable(Integer objectID){
		return true;
	}

	/**
	 * Loads all panels from parent order by index
	 * @param parentID
	 * @return
	 */
	@Override
	public List loadByParent(Integer parentID){
		List torqueList = new ArrayList();
	    Criteria crit = new Criteria();
		crit.add(PARENT,parentID);
		crit.addAscendingOrderByColumn(SORTORDER);
		try	{
			torqueList = doSelect(crit);
		}
		catch(TorqueException e){
			LOGGER.error("Loading panels by Parent failed with " + e.getMessage());
		}
		return convertTorqueListToBeanList(torqueList);
	}
	/**
	 * Gets the last used sort order
	 * @param objectID
	 * @return
	 */
	private Integer getNextSortOrder(Integer objectID,Connection con){
		Integer sortOrder = null;
		String max = "max(" + SORTORDER + ")";
		Criteria crit = new Criteria();
		crit.add(PARENT, objectID);
		crit.addSelectColumn(max);
		try {
			sortOrder = ((Record) doSelectVillageRecords(crit,con).get(0)).getValue(1).asIntegerObj();
		} catch (Exception e) {
		}
		if (sortOrder!=null){
			sortOrder = new Integer(sortOrder.intValue()+1);
		}else{
			sortOrder=new Integer(0);
		}
		LOGGER.debug("Next panel sort order="+sortOrder);
		return sortOrder;
	}

	private List convertTorqueListToBeanList(List torqueList) {
		List beanList = new ArrayList();
		if (torqueList!=null){
			Iterator itrTorqueList = torqueList.iterator();
			while (itrTorqueList.hasNext()){
				beanList.add(((TDashboardPanel)itrTorqueList.next()).getBean());
			}
		}
		return beanList;
	}
	
}

