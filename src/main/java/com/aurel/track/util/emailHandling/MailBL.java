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

package com.aurel.track.util.emailHandling;

import java.io.File;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.InternetAddress;

import com.aurel.track.dbase.HandleHome;
import com.aurel.track.prop.ApplicationBean;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;

/**
 *
 */
public class MailBL {
	protected static Logger LOGGER = LogManager.getLogger(MailBL.class);
	public static InternetAddress getCompleteAddressFromPersonBean(TPersonBean personBean) {
		InternetAddress inetAddressFrom = null;
		if (personBean==null) {
			LOGGER.error("Creating the InternetAddress failed with personBean is null");
		} else {
			String firstName = personBean.getFirstName();
			String lastName = personBean.getLastName();
			if (firstName==null) {
				firstName="";
			}
			if (lastName==null) {
				lastName="";
			}

			try {
				inetAddressFrom = new InternetAddress(personBean.getEmail(),
						(firstName + " " + lastName).trim(), ApplicationBean.getInstance().getSiteBean().getMailEncoding());
			} catch (Exception e) {
				LOGGER.error("Creating the InternetAddress from mail address " + personBean.getEmail() +
						" and person name " + personBean.getFirstName() + " " + personBean.getLastName() +
						" failed with " + e.getMessage(), e);
			}
		}
		return inetAddressFrom;
	}


	public static InternetAddress[] getAddressTo(List recipients, Boolean isPlain) {
		//ArrayList realRecipients = new ArrayList();
		ArrayList<TPersonBean> personBeans = new ArrayList<TPersonBean>();
		if(isPlain==null){
			personBeans.addAll(recipients);
		}else{
			for (int i = 0; i < recipients.size(); i++ ) {
				TPersonBean personBean = (TPersonBean)recipients.get(i);
				if (personBean.getEmail()!=null) {
					if (isPlain) {
						if (personBean.isPreferredEmailTypePlain()) {
							personBeans.add(personBean);
							//realRecipients.add(personBean.getEmail());
							LOGGER.debug("Plain recipient added " + personBean.getEmail());
						}
					}else {
						if (!personBean.isPreferredEmailTypePlain()) {
							personBeans.add(personBean);
							//realRecipients.add(personBean.getEmail());
							LOGGER.debug("HTML recipient added " + personBean.getEmail());
						}
					}
				}
			}
		}
		InternetAddress[] addressTo = null;
		if (!personBeans.isEmpty()) {
			addressTo = new InternetAddress[personBeans.size()];
			for (int i = 0; i < personBeans.size(); i++ ) {
				addressTo[i] = getCompleteAddressFromPersonBean(personBeans.get(i)); //new InternetAddress((String) realRecipients.get(i));
			}
		}
		return addressTo;
	}


	public static void setTrustKeyStore(String server) {

		String fileNameKey=server+".ks";
		LOGGER.debug("Trying to set file:"+fileNameKey+" to java trust store...");
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		try{
			File dirKeyStore=new File(HandleHome.getTrackplus_Home()+ File.separator+"keystore");
			if(dirKeyStore.exists()&&dirKeyStore.isDirectory()){
				File keyFile=new File(dirKeyStore,fileNameKey);
				if(keyFile.exists()){
					System.setProperty("javax.net.ssl.trustStore", MailReader.PATH_TO_KEY_STORE+fileNameKey);
					LOGGER.debug("File: "+keyFile.getAbsolutePath()+" set as java trust store");
				}
			}
		}catch(Exception ex){
			//ignore
		}
	}

}
