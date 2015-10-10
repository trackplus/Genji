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

package com.trackplus.dao;

import java.util.List;

import com.trackplus.model.Tmailtemplate;

/**
 * DAO for MailTemplates
 * 
 * @author Tamas Ruff
 * 
 */
public interface MailTemplateDAO {

	/**
	 * Loads a mail bean by primary key
	 * 
	 * @param objectID
	 * @return
	 */
	Tmailtemplate loadByPrimaryKey(Integer objectID);

	/**
	 * Loads list of mail beans with the specified IDs
	 * 
	 * @param mailTemplateIDList
	 * @return
	 */
	public List<Tmailtemplate> loadByIDs(List<Integer> mailTemplateIDList);

	/**
	 * Loads a mail bean by type and locale
	 * 
	 * @param type
	 * @param locale
	 * @return
	 */
	// List<Tmailtemplate> loadByTypeAndLocale(Integer type, String locale);
	/**
	 * Gets all mail templates
	 * 
	 * @return
	 */
	List<Tmailtemplate> loadAll();

	/**
	 * Saves a mail template
	 * 
	 * @param mailTemplateBean
	 * @return
	 */
	Integer save(Tmailtemplate mailTemplateBean);

	/**
	 * Deletes a mail template
	 * 
	 * @param objectID
	 * @return
	 */
	void delete(Integer objectID);
}
