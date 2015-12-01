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

package com.aurel.track.plugin;
/**
 * the description for item action plugin
 * @author Adrian Bojani
 *
 */
public class ItemActionDescription extends BasePluginDescriptor{
	private static final long serialVersionUID = -5451109135108702966L;
	private String cssClass;
	private String imageInactive;
	private String tooltipKey;
	private String labelKey;
	private boolean useWizard;
	private String firstPageTemplate;
	private String finishLabelKey;
	private String secondPageTemplate;
	private boolean useBottomTabs;
	private Integer preselectedTab;
	private boolean createItem;
	private boolean editItem;
	
	private String title1;
	private String title;
	private String menuPath1;
	private String menuPath;
	private String helpPage;
	private String helpPage1;
	public String getCssClass() {
		return cssClass;
	}
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	public String getTooltipKey() {
		return tooltipKey;
	}
	public void setTooltipKey(String tooltipKey) {
		this.tooltipKey = tooltipKey;
	}
	public String getLabelKey() {
		return labelKey;
	}
	public void setLabelKey(String labelKey) {
		this.labelKey = labelKey;
	}
	public boolean isUseWizard() {
		return useWizard;
	}
	public void setUseWizard(boolean useWizard) {
		this.useWizard = useWizard;
	}
	public String getFirstPageTemplate() {
		return firstPageTemplate;
	}
	public void setFirstPageTemplate(String firstPageTemplate) {
		this.firstPageTemplate = firstPageTemplate;
	}
	public String getSecondPageTemplate() {
		return secondPageTemplate;
	}
	public void setSecondPageTemplate(String secondPageTemplate) {
		this.secondPageTemplate = secondPageTemplate;
	}
	public boolean isUseBottomTabs() {
		return useBottomTabs;
	}
	public void setUseBottomTabs(boolean useBottomTabs) {
		this.useBottomTabs = useBottomTabs;
	}
	public String getImageInactive() {
		return imageInactive;
	}
	public void setImageInactive(String imageInactive) {
		this.imageInactive = imageInactive;
	}
	public boolean isCreateItem() {
		return createItem;
	}
	public void setCreateItem(boolean createItem) {
		this.createItem = createItem;
	}
	public boolean isEditItem() {
		return editItem;
	}
	public void setEditItem(boolean editItem) {
		this.editItem = editItem;
	}
	public String getFinishLabelKey() {
		return finishLabelKey;
	}
	public void setFinishLabelKey(String finishLabelKey) {
		this.finishLabelKey = finishLabelKey;
	}
	public String getTitle1() {
		return title1;
	}
	public void setTitle1(String title1) {
		this.title1 = title1;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMenuPath() {
		return menuPath;
	}
	public void setMenuPath(String menuPath) {
		this.menuPath = menuPath;
	}
	public String getHelpPage() {
		return helpPage;
	}
	public void setHelpPage(String helpPage) {
		this.helpPage = helpPage;
	}
	public String getMenuPath1() {
		return menuPath1;
	}
	public void setMenuPath1(String menuPath1) {
		this.menuPath1 = menuPath1;
	}
	public String getHelpPage1() {
		return helpPage1;
	}
	public void setHelpPage1(String helpPage1) {
		this.helpPage1 = helpPage1;
	}

	public Integer getPreselectedTab() {
		return preselectedTab;
	}

	public void setPreselectedTab(Integer preselectedTab) {
		this.preselectedTab = preselectedTab;
	}
}
