<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br />

<h1><fmt:message key="jobDraft.create.label"/> -- <c:out value="${heading}"/></h1>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>

<form:form cssClass="FormGrid" commandName="sampleDraft">
<form:hidden path='sampleSubtypeId' />
<form:hidden path='sampleTypeId' />
<table class="EditTable ui-widget ui-widget-content">
  	 <tr class="FormData">
      <td class="CaptionTD"><fmt:message key="jobDraft.sample_name.label"/>:</td>
      <td class="DataTD"><form:input cssClass="FormElement ui-widget-content ui-corner-all" path="name" /><span class="requiredField">*</span></td>
      <td class="CaptionTD error"><form:errors path="name" /></td>
     </tr>
     <tr class="FormData"><td class="CaptionTD"><fmt:message key="jobDraft.sample_type.label"/>:</td><td class="DataTD"><c:out value="${sampleDraft.sampleType.name}" /></td><td class="CaptionTD error"><form:errors path="" /></td></tr>
     <tr class="FormData"><td class="CaptionTD"><fmt:message key="jobDraft.sample_subtype.label"/>:</td><td class="DataTD"><c:out value="${sampleDraft.sampleSubtype.name}" /></td><td class="CaptionTD error"><form:errors path="" /></td></tr>
     <c:set var="_area" value = "sampleDraft" scope="request"/>
	 <c:set var="_metaList" value = "${normalizedMeta}" scope="request" />		
     <c:import url="/WEB-INF/jsp/meta_rw.jsp" />
     <tr class="FormData">
        <td colspan="3" align="left" class="submitBottom">
        	<input type="submit" name="submit" value="<fmt:message key="jobDraft.cancel.label"/>" />
            <input type="submit" name="submit" value="<fmt:message key="jobDraft.save.label"/>" />
        </td>
    </tr>
</table>
</form:form>

