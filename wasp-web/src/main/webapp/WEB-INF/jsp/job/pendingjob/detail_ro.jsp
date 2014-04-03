<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
     
    <h1><fmt:message key="pendingJob.detailRO_job.label" /> <c:out value="${job.jobId}"/>: <c:out value="${job.name}"/></h1>

	<p>
      <fmt:message key="pendingJob.detailRO_submittingUser.label" />: <c:out value="${job.user.firstName}"/>&nbsp;<c:out value="${job.user.lastName}"/>
    </p>
    
    <p>
      <fmt:message key="pendingJob.detailRO_pi.label" />: <c:out value="${job.lab.user.firstName}"/>&nbsp;<c:out value="${job.lab.user.lastName}"/> [<c:out value="${job.lab.department.name}"/>]
      </a>
    </p>    

    <c:forEach items="${jobmeta}" var="meta">
     
      <c:out value="${meta.k}" />:&nbsp;<c:out value="${meta.v}" />
      </br>
    </c:forEach>

   <sec:authorize access="hasRole('da-*') or hasRole('su') or hasRole('ga-*')">
    </br>
    <a href="<c:url value='job/pendingdaapproval/approve/${job.lab.department.departmentId}/${job.jobId}.do' />"><fmt:message key="pendingJob.detailRO_approve.label" /></a>
	| 
	<a href="<c:url value='job/pendingdaapproval/reject/${job.lab.department.departmentId}/${job.jobId}.do' />"><fmt:message key="pendingJob.detailRO_reject.label" /></a>
	
	</sec:authorize>
 
