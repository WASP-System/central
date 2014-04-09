<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
    

    <h1><fmt:message key="pageTitle.user/detail_ro.label" /></h1>


     <table class="EditTable ui-widget ui-widget-content">
       <sec:authorize access="hasRole('su') or hasRole('fm') or hasRole('da-*') or hasRole('u-${user.userId}')">
     	  <tr class="FormData">
              <td class="CaptionTD">
              <c:if test="${isAuthenticationExternal == true}">
	          	<fmt:message key="wasp.authentication_external.label" /> 
	          </c:if>
	          <c:if test="${isAuthenticationExternal == false}">
	          	<fmt:message key="wasp.authentication_internal.label" /> 
	          </c:if> 
              <fmt:message key="user.login.label" />:</td>
              <td class="DataTD">${user.login}</td>            
          </tr> 
        </sec:authorize>         	
          <tr class="FormData">
              <td class="CaptionTD"><fmt:message key="user.firstName.label" />:</td>
              <td class="DataTD">${user.firstName}</td>              
          </tr>
          <tr class="FormData">
              <td class="CaptionTD"><fmt:message key="user.lastName.label"/>:</td>
              <td class="DataTD">${user.lastName}</td>
          </tr>
          <tr class="FormData">
              <td class="CaptionTD"><fmt:message key="user.email.label"/>:</td>
              <td class="DataTD">${user.email}</td>
          </tr>         
          <tr class="FormData">
              <td class="CaptionTD"><fmt:message key="user.locale.label"/>:</td>
              <td class="DataTD">                              
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
          <tr class="FormData"><td class="submitBottom"><div class="submit"><a href="<wasp:relativeUrl value='user/detail_rw/${user.userId}.do' />"><fmt:message key="userDetail.edit_as_other.label" /></a></div>
          </sec:authorize>	
	  <sec:authorize access="hasRole('u-${user.userId}')">
	  	  <tr class="FormData"><td colspan="2" align=left class="submitBottom submit"><a href="<wasp:relativeUrl value='user/me_rw.do' />"><fmt:message key="userDetail.edit.label" /></a>
		  <sec:authorize access="not hasRole('ldap')">      		   		
	          &nbsp;&nbsp;<a href="<wasp:relativeUrl value="user/mypassword.do"/>"><fmt:message key="userDetail.change_password.label" /></a>
	      </sec:authorize>	
	     </td></tr>
	   </sec:authorize>
	</table>

  
 