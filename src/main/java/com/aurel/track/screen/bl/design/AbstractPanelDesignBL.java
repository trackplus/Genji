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

package com.aurel.track.screen.bl.design;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.aurel.track.beans.screen.IField;
import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.screen.bl.AbstractPanelBL;

import java.util.List;

/**
 */
public abstract class AbstractPanelDesignBL extends AbstractPanelBL{
    protected static Logger LOGGER = LogManager.getLogger(AbstractPanelBL.class);

	/**
	 * constructor
	 */
	public AbstractPanelDesignBL() {
		super();
	}
    public abstract AbstractFieldDesignBL getFieldDesignBL();

    /**
	 * Delete a panel
	 * @param id
	 */
	public void deleteScreenPanel(Integer id){
		panelDAO.delete(id);
	}
	/**
	 * Save a panel
	 * @param panel
	 * @return
	 */
	public  Integer saveScreenPanel(IPanel panel){
		return  saveScreenPanel(panel,false);
	}
	public  Integer saveScreenPanel(IPanel panel,boolean includeChildren){
		if(panel.getRowsNo()==null){
			panel.setRowsNo(new Integer(3));
		}
		if(panel.getColsNo()==null){
			panel.setColsNo(new Integer(3));
		}
		Integer pk=panelDAO.save(panel);
		if(includeChildren){
			List<IField> fields=panel.getFields();
			if(fields!=null){
				for(IField field:fields){
					field.setParent(pk);
					fieldDAO.save(field);
				}
			}
		}
		return pk;
	}
	public  void setPanelProperty(IPanel panelDB,IPanel panelScheme){
		panelDB.setName(panelScheme.getName());
		panelDB.setLabel(panelScheme.getLabel());
		panelDB.setDescription(panelScheme.getDescription());
		setPanCols(panelDB, panelScheme.getColsNo());
		setPanRows(panelDB, panelScheme.getRowsNo());
	}

    /**
     * Add a new field screen on a given panel in the given position
     * @param panelPk
     * @param fieldDefPK
     * @param row
     * @param col
     */
	public  void addFieldScreen(Integer panelPk,String fieldDefPK,int row,int col){
		LOGGER.debug("Adding a new field for:"+fieldDefPK +" in panel:"+panelPk+" ("+row+","+col+")...");
		IField field =getFieldDesignBL().createField(panelPk, fieldDefPK);
		IField old=fieldDAO.loadByParentAndIndex(panelPk, new Integer(row),new Integer(col));
		field.setRowIndex(new Integer(row));
		field.setColIndex(new Integer(col));
		if(old!=null){
			field.setRowSpan(old.getRowSpan());
			field.setColSpan(old.getColSpan());
			fieldDAO.delete(old.getObjectID());
		}
		fieldDAO.save(field);
		LOGGER.debug("Add  field screen ready!");
	}
	public  void addFieldScreen(Integer panelPk,IField field,int row,int col){
		LOGGER.debug("Adding  field in panel:"+panelPk+" ("+row+","+col+")...");
		IField old=fieldDAO.loadByParentAndIndex(panelPk, new Integer(row),new Integer(col));
		field.setRowIndex(new Integer(row));
		field.setColIndex(new Integer(col));
		if(old!=null){
			field.setRowSpan(old.getRowSpan());
			field.setColSpan(old.getColSpan());
			fieldDAO.delete(old.getObjectID());
		}
		fieldDAO.save(field);
		LOGGER.debug("Add  field screen ready!");
	}

    /**
     * Replace one field with other position
     * @param panelPk
     * @param row_source
     * @param col_source
     * @param row_target
     * @param col_target
     */
	public  void moveFieldScreen(Integer panelPk, int row_source,int col_source,int row_target,int col_target) {
		LOGGER.debug("Moving field from:("+row_source+","+col_source+") to ("+row_target+","+col_target+") in panel:"+panelPk+"...");
		IField fsSource = fieldDAO.loadByParentAndIndex(panelPk, new Integer(
				row_source),new Integer(col_source));
		IField fsTarget = fieldDAO.loadByParentAndIndex(panelPk, new Integer(
				row_target),new Integer(col_target));
		if(fsTarget!=null){
			LOGGER.debug("Move field on position where is not an empty field...");
			Integer colSpanTmp=fsSource.getColSpan();
			Integer rowSpanTmp=fsSource.getRowSpan();
			fsSource.setColSpan(fsTarget.getColSpan());
			fsSource.setRowSpan(fsTarget.getRowSpan());
			fsTarget.setRowSpan(rowSpanTmp);
			fsTarget.setColSpan(colSpanTmp);
			fsTarget.setRowIndex(new Integer(row_source));
			fsTarget.setColIndex(new Integer(col_source));
			fieldDAO.save(fsTarget);
		}else{
			fsSource.setColSpan(new Integer(1));
			fsSource.setRowSpan(new Integer(1));
		}
		fsSource.setRowIndex(new Integer(row_target));
		fsSource.setColIndex(new Integer(col_target));
		fieldDAO.save(fsSource);
		LOGGER.debug("Move screen field ready!");
	}
	public  void moveFieldFromOther(Integer panelPk,Integer fieldID,int row_target,int col_target,Integer sourcePanelId){
		LOGGER.debug("Moving a field("+fieldID+")from panel:"+sourcePanelId+" to:"+panelPk+"...");
		IField fs=fieldDAO.loadByPrimaryKey(fieldID);
		if(fs==null){
			LOGGER.debug("The field "+fieldID+" does not exist anymore!");
			return;
		}
		if(!(fs.getParent().equals(sourcePanelId))){
			//field not enymore on thr right place;
			LOGGER.debug("The field "+fieldID+"is not anymore in the right panel:"+sourcePanelId+"!");
			return;
		}
		IField fsTarget = fieldDAO.loadByParentAndIndex(panelPk, new Integer(
				row_target),new Integer(col_target));
		if(fsTarget!=null){
			LOGGER.debug("Deleting the target field...");
			fieldDAO.delete(fsTarget.getObjectID());
		}
		fs.setParent(panelPk);
		fs.setColIndex(new Integer(col_target));
		fs.setRowIndex(new Integer(row_target));
		fieldDAO.save(fs);
		LOGGER.debug("Field("+fieldID+") was moved from"+sourcePanelId+" to:"+panelPk+"!");
	}
	private  void setPanRows(IPanel pan, Integer rows){
		Integer oldRows=pan.getRowsNo();
		int cols=pan.getColsNo().intValue();
		if(oldRows.equals(rows)){
			return;
		}
		int dif=rows.intValue()-oldRows.intValue();
		if(dif<0){//must remove rows
			for(int i=rows.intValue();i<oldRows.intValue();i++){
				for(int j=0;j<cols;j++){
					IField f=pan.getScreenField(i,j);
					if(f!=null){
						fieldDAO.delete(f.getObjectID());
					}
				}
			}
			//cange extra row-span
			for(int i=0;i<rows.intValue();i++){
				for(int j=0;j<cols;j++){
					IField f=pan.getScreenField(i,j);
					if(f!=null){
						int extraSpan=(f.getRowSpan().intValue()+i)-rows.intValue();
						if(extraSpan>0){//if is out of boundary
							f.setRowSpan(new Integer(f.getRowSpan().intValue()-extraSpan));
							fieldDAO.save(f);
						}
					}
				}
			}
		}
		pan.setRowsNo(rows);
		//panelDAO.save(pan);
	}

	private  void setPanCols(IPanel pan, Integer cols){
		Integer oldCols=pan.getColsNo();
		int rows=pan.getRowsNo().intValue();
		if(oldCols.equals(cols)){
			return;
		}
		int dif=cols.intValue()-oldCols.intValue();
		if(dif<0){// must remove cols
			for(int i=0;i<rows;i++){
				for(int j=cols.intValue();j<oldCols.intValue();j++){
					IField f=pan.getScreenField(i,j);
					if(f!=null){
						fieldDAO.delete(f.getObjectID());
					}
				}
			}
			//cange extra col-span
			for(int i=0;i<rows;i++){
				for(int j=0;j<cols.intValue();j++){
					IField f=pan.getScreenField(i,j);
					if(f!=null){
						int extraSpan=(f.getColSpan().intValue()+j)-cols.intValue();
						if(extraSpan>0){//if is out of boundary
							f.setColSpan(new Integer(f.getColSpan().intValue()-extraSpan));
							fieldDAO.save(f);
						}
					}
				}
			}
		}
		pan.setColsNo(cols);
		//panelDAO.save(pan);
	}
}
