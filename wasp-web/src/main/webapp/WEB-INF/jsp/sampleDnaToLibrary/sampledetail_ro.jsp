<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
  <br />
<title><fmt:message key="pageTitle.sampleDnaToLibrary/sampledetail_ro.label"/></title>
<h1><fmt:message key="pageTitle.sampleDnaToLibrary/sampledetail_ro.label"/></h1>
<div style="width=100%; overflow:hidden">
	<div style="float:left">
		<c:import url="/WEB-INF/jsp/sampleDnaToLibrary/jobdetail_for_import.jsp" />
	</div>
	<div style="padding-left:0.5cm; overflow:hidden">
		<table class="EditTable ui-widget ui-widget-content">
		  	<tr class="FormData"><td class="CaptionTD"><fmt:message key="sampledetail_ro.sampleName.label" />:</td><td class="DataTD"><c:out value="${sample.name}" /></td></tr>
		  	<tr class="FormData"><td class="CaptionTD"><fmt:message key="sampledetail_ro.sampleType.label" />:</td><td class="DataTD"><c:out value="${sample.sampleType.name}" /></td></tr>
		     <c:set var="_area" value = "sample" scope="request"/>
			 <c:set var="_metaList" value = "${normalizedSampleMeta}" scope="request" />		
		     <c:import url="/WEB-INF/jsp/meta_ro.jsp"/>
		    <tr class="FormData"><td colspan="2" class="submitBottom"><a class="button" href="<wasp:relativeUrl value="sampleDnaToLibrary/listJobSamples/${job.jobId}.do"/>"><fmt:message key="sampledetail_ro.cancel.label" /></a>&nbsp;
			<sec:authorize access="hasRole('su') or hasRole('ft')"> 
			  <a class="button" href="<wasp:relativeUrl value="sampleDnaToLibrary/sampledetail_rw/${job.jobId}/${sample.sampleId}.do" />"><fmt:message key="sampledetail_ro.edit.label" /></a>
			 </sec:authorize>	
			 </td></tr>
		</table>
	</div>
</div>