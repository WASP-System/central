<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%-- template for pages that do NOT have a JQGrid table --%>

<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html lang="en">
<head>
  <script type="text/javascript" src="http://code.jquery.com/jquery-1.9.1.js"></script>
  <meta charset="utf-8" />
  <script type="text/javascript">
	  $( document ).ready( function(){
			waspOnLoad();
	  });
	  
	  var waspOnLoad=function() {
	      // re-define the waspOnLoad var 
	      // in head-js if you need custom body 
	      // onLoad function
	      
	   }
  </script>
  <tiles:insertAttribute name="head-js" />
  <tiles:insertAttribute name="head-style" />
</head>
<body>
<tiles:insertAttribute name="body-content" />
</body>
</html>



