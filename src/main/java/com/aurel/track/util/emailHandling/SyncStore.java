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

package com.aurel.track.util.emailHandling;

import java.util.ArrayList;

import javax.mail.Store;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.LogThrottle;

/**
 * @author Joerg Friedrich
 *
 * This class holds a single POP3 connection point including its parameters.
 * Since POP3 connection points cannot be used simultaneously we need to
 * coordinate access to them. This is done via this class, which permits to
 * check if this connection point is already being used. If so, one can 
 * retrieve the appropriate store. Otherwise, one can create a new connection
 * to it.
 */
public class SyncStore {
    
	private static final Logger LOGGER = LogManager.getLogger(SyncStore.class);
    
    static ArrayList<ConnectionPoint> connectionPoints = null;
    

    public synchronized static boolean connect (Store _store,
                                             String _protocol,
                                             String _host,
                                             int _port,
                                             String _user, String _password) {
        ConnectionPoint cp = getConnPoint(_store,
                                          _protocol,
                                          _host,
                                          _port,
                                          _user, _password);
        boolean connected = false;
        try {
            Store store = cp.getStore();
            if (!store.isConnected()) {
                cp.getStore().connect(_host, _port, _user, _password );
             }
            ++cp.connectionCount;
            LOGGER.debug("Connecting store: connectionCount = " + cp.connectionCount);
            connected = true;
        }
        catch (Exception e) {
        	connected = false;
        	if (LogThrottle.isReady("Syncstore1",240)) {
            LOGGER.warn("Could not connect..." + e.getMessage());
        	}
            LOGGER.debug(ExceptionUtils.getStackTrace(e));
        }
        return connected;
    }
    
    public synchronized static void close (Store _store,String _protocol,
                                           String _host,
                                           int _port,
                                           String _user,
                                           String _password) {
        ConnectionPoint cp = getConnPoint(_store,
                                          _protocol,
                                          _host,
                                          _port,
                                          _user, _password);        
        cp.close();
    }
    
   
    /**
     * This class returns a connection point for this set of parameters.
     * If there is already an existing one, this is being used.
     * @param protocol TODO
     * @param host
     * @param port
     * @param user
     * @param password
     * @return
     */
    public synchronized static ConnectionPoint getConnPoint(Store _store,
                                                            String protocol,
                                                            String host,
                                                            int port,
                                                            String user, String password) {
        
        ConnectionPoint cp = new ConnectionPoint(protocol,host, port, 
                                                 user, password);
        if (connectionPoints == null) {
            connectionPoints = new ArrayList();
            connectionPoints.add(cp);
            cp.setStore(_store);
            LOGGER.debug("Adding new connection point " + host + ":" + user);
        }
        else {
            ConnectionPoint spo = null;
            for (int i = 0; i < connectionPoints.size(); ++i) {
                spo = (ConnectionPoint) connectionPoints.get(i);
                if (spo.equals(cp)) {
                    break;
                }
                else {
                    spo = null;
                }
            }
            // did not find a matching connection point, can create a new one
            if (spo == null) {
                LOGGER.debug("Adding new connection point to list in SyncStore :" + host + ":" + user);
                //and also set the store
                cp.setStore(_store);
                connectionPoints.add(cp);
            }
            else {
                cp = spo;
                LOGGER.debug("Reusing existing connection point ... ");
            }
        }
        return cp;
    }

    /*
     * This inner class provides a convenient way of handling a single
     * access point to a POP3 server account. 
     */
    public static class ConnectionPoint {
    	private String protocol="pop3";
        private String host = null;
        private int port = 110;
        private String user = null;
        private String password = null;
        private Store store = null;
        private int connectionCount = 0;
        

        public ConnectionPoint (String protocol,String host,
                                int port,
                                String user,
                                String password) {
        	this.protocol=protocol;
            this.host = host;
            this.port = port;
            this.user = user;
            this.password = password;
            this.connectionCount = 0;
        }

        
        @Override
        public boolean equals(Object obj) {
			if (obj instanceof ConnectionPoint) {
				ConnectionPoint sp=(ConnectionPoint)obj;
				if (EqualUtils.equal(this.protocol, sp.getProtocol())
					&& EqualUtils.equal(this.host, sp.getHost())
					&& this.port == sp.getPort()
					&& EqualUtils.equal(this.user, sp.getUser())
					&& EqualUtils.equal(this.password, sp.getPassword())) {
					return true;
				}
				else {
					return false;
				}
			}
			return false;
        }
		@Override
		public int hashCode() {
			StringBuilder sb=new StringBuilder();
			sb.append(protocol).append(":");
			sb.append(host).append(":");
			sb.append(port).append(":");
			sb.append(user).append(":");
			sb.append(password);
			return sb.toString().hashCode();

		}


        
        public synchronized void close() {
            try {
                --connectionCount;
                LOGGER.debug("Closing connection: connectionCount = " + connectionCount);
                if (connectionCount == 0 && this.store.isConnected()) {
                    this.store.close();
                    LOGGER.debug("Closing store");
                }
            }
            catch (Exception e) {
                LOGGER.warn("Could not close this connection..." + e.getMessage());
                LOGGER.debug(ExceptionUtils.getStackTrace(e));
            }
        }

        
        /**
         * @return Returns the store.
         */
        public synchronized Store getStore() {
            return store;
        }

        /**
         * @param store The store to set.
         */
        public synchronized void setStore(Store store) {
            this.store = store;
        }
        /**
         * @return Returns the host.
         */
        public synchronized String getHost() {
            return host;
        }
        /**
         * @param host The host to set.
         */
        public synchronized void setHost(String host) {
            this.host = host;
        }
        /**
         * @return Returns the password.
         */
        public synchronized String getPassword() {
            return password;
        }
        /**
         * @param password The password to set.
         */
        public synchronized void setPassword(String password) {
            this.password = password;
        }
        /**
         * @return Returns the port.
         */
        public synchronized int getPort() {
            return port;
        }
        /**
         * @param port The port to set.
         */
        public synchronized void setPort(int port) {
            this.port = port;
        }
        /**
         * @return Returns the user.
         */
        public synchronized String getUser() {
            return user;
        }
        /**
         * @param user The user to set.
         */
        public synchronized void setUser(String user) {
            this.user = user;
        }


		/**
		 * @return the protocol
		 */
		public synchronized String getProtocol() {
			return protocol;
		}


		/**
		 * @param protocol the protocol to set
		 */
		public synchronized void setProtocol(String protocol) {
			this.protocol = protocol;
		}
        
    }
}
