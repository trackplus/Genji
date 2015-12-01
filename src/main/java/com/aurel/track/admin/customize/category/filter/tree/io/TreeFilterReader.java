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

package com.aurel.track.admin.customize.category.filter.tree.io;

import com.aurel.track.admin.customize.category.filter.QNode;


/**
 *  A class used to read the query tree from XML String
 */
public class TreeFilterReader {
	public static final String ROOT_NODE="QUERY";
	public static final String OR_NODE="OR";
	public static final String AND_NODE="AND";
	public static final String EXP_NODE="EXP";
	
	private static TreeFilterReader instance;
	public static TreeFilterReader getInstance(){
		if(instance==null){
			instance=new TreeFilterReader();
		}
		return instance;
	}

	/**
	 * Read the query tree from xml string
	 * @param xmlData the XML string data 
	 * @return the root node of the tree
	 */
	public QNode readQueryTree(String xmlData){
		QNode root=null;
		if(xmlData!=null && xmlData.length()>0){
			 root=new TreeFilterParser().parseDocument(xmlData);
		}
		return root;
	}

}
