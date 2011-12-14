<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
     
    <h1>Job <c:out value="${job.jobId}"/>: <c:out value="${job.name}"/></h1>

	<p>
      Submitting User: <c:out value="${job.user.firstName}"/>&nbsp;<c:out value="${job.user.lastName}"/>
    </p>
    
    <p>
      PI: <c:out value="${job.lab.user.firstName}"/>&nbsp;<c:out value="${job.lab.user.lastName}"/> [<c:out value="${job.lab.department.name}"/>]
      </a>
    </p>    

    <c:forEach items="${jobmeta}" var="meta">
     
      <c:out value="${meta.k}" />:&nbsp;<c:out value="${meta.v}" />
      </br>
    </c:forEach>

   <sec:authorize access="hasRole('da-*') or hasRole('god') or hasRole('ga-*')">
    </br>
    <a href="/wasp/job/pendingdaapproval/approve/<c:out value="${job.lab.department.departmentId}" />/<c:out value="${job.jobId}" />.do">Approve</a>
	| 
	<a href="/wasp/job/pendingdaapproval/reject/<c:out value="${job.lab.department.departmentId}" />/<c:out value="${job.jobId}" />.do">Reject</a>
	
	</sec:authorize>
 