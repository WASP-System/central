<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

	<wasp:message />

<form:form  cssClass="FormGrid" commandName="labPending">
	<table class="EditTable ui-widget ui-widget-content">
		<tr class="FormData">
			<td colspan=2 align=left>
			<b><fmt:message key="labPending.heading.label" />
			</b>
			</td>
		</tr>
		<tr class="FormData">
			<td class="CaptionTD"><fmt:message key="labPending.name.label" />:</td>
			<td class="DataTD"><form:input cssClass="FormElement ui-widget-content ui-corner-all" path="name" /></td>
			<td class="CaptionTD error"><form:errors path="name" />
			</td>
		</tr>
		<tr class="FormData">
			<td class="CaptionTD"><fmt:message key="labPending.primaryUserId.label" />:</td>
			<td class="DataTD"><c:out value="${puserFullName}" /><form:hidden path="primaryUserId" /></td>
			<td class="CaptionTD error">&nbsp;</td>
		</tr>
		<tr class="FormData">
			<td class="CaptionTD"><fmt:message key="labPending.departmentId.label" />:</td>
			<td class="DataTD"><select class="FormElement ui-widget-content ui-corner-all" name=departmentId>
					<option value='-1'>
						<fmt:message key="wasp.default_select.label" />
					</option>
					<c:forEach var="dept" items="${departments}">
						<option value="${dept.departmentId}"
							<c:if test="${dept.departmentId == labPending.departmentId}"> selected</c:if>>
							<c:out value="${dept.name}" />
						</option>
					</c:forEach>
			</select></td>
			<td class="CaptionTD error"><form:errors path="departmentId" />
			</td>
		</tr>
		<c:set var="_area" value="labPending" scope="request" />
		<c:set var="_metaList" value="${labPending.labPendingMeta}"
			scope="request" />
		<c:import url="/WEB-INF/jsp/meta_rw.jsp" />
		<tr class="FormData">
			<td colspan="3" align=right class="submitBottom">
				<input class="FormElement ui-widget-content ui-corner-all" type="submit" name="submit" value="<fmt:message key="labPending.cancel.label" />" />
				<input class="FormElement ui-widget-content ui-corner-all" type="submit" name="submit" value="<fmt:message key="labPending.save.label" />" />
			</td>
		</tr>

	</table>
</form:form>
