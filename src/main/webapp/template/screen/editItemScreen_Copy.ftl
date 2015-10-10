<#assign s=JspTaglibs["/WEB-INF/tlds/struts2/struts-tags.tld"]>
<#assign app=JspTaglibs["/WEB-INF/tlds/app.tld"]>
<@s.set name="iconsPath"
    	value="'${request.getContextPath()}'+'/design/'+#session.userDesignPath+'/icons/'"/>
<#-- conversion error of the editItemScreenAction -->
<@s.set name="alignR" value="#session.alignR" scope="request"/>
<div id="conversionErrors">
<@s.fielderror/>
</div>
<@s.if test="%{editCustomTemplate!=null}">
	<#include "../../WEB-INF/classes/${editCustomTemplate}">
	<div class="containerSeparator">&nbsp;</div>
</@s.if>
<#-- tabs -->
<@s.token/>
<script type="text/javascript">
	var tabsLength=<@s.property value="tabs.size()"/>
	var tabsRTEinitialized=new Object();
</script>
<@s.if test="tabs.size()==1">
	<div id="tabConted1">
		<!--this DIV is "trick" for IE6-->
		<div class="containerSeparator" style="height:0px;">&nbsp;</div>
		<@s.iterator value="tabs[0].panels" status="status">
			<@s.if test="!#status.first">
				<div class="containerSeparator">&nbsp;</div>
			</@s.if>
			<div  class="<@s.if test="#status.even">containerRow1</@s.if><@s.else>containerRow2</@s.else>"
				id="<@s.property value="panel.objectID"/>">
				<table class="containerTable">
					<@s.iterator value="fields" status="status1">
					<TR>
						<@s.iterator value="top" status="status">
							<@s.if test="top!=null">
								<@s.if test="field!=null">
									<@s.if test="labelInsideTemplate">
									     <td colspan="${2*field.colSpan}" rowspan="${field.rowSpan}">
									        <#-- <#include "field/${templateName}"> -->
									        <@s.if test="readOnlyMode">
									        	<#include "../../WEB-INF/classes/plugins/fieldType/templates/readOnly_${templateName}">
									        </@s.if>
									        <@s.else>
									        	<#include "../../WEB-INF/classes/plugins/fieldType/templates/${templateName}">
									        </@s.else>
									     </td>
									</@s.if>
									<@s.else>										
									    <td nowrap class="screenFromDataName" align="${labelHAlign}" valign="${labelVAlign}" rowspan="${field.rowSpan}">
									    	<@s.if test="readOnlyMode">
		                                    	<b><label for="${nameMapping}">${label}&nbsp:</label></b>
		                                    </@s.if>
									        <@s.else>
									        	<label class="<@s.if test="required">required</@s.if>" for="${nameMapping}">${label}&nbsp:</label>
									        </@s.else>
		                                </td>		                                
		                                <td nowrap align="${valueHAlign}" valign="${valueVAlign}" class="screenFromDataValue" colspan="${2*field.colSpan-1}" rowspan="${field.rowSpan}">		                                                                       
		                                    <#-- <#include "field/${templateName}"> -->
		                                    <@s.if test="readOnlyMode">
									        	<#include "../../WEB-INF/classes/plugins/fieldType/templates/readOnly_${templateName}">
									        </@s.if>
									        <@s.else>
									        	<#include "../../WEB-INF/classes/plugins/fieldType/templates/${templateName}">
									        </@s.else>
		                                </td>
								    </@s.else>
								</@s.if>
								<@s.else>
									<#-- empty field -->
									<TD colspan="2">&nbsp;</TD>
								</@s.else>
							</@s.if>
						</@s.iterator>
					</TR>
					</@s.iterator>
				</table>
			</div>
		</@s.iterator>
	</div>
</@s.if>
<@s.else>
	
		<@s.iterator value="tabs" status="status1">
			<div id="tab_<@s.property value="tab.objectID"/>" class="x-hide-display">
				<!--this DIV is "trick" for IE6-->
				<div class="containerSeparator" style="height:1px;">&nbsp;</div>
				<@s.iterator value="panels" status="status">
					<@s.if test="!#status.first">
						<div class="containerSeparator">&nbsp;</div>
					</@s.if>
					<div  class="<@s.if test="#status.even">containerRow1</@s.if><@s.else>containerRow2</@s.else>"
						id="<@s.property value="panel.objectID"/>"  
						style="border-left:none;border-right:none;<@s.if test="#status.first">border-top:none;</@s.if><@s.if test="#status.last">border-bottom:none;</@s.if>" >
						<table id="<@s.property value="panel.objectID"/>_table" class="containerTable">
							<@s.iterator value="fields" status="status1">
							<TR>
								<@s.iterator value="top" status="status">
									<@s.if test="top!=null">
										<@s.if test="field!=null">
											<@s.if test="labelInsideTemplate">
											     <td colspan="${2*field.colSpan}" rowspan="${field.rowSpan}">
											        <#-- <#include "field/${templateName}"> -->
											        <@s.if test="readOnlyMode">
									        			<#include "../../WEB-INF/classes/plugins/fieldType/templates/readOnly_${templateName}">
											        </@s.if>
											        <@s.else>
											        	<#include "../../WEB-INF/classes/plugins/fieldType/templates/${templateName}">
											        </@s.else>
											     </td>
											</@s.if>
											<@s.else>
											    <td nowrap class="screenFromDataName" align="${labelHAlign}" valign="${labelVAlign}" rowspan="${field.rowSpan}">
				                                    <@s.if test="readOnlyMode">
				                                    	<b><label for="${nameMapping}">${label}&nbsp:</label></b>
				                                    </@s.if>
											        <@s.else>
											        	<label class="<@s.if test="required">required</@s.if>" for="${nameMapping}">${label}&nbsp:</label>
											        </@s.else>				                                    
				                                </td>
				                                <TD nowrap align="${valueHAlign}" valign="${valueVAlign}" class="screenFromDataValue" colspan="${2*field.colSpan-1}" rowspan="${field.rowSpan}">				                                                                        
				                                    <#-- <#include "field/${templateName}"> -->
				                                    <@s.if test="readOnlyMode">
											        	<#include "../../WEB-INF/classes/plugins/fieldType/templates/readOnly_${templateName}">
											        </@s.if>
											        <@s.else>
											        	<#include "../../WEB-INF/classes/plugins/fieldType/templates/${templateName}">
											        </@s.else>
				                                </TD>
										    </@s.else>
										</@s.if>
										<@s.else>
											<#-- empty field -->
											<TD colspan="2">&nbsp;</TD>
										</@s.else>
									</@s.if>
								</@s.iterator>
							</TR>
							</@s.iterator>
						</table>
					</div>
				</@s.iterator><#--end it on panels-->
			</div>
		</@s.iterator><#--end it on tabs--> 	

    <@s.if test="tabs.size()>1">
		<script type="text/javascript">
		var tabsItem1;
		Ext.onReady(function(){
			itemCenterPanel.removeAll();
			var comp=initScreenTabs();
			itemCenterPanel.add(comp);
			itemCenterPanel.doLayout();
		});
		function initScreenTabs(){
			tabsRTEinitialized=new Object();
			var tabsItem1tt = Ext.createWidget('tabpanel',{
				margins: '0 0 0 0',
				id:'tabulupeste',
				plain:true,
				defaults:{autoScroll: true}, 
				items:[
					<@s.iterator value="tabs" status="status">
						<@s.if test="#status.first"></@s.if><@s.else>,</@s.else>
						{id:'tt<@s.property value="tab.objectID"/>',
						autoScroll:true,
						 title:<@s.if test="%{getText('item.form.tab.label.'+tab.label).equals('item.form.tab.label.'+tab.label)}">'<@s.property value="tab.label"/>',
				            </@s.if><@s.else>'<@s.text name="%{getText('item.form.tab.label.'+tab.label)}"/>',</@s.else>
						 contentEl: 'tab_<@s.property value="tab.objectID"/>'
						 <@s.if test="!readOnlyMode">
						 	,listeners: {activate: handleActivateScreenItemTab}
						 </@s.if>
						 <@s.else>
						 	,listeners: {activate: handleActivateReadOnlyScreenItemTab}
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
	</@s.if>
</@s.else>
<script type="text/javascript">
	var tabsItem1=null;
	var inlineEdit=false;
	function changeToEdit(nameMapping,richText){
		if(inlineEdit){
			doChangeToEdit(nameMapping,richText);
		}else{
			var urlStr="editItemInline.action";
			Ext.Ajax.request({
				url: addTimeToAjaxUrl(urlStr),
				disableCaching:true, 
				encoding: "utf-8",
				sync: true,
				success: function(data){
					if(data.responseText=="ok"){
						var divClearParent=document.getElementById("clearParentDiv");
						if(divClearParent!=null){
							divClearParent.style.display='block';
						}
						doChangeToEdit(nameMapping,richText);
					}else if(data.responseText=="clientHandler"){
						window.location.href='item!editItem.action?forwardAction=printItem&workItemID='+itemID;
					}else{
						showHtmlDetail("Error",data.responseText);
					}
				},
				failure: function(type, error){
					alert("Can't edit item:"+error);
				}
			});
		}
	}
	function doChangeToEdit(nameMapping,richText){
		inlineEdit=true;
		document.getElementById("forwardAction").value="printItem";
		var tollbarLink='toolbar.action?workItemID='+itemID+"&forwardAction=printItem";
		refreshToolbar(tollbarLink);
		document.getElementById('readOnly_'+nameMapping).style.display='none';
		document.getElementById('edit_'+nameMapping).style.display='block';
		if(richText){
			replaceTextArea(nameMapping);
		}else{
			var elem=document.getElementById(nameMapping);
			var evType="KeyPress";
			elem.focus();
		}
		document.forms[2].setAttribute('action',"javaScript:doSave();");
	}
	function onEnterpress(e){
	    var KeyPress 
	    if(e && e.which){
	        KeyPress = e.which
	    }else{
	        e = event
	        KeyPress = e.keyCode
	    }
	    if(KeyPress == 13){
	       doSave();
	       return false;    
	    }else{
	        return true;
	    }
	}
</script>
