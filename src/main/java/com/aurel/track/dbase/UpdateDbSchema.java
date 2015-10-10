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

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.servlet.ServletContext;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aurel.track.ApplicationStarter;
import com.aurel.track.prop.ApplicationBean;



/**
 * This class upgrades the Genji database schema if necessary.
 *
 * @version $Revision: 1439 $ $Date: 2015-09-28 13:37:25 +0200 (Mon, 28 Sep 2015) $
 */
public final class UpdateDbSchema {

	private static final long serialVersionUID = 400L;
	private static final Logger LOGGER = LogManager.getLogger(UpdateDbSchema.class);
	private static String[] migrateScripts = {
		"migrate210to300.sql",
		"migrate300to310.sql",
		"migrate310to320.sql",
		"migrate320to330.sql",
		"migrate330to340.sql",
		"migrate340to350.sql",
		"migrate350to370.sql",
		"migrate370to380.sql",
		"migrate380to400.sql",
		"migrate400to410.sql",
		"migrate410to412.sql",
		"migrate412to415.sql",
		"migrate415to500.sql",
	};





	/**
	 * Gets the database version
	 * @param dbConnection
	 * @return
	 */
	public static int getDBVersion(Connection dbConnection) {
			Statement istmt = null;
			ResultSet rs = null;
			try {
				istmt = dbConnection.createStatement();
				rs = istmt.executeQuery("SELECT DBVERSION FROM TSITE");
				if (rs==null || !rs.next()) {
					LOGGER.info("TSITE is empty.");
				} else {
					return rs.getInt(1);
				}
			} catch (Exception e) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			} finally {
				try {
					if (rs!=null)  {
						rs.close();
					}
				} catch (Exception e) {
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
				try {
					if (istmt!=null) {
						istmt.close();
					}
				} catch (Exception e) {
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
				try {
					if (dbConnection!=null) {
						dbConnection.close();
					}
				} catch (Exception e) {
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				}
			}
			return 0;
	}

	public static boolean doUpdateOrCreateFromScratch(ServletContext servletContext) {
		Connection coni = null;
		try {
			coni = InitDatabase.getConnection();
			if (hasTables(coni)) {
				LOGGER.info("Database schema exists.");
				int migrateIndex = -1;
				int dbVersion = getDBVersion(coni);
				if (dbVersion!=0) {
					LOGGER.info("Database schema version is " + dbVersion);
					if (dbVersion < UpgradeDatabase.DBVERSION && dbVersion > 200) {
						migrateIndex = getMigrateScriptIndex(dbVersion);
						runMigrateScripts(migrateIndex, servletContext);
						/*try {
							SiteDAO siteDAO = DAOFactory.getFactory().getSiteDAO();
							TSiteBean site = InitDatabase.getSiteConfiguration();
							site.setInstDate(new Long(new Date().getTime()).toString());
							siteDAO.save(site);
						} catch (ServletException e) {
							LOGGER.error(e.getMessage(), e);
						}*/	
					}
				}
			} else {
				// create new database scheme
				LOGGER.info("Creating a new database schema...");
				ApplicationStarter.getInstance().actualizePercentComplete(ApplicationStarter.getInstance().DB_TRACK_SCHEMA[0], ApplicationStarter.DB_SCHEMA_UPGRADE_SCRIPT_TEXT + " track-schema.sql...");
				runSQLScript("track-schema.sql", Math.round(ApplicationStarter.DB_TRACK_SCHEMA[1]*0.8f), 
						                         ApplicationStarter.getInstance().getProgressText());
				runSQLScript("id-table-schema.sql", Math.round(ApplicationStarter.DB_TRACK_SCHEMA[1]*0.1f), 
						      ApplicationStarter.DB_SCHEMA_UPGRADE_SCRIPT_TEXT + " id-table-schema.sql...");
				runSQLScript("quartz.sql", Math.round(ApplicationStarter.DB_TRACK_SCHEMA[1]*0.1f), 
						ApplicationStarter.DB_SCHEMA_UPGRADE_SCRIPT_TEXT + " quartz.sql...");
			}
			return true;	
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (coni != null) coni.close();
			} catch (Exception e) {
				LOGGER.info("Closing the connection failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
	}

	/**
	 * The database schema upgrades are considered to be 20%
	 * @param migrateIndex
	 * @return
	 */
	private static int getStep(int migrateIndex) {
		int numberOfMigrateScriptToExecute = migrateScripts.length-migrateIndex;
		if (numberOfMigrateScriptToExecute <= 0) {
			return 0;
		}
		int max = ApplicationStarter.DB_DATA_UPGRADE[ApplicationStarter.DB_DATA_UPGRADE.length-1];
		int step = max / numberOfMigrateScriptToExecute;
		return step;
	}

	/*
	 * Run the database migration scripts
	 */
	private static void runMigrateScripts(int migrateIndex, ServletContext servletContext) {
		int step = getStep(migrateIndex);
		for (int index=migrateIndex; index < migrateScripts.length; ++index ) {
			String migrateScript = migrateScripts[index];
			ApplicationStarter.getInstance().actualizePercentComplete(0, ApplicationStarter.DB_SCHEMA_UPGRADE_SCRIPT_TEXT + migrateScript + "...");
			runSQLScript(migrateScript, step, ApplicationStarter.getInstance().getProgressText());
		}

	}

	/**
	 * Run an SQL script
	 * @param script
	 */
	@SuppressWarnings("resource")
	private static void runSQLScript(String script, int maxValue, String progressText) {
		String folderName = getDbScriptFolder();
		int line = 0;
		int noOfLines = 0;
		int progress = 0;
		Connection cono = null;
		try {
			long start = new Date().getTime();
			cono = InitDatabase.getConnection();
			cono.setAutoCommit(false);
			Statement ostmt = cono.createStatement();
			script = "/dbase/" + folderName + "/" + script;
			URL populateURL = ApplicationBean.getApplicationBean().getServletContext().getResource(script);
			//LOGGER.info("Script URL: " + populateURL.toString());
			InputStream in = populateURL.openStream();
			java.util.Scanner s = new java.util.Scanner(in, "UTF-8").useDelimiter(";");
			while (s.hasNext()) {
				++noOfLines;
				s.nextLine();
			}
			int mod = noOfLines / maxValue;
			in.close();
			in = populateURL.openStream();
			s = new java.util.Scanner(in, "UTF-8").useDelimiter(";");
			String st = null;
			StringBuffer stb = new StringBuffer();		
			
			int modc = 0;
			progress = Math.round(new Float(mod)/new Float(noOfLines) * maxValue);
			
			LOGGER.info("Running SQL script " + script);
			while (s.hasNext()) {
				stb.append(s.nextLine().trim());
				st = stb.toString();
				++line;
				++modc;
				if (!st.isEmpty() && !st.startsWith("--") && !st.startsWith("/*") && !st.startsWith("#")) {
					if(st.trim().equalsIgnoreCase("go")){
						try{
							cono.commit();
						}catch (Exception ex){
							LOGGER.error(ExceptionUtils.getStackTrace(ex));
						}
						stb=new StringBuffer();
					}else{
						if (st.endsWith(";")) {
							stb = new StringBuffer(); // clear buffer
							st = st.substring(0, st.length()-1); // remove the semicolon
							try {
								if ("commit".equals(st.trim().toLowerCase())||
										"go".equals(st.trim().toLowerCase())) {
									cono.commit();
								} else {
									ostmt.executeUpdate(st);
									if (mod > 4 && (modc >= mod)) {
										modc = 0;
										ApplicationStarter.getInstance().actualizePercentComplete(progress, progressText);										
									}
									// LOGGER.info(st);
								}
							} catch (Exception exc) {
								if (!("Derby".equals(folderName) && exc.getMessage().contains("DROP TABLE") && exc.getMessage().contains("not exist"))) {
									LOGGER.error("Problem executing DDL statements: " + exc.getMessage());
									LOGGER.error("Line " + line + ": " + st);
								}
							}
						} else {
							stb.append(" ");
						}
					}
				} else {
					stb = new StringBuffer();
				}
			}
			in.close();
			cono.commit();
			cono.setAutoCommit(true);
			
			long now = new Date().getTime();
			LOGGER.info("Database schema creation took "+ (now - start)/1000 + " seconds.");

		}catch(Exception e) {
			LOGGER.error("Problem upgrading database schema in line " + line + " of file "
					+ script, e);
		} finally {
			try {
				if (cono != null) {
					cono.close();
				}
			} catch (Exception e) {
				LOGGER.info("Closing the connection failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
	}


	/*
	 * Returns the lowest index into the migration script table from where we have to start
	 */
	private static int getMigrateScriptIndex(int dbVersion) {
		int migrateIndex = -1;
		if (dbVersion < 300) {
			migrateIndex = 0;
		} else if (dbVersion < 310) {
			migrateIndex = 1;
		} else if (dbVersion < 320) {
			migrateIndex = 2;
		} else if (dbVersion < 330) {
			migrateIndex = 3;
		} else if (dbVersion < 340) {
			migrateIndex = 4;
		} else if (dbVersion < 350) {
			migrateIndex = 5;
		} else if (dbVersion < 370) {
			migrateIndex = 6;
		} else if (dbVersion < 380) {
			migrateIndex = 7;
		}  else if (dbVersion < 400) {
			migrateIndex = 8;
		} else if (dbVersion < 411) {
			migrateIndex = 9;
		} else if (dbVersion < 412) {
			migrateIndex = 10;
		} else if (dbVersion < 415) {
			migrateIndex = 11;
		} else if (dbVersion < 500) {
			migrateIndex = 12;
		}
		return migrateIndex;
	}

	/*
	 * Map the Torque database adapter name to our script folder name
	 */
	private static String getDbScriptFolder() {
		String folderName = null;
		try {
		PropertiesConfiguration tcfg = HandleHome.getTorqueProperties(
				ApplicationBean.getApplicationBean().getServletContext(), false);

		folderName = tcfg.getString("torque.database.track.adapter");

		if ("mysql".equals(folderName)) {
			folderName = "MySQL";
		} else if ("firebird".equals(folderName)){
			folderName = "Firebird";
		} else if ("oracle".equals(folderName)){
			folderName = "Oracle";
		} else if ("db2app".equals(folderName)){
			folderName = "DB2";
		} else if ("postgresql".equals(folderName)){
			folderName = "Postgres";
		} else if ("derby".equals(folderName)){
			folderName = "Derby";
		} else if ("mssql".equals(folderName)){
			folderName = "MSSQL";
		} else {
			// keep adapter name as folder name
		}
		} catch (Exception e) {
			LOGGER.error("Problem getting servlet: ", e);
		}
		return folderName;
	}

	/*
	 * Check if the database scheme has already been installed
	 */
		private static boolean hasTables(Connection conn)  {
			boolean hasTables = false;
			try {
				DatabaseMetaData md = conn.getMetaData();
				String userName=md.getUserName();
				String url=md.getURL();
				boolean isOracleOrDb2=url.startsWith("jdbc:oracle")||url.startsWith("jdbc:db2");
				ResultSet rsTables = md.getTables(conn.getCatalog(), isOracleOrDb2?userName:null, null, null);
				LOGGER.info("Getting the tables metadata");
				if (rsTables != null && rsTables.next()) {
					LOGGER.info("Find TSITE table...");
					//}
					/*
					ResultSet rsCatalogs = md.getCatalogs();
					System.out.println("====================================================================");
					System.out.println("Databases: ");
					while(rsCatalogs.next()) {
						System.out.println(" * " + rsCatalogs.getString("TABLE_CAT"));
					}


					// getTables(catalog, schemaPattern, tableNamePattern, types)
					rsTables = md.getTables(conn.getCatalog(), null, null, null);
					System.out.println("====================================================================");
					System.out.println("Tables: ");
					System.out.print("\n"); */
					while(rsTables.next()) {
						String tableName = rsTables.getString("TABLE_NAME");
						String tablenameUpperCase = tableName.toUpperCase();
						if ("TSITE".equals(tablenameUpperCase)) {
							LOGGER.info("TSITE table found");
							hasTables=true;
							break;
						} else {
							if (tablenameUpperCase.endsWith("TSITE")) {
								LOGGER.info(tablenameUpperCase + " table found");
								hasTables=true;
								break;
							}
						}
						/*System.out.print(" ** " + tableName);
						System.out.print(" || Type: " + rsTables.getString("TABLE_TYPE"));
						System.out.print(" || Catalog: " + rsTables.getString("TABLE_CAT"));
						System.out.println(" || Scheme: " + rsTables.getString("TABLE_SCHEM"));*/
						//getTables(catalog, schemaPattern, tableNamePattern, columnNamePattern)
					}
				}
			} catch (Exception e) {
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			if (hasTables==false) {
				Statement stmt = null;
				ResultSet rs = null;
				try {
					stmt = conn.createStatement();
					rs = stmt.executeQuery("SELECT OBJECTID FROM TSITE");
					if (rs.next()) {
						hasTables = true;
					}
				} catch (SQLException e) {
					LOGGER.info("Table TSITE  does not exist");
				} finally {
					if (rs!=null) {
						try {
							rs.close();
						} catch (SQLException e) {
						}
					}
					if (stmt!=null) {
						try {
							stmt.close();
						} catch (SQLException e) {
						}
					}
				}
			}
			return hasTables;
		}



}
