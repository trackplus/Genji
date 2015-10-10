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

package com.aurel.track.persist;

import java.lang.reflect.Method;
import java.util.UUID;

import org.apache.torque.om.BaseObject;

public abstract class TpBaseObject extends BaseObject{

	public static final long serialVersionUID = 400L;
	
	@Override
	public void setNew(boolean b) {
	   if (b) {
		   try {
			      Class clazz = this.getClass();
			      Method mgetUuid = clazz.getMethod("getUuid", (Class[])null);
			      String uuid = (String)mgetUuid.invoke(this, (Object[])null);
			      if (uuid==null) {
			    	  //set a new uuid only if not yet set 
			    	  //this is the case by exchange import: the uuid for the new internal object 
			    	  //is already set to the uuid of the external object 
			    	  //(the goal is to have a uuid match by the next import)
			    	  Method msetUuid = clazz.getMethod("setUuid", String.class);
			    	  msetUuid.invoke(this, UUID.randomUUID().toString());
			      }
		   }
		   catch (Exception e) {
			     // this will not be for everyone...
		   }
	   }
	   super.setNew(b);
   }

   /**
    * It seems that when a new torque object is created the setNew(true) is not called
    * setNew(true) is called only when transforming beans to torque objects
    * That is not the case for some "old" objects which are not saved through beans but directly
    * The isNew() method is called before saving a torque object directly so this could 
    * be the only chance to set the uuid
    * Once all torque objects are saved by transforming them from beans this method can be deleted 
    */
   @Override
public boolean isNew() {
	   boolean b = super.isNew();
	   if (b) {
		   try {
			      Class clazz = this.getClass();
			      Method mgetUuid = clazz.getMethod("getUuid", (Class[])null);
			      String uuid = (String)mgetUuid.invoke(this, (Object[])null);
			      if (uuid==null) {
			    	  //set a new uuid only if not yet set 
			    	  //this is the case by exchange import: the uuid for the new internal object 
			    	  //is already set to the uuid of the external object 
			    	  //(the goal is to have a uuid match by the next import)
			    	  Method msetUuid = clazz.getMethod("setUuid", String.class);
			    	  msetUuid.invoke(this, UUID.randomUUID().toString());		
			      }
		   }
		   catch (Exception e) {
			     // this will not be for everyone...
		   }
	   }
	return b;
}



   
}
