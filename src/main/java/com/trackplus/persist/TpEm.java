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

package com.trackplus.persist;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.SynchronizationType;

import org.apache.commons.configuration.PropertiesConfiguration;

/*
 * This is the central application wide EclipseLink entity manager
 */
public class TpEm {
	
	private static EntityManagerFactory emf = null;

	public static void initEntityManagerFactory(PropertiesConfiguration tcfg, String persistUnit) {
		Properties properties = new Properties();
		properties.put("javax.persistence.jdbc.driver", tcfg.getProperty("torque.dsfactory.track.connection.driver"));
		properties.put("javax.persistence.jdbc.url", tcfg.getProperty("torque.dsfactory.track.connection.url"));
        properties.put("javax.persistence.jdbc.user", tcfg.getProperty("torque.dsfactory.track.connection.user"));
        properties.put("javax.persistence.jdbc.password", tcfg.getProperty("torque.dsfactory.track.connection.password"));
		emf  = Persistence.createEntityManagerFactory(persistUnit, properties);
	}
	
	public static void initEntityManagerFactory(String persistUnit) {
		emf  = Persistence.createEntityManagerFactory(persistUnit);
	}
	
	public static EntityManager getEntityManager() {
		return emf.createEntityManager();
	}
	
	public static EntityManager getEntityManager(SynchronizationType st) {
		return emf.createEntityManager(st);
	}
	
	public static EntityManagerFactory initEntityManagerFactory() {
		return emf;
	}

}
