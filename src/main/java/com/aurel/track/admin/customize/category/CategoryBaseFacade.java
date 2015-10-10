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

package com.aurel.track.admin.customize.category;

import java.util.List;
import java.util.Locale;

import com.aurel.track.admin.customize.category.CategoryBL.REPOSITORY_TYPE;
import com.aurel.track.admin.customize.category.CategoryBL.TYPE;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.fieldType.runtime.base.LookupContainer;

/**
 * Abstract facade for categories and leafs
 * 
 * @author Tamas
 * 
 */
public abstract class CategoryBaseFacade {

	/**
	 * Gets the label key for this type
	 */
	public abstract String getLabelKey();

	/**
	 * Creates a new bean
	 */
	public abstract ILabelBean getNewBean();

	/**
	 * Gets the categories/leafs by label
	 * 
	 * @param repository
	 * @param parentID
	 * @param projectID
	 * @param personID
	 * @param subtype
	 * @param label
	 * @return
	 */
	public abstract List<ILabelBean> getListByLabel(Integer repository,
			Integer parentID, Integer projectID, Integer personID,
			Integer subtype, String label);

	/**
	 * Sets the label for a category/leaf
	 * 
	 * @param labelBean
	 * @param label
	 * @return
	 */
	public abstract void setLabel(ILabelBean labelBean, String label);

	/**
	 * Gets the category/leaf by key
	 * 
	 * @param categoryID
	 * @return
	 */
	public abstract ILabelBean getByKey(Integer categoryID);

	/**
	 * Gets the category/leaf by key and locale
	 * 
	 * @param categoryID
	 * @param locale
	 * @return
	 */
	public abstract ILabelBean getByKey(Integer categoryID, Locale locale);

	/**
	 * Gets the root categories/leafs
	 * 
	 * @param repository
	 * @param projectID
	 * @param personID
	 * @param locale
	 * @return
	 */
	public abstract List<ILabelBean> getRootObjects(Integer repository,
			Integer projectID, Integer personID, Locale locale);

	/**
	 * Gets the root categories/leafs by projects
	 * 
	 * @param projectIDs
	 * @param locale
	 * @return
	 */
	public abstract List<ILabelBean> getRootObjectsByProjects(
			List<Integer> projectIDs, Locale locale);

	/**
	 * Gets the categories/leafs by parent
	 * 
	 * @param parentCategoryID
	 * @param locale
	 * @return
	 */
	public abstract List<ILabelBean> getByParent(Integer parentCategoryID,
			Locale locale);

	/**
	 * Gets the categories/leafs by parents
	 * 
	 * @param parentCategoryIDs
	 * @return
	 */
	public abstract List<ILabelBean> getByParents(
			List<Integer> parentCategoryIDs);

	/**
	 * Gets the project for a category
	 * 
	 * @param labelBean
	 * @return
	 */
	public abstract Integer getProjectID(ILabelBean labelBean);

	/**
	 * Sets the project for a category
	 * 
	 * @param labelBean
	 * @param projectID
	 * @return
	 */
	public abstract void setProjectID(ILabelBean labelBean, Integer projectID);

	/**
	 * Gets the parent category for a category, or the category for a leaf
	 * 
	 * @param labelBean
	 * @return
	 */
	public abstract Integer getParentID(ILabelBean labelBean);

	/**
	 * Sets the parent for a category
	 * 
	 * @param labelBean
	 * @param parentID
	 * @return
	 */
	public abstract void setParentID(ILabelBean labelBean, Integer parentID);

	/**
	 * Sets the repository
	 * 
	 * @param labelBean
	 * @param repository
	 * @return
	 */
	public abstract void setRepository(ILabelBean labelBean, Integer repository);

	/**
	 * Gets the person
	 * 
	 * @param labelBean
	 * @return
	 */
	public abstract Integer getCreatedBy(ILabelBean labelBean);

	/**
	 * Sets the person
	 * 
	 * @param labelBean
	 * @param person
	 * @return
	 */
	public abstract void setCreatedBy(ILabelBean labelBean, Integer person);

	/**
	 * Saves a new/modified object
	 * 
	 * @param labelBean
	 * @return
	 */
	public abstract Integer save(ILabelBean labelBean);

	/**
	 * Deletes an object by key (and also the eventual descendants if not leaf)
	 * 
	 * @param objectID
	 * @param categoryType
	 * @return
	 */
	public abstract void delete(Integer objectID, String categoryType);

	/**
	 * Does the node (leaf) or anything below the node (category) should be
	 * replaced in case of deleting the node
	 * 
	 * @param objectID
	 * @return
	 */
	public abstract boolean replaceNeeded(Integer objectID);

	/**
	 * Replace the node (leaf) or leafs below the node (category) by deleting
	 * the node
	 * 
	 * @param objectID
	 *            : can be a categoryID or a leafID
	 * @param replacementID
	 *            : is always a leafID
	 * @return
	 */
	public abstract void replace(Integer objectID, Integer replacementID);

	/**
	 * Verify again before save that: the private repository is extra-verified
	 * 
	 * @param repository
	 * @param objectID
	 * @param projectID
	 * @param personBean
	 * @return
	 */
	public boolean isModifiable(Integer repository, Integer objectID,
			Integer projectID, TPersonBean personBean) {
		boolean modifiable = false;
		if (repository == null) {
			return false;
		}
		switch (repository.intValue()) {
		case REPOSITORY_TYPE.PUBLIC:
			modifiable = personBean.isSys();
			break;
		case REPOSITORY_TYPE.PROJECT:
			modifiable = personBean.isSys();
			if (!modifiable && projectID != null) {
				modifiable = PersonBL.isProjectAdmin(personBean.getObjectID(),
						projectID);
				if (!modifiable) {
					TProjectBean projectBean = LookupContainer
							.getProjectBean(projectID);
					while (projectBean != null) {
						Integer parentProjectID = projectBean.getParent();
						if (parentProjectID != null) {
							modifiable = PersonBL.isProjectAdmin(
									personBean.getObjectID(), parentProjectID);
							if (modifiable) {
								break;
							}
							projectBean = LookupContainer
									.getProjectBean(parentProjectID);
						} else {
							projectBean = null;
						}
					}
				}
			}
			break;
		case REPOSITORY_TYPE.PRIVATE:
			if (objectID == null) {
				// the main "private" node
				modifiable = true;
			} else {
				ILabelBean labelBean = getByKey(objectID);
				if (labelBean != null) {
					modifiable = personBean.getObjectID().equals(
							getCreatedBy(labelBean));
				}
			}
			break;
		default:
			break;
		}
		return modifiable;
	}

	/**
	 * Whether the new label is valid (not empty not duplicate)
	 * 
	 * @param nodeID
	 * @param personID
	 * @param subtype
	 * @param label
	 * @param add
	 * @return
	 */
	public String isValidLabel(String nodeID, Integer personID,
			Integer subtype, String label, boolean add) {
		if (label == null || "".equals(label)) {
			return "common.err.required";
		}
		CategoryTokens categoryTokens = CategoryTokens.decodeNode(nodeID);
		Integer repository = categoryTokens.getRepository();
		Integer type = categoryTokens.getType();
		Integer objectID = categoryTokens.getObjectID();
		List<ILabelBean> labelBeans = null;
		Integer createdBy = null;
		if (repository.intValue() == REPOSITORY_TYPE.PRIVATE) {
			createdBy = personID;
		}
		Integer existingID = null;
		Integer projectID = null;
		Integer parentID = null;
		if (type != null) {
			switch (type.intValue()) {
			case TYPE.PROJECT:
				if (add) {
					// project root category: add to a project node directly
					projectID = objectID;
				}
				break;
			case TYPE.CATEGORY:
			case TYPE.LEAF:
				// add to a category or edit a category or a leaf
				if (add) {
					if (type.intValue() == TYPE.CATEGORY) {
						parentID = objectID;
					} else {
						ILabelBean siblingLabelBean = getByKey(objectID);
						parentID = getParentID(siblingLabelBean);
					}
				} else {
					// edit
					ILabelBean labelBean = getByKey(objectID);
					if (repository.intValue() == REPOSITORY_TYPE.PROJECT) {
						// suppose the projects are set throughout the entire
						// category hierarchy
						projectID = getProjectID(labelBean);
					}
					existingID = objectID;
					parentID = getParentID(labelBean);
				}
				break;
			default:
				break;
			}
		}
		labelBeans = getListByLabel(repository, parentID, projectID, createdBy,
				subtype, label);
		if (labelBeans == null || labelBeans.isEmpty()) {
			return null;
		} else {
			if (!labelBeans.isEmpty() && (existingID == null || // add
					(existingID != null && !labelBeans.get(0).getObjectID()
							.equals(existingID)))) {
				return "common.err.unique";
			}
		}
		return null;
	}

	/**
	 * Prepares a leaf (filter/report) bean after submit, before save
	 * 
	 * @param categoryTokens
	 * @param label
	 * @param personID
	 * @param add
	 * 
	 */
	public ILabelBean prepareBeanAfterAddEdit(CategoryTokens categoryTokens,
			String label, Integer personID, boolean add) {
		String categoryType = categoryTokens.getCategoryType();
		Integer repository = categoryTokens.getRepository();
		Integer type = categoryTokens.getType();
		Integer objectID = categoryTokens.getObjectID();
		ILabelBean labelBean = null;
		if (!add) {
			// edit existing
			labelBean = getByKey(objectID);
		}
		if (labelBean == null) {
			// either add or the category bean was deleted by other user/session
			// in the meantime
			labelBean = getNewBean();
			setRepository(labelBean, repository);
			setCreatedBy(labelBean, personID);
			if (add) {
				// objectID is the parent's objectID
				if (type == null) {
					// add directly to the public/private repository or a
					// project
					setParentID(labelBean, null);
				} else {
					switch (type.intValue()) {
					case CategoryBL.TYPE.PROJECT:
						// add directly to a project
						setParentID(labelBean, null);
						setProjectID(labelBean, objectID);
						break;
					case CategoryBL.TYPE.CATEGORY:
						CategoryFacade categoryFacade = CategoryFacadeFactory
								.getInstance().getCategoryFacade(categoryType);
						ILabelBean parentCategoryBean = categoryFacade
								.getByKey(objectID);
						if (parentCategoryBean != null) {
							// set parent only if parent still exists
							setParentID(labelBean, objectID);
							if (repository.intValue() == CategoryBL.REPOSITORY_TYPE.PROJECT) {
								// get the project from the parent category
								setProjectID(
										labelBean,
										categoryFacade
												.getProjectID(parentCategoryBean));
							}
						} else {
							// the parent was deleted by other by user/session
							// in the meantime
							if (repository.intValue() == CategoryBL.REPOSITORY_TYPE.PROJECT) {
								// fall back to root in the private repository:
								// can't be in a project repository if no
								// project is set
								setRepository(labelBean,
										CategoryBL.REPOSITORY_TYPE.PRIVATE);
							}
							setParentID(labelBean, null);
						}
						break;
					case CategoryBL.TYPE.LEAF: 
						LeafFacade leafFacade = CategoryFacadeFactory
								.getInstance().getLeafFacade(categoryType);
						ILabelBean siblingLeafBean = leafFacade
								.getByKey(objectID);
						if (siblingLeafBean != null) {
							setParentID(labelBean, getParentID(siblingLeafBean));
						}
						break;
					default:
						break;
					}
				}
			}
		}
		setLabel(labelBean, label);
		return labelBean;
	}

	/**
	 * Saves the bean after a cut/copy operation
	 * 
	 * @param labelBeanFrom
	 * @param repositoryTo
	 * @param projectIDTo
	 * @param categoryIDTo
	 * @param personID
	 * @param locale
	 * @param mightAddCopyFlagToLabel
	 * @param isCopy
	 * @return
	 */
	public Integer saveBeanAfterCutCopy(ILabelBean labelBeanFrom,
			Integer repositoryTo, Integer projectIDTo, Integer categoryIDTo,
			Integer personID, Locale locale, boolean mightAddCopyFlagToLabel,
			boolean isCopy) {
		Integer objectID = null;
		if (labelBeanFrom != null) {
			ILabelBean labelBeanTo;
			if (isCopy) {
				// prepare a copy of the leaf
				labelBeanTo = getNewBean();
				copySpecific(labelBeanFrom, labelBeanTo);
			} else {
				// only modify the leaf cut
				labelBeanTo = labelBeanFrom;
			}
			setRepository(labelBeanTo, repositoryTo);
			setProjectID(labelBeanTo, projectIDTo);
			setParentID(labelBeanTo, categoryIDTo);
			String label = labelBeanFrom.getLabel();
			Integer createdBy = null;
			if (repositoryTo.intValue() == CategoryBL.REPOSITORY_TYPE.PRIVATE) {
				createdBy = personID;
			}
			if (mightAddCopyFlagToLabel) {
				Integer fromID = null;
				if (!isCopy) {
					// important only if cut-paste in the same directory (not to
					// rename it)
					fromID = labelBeanFrom.getObjectID();
				}
				label = CategoryBL.getNextUniqueName(fromID, label,
						repositoryTo, categoryIDTo, projectIDTo, createdBy,
						getSubtype(labelBeanFrom), locale, this);
			}
			setLabel(labelBeanTo, label);
			setCreatedBy(labelBeanTo, personID);
			objectID = save(labelBeanTo);
		}
		return objectID;
	}

	/**
	 * Copy specific data if copy (not cut)
	 * 
	 * @param labelBeanFrom
	 * @param labelBeanTo
	 */
	public abstract void copySpecific(ILabelBean labelBeanFrom,
			ILabelBean labelBeanTo);

	/**
	 * Get the subtype the next unique name is looked by
	 * 
	 * @param labelBeanFrom
	 * @return
	 */
	public abstract Integer getSubtype(ILabelBean labelBeanFrom);

}
