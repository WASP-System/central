<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<h1>
	<fmt:message key="pageTitle.auth/resetpassword/form.label" />
</h1>
<h4>
	<fmt:message key="auth.resetpassword_instructions.label" />
</h4>


<form name="f" action="<wasp:relativeUrl value='/auth/resetpassword/form.do'/>"
	method="POST">
	<input type='hidden' name='authcode'
		value='<c:out value="${authcode}" default="" />' />

	<table class="EditTable ui-widget ui-widget-content">
		<tr class="FormData">
			<td><fmt:message key="auth.resetpassword_user.label" /></td>
			<td><input class="FormElement ui-widget-content ui-corner-all" type='text' name='username' value='<c:out value="${username}" default="" />' /><span class="requiredField">*</span></td>
		</tr>
		<tr class="FormData">
			<td><fmt:message key="auth.resetpassword_password1.label" /></td>
			<td><input class="FormElement ui-widget-content ui-corner-all" type='password' name='password1' value='' /><span class="requiredField">*</span></td>
		</tr>
		<tr class="FormData">
			<td><fmt:message key="auth.resetpassword_password2.label" /></td>
			<td><input class="FormElement ui-widget-content ui-corner-all" type='password' name='password2' value='' /><span class="requiredField">*</span></td>
		</tr>
		<tr class="FormData">
			<td><fmt:message key="auth.resetpassword_captcha.label" /></td>
			<td><img src="<wasp:relativeUrl value='/stickyCaptchaImg.png'/>" alt="Captcha Image" /><br /><input class="FormElement ui-widget-content ui-corner-all" type='text' name='captcha_text' value='' /><span class="requiredField">*</span></td>
		</tr>
		<tr class="FormData">
			<td colspan='2'><input class="FormElement ui-widget-content ui-corner-all" name="submit" type="submit" value='<fmt:message key="auth.resetpassword_submit.label" />' /></td>
		</tr>
	</table>
</form>
