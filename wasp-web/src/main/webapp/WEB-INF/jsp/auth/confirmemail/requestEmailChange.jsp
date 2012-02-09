<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<wasp:message />
<h1>
	<fmt:message key="pageTitle.auth/confirmemail/requestEmailChange.label" />
</h1>


<form name="f" method="POST" action="">
	<table>
		<tr>
			<td><fmt:message key="auth.login_user.label" /></td>
			<td><input type='text' name='loginName'	value='<c:out value="${loginName}" default="" />' /><span class="requiredField">*</span></td>
		</tr>
		<tr>
			<td><fmt:message key="auth.login_password.label" /></td>
			<td><input type='password' name='password'	value='<c:out value="${password}" default="" />' /><span class="requiredField">*</span></td>
		</tr>
		<tr>
			<td><fmt:message key="auth.requestEmailChange_email.label" /></td>
			<td><input type='text' name='email'	value='<c:out value="${email}" default="" />' /><span class="requiredField">*</span></td>
		</tr>
		<tr>
			<td><fmt:message key="auth.confirmemail_captcha.label" /></td>
			<td><img src="<c:url value='/stickyCaptchaImg.png'/>" alt="Captcha Image" /><br /><input type='text' name='captcha_text'	value='<c:out value="${captcha_text}" default="" />' /><span class="requiredField">*</span></td>
		</tr>
		<tr>
			<td colspan='2'><input name="submit" type="submit"	value='<fmt:message key="auth.requestEmailChange_submit.label" />' /></td>
		</tr>
	</table>
</form>

