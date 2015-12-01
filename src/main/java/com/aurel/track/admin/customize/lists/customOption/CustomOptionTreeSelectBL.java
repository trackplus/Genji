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


package com.aurel.track.admin.customize.lists.customOption;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aurel.track.admin.customize.localize.LocalizeBL;
import com.aurel.track.beans.TOptionBean;
import com.aurel.track.cluster.ClusterBL.CHANGE_TYPE;
import com.aurel.track.cluster.ClusterMarkChangesBL;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.OptionDAO;
import com.aurel.track.fieldType.design.custom.select.config.ListHierarchy;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.index.listFields.LocalizedListIndexer;
import com.aurel.track.resources.LocalizationKeyPrefixes;

/**
 * Class implementing a tree select
 * @author Tamas Ruff
 *
 */
public class CustomOptionTreeSelectBL extends CustomOptionBaseBL {
	
	private static OptionDAO optionDAO = DAOFactory.getFactory().getOptionDAO();
	
	private static CustomOptionTreeSelectBL instance;
	
	public static CustomOptionTreeSelectBL getInstance(){
		if (instance==null){
			 instance=new CustomOptionTreeSelectBL();
		}
		return instance;
	}
	
	public static List<TOptionBean> loadByList(Integer listID){
		return optionDAO.loadActiveByListOrderedBySortorder(listID);
	}
	
	/**
	 * Delete an option with all his children (children first)
	 * @param optionID
	 * @param listID
	 */
	@Override
	public void deleteOptionWithChild(Integer optionID, Integer listID) {
		List<TOptionBean> childOptions = optionDAO.getOptionsByParent(optionID);
		Iterator<TOptionBean> itrChildOptions = childOptions.iterator();
		while (itrChildOptions.hasNext()) {
			TOptionBean optionBean = itrChildOptions.next();
			deleteOptionWithChild(optionBean.getObjectID(), listID);
		}
		deleteOptionAndBlob(optionID);
		LocalizeBL.removeLocalizedResources(
				LocalizationKeyPrefixes.FIELD_CUSTOMSELECT_KEY_PREFIX+listID, optionID);
		//delete the option from lucene index
		LocalizedListIndexer.getInstance().deleteByKeyAndType(
			optionID, LuceneUtil.LOOKUPENTITYTYPES.CUSTOMOPTION);
		//possible lucene update, custom option is not cached
		ClusterMarkChangesBL.markDirtyCustomListEntryInCluster(listID, optionID, CHANGE_TYPE.DELETE_FROM_INDEX);
	}
	
	/**
	 * Sets an option and all its descendants as deleted
	 * @param optionBean
	 * @param flag
	 */
	@Override
	public void setDeleted(TOptionBean optionBean, boolean flag) {
		optionBean.setDeleted(flag);
		OptionBL.save(optionBean);
		List<TOptionBean> childOptions = optionDAO.getOptionsByParent(optionBean.getObjectID());
		if (childOptions!=null) {
			Iterator<TOptionBean> itrChildOptions = childOptions.iterator();
			while (itrChildOptions.hasNext()) {
				TOptionBean childOptionBean = itrChildOptions.next();
				setDeleted(childOptionBean, flag);
			}
		}
	}
	
	
	/**
	 * Deletes all options hierarchically
	 * First the childs should be deleted in order to avoid foreign key problems 
	 * Before calling this method it should be tested 
	 * whether no option from the list has workItem dependence
	 * @param listID
	 */
	
	/**
	 * Whether the option or any of its descendants are associated with a workItem
	 * @param optionID
	 * @return
	 */
	@Override
	public boolean hasDependentData(Integer optionID) {
		boolean hasAssignedWorkItem = false;
		if (optionDAO.hasDependentData(optionID)) {
			return true;
		}
		List<TOptionBean> childOptions = optionDAO.getOptionsByParent(optionID);
		Iterator<TOptionBean> itrChildOptions = childOptions.iterator();
		while (itrChildOptions.hasNext()) {
			TOptionBean optionBean = itrChildOptions.next();
			hasAssignedWorkItem = hasDependentData(optionBean.getObjectID());
			if (hasAssignedWorkItem) {
				break;
			}
		}
		return hasAssignedWorkItem;
	}
	
	/**
	 * Makes a copy of the entire list hierarchically
	 * @param listHierarchy
	 * @param copiedOptionsMap out parameter
	 */
	@Override
	public void copyOptions(ListHierarchy listHierarchy, Map<Integer, Integer> copiedOptionsMap) {
		Integer srcListID = listHierarchy.getSrcListID();
		Integer destListID = listHierarchy.getDestListID();
		List<TOptionBean> optionBeanList = optionDAO.loadActiveByListOrderedBySortorder(srcListID);
		if (optionBeanList!=null && !optionBeanList.isEmpty()) {
			for (TOptionBean optionBean : optionBeanList) {
				copyBranch(optionBean, null, destListID, copiedOptionsMap);
			}
		}
	}
	
	/**
	 * Copies a tree branch recursively
	 * @param srcParentOptionBean
	 * @param destParentID
	 * @param destListID
	 * @param copiedOptionsMap
	 */
	private void copyBranch(TOptionBean srcParentOptionBean, Integer destParentID, Integer destListID, Map<Integer, Integer> copiedOptionsMap) {				
		TOptionBean destParentOptionBean = new TOptionBean();
		destParentOptionBean.setList(destListID);
		destParentOptionBean.setLabel(srcParentOptionBean.getLabel());
		destParentOptionBean.setSortOrder(srcParentOptionBean.getSortOrder());
		destParentOptionBean.setIsDefault(srcParentOptionBean.getIsDefault());
		srcParentOptionBean.setParentOption(destParentID);
		Integer destOptionID = OptionBL.save(destParentOptionBean);
		if (copiedOptionsMap!=null) {
			copiedOptionsMap.put(srcParentOptionBean.getObjectID(), destOptionID);
		}
		List<TOptionBean> childLists = optionDAO.loadActiveByListAndParentOrderedBySortorder(srcParentOptionBean.getList(), srcParentOptionBean.getObjectID());		
		if (childLists!=null && !childLists.isEmpty()) {
			for (TOptionBean childOptionBean : childLists) {
				copyBranch(childOptionBean, destOptionID, destListID, copiedOptionsMap);
			}
		}
	}
}
