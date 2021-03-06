<%@ include file="/WEB-INF/jsp/taglib.jsp"%>
<p>
	
</p>
<form:form  cssClass="FormGrid" commandName="labPending">
	<table class="EditTable ui-widget ui-widget-content">
		<tr class="FormData">
			<td colspan=2 align=left>
			<b><fmt:message key="labPending.pendingLabDetails.label" />:</b>
			</td>
		</tr>
		<tr class="FormData">
			<td class="CaptionTD"><fmt:message key="labPending.name.label" />:</td>
			<td class="DataTD">${labPending.name}</td>
		</tr>
		<tr class="FormData">
			<td class="CaptionTD"><fmt:message key="labPending.primaryUserId.label" />:</td>
			<td class="DataTD"><c:out value="${puserFullName}" /></td>
		</tr>
		<tr class="FormData">
			<td class="CaptionTD"><fmt:message key="labPending.departmentId.label" />:</td>
			<td class="DataTD"><c:forEach var="dept" items="${departments}">
					<c:if test="${dept.departmentId == labPending.departmentId}">
						<c:out value="${dept.name}" />
					</c:if>
				</c:forEach></td>
		</tr>
		<c:set var="_area" value="labPending" scope="request" />
		<c:set var="_metaList" value="${labPending.labPendingMeta}"
			scope="request" />
		<c:import url="/WEB-INF/jsp/meta_ro.jsp" />
		<sec:authorize
			access="hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('da-*')">
			<tr class="FormData">
				<td><a
					href="<wasp:relativeUrl value="lab/pending/detail_rw/${labPending.departmentId}/${labPending.labPendingId}.do" />"><fmt:message key="labPending.edit.label" /></a>
					| <a
					href="<wasp:relativeUrl value="lab/pending/approve/${labPending.departmentId}/${labPending.labPendingId}.do" />"><fmt:message key="labPending.approve.label" /></a>
					| <a
					href="<wasp:relativeUrl value="lab/pending/reject/${labPending.departmentId}/${labPending.labPendingId}.do" />"><fmt:message key="labPending.reject.label" /></a>
				</td>
			</tr>
		</sec:authorize>
	</table>
	

</form:form>
