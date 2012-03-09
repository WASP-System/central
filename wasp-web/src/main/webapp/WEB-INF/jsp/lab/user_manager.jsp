<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

    <p></p>
    <h1><fmt:message key="pageTitle.lab/user_manager.label"/></h1>
	<h2><fmt:message key="labuser.current.label"/></h2>
	<table class="EditTable ui-widget ui-widget-content">
		<tr class="FormData">
		  	<td class="CaptionTD top-heading">Name (Login Name)</td>
		  	<td class="CaptionTD top-heading">Name</td>
		  	<td class="CaptionTD top-heading">Email</td>
		  	<td class="CaptionTD top-heading">Status</td>
		  	<td class="CaptionTD top-heading">Role</td>
		  	<td class="CaptionTD top-heading">Actions</td>
		</tr>
	    <c:forEach items="${labuser}" var="ul">
	      <tr class="FormData">
	      <td class="DataTD"><a href="/wasp/user/detail_ro/<c:out value="${ul.user.userId}" />.do"><c:out value="${ul.user.login}" /></a></td>
	      <td class="DataTD">
	        <c:out value="${ul.user.firstName}" />
	        <c:out value="${ul.user.lastName}" />
	      </td>
	      <td class="DataTD"><c:out value="${ul.user.email}" /></td>
	      <td class="DataTD">
	        <c:if test="${ul.user.isActive == 1}" > <fmt:message key="labuser.active.label"/> 
	        </c:if>
	        <c:if test="${ul.user.isActive == 0}" > <fmt:message key="labuser.inactive.label"/>
	        </c:if>
	       </td>
	      <td class="DataTD" ><c:out value="${ul.role.name}" /></td>
		  <td class="submit value" nowrap="nowrap">
		      <c:if test="${ul.role.roleName == 'lx'}">
		        <a href="<c:url value="/lab/user/role/${lab.labId}/${ul.user.userId}/lu.do"/>"><fmt:message key="labuser.status_activate.label"/></a>
		      </c:if>
		      <c:if test="${ul.role.roleName == 'lu'}">
		        <a href="<c:url value="/lab/user/role/${lab.labId}/${ul.user.userId}/lm.do"/>"><fmt:message key="labuser.status_promoteLM.label"/></a>
		        <a href="<c:url value="/lab/user/role/${lab.labId}/${ul.user.userId}/lx.do"/>"><fmt:message key="labuser.status_deactivate.label"/></a>
		      </c:if>
		      <c:if test="${ul.role.roleName == 'lm'}">
		        <a href="<c:url value="/lab/user/role/${lab.labId}/${ul.user.userId}/lu.do"/>"><fmt:message key="labuser.status_demoteLU.label"/></a>
		      </c:if>
		<%--       <c:if test="${ul.role.roleName == 'lp'}"> --%>
		<%--         <a href="<c:url value="/lab/user/role/${lab.labId}/${ul.user.userId}/lu.do"/>"><fmt:message key="userPending.action_approve.label"/></a> --%>
		<%--         <a href="<c:url value="/lab/user/role/${lab.labId}/${ul.user.userId}/xx.do"/>"><fmt:message key="userPending.action_reject.label"/></a> --%>
		<%--       </c:if> --%>
			&nbsp;</td>
	      </tr>
	    </c:forEach>
	</table>

