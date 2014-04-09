<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

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
	<p>For demonstration processed you may sign is as any of the users listed below. The functionality is role-based so you might wish to try different users. You can also 
	use the links above to create new PIs and lab users. The database is reset to it's default state every night at midnight and so any changes you make will be lost.</p>
	
	<h2>Pre-loaded demo users:</h2>
	
	<table>
	<tr style="text-align:left"><th>Name</th><th>Role</th><th>Login</th><th>Password</th></tr>
	<tr><td>Super User</td><td>Super User</td><td>super</td><td>user</td></tr>
	<tr><td>John Smith</td><td>Facility Manager</td><td>jsmith</td><td>user</td></tr>
	<tr><td>Peter Walters</td><td>Facility Tech.</td><td>pwalters</td><td>user</td></tr>
	<tr><td>Robin Lister</td><td>General Admin</td><td>robin</td><td>user</td></tr>
	<tr><td>Grainne O'Donovan</td><td>Departmental Admin (External users)</td><td>gdon</td><td>user</td></tr>
	<tr><td>Joe Doe</td><td>Department Admin (Genetics Dept)</td><td>jdoe</td><td>user</td></tr>
	<tr><td>Sally Smythe</td><td>Lab PI (Smythe Lab)</td><td>ssmythe</td><td>user</td></tr>
	<tr><td>Percy Liu</td><td>Lab Member (Smythe Lab)</td><td>pliu</td><td>user</td></tr>
	</table>
	
	</div>
</wasp:displayInDemo>

