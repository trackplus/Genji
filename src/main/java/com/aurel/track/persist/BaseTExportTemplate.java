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



import com.aurel.track.persist.TProject;
import com.aurel.track.persist.TProjectPeer;
import com.aurel.track.persist.TPerson;
import com.aurel.track.persist.TPersonPeer;
import com.aurel.track.persist.TReportCategory;
import com.aurel.track.persist.TReportCategoryPeer;
import com.aurel.track.persist.TExportTemplate;
import com.aurel.track.persist.TExportTemplatePeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TExportTemplateBean;
import com.aurel.track.beans.TPersonBean;
import com.aurel.track.beans.TProjectBean;
import com.aurel.track.beans.TReportCategoryBean;
import com.aurel.track.beans.TExportTemplateBean;

import com.aurel.track.beans.TTemplatePersonBean;
import com.aurel.track.beans.TReportPersonSettingsBean;
import com.aurel.track.beans.TReportSubscribeBean;


/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TExportTemplate
 */
public abstract class BaseTExportTemplate extends TpBaseObject
{
    /** The Peer class */
    private static final TExportTemplatePeer peer =
        new TExportTemplatePeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the name field */
    private String name;

    /** The value for the reportType field */
    private String reportType;

    /** The value for the exportFormat field */
    private String exportFormat;

    /** The value for the repositoryType field */
    private Integer repositoryType;

    /** The value for the description field */
    private String description;

    /** The value for the project field */
    private Integer project;

    /** The value for the person field */
    private Integer person;

    /** The value for the categoryKey field */
    private Integer categoryKey;

    /** The value for the parent field */
    private Integer parent;

    /** The value for the sortorder field */
    private Integer sortorder;

    /** The value for the deleted field */
    private String deleted = "N";

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



        // update associated TTemplatePerson
        if (collTTemplatePersons != null)
        {
            for (int i = 0; i < collTTemplatePersons.size(); i++)
            {
                ((TTemplatePerson) collTTemplatePersons.get(i))
                        .setReportTemplate(v);
            }
        }

        // update associated TReportPersonSettings
        if (collTReportPersonSettingss != null)
        {
            for (int i = 0; i < collTReportPersonSettingss.size(); i++)
            {
                ((TReportPersonSettings) collTReportPersonSettingss.get(i))
                        .setReportTemplate(v);
            }
        }

        // update associated TReportSubscribe
        if (collTReportSubscribes != null)
        {
            for (int i = 0; i < collTReportSubscribes.size(); i++)
            {
                ((TReportSubscribe) collTReportSubscribes.get(i))
                        .setReportTemplate(v);
            }
        }
    }

    /**
     * Get the Name
     *
     * @return String
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set the value of Name
     *
     * @param v new value
     */
    public void setName(String v) 
    {

        if (!ObjectUtils.equals(this.name, v))
        {
            this.name = v;
            setModified(true);
        }


    }

    /**
     * Get the ReportType
     *
     * @return String
     */
    public String getReportType()
    {
        return reportType;
    }


    /**
     * Set the value of ReportType
     *
     * @param v new value
     */
    public void setReportType(String v) 
    {

        if (!ObjectUtils.equals(this.reportType, v))
        {
            this.reportType = v;
            setModified(true);
        }


    }

    /**
     * Get the ExportFormat
     *
     * @return String
     */
    public String getExportFormat()
    {
        return exportFormat;
    }


    /**
     * Set the value of ExportFormat
     *
     * @param v new value
     */
    public void setExportFormat(String v) 
    {

        if (!ObjectUtils.equals(this.exportFormat, v))
        {
            this.exportFormat = v;
            setModified(true);
        }


    }

    /**
     * Get the RepositoryType
     *
     * @return Integer
     */
    public Integer getRepositoryType()
    {
        return repositoryType;
    }


    /**
     * Set the value of RepositoryType
     *
     * @param v new value
     */
    public void setRepositoryType(Integer v) 
    {

        if (!ObjectUtils.equals(this.repositoryType, v))
        {
            this.repositoryType = v;
            setModified(true);
        }


    }

    /**
     * Get the Description
     *
     * @return String
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set the value of Description
     *
     * @param v new value
     */
    public void setDescription(String v) 
    {

        if (!ObjectUtils.equals(this.description, v))
        {
            this.description = v;
            setModified(true);
        }


    }

    /**
     * Get the Project
     *
     * @return Integer
     */
    public Integer getProject()
    {
        return project;
    }


    /**
     * Set the value of Project
     *
     * @param v new value
     */
    public void setProject(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.project, v))
        {
            this.project = v;
            setModified(true);
        }


        if (aTProject != null && !ObjectUtils.equals(aTProject.getObjectID(), v))
        {
            aTProject = null;
        }

    }

    /**
     * Get the Person
     *
     * @return Integer
     */
    public Integer getPerson()
    {
        return person;
    }


    /**
     * Set the value of Person
     *
     * @param v new value
     */
    public void setPerson(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.person, v))
        {
            this.person = v;
            setModified(true);
        }


        if (aTPerson != null && !ObjectUtils.equals(aTPerson.getObjectID(), v))
        {
            aTPerson = null;
        }

    }

    /**
     * Get the CategoryKey
     *
     * @return Integer
     */
    public Integer getCategoryKey()
    {
        return categoryKey;
    }


    /**
     * Set the value of CategoryKey
     *
     * @param v new value
     */
    public void setCategoryKey(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.categoryKey, v))
        {
            this.categoryKey = v;
            setModified(true);
        }


        if (aTReportCategory != null && !ObjectUtils.equals(aTReportCategory.getObjectID(), v))
        {
            aTReportCategory = null;
        }

    }

    /**
     * Get the Parent
     *
     * @return Integer
     */
    public Integer getParent()
    {
        return parent;
    }


    /**
     * Set the value of Parent
     *
     * @param v new value
     */
    public void setParent(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.parent, v))
        {
            this.parent = v;
            setModified(true);
        }


        if (aTExportTemplateRelatedByParent != null && !ObjectUtils.equals(aTExportTemplateRelatedByParent.getObjectID(), v))
        {
            aTExportTemplateRelatedByParent = null;
        }

    }

    /**
     * Get the Sortorder
     *
     * @return Integer
     */
    public Integer getSortorder()
    {
        return sortorder;
    }


    /**
     * Set the value of Sortorder
     *
     * @param v new value
     */
    public void setSortorder(Integer v) 
    {

        if (!ObjectUtils.equals(this.sortorder, v))
        {
            this.sortorder = v;
            setModified(true);
        }


    }

    /**
     * Get the Deleted
     *
     * @return String
     */
    public String getDeleted()
    {
        return deleted;
    }


    /**
     * Set the value of Deleted
     *
     * @param v new value
     */
    public void setDeleted(String v) 
    {

        if (!ObjectUtils.equals(this.deleted, v))
        {
            this.deleted = v;
            setModified(true);
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

    



    private TPerson aTPerson;

    /**
     * Declares an association between this object and a TPerson object
     *
     * @param v TPerson
     * @throws TorqueException
     */
    public void setTPerson(TPerson v) throws TorqueException
    {
        if (v == null)
        {
            setPerson((Integer) null);
        }
        else
        {
            setPerson(v.getObjectID());
        }
        aTPerson = v;
    }


    /**
     * Returns the associated TPerson object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TPerson object
     * @throws TorqueException
     */
    public TPerson getTPerson()
        throws TorqueException
    {
        if (aTPerson == null && (!ObjectUtils.equals(this.person, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.person));
        }
        return aTPerson;
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
    public TPerson getTPerson(Connection connection)
        throws TorqueException
    {
        if (aTPerson == null && (!ObjectUtils.equals(this.person, null)))
        {
            aTPerson = TPersonPeer.retrieveByPK(SimpleKey.keyFor(this.person), connection);
        }
        return aTPerson;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTPersonKey(ObjectKey key) throws TorqueException
    {

        setPerson(new Integer(((NumberKey) key).intValue()));
    }




    private TProject aTProject;

    /**
     * Declares an association between this object and a TProject object
     *
     * @param v TProject
     * @throws TorqueException
     */
    public void setTProject(TProject v) throws TorqueException
    {
        if (v == null)
        {
            setProject((Integer) null);
        }
        else
        {
            setProject(v.getObjectID());
        }
        aTProject = v;
    }


    /**
     * Returns the associated TProject object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TProject object
     * @throws TorqueException
     */
    public TProject getTProject()
        throws TorqueException
    {
        if (aTProject == null && (!ObjectUtils.equals(this.project, null)))
        {
            aTProject = TProjectPeer.retrieveByPK(SimpleKey.keyFor(this.project));
        }
        return aTProject;
    }

    /**
     * Return the associated TProject object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TProject object
     * @throws TorqueException
     */
    public TProject getTProject(Connection connection)
        throws TorqueException
    {
        if (aTProject == null && (!ObjectUtils.equals(this.project, null)))
        {
            aTProject = TProjectPeer.retrieveByPK(SimpleKey.keyFor(this.project), connection);
        }
        return aTProject;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTProjectKey(ObjectKey key) throws TorqueException
    {

        setProject(new Integer(((NumberKey) key).intValue()));
    }




    private TReportCategory aTReportCategory;

    /**
     * Declares an association between this object and a TReportCategory object
     *
     * @param v TReportCategory
     * @throws TorqueException
     */
    public void setTReportCategory(TReportCategory v) throws TorqueException
    {
        if (v == null)
        {
            setCategoryKey((Integer) null);
        }
        else
        {
            setCategoryKey(v.getObjectID());
        }
        aTReportCategory = v;
    }


    /**
     * Returns the associated TReportCategory object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TReportCategory object
     * @throws TorqueException
     */
    public TReportCategory getTReportCategory()
        throws TorqueException
    {
        if (aTReportCategory == null && (!ObjectUtils.equals(this.categoryKey, null)))
        {
            aTReportCategory = TReportCategoryPeer.retrieveByPK(SimpleKey.keyFor(this.categoryKey));
        }
        return aTReportCategory;
    }

    /**
     * Return the associated TReportCategory object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TReportCategory object
     * @throws TorqueException
     */
    public TReportCategory getTReportCategory(Connection connection)
        throws TorqueException
    {
        if (aTReportCategory == null && (!ObjectUtils.equals(this.categoryKey, null)))
        {
            aTReportCategory = TReportCategoryPeer.retrieveByPK(SimpleKey.keyFor(this.categoryKey), connection);
        }
        return aTReportCategory;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTReportCategoryKey(ObjectKey key) throws TorqueException
    {

        setCategoryKey(new Integer(((NumberKey) key).intValue()));
    }




    private TExportTemplate aTExportTemplateRelatedByParent;

    /**
     * Declares an association between this object and a TExportTemplate object
     *
     * @param v TExportTemplate
     * @throws TorqueException
     */
    public void setTExportTemplateRelatedByParent(TExportTemplate v) throws TorqueException
    {
        if (v == null)
        {
            setParent((Integer) null);
        }
        else
        {
            setParent(v.getObjectID());
        }
        aTExportTemplateRelatedByParent = v;
    }


    /**
     * Returns the associated TExportTemplate object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TExportTemplate object
     * @throws TorqueException
     */
    public TExportTemplate getTExportTemplateRelatedByParent()
        throws TorqueException
    {
        if (aTExportTemplateRelatedByParent == null && (!ObjectUtils.equals(this.parent, null)))
        {
            aTExportTemplateRelatedByParent = TExportTemplatePeer.retrieveByPK(SimpleKey.keyFor(this.parent));
        }
        return aTExportTemplateRelatedByParent;
    }

    /**
     * Return the associated TExportTemplate object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TExportTemplate object
     * @throws TorqueException
     */
    public TExportTemplate getTExportTemplateRelatedByParent(Connection connection)
        throws TorqueException
    {
        if (aTExportTemplateRelatedByParent == null && (!ObjectUtils.equals(this.parent, null)))
        {
            aTExportTemplateRelatedByParent = TExportTemplatePeer.retrieveByPK(SimpleKey.keyFor(this.parent), connection);
        }
        return aTExportTemplateRelatedByParent;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTExportTemplateRelatedByParentKey(ObjectKey key) throws TorqueException
    {

        setParent(new Integer(((NumberKey) key).intValue()));
    }
   





    /**
     * Collection to store aggregation of collTTemplatePersons
     */
    protected List<TTemplatePerson> collTTemplatePersons;

    /**
     * Temporary storage of collTTemplatePersons to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTTemplatePersons()
    {
        if (collTTemplatePersons == null)
        {
            collTTemplatePersons = new ArrayList<TTemplatePerson>();
        }
    }


    /**
     * Method called to associate a TTemplatePerson object to this object
     * through the TTemplatePerson foreign key attribute
     *
     * @param l TTemplatePerson
     * @throws TorqueException
     */
    public void addTTemplatePerson(TTemplatePerson l) throws TorqueException
    {
        getTTemplatePersons().add(l);
        l.setTExportTemplate((TExportTemplate) this);
    }

    /**
     * Method called to associate a TTemplatePerson object to this object
     * through the TTemplatePerson foreign key attribute using connection.
     *
     * @param l TTemplatePerson
     * @throws TorqueException
     */
    public void addTTemplatePerson(TTemplatePerson l, Connection con) throws TorqueException
    {
        getTTemplatePersons(con).add(l);
        l.setTExportTemplate((TExportTemplate) this);
    }

    /**
     * The criteria used to select the current contents of collTTemplatePersons
     */
    private Criteria lastTTemplatePersonsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTTemplatePersons(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TTemplatePerson> getTTemplatePersons()
        throws TorqueException
    {
        if (collTTemplatePersons == null)
        {
            collTTemplatePersons = getTTemplatePersons(new Criteria(10));
        }
        return collTTemplatePersons;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TExportTemplate has previously
     * been saved, it will retrieve related TTemplatePersons from storage.
     * If this TExportTemplate is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TTemplatePerson> getTTemplatePersons(Criteria criteria) throws TorqueException
    {
        if (collTTemplatePersons == null)
        {
            if (isNew())
            {
               collTTemplatePersons = new ArrayList<TTemplatePerson>();
            }
            else
            {
                criteria.add(TTemplatePersonPeer.REPORTTEMPLATE, getObjectID() );
                collTTemplatePersons = TTemplatePersonPeer.doSelect(criteria);
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
                criteria.add(TTemplatePersonPeer.REPORTTEMPLATE, getObjectID());
                if (!lastTTemplatePersonsCriteria.equals(criteria))
                {
                    collTTemplatePersons = TTemplatePersonPeer.doSelect(criteria);
                }
            }
        }
        lastTTemplatePersonsCriteria = criteria;

        return collTTemplatePersons;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTTemplatePersons(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TTemplatePerson> getTTemplatePersons(Connection con) throws TorqueException
    {
        if (collTTemplatePersons == null)
        {
            collTTemplatePersons = getTTemplatePersons(new Criteria(10), con);
        }
        return collTTemplatePersons;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TExportTemplate has previously
     * been saved, it will retrieve related TTemplatePersons from storage.
     * If this TExportTemplate is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TTemplatePerson> getTTemplatePersons(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTTemplatePersons == null)
        {
            if (isNew())
            {
               collTTemplatePersons = new ArrayList<TTemplatePerson>();
            }
            else
            {
                 criteria.add(TTemplatePersonPeer.REPORTTEMPLATE, getObjectID());
                 collTTemplatePersons = TTemplatePersonPeer.doSelect(criteria, con);
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
                 criteria.add(TTemplatePersonPeer.REPORTTEMPLATE, getObjectID());
                 if (!lastTTemplatePersonsCriteria.equals(criteria))
                 {
                     collTTemplatePersons = TTemplatePersonPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTTemplatePersonsCriteria = criteria;

         return collTTemplatePersons;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TExportTemplate is new, it will return
     * an empty collection; or if this TExportTemplate has previously
     * been saved, it will retrieve related TTemplatePersons from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TExportTemplate.
     */
    protected List<TTemplatePerson> getTTemplatePersonsJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTTemplatePersons == null)
        {
            if (isNew())
            {
               collTTemplatePersons = new ArrayList<TTemplatePerson>();
            }
            else
            {
                criteria.add(TTemplatePersonPeer.REPORTTEMPLATE, getObjectID());
                collTTemplatePersons = TTemplatePersonPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TTemplatePersonPeer.REPORTTEMPLATE, getObjectID());
            if (!lastTTemplatePersonsCriteria.equals(criteria))
            {
                collTTemplatePersons = TTemplatePersonPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTTemplatePersonsCriteria = criteria;

        return collTTemplatePersons;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TExportTemplate is new, it will return
     * an empty collection; or if this TExportTemplate has previously
     * been saved, it will retrieve related TTemplatePersons from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TExportTemplate.
     */
    protected List<TTemplatePerson> getTTemplatePersonsJoinTExportTemplate(Criteria criteria)
        throws TorqueException
    {
        if (collTTemplatePersons == null)
        {
            if (isNew())
            {
               collTTemplatePersons = new ArrayList<TTemplatePerson>();
            }
            else
            {
                criteria.add(TTemplatePersonPeer.REPORTTEMPLATE, getObjectID());
                collTTemplatePersons = TTemplatePersonPeer.doSelectJoinTExportTemplate(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TTemplatePersonPeer.REPORTTEMPLATE, getObjectID());
            if (!lastTTemplatePersonsCriteria.equals(criteria))
            {
                collTTemplatePersons = TTemplatePersonPeer.doSelectJoinTExportTemplate(criteria);
            }
        }
        lastTTemplatePersonsCriteria = criteria;

        return collTTemplatePersons;
    }





    /**
     * Collection to store aggregation of collTReportPersonSettingss
     */
    protected List<TReportPersonSettings> collTReportPersonSettingss;

    /**
     * Temporary storage of collTReportPersonSettingss to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTReportPersonSettingss()
    {
        if (collTReportPersonSettingss == null)
        {
            collTReportPersonSettingss = new ArrayList<TReportPersonSettings>();
        }
    }


    /**
     * Method called to associate a TReportPersonSettings object to this object
     * through the TReportPersonSettings foreign key attribute
     *
     * @param l TReportPersonSettings
     * @throws TorqueException
     */
    public void addTReportPersonSettings(TReportPersonSettings l) throws TorqueException
    {
        getTReportPersonSettingss().add(l);
        l.setTExportTemplate((TExportTemplate) this);
    }

    /**
     * Method called to associate a TReportPersonSettings object to this object
     * through the TReportPersonSettings foreign key attribute using connection.
     *
     * @param l TReportPersonSettings
     * @throws TorqueException
     */
    public void addTReportPersonSettings(TReportPersonSettings l, Connection con) throws TorqueException
    {
        getTReportPersonSettingss(con).add(l);
        l.setTExportTemplate((TExportTemplate) this);
    }

    /**
     * The criteria used to select the current contents of collTReportPersonSettingss
     */
    private Criteria lastTReportPersonSettingssCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTReportPersonSettingss(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TReportPersonSettings> getTReportPersonSettingss()
        throws TorqueException
    {
        if (collTReportPersonSettingss == null)
        {
            collTReportPersonSettingss = getTReportPersonSettingss(new Criteria(10));
        }
        return collTReportPersonSettingss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TExportTemplate has previously
     * been saved, it will retrieve related TReportPersonSettingss from storage.
     * If this TExportTemplate is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TReportPersonSettings> getTReportPersonSettingss(Criteria criteria) throws TorqueException
    {
        if (collTReportPersonSettingss == null)
        {
            if (isNew())
            {
               collTReportPersonSettingss = new ArrayList<TReportPersonSettings>();
            }
            else
            {
                criteria.add(TReportPersonSettingsPeer.REPORTTEMPLATE, getObjectID() );
                collTReportPersonSettingss = TReportPersonSettingsPeer.doSelect(criteria);
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
                criteria.add(TReportPersonSettingsPeer.REPORTTEMPLATE, getObjectID());
                if (!lastTReportPersonSettingssCriteria.equals(criteria))
                {
                    collTReportPersonSettingss = TReportPersonSettingsPeer.doSelect(criteria);
                }
            }
        }
        lastTReportPersonSettingssCriteria = criteria;

        return collTReportPersonSettingss;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTReportPersonSettingss(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TReportPersonSettings> getTReportPersonSettingss(Connection con) throws TorqueException
    {
        if (collTReportPersonSettingss == null)
        {
            collTReportPersonSettingss = getTReportPersonSettingss(new Criteria(10), con);
        }
        return collTReportPersonSettingss;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TExportTemplate has previously
     * been saved, it will retrieve related TReportPersonSettingss from storage.
     * If this TExportTemplate is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TReportPersonSettings> getTReportPersonSettingss(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTReportPersonSettingss == null)
        {
            if (isNew())
            {
               collTReportPersonSettingss = new ArrayList<TReportPersonSettings>();
            }
            else
            {
                 criteria.add(TReportPersonSettingsPeer.REPORTTEMPLATE, getObjectID());
                 collTReportPersonSettingss = TReportPersonSettingsPeer.doSelect(criteria, con);
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
                 criteria.add(TReportPersonSettingsPeer.REPORTTEMPLATE, getObjectID());
                 if (!lastTReportPersonSettingssCriteria.equals(criteria))
                 {
                     collTReportPersonSettingss = TReportPersonSettingsPeer.doSelect(criteria, con);
                 }
             }
         }
         lastTReportPersonSettingssCriteria = criteria;

         return collTReportPersonSettingss;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TExportTemplate is new, it will return
     * an empty collection; or if this TExportTemplate has previously
     * been saved, it will retrieve related TReportPersonSettingss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TExportTemplate.
     */
    protected List<TReportPersonSettings> getTReportPersonSettingssJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTReportPersonSettingss == null)
        {
            if (isNew())
            {
               collTReportPersonSettingss = new ArrayList<TReportPersonSettings>();
            }
            else
            {
                criteria.add(TReportPersonSettingsPeer.REPORTTEMPLATE, getObjectID());
                collTReportPersonSettingss = TReportPersonSettingsPeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TReportPersonSettingsPeer.REPORTTEMPLATE, getObjectID());
            if (!lastTReportPersonSettingssCriteria.equals(criteria))
            {
                collTReportPersonSettingss = TReportPersonSettingsPeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTReportPersonSettingssCriteria = criteria;

        return collTReportPersonSettingss;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TExportTemplate is new, it will return
     * an empty collection; or if this TExportTemplate has previously
     * been saved, it will retrieve related TReportPersonSettingss from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TExportTemplate.
     */
    protected List<TReportPersonSettings> getTReportPersonSettingssJoinTExportTemplate(Criteria criteria)
        throws TorqueException
    {
        if (collTReportPersonSettingss == null)
        {
            if (isNew())
            {
               collTReportPersonSettingss = new ArrayList<TReportPersonSettings>();
            }
            else
            {
                criteria.add(TReportPersonSettingsPeer.REPORTTEMPLATE, getObjectID());
                collTReportPersonSettingss = TReportPersonSettingsPeer.doSelectJoinTExportTemplate(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TReportPersonSettingsPeer.REPORTTEMPLATE, getObjectID());
            if (!lastTReportPersonSettingssCriteria.equals(criteria))
            {
                collTReportPersonSettingss = TReportPersonSettingsPeer.doSelectJoinTExportTemplate(criteria);
            }
        }
        lastTReportPersonSettingssCriteria = criteria;

        return collTReportPersonSettingss;
    }





    /**
     * Collection to store aggregation of collTReportSubscribes
     */
    protected List<TReportSubscribe> collTReportSubscribes;

    /**
     * Temporary storage of collTReportSubscribes to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initTReportSubscribes()
    {
        if (collTReportSubscribes == null)
        {
            collTReportSubscribes = new ArrayList<TReportSubscribe>();
        }
    }


    /**
     * Method called to associate a TReportSubscribe object to this object
     * through the TReportSubscribe foreign key attribute
     *
     * @param l TReportSubscribe
     * @throws TorqueException
     */
    public void addTReportSubscribe(TReportSubscribe l) throws TorqueException
    {
        getTReportSubscribes().add(l);
        l.setTExportTemplate((TExportTemplate) this);
    }

    /**
     * Method called to associate a TReportSubscribe object to this object
     * through the TReportSubscribe foreign key attribute using connection.
     *
     * @param l TReportSubscribe
     * @throws TorqueException
     */
    public void addTReportSubscribe(TReportSubscribe l, Connection con) throws TorqueException
    {
        getTReportSubscribes(con).add(l);
        l.setTExportTemplate((TExportTemplate) this);
    }

    /**
     * The criteria used to select the current contents of collTReportSubscribes
     */
    private Criteria lastTReportSubscribesCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTReportSubscribes(new Criteria())
     *
     * @return the collection of associated objects
     * @throws TorqueException
     */
    public List<TReportSubscribe> getTReportSubscribes()
        throws TorqueException
    {
        if (collTReportSubscribes == null)
        {
            collTReportSubscribes = getTReportSubscribes(new Criteria(10));
        }
        return collTReportSubscribes;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TExportTemplate has previously
     * been saved, it will retrieve related TReportSubscribes from storage.
     * If this TExportTemplate is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List<TReportSubscribe> getTReportSubscribes(Criteria criteria) throws TorqueException
    {
        if (collTReportSubscribes == null)
        {
            if (isNew())
            {
               collTReportSubscribes = new ArrayList<TReportSubscribe>();
            }
            else
            {
                criteria.add(TReportSubscribePeer.REPORTTEMPLATE, getObjectID() );
                collTReportSubscribes = TReportSubscribePeer.doSelect(criteria);
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
                criteria.add(TReportSubscribePeer.REPORTTEMPLATE, getObjectID());
                if (!lastTReportSubscribesCriteria.equals(criteria))
                {
                    collTReportSubscribes = TReportSubscribePeer.doSelect(criteria);
                }
            }
        }
        lastTReportSubscribesCriteria = criteria;

        return collTReportSubscribes;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getTReportSubscribes(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TReportSubscribe> getTReportSubscribes(Connection con) throws TorqueException
    {
        if (collTReportSubscribes == null)
        {
            collTReportSubscribes = getTReportSubscribes(new Criteria(10), con);
        }
        return collTReportSubscribes;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TExportTemplate has previously
     * been saved, it will retrieve related TReportSubscribes from storage.
     * If this TExportTemplate is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List<TReportSubscribe> getTReportSubscribes(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collTReportSubscribes == null)
        {
            if (isNew())
            {
               collTReportSubscribes = new ArrayList<TReportSubscribe>();
            }
            else
            {
                 criteria.add(TReportSubscribePeer.REPORTTEMPLATE, getObjectID());
                 collTReportSubscribes = TReportSubscribePeer.doSelect(criteria, con);
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
                 criteria.add(TReportSubscribePeer.REPORTTEMPLATE, getObjectID());
                 if (!lastTReportSubscribesCriteria.equals(criteria))
                 {
                     collTReportSubscribes = TReportSubscribePeer.doSelect(criteria, con);
                 }
             }
         }
         lastTReportSubscribesCriteria = criteria;

         return collTReportSubscribes;
     }











    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TExportTemplate is new, it will return
     * an empty collection; or if this TExportTemplate has previously
     * been saved, it will retrieve related TReportSubscribes from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TExportTemplate.
     */
    protected List<TReportSubscribe> getTReportSubscribesJoinTPerson(Criteria criteria)
        throws TorqueException
    {
        if (collTReportSubscribes == null)
        {
            if (isNew())
            {
               collTReportSubscribes = new ArrayList<TReportSubscribe>();
            }
            else
            {
                criteria.add(TReportSubscribePeer.REPORTTEMPLATE, getObjectID());
                collTReportSubscribes = TReportSubscribePeer.doSelectJoinTPerson(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TReportSubscribePeer.REPORTTEMPLATE, getObjectID());
            if (!lastTReportSubscribesCriteria.equals(criteria))
            {
                collTReportSubscribes = TReportSubscribePeer.doSelectJoinTPerson(criteria);
            }
        }
        lastTReportSubscribesCriteria = criteria;

        return collTReportSubscribes;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TExportTemplate is new, it will return
     * an empty collection; or if this TExportTemplate has previously
     * been saved, it will retrieve related TReportSubscribes from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TExportTemplate.
     */
    protected List<TReportSubscribe> getTReportSubscribesJoinTRecurrencePattern(Criteria criteria)
        throws TorqueException
    {
        if (collTReportSubscribes == null)
        {
            if (isNew())
            {
               collTReportSubscribes = new ArrayList<TReportSubscribe>();
            }
            else
            {
                criteria.add(TReportSubscribePeer.REPORTTEMPLATE, getObjectID());
                collTReportSubscribes = TReportSubscribePeer.doSelectJoinTRecurrencePattern(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TReportSubscribePeer.REPORTTEMPLATE, getObjectID());
            if (!lastTReportSubscribesCriteria.equals(criteria))
            {
                collTReportSubscribes = TReportSubscribePeer.doSelectJoinTRecurrencePattern(criteria);
            }
        }
        lastTReportSubscribesCriteria = criteria;

        return collTReportSubscribes;
    }









    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this TExportTemplate is new, it will return
     * an empty collection; or if this TExportTemplate has previously
     * been saved, it will retrieve related TReportSubscribes from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in TExportTemplate.
     */
    protected List<TReportSubscribe> getTReportSubscribesJoinTExportTemplate(Criteria criteria)
        throws TorqueException
    {
        if (collTReportSubscribes == null)
        {
            if (isNew())
            {
               collTReportSubscribes = new ArrayList<TReportSubscribe>();
            }
            else
            {
                criteria.add(TReportSubscribePeer.REPORTTEMPLATE, getObjectID());
                collTReportSubscribes = TReportSubscribePeer.doSelectJoinTExportTemplate(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            criteria.add(TReportSubscribePeer.REPORTTEMPLATE, getObjectID());
            if (!lastTReportSubscribesCriteria.equals(criteria))
            {
                collTReportSubscribes = TReportSubscribePeer.doSelectJoinTExportTemplate(criteria);
            }
        }
        lastTReportSubscribesCriteria = criteria;

        return collTReportSubscribes;
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
            fieldNames.add("Name");
            fieldNames.add("ReportType");
            fieldNames.add("ExportFormat");
            fieldNames.add("RepositoryType");
            fieldNames.add("Description");
            fieldNames.add("Project");
            fieldNames.add("Person");
            fieldNames.add("CategoryKey");
            fieldNames.add("Parent");
            fieldNames.add("Sortorder");
            fieldNames.add("Deleted");
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
        if (name.equals("Name"))
        {
            return getName();
        }
        if (name.equals("ReportType"))
        {
            return getReportType();
        }
        if (name.equals("ExportFormat"))
        {
            return getExportFormat();
        }
        if (name.equals("RepositoryType"))
        {
            return getRepositoryType();
        }
        if (name.equals("Description"))
        {
            return getDescription();
        }
        if (name.equals("Project"))
        {
            return getProject();
        }
        if (name.equals("Person"))
        {
            return getPerson();
        }
        if (name.equals("CategoryKey"))
        {
            return getCategoryKey();
        }
        if (name.equals("Parent"))
        {
            return getParent();
        }
        if (name.equals("Sortorder"))
        {
            return getSortorder();
        }
        if (name.equals("Deleted"))
        {
            return getDeleted();
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
        if (name.equals("Name"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setName((String) value);
            return true;
        }
        if (name.equals("ReportType"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setReportType((String) value);
            return true;
        }
        if (name.equals("ExportFormat"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setExportFormat((String) value);
            return true;
        }
        if (name.equals("RepositoryType"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRepositoryType((Integer) value);
            return true;
        }
        if (name.equals("Description"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDescription((String) value);
            return true;
        }
        if (name.equals("Project"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setProject((Integer) value);
            return true;
        }
        if (name.equals("Person"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setPerson((Integer) value);
            return true;
        }
        if (name.equals("CategoryKey"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setCategoryKey((Integer) value);
            return true;
        }
        if (name.equals("Parent"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setParent((Integer) value);
            return true;
        }
        if (name.equals("Sortorder"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setSortorder((Integer) value);
            return true;
        }
        if (name.equals("Deleted"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setDeleted((String) value);
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
        if (name.equals(TExportTemplatePeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TExportTemplatePeer.NAME))
        {
            return getName();
        }
        if (name.equals(TExportTemplatePeer.REPORTTYPE))
        {
            return getReportType();
        }
        if (name.equals(TExportTemplatePeer.EXPORTFORMAT))
        {
            return getExportFormat();
        }
        if (name.equals(TExportTemplatePeer.REPOSITORYTYPE))
        {
            return getRepositoryType();
        }
        if (name.equals(TExportTemplatePeer.DESCRIPTION))
        {
            return getDescription();
        }
        if (name.equals(TExportTemplatePeer.PROJECT))
        {
            return getProject();
        }
        if (name.equals(TExportTemplatePeer.PERSON))
        {
            return getPerson();
        }
        if (name.equals(TExportTemplatePeer.CATEGORYKEY))
        {
            return getCategoryKey();
        }
        if (name.equals(TExportTemplatePeer.PARENT))
        {
            return getParent();
        }
        if (name.equals(TExportTemplatePeer.SORTORDER))
        {
            return getSortorder();
        }
        if (name.equals(TExportTemplatePeer.DELETED))
        {
            return getDeleted();
        }
        if (name.equals(TExportTemplatePeer.TPUUID))
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
      if (TExportTemplatePeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TExportTemplatePeer.NAME.equals(name))
        {
            return setByName("Name", value);
        }
      if (TExportTemplatePeer.REPORTTYPE.equals(name))
        {
            return setByName("ReportType", value);
        }
      if (TExportTemplatePeer.EXPORTFORMAT.equals(name))
        {
            return setByName("ExportFormat", value);
        }
      if (TExportTemplatePeer.REPOSITORYTYPE.equals(name))
        {
            return setByName("RepositoryType", value);
        }
      if (TExportTemplatePeer.DESCRIPTION.equals(name))
        {
            return setByName("Description", value);
        }
      if (TExportTemplatePeer.PROJECT.equals(name))
        {
            return setByName("Project", value);
        }
      if (TExportTemplatePeer.PERSON.equals(name))
        {
            return setByName("Person", value);
        }
      if (TExportTemplatePeer.CATEGORYKEY.equals(name))
        {
            return setByName("CategoryKey", value);
        }
      if (TExportTemplatePeer.PARENT.equals(name))
        {
            return setByName("Parent", value);
        }
      if (TExportTemplatePeer.SORTORDER.equals(name))
        {
            return setByName("Sortorder", value);
        }
      if (TExportTemplatePeer.DELETED.equals(name))
        {
            return setByName("Deleted", value);
        }
      if (TExportTemplatePeer.TPUUID.equals(name))
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
            return getName();
        }
        if (pos == 2)
        {
            return getReportType();
        }
        if (pos == 3)
        {
            return getExportFormat();
        }
        if (pos == 4)
        {
            return getRepositoryType();
        }
        if (pos == 5)
        {
            return getDescription();
        }
        if (pos == 6)
        {
            return getProject();
        }
        if (pos == 7)
        {
            return getPerson();
        }
        if (pos == 8)
        {
            return getCategoryKey();
        }
        if (pos == 9)
        {
            return getParent();
        }
        if (pos == 10)
        {
            return getSortorder();
        }
        if (pos == 11)
        {
            return getDeleted();
        }
        if (pos == 12)
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
            return setByName("Name", value);
        }
    if (position == 2)
        {
            return setByName("ReportType", value);
        }
    if (position == 3)
        {
            return setByName("ExportFormat", value);
        }
    if (position == 4)
        {
            return setByName("RepositoryType", value);
        }
    if (position == 5)
        {
            return setByName("Description", value);
        }
    if (position == 6)
        {
            return setByName("Project", value);
        }
    if (position == 7)
        {
            return setByName("Person", value);
        }
    if (position == 8)
        {
            return setByName("CategoryKey", value);
        }
    if (position == 9)
        {
            return setByName("Parent", value);
        }
    if (position == 10)
        {
            return setByName("Sortorder", value);
        }
    if (position == 11)
        {
            return setByName("Deleted", value);
        }
    if (position == 12)
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
        save(TExportTemplatePeer.DATABASE_NAME);
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
                    TExportTemplatePeer.doInsert((TExportTemplate) this, con);
                    setNew(false);
                }
                else
                {
                    TExportTemplatePeer.doUpdate((TExportTemplate) this, con);
                }
            }


            if (collTTemplatePersons != null)
            {
                for (int i = 0; i < collTTemplatePersons.size(); i++)
                {
                    ((TTemplatePerson) collTTemplatePersons.get(i)).save(con);
                }
            }

            if (collTReportPersonSettingss != null)
            {
                for (int i = 0; i < collTReportPersonSettingss.size(); i++)
                {
                    ((TReportPersonSettings) collTReportPersonSettingss.get(i)).save(con);
                }
            }

            if (collTReportSubscribes != null)
            {
                for (int i = 0; i < collTReportSubscribes.size(); i++)
                {
                    ((TReportSubscribe) collTReportSubscribes.get(i)).save(con);
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
    public TExportTemplate copy() throws TorqueException
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
    public TExportTemplate copy(Connection con) throws TorqueException
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
    public TExportTemplate copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TExportTemplate(), deepcopy);
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
    public TExportTemplate copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TExportTemplate(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TExportTemplate copyInto(TExportTemplate copyObj) throws TorqueException
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
    protected TExportTemplate copyInto(TExportTemplate copyObj, Connection con) throws TorqueException
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
    protected TExportTemplate copyInto(TExportTemplate copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setReportType(reportType);
        copyObj.setExportFormat(exportFormat);
        copyObj.setRepositoryType(repositoryType);
        copyObj.setDescription(description);
        copyObj.setProject(project);
        copyObj.setPerson(person);
        copyObj.setCategoryKey(categoryKey);
        copyObj.setParent(parent);
        copyObj.setSortorder(sortorder);
        copyObj.setDeleted(deleted);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TTemplatePerson> vTTemplatePersons = getTTemplatePersons();
        if (vTTemplatePersons != null)
        {
            for (int i = 0; i < vTTemplatePersons.size(); i++)
            {
                TTemplatePerson obj =  vTTemplatePersons.get(i);
                copyObj.addTTemplatePerson(obj.copy());
            }
        }
        else
        {
            copyObj.collTTemplatePersons = null;
        }


        List<TReportPersonSettings> vTReportPersonSettingss = getTReportPersonSettingss();
        if (vTReportPersonSettingss != null)
        {
            for (int i = 0; i < vTReportPersonSettingss.size(); i++)
            {
                TReportPersonSettings obj =  vTReportPersonSettingss.get(i);
                copyObj.addTReportPersonSettings(obj.copy());
            }
        }
        else
        {
            copyObj.collTReportPersonSettingss = null;
        }


        List<TReportSubscribe> vTReportSubscribes = getTReportSubscribes();
        if (vTReportSubscribes != null)
        {
            for (int i = 0; i < vTReportSubscribes.size(); i++)
            {
                TReportSubscribe obj =  vTReportSubscribes.get(i);
                copyObj.addTReportSubscribe(obj.copy());
            }
        }
        else
        {
            copyObj.collTReportSubscribes = null;
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
    protected TExportTemplate copyInto(TExportTemplate copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setReportType(reportType);
        copyObj.setExportFormat(exportFormat);
        copyObj.setRepositoryType(repositoryType);
        copyObj.setDescription(description);
        copyObj.setProject(project);
        copyObj.setPerson(person);
        copyObj.setCategoryKey(categoryKey);
        copyObj.setParent(parent);
        copyObj.setSortorder(sortorder);
        copyObj.setDeleted(deleted);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {


        List<TTemplatePerson> vTTemplatePersons = getTTemplatePersons(con);
        if (vTTemplatePersons != null)
        {
            for (int i = 0; i < vTTemplatePersons.size(); i++)
            {
                TTemplatePerson obj =  vTTemplatePersons.get(i);
                copyObj.addTTemplatePerson(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTTemplatePersons = null;
        }


        List<TReportPersonSettings> vTReportPersonSettingss = getTReportPersonSettingss(con);
        if (vTReportPersonSettingss != null)
        {
            for (int i = 0; i < vTReportPersonSettingss.size(); i++)
            {
                TReportPersonSettings obj =  vTReportPersonSettingss.get(i);
                copyObj.addTReportPersonSettings(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTReportPersonSettingss = null;
        }


        List<TReportSubscribe> vTReportSubscribes = getTReportSubscribes(con);
        if (vTReportSubscribes != null)
        {
            for (int i = 0; i < vTReportSubscribes.size(); i++)
            {
                TReportSubscribe obj =  vTReportSubscribes.get(i);
                copyObj.addTReportSubscribe(obj.copy(con), con);
            }
        }
        else
        {
            copyObj.collTReportSubscribes = null;
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
    public TExportTemplatePeer getPeer()
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
        return TExportTemplatePeer.getTableMap();
    }

  
    /**
     * Creates a TExportTemplateBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TExportTemplateBean with the contents of this object
     */
    public TExportTemplateBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TExportTemplateBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TExportTemplateBean with the contents of this object
     */
    public TExportTemplateBean getBean(IdentityMap createdBeans)
    {
        TExportTemplateBean result = (TExportTemplateBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TExportTemplateBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setName(getName());
        result.setReportType(getReportType());
        result.setExportFormat(getExportFormat());
        result.setRepositoryType(getRepositoryType());
        result.setDescription(getDescription());
        result.setProject(getProject());
        result.setPerson(getPerson());
        result.setCategoryKey(getCategoryKey());
        result.setParent(getParent());
        result.setSortorder(getSortorder());
        result.setDeleted(getDeleted());
        result.setUuid(getUuid());



        if (collTTemplatePersons != null)
        {
            List<TTemplatePersonBean> relatedBeans = new ArrayList<TTemplatePersonBean>(collTTemplatePersons.size());
            for (Iterator<TTemplatePerson> collTTemplatePersonsIt = collTTemplatePersons.iterator(); collTTemplatePersonsIt.hasNext(); )
            {
                TTemplatePerson related = (TTemplatePerson) collTTemplatePersonsIt.next();
                TTemplatePersonBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTTemplatePersonBeans(relatedBeans);
        }


        if (collTReportPersonSettingss != null)
        {
            List<TReportPersonSettingsBean> relatedBeans = new ArrayList<TReportPersonSettingsBean>(collTReportPersonSettingss.size());
            for (Iterator<TReportPersonSettings> collTReportPersonSettingssIt = collTReportPersonSettingss.iterator(); collTReportPersonSettingssIt.hasNext(); )
            {
                TReportPersonSettings related = (TReportPersonSettings) collTReportPersonSettingssIt.next();
                TReportPersonSettingsBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTReportPersonSettingsBeans(relatedBeans);
        }


        if (collTReportSubscribes != null)
        {
            List<TReportSubscribeBean> relatedBeans = new ArrayList<TReportSubscribeBean>(collTReportSubscribes.size());
            for (Iterator<TReportSubscribe> collTReportSubscribesIt = collTReportSubscribes.iterator(); collTReportSubscribesIt.hasNext(); )
            {
                TReportSubscribe related = (TReportSubscribe) collTReportSubscribesIt.next();
                TReportSubscribeBean relatedBean = related.getBean(createdBeans);
                relatedBeans.add(relatedBean);
            }
            result.setTReportSubscribeBeans(relatedBeans);
        }




        if (aTPerson != null)
        {
            TPersonBean relatedBean = aTPerson.getBean(createdBeans);
            result.setTPersonBean(relatedBean);
        }



        if (aTProject != null)
        {
            TProjectBean relatedBean = aTProject.getBean(createdBeans);
            result.setTProjectBean(relatedBean);
        }



        if (aTReportCategory != null)
        {
            TReportCategoryBean relatedBean = aTReportCategory.getBean(createdBeans);
            result.setTReportCategoryBean(relatedBean);
        }



        if (aTExportTemplateRelatedByParent != null)
        {
            TExportTemplateBean relatedBean = aTExportTemplateRelatedByParent.getBean(createdBeans);
            result.setTExportTemplateBeanRelatedByParent(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TExportTemplate with the contents
     * of a TExportTemplateBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TExportTemplateBean which contents are used to create
     *        the resulting class
     * @return an instance of TExportTemplate with the contents of bean
     */
    public static TExportTemplate createTExportTemplate(TExportTemplateBean bean)
        throws TorqueException
    {
        return createTExportTemplate(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TExportTemplate with the contents
     * of a TExportTemplateBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TExportTemplateBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TExportTemplate with the contents of bean
     */

    public static TExportTemplate createTExportTemplate(TExportTemplateBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TExportTemplate result = (TExportTemplate) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TExportTemplate();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setName(bean.getName());
        result.setReportType(bean.getReportType());
        result.setExportFormat(bean.getExportFormat());
        result.setRepositoryType(bean.getRepositoryType());
        result.setDescription(bean.getDescription());
        result.setProject(bean.getProject());
        result.setPerson(bean.getPerson());
        result.setCategoryKey(bean.getCategoryKey());
        result.setParent(bean.getParent());
        result.setSortorder(bean.getSortorder());
        result.setDeleted(bean.getDeleted());
        result.setUuid(bean.getUuid());



        {
            List<TTemplatePersonBean> relatedBeans = bean.getTTemplatePersonBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TTemplatePersonBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TTemplatePersonBean relatedBean =  relatedBeansIt.next();
                    TTemplatePerson related = TTemplatePerson.createTTemplatePerson(relatedBean, createdObjects);
                    result.addTTemplatePersonFromBean(related);
                }
            }
        }


        {
            List<TReportPersonSettingsBean> relatedBeans = bean.getTReportPersonSettingsBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TReportPersonSettingsBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TReportPersonSettingsBean relatedBean =  relatedBeansIt.next();
                    TReportPersonSettings related = TReportPersonSettings.createTReportPersonSettings(relatedBean, createdObjects);
                    result.addTReportPersonSettingsFromBean(related);
                }
            }
        }


        {
            List<TReportSubscribeBean> relatedBeans = bean.getTReportSubscribeBeans();
            if (relatedBeans != null)
            {
                for (Iterator<TReportSubscribeBean> relatedBeansIt = relatedBeans.iterator(); relatedBeansIt.hasNext(); )
                {
                    TReportSubscribeBean relatedBean =  relatedBeansIt.next();
                    TReportSubscribe related = TReportSubscribe.createTReportSubscribe(relatedBean, createdObjects);
                    result.addTReportSubscribeFromBean(related);
                }
            }
        }




        {
            TPersonBean relatedBean = bean.getTPersonBean();
            if (relatedBean != null)
            {
                TPerson relatedObject = TPerson.createTPerson(relatedBean, createdObjects);
                result.setTPerson(relatedObject);
            }
        }



        {
            TProjectBean relatedBean = bean.getTProjectBean();
            if (relatedBean != null)
            {
                TProject relatedObject = TProject.createTProject(relatedBean, createdObjects);
                result.setTProject(relatedObject);
            }
        }



        {
            TReportCategoryBean relatedBean = bean.getTReportCategoryBean();
            if (relatedBean != null)
            {
                TReportCategory relatedObject = TReportCategory.createTReportCategory(relatedBean, createdObjects);
                result.setTReportCategory(relatedObject);
            }
        }



        {
            TExportTemplateBean relatedBean = bean.getTExportTemplateBeanRelatedByParent();
            if (relatedBean != null)
            {
                TExportTemplate relatedObject = TExportTemplate.createTExportTemplate(relatedBean, createdObjects);
                result.setTExportTemplateRelatedByParent(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    /**
     * Method called to associate a TTemplatePerson object to this object.
     * through the TTemplatePerson foreign key attribute
     *
     * @param toAdd TTemplatePerson
     */
    protected void addTTemplatePersonFromBean(TTemplatePerson toAdd)
    {
        initTTemplatePersons();
        collTTemplatePersons.add(toAdd);
    }


    /**
     * Method called to associate a TReportPersonSettings object to this object.
     * through the TReportPersonSettings foreign key attribute
     *
     * @param toAdd TReportPersonSettings
     */
    protected void addTReportPersonSettingsFromBean(TReportPersonSettings toAdd)
    {
        initTReportPersonSettingss();
        collTReportPersonSettingss.add(toAdd);
    }


    /**
     * Method called to associate a TReportSubscribe object to this object.
     * through the TReportSubscribe foreign key attribute
     *
     * @param toAdd TReportSubscribe
     */
    protected void addTReportSubscribeFromBean(TReportSubscribe toAdd)
    {
        initTReportSubscribes();
        collTReportSubscribes.add(toAdd);
    }


    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TExportTemplate:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Name = ")
           .append(getName())
           .append("\n");
        str.append("ReportType = ")
           .append(getReportType())
           .append("\n");
        str.append("ExportFormat = ")
           .append(getExportFormat())
           .append("\n");
        str.append("RepositoryType = ")
           .append(getRepositoryType())
           .append("\n");
        str.append("Description = ")
           .append(getDescription())
           .append("\n");
        str.append("Project = ")
           .append(getProject())
           .append("\n");
        str.append("Person = ")
           .append(getPerson())
           .append("\n");
        str.append("CategoryKey = ")
           .append(getCategoryKey())
           .append("\n");
        str.append("Parent = ")
           .append(getParent())
           .append("\n");
        str.append("Sortorder = ")
           .append(getSortorder())
           .append("\n");
        str.append("Deleted = ")
           .append(getDeleted())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
