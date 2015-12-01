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

Ext.define("js.ext.com.track.bulkSetter.releaseBulkSetter", {
	extend: "Ext.panel.Panel",
	border:false,
	items: [],
	config: {
		jsonData: {}
	},
	initComponent: function() {
		this.callParent();
		var projectJsonArray = this.jsonData["projectJson"];
        if (projectJsonArray) {
            var disabled = this.jsonData["disabled"];
            if (projectJsonArray.length===1) {
                listData = projectJsonArray[0];
                this.add(CWHF.createSingleTreePicker(null,
                    listData["name"], listData["releaseTree"], listData["releaseID"],
                    {itemId:listData["itemId"],
                	allowBlank:false,
                     width:220,
                     labelIsLocalized:true,
                     disabled:disabled
                    }));
            } else {
                Ext.Array.forEach(projectJsonArray, function(listData) {
                    this.add(CWHF.createSingleTreePicker(listData['label'],
                        listData['name'], listData["releaseTree"], listData["releaseID"],
                        {itemId:listData["itemId"],
                    	allowBlank:false,
                         labelWidth:150,
                         width:220 + 150,
                         labelIsLocalized:true,
                         disabled:disabled,
                         margin:'5 0 0 0'
                        }));
                        }, this);
            }
        }

	},
	/**
	 * Get the field value as a json
	 */
	getFieldValueJson: function() {
		var valueJson = new Object();
		if (this.items) {
			for (var i=0;i<this.items.getCount();i++) {
				var combo = this.items.getAt(i);
				var name = combo.getName();
				var value = combo.getValue();
				valueJson[name] = value;
			}
		}
		return valueJson;
	},

	setDisabled: function(disabled) {
		if (this.items) {
			for (var i=0;i<this.items.getCount();i++) {
				var combo = this.items.getAt(i);
				combo.setDisabled(disabled);
			}
		}
	}
});
