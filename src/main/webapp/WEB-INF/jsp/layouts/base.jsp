<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="edu.yu.einstein.wasp.dao.impl.DBResourceBundle" %> 
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<html>
<head>
 <title> 	 	
     <wasp:pageTitle/> 
 </title>
<link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/base.css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<tiles:insertAttribute name="head-js" />
<script>
var waspOnLoad=function() {//re-define the waspOnLoad var in head-js if you need custom body onLoad function
}
</script>

</head>
 <body onload='waspOnLoad();'>
 <!-- top -->
<sec:authorize access="isAuthenticated()">

<a href="/wasp/dashboard.do">Wasp</a> |
<a href="<spring:url value="/j_spring_security_logout" htmlEscape="true" />">Logout</a>
</sec:authorize>

<!-- /top -->
 
<tiles:insertAttribute name="body-content" />
</body>
</html>

