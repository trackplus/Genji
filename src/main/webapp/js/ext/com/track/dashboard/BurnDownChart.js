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

//Common properties (appearance)
//Color array for series
var COLORS = new Array();
var COLORS2 = new Array();

COLORS[0] = '#FD0232'; COLORS2[0] = '#962036';
COLORS[1] = '#11FB05'; COLORS2[1] = '#1b7716';
COLORS[2] = '#7EE6ED'; COLORS2[2] = '#5Ec6cD';
COLORS[3] = '#bbbbbb'; COLORS2[3] = '#989898';
COLORS[4] = '#475AAD'; COLORS2[4] = '#273A9D';
COLORS[5] = '#FAFA0A'; COLORS2[5] = '#cAcA08';
COLORS[6] = '#FF8000'; COLORS2[6] = '#d96000';
COLORS[7] = '#AA16DB'; COLORS2[7] = '#89099B';
COLORS[8] = '#A1ED09'; COLORS2[8] = '#71aD09';

//Series color intensity
var SERIES_STROKE_WIDTH = 2;
//Legend configuration
var LEGEND_ITEM_SPACING = 4;
var LEGEND_LABEL_FONT = '10px Helvetica, sans-serif';
var LEGEND_PADDING = 4;

//Grid configuration
var GRID = new Object();
GRID.odd = new Object();
GRID.odd.opacity = '1';
GRID.odd.stroke = '#eaeaea';
GRID.odd['stroke-width'] = '1';

Ext.define('Ext.chart.theme.Trackplus', {
	extend: 'Ext.chart.theme.Base',
    alias: 'chart.theme.Trackplus',
    config: {
    	axis: {
    		defaults: {
    			style: {strokeStyle: '#000000'}
    	    },
    	    left: {
    	    	title: {fillStyle: '#000000', fontSize: '16', fontFamily: 'bold 14px Arial'}
    	    },
    	    bottom: {
    	    	title: {fillStyle: '#000000', fontSize: '16', fontFamily: 'bold 14px Arial'}
      	    }
    	}
    }
});

Ext.define('js.ext.com.track.dashboard.BurnDownChart',{
    extend:'js.ext.com.track.dashboard.DashboardRenderer',

    LEGEND_WIDTH_EFFORT_TYPE_NO_ITEMS: 221,
    LEGEND_WIDTH_EFFORT_TYPE_TIME: 163,
    LEGEND_WIDTH_EFFORT_TYPE_STORY_POINT: 197,
    LEGEN_RIGHT_OFFSET: 18,
    LEGEN_TOP_OFFSET: 6,

    initComponent : function(){
    	var me=this;
        me.layout='fit';
        me.items = me.createChildren();
		me.callParent();
    },

    createHtmlString:function(){
    	var me=this;
    },

    createChildren:function(){
    	var me = this;
		if(me.jsonData.tooManyItems===true){
			return [me.createErrorCmp(getText('cockpit.err.tooManyItems'))];
		}

		if(me.jsonData.empty === false) {
			myChart = me.createChart();
		}else {
			myChart = me.createEmptyChart();
		}
        return [myChart];
    },

    createChart: function() {
    	var me = this;
    	var gradID = Math.random().toString(36).substring(7);
        var chartData = me.jsonData.chartData;
        var maxValue = me.calculateMaxValue(chartData);
        var jsonArg1 = me.createYAxeCommonConfig(maxValue);
        var jsonArg2 = me.createXAxeCommonConfig();
        Ext.Function.defer(function(){
//        	me.myChart.legend.origX  = 150;//me.getLegendXValue();
//        	me.myChart.legend.origY  = me.LEGEN_TOP_OFFSET;
//        	me.myChart.legend.setLocalX(35);//me.getLegendXValue());
//        	me.myChart.legend.setY(me.LEGEN_TOP_OFFSET);
//            me.myChart.legend.visible = true;
//            me.myChart.legend.refreshView();

        }, 100);

        var fieldsForStore = JSON.parse('[{"name": "date", "type":"string"}, {"name": "plannedValue", "type":"int"}, {"name": "earnedValue", "type":"int"}]');
        var store = Ext.create('Ext.data.Store', {
            id:'store',
            fields: fieldsForStore,
            data: chartData
        });
        var axesArray = new Array();
        axesArray.push(jsonArg1);
        axesArray.push(jsonArg2);
        var axesJSONArray = JSON.parse(JSON.stringify(axesArray));
        me.myChart =
        	Ext.create('Ext.chart.CartesianChart', {
        	theme: 'Trackplus',
            gradients: me.getGradients(gradID),
            legend: {
            	docked: 'bottom',
//            	visible: false,
            	tpl: [
             	     '<table><tr class="x-legend-container">',
 	                 	'<tpl for=".">',
 	                  		'<tr>',
 	                  			'<td class="x-legend-item">',
 	                  				'<div class="x-legend-item-marker {[ values.disabled ? Ext.baseCSSPrefix + \'legend-inactive\' : \'\' ]}">',
 	                  					'<div style="cursor:pointer; display: inline-block;width:10px;height:10px;background:{mark};"></div>',
 	                  					'<div style= "margin-left: 2px; cursor:pointer; display: inline-block;">{name}</div>',
 	                  				'</div>',
 	                  			'</td>',
 	                  		'</tr>',
 	            	     '</tpl>',
 	            	  '</table>'
 	            ],
 	           itemSelector: 'td',
 	           style: {
 	        	   borderColor: '#000000',
 	        	   borderStyle: 'solid'
 	           },
 	           margin: '0 0 5 0',
 	           border: '1px'
            },
            height: me.jsonData.height,
            animate: true,
            store: store,
            axes: axesJSONArray,
            border: false,
            bodyBorder: false
        });
        var plannedSeriesLabel = "";
        var actualSeriesLabel = "";
        if(me.jsonData.selectedEffortType === 1) {
        	plannedSeriesLabel = getText('burnDownChart.series.ideal.effort');
            actualSeriesLabel = getText('burnDownChart.series.actual.effort');

        }else if(me.jsonData.selectedEffortType === 2) {
        	plannedSeriesLabel = getText('burnDownChart.series.ideal.no.of.items');
            actualSeriesLabel = getText('burnDownChart.series.actual.no.of.items');
        }else {
        	plannedSeriesLabel = getText('burnDownChart.series.ideal.story.point');
            actualSeriesLabel = getText('burnDownChart.series.actual.story.point');
        }
        var seriesArr = [];
        seriesArr.push(me.createSeries('plannedValue', 0, plannedSeriesLabel));
        seriesArr.push(me.createSeries('earnedValue', 1, actualSeriesLabel));
        me.myChart.setSeries(seriesArr);
		return me.myChart;
    },

    getLegendXValue: function() {
    	var me = this;
    	var chartWidth = me.lastBox.width;
    	legenXValue = 0;

    	if(me.jsonData.selectedEffortType === 1) {
    		legenXValue = chartWidth - me.LEGEND_WIDTH_EFFORT_TYPE_TIME - me.LEGEN_RIGHT_OFFSET;
    	}if(me.jsonData.selectedEffortType === 2) {
    		legenXValue = chartWidth - me.LEGEND_WIDTH_EFFORT_TYPE_NO_ITEMS - me.LEGEN_RIGHT_OFFSET;
    	}if(me.jsonData.selectedEffortType === 3) {
    		legenXValue = chartWidth - me.LEGEND_WIDTH_EFFORT_TYPE_STORY_POINT - me.LEGEN_RIGHT_OFFSET;
    	}
    	return legenXValue;
    },

    getGradients: function(gradID) {
    	var gradientsArray = [
	         {
	           id: gradID + '0',
	           'angle': 0,
	           stops: {0: {color: COLORS[0]},100: {color: COLORS2[0]}}
	         },
	         {
	        	 id: gradID + '1',
	           'angle': 0,
	           stops: {0: {color: COLORS[1]},100: {color: COLORS2[1]}}
	         },
	         {
	        	 id: gradID + '2',
	             'angle': 0,
	             stops: {0: {color: COLORS[2]},100: {color: COLORS2[2]}}
	         },
	         {
	        	 id: gradID + '3',
	             'angle': 0,
	             stops: {0: {color: COLORS[3]},100: {color: COLORS2[3]}}
	         },
	         {
	        	 id: gradID + '4',
	           'angle': 0,
	           stops: {0: {color: COLORS[4]},100: {color: COLORS2[4]}}
	         },
	         {
	        	 id: gradID + '5',
	             'angle': 0,
	             stops: {0: {color: COLORS[5]},100: {color: COLORS2[5]}}
	         },
	         {
	        	 id: gradID + '6',
	             'angle': 0,
	             stops: {0: {color: COLORS[6]},100: {color: COLORS2[6]}}
	         },
	         {
	        	 id: gradID + '7',
	             'angle': 0,
	             stops: {0: {color: COLORS[7]},100: {color: COLORS2[7]}}
	         },
	         {
	        	 id: gradID + '8',
	             'angle': 0,
	             stops: {0: {color: COLORS[8]},100: {color: COLORS2[8]}}
	         }
         ];
    	return gradientsArray;
    },

    createYAxeCommonConfig: function(maxValue) {
    	var me = this;
    	var jsonArg1 = {
    		type: 'numeric',
    	    position: 'left',
    	    grid: GRID,
    	    minimum: '0',
    	    maximum: maxValue,
    	    title: me.jsonData.yAxesLabel,
    	    labelTitle: {
    	    font: 'bold 14px Arial'
    	    }
    	 };
    	 if (maxValue < 2) {
    		 jsonArg1.majorTickSteps = 0;
    	 } else if (maxValue > 1 && maxValue < 10){
    		  jsonArg1.majorTickSteps = 1;
    	 }else {
    		  jsonArg1.minorTickSteps = 2;
    		  jsonArg1.majorTickSteps = 9;
    	 }
    	 return jsonArg1;
    },

    createXAxeCommonConfig: function() {
    	var me = this;
        var jsonArg2 = {
        	id: 'bottom',
        	position: 'bottom',
        	title: me.jsonData.xAxesLabel,
        	labelTitle: {
        		font: 'bold 14px Arial'
        	},
        	grid: GRID
        };
        if (me.jsonData.reportingInterval=== "1" &&  me.jsonData.empty === false) {
        	jsonArg2.type = 'time';
            jsonArg2.fields = 'date';
            jsonArg2.dateFormat = 'M d';
            jsonArg2.fromDate = new Date(me.jsonData.dateFrom);
            jsonArg2.toDate = new Date(me.jsonData.dateTo);
            var stepType = new Array();
            stepType.push(Ext.Date.DAY);
            stepType.push(1);
            jsonArg2.step = stepType;

        //Axes specific configuration WEEKLY  GRANULARITY
        }else if (me.jsonData.reportingInterval=== "2" &&  me.jsonData.empty === false) {
            jsonArg2.type = 'category';
            jsonArg2.fields = 'date';

        //Axes specific configuration MONTHLY  GRANULARITY*/
        } else if(me.jsonData.empty === false) {
            jsonArg2.type = 'category';
            jsonArg2.fields = 'date';
        }
        return jsonArg2;
    },

    calculateMaxValue: function(chartdata) {
    	//Searching the max value from JSON response, avoiding decimal numbers
        var maxValue = -1;
        for(var i = 0; i <  chartdata.length; i++){
            var obj =  chartdata[i];
            for(var key in obj){
                if ( key !== "date") {
                    if (parseInt(obj[key]) > parseInt(maxValue)) {
                        maxValue = obj[key];
                    }
                }
            }
        }
        return maxValue;
    },

    createEmptyChart: function() {
    	var me = this;
    	var gradID = Math.random().toString(36).substring(7);
    	me.defineTheme(gradID);
        var store = Ext.create('Ext.data.Store', {
            id: 'store',
            fields: [{"name": "date", "type":"date"}],
            data: [{"date":"2013-01-01"}]
        });
        myChart = Ext.create('Ext.chart.CartesianChart', {
            background: {
                fill:'white'
            },
            animate: true,
            height: me.jsonData.height,
			store: store,
            itemId: 'myChart',
            axes: [{
                position: 'left',
                title:getText('statusOverTime.prompt.emptyChart'),
                labelTitle: {font: 'bold 10px Arial'},
                type: 'numeric',
                minimum: 0,
                maximum: 4,
                majorTickSteps: 1 // one less than max
            },{
                id: 'bottom',
                position: 'bottom',
                type: 'category',
                fields: 'date',
                title:getText('statusOverTime.prompt.emptyChart'),
                labelTitle: {font: 'bold 10px Arial'}
            }]
        });
        return myChart;
    },

    createSeries: function(name, nr, seriesTitle) {
    	var series = {
    		showMarkers: true,
	        type: 'line',
	        stacked: true,
	        fill: false,
	        xField: 'date',
	        yField: name,
	        colors: [COLORS[nr]],
	        title: seriesTitle,
	        style: {
	        	stroke: COLORS[nr],
	        	fillStyle: COLORS[nr],
	            'stroke-width': SERIES_STROKE_WIDTH
	         },
	         marker: {
	        	 type: 'circle',
	        	 fx: {
	        		 duration: 200,
	        		 easing: 'backOut'
	        	 }
	         }
    	};
    	return series;
    },

    doRefresh:function(jsonData){
    	var me=this;
    	me.jsonData=jsonData;
    	me.removeAll();
        me.add(me.createChildren());
    }
});

