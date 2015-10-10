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

package com.aurel.track.exchange.importer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.StrutsTestCase;
import org.apache.torque.util.Criteria;
import org.junit.Before;
import org.junit.Test;

import com.aurel.track.Constants;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.attachment.AttachBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeBL;
import com.aurel.track.exchange.msProject.exchange.MsProjectExchangeDataStoreBean;
import com.aurel.track.exchange.msProject.importer.MsProjectImportAction;
import com.aurel.track.exchange.msProject.importer.MsProjectImporterBL;
import com.aurel.track.exchange.msProject.importer.MsProjectUploadAction;
import com.aurel.track.persist.BaseTWorkItemPeer;
import com.aurel.track.persist.TWorkItemPeer;
import com.google.common.io.Files;
import com.opensymphony.xwork2.ActionProxy;

public class MsProjectImportActionTest extends StrutsTestCase {
	
	private Map<String, Object> sessionMap;
	private final Integer DEFAULT_PERSON_ID = Integer.valueOf(1); //sysadmin  
	private final String MS_PROJECT_FILES_PATH  = System.getProperty("user.dir").replace("\\", "/") + "/src/test/java/com/aurel/track/exchange/importer/MsProjectFiles";
	
	private final Integer PROJECT_OR_RELEASE_ID = Integer.valueOf(4);
	
	@Before
	public void setUp() throws Exception{
		sessionMap = new HashMap<String, Object>();
		TPersonBean user = PersonBL.loadByPrimaryKey(DEFAULT_PERSON_ID);
		sessionMap.put(Constants.USER_KEY, user);
		super.setUp();
	}
	
		
	/************** MsProjectUploadAction methods tests **************/
	
	/**
	 * This method test if MsProjectUploadAction exists.
	 * @throws Exception
	 */
	@Test
	public void testMsProjectUploadAction() throws Exception {
		MsProjectUploadAction result = new MsProjectUploadAction();
		assertNotNull(result);
	}
	
	/**
	 * This method tests returning project tree as target form import.
	 *When importing was executed from toolbar menu not from drag drop. 
	 */
	@Test
	public void testMsProjectUploadRender() throws Exception {
		ActionProxy actionProxy = getActionProxy("/msProjectUploadRender.action") ;
		actionProxy.getInvocation().getInvocationContext().setSession(sessionMap);

		String result = actionProxy.execute();
		String responseText = response.getContentAsString();
		System.out.println("Test method name: msProjectUploadRender result: " + result + " response: " + responseText);
		assertNotNull(responseText);
		assertTrue(responseText.length() > 0);
	}
	
	/**
	 * This method tests uploading MS. Project file.
	 * The file path: is set in MS_PROJECT_FILES_PATH constant
	 */
	@Test
	public void testUpload() throws Exception {
		File msProjectFolder = new File(MS_PROJECT_FILES_PATH);
		File[] listOfFiles = msProjectFolder.listFiles();
		
		for (File fileToUpload : listOfFiles) {
		    if (fileToUpload.isFile()) {
				request.setParameter("uploadFileFileName", fileToUpload.getName());
				request.setParameter("projectOrReleaseID", PROJECT_OR_RELEASE_ID.toString());
				ActionProxy actionProxy = getActionProxy("/msProjectUpload.action") ;
				MsProjectUploadAction action = (MsProjectUploadAction) actionProxy.getAction();
				action.setUploadFile(fileToUpload);
				actionProxy.getInvocation().getInvocationContext().setSession(sessionMap);
				String result = actionProxy.execute();
				String responseText = response.getContentAsString();
				System.out.println("Test method name: msProjectUpload result: " + result + " response: " + responseText);
				assertEquals(result, MsProjectUploadAction.MSPROJECT_IMPORT);
		    }
		}
	}
	
	/************** end of MsProjectUploadAction methods tests **************/
	
	/************** MsProjectImportAction methods tests **************/
	/**
	 * This method tests if MsProjectImportAction exists. 
	 * @throws Exception
	 */
	@Test
	public void testMsProjectImportAction() throws Exception {
		MsProjectImportAction result = new MsProjectImportAction();
		assertNotNull(result);
	}
	
	/**
	 * Run the String execute() method test
	 */
	@Test
	public void testExecute()  throws Exception{
		File msProjectFolder = new File(MS_PROJECT_FILES_PATH);
		File[] listOfFiles = msProjectFolder.listFiles();
		
		
		for (File fileToUpload : listOfFiles) {
		    if (fileToUpload.isFile()) {
				ActionProxy actionProxy = getActionProxy("/msProjectImport.action") ;
				actionProxy.getInvocation().getInvocationContext().setSession(sessionMap);
				MsProjectImportAction action = (MsProjectImportAction) actionProxy.getAction();
				File uploadedFilesLocation = new File(AttachBL.getMsProjectImportDirBase() + PROJECT_OR_RELEASE_ID + "/" + fileToUpload.getName());
				Files.copy(fileToUpload, uploadedFilesLocation);
				action.setFileName(fileToUpload.getName());
				action.setProjectOrReleaseID(PROJECT_OR_RELEASE_ID);
				String result = actionProxy.execute();
				String responseText = response.getContentAsString();
				System.out.println("Test method name: msProjectImport result: " + result + " response: " + responseText);
				assertTrue(responseText.length() > 0);
				assertNotNull(responseText);
		    }
		}
	}
	

	/**
	 * This method executes the ms project file import. In case if needed handles resource mappings.
	 */
	@Test
	public void testSubmitResourceMapping() {
		try{
			File msProjectFolder = new File(MS_PROJECT_FILES_PATH);
			File[] listOfFiles = msProjectFolder.listFiles();
			for (File fileToUpload : listOfFiles) {
			    if (fileToUpload.isFile()) {
			    	System.out.println("Testing the following Ms. project file: " + fileToUpload.getName());
			    	File tempFile = new File(msProjectFolder + "/" + FilenameUtils.removeExtension(fileToUpload.getName()) + "Tmp." + FilenameUtils.getExtension(fileToUpload.getName()));
			    	
			    	Files.copy(fileToUpload, tempFile);
			    	
					MsProjectExchangeDataStoreBean msProjectExchangeDataStoreBean;
		
					msProjectExchangeDataStoreBean = MsProjectExchangeBL.initMsProjectExchangeBeanForImport(
							PROJECT_OR_RELEASE_ID, PersonBL.loadByPrimaryKey(1), tempFile, null);
					
					SortedMap<String, Integer> resourceNameToResourceUIDMap = MsProjectImporterBL
							.getResourceNameToResourceUIDMap(msProjectExchangeDataStoreBean
									.getWorkResources());
					Map<Integer, Integer> resourceUIDToPersonIDMap = new HashMap<Integer, Integer>();
					for (String key : resourceNameToResourceUIDMap.keySet()) {
						resourceUIDToPersonIDMap.put(resourceNameToResourceUIDMap.get(key), DEFAULT_PERSON_ID);
						
					}
					sessionMap.put("msProjectImporterBean", msProjectExchangeDataStoreBean);
					ActionProxy actionProxy = getActionProxy("/msProjectImport!submitResourceMapping.action");
					actionProxy.getInvocation().getInvocationContext().setSession(sessionMap);
					
					String result = actionProxy.execute();
					String responseText = response.getContentAsString();
					
					assertTrue(responseText.length() > 0);
					assertNotNull(responseText);
					System.out.println("Test method name: submitResourceMapping result: " + result + " response: " + responseText);
					deleteProjectTasks();
					if(tempFile.exists()) {
						tempFile.delete();
					}
			    }
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void deleteProjectTasks() {
		Integer[] projectIDsArray = new Integer[1];
		projectIDsArray[0] = PROJECT_OR_RELEASE_ID * -1;
//    	FilterUpperTO filter = FilterUpperConfigUtil.getByProjectReleaseIDs(true, projectIDsArray, true, true, true);
//    	WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();
    	Criteria crit = new Criteria();
    	crit.add(BaseTWorkItemPeer.PROJECTKEY, Integer.valueOf(PROJECT_OR_RELEASE_ID));
    	TWorkItemPeer.doDelete(crit);
	}
	/************** end of MsProjectUploadAction methods tests **************/
}

