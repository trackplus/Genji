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

package com.aurel.track.screen.bl.design;
import com.aurel.track.beans.screen.IField;
import com.aurel.track.beans.screen.IPanel;
import com.aurel.track.screen.bl.AbstractFieldBL;

/**
 *
 */
public abstract class AbstractFieldDesignBL extends AbstractFieldBL {
    /**
         * constructor
         */
     public AbstractFieldDesignBL() {
            super();
     }

    /**
     * Create an field in a panel having given pk
     * @param panelPk
     * @param fieldDefPK
     * @return
     */
        public IField createField(Integer panelPk,String fieldDefPK) {
            IField field=getScreenFactory().createIFieldInstance();
            field.setColSpan(new Integer(1));
            field.setRowSpan(new Integer(1));
            field.setParent(panelPk);
            updateField(field,fieldDefPK);
            return field;
        }
        protected abstract void updateField(IField field, String fieldInfo);

        /**
         * Delete a field screen from a panel Replace the field with an empty field
         * screen
         *
         * @param panelPk
         * @param fieldId
         */
        public  void deleteFieldScreen(Integer panelPk, Integer fieldId) {
            IField fieldBean = screenFieldDAO.loadByPrimaryKey(fieldId);
            screenFieldDAO.delete(fieldBean.getObjectID());
        }
        public  void saveScreenField(IField fs){
            screenFieldDAO.save(fs);
        }
        public  void setFieldScreenProperty(IField fs,IField fsSchema){
            fs.setName(fsSchema.getName());
            fs.setDescription(fsSchema.getDescription());
            setColSpan(fs, fsSchema.getColSpan());
            setRowSpan(fs, fsSchema.getRowSpan());
        }
        private  void setColSpan(IField fs,Integer cols){
            Integer oldSpan=fs.getColSpan();
            if(oldSpan.equals(cols)){
                return;
            }
            //get full parent
            IPanel parent= getScreenFactory().getPanelDAO().loadByPrimaryKey(fs.getParent());

            int rowIndex=fs.getRowIndex().intValue();
            int colIndex=fs.getColIndex().intValue();
            int maxColSpan=parent.getColsNo().intValue()-colIndex;
            int newColSpan=cols.intValue();
            if(newColSpan<0){
                newColSpan=1;
            }
            if(newColSpan>maxColSpan){
                newColSpan=maxColSpan;
            }
            fs.setColSpan(new Integer(newColSpan));
            //remove fiels in the way of expanding
            for(int i=1;i<newColSpan;i++){
                IField f=parent.getScreenField(rowIndex, colIndex+i);
                if(f!=null){
                    screenFieldDAO.delete(f.getObjectID());
                }
            }
            screenFieldDAO.save(fs);
        }

        private  void setRowSpan(IField fs,Integer rows){
            Integer oldSpan=fs.getRowSpan();
            if(oldSpan.equals(rows)){
                return;
            }
            //get full parent
            IPanel  parent=getScreenFactory().getPanelDAO().loadByPrimaryKey(fs.getParent());

            int rowIndex=fs.getRowIndex().intValue();
            int colIndex=fs.getColIndex().intValue();
            int maxRowSpan=parent.getRowsNo().intValue()-rowIndex;
            int newRowSpan=rows.intValue();
            if(newRowSpan<0){
                newRowSpan=1;
            }
            if(newRowSpan>maxRowSpan){
                newRowSpan=maxRowSpan;
            }
            fs.setRowSpan(new Integer(newRowSpan));
            //remove fiels in the way of expanding
            for(int i=1;i<newRowSpan;i++){
                IField f=parent.getScreenField(rowIndex+i, colIndex);
                if(f!=null){
                    screenFieldDAO.delete(f.getObjectID());
                }
            }
            screenFieldDAO.save(fs);
        }

        public abstract String encodeJSON_FieldProperies(IField field,String fieldType);


    }

