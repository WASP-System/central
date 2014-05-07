<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

<c:if test="${not empty param.error}">
<script type="text/JavaScript">waspFade("waspErrorMessage", "<fmt:message key="auth.login_failed.error" />");</script>
	
</c:if>

<h1>
	<fmt:message key="pageTitle.auth/loginPage.label" />
</h1>

<div class="instructions">
	<c:if test="${isAuthenticationExternal == true}">
		<fmt:message key="auth.login_instructions_external.label" />
	</c:if>
	<c:if test="${isAuthenticationExternal == false}">
		<fmt:message key="auth.login_instructions_internal.label" />
	</c:if>
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
			<td class="CaptionTD"><fmt:message key="auth.login_rememberMe.label" />:</td>
			<td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" type='checkbox' name='_spring_security_remember_me'/></td>
		</tr>
		
	</table>

	<div class="submit">
		<input class="FormElement ui-widget-content ui-corner-all" name="submit" type="submit"
			value='<fmt:message key="auth.login_submit.label" />' />
	</div>
</form>


<nav id="loginNav">
	<c:if test="${isAuthenticationExternal == false }">
		<a href="<wasp:relativeUrl value='auth/resetpassword/request.do' />"><fmt:message	key="auth.login_anchor_forgotpass.label" /></a> | 
	</c:if>
	<a href="<wasp:relativeUrl value='auth/newuser.do' />"><fmt:message key="auth.login_anchor_newuser.label" /></a> | 
	<a href="<wasp:relativeUrl value='auth/newpi/institute.do' />"><fmt:message key="auth.login_anchor_newpi.label" /></a> <%--  |
	<a href="<wasp:relativeUrl value='static/about.do' />"><fmt:message key="auth.login_anchor_about.label" /></a> --%>
</nav>
<wasp:displayInDemo>
	<br />
	<div class="instructions">
	<p><fmt:message key="auth.login_displayDemo_instructions.label" /></p>
	
	<h2><fmt:message key="auth.login_displayDemo_preLoadedDemoUsers.label" />:</h2>
	
	<table>
	<tr style="text-align:left"><th><fmt:message key="auth.login_displayDemo_name.label" /></th><th><fmt:message key="auth.login_displayDemo_role.label" /></th><th><fmt:message key="auth.login_displayDemo_login.label" /></th><th><fmt:message key="auth.login_displayDemo_password.label" /></th></tr>
	<tr><td>Super User</td><td>Super User</td><td>super</td><td>user</td></tr>
	<tr><td>John Smith</td><td><fmt:message key="auth.login_displayDemo_roleFM.label" /></td><td>jsmith</td><td>user</td></tr>
	<tr><td>Peter Walters</td><td><fmt:message key="auth.login_displayDemo_roleFT.label" /></td><td>pwalters</td><td>user</td></tr>
	<tr><td>Robin Lister</td><td><fmt:message key="auth.login_displayDemo_roleGA.label" /></td><td>robin</td><td>user</td></tr>
	<tr><td>Grainne O'Donovan</td><td><fmt:message key="auth.login_displayDemo_roleDAEU.label" /></td><td>gdon</td><td>user</td></tr>
	<tr><td>Joe Doe</td><td><fmt:message key="auth.login_displayDemo_roleDAGD.label" /></td><td>jdoe</td><td>user</td></tr>
	<tr><td>Sally Smythe</td><td><fmt:message key="auth.login_displayDemo_roleLabPI.label" /> (Smythe Lab)</td><td>ssmythe</td><td>user</td></tr>
	<tr><td>Percy Liu</td><td><fmt:message key="auth.login_displayDemo_roleLabMem.label" /> (Smythe Lab)</td><td>pliu</td><td>user</td></tr>
	</table>
	
	</div>
</wasp:displayInDemo>

