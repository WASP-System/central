<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head> <title><fmt:message key="auth.forgotpassword.title" /></title> </head>

  <body onload='document.f.j_username.focus();'>
   
     
    <h1><fmt:message key="auth.forgotpassword.title" /></h1>

     <font color="red"><wasp:message /></font>  

    <form name="f" action="<c:url value='/auth/forgotpassword.do'/>" method="POST">
      <table>
        <tr><td><fmt:message key="auth.forgotpassword.user.label" /></td><td><input type='text' name='username' value='<c:out value="${username}" default="" />' /></td></tr>
        <tr><td>&nbsp;</td><td><img src="<c:url value='/stickyCaptchaImg.png'/>" /></td></tr>
        <tr><td><fmt:message key="auth.forgotpassword.captcha.label" /></td><td><input type='text' name='captcha_text' value=''/></td></tr>
        <tr><td colspan='2'><input name="submit" type="submit" value="<fmt:message key="auth.forgotpassword.submit" />"/></td></tr>
      </table>
    </form>

</body></html>
 
