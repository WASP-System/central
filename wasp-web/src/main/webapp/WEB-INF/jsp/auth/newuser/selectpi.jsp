<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<h1><fmt:message key="userPending.form_header.label" /></h1>

<div class="instructions"><fmt:message key="piPending.select_pi_message.label" /></div>

<div>
	<form name="f" method="POST" action='<wasp:relativeUrl value="auth/newuser/selectpi.do"/>'>
		<table class="EditTable ui-widget ui-widget-content">
			<tr class="FormData">
				<tr class="FormData">
	          <td class="CaptionTD"><fmt:message key="userPending.primaryuserid_i18n.label"/>:</td>
	          <td class="DataTD"><input type="text" class="FormElement ui-widget-content ui-corner-all" name="primaryuserid" id="primaryuserid" value="<c:out value="${primaryUserId}" />" /><span class="requiredField">*</span></td>
	          <td class="CaptionTD error"><c:out value="${primaryUserIdError}" />&nbsp;</td>
	        </tr>      
		</table>
		<div>
			<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key='piPending.select_institute_submit.label'/>" />
		</div>
	</form>
</div>
