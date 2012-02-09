<%@ include file="/WEB-INF/jsp/taglib.jsp"%>
<wasp:message />

<h1>
	<fmt:message key="pageTitle.auth/resetpassword/authcodeform.label" />
</h1>



<form name="f" action="<c:url value='/auth/resetpassword/form.do'/>" method="GET">
	<table>
		<tr>
			<td><fmt:message key="auth.resetpassword_authcode.label" />:</td>
			<td><input type='text' name='authcode' value='' /><span class="requiredField">*</span></td>
		</tr>
		<tr>
			<td colspan='2'><input name="submit" type="submit" value='<fmt:message key="auth.resetpassword_submit.label" />' /></td>
		</tr>
	</table>
</form>


