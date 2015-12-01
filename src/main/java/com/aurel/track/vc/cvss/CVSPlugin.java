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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.netbeans.lib.cvsclient.CVSRoot;
import org.netbeans.lib.cvsclient.Client;
import org.netbeans.lib.cvsclient.admin.StandardAdminHandler;
import org.netbeans.lib.cvsclient.command.CommandAbortedException;
import org.netbeans.lib.cvsclient.command.CommandException;
import org.netbeans.lib.cvsclient.command.GlobalOptions;
import org.netbeans.lib.cvsclient.command.log.LogCommand;
import org.netbeans.lib.cvsclient.command.log.LogInformation;
import org.netbeans.lib.cvsclient.command.log.RlogCommand;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.connection.Connection;
import org.netbeans.lib.cvsclient.connection.PServerConnection;
import org.netbeans.lib.cvsclient.event.CVSAdapter;
import org.netbeans.lib.cvsclient.event.FileInfoEvent;

import com.aurel.track.util.LabelValueBean;
import com.aurel.track.vc.AbstractVersionControlPlugin;
import com.aurel.track.vc.FileDiff;
import com.aurel.track.vc.Revision;

/**
 * An version control plugin for cvs
 * @author Adrian Bojani
 *
 */
public class CVSPlugin  extends AbstractVersionControlPlugin{
	private static final Logger LOGGER = LogManager.getLogger(CVSPlugin.class);
	public static final String NO_ACCESS_METHOD_ERROR="plugins.versionControl.error.noAccessMethod";
	public static final String NO_USER_NAME_ERROR="plugins.versionControl.error.noUserName";
	public static final String NO_SERVER_NAME_ERROR="plugins.versionControl.error.serverName";
	public static final String NO_REPOSITORY_PATH_ERROR="plugins.versionControl.error.repositoryPath";

	public static final String ACCESS_METHOD_FIELD="plugins.versionControl.accessMethod";
	public static final String USER_NAME_FIELD="plugins.versionControl.userName";
	public static final String PWD_FIELD="plugins.versionControl.password";
	public static final String SERVER_NAME_FIELD="plugins.versionControl.serverName";
	public static final String REPOSITORY_PATH_FIELD="plugins.versionControl.repositoryPath";

	@Override
	public List<LabelValueBean> verify(Map<String,String> parameteres,Locale locale){
		LOGGER.info("Verify cvs parameters...");
		List<LabelValueBean> errors=new ArrayList<LabelValueBean>();
		String accesMethod= parameteres.get("accessMethod");
		String userName=parameteres.get("userName");
		String serverName=parameteres.get("serverName");
		String repositoryPath=parameteres.get("repositoryPath");
		String password=parameteres.get("password");
		String passphrase=parameteres.get("passphrase");
		String ppk=parameteres.get("privateKey");
		String cvsRoot = ":"+accesMethod+":"+userName+"@"+serverName+":"+repositoryPath;
		LOGGER.info("Using cvsRoot = " + cvsRoot);
		//get old password and passphrase
		if(accesMethod==null||accesMethod.length()==0){
			errors.add(new LabelValueBean(getText(NO_ACCESS_METHOD_ERROR,locale),ACCESS_METHOD_FIELD));
		}
		if(userName==null||userName.length()==0){
			errors.add(new LabelValueBean(getText(NO_USER_NAME_ERROR,locale),USER_NAME_FIELD));
		}
		if(serverName==null||serverName.length()==0){
			errors.add(new LabelValueBean(getText(NO_SERVER_NAME_ERROR,locale),SERVER_NAME_FIELD));
		}
		if(repositoryPath==null||repositoryPath.length()==0){
			errors.add(new LabelValueBean(getText(NO_REPOSITORY_PATH_ERROR,locale),REPOSITORY_PATH_FIELD));
		}
		//not continue if there are errors
		if(!errors.isEmpty()){
			return errors;
		}
		Properties prop=new Properties();
		prop.put("method",accesMethod);
		prop.put("hostname",serverName);
		prop.put("username",userName);
		if(password!=null){
			prop.put("password",password);
		}
		prop.put("repository",repositoryPath);
		prop.put("port","2401");
		CVSRoot root =CVSRoot.parse(prop);
		GlobalOptions gtx = new GlobalOptions();
		gtx.setCVSRoot(cvsRoot);
		final Connection connection;
		if("ext".equalsIgnoreCase(accesMethod)){
			connection=new ExtConnection(serverName, userName);
			((ExtConnection)connection).setRepository(repositoryPath);
			if(ppk!=null){
				((ExtConnection)connection).setPrivateKey(ppk);
				((ExtConnection)connection).setPassphrase(passphrase);
			}else{
				((ExtConnection)connection).setPassword(password);
				((ExtConnection)connection).setPort(2401);
			}
		}else{
			try{
				connection = new PServerConnection(root);
			}catch (Exception e) {
				LOGGER.error(e.getMessage());
				errors.add(new LabelValueBean(e.getMessage(),""));
				String err=getText(GENERAL_ERROR, locale);
				errors.add(new LabelValueBean(err,SERVER_NAME_FIELD));
				errors.add(new LabelValueBean(err,USER_NAME_FIELD));
				errors.add(new LabelValueBean(err,REPOSITORY_PATH_FIELD));
				return errors;
			}
		}
		LOGGER.debug("Open connection...");
		try {
			connection.open();
		} catch (AuthenticationException e) {
			LOGGER.error(e.getMessage());
			errors.add(new LabelValueBean(e.getMessage(),""));
			String err=getText(AUTHENTICATION_ERROR,locale);
			errors.add(new LabelValueBean(err,USER_NAME_FIELD));
			errors.add(new LabelValueBean(err,PWD_FIELD));
			return errors;
		} catch (CommandAbortedException e) {
			LOGGER.error(e.getMessage());
			errors.add(new LabelValueBean(e.getMessage(),""));
			String err=getText(CONNECTION_ERROR,locale);
			errors.add(new LabelValueBean(err,SERVER_NAME_FIELD));
			errors.add(new LabelValueBean(err,REPOSITORY_PATH_FIELD));
			errors.add(new LabelValueBean(err,USER_NAME_FIELD));
			errors.add(new LabelValueBean(err,PWD_FIELD));
			return errors;
		}
		LOGGER.debug("Connection opened!");
		Client client = new Client(connection, new StandardAdminHandler());
		String localPath=System.getProperty("java.io.tmpdir");
		client.setLocalPath(localPath);

		LogCommand command=new LogCommand();
		try {
			client.executeCommand(command,gtx);
			LOGGER.debug("Rlog command processed!");
			try {
				connection.close();
				LOGGER.debug("Connection closed!");
			} catch (IOException e1) {
				LOGGER.error("Can't close connection", e1);
			}
		} catch (CommandException e) {
			LOGGER.error(e.getMessage());
			errors.add(new LabelValueBean(e.getMessage(),""));
			String err=getText(GENERAL_ERROR,locale);
			errors.add(new LabelValueBean(err,SERVER_NAME_FIELD));
			errors.add(new LabelValueBean(err,REPOSITORY_PATH_FIELD));
			errors.add(new LabelValueBean(err,USER_NAME_FIELD));
			errors.add(new LabelValueBean(err,PWD_FIELD));
			return errors;
		} catch (AuthenticationException e) {
			LOGGER.error(e.getMessage());
			errors.add(new LabelValueBean(e.getMessage(),""));
			String err=getText(AUTHENTICATION_ERROR,locale);
			errors.add(new LabelValueBean(err,USER_NAME_FIELD));
			errors.add(new LabelValueBean(err,PWD_FIELD));
			return errors;
		}
		LOGGER.info("Verify cvs parametres successfuly");
		return errors;
	}

	@Override
	protected List<Revision> getRevisions(Map<String,String> parameteres,final String prefixIssueNumber){
		final List<Revision> result=new ArrayList<Revision>();
		List<LabelValueBean> errors=verify(parameteres,Locale.getDefault());
		if(errors!=null && !errors.isEmpty()){
			LOGGER.error("Parameters invalid for CVS plugin");
			return result;
		}
		String accesMethod= parameteres.get("accessMethod");
		String userName=parameteres.get("userName");
		String serverName=parameteres.get("serverName");
		final String repositoryPath=parameteres.get("repositoryPath");
		String password=parameteres.get("password");
		String passphrase=parameteres.get("passphrase");
		String ppk=parameteres.get("privateKey");
		String cvsRoot = ":"+accesMethod+":"+userName+"@"+serverName+":"+repositoryPath;
		LOGGER.info("GetRevisions using cvsRoot = " + cvsRoot);
		if(accesMethod==null){
			return result;
		}
		Properties prop=new Properties();
		prop.put("method",accesMethod);
		prop.put("hostname",serverName);
		prop.put("username",userName);
		if(password!=null){
			prop.put("password",password);
		}
		prop.put("repository",repositoryPath);
		prop.put("port","2401");
		CVSRoot root =CVSRoot.parse(prop);
		GlobalOptions gtx = new GlobalOptions();
		gtx.setCVSRoot(cvsRoot);
		final Connection connection;
		if(accesMethod.equalsIgnoreCase("ext")){
			connection=new ExtConnection(serverName, userName);
			((ExtConnection)connection).setRepository(repositoryPath);
			if(ppk!=null){
				((ExtConnection)connection).setPrivateKey(ppk);
				((ExtConnection)connection).setPassphrase(passphrase);
			}else{
				((ExtConnection)connection).setPassword(password);
				((ExtConnection)connection).setPort(2401);
			}
		}else{
			connection = new PServerConnection(root);
		}
		try {
			connection.open();
		} catch (AuthenticationException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		} catch (CommandAbortedException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		Client client = new Client(connection, new StandardAdminHandler());
		String localPath=System.getProperty("java.io.tmpdir");
		client.setLocalPath(localPath);

		LogCommand command=new LogCommand();
		command.setRecursive(true);
		RlogCommand rlogCommand=new RlogCommand();
		rlogCommand.setModule(".");
		LOGGER.info("Process Rlog command....");

		try {
		   client.getEventManager().addCVSListener(new CVSAdapter() {
				@Override
				public void fileInfoGenerated(FileInfoEvent e) {
					LogInformation loginfo= (LogInformation) e.getInfoContainer();
					List<Revision> l=processLogInformation(loginfo,repositoryPath,prefixIssueNumber);
					result.addAll(l);
				}
			});
			client.executeCommand(rlogCommand,gtx);
			LOGGER.info("Rlog command processed!");
			try {
				connection.close();
			} catch (IOException e1) {
				LOGGER.error(ExceptionUtils.getStackTrace(e1));
			}
			LOGGER.debug("Connnection closed!");
		} catch (CommandException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		} catch (AuthenticationException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		LOGGER.debug("All valid revision found: "+result.size()+"!");
		return result;
	}


	private boolean isOkRevision(LogInformation.Revision revision,String prefixIssueNumber){
		Set<Integer> keys=getWorkItemIDs(revision.getMessage(),prefixIssueNumber);
		if(keys==null||keys.isEmpty()){
			return false;
		}else{
			LOGGER.debug("Revision found for workItems:"+keys);
			LOGGER.debug("Revision:"+revision.getNumber()+" "+revision.getMessage());
			return true;
		}
	}
	private List<Revision> processLogInformation(LogInformation loginfo,String repositoryPath,String prefixIssueNumber){
		List<Revision> result=new ArrayList<Revision>();
		List/*<LogInformation.Revision>*/ revisions=loginfo.getRevisionList();
		for (int i = 0; i < revisions.size(); i++) {
			LogInformation.Revision revision = (LogInformation.Revision) revisions.get(i);
			if(!isOkRevision(revision,prefixIssueNumber)){
				continue;
			}
			Revision r=new Revision();
			String fileName=loginfo.getRepositoryFilename();
			if(fileName.startsWith(repositoryPath)){
				fileName=fileName.substring(repositoryPath.length(),fileName.length());
			}
			if(fileName.endsWith(",v")){
				fileName=fileName.substring(0,fileName.length()-2);
			}
			List<FileDiff> changedPaths=new ArrayList<FileDiff>();
			FileDiff fileDiff=new FileDiff();
			fileDiff.setPath(fileName);
			fileDiff.setType(FileDiff.TYPE_MODIFIED);
			changedPaths.add(fileDiff);
			r.setChangedPaths(changedPaths);

			r.setRevisionAuthor(revision.getAuthor());
			r.setRevisionDate(formatDate(revision.getDate()));
			r.setRevisionNo(revision.getNumber());
			r.setRevisionComment(revision.getMessage());

			result.add(r);
		}
		return result;
	}
}
