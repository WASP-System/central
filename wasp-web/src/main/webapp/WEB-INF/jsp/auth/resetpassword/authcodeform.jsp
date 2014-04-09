<%@ include file="/WEB-INF/jsp/taglib.jsp"%>


<h1>
	<fmt:message key="pageTitle.auth/resetpassword/authcodeform.label" />
</h1>



<form name="f" action="<wasp:relativeUrl value='/auth/resetpassword/form.do'/>" method="GET">
	<table class="EditTable ui-widget ui-widget-content">
		<tr class="FormData">
			<td class="label"><fmt:message key="auth.resetpassword_authcode.label" />:</td>
			<td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" type='text' name='authcode' value='' /><span class="requiredField">*</span></td>
		</tr>
		<tr class="FormData">
			<td colspan='2' class="submitBottom"><input class="FormElement ui-widget-content ui-corner-all" name="submit" type="submit" value='<fmt:message key="auth.resetpassword_submit.label" />' /></td>
		</tr>
	</table>
</form>


