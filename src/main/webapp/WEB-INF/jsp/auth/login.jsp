<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
  <head> <title><fmt:message key="auth.login.title" /></title> 
  <script language="JavaScript">
		<!--
		function validate(){
			//alert("test123"); return false;
			var error = false;
			var message = '<fmt:message key="user.auth.login.validate.error" />';
			if(document.f.j_username.value == "" || document.f.j_password.value == ""){
				error = true;
			}
			if(error){ alert(message); return false; }
  			return true;
		}
		//-->
	</script>    
  </head>
  <body onload='document.f.j_username.focus();'>
  <h1><fmt:message key="auth.login.title" /></h1>
    <c:if test="${not empty param.error}">
      <font color="red">
        <fmt:message key="auth.login.failed" /><br/><br/>
        <fmt:message key="auth.login.reason.label" />: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
      </font>
    </c:if>
 
    <form name="f" action="<c:url value='/j_spring_security_check'/>" method="POST" onsubmit='return validate();'>
      <table>
        <tr><td><fmt:message key="auth.login.user.label" />:</td><td><input type='text' name='j_username' value='<c:if test="${not empty param.error}"><c:out value="${SPRING_SECURITY_LAST_EXCEPTION.authentication.principal}"/></c:if>'/></td></tr>

        <tr><td><fmt:message key="auth.login.password.label" />:</td><td><input type='password' name='j_password'/></td></tr>
        <tr><td colspan='2'><input name="submit" type="submit" value="<fmt:message key="auth.login.submit" />"/></td></tr>
      </table>
</form>

<div>
<a href="/wasp/auth/forgotpassword.do"><fmt:message key="auth.login.anchor.forgotpass" /></a> |
<a href="/wasp/auth/newuser.do"><fmt:message key="auth.login.anchor.newuser" /></a> |
<a href="/wasp/auth/newpi.do"><fmt:message key="auth.login.anchor.newpi" /></a> |
<a href="/wasp/static/about.do"><fmt:message key="auth.login.anchor.about" /></a>
</div>

</body></html>
