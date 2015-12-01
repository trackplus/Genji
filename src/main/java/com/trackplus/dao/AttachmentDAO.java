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

package com.trackplus.dao;

import java.util.List;

import com.trackplus.model.Tattachment;

/**
 * This defines the data access object (DAO) interface for attachment handling.
 * 
 * @author Adrian Bojani
 * 
 */
public interface AttachmentDAO {

	/**
	 * Gets all attachments
	 * 
	 * @return
	 */
	List<Tattachment> loadAll();

	/**
	 * Obtain the attachments for the item
	 * 
	 * @param itemID
	 * @return list of Tlattachment
	 */
	List<Tattachment> loadByWorkItemKey(Integer itemID);

	/**
	 * Obtain the attachments for given ids
	 * 
	 * @param workItemIDs
	 * @return
	 */
	List<Tattachment> loadByWorkItemKeys(int[] workItemIDs);

	/**
	 * Load the attachment with given id
	 * 
	 * @param attachmentID
	 * @return
	 */
	Tattachment loadByID(Integer attachmentID);

	/**
	 * Save a attachment
	 * 
	 * @param attach
	 * @return the id of the attachment
	 */
	Integer save(Tattachment attach);

	/**
	 * Delete the attachment form DB
	 * 
	 * @param attachmentID
	 */
	void delete(Integer attachmentID);

}
