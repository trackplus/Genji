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

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletContext;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.mail.ByteArrayDataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.plugin.ImageAction;
import com.aurel.track.util.LabelValueBean;

/**
 * Helper class to construct an e-mail
 */
public class MailBuilder {
	protected static Logger LOGGER = LogManager.getLogger(MailBuilder.class);
	protected static String	XMAILER	= "X-Mailer";

	private String	xmailer	= "Trackplus";
	private String mailEncoding;
	private Session session;
	private boolean notIncludeImages=false;
	private ServletContext servletContext;

	public MailBuilder(String xmailer, String mailEncoding, Session session, boolean notIncludeImages, ServletContext servletContext) {
		this.xmailer = xmailer;
		this.mailEncoding = mailEncoding;
		this.session = session;
		this.notIncludeImages = notIncludeImages;
		this.servletContext = servletContext;
	}

	/**
	 * Makes a HTML message with one recipient
	 * @return
	 * @throws Exception
	 */
	public MimeMessage makeHTMLMessage(InternetAddress internetAddressFrom,InternetAddress[] internetAddressesTo,
			String subject,String htmlBody,
			List<LabelValueBean> attachments) throws Exception {

		LOGGER.debug("Constructing the HTML MimeMessage with one recipient");
		MimeMessage msg = prepareHTMLMimeMessage(internetAddressFrom,subject,htmlBody,attachments);

		msg.setRecipients(RecipientType.TO,internetAddressesTo);
		msg.saveChanges();
		return msg;
	}

	private MimeMessage prepareHTMLMimeMessage(InternetAddress internetAddressFrom,String subject,String htmlBody,List<LabelValueBean> attachments) throws Exception {
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(internetAddressFrom);
		msg.setHeader(XMAILER, xmailer);
		msg.setSubject(subject.trim(), mailEncoding );
		msg.setSentDate(new Date());

		MimeMultipart mimeMultipart = new MimeMultipart("related");

		BodyPart messageBodyPart = new MimeBodyPart();
		//Euro sign finally, shown correctly
		messageBodyPart.setContent(htmlBody, "text/html;charset="+mailEncoding);
		mimeMultipart.addBodyPart(messageBodyPart);

		if(attachments!=null && !attachments.isEmpty()){
			LOGGER.debug("Use attachments: "+attachments.size());
			includeAttachments(mimeMultipart,attachments);
		}

		msg.setContent(mimeMultipart);

		return msg;
	}

	/**
	 * Makes a plain message for one recipient
	 * @param subject
	 * @return
	 * @throws Exception
	 */
	public MimeMessage makePlainMessage(InternetAddress internetAddressFrom,InternetAddress[] internetAddressesTo,
			String subject,String plainBody,
			List<LabelValueBean> attachments) throws Exception {

		LOGGER.debug("Constructing the plain MimeMessage with one recipient");
		MimeMessage msg = preparePlainMimeMessage(internetAddressFrom,subject, plainBody,attachments);
		msg.setRecipients(RecipientType.TO, internetAddressesTo);
		msg.saveChanges();
		return msg;
	}

	/**
	 * Prepares a plain MimeMessage: the MimeMessage.RecipientType.TO is not yet set
	 * @return
	 * @throws Exception
	 */
	private MimeMessage preparePlainMimeMessage(InternetAddress internetAddressFrom,String subject,String plainBody,List<LabelValueBean> attachments) throws Exception {
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(internetAddressFrom);
		msg.setHeader(XMAILER, xmailer);
		msg.setSubject(subject.trim(), mailEncoding);
		msg.setSentDate(new Date());
		if(attachments==null||attachments.isEmpty()){
			msg.setText(plainBody, mailEncoding);
		}else{
			MimeMultipart mimeMultipart = new MimeMultipart();

			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setText(plainBody, mailEncoding);
			mimeMultipart.addBodyPart(textBodyPart);

			if(attachments!=null){
				includeAttachments(mimeMultipart,attachments);
			}

			msg.setContent(mimeMultipart);
		}
		return msg;
	}


	private void includeAttachments(MimeMultipart mimeMultipart,List<LabelValueBean> attachments) throws MessagingException {
		for (int i = 0; i < attachments.size(); i++) {
			LabelValueBean lvb=attachments.get(i);
			File f=new File(lvb.getValue());
			if(f!=null&&f.exists()){
				LOGGER.debug("Use attachment file:"+f.getAbsolutePath());
				MimeBodyPart mbpFile = new MimeBodyPart();
				FileDataSource fds = new FileDataSource(f);
				mbpFile.setDataHandler(new DataHandler(fds));
				mbpFile.setFileName(lvb.getLabel());
				mimeMultipart.addBodyPart(mbpFile);
			}else{
				LOGGER.debug("Attachment file:\""+lvb.getValue()+"\" not exits!");
			}
		}
	}

	private void includeImageLogo(MimeMultipart mimeMultipart) {
		BodyPart messageBodyPart;ArrayList<String> imageFiles = new ArrayList<String>();
		imageFiles.add("tracklogo.gif");
		// more images can be added here
		ArrayList<String> cids = new ArrayList<String>();
		cids.add("logo");
		// for each image there should be a cid here

		URL imageURL = null;
		for (int i = 0; i < imageFiles.size(); ++i) {
			try {
				DataSource ds = null;
				messageBodyPart = new MimeBodyPart();
				InputStream in=null;
				in=ImageAction.class.getClassLoader().getResourceAsStream(imageFiles.get(i));
				int length = imageFiles.get(i).length();
				String type = imageFiles.get(i).substring(length-4,length-1);
				if (in != null) {
					ds = new ByteArrayDataSource(in, "image/"+type);
					System.err.println(type);
				} else {
					String theResource = "/WEB-INF/classes/resources/MailTemplates/"+imageFiles.get(i);
					imageURL = servletContext.getResource(theResource);
					ds = new URLDataSource( imageURL );
				}
				messageBodyPart.setDataHandler(new DataHandler(ds));
				messageBodyPart.setHeader("Content-ID", cids.get(i));
				messageBodyPart.setDisposition("inline");
				// add it
				mimeMultipart.addBodyPart(messageBodyPart);
			}
			catch (Exception e) {
				LOGGER.error(ExceptionUtils.getStackTrace(e));
				// what shall we do here?
			}
		}
	}

}
