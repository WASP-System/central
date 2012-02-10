<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<p><wasp:message /></p>


<h1><fmt:message key="pageTitle.lab/newrequest.label" /></h1>
  <div class="instructions"><fmt:message key="labuser.labUserNote.label" /></div>
  <form name="f" action="<c:url value='/lab/request.do'/>" method="POST">
  <table class="data"><tr>
    <td class="label"><fmt:message key="labuser.request_primaryuser.label" />: </td>
    <td class="input"><input type='text' name='primaryUserLogin' value=''/></td>
  </tr></table>
  <div class="submit">
    <input type="submit" value="<fmt:message key="labuser.request_submit.label" />" />
  </div>
  </form>


  <h1><fmt:message key="labPending.createNewLab.label" /></h1>
  <div class="instructions"><fmt:message key="labPending.newPiNote.label" /></div>
  <form:form commandName="labPending">
    <table class="data">
      <tr>
        <td class="label"><fmt:message key="labPending.name.label" />:</td>
        <td class="input"><form:input path="name" /></td>
        <td class="error"><form:errors path="name" /></td>
      </tr>
      <tr>
              <td class="label"><fmt:message key="labPending.departmentId.label"/>:</td>
              <td class="input">
              <select name="departmentId">
                <option value='-1'><fmt:message key="wasp.default_select.label"/></option>
                <c:forEach var="dept" items="${departments}">
                        <option value="${dept.departmentId}" <c:if test="${dept.departmentId == labPending.departmentId}"> selected</c:if>><c:out value="${dept.name}"/></option>
                </c:forEach>
              </select>
              </td>
              <td class="error"><form:errors path="departmentId" /></td>
	</tr>

    <c:set var="_area" value = "labPending" scope="request"/>
    <c:set var="_metaList" value = "${labPending.labPendingMeta}" scope="request" />
    <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
    </table>

    <div class="submit">
      <input type="submit" value="<fmt:message key="labPending.newLabSubmit.label"/>" />
    </div>

  </table>
  </form:form>


