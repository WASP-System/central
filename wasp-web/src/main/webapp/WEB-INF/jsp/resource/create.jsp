<%@ include file="/WEB-INF/jsp/taglib.jsp"%>
<wasp:message />
<h1><fmt:message key="pageTitle.resource/create.label" /></h1>

<form:form commandName="resource">

	<table>
		<tr>
			<td><fmt:message key="resource.name.label" />:</td>
			<td><form:input path="name" />
			</td>
			<td><form:errors path="name" />
			</td>
		</tr>

		<tr>
			<td><fmt:message key="resource.typeResourceId.label" />:</td>
			<td><select name=typeResourceId>
					<option value='-1'>
						<fmt:message key="wasp.default_select.label" />
					</option>
					<c:forEach var="type" items="${typeResources}">
						<option value="${type.typeResourceId}">
							<c:out value="${type.name}" />
						</option>
					</c:forEach>
			</select></td>
			<td><form:errors path="typeResourceId" />
			</td>
		</tr>

		<c:set var="_area" value="resource" scope="request" />
		<c:set var="_metaList" value="${resource.resourceMeta}"
			scope="request" />
		<c:import url="/WEB-INF/jsp/meta_rw.jsp" />

		<tr>
			<td colspan="2" align=right>
				<button type="button" onclick="javascript:history.go(-1)">
					<fmt:message key="resource.cancel.label" />
				</button>
				<input type="submit" value="<fmt:message key="resource.save.label" />" />
			</td>
		</tr>
	</table>
</form:form>
