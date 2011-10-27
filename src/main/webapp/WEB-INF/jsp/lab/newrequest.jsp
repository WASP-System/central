<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<p><font color="red"><wasp:message /></font></p>
<h1><fmt:message key="pageTitle.lab/newrequest.label" /></h1>
  <p><fmt:message key="labuser.labUserNote.label" /></p>
  <form name="f" action="<c:url value='/lab/request.do'/>" method="POST">
  <table><tr>
    <td><fmt:message key="labuser.request_primaryuser.label" />: </td>
    <td><input type='text' name='primaryUserLogin' value=''/></td>
  </tr></table>
  <input type="submit" value="<fmt:message key="labuser.request_submit.label" />" />
  </form>

  <table>

  <h1><fmt:message key="labPending.createNewLab.label" /></h1>
  <p><fmt:message key="labPending.newPiNote.label" /></p>
  <form:form commandName="labPending">
    <table>
      <tr>
        <td><fmt:message key="labPending.name.label" />:</td>
        <td><form:input path="name" /></td>
        <td><form:errors path="name" /></td>
      </tr>
      <tr>
              <td><fmt:message key="labPending.departmentId.label"/>:</td>
              <td>
              <select name="departmentId">
                <option value='-1'><fmt:message key="wasp.default_select.label"/></option>
                <c:forEach var="dept" items="${departments}">
                        <option value="${dept.departmentId}" <c:if test="${dept.departmentId == labPending.departmentId}"> selected</c:if>><c:out value="${dept.name}"/></option>
                </c:forEach>
              </select>
              </td>
              <td><form:errors path="departmentId" /></td>
	</tr>

    <c:set var="_area" value = "labPending" scope="request"/>
    <c:set var="_metaList" value = "${labPending.labPendingMeta}" scope="request" />
    <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
    <tr>
      <td colspan="2" align=right>
      <input type="submit" value="<fmt:message key="labPending.newLabSubmit.label"/>" />
      </td>
    </tr>

  </table>
  </form:form>


