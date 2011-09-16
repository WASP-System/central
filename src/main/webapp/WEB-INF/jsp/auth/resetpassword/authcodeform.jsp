<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head> <title><fmt:message key="auth.resetpassword_title1.label" /></title> </head>

  <body>

    <h1><fmt:message key="auth.resetpassword_title1.label" /></h1>
    
    <font color="red"><wasp:message /></font>

    <form name="f" action="<c:url value='/auth/resetpassword.do'/>" method="GET">
      <table>
        <tr><td><fmt:message key="auth.resetpassword_authcode.label" />:</td><td><input type='text' name='authcode' value=''/></td></tr>
        <tr><td colspan='2'><input name="submit" type="submit" value="<fmt:message key="auth.resetpassword_submit.label" />"/></td></tr>
      </table>
    </form>

</body></html>

