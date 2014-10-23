<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%-- template for pages that do NOT have a JQGrid table --%>

<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html lang="en">
<head>
  <meta charset="utf-8" />
  <title> 	 	
    <wasp:pageTitle/> 
  </title>
 
  <link rel="stylesheet" type="text/css" media="screen" href="<wasp:relativeUrl value='css/reset.css' />" />
  <link rel="stylesheet" type="text/css" media="screen" href="<wasp:relativeUrl value='css/base.css' />" />
  <link rel="stylesheet" type="text/css" href="<wasp:relativeUrl value='css/jquery/jquery-ui.css' />"/>
  
  <script type="text/javascript">
  
    var waspOnLoad=function() {
      // re-define the waspOnLoad var 
      // in head-js if you need custom body 
      // onLoad function 
      
    }
  </script>
  <tiles:insertAttribute name="head-js" />
  <tiles:insertAttribute name="head-style" />
</head>
<body class="ui-widget-content unadorned" >
<div>
<%-- <wasp:errorMessage /> --%>
<tiles:insertAttribute name="body-content" />
</div>
</body>
</html>



