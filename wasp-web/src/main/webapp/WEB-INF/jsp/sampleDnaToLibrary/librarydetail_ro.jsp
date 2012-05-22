<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
  <br />
<title><fmt:message key="pageTitle.sampleDnaToLibrary/librarydetail_ro.label"/></title>
<h1><fmt:message key="pageTitle.sampleDnaToLibrary/librarydetail_ro.label"/></h1>
<c:import url="/WEB-INF/jsp/sampleDnaToLibrary/jobdetail_for_import.jsp" />
<br /> 

<table class="EditTable ui-widget ui-widget-content">
<c:if test="${parentMacromolecule != null && parentMacromolecule.sampleId > 0}">
  	<tr class="FormData"><td class="CaptionTD"><fmt:message key="librarydetail_ro.primarySampleName.label" />: </td><td class="DataTD"><c:out value="${parentMacromolecule.name}" /></td></tr>
  	<tr class="FormData"><td class="CaptionTD"><fmt:message key="librarydetail_ro.primarySampleType.label" />: </td><td class="DataTD"><c:out value="${parentMacromolecule.sampleType.name}" /></td></tr>
	<c:forEach items="${parentMacromolecule.sampleMeta}" var="msm">
    	<c:if test="${fn:substringAfter(msm.k, 'Biomolecule.') == 'species'}">
            <tr class="FormData"><td class="CaptionTD"><fmt:message key="librarydetail_ro.primarySampleSpecies.label" />:</td><td colspan="1" class="DataTD"><c:out value="${msm.v}"/></td></tr>
        </c:if> 
    </c:forEach> 
</c:if>
   	<tr class="FormData"><td colspan="2" class="label-centered" style="font-weight:bold;text-decoration:underline"><fmt:message key="librarydetail_ro.libraryDetails.label" /></td></tr>
  
  	 <tr class="FormData"><td class="CaptionTD"><fmt:message key="librarydetail_ro.libraryName.label" />: </td><td class="DataTD"><c:out value="${sample.name}" /></td></tr>
     <tr class="FormData"><td class="CaptionTD"><fmt:message key="librarydetail_ro.librarySampleType.label" />: </td><td class="DataTD">Library</td></tr>
     <c:set var="_area" value = "library" scope="request"/>
     <c:set var="_metaList" value = "${sample.getSampleMeta()}" scope="request" />
     <c:import url="/WEB-INF/jsp/meta_ro.jsp" />
    <tr class="FormData"><td colspan="2" class="DataTD submitBottom"><a class="button" href="<c:url value="/sampleDnaToLibrary/listJobSamples/${job.jobId}.do"/>"><fmt:message key="librarydetail_ro.cancel.label" /></a>&nbsp;
	<sec:authorize access="hasRole('su') or hasRole('ft')"> 
	  <a class="button" href="<c:url value="/sampleDnaToLibrary/librarydetail_rw/${job.jobId}/${sample.sampleId}.do"/>"><fmt:message key="librarydetail_ro.edit.label" /></a>
	 </sec:authorize>	
	 </td></tr>
</table>