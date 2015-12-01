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


package com.aurel.track.dbase;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.torque.Torque;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;

import com.aurel.track.ApplicationStarter;
import com.aurel.track.StartServlet;
import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.category.filter.PredefinedQueryBL;
import com.aurel.track.admin.customize.category.report.ReportBL;
import com.aurel.track.admin.customize.lists.systemOption.PriorityBL;
import com.aurel.track.admin.customize.lists.systemOption.SeverityBL;
import com.aurel.track.admin.customize.localize.LocalizeBL;
import com.aurel.track.admin.customize.role.RoleBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.server.logging.LoggingConfigBL;
import com.aurel.track.admin.server.siteConfig.SiteConfigBL;
import com.aurel.track.admin.user.userLevel.UserLevelBL;
import com.aurel.track.beans.TBaseLineBean;
import com.aurel.track.beans.TComputedValuesBean;
import com.aurel.track.beans.TCostBean;
import com.aurel.track.beans.TExportTemplateBean;
import com.aurel.track.beans.TLinkTypeBean;
import com.aurel.track.beans.TPriorityBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TRoleBean;
import com.aurel.track.beans.TSeverityBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.beans.TStateChangeBean;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.beans.TTrailBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dao.BaseLineDAO;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.LinkTypeDAO;
import com.aurel.track.dao.SiteDAO;
import com.aurel.track.dao.StateChangeDAO;
import com.aurel.track.dao.SystemStateDAO;
import com.aurel.track.dao.TrailDAO;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.item.ItemPersisterException;
import com.aurel.track.item.budgetCost.BudgetBL;
import com.aurel.track.item.budgetCost.ComputedValueBL;
import com.aurel.track.item.budgetCost.ExpenseBL;
import com.aurel.track.item.history.HistoryBean;
import com.aurel.track.item.history.HistoryBean.HISTORY_TYPE;
import com.aurel.track.linkType.CloseDependsOnLinkType;
import com.aurel.track.linkType.ILinkType.LINK_DIRECTION;
import com.aurel.track.linkType.MsProjectLinkType;
import com.aurel.track.persist.BaseTListTypePeer;
import com.aurel.track.persist.BaseTProject;
import com.aurel.track.persist.BaseTProjectTypePeer;
import com.aurel.track.persist.BaseTPstatePeer;
import com.aurel.track.persist.BaseTStatePeer;
import com.aurel.track.persist.BaseTWorkItemPeer;
import com.aurel.track.persist.TListType;
import com.aurel.track.persist.TPstate;
import com.aurel.track.persist.TSite;
import com.aurel.track.persist.TSitePeer;
import com.aurel.track.persist.TState;
import com.aurel.track.persist.TWorkItem;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizationKeyPrefixes;
import com.aurel.track.screen.dashboard.bl.design.DashboardScreenDesignBL;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.PropertiesHelper;


/**
*
* This class manages database upgrades from previous versions
* to the actual version. This is because some operations can
* not be done with SQL scripts across many different platforms.
*
* @author Joerg Friedrich
* @version $Revision: 1795 $
*
*/
public class UpgradeDatabase {

	private static final Logger LOGGER = LogManager.getLogger(UpgradeDatabase.class);

	private static TrailDAO trailDAO = DAOFactory.getFactory().getTrailDAO();
	private static StateChangeDAO stateChangeDAO = DAOFactory.getFactory().getStateChangeDAO();
	private static BaseLineDAO baseLineDAO = DAOFactory.getFactory().getBaseLineDAO();
	private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();
	private static SiteDAO siteDAO = DAOFactory.getFactory().getSiteDAO();
	private static LinkTypeDAO linkTypeDAO = DAOFactory.getFactory().getLinkTypeDAO();
	private static SystemStateDAO systemStateDAO = DAOFactory.getFactory().getSystemStateDAO();

	// The database version for this software version
	public static final int DBVERSION = 500;

	private static transient String theVersion = "";

	public UpgradeDatabase(String _theVersion, String _theBuild) throws ServletException {
		theVersion = _theVersion;
	}

	/**
	 * This method performs the actual upgrade by reading information
	 * from the database and writing to it directly and via the Torque
	 * persistence layer. You can really screw things up if you manually
	 * mess with the database version in TSite.
	 * @throws SQLException
	 * @throws ItemPersisterException
	 * @throws TorqueException
	 */
	public void upgrade(ServletContext servletContext) throws TorqueException, SQLException, ItemPersisterException {
		LoggingConfigBL.setLevel(LOGGER, Level.INFO);
		upgrade220To300();
		upgrade300To310();
		upgrade310To320();
		upgrade320To330();
		upgrade330To340();
		upgrade340To350();
		upgrade350To370(servletContext);
		addNewReportTemplatesBy372();
		addNewReportTemplatesBy373();

		// For future releases, put additional upgrade functions
		// into method upgradeFrom370().
	}

	/**
	 * From 370 on the new upgrade code will be called from this method instead of upgrade()
	 * @param servletContext
	 * @throws SQLException
	 */
	public static void upgradeFrom370(ServletContext servletContext) throws TorqueException, SQLException {
		upgrade370To380();
		upgrade380To400();
		upgrade400To411();
		upgrade411To412();
		upgrade412To415();
		upgrade415To416();
		upgrade416To417();
		upgrade417To500();
		upgrade500To502();
		upgrade502To503();
		ApplicationStarter.getInstance().actualizePercentComplete(ApplicationStarter.DB_DATA_UPGRADE_STEP, ApplicationStarter.DATA_UPGRADE_READY_TEXT);
	}

	/*
	 * Coming from very early version 2.2.x -------------------------------------
	 */
	private void upgrade220To300() throws SQLException {
		TSiteBean siteBean = siteDAO.load1();
		if (!isNewDbVersion(siteBean, 300)) {
			return;// upgrade should not be necessary
		}
		// Insert generic project type into all projects in TPROJECT
		// that do not have an entry there already.
		try {
			List<TProjectBean> list = ProjectBL.loadAll();
			TProjectBean projectBean = ProjectBL.loadByPrimaryKey(Integer.valueOf(0));
			if (projectBean!=null) {
				list.add(projectBean);
			}
			TProjectBean prj = null;
			Iterator<TProjectBean> i = list.iterator();
			while (i.hasNext()) {
				prj = i.next();
				if ((prj != null) && (prj.getProjectType() == null)) {
					prj.setProjectType(new Integer(0));
					ProjectBL.saveSimple(prj);// prj.save();
					LOGGER.info("Adding project type to project " + prj.getLabel());
			}
		}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}


	  // Insert entries into TPSTATE if there aren't any yet
	  // This adds an entry for each list type and each state
	  // and for any project (project oid == 0). Thus we will
	  // have the same behaviour as before, but we can modify
	  // it by manually pruning the table
	  // Default states , stateflags and sort order are:
	  // 1,'opened',0,
	  // 2,'analyzed',0,2
	  // 3,'assigned',0,3
	  // 4,'suspended',1,9
	  // 5,'processing',0,4
	  // 6,'implemented',0,5
	  // 7,'integrated',2,6
	  // 8,'closed',1,8
	  // 9,'assessing',0,7
	  //
	  // Default list types are:
	  // 1,'ProblemReport',1
	  // 2,'RequirementChange',2
	  // 3,'ImplementationError',3
	  // 4,'WorkPackage',4
	  // 5,'ActionItem',5
	  // 6,'Milestone',6
	  // 7,'Risk',7
	  // 8,'Requirements',8
	  // 9,'ReleaseNotes',9

	  try {
		  List<TPstate> list = BaseTPstatePeer.doSelect(new Criteria());
		  Iterator<TPstate> i = list.iterator();
		  TPstate pstate = null;
		  if (!i.hasNext()) {  // not a single entry here
			  LOGGER.info("Adding entries to TPSTATE");
			  List<TListType> listTypeList = BaseTListTypePeer.doSelect(new Criteria());
			  Iterator<TListType> il = listTypeList.iterator();
			  TState state = null;
			  TListType listType = null;
			  while (il.hasNext()) {
				  listType = (TListType) il.next();
				  List<TState> stateList = BaseTStatePeer.doSelect(new Criteria());
				  Iterator<TState> is = stateList.iterator();
				  while (is.hasNext()) {
					  state = (TState) is.next();
					  pstate = new TPstate();
					  pstate.setTState(state);
					  pstate.setTListType(listType);
					  pstate.setProjectType(new Integer(0));
					  if ((listType.getObjectID().intValue() <= 4)
							  ||(listType.getObjectID().intValue() >= 8)) {
						  pstate.save();
					  }
					  if ((listType.getObjectID().intValue() == 5)
							  && (state.getObjectID().intValue() == 5
									  || state.getObjectID().intValue() == 6))
					  {
						  pstate.save();
					  }
					  if (((listType.getObjectID().intValue() == 5)
							  ||(listType.getObjectID().intValue() == 6)
							  ||(listType.getObjectID().intValue() == 7))
							  && (state.getObjectID().intValue() == 1
									  || state.getObjectID().intValue() == 8))
					  {
						  pstate.save();
					  }
					  System.out.print(".");
				  }
			  }
			  LOGGER.info("done");
		  }
	  }
	  catch (Exception e) {
		  LOGGER.error(e.getMessage());
		  LOGGER.error("Problem adding entries to TPSTATE");
	  }

	  // Last but not least update the TWORKITEM tables and check for
	  // null values in CLASSKEY (just to make sure, should not be any)
	  // and RELNOTICEDKEY. A RELNOTICEDKEY of null would cause
	  // problems when we extending our report queries to this field.
	  // This operation can take some time on a big database...
	  try {
		  System.err.print("Upgrading TWORKITEM table...");
		  List<TWorkItem> list = BaseTWorkItemPeer.doSelect(new Criteria());
		  Iterator<TWorkItem> i = list.iterator();
		  TWorkItem wi = null;
		  Integer reln = null;
		  Integer classn = null;
		  Integer inull = new Integer(0);
		  int count = 0;
		  while (i.hasNext()) {
			  wi = (TWorkItem) i.next();
			  reln = wi.getReleaseNoticedID();
			  classn = wi.getClassID();
			  if (reln == null) {
				  wi.setReleaseNoticedID(inull);
			  }
			  if (classn == null) {
				  wi.setClassID(inull);
			  }
			  wi.save();
			  if ((count%10) == 0) {
				  System.err.print(".");  // give some sign that we are alive
			  }
			  ++ count;
		  }
		  LOGGER.info("done");
	  }
	  catch (Exception e) {
		  LOGGER.error(e.getMessage());
		  LOGGER.error("Problem checking TWORKITEM");
	  }



	  // Finally mark this upgrade off so we don't run into it again
	  // with this database
	  LOGGER.info("Database upgrade from 220 to 300 completed.");

	  siteBean.setDbVersion("300");
	  siteDAO.save(siteBean);

  }

  /*
   * Upgrade from 30x to 31x ----------------------------
   */
  private void upgrade300To310() throws SQLException {
	  TSiteBean siteBean = siteDAO.load1();
	  if (!isNewDbVersion(siteBean, 310)) {
		  return;   // upgrade should not be necessary
	  }
	  siteBean.setDbVersion("310");
	  if (siteBean.getSmtpPort() == null || siteBean.getSmtpPort().intValue() == 0) {
		  siteBean.setSmtpPort(new Integer(25));
	  }
	  if (siteBean.getMailReceivingPort() == null || siteBean.getMailReceivingPort().intValue() == 0) {
		  siteBean.setMailReceivingPort(new Integer(110));
	  }
	  try {
		  siteDAO.save(siteBean);
	  }
	  catch (Exception e) {
		  LOGGER.error("Problem saving TSITE");
		  LOGGER.error(e.getMessage());
		  LOGGER.error("Continuing nonetheless");
	  }
	  try {
		  List<TProjectBean> list = ProjectBL.loadAll();
		  TProjectBean projectBean = ProjectBL.loadByPrimaryKey(Integer.valueOf(0));
		  if (projectBean!=null) {
			  list.add(projectBean);
		  }
		  if (list != null) {
			  Iterator<TProjectBean> it = list.iterator();
			  TProjectBean project = null;
			  while (it.hasNext()) {
				  project = it.next();
				  project.setDeleted("N");
				  if (project.getProjectType() == null) {
					  project.setProjectType(new Integer(0));
				  }
				  ProjectBL.saveSimple(project);//project.save();

			  }
		  }
	  }
	  catch (Exception e) {
		  LOGGER.error("Error when trying to update TPROJECT");
		  LOGGER.error(e.getMessage());
	  }

	  LOGGER.info("Database upgrade from 300 to 310 completed.");
  }


  private void upgrade310To320() throws SQLException {
	  TSiteBean siteBean = siteDAO.load1();
	  if (!isNewDbVersion(siteBean, 320)) {
		  return;   // upgrade should not be necessary
	  }

	  LOGGER.info("Database upgrade from 310 to 320 completed.");

	  siteBean.setDbVersion("320");
	  siteDAO.save(siteBean);
  }

  private void upgrade320To330() throws SQLException {
	  TSiteBean siteBean = siteDAO.load1();
	  if (!isNewDbVersion(siteBean, 330)) {
		  return;   // upgrade should not be necessary
	  }

	  //get all roles to migrate
	  String extendedAccessKey;
	  List<TRoleBean> roles = RoleBL.loadAll();
	  Iterator<TRoleBean> itrRoles = roles.iterator();
	  while (itrRoles.hasNext()) {
		  TRoleBean roleBean = (TRoleBean)itrRoles.next();
		  Integer accessKey = roleBean.getAccesskey();
		  extendedAccessKey = roleBean.getExtendedaccesskey();
		  if (accessKey!=null && accessKey.intValue()!=0 && (extendedAccessKey==null || extendedAccessKey.length()==0)) {
			  extendedAccessKey = migrateRoleAccessKey(accessKey.intValue());
			  roleBean.setExtendedaccesskey(extendedAccessKey);
			  RoleBL.save(roleBean);
		  }
	  }
	  LOGGER.info("Database upgrade from 320 to 330 completed.");
	  siteBean.setDbVersion("330");
	  siteDAO.save(siteBean);
  }

  private void upgrade330To340() throws SQLException {
	  TSiteBean siteBean = siteDAO.load1();
	  if (!isNewDbVersion(siteBean, 340)) {
		  return;   // upgrade should not be necessary
	  }

	  //force the warning levels for priority
	  List<TPriorityBean> priorityList = PriorityBL.loadAll();
	  if (priorityList!=null && !priorityList.isEmpty()) {
		  for (TPriorityBean priorityBean : priorityList) {
			  if (priorityBean.getWlevel()==null) {
				  priorityBean.setWlevel(new Integer(priorityBean.getObjectID().intValue()-1));
				  PriorityBL.saveSimple(priorityBean);
			  }
		  }
	  }

	  //force the warning levels for severity
	  List<TSeverityBean> severityList = SeverityBL.loadAll();
	  if (severityList!=null && !severityList.isEmpty()) {
		  for (TSeverityBean severityBean : severityList) {
			  if (severityBean.getWlevel()==null) {
				  severityBean.setWlevel(new Integer(severityBean.getObjectID().intValue()-1));
				  SeverityBL.saveSimple(severityBean);
			  }
		  }
	  }

	  //copy the content of lastEdit to the effortDate for the costs
	  List<TCostBean> costList = ExpenseBL.loadAll();
	  if (costList!=null && !costList.isEmpty()) {
		  Iterator<TCostBean> iterator = costList.iterator();
		  while (iterator.hasNext()) {
			  TCostBean costBean = (TCostBean)iterator.next();
			  if (costBean.getEffortdate()==null) {
				  costBean.setEffortdate(costBean.getLastEdit());
				  ExpenseBL.saveCostBean(costBean);
			  }
		  }
	  }

	  LOGGER.info("Database upgrade from 330 to 340 completed.");

	  siteBean.setDbVersion("340");
	  siteDAO.save(siteBean);
  }

  private void upgrade340To350() throws SQLException, ItemPersisterException, TorqueException {

	  TSiteBean siteBean = siteDAO.load1();
	  if (!isNewDbVersion(siteBean, 350)) {
		  return;   // upgrade should not be necessary
	  }

	  LOGGER.info("Performing upgrade step 340 to 350.");

	  MigrateTagReplacer tagReplacer = new MigrateTagReplacer(Locale.ENGLISH);
	  tagReplacer.setContextPath("");
	  //replace the trail descriptions
	  List<TTrailBean> trails = trailDAO.loadAll();
	  if (trails!=null) {
		  Iterator<TTrailBean> iterator = trails.iterator();
		  while (iterator.hasNext()) {
			TTrailBean trailBean = (TTrailBean) iterator.next();
			if (replaceDescriptionForHistoryBean(trailBean, tagReplacer)) {
				trailDAO.save(trailBean);
			}
		}
	  }
	  //replace the state change descriptions
	  List<TStateChangeBean> stateChanges = stateChangeDAO.loadAll();
	  if (stateChanges!=null) {
		  Iterator<TStateChangeBean> iterator = stateChanges.iterator();
		  while (iterator.hasNext()) {
			TStateChangeBean stateChangeBean = (TStateChangeBean) iterator.next();
			if (replaceDescriptionForHistoryBean(stateChangeBean, tagReplacer)) {
				stateChangeDAO.save(stateChangeBean);
			}
		}
	  }
	  //replace the base line change descriptions
	  List<TBaseLineBean> baseLineChanges = baseLineDAO.loadAll();
	  if (baseLineChanges!=null) {
		  Iterator<TBaseLineBean> iterator = baseLineChanges.iterator();
		  while (iterator.hasNext()) {
			TBaseLineBean baseLineBean = (TBaseLineBean) iterator.next();
			if (replaceDescriptionForHistoryBean(baseLineBean, tagReplacer)) {
				baseLineDAO.save(baseLineBean);
			}
		}
	  }

	  List<TWorkItemBean> allItems=workItemDAO.loadAll();
	  if (allItems!=null) {
		  Iterator<TWorkItemBean> iterator = allItems.iterator();
		  while (iterator.hasNext()) {
			TWorkItemBean workItemBean = (TWorkItemBean) iterator.next();
			if (replaceDescriptionForItemBean(workItemBean, tagReplacer)) {
				workItemDAO.save(workItemBean);
			}
		}
	  }
	  //set version control viewer  for projects. Append project in baseURL
	  List<TProjectBean> projects= ProjectBL.loadAll();
	  for (int i = 0; i < projects.size(); i++) {
		  TProjectBean prj = projects.get(i);
		  boolean useVC=(PropertiesHelper.getProperty(prj.getMoreProps(), TProjectBean.MOREPPROPS.USE_VERSION_CONTROL_PROPERTY)+"").equalsIgnoreCase("true");
		  if(useVC){
			  String viewVCBaseUrl=prj.getVersionSystemField0();
			  String viewVCProject=prj.getVersionSystemField1();
			  if(viewVCBaseUrl!=null&&viewVCProject!=null&&viewVCProject.length()>0){
				  if(!viewVCBaseUrl.endsWith("/")&&!viewVCProject.startsWith("/")){
					  viewVCBaseUrl=viewVCBaseUrl+"/";
				  }
				  viewVCBaseUrl=viewVCBaseUrl+viewVCProject;
				  prj.setVersionSystemField0(viewVCBaseUrl);
				  ProjectBL.saveSimple(prj);
			  }
		  }
	  }

	  //populates the computed values table for the existing time and cost values
	  ComputedValueBL.deleteAll();
	  saveComputedValuesBeanList(ExpenseBL.loadExpenseGroupedByWorkItem());
	  saveComputedValuesBeanList(ExpenseBL.loadExpenseGroupedByWorkItemAndPerson());
	  ComputedValueBL.computePlannedValuesToLastFromHistory(BudgetBL.loadLastPlanForWorkItems());

	  // Make sure that the next ID from the ID_TABLE for the TEXPORTTEMPLATE
	  // table begins with at least 101 for the new templates added by the customers.
	  // That to reserve the numbers till 100 for temlates supplied with the Genji application.
	  // If a customer has added some templates by version 3.4
	  // (where this problem was not taken into account)
	  // they should save/download their custom templates before installing the new version
	  // and after installing the new version upload the templates again
	  // (this time the IDs will be above 100)
	  Connection con  = Torque.getConnection(BaseTProjectTypePeer.DATABASE_NAME);
	  try {
		  int maxid = 0;
		  String sqlStmt = "SELECT MAX(OBJECTID) FROM TEXPORTTEMPLATE";

		  ResultSet rs = executeSelect(sqlStmt,con);
		  if (rs != null) {
			  maxid = rs.getInt(1) + 1;
		  }
		  if (maxid<101) {
			  maxid = 101;
			  sqlStmt = "UPDATE ID_TABLE SET NEXT_ID = " + String.valueOf(maxid)
			  + " WHERE TABLE_NAME = 'TEXPORTTEMPLATE'";
			  insertData(sqlStmt);
		  }
		  if (!isNewInstall(siteBean)) {
			  //move the existing custom (non system) templates to values over 101
			  //by 3.4 there were only 5 "system" reports
			  List<TExportTemplateBean> exportTemplateBeans = ReportBL.loadFromTo(new Integer(5), new Integer(100));
			  if (exportTemplateBeans!=null && !exportTemplateBeans.isEmpty()) {
				  Iterator<TExportTemplateBean> iterator = exportTemplateBeans.iterator();
				  while (iterator.hasNext()) {
					  //ideal would be to change the objectID, but torque does not change the objectID
					  TExportTemplateBean oldExportTemplateBean = iterator.next();
					  TExportTemplateBean newExportTemplateBean = new TExportTemplateBean();
					  newExportTemplateBean.setName(oldExportTemplateBean.getName());
					  newExportTemplateBean.setReportType(oldExportTemplateBean.getReportType());
					  newExportTemplateBean.setExportFormat(oldExportTemplateBean.getExportFormat());
					  newExportTemplateBean.setRepositoryType(oldExportTemplateBean.getRepositoryType());
					  newExportTemplateBean.setDescription(oldExportTemplateBean.getDescription());
					  newExportTemplateBean.setProject(oldExportTemplateBean.getProject());
					  newExportTemplateBean.setPerson(oldExportTemplateBean.getPerson());
					  newExportTemplateBean.setUuid(oldExportTemplateBean.getUuid());
					  Integer oldObjectID = oldExportTemplateBean.getObjectID();
					  ReportBL.delete(oldObjectID);
					  Integer newObjectID = ReportBL.saveReport(newExportTemplateBean);
					  File oldUnzippedDirectory = ReportBL.getDirTemplate(oldObjectID);
					  File newUnzippedDirectory = ReportBL.getDirTemplate(newObjectID);
					  oldUnzippedDirectory.renameTo(newUnzippedDirectory);
				  }
			  }
		  }
	  }
	  catch (Exception e) {
		  LOGGER.error("Updating the ID_TABLE for TEXPORTTEMPLATE ID failed with " + e.getMessage());
	  } finally {
		  Torque.closeConnection(con);
	  }

	  //deletes the old system templates from in the disk in order
	  //to trigger extracting the new templates from the zips found in the classpath
	  deleteOldReportTemplates();

	  LOGGER.info("Database upgrade from 340 to 350 completed.");

	  siteBean.setDbVersion("350");
	  siteDAO.save(siteBean);

  }



  private void upgrade350To370(ServletContext servletContext) throws SQLException, TorqueException {

	  TSiteBean siteBean = siteDAO.load1();
	  if (!isNewDbVersion(siteBean, 370)) {
		  upgradeFrom370(servletContext);
		  return;   // upgrade should not be necessary
	  }
	  LOGGER.info("Performing upgrade step 350 to 370.");
	  ApplicationStarter.getInstance().actualizePercentComplete(ApplicationStarter.DB_DATA_UPGRADE_STEP, ApplicationStarter.DATA_UPGRADE_TO_TEXT + "3.7");
	  new MigrateTo37(servletContext).start();
  }


  public static void upgrade370To380() throws SQLException, TorqueException {
	  TSiteBean siteBean = siteDAO.load1();
	  if (!isNewDbVersion(siteBean, 380)) {
		  return;   // upgrade should not be necessary
	  }
	  LOGGER.info("Performing upgrade step 370 to 380.");
	  ApplicationStarter.getInstance().actualizePercentComplete(ApplicationStarter.DB_DATA_UPGRADE_STEP, ApplicationStarter.DATA_UPGRADE_TO_TEXT +"3.8");
	  addNewReportTemplatesBy380();
	  Connection con  = Torque.getConnection(BaseTProjectTypePeer.DATABASE_NAME);
	  int maxid = 0;
	  String sqlStmt = "SELECT MAX(OBJECTID) FROM TLINKTYPE";
	  ResultSet rs = executeSelect(sqlStmt, con);
	  if (rs != null) {
		  maxid = rs.getInt(1) + 1;
	  }
	  if (maxid<101) {
		  maxid = 101;
		  sqlStmt = "UPDATE ID_TABLE SET NEXT_ID = " + String.valueOf(maxid)
		  + " WHERE TABLE_NAME = 'TLINKTYPE'";
		  insertData(sqlStmt);
	  }
	  Integer linkTypeID = addLinkType("close depends on", "conditioning the close", LINK_DIRECTION.RIGHT_TO_LEFT, CloseDependsOnLinkType.class.getName());
	  LocalizeBL.saveLocalizedResource("linkType.name.1.", linkTypeID, "close depends on", Locale.ENGLISH);
	  LocalizeBL.saveLocalizedResource("linkType.filterSuperset.1.", linkTypeID, "conditioning the close", Locale.ENGLISH);
	  LocalizeBL.saveLocalizedResource("linkType.name.1.", linkTypeID, "Schlieen hngt ab von", Locale.GERMAN);
	  LocalizeBL.saveLocalizedResource("linkType.filterSuperset.1.", linkTypeID, "zuerst geschlossen sein mssen", Locale.GERMAN);


	  addUnscheduledReleaseState();
	  siteBean.setDbVersion("380");
	  siteDAO.save(siteBean);
	  Torque.closeConnection(con);
  }

  static Integer addLinkType(String name,
		  String leftToRight, Integer direction, String linkTypePlugin) {
	  TLinkTypeBean linkTypeBean = new TLinkTypeBean();
	  linkTypeBean.setName(name);
	  linkTypeBean.setLeftToRightFirst(leftToRight);
	  linkTypeBean.setLinkDirection(direction);
	  linkTypeBean.setLinkTypePlugin(linkTypePlugin);
	  return linkTypeDAO.save(linkTypeBean);
  }

  private static void addUnscheduledReleaseState() {
	  TSystemStateBean systemStateBean = new TSystemStateBean();
	  systemStateBean.setLabel("unscheduled");
	  systemStateBean.setStateflag(TSystemStateBean.STATEFLAGS.NOT_PLANNED);
	  systemStateBean.setEntityflag(TSystemStateBean.ENTITYFLAGS.RELEASESTATE);
	  systemStateBean.setSortorder(Integer.valueOf(10));
	  Integer systemStateID = systemStateDAO.save(systemStateBean);
	  String fieldName = LocalizationKeyPrefixes.SYSTEM_STATUS_KEY_PREFIX + TSystemStateBean.ENTITYFLAGS.RELEASESTATE;
	  LocalizeBL.saveLocalizedResource(fieldName, systemStateID, "unscheduled", Locale.ENGLISH);
	  LocalizeBL.saveLocalizedResource(fieldName, systemStateID, "nicht geplannt", Locale.GERMAN);
  }

  /**
   * Adds the new report templates into the database
   * and
   */
  private void addNewReportTemplatesBy372() {
	  try {
		  TSite site = TSitePeer.load();
		  String version = site.getTrackVersion();
		  if (version==null || version.compareTo("3.7.2")<=0) {
			  List<TExportTemplateBean> newReportTemplates = ReportBL.loadFromTo(7, 11);
			  if (newReportTemplates == null || newReportTemplates.isEmpty()) {
				  String sqlStmt;
				  Integer templateID;
				  TExportTemplateBean exportTemplateBean;
				  exportTemplateBean = prepareExportTemplateBean(/*new Integer(8),*/
						  "Detailed expense with person, project, account", "Detailed expense report with project, date, person");
				  templateID = ReportBL.saveReport(exportTemplateBean);
				  sqlStmt = "UPDATE TEXPORTTEMPLATE SET OBJECTID = " + String.valueOf(8) + " WHERE OBJECTID = " + templateID;
				  insertData(sqlStmt);

				  exportTemplateBean = prepareExportTemplateBean(/*new Integer(9),*/
						  "Grouped expense by person, project, account", "Expense report grouped by person, project, account");
				  templateID = ReportBL.saveReport(exportTemplateBean);
				  sqlStmt = "UPDATE TEXPORTTEMPLATE SET OBJECTID = " + String.valueOf(9) + " WHERE OBJECTID = " + templateID;
				  insertData(sqlStmt);

				  exportTemplateBean = prepareExportTemplateBean(/*new Integer(10),*/
						  "Grouped expense by project, costcenter, account, person", "Expense report grouped by project, costcenter, account, person");
				  templateID = ReportBL.saveReport(exportTemplateBean);
				  sqlStmt = "UPDATE TEXPORTTEMPLATE SET OBJECTID = " + String.valueOf(10) + " WHERE OBJECTID = " + templateID;
				  insertData(sqlStmt);

				  exportTemplateBean = prepareExportTemplateBean(/*new Integer(11),*/
						  "Detailed expense with subproject and item", "Detailed expense report including subproject and item");
				  templateID = ReportBL.saveReport(exportTemplateBean);
				  sqlStmt = "UPDATE TEXPORTTEMPLATE SET OBJECTID = " + String.valueOf(11) + " WHERE OBJECTID = " + templateID;
				  insertData(sqlStmt);

				  LOGGER.info("Added new report templates in databse for 3.7.2");
			  }
		  }
	  }
	  catch(Exception e) {
		  LOGGER.error("Adding new templates in database for 3.7.2 failed with " + e.getMessage());
	  }

  }

  /**
   * Adds the new report templates into the database
   * and
   */
  private void addNewReportTemplatesBy373() {
	  try {
		  TSite site = TSitePeer.load();
		  String theVersion = site.getTrackVersion();
		  if (theVersion==null || theVersion.compareTo("3.7.3")<0) {
			  List<TExportTemplateBean> newReportTemplates = ReportBL.loadFromTo(11, 14);
			  if (newReportTemplates == null || newReportTemplates.isEmpty()) {
				  String sqlStmt;
				  Integer templateID;
				  TExportTemplateBean exportTemplateBean;
				  exportTemplateBean = prepareExportTemplateBean(/*new Integer(12),*/
						  "Filtered history", "Detailed report with filtered history");
				  templateID = ReportBL.saveReport(exportTemplateBean);
				  sqlStmt = "UPDATE TEXPORTTEMPLATE SET OBJECTID = " + String.valueOf(12) + " WHERE OBJECTID = " + templateID;
				  insertData(sqlStmt);

				  exportTemplateBean = prepareExportTemplateBean(/*new Integer(13),*/
						  "Earned value chart", "Chart with planned, actual and earned values");
				  templateID = ReportBL.saveReport(exportTemplateBean);
				  sqlStmt = "UPDATE TEXPORTTEMPLATE SET OBJECTID = " + String.valueOf(13) + " WHERE OBJECTID = " + templateID;
				  insertData(sqlStmt);

				  exportTemplateBean = prepareExportTemplateBean(/*new Integer(14),*/
						  "Opened vs. closed chart", "Chart with number of issues opened vs. closed");
				  templateID = ReportBL.saveReport(exportTemplateBean);
				  sqlStmt = "UPDATE TEXPORTTEMPLATE SET OBJECTID = " + String.valueOf(14) + " WHERE OBJECTID = " + templateID;
				  insertData(sqlStmt);

				  LOGGER.info("Added new report templates in database for 3.7.3");
			  }
		  }
	  }
	  catch(Exception e) {
		  LOGGER.error("Adding new templates in database for 3.7.3 failed with " + e.getMessage());
	  }

  }

  /**
   * Adds the new report templates into the database
   * and
   */
  private static void addNewReportTemplatesBy380() {
	  try {
		  TExportTemplateBean exportTemplateBean = ReportBL.loadByPrimaryKey(15);
		  if (exportTemplateBean == null) {
			  String sqlStmt;
			  Integer templateID;
			  exportTemplateBean = prepareExportTemplateBean("Status over time", "Status ove time table");
			  templateID = ReportBL.saveReport(exportTemplateBean);
			  sqlStmt = "UPDATE TEXPORTTEMPLATE SET OBJECTID = " + String.valueOf(15) + " WHERE OBJECTID = " + templateID;
			  insertData(sqlStmt);
			  LOGGER.info("Added new report templates in database for 3.8.0");
		  }
	  }
	  catch(Exception e) {
		  LOGGER.error("Adding new templates in database for 3.8.0 failed with " + e.getMessage());
	  }

  }

  private static TExportTemplateBean prepareExportTemplateBean(String name, String description) {
	  TExportTemplateBean exportTemplateBean;
	  exportTemplateBean = new TExportTemplateBean();
	  exportTemplateBean.setName(name);
	  exportTemplateBean.setExportFormat("pdf");
	  exportTemplateBean.setRepositoryType(new Integer(2));
	  exportTemplateBean.setDescription(description);
	  exportTemplateBean.setPerson(new Integer(1));
	  exportTemplateBean.setReportType("Jasper Report");
	  return exportTemplateBean;
  }

  private static void upgrade380To400() throws SQLException {
	  TSiteBean siteBean = siteDAO.load1();
	  if (!isNewDbVersion(siteBean, 400)) {
		  return;   // upgrade should not be necessary
	  }
	  LOGGER.info("Database upgrade from 380 to 400 started...");
	  ApplicationStarter.getInstance().actualizePercentComplete(ApplicationStarter.DB_DATA_UPGRADE_STEP, ApplicationStarter.DATA_UPGRADE_TO_TEXT+"4.0");
	  Migrate380To400.migrate380To400();
	  LOGGER.info("Database upgrade from 380 to 400 completed.");
	  siteBean.setDbVersion("400");
	  siteDAO.save(siteBean);
  }

  private static void upgrade400To411() throws SQLException {
	  TSiteBean siteBean = siteDAO.load1();
	  if (!isNewDbVersion(siteBean, 411)) {
		  return;// upgrade should not be necessary
	  }
	  LOGGER.info("Database upgrade from 400 to 411 started...");
	  ApplicationStarter.getInstance().actualizePercentComplete(ApplicationStarter.DB_DATA_UPGRADE_STEP, ApplicationStarter.DATA_UPGRADE_TO_TEXT+"4.1");
	  PredefinedQueryBL.addHardcodedFilters();
	  Migrate400To410.inverseLinkDirections(true);
	  Migrate380To400.migrateFullTrigger();
	  LOGGER.info("Database upgrade from 400 to 411 completed.");
	  siteBean.setDbVersion("411");
	  siteDAO.save(siteBean);
  }

  private static void upgrade411To412() throws SQLException {
	  TSiteBean siteBean = siteDAO.load1();
	  if (!isNewDbVersion(siteBean, 412)) {
		  return;// upgrade should not be necessary
	  }
	  LOGGER.info("Database upgrade from 411 to 412 started...");
	  ApplicationStarter.getInstance().actualizePercentComplete(ApplicationStarter.DB_DATA_UPGRADE_STEP, ApplicationStarter.DATA_UPGRADE_TO_TEXT+"4.12");
	  Migrate411To412.addTaskIsMilestone();
	  if (!(ApplicationBean.getInstance().getAppType() == ApplicationBean.APPTYPE_DESK)
		  && !ApplicationBean.getInstance().isGenji()) {
	  }
	  //forgot to add it for 400 To 410
	  PredefinedQueryBL.addWatcherFilter();
	  LOGGER.info("Database upgrade from 411 to 412 completed.");
	  siteBean.setDbVersion("412");
	  siteDAO.save(siteBean);
  }

  private static void upgrade412To415() throws SQLException {
	  TSiteBean siteBean = siteDAO.load1();
	  if (!isNewDbVersion(siteBean, 415)) {
		  return;// upgrade should not be necessary
	  }
	  LOGGER.info("Database upgrade from 412 to 415 started...");
	  ApplicationStarter.getInstance().actualizePercentComplete(ApplicationStarter.DB_DATA_UPGRADE_STEP, ApplicationStarter.DATA_UPGRADE_TO_TEXT+"4.12");
	  LOGGER.info("Database upgrade from 412 to 415 completed.");
	  siteBean.setDbVersion("415");
	  siteDAO.save(siteBean);
  }

  private static void upgrade415To416() throws SQLException {
	  TSiteBean siteBean = siteDAO.load1();
	  if (!isNewDbVersion(siteBean, 416)) {
		  return;// upgrade should not be necessary
	  }
	  LOGGER.info("Database upgrade from 415 to 416 started...");
	  if (!(ApplicationBean.getInstance().getAppType() == ApplicationBean.APPTYPE_DESK)
			  && !ApplicationBean.getInstance().isGenji()) {
		  	Migrate415To416.addNewItemTypes();
	  }
	  ApplicationStarter.getInstance().actualizePercentComplete(ApplicationStarter.DB_DATA_UPGRADE_STEP, ApplicationStarter.DATA_UPGRADE_TO_TEXT+"4.15");
	  LOGGER.info("Database upgrade from 415 to 416 completed.");
	  siteBean.setDbVersion("416");
	  siteDAO.save(siteBean);
  }

  private static void upgrade416To417() throws SQLException {
	  TSiteBean siteBean = siteDAO.load1();
	  if (!isNewDbVersion(siteBean, 417)) {
		  return;// upgrade should not be necessary
	  }
	  LOGGER.info("Database upgrade from 416 to 417 started...");
	  Migrate416To417.addDeletedBasket();
	  ApplicationStarter.getInstance().actualizePercentComplete(ApplicationStarter.DB_DATA_UPGRADE_STEP, ApplicationStarter.DATA_UPGRADE_TO_TEXT+"4.15");
	  LOGGER.info("Database upgrade from 416 to 417 completed.");
	  siteBean.setDbVersion("417");
	  siteDAO.save(siteBean);
  }

  private static void upgrade417To500() throws SQLException {
	  TSiteBean siteBean = siteDAO.load1();
	  if (!isNewDbVersion(siteBean, 500)) {
		  return;// upgrade should not be necessary
	  }
	  LOGGER.info("Database upgrade from 416 to 500 started...");
	  Migrate417To500.addWorkflowPerson();
	  Migrate417To500.migrateWorkflows();
	  Migrate417To500.setTypeFlagsForItemTypes();
	  DashboardScreenDesignBL.getInstance().checkAndCreateClientDefaultCockpit();
	  ApplicationStarter.getInstance().actualizePercentComplete(ApplicationStarter.DB_DATA_UPGRADE_STEP, ApplicationStarter.DATA_UPGRADE_TO_TEXT+"5.00");
	  LOGGER.info("Database upgrade from 416 to 500 completed.");
	  siteBean.setDbVersion("500");
	  siteDAO.save(siteBean);
  }

  private static void upgrade500To502() throws SQLException {
	  TSiteBean siteBean = siteDAO.load1();
	  if (!isNewDbVersion(siteBean, 502)) {
		  return;// upgrade should not be necessary
	  }
	  LOGGER.info("Database upgrade from 500 to 501 started...");
	  UserLevelBL.migrateFromProperyFileToDatabase(null);
	  Migrate400To410.inverseLinkDirections(false);
	  Migrate500To502.addInlineLinkedLinkType();
	  LOGGER.info("Database upgrade from 500 to 501 completed.");
	  siteBean.setDbVersion("502");
	  siteDAO.save(siteBean);
  }

  private static void upgrade502To503() throws SQLException {
	  TSiteBean siteBean = siteDAO.load1();
	  if (!isNewDbVersion(siteBean, 503)) {
		  return;// upgrade should not be necessary
	  }
	  LOGGER.info("Database upgrade from 502 to 503	started...");
	  SiteConfigBL.setDefaultPrefixToWorkspaceIfNotExists();
	  Migrate502To503.changeDateFieldNames();
	  Migrate502To503.addDurations();
	  LOGGER.info("Database upgrade from 502 to 503 completed.");
	  siteBean.setDbVersion("503");
	  siteDAO.save(siteBean);
  }


  /**
   * Populates the computed values table for the existing time and cost values
   * @param computedValuesBeanList
   */
  private void saveComputedValuesBeanList(List<TComputedValuesBean> computedValuesBeanList) {
	if (computedValuesBeanList!=null && !computedValuesBeanList.isEmpty()) {
		Iterator<TComputedValuesBean> iterator = computedValuesBeanList.iterator();
		while (iterator.hasNext()) {
			TComputedValuesBean computedValuesBean = iterator.next();
			ComputedValueBL.save(computedValuesBean);
		}
	}
  }

  /**
   * Replaces the description with the HTML description
   * @param historyBean
   * @param tagReplacer
   * @throws SQLException
 * @throws TorqueException
   */
  	private boolean replaceDescriptionForHistoryBean(HistoryBean historyBean, MigrateTagReplacer tagReplacer) throws SQLException, TorqueException {
	  	Integer workItemID = historyBean.getWorkItemID();
	  	TProjectBean projectBean = ProjectBL.loadByWorkItemKey(workItemID);
	  	String originalDescription = historyBean.getDescription();
	  	if (originalDescription!=null && originalDescription.length()>0) {
	  		String replacedDescription = tagReplacer.processSquareBracketsTags(
	  				tagReplacer.replace(originalDescription, BaseTProject.createTProject(projectBean)));
	  		if (EqualUtils.notEqual(originalDescription, replacedDescription)) {
	  			if (replacedDescription!=null && replacedDescription.length()>ApplicationBean.getInstance().getCommentMaxLength()) {
	  				LOGGER.warn("History description too long ("+replacedDescription.length()+") for item:"+workItemID+" type:"+getHistoryType(historyBean.getType())+" historyID:"+historyBean.getObjectID());
	  				LOGGER.warn("\n******************************Original description******************************\n");
	  				LOGGER.warn(originalDescription);
	  				LOGGER.warn("\n******************************Replaced description******************************\n");
	  				replacedDescription = replacedDescription.substring(0, ApplicationBean.getInstance().getCommentMaxLength()-1);
	  				LOGGER.warn(replacedDescription);
	  			}
	  			historyBean.setDescription(replacedDescription);
	  			return true;
	  		}
	  	}
	  	return false;
  }
  private String getHistoryType(int type){
	  switch (type) {
	  case HISTORY_TYPE.COMMON_HISTORYVALUES:
		return "HISTORYVALUES";
	  case HISTORY_TYPE.TRAIL:
			return "TRAIL";
	  case HISTORY_TYPE.BASELINE_CHANGE:
			return "BASELINE_CHANGE";
	  case HISTORY_TYPE.STATE_CHANGE:
			return "STATE_CHANGE";
	  case HISTORY_TYPE.PLANNED_VALUE_CHANGE:
			return "PLANNED_VALUE_CHANGE";
	  case HISTORY_TYPE.BUDGET_CHANGE:
			return "BUDGET_CHANGE";
	  case HISTORY_TYPE.REMAINING_PLAN_CHANGE:
			return "ESTIMATED_BUDGET_CHANGE";
	  case HISTORY_TYPE.COST:
			return "COST";
	  }
	  return "HISTORYVALUES";
  }
  	/**
	 * Replaces the description with the HTML description
	 *
	 * @param historyBean
	 * @param tagReplacer
	 * @throws SQLException
  	 * @throws TorqueException
	 */
	private boolean replaceDescriptionForItemBean(TWorkItemBean workItemBean,
			MigrateTagReplacer tagReplacer) throws SQLException, TorqueException {
		Integer workItemID = workItemBean.getObjectID();
		TProjectBean projectBean = ProjectBL.loadByWorkItemKey(workItemID);
		String originalDescription = workItemBean.getDescription();
		if (originalDescription != null && originalDescription.length() > 0) {
			String replacedDescription = tagReplacer
					.processSquareBracketsTags(tagReplacer.replace(
							originalDescription, BaseTProject
									.createTProject(projectBean)));
			if(!replacedDescription.startsWith("<p>")){
				replacedDescription="<p>"+replacedDescription+"</p>";
			}
			if (EqualUtils.notEqual(originalDescription, replacedDescription)) {
				if (replacedDescription!=null && replacedDescription.length()>ApplicationBean.getInstance().getCommentMaxLength()) {
					LOGGER.warn("Item description too long for item:"+workItemID);
	  				LOGGER.warn("\n*****************************************************************\n");
	  				LOGGER.warn(originalDescription);
	  				LOGGER.warn("\n*****************************************************************\n");
					replacedDescription = replacedDescription.substring(0, ApplicationBean.getInstance().getCommentMaxLength()-1);
	  			}
				workItemBean.setDescription(replacedDescription);
				return true;
			}
		}
		return false;
	}

	/**
	 * Deletes the old system templates from in the disk in order
	 * to trigger extracting the new templates from the zips found in the classpath
	 *
	 */
	private void deleteOldReportTemplates(){
		List<TExportTemplateBean> templatesList = ReportBL.getAllTemplates();
		for (int i = 0; i < templatesList.size(); i++) {
			TExportTemplateBean templateBean = (TExportTemplateBean) templatesList.get(i);
			File f=ReportBL.getDirTemplate(templateBean.getObjectID());
			if(f.exists()){
				NumberFormat nf = new DecimalFormat("00000");
				String dbId = nf.format(templateBean.getObjectID());
				String reportTemplateName=ReportBL.RESOURCE_PATH+"/"+ReportBL.EXPORT_TEMPLATE_PREFIX+dbId+".zip";
				InputStream is=StartServlet.class.getClassLoader().getResourceAsStream(reportTemplateName);
				//delete only those templates which has corresponding zip file in the classpath
				if (is==null) {
					//zip not found in the classpath: nothing to expand and copy
					continue;
				} else {
					try {
						//delete the system template to trigger the copy of the newer template version
						if (f.isDirectory()) {
							deleteDirectory(f);
						} else {
							f.delete();
						}
					} catch (Exception e) {
						LOGGER.error("Deleting the old system template for template number " +
								templateBean.getObjectID() + " failed with " + e.getMessage(), e);
					}
				}
			}
		}

	}

	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
		  File[] files = path.listFiles();
		  for(int i=0; i<files.length; i++) {
			 if(files[i].isDirectory()) {
			   deleteDirectory(files[i]);
			 }
			 else {
			   files[i].delete();
			 }
		  }
		}
		return path.delete();
	}


  private String migrateRoleAccessKey(int accessKey) {
	  	int thousand;	// close
		int hundred;	// read
		int ten;		// modify
		int one;		// create
	  	StringBuffer extendedAccessKey = new StringBuffer();
	  	//create the string buffer with no rights
	  	//-2 because at this time we have the last two permission flags not yet defined
	  	for (int i=0; i<AccessBeans.NUMBER_OF_ACCESS_FLAGS; i++) {
	  		extendedAccessKey.append(AccessBeans.OFFVALUE);
	  	}
	  	thousand = accessKey/1000;//close
	  	switch(thousand) {
			case 1:
					extendedAccessKey.setCharAt(AccessBeans.AccessFlagIndexes.CLOSETASKIFMANAGERORORIGINATOR, AccessBeans.ONVALUE);
					extendedAccessKey.setCharAt(AccessBeans.AccessFlagIndexes.CLOSETASKIFRESPONSIBLE, AccessBeans.ONVALUE);
					break;
			case 2:
					extendedAccessKey.setCharAt(AccessBeans.AccessFlagIndexes.CLOSETASKIFMANAGERORORIGINATOR, AccessBeans.ONVALUE);
					break;
			default:
					extendedAccessKey.setCharAt(AccessBeans.AccessFlagIndexes.CLOSEANYTASK, AccessBeans.ONVALUE);
					break;
		}
	  	switch(accessKey%1000) {
			case 999:
				extendedAccessKey.setCharAt(AccessBeans.AccessFlagIndexes.PROJECTADMIN, AccessBeans.ONVALUE);
				extendedAccessKey.setCharAt(AccessBeans.AccessFlagIndexes.MANAGER, AccessBeans.ONVALUE);
				extendedAccessKey.setCharAt(AccessBeans.AccessFlagIndexes.READANYTASK, AccessBeans.ONVALUE);
				extendedAccessKey.setCharAt(AccessBeans.AccessFlagIndexes.MODIFYANYTASK, AccessBeans.ONVALUE);
				extendedAccessKey.setCharAt(AccessBeans.AccessFlagIndexes.CREATETASK, AccessBeans.ONVALUE);
				break;
			case 777:
				extendedAccessKey.setCharAt(AccessBeans.AccessFlagIndexes.MANAGER, AccessBeans.ONVALUE);
				extendedAccessKey.setCharAt(AccessBeans.AccessFlagIndexes.READANYTASK, AccessBeans.ONVALUE);
				extendedAccessKey.setCharAt(AccessBeans.AccessFlagIndexes.MODIFYANYTASK, AccessBeans.ONVALUE);
				extendedAccessKey.setCharAt(AccessBeans.AccessFlagIndexes.CREATETASK, AccessBeans.ONVALUE);
				break;
			case 770:
				extendedAccessKey.setCharAt(AccessBeans.AccessFlagIndexes.MANAGER, AccessBeans.ONVALUE);
				extendedAccessKey.setCharAt(AccessBeans.AccessFlagIndexes.CREATETASK, AccessBeans.ONVALUE);
				break;
			case 666:
				extendedAccessKey.setCharAt(AccessBeans.AccessFlagIndexes.RESPONSIBLE, AccessBeans.ONVALUE);
				extendedAccessKey.setCharAt(AccessBeans.AccessFlagIndexes.READANYTASK, AccessBeans.ONVALUE);
				extendedAccessKey.setCharAt(AccessBeans.AccessFlagIndexes.MODIFYANYTASK, AccessBeans.ONVALUE);
				extendedAccessKey.setCharAt(AccessBeans.AccessFlagIndexes.CREATETASK, AccessBeans.ONVALUE);
				break;
			case 660:
				extendedAccessKey.setCharAt(AccessBeans.AccessFlagIndexes.RESPONSIBLE, AccessBeans.ONVALUE);
				extendedAccessKey.setCharAt(AccessBeans.AccessFlagIndexes.CREATETASK, AccessBeans.ONVALUE);
				break;
			default:
				// pattern: read modify create
				hundred = (accessKey-thousand*1000)/100;			   // read
				ten	 = (accessKey-thousand*1000-hundred*100)/10;	// modify
				one	 = accessKey-thousand*1000-hundred*100-ten*10;  // create
				if (hundred == 2) {
					extendedAccessKey.setCharAt(AccessBeans.AccessFlagIndexes.READANYTASK, AccessBeans.ONVALUE);
				}
				if (ten==2) {
					extendedAccessKey.setCharAt(AccessBeans.AccessFlagIndexes.MODIFYANYTASK, AccessBeans.ONVALUE);
					//extendedAccessKey.setCharAt(AccessBeans.AccessFlagMigrationIndexes.ADDMODIFYDELETEDUEDATES, AccessBeans.ONVALUE);
				}
				if (one==1) {
					extendedAccessKey.setCharAt(AccessBeans.AccessFlagIndexes.CREATETASK, AccessBeans.ONVALUE);
				}
				break;
		}
	  	extendedAccessKey.setCharAt(AccessBeans.AccessFlagIndexes.CONSULTANT, AccessBeans.ONVALUE);
	  	extendedAccessKey.setCharAt(AccessBeans.AccessFlagIndexes.INFORMANT, AccessBeans.ONVALUE);
		return extendedAccessKey.toString();
  }

  private static void insertData(String sqlStmt) throws TorqueException, SQLException {
	  Connection db = null;
	  try
	  {
		  db = Torque.getConnection(BaseTProjectTypePeer.DATABASE_NAME);
		  // it's the same name for all tables here, so we don't care
		  Statement stmt = db.createStatement();
		  stmt.executeUpdate(sqlStmt);
	  }
	  finally
	  {
		  Torque.closeConnection(db);
	  }
  }



  private static ResultSet executeSelect(String sqlStmt, Connection db) {

	  ResultSet rs = null;
	  // Connection db = null;
	  try
	  {
		  // it's the same name for all tables here, so we don't care
		  Statement stmt = db.createStatement();
		  rs = stmt.executeQuery(sqlStmt);
		  if (rs.next() == false) rs = null;
	   }
	  catch (Exception e) {
		  LOGGER.debug(ExceptionUtils.getStackTrace(e));
	  }
	  return rs;
  }


  private static boolean isNewDbVersion(TSiteBean siteBean, int theDbVersion) throws SQLException {
	  if (siteBean == null) {
		  throw new SQLException("Could not load site object");
	  }
	  Integer dbVersion = new Integer(0);
  	  if (siteBean.getDbVersion() != null || !"".equals(siteBean.getDbVersion())) {
  		try {
  		   dbVersion = new Integer(siteBean.getDbVersion());
  		}
  		catch (Exception e) {
  		  dbVersion = new Integer(0);
  		}
  	  }
	  if ( dbVersion.intValue() >= theDbVersion ) {
		 return false;			   // upgrade should not be necessary
	  }
  return true;
}


  private boolean isNewInstall(TSiteBean siteBean) {
	  //it means actually new Genji installation or upgrade on a 3.7.2 after the first start
	  //(due to a bug which resulted in overwriting the trackVersion field of the siteBean
	  //in the UpdateDatabase code to null after a new 3.7.2 install)
	  return siteBean.getTrackVersion()==null;
  }

  public static boolean isNewVersion() {
		TSite site = TSitePeer.load();
		if (site == null) {
			return true;
		}

		String tversion = site.getTrackVersion();   // e.g. 3.7.3

		return (tversion != null && !"".equals(tversion.trim()) && tversion.trim().equals(theVersion.trim()));
  }

}
