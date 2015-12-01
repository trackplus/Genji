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

package com.aurel.track;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.firebirdsql.gds.impl.GDSType;
import org.firebirdsql.management.FBManager;

//import com.ibm.db2.jcc.DB2Driver;


public class DBScriptTest {

	private final static String MY_SQL = "mysql";
	private final static String MS_SQL = "mssql";
	private final static String POSTGRES = "postgree";
	private final static String FIREBIRD = "firebird";
	private final static String ORACLE = "oracle";
	private final static String DB2 = "db2";

	private final static String DB_NAME = "trackt5";

	/***************************** HELPERS *****************************/

	public PropertiesConfiguration getRealPropertyObj (String dbTypePrefix) {
		try {
			String propFilePath = System.getProperty("user.dir") + "/src/test/resources/schema/DBScriptTester.properties";
			FileInputStream input = new FileInputStream(propFilePath);
			Properties  prop = new Properties();
			prop.load(input);
			PropertiesConfiguration  props = new PropertiesConfiguration ();
			props.addProperty("torque.dsfactory.track.connection.user", prop.get(dbTypePrefix + "user"));
			props.addProperty("torque.dsfactory.track.connection.password", prop.get(dbTypePrefix + "password"));
			props.addProperty("torque.database.track.adapter", prop.get(dbTypePrefix + "adapter"));
			props.addProperty("torque.dsfactory.track.connection.driver", prop.get(dbTypePrefix + "driver"));
			props.addProperty("torque.dsfactory.track.connection.url", prop.get(dbTypePrefix + "url"));
			props.addProperty("torque.dsfactory.track.factory","org.apache.torque.dsfactory.SharedPoolDataSourceFactory");
			props.addProperty("torque.dsfactory.track.pool.maxActive","30");
			props.addProperty("torque.dsfactory.track.pool.testOnBorrow","true");
			props.addProperty("torque.dsfactory.track.pool.validationQuery","SELECT PKEY FROM TSTATE");
			return props;
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public PropertiesConfiguration getPropertiesConfigForDB(String dbType) {
		String dbTypePrefix = "";
		switch (dbType) {
			case MY_SQL:
				dbTypePrefix = MY_SQL + ".";
				return getRealPropertyObj(dbTypePrefix);
			case MS_SQL:
				dbTypePrefix = MS_SQL + ".";
				return getRealPropertyObj(dbTypePrefix);
			case POSTGRES:
				dbTypePrefix = POSTGRES + ".";
			return getRealPropertyObj(dbTypePrefix);
			case FIREBIRD:
				dbTypePrefix = FIREBIRD + ".";
			return getRealPropertyObj(dbTypePrefix);
			case DB2:
				dbTypePrefix = DB2 + ".";
			return getRealPropertyObj(dbTypePrefix);
			case ORACLE:
				dbTypePrefix = ORACLE + ".";
			return getRealPropertyObj(dbTypePrefix);
			default:
				System.out.println("Please implement me (fn: getPropertiesConfigForDB()) for: " + dbType);
				return null;
		}
	}
	private String getScriptFolder(String folderName) {
		String script = System.getProperty("user.dir") + "/src/main/webapp/dbase/" + folderName + "/";
		return script;
	}

	private String getOldScriptFolder(String folderName) {
		String script = System.getProperty("user.dir") + "/src/test/resources/OldDbScripts/" + folderName + "/";
		return script;
	}


	public Connection getConnection(String dbType, boolean connectToDBSystem) {
		Connection con = null;
		String url = new String();
		try {
			PropertiesConfiguration tcfg = getPropertiesConfigForDB(dbType);
			if(connectToDBSystem) {
				url =  tcfg.getString("torque.dsfactory.track.connection.url");
				if(POSTGRES.equals(dbType)) {
					url += "/postgres";
				}

			}else {
				if(dbType.equals(MY_SQL)) {
					String tmpUrl = tcfg.getString("torque.dsfactory.track.connection.url");
					url = tmpUrl.substring(0, tmpUrl.indexOf("?")) + "/" + DB_NAME + tmpUrl.substring(tmpUrl.indexOf("?"), tmpUrl.length());
				}
				if(dbType.equals(MS_SQL) || dbType.equals(POSTGRES) || dbType.equals(DB2)) {
					url = tcfg.getString("torque.dsfactory.track.connection.url") + "/" + DB_NAME;
				}

				if(dbType.equals(FIREBIRD)) {
					url = tcfg.getString("torque.dsfactory.track.connection.url") + "/" + DB_NAME + ".FDB";
				}

				if(dbType.equals(ORACLE)) {
					url = tcfg.getString("torque.dsfactory.track.connection.url");
				}
			}
			Class.forName(tcfg.getString("torque.dsfactory.track.connection.driver"));
			con = DriverManager.getConnection(url,
					tcfg.getString("torque.dsfactory.track.connection.user"),
					tcfg.getString("torque.dsfactory.track.connection.password"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}


	/**
	 * Run an SQL script
	 * @param script
	 */
	private void runSQLScript(String scriptToRunWithPath, Connection cono) {
		int line = 0;
		try {
			cono.setAutoCommit(false);
			Statement ostmt = cono.createStatement();
			InputStream in = new FileInputStream(scriptToRunWithPath);//populateURL.openStream();
			java.util.Scanner s = new java.util.Scanner(in, "UTF-8").useDelimiter(";");
			String st = null;
			StringBuffer stb = new StringBuffer();
			System.out.println("Running SQL script " + scriptToRunWithPath);
			while (s.hasNext()) {
				stb.append(s.nextLine().trim());
				st = stb.toString();
				++line;
				if (!st.isEmpty() && !st.startsWith("--") && !st.startsWith("/*") && !st.startsWith("#")) {
					if(st.trim().equalsIgnoreCase("go")){
						try{
							cono.commit();
						}catch (Exception ex){
							System.err.println(org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(ex));
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
									// LOGGER.info(st);
								}
							} catch (Exception exc) {
								if (!(scriptToRunWithPath.contains("Derby") && exc.getMessage().contains("DROP TABLE") && exc.getMessage().contains("not exist"))) {
									System.err.println("Problem executing DDL statements: " + exc.getMessage());
									System.err.println("Line " + line + ": " + st);
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

		}catch(Exception e) {
			System.err.println("Problem upgrading database schema in line " + line + " of file " + scriptToRunWithPath + "  " +  e);
		}
	}
	/***************************** ENDS OF HELPERS *****************************/


	public void runMYSQLScripts(String[] scriptsToRun) {
		Connection conDBSys = getConnection(MY_SQL, true);
		Connection conToCreatedDB = null;
		Statement stmtDBSys = null;
		if(conDBSys!=null) {
			  try {
				  stmtDBSys = conDBSys.createStatement();
				  try {
					  stmtDBSys.executeUpdate("DROP DATABASE " + DB_NAME);
				  }catch(Exception ex) {
				  }
				  stmtDBSys.executeUpdate("CREATE DATABASE " + DB_NAME);
				  conToCreatedDB = getConnection(MY_SQL, false);
				  for(int i = 0; i < scriptsToRun.length; i++) {
					  runSQLScript(scriptsToRun[i], conToCreatedDB);
				  }
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try{
					stmtDBSys.close();
					conDBSys.close();
					conToCreatedDB.close();
				  }catch(Exception ex) {
					  ex.printStackTrace();
				  }
			}
		}
	}

	public void runMsSQLScripts(String[] scriptsToRun) {
		Connection conDBSys = getConnection(MS_SQL, true);
		Connection conToCreatedDB = null;
		Statement stmtDBSys = null;
		if(conDBSys!=null) {
			  try {
				  stmtDBSys = conDBSys.createStatement();
				  try {
					  stmtDBSys.executeUpdate("DROP DATABASE " + DB_NAME);
				  }catch(Exception ex) {
					  ex.printStackTrace();
				  }
				  stmtDBSys.executeUpdate("CREATE DATABASE " + DB_NAME);
				  conToCreatedDB = getConnection(MS_SQL, false);
				  for(int i = 0; i < scriptsToRun.length; i++) {
					  runSQLScript(scriptsToRun[i], conToCreatedDB);
				  }
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try{
					stmtDBSys.close();
					conDBSys.close();
					conToCreatedDB.close();
				  }catch(Exception ex) {
					  ex.printStackTrace();
				  }
			}
		}
	}

	public void runPostgreScripts(String[] scriptsToRun) {
		Connection conDBSys = getConnection(POSTGRES, true);
		Connection conToCreatedDB = null;
		Statement stmtDBSys = null;
		if(conDBSys!=null) {
			  try {
				  stmtDBSys = conDBSys.createStatement();
				  try {

					  stmtDBSys.executeUpdate("DROP DATABASE " + DB_NAME);
				  }catch(Exception ex) {
					  ex.printStackTrace();
				  }
				  stmtDBSys.executeUpdate("CREATE DATABASE " + DB_NAME);
				  conToCreatedDB = getConnection(POSTGRES, false);
				  for(int i = 0; i < scriptsToRun.length; i++) {
					  runSQLScript(scriptsToRun[i], conToCreatedDB);
				  }
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try{
					stmtDBSys.close();
					conDBSys.close();
					conToCreatedDB.close();
				  }catch(Exception ex) {
					  ex.printStackTrace();
				  }
			}
		}
	}

	public void runFirebirdScripts(String[] scriptsToRun) {
		Connection conToCreatedDB = null;
		conToCreatedDB = getConnection(FIREBIRD, false);
		try {
			for(int i = 0; i < scriptsToRun.length; i++) {
				runSQLScript(scriptsToRun[i], conToCreatedDB);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				conToCreatedDB.close();
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public void runDB2Scripts(String[] scriptsToRun) {
		Connection conToCreatedDB = null;
		try {
			conToCreatedDB = getConnection(DB2, false);
			for(int i = 0; i < scriptsToRun.length; i++) {
				runSQLScript(scriptsToRun[i], conToCreatedDB);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				conToCreatedDB.close();
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public void runOracleScripts(String[] scriptsToRun) {
		Connection conToCreatedDB = null;
		try {
			conToCreatedDB = getConnection(ORACLE, false);
			for(int i = 0; i < scriptsToRun.length; i++) {
				runSQLScript(scriptsToRun[i], conToCreatedDB);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				conToCreatedDB.close();
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}

//	static{
//
//		}

	public static void main(String[] args) {
		DBScriptTest db = new DBScriptTest();

//		String[] runMySQLFromScratch = {db.getScriptFolder("MySQL") + "track-schema.sql"};
//		db.runMYSQLScripts(runMySQLFromScratch);

//		String[] runMySQLFrom382 = {db.getOldScriptFolder("MySQL") + "track-schema382.sql",
//									db.getScriptFolder("MySQL") + "migrate380to400.sql",
//									db.getScriptFolder("MySQL") + "migrate400to410.sql",
//									db.getScriptFolder("MySQL") + "migrate410to412.sql",
//									db.getScriptFolder("MySQL") + "migrate415to500.sql",
//									};
//		db.runMYSQLScripts(runMySQLFrom382);


//		String[] runMsSQLFromScratch = {db.getScriptFolder("MSSQL") + "track-schema.sql"};
//		db.runMsSQLScripts(runMsSQLFromScratch);
//		String[] runMsSQLFrom382 = {db.getOldScriptFolder("MSSQL") + "track-schema382.sql",
//									db.getScriptFolder("MSSQL") + "migrate380to400.sql",
//									db.getScriptFolder("MSSQL") + "migrate400to410.sql",
//									db.getScriptFolder("MSSQL") + "migrate410to412.sql",
//									db.getScriptFolder("MSSQL") + "migrate415to500.sql",
//									};
//		db.runMsSQLScripts(runMsSQLFrom382);

//		String[] runPostgresFromScratch = {db.getScriptFolder("Postgres") + "track-schema.sql"};
//		db.runPostgreScripts(runPostgresFromScratch);
//		String[] runPostgresFrom382 = {db.getOldScriptFolder("Postgres") + "track-schema382.sql",
//									   db.getOldScriptFolder("Postgres") + "id-table-schema.sql",
//									   db.getScriptFolder("Postgres") + "migrate380to400.sql",
//									   db.getScriptFolder("Postgres") + "migrate400to410.sql",
//									   db.getScriptFolder("Postgres") + "migrate410to412.sql",
//									   db.getScriptFolder("Postgres") + "migrate415to500.sql",
//									   };
//		db.runPostgreScripts(runPostgresFrom382);


//		String[] runFirebirdFromScratch = {db.getScriptFolder("Firebird") + "track-schema.sql"};
//		db.runFirebirdScripts(runFirebirdFromScratch);
//		String[] runFirebirdFrom382 = {db.getOldScriptFolder("Firebird") + "track-schema382.sql",
//										db.getOldScriptFolder("Firebird") + "id-table-schema.sql",
//										db.getScriptFolder("Firebird") + "migrate380to400.sql",
//										db.getScriptFolder("Firebird") + "migrate400to410.sql",
//										db.getScriptFolder("Firebird") + "migrate410to412.sql",
//										db.getScriptFolder("Firebird") + "migrate415to500.sql",
//										};
//		db.runFirebirdScripts(runFirebirdFrom382);



//		String[] runDB2FromScratch = {db.getScriptFolder("DB2") + "track-schema.sql"};
//		db.runDB2Scripts(runDB2FromScratch);
		String[] rundb2From382 = {db.getOldScriptFolder("DB2") + "track-schema415.sql",
								  db.getOldScriptFolder("DB2") + "id-table-schema.sql",
								  db.getScriptFolder("DB2") + "migrate415to500.sql",
								 };
		db.runDB2Scripts(rundb2From382);


//		String[] runOracleScripts = {db.getScriptFolder("Oracle") + "track-schema.sql"};
//		db.runOracleScripts(runOracleScripts);
//		String[] runOracleFrom382 = {db.getOldScriptFolder("Oracle") + "track-schema382.sql",
//				db.getOldScriptFolder("Oracle") + "id-table-schema.sql",
//				db.getScriptFolder("Oracle") + "migrate380to400.sql",
//				db.getScriptFolder("Oracle") + "migrate400to410.sql",
//				db.getScriptFolder("Oracle") + "migrate410to412.sql",
//				db.getScriptFolder("Oracle") + "migrate415to500.sql",
//				};
//		db.runOracleScripts(runOracleFrom382);


	}

}
