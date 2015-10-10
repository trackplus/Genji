<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<#assign app=JspTaglibs["/WEB-INF/tlds/app.tld"]>
<@s.set name="iconsPath"
		value="'${request.getContextPath()}'+'/design/'+#session.userDesignPath+'/icons/'"/>
<#-- conversion error of the editItemScreenAction -->
<@s.set name="alignR" value="#session.alignR" scope="request"/>
<@s.token/>
<script type="text/javascript">
	var tabsLength=<@s.property value="tabs.size()"/>
	var tabsRTEinitialized=new Object();
</script>
<script type="text/javascript">
	Ext.define('com.aurel.trackplus.field.TypeRenderer',{
		render:function(parameters,data){
			return Ext.create('Ext.form.Label',{});
		}
	});
	Ext.define('com.aurel.trackplus.field.LabelTypeRenderer',{
		extend:'com.aurel.trackplus.field.TypeRenderer',
		render:function(parameters,data){
			var invisible=parameters.invisible;
			var label=parameters.label;
			var value=parameters.value;
			var displayValue=parameters.displayValue;
			if(displayValue==null||displayValue==""){
				displayValue=value;
			}
			if(invisible){
				return Ext.create('Ext.form.Label',{});
			}
			return  Ext.create('Ext.form.field.Display',{
				fieldLabel: label,
				value:displayValue
			});
		}
	});
	Ext.define('com.aurel.trackplus.field.TextFieldTypeRenderer',{
		extend:'com.aurel.trackplus.field.TypeRenderer',
		render:function(parameters,data){
			var invisible=parameters.invisible;
			var label=parameters.label;
			var value=parameters.value;
			var name=parameters.name;
			var displayValue=parameters.displayValue;
			if(displayValue==null||displayValue==""){
				displayValue=value;
			}
			if(invisible){
				return Ext.create('Ext.form.Label',{});
			}
			return Ext.create('Ext.form.field.Text',{
				fieldLabel: label,
				name: name,
				allowBlank:true,
				value:value
			});
		}
	});
	
	Ext.define('com.aurel.trackplus.field.DateTypeRenderer',{
		extend:'com.aurel.trackplus.field.TypeRenderer',
		render:function(parameters,data){
			var invisible=parameters.invisible;
			var label=parameters.label;
			var value=parameters.value;
			var name=parameters.name;
			var displayValue=parameters.displayValue;
			if(displayValue==null||displayValue==""){
				displayValue=value;
			}
			if(invisible){
				return Ext.create('Ext.form.Label',{});
			}
			return Ext.create('Ext.form.field.Date',{
				fieldLabel: label,
				name: name,
				allowBlank:true
			});
		}
	});
	
	Ext.define('com.aurel.trackplus.field.HtmlTypeRenderer',{
		extend:'com.aurel.trackplus.field.TypeRenderer',
		render:function(parameters,data){
			var invisible=parameters.invisible;
			var label=parameters.label;
			var value=parameters.value;
			var name=parameters.name;
			var displayValue=parameters.displayValue;
			if(displayValue==null||displayValue==""){
				displayValue=value;
			}
			if(invisible){
				return Ext.create('Ext.form.Label',{});
			}
			return Ext.create('Ext.form.field.HtmlEditor',{
				fieldLabel: label,
				name: name,
				allowBlank:true,
				value:value
			});
		}
	});
	
	
	Ext.define('com.aurel.trackplus.field.SelectTypeRenderer',{
		extend:'com.aurel.trackplus.field.TypeRenderer',
		render:function(parameters,data){
			var invisible=parameters.invisible;
			var label=parameters.label;
			var value=null;
			try{
				 value=parseInt(parameters.value);
			}catch(e){}
			
			var name=parameters.name;
			var displayValue=parameters.displayValue;
			if(invisible){
				return Ext.create('Ext.form.Label',{});
			}
			var cmb=Ext.create('Ext.form.ComboBox',{
				fieldLabel: label,
				store:Ext.create('Ext.data.Store', {
					fields:[
						{type: 'int', name: 'id'},
						{type: 'string', name: 'label'}
					],
					data:data.list
				}),
				valueField:'id',
				displayField: 'label',
				queryMode: 'local',
				name: name,
				grow:true,
				value:value
			});
			cmb.setValue(value);
			return cmb;
		}
	});
	
	
	
	var editItemCenterComponet;
	if(tabsLength>1){
		editItemCenterComponet=initScreenTabs();
	}else{
		editItemCenterComponet=initOneTab();
	}
	Ext.onReady(function(){
		itemCenterPanel.removeAll();
		itemCenterPanel.add(editItemCenterComponet);
		itemCenterPanel.doLayout();
	});
	
	function initOneTab(){
		var oneTab= Ext.create('Ext.panel.Panel', {
			margins: '0 0 0 0',
			border: false,
			bodyStyle: {
				overflow:'auto'
			},
			region:'center'
		});
		
		var panels=new Array(0);
		var aPanel;
		var aField=null;
		var fields;
		<@s.iterator value="tabs[0].panels" status="status">
			aPanel=Ext.create('Ext.panel.Panel', {
				//title: '<@s.property value="panel.label"/>',
				animCollapse: true,
				bodyPadding: 5,
				//collapsible: true,
				margin	 : '0 0 5 0',
				layout: {
					type: 'table',
					columns: <@s.property value="panel.colsNo"/>,
					tableAttrs: {
						style: {
							width: '100%'
						}
					}
				},
				defaults: {frame:false, border: false}
			});
			
			fields=new Array(0);
			
			<@s.iterator value="fields" status="status1">
				<@s.iterator value="top" status="status">
					<@s.if test="field!=null">
						<@s.if test="readOnlyMode">
							aField=Ext.create('${extReadOnlyClassName}').render({'label':'${label}',value:'<@s.property value="${nameMapping}"/>',displayValue:'<@s.property value="custom.displayValue"/>','name':'${nameMapping}'},${jsonData})
						</@s.if>
						<@s.else>
							aField=Ext.create('${extClassName}').render({'label':'${label}',value:'<@s.property value="${nameMapping}"/>',displayValue:'<@s.property value="custom.displayValue"/>','name':'${nameMapping}'},${jsonData})
						</@s.else>
						
					</@s.if>
					<@s.else>
						<#-- empty field -->
						aField=Ext.create('Ext.form.Label',{});
					</@s.else>
					fields.push(aField);
				</@s.iterator>
			</@s.iterator>
			aPanel.add(fields);
			panels.push(aPanel);
		</@s.iterator>
		
		oneTab.add(panels);
		return oneTab;
	}
	
	function initScreenTabs(){
		tabsRTEinitialized=new Object();
		var tabsItem1tt = Ext.createWidget('tabpanel',{
			margins: '0 0 0 0',
			id:'tabsItmeMainPanel',
			plain:true,
			defaults:{autoScroll: true},
			items:[
				<@s.iterator value="tabs" status="status">
					<@s.if test="#status.first"></@s.if><@s.else>,</@s.else>
					{id:'tt<@s.property value="tab.objectID"/>',
					autoScroll:true,
					title:<@s.if test="%{getText('item.form.tab.label.'+tab.label).equals('item.form.tab.label.'+tab.label)}">'<@s.property value="tab.label"/>',
						</@s.if><@s.else>'<@s.text name="%{getText('item.form.tab.label.'+tab.label)}"/>',</@s.else>
					/*layout: {
						type:'vbox',
						padding:'5',
						align:'stretch'
					},*/
					items:[
						<@s.iterator value="panels" status="status">
							<@s.if test="#status.first"></@s.if><@s.else>,</@s.else>
							{xtype: 'panel',
							//title: '<@s.property value="panel.label"/>',
							animCollapse: true,
							bodyPadding: 5,
							//collapsible: true,
							margin	 : '0 5 5 5',
							layout: {
								type: 'table',
								columns: <@s.property value="panel.colsNo"/>,
								tableAttrs: {
									style: {
										width: '100%'
									}
								}
							},
							defaults: {
								frame:false,
								border: false,
								bodyStyle:'padding-left:10px'
							},
							items:[
								<@s.iterator value="fields" status="status1">
									<@s.if test="#status1.first"></@s.if><@s.else>,</@s.else>
									<@s.iterator value="top" status="status">
										<@s.if test="#status.first"></@s.if><@s.else>,</@s.else>
										<@s.if test="field!=null">
											<@s.if test="readOnlyMode">
												<#include "../../WEB-INF/classes/plugins/fieldType/templates/readOnly_${templateName}">
											</@s.if>
											<@s.else>
												<#include "../../WEB-INF/classes/plugins/fieldType/templates/${templateName}">
											</@s.else>
										</@s.if>
										<@s.else>
											<#-- empty field -->
											{html:'&nbsp;'}
										</@s.else>
									</@s.iterator>
								</@s.iterator>
							]}
						</@s.iterator>
					],
					<@s.if test="!readOnlyMode">
						listeners: {activate: handleActivateScreenItemTab}
					</@s.if>
					<@s.else>
					 	listeners: {activate: handleActivateReadOnlyScreenItemTab}
					</@s.else>
					}	
				</@s.iterator>	
			]
		});
		return tabsItem1tt;
	}
	function handleActivateScreenItemTab(tab){
		//initTabEditor(tab.id);
	}
	function handleActivateReadOnlyScreenItemTab(tab){
		//refreshTabEditSize(tab);
	}
</script>

