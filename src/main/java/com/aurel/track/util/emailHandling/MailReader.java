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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;
import javax.mail.search.FlagTerm;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.dbase.HandleHome;
import com.aurel.track.util.LogThrottle;

/**
 *
 * This class provides methods for receiving e-mails sent to the Genji system.
 * The sender is checked if it is known in the Genji database. In case it is,
 * the message is queued to a default project in the Genji system, and the
 * sender receives a notification including the reference number for his
 * submission. This class can handle multipart messages, including attachments.
 * The subject of the e-mail will become the synopsis of the issue.
 *
 * In case the e-mail contains a reference number in the subject, the body text
 * will be appended to the issue as a comment in case this issue is known to the
 * system and the sender has access rights to this issue.
 *
 * There can be a separate e-mail account for each project. If this is not
 * configured, the default Genji system account is being used.
 *
 * Ideas and code in this class are taken from the Java Codebook by
 * Addison-Wesley.
 *
 *
 */
public class MailReader {

	public static String PATH_TO_KEY_STORE = HandleHome.getTrackplus_Home() + File.separator + "keystore" + File.separator;

	/**
	 * Retrieves all e-mails from the given e-mail account, newest first
	 * 
	 * @param server
	 *            name or IP address of the POP3 mail server
	 * @param user
	 *            user name for accessing the POP3 server
	 * @param password
	 *            password for accessing the POP3 server
	 */

	protected static Logger LOGGER = LogManager.getLogger(MailReader.class);
	protected static ArrayList messageParts = null;

	protected static Folder folder = null;

	protected static Store store = null;
	protected static SyncStore.ConnectionPoint connPoint = null;

	public synchronized static Message[] readAll(IncomingMailSettings ims) {
		return readAll(ims.getProtocol(), ims.getServerName(), ims.getSecurityConnection(), ims.getPort(), ims.getUser(), ims.getPassword());
	}

	public synchronized static Message[] readAll(String protocol, String server, int securityConnection, int port, String user, String password) {
		return readAll(protocol, server, securityConnection, port, user, password, false);
	}

	public synchronized static Message[] readAll(String protocol, String server, int securityConnection, int port, String user, String password,
			boolean onlyUnreadMessages) {
		Message[] msgs = null;
		try {
			Properties props = System.getProperties();
			updateSecurityProps(protocol, server, securityConnection, props);
			Session session = Session.getDefaultInstance(props, null);

			if (LOGGER.isDebugEnabled()) {
				session.setDebug(true);
			} else {
				session.setDebug(false);
			}

			// instantiate POP3-store and connect to server
			store = session.getStore(protocol);
			boolean connected = SyncStore.connect(store, protocol, server, port, user, password);
			if (!connected) {
				return msgs;
			}
			connPoint = SyncStore.getConnPoint(store, protocol, server, port, user, password);
			store = connPoint.getStore();

			// access default folder
			folder = store.getDefaultFolder();

			// can't find default folder
			if (folder == null) {
				throw new Exception("No default folder");
			}

			// messages are always in folder INBOX
			folder = folder.getFolder("INBOX");

			// can't find INBOX
			if (folder == null) {
				throw new Exception("No POP3 INBOX");
			}

			// open folder
			folder.open(Folder.READ_WRITE);

			// retrieve messages
			if (onlyUnreadMessages) {
				FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
				msgs = folder.search(ft);
			} else {
				msgs = folder.getMessages();
			}
		} catch (Exception ex) {
        	if (LogThrottle.isReady("MailReader1",240)) {
        		LOGGER.error(ExceptionUtils.getStackTrace(ex));
        	}
		}
		return msgs;
	}

	private static void updateSecurityProps(String protocol, String server, int securityConnection, Properties props) {
		props.remove("mail.pop3.socketFactory.class");
		props.remove("mail.imap.socketFactory.class");
		String oldTrustStore = (String) props.remove("javax.net.ssl.trustStore");
		switch (securityConnection) {
		case TSiteBean.SECURITY_CONNECTIONS_MODES.TLS_IF_AVAILABLE:
			if ("pop3".equalsIgnoreCase(protocol)) {
				props.put("mail.pop3.starttls.enable", "true");
				props.put("mail.pop3.ssl.protocols", "SSLv3 TLSv1");
			}
			if ("imap".equalsIgnoreCase(protocol)) {
				props.put("mail.imap.starttls.enable", "true");
				props.put("mail.imap.ssl.protocols", "SSLv3 TLSv1");
			}
			break;
		case TSiteBean.SECURITY_CONNECTIONS_MODES.TLS:
			if ("pop3".equalsIgnoreCase(protocol)) {
				props.put("mail.pop3.starttls.enable", "true");
				props.put("mail.pop3.starttls.required", "true");
				props.put("mail.pop3.ssl.protocols", "SSLv3 TLSv1");
			}
			if ("imap".equalsIgnoreCase(protocol)) {
				props.put("mail.imap.starttls.enable", "true");
				props.put("mail.imap.starttls.required", "true");
				props.put("mail.imap.ssl.protocols", "SSLv3 TLSv1");
			}
			MailBL.setTrustKeyStore(server);
			break;
		case TSiteBean.SECURITY_CONNECTIONS_MODES.SSL: {
			LOGGER.debug("Update security   to SSL...");
			LOGGER.debug("oldTrustStore=" + oldTrustStore);
			final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			MailBL.setTrustKeyStore(server);
			if ("pop3".equalsIgnoreCase(protocol)) {
				LOGGER.debug("Use SSL pop3 protocol");
				props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
			}
			if ("imap".equalsIgnoreCase(protocol)) {
				LOGGER.debug("Use SSL imap protocol");
				props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
			}
			break;
		}
		default:
			break;
		}
	}

	synchronized static Folder getFolder() {
		return folder;
	}

	public synchronized static String verifyMailSetting(String protocol, String server, int securityConnection, int port, String user, String password) {
		LOGGER.debug(" Verify mail setting: " + protocol + ":" + server + ":" + port + "," + user + "...");
		try {
			// create session instance
			Properties props = System.getProperties();
			updateSecurityProps(protocol, server, securityConnection, props);
			Session session = Session.getDefaultInstance(props, null);

			// instantiate POP3-store and connect to server
			store = session.getStore(protocol);
			store.connect(server, port, user, password);
			store.close();
		} catch (Exception ex) {
			LOGGER.warn("Incoming e-mail settings don't work: " + protocol + ":" + server + ":" + port + ", user " + user + " - " + ex.getMessage());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.error(ExceptionUtils.getStackTrace(ex));
			}
			return ex.getLocalizedMessage() == null ? ex.getClass().getName() : ex.getLocalizedMessage();
		}
		LOGGER.debug("Mail setting: " + protocol + ":" + server + ":" + port + "," + user + " valid!");
		return null;
	}

	/**
	 * Close the folder, delete all messages marked DELETED from INBOX, then
	 * close the store.
	 *
	 */
	public synchronized static void closeSession(boolean keepMessages) {
		try {
			if (folder != null && folder.isOpen()) {
				if (keepMessages) {
					LOGGER.debug("Closing folder now, keep messages on server!");
				} else {
					LOGGER.debug("Closing folder now, deleting messages.");
				}
				folder.close(true); // expunge messages marked DELETED
				// folder.close(false); // Testing only
			}
			if (connPoint.getStore() != null) {
				connPoint.close();
				LOGGER.debug("Closed  connection.");
			}
		} catch (Exception ex2) {
			LOGGER.error(ExceptionUtils.getStackTrace(ex2));
		}

	}

	/**
	 * Retrieves the senders e-mail address from this message.
	 *
	 * @param message
	 *            the message to be processed
	 * @return the senders e-mail address
	 */
	public static String getSenderEmailAddress(Message message) {
		String from = null;
		try {
			if (message.getFrom() != null && message.getFrom().length > 0) {
				InternetAddress fromAddress = (InternetAddress) message.getFrom()[0];
				if (null != fromAddress) {
					from = fromAddress.getAddress();
				}
			} else {
				LOGGER.warn("No from sender address in message!");
			}
		} catch (Exception e) {
			// can't do much here
			LOGGER.warn("Can't get sender email address", e);
		}
		return from;
	}

	/**
	 * Retrieves the sender as a TPersonBean with first and last name
	 *
	 * @param message
	 *            the message to be processed
	 * @return the sender
	 */
	public static TPersonBean getSender(Message message) {
		String from = null;
		TPersonBean sender = null;
		String firstName = "";
		String lastName = "";
		try {
			if (message.getFrom() != null && message.getFrom().length > 0) {
				InternetAddress fromAddress = (InternetAddress) message.getFrom()[0];
				if (null != fromAddress) {
					from = fromAddress.getAddress();
					String name = fromAddress.getPersonal();
					if (name != null && !"".equals(name)) {
						String nameparts[] = name.split("\\s+");
						for (int i = 0; i < nameparts.length - 1; ++i) {
							firstName = firstName + " " + nameparts[i];
						}
						firstName = firstName.trim();
						lastName = nameparts[nameparts.length - 1];
					}
					sender = new TPersonBean();
					sender.setEmail(from);
					sender.setFirstName(firstName);
					sender.setLastName(lastName);
				}
			} else {
				LOGGER.warn("No from sender address in message!");
			}
		} catch (Exception e) {
			// can't do much here
			LOGGER.warn("Can't get sender email address", e);
		}
		return sender;
	}

	/**
	 * Retrieves the senders name from this message if possible.
	 *
	 * @param message
	 *            the message to be processed
	 * @return the senders e-mail address
	 */
	public static String getSenderName(Message message) {
		String from = null;
		try {
			InternetAddress fromAddress = (InternetAddress) message.getFrom()[0];
			if (null != fromAddress) {
				if (null != fromAddress.getPersonal()) {
					// if there is a name, return it
					from = fromAddress.getPersonal();
				}
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			// can't do much here
		}
		return from;
	}

	/**
	 * Retrieves the subject line of this message
	 *
	 * @param message
	 *            the message to be processed
	 * @return the subject line of this message
	 */
	public static String getSubject(Message message) {
		String subject = null;
		try {
			subject = message.getSubject();
		} catch (Exception e) {
			// can't do much here
		}
		return subject;
	}

	/**
	 * Process the message
	 * 
	 * @param message
	 *            email message
	 * @param attachments
	 *            The list where to add the attachment
	 * @return the body/description of the message
	 */
	public static StringBuffer processMessage(Message message, List<EmailAttachment> attachments, boolean ignoreAttachments) {
		Part messagePart = message;
		return handlePart(messagePart, attachments, ignoreAttachments);
	}

	public static StringBuffer handlePart(Part messagePart, List<EmailAttachment> attachments, boolean ignoreAttachments) {
		StringBuffer body = new StringBuffer();
		try {
			Object content = messagePart.getContent();
			// Retrieve content type
			String contentType = messagePart.getContentType();
			LOGGER.debug("Content type is " + contentType);
			// Check if this is a multipart message
			if (content instanceof Multipart) {
				LOGGER.debug("(Multipart-Email)");
				String s = handleMultipart(messagePart, attachments, ignoreAttachments);
				if (s != null) {
					body.append(s);
				}
			} else {
				// process regular message
				String s = handleSimplePart(messagePart, attachments, ignoreAttachments);
				if (s != null) {
					body.append(s);
				}
			}
		} catch (Exception ex) {
			LOGGER.error(ExceptionUtils.getStackTrace(ex));
		}
		return body;
	}

	/**
	 * Process multipart e-mail
	 */
	private static String handleMultipart(Part part, List<EmailAttachment> attachments, boolean ignoreAttachments) throws MessagingException, IOException {
		if (part.isMimeType("multipart/alternative")) {
			return hadleMultipartAlternative(part, attachments, ignoreAttachments);
		} else if (part.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) part.getContent();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < mp.getCount(); i++) {
				StringBuffer s = handlePart(mp.getBodyPart(i), attachments, ignoreAttachments);
				if (s != null) {
					sb.append(s);
				}
			}
			return sb.toString();
		}
		return null;
	}

	private static String hadleMultipartAlternative(Part part, List<EmailAttachment> attachments, boolean ignoreAttachments)
			throws IOException, MessagingException {
		LOGGER.debug("hadleMultipartAlternative");
		// prefer HTML text over plain text
		Multipart mp = (Multipart) part.getContent();
		String plainText = null;
		String otherText = null;
		for (int i = 0; i < mp.getCount(); i++) {
			Part bp = mp.getBodyPart(i);
			if (bp.isMimeType("text/html")) {
				return getText(bp);
			} else if (bp.isMimeType("text/plain")) {
				plainText = getText(bp);
			} else {
				LOGGER.debug("Process alternative body part having mimeType:" + bp.getContentType());
				otherText = handlePart(bp, attachments, ignoreAttachments).toString();
			}
		}
		if (otherText != null) {
			return otherText;
		} else {
			return plainText;
		}
	}

	/**
	 * Processes part of a message
	 */
	private static String handleSimplePart(Part part, List<EmailAttachment> attachments, boolean ignoreAttachments) throws MessagingException, IOException {
		// Check for content disposition and content type
		String disposition = part.getDisposition();
		String contentType = part.getContentType();
		LOGGER.debug("disposition=" + disposition);
		LOGGER.debug("Body type is: " + contentType);

		// tread if the part is a message
		boolean inlineMessage = part.isMimeType("message/rfc822");
		if (inlineMessage) {
			LOGGER.debug("Inner message:" + part.getFileName());
			Message subMessage = (Message) part.getContent();
			StringBuffer s = handlePart(subMessage, attachments, ignoreAttachments);
			System.out.println();
			return s.toString();
		}
		if (disposition == null) {
			if ("image/BMP".equals(contentType)) {
				// BMP image add as attachment
				if (ignoreAttachments == false) {
					try {
						attachments.add(createEmailAttachment(part, "image.bmp"));
					} catch (Exception e) {
						// just ignore
					}
				}
				return null;
			} else if (part.isMimeType("text/*")) {
				return getText(part);
			} else {
				if (ignoreAttachments == false) {
					handleAttachment(part, attachments);
				}
				return null;
			}
		}
		if (disposition.equalsIgnoreCase(Part.INLINE)) {
			return handleInline(part, attachments, ignoreAttachments);
		}
		if (disposition.equalsIgnoreCase(Part.ATTACHMENT)) {
			if (ignoreAttachments == false) {
				handleAttachment(part, attachments);
			}
			return null;
		}
		LOGGER.debug("Unknown disposition:" + disposition + "Threat as attachment");
		if (ignoreAttachments == false) {
			handleAttachment(part, attachments);
		}
		return null;
	}

	private static String getText(Part part) throws MessagingException, IOException {
		String result = null;
		if (part.isMimeType("text/plain")) {
			result = Text2HTML.addHTMLBreaks(StringEscapeUtils.escapeHtml((String) part.getContent()));

		} else if (part.isMimeType("text/html")) {
			result = part.getContent().toString();
		} else {
			LOGGER.debug("Process as text the part having mimeType:" + part.getContentType());
			result = Text2HTML.addHTMLBreaks(StringEscapeUtils.escapeHtml(part.getContent().toString()));
		}
		return result;
	}

	private static String handleInline(Part part, List attachments, boolean ignoreAttachments) throws MessagingException, IOException {
		String fileName = part.getFileName();
		if (fileName != null) {
			// this is an attachment
			if (ignoreAttachments == false) {
				handleAttachment(part, attachments);
			}
			return null;
		}
		// inline content
		if (part.isMimeType("text/*")) {
			return getText(part);
		} else {
			// binary inline content and no fileNameprovide
			// treat as attachemnt with unknow file name
			if (ignoreAttachments == false) {
				handleAttachment(part, attachments);
			}
			return null;
		}
	}

	private static void handleAttachment(Part part, List<EmailAttachment> attachments) throws MessagingException {
		String fileName = part.getFileName();
		try {
			if (fileName != null) {
				fileName = MimeUtility.decodeText(fileName);
			} else {
				fileName = "binaryPart.dat";
			}
			attachments.add(createEmailAttachment(part, fileName));
		} catch (Exception e) {
			// just ignore
		}
	}

	private static EmailAttachment createEmailAttachment(Part part, String fileName) throws java.io.IOException, javax.mail.MessagingException {
		EmailAttachment emailAttachment = new EmailAttachment();
		File file = MailReader.saveFile(fileName, part.getInputStream());
		emailAttachment.setFile(file);
		emailAttachment.setFileName(fileName);
		String cid = null;
		String[] contentIDs = part.getHeader("Content-ID");
		if (contentIDs != null && contentIDs.length > 0) {
			cid = contentIDs[0];
			if (cid != null && cid.startsWith("<")) {
				cid = cid.substring(1, cid.length() - 1);
			}
		}
		emailAttachment.setCid(cid);
		return emailAttachment;
	}

	/**
	 * Saves attachment
	 */
	public static File saveFile(String filename, InputStream input) throws IOException {
		// in case there is no filename, create temporary file and use its
		// filename instead
		String tempDir = HandleHome.getTrackplus_Home() + File.separator + HandleHome.DATA_DIR + File.separator + "temp";
		File dirTem = new File(tempDir);
		if (!dirTem.exists()) {
			dirTem.mkdirs();
		}
		File file;
		if (filename == null) {
			file = File.createTempFile("xxxxxx", ".out", dirTem);
		} else {
			// Existing files will not be overwritten
			file = new File(dirTem, filename);
		}

		int i = 0;
		while (file.exists()) {
			// Extend filename by number so it is unique
			file = new File(dirTem, i + filename);
			i++;
		}

		// BufferedOutputStream for writing into the file
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));

		// BufferedInputStream for reading the parts
		BufferedInputStream bis = new BufferedInputStream(input);

		// read data and write to output stream
		int aByte;
		while ((aByte = bis.read()) != -1) {
			bos.write(aByte);
		}

		// release resources
		bos.flush();
		bos.close();
		bis.close();
		return file;
	}
}
