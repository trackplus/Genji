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
 * Class implementing a simple select
 * @author Tamas Ruff
 *
 */
public class CustomOptionSimpleSelectBL extends CustomOptionBaseBL {
	
	private OptionDAO optionDAO = DAOFactory.getFactory().getOptionDAO();
	
	private static CustomOptionSimpleSelectBL instance;
	
	public static CustomOptionSimpleSelectBL getInstance(){
		if (instance==null){
			instance=new CustomOptionSimpleSelectBL();
		}
		return instance;
	}
	
	/**
	 * Delete an option. For simple select no children are defined
	 * @param optionID
	 * @param listID
	 */
	@Override
	public void deleteOptionWithChild(Integer optionID, Integer listID) {
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
	 * Whether the option is associated with a workItem
	 * @param optionID
	 * @return
	 */
	@Override
	public boolean hasDependentData(Integer optionID) {
		return optionDAO.hasDependentData(optionID);
	}

	/**
	 * Sets an option as deleted
	 * @param optionBean
	 * @param flag
	 */
	@Override
	public void setDeleted(TOptionBean optionBean, boolean flag) {
		optionBean.setDeleted(flag);
		OptionBL.save(optionBean);
	}
	
	/**
	 * Makes a copy of the entire list
	 * @param listID
	 * @param copiedOptionsMap  out parameter
	 */
	@Override
	public synchronized void copyOptions(ListHierarchy listHierarchy, Map<Integer, Integer> copiedOptionsMap) {
		Integer srcListID = listHierarchy.getSrcListID();
		Integer destListID = listHierarchy.getDestListID();
		List<TOptionBean> optionBeanList = optionDAO.loadActiveByListOrderedBySortorder(srcListID);
		if (optionBeanList!=null) {
			for (TOptionBean srcOptionBean : optionBeanList) {
				TOptionBean destOptionBean = new TOptionBean();
				destOptionBean.setList(destListID);
				destOptionBean.setLabel(srcOptionBean.getLabel());
				destOptionBean.setSortOrder(srcOptionBean.getSortOrder());
				destOptionBean.setIsDefault(srcOptionBean.getIsDefault());
				Integer destOptionID = OptionBL.save(destOptionBean);
				if (copiedOptionsMap!=null) {
					copiedOptionsMap.put(srcOptionBean.getObjectID(), destOptionID);
				}
			}
		}
	}
}
