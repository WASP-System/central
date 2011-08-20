<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head><title>New User</title><head>
  <body>
    
    <h1>New User</h1>
    <form:form commandName="userPending">
      <table>
        <tr>
          <td><fmt:message key="userPending.firstName.label" />:</td>
          <td><form:input path="firstName" /></td>
          <td><form:errors path="firstName" /></td>
        </tr>
        <tr>
          <td><fmt:message key="userPending.lastName.label"/>:</td>
          <td><form:input path="lastName" /></td>
          <td><form:errors path="lastName" /></td>
        </tr>
        <tr>
          <td><fmt:message key="userPending.email.label"/>:</td>
          <td><form:input path="email" /></td>
          <td><form:errors path="email" /></td>
        </tr>         
        <tr>
          <td><fmt:message key="userPending.password.label"/>:</td>
          <td><form:input path="password" /></td>
          <td><form:errors path="password" /></td>
        </tr>     	   

        <tr>
          <td><fmt:message key="userPending.locale.label"/>:</td>
          <td>
            <select name=locale>
              <option value=''>-- select --</option>
              <c:forEach var="localeEntry" items="${locales}">
                <c:set var="localeValue" value="${localeEntry.key}"/>
                <c:set var="localeLabel" value="${localeEntry.value}"/>     
                <option value=${localeValue} <c:if test="${userPending.locale == localeValue}">selected</c:if>>${localeLabel}</option>
              </c:forEach>
            </select>
          </td>
        </tr>

          <c:set var="_area" value = "userPending" scope="request"/>

          <c:set var="_metaList" value = "${userPending.userPendingMeta}" scope="request" />
          <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
          <tr>
              <td colspan="2" align=right>
                  <input type="submit" value="Save Changes" />
              </td>
          </tr>

       </table>
    </form:form>
  
  </body>
</html>
