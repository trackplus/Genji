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

package com.aurel.track.admin.customize.treeConfig.mailTemplate;

import javax.servlet.http.HttpServletResponse;

import com.aurel.track.admin.customize.treeConfig.TreeConfigBL;
import com.aurel.track.admin.customize.treeConfig.TreeConfigIDTokens;
import com.aurel.track.beans.TMailTemplateConfigBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.MailTemplateConfigDAO;
import com.aurel.track.json.JSONUtility;

/**
 *
 */
public class MailTemplateConfigEditBL {
	private static MailTemplateConfigDAO mailTemplateCfgDAO= DAOFactory.getFactory().getMailTemplateConfigDAO();

	private  MailTemplateConfigEditBL(){
	}

	/**
	 * Change the mailTemplate configuration
	 * @param cfg
	 * @param mailTemplateID
	 */
	private static void updateConfig(TMailTemplateConfigBean cfg, Integer eventID, Integer mailTemplateID) {
		TMailTemplateConfigBean mailTemplateConfigBean;
		if (cfg==null) {
			mailTemplateConfigBean = new TMailTemplateConfigBean();
			mailTemplateConfigBean.setEventKey(eventID);
		}else{
			mailTemplateConfigBean=cfg;
		}
		mailTemplateConfigBean.setMailTemplate(null);//to force isModify to true
		mailTemplateConfigBean.setTMailTemplateBean(null);//
		mailTemplateConfigBean.setMailTemplate(mailTemplateID);
		mailTemplateCfgDAO.save(mailTemplateConfigBean);
	}
	/**
	 * Saves the screen configuration
	 * @return
	 */
	static String save(String node, Integer mailTemplateID, HttpServletResponse servletResponse){
		if (mailTemplateID!=null) {
			TreeConfigIDTokens treeConfigIDTokens = TreeConfigIDTokens.decodeNode(node);
			Integer issueType = treeConfigIDTokens.getIssueTypeID();
			Integer projectType = treeConfigIDTokens.getProjectTypeID();
			Integer project = treeConfigIDTokens.getProjectID();
			Integer configRel = treeConfigIDTokens.getConfigRelID();
			TMailTemplateConfigBean mailTemplateConfigBean = (TMailTemplateConfigBean)MailTemplateConfigFacade.getInstance().
					getValidConfigDirect(issueType, projectType, project, configRel);
			if (mailTemplateConfigBean==null) {
				//a fallback node will be first overridden and then actualized with the mailTemplate
				mailTemplateConfigBean = (TMailTemplateConfigBean) TreeConfigBL.overwrite(node);
			}
			updateConfig(mailTemplateConfigBean, configRel, mailTemplateID);
		}
		JSONUtility.encodeJSON(servletResponse,
				MailTemplateAssignmentJSON.getTemplateDetailSaveJSON(node));
		return null;
	}

}
