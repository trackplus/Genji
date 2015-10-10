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

package com.aurel.track.configExchange.importer;

import com.aurel.track.configExchange.impl.*;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class EntityImporterFactory {
	public static EntityImporterFactory instance;
	private Map<String,IEntityImporter> importerMap;
	public static EntityImporterFactory getInstance(){
		if(instance==null){
			instance=new EntityImporterFactory();
		}
		return instance;
	}
	private EntityImporterFactory(){
		initMap();
	}
	private void initMap(){
		importerMap=new HashMap<String, IEntityImporter>();

		importerMap.put("TScreenConfigBean",new ScreenConfigImporter());
		importerMap.put("TScreenBean",new ScreenImporter());
		importerMap.put("TScreenTabBean",new ScreenTabImporter());
		importerMap.put("TScreenPanelBean",new ScreenPanelImporter());
		importerMap.put("TScreenFieldBean",new ScreenFieldImporter());
		importerMap.put("TFieldBean",new FieldImporter());

		importerMap.put("TFieldConfigBean",new FieldConfigImporter());
		importerMap.put("TGeneralSettingsBean",new GeneralSettingImporter());
		importerMap.put("TTextBoxSettingsBean",new TextBoxSettingsImporter());
		importerMap.put("TOptionSettingsBean",new OptionSettingsImporter());

		importerMap.put("TMailTemplateBean",new MailTemplateImporter());
		importerMap.put("TMailTemplateDefBean",new MailTemplateDefImporter());
		importerMap.put("TPersonBean",new PersonImporter());
		importerMap.put("TRoleBean",new RoleImporter());
		importerMap.put("TScriptsBean",new ScriptsImporter());

		importerMap.put("TOptionBean",new OptionImporter());
		importerMap.put("TListBean",new ListImporter());
		importerMap.put("TBLOBBean",new BlobImporter());


		importerMap.put("TProjectTypeBean",new ProjectTypeImporter());
		importerMap.put("TPlistTypeBean",new PlistTypeImporter());
		importerMap.put("TPstateBean",new PstateImporter());
		importerMap.put("TPpriorityBean",new PpriorityImporter());
		importerMap.put("TPseverityBean",new PseverityImporter());
		importerMap.put("TPRoleBean",new PRoleImporter());
		importerMap.put("TChildProjectTypeBean",new ChildProjectTypeImporter());

		importerMap.put("TStateBean",new StateImporter());
		importerMap.put("TPriorityBean",new PriorityImporter());
		importerMap.put("TSeverityBean",new SeverityImporter());
		importerMap.put("TListTypeBean",new IssueTypeImporter());

	}
	public IEntityImporter getImporter(String type){
		return importerMap.get(type);
	}
}
