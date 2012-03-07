<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br /><br />
      
      <sec:authorize access="hasRole('da-*') or hasRole('su') or hasRole('ga-*')">
      <title><fmt:message key="pageTitle.department/dapendingtasks.label"/></title>

     <h2><fmt:message key="department.detail_pendinglabs.label" /></h2>
    <c:choose>
    <c:when test="${sizelabspendinglist==0}">
    <div>None</div>    
    </c:when>
    <c:otherwise>    
    <c:forEach items="${labspendinglist}" var="lp">
      <div>
      	<a href="/wasp/lab/pending/detail_ro/<c:out value="${lp.departmentId}" />/<c:out value="${lp.labPendingId}" />.do">
      	<c:out value="${lp.name}" /></a> (PI: <c:out value="${lp.userPending.firstName} ${lp.userPending.lastName}" /> [<c:out value="${lp.department.name}" />])  	
      </div>
    </c:forEach>
    </c:otherwise>
    </c:choose>
    <br />
    <h2><fmt:message key="department.detail_pendingjobs.label" /></h2>
     <c:choose>
    <c:when test="${sizejobspendinglist==0}">
    <div>None</div>    
    </c:when>
    <c:otherwise> 
    <c:forEach items="${jobspendinglist}" var="jp">
      <div>
      	<a href="/wasp/job/pending/detail_ro/<c:out value="${jp.lab.department.departmentId}" />/<c:out value="${jp.lab.labId}" />/<c:out value="${jp.jobId}" />.do">
      	Job <c:out value="${jp.jobId}" /></a> (Submitter: <c:out value="${jp.user.firstName} ${jp.user.lastName}" />; PI: <c:out value="${jp.lab.user.firstName} ${jp.lab.user.lastName}" /> [<c:out value="${jp.lab.department.name}" />])
      </div>
    </c:forEach>
    </c:otherwise>
    </c:choose>
    </sec:authorize>
