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

package com.aurel.track.item.operation;

import java.util.List;
import java.util.Locale;

import com.aurel.track.admin.customize.lists.ListBL;
import com.aurel.track.admin.customize.lists.systemOption.IssueTypeBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TListTypeBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.fieldType.constants.SystemFields;

/**
 */
public class ItemOperationSelectIssueType extends ItemOperationSelect {
	public ItemOperationSelectIssueType(Locale locale){
		super(SystemFields.ISSUETYPE,false,true,false,locale);
		this.iconName= ListBL.ICONS_CLS.ISSUETYPE_ICON;
	}
	@Override
	protected List<ILabelBean> getOptionsInternal(TPersonBean personBean,Locale locale,Integer nodeID){
		return (List)IssueTypeBL.loadAllSelectable(locale);
	}
	@Override
	protected String getSymbol(ILabelBean labelBean) {
		return ((TListTypeBean)labelBean).getSymbol();
	}
}