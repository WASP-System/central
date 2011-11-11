<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

    <h1><fmt:message key="pageTitle.auth/resetpassword/authcodeform.label" /></h1>
    
    <font color="red"><wasp:message /></font>

    <form name="f" action="<c:url value='/auth/resetpassword/form.do'/>" method="GET">
      <table>
        <tr><td><fmt:message key="auth.resetpassword_authcode.label" />:</td><td><input type='text' name='authcode' value=''/></td></tr>
        <tr><td colspan='2'><input name="submit" type="submit" value="<fmt:message key="auth.resetpassword_submit.label" />"/></td></tr>
      </table>
    </form>


