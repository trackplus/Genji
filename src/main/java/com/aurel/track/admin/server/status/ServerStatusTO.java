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

package com.aurel.track.admin.server.status;

import java.util.ArrayList;
import java.util.List;

import com.aurel.track.util.LabelValueBean;

public class ServerStatusTO {
	private String version;
	private String serverIPAddress;
	private UserCountTO licensedUsers;
	
	private String numberOfProjects;
	private int numberOfIssues;
	private List<UserCountTO> usersCountList;
	private List<LabelValueBean> usersLoggedIn;
	private List<LabelValueBean> clusterNodes;

	private String operationalStatus;
	private boolean isUserInfo;
	private String userInfo;

	private long javaVMmaxMemory;
	private long javaVMtotalMemory;
	private long javaVMusedMemory;
	private long javaVMfreeMemory;

	private String javaVersion;
	private String javaVendor;
	private String javaHome;
	private String javaVMVersion;
	private String javaVMVendor;
	private String javaVMName;

	private String operatingSystem;
	private String userName;
	private String currentUserDir;
	private String systemLocale;
	private String userTimezone;

	private String database;
	private String jdbcDriver;
	private String jdbcUrl;
	private String pingTime;
	
	private String jasperVersion;
	
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getServerIPAddress() {
		return serverIPAddress;
	}
	public void setServerIPAddress(String serverIPAddress) {
		this.serverIPAddress = serverIPAddress;
	}
	public String getNumberOfProjects() {
		return numberOfProjects;
	}
	public void setNumberOfProjects(String numberOfProjects) {
		this.numberOfProjects = numberOfProjects;
	}
	public int getNumberOfIssues() {
		return numberOfIssues;
	}
	public void setNumberOfIssues(int numberOfIssues) {
		this.numberOfIssues = numberOfIssues;
	}
	
	public List<LabelValueBean> getUsersLoggedIn() {
		return usersLoggedIn;
	}
	public void setUsersLoggedIn(List<LabelValueBean> usersLoggedIn) {
		this.usersLoggedIn = usersLoggedIn;
	}
	public List<LabelValueBean> getClusterNodes() {
		return clusterNodes;
	}
	public void setClusterNodes(List<LabelValueBean> clusterNodes) {
		this.clusterNodes = clusterNodes;
	}
	public String getOperationalStatus() {
		return operationalStatus;
	}
	public void setOperationalStatus(String operationalStatus) {
		this.operationalStatus = operationalStatus;
	}
	public boolean getHasUserInfo() {
		return isUserInfo;
	}
	public void setHasUserInfo(boolean isUserInfo) {
		this.isUserInfo = isUserInfo;
	}
	public String getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}
	public long getJavaVMmaxMemory() {
		return javaVMmaxMemory;
	}
	public void setJavaVMmaxMemory(long javaVMmaxMemory) {
		this.javaVMmaxMemory = javaVMmaxMemory;
	}
	public long getJavaVMtotalMemory() {
		return javaVMtotalMemory;
	}
	public void setJavaVMtotalMemory(long javaVMtotalMemory) {
		this.javaVMtotalMemory = javaVMtotalMemory;
	}
	public long getJavaVMusedMemory() {
		return javaVMusedMemory;
	}
	public void setJavaVMusedMemory(long javaVMusedMemory) {
		this.javaVMusedMemory = javaVMusedMemory;
	}
	public long getJavaVMfreeMemory() {
		return javaVMfreeMemory;
	}
	public void setJavaVMfreeMemory(long javaVMfreeMemory) {
		this.javaVMfreeMemory = javaVMfreeMemory;
	}
	public String getJavaVersion() {
		return javaVersion;
	}
	public void setJavaVersion(String javaVersion) {
		this.javaVersion = javaVersion;
	}
	public String getJavaVendor() {
		return javaVendor;
	}
	public void setJavaVendor(String javaVendor) {
		this.javaVendor = javaVendor;
	}
	public String getJavaHome() {
		return javaHome;
	}
	public void setJavaHome(String javaHome) {
		this.javaHome = javaHome;
	}
	public String getJavaVMVersion() {
		return javaVMVersion;
	}
	public void setJavaVMVersion(String javaVMVersion) {
		this.javaVMVersion = javaVMVersion;
	}
	public String getJavaVMVendor() {
		return javaVMVendor;
	}
	public void setJavaVMVendor(String javaVMVendor) {
		this.javaVMVendor = javaVMVendor;
	}
	public String getJavaVMName() {
		return javaVMName;
	}
	public void setJavaVMName(String javaVMName) {
		this.javaVMName = javaVMName;
	}
	public String getOperatingSystem() {
		return operatingSystem;
	}
	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCurrentUserDir() {
		return currentUserDir;
	}
	public void setCurrentUserDir(String currentUserDir) {
		this.currentUserDir = currentUserDir;
	}
	public String getSystemLocale() {
		return systemLocale;
	}
	public void setSystemLocale(String systemLocale) {
		this.systemLocale = systemLocale;
	}
	public String getUserTimezone() {
		return userTimezone;
	}
	public void setUserTimezone(String userTimezone) {
		this.userTimezone = userTimezone;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getJdbcDriver() {
		return jdbcDriver;
	}
	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	
	public UserCountTO getLicensedUsers() {
		return licensedUsers;
	}
	public void setLicensedUsers(UserCountTO licensedUsers) {
		this.licensedUsers = licensedUsers;
	}
	public List<UserCountTO> getUsersCountList() {
		return usersCountList;
	}
	public void setUsersCountList(List<UserCountTO> usersCountList) {
		this.usersCountList = usersCountList;
	}
	public String getPingTime() {
		return pingTime;
	}
	public void setPingTime(String pingTime) {
		this.pingTime = pingTime;
	}
	
	public List<String> getLogMessages() {
		if (ServerStatusBL.getMemoryAppender() != null) {
			return ServerStatusBL.getMemoryAppender().getLog();
		} else {
			ArrayList<String> msgs = new ArrayList<String>();
			msgs.add("The logging system is not configured properly. \nThis might get fixed when you restart the application (server)");
			return msgs;
		}
	}
	
	public static List<String> getLogMessageLines(int start) {
		ArrayList<String> msgs = new ArrayList<String>(); 
		if (ServerStatusBL.getMemoryAppender() != null) {
			List<String> lines = ServerStatusBL.getMemoryAppender().getLog();
			for (int i=start; i < lines.size(); ++i) {
				msgs.add(lines.get(i));
			}
		} else {
			msgs.add("The logging system is not configured properly. \nThis might get fixed when you restart the application (server)");
			return msgs;
		}
		return msgs;
	}
	
	public String getJasperVersion() {
		return jasperVersion;
	}
	
	public  void setJasperVersion(String version) {
		jasperVersion = version;
	}
}
