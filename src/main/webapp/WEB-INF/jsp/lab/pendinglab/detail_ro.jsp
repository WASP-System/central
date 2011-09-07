<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head><title>WASP</title><head>
  <body>
  <font color="blue"><wasp:message /></font>  
  <form:form commandName="labpending">
   <table>
     	  <tr><td colspan=2 align=left></br><b>Pending Lab Details:</b></td></tr>
           <tr>
              <td><fmt:message key="labPending.name.label" />:</td>
              <td>${labpending.name}</td>              
          </tr>
          <tr>
              <td><fmt:message key="labPending.primaryUserId.label"/>:</td>
              <td>
              	<c:forEach var="puser" items="${pusers}">
                	<c:if test="${puser.userId == labpending.primaryUserId}"><c:out value="${puser.lastName}, ${puser.firstName}"/></c:if>
               	</c:forEach>     
              
              </td>              
          </tr>
          <tr>
              <td><fmt:message key="labPending.departmentId.label"/>:</td>
              <td>
              
              	<c:forEach var="dept" items="${departments}">
                   <c:if test="${dept.departmentId == labpending.departmentId}"> <c:out value="${dept.name}"/></c:if>
               	</c:forEach>     
              
              </td>
          </tr>
          <c:set var="_area" value = "labPending" scope="request"/>	
		  <c:set var="_metaList" value = "${labpending.labPendingMeta}" scope="request" />		
          <c:import url="/WEB-INF/jsp/meta_ro.jsp"/>
          <sec:authorize access="hasRole('god') or hasRole('sa') or hasRole('ga') or hasRole('da-#')">
          	 <tr>
          	 	<td>
          	 		<a href="<c:url value="/lab/labpending/approve/${labpending.departmentId}/${labpending.labPendingId}.do" />">Approve</a>
          	 		|
          	 		<a href="<c:url value="/lab/labpending/reject/${labpending.departmentId}/${labpending.labPendingId}.do" />">Reject</a>
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
  </body>
</html>
