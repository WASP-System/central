<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<table class="data">
<tr><td class="label">Job ID</td><td class="value">J<c:out value="${job.jobId}" /></td></tr>
<tr><td class="label">Job Name</td><td class="value"><c:out value="${job.name}" /></td></tr>
<tr><td class="label">Submitter</td><td class="value"><c:out value="${job.user.firstName}" /> <c:out value="${job.user.lastName}" /></td></tr>
<tr><td class="label">PI</td><td class="value"><c:out value="${job.lab.user.firstName}" /> <c:out value="${job.lab.user.lastName}" /></td></tr>
<tr><td class="label">Submitted</td><td class="value"><fmt:formatDate value="${job.createts}" type="date" /></td></tr>
<tr><td class="label">Workflow</td><td class="value"><c:out value="${job.workflow.name}" /></td></tr>
</table>
<br />
<hr />
<br />
<table class="data">
<tr><td class="label-centered">Initial Macromolecule</td><td class="label-centered">Libraries</td></tr>

<c:forEach items="${samples}" var="sample" varStatus="counter">
<tr>

<c:choose>
<c:when test='${sample.typeSample.IName=="library"}'>
	<td class="value-centered">N/A</td>
	<td class="value-centered">
		Name: <c:out value="${sample.name}" /><br />
		<c:forEach items="${sample.sampleMeta}" var="sm">
        	<c:if test="${fn:substringAfter(sm.k, 'Library.') == 'adaptorindex'}">
            	Adaptor: <c:out value="${adaptors.get(sm.v).adaptorset.name}"/><br />
            	Index <c:out value="${adaptors.get(sm.v).barcodenumber}"/>: <c:out value="${adaptors.get(sm.v).barcodesequence}"/><br />
            </c:if> 
        </c:forEach> 
		<c:choose>
			<c:when test='${received[counter.index]=="RECEIVED"}'>			
				<input type="button" value="Add To FlowCell" />
		 	 </c:when>
		 	 <c:otherwise>
		 	 	Status: <c:out value="${received[counter.index]}" />
		 	 </c:otherwise>
		</c:choose>	
	</td>
</c:when>
<c:when test='${sample.typeSample.IName=="dna" || sample.typeSample.IName=="rna"}'>
	<td class="value-centered"><c:out value="${sample.name}" /><br />
	  Name: <c:out value="${sample.typeSample.name}" /><br />
	  <c:choose>
	  	<c:when test='${received[counter.index]=="RECEIVED"}'>
	  	
	  	
	  		<form method="GET" action="<c:url value="/facility/platformunit/limitPriorToAssign.do" />">
<select name="resourceCategoryId" size="1" onchange="this.parentNode.submit()">
<option value="0">--ADAPTORS FOR NEW LIBRARY--
<c:forEach items="${adaptorsets}" var="adaptorset">


<option value="<c:out value="${adaptorset.adaptorsetId}" />" ><c:out value="${adaptorset.name}" /> 
</c:forEach>
</select>
</form><br />

	
				<input type="button" value="Create Library" />
	 	 </c:when>
	 	 <c:otherwise>
	 	 	Status: <c:out value="${received[counter.index]}" />
	 	 </c:otherwise>
	  </c:choose>					  
	</td>
	<td>lib placeholder</td>
	
</c:when>
</c:choose>
</tr>
</c:forEach>
</table>