<%@ include file="/WEB-INF/jsp/taglib.jsp"%>
<wasp:message />
<h1>Update Workflow Details</h1>


<form:form commandName="workflow">

	<table>
		<tr>
			<td>
TODO SHOULD BE READONLY
<fmt:message key="workflow.iname.label" />:</td>
			<td><form:input path="iName" />
			</td>
			<td><form:errors path="iName" />
			</td>
		</tr>
		<tr>
			<td>
TODO SHOULD BE READONLY
<fmt:message key="workflow.name.label" />:</td>
			<td><form:input path="name" />
			</td>
			<td><form:errors path="name" />
			</td>
		</tr>
		<tr>
			<td><fmt:message key="workflow.isActive.label" />:</td>
			<td><form:input path="isActive" />
			</td>
			<td><form:errors path="isActive" />
			</td>
		</tr>

		<c:set var="_area" value="workflow" scope="request" />
		<c:set var="_metaList" value="${workflow.workflowMeta}"
			scope="request" />
		<c:import url="/WEB-INF/jsp/meta_rw.jsp" />

		<tr>
			<td colspan="2" align=right>
				<button type="button" onclick="javascript:history.go(-1)">
					<fmt:message key="workflow.cancel.label" />
				</button>
				<input type="submit" value="<fmt:message key="workflow.save.label" />" />
			</td>
		</tr>
	</table>
</form:form>
