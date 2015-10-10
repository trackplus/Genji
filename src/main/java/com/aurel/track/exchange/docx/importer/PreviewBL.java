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

package com.aurel.track.exchange.docx.importer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.aurel.track.util.TreeNode;

/**
 * Preparing data for docx preview
 * @author Tamas
 *
 */
public class PreviewBL {
	
	static List<TreeNode> convertToTreeNodeFromItemNode(List<ItemNode> itemNodes) {
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
		List<Integer> counterList = new LinkedList<Integer>();
		counterList.add(Integer.valueOf(0));
		if (itemNodes!=null) {
			for (ItemNode itemNode : itemNodes) {
				treeNodes.add(convertToTreeNodeFromItemNode(itemNode, counterList));
			}
		}
		return treeNodes;
	}
	
	static TreeNode convertToTreeNodeFromItemNode(ItemNode itemNode, List<Integer> counterList) {
		Integer counter = counterList.get(0);
		counter = Integer.valueOf(counter.intValue()+1);
		counterList.set(0, counter);
		TreeNode treeNode  = new TreeNode(String.valueOf(counter), itemNode.getTitle());
		treeNode.setIcon("documentPart");
		List<ItemNode> children = itemNode.getChildren();
		boolean leaf = children==null || children.isEmpty();
		treeNode.setLeaf(leaf);
		treeNode.setSelectable(false);
		if (!leaf) {
			List<TreeNode> treeNodechildren = new ArrayList<TreeNode>();
			treeNode.setChildren(treeNodechildren);
			for (ItemNode childItemNode : children) {
				treeNodechildren.add(convertToTreeNodeFromItemNode(childItemNode, counterList));
			}
		}
		return treeNode;
	}
}
