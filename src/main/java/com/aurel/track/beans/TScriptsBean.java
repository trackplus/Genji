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

import com.aurel.track.util.EqualUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains Groovy scripts
 *
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TScriptsBean
    extends /* com.trackplus.model.Tscript */ com.aurel.track.beans.base.BaseTScriptsBean
    implements Serializable, IBeanID,ILabelBean,ISerializableLabelBean{
	public static final long serialVersionUID = 400L;

	@Override
	public String getLabel() {
		return getClazzName();
	}

	public static enum ScriptType {GROOVY, JAVASCRIPT}
	
	public static interface SCRIPT_TYPE {
		public static final int WORKFLOW_ACTIVITY = 0;
		public static final int WORKFLOW_GUARD = 1;
		public static final int GENEARL_SCRIPT = 2;
		public static final int PARAMETER_SCRIPT = 3;
		public static final int FIELD_CHANGE = 4;
	}

	//the label of the trigger type: Groovy or JavaScript
	//it is his own script or other's script:
	//whether the delete button is available or not



	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", String.valueOf(getObjectID()));
		attributesMap.put("uuid", getUuid());

		Integer scriptType=getScriptType();
		if(scriptType!=null){
			attributesMap.put("scriptType", scriptType.toString());
		}

		String clazzName=getClazzName();
		if(clazzName!=null){
			attributesMap.put("clazzName", clazzName);
		}
		String sourceCode=getSourceCode();
		if(sourceCode!=null){
			attributesMap.put("sourceCode", sourceCode);
		}
		return attributesMap;
	}

	@Override
	public ISerializableLabelBean deserializeBean(Map<String, String> attributes) {
		String strObjectID = attributes.get("objectID");
		if (strObjectID!=null) {
			this.setObjectID(new Integer(strObjectID));
		}
		this.setUuid(attributes.get("uuid"));
		String scriptType=attributes.get("scriptType");
		if(scriptType!=null){
			this.setScriptType(Integer.decode(scriptType));
		}

		String clazzName=attributes.get("clazzName");
		if(clazzName!=null){
			this.setClazzName(clazzName);
		}
		String sourceCode=attributes.get("sourceCode");
		if(sourceCode!=null){
			this.setSourceCode(sourceCode);
		}
		return this;
	}

	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		TScriptsBean internalScript=(TScriptsBean)serializableLabelBean;
		if (internalScript == null) {
			return false;
		}

		String externalUuid = getUuid();
		String internalUuid = internalScript.getUuid();

		return EqualUtils.equalStrict(externalUuid, internalUuid);
	}

	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
