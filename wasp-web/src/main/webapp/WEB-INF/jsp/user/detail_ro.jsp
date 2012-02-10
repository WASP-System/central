<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
    <wasp:message />

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

  
    <div class="bottomtxt">
       <%@ include file="/WEB-INF/jsp/lorem.jsp" %>
    </div>
