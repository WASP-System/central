<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<c:if test="${not empty param.error}">
<script type="text/JavaScript">waspFade("waspErrorMessage", "<fmt:message key="auth.login_failed.error" />");</script>
	
</c:if>

<h1>
	<fmt:message key="pageTitle.auth/login.label" />
</h1>

<div class="instructions">
	<fmt:message key="auth.login_instructions.label" />
</div>

<form name="f" action="<c:url value='/j_spring_security_check'/>"
	method="POST" onsubmit='return validate();'>
	<table class="EditTable ui-widget ui-widget-content">
		<tr class="FormData">
			<td class="CaptionTD"><fmt:message key="auth.login_user.label" />:</td>
			<td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" type='text' name='j_username'
				value='<c:if test="${not empty param.error}"><c:out value="${SPRING_SECURITY_LAST_EXCEPTION.authentication.principal}"/></c:if>' /></td>
		</tr>

		<tr class="FormData">
			<td class="CaptionTD"><fmt:message key="auth.login_password.label" />:</td>
			<td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" type='password' name='j_password' /></td>
		</tr>
		
		<tr class="FormData">
			<td class="CaptionTD">Remember me:</td>
			<td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" type='checkbox' name='_spring_security_remember_me'/></td>
		</tr>
		
	</table>

	<div class="submit">
		<input class="FormElement ui-widget-content ui-corner-all" name="submit" type="submit"
			value='<fmt:message key="auth.login_submit.label" />' />
	</div>
</form>


<nav id="loginNav">
	<a href="/wasp/auth/resetpassword/request.do"><fmt:message	key="auth.login_anchor_forgotpass.label" /></a> | 
	<a href="/wasp/auth/newuser.do"><fmt:message key="auth.login_anchor_newuser.label" /></a> | 
	<a href="/wasp/auth/newpi/institute.do"><fmt:message key="auth.login_anchor_newpi.label" /></a> <%--  |
	<a href="/wasp/static/about.do"><fmt:message key="auth.login_anchor_about.label" /></a> --%>
</nav>


