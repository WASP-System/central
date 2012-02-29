<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<wasp:message />  <br />
<h1>Sample Details</h1>
<table class="data">
<tr><td class="label">Job ID</td><td class="value">J<c:out value="${job.jobId}" /></td></tr>
<tr><td class="label">Job Name</td><td class="value"><c:out value="${job.name}" /></td></tr>
<tr><td class="label">Submitter</td><td class="value"><c:out value="${job.user.firstName}" /> <c:out value="${job.user.lastName}" /></td></tr>
<tr><td class="label">PI</td><td class="value"><c:out value="${job.lab.user.firstName}" /> <c:out value="${job.lab.user.lastName}" /></td></tr>
<tr><td class="label">Submitted</td><td class="value"><fmt:formatDate value="${job.createts}" type="date" /></td></tr>
<tr><td class="label">Workflow</td><td class="value"><c:out value="${job.workflow.name}" /></td></tr>
</table>
<br />
<table class="data">
  	<tr><td class="label">Sample Name</td><td class="value"><c:out value="${sample.name}" /></td></tr>
     <c:set var="_area" value = "sample" scope="request"/>
	 <c:set var="_metaList" value = "${sample.sampleMeta}" scope="request" />		
     <c:import url="/WEB-INF/jsp/meta_ro.jsp"/>
    <tr><td colspan="2" class="value"><a href="<c:url value="/sampleDnaToLibrary/listJobSamples/${job.jobId}.do"/>">Cancel</a>&nbsp;
	<sec:authorize access="hasRole('su') or hasRole('ft')"> 
	  <a href="<c:url value="/sampleDnaToLibrary/sampledetail_rw/${job.jobId}/${sample.sampleId}.do" />">Edit</a>
	 </sec:authorize>	
	 </td></tr>
</table>
