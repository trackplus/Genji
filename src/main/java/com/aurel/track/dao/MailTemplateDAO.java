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

package com.aurel.track.dao;

import java.util.List;

import com.aurel.track.beans.TMailTemplateBean;

/**
 * DAO for MailTemplates
 * @author Tamas Ruff
 *
 */
public interface MailTemplateDAO {
	
	/**
	 * Loads a mail bean by primary key
	 * @param objectID
	 * @return
	 */
	TMailTemplateBean loadByPrimaryKey(Integer objectID);
	
	/**
	 * Loads list of mail beans with the specified IDs
	 * @param mailTemplateIDList
	 * @return
	 */
	List<TMailTemplateBean> loadByIDs(List<Integer> mailTemplateIDList);
	
	/**
	 * Gets all mail templates
	 * @return
	 */
	List<TMailTemplateBean> loadAll();
	
	/**
	 * Saves a mail template
	 * @param mailTemplateBean
	 * @return
	 */
	Integer save(TMailTemplateBean mailTemplateBean);
	
	/**
	 * Deletes a mail template
	 * @param objectID
	 * @return
	 */
	void delete(Integer objectID);
}
