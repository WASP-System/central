<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
  <br />
<h1>Sample Details</h1>
<c:import url="/WEB-INF/jsp/sampleDnaToLibrary/jobdetail_for_import.jsp" />
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
