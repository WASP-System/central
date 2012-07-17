<%@ include file="/WEB-INF/jsp/taglib.jsp" %>




<h1><fmt:message key="jobDraft.create.label" /></h1>

<div class="instructions">
<p><fmt:message key="jobDraft.create_instructions.label"/></p>
For demonstration purposes only the Chip Seq workflow is currently enabled
</div>

<form:form  cssClass="FormGrid" commandName="jobDraft">
<table class="EditTable ui-widget ui-widget-content">
  <tr class="FormData">
    <td class="CaptionTD"><fmt:message key="jobDraft.name.label"/>:</td>
    <td class="DataTD">
      <input class="FormElement ui-widget-content ui-corner-all" name="name" value="<c:out value="${jobDraft.name}"/>">
    </td>
    <td class="CaptionTD error"><form:errors path="name" /></td>
  </tr>
  <tr class="FormData">
    <td class="CaptionTD"><fmt:message key="jobDraft.labId.label"/>:</td>
    <td class="DataTD">
      <select class="FormElement ui-widget-content ui-corner-all" name="labId">
        <option value='-1'><fmt:message key="wasp.default_select.label"/></option>
        <c:forEach var="lab" items="${labs}">
          <option value="${lab.labId}" <c:if test="${lab.labId == jobDraft.labId}"> selected</c:if>><c:out value="${lab.name}"/></option>
        </c:forEach>
      </select>
    </td>
    <td class="CaptionTD error"><form:errors path="labId" /></td>
  </tr>
  <tr class="FormData">
    <td class="CaptionTD"><fmt:message key="jobDraft.workflowId.label"/>:</td>
    <td class="DataTD">
      <c:forEach var="workflow" items="${workflows}">
        <div class="radioelement">
          <input class="FormElement ui-widget-content ui-corner-all" type="radio" name="workflowId" value="${workflow.workflowId}" <c:if test="${workflow.workflowId == jobDraft.workflowId || workflow.IName == 'chipSeq'}"> checked</c:if>  <c:if test="${workflow.IName != 'chipSeq'}"> disabled="disabled"</c:if>>
          <span><fmt:message key="${workflow.IName}.workflow.label"/></span>
        </div>
      </c:forEach>
    </td>
    <td class="CaptionTD error"><form:errors path="workflowId" /></td>
  </tr>
  </table>

  <div class="submit">
    <input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="jobDraft.submit.label"/>">
  </div>

  </form:form>



