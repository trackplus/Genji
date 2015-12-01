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

package com.aurel.track.fieldType.types;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aurel.track.fieldType.types.system.SystemParent;
import com.aurel.track.fieldType.types.system.check.SystemAccessLevel;
import com.aurel.track.fieldType.types.system.check.SystemTaskIsMilestone;
import com.aurel.track.fieldType.types.system.select.SystemSelectIssueType;
import com.aurel.track.fieldType.types.system.select.SystemSelectManager;
import com.aurel.track.fieldType.types.system.select.SystemSelectPriority;
import com.aurel.track.fieldType.types.system.select.SystemSelectProject;
import com.aurel.track.fieldType.types.system.select.SystemSelectReleaseNoticed;
import com.aurel.track.fieldType.types.system.select.SystemSelectReleaseScheduled;
import com.aurel.track.fieldType.types.system.select.SystemSelectResponsible;
import com.aurel.track.fieldType.types.system.select.SystemSelectSeverity;
import com.aurel.track.fieldType.types.system.select.SystemSelectState;
import com.aurel.track.fieldType.types.system.text.SystemArchiveLevel;
import com.aurel.track.fieldType.types.system.text.SystemCommentTextEditor;
import com.aurel.track.fieldType.types.system.text.SystemDescriptionTextEditor;
import com.aurel.track.fieldType.types.system.text.SystemEndDate;
import com.aurel.track.fieldType.types.system.text.SystemEndDateTarget;
import com.aurel.track.fieldType.types.system.text.SystemLabel;
import com.aurel.track.fieldType.types.system.text.SystemLabelDate;
import com.aurel.track.fieldType.types.system.text.SystemLastEditedBy;
import com.aurel.track.fieldType.types.system.text.SystemOriginator;
import com.aurel.track.fieldType.types.system.text.SystemProjectSpecificIssueNo;
import com.aurel.track.fieldType.types.system.text.SystemStartDate;
import com.aurel.track.fieldType.types.system.text.SystemStartDateTarget;
import com.aurel.track.fieldType.types.system.text.SystemSubmitterEmail;
import com.aurel.track.fieldType.types.system.text.SystemSynopsis;
import com.aurel.track.fieldType.types.system.text.SystemTextBoxTargetDate;
import com.aurel.track.fieldType.types.system.text.SystemTextBoxText;
import com.aurel.track.fieldType.types.system.text.SystemWBS;
import com.aurel.track.plugin.FieldTypeDescriptor;
import com.aurel.track.plugin.PluginManager;
import com.aurel.track.resources.ResourceBundleManager;

public class FieldTypeDescriptorUtil {
	
	private static List<FieldTypeDescriptor> customFieldTypeDescriptorsList;
	private static List<FieldTypeDescriptor> systemFieldTypeDescriptorsList;
	private static Map<String, FieldTypeDescriptor> allFieldDescriptorsMap;
	
	public synchronized static FieldTypeDescriptor getFieldTypeDescriptor(String pluginID) {
		if (allFieldDescriptorsMap==null) {
			allFieldDescriptorsMap = new HashMap<String, FieldTypeDescriptor>();
			List<FieldTypeDescriptor> systemFieldTypeDescriptorsList = getSystemFieldTypeDescriptors();
			List<FieldTypeDescriptor> customFieldTypeDescriptorsList = getCustomFieldTypeDescriptors();
			addDescriptorListToMap(allFieldDescriptorsMap, systemFieldTypeDescriptorsList);
			addDescriptorListToMap(allFieldDescriptorsMap, customFieldTypeDescriptorsList);
		}
		return allFieldDescriptorsMap.get(pluginID);
	}

	private static void addDescriptorListToMap(Map<String, FieldTypeDescriptor> allFieldDescriptorsMap,
			List<FieldTypeDescriptor> fieldTypeDescriptorsList) {
		if (fieldTypeDescriptorsList!=null) {
			for (FieldTypeDescriptor fieldTypeDescriptor : fieldTypeDescriptorsList) {
				allFieldDescriptorsMap.put(fieldTypeDescriptor.getId(), fieldTypeDescriptor);
			}
		}
	}
	
	/**
	 * Cache the fieldType plugins
	 *
	 * @return
	 */
	public synchronized static List<FieldTypeDescriptor> getCustomFieldTypeDescriptors() {
		if (customFieldTypeDescriptorsList==null) {
			customFieldTypeDescriptorsList = PluginManager.getInstance().getCustomFieldTypeDescriptors();
			/*customFieldDescriptorsMap=new HashMap();
			if (customFieldTypeDescriptorsList!=null) {
				for (int i = 0; i < customFieldTypeDescriptorsList.size(); i++) {
					FieldTypeDescriptor fieldTypeDescriptor =  (FieldTypeDescriptor)customFieldTypeDescriptorsList.get(i);
					customFieldDescriptorsMap.put(fieldTypeDescriptor.getId(), fieldTypeDescriptor);
				}
			} */
		}
		return customFieldTypeDescriptorsList;
	}
	
	/**
	 * Get the set of resource bundle names from the custom fields
	 * @return
	 */
	public static Set<String> getCustomFieldTypeResourceBundles() {
		Set<String> resourceBundleNames = new HashSet<String>();
		List<FieldTypeDescriptor> customFieldTypeDescriptors = getCustomFieldTypeDescriptors();
		if (customFieldTypeDescriptors!=null) {
			for (FieldTypeDescriptor fieldTypeDescriptor : customFieldTypeDescriptors) {
				if (fieldTypeDescriptor.getBundleName()!=null) {
					resourceBundleNames.add(fieldTypeDescriptor.getBundleName());
				}
			}
		}
		return resourceBundleNames;
	}
	
	/**
	 * The FieldTypeDescriptors for system fields are not
	 * loaded from the trackplus-plugin.xml but are hard coded  
	 * @return
	 */
	private synchronized static List<FieldTypeDescriptor> getSystemFieldTypeDescriptors() {
		if (systemFieldTypeDescriptorsList==null) {
			//use the custom FIELDTYPE_RESOURCES bundle also for the few system fields which necessitate
			//specific configuration because they use the same templates as the corresponding custom fields
			String systemFieldTypeResourceBundle = ResourceBundleManager.FIELDTYPE_RESOURCES;
			String textBoxTextTemplate = "js.ext.com.track.fieldType.TextboxText";//"plugins/fieldType/configs/textBoxText.ftl";
			//String textBoxDateTemplate = "com.trackplus.fieldType.TextboxDate";//"plugins/fieldType/configs/textBoxDate.ftl";
			String textBoxDateTemplate = "js.ext.com.track.fieldType.TextboxSystemDate";
			
			String emailAddressTemplate = "js.ext.com.track.fieldType.EmailAddress";

			systemFieldTypeDescriptorsList =  new LinkedList<FieldTypeDescriptor>();
			//project
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemSelectProject.class, 
					systemFieldTypeResourceBundle, null));
			//issue type
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemSelectIssueType.class, 
					systemFieldTypeResourceBundle, null));
			//subproject		
			/*systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemSelectSubproject.class, 
					systemFieldTypeResourceBundle, null));*/
			//status
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemSelectState.class, 
					systemFieldTypeResourceBundle, null));
			//manager
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemSelectManager.class, 
					systemFieldTypeResourceBundle, null));
			//responsible
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemSelectResponsible.class, 
					systemFieldTypeResourceBundle, null));
			//class		
			/*systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemSelectTheClass.class, 
					systemFieldTypeResourceBundle, null));*/
			//release noticed
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemSelectReleaseNoticed.class, 
					systemFieldTypeResourceBundle, null));
			//release scheduled
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemSelectReleaseScheduled.class, 
					systemFieldTypeResourceBundle, null));
			//priority
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemSelectPriority.class, 
					systemFieldTypeResourceBundle, null));
			//severity
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemSelectSeverity.class, 
					systemFieldTypeResourceBundle, null));
			//issueNo
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemLabel.class, 
					systemFieldTypeResourceBundle, null));
			//originator
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemOriginator.class, 
					systemFieldTypeResourceBundle, null));
			//create date and last modified date
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemLabelDate.class, 
					systemFieldTypeResourceBundle, null));
			//superior workitem
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemParent.class, 
					systemFieldTypeResourceBundle, null));
			//synopsis
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemSynopsis.class, 
					systemFieldTypeResourceBundle, textBoxTextTemplate));
			//build
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemTextBoxText.class, 
					systemFieldTypeResourceBundle, textBoxTextTemplate));
			//start date, end date
			/*systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemTextBoxDate.class, 
					systemFieldTypeResourceBundle, textBoxDateTemplate));*/
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemStartDate.class, 
					systemFieldTypeResourceBundle, textBoxDateTemplate));
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemEndDate.class, 
					systemFieldTypeResourceBundle, textBoxDateTemplate));
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemStartDateTarget.class, 
					systemFieldTypeResourceBundle, textBoxDateTemplate));
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemEndDateTarget.class, 
					systemFieldTypeResourceBundle, textBoxDateTemplate));
			//description
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemDescriptionTextEditor.class, 
					systemFieldTypeResourceBundle, textBoxTextTemplate));
			//access level
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemAccessLevel.class, 
					systemFieldTypeResourceBundle, null));
			//system archive level
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemArchiveLevel.class, 
					systemFieldTypeResourceBundle, null));
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemTaskIsMilestone.class, 
					systemFieldTypeResourceBundle, null));
			//comment
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemCommentTextEditor.class, 
					systemFieldTypeResourceBundle, textBoxTextTemplate));
			//last edited by
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemLastEditedBy.class, 
					systemFieldTypeResourceBundle, null));
			//submitter email
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemSubmitterEmail.class, 
					systemFieldTypeResourceBundle, emailAddressTemplate));
			//wbs
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemWBS.class, 
					systemFieldTypeResourceBundle, "js.ext.com.track.fieldType.WBS"));
			//project specific idNumber
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemProjectSpecificIssueNo.class, 
					systemFieldTypeResourceBundle, null));
			//target (top down) dates
			systemFieldTypeDescriptorsList.add(createSystemFieldDescriptor(SystemTextBoxTargetDate.class, 
					systemFieldTypeResourceBundle, textBoxDateTemplate));
		}
		return systemFieldTypeDescriptorsList;
	}

	private static FieldTypeDescriptor createSystemFieldDescriptor(Class clazz, String bundleName, String pageConfig) {
		FieldTypeDescriptor fieldTypeDescriptor = new FieldTypeDescriptor();
		String className = clazz.getName();
		String packageName = clazz.getPackage().getName();
		String name = className.substring(packageName.length()+1);
		name = name.substring(0, 1).toLowerCase() + name.substring(1);
		fieldTypeDescriptor.setId(className);
		fieldTypeDescriptor.setName(name);
		fieldTypeDescriptor.setTheClassName(className);
		fieldTypeDescriptor.setBundleName(bundleName);
		fieldTypeDescriptor.setDescription(name + ".description");
		fieldTypeDescriptor.setLabel(name + ".label");
		fieldTypeDescriptor.setTooltip(name + ".tooltip");
		//actually only this field will be used by some of the system field types
		fieldTypeDescriptor.setJsConfigClass(pageConfig);
		return fieldTypeDescriptor;
	}
}
