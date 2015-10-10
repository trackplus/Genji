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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Transaction;

import com.aurel.track.beans.TDashboardFieldBean;
import com.aurel.track.dao.DashboardFieldDAO;
import com.aurel.track.itemNavigator.ItemNavigatorBL.QUERY_TYPE;
import com.aurel.track.itemNavigator.lastExecuted.LastExecutedBL;
import com.workingdogs.village.Record;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TDashboardFieldPeer
    extends com.aurel.track.persist.BaseTDashboardFieldPeer implements DashboardFieldDAO {

	private static final Logger LOGGER = LogManager.getLogger(TDashboardFieldPeer.class);
	public static final long serialVersionUID = 400L;
	
	/**
	 * Loads the DashboardField by primary key
	 * @param objectID
	 * @return
	 */
	public TDashboardFieldBean loadByPrimaryKey(Integer objectID)  {
		Connection con = null;
		TDashboardFieldBean field = null;
		try{
			con = Transaction.begin(DATABASE_NAME);
            TDashboardField tobject = retrieveByPK(objectID,con);
            if (tobject!=null){
               field=tobject.getBean();
               setParamaters(field,con); 
            }
            Transaction.commit(con);
        }
		catch(Exception e){
			Transaction.safeRollback(con);
            LOGGER.warn("Loading of a DashboardField by primary key " + objectID + " failed with " + e.getMessage());
            LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return field;
	}
    private void setParamaters(TDashboardFieldBean field,Connection con){
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
	 * Loads all DashboardFields from TDashboardField table
	 * @return
	 */
	public List loadAll() {
        Connection con = null;
        List beanList=null;
        Criteria crit = new Criteria();
		try {
            con = Transaction.begin(DATABASE_NAME);			
            Transaction.commit(con);
            beanList= convertTorqueListToBeanList(doSelect(crit,con));
            for (int i = 0; i < beanList.size(); i++) {
                TDashboardFieldBean o =  (TDashboardFieldBean)beanList.get(i);
                setParamaters(o,con);
            }
            Transaction.commit(con);
        } catch (TorqueException e) {
			Transaction.safeRollback(con);
            LOGGER.error("Loading all DashboardFields failed with " + e.getMessage(), e);
		}
        return beanList;
    }

	/**
	 * Loads TDashboardFields by IDs
	 * @return
	 */
	public List<TDashboardFieldBean> loadByKeys(Set<Integer> objectIDs) {
		Connection con = null;
        List beanList=null;
        Criteria crit = new Criteria();
        crit.addIn(OBJECTID, objectIDs.toArray());
		try {
            con = Transaction.begin(DATABASE_NAME);			
            beanList= convertTorqueListToBeanList(doSelect(crit,con));
            for (int i = 0; i < beanList.size(); i++) {
                TDashboardFieldBean o =  (TDashboardFieldBean)beanList.get(i);
                setParamaters(o,con);
            }
            Transaction.commit(con);
        } catch (TorqueException e) {
			Transaction.safeRollback(con);
            LOGGER.error("Loading all DashboardFields failed with " + e.getMessage(), e);
		}
        return beanList;
    }
	
	/**
	 * Saves a DashboardField in the TDashboardField table
	 * @param bean
	 * @return
	 */
	public Integer save(TDashboardFieldBean bean){
		Connection con = null;
        try {
			con = Transaction.begin(DATABASE_NAME);
            TDashboardField tobject = BaseTDashboardField.createTDashboardField(bean);
            tobject.save(con);
            bean.setObjectID(tobject.getObjectID());
            TDashboardParameterPeer.saveParameters(bean,con);
            Transaction.commit(con);
            return tobject.getObjectID();
		} catch (Exception e) {
            Transaction.safeRollback(con);
            LOGGER.error("Saving of a DashboardField failed with " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Deletes a DashboardField by primary key
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	public void delete(Integer objectID) {
        Connection con = null;
        try {
			con = Transaction.begin(DATABASE_NAME);
            //TLastExecutedQueryPeer.deleteByDashboard(objectID,con);
			LastExecutedBL.deleteByFilterIDAndFilterType(objectID, QUERY_TYPE.DASHBOARD, con);
            TDashboardParameterPeer.deleteByDashboardField(objectID,con);
            doDelete(SimpleKey.keyFor(objectID),con);
            Transaction.commit(con);
        } catch (TorqueException e) {
			Transaction.safeRollback(con);
            LOGGER.error("Deleting a DashboardField for key " + objectID + " failed with: " + e);
		}
	}

	/**
	 * Verify is a DashboardField can be delete
	 * @param objectID
	 */
	public boolean isDeletable(Integer objectID){
		return true;
	}

	/**
	 * Loads all fields from parent
	 * @param parentID
	 * @return
	 */
	public List loadByParent(Integer parentID){
		Connection con = null;
        List beanList=null;
        List torqueList = new ArrayList();
	    Criteria crit = new Criteria();
		crit.add(PARENT,parentID);
		try	{
            con = Transaction.begin(DATABASE_NAME);
            torqueList = doSelect(crit,con);
            beanList=convertTorqueListToBeanList(torqueList);
            for (int i = 0; i < beanList.size(); i++) {
                TDashboardFieldBean o =  (TDashboardFieldBean)beanList.get(i);
                setParamaters(o,con);
            }
            Transaction.commit(con);
        } catch (TorqueException e) {
			Transaction.safeRollback(con);
			LOGGER.error("Loading fiels by Parent failed with " + e.getMessage(), e);
		}

        return beanList;
    }

	public HashMap<Integer, ArrayList<String>> loadTabsContentTypes(List<Integer> tabID) {
		HashMap<Integer, ArrayList<String>>dummy = new HashMap<Integer, ArrayList<String>>();
		for(int i = 0; i < tabID.size(); i++) {
			 ArrayList<String>tmp  = new ArrayList<String>();
			 tmp.add("TEST");
			 dummy.put(tabID.get(i), tmp);
		}
		return dummy;
	}
	/**
	 * Gets the last used sort order
	 * @param objectID
	 * @return
	 */
	public static Integer getNextSortOrder(Integer objectID){
		Integer sortOrder = null;
		//String max = "max(" + SORTORDER + ")";
		Criteria crit = new Criteria();
		crit.add(PARENT, objectID);
		//crit.addSelectColumn(max);
		try {
			sortOrder = ((Record) doSelectVillageRecords(crit).get(0)).getValue(1).asIntegerObj();
		} catch (Exception e) {
			//LOGGER.error("Getting the next sortorder for the list " + listID + " failed with: " + e);
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
     * @param  col
	 * @return
	 */
	public TDashboardFieldBean loadByParentAndIndex(Integer parentID,Integer row,Integer col){
		List result = new ArrayList();
	    Criteria crit = new Criteria();
		crit.add(PARENT,parentID);
		crit.add(ROWINDEX,row);
		crit.add(COLINDEX,col);
        Connection con=null;
        TDashboardFieldBean fieldBean=null;
        try	{
            con = Transaction.begin(DATABASE_NAME);
            result = doSelect(crit,con);
            if(result!=null && !result.isEmpty()){
                fieldBean=((TDashboardField)result.get(0)).getBean();
                setParamaters(fieldBean,con);
            }
            Transaction.commit(con);
        } catch (TorqueException e) {
			 Transaction.safeRollback(con);
             LOGGER.error("Loading field by Parent and index failed with " + e.getMessage(), e);
         }
        return  fieldBean;
    }
	private List<TDashboardFieldBean> convertTorqueListToBeanList(List<TDashboardField> torqueList) {
		List<TDashboardFieldBean> beanList = new ArrayList<TDashboardFieldBean>();
		if (torqueList!=null){
			for (TDashboardField dashboardField : torqueList) {
				beanList.add(dashboardField.getBean());
			}
		}
		return beanList;
	}
}
