<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head><title>New User</title><head>
  <body>
    
    <h1>New Lab</h1>
    <form:form commandName="userPending">
      <table>
        <tr>
          <td><fmt:message key="piPending.firstName.label" />:</td>
          <td><form:input path="firstName" /></td>
          <td><form:errors path="firstName" /></td>
        </tr>
        <tr>
          <td><fmt:message key="piPending.lastName.label"/>:</td>
          <td><form:input path="lastName" /></td>
          <td><form:errors path="lastName" /></td>
        </tr>
        <tr>
          <td><fmt:message key="piPending.email.label"/>:</td>
          <td><form:input path="email" /></td>
          <td><form:errors path="email" /></td>
        </tr>         
        <tr>
          <td><fmt:message key="piPending.password.label"/>:</td>
          <td><form:password path="password" /></td>
          <td><form:errors path="password" /></td>
        </tr>     	   
		<tr>
        	<td><fmt:message key="piPending.password2.label"/>:</td>
        	<td><input type="password" name="password2" /></td>
        	<td>&nbsp;</td>
        </tr>  
        <tr>
          <td><fmt:message key="piPending.locale.label"/>:</td>
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
          <c:set var="_metaArea" value = "piPending" scope="request"/>

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
