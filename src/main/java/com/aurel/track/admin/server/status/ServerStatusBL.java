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

package com.aurel.track.admin.server.status;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.torque.Torque;

import com.aurel.track.StartServlet;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.server.logging.MemoryAppender;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.admin.user.person.feature.UserFeatureBL;
import com.aurel.track.beans.TClusterNodeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.cluster.ClusterBL;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.SiteDAO;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.dbase.InitDatabase;
import com.aurel.track.persist.BaseTSitePeer;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.LabelValueBean;
import com.trackplus.license.LicenseManager;
import com.trackplus.license.LicensedFeature;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JasperCompileManager;


/**
 * This class represents the business layer part of the ServerStatus service,
 * thus collects the data required by the {@link ServerStatusTO} transfer
 * object.
 * 
 * @author rrobi
 */
public class ServerStatusBL {

	private static final Logger LOGGER = LogManager.getLogger(ServerStatusBL.class);
	private static SiteDAO siteDAO = DAOFactory.getFactory().getSiteDAO();
	private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();

	/**
	 * Saves the operation mode and the user message
	 * @param opState
	 * @param userMsg
	 */
	static void saveOpMode(String opState, String userMsg, boolean hasUserMsg) {
		TSiteBean siteBean = siteDAO.load1();
		siteBean.setOpState(opState);
		siteBean.setUserMessage(userMsg);
		siteBean.setUserMessageActiv(hasUserMsg);
		siteDAO.save(siteBean);
	}
	
	public static ServerStatusTO load(Locale locale,ApplicationBean appBean) {
		ServerStatusTO serverStatusTO = new ServerStatusTO();
		List<TPersonBean> loggedin = PersonBL.getLoggedInUsers();
		// TODO information from appbean to to
		// max active users
		// version + build info
		// operational status
		// user message
		loadUserMessageInfo(serverStatusTO);
		loadUserStatistics(serverStatusTO, loggedin, locale);
		loadClustersAndLogins(serverStatusTO, loggedin);
		loadStatistics(serverStatusTO);
		loadSystemConfigurationInfo(serverStatusTO, appBean, locale);
		loadJavaRuntimeInfo(serverStatusTO);
		loadSystemInfo(serverStatusTO);
		loadDatabaseInfo(serverStatusTO);
		return serverStatusTO;
	}
	
	

	private static void loadUserMessageInfo(ServerStatusTO serverStatusTO) {
		TSiteBean siteBean = siteDAO.load1();
		serverStatusTO.setOperationalStatus(siteBean.getOpState());
		if (siteBean.getUserMessageActiv()!=null) {
			serverStatusTO.setHasUserInfo(siteBean.getUserMessageActiv().booleanValue());
		}
		serverStatusTO.setUserInfo(siteBean.getUserMessage());
	}

	private static void loadClustersAndLogins(ServerStatusTO serverStatusTO, List<TPersonBean> loggedin) {
		List<TClusterNodeBean> clusters = ClusterBL.loadAllClusterNodes();
		/*List<TPersonBean> loggedin = new ArrayList<TPersonBean>(
				TLoggedInUsersPeer.getLoggedInUsers().values());*/
		List<LabelValueBean> users = new ArrayList<LabelValueBean>();
		List<LabelValueBean> clusterList = new ArrayList<LabelValueBean>();
		for (TPersonBean person : loggedin) {
			users.add(new LabelValueBean(person.getFullName(),person.getLoginName()));
		}
		for (TClusterNodeBean cluster : clusters) {
			clusterList.add(new LabelValueBean(cluster.getNodeAddress(),
					cluster.getNodeAddress()));
		}

		serverStatusTO.setClusterNodes(clusterList);
		serverStatusTO.setUsersLoggedIn(users);
	}

	private static void loadStatistics(ServerStatusTO serverStatusTO) {
		int totalProjCount = ProjectBL.count(true, true) - 1;
		int mainProjCount = ProjectBL.count(false, true) - 1;
		int subProjCount = ProjectBL.count(false, false);		
		serverStatusTO.setNumberOfProjects(totalProjCount + " (" + mainProjCount + "/" + subProjCount + ")");
		int itemCount = 0;
		try {
			itemCount = workItemDAO.count();
		} catch (Exception e1) {
			LOGGER.error("Problem counting issues:", e1);
		}
		serverStatusTO.setNumberOfIssues(itemCount);
	}

	private static void loadSystemConfigurationInfo(ServerStatusTO serverStatusTO,ApplicationBean appBean, Locale locale){
		TSiteBean site = siteDAO.load1();
		serverStatusTO.setVersion(site.getTrackVersion());
		UserCountTO countLicensedUsers = new UserCountTO();
		countLicensedUsers.setLabel(LocalizeUtil.getLocalizedTextFromApplicationResources("admin.server.status.maxUsers", locale));
		countLicensedUsers.setNumberOfActiveUsers(appBean.getMaxNumberOfFullUsers());
		countLicensedUsers.setNumberOfInactiveUsers(appBean.getMaxNumberOfLimitedUsers());
		serverStatusTO.setLicensedUsers(countLicensedUsers);
		String ipNumber = ApplicationBean.getIpNumbersString();
		serverStatusTO.setServerIPAddress(ipNumber);
	}

	private static void loadJavaRuntimeInfo(ServerStatusTO serverStatusTO) {
		Runtime rt = Runtime.getRuntime();
		long mbyte = 1024 * 1024;
		serverStatusTO.setJavaVersion(System.getProperty("java.version"));
		serverStatusTO.setJavaVendor(System.getProperty("java.vendor"));
		serverStatusTO.setJavaHome(System.getProperty("java.home"));
		serverStatusTO.setJavaVMVersion(System.getProperty("java.vm.version"));
		serverStatusTO.setJavaVMVendor(System.getProperty("java.vm.vendor"));
		serverStatusTO.setJavaVMName(System.getProperty("java.vm.name"));

		serverStatusTO.setJavaVMfreeMemory(rt.freeMemory() / mbyte);
		serverStatusTO.setJavaVMmaxMemory(rt.maxMemory() / mbyte);
		serverStatusTO.setJavaVMtotalMemory(rt.totalMemory() / mbyte);
		serverStatusTO.setJavaVMusedMemory(serverStatusTO.getJavaVMtotalMemory() - serverStatusTO.getJavaVMfreeMemory());
	}

	private static void loadSystemInfo(ServerStatusTO serverStatusTO) {
		String osn = System.getProperty("os.name");
		String osv = System.getProperty("os.version");
		String osa = System.getProperty("os.arch");
		serverStatusTO.setOperatingSystem(osn + " " + osv + " (" + osa + ")");
		serverStatusTO.setUserName(System.getProperty("user.name"));
		serverStatusTO.setCurrentUserDir(System.getProperty("user.dir"));
		serverStatusTO.setSystemLocale(Locale.getDefault().getDisplayName());
		serverStatusTO.setUserTimezone(System.getProperty("user.timezone"));
		serverStatusTO.setJasperVersion(JasperCompileManager.getInstance(DefaultJasperReportsContext.getInstance()).getClass().getPackage().getImplementationVersion());
	}
	
	private void ttt() {
		JasperCompileManager.getInstance(DefaultJasperReportsContext.getInstance()).getClass().getPackage().getImplementationVersion();
	}

	private static void loadDatabaseInfo(ServerStatusTO serverStatusTO) {
		Connection conn = null;
		try {
			conn = Torque.getConnection(BaseTSitePeer.DATABASE_NAME);
			DatabaseMetaData dbm = conn.getMetaData();
			serverStatusTO.setDatabase(dbm.getDatabaseProductName() + " "
					+ dbm.getDatabaseProductVersion());
			serverStatusTO.setJdbcDriver(dbm.getDriverName() + " "
					+ dbm.getDriverVersion());
			serverStatusTO.setJdbcUrl(dbm.getURL());
		} catch (Exception e) {
			LOGGER.error("Problem retrieving database meta data: " + e.getMessage(), e);
		} finally {
			if (conn!=null) {
				Torque.closeConnection(conn);
			}
		}
		Double ping = loadPing();
		serverStatusTO.setPingTime(ping.toString() + " ms");
	}

	private static void loadUserStatistics(ServerStatusTO serverStatusTO, List<TPersonBean> loggedin, Locale locale) {
		List<UserCountTO> userCountList = new LinkedList<UserCountTO>();
		UserCountTO userCountFullUsers = new UserCountTO();
		userCountFullUsers.setLabel(LocalizeUtil.getLocalizedTextFromApplicationResources("admin.server.status.userNoFull", locale));
		userCountFullUsers.setNumberOfActiveUsers(PersonBL.countFullActive());
		userCountFullUsers.setNumberOfInactiveUsers(PersonBL.countFullInactive());
		userCountList.add(userCountFullUsers);
		
		UserCountTO userCountClientUsers = new UserCountTO();
		userCountClientUsers.setLabel(LocalizeUtil.getLocalizedTextFromApplicationResources("admin.server.status.userNoClient", locale));
		userCountClientUsers.setNumberOfActiveUsers(PersonBL.countLimitedActive());
		userCountClientUsers.setNumberOfInactiveUsers(PersonBL.countLimitedInactive());
		
		
		serverStatusTO.setUsersCountList(userCountList);

		int countLoggedInFull = 0;
		int countLoggedInEasy = 0;
		for (TPersonBean personBean : loggedin) {
			if (TPersonBean.USERLEVEL.CLIENT.equals(personBean.getUserLevel())) {
				countLoggedInEasy++;
			} else {
				countLoggedInFull++;
			}
		}
		
		UserCountTO userCountLoggedUsers = new UserCountTO();
		userCountLoggedUsers.setLabel(LocalizeUtil.getLocalizedTextFromApplicationResources("admin.server.status.numberOfUsers", locale));
		userCountLoggedUsers.setNumberOfActiveUsers(countLoggedInFull);
		userCountLoggedUsers.setNumberOfInactiveUsers(countLoggedInEasy);
		userCountList.add(userCountLoggedUsers);
		
	}
	
	/**
	 * Return the one and only memory appender
	 */
	public static MemoryAppender getMemoryAppender() {
		MemoryAppender memoryAppender = null;
		final LoggerContext ctx = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(StartServlet.class.getClassLoader(), false);

        final Configuration config = (Configuration) ctx.getConfiguration();
      
		Map<String, Appender> appenders = config.getAppenders();
		if (appenders != null) {
			memoryAppender = (MemoryAppender) appenders.get("MEMORY");
		}
		return memoryAppender;
	}

	
	/**
	 * 
	 */
	public static Double loadPing() {
//		EntityManager em = TpEm.getEntityManager();
//		if (em !=  null) {
//			try {
//				em.getTransaction().begin();
//
//				java.sql.Connection connection = em.unwrap(java.sql.Connection.class);
//				ResultSet rs = null;
//
//				Date start = new Date();
//				for (int i = 0; i < 10; ++ i) {
//					Statement stmt = connection.createStatement();
//					rs = stmt.executeQuery("SELECT OBJECTID FROM TSITE");
//					while (rs.next()) {
//						int result = rs.getInt(1);
//					}
//					stmt.close();
//				}
//				Date stop = new Date();
//
//				em.getTransaction().commit();
//
//				return  new Double((stop.getTime() - start.getTime()))/10.0;
//			} catch (Exception e) {
//				LOGGER.error("Can't ping: " + e.getMessage(), e);
//			}
//		} else {
			Connection connection = null;
			try {
				connection = InitDatabase.getConnection(); 
				Date start = new Date();
				for (int i = 0; i < 10; ++ i) {
					Statement stmt = connection.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT OBJECTID FROM TSITE");
					while (rs.next()) {
						rs.getInt(1);
					}
					rs.close();
					stmt.close();
				}
				Date stop = new Date();
				return  new Double((stop.getTime() - start.getTime()))/10.0;
			} catch (Exception e) {
				LOGGER.error("Can't ping: " + e.getMessage(), e);
			} finally {
				if (connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {
						LOGGER.warn("Closing the connection failed with " + e.getMessage());
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}
				}
			}
//		}
		return -999.99;
	}

}
