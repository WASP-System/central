<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<c:choose>
<c:when test="${controlLibrary.getSampleId().intValue()==0}">
	<h1><fmt:message key="sample.updateControlLib_create.label" /></h1>
</c:when>
<c:otherwise>
	<h1><fmt:message key="sample.updateControlLib_update.label" /></h1>
</c:otherwise>
</c:choose>

<form method="POST" id ="form" name ="form" action="<wasp:relativeUrl value="/sample/createUpdateLibraryControl.do" />">
<input type='hidden' name='sampleId' value='<c:out value="${controlLibrary.getSampleId().intValue()}" />'/>
<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><td class="CaptionTD"><fmt:message key="sample.updateControlLib_name.label" />: </td><td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" type="text" name="name" id="name" size='20' maxlength='45' value="<c:out value="${controlLibrary.getName()}"/>" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="sample.updateControlLib_adaptorSet.label" />: </td><td class="DataTD">
				<select class="FormElement ui-widget-content ui-corner-all" name="adaptorsetId" id="adaptorsetId" size="1" >
				<option value="">--<fmt:message key="sample.updateControlLib_selectASet.label" />--
				<c:forEach items="${adaptorsetList}" var="adaptorset">
					<c:set var="selectedAdaptorSet" value="" scope="page" />
					<c:if test="${adaptorset.getAdaptorsetId()==controlLibraryAdaptor.getAdaptorsetId()}">
						<c:set var="selectedAdaptorSet" value="SELECTED" scope="page" />
					</c:if>
					<option value="<c:out value="${adaptorset.getAdaptorsetId()}" />" <c:out value="${selectedAdaptorSet}"/> ><c:out value="${adaptorset.getName()}" /> 
				</c:forEach>
				</select>
</td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="sample.updateControlLib_adaptor.label" />: </td><td class="DataTD">
				<select class="FormElement ui-widget-content ui-corner-all" name="adaptorId" id="adaptorId" size="1" >
				
				<c:if test="${controlLibrary.getSampleId().intValue() != 0}">
				<option value="">--<fmt:message key="sample.updateControlLib_selectAdaptor.label" />--
				<c:forEach items="${adaptorList}" var="adaptor">
					<c:set var="selectedAdaptor" value="" scope="page" />
					<c:if test="${adaptor.getAdaptorId()==controlLibraryAdaptor.getAdaptorId()}">
						<c:set var="selectedAdaptor" value="SELECTED" scope="page" />
					</c:if>
					<option value="<c:out value="${adaptor.getAdaptorId()}" />" <c:out value="${selectedAdaptor}"/> /><fmt:message key="sample.updateControlLib_index.label" /> <c:out value="${adaptor.getBarcodenumber()}"/>&nbsp;(<c:out value="${adaptor.getBarcodesequence()}"/>)
				</c:forEach>
				</c:if>
				</select>
</td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="sample.updateControlLib_isActive.label" />: </td>
<td class="DataTD">
				<c:set var="active" value='${controlLibrary.getIsActive()==1?"checked":""}' scope="page" />
				<c:set var="inactive" value='${controlLibrary.getIsActive()==1?"":"checked"}' scope="page" />
					<input type="radio" id="active" name="active" <c:out value="${active}" /> value="1"> <fmt:message key="sample.updateControlLib_active.label" /> 
					&nbsp;&nbsp;<input type="radio" name="active" <c:out value="${inactive}" /> value="0"> <fmt:message key="sample.updateControlLib_inactive.label" /> 

</td>
</tr>
<tr class="FormData"><td class="DataTD" colspan="2">
<c:set var="disabled" value="" scope="page" />
<c:if test="${controlLibrary.getSampleId().intValue()==0}">
	<c:set var="disabled" value="disabled" scope="page" />
</c:if>
<sec:authorize access="hasRole('su') or hasRole('ft')">
<input type="button" id="submitButton" value="<fmt:message key="sample.updateControlLib_submit.label" />" <c:out value="${disabled}"/> onclick='return validate()' /> 
<input type="button" value="<fmt:message key="sample.updateControlLib_reset.label" />" onclick='location.href="<wasp:relativeUrl value="/sample/createUpdateLibraryControl/${controlLibrary.getSampleId().intValue()}.do" />"' /> 
<input type="button" value="<fmt:message key="sample.updateControlLib_cancel.label" />" onclick='location.href="<wasp:relativeUrl value="/sample/listControlLibraries.do" />"' /> 
</sec:authorize>
</td></tr>
</table>
</form>

<%--
<tr class="FormData"><td class="label-centered">Control Name</td><td class="label-centered">Adaptor Set</td><td class="label-centered">Adaptor</td><td class="label-centered">Active?</td></tr>
<tr><td colspan="4" style='background-color:black'></td></tr>
<c:forEach items="${controlLibraryList}" var="controlLibrary">
	<c:set var="adaptor" value="${libraryAdaptorMap.get(controlLibrary)}" scope="page" />
	<tr>
		<td><c:out value="${controlLibrary.getName()}" /> <a href="<wasp:relativeUrl value="/sample/createUpdateLibraryControl/${controlLibrary.getSampleId()}.do" />">[edit]</a></td>
		<td><c:out value="${adaptor.getAdaptorset().getName()}"/></td>
		<td>Index <c:out value="${adaptor.getBarcodenumber()}"/>&nbsp;(<c:out value="${adaptor.getBarcodesequence()}"/>)</td>
		<c:set var="active" value='${controlLibrary.getIsActive()==1?"Active":"Inactive"}' scope="page" />
		<td><c:out value="${active}"/></td>
	</tr>

	<c:if test='${currentJobId != "-1" && currentJobId !=  job.getJobId()}'>
 		<tr><td colspan="6" style='background-color:black'></td></tr>
 	</c:if>
	<c:set var="samplesList" value="${jobAndSamplesMap.get(job)}" scope="page" />
	<c:forEach items="${samplesList}" var="sample">
		<tr class="FormData">
		<td style='text-align:center'><a href="<wasp:relativeUrl value="/sampleDnaToLibrary/listJobSamples/${job.getJobId()}.do" />">J<c:out value="${job.getJobId()}" /></a></td>          
		<td style='text-align:center'><c:out value="${job.getName()}" /></td>
		<td style='text-align:center'><c:out value="${job.getUser().getFirstName()}" /> <c:out value="${job.getUser().getLastName()}" /></td>
		<td style='text-align:center'><c:out value="${sample.getName()}" /></td>
		<td style='text-align:center'><c:out value="${sample.getSampleType().getName()}" /></td>
		<td style='text-align:center'>
			
		<form action="<wasp:relativeUrl value="/task/samplereceive/receive.do"/>" method="POST" onsubmit="return validate(this)">
 		<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="sampleId" value="${sample.getSampleId()}"> 
 		<input class="FormElement ui-widget-content ui-corner-all" type="radio" id = "receivedStatus" name = "receivedStatus" value = "RECEIVED">Received&nbsp;<input class="FormElement ui-widget-content ui-corner-all" type="radio" id = "receivedStatus" name = "receivedStatus" value = "WITHDRAWN">Withdrawn &nbsp;<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="Submit">
		</form>
				
		</td>
		</tr>
		<c:set var="currentJobId" value="${job.getJobId()}" scope="page" />	
	</c:forEach>
		
</c:forEach>
--%>