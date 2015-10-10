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

package com.aurel.track.item.massOperation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.aurel.track.accessControl.AccessBeans;

public class MassOperationRightsBL {
	
	static boolean hasRightInAllContexts(Map<Integer, Set<Integer>> projectsToIssueTypes, 
			Integer personID, int rightFlag) {
		Iterator<Integer> itrProject = projectsToIssueTypes.keySet().iterator();
		while (itrProject.hasNext()) {
			Integer projectID = itrProject.next();
			Set<Integer> issueTypeSet = projectsToIssueTypes.get(projectID);
			if (issueTypeSet!=null) {
				for (Integer issueTypeID : issueTypeSet) {
					boolean assignConsultantInformant = AccessBeans.hasPersonRightInProjectForIssueType(personID, 
							projectID, issueTypeID, rightFlag, true, true);
					if (!assignConsultantInformant) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Whether the person has a right in a project/issueType context 
	 * @param projectsToIssueTypes
	 * @param personID
	 * @param rightFlag
	 * @return
	 */
	private static Map<Integer, Map<Integer, Boolean>> loadRightInContexts(
			Map<Integer, Set<Integer>> projectsToIssueTypes, Integer personID, int rightFlag) { 
		Map<Integer, Map<Integer, Boolean>> rightInContext = new HashMap<Integer, Map<Integer,Boolean>>();
		for (Integer projectID : projectsToIssueTypes.keySet()) {
			Set<Integer> issueTypeSet = projectsToIssueTypes.get(projectID);
			if (issueTypeSet!=null && !issueTypeSet.isEmpty()) {
				Map<Integer, Boolean> issueTypesRight = new HashMap<Integer, Boolean>();
				rightInContext.put(projectID, issueTypesRight);
				for (Integer issueTypeID : issueTypeSet) {
					boolean hasRight = AccessBeans.hasPersonRightInProjectForIssueType(personID, 
							projectID, issueTypeID, rightFlag, true, true);
					issueTypesRight.put(issueTypeID, Boolean.valueOf(hasRight));
				}
			}
		}
		return rightInContext;
	}
	
	/**
	 * Whether the person has a right in a project/issueType context 
	 * @param projectsToIssueTypes
	 * @param personID
	 * @param rightFlag
	 * @return
	 */
	static Map<Integer, Map<Integer, Boolean>> loadRightInContextsByTargetProjectAndIssueType(
			Map<Integer, Set<Integer>> projectsToIssueTypes, Integer personID, int rightFlag, 
			Integer targetProject, Integer targetIssueType) {
		if (targetProject==null && targetIssueType==null) {
			//none of target project or issueType is specified
			return loadRightInContexts(projectsToIssueTypes, personID, rightFlag);
		}
		Map<Integer, Map<Integer, Boolean>> rightInContext = new HashMap<Integer, Map<Integer,Boolean>>();
		if (targetProject!=null) {
			//target project is specified
			if (targetIssueType==null) {
				//target issue type is not specified
				//gather each issueType (from each "old" project) to verify with the targetProject
				Map<Integer, Boolean> issueTypesRight = new HashMap<Integer, Boolean>();
				rightInContext.put(targetProject, issueTypesRight);
				Iterator<Integer> itrProject = projectsToIssueTypes.keySet().iterator();
				while (itrProject.hasNext()) {
					Integer projectID = itrProject.next();
					Set<Integer> issueTypeSet = projectsToIssueTypes.get(projectID);
					if (issueTypeSet!=null && !issueTypeSet.isEmpty()) {
						for (Integer issueTypeID : issueTypeSet) {
							if (!issueTypesRight.containsKey(issueTypeID)) {
								boolean hasRight = AccessBeans.hasPersonRightInProjectForIssueType(personID, 
										targetProject, issueTypeID, rightFlag, true, true);
								issueTypesRight.put(issueTypeID, Boolean.valueOf(hasRight));
							}
						}
					}
				}
			}
			else {
				//both targetProject and targetIssueType is specified: only this context should be verified
				Map<Integer, Boolean> issueTypesRight = new HashMap<Integer, Boolean>();
				rightInContext.put(targetProject, issueTypesRight);
				boolean hasRight = AccessBeans.hasPersonRightInProjectForIssueType(personID, 
						targetProject, targetIssueType, rightFlag, true, true);
				issueTypesRight.put(targetIssueType, hasRight);				
			}
		} else {
			//no targetProject but targetIssueType is specified			
			//for each project verify the target issueType
			for (Integer projectID : projectsToIssueTypes.keySet()) {
				Map<Integer, Boolean> issueTypesRight = new HashMap<Integer, Boolean>();
				rightInContext.put(projectID, issueTypesRight);
				boolean hasRight = AccessBeans.hasPersonRightInProjectForIssueType(personID, 
						projectID, targetIssueType, rightFlag, true, true);
				issueTypesRight.put(targetIssueType, Boolean.valueOf(hasRight));
			}			
		}		
		return rightInContext;
	}
	
	/**
	 * Whether there is right in context
	 * @param rightInContextMap
	 * @param projectID
	 * @param issueTypeID
	 * @return
	 */
	static boolean hasRightInContext(Map<Integer, Map<Integer, Boolean>> rightInContextMap, Integer projectID, Integer issueTypeID) {
		if (rightInContextMap!=null) {
			Map<Integer, Boolean> issueTypeRights = rightInContextMap.get(projectID);
			if (issueTypeRights!=null) {
				Boolean right = issueTypeRights.get(issueTypeID);
				return right!=null && right.booleanValue();
			}
		}
		return false;
	}
}
