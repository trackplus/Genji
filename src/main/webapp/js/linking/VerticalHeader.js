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

Ext.define('GeneralLearning.ux.VerticalHeader', {
	alias: 'plugin.verticalheader',
	grid: null,
	extraCfg:null,
	textMetric: null,
	init: function (grid) {
		var me = this;
		me.grid = grid;
		me.grid.addCls('v-vertical-header-grid');
		me.textMetric = new Ext.util.TextMetrics();

		if (me.isLocked()) {
			var normalGridPlugin = Ext.create('GeneralLearning.ux.VerticalHeader',me.extraCfg),
				lockedGridPlugin = Ext.create('GeneralLearning.ux.VerticalHeader',me.extraCfg);
			normalGridPlugin.init(me.grid.normalGrid);
			lockedGridPlugin.init(me.grid.lockedGrid);
			me.grid.normalGrid.plugins.push(normalGridPlugin);
			me.grid.lockedGrid.plugins.push(lockedGridPlugin);

		} else {
			me.grid.on({
				afterlayout: {
					scope: me,
					fn: me.handleAfterLayout
				}
			});
		}
	},

	constructor: function (cfg) {

		var me = this;
		me.extraCfg = cfg;
		Ext.apply(this, cfg);
		me.callParent(arguments);
	},

	handleAfterLayout: function (cmp) {
		var me = this,
			maxWidth = 0,
			headerItems = cmp.headerCt.items,
			curremtWidth;

		headerItems.each(function (item) {
			if ((curremtWidth = me.textMetric.getWidth(item.text)) > maxWidth) {
				maxWidth = curremtWidth + 10;
			}
		});

		cmp.headerCt.el.select('.x-column-header-text').each(function (el) {
			el.setSize(maxWidth, maxWidth);
		});
	},

	isLocked: function () {
		var me = this;
		return me.grid.normalGrid && me.grid.lockedGrid;
	}

});
