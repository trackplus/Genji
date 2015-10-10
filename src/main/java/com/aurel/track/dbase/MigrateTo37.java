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


package com.aurel.track.dbase;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.map.TableMap;
import org.apache.torque.util.Criteria;

import com.aurel.track.admin.customize.treeConfig.screen.ActionBL;
import com.aurel.track.admin.server.logging.LoggingConfigBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TBaseLineBean;
import com.aurel.track.beans.TFieldChangeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TPersonBean.PERSON_STATUS;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.beans.TScreenConfigBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.beans.TStateChangeBean;
import com.aurel.track.beans.TTrailBean;
import com.aurel.track.dao.BaseLineDAO;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ReportLayoutDAO;
import com.aurel.track.dao.ScreenConfigDAO;
import com.aurel.track.dao.ScreenDAO;
import com.aurel.track.dao.SiteDAO;
import com.aurel.track.dao.StateChangeDAO;
import com.aurel.track.dao.TrailDAO;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.constants.ValueType;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.PropertiesHelper;

/**
 * Migrate the existing history data from TTrail, TBaseLine and TStateChange into
 * the THistoryTransaction and TFieldChange table
 * @author Tamas
 *
 */
public class MigrateTo37 extends Thread {
	private static final Logger LOGGER = LogManager.getLogger(MigrateTo37.class);
	private static TrailDAO trailDAO = DAOFactory.getFactory().getTrailDAO();
	private static StateChangeDAO stateChangeDAO = DAOFactory.getFactory().getStateChangeDAO();
	private static BaseLineDAO baseLineDAO = DAOFactory.getFactory().getBaseLineDAO();	
	private static ScreenConfigDAO screenConfigDAO = DAOFactory.getFactory().getScreenConfigDAO();
	private static ScreenDAO screenDAO = DAOFactory.getFactory().getScreenDAO();
	private static ReportLayoutDAO reportLayoutDAO = DAOFactory.getFactory().getReportLayoutDAO();
	private static SiteDAO siteDAO = DAOFactory.getFactory().getSiteDAO();
	private ServletContext servletContext;
	
	public MigrateTo37(ServletContext servletContext) {
		super();
		this.servletContext = servletContext;
		LoggingConfigBL.setLevel(LOGGER, Level.INFO);
	}

	@Override
	public void run() {
		//ApplicationBean appBean = ApplicationBean.getApplicationBean();
		boolean wasMaintenance = false;
		TSiteBean siteBean = siteDAO.load1(); // update the siteBean, it may be outdated by now...
		String userMsg = siteBean.getUserMessage(); /*appBean.getUserMsg();*/
		Boolean userMessageActiv = siteBean.getUserMessageActiv();
		if (siteBean.getOpState()==null || ApplicationBean.OPSTATE_RUNNING.equals(siteBean.getOpState()/*appBean.getOpState()*/)) {
			//appBean.setOpState(ApplicationBean.OPSTATE_MAINTENNANCE);
			siteBean.setOpState(ApplicationBean.OPSTATE_MAINTENNANCE);
			//appBean.setHasUserMsg(true);
			/*appBean.setUserMsg(LocalizeUtil.getLocalizedTextFromApplicationResources(
					"logon.err.migration", Locale.getDefault()));*/
			siteBean.setUserMessage(LocalizeUtil.getLocalizedTextFromApplicationResources(
					"logon.err.migration", Locale.getDefault()));
			siteBean.setUserMessageActiv(Boolean.TRUE);
			siteDAO.save(siteBean);
		} else {
			wasMaintenance = true;
		}
		LOGGER.info("Database upgrade from 350 to 370 start...");
		
		
		String strHistoryEntity = siteBean.getHistoryEntity();
		Integer historyEntity = null;
		if (strHistoryEntity!=null && !"".equals(strHistoryEntity)) {
			historyEntity = new Integer(strHistoryEntity);
		}
		if (historyEntity==null) {
			//no reorganize history was started and broken
			setUUIDs();
			removeCommentAction();
			updateReportLayout();
			setDisabledAndUserLevel();
			//extendPermissionRights();
		}
		reorganizeHistory(/*siteBean*/);
		LOGGER.info("Database upgrade from 350 to 370 completed.");
		System.out.println("");
		Map<Integer, Object> siteBeanValues = new HashMap<Integer, Object>();
		siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.DBVERSION, "370");
		siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.HISTORYENTITY, null);
		siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.HISTORYMIGRATIONID, null);
		siteDAO.loadAndSaveSynchronized(siteBeanValues);

		if (servletContext!=null) {
			TSiteBean appSiteBean = (TSiteBean)servletContext.getAttribute("SITECONFIG");
			if (appSiteBean!=null) {
				appSiteBean.setDbVersion("370");
			}
		}
		//if it was in maintenance state it remains in maintenance  
		if (!wasMaintenance) {
			siteBean = siteDAO.load1();
			//appBean.setOpState(ApplicationBean.OPSTATE_RUNNING);
			siteBean.setOpState(ApplicationBean.OPSTATE_RUNNING);
			//appBean.setUserMsg(userMsg);
			siteBean.setUserMessage(userMsg);
			//appBean.setHasUserMsg(false);
			siteBean.setUserMessageActiv(userMessageActiv);
			siteDAO.save(siteBean);
		}
		try {
			UpgradeDatabase.upgradeFrom370(servletContext);
		} catch (Exception e) {
			LOGGER.error("Upgrading from 370 faield with " + e.getMessage(), e);
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
	}
	
	/**
	 * Create universally unique identifiers first time. This may take some time
	 * on a large database.
	 */
	private static void setUUIDs() {
		try {
			DAOFactory daof = DAOFactory.getFactory();
			Method m[] = daof.getClass().getDeclaredMethods();
			for (int i = 0; i < m.length; ++i) {
				Object obj = null;
				try {
					obj = m[i].invoke(daof);
				} catch (Exception e) {
					LOGGER.debug("Calling the method " + m[i].getName()+ " failed with " + e.getMessage(), e);
					continue;
				}
				Class objClass = obj.getClass().getSuperclass();  // this could be a BasePeer object
				Method mtablemap = null;
				try {
					mtablemap = objClass.getMethod("getTableMap", (Class[])null);
					TableMap tbMap = (TableMap) mtablemap.invoke(objClass, (Object[])null);
					if (tbMap.containsColumn("TPUUID")) {
						LOGGER.info("Processing table " + tbMap.getName() + " via " + objClass.getName());
						Method doSelect = objClass.getMethod("doSelect", Criteria.class);
						List list = (List) doSelect.invoke(obj, new Criteria());
						LOGGER.info("Processing " + list.size() + " entries: ");
						Iterator it = list.iterator();
						int pk = 0;
						while (it.hasNext()) {
							Object daoBean = it.next();
							Class daoBeanClass = daoBean.getClass().getSuperclass();
							Method msetUuid = daoBeanClass.getMethod("setUuid", String.class);
							msetUuid.invoke(daoBean, UUID.randomUUID().toString());
							Method doUpdate = objClass.getMethod("doUpdate", daoBean.getClass());
							try {
								doUpdate.invoke(null, daoBean);
							}	catch (Exception ex) {
								LOGGER.error(ex.getMessage()); // forget it...
								LOGGER.error(ExceptionUtils.getStackTrace(ex));
							}
							++pk;
							if (pk > 79) {
								pk = 0;
							  	System.out.println(".");
							} else {
								System.out.print(".");
							}
						}
						System.out.println();
					}
				} catch (NoSuchMethodException nsme) {
					// This happens once for com.aurel.track.dao.torque.DAOFactoryTorque
				} catch (Exception ex) {
					LOGGER.error(ex.getMessage()); // forget it...
					LOGGER.error(ExceptionUtils.getStackTrace(ex));
				}
			}
		} catch (Exception e){
			LOGGER.error("Serious error when trying to create UUIDs: " + e.getMessage(), e);
			System.err.println(ExceptionUtils.getStackTrace(e));
		}
	}

	private Connection getConnection(){
		try {
			PropertiesConfiguration tcfg = new PropertiesConfiguration();
			tcfg = HandleHome.getTorqueProperties(servletContext, false);
			String jdbcURL = (String)tcfg.getProperty("torque.dsfactory.track.connection.url");
			String jdbcUser = (String)tcfg.getProperty("torque.dsfactory.track.connection.user");
			String jdbcPassword = (String)tcfg.getProperty("torque.dsfactory.track.connection.password");
			String jdbcDriver = (String)tcfg.getProperty("torque.dsfactory.track.connection.driver");  
			System.err.println("Using this username: " + jdbcUser);
			System.err.println("Using this password: " + jdbcPassword!=null);
			System.err.println("Using this JDBC URL: " + jdbcURL);
			try {
				Class.forName(jdbcDriver);
			} catch (ClassNotFoundException e) {
				System.err.println("Loading the jdbcDriver " + jdbcDriver + "  failed with " + e.getMessage());
				System.err.println(ExceptionUtils.getStackTrace(e));
			}
			// establish a connection
			return DriverManager.getConnection(jdbcURL,jdbcUser,jdbcPassword);
		} catch (Exception e) {
			System.err.println("Getting the JDBC connection failed with " + e.getMessage());
			System.err.println(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	private static void upgtadeID_TableEntries(Connection connection, int transactionID, int fieldChangeID) {	 
		 try {
			Statement stmt = connection.createStatement();
			stmt.executeUpdate("UPDATE ID_TABLE SET NEXT_ID = " + transactionID
					+ " WHERE TABLE_NAME = 'THISTORYTRANSACTION'");
		} catch (SQLException e) {
			LOGGER.error("Updating the THISTORYTRANSACTION ID in ID_TABLE failed with " + e.getMessage(), e);
			System.err.println(ExceptionUtils.getStackTrace(e));
		}
		try {
			Statement stmt = connection.createStatement();
			stmt.executeUpdate("UPDATE ID_TABLE SET NEXT_ID = " + fieldChangeID
					  + " WHERE TABLE_NAME = 'TFIELDCHANGE'");
		} catch (SQLException e) {
			LOGGER.error("Updating the TFIELDCHANGE ID in ID_TABLE failed with " + e.getMessage(), e);
			System.err.println(ExceptionUtils.getStackTrace(e));
		}
	}
	
	private static void addHistoryTransaction(PreparedStatement pstmtHistoryTransaction,
			 int transactionID, Integer workItemID, Integer changedBy, Date lastEdit) {
		 try {
			pstmtHistoryTransaction.setInt(1, transactionID);
			pstmtHistoryTransaction.setInt(2, workItemID);
			pstmtHistoryTransaction.setInt(3, changedBy);
			if (lastEdit==null) {
				pstmtHistoryTransaction.setDate(4, null);
			} else {
	 			pstmtHistoryTransaction.setTimestamp(4, new java.sql.Timestamp(lastEdit.getTime()));
	 		}
			pstmtHistoryTransaction.setString(5, UUID.randomUUID().toString());
			pstmtHistoryTransaction.executeUpdate();
		} catch (Exception e) {
			LOGGER.error("Adding a transaction  with transactionID " + transactionID +
					" workItemID " + workItemID + " changedBy " + changedBy + " at " + lastEdit + " failed with " + e.getMessage(), e);
			System.err.println(ExceptionUtils.getStackTrace(e));
		}
	}
	
	private static void addLongTextFieldChange(PreparedStatement pstmtLongText,
			 Integer fieldChangeID, Integer transactionID, String description) {
		 try {
			pstmtLongText.setInt(1, fieldChangeID);
			pstmtLongText.setInt(2, TFieldChangeBean.COMPOUND_HISTORY_FIELD);
			pstmtLongText.setInt(3, transactionID);
			pstmtLongText.setString(4, description);
			pstmtLongText.setInt(5, ValueType.LONGTEXT);
			pstmtLongText.setString(6, UUID.randomUUID().toString());
			pstmtLongText.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Adding a field change for long text with transactionID " + transactionID +
					" fieldChangeID " + fieldChangeID + " failed with " + e.getMessage(), e);
			System.err.println(ExceptionUtils.getStackTrace(e));
		}
	}
	
	private static void addStatusChange(PreparedStatement pstmtStatusChangeFirst,
			 PreparedStatement pstmtStatusChange,
			 Integer fieldChangeID, Integer transactionID, Integer newStatus, Integer oldStatus) {
		if (oldStatus==null) {
			try {
				pstmtStatusChangeFirst.setInt(1, fieldChangeID);
				pstmtStatusChangeFirst.setInt(2, SystemFields.STATE);
				pstmtStatusChangeFirst.setInt(3, transactionID);
				pstmtStatusChangeFirst.setInt(4, newStatus);
				pstmtStatusChangeFirst.setLong(5, SystemFields.STATE);
				pstmtStatusChangeFirst.setInt(6, ValueType.SYSTEMOPTION);
				pstmtStatusChangeFirst.setString(7, UUID.randomUUID().toString());
				pstmtStatusChangeFirst.executeUpdate();
			} catch (Exception e) {
				LOGGER.error("Adding a field change for status with transactionID " + transactionID +
						" fieldChangeID " + fieldChangeID  + " new status " + newStatus + " failed with " + e.getMessage(), e);
				System.err.println(ExceptionUtils.getStackTrace(e));
			}	
		} else {
			try {
				pstmtStatusChange.setInt(1, fieldChangeID);
				pstmtStatusChange.setInt(2, SystemFields.STATE);
				pstmtStatusChange.setInt(3, transactionID);
				pstmtStatusChange.setInt(4, newStatus);
				pstmtStatusChange.setInt(5, oldStatus);
				pstmtStatusChange.setLong(6, SystemFields.STATE);
				pstmtStatusChange.setInt(7, ValueType.SYSTEMOPTION);
				pstmtStatusChange.setString(8, UUID.randomUUID().toString());
				pstmtStatusChange.executeUpdate();
			} catch (Exception e) {
				LOGGER.error("Adding a field change for status with transactionID " + transactionID +
						" fieldChangeID " + fieldChangeID  + " new status " + newStatus + " oldStatus " + oldStatus + " failed with " + e.getMessage(), e);
				System.err.println(ExceptionUtils.getStackTrace(e));
			}	
		}
	}
	
	private static void addBaseLineChange(PreparedStatement pstmtBaseLineChange,
			 Integer fieldChangeID, Integer transactionID, int fieldKey, Date newDate, Date oldDate) {
		 try {
			pstmtBaseLineChange.setInt(1, fieldChangeID);
			pstmtBaseLineChange.setInt(2, fieldKey);
			pstmtBaseLineChange.setInt(3, transactionID);
			if (newDate==null) {
				pstmtBaseLineChange.setDate(4, null);
			} else {
				pstmtBaseLineChange.setDate(4, new java.sql.Date(newDate.getTime()));
			}
			if (oldDate==null) {
				pstmtBaseLineChange.setDate(5, null);
			} else {
				pstmtBaseLineChange.setDate(5, new java.sql.Date(oldDate.getTime()));
			}
			pstmtBaseLineChange.setInt(6, ValueType.DATE);
			pstmtBaseLineChange.setString(7, UUID.randomUUID().toString());
			pstmtBaseLineChange.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Adding a field change for base line with transactionID " + transactionID + " fieldChangeID " + fieldChangeID +
					 " fieldKey " + fieldKey + " newDate " + newDate + " oldDate " + oldDate + " failed with " + e.getMessage(), e);
			System.err.println(ExceptionUtils.getStackTrace(e));
		}
	 }
	 
	/**
	 * Reorganize the history data: move the data from the old history tables to the new history tables 
	 * @param siteBean
	 */
	private void reorganizeHistory(/*TSiteBean siteBean*/) {
		Integer chunkInterval = new Integer(100);
		TSiteBean siteBean = siteDAO.load1(); // update the siteBean, it may be outdated by now...
		String strHistoryEntity = siteBean.getHistoryEntity();
		Integer historyEntity = null;
		if (strHistoryEntity!=null && !"".equals(strHistoryEntity)) {
			historyEntity = new Integer(strHistoryEntity);
			LOGGER.info("Retake from the history entity " + historyEntity);
		}
		else {
			//mark the beginning of the history migration 
			historyEntity = TSiteBean.HISTORY_STATUSCHANGE;
			Map<Integer, Object> siteBeanValues = new HashMap<Integer, Object>();
			siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.HISTORYENTITY, historyEntity.toString());
			siteDAO.loadAndSaveSynchronized(siteBeanValues);
		}
		String strHistoryMigrationID = siteBean.getHistoryMigrationID();
		Integer historyMigrationID = null;
		if (strHistoryMigrationID!=null && !"".equals(strHistoryMigrationID)) {
			historyMigrationID = new Integer(strHistoryMigrationID);
			LOGGER.info("and ID " + historyMigrationID);
		}
		PreparedStatement pstmtHistoryTransaction = null;
		PreparedStatement pstmtStatusChangeFirst = null;
		PreparedStatement pstmtStatusChange = null;
		PreparedStatement pstmtLongText = null;
		PreparedStatement pstmtBaseLineChange = null;
		Connection connection = getConnection();
		try {
			pstmtHistoryTransaction = connection.prepareStatement(
					"INSERT INTO THISTORYTRANSACTION (OBJECTID, WORKITEM, CHANGEDBY, LASTEDIT, TPUUID)" +
					" VALUES  (?, ?, ?, ?, ?)");
			pstmtStatusChangeFirst = connection.prepareStatement(
					"INSERT INTO TFIELDCHANGE (OBJECTID, FIELDKEY, HISTORYTRANSACTION," +
					" NEWSYSTEMOPTIONID, SYSTEMOPTIONTYPE, VALIDVALUE, TPUUID) " +
					" VALUES  (?, ?, ?, ?, ?, ?, ?)");
			pstmtStatusChange = connection.prepareStatement(
					"INSERT INTO TFIELDCHANGE (OBJECTID, FIELDKEY, HISTORYTRANSACTION," +
					" NEWSYSTEMOPTIONID, OLDSYSTEMOPTIONID, SYSTEMOPTIONTYPE, VALIDVALUE, TPUUID) " +
					" VALUES  (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmtLongText = connection.prepareStatement(
					"INSERT INTO TFIELDCHANGE (OBJECTID, FIELDKEY, HISTORYTRANSACTION," +
					" NEWLONGTEXTVALUE, VALIDVALUE, TPUUID) " +
					" VALUES  (?, ?, ?, ?, ?, ?)");						
			pstmtBaseLineChange = connection.prepareStatement(
					"INSERT INTO TFIELDCHANGE (OBJECTID, FIELDKEY, HISTORYTRANSACTION," +
					" NEWDATEVALUE, OLDDATEVALUE, VALIDVALUE, TPUUID) " +
					" VALUES  (?, ?, ?, ?, ?, ?, ?)");
		} catch (Exception e) {
			LOGGER.error("Creating the prepared statment failed with + " + e.getMessage(), e);
		}
		
		int transactionID = 0;
		int fieldChangeID = 0;		
		try {
			String sqlStmt = "SELECT NEXT_ID FROM ID_TABLE WHERE TABLE_NAME = 'THISTORYTRANSACTION'";
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sqlStmt);
			if (rs!= null &&  rs.next()!=false) {
			  transactionID = rs.getInt(1);
			}
			if (rs != null) {
				rs.close();
			}
			stmt.close();
		} catch (Exception e) {
			LOGGER.error("Getting the actual transactionID failed with + " + e.getMessage(), e);
		}	
		try {
			String sqlStmt = "SELECT NEXT_ID FROM ID_TABLE WHERE TABLE_NAME = 'TFIELDCHANGE'";	
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sqlStmt);
			if (rs!= null &&  rs.next()!=false) {
				fieldChangeID = rs.getInt(1);
			}
			if (rs != null) {
				rs.close();
			}
			stmt.close();
		} catch (Exception e) {
			LOGGER.error("Getting the actual fieldChangeID failed with + " + e.getMessage(), e);
		}	
		
		int longTextSize = 6000;//Firebird
		try {		
			Statement stmt = connection.createStatement();
			int sizeMySQL = 16777215;//MySql
			ResultSet rs = stmt.executeQuery("SELECT NEWLONGTEXTVALUE FROM TFIELDCHANGE");
			if (rs != null) {
				longTextSize = rs.getMetaData().getColumnDisplaySize(1);
				if (longTextSize<=0) {
					//MySQL bug (MySql 5.0.26 for linux returns -1)
					LOGGER.error("Incorrect maximum description length retrieved: " + longTextSize);
					longTextSize=sizeMySQL;
				}
			}
			LOGGER.info("Maximum long text length: " + longTextSize);
			if (rs != null) {
				rs.close();
			}
			stmt.close();
		} catch (Exception e) {
			LOGGER.error("Getting the maximum long text length failed with + " + e.getMessage(), e);
		}
		
		//migrate the status changes
		if (historyEntity.intValue()==TSiteBean.HISTORY_STATUSCHANGE.intValue()) {
			//now begins the status change migration
			Map<Integer, Integer> workItemToOldStatus = new HashMap<Integer, Integer>();
			Integer maxStateChangeID = stateChangeDAO.getMaxObjectID();
			if(maxStateChangeID!=null&&maxStateChangeID.intValue()>0){
				int statusChanges=0;
				int statusChangeTrails=0;
				Integer fromID;
				if (historyMigrationID==null) {
					fromID = Integer.valueOf(0);
				} else {
					fromID = historyMigrationID;
				}
				while (fromID<maxStateChangeID) {
					List<TStateChangeBean> stateChangeChunk = stateChangeDAO.getNextChunk(fromID, chunkInterval);
					if (stateChangeChunk!=null && !stateChangeChunk.isEmpty()) {
						//for worst case scenario have valid the ID_TABLE entries for torque even if the migration stops unexpectedly  
						upgtadeID_TableEntries(connection, transactionID + chunkInterval, fieldChangeID + chunkInterval*2);
						for (TStateChangeBean stateChangeBean : stateChangeChunk) {
							Integer objectID = stateChangeBean.getObjectID();
							Integer workItemID = stateChangeBean.getWorkItemID();
							Integer statusID = stateChangeBean.getChangedToID();
							Date lastEdit = stateChangeBean.getLastEdit();
							if (historyMigrationID!=null && historyMigrationID.intValue()>=objectID.intValue()) {
								//the status change migration was stopped by a previous tomcat stop
								continue;
							}
							statusChanges++;
							Integer status = stateChangeBean.getChangedToID();
							addHistoryTransaction(pstmtHistoryTransaction, ++transactionID, workItemID, stateChangeBean.getChangedByID(), lastEdit);
							Integer oldStatus = workItemToOldStatus.get(workItemID);
							addStatusChange(pstmtStatusChangeFirst,
									 pstmtStatusChange,
									 ++fieldChangeID, transactionID, statusID, oldStatus);
							//add/overwrite the old status in the map for the next status change
							workItemToOldStatus.put(workItemID, status);
							String description = stateChangeBean.getDescription();			
							if (description!=null && !"".equals(description)) {
								int descriptionLength = description.length();
								if (descriptionLength>longTextSize) {
									LOGGER.warn("The status change description for wortItemID " + workItemID + " and new status " + statusID + 
											"at " + lastEdit + " was cut from a length of " + descriptionLength + " to " + longTextSize);
									description = description.substring(0, longTextSize-1);
								}
								addLongTextFieldChange(pstmtLongText, ++fieldChangeID, transactionID, description);
								statusChangeTrails++;
							}
							Map<Integer, Object> siteBeanValues = new HashMap<Integer, Object>();
							siteBeanValues = new HashMap<Integer, Object>();
							siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.HISTORYMIGRATIONID, objectID.toString());
							siteDAO.loadAndSaveSynchronized(siteBeanValues);
						}
						LOGGER.info(DateTimeUtils.getInstance().formatISODateTime(new Date()) + ": migrated status changes in the interval from " + fromID + " to " + 
								new Integer(fromID.intValue() + chunkInterval.intValue()) + " a number of " + stateChangeChunk.size() + " entries.");					
					}	
					fromID = new Integer(fromID.intValue() + chunkInterval.intValue());
				}
				LOGGER.info(DateTimeUtils.getInstance().formatISODateTime(new Date()) + ": migrated " + statusChanges + " status changes and " + statusChangeTrails + " trail entries from status changes");
			}
		}
		
		//migrate the baseline changes
		if (historyEntity.intValue()<=TSiteBean.HISTORY_BASELINECHANGE.intValue()) {
			boolean baseLinePreviousSession = true; 
			if (historyEntity.intValue()<TSiteBean.HISTORY_BASELINECHANGE.intValue()) {
				baseLinePreviousSession = false;
				//now begins the base line change migration
				Map<Integer, Object> siteBeanValues = new HashMap<Integer, Object>();
				siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.HISTORYENTITY, TSiteBean.HISTORY_BASELINECHANGE.toString());
				siteDAO.loadAndSaveSynchronized(siteBeanValues);
			}
			Map<Integer, Date> workItemToOldStartDate = new HashMap<Integer, Date>();
			Map<Integer, Date>  workItemToOldEndDate = new HashMap<Integer, Date>();
			Integer maxBaseLineID = baseLineDAO.getMaxObjectID();
			if(maxBaseLineID!=null&&maxBaseLineID.intValue()>0){
				int baseLineChanges=0;
				int baseLineTrails = 0;
				Integer fromID;
				if (historyMigrationID==null || !baseLinePreviousSession) {
					fromID = new Integer(0);
				} else {
					fromID = historyMigrationID;
				}
				while (fromID<maxBaseLineID) {
					List<TBaseLineBean> baseLineChangeChunk = baseLineDAO.getNextChunk(fromID, chunkInterval);
					if (baseLineChangeChunk!=null && !baseLineChangeChunk.isEmpty()) {
						//for worst case scenario have valid the ID_TABLE entries for torque even if the migration stops unexpectedly  
						upgtadeID_TableEntries(connection, transactionID + chunkInterval, fieldChangeID + chunkInterval*3);
						for (TBaseLineBean baseLineBean : baseLineChangeChunk) {
							Integer objectID = baseLineBean.getObjectID();
							Integer workItemID = baseLineBean.getWorkItemID();
							Date lastEdit = baseLineBean.getLastEdit();
							if (historyMigrationID!=null && historyMigrationID.intValue()>=objectID.intValue() && 
									baseLinePreviousSession) {
								//the status change migration was commenced by a previous tomcat start
								continue;
							}
							baseLineChanges++;
							addHistoryTransaction(pstmtHistoryTransaction, ++transactionID, workItemID,
									baseLineBean.getChangedByID(), lastEdit);
							Date newStartDate = baseLineBean.getStartDate();
							Date oldStartDate = workItemToOldStartDate.get(workItemID);
							if (EqualUtils.notEqual(newStartDate, oldStartDate)) {
								addBaseLineChange(pstmtBaseLineChange,
										 ++fieldChangeID, transactionID, SystemFields.STARTDATE, newStartDate, oldStartDate);
								//add/overwrite the old start date in the map for the next start date change
								workItemToOldStartDate.put(workItemID, newStartDate);
							}
							Date newEndDate = baseLineBean.getEndDate();
							Date oldEndDate = workItemToOldEndDate.get(workItemID);
							if (EqualUtils.notEqual(newEndDate, oldEndDate)) {
								addBaseLineChange(pstmtBaseLineChange,
										 ++fieldChangeID, transactionID, SystemFields.ENDDATE, newEndDate, oldEndDate);
								//add/overwrite the old end date in the map for the next end date change
								workItemToOldEndDate.put(workItemID, newEndDate);
							}
							String description = baseLineBean.getDescription();
							if (description!=null && !"".equals(description)) {
								int descriptionLength = description.length();
								if (descriptionLength>longTextSize) {
									LOGGER.warn("The base line change description for wortItemID " + workItemID +
											" and new start date " + newStartDate + " and new end date " + newEndDate +
											" at " + lastEdit +
											" was cut from a length of " + descriptionLength + " to " + longTextSize);
									description = description.substring(0, longTextSize-1);
								}
								addLongTextFieldChange(pstmtLongText, ++fieldChangeID, transactionID, description);
								baseLineTrails++;
							}
							Map<Integer, Object> siteBeanValues = new HashMap<Integer, Object>();
							siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.HISTORYMIGRATIONID, objectID.toString());
							siteDAO.loadAndSaveSynchronized(siteBeanValues);
						}
						LOGGER.info(DateTimeUtils.getInstance().formatISODateTime(new Date()) + ": migrated base line changes in the interval from " + fromID + " to " + 
								new Integer(fromID.intValue() + chunkInterval.intValue()) + " a number of " + baseLineChangeChunk.size() + " entries.");					
					}
					fromID = new Integer(fromID.intValue() + chunkInterval.intValue());
				}			
				LOGGER.info(DateTimeUtils.getInstance().formatISODateTime(new Date()) +  ": migrated " + baseLineChanges + " base line changes and " + baseLineTrails + " trail entries from base line changes");
			}
		}

		//migrate the trail changes
		if (historyEntity.intValue()<=TSiteBean.HISTORY_TRAIL.intValue()) {
			boolean trailPreviousSession = true; 
			if (historyEntity.intValue()<TSiteBean.HISTORY_TRAIL.intValue()) {
				trailPreviousSession = false; 
				Map<Integer, Object> siteBeanValues = new HashMap<Integer, Object>();
				siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.HISTORYENTITY, TSiteBean.HISTORY_TRAIL.toString());
				siteDAO.loadAndSaveSynchronized(siteBeanValues);
			}
			Integer maxTrailID = trailDAO.getMaxObjectID();
			if(maxTrailID!=null&&maxTrailID.intValue()>0){
				int trailChanges=0;
				Integer fromID;
				if (historyMigrationID==null || !trailPreviousSession) {
					fromID = new Integer(0);
				} else {
					fromID = historyMigrationID;
				}
				while (fromID<maxTrailID) {
					List<TTrailBean> trailChunk = trailDAO.getNextChunk(fromID, chunkInterval);
					if (trailChunk!=null && !trailChunk.isEmpty()) {
						//for worst case scenario have valid the ID_TABLE entries for torque even if the migration stops unexpectedly  
						upgtadeID_TableEntries(connection, transactionID + chunkInterval, fieldChangeID + chunkInterval);
						for (TTrailBean trailBean : trailChunk) {
							Integer objectID = trailBean.getObjectID();
							Integer workItemID = trailBean.getWorkItemID();
							Date lastEdit = trailBean.getLastEdit();
							if (historyMigrationID!=null && historyMigrationID.intValue()>=objectID.intValue() &&
									trailPreviousSession) {
								continue;
							}
							String description = trailBean.getDescription();
							if (description!=null && !"".equals(description)) {
								trailChanges++;
								addHistoryTransaction(pstmtHistoryTransaction, ++transactionID, trailBean.getWorkItemID(),
										trailBean.getChangedByID(), trailBean.getLastEdit());
								int descriptionLength = description.length();
								if (descriptionLength>longTextSize) {
									LOGGER.warn("The trail change description for wortItemID " + workItemID +
											" at " + lastEdit + " was cut from a length of " + descriptionLength + " to " + longTextSize);
									description = description.substring(0, longTextSize-1);
								}
								addLongTextFieldChange(pstmtLongText, ++fieldChangeID, transactionID, description);
							}
							Map<Integer, Object> siteBeanValues = new HashMap<Integer, Object>();
							siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.HISTORYMIGRATIONID, objectID.toString());
							siteDAO.loadAndSaveSynchronized(siteBeanValues);
						}
						LOGGER.info(DateTimeUtils.getInstance().formatISODateTime(new Date()) + ": migrated trail changes in the interval from " + fromID + " to " + 
								new Integer(fromID.intValue() + chunkInterval.intValue()) + " a number of " + trailChunk.size() + " entries.");
					}
					fromID = new Integer(fromID.intValue() + chunkInterval.intValue());
				}		
				LOGGER.info(DateTimeUtils.getInstance().formatISODateTime(new Date()) +  ": migrated " + trailChanges + " trails.");
			}
		}
		upgtadeID_TableEntries(connection, ++transactionID, ++fieldChangeID);
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.info("Closing the connection failed with " + e.getMessage(), e);
				System.err.println(ExceptionUtils.getStackTrace(e));
			}
		}
		Map<Integer, Object> siteBeanValues = new HashMap<Integer, Object>();
		siteBeanValues.put(TSiteBean.COLUMNIDENTIFIERS.HISTORYENTITY, TSiteBean.HISTORY_DONE.toString());
		siteDAO.loadAndSaveSynchronized(siteBeanValues);
		LOGGER.info("History migration done");
	 }
	
	 /**
	 * Remove the screens for the comment action and the comment action too 
	 */
	private static void removeCommentAction() {
		List<TScreenConfigBean> commentScreeenConfigs = screenConfigDAO.loadByActionKey(new Integer(6));
		if (commentScreeenConfigs!=null) {
			Iterator<TScreenConfigBean> iterator = commentScreeenConfigs.iterator();
			while (iterator.hasNext()) {
				TScreenConfigBean screenConfigBean =  iterator.next();
				screenConfigDAO.delete(screenConfigBean.getObjectID());
				screenDAO.delete(screenConfigBean.getScreen());
			}
		}
		ActionBL.delete(Integer.valueOf(6));
	}
	
	 /**
	 * Prepare the report layout to store also the priontItem tabs layout (not just the report overview layout) 
	 */
	private static void updateReportLayout() {
		List<TReportLayoutBean> reportLayoutList = reportLayoutDAO.loadAll();
		Iterator<TReportLayoutBean> iterator = reportLayoutList.iterator();
		while (iterator.hasNext()) {
			TReportLayoutBean reportLayoutBean = iterator.next();
			reportLayoutBean.setFieldType(TReportLayoutBean.FIELD_TYPE.VISIBLE);
			reportLayoutBean.setLayoutKey(TReportLayoutBean.LAYOUT_TYPE.REPORT_OVERVIEW);
			reportLayoutDAO.save(reportLayoutBean);
		}
	}
	
	 /**
	 * Split the disabled and user level representation into different database fields
	 */
	private static void setDisabledAndUserLevel() {
		List<TPersonBean> persons = PersonBL.loadPersons();
		Iterator<TPersonBean> iterator = persons.iterator();
		while (iterator.hasNext()) {
			TPersonBean personBean = iterator.next();
			String oldDisabledFlag = personBean.getDeleted();
			boolean disabled = false;
			if (PERSON_STATUS.INTERNAL_INACTIVE.equals(oldDisabledFlag) || 
					PERSON_STATUS.EXTERNAL_INACTIVE.equals(oldDisabledFlag)) {
				disabled = true;
			}
			if (disabled) {
				personBean.setDeleted(BooleanFields.TRUE_VALUE);
			} else {
				personBean.setDeleted(BooleanFields.FALSE_VALUE);
			}
			boolean external = false;
			if (PERSON_STATUS.EXTERNAL_ACTIVE.equals(oldDisabledFlag) || 
					PERSON_STATUS.EXTERNAL_INACTIVE.equals(oldDisabledFlag)) {
				external = true;
			}
			boolean systemAdmin = getIsSysAdminOld(personBean);
			if (systemAdmin) {
				personBean.setUserLevel(TPersonBean.USERLEVEL.SYSADMIN);
			} else {
				if (external) {
					personBean.setUserLevel(TPersonBean.USERLEVEL.CLIENT);
				} else {
					personBean.setUserLevel(TPersonBean.USERLEVEL.FULL);
				}
			}
			PersonBL.saveSimple(personBean);
		}
			
	}
	
	 /**
	 * Whether the person was system admin according to the old configuration
	 * @param personBean
	 * @return
	 */
	 private static boolean getIsSysAdminOld(TPersonBean personBean) {
		String preferences = personBean.getPreferences(); 
		String isSysAdmStrg = PropertiesHelper.getProperty(preferences, TPersonBean.IS_SYSADM);
		if (isSysAdmStrg!=null) {
			personBean.setPreferences(PropertiesHelper.removeProperty(preferences, TPersonBean.IS_SYSADM));
		}
		if (personBean.getObjectID().intValue() < 100) {
			return true;
		}
		if (isSysAdmStrg == null || isSysAdmStrg.length() < 1 || "N".equals(isSysAdmStrg)) {
			  return false;
		}
		else if ("Y".equals(isSysAdmStrg)){
			return true;
		}
		return false;
	}
}
