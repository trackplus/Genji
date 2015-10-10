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

package com.aurel.track.admin.customize.treeConfig.mailTemplate;

import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.event.IEventSubscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 */
public class MailEventBL {

	private  MailEventBL(){
	}

	public static List<MailEventBean> loadAll(Locale locale){
		List<MailEventBean> eventBeans=new ArrayList<MailEventBean>();
		eventBeans.add(new MailEventBean(IEventSubscriber.EVENT_POST_ISSUE_CREATE,
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.mailTemplate.opt.itemCreated", locale)));
		eventBeans.add(new MailEventBean(IEventSubscriber.EVENT_POST_ISSUE_CREATE_BY_EMAIL,
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.mailTemplate.opt.itemCreatedEmail", locale)));
		eventBeans.add(new MailEventBean(IEventSubscriber.EVENT_POST_ISSUE_UPDATE,
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.mailTemplate.opt.itemChanged", locale)));
		eventBeans.add(new MailEventBean(IEventSubscriber.EVENT_POST_USER_REGISTERED, 
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.mailTemplate.opt.userRegistered", locale)));
		eventBeans.add(new MailEventBean(IEventSubscriber.EVENT_POST_USER_SELF_REGISTERED,
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.mailTemplate.opt.userSelfRegistered", locale)));
		eventBeans.add(new MailEventBean(IEventSubscriber.EVENT_POST_ISSUE_UPDATEPLANNEDVALUE, 
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.mailTemplate.opt.budgetPlanExpense", locale)));
		eventBeans.add(new MailEventBean(IEventSubscriber.EVENT_POST_USER_FORGOTPASSWORD, 
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.mailTemplate.opt.newPassword", locale)));
		eventBeans.add(new MailEventBean(IEventSubscriber.EVENT_POST_USER_REMINDER, 
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.mailTemplate.opt.reminder", locale)));
		eventBeans.add(new MailEventBean(IEventSubscriber.EVENT_POST_USER_CREATED_BY_EMAIL, 
				LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.mailTemplate.opt.clientCreateByMail", locale)));
		return eventBeans;
	}
}
