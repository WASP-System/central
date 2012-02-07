<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<font color="red"><wasp:message /></font>

<h1><fmt:message key="jobDraft.create.label" /> -- <fmt:message key="${jobDraft.getWorkflow().getIName()}.jobsubmit/modifymeta.label" /></h1>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>

<div class="instructions">
   <fmt:message key="jobDraft.meta_instructions.label"/>
</div>


<form:form command="jobDraft">
<input type="hidden" name="name" value="<c:out value="${jobDraft.name}"/>">
<input type="hidden" name="workflowId" value="<c:out value="${jobDraft.workflowId}"/>">
<input type="hidden" name="labId" value="<c:out value="${jobDraft.labId}"/>">

<c:set var="_area" value = "${parentarea}" scope="request"/>
<c:set var="_metaArea" value = "${area}" scope="request"/>
<c:set var="_metaList" value = "${jobDraft.jobDraftMeta}" scope="request" />
<table class="data">
  <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
</table>

<div class="submit">
  <input type="submit" value="<fmt:message key="jobdraft.submit.label" />" />
</div>

</form:form>


