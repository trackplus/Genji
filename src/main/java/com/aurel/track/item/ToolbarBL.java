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

package com.aurel.track.item;

import java.util.ArrayList;
import java.util.List;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.treeConfig.field.FieldDesignBL;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.WorkItemContext;
import com.aurel.track.item.action.IPluginItemAction;
import com.aurel.track.item.action.ItemActionUtil;
import com.aurel.track.item.action.NewItemChildActionPlugin;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.plugin.ItemActionDescription;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.screen.SystemActions;
import com.aurel.track.toolbar.ToolbarItem;

/**
 */
public class ToolbarBL {
	public static List<ToolbarItem> getPrintItemToolbar(ApplicationBean appBean,Integer personID,
		WorkItemContext workItemContext,String forwardAction){

		TWorkItemBean workItem=workItemContext.getWorkItemBean();
		boolean originator=false;
		Integer projectID=workItem.getProjectID();

		boolean projectActive = ProjectBL.projectIsActive(projectID);
		originator=workItem.getOriginatorID().equals(personID);

		List<ToolbarItem> list=new ArrayList<ToolbarItem>();
		boolean allowedToChange = AccessBeans.isAllowedToChange(workItem,personID);
		boolean allowedToCreate = AccessBeans.isAllowedToCreate(personID,workItem.getProjectID(), workItem.getListTypeID());

		ToolbarItem item;
		int[] actions=new int[]{SystemActions.EDIT,
				SystemActions.COPY,SystemActions.MOVE,
				SystemActions.CHANGE_STATUS,SystemActions.NEW_CHILD,SystemActions.NEW_LINKED_ITEM
		};
		for (int i = 0; i < actions.length; i++) {
			item=new ToolbarItem();
			item.setId(ToolbarItem.ITEM_ACTION);
			int action = actions[i];
			ItemActionDescription actionDescriptor= ItemActionUtil.getDescriptor(action + "");
			boolean condition=true;
			if(actionDescriptor.getTheClassName()!=null){
				IPluginItemAction itemActionPlugin=ItemActionUtil.getPlugin(actionDescriptor.getTheClassName());
				condition=itemActionPlugin.isEnabled(personID, workItem,
						allowedToChange, allowedToCreate,appBean.getEdition());
			}
			if (action==SystemActions.MOVE || action==SystemActions.NEW_CHILD || action==SystemActions.NEW_LINKED_ITEM) {
				item.setMore(true);
			}
			item.setCondition(condition);
			item.setCssClass(actionDescriptor.getCssClass());
			item.setTooltipKey(actionDescriptor.getTooltipKey());
			item.setLabelKey(actionDescriptor.getLabelKey());
			item.setImageInactive(actionDescriptor.getImageInactive());
			StringBuilder sb=new StringBuilder();
			sb.append("{");
			JSONUtility.appendIntegerValue(sb,"actionID",actions[i],true);
			sb.append("}");
			item.setJsonData(sb.toString());
			list.add(item);
		}

		//sibling
		item=new ToolbarItem();
		item.setId(ToolbarItem.SIBLING);
		item.setMore(true);
		item.setCondition(allowedToCreate&&enabledAddSibling(workItem,allowedToChange, allowedToCreate, appBean.getEdition(),personID));
		Integer parentID=null;
		if(workItem!=null){
			parentID=workItem.getSuperiorworkitem();
		}
		item.setCssClass("itemAction_addSibling");
		item.setTooltipKey("common.btn.addSibling.tt");
		item.setLabelKey("common.btn.addSibling");
		item.setImageInactive("addSibling-inactive.png");
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendIntegerValue(sb,"parentID",parentID);
		JSONUtility.appendIntegerValue(sb,"actionID",+SystemActions.NEW_CHILD,true);
		sb.append("}");
		item.setJsonData(sb.toString());
		list.add(item);

		//chooseParent
		item=new ToolbarItem();
		item.setId(ToolbarItem.CHOOSE_PARENT);
		item.setMore(true);
		item.setCondition(enableSetParent(appBean,allowedToChange,personID,projectActive));
		item.setCssClass("itemAction_parent");
		item.setTooltipKey("common.btn.chooseParent.tt");
		item.setLabelKey("common.btn.chooseParent");
		item.setImageInactive("parent-inactive.gif");
		list.add(item);

		//print
		item=new ToolbarItem();
		item.setId(ToolbarItem.PRINT);
		item.setMore(true);
		item.setCondition(true);
		item.setCssClass("itemAction_print");
		item.setTooltipKey("common.btn.printIssue.tt");
		item.setLabelKey("common.btn.printIssue");
		item.setImageInactive("printIssue.gif");
		list.add(item);

		//print with children
		//TODO fix me
		if (ItemBL.hasChildren(workItem.getObjectID())) {
			item=new ToolbarItem();
			item.setMore(true);
			item.setId(ToolbarItem.PRINT_WITH_CHILDREN);
			item.setCondition(true);
			item.setCssClass("itemAction_printwc");
			item.setTooltipKey("common.btn.printIssueWithChildren.tt");
			item.setLabelKey("common.btn.printIssueWithChildren");
			item.setImageInactive("printIssue.gif");
			list.add(item);
		}

		//accessLevel
		boolean accessLevelDepecated = FieldDesignBL.isDeprecated(SystemFields.INTEGER_ACCESSLEVEL);
		if (!accessLevelDepecated && originator && projectActive && allowedToChange) {
			item=new ToolbarItem();
			item.setId(ToolbarItem.ACCESS_LEVEL);
			item.setMore(true);
			item.setCondition(true);
			if(workItem.isAccessLevelFlag()){
				item.setCssClass("itemAction_unlock");
				item.setTooltipKey("common.btn.unlock.tt");
				item.setLabelKey("common.btn.unlock");
				item.setImageInactive("unlock-inactive.gif");
			}else{
				item.setCssClass("itemAction_lock");
				item.setTooltipKey("common.btn.lock.tt");
				item.setLabelKey("common.btn.lock");
				item.setImageInactive("lock-inactive.gif");
			}
			list.add(item);
		}

		boolean admin=PersonBL.isProjectAdmin(personID, workItem.getProjectID());
		if(admin){
			if(workItem.isArchived()){
				//archive
				item=new ToolbarItem();
				item.setId(ToolbarItem.ARCHIVE);
				item.setMore(true);
				item.setCondition(true);
				item.setCssClass("itemAction_unarchive");
				item.setTooltipKey("common.btn.unarchive.tt");
				item.setLabelKey("common.btn.unarchive");
				list.add(item);
			}else{
				if(workItem.isDeleted()){
					item=new ToolbarItem();
					item.setId(ToolbarItem.DELETE);
					item.setMore(true);
					item.setCondition(true);
					item.setCssClass("itemAction_undelete");
					item.setTooltipKey("common.btn.undelete.tt");
					item.setLabelKey("common.btn.undelete");
					list.add(item);
				}else{
					item=new ToolbarItem();
					item.setId(ToolbarItem.ARCHIVE);
					item.setMore(true);
					item.setCondition(true);
					item.setCssClass("itemAction_archive");
					item.setTooltipKey("common.btn.archive.tt");
					item.setLabelKey("common.btn.archive");
					list.add(item);

					item=new ToolbarItem();
					item.setId(ToolbarItem.DELETE);
					item.setMore(true);
					item.setCondition(true);
					item.setCssClass("itemAction_delete");
					item.setTooltipKey("common.btn.delete.tt");
					item.setLabelKey("common.btn.delete");
					list.add(item);
				}
			}
		}

		//mail
		item=new ToolbarItem();
		item.setId(ToolbarItem.MAIL);
		item.setCondition(allowedToChange);
		item.setCssClass("itemAction_email");
		item.setTooltipKey("common.btn.sendEmail");
		item.setLabelKey("common.btn.sendEmail");
		item.setImageInactive("email-inactive.gif");
		list.add(item);

		//back
		item=new ToolbarItem();
		item.setId(ToolbarItem.BACK);
		if(forwardAction!=null&&forwardAction.length()>0&&!forwardAction.equals("null")){
			item.setCondition(true);
		}else{
			item.setCondition(false);
		}
		item.setCssClass("itemAction_back");
		item.setTooltipKey("common.btn.back.tt");
		item.setLabelKey("common.btn.back");
		item.setImageInactive("backward-inactive.gif");
		list.add(item);

		return list;
	}
	
	public static List<ToolbarItem> getEditToolbars(ApplicationBean appBean,Integer personID,
		TWorkItemBean workItem,String forwardAction,Integer actionID){

		boolean create=(workItem.getObjectID()==null);
		Integer projectID=workItem.getProjectID();
		boolean projectActive = ProjectBL.projectIsActive(projectID);

		List<ToolbarItem> list=new ArrayList<ToolbarItem>();
		ToolbarItem item;
		//save
		item=new ToolbarItem();
		item.setId(ToolbarItem.SAVE);
		item.setCondition(true);
		item.setCssClass("itemAction_save");
		item.setTooltipKey("common.btn.save");
		item.setLabelKey("common.btn.save");
		item.setImageInactive("save-inactive.gif");
		list.add(item);

		//reset
		/*item=new ToolbarItem();
		item.setId(ToolbarItem.RESET);
		item.setCondition(true);
		item.setCssClass("itemAction_reset");
		item.setTooltipKey("common.btn.reset");
		item.setLabelKey("common.btn.reset");
		item.setImageInactive("reset-inactive.gif");
		list.add(item);*/

		//cancel
		item=new ToolbarItem();
		item.setId(ToolbarItem.CANCEL);
		item.setCondition(true);
		item.setCssClass("itemAction_cancel");
		item.setTooltipKey("common.btn.cancel");
		item.setLabelKey("common.btn.cancel");
		item.setImageInactive("cancel-inactive.gif");
		list.add(item);

		//printitem
		/*item=new ToolbarItem();
		item.setId(ToolbarItem.PRINT_ITEM);
		item.setCondition(!create);
		item.setCssClass("itemAction_viewAll");
		item.setTooltipKey("common.btn.viewAll");
		item.setLabelKey("common.btn.viewAll");
		item.setImageInactive("view-all-inactive.gif");
		list.add(item);*/

		if(actionID.intValue()==SystemActions.EDIT||actionID.intValue()==SystemActions.NEW){
			//attachments
			/*item=new ToolbarItem();
			item.setCondition(enableAttachments());
			item.setCssClass("buttonAttachment");
			item.setTooltipKey("common.btn.attachment");
			item.setLabelKey("common.btn.attachment");
			item.setImageInactive("addAttachment-inactive.gif");
			list.add(item);*/

			//chooseParent
			item=new ToolbarItem();
			item.setId(ToolbarItem.CHOOSE_PARENT);
			item.setCondition(enableSetParent(appBean,true,personID,projectActive));
			String titleChooseParent="chooseParent.title";
			item.setCssClass("itemAction_parent");
			item.setTooltipKey("common.btn.chooseParent.tt");
			item.setLabelKey("common.btn.chooseParent");
			item.setImageInactive("parent-inactive.gif");
			list.add(item);
		}
		if(actionID.intValue()==SystemActions.EDIT){
			//print
			item=new ToolbarItem();
			item.setId(ToolbarItem.PRINT);
			item.setCondition(true);
			item.setCssClass("itemAction_print");
			item.setTooltipKey("common.btn.printIssue.tt");
			item.setLabelKey("common.btn.printIssue");
			item.setImageInactive("printIssue.gif");
			list.add(item);

			//mail
			item=new ToolbarItem();
			item.setId(ToolbarItem.MAIL);
			item.setCondition(true);
			item.setCssClass("itemAction_email");
			item.setTooltipKey("common.btn.sendEmail");
			item.setLabelKey("common.btn.sendEmail");
			item.setImageInactive("email-inactive.gif");
			list.add(item);
		}
		return list;
	}



	private static boolean enableSetParent(ApplicationBean appBean,boolean allowedToChange,Integer personID,boolean projectActive){
		return allowedToChange && appBean.getEdition()>=2 && projectActive;
	}
	private static boolean enabledAddSibling(TWorkItemBean workItem,boolean allowedToChange, boolean allowedToCreate, int appEdition, Integer personID){
		if (workItem==null || workItem.getSuperiorworkitem()==null) {
			return false;
		}
		TWorkItemBean workItemBeanParent = null;
		try {
			workItemBeanParent = ItemBL.loadWorkItem(workItem.getSuperiorworkitem());
		} catch (ItemLoaderException e) {
		}
		if (workItemBeanParent!=null) {
			NewItemChildActionPlugin newItemChildActionPlugin = new NewItemChildActionPlugin();
			return newItemChildActionPlugin.isEnabled(personID, workItemBeanParent, allowedToChange, allowedToCreate, appEdition);
		}
		return false;

	}
}
