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

package com.aurel.track.plugin;

import java.util.ArrayList;
import java.util.List;

import com.aurel.track.vc.bl.VersionControlBL;

/**
 */
public class VersionControlDescriptor  extends BasePluginDescriptor {

	private static final long serialVersionUID = 340L;
    private String pageDescription;
    private List<BrowserDescriptor> browsers;
    
    
    
    public List<BrowserDescriptor> getBrowsers() {
		return browsers;
	}
	public void setBrowsers(List<BrowserDescriptor> browsers) {
		this.browsers = browsers;
	}
	
	
	
	
	public VersionControlDescriptor() {
		browsers=new ArrayList<VersionControlDescriptor.BrowserDescriptor>();
	}
    public String getPageDescription() {
        return pageDescription;
    }

    public void setPageDescription(String pageDescription) {
        this.pageDescription = pageDescription;
    }

    @Override
	public String toString() {
        return "VersionControlDescriptor:["+getId()+":"+getName()+"]";
    }
    public void addBrowser(BrowserDescriptor b){
    	if(browsers==null){
    		browsers=new ArrayList<VersionControlDescriptor.BrowserDescriptor>();
    	}
    	browsers.add(b);
    }
    public BrowserDescriptor findBrowser(String browserID){
    	if(browserID==null){
    		return  null;
    	}
    	if(browsers!=null){
    		BrowserDescriptor b=null;
    		for (int i = 0; i < browsers.size(); i++) {
    			b= browsers.get(i);
    			if(b!=null&&browserID.equals(b.getId())){
    				return b;
    			}
			}
    	}
    	return null;
    }
    public static class BrowserDescriptor{
    	private String id;
    	private String name;
    	private String changesetLink;
    	private String addedLink;
    	private String modifiedLink;
    	private String replacedLink;
    	private String deletedLink;
    	private String baseURL;
    	public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getChangesetLink() {
			return changesetLink;
		}
		public void setChangesetLink(String changesetLink) {
			this.changesetLink = changesetLink;
		}
		public String getAddedLink() {
			return addedLink;
		}
		public void setAddedLink(String addedLink) {
			this.addedLink = addedLink;
		}
		public String getModifiedLink() {
			return modifiedLink;
		}
		public void setModifiedLink(String modifiedLink) {
			this.modifiedLink = modifiedLink;
		}
		public String getReplacedLink() {
			return replacedLink;
		}
		public void setReplacedLink(String replacedLink) {
			this.replacedLink = replacedLink;
		}
		public String getDeletedLink() {
			return deletedLink;
		}
		public void setDeletedLink(String deletedLink) {
			this.deletedLink = deletedLink;
		}
		@Override
		public String toString(){
			return this.getClass()+":{id="+id+",name="+name+
				",changesetLink="+changesetLink+
				",addedLink="+addedLink+
				",modifiedLink="+modifiedLink+
				",replacedLink="+replacedLink+
				",deletedLink="+deletedLink+
				"}";
		}
		public String getBaseURL() {
			return baseURL;
		}
		public void setBaseURL(String baseURL) {
			this.baseURL = baseURL;
		}
    }
}
