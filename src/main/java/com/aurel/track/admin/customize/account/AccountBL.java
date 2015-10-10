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

package com.aurel.track.admin.customize.account;

import java.util.List;

import com.aurel.track.beans.TAccountBean;
import com.aurel.track.beans.TCostCenterBean;
import com.aurel.track.dao.AccountDAO;
import com.aurel.track.dao.CostCenterDAO;
import com.aurel.track.dao.DAOFactory;

public class AccountBL {

	private static AccountDAO accountDAO = DAOFactory.getFactory().getAccountDAO();
	private static CostCenterDAO costCenterDAO = DAOFactory.getFactory().getCostCenterDAO();

	private AccountBL() {
	}

	public static TAccountBean loadByPrimaryKey(Integer objectID) {
		return accountDAO.loadByPrimaryKey(objectID);
	}

	public static List<TAccountBean> loadByCostcenter(Integer costcenterID) {
		return accountDAO.loadByCostcenter(costcenterID);
	}

	public static List<TCostCenterBean> getAllCostcenters() {
		return costCenterDAO.loadAll();
	}

	public static List<TAccountBean> getAllAccounts() {
		return accountDAO.loadAll();
	}

	public static List<TAccountBean> loadAccountsByKeys(List<Integer> accountIDs) {
		return accountDAO.loadByKeys(accountIDs);
	}

	public static List<TAccountBean> loadActiveByProject(Integer projectID) {
		return accountDAO.loadByProjectForStateflag(projectID,
				TAccountBean.STATEFLAG.AVAILABLE);
	}

	public static List<TAccountBean> loadAllActive() {
		return accountDAO.loadByStateflag(TAccountBean.STATEFLAG.AVAILABLE);
	}

	/**
	 * Saves a new/modified object
	 * 
	 * @param labelBean
	 * @return
	 */
	public static Integer saveAccount(TAccountBean accountBean) {
		return accountDAO.save(accountBean);
	}
	
	/**
	 * Saves an costCenter to the TCostCenter table.
	 * @param costCenterBean
	 * @return
	 */
	public static Integer saveCostCenter(TCostCenterBean costCenterBean) {
		return costCenterDAO.save(costCenterBean);
	}

}
