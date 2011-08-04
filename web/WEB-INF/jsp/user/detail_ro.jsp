<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head><title>User Detail</title><head>
  <body>
    
    <h1>User Detail</h1>
    
     <table>
     	  <tr><td colspan=2 align=left></br><b>User Details:</b></td></tr>
     	   
     	   <tr>
              <td><fmt:message key="user.login.label" />:</td>
              <td>${user.login}</td>            
          </tr>          	
          <tr>
              <td><fmt:message key="user.firstName.label" />:</td>
              <td>${user.firstName}</td>              
          </tr>
          <tr>
              <td><fmt:message key="user.lastName.label"/>:</td>
              <td>${user.lastName}</td>
          </tr>
          <tr>
              <td><fmt:message key="user.email.label"/>:</td>
              <td>${user.email}</td>
          </tr>         
          <tr>
              <td><fmt:message key="user.locale.label"/>:</td>
              <td>                              
                <c:if test="${user.locale == 'en_US'}">USA</c:if>
              	<c:if test="${user.locale == 'iw_IL'}">Hebrew</c:if>
              	<c:if test="${user.locale == 'ru_RU'}">Russian</c:if>   
              </td>
              <td><form:errors path="locale" /></td>
          </tr>
		
		
		<c:set var="_area" value = "user" scope="request"/>	
		<c:set var="_metaList" value = "${user.usermeta}" scope="request" />		
        <c:import url="/WEB-INF/jsp/meta_ro.jsp"/>
          
		  <sec:authorize access="hasRole('god')">      		   		
          <tr><td><a href="/wasp/user/detail_rw/${user.userId}.do">Edit</a></td></tr>
          </sec:authorize>	
		
         <tr><td colspan=2 align=left></br><b>Lab Users:</b></td></tr>
         <c:forEach items="${user.labUser}" var="ul">
    	  <tr>
            <td><a href="/wasp/lab/detail/<c:out value="${ul.lab.labId}" />.do"><c:out value="${ul.lab.name}" /></a></td>
            <td><span><c:out value="${ul.role.name}" /></span></td>
          </tr>
        </c:forEach>
          
		 <tr><td colspan=2 align=left></br><b>Samples:</b></td></tr>
         <c:forEach items="${user.sample}" var="sample">
    	  <tr>
            <td><a href="/wasp/sample/detail/<c:out value="${sample.sampleId}" />.do"><c:out value="${sample.name}" /></a></td>           
          </tr>
        </c:forEach>
		
         <tr><td colspan=2 align=left></br><b>Jobs:</b></td></tr>
         <c:forEach items="${user.job}" var="job">
    	  <tr>
            <td><a href="/wasp/job/detail/<c:out value="${job.jobId}" />.do"><c:out value="${job.name}" /></a></td>           
          </tr>
        </c:forEach>  

     </table>
  
   
 
  </body>
</html>
