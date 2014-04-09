<%@ include file="/WEB-INF/jsp/taglib.jsp"%>



<h1>
	<fmt:message key="pageTitle.auth/resetpassword/request.label" />
</h1>

<div class="instructions">
	<fmt:message key="auth.resetpassword_start_instructions.label" />
</div>

<form name="f" action="<wasp:relativeUrl value='/auth/resetpassword/request.do'/>"
	method="POST">
	<table class="EditTable ui-widget ui-widget-content">
		<tr class="FormData">
			<td class="CaptionTD"><fmt:message key="auth.resetpasswordRequest_user.label" />:</td>
			<td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" type='text' name='username' value='<c:out value="${username}" default="" />' /><span class="requiredField">*</span></td>
		</tr>
		<tr class="FormData">
			<td class="CaptionTD"><fmt:message	key="auth.resetpasswordRequest_captcha.label" />:</td>
			<td class="DataTD"><img src="<wasp:relativeUrl value='/stickyCaptchaImg.png'/>" alt="Captchca Image" /><br />
			<input class="FormElement ui-widget-content ui-corner-all" type='text' name='captcha_text' value='' /><span	class="requiredField">*</span></td>
		</tr>
	</table>
	<div>
		<input class="FormElement ui-widget-content ui-corner-all" name="submit" type="submit" value='<fmt:message key="auth.resetpasswordRequest_submit.label" />' />
	</div>
</form>


