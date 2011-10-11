<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-1.6.2.js"></script>
<link rel="stylesheet" type="text/css" href="/wasp/css/jquery/jquery-ui.css"/>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.16.custom.min.js"></script> 

 <script type="text/javascript">
     $(document).ready(function() {            
	       $("#userId").keyup(function(){getAuthNames();});
     });
      
     function getAuthNames(){        
     	if( $("#userId").val().length == 1){
	        	$.getJSON("/wasp/autocomplete/getUserNamesAndLoginForDisplay.do", { adminNameFragment: $("#userId").val() }, function(data) { $("input#userId").autocomplete(data);} );
     	}
	 }
 </script>
 <script language="JavaScript">
		<!--
		function validate(){
			var error = false;
			var message = '<fmt:message key="sysrole.list_missingparam.error" />';
			if(document.f.adminName.value == ""){
				error = true;
				document.f.userId.focus();
			}
			if(error){ alert(message); return false; }
			return true;
		}
		//-->
	</script> 