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

import com.aurel.track.beans.TMailTemplateDefBean;
import com.aurel.track.dao.MailTemplateDefDAO;
import com.aurel.track.fieldType.constants.BooleanFields;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import java.util.LinkedList;
import java.util.List;

/**
 * Locale and e-mail type dependent template definitions for notification emails
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class TMailTemplateDefPeer
    extends com.aurel.track.persist.BaseTMailTemplateDefPeer implements MailTemplateDefDAO{
	private static final Logger LOGGER = LogManager.getLogger(TMailTemplateDefPeer.class);

	/**
	 * Loads a mail bean by primary key
	 * @param objectID
	 * @return
	 */
	@Override
	public TMailTemplateDefBean loadByPrimaryKey(Integer objectID) {
		TMailTemplateDef tMailTemplateDef = null;
		try {
			tMailTemplateDef = retrieveByPK(objectID);
		} catch(Exception e) {
			LOGGER.warn("Loading the mail template def by primary key " + objectID + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		if (tMailTemplateDef!=null) {
			return tMailTemplateDef.getBean();
		}
		return null;
	}

	@Override
	public List<TMailTemplateDefBean> loadByTemplate(Integer templateID) {
		Criteria crit = new Criteria();
		crit.add(MAILTEMPLATE , templateID);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading mail templates failed with " + e.getMessage());
			return null;
		}
	}
	@Override
	public List<TMailTemplateDefBean> loadByTemplateTypeAndLocale(Integer templateID,boolean plain,String locale){
		Criteria crit = new Criteria();
		crit.add(MAILTEMPLATE , templateID);
		crit.add(PLAINEMAIL, BooleanFields.fromBooleanToString(plain));
		crit.add(THELOCALE,locale);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading mail templates failed with " + e.getMessage());
			return null;
		}
	}

	/**
	 * Gets all mail templates
	 * @return
	 */
	@Override
	public List<TMailTemplateDefBean> loadAll() {
		Criteria crit = new Criteria();
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading all mail templates def failed with " + e.getMessage());
			return null;
		}
	}
	@Override
	public List<TMailTemplateDefBean> loadByIDs(List<Integer> idList){
		Criteria crit = new Criteria();
		crit.addIn(OBJECTID, idList);
		try {
			return convertTorqueListToBeanList(doSelect(crit));
		} catch (TorqueException e) {
			LOGGER.error("Loading mail template defs failed with " + e.getMessage());
			return null;
		}
	}

	/**
	 * Saves a mail template
	 * @param mailTemplateDefBean
	 * @return
	 */
	@Override
	public Integer save(TMailTemplateDefBean mailTemplateDefBean) {
		TMailTemplateDef tMailTemplateDef;
		try {
			tMailTemplateDef = BaseTMailTemplateDef.createTMailTemplateDef(mailTemplateDefBean);
			tMailTemplateDef.save();
			return tMailTemplateDef.getObjectID();
		} catch (Exception e) {
			LOGGER.error("Saving of mail template def failed with " + e.getMessage());
			return null;
		}
	}

	/**
	 * Deletes a mail template
	 * @param objectID
	 * @return
	 */
	@Override
	public void delete(Integer objectID) {
		Criteria crit = new Criteria();
		crit.add(OBJECTID, objectID);
		try {
			doDelete(crit);
		} catch (TorqueException e) {
			LOGGER.error("Deleting the mail template def " + objectID +  " failed with " + e.getMessage());
		}
	}

	private List<TMailTemplateDefBean> convertTorqueListToBeanList(List<TMailTemplateDef> torqueList) {
		List<TMailTemplateDefBean> beanList = new LinkedList<TMailTemplateDefBean>();
		if (torqueList!=null) {
			for (TMailTemplateDef tMailTemplateDef : torqueList) {
				beanList.add(tMailTemplateDef.getBean());
			}
		}
		return beanList;
	}
}
