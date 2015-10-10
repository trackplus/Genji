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

import com.trackplus.model.Tmailtemplatedef;

public interface MailTemplateDefDAO {
	/**
	 * Loads a mail bean by primary key
	 * 
	 * @param objectID
	 * @return
	 */
	Tmailtemplatedef loadByPrimaryKey(Integer objectID);

	/**
	 * Gets all mail template defs
	 * 
	 * @return
	 */
	List<Tmailtemplatedef> loadAll();

	List<Tmailtemplatedef> loadByIDs(List<Integer> idList);

	/**
	 * Gets mail templates defs
	 * 
	 * @return
	 */
	List<Tmailtemplatedef> loadByTemplate(Integer templateID);

	List<Tmailtemplatedef> loadByTemplateTypeAndLocale(Integer templateID,
			boolean plain, String locale);

	/**
	 * Saves a mail template
	 * 
	 * @param mailTemplateBean
	 * @return
	 */
	Integer save(Tmailtemplatedef mailTemplateBean);

	/**
	 * Deletes a mail template
	 * 
	 * @param objectID
	 * @return
	 */
	void delete(Integer objectID);
}
