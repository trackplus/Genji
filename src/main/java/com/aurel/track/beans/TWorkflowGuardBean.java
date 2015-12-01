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
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class TWorkflowGuardBean
    extends com.aurel.track.beans.base.BaseTWorkflowGuardBean
    implements Serializable,ISerializableLabelBean{
	public static final long serialVersionUID = 400L;
	
	public interface GUARD_TYPE {
		public static final int ROLE_DEPRECATED = 1;
		public static final int LOGGED_IN_PERSON_DEPRECATED = 2;
		public static final int GROOVY_SCRIPT_DEPRECATED = 0;
		
		public static final int ROLE = -1;
		public static final int LOGGED_IN_PERSON = -2;
		public static final int GROOVY_SCRIPT = -3;
		public static final int FILTER_GUARD = -4;
	}

	@Override
	public Map<String, String> serializeBean() {
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("objectID", getObjectID().toString());
		attributesMap.put("uuid", getUuid());
		attributesMap.put("transitionID",getWorkflowTransition().toString());
		attributesMap.put("guardType",getGuardType().toString());

		Integer roleID= getRole();
		if(roleID!=null){
			attributesMap.put("roleID",roleID.toString());
		}
		Integer groovyScript=getGroovyScript();
		if(groovyScript!=null){
			attributesMap.put("groovyScript",groovyScript.toString());
		}
		Integer personID=getPerson();
		if(personID!=null){
			attributesMap.put("personID",personID.toString());
		}

		Integer guardType=getGuardType();
		if(guardType!=null){
			attributesMap.put("guardType",guardType.toString());
		}

		String guardParams=getGuardParams();
		if(guardParams!=null){
			attributesMap.put("guardParams",guardParams);
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
		this.setWorkflowTransition(Integer.parseInt(attributes.get("transitionID")));
		this.setGuardType(Integer.parseInt(attributes.get("guardType")));

		if(attributes.get("roleID")!=null){
			this.setRole(Integer.parseInt(attributes.get("roleID")));
		}
		if(attributes.get("groovyScript")!=null){
			this.setGroovyScript(Integer.parseInt(attributes.get("groovyScript")));
		}
		if(attributes.get("personID")!=null){
			this.setPerson(Integer.parseInt(attributes.get("personID")));
		}

		if(attributes.get("guardType")!=null){
			this.setGuardType(Integer.parseInt(attributes.get("guardType")));
		}

		if(attributes.get("guardParams")!=null){
			this.setGuardParams(attributes.get("guardParams"));
		}
		return this;
	}

	@Override
	public boolean considerAsSame(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		TWorkflowGuardBean internalGuard=(TWorkflowGuardBean)serializableLabelBean;
		if (internalGuard == null) {
			return false;
		}

		String externalUuid = getUuid();
		String internalUuid = internalGuard.getUuid();

		return EqualUtils.equalStrict(externalUuid, internalUuid);

	}

	@Override
	public Integer saveBean(ISerializableLabelBean serializableLabelBean, Map<String, Map<Integer, Integer>> matchesMap) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public String getLabel() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
