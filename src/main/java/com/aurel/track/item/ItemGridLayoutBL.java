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


package com.aurel.track.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.admin.project.ProjectAccountingTO;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.TReportLayoutBean;
import com.aurel.track.beans.TReportLayoutBean.LINK;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ReportLayoutDAO;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.resources.LocalizeUtil;

public class ItemGridLayoutBL {
	private static ReportLayoutDAO reportLayoutDAO = DAOFactory.getFactory().getReportLayoutDAO();
	public interface ILayout{
		public int getId();
		public String getHeaderKey();
		public boolean isSortable();
		public String getDataIndex();
		public int getType();
		public int getWidth();
		public ILayout getLayoutFromID(int id);
	}
	public enum HistoryLayout implements ILayout {
		LAST_EDIT("date","common.history.lbl.lastEdit",TReportLayoutBean.HISTORY.LAST_EDIT,
				true,ItemGridLayout.COLUMN_TYPE.DATETIME,120),
		
		CHANGE_BY("author","common.history.lbl.changedBy",TReportLayoutBean.HISTORY.CHANGE_BY,
				true,ItemGridLayout.COLUMN_TYPE.STRING,170),
				
		TYPE_OF_CHANGE("typeOfChange","common.history.lbl.typeOfChange",TReportLayoutBean.HISTORY.TYPE_OF_CHANGE,
				true,ItemGridLayout.COLUMN_TYPE.STRING,100),
		
		NEW_VALUE("newValues","common.history.lbl.newValue",TReportLayoutBean.HISTORY.NEW_VALUE,
				true,ItemGridLayout.COLUMN_TYPE.STRING,240),
		
		OLD_VALUE("oldValues","common.history.lbl.oldValue",TReportLayoutBean.HISTORY.OLD_VALUE,
				true,ItemGridLayout.COLUMN_TYPE.STRING,240),
				
		COMMENT("comment","common.history.lbl.comment",TReportLayoutBean.HISTORY.COMMENT,
				true,ItemGridLayout.COLUMN_TYPE.STRING,240),

		DIFF("diff","common.history.lbl.diff",TReportLayoutBean.HISTORY.DIFF,
				true,ItemGridLayout.COLUMN_TYPE.STRING,240);
		
		private final int id;
		private final String headerKey;
		private final boolean sortable;
		private final String dataIndex;
		private final int type;
		private final int width;
		private HistoryLayout(String dataIndex, String headerKey, int id,
				boolean sortable, int type, int width) {
			this.dataIndex = dataIndex;
			this.headerKey = headerKey;
			this.id = id;
			this.sortable = sortable;
			this.type = type;
			this.width = width;
		}
		@Override
		public int getId() {
			return id;
		}
		@Override
		public String getHeaderKey() {
			return headerKey;
		}
		@Override
		public boolean isSortable() {
			return sortable;
		}
		@Override
		public String getDataIndex() {
			return dataIndex;
		}
		@Override
		public int getType() {
			return type;
		}
		@Override
		public int getWidth() {
			return width;
		}
		@Override
		public ILayout getLayoutFromID(int id){
			for (HistoryLayout hl : HistoryLayout.values()) {
				if (hl.getId() == id) {
					return hl;
				}
			}
			return null;
		}
		public int getValue() {
			return ordinal();
		}
		@Override
		public String toString() {
			return getDataIndex() + " : " + id;
		}
	}
	
	public enum CommentsLayout implements ILayout {
		LAST_EDIT("date","common.history.lbl.lastEdit",TReportLayoutBean.COMMENT.LAST_EDIT,
				true,ItemGridLayout.COLUMN_TYPE.DATETIME,120),
		
		CHANGE_BY("author","common.history.lbl.changedBy",TReportLayoutBean.COMMENT.CHANGE_BY,
				true,ItemGridLayout.COLUMN_TYPE.STRING,170),
				
		COMMENT("comment","common.history.lbl.comment",TReportLayoutBean.COMMENT.COMMENT,
				true,ItemGridLayout.COLUMN_TYPE.STRING,100);
		
		private final int id;
		private final String headerKey;
		private final boolean sortable;
		private final String dataIndex;
		private final int type;
		private final int width;
		private CommentsLayout(String dataIndex, String headerKey, int id,
				boolean sortable, int type, int width) {
			this.dataIndex = dataIndex;
			this.headerKey = headerKey;
			this.id = id;
			this.sortable = sortable;
			this.type = type;
			this.width = width;
		}
		@Override
		public int getId() {
			return id;
		}
		@Override
		public String getHeaderKey() {
			return headerKey;
		}
		@Override
		public boolean isSortable() {
			return sortable;
		}
		@Override
		public String getDataIndex() {
			return dataIndex;
		}
		@Override
		public int getType() {
			return type;
		}
		@Override
		public int getWidth() {
			return width;
		}
		@Override
		public ILayout getLayoutFromID(int id){
			for (CommentsLayout hl : CommentsLayout.values()) {
				if (hl.getId() == id) {
					return hl;
				}
			}
			return null;
		}
		public int getValue() {
			return ordinal();
		}
		@Override
		public String toString() {
			return getDataIndex() + " : " + id;
		}
	}
	
	public enum WorklogLayout implements ILayout {
		LAST_EDIT("date","common.history.lbl.lastEdit",TReportLayoutBean.WORKLOG.LAST_EDIT,
				true,ItemGridLayout.COLUMN_TYPE.DATETIME,120),
		
		CHANGE_BY("author","common.history.lbl.changedBy",TReportLayoutBean.WORKLOG.CHANGE_BY,
				true,ItemGridLayout.COLUMN_TYPE.STRING,170),
				
		SUBJECT("subject","common.history.lbl.subject",TReportLayoutBean.WORKLOG.SUBJECT,
				true,ItemGridLayout.COLUMN_TYPE.STRING,150),
		
		WORK("work","item.tabs.expense.editExpense.lbl.effort",TReportLayoutBean.WORKLOG.WORK,
				true,ItemGridLayout.COLUMN_TYPE.STRING,70),
		
		COST("cost","common.lbl.cost",TReportLayoutBean.WORKLOG.COST,
				true,ItemGridLayout.COLUMN_TYPE.STRING,70),
				
		ACCOUNT("account","item.tabs.expense.editExpense.lbl.account",TReportLayoutBean.WORKLOG.ACCOUNT,
				true,ItemGridLayout.COLUMN_TYPE.STRING,120),
		
		EFFORT_DATE("effortDate", "item.tabs.expense.editExpense.lbl.effortDate",TReportLayoutBean.WORKLOG.EFFORT_DATE,
				true,ItemGridLayout.COLUMN_TYPE.DATE,90),
						
		DECRIPTION("description","common.lbl.description",TReportLayoutBean.WORKLOG.DESCRIPTION,
				true,ItemGridLayout.COLUMN_TYPE.STRING,280);
		
		private final int id;
		private final String headerKey;
		private final boolean sortable;
		private final String dataIndex;
		private final int type;
		private final int width;
		private WorklogLayout(String dataIndex, String headerKey, int id,
				boolean sortable, int type, int width) {
			this.dataIndex = dataIndex;
			this.headerKey = headerKey;
			this.id = id;
			this.sortable = sortable;
			this.type = type;
			this.width = width;
		}
		@Override
		public int getId() {
			return id;
		}
		@Override
		public String getHeaderKey() {
			return headerKey;
		}
		@Override
		public boolean isSortable() {
			return sortable;
		}
		@Override
		public String getDataIndex() {
			return dataIndex;
		}
		@Override
		public int getType() {
			return type;
		}
		@Override
		public int getWidth() {
			return width;
		}
		@Override
		public ILayout getLayoutFromID(int id){
			for (WorklogLayout hl : WorklogLayout.values()) {
				if (hl.getId() == id) {
					return hl;
				}
			}
			return null;
		}
		public int getValue() {
			return ordinal();
		}
		@Override
		public String toString() {
			return getDataIndex() + " : " + id;
		}
	}
	
	public enum AttachmentLayout implements ILayout {
		FILE_NAME("fileName","item.tabs.attachment.lbl.file",TReportLayoutBean.ATTACHMENT.FILE_NAME,
				true,ItemGridLayout.COLUMN_TYPE.STRING,100),
		
		FILE_SIZE("fileSize","common.lbl.size",TReportLayoutBean.ATTACHMENT.FILE_SIZE,
				true,ItemGridLayout.COLUMN_TYPE.BYTES,100),
				
		LAST_EDIT("date","common.history.lbl.lastEdit",TReportLayoutBean.ATTACHMENT.LAST_EDIT,
				true,ItemGridLayout.COLUMN_TYPE.DATETIME,120),
		
		CHANGE_BY("author","common.history.lbl.changedBy",TReportLayoutBean.ATTACHMENT.CHANGE_BY,
				true,ItemGridLayout.COLUMN_TYPE.STRING,170),
				
		DESCRIPTION("description","common.lbl.description",TReportLayoutBean.ATTACHMENT.DESCRIPTION,
				true,ItemGridLayout.COLUMN_TYPE.STRING,475),
		
		THUMBNAIL("thumbnail","common.lbl.attachment.thumbnail",TReportLayoutBean.ATTACHMENT.THUMBNAIL,
				false,ItemGridLayout.COLUMN_TYPE.IMAGE,120);
		 
		private final int id;
		private final String headerKey;
		private final boolean sortable;
		private final String dataIndex;
		private final int type;
		private final int width;
		private AttachmentLayout(String dataIndex, String headerKey, int id,
				boolean sortable, int type, int width) {
			this.dataIndex = dataIndex;
			this.headerKey = headerKey;
			this.id = id;
			this.sortable = sortable;
			this.type = type;
			this.width = width;
		}
		@Override
		public int getId() {
			return id;
		}
		@Override
		public String getHeaderKey() {
			return headerKey;
		}
		@Override
		public boolean isSortable() {
			return sortable;
		}
		@Override
		public String getDataIndex() {
			return dataIndex;
		}
		@Override
		public int getType() {
			return type;
		}
		@Override
		public int getWidth() {
			return width;
		}
		@Override
		public ILayout getLayoutFromID(int id){
			for (AttachmentLayout hl : AttachmentLayout.values()) {
				if (hl.getId() == id) {
					return hl;
				}
			}
			return null;
		}
		public int getValue() {
			return ordinal();
		}
		@Override
		public String toString() {
			return getDataIndex() + " : " + id;
		}
	}
	public enum WatcherLayout implements ILayout {
		TYPE("type","item.tabs.watchers.lbl.type",TReportLayoutBean.WATCHER.TYPE,
				true,ItemGridLayout.COLUMN_TYPE.STRING,150),
				
		PERSON("person","item.tabs.watchers.lbl.persons",TReportLayoutBean.WATCHER.PERSON,
				true,ItemGridLayout.COLUMN_TYPE.STRING,250)/*,
		
		IS_GROUP("isGroup","item.tabs.watchers.lbl.isGroup",TReportLayoutBean.WATCHER.IS_GROUP,
				true,ItemGridLayout.COLUMN_TYPE.STRING,110)*/;
				
		private final int id;
		private final String headerKey;
		private final boolean sortable;
		private final String dataIndex;
		private final int type;
		private final int width;
		private WatcherLayout(String dataIndex, String headerKey, int id,
				boolean sortable, int type, int width) {
			this.dataIndex = dataIndex;
			this.headerKey = headerKey;
			this.id = id;
			this.sortable = sortable;
			this.type = type;
			this.width = width;
		}
		@Override
		public int getId() {
			return id;
		}
		@Override
		public String getHeaderKey() {
			return headerKey;
		}
		@Override
		public boolean isSortable() {
			return sortable;
		}
		@Override
		public String getDataIndex() {
			return dataIndex;
		}
		@Override
		public int getType() {
			return type;
		}
		@Override
		public int getWidth() {
			return width;
		}
		@Override
		public ILayout getLayoutFromID(int id){
			for (WatcherLayout hl : WatcherLayout.values()) {
				if (hl.getId() == id) {
					return hl;
				}
			}
			return null;
		}
		public int getValue() {
			return ordinal();
		}
		@Override
		public String toString() {
			return getDataIndex() + " : " + id;
		}
	}
	
	public enum VersionControlLayout implements ILayout {
		REVISION_NO("revision","item.tabs.versionControl.revisionNo",TReportLayoutBean.VERSION_CONTROL.REVISION_NO,
				true,ItemGridLayout.COLUMN_TYPE.STRING,110),
				
		REPOSITORY("repository","item.tabs.versionControl.repository",TReportLayoutBean.VERSION_CONTROL.REPOSITORY,
				true,ItemGridLayout.COLUMN_TYPE.STRING,110),
		
		DATE("date","item.tabs.versionControl.revisionDate",TReportLayoutBean.VERSION_CONTROL.DATE,
				true,ItemGridLayout.COLUMN_TYPE.DATETIME,120),
				
		USER("user","item.tabs.versionControl.revisionAuthor",TReportLayoutBean.VERSION_CONTROL.USER,
				true,ItemGridLayout.COLUMN_TYPE.STRING,170),
		
		MESSAGE("message","item.tabs.versionControl.revisionComment",TReportLayoutBean.VERSION_CONTROL.MESSAGE,
				true,ItemGridLayout.COLUMN_TYPE.STRING,520);
		
		private final int id;
		private final String headerKey;
		private final boolean sortable;
		private final String dataIndex;
		private final int type;
		private final int width;
		private VersionControlLayout(String dataIndex, String headerKey, int id,
				boolean sortable, int type, int width) {
			this.dataIndex = dataIndex;
			this.headerKey = headerKey;
			this.id = id;
			this.sortable = sortable;
			this.type = type;
			this.width = width;
		}
		@Override
		public int getId() {
			return id;
		}
		@Override
		public String getHeaderKey() {
			return headerKey;
		}
		@Override
		public boolean isSortable() {
			return sortable;
		}
		@Override
		public String getDataIndex() {
			return dataIndex;
		}
		@Override
		public int getType() {
			return type;
		}
		@Override
		public int getWidth() {
			return width;
		}
		@Override
		public ILayout getLayoutFromID(int id){
			for (VersionControlLayout hl : VersionControlLayout.values()) {
				if (hl.getId() == id) {
					return hl;
				}
			}
			return null;
		}
		public int getValue() {
			return ordinal();
		}
		@Override
		public String toString() {
			return getDataIndex() + " : " + id;
		}
	}
	
	public enum LinkLayout implements ILayout {
		LINK_TYPE("linkType","item.tabs.itemLink.lbl.linkType",TReportLayoutBean.LINK.LINK_TYPE,
				true,ItemGridLayout.COLUMN_TYPE.STRING,110),
		ITEM_ID("itemID","item.tabs.itemLink.lbl.itemID",TReportLayoutBean.LINK.ITEM_ID,
				true,ItemGridLayout.COLUMN_TYPE.INTEGER,70),
		ITEM_TITLE("itemTitle","item.tabs.itemLink.lbl.itemTitle",TReportLayoutBean.LINK.ITEM_TITLE,
				true,ItemGridLayout.COLUMN_TYPE.STRING,210),
		ITEM_STATUS("itemStatus","reportFilter.prompt.status",
				TReportLayoutBean.LINK.ITEM_STATUS,
				true,ItemGridLayout.COLUMN_TYPE.STRING,110),
		ITEM_RESPONSIBLE("itemResponsible","reportFilter.prompt.responsible",TReportLayoutBean.LINK.ITEM_RESPONSIBLE,
						true,ItemGridLayout.COLUMN_TYPE.STRING,170),		
		PARAMETERS("parameters","item.tabs.itemLink.lbl.parameters",TReportLayoutBean.LINK.PARAMETERS,
				true,ItemGridLayout.COLUMN_TYPE.STRING,150),
		COMMENT("comment","item.tabs.itemLink.lbl.comment",TReportLayoutBean.LINK.COMMENT,
				true,ItemGridLayout.COLUMN_TYPE.STRING,110);
		private final int id;
		private final String headerKey;
		private final boolean sortable;
		private final String dataIndex;
		private final int type;
		private final int width;
		private LinkLayout(String dataIndex, String headerKey, int id,
				boolean sortable, int type, int width) {
			this.dataIndex = dataIndex;
			this.headerKey = headerKey;
			this.id = id;
			this.sortable = sortable;
			this.type = type;
			this.width = width;
		}
		@Override
		public int getId() {
			return id;
		}
		@Override
		public String getHeaderKey() {
			return headerKey;
		}
		@Override
		public boolean isSortable() {
			return sortable;
		}
		@Override
		public String getDataIndex() {
			return dataIndex;
		}
		@Override
		public int getType() {
			return type;
		}
		@Override
		public int getWidth() {
			return width;
		}
		@Override
		public ILayout getLayoutFromID(int id){
			for (LinkLayout hl : LinkLayout.values()) {
				if (hl.getId() == id) {
					return hl;
				}
			}
			return null;
		}
		public int getValue() {
			return ordinal();
		}
		@Override
		public String toString() {
			return getDataIndex() + " : " + id;
		}
	}
	
	/**
	 * 
	 * @param layout
	 * @param layoutType
	 * @return
	 */
	public static boolean saveGridLayout(List<TReportLayoutBean> layout, Integer layoutType) {			
		if (layout!=null) {
			Iterator<TReportLayoutBean> iterator = layout.iterator();
			while (iterator.hasNext()) {
				TReportLayoutBean reportLayoutBean = iterator.next();
				reportLayoutBean.setLayoutKey(layoutType);
				reportLayoutDAO.save(reportLayoutBean);
			}
		}
		return true;
	}
	
	/**
	 * TODO implement me
	 * @return
	 */
	private static List<TReportLayoutBean> loadGridLayout(Integer personID, 
			boolean grouping, Integer layoutType){
		return reportLayoutDAO.getByPerson(personID, grouping, layoutType);
	}
	
	/**
	 * TODO implement me!
	 * @param personID
	 * @param layoutType
	 * @return
	 */
	public static void removeGridLayout(Integer personID, Integer layoutType) {
		reportLayoutDAO.removeByPerson(personID, layoutType);
	}
	public static String[] findSortInfo(List<ItemGridLayout> layout){
		String sortField=null;
		String sortDir=null;
		for (int i = 0; i < layout.size(); i++) {
			if(layout.get(i).getDirection()!=null){
				sortField=layout.get(i).getDataIndex();
				if(BooleanFields.TRUE_VALUE.endsWith(layout.get(i).getDirection())){
					sortDir="ASC";
				}else{
					sortDir="DESC";
				}
				return new String[]{sortField,sortDir};
			}
		}
		return null;
	}
	public static String[] getSortInfo(Integer tabID, List<ItemGridLayout> layout){
		String[] sortInfo=null;
		if(tabID!=null){
			switch (tabID.intValue()) {
			case ItemDetailBL.TAB_HISTORY:{
				sortInfo=getHistorySortInfo(layout);
				break;
			}
			case ItemDetailBL.TAB_COMMENTS:{
				sortInfo=getCommentsSortInfo(layout);
				break;
			}
			case ItemDetailBL.TAB_ATTACHMENTS:{
				sortInfo=getAttachmentSortInfo(layout);
				break;
			}
			case ItemDetailBL.TAB_WATCHERS:{
				sortInfo=getWatcherSortInfo(layout);
				break;
			}
			default:
				break;
			}
		}
		return sortInfo;
	}
	public static String[] getHistorySortInfo(List<ItemGridLayout> layout){
		String[] sortInfo=findSortInfo(layout);
		if(sortInfo==null){
			sortInfo=new String[]{HistoryLayout.LAST_EDIT.dataIndex,"DESC"};
		}
		return sortInfo;
	}
	public static String[] getCommentsSortInfo(List<ItemGridLayout> layout){
		String[] sortInfo=findSortInfo(layout);
		if(sortInfo==null){
			sortInfo=new String[]{CommentsLayout.LAST_EDIT.dataIndex,"DESC"};
		}
		return sortInfo;
	}
	public static String[] getAttachmentSortInfo(List<ItemGridLayout> layout){
		String[] sortInfo=findSortInfo(layout);
		if(sortInfo==null){
			sortInfo=new String[]{AttachmentLayout.LAST_EDIT.dataIndex,"DESC"};
		}
		return sortInfo;
	}
	public static String[] getWorklogSortInfo(List<ItemGridLayout> layout){
		String[] sortInfo=findSortInfo(layout);
		if(sortInfo==null){
			sortInfo=new String[]{WorklogLayout.LAST_EDIT.dataIndex,"DESC"};
		}
		return sortInfo;
	}
	public static String[] getWatcherSortInfo(List<ItemGridLayout> layout){
		String[] sortInfo=findSortInfo(layout);
		if(sortInfo==null){
			sortInfo=new String[]{WatcherLayout.TYPE.dataIndex,"DESC"};
		}
		return sortInfo;
	}
	public static String[] getVersioControlSortInfo(List<ItemGridLayout> layout){
		String[] sortInfo=findSortInfo(layout);
		if(sortInfo==null){
			sortInfo=new String[]{VersionControlLayout.DATE.dataIndex,"DESC"};
		}
		return sortInfo;
	}
	public static String[] getLinkSortInfo(List<ItemGridLayout> layout){
		String[] sortInfo=findSortInfo(layout);
		if(sortInfo==null){
			sortInfo=new String[]{LinkLayout.LINK_TYPE.dataIndex,"ASC"};
		}
		return sortInfo;
	}
	public static List<ItemGridLayout> loadLayout(Integer personKey, Locale locale,int layoutType, ILayout layout, List<ItemGridLayout> defaultLayouts) {		
		List<ItemGridLayout> layouts=new ArrayList<ItemGridLayout>();
		List<TReportLayoutBean> layoutsDb=loadGridLayout(personKey, false, layoutType);
		Map<Integer, TReportLayoutBean> groupingMap=getMap(loadGridLayout(personKey, true, layoutType));
		if(layoutsDb==null||layoutsDb.isEmpty()){
			return defaultLayouts;
		}
		List<ItemGridLayout> columNotUsed=new ArrayList<ItemGridLayout>();
		columNotUsed.addAll(defaultLayouts);
		for (int i = 0; i < layoutsDb.size(); i++) {
			TReportLayoutBean rlb=layoutsDb.get(i);
			ILayout hl=layout.getLayoutFromID(rlb.getReportField().intValue());
			if(hl==null){
				//no column found for id
				continue;
			}
			ItemGridLayout itemGridLayout=createGridLayout(hl);
			if (groupingMap.containsKey(rlb.getReportField())) {
				itemGridLayout.setGrouping(true);
			}
			itemGridLayout.setHidden(rlb.getFieldType().equals(TReportLayoutBean.FIELD_TYPE.HIDDEN));
			itemGridLayout.setWidth(rlb.getFieldWidth().intValue());
			itemGridLayout.setDirection(rlb.getFieldSortDirection());
			layouts.add(itemGridLayout);
			columNotUsed.remove(itemGridLayout);
		}
		if(!columNotUsed.isEmpty()){
			for (int i = 0; i < columNotUsed.size(); i++) {
				ItemGridLayout itemGridLayout=columNotUsed.get(i);
				itemGridLayout.setHidden(true);
				layouts.add(itemGridLayout);
			}
		}
		return layouts;
	}
	public static List<ItemGridLayout> loadLayout(Integer tabID, Integer personKey,
			Integer projectID, Locale locale) {
		List<ItemGridLayout> result=null;
		if(tabID!=null){
			switch (tabID.intValue()) {
			case ItemDetailBL.TAB_HISTORY:{
				result=loadHistoryLayout(personKey, locale);
				break;
			}
			case ItemDetailBL.TAB_COMMENTS:{
				result=loadCommentLayout(personKey, locale);
				break;
			}
			case ItemDetailBL.TAB_ATTACHMENTS:{
				result=loadAttachmentLayout(personKey, locale);
				break;
			}
			case ItemDetailBL.TAB_WORKLOG:{
				result=loadWorklogLayout(personKey, projectID, locale);
				break;
			}
			case ItemDetailBL.TAB_WATCHERS:{
				result=loadWatcherLayout(personKey, locale);
				break;
			}
			case ItemDetailBL.TAB_LINKS:{
				result=loadLinksLayout(personKey, locale);
				break;
			}
			case ItemDetailBL.TAB_VERSION_CONTROL:{
				result=loadVersionControlLayout(personKey, locale);
				break;
			}
			default:
				break;
			}
		}
		return result;
	}
	public static ILayout getLayoutFromID(Integer layoutID,Integer layoutType){
		ILayout layout=null;
		if (layoutType!=null) {
			switch (layoutType.intValue()) {
				case ItemDetailBL.TAB_HISTORY:{
					for (HistoryLayout hl : HistoryLayout.values()) {
						if (hl.getId() == layoutID) {
							return hl;
						}
					}
					break;
				}
				case ItemDetailBL.TAB_COMMENTS:{
					for (CommentsLayout hl : CommentsLayout.values()) {
						if (hl.getId() == layoutID) {
							return hl;
						}
					}
					break;
				}
				case ItemDetailBL.TAB_ATTACHMENTS:{
					for (AttachmentLayout hl : AttachmentLayout.values()) {
						if (hl.getId() == layoutID) {
							return hl;
						}
					}
					break;
				}
				case ItemDetailBL.TAB_WORKLOG:{
					for (WorklogLayout hl : WorklogLayout.values()) {
						if (hl.getId() == layoutID) {
							return hl;
						}
					}
					break;
				}
				case ItemDetailBL.TAB_WATCHERS:{
					for (WatcherLayout hl : WatcherLayout.values()) {
						if (hl.getId() == layoutID) {
							return hl;
						}
					}
					break;
				}
				case ItemDetailBL.TAB_LINKS:{
					for (LinkLayout hl : LinkLayout.values()) {
						if (hl.getId() == layoutID) {
							return hl;
						}
					}
					break;
				}
				case ItemDetailBL.TAB_VERSION_CONTROL:{
					for (VersionControlLayout hl : VersionControlLayout.values()) {
						if (hl.getId() == layoutID) {
							return hl;
						}
					}
					break;
				}
				default:
					break;
			}
		}
		return  layout;
	}
	public static List<ItemGridLayout> loadHistoryLayout(Integer personKey, Locale locale) {
		 List<ItemGridLayout> result=loadLayout(personKey, locale, TReportLayoutBean.LAYOUT_TYPE.HISTORY,
				HistoryLayout.LAST_EDIT,loadDefaultHistoryLayout());
		 for (Iterator iterator = result.iterator(); iterator.hasNext();) {
			ItemGridLayout itemGridLayout = (ItemGridLayout) iterator.next();
		}
		 return result;
	}
	public static List<ItemGridLayout> loadCommentLayout(Integer personKey, Locale locale) {
		return loadLayout(personKey, locale, TReportLayoutBean.LAYOUT_TYPE.COMMENT,
				CommentsLayout.LAST_EDIT,loadDefaultCommentsLayout());
	}
	public static List<ItemGridLayout> loadWorklogLayout(Integer personKey,
			Integer projectID, Locale locale) {
		List<ItemGridLayout> itemGridLayoutList = loadLayout(personKey, locale, TReportLayoutBean.LAYOUT_TYPE.WORKLOG,
				WorklogLayout.LAST_EDIT,loadDefaultWorklogLayout());
		ProjectAccountingTO projectAccountingTO = ProjectBL.getProjectAccountingTO(projectID);
		boolean workTracking = projectAccountingTO.isWorkTracking();
		boolean costTracking = projectAccountingTO.isCostTracking();
		ItemGridLayout workGridLayout = null;
		ItemGridLayout costGridLayout = null;
		for (ItemGridLayout itemGridLayout : itemGridLayoutList) {
			int id = itemGridLayout.getId();
			if (id==TReportLayoutBean.WORKLOG.WORK) {
				workGridLayout = itemGridLayout;
			}
			if (id==TReportLayoutBean.WORKLOG.COST) {
				costGridLayout = itemGridLayout;
			}
		}
		if (workTracking && costTracking) {
			if (workGridLayout!=null && costGridLayout==null) {
				//add costGridLayout
				itemGridLayoutList.add(createGridLayout(WorklogLayout.COST));
			} else {
				if (workGridLayout==null && costGridLayout!=null) {
					//add workGrid layout
					itemGridLayoutList.add(createGridLayout(WorklogLayout.WORK));
				}
			}
		} else {
			if (workTracking && !costTracking) {
				//remove costTracking if exists
				if(costGridLayout!=null){
					itemGridLayoutList.remove(costGridLayout);
				}
			} else {
				if (!workTracking && costTracking) {
					//remove workTracking if exists
					if(workGridLayout!=null){
						itemGridLayoutList.remove(workGridLayout);
					}
				}
			} 
		}
		return itemGridLayoutList;
	}
	public static List<ItemGridLayout> loadWatcherLayout(Integer personKey, Locale locale) {
		return loadLayout(personKey, locale, TReportLayoutBean.LAYOUT_TYPE.WATCHER,
				WatcherLayout.PERSON,loadDefaultWatcherLayout());
	}
	public static List<ItemGridLayout> loadAttachmentLayout(Integer personKey, Locale locale) {
		return loadLayout(personKey, locale, TReportLayoutBean.LAYOUT_TYPE.ATTACHMENT,
				AttachmentLayout.LAST_EDIT,loadDefaultAttachmentLayout());
	}
	public static List<ItemGridLayout> loadVersionControlLayout(Integer personKey, Locale locale) {
		return loadLayout(personKey, locale, TReportLayoutBean.LAYOUT_TYPE.VERSION_CONTROL,
				VersionControlLayout.REVISION_NO,loadDefaultVersionControlLayout());
	}
	public static List<ItemGridLayout> loadLinksLayout(Integer personKey, Locale locale) {
		List<ItemGridLayout> linkLayoutList = loadLayout(personKey, locale, TReportLayoutBean.LAYOUT_TYPE.LINK,
				LinkLayout.LINK_TYPE,loadDefaultLinkLayout());
		if (linkLayoutList!=null) {
			for (Iterator<ItemGridLayout> iterator = linkLayoutList.iterator(); iterator.hasNext();) {
				ItemGridLayout itemGridLayout = iterator.next();
				switch (itemGridLayout.getId()) {
				case LINK.LINK_TYPE:
					itemGridLayout.setHeader(
							LocalizeUtil.getLocalizedTextFromApplicationResources(
									"item.tabs.itemLink.lbl.linkType", locale));
					break;
				case LINK.ITEM_ID:
					itemGridLayout.setHeader(
							FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_ISSUENO, locale));
					break;
				case LINK.ITEM_TITLE:
					itemGridLayout.setHeader(
							FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_SYNOPSIS, locale));
					break;
				case LINK.PARAMETERS:
					itemGridLayout.setHeader(
							LocalizeUtil.getLocalizedTextFromApplicationResources(
									"item.tabs.itemLink.lbl.parameters", locale));
					break;
				case LINK.COMMENT:
					itemGridLayout.setHeader(
							LocalizeUtil.getLocalizedTextFromApplicationResources(
									"item.tabs.itemLink.lbl.comment", locale));
					break;
				case LINK.ITEM_STATUS:
					itemGridLayout.setHeader(
							FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_STATE, locale));
					break;
				case LINK.ITEM_RESPONSIBLE:
					itemGridLayout.setHeader(
							FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_RESPONSIBLE, locale));
					break;
				default:break;
				}
				
			}
		}
		return linkLayoutList;
	}
	private static Map<Integer, TReportLayoutBean> getMap(List<TReportLayoutBean> reportLayoutBeanList) {
		Map<Integer, TReportLayoutBean> map = new HashMap<Integer, TReportLayoutBean>(); 
		if (reportLayoutBeanList!=null) {
			Iterator<TReportLayoutBean> iterator = reportLayoutBeanList.iterator();
			while (iterator.hasNext()) {
				TReportLayoutBean reportLayoutBean = iterator
						.next();
				map.put(reportLayoutBean.getReportField(), reportLayoutBean);				
			}
		}
		return map;
	}
	
	public static List<ItemGridLayout> loadDefaultHistoryLayout() {
		List<ItemGridLayout> layouts=new ArrayList<ItemGridLayout>();
		layouts.add(createGridLayout(HistoryLayout.LAST_EDIT));
		layouts.add(createGridLayout(HistoryLayout.CHANGE_BY));
		layouts.add(createGridLayout(HistoryLayout.TYPE_OF_CHANGE));
		layouts.add(createGridLayout(HistoryLayout.DIFF));
		layouts.add(createGridLayout(HistoryLayout.NEW_VALUE,true));
		layouts.add(createGridLayout(HistoryLayout.OLD_VALUE,true));
		layouts.add(createGridLayout(HistoryLayout.COMMENT,true));
		return layouts;
	} 
	public static List<ItemGridLayout> loadDefaultCommentsLayout() {
		List<ItemGridLayout> layouts=new ArrayList<ItemGridLayout>();
		layouts.add(createGridLayout(CommentsLayout.LAST_EDIT));
		layouts.add(createGridLayout(CommentsLayout.CHANGE_BY));
		layouts.add(createGridLayout(CommentsLayout.COMMENT));
		return layouts;
	}
	public static List<ItemGridLayout> loadDefaultWorklogLayout() {
		List<ItemGridLayout> layouts=new ArrayList<ItemGridLayout>();
		layouts.add(createGridLayout(WorklogLayout.LAST_EDIT));
		layouts.add(createGridLayout(WorklogLayout.CHANGE_BY));
		layouts.add(createGridLayout(WorklogLayout.EFFORT_DATE));
		layouts.add(createGridLayout(WorklogLayout.SUBJECT));
		layouts.add(createGridLayout(WorklogLayout.WORK));
		layouts.add(createGridLayout(WorklogLayout.COST));
		layouts.add(createGridLayout(WorklogLayout.ACCOUNT));
		layouts.add(createGridLayout(WorklogLayout.DECRIPTION));
		return layouts;
	}
	public static List<ItemGridLayout> loadDefaultWatcherLayout() {
		List<ItemGridLayout> layouts=new ArrayList<ItemGridLayout>();
		layouts.add(createGridLayout(WatcherLayout.TYPE));
		layouts.add(createGridLayout(WatcherLayout.PERSON));
		return layouts;
	}
	public static List<ItemGridLayout> loadDefaultAttachmentLayout() {
		List<ItemGridLayout> layouts=new ArrayList<ItemGridLayout>();
		layouts.add(createGridLayout(AttachmentLayout.THUMBNAIL));
		layouts.add(createGridLayout(AttachmentLayout.FILE_NAME));
		layouts.add(createGridLayout(AttachmentLayout.FILE_SIZE));
		layouts.add(createGridLayout(AttachmentLayout.LAST_EDIT));
		layouts.add(createGridLayout(AttachmentLayout.CHANGE_BY));
		layouts.add(createGridLayout(AttachmentLayout.DESCRIPTION));
		return layouts;
	}
	public static List<ItemGridLayout> loadDefaultVersionControlLayout() {
		List<ItemGridLayout> layouts=new ArrayList<ItemGridLayout>();
		layouts.add(createGridLayout(VersionControlLayout.REVISION_NO));
		layouts.add(createGridLayout(VersionControlLayout.REPOSITORY));
		layouts.add(createGridLayout(VersionControlLayout.DATE));
		layouts.add(createGridLayout(VersionControlLayout.USER));
		layouts.add(createGridLayout(VersionControlLayout.MESSAGE));
		return layouts;
	}
	public static List<ItemGridLayout> loadDefaultLinkLayout() {
		List<ItemGridLayout> layouts=new ArrayList<ItemGridLayout>();
		layouts.add(createGridLayout(LinkLayout.LINK_TYPE));
		layouts.add(createGridLayout(LinkLayout.ITEM_ID));
		layouts.add(createGridLayout(LinkLayout.ITEM_TITLE));
		layouts.add(createGridLayout(LinkLayout.ITEM_STATUS));
		layouts.add(createGridLayout(LinkLayout.ITEM_RESPONSIBLE));
		layouts.add(createGridLayout(LinkLayout.PARAMETERS));
		layouts.add(createGridLayout(LinkLayout.COMMENT));
		return layouts;
	}
	public static ItemGridLayout createGridLayout(ILayout layout){
		return createGridLayout(layout,false);
	}
	public static ItemGridLayout createGridLayout(ILayout layout,boolean hidden){
		ItemGridLayout itemGridLayout=new ItemGridLayout(layout.getDataIndex(),layout.getHeaderKey(),layout.getId(),layout.isSortable(),layout.getType(),layout.getWidth());
		itemGridLayout.setHidden(hidden);
		return itemGridLayout;
	}
}
