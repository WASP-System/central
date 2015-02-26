<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<c:import url="/WEB-INF/jsp/job/home/fadingMessage.jsp" />

<script type="text/javascript">

$(document).ready(function() {
	
	$( "#analysisSelected" ).change(function(){
		if($(this).val() == "Yes"){
			alert("it's YES");
			$("#softwareSelections").css("display", "inline");
		}
		else{
			alert("Unfortunately IT IS NO");
			$("#softwareSelections").css("display", "none");
		}
	});
});

</script>

<br />
<c:choose>
	<c:when test="${updatePermitted == false }">
		<h1>Updates No Longer Permitted For This Job</h1>
		<h2>Job Status: <c:out value="${jobStatus}"/></h2>
	</c:when>
	<c:otherwise>
		<form  method='post' name='analysisForm' id='analysisFormId' onsubmit='postFormWithAjax("analysisFormId","<wasp:relativeUrl value="job/${job.getId()}/updateAnalysisRequested.do" />"); return false;'>
			<table class="data" style="margin: 0px 0px">		
				<tr class="FormData"  >
					<th class="label-centered" style="background-color:#FAF2D6">Update Analysis</th>
				</tr>
				<tr>
					<td class="DataTD value-centered">
						Perform Post-Sequencing Primary Analysis?:&nbsp;						
						<select name="analysisSelected" id="analysisSelected" size="1" >
							<c:forEach items="${isAnalysisRequestedOptions}" var="option">
								<c:set value="" var="selected"/>
								<c:if test="${option == currentIsAnalysisRequested }"> <c:set value="selected" var="selected"/> </c:if>
								<option value="${option}" ${selected}><c:out value="${option}" /> </option>
							</c:forEach>
						</select>
						
						<div id="softwareSelections" style="display:none">
							<c:forEach items="${softwareResourceTypeList}" var="softwareResourceType">
								<br />
								<c:out value="${softwareResourceType.getName()}" />:&nbsp;
								<c:set value="${resourceTypeSoftwareListMap.get(softwareResourceType) }" var="softwareList"/>								
								<select name="selectedSoftware" id="selectedSoftware" size="1" >
									<c:forEach items="${softwareList}" var="software">
										<option value="${software.getId()}" ><c:out value="${software.getName()}" /> </option>
									</c:forEach>
								</select>								
							</c:forEach>							
						</div>
						
					</td>
				</tr>
				<tr class="FormData">
					<td  class="DataTD value-centered">
						<input type='reset' value='Reset'/>
						<input type='submit' value='Update'/>
					</td>
				</tr>	
			</table>
		</form>
	</c:otherwise>
</c:choose>