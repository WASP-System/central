<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%-- template for pages that do NOT have a JQGrid table --%>

<%@ page import="edu.yu.einstein.wasp.resourcebundle.DBResourceBundle" %> 
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html lang="en">
<head>
  <meta charset="utf-8" />
  <tiles:insertAttribute name="head-js" />
  <tiles:insertAttribute name="head-style" />
</head>
<body>
<tiles:insertAttribute name="body-content" />
</body>
</html>



