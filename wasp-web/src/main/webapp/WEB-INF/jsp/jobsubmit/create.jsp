<%@ include file="/WEB-INF/jsp/taglib.jsp" %>


<font color="red"><wasp:message /></font>

<h1><fmt:message key="jobDraft.create.label" /></h1>

<div class="instructions">
<fmt:message key="jobDraft.create_instructions.label"/>
</div>

<form:form commandName="jobDraft">
<table class="data">
  <tr>
    <td class="label"><fmt:message key="jobDraft.name.label"/>:</td>
    <td class="input">
      <input name="name" value="<c:out value="${jobDraft.name}"/>">
    </td>
    <td class="error"><form:errors path="name" /></td>
  </tr>
  <tr>
    <td class="label"><fmt:message key="jobDraft.labId.label"/>:</td>
    <td class="input">
      <select name="labId">
        <option value='-1'><fmt:message key="wasp.default_select.label"/></option>
        <c:forEach var="lab" items="${labs}">
          <option value="${lab.labId}" <c:if test="${lab.labId == jobDraft.labId}"> selected</c:if>><c:out value="${lab.name}"/></option>
        </c:forEach>
      </select>
    </td>
    <td class="error"><form:errors path="labId" /></td>
  </tr>
  <tr>
    <td class="label"><fmt:message key="jobDraft.workflowId.label"/>:</td>
    <td class="input">
      <c:forEach var="workflow" items="${workflows}">
        <div class="radioelement">
          <input type="radio" name="workflowId" value="${workflow.workflowId}" <c:if test="${workflow.workflowId == jobDraft.workflowId}"> checked</c:if> >
          <span><fmt:message key="${workflow.IName}.workflow.label"/></span>
        </div>
      </c:forEach>
    </td>
    <td class="error"><form:errors path="workflowId" /></td>
  </tr>
  </table>

  <div class="submit">
    <input type="submit" value="<fmt:message key="jobDraft.submit.label"/>">
  </div>

  </form:form>



