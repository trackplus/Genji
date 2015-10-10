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

package com.aurel.track.prop;

import java.io.Serializable;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.Constants;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.TPersonBean;

/* 
 * This class manages all session related objects. It is registered with
 * the servlet containers HttpSession object and gets notified in case
 * it dies. 
 * @version $Revision: 1229 $ $Date: 2014-07-24 16:14:35 +0200 (Do, 24 Jul 2014) $
 * @author Joerg Friedrich <joerg.friedrich@computer.org>  
 */
public class SessionBean implements HttpSessionBindingListener, Serializable {

	private static final long serialVersionUID = -3110487778636614299L;
	protected String sessionId = null;
	//private List theFileList = null;
	private ApplicationBean appBean = null;
	//private AccessBeans accessBean = null;
	private static final Logger LOGGER = LogManager.getLogger(SessionBean.class);
	//private HashMap attributes = new HashMap();
	//private GroovyScriptEngine gse = null;
	private TPersonBean person = null;
	//private GroovyScriptExecuter groovyScriptExecuter = null;
	
	public void valueBound(HttpSessionBindingEvent event) {
		// retrieve session and session-id
		HttpSession session = event.getSession();
		sessionId = session.getId();
		appBean = (ApplicationBean) session.getAttribute(Constants.APPLICATION_BEAN);
		// get the user
		person= (TPersonBean) session.getAttribute(Constants.USER_KEY);
		if (person == null) {
			LOGGER.warn("No user found in session >" + sessionId + "<");
			return;
		}
		if (appBean!=null) {
			appBean.addUser(person, sessionId);
		}
	}


	/**
	 * Session gets invalidated. Caution: at this point the session
	 * object is already dead!
	 */
	public void valueUnbound(HttpSessionBindingEvent event) {
		AttachBL.deleteTempFiles(sessionId);
		// search and remove the userLabel
		if (appBean!=null) {
			appBean.removeUser(sessionId);
		}
	}
	
	
	/*public void setAttribute(String key, Object theObject) {
		attributes.put(key, theObject);
	}
	
	public Object getAttribute(String key) {
		return attributes.get(key);
	}
	
	public void removeAttribute(String key) {
		attributes.remove(key); 
	}*/
	
	/*public GroovyScriptEngine getGroovySE() {
		return this.gse;
	}*/
	
	/**
	 * Get the user ({@link TPersonBean}) related to this session.
	 * @return - the user related to this session, the one that logged on
	 */
	public TPersonBean getUser() {
		return this.person;
	}


	/*public GroovyScriptExecuter getGroovyScriptExecuter() {
		return groovyScriptExecuter;
	}


	public void setGroovyScriptExecuter(GroovyScriptExecuter groovyScriptExecuter) {
		this.groovyScriptExecuter = groovyScriptExecuter;
	}*/
	
	

}
