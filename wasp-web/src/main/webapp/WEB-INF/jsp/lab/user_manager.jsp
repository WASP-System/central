<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

    <p><wasp:message /></p>
    <h1><fmt:message key="pageTitle.lab/user_manager.label"/></h1>
	<h2><fmt:message key="labuser.current.label"/></h2>
	<table class="EditTable ui-widget ui-widget-content">
		<tr class="FormData">
		  	<td class="label-centered">Name (Login Name)</td>
		  	<td class="label-centered">Name</td>
		  	<td class="label-centered">Email</td>
		  	<td class="label-centered">Status</td>
		  	<td class="label-centered">Role</td>
		  	<td class="label-centered">Actions</td>
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
	      <td class="DataTD"><c:out value="${ul.role.name}" /></td>
		  <td class="submit value">
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
	</table><br />
    <p>
    <a href="/wasp/lab/detail_ro/<c:out value="${lab.departmentId}"/>/<c:out value="${lab.labId}"/>.do"><fmt:message key="lab.detail.label" /></a> 
	</p>
	<!--
    <h2><fmt:message key="labuser.request.label"/></h2>
    <c:if test="${empty labuserpending}">
    	<p><fmt:message key="userPending.no_pending_users.label"/></p>
    </c:if>  
    <c:forEach items="${userpending}" var="up">
      <p>
      <c:out value="${up.firstName}" />
      <c:out value="${up.lastName}" />
      <c:out value="${up.email}" />
        <a href="<c:url value="/lab/userpending/approve/${lab.labId}/${up.userPendingId}.do"/>"><fmt:message key="userPending.action_approve.label"/></a>
        <a href="<c:url value="/lab/userpending/reject/${lab.labId}/${up.userPendingId}.do"/>"><fmt:message key="userPending.action_reject.label"/></a>
      </p>
    </c:forEach>
    <c:forEach items="${labuserpending}" var="up">
      <p>
      <c:out value="${up.user.firstName}" />
      <c:out value="${up.user.lastName}" />
      <c:out value="${up.user.email}" />
        <a href="<c:url value="/lab/labuserpending/approve/${lab.labId}/${up.labUserId}.do"/>"><fmt:message key="userPending.action_approve.label"/></a>
        <a href="<c:url value="/lab/labuserpending/reject/${lab.labId}/${up.labUserId}.do"/>"><fmt:message key="userPending.action_reject.label"/></a>
      </p>
    </c:forEach>
    -->
