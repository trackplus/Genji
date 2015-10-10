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

package com.aurel.track.exchange.msProject.importer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sf.mpxj.Resource;

import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.Perl5Pattern;

import com.aurel.track.admin.server.siteConfig.SiteConfigBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.prop.ApplicationBean;

public class MSProjectResourceMappingBL {
	
	
	public static Integer ADD_AS_NEW = Integer.valueOf(0);
	public static final int FULLNAME_BASED = 1;
	public static final int USERNAME_BASED = 2;	
	
	public static List<TPersonBean> getTrackPlusPersons(/*Integer projectID*/) {
		//better all active persons because can be that the user 
		//has no explicit right in the project but still exists already
		return PersonBL.loadActivePersonsAndGroups();
	}
	
	/**
	 * Verify adding new trackPlus users before the actual creation
	 * @param workResources
	 * @param resourceUIDToResourceNameMap
	 * @param resourceUIDToPersonIDMap
	 * @param resourceUIDToUsername
	 * @param resourceUIDToEmail
	 * @return
	 */
	public static SortedMap<Integer, List<ErrorData>> verifyNewUsers(List<Resource> workResources, 
			Map<Integer, String> resourceUIDToResourceNameMap, Map<Integer, Integer> resourceUIDToPersonIDMap, 
			Map<Integer, String> resourceUIDToUsername, Map<Integer, String> resourceUIDToEmail) {		
		SortedMap<Integer, List<ErrorData>> errorsMap = new TreeMap<Integer, List<ErrorData>>();		
		if (resourceUIDToPersonIDMap!=null) {
			Iterator<Map.Entry<Integer, Integer>> itrResourceNames = resourceUIDToPersonIDMap.entrySet().iterator();
			while (itrResourceNames.hasNext()) {
				Map.Entry<Integer, Integer> resourceUIDToPersonID = itrResourceNames.next();
				Integer resourceUID = resourceUIDToPersonID.getKey();
				Integer personID = resourceUIDToPersonID.getValue();
				if (ADD_AS_NEW.equals(personID)) {
					String userName = resourceUIDToUsername.get(resourceUID);
					String email = resourceUIDToEmail.get(resourceUID);
					String lastName = "";
					String firstName = "";
					String resourceName = resourceUIDToResourceNameMap.get(resourceUID);
					if (resourceName!=null) {
						String[] resourceNameParts = resourceName.split("\\s|,");
						if (resourceNameParts!=null && resourceNameParts.length>0) {
							lastName = resourceNameParts[0];
							if (resourceNameParts.length>1) {
								for (int i = 1; i < resourceNameParts.length; i++) {
									firstName+=resourceNameParts[i];
								}
							}
						}
					}
					List<ErrorData> errorDataList = verifyNewUser(resourceName, userName, firstName, lastName, email);
					if (errorDataList!=null && !errorDataList.isEmpty()) {
						errorsMap.put(resourceUID, errorDataList);
					}
				}
			}
		}
		return errorsMap;
	}
	
	/**
	 * Verify a new user
	 * @param resourceName
	 * @param username
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @return
	 */
	public static List<ErrorData> verifyNewUser(String resourceName, String username, String firstName, String lastName, String email) {
		List<ErrorData> errorData = new ArrayList<ErrorData>();
		if (username==null || username.length()==0) {
			errorData.add(new ErrorData("registration.error.loginName.required"));
		}
		if ((email==null) || (email.length()== 0)) {
			errorData.add(new ErrorData("registration.error.emailAddress.required"));
		} else {
			int atSign = email.indexOf("@");
			if ((atSign < 1) || (atSign >= (email.length() - 1))
				|| (email.length() > 60)) {
				errorData.add(new ErrorData("registration.error.emailAddress.format"));
			}
			Perl5Matcher pm = new Perl5Matcher();
			TSiteBean siteBean= ApplicationBean.getApplicationBean().getSiteBean();
			Perl5Pattern allowedEmailPattern = SiteConfigBL.getSiteAllowedEmailPattern(siteBean);
			if (!pm.contains(email.trim(),allowedEmailPattern)) {
				errorData.add(new ErrorData("registration.error.emailAddress.domain"));
			}
		}
		TPersonBean existingPersonBean = PersonBL.loadByLoginName(username);
		if (existingPersonBean!=null) {
			errorData.add(new ErrorData("registration.error.loginName.unique", username));
		}
		if (PersonBL.nameAndEmailExist(lastName, firstName, email, null)) {
			errorData.add(new ErrorData("registration.error.nameAndEmail.unique", 
					lastName + ", " + firstName + ", " + email));
		}
		return errorData;
	}
	
	/**
	 * Verify adding new trackPlus users before the actual creation
	 * @param workResources
	 * @param resourceUIDToResourceNameMap
	 * @param resourceUIDToPersonIDMap
	 * @param resourceUIDToUsername
	 * @param resourceUIDToEmail
	 * @return
	 */
	public static int createNewUsers(List<Resource> workResources, 
			Map<Integer, String> resourceUIDToResourceNameMap, Map<Integer, Integer> resourceUIDToPersonIDMap, 
			Map<Integer, String> resourceUIDToUsername, Map<Integer, String> resourceUIDToEmail) {
		int numberOfUsersCreated = 0;
		if (resourceUIDToPersonIDMap!=null) {
			Iterator<Map.Entry<Integer, Integer>> itrResourceNames = resourceUIDToPersonIDMap.entrySet().iterator();
			while (itrResourceNames.hasNext()) {
				Map.Entry<Integer, Integer> resourceUIDToPersonID = itrResourceNames.next();
				Integer resourceUID = resourceUIDToPersonID.getKey();
				Integer personID = resourceUIDToPersonID.getValue();
				if (ADD_AS_NEW.equals(personID)) {
					String userName = resourceUIDToUsername.get(resourceUID);
					String email = resourceUIDToEmail.get(resourceUID);
					String lastName = "";
					String firstName = "";
					String resourceName = resourceUIDToResourceNameMap.get(resourceUID);
					if (resourceName!=null) {
						String[] resourceNameParts = resourceName.split("\\s|,");
						if (resourceNameParts!=null && resourceNameParts.length>0) {
							lastName = resourceNameParts[0];
							if (resourceNameParts.length>1) {
								for (int i = 1; i < resourceNameParts.length; i++) {
									firstName+=resourceNameParts[i];
								}
							}
						}
					}
					TPersonBean personBean = PersonBL.createMsProjectImportNewUser(userName, firstName, lastName, email);
					personID = PersonBL.save(personBean);
					numberOfUsersCreated++;
					resourceUIDToPersonIDMap.put(resourceUID, personID);
				}
			}
		}
		return numberOfUsersCreated;
	}
	
	/**
	 * Creates a new user
	 * @param username
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @return
	 */
	/*private static Integer createNewUser(String username, String firstName, String lastName, String email) {		
		TPersonBean personBean = new TPersonBean();
		personBean.setFirstName(firstName);
		personBean.setLastName(lastName);
		personBean.setEmail(email);
		personBean.setLoginName(username);
		String pass = RandomStringUtils.randomAlphanumeric(8);
		personBean.setPasswd(pass);
		personBean.setDepartmentID(DepartmentBL.getDefaultDepartment());
		return PersonBL.save(personBean);
	}*/
}
