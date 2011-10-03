<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
    <h1><fmt:message key="userDetail.page_title.label" /></h1>
    
     <table>
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
              <c:forEach var="localeEntry" items="${locales}">
                <c:set var="localeValue" value="${localeEntry.key}"/>
                <c:set var="localeLabel" value="${localeEntry.value}"/>               
                <c:if test="${user.locale == localeValue}">${localeLabel}</c:if>
              </c:forEach>
  			  </td>
              <td><form:errors path="locale" /></td>
          </tr>
		
		<c:set var="_area" value = "user" scope="request"/>	
		<c:set var="_metaList" value = "${user.userMeta}" scope="request" />		
        <c:import url="/WEB-INF/jsp/meta_ro.jsp"/>
          
	  <sec:authorize access="hasRole('god') and ! hasRole('u-${user.userId}')">      		   		
          <tr><td colspan="2" align=left><a href="/wasp/user/detail_rw/${user.userId}.do"><fmt:message key="userDetail.edit_as_other.label" /></a>
          </sec:authorize>	

	  <sec:authorize access="hasRole('u-${user.userId}')">      		   		
          <tr><td colspan="2" align=left><a href="/wasp/user/me_rw.do"><fmt:message key="userDetail.edit.label" /></a>
          &nbsp;&nbsp;<a href="<c:url value="/user/mypassword.do"/>"><fmt:message key="userDetail.change_password.label" /></a></td></tr>
          </sec:authorize>	

         <tr><td colspan=2 align=left></br><b><fmt:message key="userDetail.lab_users.label" />:</b></td></tr>
         <c:forEach items="${user.labUser}" var="ul">
    	  <tr>
            <td><a href="/wasp/lab/detail/<c:out value="${ul.lab.labId}" />.do"><c:out value="${ul.lab.name}" /></a></td>
            <td><span><c:out value="${ul.role.name}" /></span></td>
          </tr>
        </c:forEach>
          
		 <tr><td colspan=2 align=left></br><b><fmt:message key="userDetail.samples.label" />:</b></td></tr>
         <c:forEach items="${user.sample}" var="sample">
    	  <tr>
            <td><a href="/wasp/sample/detail/<c:out value="${sample.sampleId}" />.do"><c:out value="${sample.name}" /></a></td>           
          </tr>
        </c:forEach>
		
         <tr><td colspan=2 align=left></br><b><fmt:message key="userDetail.jobs.label" />:</b></td></tr>
         <c:forEach items="${user.job}" var="job">
    	  <tr>
            <td><a href="/wasp/job/detail/<c:out value="${job.jobId}" />.do"><c:out value="${job.name}" /></a></td>           
          </tr>
        </c:forEach>  

     </table>
  
