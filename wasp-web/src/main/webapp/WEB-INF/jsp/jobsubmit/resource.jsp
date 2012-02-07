<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<font color="blue"><wasp:message /></font>

<h1><fmt:message key="jobDraft.create.label"/> -- <c:out value="${workflowResourceCategories.get(0).getResourceCategory().getTypeResource().getName()}" /></h1>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>

<div class="instructions">
   <fmt:message key="jobDraft.resource_instructions.label"/>
</div>

<form method="POST" class="resourcelistform">

<select name="changeResource" onchange="this.parentNode.submit()">
  <option value="-1"><fmt:message key="wasp.default_select.label"/></option>
<c:forEach items="${workflowResourceCategories}" var="w">
  <option value="<c:out value="${w.resourceCategory.resourceCategoryId}" />" <c:if test="${w.resourceCategory.resourceCategoryId == jobDraftResourceCategory.resourceCategory.resourceCategoryId}"> SELECTED</c:if>>
    <c:out value="${w.resourceCategory.name}" />
  </option>
</c:forEach>
</select>
</form>

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
  <input type="submit" value="<fmt:message key="jobDraft.submit.label" />" />
</div>

</form:form>


