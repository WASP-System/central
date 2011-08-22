<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head> <title><fmt:message key="user.changepassword.title" /></title> </head>

  <body onload='document.f.oldpassword.focus();'>

    <h1><fmt:message key="user.changepassword.title" /></h1>

     <font color="red"><wasp:message /></font>

    <form name="f" action="<c:url value='/user/mypassword.do'/>" method="POST">

      <table>
        <tr><td><fmt:message key="user.changepassword.oldpassword.label" /></td><td><input type='password' name='oldpassword' value=''/></td></tr>
        <tr><td><fmt:message key="user.changepassword.newpassword1.label" /></td><td><input type='password' name='newpassword1' value=''/></td></tr>
        <tr><td><fmt:message key="user.changepassword.newpassword2.label" /></td><td><input type='password' name='newpassword2' value=''/></td></tr>
        <tr><td colspan='2'><input name="submit" type="submit" value="<fmt:message key="user.changepassword.submit" />"/></td></tr>
      </table>
    </form>

</body></html>