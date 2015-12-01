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

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TScriptsBean;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.event.EventPublisher;
import com.aurel.track.util.event.FieldChangeScriptHandler;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * Action for script configuration
 * @author Tamas Ruff
 *
 */
public class ScriptAdminAction extends ActionSupport implements Preparable, SessionAware, ServletResponseAware {
	
	private static final long serialVersionUID = 372L;
	
	private Map<String,Object> session;
	private HttpServletResponse servletResponse;
	private Locale locale;
	private TPersonBean personBean;
	private Integer personID;
	private boolean copy;

	private Integer scriptID;
	private String clazzName;
	private String sourceCode;
	private Integer scriptType;
	//the delete should be confirmed if has dependencies
	private boolean deleteConfirmed;
	
	@Override
	public void prepare() throws Exception {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		personID = personBean.getObjectID();
		locale = (Locale)session.get(Constants.LOCALE_KEY);
	}
	
	/**
	 * List of scripts
	 * @return
	 */
	public String loadScripts(){
		List<TScriptsBean> scriptBeans = ScriptAdminBL.getAllScripts();
		Map<Integer, String> scriptTypeLabels = ScriptAdminBL.getScriptTypeLabels(locale);
		JSONUtility.encodeJSON(servletResponse,
				ScriptJSON.encodeJSONScripts(scriptBeans, scriptTypeLabels));
		return null;
	}
	
	/**
	 * Add/edit/copy a script
	 * @return
	 */
	public String edit() {
		TScriptsBean scriptBean=null;
		if(scriptID==null){
			//NEW
			scriptBean=new TScriptsBean();
		}else{
			if(copy){
				scriptBean = ScriptAdminBL.copyScript(scriptID,locale);
			}else{
				//EDIT
				scriptBean = ScriptAdminBL.loadByPrimaryKey(scriptID);
			}
		}
		List<IntegerStringBean> scriptTypeLists = ScriptAdminBL.getScriptTypesList(locale);
		JSONUtility.encodeJSON(servletResponse,
				ScriptJSON.encodeScript(scriptBean, scriptTypeLists));
		return null;
	}

	/**
	 * Save a script
	 * @return
	 */
	public String save() {
		TScriptsBean scriptBean;
		String oldClassName = null;
		if(scriptID==null || copy){
			scriptBean = new TScriptsBean();
		}else{
			scriptBean=ScriptAdminBL.loadByPrimaryKey(scriptID);
			TScriptsBean oldScriptBean = ScriptAdminBL.loadByPrimaryKey(scriptID);
			if (oldScriptBean!=null) {
				oldClassName = oldScriptBean.getClazzName();
			}
		}
		boolean classNameExists = ScriptAdminBL.classNameExists(clazzName, scriptID);
		if (classNameExists) {
			JSONUtility.encodeJSONFailure(servletResponse,
					LocalizeUtil.getParametrizedString("common.err.unique",
							new Object[] {LocalizeUtil.getLocalizedTextFromApplicationResources(
									"admin.customize.script.lbl.className", locale)} , locale));
			return null;
		}
		scriptBean.setClazzName(clazzName);
		scriptBean.setScriptType(scriptType);
		scriptBean.setSourceCode(sourceCode);
		scriptBean.setPerson(personID);
		scriptBean.setLastEdit(new Date());
		if (scriptType==null || scriptType.intValue()!=(TScriptsBean.SCRIPT_TYPE.PARAMETER_SCRIPT)) {
			GroovyScriptLoader.getInstance().reloadScript(clazzName, sourceCode, oldClassName);
		}
		EventPublisher.getInstance().detach(FieldChangeScriptHandler.getInstance());
		EventPublisher.getInstance().attach(FieldChangeScriptHandler.getInstance());
		ScriptAdminBL.saveScript(scriptBean);
		JSONUtility.encodeJSONSuccess(servletResponse);
		return null;
	}
	
	/**
	 * Compile a script
	 * @return
	 */
	public String compile() {
		String complileError = GroovyScriptLoader.getInstance().parseScript(clazzName, sourceCode);
		boolean success = false;
		String message = null;
		if (complileError!=null) {
			message = LocalizeUtil.getParametrizedString(
					"admin.customize.script.err.classNotCompilable", new String[] {complileError}, locale);
		}else {
			success = true;
			message = LocalizeUtil.getLocalizedTextFromApplicationResources(
					"admin.customize.script.lbl.classCompilable", locale);
		}
		JSONUtility.encodeJSON(servletResponse,
				ScriptJSON.compileResultJSON(success, message, locale));
		return null;
	}
	
	/**
	 * Deletes a script
	 * @return
	 */
	public String delete() {
		if (personBean.isSys() /*|| ScriptAdminBL.isAllowedToDeleteScript(scriptID, personID)*/) {
			if (!deleteConfirmed && ScriptAdminBL.hasDependentData(scriptID)) {
				String errorMessage = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.script.err.hasDependences", locale); 
				errorMessage = errorMessage + " " + LocalizeUtil.getLocalizedTextFromApplicationResources("common.lbl.cancelDeleteAlert", locale);
				JSONUtility.encodeJSONFailure(servletResponse,
						errorMessage, JSONUtility.DELETE_ERROR_CODES.NOT_EMPTY_WARNING);
			} else {
				TScriptsBean toBeDeletedScriptsBean = ScriptAdminBL.loadByPrimaryKey(scriptID);
				if (toBeDeletedScriptsBean!=null) {
					GroovyScriptLoader.getInstance().deleteScript(toBeDeletedScriptsBean.getClazzName());
				}
				ScriptAdminBL.deleteScript(scriptID);
				JSONUtility.encodeJSONSuccess(servletResponse);
			}
		} else {
			JSONUtility.encodeJSONFailure(servletResponse,
					getText("admin.customize.automail.trigger.err.deleteNotAllowed"), JSONUtility.DELETE_ERROR_CODES.NO_RIGHT_TO_DELETE);
		}
		return null;
	}

	/**
	 * @param session the session to set
	 */
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
	
	public void setScriptID(Integer scriptID) {
		this.scriptID = scriptID;
	}
	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	public void setScriptType(Integer scriptType) {
		this.scriptType = scriptType;
	}
	public void setCopy(boolean copy) {
		this.copy = copy;
	}
	public void setDeleteConfirmed(boolean deleteConfirmed) {
		this.deleteConfirmed = deleteConfirmed;
	}
	
}
