<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<h1><fmt:message key="pageTitle.resource/create.label" /></h1>

<form:form  cssClass="FormGrid" commandName="resource">

	<table class="EditTable ui-widget ui-widget-content">
		<tr class="FormData">
			<td class="CaptionTD"><fmt:message key="resource.name.label" />:</td>
			<td class="DataTD"><form:input cssClass="FormElement ui-widget-content ui-corner-all" path="name" />
			</td>
			<td class="CaptionTD error"><form:errors path="name" />
			</td>
		</tr>

		<tr class="FormData">
			<td class="CaptionTD"><fmt:message key="resource.typeResourceId.label" />:</td>
			<td class="DataTD"><select class="FormElement ui-widget-content ui-corner-all" name=typeResourceId>
					<option value='-1'>
						<fmt:message key="wasp.default_select.label" />
					</option>
					<c:forEach var="type" items="${typeResources}">
						<option value="${type.typeResourceId}">
							<c:out value="${type.name}" />
						</option>
					</c:forEach>
			</select></td>
			<td class="CaptionTD error"><form:errors path="typeResourceId" />
			</td>
		</tr>

		<c:set var="_area" value="resource" scope="request" />
		<c:set var="_metaList" value="${resource.resourceMeta}"
			scope="request" />
		<c:import url="/WEB-INF/jsp/meta_rw.jsp" />

		<tr class="FormData">
			<td colspan="2" align=right class="submitBottom">
				<button type="button" onclick="javascript:history.go(-1)">
					<fmt:message key="resource.cancel.label" />
				</button>
				<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="resource.save.label" />" />
			</td>
		</tr>
	</table>
</form:form>
