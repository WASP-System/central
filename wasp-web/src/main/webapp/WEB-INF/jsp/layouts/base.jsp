<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%-- template for pages that do NOT have a JQGrid table --%>

<%@ page import="edu.yu.einstein.wasp.dao.impl.DBResourceBundle" %> 
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html lang="en">
<head>
  <meta charset="utf-8" />
  <title> 	 	
    <wasp:pageTitle/> 
  </title>
  <link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/reset.css" />
  <link rel="stylesheet" type="text/css" href="/wasp/css/jquery/jquery-ui.css"/>
  <link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/base.css" />
  <script type="text/javascript" src="/wasp/scripts/jquery/jquery-1.7.1.js"></script>
  <script type="text/javascript">
  	function waspFade(el, msg) {
		if (msg != null && msg != ""){
			$('#'+el).html(msg);
		}
		if ($('#'+el).html() == ''){
			$('#'+el).hide();
		} else {
			$('#'+el).show();
			setTimeout(function() {
				$('#'+el).fadeOut('slow',
					function() {
						// after fadeout do the following
						$('#'+el).html('');
					});
			},7500);
		}
	}
  
    var waspOnLoad=function() {
      // re-define the waspOnLoad var 
      // in head-js if you need custom body 
      // onLoad function
    }
  </script>
  <tiles:insertAttribute name="head-js" />
</head>

<body onload='waspFade("waspErrorMessage");waspFade("waspMessage");waspOnLoad()'>
	<div id="container">
  		<div id="header">
			<tiles:insertAttribute name="banner-content" />
		</div>
  		<div id="content">
  			<wasp:errorMessage />
  			<wasp:message />
			<tiles:insertAttribute name="body-content" />
		</div>
  		<div id="footer">
			<tiles:insertAttribute name="footer-content" />
		</div>
	</div>
</body>
</html>



