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

package com.aurel.track.fieldType.runtime.renderer;

import java.util.List;

import com.aurel.track.beans.TFieldBean;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.util.TreeNode;

/**
 *
 */
public class ReleaseNoticedPickerRendererRT extends AbstractTypeRendererRT{
	//singleton instance
	private static ReleaseNoticedPickerRendererRT instance;

	/**
	 * get a singleton instance
	 * @return
	 */
	public static ReleaseNoticedPickerRendererRT getInstance() {
		if (instance == null) {
			instance = new ReleaseNoticedPickerRendererRT();
		}
		return instance;
	}

	/**
	 * constructor
	 */
	public ReleaseNoticedPickerRendererRT() {
	}
	@Override
	public String getExtClassName(){
		return "com.aurel.trackplus.field.ReleasePickerRenderer";
	}
	@Override
	public String getExtReadOnlyClassName(){
		return "com.aurel.trackplus.field.LabelTypeRenderer";
	}
	@Override
	public String encodeJsonValue(Object value){
		return value==null?null:"\""+ JSONUtility.escape(value.toString())+"\"";
	}
	@Override
	public Object decodeJsonValue(String value, Integer fieldID, WorkItemContext workItemContext) throws TypeConversionException{
		Integer result=null;
		if(value!=null){
			try{
				result=Integer.decode(value);
			}catch (NumberFormatException ex){
				throw new TypeConversionException("common.err.invalid.number",ex);
			}
		}
		return result;
	}
	@Override
	public String createJsonData(TFieldBean field, WorkItemContext workItemContext){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		/*Integer releaseID=null;
		if(isScheduled()){
			releaseID=workItemContext.getWorkItemBean().getReleaseScheduledID();
		}else{
			releaseID=workItemContext.getWorkItemBean().getReleaseNoticedID();
		}
        boolean inactive = !isScheduled();
        boolean notPlanned = isScheduled();
        boolean closed = false;
        boolean create = workItemContext.getWorkItemBeanOriginal()==null;
        if (!create) {
            //for existing issues depending on actual release type (in addition to those releases for new issue)
            //also the inactive or notPlanned or closed releases are also included because the actual release
            //should be included always in the release list independently of release status
            if(releaseID!=null) {
                TReleaseBean releaseBean= LookupContainer.getReleaseBean(releaseID);
                if(releaseBean!=null){
                    Integer statusFlag = SystemStatusBL.getStatusFlagForStatusID(TSystemStateBean.ENTITYFLAGS.RELEASESTATE, releaseBean.getStatus());
                    if (statusFlag!=null) {
                        switch (statusFlag.intValue()) {
                            case TSystemStateBean.STATEFLAGS.INACTIVE:
                                inactive = true;
                                break;
                            case TSystemStateBean.STATEFLAGS.CLOSED:
                                closed = true;
                                break;
                            case TSystemStateBean.STATEFLAGS.NOT_PLANNED:
                                notPlanned = true;
                                break;
                        }
                    }
                }
            }
        }
		Integer projectID=workItemContext.getWorkItemBean().getProjectID();
        List<Integer> projectIDList = new LinkedList<Integer>();
        projectIDList.add(projectID);
        TPersonBean personBean = LookupContainer.getPersonBean(workItemContext.getPerson());
        String releaseTree =  ReleasePickerBL.getTreeJSON(
                projectIDList, null, false, false,
                closed, inactive, true, notPlanned,
                false, null, personBean, workItemContext.getLocale());*/
        List<TreeNode> possibleReleases = workItemContext.getDropDownContainer().getDataSourceTree(MergeUtil.mergeKey(field.getObjectID(), null));
        JSONUtility.appendJSONValue(sb, "releaseTree", JSONUtility.getTreeHierarchyJSON(possibleReleases, false, true), true);
		sb.append("}");
		return sb.toString();
	}
}

