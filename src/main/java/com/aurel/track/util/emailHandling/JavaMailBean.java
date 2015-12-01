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


package com.aurel.track.util.emailHandling;

import java.io.Serializable;
import java.util.List;

import javax.mail.internet.InternetAddress;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.util.LabelValueBean;

/**
 * This class handles the sending of e-mails from the
 * Trackplus system. It requires connection to an SMTP server
 * and possibly to a POP3 server in case of authentication mode
 * "POP3 before SMTP".
 * @author Alexei Khatskevich
 * @author Joerg Friedrich
 * @author Tamas Ruff
 *
 */
public class JavaMailBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 400L;

	protected static Logger LOGGER = LogManager.getLogger(JavaMailBean.class);
	
	private static JavaMailBean javaMailBean;

	private JavaMailBean(){
	}

	/**
	 * get a singleton instance
	 * @return
	 */
	public static JavaMailBean getInstance() {
		if (javaMailBean == null) {
			javaMailBean = new JavaMailBean();
		}
		return javaMailBean;
	}

	/**
	 * Send a plain or HTML e-mail to a single person
	 * @param recipient - the recipient of this message
	 * @param subject - the message subject
	 * @param addressFrom - the sender of the message
	 * @param messageBody - the message body
	 * @param isPlain - is the message a plain text or HTML message
	 */
	public synchronized void sendMailInThread(TPersonBean recipient,String subject,TPersonBean addressFrom,String messageBody, boolean isPlain) {
		InternetAddress internetAddressFrom=MailBL.getCompleteAddressFromPersonBean(addressFrom);
		InternetAddress internetAddressesTo=MailBL.getCompleteAddressFromPersonBean(recipient);
		MailSender mailSender=new MailSender(internetAddressFrom,internetAddressesTo,subject,messageBody,isPlain);
		mailSender.setNotIncludeImages(true);
		mailSender.start();
	}
	
	/**
	 * Send a plain or HTML e-mail to a single person
	 * @param recipient - the recipient of this message
	 * @param subject - the message subject
	 * @param addressFrom - the sender of the message
	 * @param messageBody - the message body
	 * @param isPlain - is the message a plain text or HTML message
	 */
	public synchronized void sendMailInThread(TPersonBean recipient,String subject,TPersonBean addressFrom, TPersonBean cc, TPersonBean replayToPerson, String messageBody, boolean isPlain) {

		InternetAddress internetAddressFrom=MailBL.getCompleteAddressFromPersonBean(addressFrom);
		InternetAddress internetAddressesTo=MailBL.getCompleteAddressFromPersonBean(recipient);


		InternetAddress[] internetAddressesBCC=null;


		InternetAddress[] internetAddressesCC=null;
		if (cc!=null) {
			InternetAddress internetAddressCc=MailBL.getCompleteAddressFromPersonBean(cc);
			internetAddressesCC=new InternetAddress[]{internetAddressCc};
		}

		InternetAddress[] internetAddressesReplayTo=null;
		if (replayToPerson!=null) {
			InternetAddress internetAddressReplayTo=MailBL.getCompleteAddressFromPersonBean(replayToPerson);
			internetAddressesReplayTo=new InternetAddress[]{internetAddressReplayTo};
		}

		MailSender mailSender =new MailSender(internetAddressFrom, new InternetAddress[] {internetAddressesTo},
				subject, messageBody, isPlain,null,internetAddressesCC,internetAddressesBCC,internetAddressesReplayTo);

		mailSender.setNotIncludeImages(true);
		mailSender.start();
	}
	
	public boolean sendMail(TPersonBean recipient,String subject,TPersonBean addressFrom,String messageBody, boolean isPlain) {
		return sendMail(recipient,subject,addressFrom,messageBody,isPlain,null);
	}
	
	public boolean sendMail(TPersonBean recipient,String subject,TPersonBean addressFrom,String messageBody, boolean isPlain,List<LabelValueBean> attachments) {
		InternetAddress internetAddressFrom=MailBL.getCompleteAddressFromPersonBean(addressFrom);
		InternetAddress internetAddressesTo=MailBL.getCompleteAddressFromPersonBean(recipient);
		MailSender mailSender=new MailSender(internetAddressFrom,internetAddressesTo,subject,messageBody,isPlain,attachments);
		mailSender.setNotIncludeImages(true);
		return mailSender.send();
	}

	/**
	 * Send either plain text or HTML e-mail to a list of
	 * recipients. Which e-mail type to send is taken from the
	 * recipients profile. This is with attachments and callback.
	 * @param recipients - the list of intended recipients
	 * @param subject - the message subject
	 * @param addressFrom - the sender of the e-mail
	 * @param messageBody - the message body
	 * @param attachments - a list of attachments
	 */
	public synchronized void sendMailInThread(List<TPersonBean> recipients,String subject,TPersonBean addressFrom,String messageBody,boolean  isPlain,List<LabelValueBean> attachments) {
		InternetAddress internetAddressFrom=MailBL.getCompleteAddressFromPersonBean(addressFrom);
		InternetAddress[] internetAddressesTo=MailBL.getAddressTo(recipients,null);
		MailSender mailSender=new MailSender(internetAddressFrom,internetAddressesTo,subject,messageBody,isPlain,attachments );
		mailSender.setNotIncludeImages(true);
		mailSender.start();
	}
	public boolean sendMail(List<TPersonBean> recipients,String subject,TPersonBean addressFrom,String messageBody,boolean  isPlain,List<LabelValueBean> attachments,List<TPersonBean> cc,List<TPersonBean> bcc) {
		InternetAddress internetAddressFrom=MailBL.getCompleteAddressFromPersonBean(addressFrom);
		InternetAddress[] internetAddressesTo=MailBL.getAddressTo(recipients,null);
		InternetAddress[] internetAddressesCC=cc==null?null:MailBL.getAddressTo(cc,null);
		InternetAddress[] internetAddressesBCC=bcc==null?null:MailBL.getAddressTo(bcc,null);
		MailSender mailSender=new MailSender(internetAddressFrom,internetAddressesTo,subject,messageBody,isPlain,attachments,internetAddressesCC, internetAddressesBCC,null);
		mailSender.setNotIncludeImages(true);
		return mailSender.send();
	}




}

