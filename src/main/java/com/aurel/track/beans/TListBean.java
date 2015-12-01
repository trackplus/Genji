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


package com.aurel.track.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.aurel.track.admin.customize.lists.ListBL;
import com.aurel.track.exchange.track.ExchangeFieldNames;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.util.EqualUtils;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TListBean
    extends com.aurel.track.beans.base.BaseTListBean
    implements Serializable, ISerializableLabelBean
{
	
	private static final long serialVersionUID = 1L;
	
	//whether this list is editable/deletable 
	boolean modifiable;		
	
	Integer cascadingType;

	/**
	 * @return the modifiable
	 */
	public boolean isModifiable() {
		return modifiable;
	}

	/**
	 * @param modifiable the modifiable to set
	 */
	public void setModifiable(boolean modifiable) {
		this.modifiable = modifiable;
	}
	
	public static interface REPOSITORY_TYPE {
		public static final int PUBLIC = 2;
		public static final int PROJECT = 3;
	}
			
	public static interface LIST_TYPE {
		public static final int SIMPLE = 1;
		public static final int TREE = 2;
		public static final int CASCADINGPARENT = 3;
		public static final int CASCADINGCHILD = 4;
	}

	public static interface CASCADING_TYPE {
		public static final int PARENT_CHILD = 1;
		public static final int PARENT_CHILDREN = 2;
		public static final int PARENT_CHILD_GRANDCHILD = 3;
	}

	@Override
	public String getLabel() {
		return getName();
	}

	public Integer getCascadingType() {
		return cascadingType;
	}

	public void setCascadingType(Integer cascadingType) {
		this.cascadingType = cascadingType;
	}

	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("name", getName());
		String description = getDescription();
		if (description!=null && !"".equals(description)) {
			attributesMap.put("description", description);	
		}		
		Integer parentList = getParentList();
		if (parentList!=null) {
			attributesMap.put("parentList", parentList.toString());
		}
		Integer listType = getListType();
		if (listType!=null) {
			attributesMap.put("listType", listType.toString());
		}
		Integer childNumber = getChildNumber();
		if (childNumber!=null) {
			attributesMap.put("childNumber", childNumber.toString());
		}
		String tagLabel = getTagLabel();
		if (tagLabel!=null) {
			attributesMap.put("tagLabel", tagLabel);
		}
		String deleted = getDeleted();
		if (deleted!=null) {
			attributesMap.put("deleted", deleted);
		}
		Integer repositoryType = getRepositoryType();
		if (repositoryType!=null) {
			attributesMap.put("repositoryType", repositoryType.toString());
		}
		Integer project = getProject();
		if (project!=null) {
			attributesMap.put("project", project.toString());
		}
		Integer owner = getOwner();
		if (owner!=null) {
			attributesMap.put("owner", owner.toString());
		}
		attributesMap.put("uuid", getUuid());
		return attributesMap;
	}
		
	
	/**
	 * De-serialze the labelBean 
	 * @param attributes
	 * @return
	 */
	@Override
	public ISerializableLabelBean deserializeBean(
			Map<String, String> attributes) {
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			this.setObjectID(new Integer(strObjectID));
		}
		
		this.setName(attributes.get("name"));
		this.setDescription(attributes.get("description"));
		String strParentList = attributes.get("parentList");
		if (strParentList!=null) {
			this.setParentList(new Integer(strParentList));
		}else{
			this.setParentList(null);
		}
		String strListType= attributes.get("listType");
		if (strListType!=null) {
			this.setListType(new Integer(strListType));
		}else{
			this.setListType(null);
		}
		String strChildNumber = attributes.get("childNumber");
		if (strChildNumber!=null) {
			this.setChildNumber(new Integer(strChildNumber));
		}else{
			this.setChildNumber(null);
		}
		this.setDeleted(attributes.get("deleted"));
		String strRepositoryType = attributes.get("repositoryType");
		if (strRepositoryType!=null) {
			this.setRepositoryType(new Integer(strRepositoryType));
		}
		String strProject= attributes.get("project");
		if (strProject!=null) {
			this.setProject(new Integer(strProject));
		}else{
			this.setProject(null);
		}
		String strOwner = attributes.get("owner");
		if (strOwner!=null) {
			this.setOwner(new Integer(strOwner));
		}
		this.setUuid(attributes.get("uuid"));
		return this;
	}
	
	/**
	 * Test if this bean is considered to be 
	 * the same as the serializableLabelBean passed as a parameter.
	 * This is the case when the UUIDs match or <b>all</b> of the following
	 * attributes:
	 * <ul>
	 *    <li>name</li>
	 *    <li>list type</li>
	 *    <li>repository type</>
	 *    <li>project</li>
	 *    <li>parent list (if any)</li>
	 * </ul> 
	 * 
	 * @return true if the bean is already known.
	 */
	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		TListBean internalList=(TListBean)serializableLabelBean;
		if (internalList == null) {
			return false;
		}
		String externalUuid = getUuid();
		String internalUuid = internalList.getUuid();

		if(EqualUtils.equalStrict(externalUuid, internalUuid)){
			return true;
		}
		TListBean listBean = (TListBean)serializableLabelBean;
		String externalName = getName();
		String internalName = listBean.getName();
		Integer externalListType = getListType();
		Integer internalListType = listBean.getListType();
		Integer externalRepositoryType = getRepositoryType();
		Integer internalRepositoryType = listBean.getRepositoryType();
		Integer externalProject = getProject();
		Integer internalProject = listBean.getProject();
		Integer externalParent = getParentList();
		Integer internalParent = listBean.getParentList();
		if (EqualUtils.equal(externalListType, internalListType)) {
			if(matchesMap==null||matchesMap.isEmpty()){
				return EqualUtils.equal(externalName,internalName)&&
						EqualUtils.equal(externalRepositoryType,internalRepositoryType)&&
						EqualUtils.equal(externalProject,internalProject)&&
						EqualUtils.equal(externalParent,internalParent);
			}

			if (externalParent!=null || internalParent!=null) {
				if (externalParent!=null && internalParent!=null) {
					//if child list: the parent lists should match (repository and name are not important for children)
					Map<Integer, Integer> listMatch = matchesMap.get(ExchangeFieldNames.LIST);
					return listMatch!=null && listMatch.get(externalParent)!=null && listMatch.get(externalParent).equals(internalParent);
				} else {
					//parent list compared to child list
					return false;
				}
			} else {
				//simple lists or parent lists
				boolean match = false;
				if (EqualUtils.equal(externalName, internalName) && EqualUtils.equal(externalRepositoryType, internalRepositoryType)) {
					if (externalRepositoryType!=null && externalRepositoryType.intValue()==REPOSITORY_TYPE.PROJECT) {
						if (externalProject!=null && internalProject!=null) {
							Map<Integer, Integer> projectMatches = matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_PROJECT, null));
							match = projectMatches!=null && projectMatches.get(externalProject).equals(internalProject);
						}
					} else {
						//global list
						match = true;
					}
				}
				if (match) {
					String deleted = internalList.getDeleted();
					if (BooleanFields.TRUE_VALUE.equals(deleted)) {
						//remove the deleted flag if match found
						internalList.setDeleted(BooleanFields.FALSE_VALUE);
						ListBL.save(listBean);
					}
				}
				return match;
			}
		}
		return false;
	}
	
	/**
	 * Saves a serializableLabelBean into the database
	 * @param serializableLabelBean
	 * @param matchesMap
	 * @return
	 */
	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, 
			Map<String, Map<Integer, Integer>> matchesMap) {
		TListBean listBean = (TListBean)serializableLabelBean;
		Integer parentList = listBean.getParentList();
		if (parentList!=null) {
			Map<Integer, Integer> listMap = 
				matchesMap.get(ExchangeFieldNames.LIST);
			listBean.setParentList(listMap.get(parentList));
		}
		Integer projectID = listBean.getProject();
		if (projectID!=null) {
			Map<Integer, Integer> projectMap = 
				matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_PROJECT, null));
			listBean.setProject(projectMap.get(projectID));
		}
		Integer ownerID = listBean.getOwner();
		if (ownerID!=null) {
			Map<Integer, Integer> personMap = 
				matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_PERSON, null));
			listBean.setOwner(personMap.get(ownerID));
		}
		return ListBL.save(listBean);
	}
	
	/**
	 * Saves a serializableLabelBean into the database with the current owner
	 * @param serializableLabelBean
	 * @param matchesMap
	 * @return
	 */
	public Integer importBean(ISerializableLabelBean serializableLabelBean, 
			Map<String, Map<Integer, Integer>> matchesMap) {	
		TListBean listBean = (TListBean)serializableLabelBean;
		Integer parentList = listBean.getParentList();
		if (parentList!=null) {
			Map<Integer, Integer> listMap = 
				matchesMap.get(ExchangeFieldNames.LIST);
			listBean.setParentList(listMap.get(parentList));
		}
		Integer projectID = listBean.getProject();
		if (projectID!=null) {
			Map<Integer, Integer> projectMap = 
				matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_PROJECT, null));
			listBean.setProject(projectMap.get(projectID));
		}
		Integer ownerID = listBean.getOwner();
		return ListBL.save(listBean);
	}
	
	
	
	
	/**A
	 * Saves a serializableLabelBean into the database (used at TScreenImport)
	 * @param serializableLabelBean
	 * @param matchesMap
	 * @return
	 */
	public Integer saveBeanForScreen(ISerializableLabelBean serializableLabelBean, 
			Map<String, Map<Integer, Integer>> matchesMap) {	
		TListBean listBean = (TListBean)serializableLabelBean;
		Integer parentList = listBean.getParentList();
		if (parentList!=null) {
			Map<Integer, Integer> listMap = 
				matchesMap.get(ExchangeFieldNames.LIST);
			listBean.setParentList(listMap.get(parentList));
		}
		return ListBL.save(listBean);
	}
	
	
	/**A
	 * Whether two label beans are equivalent (used at TScreenImport) 	
	 * @param serializableLabelBean
	 * @param matchesMap	key: fieldID_paramaterCode
	 * 						value: map of already mapped external vs. internal objectIDs 
	 * @return
	 */
	public boolean considerAsSameForScreen(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		if (serializableLabelBean==null) {
			return false;
		}	
		
		TListBean listBean = (TListBean) serializableLabelBean;
		if (getUuid()!=null && listBean.getUuid()!=null)
			if (getUuid().equals(listBean.getUuid()))
				return true;
		
		String externalName = getName();
		String internalName = listBean.getName();	
		Integer externalListType = getListType();
		Integer internalListType = listBean.getListType();
		Integer externalRepositoryType = getRepositoryType();
		Integer internalRepositoryType = listBean.getRepositoryType();
		Integer externalChildNumber = getChildNumber();
		Integer internalChildNumber = listBean.getChildNumber();		
		Integer externalParent = getParentList();
		Integer internalParent = listBean.getParentList();
		
		Map<Integer, Integer> listMatches = 
				matchesMap.get(ExchangeFieldNames.LIST);
		
		if (EqualUtils.equal(externalName, internalName) && EqualUtils.equal(externalListType, internalListType) &&
				EqualUtils.equal(externalRepositoryType,internalRepositoryType) &&
				EqualUtils.equal(externalChildNumber, internalChildNumber)) {
			if (externalParent == null && internalParent==null)
				return true;
			else 
				if (externalParent!=null && internalParent!=null && 
				listMatches.get(externalParent).equals(internalParent))
					return true;
		}
		return false;
	}
	
	
}

