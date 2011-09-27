<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

  <h1><fmt:message key="auth.login_title.label" /></h1>
    <c:if test="${not empty param.error}">
      <font color="red">
        <fmt:message key="auth.login_failed.error" /><br/><br/>
        <fmt:message key="auth.login_reason.label" />: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
      </font>
    </c:if>
 
    <form name="f" action="<c:url value='/j_spring_security_check'/>" method="POST" onsubmit='return validate();'>
      <table>
        <tr><td><fmt:message key="auth.login_user.label" />:</td><td><input type='text' name='j_username' value='<c:if test="${not empty param.error}"><c:out value="${SPRING_SECURITY_LAST_EXCEPTION.authentication.principal}"/></c:if>'/></td></tr>

        <tr><td><fmt:message key="auth.login_password.label" />:</td><td><input type='password' name='j_password'/></td></tr>
        <tr><td colspan='2'><input name="submit" type="submit" value="<fmt:message key="auth.login_submit.label" />"/></td></tr>
      </table>
</form>

<div>
<a href="/wasp/auth/forgotpassword.do"><fmt:message key="auth.login_anchor_forgotpass.label" /></a> |
<a href="/wasp/auth/newuser.do"><fmt:message key="auth.login_anchor_newuser.label" /></a> |
<a href="/wasp/auth/newpi.do"><fmt:message key="auth.login_anchor_newpi.label" /></a> |
<a href="/wasp/static/about.do"><fmt:message key="auth.login_anchor_about.label" /></a>
</div>
