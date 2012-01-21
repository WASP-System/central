<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
    <font color="blue"><wasp:message /></font>

    <h1><fmt:message key="pageTitle.user/detail_ro.label" /></h1>

    <div class="instructions">
       <%@ include file="/WEB-INF/jsp/lorem.jsp" %>
    </div>

     <table class="data">
     	  <tr>
              <td class="label"><fmt:message key="wasp.authentication.label" /> <fmt:message key="user.login.label" /></td>
              <td class="value">${user.login}</td>            
          </tr>          	
          <tr>
              <td class="label"><fmt:message key="user.firstName.label" /></td>
              <td class="value">${user.firstName}</td>              
          </tr>
          <tr>
              <td class="label"><fmt:message key="user.lastName.label"/></td>
              <td class="value">${user.lastName}</td>
          </tr>
          <tr>
              <td class="label"><fmt:message key="user.email.label"/></td>
              <td class="value">${user.email}</td>
          </tr>         
          <tr>
              <td class="label"><fmt:message key="user.locale.label"/></td>
              <td class="value">                              
              <c:forEach var="localeEntry" items="${locales}">
                <c:set var="localeValue" value="${localeEntry.key}"/>
                <c:set var="localeLabel" value="${localeEntry.value}"/>               
                <c:if test="${user.locale == localeValue}">${localeLabel}</c:if>
              </c:forEach>
  	      </td>
          </tr>
		
		<c:set var="_area" value = "user" scope="request"/>	
		<c:set var="_metaList" value = "${user.userMeta}" scope="request" />		
        <c:import url="/WEB-INF/jsp/meta_ro.jsp"/>
	  <sec:authorize access="hasRole('su') and ! hasRole('u-${user.userId}')">      		   		
          <tr><td class="action"><a href="/wasp/user/detail_rw/${user.userId}.do"><fmt:message key="userDetail.edit_as_other.label" /></a>
          </sec:authorize>	
	  <sec:authorize access="hasRole('u-${user.userId}')">
	  	  <tr><td colspan="2" align=left><a href="/wasp/user/me_rw.do"><fmt:message key="userDetail.edit.label" /></a>
		  <sec:authorize access="not hasRole('ldap')">      		   		
	          &nbsp;&nbsp;<a href="<c:url value="/user/mypassword.do"/>"><fmt:message key="userDetail.change_password.label" /></a>
	      </sec:authorize>	
	      </td></tr>
	   </sec:authorize>
	</table>

	<table class="data list">
         <tr><td colspan=2 align=left></br><b><fmt:message key="userDetail.lab_users.label" /></b></td></tr>
         <c:forEach items="${user.labUser}" var="ul">
    	  <tr>
            <td class="label"><a href="/wasp/lab/detail_ro/<c:out value="${ul.lab.departmentId}" />/<c:out value="${ul.lab.labId}" />.do"><c:out value="${ul.lab.name}" /></a></td>
            <td class="value"><span><c:out value="${ul.role.name}" /></span></td>
          </tr>
        </c:forEach>
	</table>
          
	<table class="data list">
	 <tr><td></br><b><fmt:message key="userDetail.samples.label" /></b></td></tr>
         <c:forEach items="${user.sample}" var="sample">
    	  <tr>
            <td class="value"><a href="/wasp/sample/detail/<c:out value="${sample.sampleId}" />.do"><c:out value="${sample.name}" /></a></td>           
          </tr>
        </c:forEach>
	</table>

	<table class="data list">
         <tr><td></br><b><fmt:message key="userDetail.jobs.label" /></b></td></tr>
         <c:forEach items="${user.job}" var="job">
    	  <tr>
            <td class="value"><a href="/wasp/job/detail/<c:out value="${job.jobId}" />.do"><c:out value="${job.name}" /></a></td>           
          </tr>
        </c:forEach>  
        </table>
  
    <div class="bottomtxt">
       <%@ include file="/WEB-INF/jsp/lorem.jsp" %>
    </div>
