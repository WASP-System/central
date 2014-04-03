<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<h1><fmt:message key="piPending.form_header.label" /></h1>

<div class="instructions"><fmt:message key="piPending.select_institute_message.label" /></div>

<div>
	<form name="f" method="POST" action='<wasp:relativeUrl value="/auth/newpi/institute.do"/>'>
		<table class="EditTable ui-widget ui-widget-content">
			<tr class="FormData">
				<td class="CaptionTD"><fmt:message	key='piPending.select_institute.label' />:</td>
				<td class="DataTD"><select class="FormElement ui-widget-content ui-corner-all" name="instituteSelect" onchange=" return selectChange();">
						<option value=''>
							<fmt:message key="wasp.default_select.label" />
						</option>
						<c:forEach items="${instituteList}" var="institute"	varStatus="status">
							<option value='${institute}'>${institute}</option>
						</c:forEach>
						<option value='other'>
							<fmt:message key='piPending.select_institute_other.label' />
						</option>
				</select></td>
			</tr>
			<tr class="FormData">
				<td class="CaptionTD"><fmt:message	key='piPending.specify_other_institute.label' />:</td>
				<td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" type="text" name="instituteOther"	id="instituteOther" readonly="readonly" value="" /></td>
			</tr>
		</table>
		<div>
			<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key='piPending.select_institute_submit.label'/>" />
		</div>
	</form>
</div>
