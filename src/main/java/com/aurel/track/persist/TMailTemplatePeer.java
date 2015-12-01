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

package com.aurel.track.persist;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.beans.TMailTemplateBean;
import com.aurel.track.dao.MailTemplateDAO;

/**
 * Templates for notification emails
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TMailTemplatePeer
	extends com.aurel.track.persist.BaseTMailTemplatePeer implements MailTemplateDAO {

	private static final Logger LOGGER = LogManager.getLogger(TMailTemplatePeer.class);
	
	private static Class[] deletePeerClasses = {
		TMailTemplateConfigPeer.class,
		TMailTemplateDefPeer.class,
		TMailTemplatePeer.class
	};

	private static String[] deleteFields = {
		TMailTemplateConfigPeer.MAILTEMPLATE,
		TMailTemplateDefPeer.MAILTEMPLATE,
		TMailTemplatePeer.OBJECTID
	};

	/**
	 * Loads a mail bean by primary key
	 * @param objectID
	 * @return
	 */
	@Override
	public TMailTemplateBean loadByPrimaryKey(Integer objectID) {
		TMailTemplate tMailTemplate = null;
		try {
			tMailTemplate = retrieveByPK(objectID);			
		} catch(Exception e) {
			LOGGER.warn("Loading the mail template by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} 
		if (tMailTemplate!=null) {
			return tMailTemplate.getBean();						
		}
		return null;		
	}
	
	/**
	 * Loads list of mail beans with the specified IDs
	 * @param mailTemplateIDList
	 * @return
	 */
	@Override
	public List<TMailTemplateBean> loadByIDs(List<Integer> mailTemplateIDList) {
		Criteria crit = new Criteria();
		crit.addIn(OBJECTID, mailTemplateIDList);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading mail templates failed with " + e.getMessage());
			return null;
		}
	}

	/**
	 * Loads a mail bean by type and locale
	 * @param type
	 * @param locale
	 * @return
	 */
	
	/**
	 * Gets all mail templates
	 * @return
	 */
	@Override
	public List<TMailTemplateBean> loadAll() {
		Criteria crit = new Criteria();		
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all mail templates failed with " + e.getMessage());
			return null;
		}	
	}

	/**
	 * Saves a mail template
	 * @param mailTemplateBean
	 * @return
	 */
	@Override
	public Integer save(TMailTemplateBean mailTemplateBean) {
		TMailTemplate tMailTemplate;		
		try {
			tMailTemplate = BaseTMailTemplate.createTMailTemplate(mailTemplateBean);
			tMailTemplate.save();
			Integer objectID=tMailTemplate.getObjectID();
			return objectID;
		} catch (Exception e) {
			LOGGER.error("Saving of mail template failed with " + e.getMessage());
			return null;
		}		
	}

	/**
	 * Deletes a screen by primary key
	 * Is deletable should return true before calling this method
	 * @param objectID
	 */
	@Override
	public void delete(Integer objectID) {
		ReflectionHelper.delete(deletePeerClasses, deleteFields, objectID);
	}
	
	
	private List<TMailTemplateBean> convertTorqueListToBeanList(List<TMailTemplate> torqueList) {
		List<TMailTemplateBean> beanList = new LinkedList<TMailTemplateBean>();
		if (torqueList!=null) {
			for (TMailTemplate tMailTemplate : torqueList) {
				beanList.add(tMailTemplate.getBean());
			}
		}		
		return beanList;
	}
	
}
