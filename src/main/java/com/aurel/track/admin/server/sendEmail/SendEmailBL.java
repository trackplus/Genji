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


package com.aurel.track.admin.server.sendEmail;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.util.emailHandling.JavaMailBean;

/**
 *
 */
public class SendEmailBL {
	private static final Logger LOGGER = LogManager.getLogger(SendEmailBL.class);
	
	/**
	 * Sends an e-mail to the list of recipients.
	 * @param from - the sender of the e-mail
	 * @param recipients - the list of recipients
	 * @param subject - the subject of the e-mail
	 * @param mailBody - the body of the e-mail in HTML format
	 * @return
	 */
	public static Boolean sendEmail(TPersonBean from, List<TPersonBean> recipients,
									List<TPersonBean> recipientsCC,List<TPersonBean> recipientsBCC,
			                        String subject, String mailBody){
		Boolean emailSent = null;
		JavaMailBean javaMailBean = JavaMailBean.getInstance();
		try {
			emailSent=javaMailBean.sendMail(recipients, subject, from, mailBody,false,null,recipientsCC,recipientsBCC);
		}
		catch (Exception e) {
			LOGGER.error("Sending the email failed with " +  e.getMessage(), e);
		}
		return emailSent;
	}

}
