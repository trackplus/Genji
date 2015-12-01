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
 * Class implementing a cascading select
 * @author Tamas Ruff
 *
 */
public class CustomOptionCascadingSelectBL extends CustomOptionBaseBL {
		
	private static OptionDAO optionDAO = DAOFactory.getFactory().getOptionDAO();
	
	private static CustomOptionCascadingSelectBL instance;
	
	public static CustomOptionCascadingSelectBL getInstance(){
		if (instance==null){
			 instance=new CustomOptionCascadingSelectBL();
		}
		return instance;
	}
	
	
	/**
	 * Delete an option with all his children (children first)
	 * @param optionID
	 * @param listID
	 */
	@Override
	public void deleteOptionWithChild(Integer optionID, Integer listID){
		List<TOptionBean> childOptions = optionDAO.getOptionsByParent(optionID);
		if (childOptions!=null) {
			Iterator<TOptionBean> itrChildOptions = childOptions.iterator();
			while (itrChildOptions.hasNext()) {
				TOptionBean optionBean = itrChildOptions.next();
				deleteOptionWithChild(optionBean.getObjectID(), optionBean.getList());
			}
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
		if (childOptions!=null) {
			Iterator<TOptionBean> itrChildOptions = childOptions.iterator();
			while (itrChildOptions.hasNext()) {			
				TOptionBean optionBean = itrChildOptions.next();
				hasAssignedWorkItem = hasDependentData(optionBean.getObjectID());
				if (hasAssignedWorkItem) {
					break;
				}
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
		List<TOptionBean> optionBeanList = optionDAO.loadActiveByListOrderedBySortorder(srcListID);	
		if (optionBeanList!=null && !optionBeanList.isEmpty()) {
			for (TOptionBean optionBean : optionBeanList) {
				copyBranch(optionBean, null, listHierarchy, copiedOptionsMap);
			}
		}
	}
	
	/**
	 * Gets a data source for the list
	 * @return
	 */
	private void copyBranch(TOptionBean srcParentOptionBean, Integer destParentID, ListHierarchy listHierarchy, Map<Integer, Integer> copiedOptionsMap) {										
		//prepare the destination optionBean
		TOptionBean destParentOptionBean = new TOptionBean();
		destParentOptionBean.setList(listHierarchy.getDestListID());
		destParentOptionBean.setLabel(srcParentOptionBean.getLabel());
		destParentOptionBean.setParentOption(destParentID);
		destParentOptionBean.setSortOrder(srcParentOptionBean.getSortOrder());
		destParentOptionBean.setIsDefault(srcParentOptionBean.getIsDefault());
		destParentOptionBean.setDeleted(srcParentOptionBean.isDeleted());					
		//save the destination optionBean
		Integer destOptionID = OptionBL.save(destParentOptionBean);
		if (copiedOptionsMap!=null) {
			copiedOptionsMap.put(srcParentOptionBean.getObjectID(), destOptionID);
		}
		//get the dependent lists
		List<ListHierarchy> childListHierarchy = listHierarchy.getDependentLists();
		if (childListHierarchy!=null && !childListHierarchy.isEmpty()) {
			for (ListHierarchy childHierarchy : childListHierarchy) {
				List<TOptionBean> childOptions = optionDAO.loadActiveByListAndParentOrderedBySortorder(childHierarchy.getSrcListID(), srcParentOptionBean.getObjectID());
				if (childOptions!=null && !childOptions.isEmpty()) {
					for (TOptionBean childOptionBean : childOptions) {
						copyBranch(childOptionBean, destOptionID, childHierarchy, copiedOptionsMap);			
					}
				}
			}
		}
	}	
}
