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

package com.aurel.track.admin.server.siteConfig;

import java.io.Serializable;
import java.util.List;

import com.aurel.track.util.LabelValueBean;

/**
 * Transfer object used for lucene search configuration
 * @author Adrian Bojani
 */
public class FullTextSearchTO implements Serializable{

	private static final long serialVersionUID = 400L;
	
	/**
	 * 
	 * The fields of this transfer object, that is their names in the JSON world.
	 * For example, in the user interface world a field would be named
	 * "fullTextSearch.indexAttachments". The base name builds the first part, the second
	 * part is the specific field.
	 *
	 */
	public interface JSONFIELDS {
		//for localizing the fields by errors
		String tabFullTextSearch = "tab.fullTextSearch";
		String fsFullTextSearch = "fsFullTextSearch";
		
		// base name
		String fullTextSearch = "fullTextSearch.";
		
		//fields
		String indexAttachments = fullTextSearch + "indexAttachments";
		String reindexOnStartup = fullTextSearch + "reindexOnStartup";
		String analyzer = fullTextSearch + "analyzer";
		String indexPath = fullTextSearch + "indexPath";
		String analyzers = fullTextSearch + "analyzers";
		String useLucene = fullTextSearch + "useLucene";
	}
	
	private boolean useLucene;
	private boolean indexAttachments;
	private boolean reindexOnStartup;
	private String analyzer;
	private String indexPath;

	List<LabelValueBean> analyzers;

	public boolean isUseLucene() {
		return useLucene;
	}

	public void setUseLucene(boolean useLucene) {
		this.useLucene = useLucene;
	}

	public boolean isIndexAttachments() {
		return indexAttachments;
	}

	public void setIndexAttachments(boolean indexAttachments) {
		this.indexAttachments = indexAttachments;
	}

	public boolean isReindexOnStartup() {
		return reindexOnStartup;
	}

	public void setReindexOnStartup(boolean reindexOnStartup) {
		this.reindexOnStartup = reindexOnStartup;
	}

	public String getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(String analyzer) {
		this.analyzer = analyzer;
	}

	public String getIndexPath() {
		return indexPath;
	}

	public void setIndexPath(String indexPath) {
		this.indexPath = indexPath;
	}

	public List<LabelValueBean> getAnalyzers() {
		return analyzers;
	}

	public void setAnalyzers(List<LabelValueBean> analyzers) {
		this.analyzers = analyzers;
	}
}
