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


package com.aurel.track.dao;

import java.lang.annotation.Annotation;
import java.sql.Statement;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Table;
import javax.persistence.metamodel.EntityType;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.aurel.track.dbase.DatabaseHandler;
import com.trackplus.persist.TpEm;

@RunWith(Suite.class)
@SuiteClasses({ com.aurel.track.dao.SiteDAOTest.class
})

public class DAOTestSuite extends Thread {
	protected static DatabaseHandler db;
	protected static Boolean startFromScratch = true;

	@BeforeClass
	public static void setupEverything() {

		DatabaseHandler.cleanDatabase();
		DatabaseHandler.startDbServer();

		
		EntityManagerFactory emf  = Persistence.createEntityManagerFactory("com.trackplus.test");
		EntityManager entityManager = emf.createEntityManager();
		TpEm.initEntityManagerFactory("com.trackplus.test");
		
		fillIDTable(entityManager);

	}

	@AfterClass
	public static void tearDownEverything() {
		DatabaseHandler.stopDbServer();
	}
	

	
	private static void fillIDTable(EntityManager entityManager) {
		// Now fill the ID_TABLE
		Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
		try {
			entityManager.getTransaction().begin();
			int index = 0;
			for (EntityType<?> ent : entities) {
				Class<?> clazz = ent.getJavaType();
				String tableName = null;
				try {
					Annotation ann = clazz.getAnnotation(Table.class);
					tableName = (String) ann.annotationType().getMethod("name").invoke(ann);
				} catch (Exception e) {
					// ignore
				}
				if (tableName == null) {
					tableName = ent.getName().toUpperCase();
				}
				insertIDTableRow(tableName, entityManager, ++index);
			}
			entityManager.getTransaction().commit();
		} catch(Exception e) {

		}
	}
	
	/*
	 * Initialize the tables for the table sequence generator
	 */
	private static void insertIDTableRow(String table, EntityManager em, int index) {
		try {
			java.sql.Connection connection = em.unwrap(java.sql.Connection.class);
			Statement stmt = connection.createStatement();
			String insertStmt = "INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY)"
					+ " VALUES ("+index+",'" + table + "', 10, 10)";
			System.err.println(insertStmt);
			stmt.execute(insertStmt);
			stmt.close();
		} catch (Exception e) {
			System.err.println("Problem initializing ID_TABLE");
		}
	}

}
