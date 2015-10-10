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
import java.util.Locale;

import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.category.filter.tree.design.RACIBean;
import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.errors.ErrorData;
/**
 * This defines the data access object (DAO) interface for attachment handling.
 * @author Adrian Bojani
 *
 */
public interface AttachmentDAO {
	
	/**
	 * Gets all attachments
	 * @return
	 */
	List<TAttachmentBean> loadAll();
	
	/**
	 * Obtain the attachments for the item 
	 * @param itemID
	 * @return list of TAttachmentBean
	 */
	List<TAttachmentBean> loadByWorkItemKey(Integer itemID);

	/**
	 * Count the attachments number
	 * @param workItemID
	 * @return
	 */
	int countByWorkItemID(Integer workItemID);
	
	/**
	 * Obtain the attachments for given attachment ids
	 * @param attachmentIDs
	 * @return
	 */
	List<TAttachmentBean> loadByAttachmentIDs(List<Integer> attachmentIDs);
	
	/**
	 * Obtain the attachments for given item ids
	 * @param workItemIDs
	 * @return
	 */
	List<TAttachmentBean> loadByWorkItemKeys(int[] workItemIDs);
	
	/**
	 * Load the attachment with given id
	 * @param attachmentID
	 * @return
	 */
	TAttachmentBean loadByID(Integer attachmentID);
	
	
	/**
	 * Load the attachment with given id; assuming it is an image also fill in the original dimensions.
	 * @param attachmentID
	 * @return
	 */
	TAttachmentBean loadByIDWithDimensions(Integer attachmentID);
	
	
	/**
	 * Gets the attachments filtered by filterSelectsTO and raciBean
	 * @param filterUpperTO
	 * @param raciBean
	 * @param personID
	 * @return
	 */
	List<TAttachmentBean> loadTreeFilterAttachments(FilterUpperTO filterUpperTO, RACIBean raciBean, Integer personID);
	
	/**
	 * Get the attachments for a TQL expression
	 * @param tqlExpression
	 * @param personBean
	 * @param locale
	 * @param errors
	 * @return
	 */
	List<TAttachmentBean> loadTQLFilterAttachments(String tqlExpression, TPersonBean personBean, Locale locale, List<ErrorData> errors);
	
	/**
	 * Save a attachment
	 * @param attach
	 * @return the id of the attachment
	 */
	Integer save(TAttachmentBean attach);
	
	/**
	 * Delete the attachment form DB
	 * @param attachmentID
	 */
	void delete(Integer attachmentID);
	
}
