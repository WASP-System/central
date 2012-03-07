<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
  <br />
<title><fmt:message key="pageTitle.sampleDnaToLibrary/listJobSamples.label"/></title>
<h1><fmt:message key="sampleDnaToLibrary.listJobSamples.title_label" /></h1>

<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><td class="CaptionTD">Job ID:</td><td class="DataTD">J<c:out value="${job.jobId}" /></td></tr>
<tr class="FormData"><td class="CaptionTD">Job Name:</td><td class="DataTD"><c:out value="${job.name}" /></td></tr>
<tr class="FormData"><td class="CaptionTD">Submitter:</td><td class="DataTD"><c:out value="${job.user.firstName}" /> <c:out value="${job.user.lastName}" /></td></tr>
<tr class="FormData"><td class="CaptionTD">PI:</td><td class="DataTD"><c:out value="${job.lab.user.firstName}" /> <c:out value="${job.lab.user.lastName}" /></td></tr>
<tr class="FormData"><td class="CaptionTD">Submitted:</td><td class="DataTD"><fmt:formatDate value="${job.createts}" type="date" /></td></tr>
<tr class="FormData"><td class="CaptionTD">Workflow:</td><td class="DataTD"><c:out value="${job.workflow.name}" /></td></tr>
<tr><td class="label">Resource</td><td class="value"><c:out value='${extraJobDetailsMap["resource"]}' /><br />Read Length: <c:out value='${extraJobDetailsMap["readLength"]}' /><br />Read Type: <c:out value='${extraJobDetailsMap["readType"]}' /></td></tr>

</table>
<br />
<hr />
<br />
<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><td class="label-centered">Initial Macromolecule</td><td class="label-centered">Libraries</td></tr>

<c:forEach items="${samples}" var="sample" varStatus="counter">
<tr class="FormData">

<c:choose>
<c:when test='${sample.typeSample.IName=="library"}'>
	<td class="value-centered" >N/A <br />DEBUG: Libraries/sample: <c:out value="${librariespersample[counter.index]}"/></td>
	<td class="value-centered">
		Name: <a href="<c:url value="/sampleDnaToLibrary/librarydetail_ro/${job.jobId}/${sample.sampleId}.do" />"><c:out value="${sample.name}" /></a><br />
		<c:forEach items="${sample.sampleMeta}" var="sm">
        	<c:if test="${fn:substringAfter(sm.k, 'Library.') == 'adaptorindex'}">
            	Adaptor: <c:out value="${adaptors.get(sm.v).adaptorset.name}"/><br />
            	Index <c:out value="${adaptors.get(sm.v).barcodenumber}"/>: <c:out value="${adaptors.get(sm.v).barcodesequence}"/><br />
            </c:if> 
        </c:forEach> 
		<c:choose>
			<c:when test='${received[counter.index]=="RECEIVED"}'>			
				<input class="FormElement ui-widget-content ui-corner-all" type="button" value="Add To FlowCell" />
		 	 </c:when>
		 	 <c:otherwise>
		 	 	Status: <c:out value="${received[counter.index]}" />
		 	 </c:otherwise>
		</c:choose>	
	</td>
</c:when>
<c:when test='${sample.typeSample.IName=="dna" || sample.typeSample.IName=="rna"}'>
	<td class="value-centered">Name: <a href="<c:url value="/sampleDnaToLibrary/sampledetail_ro/${job.jobId}/${sample.sampleId}.do" />"><c:out value="${sample.name}" /></a><br />DEBUG: Libraries/sample: <c:out value="${librariespersample[counter.index]}"/><br />
	  Molecule: <c:out value="${sample.typeSample.name}" /><br /> 
	  <c:forEach items="${sample.sampleMeta}" var="sm">
        	<c:if test="${fn:substringAfter(sm.k, 'Biomolecule.') == 'species'}">
            	Species: <c:out value="${sm.v}"/><br />
            </c:if> 
        </c:forEach> 
	  
	  
	  <br />
	  <c:choose>
	  	<c:when test='${received[counter.index]=="RECEIVED"}'>
	  	
	  	
	  		<form method="GET" action="<c:url value="/sampleDnaToLibrary/createLibraryFromMacro.do" />">
	  		<input class="FormElement ui-widget-content ui-corner-all" type='hidden' name='jobId' value='<c:out value="${job.jobId}" />'/>
	  		<input class="FormElement ui-widget-content ui-corner-all" type='hidden' name='macromolSampleId' value='<c:out value="${sample.sampleId}" />'/>
	  		
				<select class="FormElement ui-widget-content ui-corner-all" name="adaptorsetId" size="1" onchange="if(this.options[selectedIndex].value != 0){this.parentNode.submit();}">
				<option value="0">--ADAPTORS FOR NEW LIBRARY--
				<c:forEach items="${adaptorsets}" var="adaptorset">
					<option value="<c:out value="${adaptorset.adaptorsetId}" />" ><c:out value="${adaptorset.name}" /> 
				</c:forEach>
				</select>
			</form><br />
	 	 </c:when>
	 	 <c:otherwise>
	 	 	Status: <c:out value="${received[counter.index]}" />
	 	 </c:otherwise>
	  </c:choose>					  
	</td>
	<td class="value-centered">
		<c:set var="i" value="0" scope="page" /> 
		<c:forEach items="${sample.sampleSourceViaSourceSampleId}" var="samplesource">
			<c:set var="lib" value="${samplesource.sample}"/> 
				<c:if test="${i > 0}">
					<hr />
				</c:if>
				<c:set var="i" value="${i + 1}" scope="page" />		
				Name: <a href="<c:url value="/sampleDnaToLibrary/librarydetail_ro/${job.jobId}/${lib.sampleId}.do" />"><c:out value="${lib.name}" /></a><br />
				<c:forEach items="${lib.sampleMeta}" var="sm">
        			<c:if test="${fn:substringAfter(sm.k, 'Library.') == 'adaptorindex'}">
            			Adaptor: <c:out value="${adaptors.get(sm.v).adaptorset.name}"/><br />
            			Index <c:out value="${adaptors.get(sm.v).barcodenumber}"/>: <c:out value="${adaptors.get(sm.v).barcodesequence}"/><br />
            		</c:if> 
		        </c:forEach> 
		
		</c:forEach>
	</td>
	
</c:when>
</c:choose>
</tr>
</c:forEach>
</table>