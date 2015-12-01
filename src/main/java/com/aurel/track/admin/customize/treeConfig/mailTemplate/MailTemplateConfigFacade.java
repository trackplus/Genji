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

package com.aurel.track.admin.customize.treeConfig.mailTemplate;

import com.aurel.track.admin.customize.treeConfig.*;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.*;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.MailTemplateConfigDAO;
import com.aurel.track.dao.MailTemplateDAO;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.event.IEventSubscriber;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.*;

/**
 *
 */
public class MailTemplateConfigFacade extends ConfigItemAbstract implements ConfigItemFacade {

	private static final Logger LOGGER = LogManager.getLogger(MailTemplateConfigFacade.class);
	private static MailTemplateConfigDAO mailTemplateCfgDAO= DAOFactory.getFactory().getMailTemplateConfigDAO();
	private static MailTemplateDAO mailTemplateDAO=DAOFactory.getFactory().getMailTemplateDAO();
	private static MailTemplateConfigFacade instance;

	private static Integer EVENT_KEY = Integer.valueOf(1);
	private static Integer TEMPLATE_KEY = Integer.valueOf(2);

	/**
	 * Get a singleton instance
	 * @return
	 */
	public static MailTemplateConfigFacade getInstance(){
		if(instance==null){
			instance=new MailTemplateConfigFacade();
		}
		return instance;
	}

	/**
	 * Expand a tree node from tree
	 * @return
	 */
	@Override
	public List<TreeConfigNodeTO> getFirstLevelNodes(TPersonBean personBean, Locale locale){
		List<TreeConfigNodeTO> rootNodes = new ArrayList<TreeConfigNodeTO>();
		if (personBean.isSys()) {
			TreeConfigNodeTO standardScreenAssignments = new TreeConfigNodeTO(
					TreeConfigIDTokens.encodeRootNode(TreeConfigBL.MAIL_TEMPLATE_CONFIG, TreeConfigBL.MAIL_TEMPLATE_TYPE),
					TreeConfigBL.MAIL_TEMPLATE_CONFIG, TreeConfigBL.MAIL_TEMPLATE_TYPE,
					LocalizeUtil.getLocalizedTextFromApplicationResources("common.nodeDetail.lbl.mailTemplate.standard", locale), false);
			standardScreenAssignments.setIconCls(TreeConfigBL.ICON_CLS.GLOBAL_CONFIG);
			standardScreenAssignments.setDefaultConfig(true);
			standardScreenAssignments.setChildrenAreLeaf(true);
			rootNodes.add(standardScreenAssignments);
			List<TMailTemplateConfigBean> issueTypeSpecificFieldConfigList = mailTemplateCfgDAO.loadAllByIssueType(null, null, null);
			String issueTypeTitle = LocalizeUtil.getLocalizedTextFromApplicationResources("common.nodeDetail.lbl.mailTemplate.issueType", locale);
			if (issueTypeSpecificFieldConfigList==null || issueTypeSpecificFieldConfigList.isEmpty()) {
				issueTypeTitle = TreeConfigBL.addInheritedMarkup(issueTypeTitle);
			}
			TreeConfigNodeTO issueTypeFields = new TreeConfigNodeTO(
					TreeConfigIDTokens.encodeRootNode(TreeConfigBL.MAIL_TEMPLATE_CONFIG, TreeConfigBL.ISSUE_TYPE),
					TreeConfigBL.MAIL_TEMPLATE_CONFIG, TreeConfigBL.ISSUE_TYPE,
					issueTypeTitle, false);
			issueTypeFields.setIconCls(TreeConfigBL.ICON_CLS.ISSUE_TYPE_CONFIG);
			rootNodes.add(issueTypeFields);
			List<TMailTemplateConfigBean> projectTypeSpecificFieldConfigList = mailTemplateCfgDAO.loadAllByProjectType(null);
			String projectTypeTitle = LocalizeUtil.getLocalizedTextFromApplicationResources("common.nodeDetail.lbl.mailTemplate.projectType", locale);
			if (projectTypeSpecificFieldConfigList==null || projectTypeSpecificFieldConfigList.isEmpty()) {
				projectTypeTitle = TreeConfigBL.addInheritedMarkup(projectTypeTitle);
			}
			TreeConfigNodeTO projectTypeFields = new TreeConfigNodeTO(
					TreeConfigIDTokens.encodeRootNode(TreeConfigBL.MAIL_TEMPLATE_CONFIG, TreeConfigBL.PROJECT_TYPE),
					TreeConfigBL.MAIL_TEMPLATE_CONFIG, TreeConfigBL.PROJECT_TYPE,
					projectTypeTitle, false);
			projectTypeFields.setIconCls(TreeConfigBL.ICON_CLS.PROJECT_TYPE_CONFIG);
			rootNodes.add(projectTypeFields);
		}
		List<TMailTemplateConfigBean> projectSpecificFieldConfigList = mailTemplateCfgDAO.loadAllByProject(null);
		String projectTitle = LocalizeUtil.getLocalizedTextFromApplicationResources("common.nodeDetail.lbl.mailTemplate.project", locale);
		if (projectSpecificFieldConfigList==null || projectSpecificFieldConfigList.isEmpty()) {
			projectTitle = TreeConfigBL.addInheritedMarkup(projectTitle);
		}
		TreeConfigNodeTO projectFields = new TreeConfigNodeTO(
				TreeConfigIDTokens.encodeRootNode(TreeConfigBL.MAIL_TEMPLATE_CONFIG, TreeConfigBL.PROJECT),
				TreeConfigBL.MAIL_TEMPLATE_CONFIG, TreeConfigBL.PROJECT,
				projectTitle, false);
		projectFields.setIconCls(TreeConfigBL.ICON_CLS.PROJECT_CONFIG);
		rootNodes.add(projectFields);
		return rootNodes;
	}

	/**
	 * Whether there are issue type specific configurations
	 */
	@Override
	public boolean hasIssueTypeSpecificConfig(Integer issueType, Integer projectType, Integer project) {
		List<TMailTemplateConfigBean> mailTemplateConfigBeans = mailTemplateCfgDAO.loadAllByIssueType(issueType, projectType, project);
		return mailTemplateConfigBeans!=null && !mailTemplateConfigBeans.isEmpty();
	}

	/**
	 * Whether there are project type specific configurations
	 */
	@Override
	public boolean hasProjectTypeSpecificConfig(Integer projectType) {
		List<TMailTemplateConfigBean> mailTemplateConfigBeans = mailTemplateCfgDAO.loadAllByProjectType(projectType);
		return mailTemplateConfigBeans!=null && !mailTemplateConfigBeans.isEmpty();
	}
	
	/**
	 * Load all project specific configurations (also those which have item type specified)
	 * @param projectIDs if null get all project specific configurations
	 * @return
	 */
	@Override
	public List<ConfigItem> loadAllProjectSpecificConfig(List<Integer> projectIDs) {
		return (List)mailTemplateCfgDAO.loadAllByProjects(projectIDs);
	}
	
	/**
	 * Whether there are specific configurations for system fields or custom fields
	 * @param issueType
	 * @param projectType
	 * @param project
	 * @param isCustom
	 * @return
	 */
	@Override
	public boolean hasFieldTypeSpecificConfig(Integer issueType, Integer projectType,  Integer project, boolean isCustom) {
		return true;
	}

	/**
	 *  Gets the configuration by itemType and project
	 * @param configRel
	 * @param itemTypeID
	 * @param projectID
	 * @return
	 */
	@Override
	public ConfigItem loadForConfigRelByIssueTypeAndProject(Integer configRel, Integer itemTypeID, Integer projectID) {
		return mailTemplateCfgDAO.loadByIssueTypeAndProject(configRel, itemTypeID, projectID);
	}
		
	/**
	 * Gets the configuration by itemType and projectType
	 * @param configRel
	 * @param itemTypeID
	 * @param projectTypeID
	 * @return
	 */
	@Override
	public ConfigItem loadForConfigRelByIssueTypeAndProjectType(Integer configRel, Integer itemTypeID, Integer projectTypeID) {
		return mailTemplateCfgDAO.loadByIssueTypeAndProjectType(configRel, itemTypeID, projectTypeID);
	}
	
	/**
	 * Gets the configuration by project
	 * @param configRel
	 * @param projectID
	 * @return
	 */
	@Override
	public ConfigItem loadForConfigRelByProject(Integer configRel, Integer projectID) {
		return mailTemplateCfgDAO.loadByProject(configRel, projectID);
	}
	
	/**
	 * Gets the configuration by projectType
	 * @param configRel
	 * @param projectTypeID
	 * @return
	 */
	@Override
	public ConfigItem loadForConfigRelByProjectType(Integer configRel, Integer projectTypeID) {
		return mailTemplateCfgDAO.loadByProjectType(configRel, projectTypeID);
	}
	
	/**
	 * Gets the configuration by itemType
	 * @param configRel
	 * @param itemTypeID
	 * @return
	 */
	@Override
	public ConfigItem loadForConfigRelByItemType(Integer configRel, Integer itemTypeID) {
		return mailTemplateCfgDAO.loadByIssueType(configRel, itemTypeID);
	}
	
	/**
	 * Gets the default configuration
	 * @param configRel
	 * @return
	 */
	@Override
	public ConfigItem loadForConfigRelDefault(Integer configRel) {
		return mailTemplateCfgDAO.loadDefault(configRel);
	}
	
	/**
	 * Whether the only project or only projectTyp specific configuration can be defined
	 * @return
	 */
	@Override
	public boolean hasProjectOrProjectTypSpecificConfigs() {
		return false;
	}
	/**
	 * Deletes a single configuration
	 * @param treeConfigIDTokens
	 */
	@Override
	public void deleteConfig(TreeConfigIDTokens treeConfigIDTokens) {
		if (!(treeConfigIDTokens.getIssueTypeID()==null && treeConfigIDTokens.getProjectTypeID()==null &&
				treeConfigIDTokens.getProjectID()==null) || !TreeConfigBL.MAIL_TEMPLATE_TYPE.equals(treeConfigIDTokens.getType())) {
			String type = treeConfigIDTokens.getType();
			Integer issueTypeID = treeConfigIDTokens.getIssueTypeID();
			Integer projectTypeID = treeConfigIDTokens.getProjectTypeID();
			Integer projectID = treeConfigIDTokens.getProjectID();
			Integer eventID = treeConfigIDTokens.getConfigRelID();
			if(type.equals(TreeConfigBL.CONFIG_ITEM)){
				TMailTemplateConfigBean mailTemplateConfigBean = (TMailTemplateConfigBean)getValidConfigDirect(issueTypeID,
								projectTypeID, projectID, eventID);
				if(mailTemplateConfigBean==null){
					LOGGER.info("Can't reset a MailTemplateConfigItem node that don't have a origanl value!");
				} else {
					mailTemplateCfgDAO.delete(mailTemplateConfigBean.getObjectID());
				}
			} else if (type.equals(TreeConfigBL.PROJECT)) {
				List<Integer> projectIDs = null;
				if (projectID!=null) {
					projectIDs = ProjectBL.getDescendantProjectIDsAsList(projectID);
				}
				mailTemplateCfgDAO.deleteByProjects(projectIDs);
			} else if (type.equals(TreeConfigBL.PROJECT_TYPE)){
				mailTemplateCfgDAO.deleteByProjectType(projectTypeID);
			} else if (type.equals(TreeConfigBL.ISSUE_TYPE)){
				mailTemplateCfgDAO.deleteByIssueType(issueTypeID, projectTypeID, projectID);
			}
		}
	}

	/**
	 * Overwrites the inherited configuration for originalConfig
	 * @param directConfig
	 * @param fallbackConfig
	 */
	@Override
	public Integer overwriteConfig(ConfigItem directConfig, ConfigItem fallbackConfig) {
		if (fallbackConfig==null) {
			return null;
		}
		copyExtraInfo(fallbackConfig, directConfig);
		return mailTemplateCfgDAO.save((TMailTemplateConfigBean)directConfig);
	}

	/**
	 * Whether after a reset operation a tree refresh is needed
	 * @return
	 */
	@Override
	public boolean refreshEntireTreeAfterReset() {
		//true because the assignment info is shown in the node's label
		return true;
	}

	/**
	 * Get the lookup map for title
	 * @param locale
	 * @return
	 */
	@Override
	public Map<Integer, Map<Integer, ILabelBean>> getLookupMap(Locale locale) {
		Map<Integer, Map<Integer, ILabelBean>> lookupMap = new HashMap<Integer, Map<Integer,ILabelBean>>();
		List<MailEventBean> eventList = MailEventBL.loadAll(locale);
		lookupMap.put(EVENT_KEY, GeneralUtils.createMapFromList(eventList));
		List<TMailTemplateBean> mailTemplateList = mailTemplateDAO.loadAll();
		lookupMap.put(TEMPLATE_KEY, GeneralUtils.createMapFromList(mailTemplateList));
		return lookupMap;
	}

	/**
	 * Get the title for a config
	 * @param cfg
	 * @return
	 */
	@Override
	public String getTitle(ConfigItem cfg, Map<Integer, Map<Integer, ILabelBean>> lookupMap) {
		Map<Integer, ILabelBean> eventMap = lookupMap.get(EVENT_KEY);
		MailEventBean mailEventBean = (MailEventBean)eventMap.get(((TMailTemplateConfigBean)cfg).getEventKey());
		Map<Integer, ILabelBean> mailTemplateMap = lookupMap.get(TEMPLATE_KEY);
		TMailTemplateBean mailTemplateBean = (TMailTemplateBean)mailTemplateMap.get(((TMailTemplateConfigBean)cfg).getMailTemplate());
		String templateName = "";
		if (mailTemplateBean!=null) {
			templateName = mailTemplateBean.getName();
		}
		return "<span class=\'event\'>"+mailEventBean.getLabel()+"</span>:" + "<span class=\'formName\'>"+ templateName+"</span>";
	}

	@Override
	public List load(ConfigItem cfg) {
		List<TMailTemplateConfigBean> mailTemplateConfigs =  mailTemplateCfgDAO.load((TMailTemplateConfigBean)cfg);
		if (cfg.isDefault()) {
			List<Integer> eventsList = new LinkedList<Integer>();
			eventsList.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_CREATE));
			eventsList.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_CREATE_BY_EMAIL));
			eventsList.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_UPDATE));
			eventsList.add(Integer.valueOf(IEventSubscriber.EVENT_POST_ISSUE_UPDATEPLANNEDVALUE));
			eventsList.add(Integer.valueOf(IEventSubscriber.EVENT_POST_USER_REGISTERED));
			eventsList.add(Integer.valueOf(IEventSubscriber.EVENT_POST_USER_SELF_REGISTERED));
			eventsList.add(Integer.valueOf(IEventSubscriber.EVENT_POST_USER_FORGOTPASSWORD));
			eventsList.add(Integer.valueOf(IEventSubscriber.EVENT_POST_USER_REMINDER));
			eventsList.add(Integer.valueOf(IEventSubscriber.EVENT_POST_USER_CREATED_BY_EMAIL));
			
			if (mailTemplateConfigs.size()<eventsList.size()) {
				for (Integer eventID : eventsList) {
					boolean eventFound = false;
					for (TMailTemplateConfigBean mailTemplateConfigBean : mailTemplateConfigs) {
						Integer event = mailTemplateConfigBean.getEventKey();
						if (eventID.equals(event)) {
							eventFound = true;
							break;
						}
					}
					if (!eventFound) {
						TMailTemplateConfigBean mailTemplateConfigBean = new TMailTemplateConfigBean();
						mailTemplateConfigBean.setEventKey(eventID);
						mailTemplateConfigs.add(mailTemplateConfigBean);
					}
				}
			}
		}
		return mailTemplateConfigs;
		
	}

	@Override
	public ConfigItem createConfigItem() {
		return new TMailTemplateConfigBean();
	}

	@Override
	public void copyExtraInfo(ConfigItem cfgSource, ConfigItem cfgTarget) {
		TMailTemplateConfigBean newCfg=(TMailTemplateConfigBean)cfgTarget;
		TMailTemplateConfigBean cfg=(TMailTemplateConfigBean)cfgSource;
		newCfg.setMailTemplate(cfg.getMailTemplate());

	}

	@Override
	public Integer saveConfig(ConfigItem cfg) {
		return mailTemplateCfgDAO.save((TMailTemplateConfigBean)cfg);
	}

	@Override
	public ConfigItem loadConfigByPk(Integer pk) {
		return mailTemplateCfgDAO.loadByPrimaryKey(pk);
	}
}
