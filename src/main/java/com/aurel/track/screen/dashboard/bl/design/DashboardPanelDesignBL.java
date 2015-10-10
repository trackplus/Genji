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


package com.aurel.track.screen.dashboard.bl.design;

import java.util.Locale;

import com.aurel.track.beans.TDashboardFieldBean;
import com.aurel.track.beans.screen.IField;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.plugin.DashboardDescriptor;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.screen.FieldWrapper;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.bl.design.AbstractFieldDesignBL;
import com.aurel.track.screen.bl.design.AbstractPanelDesignBL;
import com.aurel.track.screen.dashboard.DashboardFieldWrapper;
import com.aurel.track.screen.dashboard.adapterDAO.DashboardScreenFactory;
import com.aurel.track.screen.dashboard.bl.DashboardUtil;

/**
 * A Business Logic class for ScreenPanel for designTime
 * @author Adrian Bojani
 *
 */
public class DashboardPanelDesignBL extends AbstractPanelDesignBL {
	//singleton isntance
	private static DashboardPanelDesignBL instance;
	private Locale locale;
	//private DashboarFieldDesignBL screenFieldBL;

	/**
	 * get a singleton instance
	 * @return
	 */
	public static DashboardPanelDesignBL getInstance() {
		if (instance == null) {
			instance = new DashboardPanelDesignBL();
		}
		return instance;
	}

	@Override
	protected ScreenFactory getScreenFactory() {
		return DashboardScreenFactory.getInstance();
	}

	/**
	 * constructor
	 */
	public DashboardPanelDesignBL() {
		super();
		//screenFieldBL=ScreenFieldDesignBL.getInstance();
	}
	@Override
	protected FieldWrapper createFieldWrapper(){
		return new DashboardFieldWrapper();
	}

	@Override
	protected void updateFieldWrapper(FieldWrapper fieldWrapper, IField fieldScreen) {
		if(locale==null){
			locale=Locale.getDefault();
		}
		String imageName=null;
		DashboardDescriptor descriptor= DashboardUtil.getDescriptor((TDashboardFieldBean)fieldScreen);
		if(descriptor!=null){
			imageName=descriptor.getThumbnail();
			((DashboardFieldWrapper)fieldWrapper).setJsConfigClass(descriptor.getJsConfigClass());
			fieldWrapper.setFieldType(descriptor.getName());
			fieldWrapper.setTooltip(LocalizeUtil.getLocalizedText(descriptor.getTooltip(), locale, descriptor.getBundleName()));
			//fieldWrapper.setSrcImageProject(descriptor.getThumbnailProject());
			//fieldWrapper.setSrcImageRelease(descriptor.getThumbnailRelease());
		}else{
			((DashboardFieldWrapper)fieldWrapper).setMissingPlugin(true);
			fieldWrapper.setHtmlString("Plugin missing:<B><I>"+((TDashboardFieldBean)fieldScreen).getDashboardID()+"</I></B>");
		}
		fieldWrapper.setJsonData(createJSonData(descriptor, (TDashboardFieldBean)fieldScreen, locale));
		//fieldWrapper.setSrcImage(imageName);
		//fieldWrapper.setOneColumn(true);
	}
	private String createJSonData( DashboardDescriptor descriptor,TDashboardFieldBean dashboardFieldBean,Locale locale){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		if(descriptor==null){
			JSONUtility.appendBooleanValue(sb, "missingPlugin", true);
			JSONUtility.appendStringValue(sb, "htmlString", "Plugin missing:<B><I>"+dashboardFieldBean.getDashboardID()+"</I></B>",true);
		}else{
			JSONUtility.appendStringValue(sb,"tooltip",LocalizeUtil.getLocalizedText(descriptor.getTooltip(), locale, descriptor.getBundleName()));
			JSONUtility.appendStringValue(sb,"srcImageProject",descriptor.getThumbnailProject());
			JSONUtility.appendStringValue(sb,"srcImageRelease",descriptor.getThumbnailRelease());
			JSONUtility.appendStringValue(sb,"srcImage",descriptor.getThumbnail());
			JSONUtility.appendBooleanValue(sb, "missingPlugin", false,true);
		}
		sb.append("}");
		return sb.toString();
	}

	@Override
	public AbstractFieldDesignBL getFieldDesignBL() {
		return DashboardFieldDesignBL.getInstance();
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}
