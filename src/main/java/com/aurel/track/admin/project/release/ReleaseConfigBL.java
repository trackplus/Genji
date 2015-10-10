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

package com.aurel.track.admin.project.release;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.accessControl.AccessBeans;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.FilterUpperConfigUtil;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.LoadTreeFilterItems;
import com.aurel.track.admin.customize.category.filter.execute.loadItems.TooManyItemsToLoadException;
import com.aurel.track.admin.customize.category.filter.tree.design.FilterUpperTO;
import com.aurel.track.admin.customize.objectStatus.SystemStatusBL;
import com.aurel.track.admin.customize.projectType.ProjectTypesBL;
import com.aurel.track.admin.customize.projectType.ProjectTypesBL.PROJECT_TYPE_FLAG;
import com.aurel.track.admin.project.ProjectBL;
import com.aurel.track.beans.ILabelBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TProjectTypeBean;
import com.aurel.track.beans.TReleaseBean;
import com.aurel.track.beans.TSystemStateBean;
import com.aurel.track.beans.TWorkItemBean;
import com.aurel.track.cluster.ClusterMarkChangesBL;
import com.aurel.track.dao.DAOFactory;
import com.aurel.track.dao.ReleaseDAO;
import com.aurel.track.dao.WorkItemDAO;
import com.aurel.track.errors.ErrorData;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.base.FieldsManagerRT;
import com.aurel.track.fieldType.runtime.base.LookupContainer;
import com.aurel.track.fieldType.runtime.bl.FieldRuntimeBL;
import com.aurel.track.json.JSONUtility;
import com.aurel.track.lucene.LuceneUtil;
import com.aurel.track.lucene.index.LuceneIndexer;
import com.aurel.track.lucene.index.listFields.NotLocalizedListIndexer;
import com.aurel.track.resources.LocalizeUtil;
import com.aurel.track.util.EqualUtils;
import com.aurel.track.util.GeneralUtils;
import com.aurel.track.util.PropertiesHelper;
import com.aurel.track.util.SortOrderUtil;

public class ReleaseConfigBL {

	private static final Logger LOGGER = LogManager.getLogger(ReleaseConfigBL.class);
	
	private static String LINK_CHAR = "_";
	static ReleaseDAO releaseDAO = DAOFactory.getFactory().getReleaseDAO();
	private static WorkItemDAO workItemDAO = DAOFactory.getFactory().getWorkItemDAO();

	public static String RELEAESE_KEY = "admin.project.lbl.release";
	public static String RELEAESE_KEY_IN_OP = "admin.project.release.lbl.main";
	
	/**
	 * Gets the root node for the releases for a project
	 * @param projectID
	 * @return
	 */
	public static String getProjectBranchNodeID(Integer projectID) {
		return encodeNode(new ProjectReleaseTokens(projectID));
	}
	
	static String getLocalizedLabels(String node, Locale locale) {
		String localizedMain = LocalizeUtil.getLocalizedTextFromApplicationResources(RELEAESE_KEY_IN_OP, locale);
		String localizedChild = null;
		ProjectReleaseTokens projectReleaseTokens = decodeNode(node);
		Integer projectID = projectReleaseTokens.getProjectID();
		boolean showClosedReleases = true;
		if (projectID!=null) {
			TProjectBean projectBean = ProjectBL.loadByPrimaryKey(projectID);
			if (projectBean!=null) {
				showClosedReleases = PropertiesHelper.getBooleanProperty(projectBean.getMoreProps(), TProjectBean.MOREPPROPS.SHOW_ARCHIVED_RELEASE);
				Integer projectTypeID = projectBean.getProjectType();
				if (projectTypeID!=null) {
					TProjectTypeBean projectTypeBean = ProjectTypesBL.loadByPrimaryKey(projectTypeID);
					if (projectTypeBean!=null) {
						Integer projectTypeFlag = projectTypeBean.getProjectTypeFlag();
						localizedChild = getLocalizedChildLabel(projectTypeFlag, locale);
					}
				}
			}
		}
		if (localizedChild==null) {
			localizedChild = LocalizeUtil.getLocalizedTextFromApplicationResources("admin.project.release.childPhase.general", locale);
		}
		//TODO: if only one of releaseNoticed and releaseseScheduled is used take the localizedMain from Field config: uncomment this
		/*TFieldConfigBean fieldConfigBean = LocalizeUtil.localizeFieldConfig(
				FieldRuntimeBL.getValidConfig(SystemFields.INTEGER_RELEASESCHEDULED, null, projectID), locale);
		if (fieldConfigBean!=null) {
			localizedMain = fieldConfigBean.getLabel();
		}*/
		
		/*if (localizedMain==null) {
			localizedMain = LocalizeUtil.localizeRelease(locale);
		}*/
		//String localizedDateFormat = DateTimeUtils.getInstance().getExtJSDateFormat(locale);
		return ReleaseJSON.createLocalizedLabelsJSON(localizedMain, localizedChild, showClosedReleases);
	}
	
	private static String getLocalizedChildLabel(Integer projectTypeFlag, Locale locale) {
		String localizedChild = null;
		if (projectTypeFlag!=null) {
			switch (projectTypeFlag.intValue()) {
			case PROJECT_TYPE_FLAG.HELPDESK:
				localizedChild = LocalizeUtil.getLocalizedTextFromApplicationResources(
						"admin.project.release.childPhase.helpdesk", locale);
				break;
			case PROJECT_TYPE_FLAG.SCRUM:
				localizedChild = LocalizeUtil.getLocalizedTextFromApplicationResources(
						"admin.project.release.childPhase.scrum", locale);
				break;
			case PROJECT_TYPE_FLAG.KANBAN:
				localizedChild = LocalizeUtil.getLocalizedTextFromApplicationResources(
						"admin.project.release.childPhase.kanban", locale);
				break;
			default:
				localizedChild = LocalizeUtil.getLocalizedTextFromApplicationResources(
						"admin.project.release.childPhase.general", locale);
				break;
			}
		}
		return localizedChild;
	}
	
	private static String getDescendantsError(String errorReasonKey, String errorResolutionKey,
			boolean isChild, Integer projectTypeFlag, Locale locale) {
		String localizedClosedDescendantLabelKey = null;
		if (isChild) {
			if (projectTypeFlag!=null) {
				switch (projectTypeFlag.intValue()) {
				case PROJECT_TYPE_FLAG.HELPDESK:
					localizedClosedDescendantLabelKey = errorReasonKey + ".helpdesk";
					break;
				case PROJECT_TYPE_FLAG.SCRUM:
					localizedClosedDescendantLabelKey = errorReasonKey + ".scrum";
					break;
				case PROJECT_TYPE_FLAG.KANBAN:
					localizedClosedDescendantLabelKey = errorReasonKey + ".kanban";;
					break;
				default:
					localizedClosedDescendantLabelKey = errorReasonKey + ".general";
					break;
				}
			}
		} else {
			localizedClosedDescendantLabelKey = errorReasonKey + ".release";
		}
		String warningLabel = "";
		if (localizedClosedDescendantLabelKey!=null) {
			warningLabel = LocalizeUtil.getLocalizedTextFromApplicationResources(localizedClosedDescendantLabelKey, locale);
		}
		return warningLabel + " " + LocalizeUtil.getLocalizedTextFromApplicationResources(errorResolutionKey, locale);
	}
	
	/*private static String getLocalizedOpenedAncestorsLabel(Integer projectTypeFlag, Locale locale) {
		String localizedOpenedAncestorsLabelKey = null;
		if (projectTypeFlag!=null) {
			switch (projectTypeFlag.intValue()) {
			case PROJECT_TYPE_FLAG.HELPDESK:
				localizedOpenedAncestorsLabelKey = "admin.project.release.err.openedAncestors.helpdesk";
				break;
			case PROJECT_TYPE_FLAG.SCRUM:
				localizedOpenedAncestorsLabelKey = "admin.project.release.err.openedAncestors.scrum";
				break;
			case PROJECT_TYPE_FLAG.KANBAN:
				localizedOpenedAncestorsLabelKey = "admin.project.release.err.openedAncestors.kanban";;
				break;
			default:
				localizedOpenedAncestorsLabelKey = "admin.project.release.err.openedAncestors.general";
				break;
			}
		}
		String warningLabel = "";
		if (localizedOpenedAncestorsLabelKey!=null) {
			warningLabel = LocalizeUtil.getLocalizedTextFromApplicationResources(localizedOpenedAncestorsLabelKey, locale);
		}
		return warningLabel + LocalizeUtil.getLocalizedTextFromApplicationResources(
				"admin.project.release.err.openClosedAncestors", locale);
	}*/
	
	/**
	 * Encode a node
	 * @param projectReleaseTokens
	 * @return
	 */
	public static String encodeNode(ProjectReleaseTokens projectReleaseTokens){
		StringBuffer stringBuffer = new StringBuffer();
		Integer projectID = projectReleaseTokens.getProjectID();
		if (projectID!=null) {
			stringBuffer.append(projectID);
			Integer releaseID = projectReleaseTokens.getReleaseID();
			if (releaseID!=null) {
				stringBuffer.append(LINK_CHAR);
				stringBuffer.append(releaseID);
			}
		}
		return stringBuffer.toString();
	}
	
	/**
	 * Decode a node
	 * @param id
	 * @return
	 */
	public static ProjectReleaseTokens decodeNode(String id) {
		ProjectReleaseTokens projectReleaseTokens = new ProjectReleaseTokens();
		if (id!=null && !"".equals(id)) {
			String[] tokens = id.split(LINK_CHAR);
			if (tokens!=null && tokens.length>0) {
				String projectToken = tokens[0];
				if (projectToken!=null && !"".equals(projectToken)) {
					projectReleaseTokens.setProjectID(Integer.valueOf(projectToken));
					if (tokens.length>1) {
						String releaseToken = tokens[1];
						if (releaseToken!=null && !"".equals(releaseToken)) {
							projectReleaseTokens.setReleaseID(Integer.valueOf(releaseToken));
						}
					}
				}
			}
		}
		return projectReleaseTokens;
	}
	
	/**
	 * Set the showArchivedRelease flag for a project
	 * @param projectID
	 */
	private static boolean getShowClosedReleasesFlag(Integer projectID) {
		TProjectBean projectBean = ProjectBL.loadByPrimaryKey(projectID);
		if (projectBean!=null) {
			return PropertiesHelper.getBooleanProperty(projectBean.getMoreProps(), TProjectBean.MOREPPROPS.SHOW_ARCHIVED_RELEASE);
		}
		return true;
	}
	
	/**
	 * Set the showArchivedRelease flag for a project
	 * @param projectID
	 * @param showArchived
	 */
	private static void setShowClosedReleasesFlag(Integer projectID, boolean showArchived) {
		TProjectBean projectBean = ProjectBL.loadByPrimaryKey(projectID);
		if (projectBean!=null) {
				projectBean.setMoreProps(PropertiesHelper.setBooleanProperty(projectBean.getMoreProps(),
					TProjectBean.MOREPPROPS.SHOW_ARCHIVED_RELEASE, showArchived));
				ProjectBL.saveSimple(projectBean);
		}
	}
	
	/**
	 * Gets the releases for a project or children for a release
	 * @param node
	 * @return
	 */
	static List<ReleaseTreeNodeTO> getReleaseNodes(String node) {
		ProjectReleaseTokens projectReleaseTokens = decodeNode(node);
		List<ReleaseTreeNodeTO> releaseNodes = new LinkedList<ReleaseTreeNodeTO>();
		Integer projectID = projectReleaseTokens.getProjectID();
		Integer releaseID = projectReleaseTokens.getReleaseID();
		if (projectID!=null) {
			boolean showClosedReleases = getShowClosedReleasesFlag(projectID);
			Map<Integer, TSystemStateBean> releaseStatusMap = GeneralUtils.createMapFromList(
					SystemStatusBL.getSystemStatesByByEntityAndStateFlags(TSystemStateBean.ENTITYFLAGS.RELEASESTATE,
							ReleaseBL.getStateFlags(showClosedReleases, true, true, true)));
			List<TReleaseBean> releaseBeanList = ReleaseBL.getReleases(projectID, releaseID, showClosedReleases);
			for (TReleaseBean releaseBean : releaseBeanList) {
				ProjectReleaseTokens releaseTokens = new ProjectReleaseTokens(projectID, releaseBean.getObjectID());
				ReleaseTreeNodeTO releaseTreeNodeTO = new ReleaseTreeNodeTO(encodeNode(releaseTokens),
						releaseBean.getLabel(), ReleaseBL.getReleaseIcon(ReleaseBL.getReleaseStateFlag(releaseStatusMap, releaseBean.getStatus())), releaseID!=null);
				//false because it should be collapsable (even if it is leaf, not known previously because of lazy load)
				releaseTreeNodeTO.setLeaf(false);
				//releaseTreeNodeTO.setIconCls(RELEASE_ICON_CLASS);
				releaseNodes.add(releaseTreeNodeTO);
			}
		}
		return releaseNodes;
	}
	
	/**
	 * Gets the releases for a project or children for a release
	 * @param node
	 * @param showClosedReleases
     * @param locale
	 * @return
	 */
	static List<ReleaseRowTO> getReleaseRows(String node, Boolean showClosedReleases, Locale locale) {
		ProjectReleaseTokens projectReleaseTokens = decodeNode(node);
		List<ReleaseRowTO> releaseNodes = new LinkedList<ReleaseRowTO>();
		Integer projectID = projectReleaseTokens.getProjectID();
		//Integer projectConfigType = projectReleaseTokens.getProjectConfigType();
		Integer releaseID = projectReleaseTokens.getReleaseID();
		if (showClosedReleases==null) {
			//not sent a request parameter
			showClosedReleases = getShowClosedReleasesFlag(projectID);
		} else {
			setShowClosedReleasesFlag(projectID, showClosedReleases.booleanValue());
		}
		List<TReleaseBean> releaseBeanList = ReleaseBL.getReleases(projectID, releaseID, showClosedReleases);
		List<TSystemStateBean> releaseStatusList = SystemStatusBL.getStatusOptions(TSystemStateBean.ENTITYFLAGS.RELEASESTATE, locale);
		Map<Integer, TSystemStateBean> releaseStatusMap = GeneralUtils.createMapFromList(releaseStatusList);
		for (TReleaseBean releaseBean : releaseBeanList) {
			ReleaseRowTO releaseRowTO = new ReleaseRowTO();
			String moreProps = releaseBean.getMoreProps();
			if (releaseID!=null) {
				//some extra fields only for releases
				releaseRowTO.setChild(true);
			}
			ProjectReleaseTokens releaseTokens = new ProjectReleaseTokens(projectID, /*projectConfigType,*/ releaseBean.getObjectID());
			releaseRowTO.setNode(encodeNode(releaseTokens));
			releaseRowTO.setLabel(releaseBean.getLabel());
			Integer status = releaseBean.getStatus();
			if (status!=null) {
				TSystemStateBean systemStateBean = releaseStatusMap.get(status);
				if (systemStateBean!=null) {
					releaseRowTO.setReleaseStatusLabel(systemStateBean.getLabel());
					releaseRowTO.setStatusFlag(systemStateBean.getStateflag());
				}
			}
			releaseRowTO.setDueDate(releaseBean.getDueDate());
			boolean defaultReleaseNoticed = PropertiesHelper.getBooleanProperty(moreProps, TReleaseBean.MOREPPROPS.DEFAULT_RELEASENOTICED);
			releaseRowTO.setDefaultNoticed(defaultReleaseNoticed);
			boolean defaultReleaseScheduled = PropertiesHelper.getBooleanProperty(moreProps, TReleaseBean.MOREPPROPS.DEFAULT_RELEASESCHEDULED);			
			releaseRowTO.setDefaultScheduled(defaultReleaseScheduled);
			releaseNodes.add(releaseRowTO);
		}
		return releaseNodes;
	}
	
	/**
	 * Loads the details for a new/existing release 
	 * @param node
	 * @param add
     * @param addAsChild
	 * @param locale
	 * @return
	 */
	static String getReleaseDetail(String node, boolean add, boolean addAsChild, Locale locale) {
		ProjectReleaseTokens projectReleaseTokens = decodeNode(node);
		Integer releaseID = projectReleaseTokens.getReleaseID();
		ReleaseDetailTO releaseDetailTO = new ReleaseDetailTO();
		TReleaseBean releaseBean = null;
		if (add) {
			releaseBean = new TReleaseBean();
		} else {
			releaseBean = ReleaseBL.loadByPrimaryKey(releaseID);
		}
		List<TSystemStateBean> releaseStatusList = SystemStatusBL.getStatusOptions(TSystemStateBean.ENTITYFLAGS.RELEASESTATE, locale);
		releaseDetailTO.setReleaseStatusList(releaseStatusList);
		if (add) {
			if (releaseStatusList!=null && !releaseStatusList.isEmpty()) {
				releaseDetailTO.setReleaseStatusID(releaseStatusList.get(0).getObjectID());
			}
		} else {
			String moreProps = releaseBean.getMoreProps();
			releaseDetailTO.setLabel(releaseBean.getLabel());
			releaseDetailTO.setDescription(releaseBean.getDescription());
			releaseDetailTO.setDueDate(releaseBean.getDueDate());
			boolean defaultReleaseNoticed = PropertiesHelper.getBooleanProperty(
					moreProps, TReleaseBean.MOREPPROPS.DEFAULT_RELEASENOTICED);
			releaseDetailTO.setDefaultNoticed(defaultReleaseNoticed);
			boolean defaultReleaseScheduled = PropertiesHelper.getBooleanProperty(
					moreProps, TReleaseBean.MOREPPROPS.DEFAULT_RELEASESCHEDULED);
			releaseDetailTO.setDefaultScheduled(defaultReleaseScheduled);
			releaseDetailTO.setReleaseStatusID(releaseBean.getStatus());
		}
		return ReleaseJSON.loadReleaseDetailJSON(releaseDetailTO);
	}
	
	/**
	 * Saves a new/existing project
	 * @param node
	 * @param add
	 * @param addAsChild
	 * @param releaseDetailTO
	 * @param confirmSave
	 * @param locale
	 * @return
	 */
	static String saveReleaseDetail(String node, boolean add, boolean addAsChild,
			ReleaseDetailTO releaseDetailTO, boolean confirmSave, Integer replacementID, TPersonBean personBean, Locale locale) {
		ProjectReleaseTokens projectReleaseTokens = decodeNode(node);
		Integer projectID = projectReleaseTokens.getProjectID();
		Integer releaseID = projectReleaseTokens.getReleaseID();
		TReleaseBean releaseBean = null;
		//whether the root or a branch should be reloaded after project save 
		boolean reloadTree = false;
		//if null (and reloadTree is true) reload the root node
		String nodeIDToReload = null;
		//which project to select after save: set only if reloadTree==true
		//if this is not null a reload is made in the client 
		//String nodeToSelect = null;		
		boolean isChild = false;
		if (releaseDetailTO==null) {
			releaseDetailTO = new ReleaseDetailTO();
		}
		
		Integer releaseStatusNew = releaseDetailTO.getReleaseStatusID();
		Integer statusFlagNew = SystemStatusBL.getStatusFlagForStatusID(TSystemStateBean.ENTITYFLAGS.RELEASESTATE, releaseStatusNew);
		if (add) {
			reloadTree=true;
			releaseBean = new TReleaseBean();
			if (addAsChild) {
				isChild = true;
				if (releaseID!=null) {
					releaseBean.setParent(releaseID);
					//reload the parent release
					nodeIDToReload = encodeNode(new ProjectReleaseTokens(
							projectID, releaseID));
					if (statusFlagNew!=null) {
						TReleaseBean parentRelease = ReleaseBL.loadByPrimaryKey(releaseID);
						if (parentRelease!=null) {
							Integer parentReleaseStatus = parentRelease.getStatus();
							if (parentReleaseStatus!=null) {
								Integer parentStatusFlag = SystemStatusBL.getStatusFlagForStatusID(TSystemStateBean.ENTITYFLAGS.RELEASESTATE, parentReleaseStatus);
								if (parentStatusFlag!=null) {
									String errorKey = null;
									switch (parentStatusFlag.intValue()) {
									case TSystemStateBean.STATEFLAGS.NOT_PLANNED:
										if (statusFlagNew.intValue()!=TSystemStateBean.STATEFLAGS.NOT_PLANNED) {
											errorKey = "admin.project.release.err.add.scheduledChildToUnscheduledParent";
										}
										break;
									case TSystemStateBean.STATEFLAGS.INACTIVE:
										if (statusFlagNew.intValue()==TSystemStateBean.STATEFLAGS.ACTIVE ||
											statusFlagNew.intValue()==TSystemStateBean.STATEFLAGS.NOT_PLANNED) {
											errorKey = "admin.project.release.err.add.activeOrUnscheduledChildToInactiveParent";
										}
										break;
									case TSystemStateBean.STATEFLAGS.CLOSED:
										if (statusFlagNew.intValue()!=TSystemStateBean.STATEFLAGS.CLOSED) {
											errorKey = "admin.project.release.err.add.openChildToClosedParent";
										}
										break;
									
									}
									if (errorKey!=null) {
										return JSONUtility.encodeJsonError("releaseDetailTO.releaseStatusID", LocalizeUtil.getLocalizedTextFromApplicationResources(errorKey, locale));
									}
								}
							}
						}
					}
				}
			} else {
				//reload the main releases
				nodeIDToReload = encodeNode(new ProjectReleaseTokens(
						projectID));
			}
		} else {
			releaseBean = ReleaseBL.loadByPrimaryKey(releaseID);
			if (releaseBean==null) {
				//somebody else has deleted
				releaseBean = new TReleaseBean();
			}
			Integer releaseStatusOld = releaseBean.getStatus();
			isChild = releaseBean.getParent()!=null;
			if (EqualUtils.notEqual(releaseBean.getLabel(), releaseDetailTO.getLabel())) {
				reloadTree=true;
			}
			//test the closed statuses
			if (releaseStatusOld!=null && releaseStatusNew!=null && !releaseStatusOld.equals(releaseStatusNew)) {
				Integer statusFlagOld = SystemStatusBL.getStatusFlagForStatusID(TSystemStateBean.ENTITYFLAGS.RELEASESTATE, releaseStatusOld);
				if (statusFlagOld!=null && statusFlagNew!=null && EqualUtils.notEqual(statusFlagOld, statusFlagNew)) {
					boolean toUnscdeduled = statusFlagOld.intValue()!=TSystemStateBean.STATEFLAGS.NOT_PLANNED &&
							statusFlagNew.intValue()==TSystemStateBean.STATEFLAGS.NOT_PLANNED;
					boolean toActive = statusFlagOld.intValue()!=TSystemStateBean.STATEFLAGS.ACTIVE &&
							statusFlagNew.intValue()==TSystemStateBean.STATEFLAGS.ACTIVE;
					boolean toInactive = statusFlagOld.intValue()!=TSystemStateBean.STATEFLAGS.INACTIVE &&
							statusFlagNew.intValue()==TSystemStateBean.STATEFLAGS.INACTIVE;
					boolean toClosed = statusFlagOld.intValue()!=TSystemStateBean.STATEFLAGS.CLOSED &&
							statusFlagNew.intValue()==TSystemStateBean.STATEFLAGS.CLOSED;
					
					
					//1. verify descendants
					String titleKeyDescendants = null;
					String errorMessageDescendants = null;
					if (toClosed || toInactive || toUnscdeduled) {
						List<TReleaseBean> inconsistenSubreleases = null;
						if (releaseID!=null) {
							if (toClosed) {
								inconsistenSubreleases = ReleaseBL.loadChildrenNotClosed(releaseID);
							} else {
								if (toInactive) {
									inconsistenSubreleases = ReleaseBL.loadChildrenActiveNotPlanned(releaseID);
								} else {
									inconsistenSubreleases = ReleaseBL.loadChildrenScheduled(releaseID);
								}
							}
						}
						if (inconsistenSubreleases!=null && !inconsistenSubreleases.isEmpty()) {
							if (confirmSave) {
								for (TReleaseBean childReleaseBean : inconsistenSubreleases) {
									changeDescendantsStatus(childReleaseBean, releaseStatusNew, toClosed, toInactive, toUnscdeduled);
								}
							} else {
								Integer projectTypeFlag = null;
								if (isChild) {
									TProjectTypeBean projectTypeBean = ProjectTypesBL.getProjectTypeByProject(projectID);
									if (projectTypeBean!=null) {
										projectTypeFlag = projectTypeBean.getProjectTypeFlag();
									}
								}
								//String titleKey = null;
								String errorReasonKey = null;
								String errorResolutionKey = null;
								if (toClosed) {
									titleKeyDescendants = "admin.project.release.err.desc.close.notClosedChildrenTitle";
									errorReasonKey = "admin.project.release.err.desc.close.notClosedChildren";
									errorResolutionKey = "admin.project.release.err.desc.close.notClosedChildren";
								} else {
									if (toInactive) {
										titleKeyDescendants = "admin.project.release.err.desc.deactivate.activNotScheduledChildrenTitle";
										errorReasonKey = "admin.project.release.err.desc.deactivate.activNotScheduledChildren";
										errorResolutionKey = "admin.project.release.err.desc.deactivate.activNotScheduledChildren";
									} else {
										titleKeyDescendants = "admin.project.release.err.desc.unschedule.scheduledChildrenTitle";
										errorReasonKey = "admin.project.release.err.desc.unschedule.scheduledChildren";
										errorResolutionKey = "admin.project.release.err.desc.unschedule.scheduledChildren";
									}
								}
								errorMessageDescendants = getDescendantsError(errorReasonKey, errorResolutionKey, isChild, projectTypeFlag, locale);
							}
						}
					}
					//2. verify ascendents
					String titleKeyAscendents = null;
					String errorMessageAscendents = null;
					if (isChild && (toUnscdeduled || toActive || toInactive || toClosed)) {
						Integer parentReleaseID = releaseBean.getParent();
						TReleaseBean parentReleaseBean = ReleaseBL.loadByPrimaryKey(parentReleaseID);
						if (parentReleaseBean!=null) {
							Integer releaseStatusParent = parentReleaseBean.getStatus();
							if (releaseStatusParent!=null) {
								Integer parentStatusFlag = SystemStatusBL.getStatusFlagForStatusID(TSystemStateBean.ENTITYFLAGS.RELEASESTATE, releaseStatusParent);
								if (parentStatusFlag!=null) {
									boolean inconsistentParent = false;
									if (toUnscdeduled) {
										inconsistentParent =  parentStatusFlag.intValue()==TSystemStateBean.STATEFLAGS.CLOSED ||
												parentStatusFlag.intValue()==TSystemStateBean.STATEFLAGS.INACTIVE;
									} else {
										if (toActive) {
											inconsistentParent = parentStatusFlag.intValue()!=TSystemStateBean.STATEFLAGS.ACTIVE;
										} else {
											if (toInactive) {
												inconsistentParent =  parentStatusFlag.intValue()==TSystemStateBean.STATEFLAGS.CLOSED ||
														parentStatusFlag.intValue()==TSystemStateBean.STATEFLAGS.NOT_PLANNED;
											} else {
												if (toClosed) {
													inconsistentParent = parentStatusFlag.intValue()==TSystemStateBean.STATEFLAGS.NOT_PLANNED;
												}
											}
										}
									}
									if (inconsistentParent) {
										if (confirmSave) {
											Integer parentStatusID = null;
											if (statusFlagNew.intValue()!=TSystemStateBean.STATEFLAGS.ACTIVE) {
												List<TSystemStateBean> systemStateBeanList = SystemStatusBL.getSystemStatesByByEntityAndStateFlags(
														TSystemStateBean.ENTITYFLAGS.RELEASESTATE, new int[]{TSystemStateBean.STATEFLAGS.ACTIVE});
												if (systemStateBeanList!=null && !systemStateBeanList.isEmpty()) {
													parentStatusID = systemStateBeanList.get(0).getObjectID();
												}
											} else {
												parentStatusID = releaseStatusNew;
											}
											if (parentStatusID!=null) {
												changeAscendentsStatus(parentReleaseBean, parentStatusID);
											}
										} else {
											titleKeyAscendents = "admin.project.release.err.anc.activateAncestorsTitle";
											String errorReasonKey = "admin.project.release.err.anc.activateAncestors";
											String errorResolutionKey = "admin.project.release.err.anc.activateAncestors";
											TProjectTypeBean projectTypeBean = ProjectTypesBL.getProjectTypeByProject(projectID);
											Integer projectTypeFlag =  null;
											if (projectTypeBean!=null) {
												projectTypeFlag = projectTypeBean.getProjectTypeFlag();
											}
											errorMessageAscendents = getDescendantsError(errorReasonKey, errorResolutionKey, true, projectTypeFlag, locale);
										}
									}
								}
							}
						}
					}
					if (errorMessageDescendants!=null || errorMessageAscendents!=null) {
						StringBuilder sbTitle = new StringBuilder();
						StringBuilder sbErrorMessage = new StringBuilder();
						if (errorMessageDescendants!=null) {
							sbTitle.append(LocalizeUtil.getLocalizedTextFromApplicationResources(titleKeyDescendants, locale));
							sbErrorMessage.append(errorMessageDescendants);
						} 
						if (errorMessageAscendents!=null) {
							if (sbTitle.length()>0) {
								sbTitle.append(" ");
							}
							sbTitle.append(LocalizeUtil.getLocalizedTextFromApplicationResources(titleKeyAscendents, locale));
							if (sbErrorMessage.length()>0) {
								sbErrorMessage.append(" ");
							}
							sbErrorMessage.append(errorMessageAscendents);
						}
						return ReleaseJSON.saveFailureJSON(JSONUtility.EDIT_ERROR_CODES.NEED_CONFIRMATION,
								sbTitle.toString(), sbErrorMessage.toString());
					}
					//3. verify opened items
					if (toClosed /*|| toInactive*/) {
						FilterUpperTO filterUpperTO = FilterUpperConfigUtil.getByProjectReleaseID(false, releaseID, true, true, false);
						List<TWorkItemBean> releaseOpenItems = null;
						try {
							releaseOpenItems = LoadTreeFilterItems.getTreeFilterWorkItemBeans(filterUpperTO, personBean, locale, false);
						} catch (TooManyItemsToLoadException e) {
							LOGGER.info("Number of items to load " + e.getItemCount());
						}
						if (releaseOpenItems!=null && !releaseOpenItems.isEmpty()) {
							if (replacementID==null) {
								List<Integer> projectIDList = new LinkedList<Integer>();
						        projectIDList.add(projectID);
						        String releaseTree =  ReleasePickerBL.getTreeJSON(
						                projectIDList, null, false, false,
						                false, false, true, true,
						                false, releaseID, personBean, locale);
						        String waringMessage = LocalizeUtil.getParametrizedString("admin.project.release.moveReleaseItems.warn",
						        		new Object[] {releaseDetailTO.getLabel(), FieldRuntimeBL.getLocalizedDefaultFieldLabel(SystemFields.INTEGER_RELEASESCHEDULED, locale)}, locale);
								return ReleaseJSON.saveReplacemenetJSON(JSONUtility.EDIT_ERROR_CODES.RECOMMENDED_REPLACE, waringMessage, releaseTree);
							} else {
								moveOpenItems(replacementID, releaseOpenItems, personBean.getObjectID(), locale);
							}
						}
					}
				}
			}
		}
		releaseBean.setProjectID(projectID);
		releaseBean.setLabel(releaseDetailTO.getLabel());
		releaseBean.setDescription(releaseDetailTO.getDescription());
		releaseBean.setStatus(releaseStatusNew);
		releaseBean.setDueDate(releaseDetailTO.getDueDate());
		boolean isDefaultNoticed = releaseDetailTO.isDefaultNoticed();
		boolean isDefaultScheduled = releaseDetailTO.isDefaultScheduled();
		if (isDefaultNoticed || isDefaultScheduled) {
			//remove the previous defaults at any level
			List<TReleaseBean> projectReleases = releaseDAO.loadAllByProject(projectID);
			for (TReleaseBean existingReleaseBean : projectReleases) {
				boolean wasDefault = false;
				boolean existingNoticedDefault = PropertiesHelper.getBooleanProperty(existingReleaseBean.getMoreProps(), 
						TReleaseBean.MOREPPROPS.DEFAULT_RELEASENOTICED);
				if (existingNoticedDefault && isDefaultNoticed) {
					existingReleaseBean.setMoreProps(PropertiesHelper.setBooleanProperty(existingReleaseBean.getMoreProps(), 
						TReleaseBean.MOREPPROPS.DEFAULT_RELEASENOTICED, false));
					wasDefault = true;
				}
				boolean existingScheduledDefault = PropertiesHelper.getBooleanProperty(existingReleaseBean.getMoreProps(), 
						TReleaseBean.MOREPPROPS.DEFAULT_RELEASESCHEDULED);
				if (existingScheduledDefault && isDefaultScheduled) {
					existingReleaseBean.setMoreProps(PropertiesHelper.setBooleanProperty(existingReleaseBean.getMoreProps(), 
						TReleaseBean.MOREPPROPS.DEFAULT_RELEASESCHEDULED, false));
					wasDefault = true;
				}
				if (wasDefault) {
					ReleaseBL.saveSimple(existingReleaseBean);
				}
			}
		}
		releaseBean.setMoreProps(PropertiesHelper.setBooleanProperty(releaseBean.getMoreProps(),
				TReleaseBean.MOREPPROPS.DEFAULT_RELEASENOTICED, isDefaultNoticed));
		releaseBean.setMoreProps(PropertiesHelper.setBooleanProperty(releaseBean.getMoreProps(),
				TReleaseBean.MOREPPROPS.DEFAULT_RELEASESCHEDULED, isDefaultScheduled));
		releaseID = ReleaseBL.save(releaseBean);
		if (reloadTree) {
			//force reloading of all releases after add or name change
			LookupContainer.resetReleaseMap();
		}
		return ReleaseJSON.saveReleaseDetailJSON(nodeIDToReload, encodeNode(new ProjectReleaseTokens(
				projectID, releaseID)), reloadTree);
	}
	
	private static void moveOpenItems(Integer newReleaseID, List<TWorkItemBean> releaseOpenItems, Integer personID, Locale locale) {
		for (TWorkItemBean workItemBean : releaseOpenItems) {
			//is the workItem selected for change
			List<ErrorData> errorList = new LinkedList<ErrorData>();
			FieldsManagerRT.saveOneField(personID, workItemBean.getObjectID(), locale, false, SystemFields.INTEGER_RELEASESCHEDULED, newReleaseID, true, null, errorList);
			if (!errorList.isEmpty()) {
				//log
			}
		}
	}
	
	/**
	 * Copies a filter category with the entire subtree
	 * @param nodeFrom
	 * @param nodeTo
	 * @return
	 * 
	 */
	static String copy(String nodeFrom, String nodeTo){
		ProjectReleaseTokens departmentFromToken = decodeNode(nodeFrom);
		ProjectReleaseTokens departmentToToken = decodeNode(nodeTo);
		Integer releaseFrom = departmentFromToken.getReleaseID();
		Integer releaseTo = departmentToToken.getReleaseID();
		if (EqualUtils.equal(releaseFrom, releaseTo)) {
			return JSONUtility.encodeJSONSuccessAndNode(nodeFrom);
		}
		if (releaseFrom!=null && releaseTo!=null) {
			TReleaseBean releaseBean = ReleaseBL.loadByPrimaryKey(releaseFrom);
			if (releaseBean!=null) {
				releaseBean.setParent(releaseTo);
				ReleaseBL.saveSimple(releaseBean);
			}
		}
		return JSONUtility.encodeJSONSuccessAndNode(nodeFrom);
	}
	
	/**
	 * Clears the department's parent
	 * @param node
	 * @return
	 */
	static String clearParent(String node) {
		ProjectReleaseTokens projectReleaseTokens = decodeNode(node);
		Integer releaseID = projectReleaseTokens.getReleaseID();
		if (releaseID!=null) {
			TReleaseBean releaseBean = ReleaseBL.loadByPrimaryKey(releaseID);
			Integer parentRelease = releaseBean.getParent();
			if (parentRelease!=null) {
				releaseBean.setParent(null);
				ReleaseBL.saveSimple(releaseBean);
			}
		}
		return JSONUtility.encodeJSONSuccess();
	}
	
	/**
	 * Change the descendent's status
	 * @param releaseBean
	 * @param newStatusID
	 * @param toClosed
	 * @param toInactive
	 * @param toUnscheduled
	 */
	private static void changeDescendantsStatus(TReleaseBean releaseBean, Integer newStatusID,
			boolean toClosed, boolean toInactive, boolean toUnscheduled) {
		List<TReleaseBean> childReleases = null;
		if (toClosed) {
			childReleases = ReleaseBL.loadChildrenNotClosed(releaseBean.getObjectID());
		} else {
			if (toInactive) {
				childReleases = ReleaseBL.loadChildrenActiveNotPlanned(releaseBean.getObjectID());
			} else {
				childReleases = ReleaseBL.loadChildrenScheduled(releaseBean.getObjectID());
			}
		}
		if (childReleases!=null) {
			for (TReleaseBean childReleaseBean : childReleases) {
				changeDescendantsStatus(childReleaseBean, newStatusID, toClosed, toInactive, toUnscheduled);
			}
		}
		releaseBean.setStatus(newStatusID);
		ReleaseBL.saveSimple(releaseBean);
	}
	
	/**
	 * Change the ascendent's status up to the first up to the first compatible ascendent
	 * @param releaseBean
	 * @param newStatusID
	 * @param closedToNotClosed
	 * @param inactiveToActive
	 * @param unsheduledToScheduled
	 */
	private static void changeAscendentsStatus(TReleaseBean releaseBean, Integer newStatusID/*,
			boolean closedToNotClosed, boolean inactiveToActive, boolean unsheduledToScheduled*/) {
		if (releaseBean!=null) {
			Integer releaseStatus = releaseBean.getStatus();
			Integer statusFlag= SystemStatusBL.getStatusFlagForStatusID(TSystemStateBean.ENTITYFLAGS.RELEASESTATE, releaseStatus);
			if (statusFlag!=null) {
				boolean changeAscendent = statusFlag.intValue()!=TSystemStateBean.STATEFLAGS.ACTIVE;
				/*if (closedToNotClosed) {
					changeAscendent = statusFlag.intValue()==TSystemStateBean.STATEFLAGS.CLOSED;
				} else {
					if (inactiveToActive) {
						changeAscendent = statusFlag.intValue()==TSystemStateBean.STATEFLAGS.INACTIVE || statusFlag.intValue()==TSystemStateBean.STATEFLAGS.NOT_PLANNED;
					} else {
						changeAscendent = statusFlag.intValue()==TSystemStateBean.STATEFLAGS.NOT_PLANNED;
					}
				}*/
				if (changeAscendent) {
					releaseBean.setStatus(newStatusID);
					ReleaseBL.saveSimple(releaseBean);
					Integer parentID = releaseBean.getParent();
					if (parentID!=null) {
						TReleaseBean parentReleaseBean = ReleaseBL.loadByPrimaryKey(parentID);
						changeAscendentsStatus(parentReleaseBean, newStatusID/*, closedToNotClosed, inactiveToActive, unsheduledToScheduled*/);
					}
				}
			}
		}
	}
	
	static String delete(String node,  TPersonBean personBean, Locale locale) {
		String errorMessage;
		ProjectReleaseTokens projectReleaseTokens = decodeNode(node);
		Integer projectID = projectReleaseTokens.getProjectID();
		Integer releaseID = projectReleaseTokens.getReleaseID();
		if (!personBean.isSys()) {
			//false to do not allow to delete a release as substitute
			boolean isProjectAdmin = AccessBeans.isPersonProjectAdminForProject(
					personBean.getObjectID(), projectID, false);
			if (!isProjectAdmin) {
				errorMessage = LocalizeUtil.getParametrizedString("common.lbl.noDeleteRight",
						new Object[] {LocalizeUtil.getLocalizedTextFromApplicationResources(RELEAESE_KEY_IN_OP, locale)}, locale);
				return JSONUtility.encodeJSONFailure(
						errorMessage, JSONUtility.DELETE_ERROR_CODES.NO_RIGHT_TO_DELETE);
			}
		}
		boolean hasDependentIssues = releaseDAO.hasDependentIssues(releaseID);
		List<TReleaseBean> childrenReleases = releaseDAO.loadChildren(releaseID);
		boolean hasChildren = childrenReleases!=null && !childrenReleases.isEmpty();
		if (hasDependentIssues || hasChildren) {
			return JSONUtility.encodeJSONFailure(JSONUtility.DELETE_ERROR_CODES.NEED_REPLACE);
		}
		delete(releaseID);
		return JSONUtility.encodeJSONSuccess();
	}
	
	/**
	 * Deletes the status
	 * @param objectID
	 */
	private static void delete(Integer objectID) {
		if (objectID!=null) {
			releaseDAO.delete(objectID);
			LookupContainer.resetReleaseMap();
			//delete the release from lucene index
			NotLocalizedListIndexer.getInstance().deleteByKeyAndType(objectID, 
					LuceneUtil.LOOKUPENTITYTYPES.RELEASE);
			//cache and possible lucene update in other nodes
			ClusterMarkChangesBL.markDirtySystemListEntryInCluster(SystemFields.INTEGER_RELEASE, objectID, ClusterMarkChangesBL.getChangeTypeByDelete());
		}
	}
	
	/**
	 * Render the replacement option 
	 * @param node
	 * @param errorMessage
	 * @param locale
	 * @return
	 */
	static String renderReplace(String node, String errorMessage, TPersonBean personBean, Locale locale) {
		ProjectReleaseTokens projectReleaseTokens = decodeNode(node);
		Integer projectID = projectReleaseTokens.getProjectID();
		Integer releaseID = projectReleaseTokens.getReleaseID();
		TReleaseBean releaseBean = ReleaseBL.loadByPrimaryKey(releaseID);
		String label = null;
		if (releaseBean!=null) {
			label = releaseBean.getLabel();
		}
        List<Integer> projectIDList = new LinkedList<Integer>();
        projectIDList.add(projectID);
        String releaseTree =  ReleasePickerBL.getTreeJSON(
                projectIDList, null, false, false,
                false, true, true, true,
                false, releaseID, personBean, locale);
		//List<TReleaseBean> replacementsList = getReplacementReleases(releaseID, projectID);
		String localizedLabel=LocalizeUtil.getLocalizedTextFromApplicationResources(RELEAESE_KEY_IN_OP, locale);
		return JSONUtility.createReplacementTreeJSON(true, label, localizedLabel, localizedLabel, releaseTree, errorMessage, locale);
	}
	
	/**
	 * Gets the replacement releases
	 * TODO will be rendered as a plain list or as a hierarchy (tree)?
	 * @param releaseID
	 * @param projectID
	 * @return
	 */
	/*private static List<TReleaseBean> getReplacementReleases(Integer releaseID, Integer projectID) {
		List<TReleaseBean> replacementOptions = releaseDAO.loadMainByProject(projectID);
		if (releaseID!=null) {
			for (Iterator<TReleaseBean> iterator = replacementOptions.iterator(); iterator.hasNext();) {
				TReleaseBean releaseBean = iterator.next();
				if (releaseBean.getObjectID().equals(releaseID)) {
					iterator.remove();
					break;
				}
			}
		}
		return replacementOptions;
	}*/
	
	/**
	 * Replace and delete
	 * @param replacementID
	 * @param node
     * @param personBean
	 * @param locale
	 * @return
	 */
	static String replaceAndDelete(Integer replacementID, String node, TPersonBean personBean, Locale locale) {
		ProjectReleaseTokens projectReleaseTokens = decodeNode(node);
		Integer projectID = projectReleaseTokens.getProjectID();
		Integer releaseID = projectReleaseTokens.getReleaseID();
		if (replacementID==null) {
			String errorMessage =  LocalizeUtil.getParametrizedString("common.err.replacementRequired", 
					new Object[] {LocalizeUtil.getLocalizedTextFromApplicationResources(RELEAESE_KEY_IN_OP, locale)}, locale);
			return renderReplace(node, errorMessage, personBean, locale);
		} else {
			replaceAndDelete(releaseID, replacementID);
			return JSONUtility.encodeJSONSuccessAndNode(encodeNode(new ProjectReleaseTokens(
					projectID, /*projectReleaseTokens.getProjectConfigType(),*/ replacementID)));
		}
	}	
	
	/** 
	 * This method replaces all occurrences of oldOID release with
	 * newOID release and then deletes the release
	 * @param oldReleaseID
	 * @param newReleaseID
	 */
	private static void replaceAndDelete(Integer oldReleaseID, Integer newReleaseID) {	
		int[] reindexWorkItems = workItemDAO.loadBySystemList(SystemFields.RELEASE, oldReleaseID);
		if (newReleaseID!=null) {
			releaseDAO.replace(oldReleaseID, newReleaseID);
		}
		delete(oldReleaseID);
		LuceneIndexer.updateWorkItemIndex(reindexWorkItems, SystemFields.INTEGER_RELEASE);
		ClusterMarkChangesBL.markDirtyWorkItemsInCluster(reindexWorkItems);
	}
	
	
	/**
	 * Drop after drag
	 * @param draggedNodeID
	 * @param droppedToNodeID
	 * @param before
	 */
	static Integer droppedNear(String draggedNodeID, String droppedToNodeID, boolean before) {
		ProjectReleaseTokens draggedTokens = decodeNode(draggedNodeID);
		Integer draggedReleaseID = draggedTokens.getReleaseID();
		ProjectReleaseTokens droppedToTokens = decodeNode(droppedToNodeID);
		Integer droppedToReleaseID = droppedToTokens.getReleaseID();
		//hopefully not needed, but just to be sure
		normalizeSortOrder(draggedNodeID);
		dropNear(draggedReleaseID, droppedToReleaseID, before);
		return draggedReleaseID;
	}
	
	/**
	 * Move up the option
	 * @param draggedNodeID
	 */
	static Integer moveUp(String draggedNodeID) {
		ProjectReleaseTokens draggedTokens = decodeNode(draggedNodeID);
		Integer draggedReleaseID = draggedTokens.getReleaseID();
		List<TReleaseBean> sortOrderBeans = normalizeSortOrder(draggedNodeID);
		ILabelBean previousLabelBean = null;
		boolean found = false;
		if (sortOrderBeans!=null) {
			for (TReleaseBean releaseBean : sortOrderBeans) {
				if (releaseBean.getObjectID().equals(draggedReleaseID)) {
					found = true;
					break;
				}
				previousLabelBean = releaseBean;
			}
		}
		if (found && previousLabelBean!=null) {
			dropNear(draggedReleaseID, previousLabelBean.getObjectID(), true);
		}
		return draggedReleaseID;
	}
	
	/**
	 * Move down the option
	 * @param draggedNodeID
	 */
	static Integer moveDown(String draggedNodeID) {
		ProjectReleaseTokens draggedTokens = decodeNode(draggedNodeID);
		Integer draggedOptionID = draggedTokens.getReleaseID();
		List<TReleaseBean> sortOrderBeans = normalizeSortOrder(draggedNodeID);
		ILabelBean nextSortedBean = null;
		if (sortOrderBeans!=null) {
			for (Iterator<TReleaseBean> iterator = sortOrderBeans.iterator(); iterator.hasNext();) {
				TReleaseBean releaseBean = iterator.next();
				if (releaseBean.getObjectID().equals(draggedOptionID)) {
					if (iterator.hasNext()) {
						nextSortedBean = iterator.next();
						break;
					}
				}
			}	
		}
		if (nextSortedBean!=null) {
			dropNear(draggedOptionID, nextSortedBean.getObjectID(), false);
		}
		return draggedOptionID;
	}
	
	/**
	 * Normalize the sortorder before change  
	 * @param node
	 * @return
	 */
	private static List<TReleaseBean> normalizeSortOrder(String node) {
		ProjectReleaseTokens projectReleaseTokens = decodeNode(node);
		Integer projectID = projectReleaseTokens.getProjectID();
		Integer releaseID = projectReleaseTokens.getReleaseID();
		List<TReleaseBean> siblingReleaseBeans = null;
		if (releaseID!=null) {
			TReleaseBean droppedReleaseBean = ReleaseBL.loadByPrimaryKey(releaseID);
			if (droppedReleaseBean!=null) {
				Integer parentID = droppedReleaseBean.getParent();
				siblingReleaseBeans = ReleaseBL.getReleases(projectID, parentID, true);
				if (siblingReleaseBeans!=null) {
					int i = 0;
					for (TReleaseBean releaseBean: siblingReleaseBeans) {
						i++;
						Integer sortOrder = releaseBean.getSortorder(); 
						if (sortOrder==null || sortOrder.intValue()!=i) {
							//set and save only if differs
							sortOrder = Integer.valueOf(i);
							releaseBean.setSortorder(sortOrder);
							ReleaseBL.saveSimple(releaseBean) ;
						}
					}
				}
			}
		}
		
		return siblingReleaseBeans;
	}
	
	/**
	 * Sets the wbs on the affected workItems after a drag and drop operation
	 * @param draggedID
	 * @param droppedToID
	 * @param before
	 */
	private static synchronized void dropNear(Integer draggedID, Integer droppedToID, boolean before) {
		TReleaseBean draggedBean = ReleaseBL.loadByPrimaryKey(draggedID);
		TReleaseBean droppedToBean =  ReleaseBL.loadByPrimaryKey(droppedToID);
		Integer draggedSortOrder = draggedBean.getSortorder();
		Integer droppedToSortOrder = droppedToBean.getSortorder();
		String sortOrderColumn = releaseDAO.getSortOrderColumn();
		String tabelName = releaseDAO.getTableName();
		Integer newSortOrder = SortOrderUtil.dropNear(draggedSortOrder, droppedToSortOrder,
				sortOrderColumn, tabelName, getSpecificCriteria(draggedBean), before);
		draggedBean.setSortorder(newSortOrder);
		ReleaseBL.saveSimple(draggedBean);
		//force reloading of all releases after sort order change
		LookupContainer.resetReleaseMap();
	}
	
	/**
	 * Gets the specific extra constraints
	 * @param releaseBean
	 * @return
	 */
	private static String getSpecificCriteria(TReleaseBean releaseBean) {
		Integer parentOfDraggedRelease = releaseBean.getParent();
		Integer projectOfDraggedRelease = releaseBean.getProjectID();
		String projectCriteria = " AND PROJKEY = " + projectOfDraggedRelease;
		String parentCriteria = "";		
		if (parentOfDraggedRelease!=null) {
			parentCriteria = " AND PARENT = " + parentOfDraggedRelease;
		} else {
			parentCriteria = " AND PARENT IS NULL ";
		}
		return projectCriteria + parentCriteria;
	}
}
