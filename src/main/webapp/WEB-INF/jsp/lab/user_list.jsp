<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<h1><fmt:message key="pageTitle.lab/user_list.label" /> (${lab.name})</h1>
<h2><fmt:message key="lab.list_pi.label" /></h2>
<p>
	<c:out value="${pi.firstName}" />
    <c:out value="${pi.lastName}" />
    <c:out value="${pi.email}" />
</p>
<h2><fmt:message key="lab.list_users.label" /></h2>
<c:forEach items="${labManagers}" var="manager" >
	<p>
	<c:out value="${manager.firstName}" />
    <c:out value="${manager.lastName}" />
    <c:out value="${manager.email}" />
    <c:out value="(lab manager)" />
	</p>
</c:forEach>
<c:forEach items="${labMembers}" var="member" >
	<p>
	<c:out value="${member.firstName}" />
    <c:out value="${member.lastName}" />
    <c:out value="${member.email}" />
	</p>
</c:forEach>