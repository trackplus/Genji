<#list revisions as x>
	<#if x_index=0>
    <div class="historyRow" style="border-top: 0; margin-left: 0px;margin-right:0px;margin-top:5px;background-color: white;">
	<#else>
    <div class="historyRow" style="margin-left: 0px;margin-right:0px;margin-top:5px;background-color: white;">
	</#if>
		<table width="100%" border="0" cellPadding="0" cellSpacing="0" style="table-layout:fixed;">
			<tr style="height:0px;padding:0px;border:none;margin:0px;">
				<td style="width:75px;padding:0px;border:none;margin:0px;"></td>
				<td style="width:150px;padding:0px;border:none;margin:0px;"></td>
				<td style="width:100px;padding:0px;border:none;margin:0px;"></td>
				<td style="width:100%;padding:0px;border:none;margin:0px;"></td>
			</tr>
			<tr class="dataRow1">
				<td class="dataName" style="padding-bottom:5px">
					${x.revisionNo}
				</td>
                <td class="dataName" style="width:100%; padding-bottom:5px;">
					${x.revisionDate}
				</td>
				<td class="dataName" style="width:100%; padding-bottom:5px;">

					${x.revisionAuthor}
				</td>
                <td class="dataName" style="width:100%; padding-bottom:5px;">
					${x.repository}
                </td>
			</tr>
			<#if x.revisionComment??>
				<tr class="dataRow1">
					<td></td>
					<td class="dataValue" style="white-space: normal;" colspan="3">
						${x.revisionComment}
					</td>
				</tr>
			</#if>
			<#if x.changedPaths??>
				<#list x.changedPaths as y>
					<tr class="dataRow1">
						<td></td>
						<td class="dataName" style="white-space: normal;" colspan="3" >
                            &nbsp;${y.localizedType}&nbsp;&nbsp;
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
	</div>
</#list>