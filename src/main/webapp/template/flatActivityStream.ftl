<#if allActivityStreamItems?has_content>
<#list allActivityStreamItems as x>
	<#if x_index=0>
    <div class="historyRow" style="border-top: 0; padding-left:5px;margin-left: 0px;margin-right:0px;margin-top:0px;">
	<#else>
    <div class="historyRow" style="padding-left:5px;margin-left: 0px;margin-right:0px;margin-top:0px;">
	</#if>
    <table width="100%" border="0" cellPadding="0" cellSpacing="0" style="table-layout:fixed;">
	<tr>
		<td width="65" style="vertical-align: top;text-align: left">
        	<#if x.personID??>
        		<img width="50" height="50" src="avatar!download.action?personID=${x.personID}"/>
			<#else>
                <img width="50" height="50" src="${iconsPath}/icons/avatar.png"/>
			</#if>
    	</td>
		<td style="width:100%;;padding:0px;border:none;margin:0px;">
		<table width="100%" border="0" cellPadding="0" cellSpacing="0" style="table-layout:fixed;">
			<tr style="height:0px;padding:0px;border:none;margin:0px;">
				<td style="width:20px;padding:0px;border:none;margin:0px;"></td>
				<td style="width:100%;padding:0px;border:none;margin:0px;"></td>
				<td style="width:50%;padding:0px;border:none;margin:0px;"></td>
				<td style="width:50%;padding:0px;border:none;margin:0px;"></td>
			</tr>
			<tr class="dataRow1">
				<td class="dataName">
					<img width="16" height="16" src="${iconsPath}/16x16/${x.iconName}"/>
				</td>
				<td class="dataName" style="width:100%; padding-bottom:5px;" colspan="3">
					${x.dateFormatted}&nbsp;&nbsp;${x.changedByName}
				</td>
			</tr>
			<tr class="dataRow1">
				<td class="dataName" colspan="3">
					${x.project}&nbsp;/&nbsp;<span class="emphasize">${x.itemID}</span>&nbsp;
					<a href="printItem.action?key=${x.workItemID}" target="printItem${x.workItemID}"  class="synopsis_blue">${x.title}</a>
				</td>
			</tr>


			<!--history entries-->
			<#if x.historyEntries??>
				<#list x.historyEntries as y>
					<tr class="dataRow1">
						<#if y.changedText?has_content>
							<td class="dataName" colspan="4" style="white-space: normal;overflow:hidden;">
							${y.changedText}
							</td>
						<#else>
							<#if y.newValue??&&y.oldValue??>
								<td class="dataValue" colspan="2" style="white-space: normal;">
									<span class="dataEmphasize">
										<#if y.newValue??>
											<#if y.fieldLabel??>
												${y.fieldLabel}
											</#if>
											${y.newValue}
										</#if>
									</span>
								</td>
								<td class="dataValue" colspan="2" style="white-space: normal;">
									<span class="dataEmphasize">
										<#if y.oldValue??>
											${y.oldValue}
										</#if>
									</span>
								</td>
							<#else>
								<td class="dataValue" colspan="4" style="white-space: normal;">
									<span class="dataEmphasize">
										<#if y.fieldLabel??>
											${y.fieldLabel}
										</#if>
										<#if y.newValue??>
											${y.newValue}
										<#else>
											${y.oldValue}
										</#if>
									</span>
								</td>
							</#if>
						</#if>
					</tr>
				</#list>
			</#if>
			<#if x.historyLongEntries??>
				<#list x.historyLongEntries as z>
					<#if z.diff??>
						<tr>
							<td colspan="3" class="dataValue" style="white-space: normal;vertical-align: top;padding-bottom: 2px;">
							${z.diff}
							</td>
						</tr>
					<#else>
						<#if z.fieldLabel?has_content>
							<tr>
							   <td colspan="3" class="dataName" style="white-space: normal;height: 19px">
									<strong>
										<#if z.fieldLabel??>
											${z.fieldLabel}
										</#if>
									</strong>
								</td>
							</tr>
						</#if>
						<#if z.newValue?has_content>
							<tr>
								<td colspan="3" class="dataValue" style="white-space: normal;vertical-align: top;padding-bottom: 2px;">
									<#if z.newValue??>
										${z.newValue}
									</#if>
								</td>
							</tr>
						</#if>
						<#if z.oldValue?has_content>
							<tr>
								<td colspan="3" class="dataValue" style="white-space: normal;vertical-align: top;padding-bottom: 2px;">
									<#if z.oldValue??>
										${z.oldValue}
									</#if>
								</td>
							</tr>
						</#if>
					</#if>
				</#list>
			</#if>


			<!--version control -->
			<#if x.revisionComment??>
				<tr class="dataRow1">
					<td class="dataValue" style="white-space: normal;" colspan="3">
						${x.revisionNo}:&nbsp;${x.repository}
					</td>
				</tr>
				<tr class="dataRow1">
					<td class="dataValue" style="white-space: normal;" colspan="3">
						${x.revisionComment}
					</td>
				</tr>
			</#if>
			<#if x.changedPaths??>
				<#list x.changedPaths as y>
					<tr class="dataRow1">
						<td class="dataName" style="white-space: normal;" colspan="5" >
							${y.localizedType}&nbsp;&nbsp;
							<#if y.link??>
								<a href="${y.link}" target="fileDiff" cls="synopsis_blue">${y.path}</a>
							<#else>
							${y.path}
							</#if>
						</td>
					</tr>
				</#list>
			</#if>
		</table>
       </td>
    </tr>
    </table>
</div>
</#list>
<#else>
	<#if tooManyItems>
		<div class="infoBox1">${errorMessage}</div>
	<#else>
		<div class="infoBox1">&nbsp;</div>
	</#if>
</#if>



