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

package com.aurel.track.prop;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.admin.server.logging.LoggingConfigBL;
import com.aurel.track.dbase.jobs.BanUpdateJob;

/**
 * <p>
 * Bans clients when they repeatedly fail to authenticate themselves.
 * BanProcessor is a singleton.</p>
 * <p>
 * This does not work well if clients are hidden behind proxies, because
 * the application sees only the proxy IP and bans all clients that hide
 * behind that proxy. In that case we have to reduce the ban time in
 * file in <code>WEB-INF/quartz-jobs.xml</code>.
 *
 */
public class BanProcessor {
	
	private static final Logger LOGGER = LogManager.getLogger(BanProcessor.class);
	
	public HashMap<String, BanEntry> banMap = new HashMap<String, BanEntry>();
	private static int banTime = 60*60*1000;  // defaults to 60 minutes, but is overwritten below
	private static int maxNoOfBadAttempts = 15;
	private static BanProcessor bp = null;
	
	private BanProcessor() {

	}
	
	/**
	 * 
	 * @return a BanProcessor singleton object.
	 */
	public static synchronized BanProcessor getBanProcessor() {
		if (bp == null) {
			bp = new BanProcessor();
			bp.setBanTime(BanUpdateJob.getBanTime());
			bp.setMaxNoOfBadAttempts(BanUpdateJob.getNoOfBadAttempts());
		}
		LoggingConfigBL.setLevel(LOGGER, Level.INFO);
		return bp;
	}

	/**
	 * Set the time in minutes this ip is banned.
	 * @param _banTime in minutes
	 */
	public static void setBanTime(int _banTime) {
	   banTime = _banTime*60*1000;	
	}
	
	/**
	 * Set the maximum number of bad attempts before the ban is enforced.
	 * @param maxatt number of bad attempts permitted
	 */
	public static void setMaxNoOfBadAttempts(int maxatt) {
		maxNoOfBadAttempts = maxatt;
	}
	
	
	/**
	 * If there was a bad attempt, call this function to mark it.
	 * @param ipNumber the IP number of the client with the bad attempt.
	 */
	public void markBadAttempt(String ipNumber) {
		BanEntry be = banMap.get(ipNumber);
		if (be == null) {
			be = new BanEntry();
		}
		be.setLastBadAttemptDate(new Date());
		be.setNumberOfBadAttempts(be.getNumberOfBadAttempts()+1);
		if (be.getNumberOfBadAttempts() >= maxNoOfBadAttempts) {
			LOGGER.info("Client " + ipNumber + " has been banned until " +
					new Date(be.getLastBadAttemptDate().getTime() + banTime));
		}
		banMap.put(ipNumber, be);
	}
	
	/**
	 * If there was a good attempt, you can remove the ban with this function.
	 * @param ipNumber the IP number of the client with the bad attempt.
	 */
	public void removeBanEntry(String ipNumber) {
		banMap.remove(ipNumber);
	}
	
	/**
	 * Check if this client is banned. If there have been repeated attempts to break
	 * in the client is banned for the BANTIME.
	 * @param ipNumber the IP number of the client to be checked for.
	 * @return
	 */
	public boolean isBanned(String ipNumber) {
		BanEntry be = banMap.get(ipNumber);
		if (be == null) {
			return false;
		}
		Date now = new Date();
		if ((be.getNumberOfBadAttempts() > maxNoOfBadAttempts) && (be.getLastBadAttemptDate().getTime() + banTime > now.getTime())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * This needs to be regularly called (by a job) to reset old bans. Otherwise clients
	 * would be permanently banned until the next server restart.
	 */
	public void updateBanMap() {
		Set<String> keys = banMap.keySet();
		Iterator<String> it = keys.iterator();
		Date now = new Date();
		while (it.hasNext()) {
			String key = it.next();
			BanEntry be = banMap.get(key);
			if (be.getLastBadAttemptDate().getTime() + banTime < now.getTime()){
				banMap.remove(key);
			}
		}
	}
	
	public class BanEntry {
		
		private Date lastBadAttemptDate;
		private int numberOfBadAttempts = 0;
		
		public Date getLastBadAttemptDate() {
			return lastBadAttemptDate;
		}
		
		public void setLastBadAttemptDate(Date lastBadAttemptDate) {
			this.lastBadAttemptDate = lastBadAttemptDate;
		}
		public int getNumberOfBadAttempts() {
			return numberOfBadAttempts;
		}
		public void setNumberOfBadAttempts(int numberOfBadAttempts) {
			this.numberOfBadAttempts = numberOfBadAttempts;
		}
		
	}

}
