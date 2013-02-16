<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%-- template for pages that do NOT have a JQGrid table --%>

<%@ page import="edu.yu.einstein.wasp.resourcebundle.DBResourceBundle" %> 
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
  <link rel="stylesheet" type="text/css" href="/wasp/css/tree-interactive.css" />
  <script type="text/javascript" src="http://code.jquery.com/jquery-1.9.1.js"></script>
  <script type="text/javascript" src="http://code.jquery.com/ui/1.10.0/jquery-ui.js"></script> 
  
  <script type="text/javascript">
  
  function waspTooltip(){
		$( ".tooltip" ).tooltip({
	  	      position: {
	  	        my: "center bottom-20",
	  	        at: "center top",
	  	        using: function( position, feedback ) {
	  	          $( this ).css( position );
	  	          $( "<div>" )
	  	            .addClass( "arrow" )
	  	            .addClass( feedback.vertical )
	  	            .addClass( feedback.horizontal )
	  	            .appendTo( this );
	  	        }
	  	      }
	  	    });
	}

	$( document ).ready( function(){
		waspTooltip();
  		waspFade("waspErrorMessage");
  		waspFade("waspMessage");
  		waspOnLoad();
  	});
  
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

<body >
	<div id="container">
  		<div id="header">
			<tiles:insertAttribute name="banner-content" />
		</div>
  		<div id="content">
  			<wasp:breadcrumbs />
  			<tiles:insertAttribute name="menu-content" /> 
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



