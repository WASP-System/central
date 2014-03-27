<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<h1>
	<fmt:message key="pageTitle.auth/getEmailForDemoForm.label" />
</h1>

<div class="instructions">
	<fmt:message key="auth.get_email_instructions.label" />
</div>
<form:form method="POST" action="getEmailForDemo.do">
<table class="EditTable ui-widget ui-widget-content">
	<tr class="FormData">
		<td class="CaptionTD"><fmt:message key="auth.demo_email.label" />:</td>
		<td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" type='text' name='email' /></td>
	</tr>
</table>

<div class="submit">
	<input class="FormElement ui-widget-content ui-corner-all" name="submit" type="submit"
		value='<fmt:message key="auth.demo_email_submit.label" />' />
</div>
</form:form>
