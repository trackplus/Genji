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


package com.aurel.track.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.SizeLimitExceededException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.PagedResultsControl;
import javax.naming.ldap.PagedResultsResponseControl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aurel.track.admin.user.person.PersonBL;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TSiteBean;
import com.aurel.track.dbase.HandleHome;
import com.aurel.track.prop.ApplicationBean;

public class LdapUtil {

	public static interface LDAP_CONFIG {
		// enable automatic (quartz job based) synchronization with LDAP
		static String ENABLE_AUTOMATIC_SYNC = "enabled";
		// enable user synchronization with LDAP
		static String ENABLE_USER_SYNC = "enabledUserSync";
		// enable group and group users synchronization with LDAP
		static String ENABLE_GROUP_SYNC = "enabledGroupSync";

		// baseDN for group synchronization
		static String BASE_DN_FOR_GROUP = "baseDnGroup";

		// deactivate users unknown in LDAP
		static String DEACTIVATE_UNKNOWN = "deactivateUnknown";

		// LDAP filter expression for users
		static String LDAP_FILTER_PERSONS = "ldapFilter";
		// LDAP filter expression for groups
		static String LDAP_FILTER_GROUPS = "ldapFilterGroups";
		// first name attribute in LDAP: defaults to gn
		static String FIRST_NAME = "firstName";
		// last name attribute in LDAP: defaults to sn
		static String LAST_NAME = "lastName";
		// e-mail attribute in LDAP: defaults to mail
		static String EMAIL = "email";
		// phone attribute in LDAP: defaults to phone
		static String PHONE = "phone";

		// groupName attribute in LDAP: defaults to cn
		static String GROUP_NAME = "groupName";

		/**
		 * The attribute which specifies the members of the group
		 */
		static String GROUP_MEMBER = "groupMember";
	}

	static String DEFAULT_GROUP_MEMBER = "member";

	private static String PATH_TO_KEY_STORE;

	static {
		PATH_TO_KEY_STORE = HandleHome.getTrackplus_Home() + File.separator + "keystore" + File.separator + "trackplus.ks";
	}

	private final static Logger LOGGER = LogManager.getLogger(LdapUtil.class);
	private static HashMap<String, String> ldapMap = new HashMap<String, String>();

	/**
	 * Gets the base URL from a complete URL (possibly with dn)
	 * 
	 * @param ldapServerURL
	 * @return
	 */
	public static String getBaseURL(String ldapServerURL) {
		String baseURL = null;
		URI uri = null;
		try {
			uri = new URI(ldapServerURL);
		} catch (URISyntaxException e) {
			LOGGER.warn("Creating an URI from " + ldapServerURL + " failed with " + e.getMessage());
		}
		if (uri != null) {
			// uri.get
			baseURL = uri.getScheme() + "://" + uri.getHost();
			int port = uri.getPort();
			if (port>0) {
				baseURL+= ":" + uri.getPort();
			}
			baseURL+="/";
			LOGGER.debug("Base url from " + ldapServerURL + " is " + baseURL);
		}
		return baseURL;
	}

	/**
	 * Returns true if the specified password can be authenticated against
	 * Ldap-Server
	 * 
	 * @param loginName
	 *            the loginName of the user
	 * @param ppassword
	 *            the password to check for
	 * @return true if the password is o.k., false if not
	 */
	public static boolean authenticate(String loginName, String ppassword) {
		TSiteBean siteBean = ApplicationBean.getInstance().getSiteBean();
		try {
			return authenticate(siteBean, loginName, ppassword);
		} catch (NamingException e) {
			LOGGER.debug("authenticate failed with " + e);
			return false;
		}
	}

	public static boolean authenticate(TSiteBean siteBean, String loginName, String ppassword) throws NamingException {
		boolean userIsOK = false;
		ArrayList<String> trace = new ArrayList<String>();

		trace.add("Ldap trying to authenticate user with loginname >" + loginName + "<");

		if (siteBean.getLdapServerURL().startsWith("ldaps:")) {
			System.setProperty("javax.net.ssl.trustStore", PATH_TO_KEY_STORE);
		}
		// get the CN
		String keyDn = getCn(siteBean, loginName);

		try {
			if (keyDn != null) {
				trace.add("Using keyDn >" + keyDn + "<");
				// Set up the environment for creating the initial context
				Hashtable<String, String> env = new Hashtable<String, String>(11);
				env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
				env.put(Context.PROVIDER_URL, siteBean.getLdapServerURL());
				env.put(Context.SECURITY_AUTHENTICATION, "simple");
				env.put(Context.SECURITY_PRINCIPAL, keyDn);
				env.put(Context.SECURITY_CREDENTIALS, ppassword);
				// Create initial context
				DirContext itest = new InitialDirContext(env);
				itest.close();
				// user was validated
				userIsOK = true;
			}
			return userIsOK;
		} catch (NamingException e) {
			for (String msg : trace) {
				LOGGER.warn(msg);
			}
			throw e;
		}
	}

	private static String getCn(String loginName) {
		TSiteBean siteBean = ApplicationBean.getInstance().getSiteBean();
		try {
			return getCn(siteBean, loginName);
		} catch (NamingException e) {
			LOGGER.debug("get common name (CN) for >" + loginName + "< failed with " + e);
			return null;
		}
	}

	/**
	 * Returns the CN (common name) for a given login name
	 * 
	 * @param loginName
	 *            the loginName of the user
	 * @return CN as a String(if found), or null (else)
	 */
	private static String getCn(TSiteBean siteBean, String loginName) throws NamingException {
		String keyDn = null;
		DirContext ctx = getInitialContext(siteBean.getLdapServerURL(), siteBean.getLdapBindDN(), siteBean.getLdapBindPassword());
		if (ctx != null) {
			SearchControls ctls = new SearchControls();
			ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			// Search for the user-id
			String searchStr = "(" + siteBean.getLdapAttributeLoginName() + "=" + loginName + ")";
			NamingEnumeration<SearchResult> answer = ctx.search("", searchStr, ctls);
			if (answer.hasMore()) {
				// retrieve the CN
				SearchResult sr = answer.next();
				keyDn = sr.getName();// + "," + ctx.getNameInNamespace();
				LOGGER.debug("Name = " + keyDn);
				String nameInNamespace = ctx.getNameInNamespace();
				LOGGER.debug("Name in namespace " + nameInNamespace);
				if (nameInNamespace!=null && nameInNamespace.trim().length()>0) {
					keyDn += "," + ctx.getNameInNamespace();
				}
				LOGGER.debug("entry found for LDAP-search >" + searchStr + "<: dn= >" + keyDn + "<!");
				answer.close(); // wo don't need more answers
			} else {
				LOGGER.debug("no entry found for LDAP-search >" + searchStr + "<!");
			}
			ctx.close();
		}
		return keyDn;
	}

	/**
	 * Gets the initial context
	 * 
	 * @param providerUrl
	 * @param bindDN
	 * @param bindPassword
	 * @return
	 */
	public static LdapContext getInitialContext(String providerUrl, String bindDN, String bindPassword) {
		List<String> trace = new ArrayList<String>();
		LOGGER.debug("providerURL: " + providerUrl);
		trace.add("Attempting to connect to the LDAP server...");
		if (providerUrl != null && providerUrl.startsWith("ldaps:")) {
			System.setProperty("javax.net.ssl.trustStore", PATH_TO_KEY_STORE);
			trace.add("Using ldaps: with keystore at " + PATH_TO_KEY_STORE);
			File ks = new File(PATH_TO_KEY_STORE);
			if (!ks.exists()) {
				trace.add("*** There is no keystore at " + PATH_TO_KEY_STORE);
			}
		}
		if (providerUrl == null) {
			LOGGER.warn("LDAP provider URL should not be null.");
			return null;
		}
		Hashtable<String, Object> env = new Hashtable<String, Object>();
		if (LOGGER.isDebugEnabled()) {
			env.put("com.sun.jndi.ldape.trace.ber", System.err);
		}
		env.put("java.naming.ldap.version", "3" );
		env.put( "com.sun.jndi.ldap.connect.timeout", "10000" );
		env.put( "com.sun.jndi.dns.timeout.initial", "2000" );
		env.put( "com.sun.jndi.dns.timeout.retries", "3" );
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, providerUrl);
		if ((bindDN != null) && !bindDN.equals("")) {
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, bindDN);
			env.put(Context.SECURITY_CREDENTIALS, bindPassword);
			LOGGER.debug("bind with bindDN:" + bindDN + " " + "bindPassword=" + bindPassword.replaceAll(".", "*"));
			trace.add("Preparing to bind to the LDAP server with DN = " + bindDN + " and password '****");
		} else {
			LOGGER.debug("bind anonymous");
			trace.add("Preparing to bind anonymously to the LDAP server");
		}
		try {
			return new InitialLdapContext(env, null);
		} catch (NamingException e) {
			for (String msg : trace) {
				LOGGER.error(msg);
			}
			LOGGER.error("Getting the initial ldap context failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
			try {
				new InitialDirContext(env);
			} catch (NamingException e1) {
				LOGGER.error("Getting the initial dir context failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
			return null;
		}
	}

	/**
	 * Gets a personBean from LDAP
	 * 
	 * @param searchResult
	 * @param loginAttributeName
	 * @param firstNameAttributeName
	 * @param lastNameAttributName
	 * @param emailAttributeName
	 * @param phoneAttributName
	 * @return
	 */
	public static TPersonBean getPersonBean(SearchResult searchResult, String loginAttributeName, String firstNameAttributeName, String lastNameAttributName,
			String emailAttributeName, String phoneAttributName) {
		Attributes attributes = searchResult.getAttributes();
		if (attributes == null) {
			LOGGER.warn("No attributes found in LDAP search result " + searchResult.getName());
			return null;
		}
		TPersonBean personBean = new TPersonBean();
		try {
			Attribute loginAttribute = attributes.get(loginAttributeName);
			if (loginAttribute != null) {
				String loginName = (String) loginAttribute.get();
				LOGGER.debug("Loginname: " + loginName);
				if (loginName == null || "".equals(loginName)) {
					LOGGER.info("No value for loginame attribute " + loginAttributeName);
					return null;
				} else {
					// loginname is mandatory for person
					personBean.setLoginName(loginName);
				}
			} else {
				LOGGER.info("No loginame attribute " + loginAttributeName);
				return null;
			}
			Attribute emailAttribute = attributes.get(emailAttributeName);
			if (emailAttribute != null) {
				String email = (String) emailAttribute.get();
				LOGGER.debug("E-mail: " + email);
				if (email == null || "".equals(email)) {
					LOGGER.info("No value for e-mail attribute " + emailAttributeName);
					// e-mail is mandatory for person
					return null;
				} else {
					personBean.setEmail(email);
				}
			} else {
				LOGGER.info("No e-mail attribute " + emailAttributeName);
				return null;
			}
			Attribute firstNameAttribute = attributes.get(firstNameAttributeName);
			if (firstNameAttribute != null) {
				String firstName = (String) firstNameAttribute.get();
				LOGGER.debug("Firstname: " + firstName);
				personBean.setFirstName(firstName);
			}
			Attribute lastNameAttribute = attributes.get(lastNameAttributName);
			if (lastNameAttribute != null) {
				String lastName = (String) lastNameAttribute.get();
				LOGGER.debug("Lastname: " + lastName);
				if (lastName == null || "".equals(lastName)) {
					LOGGER.info("No value for lastname attribute " + lastNameAttributName);
					// lastname is mandatory for person
					return null;
				} else {
					personBean.setLastName(lastName);
				}
			}
			if (phoneAttributName != null) {
				Attribute phoneAttribute = attributes.get(phoneAttributName);
				if (phoneAttribute != null) {
					String phone = (String) phoneAttribute.get();
					LOGGER.debug("Phone: " + phone);
					personBean.setPhone(phone);
				}
			}
			LOGGER.debug("LDAP entry cn: " + (String) attributes.get("cn").get());
			LOGGER.debug("Processed " + personBean.getLoginName() + " (" + personBean.getFirstName() + " " + personBean.getLastName() + ")");
		} catch (Exception e) {
			LOGGER.warn("Problem setting attributes from LDAP: " + e.getMessage());
			LOGGER.warn("This is probably a configuration error in the LDAP mapping section of quartz-jobs.xml");
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		}
		return personBean;
	}

	/**
	 * Get all ldap groups
	 * 
	 * @param siteBean
	 * @param baseDnGroup
	 * @param ldapFilterGroups
	 * @param groupAttributeName
	 * @param groupToMemberReferencesMap
	 * @return
	 * @throws Exception
	 */
	public static Map<String, TPersonBean> getLdapGroupsByList(String baseURL, TSiteBean siteBean, String groupAttributeName,
			Map<String, List<String>> groupToMemberReferencesMap, Map<String, String> groups) throws Exception {
		HashMap<String, TPersonBean> ldapGroupsMap = new HashMap<String, TPersonBean>();
		String bindDN = siteBean.getLdapBindDN();
		String bindPassword = siteBean.getLdapBindPassword();
		String groupMemberAttributName = ldapMap.get(LDAP_CONFIG.GROUP_MEMBER);
		if (groupMemberAttributName == null) {
			LOGGER.debug("No groupMember attribute defined in quartz-jobs.xml. Fall back to " + DEFAULT_GROUP_MEMBER);
			groupMemberAttributName = DEFAULT_GROUP_MEMBER;
		}
		LdapContext baseContext = getInitialContext(baseURL, bindDN, bindPassword);
		if (baseContext == null) {
			LOGGER.warn("Context is null for baseURL " + baseURL);
			return ldapGroupsMap;
		}
		for (Map.Entry<String, String> groupEntry : groups.entrySet()) {
			String groupName = groupEntry.getKey();
			String groupDN = groupEntry.getValue();
			int index = groupDN.indexOf(",");
			if (index != -1) {
				String searchPart = groupDN.substring(0, index);
				String searchStr = "(" + searchPart + ")";
				String parentDNPart = groupDN.substring(index + 1);
				LdapContext context = (LdapContext) baseContext.lookup(parentDNPart);
				if (context == null) {
					LOGGER.warn("Context is null after lookup for " + parentDNPart);
					continue;
				}
				int recordCount = 0;
				SearchControls ctls = null;
				try {
					// Activate paged results
					int pageSize = 5;
					byte[] cookie = null;
					context.setRequestControls(new Control[] { new PagedResultsControl(pageSize, Control.NONCRITICAL) });
					int total;
					// Control the search
					ctls = new SearchControls();
					ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
					ctls.setCountLimit(
							(ApplicationBean.getInstance().getMaxNumberOfFullUsers() + ApplicationBean.getInstance().getMaxNumberOfLimitedUsers())
									* 3 + 10); // Don't ask for more than we can
												// handle anyways
					do {
						/* perform the search */
						NamingEnumeration<SearchResult> results = context.search("", searchStr, ctls);
						/*
						 * for each entry print out name + all attrs and values
						 */
						while (results != null && results.hasMore()) {
							SearchResult searchResult = (SearchResult) results.next();
							// Attributes atrs = sr.getAttributes();
							Attributes attributes = searchResult.getAttributes();
							if (attributes == null) {
								LOGGER.warn("No attributes found in LDAP search result " + searchResult.getName());
								continue;
							}
							TPersonBean personBean = new TPersonBean();
							try {
								personBean.setLoginName(groupName);
								ldapGroupsMap.put(personBean.getLoginName(), personBean);
								Attribute memberAttribute = attributes.get(groupMemberAttributName);
								if (memberAttribute != null) {
									NamingEnumeration<?> members = memberAttribute.getAll();
									while (members != null && members.hasMore()) {
										String memberSearchResult = (String) members.next();
										List<String> memberDNList = groupToMemberReferencesMap.get(groupName);
										if (memberDNList == null) {
											memberDNList = new ArrayList<String>();
											groupToMemberReferencesMap.put(groupName, memberDNList);
										}
										LOGGER.debug("Member found: " + memberSearchResult);
										memberDNList.add(memberSearchResult);
									}
								} else {
									LOGGER.info("Could not find value(s) for group member attribute " + groupMemberAttributName + " for group " + groupName);
								}
								LOGGER.debug("LDAP entry cn: " + (String) attributes.get("cn").get());
								LOGGER.debug("Processed group " + groupName);
							} catch (Exception e) {
								LOGGER.warn("Problem setting attributes from LDAP: " + e.getMessage());
								LOGGER.warn("This is probably a configuration error in the LDAP mapping section of quartz-jobs.xml");
								if (LOGGER.isDebugEnabled()) {
									LOGGER.debug("Stack trace:", e);
								}
							}
							++recordCount;
						}
						// Examine the paged results control response
						Control[] controls = context.getResponseControls();
						if (controls != null) {
							for (int i = 0; i < controls.length; i++) {
								if (controls[i] instanceof PagedResultsResponseControl) {
									PagedResultsResponseControl prrc = (PagedResultsResponseControl) controls[i];
									total = prrc.getResultSize();
									if (total != 0) {
										LOGGER.debug("***************** END-OF-PAGE " + "(total : " + total + ") *****************\n");
									} else {
										LOGGER.debug("***************** END-OF-PAGE " + "(total: unknown) ***************\n");
									}
									cookie = prrc.getCookie();
								}
							}
						} else {
							LOGGER.debug("No controls were sent from the server");
						}
						// Re-activate paged results
						context.setRequestControls(new Control[] { new PagedResultsControl(pageSize, cookie, Control.CRITICAL) });

					} while (cookie != null);
				} catch (SizeLimitExceededException sle) {
					if (recordCount < ctls.getCountLimit()) {
						LOGGER.error("Searching LDAP asked for more entries than permitted by the LDAP server.");
						LOGGER.error("Size limit exceeded error occurred after record " + recordCount + " with " + sle.getMessage());
						LOGGER.error("You have to ask your LDAP server admin to increase the limit or specify a more suitable search base or filter.");
					} else {
						LOGGER.error("Searching LDAP asked for more entries than permitted by the Genji server (" + recordCount + ").");
						LOGGER.error("You have to get more user licenses for Genji or specify a more suitable search base or filter.");
					}
					LOGGER.error("The LDAP synchronization is most likely incomplete.");
				} catch (NamingException e) {
					LOGGER.error("PagedSearch failed.");
					LOGGER.debug(ExceptionUtils.getStackTrace(e));
				} catch (IOException ie) {
					LOGGER.error("PagedSearch failed.");
					LOGGER.debug(ExceptionUtils.getStackTrace(ie));
				} finally {
					context.close();
				}
			}
		}
		return ldapGroupsMap;
	}

	/**
	 * Get all ldap groups
	 * 
	 * @param siteBean
	 * @param baseDnGroup
	 * @param ldapFilterGroups
	 * @param groupAttributeName
	 * @param groupToMemberReferencesMap
	 * @return
	 * @throws Exception
	 */
	public static Map<String, TPersonBean> getLdapGroupsPaged(String baseURL, TSiteBean siteBean, String baseDnGroup, String ldapFilterGroups,
			String groupAttributeName, Map<String, List<String>> groupToMemberReferencesMap) throws Exception {
		if (ldapFilterGroups == null || "".equals(ldapFilterGroups) || "*".equals(ldapFilterGroups)) {
			ldapFilterGroups = "(" + groupAttributeName + "=*)";
		}
		String bindDN = siteBean.getLdapBindDN();
		String bindPassword = siteBean.getLdapBindPassword();
		LdapContext context = getInitialContext(baseURL + baseDnGroup, bindDN, bindPassword);
		HashMap<String, TPersonBean> ldapGroupsMap = new HashMap<String, TPersonBean>();
		if (context == null) {
			LOGGER.warn("Context is null");
			return ldapGroupsMap;
		}
		int recordCount = 0;
		SearchControls ctls = null;
		String groupMemberAttributName = ldapMap.get(LDAP_CONFIG.GROUP_MEMBER);
		if (groupMemberAttributName == null) {
			groupMemberAttributName = DEFAULT_GROUP_MEMBER;
		}
		try {
			// Activate paged results
			int pageSize = 5;
			byte[] cookie = null;
			context.setRequestControls(new Control[] { new PagedResultsControl(pageSize, Control.NONCRITICAL) });
			int total;
			// Control the search
			ctls = new SearchControls();
			ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			ctls.setCountLimit(
					(ApplicationBean.getInstance().getMaxNumberOfFullUsers() + ApplicationBean.getInstance().getMaxNumberOfLimitedUsers()) * 3
							+ 10); // Don't ask for more than we can handle
									// anyways
			do {
				/* perform the search */
				NamingEnumeration<SearchResult> results = context.search("", ldapFilterGroups, ctls);
				/* for each entry print out name + all attrs and values */
				while (results != null && results.hasMore()) {
					SearchResult searchResult = (SearchResult) results.next();
					// Attributes atrs = sr.getAttributes();
					Attributes attributes = searchResult.getAttributes();
					if (attributes == null) {
						LOGGER.warn("No attributes found in LDAP search result " + searchResult.getName());
						return null;
					}
					TPersonBean personBean = new TPersonBean();
					try {
						Attribute groupNameAttribute = attributes.get(groupAttributeName);
						if (groupNameAttribute != null) {
							String groupName = (String) groupNameAttribute.get();
							LOGGER.debug("Groupname: " + groupName);
							if (groupName == null || "".equals(groupName)) {
								LOGGER.info("No value for group name attribute " + groupAttributeName);
								return null;
							} else {
								personBean.setLoginName(groupName);
								ldapGroupsMap.put(personBean.getLoginName(), personBean);
							}
							Attribute memberAttribute = attributes.get(groupMemberAttributName);
							if (memberAttribute != null) {
								NamingEnumeration<?> members = memberAttribute.getAll();
								while (members != null && members.hasMore()) {
									String memberSearchResult = (String) members.next();
									List<String> memberDNList = groupToMemberReferencesMap.get(groupName);
									if (memberDNList == null) {
										memberDNList = new ArrayList<String>();
										groupToMemberReferencesMap.put(groupName, memberDNList);
									}
									memberDNList.add(memberSearchResult);
								}
							} else {
								LOGGER.info("Could not find value(s) for group member attribute " + groupMemberAttributName + " for group " + groupName);
							}
						}
						LOGGER.debug("LDAP entry cn: " + (String) attributes.get("cn").get());
						LOGGER.debug("Processed " + personBean.getLoginName() + " (" + personBean.getFirstName() + " " + personBean.getLastName() + ")");
					} catch (Exception e) {
						LOGGER.warn("Problem setting attributes from LDAP: " + e.getMessage());
						LOGGER.warn("This is probably a configuration error in the LDAP mapping section of quartz-jobs.xml");
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("Stack trace:", e);
						}
					}
					++recordCount;
				}
				// Examine the paged results control response
				Control[] controls = context.getResponseControls();
				if (controls != null) {
					for (int i = 0; i < controls.length; i++) {
						if (controls[i] instanceof PagedResultsResponseControl) {
							PagedResultsResponseControl prrc = (PagedResultsResponseControl) controls[i];
							total = prrc.getResultSize();
							if (total != 0) {
								LOGGER.debug("***************** END-OF-PAGE " + "(total : " + total + ") *****************\n");
							} else {
								LOGGER.debug("***************** END-OF-PAGE " + "(total: unknown) ***************\n");
							}
							cookie = prrc.getCookie();
						}
					}
				} else {
					LOGGER.debug("No controls were sent from the server");
				}
				// Re-activate paged results
				context.setRequestControls(new Control[] { new PagedResultsControl(pageSize, cookie, Control.CRITICAL) });

			} while (cookie != null);
		} catch (SizeLimitExceededException sle) {
			if (recordCount < ctls.getCountLimit()) {
				LOGGER.error("Searching LDAP asked for more entries than permitted by the LDAP server.");
				LOGGER.error("Size limit exceeded error occurred after record " + recordCount + " with " + sle.getMessage());
				LOGGER.error("You have to ask your LDAP server admin to increase the limit or specify a more suitable search base or filter.");
			} else {
				LOGGER.error("Searching LDAP asked for more entries than permitted by the Genji server (" + recordCount + ").");
				LOGGER.error("You have to get more user licenses for Genji or specify a more suitable search base or filter.");
			}
			LOGGER.error("The LDAP synchronization is most likely incomplete.");
		} catch (NamingException e) {
			LOGGER.error("PagedSearch failed.");
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} catch (IOException ie) {
			LOGGER.error("PagedSearch failed.");
			LOGGER.debug(ExceptionUtils.getStackTrace(ie));
		} finally {
			context.close();
		}
		return ldapGroupsMap;
	}

	/**
	 * Gets the ldap persons for ldap groups based on the person DNs
	 * 
	 * @param siteBean
	 * @param groups
	 * @param groupToMemberReferencesMap
	 * @return
	 */
	public static Map<String, List<TPersonBean>> getGroupToPersonMaps(String baseURL, TSiteBean siteBean, Map<String, TPersonBean> groups,
			Map<String, List<String>> groupToMemberReferencesMap) {
		String loginAttributeName = siteBean.getLdapAttributeLoginName();
		String bindDN = siteBean.getLdapBindDN();
		String bindPassword = siteBean.getLdapBindPassword();
		Map<String, String> ldapMap = getLdapMap();
		String personFilter = ldapMap.get(LdapUtil.LDAP_CONFIG.LDAP_FILTER_PERSONS);
		boolean hasPersonFilter = false;
		if (personFilter != null && !"".equals(personFilter) && !"*".equals(personFilter)) {
			hasPersonFilter = true;
			if (!(personFilter.startsWith("(") && personFilter.endsWith(")"))) {
				personFilter = "(" + personFilter + ")";
			}
			LOGGER.debug("General user filter expression " + personFilter);
		}
		Map<String, List<TPersonBean>> groupToPersons = new HashMap<String, List<TPersonBean>>();
		if (groupToMemberReferencesMap != null) {
			for (Map.Entry<String, List<String>> groupPersons : groupToMemberReferencesMap.entrySet()) {
				String groupName = groupPersons.getKey();
				List<String> personDNs = groupPersons.getValue();
				if (personDNs != null) {
					LOGGER.debug("Getting the persons for group " + groupName);
					List<TPersonBean> personList = new ArrayList<TPersonBean>();
					groupToPersons.put(groupName, personList);
					for (String personDN : personDNs) {
						int index = personDN.indexOf(",");
						if (index != -1) {
							String searchPart = personDN.substring(0, index);
							String searchStr = "(" + searchPart + ")";
							if (hasPersonFilter) {
								searchStr = "(&" + personFilter + searchStr + ")";
							}
							LOGGER.debug("Search string " + searchStr);
							String parentDNPart = personDN.substring(index + 1);
							try {
								TPersonBean personBean = getLdapUser(baseURL + parentDNPart, bindDN, bindPassword, loginAttributeName, searchStr);
								if (personBean != null) {
									personList.add(personBean);
									LOGGER.debug("Person with dn " + personDN + " found as member of group " + groupName);
								}
							} catch (Exception e) {
								LOGGER.warn("Getting the person " + personDN + " for group " + groupName + " failed with " + e.getMessage());
								LOGGER.debug(ExceptionUtils.getStackTrace(e));
							}
						}
					}
				}
			}
		}
		return groupToPersons;
	}

	/**
	 * Gets the ldap persons for ldap groups based on the person DNs
	 * 
	 * @param siteBean
	 * @param groups
	 * @param groupToMemberReferencesMap
	 * @return
	 */
	public static Map<String, List<TPersonBean>> getGroupToPersonMaps1(String baseURL, TSiteBean siteBean, Map<String, TPersonBean> groups,
			Map<String, List<String>> groupToMemberReferencesMap) {
		String loginAttributeName = siteBean.getLdapAttributeLoginName();
		String bindDN = siteBean.getLdapBindDN();
		String bindPassword = siteBean.getLdapBindPassword();
		Map<String, String> ldapMap = getLdapMap();
		String personFilter = ldapMap.get(LdapUtil.LDAP_CONFIG.LDAP_FILTER_PERSONS);
		boolean hasPersonFilter = false;
		if (personFilter != null && !"".equals(personFilter) && !"*".equals(personFilter)) {
			hasPersonFilter = true;
			if (!(personFilter.startsWith("(") && personFilter.endsWith(")"))) {
				personFilter = "(" + personFilter + ")";
			}
			LOGGER.debug("General user filter expression " + personFilter);
		}
		Map<String, List<TPersonBean>> groupToPersons = new HashMap<String, List<TPersonBean>>();
		if (groupToMemberReferencesMap != null) {
			for (Map.Entry<String, List<String>> groupPersons : groupToMemberReferencesMap.entrySet()) {
				String groupName = groupPersons.getKey();
				List<String> personDNs = groupPersons.getValue();
				Map<String, List<String>> parentDNPartToPersonSearchStrings = new HashMap<String, List<String>>();
				if (personDNs != null) {
					LOGGER.debug("Getting the persons for group " + groupName);
					for (String personDN : personDNs) {
						int index = personDN.indexOf(",");
						if (index != -1) {
							String searchPart = personDN.substring(0, index);
							String searchStr = "(" + searchPart + ")";
							if (hasPersonFilter) {
								searchStr = "(&" + personFilter + searchStr + ")";
							}
							String parentDNPart = personDN.substring(index + 1);
							List<String> personSearchStrings = parentDNPartToPersonSearchStrings.get(parentDNPart);
							if (personSearchStrings == null) {
								personSearchStrings = new LinkedList<String>();
								parentDNPartToPersonSearchStrings.put(parentDNPart, personSearchStrings);
							}
							personSearchStrings.add(searchStr);
						}
					}
				}
				List<TPersonBean> personsInGroup = new LinkedList<TPersonBean>();
				LdapContext baseContext = getInitialContext(baseURL, bindDN, bindPassword);
				if (baseContext == null) {
					LOGGER.warn("Context is null for base url " + baseURL);
					continue;
				}
				for (Map.Entry<String, List<String>> parentDnToPersonSearchStrings : parentDNPartToPersonSearchStrings.entrySet()) {
					String parentDn = parentDnToPersonSearchStrings.getKey();
					List<String> searchStrs = parentDnToPersonSearchStrings.getValue();
					if (searchStrs == null || searchStrs.isEmpty()) {
						continue;
					}
					LdapContext ldapContext = null;
					try {
						LOGGER.debug("Number of persons searched for parentDn " + parentDn + ": " + searchStrs.size());
						ldapContext = (LdapContext) baseContext.lookup(parentDn);
						if (ldapContext == null) {
							LOGGER.warn("Lookup context is null for " + parentDn);
							continue;
						}
						List<TPersonBean> personsInParentDN = getLdapUsers(ldapContext, loginAttributeName, searchStrs);
						if (personsInParentDN != null) {
							LOGGER.debug("Number of persons found for parentDn " + parentDn + ": " + personsInParentDN.size());
							personsInGroup.addAll(personsInParentDN);
						}
					} catch (Exception e) {
						LOGGER.warn("Getting the persons with parentDn  " + parentDn + " for group " + groupName + " failed with " + e.getMessage());
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					} finally {
						try {
							if (ldapContext != null) {
								ldapContext.close();
							}
						} catch (NamingException e) {
							LOGGER.warn("Closing the context with " + parentDn + " for group " + groupName + " failed with " + e.getMessage());
							LOGGER.debug(ExceptionUtils.getStackTrace(e));
						}
					}
				}
				groupToPersons.put(groupName, personsInGroup);
				if (baseContext!=null) {
					try {
						baseContext.close();
					} catch (NamingException e) {
						LOGGER.warn("Closing the base context with failed with " + e.getMessage());
						LOGGER.debug(ExceptionUtils.getStackTrace(e));
					}
				}
			}
		}
		return groupToPersons;
	}

	/**
	 * Returns a HashMap <login name, TPersonBean> for all LDAP objects found in
	 * the directory und the DN configured in the Genji server configuration.
	 * 
	 * @return Map with <login name, TPersonBean>
	 */
	public static HashMap<String, TPersonBean> getAllLdapPersonsPaged(TSiteBean siteBean, String filter) throws Exception {
		if (filter == null || "".equals(filter) || "*".equals(filter)) {
			filter = siteBean.getLdapAttributeLoginName() + "=*";
		}
		if (!(filter.startsWith("(") && filter.endsWith(")"))) {
			filter = "(" + filter + ")";
		}
		LOGGER.debug("User filter expression " + filter);
		String bindDN = siteBean.getLdapBindDN();
		String bindPassword = siteBean.getLdapBindPassword();
		HashMap<String, TPersonBean> ldapPersonsMap = new HashMap<String, TPersonBean>();
		LdapContext context = getInitialContext(siteBean.getLdapServerURL(), bindDN, bindPassword);
		if (context == null) {
			return ldapPersonsMap;
		}
		int recordCount = 0;
		// Create initial context
		// Control the search
		SearchControls ctls = null;
		try {
			// Activate paged results
			int pageSize = 5;
			byte[] cookie = null;
			context.setRequestControls(new Control[] { new PagedResultsControl(pageSize, Control.NONCRITICAL) });
			int total;
			// Control the search
			ctls = new SearchControls();
			ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			ctls.setCountLimit(
					(ApplicationBean.getInstance().getMaxNumberOfFullUsers() + ApplicationBean.getInstance().getMaxNumberOfLimitedUsers()) * 3
							+ 10); // Don't ask for more than we can handle
									// anyways
			if (ldapMap == null || ldapMap.isEmpty()) {
				LOGGER.error("There is no LDAP mapping in quartz-jobs.xml. Please provide!");
				return null;
			}
			String firstNameAttributeName = ldapMap.get(LdapUtil.LDAP_CONFIG.FIRST_NAME);
			String lastNameAttributName = ldapMap.get(LdapUtil.LDAP_CONFIG.LAST_NAME);
			String emailAttributeName = ldapMap.get(LdapUtil.LDAP_CONFIG.EMAIL);
			String phoneAttributName = ldapMap.get(LdapUtil.LDAP_CONFIG.PHONE);
			String loginAttributeName = siteBean.getLdapAttributeLoginName();
			do {
				/* perform the search */
				NamingEnumeration<SearchResult> results = context.search("", filter, ctls);
				/* for each entry print out name + all attrs and values */
				while (results != null && results.hasMore()) {
					SearchResult sr = (SearchResult) results.next();
					// Attributes atrs = sr.getAttributes();
					TPersonBean personBean = getPersonBean(sr, loginAttributeName, firstNameAttributeName, lastNameAttributName, emailAttributeName,
							phoneAttributName);
					if (personBean != null) {
						ldapPersonsMap.put(personBean.getLoginName(), personBean);
					}
					++recordCount;
				}
				// Examine the paged results control response
				Control[] controls = context.getResponseControls();
				if (controls != null) {
					for (int i = 0; i < controls.length; i++) {
						if (controls[i] instanceof PagedResultsResponseControl) {
							PagedResultsResponseControl prrc = (PagedResultsResponseControl) controls[i];
							total = prrc.getResultSize();
							if (total != 0) {
								LOGGER.debug("***************** END-OF-PAGE " + "(total : " + total + ") *****************\n");
							} else {
								LOGGER.debug("***************** END-OF-PAGE " + "(total: unknown) ***************\n");
							}
							cookie = prrc.getCookie();
						}
					}
				} else {
					LOGGER.debug("No controls were sent from the server");
				}
				// Re-activate paged results
				context.setRequestControls(new Control[] { new PagedResultsControl(pageSize, cookie, Control.CRITICAL) });

			} while (cookie != null);
		} catch (SizeLimitExceededException sle) {
			if (recordCount < ctls.getCountLimit()) {
				LOGGER.error("Searching LDAP asked for more entries than permitted by the LDAP server.");
				LOGGER.error("Size limit exceeded error occurred after record " + recordCount + " with " + sle.getMessage());
				LOGGER.error("You have to ask your LDAP server admin to increase the limit or specify a more suitable search base or filter.");
			} else {
				LOGGER.error("Searching LDAP asked for more entries than permitted by the Genji server (" + recordCount + ").");
				LOGGER.error("You have to get more user licenses for Genji or specify a more suitable search base or filter.");
			}
			LOGGER.error("The LDAP synchronization is most likely incomplete.");
		} catch (NamingException e) {
			LOGGER.error("PagedSearch failed.");
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} catch (IOException ie) {
			LOGGER.error("PagedSearch failed.");
			LOGGER.debug(ExceptionUtils.getStackTrace(ie));
		} finally {
			if (context != null) {
				context.close();
			}
		}
		return ldapPersonsMap;
	}

	/**
	 * Check if a given login name can be found on the Ldap-server
	 * 
	 * @param loginName
	 *            , the loginName of the user
	 * @return true, if loginname is found
	 */
	public static boolean isOnLdapServer(String loginName) {
		return getCn(loginName) != null;
	}

	/**
	 * Set the mapping table for mapping between LDAP directory attributes and
	 * Track user database attributes.
	 * 
	 * @param theMap
	 */
	public static void setLdapMap(HashMap theMap) {
		ldapMap = theMap;
	}

	/**
	 * Set the mapping table for mapping between LDAP directory attributes and
	 * Track user database attributes.
	 * 
	 * @param theMap
	 */
	public static Map getLdapMap() {
		return ldapMap;
	}

	static TPersonBean getLdapUser(String providerUrl, String bindDN, String bindPassword, String loginAttributeName, String searchStr) throws Exception {
		LdapContext ctx = null;
		try {
			ctx = getInitialContext(providerUrl, bindDN, bindPassword);
			if (ctx == null) {
				LOGGER.warn("The context is null");
			}
			// Control the search
			SearchControls ctls = new SearchControls();
			ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			// Don't ask for more than we can handle anyways
			if (ldapMap == null || ldapMap.isEmpty()) {
				LOGGER.error("There is no LDAP mapping in quartz-jobs.xml. Please provide!");
				return null;
			}
			String firstNameAttributeName = ldapMap.get(LdapUtil.LDAP_CONFIG.FIRST_NAME);
			String lastNameAttributName = ldapMap.get(LdapUtil.LDAP_CONFIG.LAST_NAME);
			String emailAttributeName = ldapMap.get(LdapUtil.LDAP_CONFIG.EMAIL);
			String phoneAttributName = ldapMap.get(LdapUtil.LDAP_CONFIG.PHONE);
			NamingEnumeration<SearchResult> results = ctx.search("", searchStr, ctls);
			/* for each entry print out name + all attrs and values */
			while (results != null && results.hasMore()) {
				SearchResult sr = (SearchResult) results.next();
				return getPersonBean(sr, loginAttributeName, firstNameAttributeName, lastNameAttributName, emailAttributeName, phoneAttributName);
			}
		} catch (NamingException e) {
			LOGGER.warn("Searching from " + providerUrl + " by filter " + searchStr + " failed with " + e.getMessage());
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} finally {
			if (ctx != null) {
				ctx.close();
			}
		}
		return null;
	}

	/**
	 * Gets the LDAP users
	 * 
	 * @param ctx
	 * @param loginAttributeName
	 * @param searchStrs
	 * @return
	 */
	static List<TPersonBean> getLdapUsers(LdapContext ctx, String loginAttributeName, List<String> searchStrs) {
		List<TPersonBean> personBeans = new LinkedList<TPersonBean>();
		if (ldapMap == null || ldapMap.isEmpty()) {
			LOGGER.error("There is no LDAP mapping in quartz-jobs.xml. Please provide!");
			return personBeans;
		}
		String firstNameAttributeName = ldapMap.get(LdapUtil.LDAP_CONFIG.FIRST_NAME);
		String lastNameAttributName = ldapMap.get(LdapUtil.LDAP_CONFIG.LAST_NAME);
		String emailAttributeName = ldapMap.get(LdapUtil.LDAP_CONFIG.EMAIL);
		String phoneAttributName = ldapMap.get(LdapUtil.LDAP_CONFIG.PHONE);
		for (String searchStr : searchStrs) {
			LOGGER.debug("Searching by filter " + searchStr);
			SearchControls ctls = new SearchControls();
			ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			try {
				NamingEnumeration<SearchResult> results = ctx.search("", searchStr, ctls);
				while (results != null && results.hasMore()) {
					SearchResult sr = (SearchResult) results.next();
					TPersonBean personBean = getPersonBean(sr, loginAttributeName, firstNameAttributeName, lastNameAttributName, emailAttributeName,
							phoneAttributName);
					if (personBean != null) {
						LOGGER.debug("Search successful " + searchStr);
						personBeans.add(personBean);
					}
				}
			} catch (NamingException e) {
				LOGGER.warn("Search failed with " + e.getMessage());
				LOGGER.debug(ExceptionUtils.getStackTrace(e));
			}
		}
		return personBeans;
	}

	/**
	 * Synchronizes the list of users in the Genji database with a HashMap of
	 * login names and TPersonBeans.
	 * 
	 * @param ldapPersons
	 *            a map with login names and their corresponding TPersonBeans
	 * 
	 */
	public static void synchronizeUserList(Map<String, TPersonBean> ldapPersons) throws Exception {
		List<TPersonBean> dbasePersons = PersonBL.loadPersons();
		Map<String, TPersonBean> dbMap = new HashMap<String, TPersonBean>();
		for (TPersonBean dbasePerson : dbasePersons) {
			dbMap.put(dbasePerson.getLoginName(), dbasePerson);
		}
		/* Compare all LDAP entries with Genji entries. If not found add. */
		/*
		 * If deactivated, keep deactivated. If some values changed, merge
		 * changes.
		 */
		int count = 0;
		for (String key : ldapPersons.keySet()) {
			TPersonBean ldapPerson = ldapPersons.get(key);
			String ldapLoginame = ldapPerson.getLoginName();
			String ldapFirstname = ldapPerson.getFirstName();
			String ldapLastname = ldapPerson.getLastName();
			String ldapEmail = ldapPerson.getEmail();
			String phone = ldapPerson.getPhone();
			TPersonBean dbPerson = dbMap.get(ldapLoginame);
			boolean saveNeeded = false;
			if (dbPerson != null) {
				saveNeeded = !PersonBL.isLdapPersonSame(dbPerson, ldapPerson);
				if (saveNeeded) {
					PersonBL.updateLdapPerson(dbPerson, ldapFirstname, ldapLastname, ldapEmail, phone);
					LOGGER.info("Existing user " + ldapFirstname + " " + ldapLastname + " (" + ldapEmail + ") to be changed in Genji database.");
				}
			} else {
				saveNeeded = true;
				dbPerson = PersonBL.createLdapPerson(ldapLoginame, ldapFirstname, ldapLastname, ldapEmail, phone);
				++count;
				LOGGER.info(
						"Adding user " + ldapPerson.getFirstName() + " " + ldapPerson.getLastName() + " (" + ldapPerson.getEmail() + ") to Genji database.");
			}
			if (saveNeeded) {
				PersonBL.saveAndAddMenuFilters(dbPerson);
			}
		}
		if (count > 0) {
			ApplicationBean.getInstance().setActualUsers();
		}
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("User sync results for " + ldapPersons.size() + " ldap persons:");
			if (count == 0) {
				LOGGER.info("LDAP directory users in sync with Genji database.");
			} else {
				LOGGER.info(count + " user(s) from LDAP directory added to Genji database.");
			}
		}
	}

	/**
	 * Synchronizes the list of users in the Genji database with a HashMap of
	 * login names and TPersonBeans.
	 * 
	 * @param ldapPersons
	 *            a map with login names and their corresponding TPersonBeans
	 * 
	 */
	public static void deactivateUsers(Map<String, TPersonBean> ldapPersons) throws Exception {
		List<TPersonBean> dbasePersons = PersonBL.loadActivePersons();
		if (dbasePersons != null && ldapPersons != null) {
			LOGGER.info("Number of users found on LDAP server: " + ldapPersons.size());
			LOGGER.info("Number of active users found in Genji after LDAP sync: " + dbasePersons.size());
			int count = 0;
			List<Integer> deactivate = new ArrayList<Integer>();
			boolean isForceLDAP =  ApplicationBean.getInstance().getSiteBean().getIsForceLdap();
			for (TPersonBean dbasePerson : dbasePersons) {
				String loginName = dbasePerson.getLoginName();
				if (!ldapPersons.containsKey(loginName) && (isForceLDAP || dbasePerson.isLdapUser())) {
					String lname = loginName.toLowerCase();
					if (TPersonBean.ADMIN_USER.equals(lname) || TPersonBean.GUEST_USER.equals(lname) || dbasePerson.getObjectID().intValue() <= 100) {
						LOGGER.debug("We do not deactivate admin, guest, or anything with OID below 100");
					} else {
						deactivate.add(dbasePerson.getObjectID());
						count++;
						LOGGER.debug("Deactivating " + lname);
					}
				}
			}
			if (count > 0) {
				PersonBL.activateDeactivatePersons(deactivate, true);
				LOGGER.info(count + " user(s) were deactivated");
			} else {
				LOGGER.info("No user were deactivated");
			}
		}
	}

	/**
	 * Gets all persons for a group
	 * 
	 * @param groups
	 * @param siteBean
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	static List<TPersonBean> getAllLdapUsersDescendants(String providerUrl, String bindDN, String bindPassword, String loginAttributeName, String filter)
			throws Exception {
		List<TPersonBean> personBeans = new ArrayList<TPersonBean>();
		if (filter == null || "".equals(filter) || "*".equals(filter)) {
			filter = loginAttributeName + "=*";
		}
		int recordCount = 0;
		SearchControls ctls = null;
		LdapContext ctx = null;
		try {
			ctx = getInitialContext(providerUrl, bindDN, bindPassword);
			if (ctx == null) {
				return personBeans;
			}
			// Activate paged results
			int pageSize = 5;
			// TODO replace for GROOVY
			ctx.setRequestControls(new Control[] { new PagedResultsControl(pageSize, Control.NONCRITICAL) });
			int total;
			String searchStr = "(" + filter + ")";
			// Control the search
			ctls = new SearchControls();
			ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			ctls.setCountLimit(
					(ApplicationBean.getInstance().getMaxNumberOfFullUsers() + ApplicationBean.getInstance().getMaxNumberOfLimitedUsers()) * 3
							+ 10); // Don't ask for more than we can handle
									// anyways
			if (ldapMap == null || ldapMap.isEmpty()) {
				LOGGER.error("There is no LDAP mapping in quartz-jobs.xml. Please provide!");
				return personBeans;
			}
			String firstNameAttributeName = ldapMap.get("firstName");
			String lastNameAttributName = ldapMap.get("lastName");
			String emailAttributeName = ldapMap.get("email");
			String phoneAttributName = ldapMap.get("phone");
			byte[] cookie = null;
			// TODO replace for GROOVY
			cookie = new byte[] {};
			// cookie = [] as byte[];
			while (cookie != null) {
				NamingEnumeration<SearchResult> results = ctx.search("", searchStr, ctls);
				while (results != null && results.hasMore()) {
					SearchResult sr = (SearchResult) results.next();
					TPersonBean personBean = getPersonBean(sr, loginAttributeName, firstNameAttributeName, lastNameAttributName, emailAttributeName,
							phoneAttributName);
					if (personBean != null) {
						personBeans.add(personBean);
						++recordCount;
					}
				}
				// Examine the paged results control response
				Control[] controls = ctx.getResponseControls();
				if (controls != null) {
					for (int i = 0; i < controls.length; i++) {
						if (controls[i] instanceof PagedResultsResponseControl) {
							PagedResultsResponseControl prrc = (PagedResultsResponseControl) controls[i];
							total = prrc.getResultSize();
							if (total != 0) {
								LOGGER.debug("***************** END-OF-PAGE " + "(total : " + total + ") *****************\n");
							} else {
								LOGGER.debug("***************** END-OF-PAGE " + "(total: unknown) ***************\n");
							}
							cookie = prrc.getCookie();
						}
					}
				} else {
					LOGGER.debug("No controls were sent from the server");
				}
				// Re-activate paged results
				// TODO replace for GROOVY
				ctx.setRequestControls(new Control[] { new PagedResultsControl(pageSize, cookie, Control.CRITICAL) });
			}
		} catch (SizeLimitExceededException sle) {
			if (recordCount < ctls.getCountLimit()) {
				LOGGER.error("Searching LDAP asked for more entries than permitted by the LDAP server.");
				LOGGER.error("Size limit exceeded error occurred after record " + recordCount + " with " + sle.getMessage());
				LOGGER.error("You have to ask your LDAP server admin to increase the limit or specify a more suitable search base or filter.");
			} else {
				LOGGER.error("Searching LDAP asked for more entries than permitted by the Genji server (" + recordCount + ").");
				LOGGER.error("You have to get more user licenses for Genji or specify a more suitable search base or filter.");
			}
			LOGGER.error("The LDAP synchronization is most likely incomplete.");
		} catch (NamingException e) {
			LOGGER.error("PagedSearch failed.");
			LOGGER.debug(ExceptionUtils.getStackTrace(e));
		} catch (IOException ie) {
			LOGGER.error("PagedSearch failed.");
			LOGGER.debug(ExceptionUtils.getStackTrace(ie));
		} finally {
			if (ctx != null) {
				ctx.close();
			}
		}
		return personBeans;
	}

}
