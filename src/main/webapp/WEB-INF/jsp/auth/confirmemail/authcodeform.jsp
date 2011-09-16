<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head> <title><fmt:message key="auth.confirmemail_title.label" /></title> </head>

  <body>

    <h1><fmt:message key="auth.confirmemail_title.label" /></h1>
     <font color="red"><wasp:message /></font>

    <form name="f" action="<c:url value='/auth/confirmemail.do'/>" method="POST">
       <input type='hidden' name='authcode' value='<c:out value="${authcode}" default="" />'/>

      <table>
      	<tr><td><fmt:message key="auth.confirmemail_email.label" /></td><td><input type='text' name='email' value='<c:out value="${email}" default="" />'/></td></tr>
        <tr><td><fmt:message key="auth.confirmemail_authcode.label" /></td><td><input type='text' name='authcode' value='<c:out value="${authcode}" default="" />'/></td></tr>
        <tr><td>&nbsp;</td><td><img src="<c:url value='/stickyCaptchaImg.png'/>" /></td></tr>
        <tr><td><fmt:message key="auth.confirmemail_captcha.label" /></td><td><input type='text' name='captcha_text' value=''/></td></tr>
        <tr><td colspan='2'><input name="submit" type="submit" value="<fmt:message key="auth.confirmemail_submit.label" />"/></td></tr>
      </table>
    </form>

</body></html>

