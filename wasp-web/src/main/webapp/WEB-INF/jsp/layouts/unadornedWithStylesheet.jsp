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
 <%--   
   if these pages are displayed in a div of a wasp page, the next 7 link and script lines are not needed, 
   but if displayed in an iframe, they are needed 
--%>
  <link rel="stylesheet" type="text/css" media="screen" href="<c:url value='css/reset.css' />" />
  <link rel="stylesheet" type="text/css" href="<c:url value='css/jquery/jquery-ui.css' />"/>
  <link rel="stylesheet" type="text/css" media="screen" href="<c:url value='css/base.css' />" />
  <link rel="stylesheet" type="text/css" href="<c:url value='css/tree-interactive.css' />" />
  <link rel="stylesheet" type="text/css" media="screen" href="<c:url value='css/menu.css' />" />
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
  <tiles:insertAttribute name="head-style" />
</head>
<body style="background-image:url(''); background-color: #FFFFE6; "><%--most likely, these style changes are NO LONGER NEEDED --%>
<div>
<%-- <wasp:errorMessage /> --%>
<tiles:insertAttribute name="body-content" />
</div>
</body>
</html>



