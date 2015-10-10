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

package com.trackplus.persist;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.trackplus.dao.SiteDAO;
import com.trackplus.model.Tsite;

/**
 */
public class TSitePeer extends BasePeer<Tsite> implements SiteDAO {

    private static final long serialVersionUID = -130849904047755361L;
    private static final Logger LOGGER = LogManager.getLogger(TSitePeer.class);
        
    /**
     * Load the one and only TSITE object from the database
     * @return
     */
    public Tsite load() { 
		EntityManager em = TpEm.getEntityManager();
		List<Tsite> tsites = em.createQuery("SELECT s FROM Tsite s").getResultList();
		em.close();
		if (tsites != null && tsites.size() > 0) {
			return tsites.get(0);
		} else {
			return null;
		}
    }

	
	/**
	 * loads, updates and saves the Tsite in a synchronized method
	 * @param fieldValues
	 */
	synchronized public void loadAndSaveSynchronized(Map<Integer, Object> fieldValues) {
		Tsite site = load();
		
		if (site==null || fieldValues==null || fieldValues.isEmpty()) {
			return;
		}
		Iterator<Integer> iterator = fieldValues.keySet().iterator();
		while (iterator.hasNext()) {
			Integer columnID = iterator.next();
			Object value = fieldValues.get(columnID);
			switch(columnID) {
			case Tsite.COLUMNIDENTIFIERS.TRACKVERSION:
				site.setTrackversion((String)value);
				break;
			case Tsite.COLUMNIDENTIFIERS.DBVERSION:
				site.setDbVersion((String)value);
				break;
			case Tsite.COLUMNIDENTIFIERS.LICENSEKEY:
				site.setLicenseKey((String)value);
				break;
			case Tsite.COLUMNIDENTIFIERS.EXPDATE:
				site.setExpDate((Date)value);
				break;
			case Tsite.COLUMNIDENTIFIERS.NUMBEROFUSERS:
				site.setNumberOfUsers((Integer)value);
				break;
			case Tsite.COLUMNIDENTIFIERS.TRACKEMAIL:
				site.setTrackEmail((String)value);
				break;
			case Tsite.COLUMNIDENTIFIERS.SMTPSERVERNAME:
				site.setSmtpServerName((String)value);
				break;
			case Tsite.COLUMNIDENTIFIERS.SMTPPORT:
				site.setSmtpPort((Integer)value);
				break;
			case Tsite.COLUMNIDENTIFIERS.MAILENCODING:
				site.setMailEncoding((String)value);
				break;
			case Tsite.COLUMNIDENTIFIERS.SMTPUSER:
				site.setSmtpUser((String)value);
				break;
			case Tsite.COLUMNIDENTIFIERS.SMTPPASSWORD:
				site.setSmtpPassword((String)value);
				break;
			case Tsite.COLUMNIDENTIFIERS.POPSERVERNAME:
				site.setMailReceivingServerName((String)value);
				break;				
			case Tsite.COLUMNIDENTIFIERS.POPPORT:
				site.setMailReceivingPort((Integer)value);
				break;
			case Tsite.COLUMNIDENTIFIERS.POPUSER:
				site.setMailReceivingUser((String)value);
				break;
			case Tsite.COLUMNIDENTIFIERS.POPPASSWORD:
				site.setMailReceivingPassword((String)value);
				break;
			case Tsite.COLUMNIDENTIFIERS.ALLOWEDEMAILPATTERN:
				site.setAllowedEmailPattern((String)value);
				break;
			case Tsite.COLUMNIDENTIFIERS.ISLDAPON:
				site.setIsLDAPOn((String)value);
				break;
				
			case Tsite.COLUMNIDENTIFIERS.LDAPSERVERURL:
				site.setLdapServerURL((String)value);
				break;
			case Tsite.COLUMNIDENTIFIERS.LDAPATTRIBUTELOGINNAME:
				site.setLdapAttributeLoginName((String)value);
				break;

			case Tsite.COLUMNIDENTIFIERS.DESCRIPTIONLENGTH:
				site.setDescriptionLength((Integer)value);
				break;
				
			case Tsite.COLUMNIDENTIFIERS.HISTORYENTITY:
				site.setHistoryEntity((String)value);
				break;
			case Tsite.COLUMNIDENTIFIERS.HISTORYMIGRATIONID:
				site.setHistoryMigrationID((String)value);
				break;
			case Tsite.COLUMNIDENTIFIERS.USELUCENE:
				site.setUseLucene((String)value);
				break;
			case Tsite.COLUMNIDENTIFIERS.INDEXATTACHMENTS:
				site.setIndexAttachments((String)value);
				break;
			case Tsite.COLUMNIDENTIFIERS.REINDEXONSTARTUP:
				site.setReindexOnStartup((String)value);
				break;
			case Tsite.COLUMNIDENTIFIERS.ANALYZER:
				site.setAnalyzer((String)value);
				break;			
			case Tsite.COLUMNIDENTIFIERS.LASTSERVERURL:
				site.setLastServerURL((String)value);
				break;	
			case Tsite.COLUMNIDENTIFIERS.LASTBASEURL:
				site.setLastBaseURL((String)value);
				break;
			case Tsite.COLUMNIDENTIFIERS.DERBY_BACKUP:
				site.setDerbyBackup((String)value);
				break;
			case Tsite.COLUMNIDENTIFIERS.INSTDATE:
				site.setDerbyBackup((String)value);

			}
		}
		;
	}	
	
	/**
	 * Sets the SMTPUser and SMTPPassword fields to null
	 *
	 */
	public void clearSMTPPassword() {
		Tsite site = load();
		site.setSmtpPassword(null);
		EntityManager em = TpEm.getEntityManager();
		em.persist(site);
	}
	
}
