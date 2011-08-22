<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<!-- top -->
<sec:authorize access="isAuthenticated()">

<a href="/wasp/dashboard.do">Wasp</a> |
<a href="<spring:url value="/j_spring_security_logout" htmlEscape="true" />">Logout</a>
</sec:authorize>

<!-- /top -->
