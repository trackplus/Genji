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

package com.aurel.track.admin.shortcut;

import com.aurel.track.json.JSONUtility;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ShortcutBL {
	public static final String SHORTCUTS_JSON = "shortcutsJSON";
	public static interface MENU_ITEM{
		public final int DASHBOARD=0;
		public final int BROWSE_PROJECT=1;
		public final int ITEM_NAVIGATOR=3;
		public final int REPORTS=4;
		public final int ADMIN=5;
		public final int ADMIN_MY_SETTINGS=5001;
		public final int ADMIN_AUTOMAIL=5002;
		public final int ADMIN_ICALENDAR=5003;
		public final int ADMIN_PROJECTS=5004;
		public final int ADMIN_MANAGE_FILTERS=5005;
		public final int ADMIN_REPORT_TEMPLATES=5006;
		public final int NEW_ITEM=6;
	}

	public static interface KEY{
		public final int A=65;
		public final int H=72;
		public final int W=87;
		public final int N=78;
		public final int R=82;
		public final int I=73;
		public final int F1=112;
		public final int ZERO=48;
		public final int ONE=49;
		public final int TWO=50;


		public final int NINE=57;
	}

	public static List<Shortcut> getDefaultShortcuts(){
		List<Shortcut> result=new ArrayList<Shortcut>();
		result.add(new Shortcut(MENU_ITEM.DASHBOARD,true,false,true,KEY.H,"H"));
		result.add(new Shortcut(MENU_ITEM.BROWSE_PROJECT,true,false,true,KEY.W,"W"));
		result.add(new Shortcut(MENU_ITEM.ITEM_NAVIGATOR,true,false,true,KEY.N,"N"));
		result.add(new Shortcut(MENU_ITEM.REPORTS,true,false,true,KEY.R,"R"));
		result.add(new Shortcut(MENU_ITEM.ADMIN,true,false,true,KEY.A,"A"));
		result.add(new Shortcut(MENU_ITEM.NEW_ITEM,true,false,true,KEY.I,"I"));
		return result;
	}


	public static String encodeShortcutsJSON(){
		List<Shortcut> shortcuts=getDefaultShortcuts();
		StringBuilder sb=new StringBuilder();
		sb.append("[");
		for(int i=0;i<shortcuts.size();i++){
			Shortcut s=shortcuts.get(i);
			encodeShortcut(sb,s);
			if(i<shortcuts.size()-1){
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	private static void encodeShortcut(StringBuilder sb,Shortcut s){
		sb.append("{");
		JSONUtility.appendIntegerValue(sb,"menuItemID",s.getMenuItemID());
		JSONUtility.appendIntegerValue(sb,"ctx",s.getCtx());
		JSONUtility.appendBooleanValue(sb,"ctrl",s.isCtrl());
		JSONUtility.appendBooleanValue(sb,"alt",s.isAlt());
		JSONUtility.appendBooleanValue(sb,"shift",s.isShift());
		JSONUtility.appendIntegerValue(sb,"key",s.getKey());
		JSONUtility.appendStringValue(sb,"keyName",s.getKeyName(),true);
		sb.append("}");
	}
	public static class Shortcut{
		private int menuItemID;
		private int ctx;
		private boolean ctrl;
		private boolean alt;
		private boolean shift;
		private int key;
		private String keyName;
		public Shortcut(){
		}

		public Shortcut(int menuItemID, boolean ctrl, boolean alt, boolean shift, int key,String keyName) {
			this.menuItemID = menuItemID;
			this.ctrl = ctrl;
			this.alt = alt;
			this.shift = shift;
			this.key = key;
			this.keyName = keyName;
		}

		public int getMenuItemID() {
			return menuItemID;
		}

		public void setMenuItemID(int menuItemID) {
			this.menuItemID = menuItemID;
		}

		public int getCtx() {
			return ctx;
		}

		public void setCtx(int ctx) {
			this.ctx = ctx;
		}

		public boolean isCtrl() {
			return ctrl;
		}

		public void setCtrl(boolean ctrl) {
			this.ctrl = ctrl;
		}

		public boolean isAlt() {
			return alt;
		}

		public void setAlt(boolean alt) {
			this.alt = alt;
		}

		public boolean isShift() {
			return shift;
		}

		public void setShift(boolean shift) {
			this.shift = shift;
		}

		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key = key;
		}

		public String getKeyName() {
			return keyName;
		}

		public void setKeyName(String keyName) {
			this.keyName = keyName;
		}
	}
}
