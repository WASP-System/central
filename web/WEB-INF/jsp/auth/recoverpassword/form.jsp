<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head> <title>Recover Password</title> </head>

  <body onload='document.f.j_username.focus();'>

    <h1>Recover Password</h1>

    <form name="f" action="<c:url value='/auth/recoverpassword.do'/>" method="POST">
      <table>
        <tr><td>User:</td><td><input type='text' name='j_username' value=''/></td></tr>
        <tr><td>Authcode:</td><td><input type='text' name='authcode' value=''/></td></tr>
        <tr><td>Password1:</td><td><input type='password' name='password1' value=''/></td></tr>
        <tr><td>Password2:</td><td><input type='password' name='password2' value=''/></td></tr>
        <tr><td colspan='2'><input name="submit" type="submit" value="Login"/></td></tr>
      </table>
    </form>

</body></html>

