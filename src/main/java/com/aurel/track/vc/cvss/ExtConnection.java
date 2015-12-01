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

package com.aurel.track.vc.cvss;

/**
 * An ext connection implementation for CVS
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.netbeans.lib.cvsclient.command.CommandAbortedException;
import org.netbeans.lib.cvsclient.connection.AbstractConnection;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.connection.ConnectionModifier;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;
import org.netbeans.lib.cvsclient.util.LoggedDataOutputStream;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

class ExtConnection extends AbstractConnection {
	
	private static final Logger LOGGER = LogManager.getLogger(ExtConnection.class);
	
    private Connection  connection;
    private  BufferedReader stderrReader;
    Session session=null;
    String host;
    int port;
    String userName;
    String privateKey;
    String passphrase;
    String password;
    
    public ExtConnection(String host,String userName) {
        this.host = host;
        this.userName = userName;
    }

    @Override
    public void open() throws AuthenticationException, CommandAbortedException {
        if(port==0){
            connection = new Connection(host);
        }else{
            connection = new Connection(host,port);
        }
        try {
            connection.connect();
        }
        catch ( IOException e ) {
            String message = "Cannot connect. Reason: "+ e.getMessage();
            throw new AuthenticationException( message, e, message );
        }
        boolean auth = false;
        try {
            if(privateKey!=null){
                auth = connection.authenticateWithPublicKey( userName, privateKey.toCharArray(),passphrase);
            }else{
                auth = connection.authenticateWithPassword(userName,password);
            }
        } catch (IOException e1) {
            String message = "Authentication failed:"+e1.getMessage();
            throw new AuthenticationException( message, message );
        }
        if (!auth ){
            String message = "Authentication failed.";
            throw new AuthenticationException( message, message );
        }
        try{
            session = connection.openSession();
        }
        catch ( IOException e ) {
            String message = "Cannot open session. Reason: "+ e.getMessage();
            throw new CommandAbortedException( message, message );
        }
        String command = "cvs server";
        try{
            session.execCommand( command );
        }
        catch ( IOException e ){
            String message = "Cannot execute remote command: "+command;
            throw new CommandAbortedException( message, message );
        }
        InputStream stdout = new StreamGobbler(session.getStdout());
        InputStream stderr = new StreamGobbler(session.getStderr());
        stderrReader = new BufferedReader(new InputStreamReader( stderr));
        setInputStream(new LoggedDataInputStream(stdout));
        setOutputStream(new LoggedDataOutputStream(session.getStdin()));
    }
    
    
    @Override
    public void verify() {
        try {
            open();
            verifyProtocol();
            close();
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
    }

    
    private void closeConnection() {
       if ( session != null ){
           session.close();
       }
       if ( connection != null ){
           connection.close();
       }
       reset();
   }

   private void reset(){
       connection = null;
       session = null;
       stderrReader = null;
       setInputStream(null);
       setOutputStream(null);
   }

   @Override
   public void close() {
       closeConnection();
   }

    @Override
    public boolean isOpen() {
       return connection != null;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public void modifyInputStream(ConnectionModifier modifier) throws IOException {
        modifier.modifyInputStream(getInputStream());
    }

    @Override
    public void modifyOutputStream(ConnectionModifier modifier) throws IOException {
        modifier.modifyOutputStream(getOutputStream());
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public String getUserName() {
        return userName;
    }

    public void setPort(int port) {
        this.port = port;
    }
}


