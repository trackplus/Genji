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

package com.aurel.track.admin.customize.scripting;

import groovy.lang.GroovyObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.prop.SessionBean;
import com.aurel.track.util.LdapUtil;
import com.aurel.track.util.event.IEventSubscriber;

public class GroovyScriptExecuter {
	private static final Logger LOGGER = LogManager.getLogger(GroovyScriptExecuter.class);

	/**
	 * the name of the handler method in the script
	 */
	public static final String HANDLE_METHOD_NAME = "handleEvent";

	/**
	 * Hardcoded script name name for filtering out e-mail submissions
	 */
	public static final String EMAIL_GUARD_SCRIPT = "EmailGuardScript";

	/**
	 * Hardcoded script name for configuring the newly created item by e-mail
	 */
	public static final String EMAIL_ACTIVITY_SCRIPT = "EmailActivityScript";

	/**
	 * Hardcoded script name executed by sending item e-mail
	 */
	public static final String EMAIL_SEND_SCRIPT = "EmailSendScript";

	/**
	 * Hardcoded script name for ldap synchronize
	 */
	public static final String EVENT_HANDLER_LDAP_SYNCHRONIZER_CLASS = "LdapSynchronizer";

	/**
	 * Used in guards to prevent either creation of an item (for e-mail
	 * submission) or reject the status transition
	 */
	public static final String HPACKAGE = "com.trackplus.event.";

	/**
	 * Class name for the event handler for user events (login, logout, add
	 * user, remove user, etc.)
	 */
	public static final String EVENT_HANDLER_USER_CLASS = HPACKAGE + "EventHandlerUser";

	/**
	 * Class name for the event handler handling system events (startup,
	 * shutdown, etc.)
	 */
	public static final String EVENT_HANDLER_SYSTEM_CLASS = HPACKAGE + "EventHandlerSystem";

	/**
	 * Class name for the event handler handling project events (create project,
	 * delete project, etc.)
	 */
	public static final String EVENT_HANDLER_PROJECT_CLASS = HPACKAGE + "EventHandlerProject";

	/**
	 * Script class handling item save events.
	 */
	public static final String ITEM_SAVE_HANDLER = "ItemSaveHandler";

	/**
	 * The constructor is being called from the system during login. It
	 * initializes the EventHandler with some helpful information which is
	 * stored in a {@link TPersonBean} and a {@link SessionBean}.
	 * <p/>
	 * The user and session bean contain information such as user name, user
	 * e-mail, user preferred locale, last login, the {@link ApplicationBean}
	 * containing the application context, and much more.
	 * <p/>
	 * Access to the persistence layer can be obtained via the static
	 * {@link DAOFactory} class like this:<br/>
	 * <code>
	 * &nbsp;&nbsp;...<br/>
	 * &nbsp;&nbsp;DAOFactory fact = DAOFactory.getFactory();<br/>
	 * &nbsp;&nbsp;WorkItemDAO widao = fact.getWorkItemDAO();<br/>
	 * &nbsp;&nbsp;...<br/>
	 */
	public GroovyScriptExecuter() {

	}

	/**
	 * 
	 * @param eventNo
	 * @param inputBinding
	 * @return
	 */
	public Map handleEvent(int eventNo, Map<String, Object> inputBinding) {
		Map returnBinding = null;// = new HashMap();
		inputBinding.put(BINDING_PARAMS.CONTINUE, new Boolean(true));
		inputBinding.put(BINDING_PARAMS.EVENT, new Integer(eventNo));
		if (eventNo >= IEventSubscriber.EVENT_ISSUE_BASE && eventNo < IEventSubscriber.EVENT_ISSUE_BASE + 1000) {
			if (GroovyScriptLoader.getInstance().doesGroovyClassExist(ITEM_SAVE_HANDLER)) {
				returnBinding = executeGroovyHandler(ITEM_SAVE_HANDLER, inputBinding);
			}
		} else if (eventNo >= IEventSubscriber.EVENT_USER_BASE && eventNo < IEventSubscriber.EVENT_USER_BASE + 1000) {
			if (GroovyScriptLoader.getInstance().doesGroovyClassExist(EVENT_HANDLER_USER_CLASS)) {
				returnBinding = executeGroovyHandler(EVENT_HANDLER_USER_CLASS, inputBinding);
			}
		} else if (eventNo >= IEventSubscriber.EVENT_PROJECT_BASE && eventNo < IEventSubscriber.EVENT_PROJECT_BASE + 1000) {
			if (GroovyScriptLoader.getInstance().doesGroovyClassExist(EVENT_HANDLER_PROJECT_CLASS)) {
				returnBinding = executeGroovyHandler(EVENT_HANDLER_PROJECT_CLASS, inputBinding);
			}
		} else if (eventNo >= IEventSubscriber.EVENT_SYSTEM_BASE && eventNo < IEventSubscriber.EVENT_SYSTEM_BASE + 1000) {
			if (GroovyScriptLoader.getInstance().doesGroovyClassExist(EVENT_HANDLER_SYSTEM_CLASS)) {
				returnBinding = executeGroovyHandler(EVENT_HANDLER_SYSTEM_CLASS, inputBinding);
			}
		}
		if (returnBinding == null || returnBinding.get(BINDING_PARAMS.CONTINUE) == null
				|| !returnBinding.get(BINDING_PARAMS.CONTINUE).getClass().equals(Boolean.class)) {
			if (returnBinding != null) {
				LOGGER.warn("Problem with Groovy EventHandler returnBinding type: is not Boolean");
			}
			returnBinding = new HashMap();
			returnBinding.put(BINDING_PARAMS.CONTINUE, new Boolean(true)); // default
																			// is
																			// to
																			// proceed
																			// with
																			// operation
		}
		return returnBinding;
	}

	/**
	 * Helper method for parameter classes. The "parameter" script are probably
	 * changed according to the customer needs while the logic scripts are
	 * relative stable (probably not changed by the customer) If the parameter
	 * script is changed (and automatically recompiled) the original "parameter"
	 * class file seems to remain in the Groovy classpath. In the local
	 * GroovyScriptLoader cache (availableClasses map) the actual version is
	 * stored That's why in the logic scripts the parameter script's
	 * fields/methods shouldn't be accessed directly (it contains the old
	 * version of the class from the Groovy classpath) but from the
	 * GroovyScriptLoader cache through reflection.
	 * 
	 * @param handlerClass
	 * @param methodName
	 * @return
	 */
	public static Object getParameterInstanceGroovyHandler(String handlerClass, String methodName) {
		if (GroovyScriptLoader.getInstance().doesGroovyClassExist(handlerClass)) {
			try {
				Class groovyClass = GroovyScriptLoader.getInstance().getGroovyClass(handlerClass);
				Object object = null;
				Method getInstance = groovyClass.getMethod("getInstance", new Class[] {});
				if (getInstance != null) {
					// if getInstance() exists get the already initialized
					// "parameter" object (instance of "parameter" class)
					object = getInstance.invoke(groovyClass, new Object[] {});
				} else {
					// no getInstance(): always create and initialize a new
					// "parameter" object (instance of "parameter" class)
					object = groovyClass.newInstance();
				}
				// method name (probably a getter method)
				Method method = groovyClass.getMethod(methodName, new Class[] {});
				return method.invoke(object, (Object[]) new Class[] {});
			} catch (Exception e) {
				LOGGER.warn("Problem calling Groovy EventHandler: " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		} else {
			LOGGER.debug("The Groovy class " + handlerClass + " was not found");
		}
		return null;
	}

	/**
	 * If something goes wrong here the returnBinding is unchanged, that is the
	 * regular action commences.
	 */
	public static Map<String, Object> executeGroovyHandler(String handlerClass, Map<String, Object> inputBinding) {
		Map<String, Object> returnBinding = null;
		if (GroovyScriptLoader.getInstance().doesGroovyClassExist(handlerClass)) {
			try {
				// GroovyObject handler =
				// GroovyScriptLoader.getInstance().newInstance(handlerClass);
				GroovyObject handler = (GroovyObject) GroovyScriptLoader.getInstance().getGroovyClass(handlerClass).newInstance();
				returnBinding = (Map<String, Object>) handler.invokeMethod(HANDLE_METHOD_NAME, inputBinding);
			} catch (Exception e) {
				LOGGER.error("Problem calling Groovy EventHandler: " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		} else {
			LOGGER.debug("The Groovy class " + handlerClass + " was not found");
		}
		return returnBinding;
	}

	/**
	 * Execute a Groovy method on a Groovy class
	 * 
	 * @param handlerClass
	 * @param methodName
	 * @param inputBinding
	 * @return
	 */
	public static Object executeGroovyMethod(String handlerClass, String methodName, Object inputBinding) {
		if (GroovyScriptLoader.getInstance().doesGroovyClassExist(handlerClass)) {
			try {
				GroovyObject handler = (GroovyObject) GroovyScriptLoader.getInstance().getGroovyClass(handlerClass).newInstance();
				return handler.invokeMethod(methodName, inputBinding);
			} catch (Exception e) {
				LOGGER.warn("Calling method " + methodName + " on handler class " + handlerClass + " failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		} else {
			LOGGER.debug("The Groovy class " + handlerClass + " was not found");
		}
		return null;
	}

	/**
	 * Executes the Groovy script activity 1. may modify the workItemBean before
	 * save 2. may return list of ErrorData in the returnBinding map containing
	 * "errorList" as key. If the list is not empty then the issue will not be
	 * saved but the error message will be shown as a validation error
	 * 
	 * @param handlerClass
	 * @param workItemContext
	 * @param personBean
	 * @return
	 */
	public static List<ErrorData> executeActivityScript(String handlerClass, WorkItemContext workItemContext, TPersonBean personBean) {
		Map<String, Object> inputBinding = workItemContext.getInputBinding();
		if (inputBinding == null) {
			// not from e-mail submission
			inputBinding = new HashMap<String, Object>();
		}
		inputBinding.put(BINDING_PARAMS.USER, personBean);
		inputBinding.put(BINDING_PARAMS.LOCALE, workItemContext.getLocale());
		inputBinding.put(BINDING_PARAMS.ISSUE, workItemContext.getWorkItemBean());
		inputBinding.put(BINDING_PARAMS.ISSUE_ORIGINAL, workItemContext.getWorkItemBeanOriginal());
		inputBinding.put(BINDING_PARAMS.WORKITEM_CONTEXT, workItemContext);
		Map<String, Object> returnBinding = executeGroovyHandler(handlerClass, inputBinding);
		if (returnBinding != null) {
			return (List<ErrorData>) returnBinding.get(BINDING_PARAMS.ERRORLIST);
		} else {
			LOGGER.debug("Activity script " + handlerClass + " does not exist");
			return null;
		}
	}

	/**
	 * Executes the Groovy script guard. If the guard is not satisfied then the
	 * script should set the guardPassed boolean value to false in the return
	 * binding
	 * 
	 * @param handlerClass
	 * @param workItemBean
	 * @param personID
	 * @param inputBinding
	 *            context coming from specific source (e-mail)
	 * @return
	 */
	public static boolean executeGuardScript(String handlerClass, TWorkItemBean workItemBean, Integer personID, Map<String, Object> inputBinding) {
		if (inputBinding == null) {
			inputBinding = new HashMap<String, Object>();
		}
		inputBinding.put(BINDING_PARAMS.USER_ID, personID);
		inputBinding.put(BINDING_PARAMS.ISSUE, workItemBean);
		inputBinding.put(BINDING_PARAMS.ISSUE_ORIGINAL, workItemBean);
		Map<String, Object> returnBinding = executeGroovyHandler(handlerClass, inputBinding);
		Boolean guardPassed = null;
		if (returnBinding != null) {
			guardPassed = (Boolean) returnBinding.get(BINDING_PARAMS.GUARD_PASSED);
			if (guardPassed != null) {
				LOGGER.debug("Guard " + handlerClass + " passed: " + guardPassed.booleanValue());
			}
		} else {
			LOGGER.debug("Guard " + handlerClass + " does not exists");
		}
		if (guardPassed == null) {
			return true;
		}
		return guardPassed.booleanValue();
	}

	/**
	 * Executes the Groovy script guard. If the guard is not satisfied then the
	 * script should set the "reject" boolean value in the return binding
	 * 
	 * @param handlerClass
	 *            the Groovy handler class
	 * @param siteBean
	 *            the TSiteBean object for this Genji instance
	 * @param filter
	 *            the LDAP filter expression for the search
	 * @return a single HashMap <"map", HashMap (login name, TPersonBean)>
	 */
	public static Map<String, Object> executeLdapScript(String handlerClass, TSiteBean siteBean, String filter) {
		Map<String, Object> inputBinding = null;
		if (inputBinding == null) {
			inputBinding = new HashMap<String, Object>();
		}
		inputBinding.put(BINDING_PARAMS.SITEBEAN, siteBean);
		inputBinding.put(BINDING_PARAMS.LDAP_FILTER, filter);
		inputBinding.put(BINDING_PARAMS.LDAPMAP, LdapUtil.getLdapMap());
		Map<String, Object> returnBinding = null;
		try {
			returnBinding = executeGroovyHandler(handlerClass, inputBinding);
		} catch (Exception e) {
			return null;
		}
		return returnBinding;
	}

}
