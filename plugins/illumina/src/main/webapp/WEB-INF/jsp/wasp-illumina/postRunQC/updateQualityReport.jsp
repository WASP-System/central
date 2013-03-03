<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<div id="error_dialog-modal" title="Warning" >
	<p><span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;" ></span><span id="warningText"></span></p>
</div>

<div id="main" class="center">
	<p class="ui-state-default ui-corner-all ui-helper-clearfix" style="padding:4px; font-size: 14px">
		<span class="ui-icon ui-icon-info" style="float:left; margin:-2px 5px 0 0;"></span>
		Illumina OLB Stats: Finalize Validation
	</p>
	<p>Please review this summary of your responses in the table below and make a decision whether or not to accept or reject each lane. Note that:
	<ul>
		<li>Libraries on rejected lanes will not be analysed. </li>
		<li>It is mandatory to write comments on the decision if a lane is rejected.</li>
		<li>The decision and comments will appear on user Wiki pages for relevant jobs.</li>
	</ul>
	</p>
	<br />
	
	<table id='validationTable' align="center">
		<form id="qualityForm" method="post">
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
					<c:if test="${passed == true}"><div><img src='/wasp/images/wasp-illumina/postRunQC/pass.png'></div></c:if>
					<c:if test="${passed == false}">
						<div><img src='/wasp/images/wasp-illumina/postRunQC/fail.png'></div>
						<c:if test="${comment.length() > 0}"><div><c:out value="${comment}" /></div></c:if>
					</c:if>
				</td>
			</c:forEach>
			<td>
				<div id="radioL<c:out value="${index}" />">
					<input type="radio" id="passL<c:out value="${index}" />" name="radioL<c:out value="${index}" />" size="25" value="1" 
						<c:if test="${not empty(existingQcValuesIndexed.get(index).isPassedQc()) && existingQcValuesIndexed.get(index).isPassedQc() == true}"> checked="checked"</c:if> />
					<label for="passL<c:out value="${index}" />" >Accept</label>
					<input type="radio" id="failL<c:out value="${index}" />" name="radioL<c:out value="${index}" />" size="25" value="0" 
						<c:if test="${(not empty(existingQcValuesIndexed.get(index).isPassedQc())) && existingQcValuesIndexed.get(index).isPassedQc() == false}"> checked="checked"</c:if>/>
					<label for="failL<c:out value="${index}" />" >Reject</label>
				</div>
			</td>
			<td><textarea style="resize: none;" name="commentsL<c:out value="${index}" />" id="commentsL<c:out value="${index}" />" rows="4" cols="20" maxlength="100"><c:if test="${not empty(existingQcValuesIndexed.get(index).getComment())}"><c:out value="${existingQcValuesIndexed.get(index).getComment()}" /></c:if></textarea></td>
			</tr>
		</c:forEach>
		</form>
		<tr><th  colspan="7"><center><button id="submitForm">Submit Run QC</button></center></th></tr>
	</table>
	
</div>	


