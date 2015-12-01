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

package com.aurel.track.admin.customize.category;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.beans.ILabelBean;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.TreeNode;

/**
 * Abstract facade for categories
 * @author Tamas
 *
 */
public abstract class CategoryFacade extends CategoryBaseFacade {
		
	/**
	 * Gets the label key for this type
	 */
	@Override
	public String getLabelKey() {
		return "admin.customize.queryFilter.lbl.category";
	}
	
	/**
	 * Does the node has content (subcategories or leafs) 
	 * @param objectID
	 * @return
	 */
	public abstract boolean hasDependentData(Integer objectID);
	
	/**
	 * Get simple nodes for picker
	 * @param categoryType
	 * @param repository
	 * @param labelBean
	 * 
	 */
	public TreeNode getSimpleNode(String categoryType, Integer repository, ILabelBean labelBean) {
		CategoryTokens categoryTokens = new CategoryTokens(categoryType,
			repository, Integer.valueOf(CategoryBL.TYPE.CATEGORY), labelBean.getObjectID());
		String nodeID = CategoryTokens.encodeNode(categoryTokens);
		TreeNode treeNode = new TreeNode(nodeID, labelBean.getLabel());
		treeNode.setLeaf(Boolean.FALSE);
		return treeNode;
	}
	
	/**
	 * Add category nodes/rows
	 * @param categoryType
	 * @param repository
	 * @param labelBeans
	 * @param modifiable
	 * @param tree
	 * @param locale
	 * @param nodes
	 */
	protected void addCategories(String categoryType, Integer repository, List<ILabelBean> labelBeans,
			boolean modifiable, boolean tree, Locale locale, List<CategoryTO> nodes) {
		if (labelBeans!=null) {
			String typeLabel = null;
			if (!tree) {
				typeLabel = LocalizeUtil.getLocalizedTextFromApplicationResources(
						"admin.customize.queryFilter.lbl.category", locale);
			}		
			for (ILabelBean labelBean : labelBeans) {
				CategoryTokens categoryTokens = new CategoryTokens(categoryType,
						repository, Integer.valueOf(CategoryBL.TYPE.CATEGORY), labelBean.getObjectID());
				String nodeID = CategoryTokens.encodeNode(categoryTokens);
				CategoryTO categoryTO = null;
				if (tree) {
					categoryTO = new CategoryNodeTO(nodeID, categoryType,
							labelBean.getLabel(), modifiable, modifiable, true, false);
				} else {
					categoryTO = new CategoryGridRowTO(nodeID, categoryType,
							labelBean.getLabel(), typeLabel, modifiable, false);
				}
				nodes.add(categoryTO);
			}
		}
	}
	
	/**
	 * Gets the detail json for a leaf
	 * @param objectID
	 * @param add
	 * @param modifiable
	 * @param personID
	 * @param locale
	 * @return
	 */
	public String getDetailJSON(Integer objectID, boolean add,
			boolean modifiable, Integer personID, Locale locale) {
		String label = null;	
		if (!add) {
			ILabelBean labelBean = getByKey(objectID);
			if (labelBean!=null) {
				label = labelBean.getLabel();
			}
		}
		return CategoryJSON.getCategoryDetailJSON(label, modifiable);
	}
	
	/**
	 * Whether the copy of an ancestor is intended to be made into a descendant
	 * (endless circle)  
	 * @param ancestorCategory
	 * @param childCategory
	 * @return
	 * 
	 */
	public boolean isAncestor(Integer ancestorCategory, Integer childCategory) {
		ILabelBean labelBean = getByKey(childCategory);
		if (labelBean!=null) {
			Integer parentCategory = getParentID(labelBean);
			if (parentCategory==null) {
				//up to the root the ancestor was not found 
				return false;				
			} else {
				if (parentCategory.equals(ancestorCategory)) {
					//the ancestor was found at a certain level
					return true;
				} else {
					return isAncestor(ancestorCategory, parentCategory);					
				}
			}
		} else {
			return false;
		}
	}		
	
	/**
	 *  Copies a filter category recursively (with descendants)
	 * @param labelBeanFrom
	 * @param repositoryFrom
	 * @param repositoryTo
	 * @param projectTo
	 * @param categoryTo
	 * @param personID
	 * @param locale
	 * @param mightAddCopyFlagToLabel
	 * @param isCopy
	 * @param leafFacade
	 * @param oldToNewLeafIDs out parameter for storing the correspondence between old and new leaf ids
	 * @return
	 */
	public Integer copySubtree(ILabelBean labelBeanFrom,
			Integer repositoryFrom, Integer repositoryTo, Integer projectTo, Integer categoryTo,
			Integer personID, Locale locale, boolean mightAddCopyFlagToLabel, boolean isCopy,
			LeafFacade leafFacade, Map<Integer, Integer> oldToNewLeafIDs) {					
		//copy the category with descendants
		Integer categoryObjectID = saveBeanAfterCutCopy(labelBeanFrom, repositoryTo,
				projectTo, categoryTo, personID, locale, mightAddCopyFlagToLabel, isCopy);		
				
		//recursively copy/cut the subcategories: if cut only actualize some fields: repository, project			
		List<ILabelBean> categoryChildrenFrom = getByParent(labelBeanFrom.getObjectID(), locale);
		
		//copy/cut the subcategories recursively
		if (categoryChildrenFrom!=null) {
			Iterator<ILabelBean> iterator = categoryChildrenFrom.iterator();
			while (iterator.hasNext()) {
				ILabelBean labelBean = iterator.next();
				copySubtree(labelBean, repositoryFrom, repositoryTo,
						projectTo, categoryObjectID, personID, locale, false, false, leafFacade, oldToNewLeafIDs);
			}
		}
		//copy/cut the leafs
		List<ILabelBean> leafs = leafFacade.getByParent(labelBeanFrom.getObjectID(), locale);
		for (ILabelBean labelBean : leafs) {
			Integer newLeadID = leafFacade.saveBeanAfterCutCopy(labelBean, repositoryTo, projectTo, categoryObjectID,
					personID, locale, false, isCopy);
			oldToNewLeafIDs.put(labelBean.getObjectID(), newLeadID);
		}
		return categoryObjectID;
	}	
	
	/**
	 * Copy specific data if copy (not cut)
	 * @param labelBeanFrom
	 * @param labelBeanTo
	 */
	@Override
	public void copySpecific(ILabelBean labelBeanFrom, ILabelBean labelBeanTo) {		
	}
	
	/**
	 * Get the subtype the next unique name is looked by
	 * @param labelBeanFrom
	 * @return
	 */
	@Override
	public Integer getSubtype(ILabelBean labelBeanFrom) {
		return null;
	}
	
	
	/**
	 * Deletes an object by key (and also the eventual descendants if not leaf)
	 * @param objectID
	 * @param categoryType
	 * @return
	 */
	@Override
	public void delete(Integer objectID, String categoryType) {		
		LeafFacade leafFacade = CategoryFacadeFactory.getInstance().getLeafFacade(categoryType);
		//delete the subcategories
		List<ILabelBean> categoryChildren = getByParent(objectID, null);
		for (ILabelBean labelBean : categoryChildren) {
			delete(labelBean.getObjectID(), categoryType);
		}
		//delete the leafs	
		List<ILabelBean> leafs = leafFacade.getByParent(objectID, null);
		for (ILabelBean labelBean : leafs) {
			leafFacade.delete(labelBean.getObjectID(), categoryType);
		}
		//now delete the empty category
		deleteCategory(objectID);				
	}
	
	/**
	 * Deletes the category itself after all descendants are deleted
	 * @param categoryID
	 * @return
	 */
	public abstract void deleteCategory(Integer categoryID);
}
