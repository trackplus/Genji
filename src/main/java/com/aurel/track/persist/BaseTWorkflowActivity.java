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


import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.torque.TorqueException;
import org.apache.torque.map.TableMap;
import org.apache.torque.om.BaseObject;
import org.apache.torque.om.ComboKey;
import org.apache.torque.om.DateKey;
import org.apache.torque.om.NumberKey;
import org.apache.torque.om.ObjectKey;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.om.StringKey;
import org.apache.torque.om.Persistent;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Transaction;



import com.aurel.track.persist.TWorkflowTransition;
import com.aurel.track.persist.TWorkflowTransitionPeer;
import com.aurel.track.persist.TWorkflowStation;
import com.aurel.track.persist.TWorkflowStationPeer;
import com.aurel.track.persist.TWorkflowStation;
import com.aurel.track.persist.TWorkflowStationPeer;
import com.aurel.track.persist.TWorkflowStation;
import com.aurel.track.persist.TWorkflowStationPeer;
import com.aurel.track.persist.TScripts;
import com.aurel.track.persist.TScriptsPeer;
import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;
import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;
import com.aurel.track.persist.TSLA;
import com.aurel.track.persist.TSLAPeer;
import com.aurel.track.persist.TScreen;
import com.aurel.track.persist.TScreenPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TWorkflowActivityBean;
import com.aurel.track.beans.TWorkflowTransitionBean;
import com.aurel.track.beans.TWorkflowStationBean;
import com.aurel.track.beans.TWorkflowStationBean;
import com.aurel.track.beans.TWorkflowStationBean;
import com.aurel.track.beans.TScriptsBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TSLABean;
import com.aurel.track.beans.TScreenBean;

import com.aurel.track.beans.TWfActivityContextParamsBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TWorkflowActivity
 */
public abstract class BaseTWorkflowActivity extends TpBaseObject
{
    /** The Peer class */
    private static final TWorkflowActivityPeer peer =
        new TWorkflowActivityPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the transitionActivity field */
    private Integer transitionActivity;

    /** The value for the stationEntryActivity field */
    private Integer stationEntryActivity;

    /** The value for the stationExitActivity field */
    private Integer stationExitActivity;

    /** The value for the stationDoActivity field */
    private Integer stationDoActivity;

    /** The value for the activityType field */
    private Integer activityType;

    /** The value for the activityParams field */
    private String activityParams;

    /** The value for the groovyScript field */
    private Integer groovyScript;

    /** The value for the newMan field */
    private Integer newMan;

    /** The value for the newResp field */
    private Integer newResp;

    /** The value for the fieldSetterRelation field */
    private Integer fieldSetterRelation;

    /** The value for the paramName field */
    private String paramName;

    /** The value for the fieldSetMode field */
    private Integer fieldSetMode;

    /** The value for the sortOrder field */
    private Integer sortOrder;

    /** The value for the sla field */
    private Integer sla;

    /** The value for the screen field */
    private Integer screen;

    /** The value for the uuid field */
    private String uuid;


    /**
     * Get the ObjectID
     *
     * @return Integer
     */
    public Integer getObjectID()
    {
        return objectID;
    }


    /**
     * Set the value of ObjectID
     *
     * @param v new value
     */
    public void setObjectID(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.objectID, v))
        {
            this.objectID = v;
            setModified(true);
        }



        // update associated TWfActivityContextParams
        if (collTWfActivityContextParamss != null)
        {
            for (int i = 0; i < collTWfActivityContextParamss.size(); i++)
            {
                ((TWfActivityContextParams) collTWfActivityContextParamss.get(i))
                        .setWorkflowActivity(v);
            }
        }
    }

    /**
     * Get the TransitionActivity
     *
     * @return Integer
     */
    public Integer getTransitionActivity()
    {
        return transitionActivity;
    }


    /**
     * Set the value of TransitionActivity
     *
     * @param v new value
     */
    public void setTransitionActivity(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.transitionActivity, v))
        {
            this.transitionActivity = v;
            setModified(true);
        }


        if (aTWorkflowTransition != null && !ObjectUtils.equals(aTWorkflowTransition.getObjectID(), v))
        {
            aTWorkflowTransition = null;
        }

    }

    /**
     * Get the StationEntryActivity
     *
     * @return Integer
     */
    public Integer getStationEntryActivity()
    {
        return stationEntryActivity;
    }


    /**
     * Set the value of StationEntryActivity
     *
     * @param v new value
     */
    public void setStationEntryActivity(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.stationEntryActivity, v))
        {
            this.stationEntryActivity = v;
            setModified(true);
        }


        if (aTWorkflowStationRelatedByStationEntryActivity != null && !ObjectUtils.equals(aTWorkflowStationRelatedByStationEntryActivity.getObjectID(), v))
        {
            aTWorkflowStationRelatedByStationEntryActivity = null;
        }

    }

    /**
     * Get the StationExitActivity
     *
     * @return Integer
     */
    public Integer getStationExitActivity()
    {
        return stationExitActivity;
    }


    /**
     * Set the value of StationExitActivity
     *
     * @param v new value
     */
    public void setStationExitActivity(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.stationExitActivity, v))
        {
            this.stationExitActivity = v;
            setModified(true);
        }


        if (aTWorkflowStationRelatedByStationExitActivity != null && !ObjectUtils.equals(aTWorkflowStationRelatedByStationExitActivity.getObjectID(), v))
        {
            aTWorkflowStationRelatedByStationExitActivity = null;
        }

    }

    /**
     * Get the StationDoActivity
     *
     * @return Integer
     */
    public Integer getStationDoActivity()
    {
        return stationDoActivity;
    }


    /**
     * Set the value of StationDoActivity
     *
     * @param v new value
     */
    public void setStationDoActivity(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.stationDoActivity, v))
        {
            this.stationDoActivity = v;
            setModified(true);
        }


        if (aTWorkflowStationRelatedByStationDoActivity != null && !ObjectUtils.equals(aTWorkflowStationRelatedByStationDoActivity.getObjectID(), v))
        {
            aTWorkflowStationRelatedByStationDoActivity = null;
        }

    }

    /**
     * Get the ActivityType
     *
     * @return Integer
     */
    public Integer getActivityType()
    {
        return activityType;
    }


    /**
     * Set the value of ActivityType
     *
     * @param v new value
     */
    public void setActivityType(Integer v) 
    {

        if (!ObjectUtils.equals(this.activityType, v))
        {
            this.activityType = v;
            setModified(true);
        }


    }

    /**
     * Get the ActivityParams
     *
     * @return String
     */
    public String getActivityParams()
    {
        return activityParams;
    }


    /**
     * Set the value of ActivityParams
     *
     * @param v new value
     */
    public void setActivityParams(String v) 
    {

        if (!ObjectUtils.equals(this.activityParams, v))
        {
            this.activityParams = v;
            setModified(true);
        }


    }

    /**
     * Get the GroovyScript
     *
     * @return Integer
     */
    public Integer getGroovyScript()
    {
        return groovyScript;
    }


    /**
     * Set the value of GroovyScript
     *
     * @param v new value
     */
    public void setGroovyScript(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.groovyScript, v))
        {
            this.groovyScript = v;
            setModified(true);
        }


        if (aTScripts != null && !ObjectUtils.equals(aTScripts.getObjectID(), v))
        {
            aTScripts = null;
        }

    }

    /**
     * Get the NewMan
     *
     * @return Integer
     */
    public Integer getNewMan()
    {
        return newMan;
    }


    /**
     * Set the value of NewMan
     *
     * @param v new value
     */
    public void setNewMan(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.newMan, v))
        {
            this.newMan = v;
            setModified(true);
        }


        if (aTPersonRelatedByNewMan != null && !ObjectUtils.equals(aTPersonRelatedByNewMan.getObjectID(), v))
        {
            aTPersonRelatedByNewMan = null;
        }

    }

    /**
     * Get the NewResp
     *
     * @return Integer
     */
    public Integer getNewResp()
    {
        return newResp;
    }


    /**
     * Set the value of NewResp
     *
     * @param v new value
     */
    public void setNewResp(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.newResp, v))
        {
            this.newResp = v;
            setModified(true);
        }


        if (aTPersonRelatedByNewResp != null && !ObjectUtils.equals(aTPersonRelatedByNewResp.getObjectID(), v))
        {
            aTPersonRelatedByNewResp = null;
        }

    }

    /**
     * Get the FieldSetterRelation
     *
     * @return Integer
     */
    public Integer getFieldSetterRelation()
    {
        return fieldSetterRelation;
    }


    /**
     * Set the value of FieldSetterRelation
     *
     * @param v new value
     */
    public void setFieldSetterRelation(Integer v) 
    {

        if (!ObjectUtils.equals(this.fieldSetterRelation, v))
        {
            this.fieldSetterRelation = v;
            setModified(true);
        }


    }

    /**
     * Get the ParamName
     *
     * @return String
     */
    public String getParamName()
    {
        return paramName;
    }


    /**
     * Set the value of ParamName
     *
     * @param v new value
     */
    public void setParamName(String v) 
    {

        if (!ObjectUtils.equals(this.paramName, v))
        {
            this.paramName = v;
            setModified(true);
        }


    }

    /**
     * Get the FieldSetMode
     *
     * @return Integer
     */
    public Integer getFieldSetMode()
    {
        return fieldSetMode;
    }


    /**
     * Set the value of FieldSetMode
     *
     * @param v new value
     */
    public void setFieldSetMode(Integer v) 
    {

        if (!ObjectUtils.equals(this.fieldSetMode, v))
        {
            this.fieldSetMode = v;
            setModified(true);
        }


    }

    /**
     * Get the SortOrder
     *
     * @return Integer
     */
    public Integer getSortOrder()
    {
        return sortOrder;
    }


    /**
     * Set the value of SortOrder
     *
     * @param v new value
     */
    public void setSortOrder(Integer v) 
    {

        if (!ObjectUtils.equals(this.sortOrder, v))
        {
            this.sortOrder = v;
            setModified(true);
        }


    }

    /**
     * Get the Sla
     *
     * @return Integer
     */
    public Integer getSla()
    {
        return sla;
    }


    /**
     * Set the value of Sla
     *
     * @param v new value
     */
    public void setSla(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.sla, v))
        {
            this.sla = v;
            setModified(true);
        }


        if (aTSLA != null && !ObjectUtils.equals(aTSLA.getObjectID(), v))
        {
            aTSLA = null;
        }

    }

    /**
     * Get the Screen
     *
     * @return Integer
     */
    public Integer getScreen()
    {
        return screen;
    }


    /**
     * Set the value of Screen
     *
     * @param v new value
     */
    public void setScreen(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.screen, v))
        {
            this.screen = v;
            setModified(true);
        }


        if (aTScreen != null && !ObjectUtils.equals(aTScreen.getObjectID(), v))
        {
            aTScreen = null;
        }

    }

    /**
     * Get the Uuid
     *
     * @return String
     */
    public String getUuid()
    {
        return uuid;
    }


    /**
     * Set the value of Uuid
     *
     * @param v new value
     */
    public void setUuid(String v) 
    {

        if (!ObjectUtils.equals(this.uuid, v))
        {
            this.uuid = v;
            setModified(true);
        }


    }

    



    private TWorkflowTransition aTWorkflowTransition;

    /**
     * Declares an association between this object and a TWorkflowTransition object
     *
     * @param v TWorkflowTransition
     * @throws TorqueException
     */
    public void setTWorkflowTransition(TWorkflowTransition v) throws TorqueException
    {
        if (v == null)
        {
            setTransitionActivity((Integer) null);
        }
        else
        {
            setTransitionActivity(v.getObjectID());
        }
        aTWorkflowTransition = v;
    }


    /**
     * Returns the associated TWorkflowTransition object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TWorkflowTransition object
     * @throws TorqueException
     */
    public TWorkflowTransition getTWorkflowTransition()
        throws TorqueException
    {
        if (aTWorkflowTransition == null && (!ObjectUtils.equals(this.transitionActivity, null)))
        {
            aTWorkflowTransition = TWorkflowTransitionPeer.retrieveByPK(SimpleKey.keyFor(this.transitionActivity));
        }
        return aTWorkflowTransition;
    }

    /**
     * Return the associated TWorkflowTransition object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TWorkflowTransition object
     * @throws TorqueException
     */
    public TWorkflowTransition getTWorkflowTransition(Connection connection)
        throws TorqueException
    {
        if (aTWorkflowTransition == null && (!ObjectUtils.equals(this.transitionActivity, null)))
        {
            aTWorkflowTransition = TWorkflowTransitionPeer.retrieveByPK(SimpleKey.keyFor(this.transitionActivity), connection);
        }
        return aTWorkflowTransition;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTWorkflowTransitionKey(ObjectKey key) throws TorqueException
    {

        setTransitionActivity(new Integer(((NumberKey) key).intValue()));
    }




    private TWorkflowStation aTWorkflowStationRelatedByStationEntryActivity;

    /**
     * Declares an association between this object and a TWorkflowStation object
     *
     * @param v TWorkflowStation
     * @throws TorqueException
     */
    public void setTWorkflowStationRelatedByStationEntryActivity(TWorkflowStation v) throws TorqueException
    {
        if (v == null)
        {
            setStationEntryActivity((Integer) null);
        }
        else
        {
            setStationEntryActivity(v.getObjectID());
        }
        aTWorkflowStationRelatedByStationEntryActivity = v;
    }


    /**
     * Returns the associated TWorkflowStation object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TWorkflowStation object
     * @throws TorqueException
     */
    public TWorkflowStation getTWorkflowStationRelatedByStationEntryActivity()
        throws TorqueException
    {
        if (aTWorkflowStationRelatedByStationEntryActivity == null && (!ObjectUtils.equals(this.stationEntryActivity, null)))
        {
            aTWorkflowStationRelatedByStationEntryActivity = TWorkflowStationPeer.retrieveByPK(SimpleKey.keyFor(this.stationEntryActivity));
        }
        return aTWorkflowStationRelatedByStationEntryActivity;
    }

    /**
     * Return the associated TWorkflowStation object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TWorkflowStation object
     * @throws TorqueException
     */
    public TWorkflowStation getTWorkflowStationRelatedByStationEntryActivity(Connection connection)
        throws TorqueException
    {
        if (aTWorkflowStationRelatedByStationEntryActivity == null && (!ObjectUtils.equals(this.stationEntryActivity, null)))
        {
            aTWorkflowStationRelatedByStationEntryActivity = TWorkflowStationPeer.retrieveByPK(SimpleKey.keyFor(this.stationEntryActivity), connection);
        }
        return aTWorkflowStationRelatedByStationEntryActivity;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTWorkflowStationRelatedByStationEntryActivityKey(ObjectKey key) throws TorqueException
    {

        setStationEntryActivity(new Integer(((NumberKey) key).intValue()));
    }




    private TWorkflowStation aTWorkflowStationRelatedByStationExitActivity;

    /**
     * Declares an association between this object and a TWorkflowStation object
     *
     * @param v TWorkflowStation
     * @throws TorqueException
     */
    public void setTWorkflowStationRelatedByStationExitActivity(TWorkflowStation v) throws TorqueException
    {
        if (v == null)
        {
            setStationExitActivity((Integer) null);
        }
        else
        {
            setStationExitActivity(v.getObjectID());
        }
        aTWorkflowStationRelatedByStationExitActivity = v;
    }


    /**
     * Returns the associated TWorkflowStation object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TWorkflowStation object
     * @throws TorqueException
     */
    public TWorkflowStation getTWorkflowStationRelatedByStationExitActivity()
        throws TorqueException
    {
        if (aTWorkflowStationRelatedByStationExitActivity == null && (!ObjectUtils.equals(this.stationExitActivity, null)))
        {
            aTWorkflowStationRelatedByStationExitActivity = TWorkflowStationPeer.retrieveByPK(SimpleKey.keyFor(this.stationExitActivity));
        }
        return aTWorkflowStationRelatedByStationExitActivity;
    }

    /**
     * Return the associated TWorkflowStation object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TWorkflowStation object
     * @throws TorqueException
     */
    public TWorkflowStation getTWorkflowStationRelatedByStationExitActivity(Connection connection)
        throws TorqueException
    {
        if (aTWorkflowStationRelatedByStationExitActivity == null && (!ObjectUtils.equals(this.stationExitActivity, null)))
        {
            aTWorkflowStationRelatedByStationExitActivity = TWorkflowStationPeer.retrieveByPK(SimpleKey.keyFor(this.stationExitActivity), connection);
        }
        return aTWorkflowStationRelatedByStationExitActivity;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTWorkflowStationRelatedByStationExitActivityKey(ObjectKey key) throws TorqueException
    {

        setStationExitActivity(new Integer(((NumberKey) key).intValue()));
    }




    private TWorkflowStation aTWorkflowStationRelatedByStationDoActivity;

    /**
     * Declares an association between this object and a TWorkflowStation object
     *
     * @param v TWorkflowStation
     * @throws TorqueException
     */
    public void setTWorkflowStationRelatedByStationDoActivity(TWorkflowStation v) throws TorqueException
    {
        if (v == null)
        {
            setStationDoActivity((Integer) null);
        }
        else
        {
            setStationDoActivity(v.getObjectID());
        }
        aTWorkflowStationRelatedByStationDoActivity = v;
    }


    /**
     * Returns the associated TWorkflowStation object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TWorkflowStation object
     * @throws TorqueException
     */
    public TWorkflowStation getTWorkflowStationRelatedByStationDoActivity()
        throws TorqueException
    {
        if (aTWorkflowStationRelatedByStationDoActivity == null && (!ObjectUtils.equals(this.stationDoActivity, null)))
        {
            aTWorkflowStationRelatedByStationDoActivity = TWorkflowStationPeer.retrieveByPK(SimpleKey.keyFor(this.stationDoActivity));
        }
        return aTWorkflowStationRelatedByStationDoActivity;
    }

    /**
     * Return the associated TWorkflowStation object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TWorkflowStation object
     * @throws TorqueException
     */
    public TWorkflowStation getTWorkflowStationRelatedByStationDoActivity(Connection connection)
        throws TorqueException
    {
        if (aTWorkflowStationRelatedByStationDoActivity == null && (!ObjectUtils.equals(this.stationDoActivity, null)))
        {
            aTWorkflowStationRelatedByStationDoActivity = TWorkflowStationPeer.retrieveByPK(SimpleKey.keyFor(this.stationDoActivity), connection);
        }
        return aTWorkflowStationRelatedByStationDoActivity;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTWorkflowStationRelatedByStationDoActivityKey(ObjectKey key) throws TorqueException
    {

        setStationDoActivity(new Integer(((NumberKey) key).intValue()));
    }




    private TScripts aTScripts;

    /**
     * Declares an association between this object and a TScripts object
     *
     * @param v TScripts
     * @throws TorqueException
     */
    public void setTScripts(TScripts v) throws TorqueException
    {
        if (v == null)
        {
            setGroovyScript((Integer) null);
        }
        else
        {
            setGroovyScript(v.getObjectID());
        }
        aTScripts = v;
    }


    /**
     * Returns the associated TScripts object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TScripts object
     * @throws TorqueException
     */
    public TScripts getTScripts()
        throws TorqueException
    {
        if (aTScripts == null && (!ObjectUtils.equals(this.groovyScript, null)))
        {
            aTScripts = TScriptsPeer.retrieveByPK(SimpleKey.keyFor(this.groovyScript));
        }
        return aTScripts;
    }

    /**
     * Return the associated TScripts object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TScripts object
     * @throws TorqueException
     */
    public TScripts getTScripts(Connection connection)
        throws TorqueException
    {
        if (aTScripts == null && (!ObjectUtils.equals(this.groovyScript, null)))
        {
            aTScripts = TScriptsPeer.retrieveByPK(SimpleKey.keyFor(this.groovyScript), connection);
        }
        return aTScripts;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTScriptsKey(ObjectKey key) throws TorqueException
    {

        setGroovyScript(new Integer(((NumberKey) key).intValue()));
    }




    private TPerson aTPersonRelatedByNewMan;

    /**
     * Declares an association between this object and a TPerson object
     *
     * @param v TPerson
     * @throws TorqueException
     */
    public void setTPersonRelatedByNewMan(TPerson v) throws TorqueException
    {
        if (v == null)
        {
            setNewMan((Integer) null);
        }
        else
        {
            setNewMan(v.getObjectID());
        }
        aTPersonRelatedByNewMan = v;
    }


    /**
     * Returns the associated TPerson object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPersonRelatedByNewMan()
        throws TorqueException
    {
        if (aTPersonRelatedByNewMan == null && (!ObjectUtils.equals(this.newMan, null)))
        {
            aTPersonRelatedByNewMan = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.newMan));
        }
        return aTPersonRelatedByNewMan;
    }

    /**
     * Return the associated TPerson object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPersonRelatedByNewMan(Connection connection)
        throws TorqueException
    {
        if (aTPersonRelatedByNewMan == null && (!ObjectUtils.equals(this.newMan, null)))
        {
            aTPersonRelatedByNewMan = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.newMan), connection);
        }
        return aTPersonRelatedByNewMan;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTPersonRelatedByNewManKey(ObjectKey key) throws TorqueException
    {

        setNewMan(new Integer(((NumberKey) key).intValue()));
    }




    private TPerson aTPersonRelatedByNewResp;

    /**
     * Declares an association between this object and a TPerson object
     *
     * @param v TPerson
     * @throws TorqueException
     */
    public void setTPersonRelatedByNewResp(TPerson v) throws TorqueException
    {
        if (v == null)
        {
            setNewResp((Integer) null);
        }
        else
        {
            setNewResp(v.getObjectID());
        }
        aTPersonRelatedByNewResp = v;
    }


    /**
     * Returns the associated TPerson object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPersonRelatedByNewResp()
        throws TorqueException
    {
        if (aTPersonRelatedByNewResp == null && (!ObjectUtils.equals(this.newResp, null)))
        {
            aTPersonRelatedByNewResp = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.newResp));
        }
        return aTPersonRelatedByNewResp;
    }

    /**
     * Return the associated TPerson object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPersonRelatedByNewResp(Connection connection)
        throws TorqueException
    {
        if (aTPersonRelatedByNewResp == null && (!ObjectUtils.equals(this.newResp, null)))
        {
            aTPersonRelatedByNewResp = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.newResp), connection);
        }
        return aTPersonRelatedByNewResp;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTPersonRelatedByNewRespKey(ObjectKey key) throws TorqueException
    {

        setNewResp(new Integer(((NumberKey) key).intValue()));
    }




    private TSLA aTSLA;

    /**
     * Declares an association between this object and a TSLA object
     *
     * @param v TSLA
     * @throws TorqueException
     */
    public void setTSLA(TSLA v) throws TorqueException
    {
        if (v == null)
        {
            setSla((Integer) null);
        }
        else
        {
            setSla(v.getObjectID());
        }
        aTSLA = v;
    }


    /**
     * Returns the associated TSLA object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TSLA object
     * @throws TorqueException
     */
    public TSLA getTSLA()
        throws TorqueException
    {
        if (aTSLA == null && (!ObjectUtils.equals(this.sla, null)))
        {
            aTSLA = TSLAPeer.retrieveByPK(SimpleKey.keyFor(this.sla));
        }
        return aTSLA;
    }

    /**
     * Return the associated TSLA object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TSLA object
     * @throws TorqueException
     */
    public TSLA getTSLA(Connection connection)
        throws TorqueException
    {
        if (aTSLA == null && (!ObjectUtils.equals(this.sla, null)))
        {
            aTSLA = TSLAPeer.retrieveByPK(SimpleKey.keyFor(this.sla), connection);
        }
        return aTSLA;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTSLAKey(ObjectKey key) throws TorqueException
    {

        setSla(new Integer(((NumberKey) key).intValue()));
    }




    private TScreen aTScreen;

    /**
     * Declares an association between this object and a TScreen object
     *
     * @param v TScreen
     * @throws TorqueException
     */
    public void setTScreen(TScreen v) throws TorqueException
    {
        if (v == null)
        {
            setScreen((Integer) null);
        }
        else
        {
            setScreen(v.getObjectID());
        }
        aTScreen = v;
    }


    /**
     * Returns the associated TScreen object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TScreen object
     * @throws TorqueException
     */
    public TScreen getTScreen()
        throws TorqueException
    {
        if (aTScreen == null && (!ObjectUtils.equals(this.screen, null)))
        {
            aTScreen = TScreenPeer.retrieveByPK(SimpleKey.keyFor(this.screen));
        }
        return aTScreen;
    }

    /**
     * Return the associated TScreen object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TScreen object
     * @throws TorqueException
     */
    public TScreen getTScreen(Connection connection)
        throws TorqueException
    {
        if (aTScreen == null && (!ObjectUtils.equals(this.screen, null)))
        {
            aTScreen = TScreenPeer.retrieveByPK(SimpleKey.keyFor(this.screen), connection);
        }
        return aTScreen;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTScreenKey(ObjectKey key) throws TorqueException
    {

        setScreen(new Integer(((NumberKey) key).intValue()));
    }
   


    /**
     * Collection to store aggregation of collTWfActivityContextParamss
     */
    protected List<TWfActivityContextParams> collTWfActivityContextParamss;

    /**
     * Temporary storage of collTWfActivityContextParamss to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTWfActivityContextParamss()
    {
        if (collTWfActivityContextParamss == null)
        {
            collTWfActivityContextParamss = new ArrayList<TWfActivityContextParams>();
        }
    }


    /**
     * Method called to associate a TWfActivityContextParams object to this object
     * through the TWfActivityContextParams foreign key attribute
     *
     * @param l TWfActivityContextParams
     * @throws TorqueException
     */
    public void addTWfActivityContextParams(TWfActivityContextParams l) throws TorqueException
    {
        getTWfActivityContextParamss().add(l);
        l.setTWorkflowActivity((TWorkflowActivity) this);
    }

    /**
     * Method called to associate a TWfActivityContextParams object to this object
     * through the TWfActivityContextParams foreign key attribute using connection.
     *
     * @param l TWfActivityContextParams
     * @throws TorqueException
     */
    public void addTWfActivityContextParams(TWfActivityContextParams l, Connection con) throws TorqueException
    {
        getTWfActivityContextParamss(con).add(l);
        l.setTWorkflowActivity((TWorkflowActivity) this);
    }

    /**
     * The criteria used to select the current contents of collTWfActivityContextParamss
     */
    private Criteria lastTWfActivityContextParamssCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWfActivityContextParamss(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TWfActivityContextParams> getTWfActivityContextParamss()
        throws TorqueException
    {
        if (collTWfActivityContextParamss == null)
        {
            collTWfActivityContextParamss = getTWfActivityContextParamss(new Criteria(10));
        }
        return collTWfActivityContextParamss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowActivity has previously
     * been saved, it will retrieve related TWfActivityContextParamss from storage.
     * If this TWorkflowActivity is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TWfActivityContextParams> getTWfActivityContextParamss(Criteria criteria) throws TorqueException
    {
        if (collTWfActivityContextParamss == null)
        {
            if (isNew())
            {
               collTWfActivityContextParamss = new ArrayList<TWfActivityContextParams>();
            }
            else
            {
                criteria.add(TWfActivityContextParamsPeer.WORKFLOWACTIVITY, getObjectID() );
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                criteria.add(TWfActivityContextParamsPeer.WORKFLOWACTIVITY, getObjectID());
                if (!lastTWfActivityContextParamssCriteria.equals(criteria))
                {
                    collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelect(criteria);
                }
            }
        }
        lastTWfActivityContextParamssCriteria = criteria;

        return collTWfActivityContextParamss;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTWfActivityContextParamss(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWfActivityContextParams> getTWfActivityContextParamss(Connection con) throws TorqueException
    {
        if (collTWfActivityContextParamss == null)
        {
            collTWfActivityContextParamss = getTWfActivityContextParamss(new Criteria(10), con);
        }
        return collTWfActivityContextParamss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowActivity has previously
     * been saved, it will retrieve related TWfActivityContextParamss from storage.
     * If this TWorkflowActivity is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TWfActivityContextParams> getTWfActivityContextParamss(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTWfActivityContextParamss == null)
        {
            if (isNew())
            {
               collTWfActivityContextParamss = new ArrayList<TWfActivityContextParams>();
            }
            else
            {
                 criteria.add(TWfActivityContextParamsPeer.WORKFLOWACTIVITY, getObjectID());
                 collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                 criteria.add(TWfActivityContextParamsPeer.WORKFLOWACTIVITY, getObjectID());
                 if (!lastTWfActivityContextParamssCriteria.equals(criteria))
                 {
                     collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTWfActivityContextParamssCriteria = criteria;

         return collTWfActivityContextParamss;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowActivity is new, it will return
     * an empty collection; or if this TWorkflowActivity has previously
     * been saved, it will retrieve related TWfActivityContextParamss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivity.
     */
    protected List<TWfActivityContextParams> getTWfActivityContextParamssJoinTWorkflowActivity(Criteria criteria)
        throws TorqueException
    {
        if (collTWfActivityContextParamss == null)
        {
            if (isNew())
            {
               collTWfActivityContextParamss = new ArrayList<TWfActivityContextParams>();
            }
            else
            {
                criteria.add(TWfActivityContextParamsPeer.WORKFLOWACTIVITY, getObjectID());
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTWorkflowActivity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWfActivityContextParamsPeer.WORKFLOWACTIVITY, getObjectID());
            if (!lastTWfActivityContextParamssCriteria.equals(criteria))
            {
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTWorkflowActivity(criteria);
            }
        }
        lastTWfActivityContextParamssCriteria = criteria;

        return collTWfActivityContextParamss;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowActivity is new, it will return
     * an empty collection; or if this TWorkflowActivity has previously
     * been saved, it will retrieve related TWfActivityContextParamss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivity.
     */
    protected List<TWfActivityContextParams> getTWfActivityContextParamssJoinTListType(Criteria criteria)
        throws TorqueException
    {
        if (collTWfActivityContextParamss == null)
        {
            if (isNew())
            {
               collTWfActivityContextParamss = new ArrayList<TWfActivityContextParams>();
            }
            else
            {
                criteria.add(TWfActivityContextParamsPeer.WORKFLOWACTIVITY, getObjectID());
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTListType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWfActivityContextParamsPeer.WORKFLOWACTIVITY, getObjectID());
            if (!lastTWfActivityContextParamssCriteria.equals(criteria))
            {
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTListType(criteria);
            }
        }
        lastTWfActivityContextParamssCriteria = criteria;

        return collTWfActivityContextParamss;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowActivity is new, it will return
     * an empty collection; or if this TWorkflowActivity has previously
     * been saved, it will retrieve related TWfActivityContextParamss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivity.
     */
    protected List<TWfActivityContextParams> getTWfActivityContextParamssJoinTProjectType(Criteria criteria)
        throws TorqueException
    {
        if (collTWfActivityContextParamss == null)
        {
            if (isNew())
            {
               collTWfActivityContextParamss = new ArrayList<TWfActivityContextParams>();
            }
            else
            {
                criteria.add(TWfActivityContextParamsPeer.WORKFLOWACTIVITY, getObjectID());
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTProjectType(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWfActivityContextParamsPeer.WORKFLOWACTIVITY, getObjectID());
            if (!lastTWfActivityContextParamssCriteria.equals(criteria))
            {
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTProjectType(criteria);
            }
        }
        lastTWfActivityContextParamssCriteria = criteria;

        return collTWfActivityContextParamss;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TWorkflowActivity is new, it will return
     * an empty collection; or if this TWorkflowActivity has previously
     * been saved, it will retrieve related TWfActivityContextParamss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TWorkflowActivity.
     */
    protected List<TWfActivityContextParams> getTWfActivityContextParamssJoinTProject(Criteria criteria)
        throws TorqueException
    {
        if (collTWfActivityContextParamss == null)
        {
            if (isNew())
            {
               collTWfActivityContextParamss = new ArrayList<TWfActivityContextParams>();
            }
            else
            {
                criteria.add(TWfActivityContextParamsPeer.WORKFLOWACTIVITY, getObjectID());
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTProject(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TWfActivityContextParamsPeer.WORKFLOWACTIVITY, getObjectID());
            if (!lastTWfActivityContextParamssCriteria.equals(criteria))
            {
                collTWfActivityContextParamss = TWfActivityContextParamsPeer.doSelectJoinTProject(criteria);
            }
        }
        lastTWfActivityContextParamssCriteria = criteria;

        return collTWfActivityContextParamss;
    }



        
    private static List<String> fieldNames = null;

    /**
     * Generate a list of field names.
     *
     * @return a list of field names
     */
    public static synchronized List<String> getFieldNames()
    {
        if (fieldNames == null)
        {
            fieldNames = new ArrayList<String>();
            fieldNames.add("ObjectID");
            fieldNames.add("TransitionActivity");
            fieldNames.add("StationEntryActivity");
            fieldNames.add("StationExitActivity");
            fieldNames.add("StationDoActivity");
            fieldNames.add("ActivityType");
            fieldNames.add("ActivityParams");
            fieldNames.add("GroovyScript");
            fieldNames.add("NewMan");
            fieldNames.add("NewResp");
            fieldNames.add("FieldSetterRelation");
            fieldNames.add("ParamName");
            fieldNames.add("FieldSetMode");
            fieldNames.add("SortOrder");
            fieldNames.add("Sla");
            fieldNames.add("Screen");
            fieldNames.add("Uuid");
            fieldNames = Collections.unmodifiableList(fieldNames);
        }
        return fieldNames;
    }

    /**
     * Retrieves a field from the object by field (Java) name passed in as a String.
     *
     * @param name field name
     * @return value
     */
    public Object getByName(String name)
    {
        if (name.equals("ObjectID"))
        {
            return getObjectID();
        }
        if (name.equals("TransitionActivity"))
        {
            return getTransitionActivity();
        }
        if (name.equals("StationEntryActivity"))
        {
            return getStationEntryActivity();
        }
        if (name.equals("StationExitActivity"))
        {
            return getStationExitActivity();
        }
        if (name.equals("StationDoActivity"))
        {
            return getStationDoActivity();
        }
        if (name.equals("ActivityType"))
        {
            return getActivityType();
        }
        if (name.equals("ActivityParams"))
        {
            return getActivityParams();
        }
        if (name.equals("GroovyScript"))
        {
            return getGroovyScript();
        }
        if (name.equals("NewMan"))
        {
            return getNewMan();
        }
        if (name.equals("NewResp"))
        {
            return getNewResp();
        }
        if (name.equals("FieldSetterRelation"))
        {
            return getFieldSetterRelation();
        }
        if (name.equals("ParamName"))
        {
            return getParamName();
        }
        if (name.equals("FieldSetMode"))
        {
            return getFieldSetMode();
        }
        if (name.equals("SortOrder"))
        {
            return getSortOrder();
        }
        if (name.equals("Sla"))
        {
            return getSla();
        }
        if (name.equals("Screen"))
        {
            return getScreen();
        }
        if (name.equals("Uuid"))
        {
            return getUuid();
        }
        return null;
    }

    /**
     * Set a field in the object by field (Java) name.
     *
     * @param name field name
     * @param value field value
     * @return True if value was set, false if not (invalid name / protected field).
     * @throws IllegalArgumentException if object type of value does not match field object type.
     * @throws TorqueException If a problem occurs with the set[Field] method.
     */
    public boolean setByName(String name, Object value )
        throws TorqueException, IllegalArgumentException
    {
        if (name.equals("ObjectID"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setObjectID((Integer) value);
            return true;
        }
        if (name.equals("TransitionActivity"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setTransitionActivity((Integer) value);
            return true;
        }
        if (name.equals("StationEntryActivity"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setStationEntryActivity((Integer) value);
            return true;
        }
        if (name.equals("StationExitActivity"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setStationExitActivity((Integer) value);
            return true;
        }
        if (name.equals("StationDoActivity"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setStationDoActivity((Integer) value);
            return true;
        }
        if (name.equals("ActivityType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setActivityType((Integer) value);
            return true;
        }
        if (name.equals("ActivityParams"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setActivityParams((String) value);
            return true;
        }
        if (name.equals("GroovyScript"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setGroovyScript((Integer) value);
            return true;
        }
        if (name.equals("NewMan"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNewMan((Integer) value);
            return true;
        }
        if (name.equals("NewResp"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setNewResp((Integer) value);
            return true;
        }
        if (name.equals("FieldSetterRelation"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setFieldSetterRelation((Integer) value);
            return true;
        }
        if (name.equals("ParamName"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setParamName((String) value);
            return true;
        }
        if (name.equals("FieldSetMode"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setFieldSetMode((Integer) value);
            return true;
        }
        if (name.equals("SortOrder"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSortOrder((Integer) value);
            return true;
        }
        if (name.equals("Sla"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSla((Integer) value);
            return true;
        }
        if (name.equals("Screen"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setScreen((Integer) value);
            return true;
        }
        if (name.equals("Uuid"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setUuid((String) value);
            return true;
        }
        return false;
    }

    /**
     * Retrieves a field from the object by name passed in
     * as a String.  The String must be one of the static
     * Strings defined in this Class' Peer.
     *
     * @param name peer name
     * @return value
     */
    public Object getByPeerName(String name)
    {
        if (name.equals(TWorkflowActivityPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TWorkflowActivityPeer.TRANSITIONACTIVITY))
        {
            return getTransitionActivity();
        }
        if (name.equals(TWorkflowActivityPeer.STATIONENTRYACTIVITY))
        {
            return getStationEntryActivity();
        }
        if (name.equals(TWorkflowActivityPeer.STATIONEXITACTIVITY))
        {
            return getStationExitActivity();
        }
        if (name.equals(TWorkflowActivityPeer.STATIONDOACTIVITY))
        {
            return getStationDoActivity();
        }
        if (name.equals(TWorkflowActivityPeer.ACTIVITYTYPE))
        {
            return getActivityType();
        }
        if (name.equals(TWorkflowActivityPeer.ACTIVITYPARAMS))
        {
            return getActivityParams();
        }
        if (name.equals(TWorkflowActivityPeer.GROOVYSCRIPT))
        {
            return getGroovyScript();
        }
        if (name.equals(TWorkflowActivityPeer.NEWMAN))
        {
            return getNewMan();
        }
        if (name.equals(TWorkflowActivityPeer.NEWRESP))
        {
            return getNewResp();
        }
        if (name.equals(TWorkflowActivityPeer.FIELDSETTERRELATION))
        {
            return getFieldSetterRelation();
        }
        if (name.equals(TWorkflowActivityPeer.PARAMNAME))
        {
            return getParamName();
        }
        if (name.equals(TWorkflowActivityPeer.FIELDSETMODE))
        {
            return getFieldSetMode();
        }
        if (name.equals(TWorkflowActivityPeer.SORTORDER))
        {
            return getSortOrder();
        }
        if (name.equals(TWorkflowActivityPeer.SLA))
        {
            return getSla();
        }
        if (name.equals(TWorkflowActivityPeer.SCREEN))
        {
            return getScreen();
        }
        if (name.equals(TWorkflowActivityPeer.TPUUID))
        {
            return getUuid();
        }
        return null;
    }

    /**
     * Set field values by Peer Field Name
     *
     * @param name field name
     * @param value field value
     * @return True if value was set, false if not (invalid name / protected field).
     * @throws IllegalArgumentException if object type of value does not match field object type.
     * @throws TorqueException If a problem occurs with the set[Field] method.
     */
    public boolean setByPeerName(String name, Object value)
        throws TorqueException, IllegalArgumentException
    {
      if (TWorkflowActivityPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TWorkflowActivityPeer.TRANSITIONACTIVITY.equals(name))
        {
            return setByName("TransitionActivity", value);
        }
      if (TWorkflowActivityPeer.STATIONENTRYACTIVITY.equals(name))
        {
            return setByName("StationEntryActivity", value);
        }
      if (TWorkflowActivityPeer.STATIONEXITACTIVITY.equals(name))
        {
            return setByName("StationExitActivity", value);
        }
      if (TWorkflowActivityPeer.STATIONDOACTIVITY.equals(name))
        {
            return setByName("StationDoActivity", value);
        }
      if (TWorkflowActivityPeer.ACTIVITYTYPE.equals(name))
        {
            return setByName("ActivityType", value);
        }
      if (TWorkflowActivityPeer.ACTIVITYPARAMS.equals(name))
        {
            return setByName("ActivityParams", value);
        }
      if (TWorkflowActivityPeer.GROOVYSCRIPT.equals(name))
        {
            return setByName("GroovyScript", value);
        }
      if (TWorkflowActivityPeer.NEWMAN.equals(name))
        {
            return setByName("NewMan", value);
        }
      if (TWorkflowActivityPeer.NEWRESP.equals(name))
        {
            return setByName("NewResp", value);
        }
      if (TWorkflowActivityPeer.FIELDSETTERRELATION.equals(name))
        {
            return setByName("FieldSetterRelation", value);
        }
      if (TWorkflowActivityPeer.PARAMNAME.equals(name))
        {
            return setByName("ParamName", value);
        }
      if (TWorkflowActivityPeer.FIELDSETMODE.equals(name))
        {
            return setByName("FieldSetMode", value);
        }
      if (TWorkflowActivityPeer.SORTORDER.equals(name))
        {
            return setByName("SortOrder", value);
        }
      if (TWorkflowActivityPeer.SLA.equals(name))
        {
            return setByName("Sla", value);
        }
      if (TWorkflowActivityPeer.SCREEN.equals(name))
        {
            return setByName("Screen", value);
        }
      if (TWorkflowActivityPeer.TPUUID.equals(name))
        {
            return setByName("Uuid", value);
        }
        return false;
    }

    /**
     * Retrieves a field from the object by Position as specified
     * in the xml schema.  Zero-based.
     *
     * @param pos position in xml schema
     * @return value
     */
    public Object getByPosition(int pos)
    {
        if (pos == 0)
        {
            return getObjectID();
        }
        if (pos == 1)
        {
            return getTransitionActivity();
        }
        if (pos == 2)
        {
            return getStationEntryActivity();
        }
        if (pos == 3)
        {
            return getStationExitActivity();
        }
        if (pos == 4)
        {
            return getStationDoActivity();
        }
        if (pos == 5)
        {
            return getActivityType();
        }
        if (pos == 6)
        {
            return getActivityParams();
        }
        if (pos == 7)
        {
            return getGroovyScript();
        }
        if (pos == 8)
        {
            return getNewMan();
        }
        if (pos == 9)
        {
            return getNewResp();
        }
        if (pos == 10)
        {
            return getFieldSetterRelation();
        }
        if (pos == 11)
        {
            return getParamName();
        }
        if (pos == 12)
        {
            return getFieldSetMode();
        }
        if (pos == 13)
        {
            return getSortOrder();
        }
        if (pos == 14)
        {
            return getSla();
        }
        if (pos == 15)
        {
            return getScreen();
        }
        if (pos == 16)
        {
            return getUuid();
        }
        return null;
    }

    /**
     * Set field values by its position (zero based) in the XML schema.
     *
     * @param position The field position
     * @param value field value
     * @return True if value was set, false if not (invalid position / protected field).
     * @throws IllegalArgumentException if object type of value does not match field object type.
     * @throws TorqueException If a problem occurs with the set[Field] method.
     */
    public boolean setByPosition(int position, Object value)
        throws TorqueException, IllegalArgumentException
    {
    if (position == 0)
        {
            return setByName("ObjectID", value);
        }
    if (position == 1)
        {
            return setByName("TransitionActivity", value);
        }
    if (position == 2)
        {
            return setByName("StationEntryActivity", value);
        }
    if (position == 3)
        {
            return setByName("StationExitActivity", value);
        }
    if (position == 4)
        {
            return setByName("StationDoActivity", value);
        }
    if (position == 5)
        {
            return setByName("ActivityType", value);
        }
    if (position == 6)
        {
            return setByName("ActivityParams", value);
        }
    if (position == 7)
        {
            return setByName("GroovyScript", value);
        }
    if (position == 8)
        {
            return setByName("NewMan", value);
        }
    if (position == 9)
        {
            return setByName("NewResp", value);
        }
    if (position == 10)
        {
            return setByName("FieldSetterRelation", value);
        }
    if (position == 11)
        {
            return setByName("ParamName", value);
        }
    if (position == 12)
        {
            return setByName("FieldSetMode", value);
        }
    if (position == 13)
        {
            return setByName("SortOrder", value);
        }
    if (position == 14)
        {
            return setByName("Sla", value);
        }
    if (position == 15)
        {
            return setByName("Screen", value);
        }
    if (position == 16)
        {
            return setByName("Uuid", value);
        }
        return false;
    }
     
    /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.
     *
     * @throws Exception
     */
    public void save() throws Exception
    {
        save(TWorkflowActivityPeer.DATABASE_NAME);
    }

    /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.
     * Note: this code is here because the method body is
     * auto-generated conditionally and therefore needs to be
     * in this file instead of in the super class, BaseObject.
     *
     * @param dbName
     * @throws TorqueException
     */
    public void save(String dbName) throws TorqueException
    {
        Connection con = null;
        try
        {
            con = Transaction.begin(dbName);
            save(con);
            Transaction.commit(con);
        }
        catch(TorqueException e)
        {
            Transaction.safeRollback(con);
            throw e;
        }
    }

    /** flag to prevent endless save loop, if this object is referenced
        by another object which falls in this transaction. */
    private boolean alreadyInSave = false;
    /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.  This method
     * is meant to be used as part of a transaction, otherwise use
     * the save() method and the connection details will be handled
     * internally
     *
     * @param con
     * @throws TorqueException
     */
    public void save(Connection con) throws TorqueException
    {
        if (!alreadyInSave)
        {
            alreadyInSave = true;



            // If this object has been modified, then save it to the database.
            if (isModified())
            {
                if (isNew())
                {
                    TWorkflowActivityPeer.doInsert((TWorkflowActivity) this, con);
                    setNew(false);
                }
                else
                {
                    TWorkflowActivityPeer.doUpdate((TWorkflowActivity) this, con);
                }
            }


            if (collTWfActivityContextParamss != null)
            {
                for (int i = 0; i < collTWfActivityContextParamss.size(); i++)
                {
                    ((TWfActivityContextParams) collTWfActivityContextParamss.get(i)).save(con);
                }
            }
            alreadyInSave = false;
        }
    }


    /**
     * Set the PrimaryKey using ObjectKey.
     *
     * @param key objectID ObjectKey
     */
    public void setPrimaryKey(ObjectKey key)
        throws TorqueException
    {
        setObjectID(new Integer(((NumberKey) key).intValue()));
    }

    /**
     * Set the PrimaryKey using a String.
     *
     * @param key
     */
    public void setPrimaryKey(String key) throws TorqueException
    {
        setObjectID(new Integer(key));
    }


    /**
     * returns an id that differentiates this object from others
     * of its class.
     */
    public ObjectKey getPrimaryKey()
    {
        return SimpleKey.keyFor(getObjectID());
    }
 

    /**
     * Makes a copy of this object.
     * It creates a new object filling in the simple attributes.
     * It then fills all the association collections and sets the
     * related objects to isNew=true.
     */
    public TWorkflowActivity copy() throws TorqueException
    {
        return copy(true);
    }

    /**
     * Makes a copy of this object using connection.
     * It creates a new object filling in the simple attributes.
     * It then fills all the association collections and sets the
     * related objects to isNew=true.
     *
     * @param con the database connection to read associated objects.
     */
    public TWorkflowActivity copy(Connection con) throws TorqueException
    {
        return copy(true, con);
    }

    /**
     * Makes a copy of this object.
     * It creates a new object filling in the simple attributes.
     * If the parameter deepcopy is true, it then fills all the
     * association collections and sets the related objects to
     * isNew=true.
     *
     * @param deepcopy whether to copy the associated objects.
     */
    public TWorkflowActivity copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TWorkflowActivity(), deepcopy);
    }

    /**
     * Makes a copy of this object using connection.
     * It creates a new object filling in the simple attributes.
     * If the parameter deepcopy is true, it then fills all the
     * association collections and sets the related objects to
     * isNew=true.
     *
     * @param deepcopy whether to copy the associated objects.
     * @param con the database connection to read associated objects.
     */
    public TWorkflowActivity copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TWorkflowActivity(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TWorkflowActivity copyInto(TWorkflowActivity copyObj) throws TorqueException
    {
        return copyInto(copyObj, true);
    }

  
    /**
     * Fills the copyObj with the contents of this object using connection.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     * @param con the database connection to read associated objects.
     */
    protected TWorkflowActivity copyInto(TWorkflowActivity copyObj, Connection con) throws TorqueException
    {
        return copyInto(copyObj, true, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * If deepcopy is true, The associated objects are also copied
     * and treated as new objects.
     *
     * @param copyObj the object to fill.
     * @param deepcopy whether the associated objects should be copied.
     */
    protected TWorkflowActivity copyInto(TWorkflowActivity copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setTransitionActivity(transitionActivity);
        copyObj.setStationEntryActivity(stationEntryActivity);
        copyObj.setStationExitActivity(stationExitActivity);
        copyObj.setStationDoActivity(stationDoActivity);
        copyObj.setActivityType(activityType);
        copyObj.setActivityParams(activityParams);
        copyObj.setGroovyScript(groovyScript);
        copyObj.setNewMan(newMan);
        copyObj.setNewResp(newResp);
        copyObj.setFieldSetterRelation(fieldSetterRelation);
        copyObj.setParamName(paramName);
        copyObj.setFieldSetMode(fieldSetMode);
        copyObj.setSortOrder(sortOrder);
        copyObj.setSla(sla);
        copyObj.setScreen(screen);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TWfActivityContextParams> vTWfActivityContextParamss = getTWfActivityContextParamss();
        if (vTWfActivityContextParamss != null)
        {
            for (int i = 0; i < vTWfActivityContextParamss.size(); i++)
            {
                TWfActivityContextParams obj =  vTWfActivityContextParamss.get(i);
                copyObj.addTWfActivityContextParams(obj.copy());
            }
        }
        else
        {
            copyObj.collTWfActivityContextParamss = null;
        }
        }
        return copyObj;
    }
        
    
    /**
     * Fills the copyObj with the contents of this object using connection.
     * If deepcopy is true, The associated objects are also copied
     * and treated as new objects.
     *
     * @param copyObj the object to fill.
     * @param deepcopy whether the associated objects should be copied.
     * @param con the database connection to read associated objects.
     */
    protected TWorkflowActivity copyInto(TWorkflowActivity copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setTransitionActivity(transitionActivity);
        copyObj.setStationEntryActivity(stationEntryActivity);
        copyObj.setStationExitActivity(stationExitActivity);
        copyObj.setStationDoActivity(stationDoActivity);
        copyObj.setActivityType(activityType);
        copyObj.setActivityParams(activityParams);
        copyObj.setGroovyScript(groovyScript);
        copyObj.setNewMan(newMan);
        copyObj.setNewResp(newResp);
        copyObj.setFieldSetterRelation(fieldSetterRelation);
        copyObj.setParamName(paramName);
        copyObj.setFieldSetMode(fieldSetMode);
        copyObj.setSortOrder(sortOrder);
        copyObj.setSla(sla);
        copyObj.setScreen(screen);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TWfActivityContextParams> vTWfActivityContextParamss = getTWfActivityContextParamss(con);
        if (vTWfActivityContextParamss != null)
        {
            for (int i = 0; i < vTWfActivityContextParamss.size(); i++)
            {
                TWfActivityContextParams obj =  vTWfActivityContextParamss.get(i);
                copyObj.addTWfActivityContextParams(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTWfActivityContextParamss = null;
        }
        }
        return copyObj;
    }
    
    

    /**
     * returns a peer instance associated with this om.  Since Peer classes
     * are not to have any instance attributes, this method returns the
     * same instance for all member of this class. The method could therefore
     * be static, but this would prevent one from overriding the behavior.
     */
    public TWorkflowActivityPeer getPeer()
    {
        return peer;
    }

    /**
     * Retrieves the TableMap object related to this Table data without
     * compiler warnings of using getPeer().getTableMap().
     *
     * @return The associated TableMap object.
     */
    public TableMap getTableMap() throws TorqueException
    {
        return TWorkflowActivityPeer.getTableMap();
    }

  
    /**
     * Creates a TWorkflowActivityBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TWorkflowActivityBean with the contents of this object
     */
    public TWorkflowActivityBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TWorkflowActivityBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TWorkflowActivityBean with the contents of this object
     */
    public TWorkflowActivityBean getBean(IdentityMap createdBeans)
    {
        TWorkflowActivityBean result = (TWorkflowActivityBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TWorkflowActivityBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setTransitionActivity(getTransitionActivity());
        result.setStationEntryActivity(getStationEntryActivity());
        result.setStationExitActivity(getStationExitActivity());
        result.setStationDoActivity(getStationDoActivity());
        result.setActivityType(getActivityType());
        result.setActivityParams(getActivityParams());
        result.setGroovyScript(getGroovyScript());
        result.setNewMan(getNewMan());
        result.setNewResp(getNewResp());
        result.setFieldSetterRelation(getFieldSetterRelation());
        result.setParamName(getParamName());
        result.setFieldSetMode(getFieldSetMode());
        result.setSortOrder(getSortOrder());
        result.setSla(getSla());
        result.setScreen(getScreen());
        result.setUuid(getUuid());



        if (collTWfActivityContextParamss != null)
        {
            List<TWfActivityContextParamsBean> relatedBeans = new ArrayList<TWfActivityContextParamsBean>(collTWfActivityContextParamss.size());
            for (Iterator<TWfActivityContextParams> collTWfActivityContextParamssIt = collTWfActivityContextParamss.iterator(); collTWfActivityContextParamssIt.hasNext(); )
            {
                TWfActivityContextParams related = (TWfActivityContextParams) collTWfActivityContextParamssIt.next();
                TWfActivityContextParamsBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTWfActivityContextParamsBeans(relatedBeans);
        }




        if (aTWorkflowTransition != null)
        {
            TWorkflowTransitionBean relatedBean = aTWorkflowTransition.getBean(createdBeans);
            result.setTWorkflowTransitionBean(relatedBean);
        }



        if (aTWorkflowStationRelatedByStationEntryActivity != null)
        {
            TWorkflowStationBean relatedBean = aTWorkflowStationRelatedByStationEntryActivity.getBean(createdBeans);
            result.setTWorkflowStationBeanRelatedByStationEntryActivity(relatedBean);
        }



        if (aTWorkflowStationRelatedByStationExitActivity != null)
        {
            TWorkflowStationBean relatedBean = aTWorkflowStationRelatedByStationExitActivity.getBean(createdBeans);
            result.setTWorkflowStationBeanRelatedByStationExitActivity(relatedBean);
        }



        if (aTWorkflowStationRelatedByStationDoActivity != null)
        {
            TWorkflowStationBean relatedBean = aTWorkflowStationRelatedByStationDoActivity.getBean(createdBeans);
            result.setTWorkflowStationBeanRelatedByStationDoActivity(relatedBean);
        }



        if (aTScripts != null)
        {
            TScriptsBean relatedBean = aTScripts.getBean(createdBeans);
            result.setTScriptsBean(relatedBean);
        }



        if (aTPersonRelatedByNewMan != null)
        {
            TPersonBean relatedBean = aTPersonRelatedByNewMan.getBean(createdBeans);
            result.setTPersonBeanRelatedByNewMan(relatedBean);
        }



        if (aTPersonRelatedByNewResp != null)
        {
            TPersonBean relatedBean = aTPersonRelatedByNewResp.getBean(createdBeans);
            result.setTPersonBeanRelatedByNewResp(relatedBean);
        }



        if (aTSLA != null)
        {
            TSLABean relatedBean = aTSLA.getBean(createdBeans);
            result.setTSLABean(relatedBean);
        }



        if (aTScreen != null)
        {
            TScreenBean relatedBean = aTScreen.getBean(createdBeans);
            result.setTScreenBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TWorkflowActivity with the contents
     * of a TWorkflowActivityBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TWorkflowActivityBean which contents are used to create
     *        the resulting class
     * @return an instance of TWorkflowActivity with the contents of bean
     */
    public static TWorkflowActivity createTWorkflowActivity(TWorkflowActivityBean bean)
        throws TorqueException
    {
        return createTWorkflowActivity(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TWorkflowActivity with the contents
     * of a TWorkflowActivityBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TWorkflowActivityBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TWorkflowActivity with the contents of bean
     */

    public static TWorkflowActivity createTWorkflowActivity(TWorkflowActivityBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TWorkflowActivity result = (TWorkflowActivity) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TWorkflowActivity();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setTransitionActivity(bean.getTransitionActivity());
        result.setStationEntryActivity(bean.getStationEntryActivity());
        result.setStationExitActivity(bean.getStationExitActivity());
        result.setStationDoActivity(bean.getStationDoActivity());
        result.setActivityType(bean.getActivityType());
        result.setActivityParams(bean.getActivityParams());
        result.setGroovyScript(bean.getGroovyScript());
        result.setNewMan(bean.getNewMan());
        result.setNewResp(bean.getNewResp());
        result.setFieldSetterRelation(bean.getFieldSetterRelation());
        result.setParamName(bean.getParamName());
        result.setFieldSetMode(bean.getFieldSetMode());
        result.setSortOrder(bean.getSortOrder());
        result.setSla(bean.getSla());
        result.setScreen(bean.getScreen());
        result.setUuid(bean.getUuid());



        {
            List<TWfActivityContextParamsBean> relatedBeans = bean.getTWfActivityContextParamsBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TWfActivityContextParamsBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TWfActivityContextParamsBean relatedBean =  relatedBeansIt.next();
                    TWfActivityContextParams related = TWfActivityContextParams.createTWfActivityContextParams(relatedBean, createdObjects);
                    result.addTWfActivityContextParamsFromBean(related);
                }
            }
        }




        {
            TWorkflowTransitionBean relatedBean = bean.getTWorkflowTransitionBean();
            if (relatedBean != null)
            {
                TWorkflowTransition relatedObject = TWorkflowTransition.createTWorkflowTransition(relatedBean, createdObjects);
                result.setTWorkflowTransition(relatedObject);
            }
        }



        {
            TWorkflowStationBean relatedBean = bean.getTWorkflowStationBeanRelatedByStationEntryActivity();
            if (relatedBean != null)
            {
                TWorkflowStation relatedObject = TWorkflowStation.createTWorkflowStation(relatedBean, createdObjects);
                result.setTWorkflowStationRelatedByStationEntryActivity(relatedObject);
            }
        }



        {
            TWorkflowStationBean relatedBean = bean.getTWorkflowStationBeanRelatedByStationExitActivity();
            if (relatedBean != null)
            {
                TWorkflowStation relatedObject = TWorkflowStation.createTWorkflowStation(relatedBean, createdObjects);
                result.setTWorkflowStationRelatedByStationExitActivity(relatedObject);
            }
        }



        {
            TWorkflowStationBean relatedBean = bean.getTWorkflowStationBeanRelatedByStationDoActivity();
            if (relatedBean != null)
            {
                TWorkflowStation relatedObject = TWorkflowStation.createTWorkflowStation(relatedBean, createdObjects);
                result.setTWorkflowStationRelatedByStationDoActivity(relatedObject);
            }
        }



        {
            TScriptsBean relatedBean = bean.getTScriptsBean();
            if (relatedBean != null)
            {
                TScripts relatedObject = TScripts.createTScripts(relatedBean, createdObjects);
                result.setTScripts(relatedObject);
            }
        }



        {
            TPersonBean relatedBean = bean.getTPersonBeanRelatedByNewMan();
            if (relatedBean != null)
            {
                TPerson relatedObject = TPerson.createTPerson(relatedBean, createdObjects);
                result.setTPersonRelatedByNewMan(relatedObject);
            }
        }



        {
            TPersonBean relatedBean = bean.getTPersonBeanRelatedByNewResp();
            if (relatedBean != null)
            {
                TPerson relatedObject = TPerson.createTPerson(relatedBean, createdObjects);
                result.setTPersonRelatedByNewResp(relatedObject);
            }
        }



        {
            TSLABean relatedBean = bean.getTSLABean();
            if (relatedBean != null)
            {
                TSLA relatedObject = TSLA.createTSLA(relatedBean, createdObjects);
                result.setTSLA(relatedObject);
            }
        }



        {
            TScreenBean relatedBean = bean.getTScreenBean();
            if (relatedBean != null)
            {
                TScreen relatedObject = TScreen.createTScreen(relatedBean, createdObjects);
                result.setTScreen(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TWfActivityContextParams object to this object.
     * through the TWfActivityContextParams foreign key attribute
     *
     * @param toAdd TWfActivityContextParams
     */
    protected void addTWfActivityContextParamsFromBean(TWfActivityContextParams toAdd)
    {
        initTWfActivityContextParamss();
        collTWfActivityContextParamss.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TWorkflowActivity:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("TransitionActivity = ")
           .append(getTransitionActivity())
           .append("\n");
        str.append("StationEntryActivity = ")
           .append(getStationEntryActivity())
           .append("\n");
        str.append("StationExitActivity = ")
           .append(getStationExitActivity())
           .append("\n");
        str.append("StationDoActivity = ")
           .append(getStationDoActivity())
           .append("\n");
        str.append("ActivityType = ")
           .append(getActivityType())
           .append("\n");
        str.append("ActivityParams = ")
           .append(getActivityParams())
           .append("\n");
        str.append("GroovyScript = ")
           .append(getGroovyScript())
           .append("\n");
        str.append("NewMan = ")
           .append(getNewMan())
           .append("\n");
        str.append("NewResp = ")
           .append(getNewResp())
           .append("\n");
        str.append("FieldSetterRelation = ")
           .append(getFieldSetterRelation())
           .append("\n");
        str.append("ParamName = ")
           .append(getParamName())
           .append("\n");
        str.append("FieldSetMode = ")
           .append(getFieldSetMode())
           .append("\n");
        str.append("SortOrder = ")
           .append(getSortOrder())
           .append("\n");
        str.append("Sla = ")
           .append(getSla())
           .append("\n");
        str.append("Screen = ")
           .append(getScreen())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
