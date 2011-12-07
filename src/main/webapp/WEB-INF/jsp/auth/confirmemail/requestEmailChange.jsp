<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

    <h1><fmt:message key="pageTitle.auth/confirmemail/requestEmailChange.label" /></h1>
     <font color="red"><wasp:message /></font>

    <form name="f"  method="POST">
       <table>
      	<tr><td><fmt:message key="auth.login_user.label" /></td><td><input type='text' name='loginName' value='<c:out value="${loginName}" default="" />'/></td></tr>
        <tr><td><fmt:message key="auth.login_password.label" /></td><td><input type='password' name='password' value='<c:out value="${password}" default="" />'/></td></tr>
        <tr><td><fmt:message key="auth.requestEmailChange_email.label" /></td><td><input type='text' name='email' value='<c:out value="${email}" default="" />'/></td></tr>
        <tr><td>&nbsp;</td><td><img src="<c:url value='/stickyCaptchaImg.png'/>" /></td></tr>
        <tr><td><fmt:message key="auth.confirmemail_captcha.label" /></td><td><input type='text' name='captcha_text' value='<c:out value="${captcha_text}" default="" />'/></td></tr>
        <tr><td colspan='2'><input name="submit" type="submit" value="<fmt:message key="auth.requestEmailChange_submit.label" />"/></td></tr>
      </table>
    </form>

