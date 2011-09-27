<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="edu.yu.einstein.wasp.dao.impl.DBResourceBundle" %> 
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<html>
<head>
 <title> 
 	<%=DBResourceBundle.getPageTitle(request)%>
 </title>
<link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/base.css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script>
var waspOnLoad=function() {//re-define the var in head-js if you need custom body onLoad function
}
</script>
<tiles:insertAttribute name="head-js" />
</head>
 <body onload='waspOnLoad();'>
<tiles:insertAttribute name="primary-content" />
</body>
</html>

