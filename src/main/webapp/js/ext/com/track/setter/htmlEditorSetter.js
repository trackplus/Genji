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

Ext.define("js.ext.com.track.setter.htmlEditorSetter", {
	extend: "Ext.form.field.TextArea",
	flex:1,
	anchor:'100% -25',
	height:207,
	config: {
		jsonData: {}
	},
	initComponent: function() {
		this.itemId = this.jsonData.name;
		this.name = this.jsonData.name;
		this.disabled = this.jsonData.disabled;
		this.value = this.jsonData.value;
		this.labelWidth=this.jsonData.labelWidth;
		var idRTE="setter_id_"+this.jsonData.name;
		this.id=idRTE;
		this.callParent();
		this.addListener('afterrender',CWHF.initRTEditor,CWHF,{id:idRTE,focus:false,resize:true});
		this.addListener('destroy',CWHF.destroyRTEditor,CWHF,{id:idRTE,focus:false,resize:true});
	},
	getSetterValue:function(){
		CWHF.submitRTEditor(this);
		return this.getValue();
	},
	getSetterDisplayValue:function(){
		CWHF.submitRTEditor(this);
		return this.getValue();
	}
});
