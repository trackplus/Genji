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



import com.aurel.track.persist.TCardPanel;
import com.aurel.track.persist.TCardPanelPeer;

  import org.apache.commons.collections.map.IdentityMap;
import java.util.Iterator;
import com.aurel.track.beans.TCardFieldBean;
import com.aurel.track.beans.TCardPanelBean;



/**
 * You should not use this class directly.  It should not even be
 * extended all references should be to TCardField
 */
public abstract class BaseTCardField extends TpBaseObject
{
    /** The Peer class */
    private static final TCardFieldPeer peer =
        new TCardFieldPeer();


    /** The value for the objectID field */
    private Integer objectID;

    /** The value for the name field */
    private String name;

    /** The value for the colIndex field */
    private Integer colIndex;

    /** The value for the rowIndex field */
    private Integer rowIndex;

    /** The value for the colSpan field */
    private Integer colSpan;

    /** The value for the rowSpan field */
    private Integer rowSpan;

    /** The value for the labelHAlign field */
    private Integer labelHAlign;

    /** The value for the labelVAlign field */
    private Integer labelVAlign;

    /** The value for the valueHAlign field */
    private Integer valueHAlign;

    /** The value for the valueVAlign field */
    private Integer valueVAlign;

    /** The value for the cardPanel field */
    private Integer cardPanel;

    /** The value for the field field */
    private Integer field;

    /** The value for the hideLabel field */
    private String hideLabel = "Y";

    /** The value for the iconRendering field */
    private Integer iconRendering;

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
    public void setObjectID(Integer v) 
    {

        if (!ObjectUtils.equals(this.objectID, v))
        {
            this.objectID = v;
            setModified(true);
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
     * Get the ColIndex
     *
     * @return Integer
     */
    public Integer getColIndex()
    {
        return colIndex;
    }


    /**
     * Set the value of ColIndex
     *
     * @param v new value
     */
    public void setColIndex(Integer v) 
    {

        if (!ObjectUtils.equals(this.colIndex, v))
        {
            this.colIndex = v;
            setModified(true);
        }


    }

    /**
     * Get the RowIndex
     *
     * @return Integer
     */
    public Integer getRowIndex()
    {
        return rowIndex;
    }


    /**
     * Set the value of RowIndex
     *
     * @param v new value
     */
    public void setRowIndex(Integer v) 
    {

        if (!ObjectUtils.equals(this.rowIndex, v))
        {
            this.rowIndex = v;
            setModified(true);
        }


    }

    /**
     * Get the ColSpan
     *
     * @return Integer
     */
    public Integer getColSpan()
    {
        return colSpan;
    }


    /**
     * Set the value of ColSpan
     *
     * @param v new value
     */
    public void setColSpan(Integer v) 
    {

        if (!ObjectUtils.equals(this.colSpan, v))
        {
            this.colSpan = v;
            setModified(true);
        }


    }

    /**
     * Get the RowSpan
     *
     * @return Integer
     */
    public Integer getRowSpan()
    {
        return rowSpan;
    }


    /**
     * Set the value of RowSpan
     *
     * @param v new value
     */
    public void setRowSpan(Integer v) 
    {

        if (!ObjectUtils.equals(this.rowSpan, v))
        {
            this.rowSpan = v;
            setModified(true);
        }


    }

    /**
     * Get the LabelHAlign
     *
     * @return Integer
     */
    public Integer getLabelHAlign()
    {
        return labelHAlign;
    }


    /**
     * Set the value of LabelHAlign
     *
     * @param v new value
     */
    public void setLabelHAlign(Integer v) 
    {

        if (!ObjectUtils.equals(this.labelHAlign, v))
        {
            this.labelHAlign = v;
            setModified(true);
        }


    }

    /**
     * Get the LabelVAlign
     *
     * @return Integer
     */
    public Integer getLabelVAlign()
    {
        return labelVAlign;
    }


    /**
     * Set the value of LabelVAlign
     *
     * @param v new value
     */
    public void setLabelVAlign(Integer v) 
    {

        if (!ObjectUtils.equals(this.labelVAlign, v))
        {
            this.labelVAlign = v;
            setModified(true);
        }


    }

    /**
     * Get the ValueHAlign
     *
     * @return Integer
     */
    public Integer getValueHAlign()
    {
        return valueHAlign;
    }


    /**
     * Set the value of ValueHAlign
     *
     * @param v new value
     */
    public void setValueHAlign(Integer v) 
    {

        if (!ObjectUtils.equals(this.valueHAlign, v))
        {
            this.valueHAlign = v;
            setModified(true);
        }


    }

    /**
     * Get the ValueVAlign
     *
     * @return Integer
     */
    public Integer getValueVAlign()
    {
        return valueVAlign;
    }


    /**
     * Set the value of ValueVAlign
     *
     * @param v new value
     */
    public void setValueVAlign(Integer v) 
    {

        if (!ObjectUtils.equals(this.valueVAlign, v))
        {
            this.valueVAlign = v;
            setModified(true);
        }


    }

    /**
     * Get the CardPanel
     *
     * @return Integer
     */
    public Integer getCardPanel()
    {
        return cardPanel;
    }


    /**
     * Set the value of CardPanel
     *
     * @param v new value
     */
    public void setCardPanel(Integer v) throws TorqueException
    {

        if (!ObjectUtils.equals(this.cardPanel, v))
        {
            this.cardPanel = v;
            setModified(true);
        }


        if (aTCardPanel != null && !ObjectUtils.equals(aTCardPanel.getObjectID(), v))
        {
            aTCardPanel = null;
        }

    }

    /**
     * Get the Field
     *
     * @return Integer
     */
    public Integer getField()
    {
        return field;
    }


    /**
     * Set the value of Field
     *
     * @param v new value
     */
    public void setField(Integer v) 
    {

        if (!ObjectUtils.equals(this.field, v))
        {
            this.field = v;
            setModified(true);
        }


    }

    /**
     * Get the HideLabel
     *
     * @return String
     */
    public String getHideLabel()
    {
        return hideLabel;
    }


    /**
     * Set the value of HideLabel
     *
     * @param v new value
     */
    public void setHideLabel(String v) 
    {

        if (!ObjectUtils.equals(this.hideLabel, v))
        {
            this.hideLabel = v;
            setModified(true);
        }


    }

    /**
     * Get the IconRendering
     *
     * @return Integer
     */
    public Integer getIconRendering()
    {
        return iconRendering;
    }


    /**
     * Set the value of IconRendering
     *
     * @param v new value
     */
    public void setIconRendering(Integer v) 
    {

        if (!ObjectUtils.equals(this.iconRendering, v))
        {
            this.iconRendering = v;
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

    



    private TCardPanel aTCardPanel;

    /**
     * Declares an association between this object and a TCardPanel object
     *
     * @param v TCardPanel
     * @throws TorqueException
     */
    public void setTCardPanel(TCardPanel v) throws TorqueException
    {
        if (v == null)
        {
            setCardPanel((Integer) null);
        }
        else
        {
            setCardPanel(v.getObjectID());
        }
        aTCardPanel = v;
    }


    /**
     * Returns the associated TCardPanel object.
     * If it was not retrieved before, the object is retrieved from
     * the database
     *
     * @return the associated TCardPanel object
     * @throws TorqueException
     */
    public TCardPanel getTCardPanel()
        throws TorqueException
    {
        if (aTCardPanel == null && (!ObjectUtils.equals(this.cardPanel, null)))
        {
            aTCardPanel = TCardPanelPeer.retrieveByPK(SimpleKey.keyFor(this.cardPanel));
        }
        return aTCardPanel;
    }

    /**
     * Return the associated TCardPanel object
     * If it was not retrieved before, the object is retrieved from
     * the database using the passed connection
     *
     * @param connection the connection used to retrieve the associated object
     *        from the database, if it was not retrieved before
     * @return the associated TCardPanel object
     * @throws TorqueException
     */
    public TCardPanel getTCardPanel(Connection connection)
        throws TorqueException
    {
        if (aTCardPanel == null && (!ObjectUtils.equals(this.cardPanel, null)))
        {
            aTCardPanel = TCardPanelPeer.retrieveByPK(SimpleKey.keyFor(this.cardPanel), connection);
        }
        return aTCardPanel;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey, for example
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
     */
    public void setTCardPanelKey(ObjectKey key) throws TorqueException
    {

        setCardPanel(new Integer(((NumberKey) key).intValue()));
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
            fieldNames.add("ColIndex");
            fieldNames.add("RowIndex");
            fieldNames.add("ColSpan");
            fieldNames.add("RowSpan");
            fieldNames.add("LabelHAlign");
            fieldNames.add("LabelVAlign");
            fieldNames.add("ValueHAlign");
            fieldNames.add("ValueVAlign");
            fieldNames.add("CardPanel");
            fieldNames.add("Field");
            fieldNames.add("HideLabel");
            fieldNames.add("IconRendering");
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
        if (name.equals("ColIndex"))
        {
            return getColIndex();
        }
        if (name.equals("RowIndex"))
        {
            return getRowIndex();
        }
        if (name.equals("ColSpan"))
        {
            return getColSpan();
        }
        if (name.equals("RowSpan"))
        {
            return getRowSpan();
        }
        if (name.equals("LabelHAlign"))
        {
            return getLabelHAlign();
        }
        if (name.equals("LabelVAlign"))
        {
            return getLabelVAlign();
        }
        if (name.equals("ValueHAlign"))
        {
            return getValueHAlign();
        }
        if (name.equals("ValueVAlign"))
        {
            return getValueVAlign();
        }
        if (name.equals("CardPanel"))
        {
            return getCardPanel();
        }
        if (name.equals("Field"))
        {
            return getField();
        }
        if (name.equals("HideLabel"))
        {
            return getHideLabel();
        }
        if (name.equals("IconRendering"))
        {
            return getIconRendering();
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
        if (name.equals("ColIndex"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setColIndex((Integer) value);
            return true;
        }
        if (name.equals("RowIndex"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRowIndex((Integer) value);
            return true;
        }
        if (name.equals("ColSpan"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setColSpan((Integer) value);
            return true;
        }
        if (name.equals("RowSpan"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setRowSpan((Integer) value);
            return true;
        }
        if (name.equals("LabelHAlign"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLabelHAlign((Integer) value);
            return true;
        }
        if (name.equals("LabelVAlign"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setLabelVAlign((Integer) value);
            return true;
        }
        if (name.equals("ValueHAlign"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setValueHAlign((Integer) value);
            return true;
        }
        if (name.equals("ValueVAlign"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setValueVAlign((Integer) value);
            return true;
        }
        if (name.equals("CardPanel"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setCardPanel((Integer) value);
            return true;
        }
        if (name.equals("Field"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setField((Integer) value);
            return true;
        }
        if (name.equals("HideLabel"))
        {
            // Object fields can be null
            if (value != null && ! String.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setHideLabel((String) value);
            return true;
        }
        if (name.equals("IconRendering"))
        {
            // Object fields can be null
            if (value != null && ! Integer.class.isInstance(value))
            {
                throw new IllegalArgumentException("Invalid type of object specified for value in setByName");
            }
            setIconRendering((Integer) value);
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
        if (name.equals(TCardFieldPeer.OBJECTID))
        {
            return getObjectID();
        }
        if (name.equals(TCardFieldPeer.NAME))
        {
            return getName();
        }
        if (name.equals(TCardFieldPeer.COLINDEX))
        {
            return getColIndex();
        }
        if (name.equals(TCardFieldPeer.ROWINDEX))
        {
            return getRowIndex();
        }
        if (name.equals(TCardFieldPeer.COLSPAN))
        {
            return getColSpan();
        }
        if (name.equals(TCardFieldPeer.ROWSPAN))
        {
            return getRowSpan();
        }
        if (name.equals(TCardFieldPeer.LABELHALIGN))
        {
            return getLabelHAlign();
        }
        if (name.equals(TCardFieldPeer.LABELVALIGN))
        {
            return getLabelVAlign();
        }
        if (name.equals(TCardFieldPeer.VALUEHALIGN))
        {
            return getValueHAlign();
        }
        if (name.equals(TCardFieldPeer.VALUEVALIGN))
        {
            return getValueVAlign();
        }
        if (name.equals(TCardFieldPeer.CARDPANEL))
        {
            return getCardPanel();
        }
        if (name.equals(TCardFieldPeer.FIELDKEY))
        {
            return getField();
        }
        if (name.equals(TCardFieldPeer.HIDELABEL))
        {
            return getHideLabel();
        }
        if (name.equals(TCardFieldPeer.ICONRENDERING))
        {
            return getIconRendering();
        }
        if (name.equals(TCardFieldPeer.TPUUID))
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
      if (TCardFieldPeer.OBJECTID.equals(name))
        {
            return setByName("ObjectID", value);
        }
      if (TCardFieldPeer.NAME.equals(name))
        {
            return setByName("Name", value);
        }
      if (TCardFieldPeer.COLINDEX.equals(name))
        {
            return setByName("ColIndex", value);
        }
      if (TCardFieldPeer.ROWINDEX.equals(name))
        {
            return setByName("RowIndex", value);
        }
      if (TCardFieldPeer.COLSPAN.equals(name))
        {
            return setByName("ColSpan", value);
        }
      if (TCardFieldPeer.ROWSPAN.equals(name))
        {
            return setByName("RowSpan", value);
        }
      if (TCardFieldPeer.LABELHALIGN.equals(name))
        {
            return setByName("LabelHAlign", value);
        }
      if (TCardFieldPeer.LABELVALIGN.equals(name))
        {
            return setByName("LabelVAlign", value);
        }
      if (TCardFieldPeer.VALUEHALIGN.equals(name))
        {
            return setByName("ValueHAlign", value);
        }
      if (TCardFieldPeer.VALUEVALIGN.equals(name))
        {
            return setByName("ValueVAlign", value);
        }
      if (TCardFieldPeer.CARDPANEL.equals(name))
        {
            return setByName("CardPanel", value);
        }
      if (TCardFieldPeer.FIELDKEY.equals(name))
        {
            return setByName("Field", value);
        }
      if (TCardFieldPeer.HIDELABEL.equals(name))
        {
            return setByName("HideLabel", value);
        }
      if (TCardFieldPeer.ICONRENDERING.equals(name))
        {
            return setByName("IconRendering", value);
        }
      if (TCardFieldPeer.TPUUID.equals(name))
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
            return getColIndex();
        }
        if (pos == 3)
        {
            return getRowIndex();
        }
        if (pos == 4)
        {
            return getColSpan();
        }
        if (pos == 5)
        {
            return getRowSpan();
        }
        if (pos == 6)
        {
            return getLabelHAlign();
        }
        if (pos == 7)
        {
            return getLabelVAlign();
        }
        if (pos == 8)
        {
            return getValueHAlign();
        }
        if (pos == 9)
        {
            return getValueVAlign();
        }
        if (pos == 10)
        {
            return getCardPanel();
        }
        if (pos == 11)
        {
            return getField();
        }
        if (pos == 12)
        {
            return getHideLabel();
        }
        if (pos == 13)
        {
            return getIconRendering();
        }
        if (pos == 14)
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
            return setByName("ColIndex", value);
        }
    if (position == 3)
        {
            return setByName("RowIndex", value);
        }
    if (position == 4)
        {
            return setByName("ColSpan", value);
        }
    if (position == 5)
        {
            return setByName("RowSpan", value);
        }
    if (position == 6)
        {
            return setByName("LabelHAlign", value);
        }
    if (position == 7)
        {
            return setByName("LabelVAlign", value);
        }
    if (position == 8)
        {
            return setByName("ValueHAlign", value);
        }
    if (position == 9)
        {
            return setByName("ValueVAlign", value);
        }
    if (position == 10)
        {
            return setByName("CardPanel", value);
        }
    if (position == 11)
        {
            return setByName("Field", value);
        }
    if (position == 12)
        {
            return setByName("HideLabel", value);
        }
    if (position == 13)
        {
            return setByName("IconRendering", value);
        }
    if (position == 14)
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
        save(TCardFieldPeer.DATABASE_NAME);
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
                    TCardFieldPeer.doInsert((TCardField) this, con);
                    setNew(false);
                }
                else
                {
                    TCardFieldPeer.doUpdate((TCardField) this, con);
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
        
    {
        setObjectID(new Integer(((NumberKey) key).intValue()));
    }

    /**
     * Set the PrimaryKey using a String.
     *
     * @param key
     */
    public void setPrimaryKey(String key) 
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
    public TCardField copy() throws TorqueException
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
    public TCardField copy(Connection con) throws TorqueException
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
    public TCardField copy(boolean deepcopy) throws TorqueException
    {
        return copyInto(new TCardField(), deepcopy);
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
    public TCardField copy(boolean deepcopy, Connection con) throws TorqueException
    {
        return copyInto(new TCardField(), deepcopy, con);
    }
  
    /**
     * Fills the copyObj with the contents of this object.
     * The associated objects are also copied and treated as new objects.
     *
     * @param copyObj the object to fill.
     */
    protected TCardField copyInto(TCardField copyObj) throws TorqueException
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
    protected TCardField copyInto(TCardField copyObj, Connection con) throws TorqueException
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
    protected TCardField copyInto(TCardField copyObj, boolean deepcopy) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setColIndex(colIndex);
        copyObj.setRowIndex(rowIndex);
        copyObj.setColSpan(colSpan);
        copyObj.setRowSpan(rowSpan);
        copyObj.setLabelHAlign(labelHAlign);
        copyObj.setLabelVAlign(labelVAlign);
        copyObj.setValueHAlign(valueHAlign);
        copyObj.setValueVAlign(valueVAlign);
        copyObj.setCardPanel(cardPanel);
        copyObj.setField(field);
        copyObj.setHideLabel(hideLabel);
        copyObj.setIconRendering(iconRendering);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {
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
    protected TCardField copyInto(TCardField copyObj, boolean deepcopy, Connection con) throws TorqueException
    {
        copyObj.setObjectID(objectID);
        copyObj.setName(name);
        copyObj.setColIndex(colIndex);
        copyObj.setRowIndex(rowIndex);
        copyObj.setColSpan(colSpan);
        copyObj.setRowSpan(rowSpan);
        copyObj.setLabelHAlign(labelHAlign);
        copyObj.setLabelVAlign(labelVAlign);
        copyObj.setValueHAlign(valueHAlign);
        copyObj.setValueVAlign(valueVAlign);
        copyObj.setCardPanel(cardPanel);
        copyObj.setField(field);
        copyObj.setHideLabel(hideLabel);
        copyObj.setIconRendering(iconRendering);
        copyObj.setUuid(uuid);

        copyObj.setObjectID((Integer)null);

        if (deepcopy)
        {
        }
        return copyObj;
    }
    
    

    /**
     * returns a peer instance associated with this om.  Since Peer classes
     * are not to have any instance attributes, this method returns the
     * same instance for all member of this class. The method could therefore
     * be static, but this would prevent one from overriding the behavior.
     */
    public TCardFieldPeer getPeer()
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
        return TCardFieldPeer.getTableMap();
    }

  
    /**
     * Creates a TCardFieldBean with the contents of this object
     * This also creates beans for cached related objects, if they exist
     * @return a TCardFieldBean with the contents of this object
     */
    public TCardFieldBean getBean()
    {
        return getBean(new IdentityMap());
    }

    /**
     * Creates a TCardFieldBean with the contents of this object
     * intended for internal use only
     * @param createdBeans a IdentityMap which maps objects
     *        to already created beans
     * @return a TCardFieldBean with the contents of this object
     */
    public TCardFieldBean getBean(IdentityMap createdBeans)
    {
        TCardFieldBean result = (TCardFieldBean) createdBeans.get(this);
        if (result != null ) {
            // we have already created a bean for this object, return it
            return result;
        }
        // no bean exists for this object; create a new one
        result = new TCardFieldBean();
        createdBeans.put(this, result);

        result.setObjectID(getObjectID());
        result.setName(getName());
        result.setColIndex(getColIndex());
        result.setRowIndex(getRowIndex());
        result.setColSpan(getColSpan());
        result.setRowSpan(getRowSpan());
        result.setLabelHAlign(getLabelHAlign());
        result.setLabelVAlign(getLabelVAlign());
        result.setValueHAlign(getValueHAlign());
        result.setValueVAlign(getValueVAlign());
        result.setCardPanel(getCardPanel());
        result.setField(getField());
        result.setHideLabel(getHideLabel());
        result.setIconRendering(getIconRendering());
        result.setUuid(getUuid());





        if (aTCardPanel != null)
        {
            TCardPanelBean relatedBean = aTCardPanel.getBean(createdBeans);
            result.setTCardPanelBean(relatedBean);
        }
        result.setModified(isModified());
        result.setNew(isNew());
        return result;
    }

    /**
     * Creates an instance of TCardField with the contents
     * of a TCardFieldBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed
     * @param bean the TCardFieldBean which contents are used to create
     *        the resulting class
     * @return an instance of TCardField with the contents of bean
     */
    public static TCardField createTCardField(TCardFieldBean bean)
        throws TorqueException
    {
        return createTCardField(bean, new IdentityMap());
    }

    /**
     * Creates an instance of TCardField with the contents
     * of a TCardFieldBean.
     * This behaviour could have also been achieved using a constructor,
     * however as this class is abstract no constructors are allowed.
     *
     * This method is intended for internal use only.
     * @param bean the TCardFieldBean which contents are used to create
     *        the resulting class
     * @param createdObjects a IdentityMap which maps beans
     *        to already created objects
     * @return an instance of TCardField with the contents of bean
     */

    public static TCardField createTCardField(TCardFieldBean bean, IdentityMap createdObjects)
        throws TorqueException
    {
        TCardField result = (TCardField) createdObjects.get(bean);
        if (result != null)
        {
            // we already have an object for the bean, return it
            return result;
        }
        result = new TCardField();
        createdObjects.put(bean, result);

        result.setObjectID(bean.getObjectID());
        result.setName(bean.getName());
        result.setColIndex(bean.getColIndex());
        result.setRowIndex(bean.getRowIndex());
        result.setColSpan(bean.getColSpan());
        result.setRowSpan(bean.getRowSpan());
        result.setLabelHAlign(bean.getLabelHAlign());
        result.setLabelVAlign(bean.getLabelVAlign());
        result.setValueHAlign(bean.getValueHAlign());
        result.setValueVAlign(bean.getValueVAlign());
        result.setCardPanel(bean.getCardPanel());
        result.setField(bean.getField());
        result.setHideLabel(bean.getHideLabel());
        result.setIconRendering(bean.getIconRendering());
        result.setUuid(bean.getUuid());





        {
            TCardPanelBean relatedBean = bean.getTCardPanelBean();
            if (relatedBean != null)
            {
                TCardPanel relatedObject = TCardPanel.createTCardPanel(relatedBean, createdObjects);
                result.setTCardPanel(relatedObject);
            }
        }
    result.setModified(bean.isModified());
    result.setNew(bean.isNew());
      return result;
    }



    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TCardField:\n");
        str.append("ObjectID = ")
           .append(getObjectID())
           .append("\n");
        str.append("Name = ")
           .append(getName())
           .append("\n");
        str.append("ColIndex = ")
           .append(getColIndex())
           .append("\n");
        str.append("RowIndex = ")
           .append(getRowIndex())
           .append("\n");
        str.append("ColSpan = ")
           .append(getColSpan())
           .append("\n");
        str.append("RowSpan = ")
           .append(getRowSpan())
           .append("\n");
        str.append("LabelHAlign = ")
           .append(getLabelHAlign())
           .append("\n");
        str.append("LabelVAlign = ")
           .append(getLabelVAlign())
           .append("\n");
        str.append("ValueHAlign = ")
           .append(getValueHAlign())
           .append("\n");
        str.append("ValueVAlign = ")
           .append(getValueVAlign())
           .append("\n");
        str.append("CardPanel = ")
           .append(getCardPanel())
           .append("\n");
        str.append("Field = ")
           .append(getField())
           .append("\n");
        str.append("HideLabel = ")
           .append(getHideLabel())
           .append("\n");
        str.append("IconRendering = ")
           .append(getIconRendering())
           .append("\n");
        str.append("Uuid = ")
           .append(getUuid())
           .append("\n");
        return(str.toString());
    }
}
