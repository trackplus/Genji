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

Ext.define('js.ext.com.track.dashboard.CalendarDash',{
	extend:'js.ext.com.track.dashboard.DashboardRenderer',
	calendarView:null,
	currentDay:null,
	today:null,
	markedDay:null,
	btnToday:null,btnWeekMonth:null,

	TYPE_DATE_START:0,
	TYPE_DATE_STOP:1,
	TYPE_DATE_DOT:2,

	TYPE_TOP_DOWN_DATE_START:100,
	TYPE_TOP_DOWN_DATE_STOP:101,
	TYPE_TOP_DOWN_DATE_DOT:102,


	initComponent : function(){
		var me=this;
		me.btnToday=Ext.create('Ext.button.Button',{
			text:getText('CalendarDash.todayBtn'),
			scope:me,
			handler:me.doToday
		});
		me.btnWeekMonth=Ext.create('Ext.button.Button',{
			text:getText('CalendarDash.monthBtn'),
			scope:me,
			handler:me.doWeekMonth
		});
		/*me.dockedItems=[{
			xtype: 'toolbar',
			dock: 'bottom',
			items: ['->',me.btnToday,me.btnWeekMonth]
		}];*/
		me.items=me.createChildren();
		me.callParent();
	},
	createChildren:function(){
		var me=this;
		if(me.jsonData.tooManyItems===true){
			return [me.createErrorCmp(getText('cockpit.err.tooManyItems'))];
		}
		me.calendarView=me.jsonData.calendarView;
		me.currentDay=me.jsonData.currentDay;
		me.markedDay=me.jsonData.markedDay;
		me.today=me.jsonData.today;
		var viewMode;
		if(me.calendarView==="week"){
			viewMode=me.createWeekComponent();
		}else{
			viewMode=me.createMonthComponent();
		}
		return [viewMode];
	},
	createWeekComponent:function(){
		var me=this;
		me.week=me.jsonData.week;
		var items=[];
		var dashboardID=me.jsonData.dashboardID;
		var urlBack='javascript:com.trackplus.dashboard.reload('+dashboardID+',21)';
		var urlNext='javascript:com.trackplus.dashboard.reload('+dashboardID+',22)';
		var htmlTitle='<table border="0" cellpadding="0" cellspacing="0" width="100%" style="height:100%">';
		htmlTitle+='<tr align="center">';
		htmlTitle+='<td style="width:25px;"><a href="'+urlBack+'" class="hovBackw"></a></td>';
		htmlTitle+='<td style="font-size:1.2em; font-weight:bold;color:#606060;">'+me.week.fromTo+'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;('+getText("CalendarDash.week")+'&nbsp'+me.week.weekOfYear+')</td>';
		var todayStr='<td style="width:100px;"><a href="javascript:com.trackplus.dashboard.reload('+dashboardID+',11)">'+getText('CalendarDash.todayBtn')+'</a></td>';
		var monthViewStr='<td style="width:100px;"><a href="javascript:com.trackplus.dashboard.reload('+dashboardID+',12)" >'+getText('CalendarDash.monthBtn')+'</a></td>';
		htmlTitle+=todayStr+monthViewStr;
		htmlTitle+='<td  style="width:100px;">&nbsp;</td>';
		htmlTitle+='<td  style="width:25px;"><a href="'+urlNext+'" class="hovForw"></a></td>';
		htmlTitle+='</tr></table>';

		var titleComp={xtype:'component',
			html:htmlTitle,
			colspan:7,
			style: {'background-color':'#E0E0E0',
		        'padding':'7px 0 7px 0',
		        'border':'none', // '0px solid #c0c0c0'
		        'border-top':'1px solid #b0b0b0'
		        }
		};
		items.push(titleComp);

		for(var i=0;i<7;i++){
			var dayCmp=me.createWeekDay(me.week.days[i]);
			if(i===3||i===4){
				// dayCmp.rowspan=2;
			}
			if(i===5||i===6){
				dayCmp.style['background-color']='#e8e8e8';
			}
			items.push(dayCmp);
		}
		return Ext.create('Ext.panel.Panel',{
			layout: {
				type: 'table',
				columns: 7,
				tableAttrs: {
					style: {
						'border': '0px 1px 0px 1px solid #b0b0b0',
					    'margin-left': '-1px',
						'margin-right': '-1px',
						'width': '100%'
					}
				},
				tdAttrs:{
					width:'14%',
					style:{
						'vertical-align':'top',
						'border': '0 1px 0 1px solid #b0b0b0',
						'padding':'0px'
					}
				}
			},
			defaults: {
				border: true
			},
			border:false,
			bodyBorder:false,
			items:items
		});
	},
	createWeekDay:function(day){
		var me=this;
		var htmlStr='&nbsp';
		var issues=day.issues;
		if(issues&&issues.length>0){
			htmlStr=me.createHtmlIssues(issues,day.value);
		}
		var bodyStyle={
				'border': 'none', // '0 0px 0 0px solid #BF2128', //2px
				'text-align':'left',
				'border-left': '1px solid #b0b0b0',
				'color':'#e0e0e0',
				'font-weight':'bold',
				'padding':'0 0 0 0',
				'minHeight':'200px'
		};
		var headStyle={
				'color': '#606060',
				'text-align': 'center',
				'background-color': '#e0e0e0',
				'border-bottom' :'1px solid #b0b0b0',
				'border-top' : '1px solid #b0b0b0',
				'width': '100%',
				'height': '30px',
				'margin': '0',
				'padding': '7px 0 7px 0'
		};
		if(day.value===me.today){
			bodyStyle['border']='1px solid #ff0000';
			bodyStyle['border-left']="1px solid #ff0000";
			bodyStyle['border-right']="2px solid #ff0000";
			bodyStyle['border-top']="1px solid #ff0000";
			headStyle['padding-top']='6px';
			headStyle['height']='29px';
		}
		if(day.value===me.markedDay){
			htmlStr+=" markedDay";
		}
		var header={
				xtype:'container',
				layout:'hbox',
				items: [{xtype:'component', width:'15%', html:day.dom, style: {'text-align':'left', 'padding-left':'4px'}},
				        {xtype:'component', html:day.label, width:'70%',style: {'text-align':'center'}},
				        {xtype:'component', html:'&nbsp;&nbsp;', style: {'text-align':'right'}}
				],
				style: headStyle,
				componentCls: 'headerComp'
		};
		return {
			xtype:'container',
			layout:'auto',
            items:[header,
                   {xtype:'component', style:{'padding':'4px'}, html:htmlStr}
                   ],
			margin: '0 0 0 0', // 3 3 3 3
//			style:{'border': 'none', 'color': '#606060'},
			style:bodyStyle,
			componentCls:'wholeDay'
//			,
//			listeners: {
//				render:{
//					fn:function(c){
//						c.body.on('click', function(){
//						})
//					}
//				}
//			}
		}
	},
	createHtmlIssues:function(issues,day){
		var me=this;
		var htmlStr="";
		if(issues){
			for(var i=0;i<issues.length;i++){
				var issue=issues[i];
				var htmlIssue="";
				var type=me.getType(issue,day);
				var urlNr=com.trackplus.TrackplusConfig.contextPath+'/printItem.action?key='+issue.objectID;

				/*TYPE_DATE_START:0,
				 TYPE_DATE_STOP:1,
				 TYPE_DATE_DOT:2,

				 TYPE_TOP_DOWN_DATE_START:100,
				 TYPE_TOP_DOWN_DATE_STOP:101,
				 TYPE_TOP_DOWN_DATE_DOT:102*/

				var extraCls='';
				switch (type){
					case me.TYPE_DATE_START:{
						htmlIssue+='<img  src="'+com.trackplus.TrackplusConfig.iconsPath+'dashcalendar_start.gif"/>&nbsp;';
						break;
					}
					case me.TYPE_DATE_STOP:{
						htmlIssue+='<img  src="'+com.trackplus.TrackplusConfig.iconsPath+'dashcalendar_stop.gif"/>&nbsp;';
						break;
					}
					case me.TYPE_DATE_DOT:{
						htmlIssue+='<img  src="'+com.trackplus.TrackplusConfig.iconsPath+'dashcalendar_dot.gif"/>&nbsp;';
						break;
					}

					case me.TYPE_TOP_DOWN_DATE_START:{
						htmlIssue+='<img  src="'+com.trackplus.TrackplusConfig.iconsPath+'dashcalendar_topDown_start.png"/>&nbsp;';
						extraCls="calendarTopDownDate";
						break;
					}
					case me.TYPE_TOP_DOWN_DATE_STOP:{
						htmlIssue+='<img  src="'+com.trackplus.TrackplusConfig.iconsPath+'dashcalendar_topDown_stop.png"/>&nbsp;';
						extraCls="calendarTopDownDate";
						break;
					}
					case me.TYPE_TOP_DOWN_DATE_DOT:{
						htmlIssue+='<img  src="'+com.trackplus.TrackplusConfig.iconsPath+'dashcalendar_topDown_dot.png"/>&nbsp;';
						extraCls="calendarTopDownDate";
						break;
					}
				}
				htmlIssue+='<a title="'+issue.tooltip+'" href="'+urlNr+'" class="'+issue.cssColorClass+' '+extraCls+'">'+
					issue.title+'</a>';
				htmlStr+=htmlIssue+"</br>";
			}
		}
		return htmlStr;
	},
	getType:function(issue,day){
		var me=this;
		var startDate=0;
		if(issue.startDate){
			startDate=parseInt(issue.startDate+"");
		}
		var endDate=0;
		if(issue.endDate){
			endDate=parseInt(issue.endDate+"");
		}
		var currentDate=parseInt(day+"");

		var type=-1;
		if(startDate===currentDate){
			if(endDate===currentDate){
				type=me.TYPE_DATE_DOT;
			}else{
				type=me.TYPE_DATE_START;

			}
		}else if(endDate===currentDate){
			type=me.TYPE_DATE_STOP;
		}
		if(type===-1){
			startDate=0;
			if(issue.topDownStartDate){
				startDate=parseInt(issue.topDownStartDate+"");
			}
			endDate=0;
			if(issue.topDownEndDate){
				endDate=parseInt(issue.topDownEndDate+"");
			}
			if(startDate===currentDate){
				if(endDate===currentDate){
					type=me.TYPE_TOP_DOWN_DATE_DOT;
				}else{
					type=me.TYPE_TOP_DOWN_DATE_START;

				}
			}else if(endDate===currentDate){
				type=me.TYPE_TOP_DOWN_DATE_STOP;
			}
		}
		return type;
	},
	createMonthComponent:function(){
		var me=this;
		var items=[];
		me.month=me.jsonData.month;

		var dashboardID=me.jsonData.dashboardID;
		var urlBack='javascript:com.trackplus.dashboard.reload('+dashboardID+',41)';
		var urlLastYear='javascript:com.trackplus.dashboard.reload('+dashboardID+',42)';
		var urlNextYear='javascript:com.trackplus.dashboard.reload('+dashboardID+',43)';
		var urlNext='javascript:com.trackplus.dashboard.reload('+dashboardID+',44)';


		var htmlTitle='<table border="0" cellpadding="0" cellspacing="0" width="100%" style="height:100%">';
		htmlTitle+='<tr align="center">';
		htmlTitle+='<td style="width:25px;"><a href="'+urlBack+'" class="hovBackw"></a></td>';
		htmlTitle+='<td style="width:100px;"><a href="'+urlLastYear+'">'+me.month.lastYear+'</a></td>';
		htmlTitle+='<td style="font-size:1.2em;font-weight:bold;color:#606060">'+me.month.header+'</td>';
		var todayStr='<td style="width:100px;"><a href="javascript:com.trackplus.dashboard.reload('+dashboardID+',31)">'+getText('CalendarDash.todayBtn')+'</a></td>';
		var weekViewStr='<td style="width:100px;"><a href="javascript:com.trackplus.dashboard.reload('+dashboardID+',32)" >'+getText('CalendarDash.weekBtn')+'</a></td>';
		htmlTitle+=todayStr+weekViewStr;
		htmlTitle+='<td style="width:100px;"><a href="'+urlNextYear+'">'+me.month.nextYear+'</a></td>';
		htmlTitle+='<td  style="width:25px; margin-right:-2px;"><a href="'+urlNext+'" class="hovForw"></a></td>';
		htmlTitle+='</tr></table>';

		var titleCmp={html:htmlTitle,
			bodyCls: 'calHeader',
			bodyStyle: {'background-color':'#E0E0E0',
				        'padding':'7px 0 7px 0',
				        'border':'0px solid #ff0000'
			},
			colspan:6
		};
		items.push(titleCmp);

		var weekDaysHeader=me.month.weekDays;

		var headerCmp={html:' '};
		headerCmp.bodyStyle={border:'none'};
		headerCmp.bodyStyle['color']='#606060';
		headerCmp.bodyStyle['height']='30px';
		headerCmp.bodyStyle['text-align']='center';
		headerCmp.bodyStyle['font-weight']='bold';
		headerCmp.bodyStyle['background-color']='#E0E0E0';
		headerCmp.bodyStyle['padding']='7px 0 7px 0';

		for(var i=0;i<weekDaysHeader.length-2;i++){
			var copyCmp={html:' '};
			Ext.apply(copyCmp, headerCmp);
			copyCmp.html=weekDaysHeader[i];
			items.push(copyCmp);
		}
		headerCmp.html=weekDaysHeader[weekDaysHeader.length-2]+'/'+weekDaysHeader[weekDaysHeader.length-1];
		items.push(headerCmp);
		var days=me.month.days;
		var weekDays;
		var firstDay=parseInt(me.month.firstDay+"");
		var lastDay=parseInt(me.month.lastDay+"");
		for(var i=0;i<days.length;i++){
			weekDays=days[i];
			for(var j=0;j<days[i].length;j++){
				var dayCmp={xtype:'component', html:days[i][j].label+'</br>'+me.createHtmlIssues(days[i][j].issues,days[i][j].value)};
				dayCmp.style={border:'none'};
				dayCmp.style['color']='#606060';
				if(j<days[i].length-2){
					dayCmp.rowspan=2;
					dayCmp.style['minHeight']='81px';
					if(CWHF.isNull(days[i][j].issues)||days[i][j].issues.length===0){
						dayCmp.html=dayCmp.html+"&nbsp";
					}
				}else{
					dayCmp.style['background-color']= '#e7e7e7';
					dayCmp.style['minHeight']='40px';
				}
				if(days[i][j].value===me.today){
					dayCmp.style['border']='2px solid #BF2128;';  // today

				}
				var dayValue=parseInt(days[i][j].value+"");
				if(dayValue<firstDay||dayValue>=lastDay){
					dayCmp.style['color']='#a0a0a0';
					// dayCmp.bodyStyle['background-color']= '#BEC0C6';
				}
				dayCmp.style['padding']= '2px 3px 3px 4px';
				dayCmp.style['font-weight']='bold';
				dayCmp.style['height']='100%';
				items.push(dayCmp);
			}
		}
		var dashboardID=me.jsonData.dashboardID;
		return Ext.create('Ext.panel.Panel',{
			layout: {
				type: 'table',
				columns: 6,
				tableAttrs: {
					style: {
						'border': '0px 1px 0px 1px solid #c0c0c0',
					    'margin-left': '-1px',
						'margin-right': '-1px',
						'width': '100%'
					}
				},
				tdAttrs:{
					width:'16%',
					style:{
						'vertical-align':'top',
						'border':'1px solid #c0c0c0',
						'padding':'0px'
					}
				}
			},
			defaults: {
				border: true
			},
			border:false,
			bodyBorder:false,
			items:items
		});
	},
	doRefresh:function(jsonData){
		var me=this;
		me.jsonData=jsonData;
		me.removeAll();
		me.add(me.createChildren());
	},
	doWeekMonth:function(){
		var me=this;
		var linkNo;
		if(me.calendarView==='week'){
			linkNo=12; //monthButton in week view
			me.btnWeekMonth.setText(getText('CalendarDash.weekBtn'));
		}else{
			linkNo=32;//weekButton in month view
			me.btnWeekMonth.setText(getText('CalendarDash.monthBtn'))
		}
		me.refreshDashboard(me.createParamsMap(linkNo));
	},
	doToday:function(){
		var me=this;
		var linkNo;
		if(me.calendarView==='week'){
			linkNo=11;
		}else{
			linkNo=31;
		}
		me.refreshDashboard(me.createParamsMap(linkNo));
	},
	createParamsMap:function(nr) {
		var me=this;
		var params={};
		params['params.linkNo']=nr;
		var currentDayParam=me.currentDay;
		switch (nr) {
			case 11:	// todayButton in week view
			case 31:	// todayButton in month view
				currentDayParam=null;
				break;
		}
		params['params.currentDay']=currentDayParam;
		return params;
	}
});
