<%@ include file="/WEB-INF/jsp/taglib.jsp"%>
<wasp:message />
<h1>
	<fmt:message key="pageTitle.auth/resetpassword/form.label" />
</h1>
<h4>
	<fmt:message key="auth.resetpassword_instructions.label" />
</h4>


<form name="f" action="<c:url value='/auth/resetpassword/form.do'/>"
	method="POST">
	<input type='hidden' name='authcode'
		value='<c:out value="${authcode}" default="" />' />

	<table>
		<tr>
			<td><fmt:message key="auth.resetpassword_user.label" /></td>
			<td><input type='text' name='username' value='<c:out value="${username}" default="" />' /><span class="requiredField">*</span></td>
		</tr>
		<tr>
			<td><fmt:message key="auth.resetpassword_password1.label" /></td>
			<td><input type='password' name='password1' value='' /><span class="requiredField">*</span></td>
		</tr>
		<tr>
			<td><fmt:message key="auth.resetpassword_password2.label" /></td>
			<td><input type='password' name='password2' value='' /><span class="requiredField">*</span></td>
		</tr>
		<tr>
			<td><fmt:message key="auth.resetpassword_captcha.label" /></td>
			<td><img src="<c:url value='/stickyCaptchaImg.png'/>" alt="Captcha Image" /><br /><input type='text' name='captcha_text' value='' /><span class="requiredField">*</span></td>
		</tr>
		<tr>
			<td colspan='2'><input name="submit" type="submit" value='<fmt:message key="auth.resetpassword_submit.label" />' /></td>
		</tr>
	</table>
</form>
