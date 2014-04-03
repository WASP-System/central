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
  <link rel="stylesheet" type="text/css" media="screen" href="<c:url value='css/reset.css' />" />
  <link rel="stylesheet" type="text/css" href="<c:url value='css/jquery/jquery-ui.css' />"/>
  <link rel="stylesheet" type="text/css" media="screen" href="<c:url value='css/base.css' />" />
  <link rel="stylesheet" type="text/css" href="<c:url value='css/tree-interactive.css' />" />
  <link rel="stylesheet" type="text/css" media="screen" href="<c:url value='css/menu.css' />" />
  <script type="text/javascript" src="http://code.jquery.com/jquery-1.9.1.js"></script>
  <script type="text/javascript" src="http://code.jquery.com/ui/1.10.0/jquery-ui.js"></script> 
  
  <script type="text/javascript">
  
  function openWaitDialog(){
	  $("#wait_dialog-modal").dialog("open");
  }
  
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
  		$( "#wait_dialog-modal" ).dialog({
  			dialogClass: "no-close",
			height: 170,
			autoOpen: false,
			modal: true
		});
  		$("#wait_dialog-modal").css("visibility", "visible");
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

<body >
	<div id="container">
  		<div id="header">
			<tiles:insertAttribute name="banner-content" />
		</div>
		<sec:authorize access="isAuthenticated()">
			<div id="menu">
				<tiles:insertAttribute name="menu-content" />
			</div>
		</sec:authorize>
  		<div id="content"> 
  			<!-- <wasp:breadcrumbs /> -->
  			<div id="wait_dialog-modal" title="<fmt:message key="wasp.wait_title.label" />"  >
				<table border="0" cellpadding="5">
				<tr>
				<td><img src="<c:url value='images/spinner.gif' />" align="left" border="0" ></td>
				<td><fmt:message key="wasp.wait_message.label" /></td>
				</tr>
				</table>
			</div>
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



