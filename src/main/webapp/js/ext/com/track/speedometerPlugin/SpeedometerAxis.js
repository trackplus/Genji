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

Ext.define('js.ext.com.track.speedometerPlugin.SpeedometerAxis', {
//    extend: 'Ext.chart.axis.Gauge',
	extend: 'Ext.chart.axis.Category',
    alias: 'axis.kpigauge',
	position: 'left',
	drawAxis: function(init) {
        var me = this,
			chart = me.chart,
            surface = chart.surface,
            bbox = chart.chartBBox,
            centerX = bbox.x + (bbox.width / 2),
            centerY = bbox.y + bbox.height,
            margin = me.margin || 10,
            rho = Math.min(bbox.width, 2 * bbox.height) /2 + margin,
            sprites = [], sprite,
            steps = me.steps,
            i, pi = Math.PI,
            cos = Math.cos,
            sin = Math.sin;

        if (me.sprites && !chart.resizing) {
            me.drawLabel();
            return;
        }

        if (me.margin >= 0) {
            if (!me.sprites) {
                //draw circles
                for (i = 0; i <= steps; i++) {
                    sprite = surface.add(Ext.apply({
                        type: 'path',
                        path: ['M', centerX + (rho - margin) * cos(i / steps * pi - pi),
                                    centerY + (rho - margin) * sin(i / steps * pi - pi),
                                    'L', centerX + rho * cos(i / steps * pi - pi),
                                    centerY + rho * sin(i / steps * pi - pi), 'Z']
                    }, me.axisStyle));
                    sprite.setAttributes({
                        hidden: false
                    }, true);
                    sprites.push(sprite);
                }
            } else {
                sprites = me.sprites;
                //draw circles
                for (i = 0; i <= steps; i++) {
                    sprites[i].setAttributes(Ext.apply({
                        path: ['M', centerX + (rho - margin) * cos(i / steps * pi - pi),
                                    centerY + (rho - margin) * sin(i / steps * pi - pi),
                               'L', centerX + rho * cos(i / steps * pi - pi),
                                    centerY + rho * sin(i / steps * pi - pi), 'Z']
                    }, me.axisStyle), true);
                }
            }
        }
        me.sprites = sprites;
        me.drawLabel();
        if (me.title) {
            me.drawTitle();
        }
    },
	drawLabel: function() {
        var me = this,
			chart = me.chart,
            surface = chart.surface,
            bbox = chart.chartBBox,
            centerX = bbox.x + (bbox.width / 2),
            centerY = bbox.y + bbox.height,
            margin = me.margin || 10,
            rho = Math.min(bbox.width, 2 * bbox.height) /2 + 2 * margin,
            round = Math.round,
			labelGroup = me.labelGroup,
            maxValue = me.maximum || 0,
            minValue = me.minimum || 0,
            steps = me.steps, i = 0,
            pi = Math.PI,
            cos = Math.cos,
            sin = Math.sin,
			textLabel, adjY;

		me.label.renderer = me.label.renderer || Ext.identityFn

		for (i = 0; i <= steps; i++) {
			textLabel = labelGroup.getAt(i);
			adjY = (i === 0 || i === steps) ? 7 : 0;
			x = centerX + rho * cos(i / steps * pi - pi);
			y = centerY + rho * sin(i / steps * pi - pi) - adjY
			text = me.label.renderer(round(minValue + i / steps * (maxValue - minValue)));
			if (textLabel) {
				textLabel.setAttributes(Ext.apply({
                    text: text,
                    x: x,
                    y: y
                }, me.label), true);
			} else {
				textLabel = surface.add(Ext.apply({
					group: labelGroup,
					type: 'text',
					x: x,
					y: y,
					text: text,
					zIndex: 10,
					'text-anchor': 'middle'
				}, me.label));
				surface.renderItem(textLabel);
			}
		}
		textLabel.setAttributes({
			hidden: false
		}, true);
    }
});
