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

package com.aurel.track.macro;

import com.aurel.track.Constants;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadItemLinksUtil;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.beans.TWorkItemLinkBean;
import com.aurel.track.item.ItemBL;
import com.aurel.track.item.ItemLoaderException;
import com.aurel.track.item.link.InlineItemLinkBL;
import com.aurel.track.item.link.ItemLinkBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.report.execute.ReportBeanLink;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.LocaleHandler;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.SessionAware;

import java.util.*;


public class MacroIssueAction extends ActionSupport implements Preparable, SessionAware,ApplicationAware{
	private static final Logger LOGGER = Logger.getLogger(MacroIssueAction.class);
	private Map<String,Object> session;
	private TPersonBean personBean;
	private Locale locale;

	private Integer workItemID;
	private Map application;
	private boolean anonymous;

	public void setSession(Map<String, Object> session) {
		this.session=session;
	}

	public void prepare() throws Exception {
		personBean = (TPersonBean) session.get(Constants.USER_KEY);
		locale=(Locale) session.get(Constants.LOCALE_KEY);
		if(personBean==null){
			personBean = PersonBL.loadByLoginName(TPersonBean.GUEST_USER);
			if(personBean!=null){
				PersonBL.setLicensedFeatures(personBean);
				anonymous=true;
				locale=personBean.getLocale();
				//
				LocaleHandler.exportLocaleToSession(session, locale);
			}

			//LOGGER.debug("Can't find 'guest' user: anonymous submission disabled");
		}else{
			anonymous=false;
		}
	}


	@Override
	public String execute(){
		LOGGER.debug("execute: workItemID:"+workItemID);
		TWorkItemBean workItemBean=null;
		try {
			workItemBean = ItemBL.loadWorkItem(workItemID);
		} catch (ItemLoaderException e) {
			e.printStackTrace();
			String errorMessage="No workItem found!";
			JSONUtility.encodeJSON(ServletActionContext.getResponse(), JSONUtility.encodeJSONFailure(errorMessage));
			return null;
		}


		List<TWorkItemLinkBean> allInlineItemLinkBeans = ItemLinkBL.loadByWorkItems(new int[]{workItemID});
		List<TWorkItemLinkBean> inlineItemLinkBeans=new ArrayList<TWorkItemLinkBean>();
		if(allInlineItemLinkBeans!=null){
			Integer linkTypeID= InlineItemLinkBL.getInlineItemLinkType();
			for(TWorkItemLinkBean workItemLinkBean:allInlineItemLinkBeans){
				if(!workItemLinkBean.getLinkType().equals(linkTypeID)){
					inlineItemLinkBeans.add(workItemLinkBean);
				}
			}
		}

		List<TWorkItemBean> itemBeanList=new ArrayList<TWorkItemBean>();
		itemBeanList.add(workItemBean);
		Map<Integer, SortedSet<ReportBeanLink>> inlineLinksMap = LoadItemLinksUtil.getAllLinkedWorkItems(itemBeanList, inlineItemLinkBeans, personBean.getObjectID(), locale, null);
		SortedSet<ReportBeanLink> links=inlineLinksMap.get(workItemID);
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendBooleanValue(sb, JSONUtility.JSON_FIELDS.SUCCESS, true);
		sb.append("\"data\":{");
		StringBuilder inlineItemHTML = MacroIssueBL.createInlineItemHTML(false,false, false, locale, workItemBean, links);
		JSONUtility.appendStringValue(sb,"html",inlineItemHTML.toString(),true);
		sb.append("}");
		sb.append("}");
		JSONUtility.encodeJSON(ServletActionContext.getResponse(), sb.toString());
		return null;
	}


	public Map getApplication() {
		return application;
	}

	public void setApplication(Map application) {
		this.application = application;
	}

	public void setWorkItemID(Integer workItemID) {
		this.workItemID = workItemID;
	}

	public Integer getWorkItemID() {
		return workItemID;
	}
}
