<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<div id="selectionWindow">
	<div class="ui-overlay" >
		<div class="ui-widget-shadow ui-corner-all selection_dialog_shadow" ></div>
	</div>
	<div class="selection_dialog ui-widget-content ui-corner-all">
		<div class="ui-widget-header ui-corner-all dialog_header" ><fmt:message key="waspIlluminaPlugin.clustersQc_dialogTitle.label" /></div>
		<div  class="verifyQualityForm" class="center">		
			<p><fmt:message key="waspIlluminaPlugin.clustersQc_dialogMessage.label" /></p>
			<form id="qualityForm"  method="post">
			<table >
<c:forEach items="${cellIndexList}" var="index" >

				<tr>
					<td class="formLabel vcenter"><fmt:message key="waspIlluminaPlugin.displayQc_lane.label" /><c:out value="${index}" /> : </td>
					<td class="vcenter">
						<div id="radioL<c:out value="${index}" />">
							<input type="radio" id="passL<c:out value="${index}" />" name="radioL<c:out value="${index}" />" size="25" value="1" 
								<c:if test="${not empty(existingQcValuesIndexed.get(index).isPassedQc()) && existingQcValuesIndexed.get(index).isPassedQc() == true}"> checked="checked"</c:if> /><label for="passL<c:out value="${index}" />" ><fmt:message key="waspIlluminaPlugin.displayQc_pass.label" /></label>
							<input type="radio" id="failL<c:out value="${index}" />" name="radioL<c:out value="${index}" />" size="25" value="0"  
								<c:if test="${(not empty(existingQcValuesIndexed.get(index).isPassedQc())) && existingQcValuesIndexed.get(index).isPassedQc() == false}"> checked="checked"</c:if> /><label for="failL<c:out value="${index}" />" ><fmt:message key="waspIlluminaPlugin.displayQc_fail.label" /></label>
						</div>
					</td>
					<td class="formLabel vcenter"><fmt:message key="waspIlluminaPlugin.displayQc_comments.label" /></td>
					<td class="vcenter"><textarea style="resize: none;" name="commentsL<c:out value="${index}" />" rows="3" cols="20" maxlength="100"><c:if test="${not empty(existingQcValuesIndexed.get(index).getComment())}"><c:out value="${existingQcValuesIndexed.get(index).getComment()}" /></c:if></textarea></td>
				</tr>
</c:forEach>
					
				</table>
				</form>
				<br />
				<center><button id="submitForm"><fmt:message key="waspIlluminaPlugin.displayQc_continue.label" /></button><button id="cancelForm"><fmt:message key="waspIlluminaPlugin.displayQc_cancel.label" /></button></center>
		</div>
	</div>
</div>


<div id="main" class="center">
	<p class="ui-state-default ui-corner-all ui-helper-clearfix qc_title">
		<span class="ui-icon ui-icon-info infoIcon"></span>
		<fmt:message key="waspIlluminaPlugin.clustersQc_title.label" /><c:out value="${runName}" />
	</p>
	<p><fmt:message key="waspIlluminaPlugin.clustersQc_instructions.label" /></p>
	<div id="displayWindow" style="float:right;"><button id="showForm"><fmt:message key="waspIlluminaPlugin.displayQc_continue.label" /></button></div>
	<div id="mainImage" class="ui-widget-content ui-corner-all"><img src='<c:out value="${runReportBaseImagePath}/NumClusters%20By%20Lane.png" />' height='400' width='800'></div>
</div>	

<div id="error_dialog-modal" title="<fmt:message key="waspIlluminaPlugin.displayQc_warningTitle.label" />" >
	<p><span class="ui-icon ui-icon-alert alert_icon" ></span><span id="warningText"></span></p>
</div>

