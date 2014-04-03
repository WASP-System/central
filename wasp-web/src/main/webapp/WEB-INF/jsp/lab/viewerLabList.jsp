<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br />
<h1><fmt:message key="pageTitle.lab/viewerLabList.label"/></h1>
<ul class="navTabs">
	<c:forEach items="${labUserList}" var="lu">
		<br />
		<div style="font-size:16px;"><b><fmt:message key="menu.labPI.label" />: <c:out value="${lu.lab.user.getNameLstCmFst()}" /> </b></div>
		<li>
			<fmt:message key="menu.myRoleInLab.label" />: <c:out value="${lu.getRole().getName()}" />
		</li>
		<sec:authorize access="hasRole('lu-${lu.lab.labId}' )">
			<li>
				<a href='<wasp:relativeUrl value="/lab/detail_ro/${lu.lab.departmentId}/${lu.lab.labId}.do"/>'><fmt:message key="dashboard.labDetails.label" /></a>
			</li>						
			<li>
				<a href='<wasp:relativeUrl value="/lab/user_manager/${lu.lab.labId}.do"/>'><fmt:message key="dashboard.userManager.label" /></a>
			</li>							
		</sec:authorize>
	</c:forEach>
	<c:if test="${isExistingUserPIPending==true}">
		<br />
		<div style="font-size:16px;"><b><fmt:message key="menu.labPI.label" />: <c:out value="${user.getNameLstCmFst()}" /> <span style="color:red">(<fmt:message key="menu.pendingApproval.label" />)</span></b></div>
		<li>
			<fmt:message key="menu.myRoleInLab.label" />: <fmt:message key="menu.labPI.label" /> 
		</li>
	</c:if>
</ul>