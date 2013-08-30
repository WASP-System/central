<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%-- template for pages that do NOT have a JQGrid table --%>

<%@ page import="edu.yu.einstein.wasp.resourcebundle.DBResourceBundle" %> 
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
  <meta charset="utf-8" />
  <title> 	 	
    <wasp:pageTitle/> 
  </title>
  <link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/reset.css" />
  <link rel="stylesheet" type="text/css" href="/wasp/css/jquery/jquery-ui.css"/>
  <link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/base.css" />
  <link rel="stylesheet" type="text/css" href="/wasp/css/tree-interactive.css" />
  <link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/menu.css" />
  <script type="text/javascript" src="https://github.com/rgrove/lazyload/raw/master/lazyload.js"></script>
  <script type="text/javascript" src="http://code.jquery.com/jquery-1.9.1.js"></script>
  <script type="text/javascript" src="http://code.jquery.com/ui/1.10.0/jquery-ui.js"></script> 
  
  <script type="text/javascript">
  
  function submitViaAjaxAndOpenReceivedPageHtml(frm){
	    $("#wait_dialog-modal").dialog("open");
  		$.ajax({
			type: frm.attr('method'),
	        url: frm.attr('action'),
	        data: frm.serialize(),
	        success: function (data, textStatus, jqXHR) {
	        	// data represents the entire html from returned page. We just need to replace the head and body sections of the current page.
	        	// strip loaded scripts from head and use LazyLoad to load them. This means we can take advantage of the callback on script loading
	        	// to call readyFn(). Left as was, the scripts load but readyFn() may execute before this is finished and try and access not-yet available
	        	// methods. Sadly document-ready code ONLY works when the DOM is created, not when modified by ajax so this is our best solution.
	        	var newHeadCode = data.replace(/^[\s\S]*[\s>;]<head>([\s\S]*)<\/head>[\s\S]*$/, "$1");
	        	var regEx = /^[\s\S]*<script[\s\S]*src=["']([\s\S]*)["'][\s\S]*><\/script>[\s\S]*$/g;
	        	var scripts = [];
	        	var match;
	        	while (match = regEx.exec(newHeadCode)){
	        		scripts.push(match[0]);
	        	}
	        	$('head').html(newHeadCode.replace(regEx, ""));
	        	$('body').html(data.replace(/^[\s\S]*<body>([\s\S]*)<\/body>[\s\S]*$/, "$1"));
	        	LazyLoad.js(scripts, function(){
	        		readyFn(); // run document ready code when all dependencies loaded
	        		$("#wait_dialog-modal").dialog("close");
	        	});
	     	}
	    });
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
  
  	function readyFn(){
  		waspTooltip();
  		waspFade("waspErrorMessage");
  		waspFade("waspMessage");
  		
  		$( "#wait_dialog-modal" ).dialog({
  			dialogClass: "no-close",
			height: 170,
			autoOpen: false,
			modal: true
		});
  		
  		waspOnLoad();
  	}

	$( document ).ready( readyFn );
  
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

<body>
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
				<td><img src="/wasp/images/spinner.gif" align="left" border="0" ></td>
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



