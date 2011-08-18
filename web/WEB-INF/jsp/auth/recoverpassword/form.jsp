<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head> <title><fmt:message key="auth.recoverpassword.title" /></title> </head>

  <body onload='document.f.j_username.focus();'>

    <h1><fmt:message key="auth.recoverpassword.title" /></h1>

     <font color="red"><wasp:message /></font>

    <form name="f" action="<c:url value='/auth/recoverpassword.do'/>" method="POST">
       <input type='hidden' name='authcode' value='<c:out value="${authcode}" />'/>

      <table>
        <tr><td><fmt:message key="auth.recoverpassword.user.label" /></td><td><input type='text' name='j_username' value=''/></td></tr>
        <tr><td><fmt:message key="auth.recoverpassword.password.label" /></td><td><input type='password' name='password1' value=''/></td></tr>
        <tr><td><fmt:message key="auth.recoverpassword.password2.label" /></td><td><input type='password' name='password2' value=''/></td></tr>
        <tr><td colspan='2'><input name="submit" type="submit" value="<fmt:message key="auth.recoverpassword.submit" />"/></td></tr>
      </table>
    </form>

</body></html>

