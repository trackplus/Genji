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


package com.trackplus.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.torque.Torque;
import org.apache.torque.TorqueException;

import com.aurel.track.persist.BaseTWorkItemPeer;
import com.trackplus.persist.TSitePeer;


public class DAOFactory {

	private static final Logger LOGGER = LogManager.getLogger(DAOFactory.class);
	private static DAOFactory instance;

//
//
//
//

	public static DAOFactory getInstance(){
		if(instance==null){
			 instance=new DAOFactory();
		}
		return instance;
	}


	public void executeUpdateStatement(String sqlStatement) {
		Connection db = null;
		try {
			//get the database name from any peer
			db = Torque.getConnection(BaseTWorkItemPeer.DATABASE_NAME);
			// it's the same name for all tables here, so we don't care
			Statement stmt;
			stmt = db.createStatement();
			stmt.executeUpdate(sqlStatement);
		} catch (TorqueException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		} catch (SQLException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		} finally {
			Torque.closeConnection(db);
		}
	}

	/* A complete list with getters you can find in a previous version on SVN */


	public static SiteDAO getSiteDAO() {
		return new TSitePeer();
	}


}
