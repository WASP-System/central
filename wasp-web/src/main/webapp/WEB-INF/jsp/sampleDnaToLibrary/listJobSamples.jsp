<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
  <br />
<title><fmt:message key="pageTitle.sampleDnaToLibrary/listJobSamples.label"/></title>
<h1><fmt:message key="sampleDnaToLibrary.listJobSamples.title_label" /></h1>
<c:import url="/WEB-INF/jsp/sampleDnaToLibrary/jobdetail_for_import.jsp" />
<br />
<hr />
<br />
<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><td class="label-centered">Initial Macromolecule</td><td class="label-centered">Libraries</td></tr>

<c:forEach items="${samplesSubmitted}" var="sample" varStatus="counter">
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