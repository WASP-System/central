<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
  <p><font color="blue"><wasp:message /></font></p>
  <form:form commandName="lab">
   <table>
     	  <tr><td colspan=2 align=left></br><b>Lab Details:</b></td></tr>
           <tr>
              <td><fmt:message key="lab.name.label" />:</td>
              <td>${lab.name}</td>              
          </tr>
          <tr>
              <td><fmt:message key="lab.primaryUserId.label"/>:</td>
              <td>
              <c:out value="${puserFullName}"/>
              </td>             
          </tr>
          <tr>
              <td><fmt:message key="lab.departmentId.label"/>:</td>
              <td>
              
              	<c:forEach var="dept" items="${departments}">
                   <c:if test="${dept.departmentId == lab.departmentId}"> <c:out value="${dept.name}"/></c:if>
               	</c:forEach>     
              
              </td>
          </tr>
          <c:set var="_area" value = "lab" scope="request"/>	
		  <c:set var="_metaList" value = "${lab.labMeta}" scope="request" />		
          <c:import url="/WEB-INF/jsp/meta_ro.jsp"/>
          <sec:authorize access="hasRole('god') or hasRole('sa') or hasRole('ga') or hasRole('lu-#')">
          <tr>
          	 <td colspan = "3">
          	 		<a href="<c:url value="/lab/detail_rw/${lab.departmentId}/${lab.labId}.do" />">Edit</a>
          	 </td>
          </tr>

          </sec:authorize>            	      
   </table> 
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
   </form:form>
