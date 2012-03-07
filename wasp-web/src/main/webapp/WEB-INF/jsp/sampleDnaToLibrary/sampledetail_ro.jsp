<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
  <br />
<h1>Sample Details</h1>
<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><td class="CaptionTD">Job ID:</td><td class="DataTD">J<c:out value="${job.jobId}" /></td></tr>
<tr class="FormData"><td class="CaptionTD">Job Name:</td><td class="DataTD"><c:out value="${job.name}" /></td></tr>
<tr class="FormData"><td class="CaptionTD">Submitter:</td><td class="DataTD"><c:out value="${job.user.firstName}" /> <c:out value="${job.user.lastName}" /></td></tr>
<tr class="FormData"><td class="CaptionTD">PI:</td><td class="DataTD"><c:out value="${job.lab.user.firstName}" /> <c:out value="${job.lab.user.lastName}" /></td></tr>
<tr class="FormData"><td class="CaptionTD">Submitted:</td><td class="DataTD"><fmt:formatDate value="${job.createts}" type="date" /></td></tr>
<tr class="FormData"><td class="CaptionTD">Workflow:</td><td class="DataTD"><c:out value="${job.workflow.name}" /></td></tr>
</table>
<br />
<table class="EditTable ui-widget ui-widget-content">
  	<tr class="FormData"><td class="CaptionTD">Sample Name:</td><td class="DataTD"><c:out value="${sample.name}" /></td></tr>
  	<tr class="FormData"><td class="CaptionTD">Sample Type:</td><td class="DataTD"><c:out value="${sample.typeSample.name}" /></td></tr>
     <c:set var="_area" value = "sample" scope="request"/>
	 <c:set var="_metaList" value = "${sample.sampleMeta}" scope="request" />		
     <c:import url="/WEB-INF/jsp/meta_ro.jsp"/>
    <tr class="FormData"><td colspan="2" class="DataTD"><a href="<c:url value="/sampleDnaToLibrary/listJobSamples/${job.jobId}.do"/>">Cancel</a>&nbsp;
	<sec:authorize access="hasRole('su') or hasRole('ft')"> 
	  <a href="<c:url value="/sampleDnaToLibrary/sampledetail_rw/${job.jobId}/${sample.sampleId}.do" />">Edit</a>
	 </sec:authorize>	
	 </td></tr>
</table>
