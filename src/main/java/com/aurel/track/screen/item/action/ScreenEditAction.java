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

package com.aurel.track.screen.item.action;

import java.util.ArrayList;
import java.util.List;

import com.aurel.track.beans.screen.IScreen;
import com.aurel.track.beans.screen.ITab;
import com.aurel.track.item.ItemScreenCache;
import com.aurel.track.screen.ScreenFactory;
import com.aurel.track.screen.action.AbstractScreenEditAction;
import com.aurel.track.screen.bl.design.AbstractFieldDesignBL;
import com.aurel.track.screen.bl.design.AbstractPanelDesignBL;
import com.aurel.track.screen.bl.design.AbstractScreenDesignBL;
import com.aurel.track.screen.bl.design.AbstractTabDesignBL;
import com.aurel.track.screen.item.adapterDAO.ItemScreenFactory;
import com.aurel.track.screen.item.bl.design.ScreenDesignBL;
import com.aurel.track.screen.item.bl.design.ScreenFieldDesignBL;
import com.aurel.track.screen.item.bl.design.ScreenPanelDesignBL;
import com.aurel.track.screen.item.bl.design.ScreenTabDesignBL;

/**
 * Used to treat the operations from screen designer
 * operations on screen:reload,getproperty setproperty, deletetab,addtab
 * @author Adrian Bojani
 *
 */
public class ScreenEditAction extends AbstractScreenEditAction {

	private static final long serialVersionUID = 340L;

	@Override
	public AbstractScreenDesignBL getScreenDesignBL() {
        return ScreenDesignBL.getInstance();
    }

    @Override
	public AbstractTabDesignBL getTabDesignBL() {
        return ScreenTabDesignBL.getInstance();
    }

    @Override
	public AbstractPanelDesignBL getPanelDesignBL() {
        return ScreenPanelDesignBL.getInstance();
    }

	@Override
	protected AbstractFieldDesignBL getFieldDesignBL() {
		return ScreenFieldDesignBL.getInstance();
	}

	@Override
	public ScreenFactory getScreenFactory() {
        return ItemScreenFactory.getInstance();
    }

	@Override
	protected IScreen loadScreen() {
		IScreen screen=null;
		screen=getScreenDesignBL().loadScreen(componentID);
		List<ITab> tabs=screen.getTabs();
		if(tabs.size()==1){
			Integer firstTabID=tabs.get(0).getObjectID();
			ITab firstTab= ScreenTabDesignBL.getInstance().loadFullTab(firstTabID);
			ScreenPanelDesignBL panelDesignBL=new ScreenPanelDesignBL();
			panelDesignBL.setLocale(locale);
			panelDesignBL.calculateFieldWrappers(firstTab);
			List<ITab> tabsFull=new ArrayList<ITab>();
			tabsFull.add(firstTab);
			screen.setTabs(tabsFull);
		}
		return screen;
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
		ItemScreenCache.getInstance().removeScreen(componentID);
	}
}
