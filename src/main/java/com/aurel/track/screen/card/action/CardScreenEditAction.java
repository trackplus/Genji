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

package com.aurel.track.screen.card.action;

import java.util.ArrayList;
import java.util.List;

import com.aurel.track.Constants;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.beans.screen.IScreen;
import com.aurel.track.beans.screen.ITab;
import com.aurel.track.itemNavigator.cardView.CardViewBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.action.AbstractScreenEditAction;
import com.aurel.track.screen.bl.design.AbstractFieldDesignBL;
import com.aurel.track.screen.bl.design.AbstractPanelDesignBL;
import com.aurel.track.screen.bl.design.AbstractScreenDesignBL;
import com.aurel.track.screen.bl.design.AbstractTabDesignBL;
import com.aurel.track.screen.card.CardScreen;
import com.aurel.track.screen.card.CardTab;
import com.aurel.track.screen.card.bl.design.*;
import com.aurel.track.screen.item.adapterDAO.ItemScreenFactory;


/**
 * Used to treat the operations from screen designer
 * operations on screen:reload,getproperty setproperty, deletetab,addtab
 * @author Adrian Bojani
 *
 */
public class CardScreenEditAction extends AbstractScreenEditAction {

	private static final long serialVersionUID = 340L;
	private TPersonBean personBean;

	@Override
	public void prepare() throws Exception {
		personBean=((TPersonBean) session.get(Constants.USER_KEY));

		super.prepare();
	}

	@Override
	public AbstractScreenDesignBL getScreenDesignBL() {
		return CardScreenDesignBL.getInstance();
	}

	@Override
	public AbstractTabDesignBL getTabDesignBL() {
		return CardTabDesignBL.getInstance();
	}

	@Override
	public AbstractPanelDesignBL getPanelDesignBL() {
		return CardPanelDesignBL.getInstance();
	}

	@Override
	protected AbstractFieldDesignBL getFieldDesignBL() {
		return CardFieldDesignBL.getInstance();
	}

	@Override
	public ScreenFactory getScreenFactory() {
		return ItemScreenFactory.getInstance();
	}

	@Override
	protected IScreen loadScreen() {
		IScreen screen=new CardScreen();
		screen.setObjectID(1);
		screen.setName("screen");

		ITab tab=new CardTab();
		tab.setObjectID(1);
		tab.setName("tab");
		tab.setLabel("tab");



		IPanel panel= CardViewBL.loadDesignCardTemplate(personBean);
		List<IPanel> panels=new ArrayList<IPanel>();
		panels.add(panel);
		tab.setPanels(panels);

		tabs=new ArrayList<ITab>();
		tabs.add(tab);
		screen.setTabs(tabs);

		return screen;
	}

	@Override
	protected String prepareJSON(IScreen screen,Integer selectedTab){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		JSONUtility.appendIntegerValue(sb, "selectedTab", selectedTab);
		JSONUtility.appendStringValue(sb,"backAction",backAction);
		JSONUtility.appendStringValue(sb,"configURL",configURL);
		sb.append(appendExtraInitJSON(screen,selectedTab));
		JSONUtility.appendJSONValue(sb, "screen",new CardScreenDesignJSON().encodeScreen(screen) , true);
		sb.append("}");
		return sb.toString();
	}

	public String localizeTab(String label){
		if(getText("item.form.tab.label."+label).equals("item.form.tab.label."+label)){
			return label;
		}else{
			return getText("item.form.tab.label."+label);
		}
	}
	@Override
	protected void clearCache(){
	}

	@Override
	public String getLayoutCls() {
		return "com.trackplus.layout.CardScreenEditLayout";
	}

	@Override
	public String getPageTitle() {
		return "itemov.cardView.configCardContent.title";
	}
}
