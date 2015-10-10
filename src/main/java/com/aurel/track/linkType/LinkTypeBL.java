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

package com.aurel.track.linkType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aurel.track.beans.TLinkTypeBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.LinkTypeDAO;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.linkType.ILinkType.FILTER_EXPRESSION_HIERARCHICAL;
import com.aurel.track.linkType.ILinkType.FILTER_EXPRESSION_PLAIN;
import com.aurel.track.linkType.ILinkType.LINK_DIRECTION;
import com.aurel.track.linkType.ILinkType.PARENT_CHILD_EXPRESSION;
import com.aurel.track.plugin.PluginDescriptor;
import com.aurel.track.plugin.PluginManager;
import com.aurel.track.resources.LocalizationKeyPrefixes;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.IntegerStringBean;
import com.aurel.track.util.LabelValueBean;

/**
 * Business logic for link type
 * @author Tamas
 *
 */
public class LinkTypeBL {
	private static LinkTypeDAO linkTypeDAO = DAOFactory.getFactory().getLinkTypeDAO();




	/**
	 * Loads a linkTypeBean by key
	 * @param objectID
	 * @return
	 */
	public static TLinkTypeBean loadByPrimaryKey(Integer objectID) {
		return linkTypeDAO.loadByPrimaryKey(objectID);
	}

	/**
	 * Gets the list of TWorkItemLinkBean by linkIDsList
	 * @param linkTypeIDsList
	 * @return
	 */
	public static List<TLinkTypeBean> loadByLinkIDs(List<Integer> linkTypeIDsList) {
		return linkTypeDAO.loadByLinkIDs(linkTypeIDsList);
	}

	/**
	 * Load all defined linkType beans
	 * @return
	 */
	public static List<TLinkTypeBean> loadAll() {
		return linkTypeDAO.loadAll();
	}



	/**
	 * Load all linkType beans of a type
	 * @param linkTypeClass
	 * @return
	 */
	public static List<TLinkTypeBean> loadByLinkType(String linkTypeClass, Integer linkTypeID) {
		return linkTypeDAO.loadByLinkType(linkTypeClass, linkTypeID);
	}

	public static boolean hasLinksOfType(Integer linkTypeID) {
		return linkTypeDAO.hasLinksOfType(linkTypeID);
	}

	/**
	 * Saves a linkType
	 * @param linkTypeBean
	 * @return
	 */
	public static Integer save(TLinkTypeBean linkTypeBean) {
		return linkTypeDAO.save(linkTypeBean);
	}

	/**
	 * Saves a linkType
	 * @param linkTypeBean
	 * @return
	 */
	public static void delete(Integer objectID) {
		linkTypeDAO.delete(objectID);
	}



	/**
	 * Deletes a linkType
	 * @param oldLinkTypeID
	 * @param newLinkTypeID
	 */
	public static void replaceAndDelete(Integer oldLinkTypeID, Integer newLinkTypeID) {
		linkTypeDAO.replaceAndDelete(oldLinkTypeID, newLinkTypeID);
	}

	/**
	 * Gets the list of installed link type plugins
	 * @return
	 */
	public static List<LabelValueBean> getLinkTypePluginList(){
		List<PluginDescriptor> installedLinkTypePluginsList=getInstalledLinkTypePluginsList();
		List<LabelValueBean> linkTypePluginList=new ArrayList<LabelValueBean>();
		PluginDescriptor pluginDescriptor;
		for (int i = 0; i < installedLinkTypePluginsList.size(); i++) {
			pluginDescriptor=installedLinkTypePluginsList.get(i);
			linkTypePluginList.add(new LabelValueBean(pluginDescriptor.getName(),pluginDescriptor.getId()));
		}
		return linkTypePluginList;
	}

	/**
	 * Get the reverse direction if possible
	 * @param direction
	 * @return
	 */
	public static int getReverseDirection(int direction) {
		if (direction==LINK_DIRECTION.LEFT_TO_RIGHT) {
			return LINK_DIRECTION.RIGHT_TO_LEFT;
		} else {
			if (direction==LINK_DIRECTION.RIGHT_TO_LEFT) {
				return LINK_DIRECTION.LEFT_TO_RIGHT;
			}
		}
		return direction;
	}

	/**
	 * Gets a link type instance from a linkTypeID
	 * @param linkTypeID
	 * @return
	 */
	public static ILinkType getLinkTypePluginInstanceByLinkTypeKey(Integer linkTypeID) {
		if (linkTypeID!=null) {
			TLinkTypeBean linkTypeBean = loadByPrimaryKey(linkTypeID);
			if (linkTypeBean!=null) {
				String linkTypePlugin = linkTypeBean.getLinkTypePlugin();
				if (linkTypePlugin!=null) {
					return (ILinkType)PluginManager.getInstance().getPluginClass(PluginManager.LINKTYPE_ELEMENT, linkTypePlugin);
				}
			}
		}
		return null;
	}

	/**
	 * Gets the link type instances for each link type objectID
	 * @return
	 */
	public static Map<Integer, ILinkType> getLinkTypeIDToLinkTypeMap() {
		Map<Integer, ILinkType> linkTypeIDToLinkTypeMap = new HashMap<Integer, ILinkType>();
		List<TLinkTypeBean> linkTypeBeans = loadAll();
		if (linkTypeBeans!=null) {
			for (TLinkTypeBean linkTypeBean : linkTypeBeans) {
				String linkTypePlugin = linkTypeBean.getLinkTypePlugin();
				if (linkTypePlugin!=null) {
					ILinkType linkType = (ILinkType)PluginManager.getInstance().getPluginClass(PluginManager.LINKTYPE_ELEMENT, linkTypePlugin);
					if (linkType!=null) {
						linkTypeIDToLinkTypeMap.put(linkTypeBean.getObjectID(), linkType);
					}
				}
			}
		}
		return linkTypeIDToLinkTypeMap;
		
	}
	
	/**
	 * Gets a link type instance from a linkTypeID
	 * @param linkTypeID
	 * @return
	 */
	public static String getLinkTypePluginString(Integer linkTypeID) {
		if (linkTypeID!=null) {
			TLinkTypeBean linkTypeBean = loadByPrimaryKey(linkTypeID);
			if (linkTypeBean!=null) {
				return linkTypeBean.getLinkTypePlugin();
			}
		}
		return null;
	}

	/**
	 * Whether a link type is bidirectional
	 * @param objectID
	 * @return
	 */
	public static boolean isBidirectional(Integer objectID) {
		TLinkTypeBean linkTypeBean = loadByPrimaryKey(objectID);
		if (linkTypeBean==null || linkTypeBean.getLinkDirection()==null) {
			return false;
		} else {
			return LINK_DIRECTION.BIDIRECTIONAL==linkTypeBean.getLinkDirection().intValue();
		}
	}

	/**
	 * Gets the MS project link types defined. Probably only one
	 * @return
	 */
	public static List<Integer> getLinkTypesByPluginClass(ILinkType linkType) {
		List<TLinkTypeBean> linkTypeBeans = loadByLinkType(linkType.getPluginID(), null);
		return GeneralUtils.createIntegerListFromBeanList(linkTypeBeans);
	}

	/**
	 * Get the installed linkType plugins list
	 * @return
	 */
	private static List<PluginDescriptor> getInstalledLinkTypePluginsList() {
		List<PluginDescriptor> installedLinkTypes = PluginManager.getInstance().getPluginDescriptorListOfType(PluginManager.LINKTYPE_ELEMENT);
		if (installedLinkTypes==null) {
			return new ArrayList<PluginDescriptor>();
		} else {
			return installedLinkTypes;
		}
	}

	/**
	 * Whether a linkType plugin is selectable for query result superset
	 * (appears in the link types dropdown in tree queries)
	 * @param linkTypePluginID
	 * @return
	 */
	public static boolean selectableForQueryResultSuperset(String linkTypePluginID) {
		ILinkType linkType = (ILinkType)PluginManager.getInstance().getPluginClass(PluginManager.LINKTYPE_ELEMENT, linkTypePluginID);
		if (linkType==null) {
			return false;
		} else {
			return linkType.selectableForQueryResultSuperset();
		}
	}

	/**
	 * Whether a linkType plugin is hierarchical (more levels make sense) or just one level
	 * @param linkTypePluginID
	 * @return
	 */
	public static boolean isHierarchical(String linkTypePluginID) {
		ILinkType linkType = (ILinkType)PluginManager.getInstance().getPluginClass(PluginManager.LINKTYPE_ELEMENT, linkTypePluginID);
		if (linkType==null) {
			return false;
		} else {
			return linkType.isHierarchical();
		}
	}

	/**
	 * Get link names for link types.
	 * @param locale
	 * @param onlyGantt
	 * @param excludeInline
	 * @return
	 */
	public static List<LabelValueBean> getLinkTypeNamesList(Locale locale, boolean onlyGantt, boolean excludeInline) {
		List<LabelValueBean> linkTypeNames = new ArrayList<LabelValueBean>();
		List<TLinkTypeBean> linkTypeBeans = loadAll();
		if (onlyGantt) {
			for (Iterator<TLinkTypeBean> iterator = linkTypeBeans.iterator(); iterator.hasNext();) {
				TLinkTypeBean linkTypeBean = iterator.next();
				Integer linkTypeID = linkTypeBean.getObjectID();
				String linkTypePluginClass = LinkTypeBL.getLinkTypePluginString(linkTypeID);
				ILinkType linkType = (ILinkType)PluginManager.getInstance().getPluginClass(PluginManager.LINKTYPE_ELEMENT, linkTypePluginClass);
				if (linkType != null && !linkType.isGanttSpecific()) {
					iterator.remove();
				}
			}
		} else {
			if (excludeInline) {
				for (Iterator<TLinkTypeBean> iterator = linkTypeBeans.iterator(); iterator.hasNext();) {
					TLinkTypeBean linkTypeBean = iterator.next();
					Integer linkTypeID = linkTypeBean.getObjectID();
					String linkTypePluginClass = LinkTypeBL.getLinkTypePluginString(linkTypeID);
					ILinkType linkType = (ILinkType)PluginManager.getInstance().getPluginClass(PluginManager.LINKTYPE_ELEMENT, linkTypePluginClass);
					if (linkType != null && linkType.isInline()) {
						iterator.remove();
					}
				}
			}
		}
		//get all localized names for all linkTypes:
		//key - linkTypeID (primaryKeyValue from TLocalizedResources table)
		//value - map: key - fieldName form TLocalizedResources table composed of LocalizationKeyPrefixes.LINKTYPE_NAME + direction
		//				value - the localized text
		if (linkTypeBeans!=null && !linkTypeBeans.isEmpty()) {
			Map<Integer, Map<String, String>> localizedNameLabels =
					getLocalizedResourcesMap(linkTypeBeans, LocalizationKeyPrefixes.LINKTYPE_NAME, locale);
			for (TLinkTypeBean linkTypeBean : linkTypeBeans) {
				addNameForLinkType(linkTypeNames, linkTypeBean, localizedNameLabels);
			}
		}
		return linkTypeNames;
	}

	/**
	 * Get the possible superset options
	 * @param name
	 * @param linkTypeBean
	 * @param localizedSupersetLabels
	 * @return
	 */
	private static void addNameForLinkType(List<LabelValueBean> name,
			TLinkTypeBean linkTypeBean, Map<Integer, Map<String, String>> localizedSupersetLabels) {
		Integer direction = linkTypeBean.getLinkDirection();
		if (linkTypeBean!=null) {
			Integer linkTypeID = linkTypeBean.getObjectID();
			switch (direction.intValue()) {
			case LINK_DIRECTION.LEFT_TO_RIGHT:
				addLocalizedLinkTypeLabels(name, linkTypeID,
					linkTypeBean.getLinkName(LINK_DIRECTION.LEFT_TO_RIGHT),
					Integer.valueOf(LINK_DIRECTION.LEFT_TO_RIGHT), Integer.valueOf(LINK_DIRECTION.LEFT_TO_RIGHT), true, localizedSupersetLabels);
				break;
			case LINK_DIRECTION.RIGHT_TO_LEFT:
				addLocalizedLinkTypeLabels(name, linkTypeID,
					//not an error: when it is unidirectional we take always the Name and not the reverse name (independently of the direction)
					linkTypeBean.getLinkName(LINK_DIRECTION.LEFT_TO_RIGHT),
					Integer.valueOf(LINK_DIRECTION.LEFT_TO_RIGHT), Integer.valueOf(LINK_DIRECTION.RIGHT_TO_LEFT), true, localizedSupersetLabels);
				break;
			default:
				addLocalizedLinkTypeLabels(name, linkTypeID,
					linkTypeBean.getLinkName(LINK_DIRECTION.LEFT_TO_RIGHT),
					Integer.valueOf(LINK_DIRECTION.LEFT_TO_RIGHT), Integer.valueOf(LINK_DIRECTION.LEFT_TO_RIGHT), true, localizedSupersetLabels);
				addLocalizedLinkTypeLabels(name, linkTypeID,
					linkTypeBean.getLinkName(LINK_DIRECTION.RIGHT_TO_LEFT),
					Integer.valueOf(LINK_DIRECTION.RIGHT_TO_LEFT), Integer.valueOf(LINK_DIRECTION.RIGHT_TO_LEFT), true, localizedSupersetLabels);
				break;
			}
		}
	}

	/**
	 * Get those link types which supports the superset search (for link types dropdown in tree queries)
	 * @return
	 */
	public static List<LabelValueBean> getLinkTypeFilterSupersetExpressions(Locale locale, boolean includeParentChildRelations) {
		List<LabelValueBean> filterSupersetOptions = new ArrayList<LabelValueBean>();
		List<TLinkTypeBean> linkTypeBeans = loadAll();
		//get all localized names for all linkTypes:
		//key - linkTypeID (primaryKeyValue from TLocalizedResources table)
		//value - map: key - fieldName form TLocalizedResources table composed of LocalizationKeyPrefixes.LINKTYPE_SUPERSET + direction
		//				value - the localized text
		Map<Integer, Map<String, String>> localizedSupersetLabels =
			getLocalizedResourcesMap(linkTypeBeans, LocalizationKeyPrefixes.LINKTYPE_SUPERSET, locale);
		if (linkTypeBeans!=null) {
			Iterator<TLinkTypeBean> iterator = linkTypeBeans.iterator();
			while (iterator.hasNext()) {
				TLinkTypeBean linkTypeBean = iterator.next();
				if (selectableForQueryResultSuperset(linkTypeBean.getLinkTypePlugin())) {
					addFilterExpressionForLinkType(filterSupersetOptions, linkTypeBean, localizedSupersetLabels);
				}
			}
		}
		if (includeParentChildRelations) {
			filterSupersetOptions.add(0, new LabelValueBean(
					LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.queryFilter.opt.link.allAscendents", locale),
					MergeUtil.mergeKey(Integer.valueOf(ILinkType.PARENT_CHILD), PARENT_CHILD_EXPRESSION.ALL_PARENTS)));
			filterSupersetOptions.add(0, new LabelValueBean(
					LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.queryFilter.opt.link.allChildren", locale),
					MergeUtil.mergeKey(Integer.valueOf(ILinkType.PARENT_CHILD), PARENT_CHILD_EXPRESSION.ALL_CHILDREN)));
			filterSupersetOptions.add(0, new LabelValueBean(
					LocalizeUtil.getLocalizedTextFromApplicationResources("admin.customize.queryFilter.opt.link.notClosedChildren", locale),
					MergeUtil.mergeKey(Integer.valueOf(ILinkType.PARENT_CHILD), PARENT_CHILD_EXPRESSION.ALL_NOT_CLOSED_CHILDREN)));
		}
		filterSupersetOptions.add(0, new LabelValueBean("-", ""));

		return filterSupersetOptions;
	}

	/**
	 * Get the possible superset options
	 * @param filterSupersetOptions
	 * @param linkTypeBean
	 * @param localizedSupersetLabels
	 * @return
	 */
	private static void addFilterExpressionForLinkType(List<LabelValueBean> filterSupersetOptions,
			TLinkTypeBean linkTypeBean, Map<Integer, Map<String, String>> localizedSupersetLabels) {
		boolean hierarchical = isHierarchical(linkTypeBean.getLinkTypePlugin());
		Integer direction = linkTypeBean.getLinkDirection();
		if (linkTypeBean!=null) {
			Integer linkTypeID = linkTypeBean.getObjectID();
			switch (direction.intValue()) {
			case LINK_DIRECTION.LEFT_TO_RIGHT:
				if (hierarchical) {
					addLocalizedLinkTypeLabels(filterSupersetOptions, linkTypeID,
							linkTypeBean.getFilterLinkExpression(new Integer(FILTER_EXPRESSION_HIERARCHICAL.LEFT_TO_RIGHT_FIRST_LEVEL)),
							Integer.valueOf(FILTER_EXPRESSION_HIERARCHICAL.LEFT_TO_RIGHT_FIRST_LEVEL),
							Integer.valueOf(LINK_DIRECTION.LEFT_TO_RIGHT), false, localizedSupersetLabels);
					addLocalizedLinkTypeLabels(filterSupersetOptions, linkTypeID,
							linkTypeBean.getFilterLinkExpression(Integer.valueOf(FILTER_EXPRESSION_HIERARCHICAL.LEFT_TO_RIGHT_LEVEL_X)),
							Integer.valueOf(FILTER_EXPRESSION_HIERARCHICAL.LEFT_TO_RIGHT_LEVEL_X),
							Integer.valueOf(LINK_DIRECTION.LEFT_TO_RIGHT), false, localizedSupersetLabels);
					addLocalizedLinkTypeLabels(filterSupersetOptions, linkTypeID,
							linkTypeBean.getFilterLinkExpression(Integer.valueOf(FILTER_EXPRESSION_HIERARCHICAL.LEFT_TO_RIGHT_ALL)),
							Integer.valueOf(FILTER_EXPRESSION_HIERARCHICAL.LEFT_TO_RIGHT_ALL),
							Integer.valueOf(LINK_DIRECTION.LEFT_TO_RIGHT), false, localizedSupersetLabels);
				} else {
					addLocalizedLinkTypeLabels(filterSupersetOptions, linkTypeID,
							linkTypeBean.getFilterLinkExpression(Integer.valueOf(FILTER_EXPRESSION_PLAIN.LEFT_TO_RIGHT)),
							Integer.valueOf(FILTER_EXPRESSION_PLAIN.LEFT_TO_RIGHT),
							Integer.valueOf(LINK_DIRECTION.LEFT_TO_RIGHT), false, localizedSupersetLabels);
				}
				break;
			case LINK_DIRECTION.RIGHT_TO_LEFT:
				if (hierarchical) {
					addLocalizedLinkTypeLabels(filterSupersetOptions, linkTypeID,
							linkTypeBean.getFilterLinkExpression(Integer.valueOf(FILTER_EXPRESSION_HIERARCHICAL.RIGHT_TO_LEFT_FIRST_LEVEL)),
							Integer.valueOf(FILTER_EXPRESSION_HIERARCHICAL.LEFT_TO_RIGHT_FIRST_LEVEL),
							Integer.valueOf(FILTER_EXPRESSION_PLAIN.RIGHT_TO_LEFT), false, localizedSupersetLabels);
					addLocalizedLinkTypeLabels(filterSupersetOptions, linkTypeID,
							linkTypeBean.getFilterLinkExpression(Integer.valueOf(FILTER_EXPRESSION_HIERARCHICAL.RIGHT_TO_LEFT_LEVEL_X)),
							Integer.valueOf(FILTER_EXPRESSION_HIERARCHICAL.LEFT_TO_RIGHT_LEVEL_X),
							Integer.valueOf(FILTER_EXPRESSION_PLAIN.RIGHT_TO_LEFT), false, localizedSupersetLabels);
					addLocalizedLinkTypeLabels(filterSupersetOptions, linkTypeID,
							linkTypeBean.getFilterLinkExpression(Integer.valueOf(FILTER_EXPRESSION_HIERARCHICAL.LEFT_TO_RIGHT_ALL)),
							Integer.valueOf(FILTER_EXPRESSION_HIERARCHICAL.RIGHT_TO_LEFT_ALL),
							Integer.valueOf(FILTER_EXPRESSION_PLAIN.RIGHT_TO_LEFT), false, localizedSupersetLabels);
				} else {
					addLocalizedLinkTypeLabels(filterSupersetOptions, linkTypeID,
							linkTypeBean.getFilterLinkExpression(Integer.valueOf(FILTER_EXPRESSION_PLAIN.RIGHT_TO_LEFT)),
							Integer.valueOf(FILTER_EXPRESSION_PLAIN.LEFT_TO_RIGHT),
							Integer.valueOf(FILTER_EXPRESSION_PLAIN.RIGHT_TO_LEFT), false, localizedSupersetLabels);
				}
				break;
			default:
				if (hierarchical) {
					addLocalizedLinkTypeLabels(filterSupersetOptions, linkTypeID,
							linkTypeBean.getFilterLinkExpression(Integer.valueOf(FILTER_EXPRESSION_HIERARCHICAL.LEFT_TO_RIGHT_FIRST_LEVEL)),
							Integer.valueOf(FILTER_EXPRESSION_HIERARCHICAL.LEFT_TO_RIGHT_FIRST_LEVEL),
							Integer.valueOf(FILTER_EXPRESSION_PLAIN.LEFT_TO_RIGHT), false, localizedSupersetLabels);
					addLocalizedLinkTypeLabels(filterSupersetOptions, linkTypeID,
							linkTypeBean.getFilterLinkExpression(new Integer(FILTER_EXPRESSION_HIERARCHICAL.LEFT_TO_RIGHT_LEVEL_X)),
							Integer.valueOf(FILTER_EXPRESSION_HIERARCHICAL.LEFT_TO_RIGHT_LEVEL_X),
							Integer.valueOf(FILTER_EXPRESSION_PLAIN.LEFT_TO_RIGHT), false, localizedSupersetLabels);
					addLocalizedLinkTypeLabels(filterSupersetOptions, linkTypeID,
							linkTypeBean.getFilterLinkExpression(Integer.valueOf(FILTER_EXPRESSION_HIERARCHICAL.LEFT_TO_RIGHT_ALL)),
							Integer.valueOf(FILTER_EXPRESSION_HIERARCHICAL.LEFT_TO_RIGHT_ALL),
							Integer.valueOf(FILTER_EXPRESSION_PLAIN.LEFT_TO_RIGHT), false, localizedSupersetLabels);
					addLocalizedLinkTypeLabels(filterSupersetOptions, linkTypeID,
							linkTypeBean.getFilterLinkExpression(Integer.valueOf(FILTER_EXPRESSION_HIERARCHICAL.RIGHT_TO_LEFT_FIRST_LEVEL)),
							new Integer(FILTER_EXPRESSION_HIERARCHICAL.RIGHT_TO_LEFT_FIRST_LEVEL),
							Integer.valueOf(FILTER_EXPRESSION_PLAIN.RIGHT_TO_LEFT), false, localizedSupersetLabels);
					addLocalizedLinkTypeLabels(filterSupersetOptions, linkTypeID,
							linkTypeBean.getFilterLinkExpression(new Integer(FILTER_EXPRESSION_HIERARCHICAL.RIGHT_TO_LEFT_LEVEL_X)),
							new Integer(FILTER_EXPRESSION_HIERARCHICAL.RIGHT_TO_LEFT_LEVEL_X),
							Integer.valueOf(FILTER_EXPRESSION_PLAIN.RIGHT_TO_LEFT), false, localizedSupersetLabels);
					addLocalizedLinkTypeLabels(filterSupersetOptions, linkTypeID,
							linkTypeBean.getFilterLinkExpression(Integer.valueOf(FILTER_EXPRESSION_HIERARCHICAL.RIGHT_TO_LEFT_ALL)),
							Integer.valueOf(FILTER_EXPRESSION_HIERARCHICAL.RIGHT_TO_LEFT_ALL),
							Integer.valueOf(FILTER_EXPRESSION_PLAIN.RIGHT_TO_LEFT), false, localizedSupersetLabels);
				} else {
					addLocalizedLinkTypeLabels(filterSupersetOptions, linkTypeID,
							linkTypeBean.getFilterLinkExpression(Integer.valueOf(FILTER_EXPRESSION_PLAIN.LEFT_TO_RIGHT)),
							Integer.valueOf(FILTER_EXPRESSION_PLAIN.LEFT_TO_RIGHT),
							Integer.valueOf(FILTER_EXPRESSION_PLAIN.LEFT_TO_RIGHT), false, localizedSupersetLabels);
					addLocalizedLinkTypeLabels(filterSupersetOptions, linkTypeID,
							linkTypeBean.getFilterLinkExpression(Integer.valueOf(FILTER_EXPRESSION_PLAIN.RIGHT_TO_LEFT)),
							Integer.valueOf(FILTER_EXPRESSION_PLAIN.RIGHT_TO_LEFT),
							Integer.valueOf(FILTER_EXPRESSION_PLAIN.RIGHT_TO_LEFT), false, localizedSupersetLabels);
				}
				break;
			}
		}
	}

	/**
	 * Add an option only it it has a name specified
	 * @param labelValueBeans
	 * @param linkTypeID
	 * @param notLocalizedLabel
	 * @param direction
	 * @param name
	 * @param localizedLabels
	 */
	private static void addLocalizedLinkTypeLabels(List<LabelValueBean> labelValueBeans, Integer linkTypeID,
			String notLocalizedLabel, Integer directionForName, Integer direction, boolean name, Map<Integer, Map<String, String>> localizedLabels) {
		String label;
		String localizedLabel=null;
		Map<String, String> localizedResourceMapForPrimaryKey = localizedLabels.get(linkTypeID);
		if (localizedResourceMapForPrimaryKey!=null) {
			if (name) {
				localizedLabel = localizedResourceMapForPrimaryKey.get(LocalizationKeyPrefixes.LINKTYPE_NAME + directionForName  + ".");
			} else {
				localizedLabel = localizedResourceMapForPrimaryKey.get(LocalizationKeyPrefixes.LINKTYPE_SUPERSET + directionForName  + ".");
			}
		}
		if (localizedLabel!=null && !"".equals(localizedLabel)) {
			label = localizedLabel;
		} else {
			label = notLocalizedLabel;
		}
		if (label!=null && !"".equals(label)) {
			labelValueBeans.add(new LabelValueBean(label, MergeUtil.mergeKey(linkTypeID, direction)));
		}
	}

	public static String getLinkTypeName(TLinkTypeBean linkTypeBean, Integer direction, Locale locale) {
		String name = "";
		if (linkTypeBean==null || direction==null) {
			return name;
		}
		Integer linkTypeDirection = linkTypeBean.getLinkDirection();
		boolean reverseName = false;
		if (isReverse(linkTypeDirection, direction)) {
			reverseName = true;
		}
		int keyDirection = LINK_DIRECTION.LEFT_TO_RIGHT;
		if (reverseName) {
			keyDirection = LINK_DIRECTION.RIGHT_TO_LEFT;
		}
		String key = LocalizationKeyPrefixes.LINKTYPE_NAME +
				keyDirection + "." + linkTypeBean.getObjectID();
		String localizedName = LocalizeUtil.getLocalizedTextFromApplicationResources(key, locale);
		if (localizedName!=null && !"".equals(localizedName) && !key.equals(localizedName)) {
			return localizedName;
		}
		//TLinkTypeBean linkTypeBean = linkTypeDAO.loadByPrimaryKey(linkType);
		if (reverseName) {
			//reverse name is used only if the linkType is bidirectional and the workItemLink is right to left
			name = linkTypeBean.getReverseName();
		} else {
			name = linkTypeBean.getName();
		}
		return name;
	}

	/**
	 * Reverse only by right to left in case of bidirectional
	 * @param linkTypeDirection
	 * @param workItemLinkDirection
	 * @return
	 */
	public static boolean isReverse(Integer linkTypeDirection, Integer workItemLinkDirection) {
		if (linkTypeDirection==null || workItemLinkDirection==null) {
			return false;
		}
		if (linkTypeDirection.intValue()==LINK_DIRECTION.BIDIRECTIONAL &&
				workItemLinkDirection==LINK_DIRECTION.RIGHT_TO_LEFT) {
			return true;
		}
		return false;
	}

	/**
	 * Get those link names for all defined link types.
	 * If
	 * @return
	 */
	public static Map<String, String> getLinkTypeNamesMap(Locale locale) {
		Map<String, String> linkTypeNamesMap = new HashMap<String, String>();
		List<TLinkTypeBean> linkTypeBeans = loadAll();
		//get all localized names for all linkTypes:
		//key - linkTypeID (primaryKeyValue from TLocalizedResources table)
		//value - map: key - fieldName form TLocalizedResources table composed of LocalizationKeyPrefixes.LINKTYPE_NAME + direction
		//				value - the localized text
		Map<Integer, Map<String, String>> localizedNameLabels =
			getLocalizedResourcesMap(linkTypeBeans, LocalizationKeyPrefixes.LINKTYPE_NAME, locale);
		if (linkTypeBeans!=null) {
			for (TLinkTypeBean linkTypeBean : linkTypeBeans) {
					addNameForLinkTypeMap(linkTypeNamesMap, linkTypeBean, localizedNameLabels);
			}
		}
		return linkTypeNamesMap;
	}


	/**
	 * add a name for link type
	 * @param name
	 * @param linkTypeBean
	 * @param localizedSupersetLabels
	 */
	private static void addNameForLinkTypeMap(Map<String, String> name,
			TLinkTypeBean linkTypeBean, Map<Integer, Map<String, String>> localizedSupersetLabels) {
		Integer direction = linkTypeBean.getLinkDirection();
		if (linkTypeBean!=null) {
			Integer linkTypeID = linkTypeBean.getObjectID();
			switch (direction.intValue()) {
			case LINK_DIRECTION.LEFT_TO_RIGHT:
				addLocalizedLinkTypeLabels(name, linkTypeID,
					linkTypeBean.getLinkName(LINK_DIRECTION.LEFT_TO_RIGHT),
					Integer.valueOf(LINK_DIRECTION.LEFT_TO_RIGHT), localizedSupersetLabels);
				break;
			case LINK_DIRECTION.RIGHT_TO_LEFT:
				addLocalizedLinkTypeLabels(name, linkTypeID,
					//not an error: when it is unidirectional we take always the Name and not the reverse name (independently of the direction)
					linkTypeBean.getLinkName(LINK_DIRECTION.LEFT_TO_RIGHT),
					Integer.valueOf(LINK_DIRECTION.RIGHT_TO_LEFT), localizedSupersetLabels);
				break;
			default:
				addLocalizedLinkTypeLabels(name, linkTypeID,
					linkTypeBean.getLinkName(LINK_DIRECTION.LEFT_TO_RIGHT),
					Integer.valueOf(LINK_DIRECTION.LEFT_TO_RIGHT), localizedSupersetLabels);
				addLocalizedLinkTypeLabels(name, linkTypeID,
					linkTypeBean.getLinkName(LINK_DIRECTION.RIGHT_TO_LEFT),
					Integer.valueOf(LINK_DIRECTION.RIGHT_TO_LEFT), localizedSupersetLabels);
				break;
			}
		}
	}

	/**
	 * Add an option only it it has a name specified
	 * @param nameMap
	 * @param linkTypeID
	 * @param  notLocalizedLabel
	 * @param direction
	 * @param localizedLabels
	 */
	private static void addLocalizedLinkTypeLabels(Map<String, String> nameMap, Integer linkTypeID,
			String notLocalizedLabel, Integer direction, Map<Integer, Map<String, String>> localizedLabels) {
		String label;
		String localizedLabel=null;
		Map<String, String> localizedResourceMapForPrimaryKey = localizedLabels.get(linkTypeID);
		if (localizedResourceMapForPrimaryKey!=null) {
			localizedLabel = localizedResourceMapForPrimaryKey.get(LocalizationKeyPrefixes.LINKTYPE_NAME + direction + ".");
		}
		if (localizedLabel!=null && !"".equals(localizedLabel)) {
			label = localizedLabel;
		} else {
			label = notLocalizedLabel;
		}
		if (label!=null && !"".equals(label)) {
			nameMap.put(MergeUtil.mergeKey(linkTypeID, direction), label);
		}
	}

	/**
	 * whether for the newly selected link type the previously selected direction is valid
	 * @param directions
	 * @param direction
	 * @return
	 */
	public static boolean containsDirection(List<IntegerStringBean> directions, Integer direction) {
		if (directions==null || direction==null) {
			return false;
		}
		Iterator<IntegerStringBean> iterator = directions.iterator();
		while (iterator.hasNext()) {
			IntegerStringBean integerStringBean = iterator.next();
			if (direction.equals(integerStringBean.getValue())) {
				return true;
			}
		}
		return false;
	}


	public static boolean isEnableTab(TProjectBean projectBean) {
		return projectBean.isLinkingActive();
	}

	private static Map<Integer, Map<String, String>> getLocalizedResourcesMap(
			List<TLinkTypeBean> linkTypeBeans, String prefix, Locale locale) {
		Map<Integer, Map<String, String>> localizedResourcesMap = new HashMap<Integer, Map<String, String>>();
		int[] values = null;
		if (LocalizationKeyPrefixes.LINKTYPE_NAME.equals(prefix)) {
			values = new int[] {LINK_DIRECTION.LEFT_TO_RIGHT, LINK_DIRECTION.RIGHT_TO_LEFT};
		} else {
			if (LocalizationKeyPrefixes.LINKTYPE_SUPERSET.equals(prefix)) {
				values = new int[] { FILTER_EXPRESSION_HIERARCHICAL.LEFT_TO_RIGHT_FIRST_LEVEL,
						FILTER_EXPRESSION_HIERARCHICAL.RIGHT_TO_LEFT_FIRST_LEVEL,
						FILTER_EXPRESSION_HIERARCHICAL.LEFT_TO_RIGHT_LEVEL_X,
						FILTER_EXPRESSION_HIERARCHICAL.RIGHT_TO_LEFT_LEVEL_X,
						FILTER_EXPRESSION_HIERARCHICAL.LEFT_TO_RIGHT_ALL,
						FILTER_EXPRESSION_HIERARCHICAL.RIGHT_TO_LEFT_ALL};
			}
		}
		if (values!=null && linkTypeBeans!=null) {
			for (TLinkTypeBean linkTypeBean : linkTypeBeans) {
				Integer primaryKey = linkTypeBean.getObjectID();
				Map<String, String> localizedResourceMapForPrimaryKey = new HashMap<String,String>();
				localizedResourcesMap.put(primaryKey, localizedResourceMapForPrimaryKey);
				for (int i = 0; i < values.length; i++) {
					int value = values[i];
					String fieldName = prefix + value + ".";
					String localizedLabel = LocalizeUtil.getLocalizedEntity(fieldName, primaryKey, locale);
					if (localizedLabel!=null && !"".equals(localizedLabel)) {
						localizedResourceMapForPrimaryKey.put(fieldName, localizedLabel);
					}
				}
			}
		}
		return localizedResourcesMap;
	}

	/**
	 * Get the label for a linkID and direction
	 * @param linkID
	 * @param direction
	 * @param filterSuperset
	 * @param locale
	 * @return
	 */
	public static String getLocalizedLinkExpressionLabel(Integer linkID, Integer direction, boolean filterSuperset, Locale locale) {
		String prefix = null;
		if (filterSuperset) {
				if (linkID.intValue()==0) {
					switch (direction) {
					case PARENT_CHILD_EXPRESSION.ALL_PARENTS:
						return LocalizeUtil.getLocalizedTextFromApplicationResources(
								"admin.customize.queryFilter.opt.link.allAscendents", locale);
					case PARENT_CHILD_EXPRESSION.ALL_CHILDREN:
						return LocalizeUtil.getLocalizedTextFromApplicationResources(
								"admin.customize.queryFilter.opt.link.allChildren", locale);
					case PARENT_CHILD_EXPRESSION.ALL_NOT_CLOSED_CHILDREN:
						return LocalizeUtil.getLocalizedTextFromApplicationResources(
								"admin.customize.queryFilter.opt.link.notClosedChildren", locale);
					}
				} else {
					prefix = LocalizationKeyPrefixes.LINKTYPE_SUPERSET;
				}
		} else {
			prefix = LocalizationKeyPrefixes.LINKTYPE_NAME;
		}
		ILinkType linkType = LinkTypeBL.getLinkTypePluginInstanceByLinkTypeKey(linkID);
		if (linkType.getPossibleDirection()!=LINK_DIRECTION.BIDIRECTIONAL) {
			//if unidirectional then the name is stored under LEFT_TO_RIGHT even for RIGHT_TO_LEFT
			direction = LINK_DIRECTION.LEFT_TO_RIGHT;
		}
		String fieldName = prefix + direction + ".";
		return LocalizeUtil.getLocalizedEntity(fieldName, linkID, locale);
	}
}
