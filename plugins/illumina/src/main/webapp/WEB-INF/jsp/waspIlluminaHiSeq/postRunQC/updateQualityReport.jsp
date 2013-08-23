<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wasp" uri="http://einstein.yu.edu/wasp" %>

<div id="error_dialog-modal" title="<fmt:message key="waspIlluminaPlugin.displayQc_warningTitle.label" />" >
	<p><span class="ui-icon ui-icon-alert alert_icon" ></span><span id="warningText"></span></p>
</div>

<div id="main" class="center">
	<p class="ui-state-default ui-corner-all ui-helper-clearfix qc_title" >
		<span class="ui-icon ui-icon-info infoIcon"></span>
		<fmt:message key="waspIlluminaPlugin.updateQualityReport_title.label" /><c:out value="${runName}" />
	</p>
	<p><fmt:message key="waspIlluminaPlugin.updateQualityReport_instructions.label" />
	</p>
	<br />
	<form id="qualityForm" method="post">
		<table id='validationTable' align="center">
			<tr>
				<th><fmt:message key="waspIlluminaPlugin.updateQualityReport_lane.label" /></th>
				<c:forEach items="${qcHeadingsByMetaKey.keySet()}" var="metaKey">
					<th><c:out value="${qcHeadingsByMetaKey.get(metaKey)}" /></th>
				</c:forEach>
				<th><fmt:message key="waspIlluminaPlugin.updateQualityReport_finalDescision.label" /></th>
				<th><fmt:message key="waspIlluminaPlugin.updateQualityReport_comments.label" /></th>
			</tr>
			
			<c:forEach items="${cellIndexList}" var="index" >
				<tr>
					<td><c:out value="${index}" /></td>
				<c:forEach items="${qcDataMap.keySet()}" var="qcDataMetaKey" >
					<c:set var="passed" value="${fn:trim(qcDataMap.get(qcDataMetaKey).get(index).isPassedQc())}" />
					<c:set var="comment" value="${fn:trim(qcDataMap.get(qcDataMetaKey).get(index).getComment())}" />
					<td class='fixedWidth'>
						<c:if test="${passed == true}"><wasp:successIcon value="${comment}" /></c:if>
						<c:if test="${passed == false}"><wasp:failureIcon value="${comment}" /></c:if>
					</td>
				</c:forEach>
				<td>
					<div class="radio-jquery-ui">
						<input type="radio" id="passL<c:out value="${index}" />" name="radioL<c:out value="${index}" />" size="25" value="1" 
							<c:if test="${not empty(existingQcValuesIndexed) && not empty(existingQcValuesIndexed.get(index)) && existingQcValuesIndexed.get(index).isPassedQc() == true}"> checked="checked"</c:if> />
						<label for="passL<c:out value="${index}" />" ><fmt:message key="waspIlluminaPlugin.updateQualityReport_accept.label" /></label>
						<input type="radio" id="failL<c:out value="${index}" />" name="radioL<c:out value="${index}" />" size="25" value="0" 
							<c:if test="${not empty(existingQcValuesIndexed) && not empty(existingQcValuesIndexed.get(index)) && existingQcValuesIndexed.get(index).isPassedQc() == false}"> checked="checked"</c:if>/>
						<label for="failL<c:out value="${index}" />" ><fmt:message key="waspIlluminaPlugin.updateQualityReport_reject.label" /></label>
					</div>
				</td>
				<td><textarea style="resize: none;" name="commentsL<c:out value="${index}" />" id="commentsL<c:out value="${index}" />" rows="4" cols="20" maxlength="100"><c:if test="${not empty(existingQcValuesIndexed) && not empty(existingQcValuesIndexed.get(index)) && not empty(existingQcValuesIndexed.get(index).getComment())}"><c:out value="${existingQcValuesIndexed.get(index).getComment()}" /></c:if></textarea></td>
				</tr>
			</c:forEach>
		</table>
	</form>
	<center><button id="submitForm"><fmt:message key="waspIlluminaPlugin.updateQualityReport_submitButton.label" /></button></center>
	
	
</div>	


