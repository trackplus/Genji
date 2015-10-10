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


package com.aurel.track.beans.base;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

  import com.aurel.track.beans.*;


/**
 * You should not use this class directly.  It should not even be
 * extended; all references should be to TWorkflowActivityBean
 */
public abstract class BaseTWorkflowActivityBean
    implements Serializable
{

    /**
     * whether the bean or its underlying object has changed
     * since last reading from the database
     */
    private boolean modified = true;

    /**
     * false if the underlying object has been read from the database,
     * true otherwise
     */
    private boolean isNew = true;


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
     * sets whether the bean exists in the database
     */
    public void setNew(boolean isNew)
    {
        this.isNew = isNew;
    }

    /**
     * returns whether the bean exists in the database
     */
    public boolean isNew()
    {
        return this.isNew;
    }

    /**
     * sets whether the bean or the object it was created from
     * was modified since the object was last read from the database
     */
    public void setModified(boolean isModified)
    {
        this.modified = isModified;
    }

    /**
     * returns whether the bean or the object it was created from
     * was modified since the object was last read from the database
     */
    public boolean isModified()
    {
        return this.modified;
    }


    /**
     * Get the ObjectID
     *
     * @return Integer
     */
    public Integer getObjectID ()
    {
        return objectID;
    }

    /**
     * Set the value of ObjectID
     *
     * @param v new value
     */
    public void setObjectID(Integer v)
    {

        this.objectID = v;
        setModified(true);

    }

    /**
     * Get the TransitionActivity
     *
     * @return Integer
     */
    public Integer getTransitionActivity ()
    {
        return transitionActivity;
    }

    /**
     * Set the value of TransitionActivity
     *
     * @param v new value
     */
    public void setTransitionActivity(Integer v)
    {

        this.transitionActivity = v;
        setModified(true);

    }

    /**
     * Get the StationEntryActivity
     *
     * @return Integer
     */
    public Integer getStationEntryActivity ()
    {
        return stationEntryActivity;
    }

    /**
     * Set the value of StationEntryActivity
     *
     * @param v new value
     */
    public void setStationEntryActivity(Integer v)
    {

        this.stationEntryActivity = v;
        setModified(true);

    }

    /**
     * Get the StationExitActivity
     *
     * @return Integer
     */
    public Integer getStationExitActivity ()
    {
        return stationExitActivity;
    }

    /**
     * Set the value of StationExitActivity
     *
     * @param v new value
     */
    public void setStationExitActivity(Integer v)
    {

        this.stationExitActivity = v;
        setModified(true);

    }

    /**
     * Get the StationDoActivity
     *
     * @return Integer
     */
    public Integer getStationDoActivity ()
    {
        return stationDoActivity;
    }

    /**
     * Set the value of StationDoActivity
     *
     * @param v new value
     */
    public void setStationDoActivity(Integer v)
    {

        this.stationDoActivity = v;
        setModified(true);

    }

    /**
     * Get the ActivityType
     *
     * @return Integer
     */
    public Integer getActivityType ()
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

        this.activityType = v;
        setModified(true);

    }

    /**
     * Get the ActivityParams
     *
     * @return String
     */
    public String getActivityParams ()
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

        this.activityParams = v;
        setModified(true);

    }

    /**
     * Get the GroovyScript
     *
     * @return Integer
     */
    public Integer getGroovyScript ()
    {
        return groovyScript;
    }

    /**
     * Set the value of GroovyScript
     *
     * @param v new value
     */
    public void setGroovyScript(Integer v)
    {

        this.groovyScript = v;
        setModified(true);

    }

    /**
     * Get the NewMan
     *
     * @return Integer
     */
    public Integer getNewMan ()
    {
        return newMan;
    }

    /**
     * Set the value of NewMan
     *
     * @param v new value
     */
    public void setNewMan(Integer v)
    {

        this.newMan = v;
        setModified(true);

    }

    /**
     * Get the NewResp
     *
     * @return Integer
     */
    public Integer getNewResp ()
    {
        return newResp;
    }

    /**
     * Set the value of NewResp
     *
     * @param v new value
     */
    public void setNewResp(Integer v)
    {

        this.newResp = v;
        setModified(true);

    }

    /**
     * Get the FieldSetterRelation
     *
     * @return Integer
     */
    public Integer getFieldSetterRelation ()
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

        this.fieldSetterRelation = v;
        setModified(true);

    }

    /**
     * Get the ParamName
     *
     * @return String
     */
    public String getParamName ()
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

        this.paramName = v;
        setModified(true);

    }

    /**
     * Get the FieldSetMode
     *
     * @return Integer
     */
    public Integer getFieldSetMode ()
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

        this.fieldSetMode = v;
        setModified(true);

    }

    /**
     * Get the SortOrder
     *
     * @return Integer
     */
    public Integer getSortOrder ()
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

        this.sortOrder = v;
        setModified(true);

    }

    /**
     * Get the Sla
     *
     * @return Integer
     */
    public Integer getSla ()
    {
        return sla;
    }

    /**
     * Set the value of Sla
     *
     * @param v new value
     */
    public void setSla(Integer v)
    {

        this.sla = v;
        setModified(true);

    }

    /**
     * Get the Screen
     *
     * @return Integer
     */
    public Integer getScreen ()
    {
        return screen;
    }

    /**
     * Set the value of Screen
     *
     * @param v new value
     */
    public void setScreen(Integer v)
    {

        this.screen = v;
        setModified(true);

    }

    /**
     * Get the Uuid
     *
     * @return String
     */
    public String getUuid ()
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

        this.uuid = v;
        setModified(true);

    }

    



    private TWorkflowTransitionBean aTWorkflowTransitionBean;

    /**
     * sets an associated TWorkflowTransitionBean object
     *
     * @param v TWorkflowTransitionBean
     */
    public void setTWorkflowTransitionBean(TWorkflowTransitionBean v)
    {
        if (v == null)
        {
            setTransitionActivity((Integer) null);
        }
        else
        {
            setTransitionActivity(v.getObjectID());
        }
        aTWorkflowTransitionBean = v;
    }


    /**
     * Get the associated TWorkflowTransitionBean object
     *
     * @return the associated TWorkflowTransitionBean object
     */
    public TWorkflowTransitionBean getTWorkflowTransitionBean()
    {
        return aTWorkflowTransitionBean;
    }





    private TWorkflowStationBean aTWorkflowStationBeanRelatedByStationEntryActivity;

    /**
     * sets an associated TWorkflowStationBean object
     *
     * @param v TWorkflowStationBean
     */
    public void setTWorkflowStationBeanRelatedByStationEntryActivity(TWorkflowStationBean v)
    {
        if (v == null)
        {
            setStationEntryActivity((Integer) null);
        }
        else
        {
            setStationEntryActivity(v.getObjectID());
        }
        aTWorkflowStationBeanRelatedByStationEntryActivity = v;
    }


    /**
     * Get the associated TWorkflowStationBean object
     *
     * @return the associated TWorkflowStationBean object
     */
    public TWorkflowStationBean getTWorkflowStationBeanRelatedByStationEntryActivity()
    {
        return aTWorkflowStationBeanRelatedByStationEntryActivity;
    }





    private TWorkflowStationBean aTWorkflowStationBeanRelatedByStationExitActivity;

    /**
     * sets an associated TWorkflowStationBean object
     *
     * @param v TWorkflowStationBean
     */
    public void setTWorkflowStationBeanRelatedByStationExitActivity(TWorkflowStationBean v)
    {
        if (v == null)
        {
            setStationExitActivity((Integer) null);
        }
        else
        {
            setStationExitActivity(v.getObjectID());
        }
        aTWorkflowStationBeanRelatedByStationExitActivity = v;
    }


    /**
     * Get the associated TWorkflowStationBean object
     *
     * @return the associated TWorkflowStationBean object
     */
    public TWorkflowStationBean getTWorkflowStationBeanRelatedByStationExitActivity()
    {
        return aTWorkflowStationBeanRelatedByStationExitActivity;
    }





    private TWorkflowStationBean aTWorkflowStationBeanRelatedByStationDoActivity;

    /**
     * sets an associated TWorkflowStationBean object
     *
     * @param v TWorkflowStationBean
     */
    public void setTWorkflowStationBeanRelatedByStationDoActivity(TWorkflowStationBean v)
    {
        if (v == null)
        {
            setStationDoActivity((Integer) null);
        }
        else
        {
            setStationDoActivity(v.getObjectID());
        }
        aTWorkflowStationBeanRelatedByStationDoActivity = v;
    }


    /**
     * Get the associated TWorkflowStationBean object
     *
     * @return the associated TWorkflowStationBean object
     */
    public TWorkflowStationBean getTWorkflowStationBeanRelatedByStationDoActivity()
    {
        return aTWorkflowStationBeanRelatedByStationDoActivity;
    }





    private TScriptsBean aTScriptsBean;

    /**
     * sets an associated TScriptsBean object
     *
     * @param v TScriptsBean
     */
    public void setTScriptsBean(TScriptsBean v)
    {
        if (v == null)
        {
            setGroovyScript((Integer) null);
        }
        else
        {
            setGroovyScript(v.getObjectID());
        }
        aTScriptsBean = v;
    }


    /**
     * Get the associated TScriptsBean object
     *
     * @return the associated TScriptsBean object
     */
    public TScriptsBean getTScriptsBean()
    {
        return aTScriptsBean;
    }





    private TPersonBean aTPersonBeanRelatedByNewMan;

    /**
     * sets an associated TPersonBean object
     *
     * @param v TPersonBean
     */
    public void setTPersonBeanRelatedByNewMan(TPersonBean v)
    {
        if (v == null)
        {
            setNewMan((Integer) null);
        }
        else
        {
            setNewMan(v.getObjectID());
        }
        aTPersonBeanRelatedByNewMan = v;
    }


    /**
     * Get the associated TPersonBean object
     *
     * @return the associated TPersonBean object
     */
    public TPersonBean getTPersonBeanRelatedByNewMan()
    {
        return aTPersonBeanRelatedByNewMan;
    }





    private TPersonBean aTPersonBeanRelatedByNewResp;

    /**
     * sets an associated TPersonBean object
     *
     * @param v TPersonBean
     */
    public void setTPersonBeanRelatedByNewResp(TPersonBean v)
    {
        if (v == null)
        {
            setNewResp((Integer) null);
        }
        else
        {
            setNewResp(v.getObjectID());
        }
        aTPersonBeanRelatedByNewResp = v;
    }


    /**
     * Get the associated TPersonBean object
     *
     * @return the associated TPersonBean object
     */
    public TPersonBean getTPersonBeanRelatedByNewResp()
    {
        return aTPersonBeanRelatedByNewResp;
    }





    private TSLABean aTSLABean;

    /**
     * sets an associated TSLABean object
     *
     * @param v TSLABean
     */
    public void setTSLABean(TSLABean v)
    {
        if (v == null)
        {
            setSla((Integer) null);
        }
        else
        {
            setSla(v.getObjectID());
        }
        aTSLABean = v;
    }


    /**
     * Get the associated TSLABean object
     *
     * @return the associated TSLABean object
     */
    public TSLABean getTSLABean()
    {
        return aTSLABean;
    }





    private TScreenBean aTScreenBean;

    /**
     * sets an associated TScreenBean object
     *
     * @param v TScreenBean
     */
    public void setTScreenBean(TScreenBean v)
    {
        if (v == null)
        {
            setScreen((Integer) null);
        }
        else
        {
            setScreen(v.getObjectID());
        }
        aTScreenBean = v;
    }


    /**
     * Get the associated TScreenBean object
     *
     * @return the associated TScreenBean object
     */
    public TScreenBean getTScreenBean()
    {
        return aTScreenBean;
    }





    /**
     * Collection to store aggregation of collTWfActivityContextParamsBeans
     */
    protected List<TWfActivityContextParamsBean> collTWfActivityContextParamsBeans;

    /**
     * Returns the collection of TWfActivityContextParamsBeans
     */
    public List<TWfActivityContextParamsBean> getTWfActivityContextParamsBeans()
    {
        return collTWfActivityContextParamsBeans;
    }

    /**
     * Sets the collection of TWfActivityContextParamsBeans to the specified value
     */
    public void setTWfActivityContextParamsBeans(List<TWfActivityContextParamsBean> list)
    {
        if (list == null)
        {
            collTWfActivityContextParamsBeans = null;
        }
        else
        {
            collTWfActivityContextParamsBeans = new ArrayList<TWfActivityContextParamsBean>(list);
        }
    }

}
