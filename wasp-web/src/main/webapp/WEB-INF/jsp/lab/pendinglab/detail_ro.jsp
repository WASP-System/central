<%@ include file="/WEB-INF/jsp/taglib.jsp"%>
<p>
	<wasp:message />
</p>
<form:form commandName="labPending">
	<table>
		<tr>
			<td colspan=2 align=left>
			<b>Pending Lab Details:</b>
			</td>
		</tr>
		<tr>
			<td><fmt:message key="labPending.name.label" />:</td>
			<td>${labPending.name}</td>
		</tr>
		<tr>
			<td><fmt:message key="labPending.primaryUserId.label" />:</td>
			<td><c:out value="${puserFullName}" /></td>
		</tr>
		<tr>
			<td><fmt:message key="labPending.departmentId.label" />:</td>
			<td><c:forEach var="dept" items="${departments}">
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
			<tr>
				<td><a
					href="<c:url value="/lab/pending/detail_rw/${labPending.departmentId}/${labPending.labPendingId}.do" />">Edit</a>
					| <a
					href="<c:url value="/lab/pending/approve/${labPending.departmentId}/${labPending.labPendingId}.do" />">Approve</a>
					| <a
					href="<c:url value="/lab/pending/reject/${labPending.departmentId}/${labPending.labPendingId}.do" />">Reject</a>
				</td>
			</tr>
		</sec:authorize>
	</table>
	<%-- 
     <c:forEach items="${labuser}" var="ul">
      <p>
      <a href="/wasp/user/detail/<c:out value="${ul.user.userId}" />.do"><c:out value="${ul.user.login}" /></a>
        <c:out value="${ul.user.firstName}" />
        <c:out value="${ul.user.lastName}" />
      <span><c:out value="${ul.role.name}" /></span>
      </p>
    </c:forEach>

    <c:if test="${!  empty( job ) }">   
      <h2>Jobs</h2>
      <c:forEach items="${job}" var="j">
        <div>
          <a href="/wasp/job/detail/<c:out value="${j.jobId}" />.do">
          <c:out value="${j.name}" />
          </a>
        </div>
      </c:forEach>
    </c:if>

    <div>
      <a href="/wasp/job/create/form.do?labid=<c:out value="${lab.labId}" />">create job</a>
    </div>
 --%>

</form:form>
