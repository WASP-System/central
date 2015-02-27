<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<c:import url="/WEB-INF/jsp/job/home/fadingMessage.jsp" />

<script type="text/javascript">

$(document).ready(function() {
	
	$( "#analysisSelected" ).change(function(){
		if($(this).val() == "Yes"){
			$("#softwareSelections").css("display", "inline");
		}
		else{
			$("#softwareSelections").css("display", "none");
		}
	});
});

</script>

<br />
<c:choose>
	<c:when test="${updatePermitted == false }">
		<h1><fmt:message key="jobHomeBasicUpdate.updatesNoLongerPermitted.label" /></h1>
		<h2><fmt:message key="jobHomeBasicUpdate.jobStatus.label" />: <c:out value="${jobStatus}"/></h2>
	</c:when>
	
	<c:when test="${updatePermitted == true && empty softwareResourceTypeList }">
		<table class="data" style="margin: 0px 0px">		
			<tr class="FormData"  >
				<th class="label-centered" style="background-color:#FAF2D6"><fmt:message key="jobHomeBasicUpdate.updateAnalysis.label" /></th>
			</tr>
			<tr>
				<td class="DataTD value-centered">
					<fmt:message key="jobHomeBasicUpdate.noSoftwareAvailableForThisWorkflow.label" />: <c:out value="${job.getWorkflow().getName()}" />
				</td>
			</tr>
		</table>
	</c:when>
	<c:when test="${updatePermitted == true && not empty softwareResourceTypeList }">
		<form  method='post' name='analysisForm' id='analysisFormId' onsubmit='postFormWithAjax("analysisFormId","<wasp:relativeUrl value="job/${job.getId()}/updateAnalysisRequested.do" />"); return false;'>
			<table class="data" style="margin: 0px 0px">		
				<tr class="FormData"  >
					<th class="label-centered" style="background-color:#FAF2D6"><fmt:message key="jobHomeBasicUpdate.updateAnalysis.label" /></th>
				</tr>
				<tr>
					<td class="DataTD value-centered">
						<fmt:message key="jobHomeBasicUpdate.performPostSequencingPrimaryAnalysis.label" />:&nbsp;						
						<select name="analysisSelected" id="analysisSelected" size="1" >
							<c:forEach items="${isAnalysisRequestedOptions}" var="option">
								<c:set value="" var="selected"/>
								<c:if test="${option == currentIsAnalysisRequested }"> <c:set value="selected" var="selected"/> </c:if>
								<option value="${option}" ${selected}><c:out value="${option}" /> </option>
							</c:forEach>
						</select>
						
						<c:set value="none" var="displaySetting"/>
						<c:if test='${currentIsAnalysisRequested == "Yes"}'> <c:set value="inline" var="displaySetting"/> </c:if>
						
						<div id="softwareSelections" style="display:${displaySetting}">
							<c:forEach items="${softwareResourceTypeList}" var="softwareResourceType">
								<br />
								<c:out value="${softwareResourceType.getName()}" />:&nbsp;
								<c:set value="${resourceTypeSoftwareListMap.get(softwareResourceType) }" var="softwareList"/>								
								<select name="selectedSoftware" id="selectedSoftware" size="1" >
									<c:forEach items="${softwareList}" var="software">
										<c:set value="" var="selected_two"/>
										<c:forEach items="${currentSoftwareForThisJob}" var="currentSoftwareForThisJob">
											<c:if test="${currentSoftwareForThisJob.getId() == software.getId() }"> <c:set value="selected" var="selected_two"/> </c:if>
										</c:forEach>
										<option value="${software.getId()}" ${selected_two}><c:out value="${software.getName()}" /> </option>
									</c:forEach>
								</select>								
							</c:forEach>							
						</div>
						
					</td>
				</tr>
				<tr class="FormData">
					<td  class="DataTD value-centered">
						<input type='reset' value='<fmt:message key="jobHomeBasicUpdate.reset.label" />'/>
						<input type='submit' value='<fmt:message key="jobHomeBasicUpdate.update.label" />'/>
					</td>
				</tr>	
			</table>
		</form>
	</c:when>
	
</c:choose>