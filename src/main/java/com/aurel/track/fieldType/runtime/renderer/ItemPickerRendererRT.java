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

import com.aurel.track.admin.project.ProjectPickerBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TFieldBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.custom.picker.ItemPickerRT;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.StringArrayParameterUtils;
import com.aurel.track.util.TreeNode;

/**
 * Renderer for project picker 
 * @author Tamas
 *
 */
public class ItemPickerRendererRT extends AbstractTypeRendererRT{
	private static ItemPickerRendererRT instance;

	/**
	 * get a singleton instance
	 * @return
	 */
	public static ItemPickerRendererRT getInstance() {
		if (instance == null) {
			instance = new ItemPickerRendererRT();
		}
		return instance;
	}

	/**
	 * constructor
	 */
	public ItemPickerRendererRT() {
	}
	
	@Override
	public String getExtClassName(){
		return "com.aurel.trackplus.field.ItemPickerRenderer";
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
		Set<Integer> itemIDSet = null;
		Object pickedItem=workItemContext.getWorkItemBean().getAttribute(field.getObjectID());	
        boolean create = workItemContext.getWorkItemBeanOriginal()==null;
        if (!create) {
            //for existing issues depending on actual release type (in addition to those releases for new issue)
            //also the inactive or notPlanned or closed releases are also included because the actual release
            //should be included always in the release list independently of release status
            if (pickedItem!=null) {
            	Object[] pickedProjectArr = (Object[])pickedItem;
            	itemIDSet = new HashSet<Integer>();
            	for (int i = 0; i < pickedProjectArr.length; i++) {
            		itemIDSet.add((Integer)pickedProjectArr[i]);
				}
            }
        }
        List<TreeNode> possibleItems=workItemContext.getDropDownContainer().getDataSourceTree(MergeUtil.mergeKey(field.getObjectID(), null));
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendJSONValue(sb, "itemTree", JSONUtility.getTreeHierarchyJSON(possibleItems, true, false), true);
		sb.append("}");
		return sb.toString();
	}
}

