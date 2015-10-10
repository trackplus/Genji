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

package com.aurel.track.admin.project;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aurel.track.beans.TProjectBean;


public class ProjectBLTest {

	public static void main(String[] arg ){
		TProjectBean unu = createProject(null,1);
		TProjectBean doi = createProject(1,2);
		TProjectBean trei = createProject(1,3);
		TProjectBean patru = createProject(3,4);
		TProjectBean unspe = createProject(4,11);
		TProjectBean doispe = createProject(4,12);
		TProjectBean cinci = createProject(3,5);
		TProjectBean sase = createProject(null,6);
		TProjectBean sapte = createProject(6,7);
		TProjectBean opt = createProject(null,8);
		List<TProjectBean> selectedProjects = Arrays.asList(new TProjectBean[]{unu, trei, sase, opt});
		List<TProjectBean> allDescendantProjects =  Arrays.asList(new TProjectBean[]{doi, trei, patru, cinci, sapte, unspe, doispe, opt});
		
		Map<Integer,Set<Integer>> result=getDescendantProjectsMap(selectedProjects, allDescendantProjects);
		
		result.size();
	}

	public static Map<Integer,Set<Integer>> getDescendantProjectsMap(List<TProjectBean> selectedProjects,List<TProjectBean> allDescendantProjects){
		Map<Integer, Set<Integer>> result = initializeEmptyDescendantMap(selectedProjects);
		
		for (Map.Entry<Integer, Set<Integer>> entry : result.entrySet()) {
			Integer selectedProjectId = entry.getKey();
			
			for(TProjectBean descendantProject : allDescendantProjects) {
				if (descendantProjectBelongsTo(selectedProjectId, descendantProject, allDescendantProjects)) {
					result.get(selectedProjectId).add(descendantProject.getObjectID());
				}
			}
		}
		return result;
	}
	
	private static boolean descendantProjectBelongsTo(
			Integer selectedProjectId, TProjectBean descendantProject,
			List<TProjectBean> allDescendantProjects) {
		
		if (descendantProject.getParent()==null) {
			return false;
		} else if (selectedProjectId.equals(descendantProject.getParent())) {
			return true;
		} else {
			 Integer parentProjectId = descendantProject.getParent();
			 TProjectBean parentProject = findParent(parentProjectId,allDescendantProjects);
			if (parentProject!=null) {
				return descendantProjectBelongsTo(selectedProjectId, parentProject, allDescendantProjects);
			}
		}
		
		return false;
	}

	private static TProjectBean findParent(Integer parentId,
			List<TProjectBean> projects) {
		for (TProjectBean project : projects) {
			if (parentId.equals(project.getObjectID())) {
				return project;
			}
		}
		return null;
	}

	private static Map<Integer, Set<Integer>> initializeEmptyDescendantMap(
			List<TProjectBean> selectedProjects) {
		Map<Integer,Set<Integer>> result=new HashMap<Integer, Set<Integer>>();
		for(TProjectBean selectedProject : selectedProjects){
			result.put(selectedProject.getObjectID(), new HashSet<Integer>());
		}
		return result;
	}
	
	
	private static TProjectBean createProject(Integer parentId, Integer objectId ) {
		TProjectBean bean = new TProjectBean();
		bean.setObjectID(objectId);
		bean.setParent(parentId);
		return bean;
	}
}
