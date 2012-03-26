<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<wasp:message />  <br />
<h1>Library Details</h1>
<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><td class="CaptionTD">Job ID:</td><td class="DataTD">J<c:out value="${job.jobId}" /></td></tr>
<tr class="FormData"><td class="CaptionTD">Job Name:</td><td class="DataTD"><c:out value="${job.name}" /></td></tr>
<tr class="FormData"><td class="CaptionTD">Submitter:</td><td class="DataTD"><c:out value="${job.user.firstName}" /> <c:out value="${job.user.lastName}" /></td></tr>
<tr class="FormData"><td class="CaptionTD">PI:</td><td class="DataTD"><c:out value="${job.lab.user.firstName}" /> <c:out value="${job.lab.user.lastName}" /></td></tr>
<tr class="FormData"><td class="CaptionTD">Submitted:</td><td class="DataTD"><fmt:formatDate value="${job.createts}" type="date" /></td></tr>
<tr class="FormData"><td class="CaptionTD">Workflow:</td><td class="DataTD"><c:out value="${job.workflow.name}" /></td></tr>
<c:forEach items='${extraJobDetailsMap}' var="detail">
	<tr class="FormData"><td class="CaptionTD">  <c:out value='${detail.key}' />   </td><td class="DataTD"> <c:out value='${detail.value}' /> </td></tr>
</c:forEach>
</table>
<br /> 


<table class="EditTable ui-widget ui-widget-content">
<c:if test="${parentMacromolecule != null && parentMacromolecule.sampleId > 0}">
  	<tr class="FormData"><td class="CaptionTD">Primary Sample Name: </td><td class="DataTD"><c:out value="${parentMacromolecule.name}" /></td></tr>
  	<tr class="FormData"><td class="CaptionTD">Primary Sample Type: </td><td class="DataTD"><c:out value="${parentMacromolecule.sampleType.name}" /></td></tr>
    <c:forEach items="${parentMacromolecule.sampleMeta}" var="msm">
    	<c:if test="${fn:substringAfter(msm.k, 'Biomolecule.') == 'species'}">
            <tr class="FormData"><td class="CaptionTD">Primary Sample Species: </td><td class="DataTD"><c:out value="${msm.v}"/></td></tr>
        </c:if> 
    </c:forEach> 
</c:if>
   	<tr class="FormData"><td colspan="2" class="label-centered">LIBRARY DETAILS</td></tr>
  
  	 <tr class="FormData"><td class="CaptionTD">Library Name: </td><td class="DataTD"><c:out value="${sample.name}" /></td></tr>
     <tr class="FormData"><td class="CaptionTD">Sample Type: </td><td class="DataTD">Library</td></tr>
     <c:set var="_area" value = "library" scope="request"/>
     <c:set var="_metaList" value = "${normalizedSampleMeta}" scope="request" />
     <c:import url="/WEB-INF/jsp/meta_ro.jsp" />
    <tr class="FormData"><td colspan="2" class="DataTD"><a href="<c:url value="/sampleDnaToLibrary/listJobSamples/${job.jobId}.do"/>">Cancel</a>&nbsp;
	<sec:authorize access="hasRole('su') or hasRole('ft')"> 
	  <a href="<c:url value="/sampleDnaToLibrary/librarydetail_rw/${job.jobId}/${sample.sampleId}.do"/>">Edit</a>
	 </sec:authorize>	
	 </td></tr>
</table>