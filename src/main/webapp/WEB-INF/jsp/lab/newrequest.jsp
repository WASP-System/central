<%@ include file="/WEB-INF/jsp/taglib.jsp" %>


<h1><fmt:message key="labuser.request_title.label" /></h1>

<font color="blue"><wasp:message /></font>

  <form name="f" action="<c:url value='/lab/request.do'/>" method="POST">
  <table><tr>
    <td><fmt:message key="labuser.request_primaryuser.label" /></td>
    <td><input type='text' name='primaryuseremail' value=''/></td>
  </tr></table>
  <input type="submit" value="<fmt:message key="labuser.request_submit.label" />" />
  </form>

  <table>

  <sec:authorize access="not hasRole('lm-*')">
  <form:form commandName="labPending">
    <table>
      <tr>
        <td><fmt:message key="labPending.name.label" />:</td>
        <td><form:input path="name" /></td>
        <td><form:errors path="name" /></td>
      </tr>
              <td><fmt:message key="lab.departmentId.label"/>:</td>
              <td>
              <select name=departmentId>
                <option value='-1'>-- select --</option>
                <c:forEach var="dept" items="${departments}">
                        <option value="${dept.departmentId}" <c:if test="${dept.departmentId == lab.departmentId}"> selected</c:if>><c:out value="${dept.name}"/></option>
                </c:forEach>
              </select>
              </td>
              <td><form:errors path="departmentId" /></td>


    <c:set var="_area" value = "labPending" scope="request"/>
    <c:set var="_metaList" value = "${labPending.labPendingMeta}" scope="request" />
    <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
    <tr>
      <td colspan="2" align=right>
      <input type="submit" value="Save Changes" />
      </td>
    </tr>

  </table>
  </form:form>
  </sec:authorize>

