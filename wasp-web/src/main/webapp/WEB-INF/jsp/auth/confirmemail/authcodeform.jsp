<%@ include file="/WEB-INF/jsp/taglib.jsp"%>


<h1>
	<fmt:message key="pageTitle.auth/confirmemail/authcodeform.label" />
</h1>


<form name="f" method="POST" action="">
	<table class="EditTable ui-widget ui-widget-content">
		<tr class="FormData">
			<td class="CaptionTD"><fmt:message key="auth.confirmemail_email.label" />:</td>
			<td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" type='text' name='email'	value='<c:out value="${email}" default="" />' /><span class="requiredField">*</span></td>
		</tr>
		<tr class="FormData">
			<td class="CaptionTD"><fmt:message key="auth.confirmemail_authcode.label" />:</td>
			<td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" type='text' name='authcode'	value='<c:out value="${authcode}" default="" />' /><span class="requiredField">*</span></td>
		</tr>
		<tr class="FormData">
			<td class="CaptionTD"><fmt:message key="auth.confirmemail_captcha.label" />:/td>
			<td class="DataTD"><img src="<c:url value='/stickyCaptchaImg.png'/>" alt="Captcha Image" /><br /><input class="FormElement ui-widget-content ui-corner-all" type='text' name='captcha_text' value='' /><span class="requiredField">*</span></td>
		</tr>
		<tr class="FormData">
			<td class="submitBottom" colspan='2'><input class="FormElement ui-widget-content ui-corner-all" name="submit" type="submit"	value='<fmt:message key="auth.confirmemail_submit.label" />' /></td>
		</tr>
	</table>
</form>

