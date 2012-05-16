<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-1.7.1.js"></script>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.18.custom.min.js"></script>
<script>
	
	$(document).ready(function() {
		
	    $("#accordion").accordion({
			collapsible: true,
			autoHeight: false,
			navigation: true,
			active: false,
			header: 'h4'			
		});
	    $("#accordion2").accordion({
			collapsible: true,
			autoHeight: false,
			navigation: true,
			active: false,
			header: 'h4'			
		});
	 	//this is not being used
	 	$('button[id^="robbutton"]').click(function() {
		  var id = parseInt(this.id.replace("robbutton", ""));
	  	  $("#robdiv" + id).fadeToggle("fast", "linear");
	  	  if( $(this).html() == "show"){ $(this).html("hide"); }
		  else{ $(this).html("show"); }	  	  
	  	});
	 	
	}); 
</script>
