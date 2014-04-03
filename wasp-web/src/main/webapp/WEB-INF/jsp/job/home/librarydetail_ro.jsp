<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<c:import url="/WEB-INF/jsp/job/home/fadingMessage.jsp" />
<br />
<a class="button" href="javascript:void(0);"  onclick='loadNewPageWithAjax("<wasp:relativeUrl value="/job/${job.getId()}/samples.do" />");' >Back To: Samples, Libraries &amp; Runs</a>
<br /><br /><br />
<table class="EditTable ui-widget ui-widget-content">
   	<tr class="FormData"><td colspan="2" class="label-centered" style="font-weight:bold;text-decoration:underline"><fmt:message key="librarydetail_ro.libraryDetails.label" /></td></tr>
  	 <tr class="FormData"><td class="CaptionTD"><fmt:message key="librarydetail_ro.libraryName.label" />: </td><td class="DataTD"><c:out value="${sample.name}" /></td></tr>
     <tr class="FormData"><td class="CaptionTD"><fmt:message key="librarydetail_ro.librarySampleType.label" />: </td><td class="DataTD">Library</td></tr>
     <c:set var="_area" value = "library" scope="request"/>
     <c:set var="_metaList" value = "${sample.getSampleMeta()}" scope="request" />
     <c:import url="/WEB-INF/jsp/meta_ro.jsp" />
    <tr class="FormData"><td colspan="2" class="submitBottom"><a class="button" href="javascript:void(0);" onclick='loadNewPageWithAjax("<wasp:relativeUrl value="/job/${job.getId()}/samples.do" />");' ><fmt:message key="librarydetail_ro.cancel.label" /></a>&nbsp;
	<sec:authorize access="hasRole('su') or hasRole('ft')"> 
	  <a class="button" href="javascript:void(0);" onclick='loadNewPageWithAjax("<wasp:relativeUrl value="/job/${job.getId()}/library/${sample.getId()}/librarydetail_rw.do" />");' ><fmt:message key="librarydetail_ro.edit.label" /></a>
	 </sec:authorize>	
	 </td></tr>
</table>
<br />