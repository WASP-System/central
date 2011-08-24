<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
  <head> <title>Login Page</title> </head>
  <body onload='document.f.j_username.focus();'>
    <c:if test="${not empty param.error}">
      <font color="red">
        Your login attempt was not successful, try again.<br/><br/>
        Reason: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
      </font>
    </c:if>
 
    <form name="f" action="<c:url value='/j_spring_security_check'/>" method="POST">
      <table>
        <tr><td>User:</td><td><input type='text' name='j_username' value='<c:if test="${not empty param.error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>'/></td></tr>

        <tr><td>Password:</td><td><input type='password' name='j_password'/></td></tr>
        <tr><td colspan='2'><input name="submit" type="submit" value="Login"/></td></tr>
      </table>
</form>

<div>
<a href="/wasp/auth/forgotpassword.do">Forgot Password</a> |
<a href="/wasp/auth/newuser.do">New User</a> |
<a href="/wasp/auth/newpi.do">New PI</a> |
<a href="/wasp/static/about.do">About</a>
</div>

</body></html>
