<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<wasp:message />
<h1>
	<fmt:message key="pageTitle.auth/confirmemail/authcodeform.label" />
</h1>


<form name="f" method="POST" action="">
	<table>
		<tr>
			<td><fmt:message key="auth.confirmemail_email.label" /></td>
			<td><input type='text' name='email'	value='<c:out value="${email}" default="" />' /><span class="requiredField">*</span></td>
		</tr>
		<tr>
			<td><fmt:message key="auth.confirmemail_authcode.label" /></td>
			<td><input type='text' name='authcode'	value='<c:out value="${authcode}" default="" />' /><span class="requiredField">*</span></td>
		</tr>
		<tr>
			<td><fmt:message key="auth.confirmemail_captcha.label" /></td>
			<td><img src="<c:url value='/stickyCaptchaImg.png'/>" alt="Captcha Image" /><br /><input type='text' name='captcha_text' value='' /><span class="requiredField">*</span></td>
		</tr>
		<tr>
			<td colspan='2'><input name="submit" type="submit"	value='<fmt:message key="auth.confirmemail_submit.label" />' /></td>
		</tr>
	</table>
</form>

