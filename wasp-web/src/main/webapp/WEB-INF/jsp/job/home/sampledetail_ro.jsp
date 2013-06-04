<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<a class="button" href="javascript:void(0);"  onclick='parent.loadNewPage(this, "<c:url value="/job/${job.getId()}/samples.do" />");' >Back To: Samples, Libraries &amp; Runs</a>
<br />
<br />
<c:choose>
	<c:when test="${fn:length(errorMessage)>0}">
		<h2 style="color:red;font-weight:bold"><c:out value="${errorMessage}" /></h2>
	</c:when>
<c:otherwise>
	<table class="EditTable ui-widget ui-widget-content">
	  	<tr class="FormData"><td class="CaptionTD"><fmt:message key="sampledetail_ro.sampleName.label" />:</td><td class="DataTD"><c:out value="${sample.name}" /></td></tr>
	  	<tr class="FormData"><td class="CaptionTD"><fmt:message key="sampledetail_ro.sampleType.label" />:</td><td class="DataTD"><c:out value="${sample.sampleType.name}" /></td></tr>
	     <c:set var="_area" value = "sample" scope="request"/>
		 <c:set var="_metaList" value = "${normalizedSampleMeta}" scope="request" />		
	     <c:import url="/WEB-INF/jsp/meta_ro.jsp"/>
	    <tr class="FormData"><td colspan="2" class="submitBottom"><a class="button" href="javascript:void(0);" onclick='parent.loadNewPage(this, "<c:url value="/job/${job.getId()}/samples.do" />");' ><fmt:message key="sampledetail_ro.cancel.label" /></a>&nbsp;
			<sec:authorize access="hasRole('su') or hasRole('ft')"> 
		  		<a class="button" href="javascript:void(0);" onclick='parent.loadIFrameAnotherWay(this, "<c:url value="/job/${job.getId()}/sample/${sample.getId()}/sampledetail_rw.do" />");' ><fmt:message key="sampledetail_ro.edit.label" /></a>
		 	</sec:authorize>	
		</td></tr>
	</table>
</c:otherwise>
</c:choose>
<br />

