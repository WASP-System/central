<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

<h1><fmt:message key="pageTitle.lab/upgradeStatusToPI/form.label" /></h1>
  <div class="instructions"><fmt:message key="lab.upgradeStatusToPI_instructions.label" />
  	<div style="color:red"><fmt:message key="lab.upgradeStatusToPI_instructionsWarning.label" /></div>
  </div>
  
  <c:if test='${userIsPI=="false" && userIsPIPending == "false"}'>
   <form:form  cssClass="FormGrid" commandName="labPending">
    <table class="EditTable ui-widget ui-widget-content">
      <tr class="FormData">
        <td class="CaptionTD"><fmt:message key="labPending.name.label" />:</td>
        <td class="DataTD"><form:input cssClass="FormElement ui-widget-content ui-corner-all" path="name" /><span class="requiredField">*</span></td>
        <td class="CaptionTD error"><form:errors path="name" /></td>
      </tr>
      <tr class="FormData">
              <td class="CaptionTD"><fmt:message key="labPending.departmentId.label"/>:</td>
              <td class="DataTD">
              <select class="FormElement ui-widget-content ui-corner-all" name="departmentId">
                <option value='-1'><fmt:message key="wasp.default_select.label"/></option>
                <c:forEach var="dept" items="${departments}">
                        <option value="${dept.departmentId}" <c:if test="${dept.departmentId == labPending.departmentId}"> selected</c:if>><c:out value="${dept.name}"/></option>
                </c:forEach>
              </select><span class="requiredField">*</span>
              </td>
              <td class="CaptionTD error"><form:errors path="departmentId" /></td>
	</tr>

    <c:set var="_area" value = "labPending" scope="request"/>
    <c:set var="_metaList" value = "${labPending.labPendingMeta}" scope="request" />
    <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
    </table>
    <div class="submit">
      <input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="labPending.newLabSubmit.label"/>" />
    </div>  
  </form:form>
</c:if>

  