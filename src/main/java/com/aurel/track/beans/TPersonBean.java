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


package com.aurel.track.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.Constants;
import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.admin.user.profile.ProfileBL;
import com.aurel.track.admin.user.userLevel.UserLevelsProxy;
import com.aurel.track.exchange.track.ExchangeFieldNames;
import com.aurel.track.fieldType.constants.BooleanFields;
import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.fieldType.runtime.helpers.MergeUtil;
import com.aurel.track.fieldType.runtime.matchers.run.MatcherContext;
import com.aurel.track.prop.ApplicationBean;
import com.aurel.track.user.TpPasswordEncoder;
import com.aurel.track.util.DateTimeUtils;
import com.aurel.track.util.LdapUtil;
import com.aurel.track.util.LocaleHandler;
import com.aurel.track.util.PropertiesHelper;

/**
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TPersonBean
extends com.aurel.track.beans.base.BaseTPersonBean
	implements Serializable, IBeanID, ISortedBean,
		ISerializableLabelBean, Comparable
{
	public static interface PERSON_CATEGORY {
		public static final int ALL = 0; // all persons and persons via groups
		public static final int ALL_DIRECT = 1;  // all persons not via groups and groups
		public static final int ALL_PERSONS = 2; // just persons
		public static final int DIRECT_PERSONS = 3; // all persons not via groups
		public static final int GROUPS = 4; // just groups
		public static final int INDIRECT_PERSONS = 5; // all persons via groups
	}

	/**
	 * @deprecated
	 * @author Tamas
	 *
	 */
	public static interface PERSON_STATUS {
		public static final String INTERNAL_ACTIVE = "N";
		public static final String INTERNAL_INACTIVE = "Y";
		public static final String EXTERNAL_ACTIVE = "E";
		public static final String EXTERNAL_INACTIVE = "X";
	}

	public static interface USERLEVEL {
		public static final Integer CLIENT = Integer.valueOf(0);
		public static final Integer FULL = Integer.valueOf(1);
		public static final Integer SYSADMIN = Integer.valueOf(2);
		public static final Integer SYSMAN = Integer.valueOf(3);
	}

	public static Integer DEFAULT_USERLEVEL=USERLEVEL.FULL;

	public static interface EMAIL {
		public static final Integer YES_EMAIL_PLEASE = Integer.valueOf(0);
		public static final Integer NO_EMAIL_PLEASE = Integer.valueOf(1);
	}

	public static String GUEST_USER = "guest";
	public static String ADMIN_USER = "admin";
	//workflow user
	public static String WORKFLOW_USER = "workflow";
	public static String WORKFLOW_FIRST_NAME = "Genji System";
	public static String WORKFLOW_LAST_NAME = "Workflow";
	public static int WORKFLOW_USER_ID = -1;

	public static final String LAST_LOGIN = "LastLogin";
	public static final String IS_SYSADM = "SYSA";

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(TPersonBean.class);

	// the properties currently supported by this bean
	//TODO move the other such properties from TPerson as soon as the TPersonBean
	//will be stored in the session instead of the torque TPerson
	//public static final String REPORT_SORTORDER = "ReportSortOrder";
	//public static final String REPORT_GROUPBYFIELD = "ReportGroupByField";

	public static final String CSV_CHAR = "CsvChar";
	public static final String CSV_ENCODING = "CsvEncoding";

	public static final String RICH_TEXT_EDITOR_EXPANDED="richTextEditorExpanded";
	public static final String HISTORY_FULL="historyFull";
	public static final String SHOW_COMMENTS_HISTORY="showCommentsHistory";
	public static final String PRINT_ITEM_EDITABLE="printItemEditable";
	public static final String AUTOLOAD_TIME="autoLoadTime";
	public static final String SHOW_DASHBOARD_TIMER="showDashboardTimer";
	public static final String ENABLE_QUERY_LAYOUT="enableQueryLayout";
	public static final String SHOW_FLAT_HISTORY = "showFlatHistory";
	public static final String CARD_VIEW_GROUP_FIELD = "cardViewGroupField";
	public static final String CARD_VIEW_GROUP_FILTER = "cardViewGroupFilter";
	public static final String ITEM_NAVIGATOR_SETTINGS_VISIBLE = "itemNavigator_settingsVisible";
	public static final String ITEM_NAVIGATOR_FILTER_EDIT_VISIBLE = "itemNavigator_filterEditVisible";
	public static final String ITEM_NAVIGATOR_LAST_SELECTED_VIEW = "itemNavigator.lastSelectedView";
	public static final String ITEM_NAVIGATOR_PAGINATE = "itemNavigatorPaginate";
	public static final String ITEM_NAVIGATOR_PAGE_SIZE = "itemNavigatorPageSize";
	public static final String SHOW_FLAT_VERSION_CONTROL = "showFlatVersionControl";
	public static final String ALWAYS_SAVE_ATTACHMENT="allwaysSaveAttachment";
	public static final String SESSION_TIMEOUT_MINUTES="sessionTimeoutMinutes";
	public static final String HOME_PAGE="homePage";
	public static final String USERTZ="usertz";
	public static final String DESIGNPATH = "DesignPath";
	private static final String REMINDER_DAYS = "ReminderDays";
	public static final String LOCALE = "Loc";
	public static final String WIKI_EDITABLE_MODE = "wikiEditableMode";
	public static final String WIKI_LAST_DOCUMENT = "wikiLastDocument";
	public static final String LINKING_LAST_SEARCH = "linkingLastSearch";

	public static final String DOCX_LAST_TEMPLATE = "docxLastTemplate";

	public static final String NAME_SEPARATOR = ", ";
	private boolean isProjAdmin = false;
	private boolean isAnonimous = false;
	private Date lastButOneLogin = null;

	private boolean failure;
	private String failureDescription;
	private String fullName=null;
	//added for performance reasons (avoid DB accesses)
	private List<Integer> substitutedPersons;
	 /**
     * Whether the number of project role based items are above the configured limit
     */
    private Boolean projectRoleItemsAboveLimit;

    /**
     * Whether the number of RACI items are above the configured limit
     */
    private Boolean raciRoleItemsAboveLimit;

    private String plainPwd;

    private Map<String, Boolean> licensedFeaturesMap = new HashMap<String, Boolean>();

	public TPersonBean() {
		super();
		this.moreProperties = PropertiesHelper.getProperties(getPreferences());
	}

	public TPersonBean(String firstName, String lastName, String email) {
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setEmail(email);
		this.moreProperties = new Properties();
	}

	/**
	 * Gets the user level map for person
	 * @return
	 */
	public Map<Integer, Boolean> getUserLevelMap() {
		Integer userLevel = getUserLevel();
		if (userLevel==null) {
			userLevel = USERLEVEL.FULL;
		}
		UserLevelsProxy ul = UserLevelsProxy.getInstance();
		Map<Integer, Boolean> userLevelMap = ul.getMapByUserLevel(userLevel);
		if (userLevelMap==null) {
			return ul.getMapByUserLevel(USERLEVEL.FULL);
		}
		return userLevelMap;
	}

	public List<Integer> getSubstitutedPersons() {
		return substitutedPersons;
	}

	public void setSubstitutedPersons(List<Integer> substitutedPersons) {
		this.substitutedPersons = substitutedPersons;
	}

	public Boolean getProjectRoleItemsAboveLimit() {
		return projectRoleItemsAboveLimit;
	}

	public void setProjectRoleItemsAboveLimit(Boolean projectRoleItemsAboveLimit) {
		this.projectRoleItemsAboveLimit = projectRoleItemsAboveLimit;
	}

	public Boolean getRaciRoleItemsAboveLimit() {
		return raciRoleItemsAboveLimit;
	}

	public void setRaciRoleItemsAboveLimit(Boolean raciRoleItemsAboveLimit) {
		this.raciRoleItemsAboveLimit = raciRoleItemsAboveLimit;
	}

	/** TODO Comment this out breaks webservice build */
    public boolean isFailure() {
		return failure;
	}
	public void setFailure(boolean failure) {
		this.failure = failure;
	}
	public String getFailureDescription() {
		return failureDescription;
	}
	public void setFailureDescription(String failureDescription) {
		this.failureDescription = failureDescription;
	}

	public boolean isProjAdmin() {
		return isProjAdmin;
	}
	public void setProjAdmin(boolean isProjAdmin) {
		this.isProjAdmin = isProjAdmin;
	}

	public boolean isAnonimous() {
		return isAnonimous;
	}
	public void setAnonimous(boolean isAnonimous) {
		this.isAnonimous = isAnonimous;
	}
	public boolean isGroup() {
		return BooleanFields.TRUE_VALUE.equals(getIsgroup());
	}

	public void setIsGroupBool(boolean isGroup) {
		setIsgroup(BooleanFields.fromBooleanToString(isGroup));
	}


	/**
	 * Returns true if person wants to use LDAP
	 * @return true if person wants to use LDAP, false if not
	 */
	public boolean isLdapUser() {
		TpPasswordEncoder enc = new TpPasswordEncoder();
		return (enc.encodePassword(Constants.LdapUserPwd, getSalt()).equals(getPasswd())
				|| enc.encodePassword1(Constants.LdapUserPwd).equals(getPasswd()));
	}

	public void setIsLdapUser() {
		setPasswdEncrypted(Constants.LdapUserPwd);
	}

	/**
	 * Get the full name (lastname, firstname) of a TPerson
	 * Fullname is appended by a '*' if TPerson is marked as deleted
	 * @return the full name
	 */
	public String getFullName() {
		if(fullName==null){
			if (isGroup()) {
				fullName=getFullName(getLoginName(), null, isDisabled(), isSysAdmin());
			} else {
				fullName= getFullName(getLastName(), getFirstName(), isDisabled(), isSysAdmin());
			}
		}
		return fullName;
	}

	/**
	 * Get the full name (lastname, firstname) of a TPerson
	 * Fullname is appended by a '*' if TPerson is marked as deleted
	 * @return the full name
	 */
	public String getSimpleFullName() {
		if (isGroup()) {
			return getLoginName();
		} else {
			StringBuffer fullSimpleName = new StringBuffer();
			if (getLastName()!=null) {
				fullSimpleName.append(getLastName());
			}
			if (getFirstName()!=null) {
				fullSimpleName.append(" ");
				fullSimpleName.append(getFirstName());
			}
			return fullSimpleName.toString();
		}
	}

	/**
	 * Get the full name (lastname, firstname) of a TPerson
	 * @return the full name
	 */
	public String getName() {
		if (isGroup()) {
			return getLoginName();
		} else {
			StringBuffer fullSimpleName = new StringBuffer();
			if (getLastName()!=null) {
				fullSimpleName.append(getLastName());
			}
			if (getFirstName()!=null) {
				fullSimpleName.append(", ");
				fullSimpleName.append(getFirstName());
			}
			return fullSimpleName.toString();
		}
	}

	/**
	 * Get the full name (firstname lastname) of a TPerson
	 * with no further additional flags
	 * @return the full name
	 */
	public String getPureFullName() {
		if (isGroup()) {
			return getLoginName();
		} else {
			StringBuffer fullSimpleName = new StringBuffer();
			if (getFirstName()!=null) {
				fullSimpleName.append(getFirstName());
			}
			if (getLastName()!=null) {
				fullSimpleName.append(" ");
				fullSimpleName.append(getLastName());
			}
			return fullSimpleName.toString();
		}
	}

	/**
	 * Helper method for building the fullname
	 * @param lastName
	 * @param firstName
	 * @param deactivated
	 * @param external
	 * @param isSysAdmin
	 * @return
	 */
	public String getFullName(String lastName, String firstName, boolean deactivated, boolean isSysAdmin) {
		StringBuilder fullName = new StringBuilder();
		if (lastName!=null) {
			fullName.append(lastName);
		}
		if (firstName!=null && !"".equals(firstName)) {
			fullName.append(NAME_SEPARATOR);
			fullName.append(firstName);
		}
		fullName.append(getMarker(deactivated, isSysAdmin));
		return fullName.toString();
	}

	public static String DEACTIVATED = " *";

	private StringBuilder getMarker(boolean deactivated, boolean isSysAdmin) {
		StringBuilder marker = new StringBuilder("");
		if (deactivated) {
			marker.append(DEACTIVATED);
		}
		return marker;
	}

	/**
	 * @return true, if person was deactivated
	 */
	public boolean isDisabled() {
		return BooleanFields.TRUE_VALUE.equals(getDeleted());
	}

	public void setDisabled(boolean disabled) {
		if (disabled) {
			setDeleted(BooleanFields.TRUE_VALUE);
		} else {
			setDeleted(BooleanFields.FALSE_VALUE);
		}
	}

	public boolean isExternal() {
		return USERLEVEL.CLIENT.equals(getUserLevel());
	}

	public boolean isSysAdmin() {
		if (getObjectID()==null ||
				getObjectID().equals(MatcherContext.LOGGED_USER_SYMBOLIC) ||
				getObjectID().equals(MatcherContext.PARAMETER)) {
			//a new user or the symbolic user is newer system admin
			return false;
		}
		if (getObjectID().intValue() < 100 && getObjectID().intValue()>0) {
			//user below 100 are always sysAdmins independently of the UserLevel
			return true;
		}
		return USERLEVEL.SYSADMIN.equals(getUserLevel());
	}

	public void setSysAdmin(boolean sysAdmin) {
		if (sysAdmin) {
			setUserLevel(USERLEVEL.SYSADMIN);
		} else {
			setUserLevel(USERLEVEL.FULL);
		}
	}

	public boolean isSysManager() {
		return USERLEVEL.SYSMAN.equals(getUserLevel());
	}

	public void setSysManager(boolean sysMan) {
		if (sysMan) {
			setUserLevel(USERLEVEL.SYSMAN);
		} else {
			setUserLevel(USERLEVEL.FULL);
		}
	}

	/**
	 * Whether the user is either system administrator or system manager
	 * @return
	 */
	public boolean isSys() {
		return isSysAdmin() || isSysManager();
	}

	public boolean isAlwaysSaveAttachment() {
		return PropertiesHelper.getBooleanProperty(getMoreProperties(), TPersonBean.ALWAYS_SAVE_ATTACHMENT);
	}

	public void setAlwaysSaveAttachment(Boolean value) {
		setPreferences(PropertiesHelper.setBooleanProperty(getPreferences(), TPersonBean.ALWAYS_SAVE_ATTACHMENT, value));
	}


	/**
	 * @return Returns the noEmailPlease.
	 */
	public boolean getNoEmailPleaseBool() {
		if ((EMAIL.NO_EMAIL_PLEASE).equals(super.getNoEmailPlease())) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * @param noEmailPlease The noEmailPlease to set.
	 */
	public void setNoEmailPleaseBool(boolean noEmailPlease) {
		if (noEmailPlease) {
			super.setNoEmailPlease(EMAIL.NO_EMAIL_PLEASE);
		}
		else {
			super.setNoEmailPlease(EMAIL.YES_EMAIL_PLEASE);
		}
	}

	/**
	 * Remind me as originator
	 * @param remindMe
	 */
	public void setRemindMeAsOriginatorBool(boolean remindMe) {
		if (remindMe) {
			setRemindMeAsOriginator(BooleanFields.TRUE_VALUE);
		} else {
			setRemindMeAsOriginator(BooleanFields.FALSE_VALUE);
		}
	}

	public boolean getRemindMeAsOriginatorBool() {
		return BooleanFields.TRUE_VALUE.equals(getRemindMeAsOriginator().toUpperCase());
	}

	/**
	 * Remind me as manager
	 * @param remindMe
	 */
	public void setRemindMeAsManagerBool(boolean remindMe) {
		if (remindMe) {
			setRemindMeAsManager(BooleanFields.TRUE_VALUE);
		} else {
			setRemindMeAsManager(BooleanFields.FALSE_VALUE);
		}
	}

	public boolean getRemindMeAsManagerBool() {
		return BooleanFields.TRUE_VALUE.equals(getRemindMeAsManager().toUpperCase());
	}

	/**
	 * Remind me as responsible
	 * @param remindMe
	 */
	public void setRemindMeAsResponsibleBool(boolean remindMe) {
		if (remindMe) {
			setRemindMeAsResponsible(BooleanFields.TRUE_VALUE);
		} else {
			setRemindMeAsResponsible(BooleanFields.FALSE_VALUE);
		}
	}

	public boolean getRemindMeAsResponsibleBool() {
		return BooleanFields.TRUE_VALUE.equals(getRemindMeAsResponsible().toUpperCase());
	}

	@Override
	public Comparable getSortOrderValue() {
		return getFullName();
	}

	@Override
	public String getLabel() {
		return getFullName();
	}

	@Override
	public void setPreferences(String _moreProps) {
		moreProperties = PropertiesHelper.getProperties(_moreProps);
		super.setPreferences(_moreProps);
	}

	protected Properties moreProperties = null;

	public Properties getMoreProperties() {
		if (moreProperties == null) {
			moreProperties = PropertiesHelper.getProperties(getPreferences());
			if (moreProperties == null) {
				moreProperties = new Properties();
			}
		}
		return moreProperties;
	}

	public Date getLastLogin() {
		Date lastLogin = null;
		long lastLoginDateInMilliseconds = 0;
		//it is stored as a long based string (milliseconds value) to avoid the data parsing problems
		String strLastLogin = PropertiesHelper.getProperty(getMoreProperties(), TPersonBean.LAST_LOGIN);
		if (strLastLogin!=null) {
			try {
				lastLoginDateInMilliseconds = new Long(strLastLogin).longValue();
			} catch (Exception e) {
				LOGGER.error("Loading the last login from preferences failed with " + e.getMessage());
			}
			if (lastLoginDateInMilliseconds>0) {
				lastLogin = new Date(lastLoginDateInMilliseconds);
			}
		}
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		String preferences = getPreferences();
		if (lastLogin==null) {
			PropertiesHelper.removeProperty(preferences, LAST_LOGIN);
			return;
		}
		String strLastLogin = new Long(lastLogin.getTime()).toString();
		setPreferences(PropertiesHelper.setProperty(preferences, LAST_LOGIN, strLastLogin));
	}

	/**
	 * @return the lastButOneLogin
	 */
	public Date getLastButOneLogin() {
		if (lastButOneLogin!=null) {
			return lastButOneLogin;
		} else {
			return getLastLogin();
		}
	}
	/**
	 * @param lastButOneLogin the lastButOneLogin to set
	 */
	public void setLastButOneLogin(Date lastButOneLogin) {
		this.lastButOneLogin = lastButOneLogin;
	}

	/**
	 * Get the sorted by field
	 *
	 * @return
	 */

	/**
	 * Get the sort order
	 *
	 * @return
	 */

	/**
	 * Set the sort order
	 * @param sortOrder
	 */

	/**
	 * Get the sorted by field
	 * @return
	 */

	/**
	 * Set the the sorted by field
	 * @param groupByFieldString
	 */

	/**
	 * The character used for comma separated value files, e.g. Excel exports
	 * @return
	 */
	public Character getCsvCharacter() {
		String preferences = getPreferences();
		String csv = PropertiesHelper.getProperty(getMoreProperties(), CSV_CHAR);
		if (csv == null || csv.length() < 1) {
			  return new Character(';');
		}
		else {
			return new Character(csv.charAt(0));
		}
	}

	/**
	 * The character used for comma separated value files, e.g. Excel exports
	 * @return
	 */
	public void setCsvCharacter(String csvSeparator) {
		if (csvSeparator!=null) {
			setPreferences(PropertiesHelper.setProperty(getPreferences(),
					CSV_CHAR, csvSeparator));
		}
	}

	/**
	 * The character used for comma separated value files, e.g. Excel exports
	 * @return
	 */
	public String getCsvEncoding() {
		return PropertiesHelper.getProperty(getMoreProperties(), CSV_ENCODING);
	}

	/**
	 * The character used for comma separated value files, e.g. Excel exports
	 * @return
	 */
	public void setCsvEncoding(String csvEncoding) {
		if (csvEncoding!=null) {
			setPreferences(PropertiesHelper.setProperty(getPreferences(),
				CSV_ENCODING, csvEncoding));
		}
	}


	/**
	 * The character used for comma separated value files, e.g. Excel exports
	 * @return
	 */
	public String getLastSelectedView() {
		return PropertiesHelper.getProperty(getMoreProperties(), ITEM_NAVIGATOR_LAST_SELECTED_VIEW);
	}

	/**
	 * The character used for comma separated value files, e.g. Excel exports
	 * @return
	 */
	public void setLastSelectedView(String lastSelectedView) {
		if (lastSelectedView!=null) {
			setPreferences(PropertiesHelper.setProperty(getPreferences(),
				ITEM_NAVIGATOR_LAST_SELECTED_VIEW, lastSelectedView));
		}
	}

	/**
	 * Used by sorting sets of personBeans by group and fullName
	 */
	@Override
	public int compareTo(Object o) {
		TPersonBean personBean = (TPersonBean)o;
		if (getIsgroup()==null && personBean.getIsgroup()==null) {
			return getFullName().compareTo(personBean.getFullName());
		}
		if (getIsgroup()==null) {
			return -1;
		}
		if (personBean.getIsgroup()==null) {
			return 1;
		}
		if (getIsgroup().equals(personBean.getIsgroup())) {
			return getFullName().compareTo(personBean.getFullName());
		} else {
			return getIsgroup().compareTo(personBean.getIsgroup());
		}
	}

	@Override
	public boolean equals(Object o) {
		TPersonBean personBean = (TPersonBean)o;
		if (personBean == null || o == null) {
			return false;
		}
		return getObjectID().equals(personBean.getObjectID());
	}

	@Override
	public int hashCode() {
		if (getObjectID() == null) {
			return -999999;
		} else {
			return getObjectID();
		}
	}

	public boolean isRichTextEditorExpanded() {
		return PropertiesHelper.getBooleanProperty(getMoreProperties(), RICH_TEXT_EDITOR_EXPANDED);
	}

	public void setRichTextEditorExpanded(boolean richTextEditorExpanded) {
		setPreferences(PropertiesHelper.setBooleanProperty(getPreferences(), RICH_TEXT_EDITOR_EXPANDED, richTextEditorExpanded));
	}

	public boolean isShowFullHistory() {
		return PropertiesHelper.getBooleanProperty(getMoreProperties(), HISTORY_FULL);
	}

	public void setShowFullHistory(boolean showFullHistory) {
		setPreferences(PropertiesHelper.setBooleanProperty(getPreferences(), HISTORY_FULL, showFullHistory));
	}


	public boolean isShowCommentsHistory() {
		return PropertiesHelper.getBooleanProperty(getMoreProperties(), SHOW_COMMENTS_HISTORY);
	}

	public void setShowCommentsHistory(boolean showCommentsHistory) {
		setPreferences(PropertiesHelper.setBooleanProperty(getPreferences(), SHOW_COMMENTS_HISTORY, showCommentsHistory));
	}

	public boolean isPrintItemEditable() {
		return PropertiesHelper.getBooleanProperty(getMoreProperties(), PRINT_ITEM_EDITABLE);
	}

	public void setPrintItemEditable(boolean printItemEditable) {
		setPreferences(PropertiesHelper.setBooleanProperty(getPreferences(), PRINT_ITEM_EDITABLE, printItemEditable));
	}

	public Integer getAutoLoadTime() {
		return PropertiesHelper.getIntegerProperty(getMoreProperties(), AUTOLOAD_TIME);
	}

	public void setAutoLoadTime(Integer autoLoadTime) {
		setPreferences(PropertiesHelper.setIntegerProperty(getPreferences(), AUTOLOAD_TIME, autoLoadTime));
	}

	public boolean getShowDashboardTimer() {
		return PropertiesHelper.getBooleanProperty(getMoreProperties(), SHOW_DASHBOARD_TIMER);
	}

	public void setShowDashboardTimer(boolean showDashboardTimer) {
		setPreferences(PropertiesHelper.setBooleanProperty(getPreferences(), SHOW_DASHBOARD_TIMER, showDashboardTimer));
	}

	public boolean isEnableQueryLayout() {
		return PropertiesHelper.getBooleanProperty(getMoreProperties(), ENABLE_QUERY_LAYOUT);
	}

	public void setEnableQueryLayout(boolean enableQueryLayout) {
		setPreferences(PropertiesHelper.setBooleanProperty(getPreferences(), ENABLE_QUERY_LAYOUT, enableQueryLayout));
	}

	public boolean isPaginate() {
		return PropertiesHelper.getBooleanProperty(getMoreProperties(), ITEM_NAVIGATOR_PAGINATE);
	}

	public void setPaginate(boolean paginate) {
		setPreferences(PropertiesHelper.setBooleanProperty(getPreferences(), ITEM_NAVIGATOR_PAGINATE, paginate));
	}

	public boolean isShowFlatHistory() {
		return PropertiesHelper.getBooleanProperty(getMoreProperties(), SHOW_FLAT_HISTORY);
	}

	public void setShowFlatHistory(boolean showFlatHistory) {
		setPreferences(PropertiesHelper.setBooleanProperty(getPreferences(), SHOW_FLAT_HISTORY, showFlatHistory));
	}

	public Integer getSessionTimeoutMinutes() {
		return PropertiesHelper.getIntegerProperty(getMoreProperties(), SESSION_TIMEOUT_MINUTES);
	}

	public void setSessionTimeoutMinutes(Integer sessionTimeoutMinutes) {
		if (sessionTimeoutMinutes == null) {
			sessionTimeoutMinutes = new Integer(120);
		}
		setPreferences(PropertiesHelper.setProperty(getPreferences(), SESSION_TIMEOUT_MINUTES, sessionTimeoutMinutes.toString()));
	}

	public String getHomePage() {
		return PropertiesHelper.getProperty(getMoreProperties(), HOME_PAGE);
	}

	public void setHomePage(String homePage) {
		setPreferences(PropertiesHelper.setProperty(getPreferences(), HOME_PAGE, homePage));
	}


	public String getUserTimeZoneId() {
		String utz = PropertiesHelper.getProperty(getMoreProperties(), USERTZ);
		if (utz == null || "".equals(utz)) {
			utz = DateTimeUtils.getServerTimeZone().getID();
		}
		return utz;
	}

	public void setUserTimeZoneId(String userTz) {
		setPreferences(PropertiesHelper.setProperty(getPreferences(), USERTZ, userTz));
	}

	/**
	 * Gets the design path selected by the user
	 * @return
	 */
	public String getDesignPath() {
		String designPath = PropertiesHelper.getProperty(getMoreProperties(), DESIGNPATH);
		if (designPath==null || designPath.length()==0) {
			return Constants.DEFAULTDESIGNPATH;
		} else {
			return designPath;
		}
	}

	/**
	 * Sets the design path selected by the user
	 * @param designPath
	 */
	public void setDesignPath(String designPath) {
		String preferences = getPreferences();
		if (designPath == null) {
			designPath = Constants.DEFAULTDESIGNPATH;
		}
		setPreferences(PropertiesHelper.setProperty(preferences, DESIGNPATH, designPath));
	}


	public Locale getLocale() {
		// This is just for transport from TPerson.
		// The locale itself will come for TPerson.
		String loc = PropertiesHelper.getProperty(getMoreProperties(), TPersonBean.LOCALE);
		if (loc == null || "".equals(loc)) {
			return Locale.getDefault();
		}
		return LocaleHandler.getLocaleFromString(loc);
	}

	public void setLocale(Locale locale) {
		if (locale!=null) {
			setPreferences(PropertiesHelper.setProperty(getPreferences(), LOCALE, locale.toString()));
		}
	}

	public boolean isPreferredEmailTypePlain() {
		String preferredEmail = this.getPrefEmailType();
		if (preferredEmail==null) {
			return false;
		}
		return ProfileBL.EMAIL_TYPE.PLAIN.equals(preferredEmail);
	}


	/*
	 * The following methods are provided for the Spring security framework
	 */

	public String getPassword() {
		return this.getPasswd();
	}

	public String getPlainPwd() {
		return plainPwd;
	}

	public void setPlainPwd(String plainPwd) {
		this.plainPwd = plainPwd;
	}


	public Map<String, Boolean> getLicensedFeaturesMap() {
		return licensedFeaturesMap;
	}

	public void setLicensedFeaturesMap(Map<String, Boolean> licensedFeaturesMap) {
		this.licensedFeaturesMap = licensedFeaturesMap;
	}

	public String getUsername() {
		return this.getLoginName();
	}

	public boolean isAccountNonExpired() {

		return true;
	}

	public boolean isAccountNonLocked() {

		return true;
	}

	public boolean isCredentialsNonExpired() {

		return true;
	}

	public boolean isEnabled() {
		return !isDisabled();
	}

	public List<Integer> getReminderDays()
	{
		List<Integer> reminderDays = new ArrayList<Integer>(7);
		String preferences = getPreferences();
		String strReminderDays = PropertiesHelper.getProperty(getMoreProperties(), REMINDER_DAYS);
		if (strReminderDays!=null) {
			reminderDays = new ArrayList<Integer>(strReminderDays.length());
			for (int i=0;i<strReminderDays.length();i++) {
				char dayNo = strReminderDays.charAt(i);
				try {
					reminderDays.add(Integer.valueOf(new Character(dayNo).toString()).intValue());
				} catch(Exception e) {

				}
			}
		}
		return reminderDays;
	}

	public void setReminderDays(List<Integer> reminderDays) {
		String preferences = getPreferences();
		StringBuffer buffer = new StringBuffer();
		if (reminderDays==null || reminderDays.isEmpty()) {
			PropertiesHelper.removeProperty(preferences, REMINDER_DAYS);
			return;
		}
		for (Integer reminderDay : reminderDays) {
			if (reminderDay!=null) {
				buffer.append(reminderDay);
			}
		}
		String strReminderDays = buffer.toString();
		setPreferences(PropertiesHelper.setProperty(preferences, REMINDER_DAYS, strReminderDays));
	}


	/**
	 * Extends the standard setter method by converting the password to
	 * its encrypted form first. A unique salt is generated for each user.
	 * @param ppasswd the password to set
	 */
	public void setPasswdEncrypted(String ppasswd) {
		TpPasswordEncoder enc = new TpPasswordEncoder();
		setSalt(UUID.randomUUID().toString());
		super.setPasswd(enc.encodePassword(ppasswd, getSalt()));
	}

	/** Returns true if this person has system administrator rights */
	public boolean getIsSysAdmin() {
		if (getObjectID()==null) {
			//a new user is newer system admin
			return false;
		}
		if (getObjectID().intValue() < 100) {
			//user below 100 are always sysAdmins independently of the UserLevel
			return true;
		}
		return USERLEVEL.SYSADMIN.equals(getUserLevel());
	}

	/** Set to true if this person has system administrator rights */
	public void setIsSysAdmin(boolean value) {
		if (value) {
			setUserLevel(USERLEVEL.SYSADMIN);
		} else {
			setUserLevel(USERLEVEL.FULL);
		}

	}

	/**
	 * Get the sorted by field
	 * @return
	 */
	public String getDocxLastTemplate() {
		return PropertiesHelper.getProperty(getMoreProperties(), DOCX_LAST_TEMPLATE);
	}

	/**
	 * Set the the sorted by field
	 * @param groupByFieldString
	 */
	public void setDocxLastTemplate(String docxLastTemplate) {
		if (docxLastTemplate!=null) {
			setPreferences(PropertiesHelper.setProperty(getPreferences(),
					DOCX_LAST_TEMPLATE, docxLastTemplate));
		}
	}

	/**
	 * Serialize a label bean to a map
	 * @return
	 */
	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("uuid", getUuid());
		attributesMap.put("firstName", getFirstName());
		attributesMap.put("lastName", getLastName());
		attributesMap.put("loginName", getLoginName());
		attributesMap.put("email", getEmail());
		String passwd = getPasswd();
		if (passwd!=null && !"".equals(passwd)) {
			attributesMap.put("passwd", passwd);
		}
		String phone = getPhone();
		if (phone!=null && !"".equals(phone)) {
			attributesMap.put("phone", phone);
		}
		Integer departmentID = getDepartmentID();
		if (departmentID!=null) {
			attributesMap.put("departmentID", departmentID.toString());
		}
		String preferences = getPreferences();
		if (preferences!=null && !"".equals(preferences)) {
			attributesMap.put("preferences", preferences);
		}
		attributesMap.put("deleted", getDeleted());
		Integer emailFrequency = getEmailFrequency();
		if (emailFrequency!=null) {
			attributesMap.put("emailFrequency", emailFrequency.toString());
		}
		Integer emailLead = getEmailLead();
		if (emailLead!=null) {
			attributesMap.put("emailLead", emailLead.toString());
		}
		Date emailLastReminded = getEmailLastReminded();
		if (emailLastReminded!=null) {
			attributesMap.put("emailLastReminded", DateTimeUtils.getInstance().formatISODate(emailLastReminded));
		}
		attributesMap.put("emailRemindMe", getEmailRemindMe());
		attributesMap.put("prefEmailType", getPrefEmailType());
		attributesMap.put("prefLocale", getPrefLocale());
		Integer noEmailPlease = getNoEmailPlease();
		if (noEmailPlease!=null) {
			attributesMap.put("noEmailPlease", noEmailPlease.toString());
		}
		attributesMap.put("remindMeAsOriginator", getRemindMeAsOriginator());
		attributesMap.put("remindMeAsManager", getRemindMeAsManager());
		attributesMap.put("remindMeAsResponsible", getRemindMeAsResponsible());

		Integer emailRemindPriorityLevel = getEmailRemindPriorityLevel();
		if (emailRemindPriorityLevel!=null) {
			attributesMap.put("emailRemindPriorityLevel", emailRemindPriorityLevel.toString());
		}
		Integer emailRemindSeverityLevel = getEmailRemindSeverityLevel();
		if (emailRemindSeverityLevel!=null) {
			attributesMap.put("emailRemindSeverityLevel", emailRemindSeverityLevel.toString());
		}
		Double hoursPerWorkDay = getHoursPerWorkDay();
		if (hoursPerWorkDay!=null) {
			attributesMap.put("hoursPerWorkDay", hoursPerWorkDay.toString());
		}
		Double hourlyWage = getHourlyWage();
		if (hourlyWage!=null) {
			attributesMap.put("hourlyWage", hourlyWage.toString());
		}
		Double extraHourlWage = getExtraHourWage();
		if (extraHourlWage!=null) {
			attributesMap.put("extraHourWage", extraHourlWage.toString());
		}
		String employeeID = getEmployeeID();
		if (employeeID!=null && !"".equals(employeeID)) {
			attributesMap.put("employeeID", employeeID);
		}
		attributesMap.put("isgroup", getIsgroup());
		Integer userLevel = getEmailRemindSeverityLevel();
		if (userLevel!=null) {
			attributesMap.put("userLevel", userLevel.toString());
		}
		return attributesMap;
	}

	/**
	 * De-serialze the labelBean
	 * @param attributes
	 * @return
	 */
	@Override
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		TPersonBean personBean = new TPersonBean();
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			personBean.setObjectID(new Integer(strObjectID));
		}
		personBean.setUuid(attributes.get("uuid"));
		personBean.setFirstName(attributes.get("firstName"));
		personBean.setLastName(attributes.get("lastName"));
		personBean.setLoginName(attributes.get("loginName"));
		personBean.setEmail(attributes.get("email"));
		personBean.setPasswd(attributes.get("passwd"));
		personBean.setPhone(attributes.get("phone"));
		String strDepartment = attributes.get("departmentID");
		if (strDepartment!=null) {
			personBean.setDepartmentID(new Integer(strDepartment));
		}
		personBean.setPreferences(attributes.get("preferences"));
		personBean.setDeleted(attributes.get("deleted"));
		String strEmailFrequency = attributes.get("emailFrequency");
		if (strEmailFrequency!=null) {
			personBean.setEmailFrequency(new Integer(strEmailFrequency));
		}
		String strEmailLead = attributes.get("emailLead");
		if (strEmailLead!=null) {
			personBean.setEmailLead(new Integer(strEmailLead));
		}
		String strEmailLastReminded = attributes.get("emailLastReminded");
		if (strEmailLastReminded!=null) {
			personBean.setEmailLastReminded(DateTimeUtils.getInstance().parseISODate(strEmailLastReminded));
		}
		personBean.setEmailRemindMe(attributes.get("emailRemindMe"));
		personBean.setPrefEmailType(attributes.get("prefEmailType"));
		personBean.setPrefLocale(attributes.get("prefLocale"));

		String strNoEmailPlease = attributes.get("noEmailPlease");
		if (strNoEmailPlease!=null) {
			personBean.setNoEmailPlease(new Integer(strNoEmailPlease));
		}
		personBean.setRemindMeAsOriginator(attributes.get("remindMeAsOriginator"));
		personBean.setRemindMeAsManager(attributes.get("remindMeAsManager"));
		personBean.setRemindMeAsResponsible(attributes.get("remindMeAsResponsible"));

		String strEmailRemindPriorityLevel = attributes.get("emailRemindPriorityLevel");
		if (strEmailRemindPriorityLevel!=null) {
			personBean.setEmailRemindPriorityLevel(new Integer(strEmailRemindPriorityLevel));
		}
		String strEmailRemindSeverityLevel = attributes.get("emailRemindSeverityLevel");
		if (strEmailRemindSeverityLevel!=null) {
			personBean.setEmailRemindSeverityLevel(new Integer(strEmailRemindSeverityLevel));
		}
		String strHoursPerWorkDay = attributes.get("hoursPerWorkDay");
		if (strHoursPerWorkDay!=null) {
			personBean.setHoursPerWorkDay(new Double(strHoursPerWorkDay));
		}
		String strHourlyWage = attributes.get("hourlyWage");
		if (strHourlyWage!=null) {
			personBean.setHourlyWage(new Double(strHourlyWage));
		}
		String strExtraHourWage = attributes.get("extraHourWage");
		if (strExtraHourWage!=null) {
			personBean.setExtraHourWage(new Double(strExtraHourWage));
		}
		personBean.setEmployeeID(attributes.get("employeeID"));
		personBean.setIsgroup(attributes.get("isgroup"));
		String strUserLevel = attributes.get("userLevel");
		if (strUserLevel!=null) {
			personBean.setUserLevel(new Integer(strUserLevel));
		}
		return personBean;
	}

	/**
	 * Whether two label beans are equivalent
	 * @param serializableLabelBean
	 * @param matchesMap	key: fieldID_paramaterCode
	 * 						value: map of already mapped external vs. internal objectIDs
	 * @return
	 */
	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		if (serializableLabelBean==null) {
			return false;
		}
		TPersonBean personBean = (TPersonBean)serializableLabelBean;
		String externUserName = getUsername();
		String internUserName = personBean.getUsername();
		String externEmail = getEmail();
		String internEmail = personBean.getEmail();
		if (externEmail!=null && internEmail!=null) {
			if (externEmail.equals(internEmail)) {
				return true;
			}
		}
		//for admin and guest users the names are enough
		if (externUserName!=null && internUserName!=null) {
			return externUserName.equals(internUserName) &&
				(ADMIN_USER.equals(externUserName) || GUEST_USER.equals(externUserName));
		}
		return false;
	}

	/**
	 * Saves a serializableLabelBean into the database
	 * @param serializableLabelBean
	 * @param matchesMap
	 * @return
	 */
	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean,
			Map<String, Map<Integer, Integer>> matchesMap) {
		TPersonBean personBean = (TPersonBean)serializableLabelBean;
		//deactivate the newly created persons
		personBean.setDeleted(BooleanFields.TRUE_VALUE);
		Integer departmentID = personBean.getDepartmentID();
		if (departmentID!=null) {
			Map<Integer, Integer> departmentMap =
				matchesMap.get(ExchangeFieldNames.DEPARTMENT);
			personBean.setDepartmentID(departmentMap.get(departmentID));
		}
		Integer priorityLevel = personBean.getEmailRemindPriorityLevel();
		if (priorityLevel!=null) {
			Map<Integer, Integer> priorityMap =
				matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_PRIORITY, null));
			personBean.setEmailRemindPriorityLevel(priorityMap.get(priorityLevel));
		}
		Integer severityLevel = personBean.getEmailRemindSeverityLevel();
		if (severityLevel!=null) {
			Map<Integer, Integer> severityMap =
				matchesMap.get(MergeUtil.mergeKey(SystemFields.INTEGER_SEVERITY, null));
			personBean.setEmailRemindSeverityLevel(severityMap.get(severityLevel));
		}
		return PersonBL.save((TPersonBean)serializableLabelBean);
	}

	/**
	 * Returns true if the specified password matches the one of this
	 * object.
	 * @param ppassword the password to check for
	 * @return true if the password is o.k., false if not
	 */
	public boolean authenticate(String ppassword)
			throws Exception {
		if (ppassword == null) {
			ppassword = "";
		}
		boolean userIsOK = false;
		TpPasswordEncoder enc = new TpPasswordEncoder();
		if (ApplicationBean.getInstance().getSiteBean().getIsLDAPOnBool() && this.isLdapUser()) {
			userIsOK = LdapUtil.authenticate(this.getLoginName().trim(),
					ppassword);
		}
		else {
			userIsOK = enc.isPasswordValid(this.getPasswd(), ppassword, this.getSalt());
		}
		return userIsOK;
	}
}
