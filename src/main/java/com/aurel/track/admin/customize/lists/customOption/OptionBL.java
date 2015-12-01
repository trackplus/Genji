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

import com.aurel.track.beans.TOptionBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.OptionDAO;
/**
 * DAO BL for custom options
 * @author Tamas Ruff
 *
 */
public class OptionBL {
	private static OptionDAO optionDAO = DAOFactory.getFactory().getOptionDAO();
	
	/**
	 * Gets a optionBean from the TOption table
	 * @param objectID
	 * @return
	 */
	public static TOptionBean loadByPrimaryKey(Integer objectID) {
		return optionDAO.loadByPrimaryKey(objectID);
	}
	
	/**
	 * Gets an optionBean by label
	 * @param list
	 * @param parentID
	 * @param label
	 * @return
	 */
	public static List<TOptionBean> loadByLabel(Integer list, Integer parentID, String label) {
		return optionDAO.loadByLabel(list, parentID, label);
	}
	
	/**
	 * Load all options
	 * @return
	 */
	public static List<TOptionBean> loadAll() {
		return optionDAO.loadAll();
	}
	
	/**
	 * Loads the create datasource for a listID
	 * @param listID
	 * @return
	 */
	public static List<TOptionBean> loadDataSourceByList(Integer listID) {
		return optionDAO.loadDataSourceByList(listID);
	}
	
	/**
	 * Load by keys
	 * @param objectIDs
	 * @return
	 */
	public static List<TOptionBean> loadByKeys(Integer[] objectIDs) {
		return optionDAO.loadByKeys(objectIDs);
	}
	
	/**
	 * returns all options related to the list with the object id
	 * @param listID
	 * @return
	 */
	public static List<TOptionBean> loadByListID(Integer listID) {
		return optionDAO.loadByListID(listID);
	}
	
	public static List<TOptionBean> loadByParentID(Integer parentID) {
		return optionDAO.getOptionsByParent(parentID);
	}
	
	/**
	 * Load all options for the selected list IDs
	 * @param listIds
	 * @return
	 */
	public static List<TOptionBean> loadForListIDs(List<Integer> listIds) {
		return optionDAO.loadForListIDs(listIds);
	}
	
	/**
	 * Saves a new/existing optionBean in the TOption table
	 * @param optionBean
	 * @return the created optionID
	 */
	public static Integer save(TOptionBean optionBean) {
		return optionDAO.save(optionBean);
	}
	
	/**
	 * Deletes an optionBean from the TOption table
	 * @param objectID
	 */
	public static void delete(Integer objectID) {
		optionDAO.delete(objectID);
	}
	
}
