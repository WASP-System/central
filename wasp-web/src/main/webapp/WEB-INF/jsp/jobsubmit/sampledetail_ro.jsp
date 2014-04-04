<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br />

<h1><fmt:message key="jobDraft.create.label"/> -- <fmt:message key="jobDraft.sample_ro_heading.label"/></h1>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>

<table class="EditTable ui-widget ui-widget-content">
  	<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobDraft.sample_name.label"/>:</td><td class="DataTD"><c:out value="${sampleDraft.name}" /></td></tr>
  	<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobDraft.sample_type.label"/>:</td><td class="DataTD"><c:out value="${sampleDraft.sampleType.name}" /></td></tr>
  	<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobDraft.sample_subtype.label"/>:</td><td class="DataTD"><c:out value="${sampleDraft.sampleSubtype.name}" /></td></tr>
    <c:set var="_area" value = "sampleDraft" scope="request"/>
	<c:set var="_metaList" value = "${normalizedMeta}" scope="request" />		
    <c:import url="/WEB-INF/jsp/meta_ro.jsp"/>
    <tr class="FormData">
    	<td colspan="2" class="submitBottom">
    		<a class="FormElement button" href="<wasp:relativeUrl value="jobsubmit/samples/${jobDraft.jobDraftId}.do"/>"><fmt:message key="jobDraft.cancel.label"/></a>&nbsp;
			<a class="FormElement button" href="<wasp:relativeUrl value="jobsubmit/samples/edit/${jobDraft.jobDraftId}/${sampleDraft.sampleDraftId}.do" />"><fmt:message key="jobDraft.edit.label"/></a>
		</td>
	</tr>
</table>
