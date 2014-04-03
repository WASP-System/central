<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
  <br />
<title><fmt:message key="pageTitle.sampleDnaToLibrary/librarydetail_ro.label"/></title>
<h1><fmt:message key="pageTitle.sampleDnaToLibrary/librarydetail_ro.label"/></h1>
<div style="width=100%; overflow:hidden">
	<div style="float:left">
		<c:import url="/WEB-INF/jsp/sampleDnaToLibrary/jobdetail_for_import.jsp" />
	</div>
	<div style="padding-left:0.5cm; overflow:hidden">
	
		<table class="EditTable ui-widget ui-widget-content">
		   	<tr class="FormData"><td colspan="2" class="label-centered" style="font-weight:bold;text-decoration:underline"><fmt:message key="librarydetail_ro.libraryDetails.label" /></td></tr>
		  	 <tr class="FormData"><td class="CaptionTD"><fmt:message key="librarydetail_ro.libraryName.label" />: </td><td class="DataTD"><c:out value="${sample.name}" /></td></tr>
		     <tr class="FormData"><td class="CaptionTD"><fmt:message key="librarydetail_ro.librarySampleType.label" />: </td><td class="DataTD">Library</td></tr>
		     <c:set var="_area" value = "library" scope="request"/>
		     <c:set var="_metaList" value = "${sample.getSampleMeta()}" scope="request" />
		     <c:import url="/WEB-INF/jsp/meta_ro.jsp" />
		    <tr class="FormData"><td colspan="2" class="submitBottom"><a class="button" href="<wasp:relativeUrl value="/sampleDnaToLibrary/listJobSamples/${job.jobId}.do"/>"><fmt:message key="librarydetail_ro.cancel.label" /></a>&nbsp;
			<sec:authorize access="hasRole('su') or hasRole('ft')"> 
			  <a class="button" href="<wasp:relativeUrl value="/sampleDnaToLibrary/librarydetail_rw/${job.jobId}/${sample.sampleId}.do"/>"><fmt:message key="librarydetail_ro.edit.label" /></a>
			 </sec:authorize>	
			 </td></tr>
		</table>
	</div>
</div>