<%@ include file="/WEB-INF/jsp/taglib.jsp"%>


<h1>
	<fmt:message key="pageTitle.auth/confirmemail/requestEmailChange.label" />
</h1>


<form name="f" method="POST" action="">
	<table class="EditTable ui-widget ui-widget-content">
		<tr class="FormData">
			<td class="CaptionTD"><fmt:message key="auth.login_user.label" />:</td>
			<td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" type='text' name='loginName'	value='<c:out value="${loginName}" default="" />' /><span class="requiredField">*</span></td>
		</tr>
		<tr class="FormData">
			<td class="CaptionTD"><fmt:message key="auth.login_password.label" />:</td>
			<td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" type='password' name='password'	value='<c:out value="${password}" default="" />' /><span class="requiredField">*</span></td>
		</tr>
		<tr class="FormData">
			<td class="CaptionTD"><fmt:message key="auth.requestEmailChange_email.label" />:</td>
			<td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" type='text' name='email'	value='<c:out value="${email}" default="" />' /><span class="requiredField">*</span></td>
		</tr>
		<tr class="FormData">
			<td class="CaptionTD"><fmt:message key="auth.confirmemail_captcha.label" />:</td>
			<td class="DataTD"><img src="<wasp:relativeUrl value='/stickyCaptchaImg.png'/>" alt="Captcha Image" /><br /><input class="FormElement ui-widget-content ui-corner-all" type='text' name='captcha_text'	value='<c:out value="${captcha_text}" default="" />' /><span class="requiredField">*</span></td>
		</tr>
		<tr class="FormData">
			<td colspan='2'  class="submitBottom"><input class="FormElement ui-widget-content ui-corner-all" name="submit" type="submit"	value='<fmt:message key="auth.requestEmailChange_submit.label" />' /></td>
		</tr>
	</table>
</form>

