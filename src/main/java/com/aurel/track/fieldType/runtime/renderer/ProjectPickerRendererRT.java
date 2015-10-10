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

package com.aurel.track.fieldType.runtime.renderer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aurel.track.beans.TFieldBean;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.StringArrayParameterUtils;
import com.aurel.track.util.TreeNode;

/**
 * Renderer for project picker 
 * @author Tamas
 *
 */
public class ProjectPickerRendererRT extends AbstractTypeRendererRT{
	private static ProjectPickerRendererRT instance;

	/**
	 * get a singleton instance
	 * @return
	 */
	public static ProjectPickerRendererRT getInstance() {
		if (instance == null) {
			instance = new ProjectPickerRendererRT();
		}
		return instance;
	}

	/**
	 * constructor
	 */
	public ProjectPickerRendererRT() {
	}
	
	@Override
	public String getExtClassName(){
		return "com.aurel.trackplus.field.ProjectPickerRenderer";
	}
	@Override
	public String getExtReadOnlyClassName(){
		return "com.aurel.trackplus.field.LabelTypeRenderer";
	}
	@Override
	public String encodeJsonValue(Object value){
		Object[] valueArr = (Object[])value;
		if (valueArr!=null) {
			return JSONUtility.encodeObjectArrayAsArray(valueArr);
		}
		return null;
	}
	
	@Override
	public Object decodeJsonValue(String value, Integer fieldID, WorkItemContext workItemContext) throws TypeConversionException{
		Object[] result=null;
		if(value!=null){
			List<Integer> integerList=StringArrayParameterUtils.splitSelectionAsInteger(value);
			result=new Object[integerList.size()];
			for(int i=0;i< integerList.size();i++){
				Integer intValue=integerList.get(i);
				result[i]=intValue;
			}
		}
		return result;
	}
	
	@Override
	public String createJsonData(TFieldBean field, WorkItemContext workItemContext){
		Set<Integer> projectIDSet = null;
		Object pickedProject=workItemContext.getWorkItemBean().getAttribute(field.getObjectID());	
        boolean create = workItemContext.getWorkItemBeanOriginal()==null;
        if (!create) {
            //for existing issues depending on actual release type (in addition to those releases for new issue)
            //also the inactive or notPlanned or closed releases are also included because the actual release
            //should be included always in the release list independently of release status
            if (pickedProject!=null) {
            	Object[] pickedProjectArr = (Object[])pickedProject;
            	projectIDSet = new HashSet<Integer>();
            	for (int i = 0; i < pickedProjectArr.length; i++) {
            		projectIDSet.add((Integer)pickedProjectArr[i]);
				}
            }
        }
       // List<TreeNode> projectTree =  ProjectPickerBL.getTreeNodesForCreateModify(projectIDSet, true, personBean, workItemContext.getLocale());
        List<TreeNode> projectTree = workItemContext.getDropDownContainer().getDataSourceTree(MergeUtil.mergeKey(field.getObjectID(), null));
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendJSONValue(sb, "projectTree", JSONUtility.getTreeHierarchyJSON(projectTree, true, false), true);
		sb.append("}");
		return sb.toString();
	}
}

