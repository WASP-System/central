<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<!-- top -->
<sec:authorize access="isAuthenticated()">

<a href="/wasp/dashboard.do">Wasp</a> |
<a href="<spring:url value="/j_spring_security_logout" htmlEscape="true" />">Logout</a>
</sec:authorize>

<!-- /top -->
