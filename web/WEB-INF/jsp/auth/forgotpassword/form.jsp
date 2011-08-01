<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head> <title>Forgot Password</title> </head>

  <body onload='document.f.j_username.focus();'>

    <h1>Forgot Password</h1>

    <form name="f" action="<c:url value='/auth/forgotpassword.do'/>" method="POST">
      <table>
        <tr><td>User:</td><td><input type='text' name='j_username' value=''/></td></tr>
        <tr><td colspan='2'><input name="submit" type="submit" value="Login"/></td></tr>
      </table>
    </form>

</body></html>
 
